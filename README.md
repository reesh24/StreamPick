# 🎬 StreamPick

**End the scroll. Start watching.**

StreamPick is an AI-powered movie recommendation system that helps users find the perfect movie in just 3 clicks based on their mood, available time, and contextual intelligence.

## 📋 Overview

StreamPick solves decision fatigue for streaming service users by combining:
- **Contentstack Headless CMS** for content management
- **Custom recommendation algorithm** with contextual awareness (time of day, day of week)
- **Modern full-stack architecture** with React and Spring Boot
- **Beautiful, intuitive UI** for seamless user experience

## ✨ Features

- **3-Click Experience:** Mood → Time → Perfect Recommendation
- **Smart Algorithm:** Context-aware scoring based on mood, runtime, quality, time of day, and day of week
- **AI-Powered Descriptions:** Brandkit AI generates compelling movie descriptions
- **Personalization Ready:** Contentstack Personalize integration for audience targeting
- **Mobile Responsive:** Beautiful UI that works on all devices

## 🏗️ Architecture

```
StreamPick/
├── backend/          # Spring Boot REST API
│   └── src/main/java/com/streampick/
│       ├── config/       # Contentstack & CORS configuration
│       ├── controller/   # REST endpoints
│       ├── service/      # Business logic & recommendation algorithm
│       ├── model/        # Domain models
│       ├── dto/          # Data Transfer Objects
│       └── exception/    # Error handling
│
├── frontend/         # React + Vite UI
│   └── src/
│       ├── components/   # React components
│       ├── services/     # API integration
│       ├── hooks/        # Custom hooks
│       └── utils/        # Helper functions
│
└── README.md
```

## 🛠️ Tech Stack

### Backend
- **Framework:** Spring Boot 3.2
- **Language:** Java 17
- **Build Tool:** Maven
- **CMS SDK:** Contentstack Java SDK 1.6.4
- **Architecture:** Service Layer Pattern with RESTful APIs

### Frontend
- **Framework:** React 18
- **Build Tool:** Vite
- **Styling:** Tailwind CSS
- **HTTP Client:** Axios
- **Icons:** Lucide React

### CMS & Content
- **Headless CMS:** Contentstack
- **Content Delivery:** Contentstack Delivery API
- **AI Content:** Brandkit AI (for movie descriptions)
- **Personalization:** Contentstack Personalize (optional)

## 🚀 Quick Start

### Prerequisites

- **Java 17+** and Maven
- **Node.js 18+** and npm
- **Contentstack account** with API credentials

### 1. Contentstack Setup

1. Create a free account at [Contentstack](https://www.contentstack.com)
2. Create a new Stack called "StreamPick"
3. Get your API credentials from Settings → Tokens:
   - API Key
   - Delivery Token
   - Environment name

4. Create "Movie" content type with these fields:
   - `title` (Single Line Text, required)
   - `year` (Number)
   - `runtime` (Number, required) - in minutes
   - `rating` (Number) - 0-10 scale
   - `genre` (Select Multiple)
   - `mood_tags` (Select Multiple: cozy, thrilling, laugh, deep, escape, chill)
   - `platforms` (Select Multiple: Netflix, Prime Video, HBO Max, Disney+, Hulu)
   - `description` (Multi Line Text)
   - `ai_description` (Multi Line Text)
   - `poster` (File)

5. Add 3-5 test movies and publish them

### 2. Backend Setup

```bash
cd backend

# Copy environment template
cp .env.example .env

# Add your Contentstack credentials to .env
# CONTENTSTACK_API_KEY=your_key
# CONTENTSTACK_DELIVERY_TOKEN=your_token
# CONTENTSTACK_ENVIRONMENT=production

# Run the application
./mvnw spring-boot:run
```

Backend will start on `http://localhost:8080`

See [backend/README.md](backend/README.md) for detailed instructions.

### 3. Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Copy environment template
cp .env.example .env

# Configure backend URL in .env
# VITE_API_URL=http://localhost:8080

# Run development server
npm run dev
```

Frontend will open at `http://localhost:5173`

See [frontend/README.md](frontend/README.md) for detailed instructions.

## 📊 How It Works

### The Recommendation Algorithm

StreamPick's core intelligence is a custom scoring algorithm that evaluates movies based on:

1. **Mood Match (40%)** - Does the movie match the user's selected mood?
2. **Runtime Fit (20%)** - How well does the movie length fit available time?
3. **Quality Rating (15%)** - Movie's IMDb rating score
4. **Time of Day Context (15%)** - Late night? Prefer chill content. Evening? Suggest thrillers.
5. **Day of Week Context (10%)** - Weekend? Show longer movies. Weekday? Quick watches.

The highest-scoring movie is recommended with an AI-generated description explaining why it's perfect for the user.

## 🎯 Project Status

### ✅ Completed
- [x] Project structure created
- [x] Backend Spring Boot setup with Contentstack integration
- [x] Frontend React + Vite setup with Tailwind CSS
- [x] Configuration files and environment templates
- [x] API service layer structure

### 🚧 In Progress / Next Steps
- [ ] Implement ContentstackService (fetch movies from CMS)
- [ ] Implement RecommendationService (scoring algorithm)
- [ ] Create REST API controllers
- [ ] Build UI components (Home, MoodSelector, TimeSelector, RecommendationCard)
- [ ] Connect frontend to backend API
- [ ] Add Brandkit AI for descriptions
- [ ] Integrate Contentstack Personalize
- [ ] Deploy to production

## 📁 API Endpoints (To Be Implemented)

### Movies
- `GET /api/movies` - Get all movies
- `GET /api/movies/mood/{mood}` - Get movies by mood

### Recommendations
- `POST /api/recommendations` - Get movie recommendation
  ```json
  Request:
  {
    "mood": "cozy",
    "timeAvailable": 90
  }
  
  Response:
  {
    "movie": { ... },
    "aiReason": "Perfect for your cozy mood...",
    "matchScore": 87.5
  }
  ```

## 🔧 Development Workflow

1. **Start Backend:** `cd backend && ./mvnw spring-boot:run`
2. **Start Frontend:** `cd frontend && npm run dev`
3. **Make Changes:** Edit code, auto-reloads on save
4. **Test:** Use browser + Postman for API testing

## 📝 Environment Variables

### Backend (.env)
```bash
CONTENTSTACK_API_KEY=your_api_key
CONTENTSTACK_DELIVERY_TOKEN=your_delivery_token
CONTENTSTACK_ENVIRONMENT=production
CORS_ALLOWED_ORIGINS=http://localhost:5173
```

### Frontend (.env)
```bash
VITE_API_URL=http://localhost:8080
VITE_PERSONALIZE_PROJECT_UID=your_project_uid  # Optional
```

## 🚀 Deployment (Future)

- **Backend:** Railway (Spring Boot optimized)
- **Frontend:** Contentstack Launch or Vercel
- **CI/CD:** Auto-deploy from GitHub

## 🤝 Contributing

This is a learning project demonstrating full-stack development with headless CMS integration.

## 📄 License

MIT License - See LICENSE file for details

## 🎓 Learning Objectives

This project demonstrates:
- Full-stack application architecture
- RESTful API design
- Headless CMS integration
- Custom algorithm development
- Modern frontend development with React
- Spring Boot best practices
- Tailwind CSS utility-first styling

---

**Built with ❤️ using Contentstack, Spring Boot, and React**

