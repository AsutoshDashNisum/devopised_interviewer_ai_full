@echo off
rem Run backend and frontend in separate windows
start "Backend" cmd /k "cd /d \"c:\Users\adash\Desktop\interviewer demo\Interview-eavluation-full\interview api POC TS\" && mvn spring-boot:run"
start "Frontend" cmd /k "cd /d \"c:\Users\adash\Desktop\interviewer demo\Interview-eavluation-full\interview ui poc ts\my-react-app\" && npm run dev"
