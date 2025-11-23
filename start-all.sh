#!/bin/bash

echo "🚀 Starting StreamPick - Full Stack Application"
echo "=============================================="

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Function to cleanup on exit
cleanup() {
    echo -e "\n${RED}Shutting down all services...${NC}"
    kill $(jobs -p) 2>/dev/null
    exit
}

trap cleanup SIGINT SIGTERM

# Navigate to project root
cd "$(dirname "$0")"

# 1. Start Python ML Service
echo -e "\n${BLUE}[1/3] Starting Python ML Service (port 8001)...${NC}"
cd recommendation-service
source venv/bin/activate
uvicorn app.main:app --host 0.0.0.0 --port 8001 > ../logs/python.log 2>&1 &
PYTHON_PID=$!
echo -e "${GREEN}✓ Python service started (PID: $PYTHON_PID)${NC}"
cd ..

# Wait for Python to initialize
sleep 3

# 2. Start Java Backend
echo -e "\n${BLUE}[2/3] Starting Java Backend (port 8080)...${NC}"
cd backend
./mvnw spring-boot:run > ../logs/java.log 2>&1 &
JAVA_PID=$!
echo -e "${GREEN}✓ Java backend starting (PID: $JAVA_PID)${NC}"
cd ..

# Wait for Java to initialize
sleep 10

# 3. Start React Frontend
echo -e "\n${BLUE}[3/3] Starting React Frontend (port 5173)...${NC}"
cd frontend
npm run dev > ../logs/frontend.log 2>&1 &
FRONTEND_PID=$!
echo -e "${GREEN}✓ Frontend started (PID: $FRONTEND_PID)${NC}"
cd ..

echo -e "\n${GREEN}=============================================="
echo -e "✅ All services are running!"
echo -e "=============================================="
echo -e "${NC}"
echo "📊 Service URLs:"
echo "  • Frontend:  http://localhost:5173"
echo "  • Java API:  http://localhost:8080"
echo "  • Python ML: http://localhost:8001"
echo ""
echo "📝 Logs are in:"
echo "  • logs/python.log"
echo "  • logs/java.log"
echo "  • logs/frontend.log"
echo ""
echo "Press Ctrl+C to stop all services"
echo ""

# Wait for all background jobs
wait