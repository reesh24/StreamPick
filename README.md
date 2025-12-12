# 🎬 StreamPick

**End the scroll. Start watching.**

StreamPick is an AI-powered movie recommendation system that finds your perfect movie in 3 clicks based on mood and time. Plus, subscribe to get personalized movie alerts via email!

## ✨ Features

- 🤖 **AI-Powered Recommendations** - ML-based content filtering with mood mapping
- 🎭 **Mood-Based Selection** - Six curated moods that match your vibe
- ⏱️ **Time-Aware Filtering** - Smart recommendations based on available time
- 📧 **Email Subscriptions** - Get notified when movies matching your preferred moods are published
- 🎯 **Smart Mood Matching** - Backend normalizes UI-friendly moods to match movie tags
- 🎨 **Beautiful UI** - Modern, responsive design with smooth animations
- 📱 **Mobile Friendly** - Works seamlessly on all devices

## 🏗️ Architecture

```
┌──────────────────┐
│ Contentstack CMS │ (Movies + Subscribers)
└────────┬─────────┘
         │
         ↓
   ┌─────────────────────────┐
   │ Contentstack Automate   │ (Email Automation)
   │ - Entry Trigger         │
   │ - HTTP Request          │
   │ - Loop & Send Emails    │
   └─────────┬───────────────┘
             │
             ↓
   ┌─────────────────────────┐
   │   Backend (Spring Boot) │ (Deployed on Render)
   │ - Movie API             │
   │ - Subscriber Management │
   │ - Mood Filtering        │
   │ - MoodMapper Utility    │
   └─────────┬───────────────┘
             │
             ↓
   ┌─────────────────────────┐
   │  ML Service (FastAPI)   │ (Deployed on Render)
   │ - Content-based Filtering│
   │ - TF-IDF + Cosine Sim   │
   │ - Mood Normalization    │
   └─────────┬───────────────┘
             │
             ↓
   ┌─────────────────────────┐
   │   Frontend (React)      │ (Vite + Tailwind)
   │ - Movie Discovery       │
   │ - Subscription Form     │
   └─────────────────────────┘
```

## 🛠️ Tech Stack

### Backend
- **Framework:** Spring Boot 3.2
- **Language:** Java 17
- **CMS SDK:** Contentstack Management & Delivery SDK
- **Deployment:** Render.com
- **URL:** https://streampickbackend.onrender.com

### ML Service
- **Framework:** FastAPI
- **Language:** Python 3.11
- **ML Libraries:** scikit-learn, pandas, numpy
- **Algorithm:** TF-IDF vectorization + Cosine similarity
- **Deployment:** Render.com
- **URL:** https://streampick-ml-service.onrender.com

### Frontend
- **Framework:** React 18 + Vite
- **Styling:** Tailwind CSS
- **HTTP Client:** Axios
- **Icons:** Lucide React
- **Deployment:** Vercel (or Contentstack Launch)

### Content & Automation
- **CMS:** Contentstack (Headless CMS)
- **Email Automation:** Contentstack Automate
- **Subscriber Storage:** Contentstack Modular Blocks

## 🚀 Quick Start

### Prerequisites
- Java 17+ and Maven
- Python 3.11+
- Node.js 18+
- Contentstack account with API credentials

### Local Development

**1. Clone the repository:**
```bash
git clone https://github.com/reesh24/StreamPick.git
cd StreamPick
```

**2. Set up Backend:**
```bash
cd backend
# Create .env file with your Contentstack credentials
mvn spring-boot:run
# Backend runs on http://localhost:8080
```

**3. Set up ML Service:**
```bash
cd recommendation-service
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
uvicorn app.main:app --host 0.0.0.0 --port 8001
# ML service runs on http://localhost:8001
```

**4. Set up Frontend:**
```bash
cd frontend
npm install
npm run dev
# Frontend runs on http://localhost:5173
```

## 📧 Email Subscription System

### How It Works

1. **User Subscribes** - Fills form with name, email, and preferred moods
2. **Stored in Contentstack** - Added to Subscribers entry (modular block)
3. **Movie Published** - Admin publishes new movie with mood tags
4. **Automate Triggers** - Contentstack Automate workflow fires
5. **Backend Filters** - Fetches and filters subscribers by matching moods
6. **Email Sent** - Only matching subscribers receive personalized emails

### Mood Mapping

Users select UI-friendly moods that are normalized to backend tags:

| UI Label | Backend Tag |
|----------|-------------|
| Cozy & Warm | `cozy` |
| Edge of Seat | `thrilling` |
| Need Laughs | `laugh` |
| Make Me Think | `deep` |
| Pure Escapism | `escape` |
| Background Vibe | `chill` |

The `MoodMapper` utility (both Java & Python) handles normalization automatically.

## 🎯 API Endpoints

### Movies
- `GET /api/movies` - Get all published movies
- `GET /api/movies/mood/{mood}` - Get movies by mood tag

### Recommendations
- `POST /api/recommendations` - Get personalized recommendation
  ```json
  {
    "mood": "cozy",
    "timeAvailable": 120
  }
  ```

