#!/bin/bash

echo "🛑 Stopping all StreamPick services..."

# Kill Python service
echo "Stopping Python service (port 8001)..."
lsof -ti:8001 | xargs kill -9 2>/dev/null

# Kill Java backend
echo "Stopping Java backend (port 8080)..."
lsof -ti:8080 | xargs kill -9 2>/dev/null

# Kill React frontend
echo "Stopping React frontend (port 5173)..."
lsof -ti:5173 | xargs kill -9 2>/dev/null

echo "✅ All services stopped!"