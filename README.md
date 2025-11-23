# 🎬 StreamPick

**AI-Powered Movie Recommendations in 3 Clicks**

StreamPick is an intelligent movie recommendation system that uses machine learning to find your perfect movie based on your mood and available time.

## ✨ Features

- 🤖 **ML-Powered Recommendations** - Content-based filtering using TF-IDF and cosine similarity
- 🎭 **Mood-Based Selection** - Six curated moods (cozy, thrilling, laugh, deep, escape, chill)
- ⏱️ **Time-Aware** - Smart filtering based on your available time
- 🎬 **Interactive UI** - Click alternative recommendations to explore all options
- 📊 **Match Scores** - See how well each movie fits your preferences
- 🎨 **Beautiful Design** - Modern, responsive interface with smooth animations
- 📱 **Mobile Friendly** - Works seamlessly on all devices

## 🏗️ Architecture

```
┌─────────────┐
│ Contentstack│ (Movie data + images)
└──────┬──────┘
       │
       ↓
┌──────────────┐
│ Java Backend │ (Spring Boot + Contentstack SDK)
└──────┬───────┘
       │
       ↓
┌──────────────┐
│ Python ML    │ (FastAPI + scikit-learn)
│  Service     │ (TF-IDF + Cosine Similarity)
└──────┬───────┘
       │
       ↓
┌──────────────┐
│ React        │ (Vite + Tailwind CSS)
│  Frontend    │
└──────────────┘
```

## 🛠️ Tech Stack

### Backend (Java)
- **Framework:** Spring Boot 3.2
- **Language:** Java 17
- **CMS SDK:** Contentstack Java SDK
- **HTTP Client:** RestTemplate

### ML Service (Python)
- **Framework:** FastAPI
- **ML Library:** scikit-learn
- **Data Processing:** pandas, numpy
- **Algorithms:** TF-IDF Vectorization, Cosine Similarity

### Frontend
- **Framework:** React 18
- **Build Tool:** Vite
- **Styling:** Tailwind CSS
- **HTTP Client:** Axios
- **Icons:** Lucide React

### Content Management
- **CMS:** Contentstack
- **Content Delivery:** Contentstack Delivery API

## 📁 Project Structure

```
personalize-project/
├── backend/                 # Java Spring Boot API
│   ├── src/main/java/com/streampick/
│   │   ├── config/         # Configuration
│   │   ├── controller/     # REST endpoints
│   │   ├── service/        # Business logic
│   │   ├── model/          # Domain models
│   │   └── dto/            # Data transfer objects
│   └── pom.xml
│
├── recommendation-service/  # Python ML service
│   ├── app/
│   │   ├── main.py         # FastAPI app
│   │   ├── models/         # ML models
│   │   ├── schemas/        # Pydantic schemas
│   │   └── services/       # ML engine
│   └── requirements.txt
│
├── frontend/               # React UI
│   ├── src/
│   │   ├── components/    # React components
│   │   ├── services/      # API integration
│   │   ├── App.jsx
│   │   └── main.jsx
│   └── package.json
│
├── start-all.sh           # Start all services
├── stop-all.sh            # Stop all services
└── README.md
```

## 🚀 Quick Start

### Prerequisites

- Java 17+
- Maven 3.6+
- Python 3.13+
- Node.js 18+
- Contentstack account

### 1. Clone & Setup

```bash
# Clone the repository
cd personalize-project

# Configure Contentstack credentials
# Edit backend/src/main/resources/application.properties
# Add your API key, delivery token, and environment
```

### 2. Start All Services

```bash
# Make script executable
chmod +x start-all.sh

# Start everything (Python ML + Java backend + React frontend)
./start-all.sh
```

This will:
- Start Python ML service on `http://localhost:8001`
- Start Java backend on `http://localhost:8080`
- Start React frontend on `http://localhost:5173`

### 3. Access the App

Open your browser to **http://localhost:5173** and:
1. Select your mood
2. Choose available time
3. Get your perfect movie recommendation!

### Stop All Services

```bash
./stop-all.sh
```

## 🧠 How It Works

### Content-Based Filtering

The recommendation engine uses **TF-IDF (Term Frequency-Inverse Document Frequency)** to convert movie features into numerical vectors, then calculates **Cosine Similarity** to find movies similar to user preferences.

**Scoring Algorithm:**
1. **Mood Match (40 points)** - Perfect mood tag match
2. **Quality Rating (20 points)** - Higher rated movies get bonus
3. **Content Similarity (30 points)** - TF-IDF + cosine similarity
4. **Runtime Fit (10 points)** - How well it fits your time

### Data Flow

```
1. User selects mood & time
2. Frontend sends request to Java backend
3. Java fetches ALL movies from Contentstack (with images)
4. Java sends movies to Python ML service
5. Python trains TF-IDF model on the fly
6. Python scores and ranks all movies
7. Python returns top 5 recommendations
8. Java enriches with full movie data
9. Frontend displays featured + 4 alternatives
10. User can click any alternative to explore
```

## 📊 API Endpoints

### Java Backend (Port 8080)

- `GET /api/movies` - Get all movies
- `GET /api/movies/mood/{mood}` - Get movies by mood
- `POST /api/recommendations` - Get recommendations
  ```json
  {
    "mood": "laugh",
    "timeAvailable": 90,
    "userId": "optional"
  }
  ```

### Python ML Service (Port 8001)

- `GET /health` - Health check
- `GET /moods` - Get available moods
- `POST /recommend` - ML recommendation engine

## 🎯 Features in Detail

### Interactive Recommendations
- **Featured Movie**: Large display with full details, poster, platforms, description
- **Alternative Movies**: 4 additional recommendations in card format
- **Click to Explore**: Click any alternative to make it the featured movie
- **Smooth Animations**: Hover effects, image zoom, smooth transitions

### Smart Filtering
- **Mood-based**: Filters movies matching selected mood
- **Time-aware**: Prioritizes movies that fit available time
- **Quality-weighted**: Higher rated movies get preference
- **Fallback Logic**: Java fallback if Python service is unavailable

### Beautiful UI
- **Gradient backgrounds**: Purple/pink themed
- **Glass morphism**: Backdrop blur effects
- **Smooth scrolling**: Auto-scroll to top on card swap
- **Responsive design**: Works on mobile, tablet, desktop
- **Loading states**: Elegant loading animations

## 🔧 Development

### Run Individual Services

**Python ML Service:**
```bash
cd recommendation-service
python3 -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
pip install -r requirements.txt
uvicorn app.main:app --host 0.0.0.0 --port 8001 --reload
```

**Java Backend:**
```bash
cd backend
./mvnw spring-boot:run
```

**React Frontend:**
```bash
cd frontend
npm install
npm run dev
```

## 📝 Configuration

### Backend (`application.properties`)
```properties
contentstack.api.key=your_api_key
contentstack.delivery.token=your_delivery_token
contentstack.environment=production
python.recommendation.service.url=http://localhost:8001
cors.allowed.origins=http://localhost:5173
```

## 🎓 Learning Highlights

This project demonstrates:
- ✅ Full-stack ML application architecture
- ✅ Content-based recommendation systems
- ✅ TF-IDF and cosine similarity
- ✅ Microservices architecture (Java + Python)
- ✅ Headless CMS integration
- ✅ RESTful API design
- ✅ Modern React with hooks
- ✅ Responsive UI design
- ✅ Error handling and fallbacks

## 📄 License

MIT License

---

**Made with ❤️ using Contentstack + Spring Boot + Python ML + React**
