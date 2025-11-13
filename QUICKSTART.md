# 🚀 StreamPick Quick Start Guide

Get StreamPick running in 15 minutes!

## Step 1: Contentstack Setup (5 mins)

1. **Create Account:**
   - Go to https://www.contentstack.com
   - Sign up (it's free)
   - Create a new Stack: "StreamPick"

2. **Get Credentials:**
   - Go to Settings → Tokens
   - Copy these 3 values:
     - ✅ API Key
     - ✅ Delivery Token
     - ✅ Environment (usually "production")

3. **Create Movie Content Type:**
   - Go to Content Models → Create Content Type
   - Name: "Movie", UID: "movie"
   - Add these fields (copy-paste names exactly):

   | Field Name | Field Type | UID | Required? | Options |
   |------------|-----------|-----|-----------|---------|
   | Title | Single Line Text | title | Yes | - |
   | Year | Number | year | No | - |
   | Runtime | Number | runtime | Yes | - |
   | Rating | Number | rating | No | (0-10 scale) |
   | Genre | Select (Multiple) | genre | No | Action, Comedy, Drama, Romance, Thriller, Sci-Fi |
   | Mood Tags | Select (Multiple) | mood_tags | Yes | cozy, thrilling, laugh, deep, escape, chill |
   | Platforms | Select (Multiple) | platforms | No | Netflix, Prime Video, HBO Max, Disney+, Hulu |
   | Description | Multi Line Text | description | No | - |
   | AI Description | Multi Line Text | ai_description | No | - |
   | Poster | File | poster | No | - |

4. **Add 3 Test Movies:**

   **Movie 1:**
   - Title: Inception
   - Year: 2010
   - Runtime: 148
   - Rating: 8.8
   - Mood Tags: thrilling, deep
   - Platforms: Netflix
   - Description: "A mind-bending thriller"
   - **IMPORTANT:** Click "Publish"

   **Movie 2:**
   - Title: The Grand Budapest Hotel
   - Year: 2014
   - Runtime: 99
   - Rating: 8.1
   - Mood Tags: cozy, laugh
   - Platforms: HBO Max
   - Description: "A whimsical comedy"
   - **IMPORTANT:** Click "Publish"

   **Movie 3:**
   - Title: Finding Nemo
   - Year: 2003
   - Runtime: 100
   - Rating: 8.2
   - Mood Tags: chill, escape
   - Platforms: Disney+
   - Description: "An underwater adventure"
   - **IMPORTANT:** Click "Publish"

---

## Step 2: Backend Setup (3 mins)

```bash
# Navigate to backend
cd backend

# Create .env file
cp .env.example .env

# Open .env and add YOUR Contentstack credentials:
# CONTENTSTACK_API_KEY=your_actual_api_key
# CONTENTSTACK_DELIVERY_TOKEN=your_actual_token
# CONTENTSTACK_ENVIRONMENT=production
```

**On Mac/Linux:**
```bash
# Make mvnw executable
chmod +x mvnw

# Run backend
./mvnw spring-boot:run
```

**On Windows:**
```bash
mvnw.cmd spring-boot:run
```

✅ **Success:** You should see "Started StreamPickApplication" at http://localhost:8080

---

## Step 3: Frontend Setup (3 mins)

**Open a NEW terminal window** (keep backend running!):

```bash
# Navigate to frontend
cd frontend

# Install dependencies
npm install

# Create .env file
cp .env.example .env

# The .env should have:
# VITE_API_URL=http://localhost:8080
```

**Run frontend:**
```bash
npm run dev
```

✅ **Success:** Browser opens at http://localhost:5173 with StreamPick UI!

---

## Step 4: Test It! (2 mins)

### Test Backend API:

Open http://localhost:8080/api/movies in your browser or:

```bash
curl http://localhost:8080/api/movies
```

You should see JSON with your 3 movies!

### Test Frontend:

The browser should show:
- "StreamPick" title with gradient
- "End the scroll. Start watching."
- A card saying "StreamPick is ready!"

---

## ✅ You're All Set!

### Next Steps:

**Phase 2: Build Core Features**
1. Implement ContentstackService (fetch movies)
2. Implement RecommendationService (the magic algorithm!)
3. Create REST controllers
4. Build UI components
5. Connect everything

### Need Help?

**Common Issues:**

1. **Backend won't start:**
   - Check Java version: `java -version` (need 17+)
   - Verify .env file has correct Contentstack credentials
   - Check if port 8080 is available

2. **Frontend won't start:**
   - Check Node version: `node -v` (need 18+)
   - Run `npm install` again
   - Check if port 5173 is available

3. **Can't fetch movies:**
   - Verify movies are PUBLISHED in Contentstack
   - Check backend logs for errors
   - Test API directly: http://localhost:8080/api/movies

4. **CORS errors:**
   - Backend must be running first
   - Check CORS_ALLOWED_ORIGINS in backend .env
   - Should be: `http://localhost:5173`

---

## 📚 What's Next?

Check out:
- [Main README](README.md) - Full project overview
- [Backend README](backend/README.md) - Backend details
- [Frontend README](frontend/README.md) - Frontend details

Happy coding! 🎬✨

