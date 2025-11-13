# 📁 StreamPick Project Structure

## Complete File Tree

```
personalize-project/
│
├── README.md                          # Main project documentation
├── QUICKSTART.md                      # 15-minute setup guide
├── PROJECT_STRUCTURE.md               # This file
├── .gitignore                         # Root gitignore
│
├── backend/                           # Spring Boot Backend
│   ├── pom.xml                        # Maven dependencies
│   ├── mvnw                           # Maven wrapper (Unix)
│   ├── mvnw.cmd                       # Maven wrapper (Windows)
│   ├── .gitignore                     # Backend gitignore
│   ├── .env.example                   # Environment template
│   ├── README.md                      # Backend documentation
│   │
│   ├── .mvn/                          # Maven wrapper config
│   │   └── wrapper/
│   │       └── maven-wrapper.properties
│   │
│   └── src/main/
│       ├── java/com/streampick/
│       │   │
│       │   ├── StreamPickApplication.java    # Main Spring Boot app
│       │   │
│       │   ├── config/                       # Configuration classes
│       │   │   ├── ContentstackConfig.java   # Contentstack SDK setup
│       │   │   └── CorsConfig.java           # CORS configuration
│       │   │
│       │   ├── controller/                   # REST API controllers (to be created)
│       │   │   ├── MovieController.java      # TODO
│       │   │   └── RecommendationController.java  # TODO
│       │   │
│       │   ├── service/                      # Business logic
│       │   │   ├── ContentstackService.java  # TODO
│       │   │   └── RecommendationService.java # TODO
│       │   │
│       │   ├── model/                        # Domain models
│       │   │   └── Movie.java                # ✅ Created
│       │   │
│       │   ├── dto/                          # Data Transfer Objects
│       │   │   ├── RecommendationRequest.java   # ✅ Created
│       │   │   └── RecommendationResponse.java  # ✅ Created
│       │   │
│       │   └── exception/                    # Exception handlers
│       │       └── GlobalExceptionHandler.java  # ✅ Created
│       │
│       └── resources/
│           └── application.properties        # App configuration
│
│
└── frontend/                          # React Frontend
    ├── package.json                   # Node dependencies
    ├── vite.config.js                 # Vite configuration
    ├── tailwind.config.js             # Tailwind CSS config
    ├── postcss.config.js              # PostCSS config
    ├── index.html                     # HTML entry point
    ├── .gitignore                     # Frontend gitignore
    ├── .env.example                   # Environment template
    ├── README.md                      # Frontend documentation
    │
    └── src/
        ├── main.jsx                   # React entry point
        ├── App.jsx                    # Main app component
        ├── index.css                  # Global styles + Tailwind
        │
        ├── components/                # React components (to be created)
        │   ├── Home.jsx               # TODO
        │   ├── MoodSelector.jsx       # TODO
        │   ├── TimeSelector.jsx       # TODO
        │   └── RecommendationCard.jsx # TODO
        │
        ├── services/                  # API integration
        │   ├── api.js                 # ✅ Axios instance
        │   └── recommendationService.js  # ✅ API methods
        │
        ├── hooks/                     # Custom React hooks (future)
        │
        └── utils/                     # Helper functions (future)
```

## 📊 What's Created vs What's Next

### ✅ Completed (Phase 1: Foundation)

**Backend:**
- [x] Spring Boot project structure
- [x] Maven configuration with all dependencies
- [x] Contentstack SDK configuration
- [x] CORS configuration
- [x] Domain models (Movie)
- [x] DTOs (Request/Response)
- [x] Global exception handler
- [x] Application properties
- [x] Environment template
- [x] Maven wrapper for easy setup

**Frontend:**
- [x] React + Vite project structure
- [x] Tailwind CSS configuration
- [x] Axios API service layer
- [x] Recommendation service methods
- [x] Basic App component
- [x] Global styling
- [x] Environment template
- [x] Package.json with all dependencies

**Documentation:**
- [x] Main README with full overview
- [x] Quick Start Guide (15 mins)
- [x] Backend README
- [x] Frontend README
- [x] Project structure documentation

### 🚧 Next Steps (Phase 2: Core Features)

**Backend Services (Highest Priority):**
1. [ ] `ContentstackService.java` - Fetch movies from CMS
2. [ ] `RecommendationService.java` - Scoring algorithm (THE CORE!)
3. [ ] `MovieController.java` - REST endpoints for movies
4. [ ] `RecommendationController.java` - Recommendation endpoint

**Frontend Components:**
1. [ ] `Home.jsx` - Landing page
2. [ ] `MoodSelector.jsx` - 6 mood options
3. [ ] `TimeSelector.jsx` - Time selection
4. [ ] `RecommendationCard.jsx` - Show result
5. [ ] Wire everything in `App.jsx`

**Content:**
1. [ ] Add 20+ movies to Contentstack
2. [ ] Set up Brandkit AI (optional)
3. [ ] Generate AI descriptions (optional)

**Integration & Testing:**
1. [ ] Connect frontend to backend
2. [ ] Test end-to-end flow
3. [ ] Handle errors and loading states

## 🎯 File Purposes

### Key Backend Files

| File | Purpose | Status |
|------|---------|--------|
| `pom.xml` | Maven dependencies and build config | ✅ Ready |
| `StreamPickApplication.java` | Spring Boot entry point | ✅ Ready |
| `ContentstackConfig.java` | Initializes Contentstack SDK | ✅ Ready |
| `Movie.java` | Domain model for movies | ✅ Ready |
| `RecommendationRequest.java` | API request DTO | ✅ Ready |
| `RecommendationResponse.java` | API response DTO | ✅ Ready |
| `ContentstackService.java` | Fetches movies from CMS | ⏳ TODO |
| `RecommendationService.java` | **Core algorithm** | ⏳ TODO |
| `MovieController.java` | REST endpoints | ⏳ TODO |

### Key Frontend Files

| File | Purpose | Status |
|------|---------|--------|
| `package.json` | Dependencies and scripts | ✅ Ready |
| `vite.config.js` | Vite build configuration | ✅ Ready |
| `tailwind.config.js` | Tailwind theme and colors | ✅ Ready |
| `App.jsx` | Main React component | ✅ Basic |
| `api.js` | Axios instance | ✅ Ready |
| `recommendationService.js` | API methods | ✅ Ready |
| `Home.jsx` | Landing page component | ⏳ TODO |
| `MoodSelector.jsx` | Mood selection UI | ⏳ TODO |
| `TimeSelector.jsx` | Time selection UI | ⏳ TODO |
| `RecommendationCard.jsx` | Result display | ⏳ TODO |

## 🚀 Running the Project

### Backend
```bash
cd backend
cp .env.example .env
# Add Contentstack credentials to .env
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
cp .env.example .env
npm run dev
```

## 📝 Important Notes

1. **Maven Wrapper:** The `mvnw` and `mvnw.cmd` files allow you to run Maven without installing it globally.

2. **Environment Files:** The `.env.example` files are templates. Copy them to `.env` and add your actual credentials.

3. **Folder Structure:** Empty directories like `controller/` and `service/` are ready for your code.

4. **Git Ignore:** Sensitive files (`.env`, `node_modules/`, `target/`) are already ignored.

5. **Next Critical Step:** Implement `ContentstackService` to connect to your CMS and fetch movies!

---

**Built with ❤️ - Ready for Phase 2!**

