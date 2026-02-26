# 📦 Project Run Guide

## Prerequisites

1. **Java Development Kit (JDK) 17**
   - Install from the official Oracle or OpenJDK distribution.
   - Verify with `java -version` (should show `17`).
2. **Maven 3.9+**
   - Install and ensure `mvn -v` works.
3. **Node.js 20+** (includes npm)
   - Verify with `node -v` and `npm -v`.
4. **Git** (optional, for cloning the repo).

## Backend (Spring Boot API)

The backend lives in `interview api POC TS`.

1. Open a terminal and navigate to the backend directory:
   ```bash
   cd "c:/Users/adash/Desktop/interviewer demo/Interview-eavluation-full/interview api POC TS"
   ```
2. Install Maven dependencies and build the project:
   ```bash
   mvn clean install
   ```
   - This will download all required libraries.
3. Run the API (the provided script does this for you):
   ```bash
   ./start-backend.sh   # on Git Bash / WSL
   # or on Windows CMD
   start-backend.bat
   ```
   - The server starts on **http://localhost:8080**.
   - You should see console output confirming `Started InterviewEvaluationApiApplication`.

## Frontend (React UI)

The frontend lives in `interview ui poc ts/my-react-app`.

1. Open another terminal and navigate to the React app directory:
   ```bash
   cd "c:/Users/adash/Desktop/interviewer demo/Interview-eavluation-full/interview ui poc ts/my-react-app"
   ```
2. Install npm dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm run dev
   ```
   - Vite will launch the UI at **http://localhost:5173**.

## Quick One‑Click Start (Windows)

A helper batch file `run-all.bat` is provided. It opens two separate command windows – one for the backend and one for the frontend.

```bat
@echo off
start "Backend" cmd /k "cd /d \"c:\Users\adash\Desktop\interviewer demo\Interview-eavluation-full\interview api POC TS\" && mvn spring-boot:run"
start "Frontend" cmd /k "cd /d \"c:\Users\adash\Desktop\interviewer demo\Interview-eavluation-full\interview ui poc ts\my-react-app\" && npm run dev"
```

Run it by double‑clicking `run-all.bat` or executing `run-all.bat` from a command prompt.

## Troubleshooting

- **Port conflicts**: If port 8080 or 5173 is already in use, stop the conflicting process or change the ports in `application.yml` (backend) and `vite.config.ts` (frontend).
- **Maven build failures**: Ensure Java 17 is active (`java -version`). Clean the repo with `mvn clean` and retry.
- **Node modules missing**: Delete `node_modules` and run `npm install` again.
- **Environment variables**: If the API expects a JWT secret, create a `.env` file in the backend root:
  ```
  JWT_SECRET=your‑very‑secure‑secret
  ```
  The Spring Boot config can read it via `${JWT_SECRET}`.

## Verification

1. Open a browser and visit **http://localhost:5173** – you should see the Interview Evaluation UI.
2. Use a tool like `curl` or Postman to hit **http://localhost:8080/actuator/health** – it should return `{"status":"UP"}`.

You now have the full stack running locally! 🎉
