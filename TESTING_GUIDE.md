# 🧪 StreamPick Testing Guide

## ✅ What's Built

Your StreamPick application is now **fully functional** with:

### Backend (Spring Boot)
- ✅ ContentstackService with 15 mock movies
- ✅ RecommendationService with smart scoring algorithm
- ✅ MovieController (GET /api/movies, GET /api/movies/mood/{mood})
- ✅ RecommendationController (POST /api/recommendations)

### Frontend (React + Vite)
- ✅ Home component with beautiful landing page
- ✅ MoodSelector with 6 mood options
- ✅ TimeSelector with 3 time options
- ✅ RecommendationCard with movie details and AI reasoning
- ✅ Complete flow wired in App.jsx

---

## 🚀 Start Testing (2 Terminals)

### Terminal 1: Start Backend

```bash
cd backend

# Create .env file if you haven't
cat > .env << 'EOF'
CONTENTSTACK_API_KEY=mock_api_key
CONTENTSTACK_DELIVERY_TOKEN=mock_token
CONTENTSTACK_ENVIRONMENT=production
CORS_ALLOWED_ORIGINS=http://localhost:5173
EOF

# Run backend
./mvnw spring-boot:run
```

**Expected output:**
```
Started StreamPickApplication in X seconds
ContentstackService initialized with 15 mock movies
```

Backend runs on: **http://localhost:8080**

### Terminal 2: Start Frontend

```bash
cd frontend

# Install dependencies (first time only)
npm install

# Create .env file
cat > .env << 'EOF'
VITE_API_URL=http://localhost:8080
EOF

# Run frontend
npm run dev
```

**Expected output:**
```
VITE ready in X ms
Local: http://localhost:5173/
```

Frontend opens at: **http://localhost:5173**

---

## 🎯 Test the Complete Flow

### Step 1: Home Screen
- ✅ Should see "StreamPick" title with gradient
- ✅ "Find My Perfect Watch" button
- ✅ Feature badges at bottom
- **Action:** Click the main button

### Step 2: Mood Selection
- ✅ Should see 6 mood cards with emojis
- ✅ Cozy ☕, Thrilling 🎢, Laugh 😂, Deep 🧠, Escape 🚀, Chill 🌊
- ✅ Hover effects work
- **Action:** Click any mood (e.g., "Cozy & Warm")

### Step 3: Time Selection
- ✅ Should see 3 time options
- ✅ Quick Watch (30 mins), Movie Night (90 mins), Binge Mode (3+ hours)
- ✅ Clock icon at top
- **Action:** Click any time option (e.g., "Movie Night")

### Step 4: Result Screen
- ✅ Loading spinner appears briefly
- ✅ Movie card appears with:
  - Movie title and details
  - Match score percentage with animated bar
  - AI reasoning in yellow box with sparkle icon
  - Platforms, genres, rating, runtime
  - "Try Another" and "Start Over" buttons

---

## 🧪 Test Scenarios

### Scenario 1: Cozy Movie for 90 Minutes
1. Select: **Cozy & Warm**
2. Select: **90 minutes**
3. **Expected:** Should get "The Grand Budapest Hotel" or "Amélie" or "The Princess Bride"
4. **Check:** Match score should be 70%+ (mood match + good runtime fit)

### Scenario 2: Thrilling Movie for Long Session
1. Select: **Edge of Seat**
2. Select: **3+ hours** (180 mins)
3. **Expected:** Should get "Inception" or "Interstellar" or "Avatar"
4. **Check:** Long movies get bonus on weekends!

### Scenario 3: Quick Laugh
1. Select: **Need Laughs**
2. Select: **30 minutes**
3. **Expected:** Might get shorter comedies or a message about runtime
4. **Check:** Algorithm penalizes movies much longer than available time

### Scenario 4: Try Another
1. Complete any flow
2. Click **"Try Another"**
3. **Expected:** New recommendation with same mood/time
4. **Check:** Might get same movie (limited mock data) but scoring recalculates

### Scenario 5: Start Over
1. Complete any flow
2. Click **"Start Over"**
3. **Expected:** Back to home screen
4. **Action:** Try different mood/time combination

---

## 🔍 Test Backend API Directly

### Test 1: Get All Movies
```bash
curl http://localhost:8080/api/movies
```

**Expected:** JSON array with 15 movies

### Test 2: Get Movies by Mood
```bash
curl http://localhost:8080/api/movies/mood/cozy
```

**Expected:** Movies with "cozy" mood tag (Grand Budapest Hotel, Amélie, Princess Bride, etc.)

### Test 3: Get Recommendation
```bash
curl -X POST http://localhost:8080/api/recommendations \
  -H "Content-Type: application/json" \
  -d '{
    "mood": "thrilling",
    "timeAvailable": 120
  }'
```

**Expected:** JSON with movie, aiReason, and matchScore

---

## 🐛 Troubleshooting

### Backend Won't Start

**Issue:** Port 8080 already in use
```bash
# Find what's using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

**Issue:** Missing dependencies
```bash
cd backend
./mvnw clean install
```

### Frontend Won't Start

**Issue:** Dependencies not installed
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
```

**Issue:** Port 5173 in use
```bash
# Kill process on 5173
lsof -i :5173
kill -9 <PID>

# Or use different port
npm run dev -- --port 3000
```

### CORS Errors

**Issue:** "Access blocked by CORS policy"

**Solution:** Check backend .env has:
```
CORS_ALLOWED_ORIGINS=http://localhost:5173
```

Then restart backend.

### Can't Fetch Movies

**Check:**
1. Backend is running (http://localhost:8080/api/movies should work)
2. Frontend .env has correct `VITE_API_URL`
3. Check browser console for errors (F12)

---

## 📊 Scoring Algorithm Test

The recommendation algorithm considers:

1. **Mood Match (40%)** - Direct match with selected mood
2. **Runtime Fit (20%)** - How well movie length fits available time
3. **Quality Rating (15%)** - IMDb score converted to points
4. **Time of Day (15%)** - Context aware (e.g., chill content for late night)
5. **Day of Week (10%)** - Longer movies on weekends

**Test this:**
- Try same request at different times of day
- Try on weekend vs weekday
- Notice how scores change based on context!

---

## ✅ Success Criteria

Your app is working if:
- ✅ Backend starts without errors
- ✅ Frontend loads with gradient background
- ✅ Can complete full flow: Home → Mood → Time → Result
- ✅ Movie recommendation appears with match score
- ✅ "Try Another" button works
- ✅ "Start Over" returns to home
- ✅ No console errors (check with F12)
- ✅ Responsive on mobile (try resizing browser)

---

## 🎉 Next Steps After Testing

Once everything works:

1. **Add to Git:**
   ```bash
   git add .
   git commit -m "feat: complete core application with mock data"
   git push
   ```

2. **Phase 3 Options:**
   - Connect to real Contentstack CMS
   - Add more movies to mock data
   - Add image URLs for posters
   - Add Brandkit AI descriptions
   - Integrate Contentstack Personalize
   - Deploy to production

3. **Enhancements:**
   - Add filters (platform, genre)
   - User favorites/history
   - Social sharing
   - Movie trailers
   - Reviews integration

---

**Happy Testing! 🚀**

If you encounter issues, check:
1. Both terminals are running
2. No port conflicts
3. .env files are created
4. Browser console for errors (F12)

