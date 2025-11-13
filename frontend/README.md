# StreamPick Frontend

React frontend for StreamPick movie recommendation system.

## Prerequisites

- Node.js 18+ and npm
- Backend API running (see backend/README.md)

## Setup

1. **Install dependencies:**
   ```bash
   npm install
   ```

2. **Copy environment template:**
   ```bash
   cp .env.example .env
   ```

3. **Configure environment variables in `.env`:**
   ```
   VITE_API_URL=http://localhost:8080
   ```

4. **Run development server:**
   ```bash
   npm run dev
   ```

   The frontend will open at `http://localhost:5173`

## Project Structure

```
frontend/
├── src/
│   ├── components/           # React components
│   │   ├── Home.jsx         # (To be created)
│   │   ├── MoodSelector.jsx # (To be created)
│   │   ├── TimeSelector.jsx # (To be created)
│   │   └── RecommendationCard.jsx # (To be created)
│   ├── services/            # API integration
│   │   ├── api.js           # Axios instance
│   │   └── recommendationService.js
│   ├── hooks/               # Custom React hooks (future)
│   ├── utils/               # Helper functions (future)
│   ├── App.jsx              # Main app component
│   ├── main.jsx             # Entry point
│   └── index.css            # Global styles with Tailwind
├── index.html
├── package.json
├── vite.config.js
└── tailwind.config.js
```

## Available Scripts

- `npm run dev` - Start development server with hot reload
- `npm run build` - Build for production
- `npm run preview` - Preview production build locally

## Next Steps

1. Create UI components (Home, MoodSelector, TimeSelector, RecommendationCard)
2. Wire components together in App.jsx
3. Connect to backend API
4. Add loading and error states
5. Test end-to-end flow

## Tech Stack

- React 18
- Vite
- Tailwind CSS
- Axios
- Lucide React (icons)

