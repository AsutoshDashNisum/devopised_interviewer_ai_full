@echo off
REM Test script for Interview Evaluation API - Windows batch file

echo.
echo ==========================================
echo Interview Evaluation API - Test Script
echo ==========================================
echo.

REM Test 1: Health Check
echo Test 1: Health Check
echo GET http://localhost:8080/health
powershell -Command "Invoke-WebRequest -Uri 'http://localhost:8080/health' -Method GET | ConvertTo-Json"
echo.
echo.

REM Test 2: API Info
echo Test 2: API Info
echo GET http://localhost:8080/
powershell -Command "Invoke-WebRequest -Uri 'http://localhost:8080/' -Method GET | ConvertTo-Json"
echo.
echo.

REM Test 3: Evaluate Candidate (Valid Request)
echo Test 3: Evaluate Candidate
echo POST http://localhost:8080/api/v1/evaluate
powershell -Command @"
`$body = '{
  \"jobDescription\": \"Senior Full Stack Engineer with 5+ years TypeScript, React, Node.js\",
  \"interviewTranscript\": \"Interviewer: Tell me about your TypeScript experience. Candidate: I have 6 years of professional TypeScript experience working on microservices and React applications.\",
  \"seniorityLevel\": \"Senior (5-10 years)\",
  \"includeInterviewerEvaluation\": false
}'

`$response = Invoke-WebRequest -Uri 'http://localhost:8080/api/v1/evaluate' -Method POST -ContentType 'application/json' -Body `$body
`$response.Content
"@
echo.
echo.

REM Test 4: Error Case - Missing Job Description
echo Test 4: Error Case - Missing Job Description
echo POST http://localhost:8080/api/v1/evaluate (missing jobDescription - should return 400)
powershell -Command @"
`$body = '{
  \"interviewTranscript\": \"This is a valid transcript with enough content to pass validation\",
  \"seniorityLevel\": \"Senior (5-10 years)\",
  \"includeInterviewerEvaluation\": false
}'

try {
  `$response = Invoke-WebRequest -Uri 'http://localhost:8080/api/v1/evaluate' -Method POST -ContentType 'application/json' -Body `$body
  `$response.Content
} catch {
  `$_.Exception.Response.StatusCode.value__
  `$_.Exception.Response.Content.ReadAsStringAsync().Result
}
"@
echo.
echo.

echo ==========================================
echo All tests completed!
echo ==========================================
pause

