# 🚀 DevOps Guide - Interview Evaluation AI

This project has been fully "DevOps-ified" with Docker, Docker Compose, and GitHub Actions.

## 📦 1. Local Development (Docker)
You can now run the entire stack locally without installing Java or Node.js manually.

```bash
# Start all services
docker-compose up --build

# Run in background
docker-compose up -d
```

- **Backend**: [http://localhost:8080](http://localhost:8080)
- **Frontend**: [http://localhost:80](http://localhost:80)

## 🏗️ 2. CI/CD Pipeline (GitHub Actions)
Every time you push to `main` or `master`, GitHub will automatically:
1. Build your Backend Docker image.
2. Build your Frontend Docker image.
3. Push both images to your Docker Hub: `adash1515/`.

### 🔐 Setup Required in GitHub
Go to **Settings > Secrets and variables > Actions** and add:
- `DOCKERHUB_USERNAME`: `adash1515`
- `DOCKERHUB_TOKEN`: (The token you generated)

## ☁️ 3. Deployment (Cloud)
Since the images are now on Docker Hub, you can deploy to any cloud provider (Render, Railway, AWS, etc.) simply by pointing them to your Docker Hub repository.

- **Backend Image**: `adash1515/interview-backend:latest`
- **Frontend Image**: `adash1515/interview-frontend:latest`

## 🛠️ Files Created
- `Dockerfile` (Backend & Frontend): Multi-stage production builds.
- `.dockerignore`: Optimizes build speed by excluding unnecessary files.
- `docker-compose.yml`: Orchestrates both services for local testing.
- `.github/workflows/deploy.yml`: Automated CI/CD pipeline.
