"""
Data models for recommendation API requests and responses
"""
from pydantic import BaseModel, Field
from typing import List, Optional


class MovieInput(BaseModel):
    """Movie data coming from Contentstack"""
    title: str
    year: Optional[int] = None
    runtime: Optional[int] = None
    rating: Optional[float] = None
    genre: Optional[List[str]] = []
    mood_tags: Optional[List[str]] = []
    platforms: Optional[List[str]] = []
    description: Optional[str] = ""
    ai_description: Optional[str] = ""
    image_url: Optional[str] = None


class RecommendationRequest(BaseModel):
    """Request model for getting recommendations"""
    mood: str = Field(
        ..., 
        description="User's current mood",
        json_schema_extra={"examples": ["cozy", "thrilling", "laugh", "deep", "escape", "chill"]}
    )
    time_available: int = Field(
        ..., 
        description="Available time in minutes", 
        gt=0,
        le=500  # Max 500 minutes (8+ hours is unrealistic)
    )
    movies: List[MovieInput] = Field(
        ...,
        description="All movies from Contentstack to choose from"
    )
    top_n: int = Field(
        5,
        description="Number of recommendations to return",
        ge=1,  # At least 1
        le=10  # Max 10
    )
    user_id: Optional[str] = Field(None, description="Optional user ID for personalization")


class MovieResponse(BaseModel):
    """Movie data returned in response"""
    title: str
    year: int
    runtime: int
    rating: float
    genre: List[str]
    mood_tags: List[str]
    platforms: List[str]
    description: str
    ai_description: str
    similarity_score: Optional[float] = None  # How similar to user preferences
    image_url: Optional[str] = None  # Image URL from Contentstack


class SingleRecommendation(BaseModel):
    """Single movie recommendation with score and reason"""
    movie: MovieResponse
    match_score: float = Field(..., description="Overall match score (0-100)")
    reason: str = Field(..., description="Why this movie was recommended")


class RecommendationResponse(BaseModel):
    """Complete recommendation response with multiple options"""
    recommendations: List[SingleRecommendation] = Field(
        ..., 
        description="List of recommended movies, ranked by match score"
    )
    total_candidates: int = Field(..., description="Total number of movies considered")
    filters_applied: dict = Field(..., description="Filters that were applied")