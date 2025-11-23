"""
Main recommendation engine that orchestrates the ML models
"""
from app.services.mood_mapper import MoodMapper
from app.models.content_based import ContentBasedRecommender
from app.schemas.recommendation import (
    RecommendationRequest,
    RecommendationResponse,
    SingleRecommendation,
    MovieResponse
)
import pandas as pd
import logging

logger = logging.getLogger(__name__)


class RecommendationEngine:
    """
    Main engine that coordinates data loading and recommendations
    """
    
    def __init__(self):
        self.content_based_model = ContentBasedRecommender()
    
    def initialize(self):
        """No initialization needed - we get data from each request"""
        logger.info("Recommendation engine ready!")
    
    def get_recommendation(
        self,
        request: RecommendationRequest
    ) -> RecommendationResponse:
        """
        Get personalized movie recommendations
        
        Args:
            request: User's mood, time preferences, and all movies from Contentstack
            
        Returns:
            Top N recommended movies with match scores and reasoning
        """
        # Convert incoming movies to DataFrame
        movies_data = []
        for movie in request.movies:
            movies_data.append({
                'Title': movie.title,
                'Year': movie.year or 2020,
                'Runtime': movie.runtime or 120,
                'Rating': movie.rating or 7.0,
                'Genre': movie.genre or [],
                'Mood Tags': movie.mood_tags or [],
                'Platforms': movie.platforms or [],
                'Description': movie.description or "",
                'AI Description': movie.ai_description or "",
                'image_url': movie.image_url
            })
        
        movies_df = pd.DataFrame(movies_data)
        logger.info(f"Received {len(movies_df)} movies from Contentstack")
        
        # Train the model on this fresh data
        self.content_based_model.fit(movies_df)
        
        top_n = request.top_n
        logger.info(f"Getting {top_n} recommendations for mood={request.mood}, time={request.time_available}")
        
        # Normalize mood using MoodMapper (handles UI labels like "Edge of Seat" -> "thrilling")
        try:
            normalized_mood = MoodMapper.normalize_mood(request.mood)
            logger.info(f"Mapped mood '{request.mood}' -> '{normalized_mood}'")
        except ValueError as e:
            # Mood not recognized
            raise ValueError(str(e))
        
        # Step 1: Filter candidates by mood
        candidates = movies_df[
            movies_df['Mood Tags'].apply(lambda tags: normalized_mood in [t.lower() for t in tags])
        ]
        total_candidates = len(candidates)
        
        if len(candidates) == 0:
            # Provide helpful error with UI-friendly mood labels
            available_moods = MoodMapper.get_ui_friendly_moods()
            raise ValueError(
                f"No movies found for mood: '{request.mood}'. "
                f"Try one of these: {', '.join(available_moods)}"
            )
        
        # Step 2: Filter by time - but prioritize having enough options
        time_filtered = candidates[
            candidates['Runtime'] <= request.time_available + 60
        ]
        
        time_constraint_relaxed = False
        
        # If we have fewer than top_n matches, use all candidates instead
        if len(time_filtered) < top_n:
            logger.warning(f"Only {len(time_filtered)} movies fit strict time constraint, using all {len(candidates)} candidates")
            time_constraint_relaxed = True
            # Keep all candidates to ensure we get enough recommendations
        else:
            # We have enough movies that fit the time constraint
            candidates = time_filtered
            logger.info(f"Using {len(candidates)} time-filtered candidates")
        
        logger.info(f"Found {len(candidates)} candidate movies")
        
        # Step 3: Use ML to score and rank candidates
        # Get more than requested in case we need alternatives
        num_to_get = min(top_n, len(candidates))
        logger.info(f"Requesting {num_to_get} recommendations (top_n={top_n}, candidates={len(candidates)})")
        
        ml_recommendations = self.content_based_model.get_recommendations(
            candidates_df=candidates,
            user_mood=normalized_mood,
            time_available=request.time_available,
            top_n=num_to_get
        )
        
        logger.info(f"ML returned {len(ml_recommendations)} recommendations")
        
        # Step 4: Build response for each recommendation
        recommendations_list = []
        
        for movie_idx, match_score in ml_recommendations:
            movie_data = self.content_based_model.get_movie_by_index(movie_idx)
            
            # Convert to response format
            movie_response = MovieResponse(
                title=movie_data['Title'],
                year=int(movie_data['Year']),
                runtime=int(movie_data['Runtime']),
                rating=float(movie_data['Rating']),
                genre=movie_data['Genre'],
                mood_tags=movie_data['Mood Tags'],
                platforms=movie_data['Platforms'],
                description=movie_data['Description'],
                ai_description=movie_data['AI Description'],
                similarity_score=match_score,
                image_url=movie_data.get('image_url')  # Include image URL from Contentstack
            )
            
            # Generate reasoning
            reason = self._generate_reason(
                movie_data, 
                normalized_mood, 
                request.time_available,
                match_score, 
                time_constraint_relaxed
            )
            
            single_rec = SingleRecommendation(
                movie=movie_response,
                match_score=round(match_score, 1),
                reason=reason
            )
            
            recommendations_list.append(single_rec)
            logger.info(f"  #{len(recommendations_list)}: {movie_data['Title']} - Score: {match_score:.1f}")
        
        # Build final response
        response = RecommendationResponse(
            recommendations=recommendations_list,
            total_candidates=total_candidates,
            filters_applied={
                "mood": normalized_mood,
                "time_available": request.time_available,
                "time_constraint_relaxed": time_constraint_relaxed
            }
        )
        
        logger.info(f"Returning {len(recommendations_list)} recommendations")
        
        return response
    
    
    def _generate_reason(self, movie, mood, time_available, score, time_constraint_relaxed=False):
        """Generate human-readable explanation for the recommendation"""
        reasons = []
        
        # Mood match
        if mood.lower() in [tag.lower() for tag in movie['Mood Tags']]:
            reasons.append(f"Perfect match for your '{mood}' mood")
        
        # High rating
        if movie['Rating'] >= 8.0:
            reasons.append(f"Highly rated ({movie['Rating']}/10)")
        
        # Runtime handling
        time_diff = abs(movie['Runtime'] - time_available)
        if time_diff <= 20:
            reasons.append(f"Runtime: {movie['Runtime']} mins")
        elif time_constraint_relaxed:
            reasons.append(f"Runtime: {movie['Runtime']} mins (longer than requested, but worth it!)")
        
        # Genre
        if len(movie['Genre']) > 0:
            genres = ', '.join(movie['Genre'][:2])
            reasons.append(f"Great {genres}")
        
        # Use AI description if available
        if movie['AI Description']:
            return f"{movie['AI Description']} {' • '.join(reasons)}"
        
        return ' • '.join(reasons)

