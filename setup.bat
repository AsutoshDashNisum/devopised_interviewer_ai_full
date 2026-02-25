@echo off
REM Interview Evaluation System - Complete Setup Script for Windows
REM This script sets up and starts both backend and frontend

setlocal enabledelayedexpansion

echo.
echo =====================================================
echo  Interview Evaluation System - Complete Setup
echo =====================================================
echo.

REM Check for Java
echo Checking for Java 17+...
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17+ from https://www.oracle.com/java/technologies/downloads/
    exit /b 1
)
for /f "tokens=2" %%i in ('java -version 2^>^&1 ^| findstr /R "version"') do set JAVA_VERSION=%%i
echo Found Java !JAVA_VERSION!
echo.

REM Check for Maven
echo Checking for Maven...
mvn -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven from https://maven.apache.org/download.cgi
    exit /b 1
)
echo Maven is installed
echo.

REM Check for Node.js
echo Checking for Node.js...
node --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Node.js is not installed or not in PATH
    echo Please install Node.js from https://nodejs.org
    exit /b 1
)
for /f %%i in ('node --version') do set NODE_VERSION=%%i
echo Found Node.js !NODE_VERSION!
echo.

REM Setup Backend
echo =====================================================
echo Setting up Backend (Spring Boot)...
echo =====================================================
cd "interview api POC TS"

if not exist ".env" (
    echo Creating .env from template...
    if exist ".env.example" (
        copy .env.example .env > nul
        echo .env file created
    ) else (
        echo WARNING: .env.example not found
    )
) else (
    echo .env already exists
)

echo Installing backend dependencies...
call mvn clean install -q
if errorlevel 1 (
    echo ERROR: Failed to install backend dependencies
    exit /b 1
)
echo Backend dependencies installed successfully
echo.

cd ..

REM Setup Frontend
echo =====================================================
echo Setting up Frontend (React + Vite)...
echo =====================================================
cd "interview ui poc ts\my-react-app"

if not exist ".env.local" (
    echo Creating .env.local from template...
    if exist ".env.example" (
        copy .env.example .env.local > nul
        echo .env.local file created
    ) else (
        echo WARNING: .env.example not found
    )
) else (
    echo .env.local already exists
)

echo Installing frontend dependencies...
call npm install
if errorlevel 1 (
    echo ERROR: Failed to install frontend dependencies
    exit /b 1
)
echo Frontend dependencies installed successfully
echo.

cd ..\..

REM Summary
echo =====================================================
echo Setup Complete!
echo =====================================================
echo.
echo To start the application:
echo.
echo 1. Start Backend (in one terminal):
echo    cd "interview api POC TS"
echo    mvn spring-boot:run
echo.
echo 2. Start Frontend (in another terminal):
echo    cd "interview ui poc ts\my-react-app"
echo    npm run dev
echo.
echo Then open your browser to: http://localhost:5173
echo.
echo Backend API: http://localhost:8080
echo Health check: curl http://localhost:8080/health
echo.
pause

