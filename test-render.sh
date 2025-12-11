#!/bin/bash

echo "🧪 Testing Render Backend..."
echo ""

curl -X POST https://streampickbackend.onrender.com/api/subscribers/filter-by-moods \
  -H "Content-Type: application/json" \
  -d '{"moodTags":["cozy","laugh"]}' \
  --silent | python3 -m json.tool

echo ""
echo "✅ Test complete!"

