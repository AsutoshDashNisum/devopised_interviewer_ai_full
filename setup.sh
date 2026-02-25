#!/bin/bash
# Interview Evaluation System - Complete Setup Script for macOS/Linux
# This script sets up and starts both backend and frontend

set -e

echo ""
echo "====================================================="
echo "  Interview Evaluation System - Complete Setup"
echo "====================================================="
echo ""

# Check for Java
echo "Checking for Java 17+..."
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java 17+ from https://www.oracle.com/java/technologies/downloads/"
    exit 1
fi
JAVA_VERSION=$(java -version 2>&1 | grep -oP 'version "\K[^"]*')
echo "Found Java $JAVA_VERSION"
echo ""

# Check for Maven
echo "Checking for Maven..."
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH"
    echo "Please install Maven from https://maven.apache.org/download.cgi"
    exit 1
fi
echo "Maven is installed"
echo ""

# Check for Node.js
echo "Checking for Node.js..."
if ! command -v node &> /dev/null; then
    echo "ERROR: Node.js is not installed or not in PATH"
    echo "Please install Node.js from https://nodejs.org"
    exit 1
fi
NODE_VERSION=$(node --version)
echo "Found Node.js $NODE_VERSION"
echo ""

# Setup Backend
echo "====================================================="
echo "Setting up Backend (Spring Boot)..."
echo "====================================================="
cd "interview api POC TS"

if [ ! -f ".env" ]; then
    echo "Creating .env from template..."
    if [ -f ".env.example" ]; then
        cp .env.example .env
        echo ".env file created"
    else
        echo "WARNING: .env.example not found"
    fi
else
    echo ".env already exists"
fi

echo "Installing backend dependencies..."
mvn clean install -q
if [ $? -ne 0 ]; then
    echo "ERROR: Failed to install backend dependencies"
    exit 1
fi
echo "Backend dependencies installed successfully"
echo ""

cd ..

# Setup Frontend
echo "====================================================="
echo "Setting up Frontend (React + Vite)..."
echo "====================================================="
cd "interview ui poc ts/my-react-app"

if [ ! -f ".env.local" ]; then
    echo "Creating .env.local from template..."
    if [ -f ".env.example" ]; then
        cp .env.example .env.local
        echo ".env.local file created"
    else
        echo "WARNING: .env.example not found"
    fi
else
    echo ".env.local already exists"
fi

echo "Installing frontend dependencies..."
npm install
if [ $? -ne 0 ]; then
    echo "ERROR: Failed to install frontend dependencies"
    exit 1
fi
echo "Frontend dependencies installed successfully"
echo ""

cd ../..

# Summary
echo "====================================================="
echo "Setup Complete!"
echo "====================================================="
echo ""
echo "To start the application:"
echo ""
echo "1. Start Backend (in one terminal):"
echo "   cd \"interview api POC TS\""
echo "   mvn spring-boot:run"
echo ""
echo "2. Start Frontend (in another terminal):"
echo "   cd \"interview ui poc ts/my-react-app\""
echo "   npm run dev"
echo ""
echo "Then open your browser to: http://localhost:5173"
echo ""
echo "Backend API: http://localhost:8080"
echo "Health check: curl http://localhost:8080/health"
echo ""

