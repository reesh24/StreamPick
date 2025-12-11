"""
Content-Based Filtering Recommendation Model

This uses TF-IDF (Term Frequency-Inverse Document Frequency) to convert movie 
features into vectors, then calculates cosine similarity to find similar movies.
"""
import pandas as pd
import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from typing import List, Tuple
import logging

logger = logging.getLogger(__name__)


class ContentBasedRecommender:
    """
    Content-based filtering using TF-IDF and cosine similarity
    
    How it works:
    1. Combine movie features (genre, mood, description) into text
    2. Convert text to TF-IDF vectors (numbers representing words)
    3. Calculate cosine similarity between all movies
    4. Recommend movies with highest similarity scores
    """
    
    def __init__(self):
        self.tfidf_vectorizer = TfidfVectorizer(
            max_features=500,  # Use top 500 most important words
            stop_words='english',  # Ignore common words like "the", "is"
            ngram_range=(1, 2)  # Use single words and two-word phrases
        )
        self.tfidf_matrix = None
        self.movies_df = None
        self.similarity_matrix = None
    
    def fit(self, movies_df: pd.DataFrame):
        """
        Train the model on movie data
        
        Args:
            movies_df: DataFrame with movie information
        """
        logger.info("Training content-based recommender...")
        
        self.movies_df = movies_df.copy()
        
        # Create combined text features for each movie
        self.movies_df['combined_features'] = self._create_combined_features(movies_df)
        
        # Convert text to TF-IDF vectors
        self.tfidf_matrix = self.tfidf_vectorizer.fit_transform(
            self.movies_df['combined_features']
        )
        
        # Calculate similarity between all movies
        self.similarity_matrix = cosine_similarity(self.tfidf_matrix)
        
        logger.info(f"Model trained on {len(movies_df)} movies")
        logger.info(f"TF-IDF matrix shape: {self.tfidf_matrix.shape}")
    
    def _create_combined_features(self, df: pd.DataFrame) -> pd.Series:
        """
        Combine multiple movie features into a single text string
        
        For example: "Comedy Drama cozy laugh heartwarming funny feel-good story"
        """
        combined = []
        
        for idx, row in df.iterrows():
            features = []
            
            # Add genres (weighted more - repeat 3 times)
            if isinstance(row['Genre'], list):
                features.extend(row['Genre'] * 3)
            
            # Add mood tags (weighted more - repeat 3 times)
            if isinstance(row['Mood Tags'], list):
                features.extend(row['Mood Tags'] * 3)
            
            # Add description words
            if pd.notna(row['Description']):
                features.append(row['Description'])
            
            # Add AI description
            if pd.notna(row['AI Description']):
                features.append(row['AI Description'])
            
            # Combine all features into one string
            combined.append(' '.join(str(f) for f in features))
        
        return pd.Series(combined)
    
    def get_recommendations(
        self, 
        candidates_df: pd.DataFrame,
        user_mood: str,
        time_available: int,
        top_n: int = 5
    ) -> List[Tuple[int, float]]:
        """
        Get movie recommendations based on user preferences
        
        Args:
            candidates_df: Pre-filtered movies (by mood/time)
            user_mood: User's current mood
            time_available: Available time in minutes
            top_n: Number of recommendations to return
            
        Returns:
            List of (movie_index, similarity_score) tuples
        """
        if self.similarity_matrix is None:
            raise ValueError("Model not trained. Call fit() first.")
        
        # Create a "virtual user preference" based on mood and time
        user_profile = self._create_user_profile(user_mood, time_available)
        
        # Calculate how similar each candidate movie is to user preferences
        recommendations = []
        
        for idx, movie in candidates_df.iterrows():
            # Get movie index in original dataset
            movie_title = movie['Title']
            movie_idx = self.movies_df[self.movies_df['Title'] == movie_title].index[0]
            
            # Calculate similarity score
            score = self._calculate_movie_score(
                movie_idx, 
                movie, 
                user_mood, 
                time_available
            )
            
            recommendations.append((movie_idx, score))
        
        # Sort by score (highest first) and return top N
        recommendations.sort(key=lambda x: x[1], reverse=True)
        
        return recommendations[:top_n]
    
    def _create_user_profile(self, mood: str, time_available: int) -> str:
        """Create a text representation of user preferences"""
        # Emphasize the mood by repeating it
        profile = f"{mood} {mood} {mood}"
        
        # Add time preference context
        if time_available < 100:
            profile += " short quick"
        elif time_available > 150:
            profile += " epic long immersive"
        
        return profile
    
    def _calculate_movie_score(
        self,
        movie_idx: int,
        movie: pd.Series,
        user_mood: str,
        time_available: int
    ) -> float:
        """
        Calculate overall score for a movie
        
        Score breakdown:
        - Mood match: 0-40 points (MOST IMPORTANT)
        - Content similarity: 0-30 points
        - Quality: 0-20 points  
        - Runtime fit: 0-10 points (bonus/penalty)
        """
        score = 0.0
        
        # 1. MOOD MATCH - Most important! (0-40 points)
        if isinstance(movie['Mood Tags'], list):
            if user_mood.lower() in [tag.lower() for tag in movie['Mood Tags']]:
                score += 40  # Strong bonus for mood match
        
        # 2. Content similarity (0-30 points)
        similarities = self.similarity_matrix[movie_idx]
        top_similarities = np.sort(similarities)[-10:]  # Top 10 similar
        content_score = np.mean(top_similarities) * 30  # Scale to 0-30
        score += content_score
        
        # 3. Quality bonus (0-20 points)
        rating = movie['Rating']
        if pd.notna(rating):
            score += (rating / 10.0) * 20
        
        # 4. Runtime fit (0-10 points, more forgiving)
        runtime = movie['Runtime']
        if pd.notna(runtime):
            time_diff = abs(runtime - time_available)
            if time_diff <= 20:
                score += 10  # Perfect fit
            elif time_diff <= 40:
                score += 7   # Good fit
            elif time_diff <= 60:
                score += 4   # Acceptable
            elif runtime > time_available + 60:
                score -= 5   # Small penalty only if WAY too long
        
        return score
    
    def get_movie_by_index(self, idx: int) -> pd.Series:
        """Get movie data by index"""
        return self.movies_df.iloc[idx]

