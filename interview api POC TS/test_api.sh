#!/bin/bash
# Test script for Interview Evaluation API

echo "=========================================="
echo "Interview Evaluation API - Test Script"
echo "=========================================="
echo ""

# Test 1: Health Check
echo "Test 1: Health Check"
echo "GET http://localhost:8080/health"
curl -X GET http://localhost:8080/health
echo ""
echo ""

# Test 2: API Info
echo "Test 2: API Info"
echo "GET http://localhost:8080/"
curl -X GET http://localhost:8080/
echo ""
echo ""

# Test 3: Evaluate Candidate (Valid)
echo "Test 3: Evaluate Candidate"
echo "POST http://localhost:8080/api/v1/evaluate"
curl -X POST http://localhost:8080/api/v1/evaluate \
  -H "Content-Type: application/json" \
  -d '{
    "jobDescription": "Senior Full Stack Engineer with 5+ years TypeScript, React, Node.js",
    "interviewTranscript": "Interviewer: Tell me about your TypeScript experience. Candidate: I have 6 years of professional TypeScript experience working on microservices and React applications.",
    "seniorityLevel": "Senior (5-10 years)",
    "includeInterviewerEvaluation": false
  }'
echo ""
echo ""

# Test 4: Evaluate with Interviewer
echo "Test 4: Evaluate with Interviewer"
echo "POST http://localhost:8080/api/v1/evaluate (with interviewer evaluation)"
curl -X POST http://localhost:8080/api/v1/evaluate \
  -H "Content-Type: application/json" \
  -d '{
    "jobDescription": "Senior Backend Engineer with experience in Java, Spring Boot, microservices",
    "interviewTranscript": "Interviewer: Can you describe your microservices experience? Candidate: Yes, I have designed and deployed microservices systems handling millions of requests per day. I use Spring Boot with Spring Cloud for service discovery.",
    "seniorityLevel": "Senior (5-10 years)",
    "includeInterviewerEvaluation": true
  }'
echo ""
echo ""

# Test 5: Error - Missing Job Description
echo "Test 5: Error Case - Missing Job Description"
echo "POST http://localhost:8080/api/v1/evaluate (missing jobDescription)"
curl -X POST http://localhost:8080/api/v1/evaluate \
  -H "Content-Type: application/json" \
  -d '{
    "interviewTranscript": "This is a valid transcript with enough content",
    "seniorityLevel": "Senior (5-10 years)",
    "includeInterviewerEvaluation": false
  }'
echo ""
echo ""

echo "=========================================="
echo "All tests completed!"
echo "=========================================="

