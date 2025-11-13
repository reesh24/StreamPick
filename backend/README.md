# StreamPick Backend

Spring Boot backend for StreamPick movie recommendation system with Contentstack integration.

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Contentstack account with API credentials

## Setup

1. **Copy environment template:**
   ```bash
   cp .env.example .env
   ```

2. **Add your Contentstack credentials to `.env`:**
   ```
   CONTENTSTACK_API_KEY=your_actual_api_key
   CONTENTSTACK_DELIVERY_TOKEN=your_actual_delivery_token
   CONTENTSTACK_ENVIRONMENT=production
   ```

3. **Install dependencies:**
   ```bash
   ./mvnw clean install
   ```

4. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

   The backend will start on `http://localhost:8080`

## Project Structure

```
backend/
├── src/main/java/com/streampick/
│   ├── StreamPickApplication.java    # Main application class
│   ├── config/                        # Configuration classes
│   │   ├── ContentstackConfig.java   # Contentstack SDK setup
│   │   └── CorsConfig.java           # CORS configuration
│   ├── controller/                    # REST API controllers
│   ├── service/                       # Business logic layer
│   ├── model/                         # Domain models
│   │   └── Movie.java
│   ├── dto/                           # Data Transfer Objects
│   │   ├── RecommendationRequest.java
│   │   └── RecommendationResponse.java
│   └── exception/                     # Exception handlers
│       └── GlobalExceptionHandler.java
└── src/main/resources/
    └── application.properties         # Application configuration
```

## Next Steps

1. Implement `ContentstackService` to fetch movies from CMS
2. Implement `RecommendationService` with scoring algorithm
3. Create REST controllers for API endpoints
4. Test with sample movie data

## API Endpoints (To Be Implemented)

- `GET /api/movies` - Get all movies
- `GET /api/movies/mood/{mood}` - Get movies by mood
- `POST /api/recommendations` - Get movie recommendation

## Tech Stack

- Spring Boot 3.2
- Java 17
- Contentstack Java SDK 1.6.4
- Lombok
- Maven