### Subscribers
- `POST /api/subscribers/add` - Add new subscriber
- `GET /api/subscribers/count` - Get subscriber count
- `POST /api/subscribers/filter-by-moods` - Filter subscribers by mood (for Automate)

## 🌐 Deployed Services

### Production URLs
- **Backend API:** https://streampickbackend.onrender.com
- **ML Service:** https://streampick-ml-service.onrender.com
- **Frontend:** (Deploy to Vercel or Contentstack Launch)

### Environment Variables

**Backend (Render):**
```
CONTENTSTACK_API_KEY=<your_api_key>
CONTENTSTACK_DELIVERY_TOKEN=<your_delivery_token>
CONTENTSTACK_AUTHTOKEN=<your_management_token>
CONTENTSTACK_ENVIRONMENT=production
CONTENTSTACK_SUBSCRIBERS_ENTRY_UID=<subscribers_entry_uid>
CORS_ALLOWED_ORIGINS=http://localhost:5173,https://your-frontend-url
PYTHON_RECOMMENDATION_SERVICE_URL=https://streampick-ml-service.onrender.com
```

**ML Service (Render):**
```
CONTENTSTACK_API_KEY=<your_api_key>
CONTENTSTACK_DELIVERY_TOKEN=<your_delivery_token>
CONTENTSTACK_ENVIRONMENT=production
```

**Frontend:**
```
VITE_API_URL=https://streampickbackend.onrender.com
```

## 🎭 How the Recommendation Algorithm Works

1. **Content-Based Filtering** - Uses TF-IDF to vectorize movie descriptions and tags
2. **Mood Mapping** - Normalizes user mood to match movie mood tags
3. **Time Filtering** - Only shows movies that fit available time (with buffer)
4. **Scoring System** - Ranks movies by content similarity
5. **Alternative Recommendations** - Provides 4 similar options

## 📚 Project Structure

```
StreamPick/
├── backend/                 # Spring Boot API
│   ├── src/main/java/com/streampick/
│   │   ├── config/         # Contentstack & CORS config
│   │   ├── controller/     # REST endpoints
│   │   ├── service/        # Business logic
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── model/          # Domain models
│   │   ├── util/           # MoodMapper utility
│   │   └── exception/      # Error handling
│   └── Dockerfile
│
├── recommendation-service/  # Python ML API
│   ├── app/
│   │   ├── main.py         # FastAPI app
│   │   ├── models/         # ML models
│   │   ├── services/       # Recommendation engine
│   │   └── schemas/        # Request/Response models
│   └── Dockerfile
│
├── frontend/               # React UI
│   ├── src/
│   │   ├── components/     # React components
│   │   ├── services/       # API integration
│   │   └── App.jsx
│   └── package.json
│
└── README.md
```

## 🔧 Development

### Backend
```bash
cd backend
mvn spring-boot:run
```

### ML Service
```bash
cd recommendation-service
source venv/bin/activate
uvicorn app.main:app --reload --port 8001
```

### Frontend
```bash
cd frontend
npm run dev
```

## 🚀 Deployment

See `DEPLOYMENT.md` for detailed deployment instructions.

**Deployed Stack:**
- Backend: Render.com
- ML Service: Render.com
- Frontend: Vercel or Contentstack Launch
- Email Automation: Contentstack Automate

## 📧 Email Automation Setup

1. **Create Automate Workflow** in Contentstack
2. **Add Entry Trigger** for movie content type
3. **Add HTTP Request** to filter subscribers by mood
4. **Add Repeat Path** to loop through filtered subscribers
5. **Add Email Action** to send personalized notifications

Details in `DEPLOYMENT.md`.

## 🎉 Key Features

### Smart Mood Filtering
When a movie with moods `["cozy", "laugh"]` is published:
- ✅ Subscribers with "Cozy & Warm" get notified
- ✅ Subscribers with "Need Laughs" get notified
- ❌ Subscribers with only "Edge of Seat" don't get notified

### Personalized Email Content
Each email includes:
- Movie title, poster, year, runtime, rating
- Why it matches their preferred moods
- Genres and streaming platforms
- Beautiful HTML design

## 🧪 Testing

**Test Backend:**
```bash
curl https://streampickbackend.onrender.com/api/movies
```

**Test ML Service:**
```bash
curl https://streampick-ml-service.onrender.com/
```

**Test Mood Filtering:**
```bash
curl -X POST https://streampickbackend.onrender.com/api/subscribers/filter-by-moods \
  -H "Content-Type: application/json" \
  -d '{"moodTags":["cozy","laugh"]}'
```

## 📄 License

MIT License

## 🙏 Acknowledgments

Built with:
- [Contentstack](https://www.contentstack.com) - Headless CMS
- [Spring Boot](https://spring.io/projects/spring-boot) - Backend framework
- [FastAPI](https://fastapi.tiangolo.com) - Python API framework
- [React](https://react.dev) + [Vite](https://vitejs.dev) - Frontend
- [Tailwind CSS](https://tailwindcss.com) - Styling

---

**Made with ❤️ using Contentstack + Python ML + Spring Boot + React**
