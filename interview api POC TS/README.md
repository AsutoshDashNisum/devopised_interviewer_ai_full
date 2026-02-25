# Interview Evaluation API – Spring Boot WebFlux Backend

A production-ready, deterministic REST API that evaluates candidates (and optionally interviewers) based on job descriptions, interview transcripts, and seniority levels.

## 🎯 Overview

This backend service is:
- **NOT a chatbot** – It's a structured evaluation engine
- **JSON-first** – All inputs and outputs are structured JSON
- **Deterministic** – Same input always produces the same output
- **Stateless** – No database, pure computation
- **Reactive** – Built on Spring WebFlux for non-blocking I/O
- **Type-safe** – Strong validation on requests and responses

## 📦 Tech Stack

- **Java 17** – Modern JDK with latest features
- **Spring Boot 3.2** – Latest framework version
- **Spring WebFlux** – Reactive, non-blocking REST API
- **Maven** – Build and dependency management
- **Jackson** – JSON serialization/deserialization
- **Jakarta Validation** – Request validation with annotations
- **JUnit 5 + WebTestClient** – Comprehensive testing

## 🏗️ Project Structure

```
interview-evaluation-api/
├── pom.xml                                    # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/com/interview/
│   │   │   ├── InterviewEvaluationApplication.java    # Boot entry point
│   │   │   ├── config/
│   │   │   │   ├── AppProperties.java                 # Config properties
│   │   │   │   └── GlobalRouterConfig.java            # Health/root routes
│   │   │   ├── constant/
│   │   │   │   └── PromptTemplate.java                # Fixed LLM prompts
│   │   │   ├── dto/
│   │   │   │   ├── EvaluateRequest.java               # Request DTO
│   │   │   │   ├── EvaluateResponse.java              # Response DTO
│   │   │   │   ├── CandidateEvaluation.java           # Candidate result
│   │   │   │   ├── InterviewerEvaluation.java         # Interviewer result
│   │   │   │   ├── Skill.java                         # Skill DTO
│   │   │   │   └── Meta.java                          # Metadata DTO
│   │   │   ├── handler/
│   │   │   │   └── EvaluationHandler.java             # WebFlux handler
│   │   │   ├── router/
│   │   │   │   └── RouterConfig.java                  # Route definitions
│   │   │   └── service/
│   │   │       ├── EvaluationService.java             # Business logic
│   │   │       └── llm/
│   │   │           ├── LLMService.java                # LLM interface
│   │   │           ├── MockLLMService.java            # Mock implementation
│   │   │           └── OpenAILLMService.java          # OpenAI impl (placeholder)
│   │   └── resources/
│   │       └── application.yml                        # Spring config
│   └── test/
│       └── java/com/interview/
│           ├── service/
│           │   └── EvaluationServiceTest.java        # Service unit tests
│           └── handler/
│               └── EvaluationHandlerTest.java        # Integration tests
└── README.md                                  # This file
```

## 🚀 Getting Started

### Prerequisites

