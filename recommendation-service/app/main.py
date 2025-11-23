"""
FastAPI server for movie recommendation service
"""
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from app.schemas.recommendation import RecommendationRequest, RecommendationResponse
from app.services.recommendation_engine import RecommendationEngine
import logging
import uvicorn

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

# Create FastAPI app
app = FastAPI(
    title="StreamPick Recommendation Service",
    description="ML-based movie recommendation engine using content-based filtering",
    version="1.0.0"
)

# Configure CORS (allow requests from Java backend and frontend)
app.add_middleware(
    CORSMiddleware,
    allow_origins=[
        "http://localhost:8080",  # Java backend
        "http://localhost:5173",  # React frontend
        "http://localhost:3000"   # Alternative React port
    ],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Initialize recommendation engine (will load on startup)
recommendation_engine = RecommendationEngine()


@app.on_event("startup")
async def startup_event():
    """Initialize the recommendation engine when server starts"""
    logger.info("Starting up recommendation service...")
    recommendation_engine.initialize()
    logger.info("✓ Recommendation service ready! Waiting for movie data from Java backend.")


@app.get("/")
async def root():
    """Welcome endpoint"""
    return {
        "service": "StreamPick Recommendation Service",
        "status": "running",
        "version": "1.0.0",
        "description": "ML-based movie recommendations using content-based filtering"
    }


@app.get("/health")
async def health_check():
    """Health check endpoint"""
    from app.services.mood_mapper import MoodMapper
    return {
        "status": "healthy",
        "message": "Service receives movie data from Java backend with each request",
        "available_moods": MoodMapper.get_backend_moods()
    }


@app.post("/recommend", response_model=RecommendationResponse)
async def get_recommendation(request: RecommendationRequest):
    """
    Get personalized movie recommendations
    
    Request body:
    - mood: User's current mood (e.g., "cozy", "thrilling", "laugh", "deep", "escape", "chill")
    - time_available: Available time in minutes (1-500)
    - top_n: Number of recommendations to return (default: 5, min: 1, max: 10)
    - user_id: Optional user ID
    
    Returns:
    - List of recommended movies with match scores and reasoning, ranked best to worst
    """
    import time
    start_time = time.time()
    
    try:
        # Sanitize inputs
        request.mood = request.mood.strip()
        
        logger.info(
            f"Recommendation request: mood='{request.mood}', "
            f"time={request.time_available}, top_n={request.top_n}, "
            f"movies_received={len(request.movies)}, "
            f"user_id={request.user_id or 'anonymous'}"
        )
        
        # Get recommendations from ML engine
        response = recommendation_engine.get_recommendation(request)
        
        elapsed_time = (time.time() - start_time) * 1000  # Convert to ms
        logger.info(f"Request completed in {elapsed_time:.2f}ms, returned {len(response.recommendations)} recommendations")
        
        return response
        
    except ValueError as e:
        logger.warning(f"Validation error: {e}")
        raise HTTPException(status_code=400, detail=str(e))
        
    except Exception as e:
        logger.error(f"Error generating recommendation: {e}", exc_info=True)
        raise HTTPException(
            status_code=500,
            detail=f"Failed to generate recommendation: {str(e)}"
        )


@app.get("/moods")
async def get_available_moods():
    """Get list of available mood options with UI-friendly labels"""
    from app.services.mood_mapper import MoodMapper
    
    return {
        "ui_labels": MoodMapper.get_ui_friendly_moods(),
        "backend_tags": MoodMapper.get_backend_moods(),
        "all_valid_inputs": MoodMapper.get_all_valid_inputs(),
        "examples": [
            {"label": "Cozy & Warm", "backend": "cozy", "description": "Heartwarming, feel-good movies"},
            {"label": "Edge of Seat", "backend": "thrilling", "description": "Suspenseful, intense thrillers"},
            {"label": "Need Laughs", "backend": "laugh", "description": "Comedies that will crack you up"},
            {"label": "Make Me Think", "backend": "deep", "description": "Thought-provoking, intellectual films"},
            {"label": "Pure Escapism", "backend": "escape", "description": "Adventures to transport you away"},
            {"label": "Background Vibe", "backend": "chill", "description": "Relaxing, mellow content"}
        ]
    }


# For running directly with python
if __name__ == "__main__":
    uvicorn.run(
        "app.main:app",
        host="0.0.0.0",
        port=8001,
        reload=True,  # Auto-reload on code changes
        log_level="info"
    )

