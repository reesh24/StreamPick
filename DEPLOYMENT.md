# 🚀 StreamPick Deployment Guide

## 📦 Deployed Services

### Backend (Spring Boot)
- **URL:** https://streampickbackend.onrender.com
- **Platform:** Render.com
- **Repository:** `reesh24/StreamPick`
- **Root Directory:** `backend`
- **Branch:** `main`

### ML Service (Python FastAPI)
- **URL:** https://streampick-ml-service.onrender.com
- **Platform:** Render.com
- **Repository:** `reesh24/StreamPick`
- **Root Directory:** `recommendation-service`
- **Branch:** `main`

### Frontend (React + Vite)
- **Platform:** Contentstack Launch
- **Repository:** `reesh24/StreamPick`
- **Root Directory:** `frontend`
- **Branch:** `main`

---

## 🔐 Environment Variables

### Backend (Render)
```
CONTENTSTACK_API_KEY=<your_api_key>
CONTENTSTACK_DELIVERY_TOKEN=<your_delivery_token>
CONTENTSTACK_AUTHTOKEN=<your_management_token>
CONTENTSTACK_ENVIRONMENT=production
CONTENTSTACK_SUBSCRIBERS_ENTRY_UID=<subscribers_entry_uid>
CORS_ALLOWED_ORIGINS=http://localhost:5173,https://your-launch-url.contentstackapp.com
PYTHON_RECOMMENDATION_SERVICE_URL=https://streampick-ml-service.onrender.com
```

### ML Service (Render)
```
CONTENTSTACK_API_KEY=<your_api_key>
CONTENTSTACK_DELIVERY_TOKEN=<your_delivery_token>
CONTENTSTACK_ENVIRONMENT=production
```

### Frontend (Contentstack Launch)
```
VITE_API_URL=https://streampickbackend.onrender.com
```

---

## 🎯 Contentstack Automate Workflow

**Workflow:** Movie Email Notifications

**Steps:**
1. **Entry Trigger** - Fires when movie is published
2. **HTTP Request** - Calls `https://streampickbackend.onrender.com/api/subscribers/filter-by-moods`
3. **Repeat Path** - Loops through filtered subscribers
4. **Email by Automate** - Sends personalized email to each subscriber

---

## 🚀 Deployment Instructions

### Deploy Frontend to Contentstack Launch

1. **Log in to Contentstack** → Click **Launch** icon
2. **Create New Project:**
   - Click **+ New Project** → **Import from Git Repository**
   - Select **GitHub** → Choose `reesh24/StreamPick`
   - **Repository:** `reesh24/StreamPick`
   - **Branch:** `main`
   - **Root Directory:** `frontend`
   - **Project Name:** `StreamPick Frontend`
   - **Environment Name:** `production`
3. **Build Settings** (auto-populated for React):
   - **Build Command:** `npm run build`
   - **Output Directory:** `dist`
   - **Install Command:** `npm install`
4. **Environment Variables:**
   - **Key:** `VITE_API_URL`
   - **Value:** `https://streampickbackend.onrender.com`
5. Click **Deploy**

---

## 🧪 Testing

### Test Backend
```bash
curl https://streampickbackend.onrender.com/api/subscribers/count
```

### Test ML Service
```bash
curl https://streampick-ml-service.onrender.com/
```

### Test Subscriber Filtering
```bash
curl -X POST https://streampickbackend.onrender.com/api/subscribers/filter-by-moods \
  -H "Content-Type: application/json" \
  -d '{"moodTags":["cozy","laugh"]}'
```

### Test Full Recommendation Flow
```bash
curl -X POST https://streampickbackend.onrender.com/api/recommendations \
  -H "Content-Type: application/json" \
  -d '{"mood":"cozy","timeAvailable":120}'
```

---

## 📧 Email Notification Flow

1. **Publish a movie** in Contentstack with mood tags (e.g., `cozy`, `laugh`)
2. **Contentstack Automate** triggers workflow
3. **Backend filters** subscribers by matching moods
4. **Automate sends** personalized emails to matching subscribers

---

## 🔗 Architecture

```
Frontend (Contentstack Launch)
  ↓
Backend API (Render)
  ↓
ML Service (Render)
  ↓
Contentstack CMS
  ↓
Contentstack Automate (Email Notifications)
```

---

## ✅ Checklist

- [x] Backend deployed on Render
- [x] ML Service deployed on Render
- [x] Subscriber management working
- [x] Mood-based filtering working
- [x] Contentstack Automate configured
- [x] Email notifications working
- [ ] Frontend deployed on Contentstack Launch
- [ ] Update CORS in backend to include Launch URL
- [ ] Test full end-to-end flow

---

## 📝 Notes

- **Cold Starts:** Free tier services spin down after 15 min. First request may take 30-120 seconds.
- **CORS:** Update backend `CORS_ALLOWED_ORIGINS` after deploying frontend to Launch.
- **Environment Variables:** Never commit `.env` files. Use platform environment variables.

