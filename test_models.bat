@echo off
set API_KEY=AIzaSyAq3RDW2E0qSojTdSsUErgJuMNd0M27ALo

echo Testing gemini-1.5-flash...
curl -s -X POST -H "Content-Type: application/json" -d "{\"contents\": [{\"parts\":[{\"text\": \"hi\"}]}]}" "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=%API_KEY%" > test_flash.json
echo Testing gemini-1.5-pro...
curl -s -X POST -H "Content-Type: application/json" -d "{\"contents\": [{\"parts\":[{\"text\": \"hi\"}]}]}" "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro:generateContent?key=%API_KEY%" > test_pro.json
echo Testing gemini-1.0-pro...
curl -s -X POST -H "Content-Type: application/json" -d "{\"contents\": [{\"parts\":[{\"text\": \"hi\"}]}]}" "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=%API_KEY%" > test_pro_legacy.json
echo Done.