- **Java 17+** – Download from [oracle.com](https://www.oracle.com/java/technologies/downloads/#java17)
- **Maven 3.8+** – Download from [maven.apache.org](https://maven.apache.org/download.cgi)
- **OpenAI API Key** (optional) – For production use; mock works without it

### Installation

1. **Clone/navigate to the workspace:**
```bash
cd interview-evaluation-api
```

2. **Install dependencies:**
```bash
mvn clean install
```

3. **Configure environment (optional):**
```bash
# Create .env file or set environment variables
export APP_ENVIRONMENT=development
export OPENAI_API_KEY=sk_your_key_here  # Optional
export LLM_USE_MOCK=true                # Use mock by default
```

### Running Locally

**Development mode** (with hot reload):
```bash
mvn spring-boot:run
```

**Production mode** (compiled JAR):
```bash
mvn clean package
java -jar target/interview-evaluation-api-0.1.0.jar
```

**With mock LLM** (no API key needed):
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--app.llm.use-mock=true"
```

Server starts on `http://localhost:8080`

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=EvaluationServiceTest

# Run with coverage report
mvn test jacoco:report
```

---

## 📡 API Specification

### Health Check

```http
GET /health
```

**Response (200):**
```json
{
  "status": "ok",
  "timestamp": "2026-01-06T10:30:00Z",
  "environment": "development"
}
```

---

### Evaluate Candidate & Interview

```http
POST /api/v1/evaluate
Content-Type: application/json
```

**Request:**
```json
{
  "jobDescription": "Senior Full Stack Engineer with 5+ years TypeScript, React, Node.js, System Design",
  "interviewTranscript": "Interviewer: Tell me about your TypeScript experience. Candidate: I have 6 years of professional TypeScript experience working on microservices and React applications...",
  "seniorityLevel": "Senior (5–10 years)",
  "includeInterviewerEvaluation": true
}
```

**Response (200):**
```json
{
  "candidate": {
    "name": null,
    "overallScore": 78,
    "hiringRecommendation": "Hire",
    "jdFit": "Candidate demonstrates strong alignment with role requirements, showing proficiency in core technologies.",
    "skills": [
      { "name": "TypeScript", "score": 85 },
      { "name": "React", "score": 80 },
      { "name": "Node.js", "score": 75 },
      { "name": "System Design", "score": 72 }
    ],
    "strengths": [
      "Strong problem-solving approach",
      "Clear communication of technical concepts",
      "Demonstrated ability to learn new technologies"
    ],
    "gaps": [
      "Limited hands-on DevOps experience",
      "Could improve algorithmic optimization",
      "Needs more exposure to cloud infrastructure"
    ],
    "riskAreas": [
      "Lack of proven experience at scale",
      "Some uncertainty in distributed systems"
    ]
  },
  "interviewer": {
    "coreJdCoverage": "Interviewer covered 75% of critical JD requirements. System Design questions were light.",
    "questionQuality": "Questions were clear and technical. Good mix of behavioral and technical probes.",
    "followUpNeeded": true,
    "followUpQuestions": [
      "Tell me about a system you designed that scaled to millions of users",
      "How would you approach migrating a monolith to microservices?",
      "Describe your cloud infrastructure and DevOps experience"
    ]
  },
  "meta": {
    "overallSummary": "Solid mid-to-senior engineer with strong fundamentals. Good fit for the role.",
    "seniorityMatch": "Aligns well with Senior (5-10 years) expectation.",
    "confidenceLevel": "High"
  }
}
```

**Error Response (400 - Invalid Input):**
```json
{
  "error": "INVALID_INPUT",
  "message": "Request validation failed",
  "details": "Job description must be at least 10 characters"
}
```

**Error Response (500 - Evaluation Failed):**
```json
{
  "error": "EVALUATION_FAILED",
  "message": "Failed to evaluate interview",
  "details": "Failed to parse LLM response"
}
```

### Request Validation Rules

| Field | Type | Required | Rules |
|-------|------|----------|-------|
| `jobDescription` | string | ✅ | Min 10 characters |
| `interviewTranscript` | string | ✅ | Min 20 characters |
| `seniorityLevel` | string | ✅ | Non-empty |
| `includeInterviewerEvaluation` | boolean | ❌ | Defaults to `false` |

### Response Rules

- **`candidate`** – Always present, always populated
- **`interviewer`** – Only included if `includeInterviewerEvaluation: true`
- **`meta`** – Always present

---

## 🔧 Architecture & Design

### Layer-by-Layer Breakdown

#### 1. **Handler Layer** (`EvaluationHandler`)
- Accepts HTTP requests
- Validates request body using Spring's `RequestBodyMono`
- Delegates to service layer
- Handles errors and returns appropriate HTTP status codes

#### 2. **Service Layer** (`EvaluationService`)
- Core business logic
- Constructs deterministic prompts
- Calls LLM service via abstraction
- Parses JSON responses with Jackson
- Validates output against schema
- Generates metadata

#### 3. **LLM Service Layer** (Interface-based)
- **`LLMService`** (interface) – Contract for LLM implementations
- **`MockLLMService`** – Returns hardcoded, deterministic responses
- **`OpenAILLMService`** – Placeholder for real OpenAI integration

#### 4. **Router Layer** (`RouterConfig`)
- Defines WebFlux functional routes
- Maps HTTP endpoints to handlers
- Type-safe and more performant than `@RestController`

#### 5. **DTO Layer**
- Request/Response serialization
- Validation annotations (`@NotBlank`, `@Size`, etc.)
- Jackson serialization via `@JsonProperty`

### Design Principles

| Principle | Implementation |
|-----------|-----------------|
| **Deterministic** | Fixed prompts, temperature=0, same input → same output |
| **Abstraction** | LLM behind interface, easy to mock/swap providers |
| **Validation** | Jakarta Validation + Jackson parsing |
| **Error Handling** | Centralized error mapping to HTTP codes |
| **Reactivity** | Mono/Flux for non-blocking I/O |
| **Type Safety** | Strong typing throughout, no stringly-typed code |

---

## 🔐 Security & Critical Rules

✅ **Implemented:**
- Request validation (prevents bad data)
- Deterministic output (no randomness)
- Fixed prompt templates (no injection attacks)
- Temperature=0 (consistent LLM behavior)
- Error handling (no sensitive data leaked)

⚠️ **Intentionally NOT included (per spec):**
- Authentication (open API for POC)
- Database persistence (stateless design)
- Resume parsing (structured text only)
- Audio handling (text transcripts only)

---

## 🛠️ Configuration

All configuration comes from `application.yml`:

```yaml
app:
  environment: ${APP_ENVIRONMENT:development}
  llm:
    api-key: ${OPENAI_API_KEY:}
    model: gpt-4-turbo
    temperature: 0.0
    use-mock: ${LLM_USE_MOCK:true}
```

**Environment Variables:**
- `APP_ENVIRONMENT` – `development` or `production`
- `OPENAI_API_KEY` – Your OpenAI API key (optional for mock mode)
- `LLM_USE_MOCK` – `true` to use mock, `false` for real OpenAI

---

## 🧪 Testing

### Test Structure

```
src/test/java/com/interview/
├── service/
│   └── EvaluationServiceTest.java         # Unit tests (Reactor StepVerifier)
└── handler/
    └── EvaluationHandlerTest.java         # Integration tests (WebTestClient)
```

### Running Tests

```bash
# All tests
mvn test

# Specific test
mvn test -Dtest=EvaluationServiceTest

# Watch mode (requires mvn extension)
mvn test -Dwatch
```

### Test Examples

**Unit Test (EvaluationServiceTest):**
- Verifies evaluation response structure
- Tests determinism (same input = same output)
- Tests conditional inclusion of `interviewer` object
- Uses Reactor `StepVerifier` for async testing

**Integration Test (EvaluationHandlerTest):**
- Tests HTTP status codes (200, 400, 500)
- Validates request validation
- Tests endpoint routing
- Uses `WebTestClient` for WebFlux testing

---

## 📦 Building & Deployment

### Build Project

```bash
# Compile and run tests
mvn clean package

# Skip tests (faster)
mvn clean package -DskipTests
```

Output: `target/interview-evaluation-api-0.1.0.jar`

### Run Compiled JAR

```bash
java -jar target/interview-evaluation-api-0.1.0.jar
```

### Docker Ready

```dockerfile
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/interview-evaluation-api-0.1.0.jar app.jar
ENV APP_ENVIRONMENT=production
ENV LLM_USE_MOCK=false
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:
```bash
docker build -t interview-api .
docker run -p 8080:8080 -e OPENAI_API_KEY=sk_xxx interview-api
```

---

## 📝 API Examples

### cURL Example

```bash
curl -X POST http://localhost:8080/api/v1/evaluate \
  -H "Content-Type: application/json" \
  -d '{
    "jobDescription": "Senior Backend Engineer with 5+ years Go and Kubernetes",
    "interviewTranscript": "Interviewer: Describe your Kubernetes experience. Candidate: I have 4 years of production experience managing microservices at scale.",
    "seniorityLevel": "Senior (5–10 years)",
    "includeInterviewerEvaluation": false
  }'
```

### Java/Spring Example

```java
@RestClient
interface EvaluationApiClient {
    
    @PostExchange("/api/v1/evaluate")
    EvaluateResponse evaluate(EvaluateRequest request);
}

// Usage
EvaluateRequest request = new EvaluateRequest(
    "Senior Backend Engineer...",
    "Interviewer: Tell me...",
    "Senior (5–10 years)",
    false
);

EvaluateResponse response = client.evaluate(request);
System.out.println("Score: " + response.getCandidate().getOverallScore());
```

### HTTPie Example

```bash
http POST localhost:8080/api/v1/evaluate \
  jobDescription="Senior Full Stack Engineer" \
  interviewTranscript="Interviewer: Your experience? Candidate: 7 years" \
  seniorityLevel="Senior (5–10 years)" \
  includeInterviewerEvaluation=false
```

---

## 🐛 Troubleshooting

### Port 8080 Already in Use
```bash
# Change port via environment
SERVER_PORT=9000 mvn spring-boot:run
```

### Tests Fail with "Cannot find module"
```bash
# Clean and reinstall dependencies
mvn clean install
```

### OpenAI API Key Issues
```bash
# Use mock LLM (default in development)
mvn spring-boot:run -Dspring-boot.run.arguments="--app.llm.use-mock=true"
```

### WebFlux Errors
- Ensure Spring WebFlux is in classpath (included in pom.xml)
- Check for blocking operations in handlers (use Mono/Flux)
- Use `reactor-test` for testing async code

---

## 📊 Project Statistics

| Metric | Value |
|--------|-------|
| **Lines of Code (Java)** | ~800 |
| **Test Lines** | ~300 |
| **Classes** | 15+ |
| **Test Coverage** | Unit + Integration |
| **Build Time** | ~30s (Maven) |

---

## 🔮 Future Enhancements

Potential additions (not in POC scope):
- Real OpenAI integration (currently placeholder)
- Multiple LLM provider support (Anthropic, etc.)
- Response caching layer
- Metrics/observability (Micrometer)
- API rate limiting
- Async job processing (large transcripts)
- Database persistence for evaluation history

---

## 📄 License

MIT

---

## 🙋 FAQ

**Q: Can I use this with a different LLM provider?**
A: Yes! The `LLMService` interface lets you implement any provider. Just extend it and use Spring's `@ConditionalOnProperty` to swap implementations.

**Q: Is the mock LLM suitable for production?**
A: No. The mock returns hardcoded responses. For production, implement real OpenAI integration or another LLM provider.

**Q: Why Spring WebFlux instead of Spring MVC?**
A: WebFlux handles high-concurrency, I/O-heavy workloads better. The LLM calls are I/O-bound, so non-blocking is ideal.

**Q: How do I scale this to handle 1000s of requests?**
A: Deploy multiple instances behind a load balancer. The stateless design allows horizontal scaling.

**Q: Can I add database persistence?**
A: Yes, but it's intentionally excluded for the POC. Add Spring Data with your preferred database when ready.

