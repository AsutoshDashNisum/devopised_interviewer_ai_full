#!/bin/bash
# Start Backend API
# This script runs the Interview Evaluation API on port 8080

echo ""
echo "====================================================="
echo "  Interview Evaluation API - Backend"
echo "====================================================="
echo ""
echo "Starting backend server on http://localhost:8080"
echo ""

cd "interview api POC TS"
mvn spring-boot:run

