# 🚀 Complete Setup & Troubleshooting Guide

## ✅ Project Build Status

Your project has been successfully built:
- ✅ JAR file created: `target/interview-evaluation-api-0.1.0.jar`
- ✅ All classes compiled
- ✅ Ready to run

---

## 🔧 How to Start the Server

### **Option 1: Run Using Java (RECOMMENDED)**

Open **PowerShell** or **Command Prompt** and run:

```bash
cd "C:\Users\adash\Desktop\interview api POC TS"
java -jar target/interview-evaluation-api-0.1.0.jar
```

**Expected Output:**
```
  ╭─────────────────────────────────────────────────────────────────────────────╮
  │  Interview Evaluation API                                                   │
  │  Environment: development                                                   │
  │  Port: 8080                                                                 │
  │  API Prefix: /api/v1                                                        │
  │  Using Mock LLM Service                                                     │
  ╰─────────────────────────────────────────────────────────────────────────────╯
     Ready to accept requests at http://localhost:8080
```

Keep this terminal window open while testing.

### **Option 2: Run Using Maven**

```bash
cd "C:\Users\adash\Desktop\interview api POC TS"
mvn spring-boot:run
```

---

## ✅ Verify Server is Running

In a **new PowerShell/Command Prompt window**, test if the server is responding:

```bash
curl http://localhost:8080/health
```

**Expected Response:**
```json
{
  "status": "ok",
  "timestamp": "2026-01-06T10:30:00Z",
  "environment": "development"
}
```

If this works, your server is running correctly ✅

---

## 🔴 If Server Won't Start

### **Problem 1: "Port 8080 already in use"**

Another process is using port 8080. Kill it:

```bash
# PowerShell
Get-Process | Where-Object {$_.ProcessName -like "*java*"} | Stop-Process -Force

# Or manually change the port:
java -jar target/interview-evaluation-api-0.1.0.jar --server.port=9000
```

Then update Postman: Change `{{base_url}}` to `http://localhost:9000`

### **Problem 2: "Java command not found"**

Java isn't installed or not in PATH. Download from [oracle.com](https://www.oracle.com/java/technologies/downloads/#java17)

Verify Java is installed:
```bash
java -version
```

---

## 📮 Setup Postman

### **Step 1: Import Collection**

1. Open **Postman** (download from [postman.com](https://www.postman.com/downloads/) if not installed)
2. Click **File → Import**
3. Select: `Interview_Evaluation_API.postman_collection.json`
4. Click **Import**

### **Step 2: Verify Environment Variable**

1. Look for **Environments** dropdown (top right of Postman)
2. Click on it and select the environment (or create one)
3. Make sure `base_url` is set to `http://localhost:8080`

If no environment exists:
1. Click **Environments** button (top left)
2. Click **Create New Environment**
3. Add variable: `base_url` = `http://localhost:8080`
4. Click **Save**

### **Step 3: Test a Simple Request**

1. Click **Health & Status → Health Check**
2. Click the blue **Send** button
3. You should see a **200 OK** response with status "ok"

If this works, proceed to main evaluation requests ✅

---

## 🧪 Test the API with Postman

### **Test 1: Evaluate Candidate (Easiest)**

1. Click **Evaluation → Evaluate - Candidate Only**
2. Click **Send**
3. Wait ~1 second for response
4. You should see **200 OK** with evaluation results

**Expected Response Structure:**
```json
{
  "candidate": {
    "name": null,
    "overallScore": 75,
    "hiringRecommendation": "Hire",
    ...
  },
  "meta": {
    "overallSummary": "...",
    "seniorityMatch": "...",
    "confidenceLevel": "High"
  }
}
```

### **Test 2: Evaluate with Interviewer**

1. Click **Evaluation → Evaluate - Candidate + Interviewer**
2. Click **Send**
3. Response should include `interviewer` object with follow-up questions

### **Test 3: Test Error Handling**

1. Click **Error Cases → Error - Missing Job Description**
2. Click **Send**
3. Should get **400 BAD_REQUEST** with error message

---

## 🐛 Common Postman Issues & Fixes

### **Issue: "Could not get any response"**

**Cause:** Server is not running

**Fix:**
```bash
# In a new terminal, start the server:
cd "C:\Users\adash\Desktop\interview api POC TS"
java -jar target/interview-evaluation-api-0.1.0.jar

# Then try request again in Postman
```

### **Issue: "Status 0" or "Connection refused"**

**Cause:** Wrong URL or port

**Fix:**
1. Click **Variables** in Postman
2. Verify `base_url` = `http://localhost:8080` (no trailing slash)
3. Test health check: `http://localhost:8080/health`

### **Issue: "500 Internal Server Error"**

**Cause:** Server is running but something failed

**Fix:**
1. Check the server terminal - look for error messages
2. Restart the server:
   ```bash
   # Kill the running server (Ctrl+C in the terminal)
   # Then restart:
   java -jar target/interview-evaluation-api-0.1.0.jar
   ```

### **Issue: "400 Bad Request" on valid request**

**Cause:** Request body malformed or JSON invalid

**Fix:**
1. Click the request in Postman
2. Check **Body** tab - should be raw JSON
3. Click **Beautify** to format JSON properly
4. Verify all required fields are present:
   - `jobDescription` (min 10 chars)
   - `interviewTranscript` (min 20 chars)
   - `seniorityLevel` (any non-empty string)
   - `includeInterviewerEvaluation` (true/false)

---

## ✅ Complete Working Setup Checklist

- [ ] Java 17+ installed (`java -version`)
- [ ] Project built successfully (`target/interview-evaluation-api-0.1.0.jar` exists)
- [ ] Server running (`java -jar target/interview-evaluation-api-0.1.0.jar`)
- [ ] Health check works (`curl http://localhost:8080/health`)
- [ ] Postman installed
- [ ] Collection imported into Postman
- [ ] Base URL set to `http://localhost:8080` in Postman
- [ ] Can send "Health Check" request successfully
- [ ] Can send "Evaluate - Candidate Only" request successfully

---

## 🎯 Step-by-Step Quick Start

**Time: ~2 minutes**

### **Terminal 1 (Server)**
```bash
cd "C:\Users\adash\Desktop\interview api POC TS"
java -jar target/interview-evaluation-api-0.1.0.jar
```

Wait for:
```
Ready to accept requests at http://localhost:8080
```

### **Terminal 2 (Test)**
```bash
# Test health check
curl http://localhost:8080/health

# Should return:
# {"status":"ok","timestamp":"...","environment":"development"}
```

### **Postman**
1. Open Postman
2. Import `Interview_Evaluation_API.postman_collection.json`
3. Click **Health & Status → Health Check**
4. Click **Send**
5. See **200 OK** ✅
6. Click **Evaluation → Evaluate - Candidate Only**
7. Click **Send**
8. See evaluation results ✅

---

## 📊 Test Each Endpoint

| Endpoint | Expected Status | Expected Time |
|----------|-----------------|----------------|
| GET /health | 200 OK | <100ms |
| GET / | 200 OK | <100ms |
| POST /api/v1/evaluate (valid) | 200 OK | 100-500ms |
| POST /api/v1/evaluate (missing field) | 400 BAD_REQUEST | <100ms |
| POST /api/v1/evaluate (field too short) | 400 BAD_REQUEST | <100ms |
| POST /api/v1/evaluate (invalid JSON) | 400 BAD_REQUEST | <100ms |

---

## 💾 Logs & Debugging

When server is running, you'll see logs like:

```
2026-01-06 10:30:00 - Received evaluation request
2026-01-06 10:30:00 - Request validated: seniority=Senior (5–10 years), includeInterviewer=false
2026-01-06 10:30:00 - Starting evaluation for seniority level: Senior (5–10 years)
2026-01-06 10:30:00 - MockLLMService: Evaluating prompt (mock mode - no API calls)
2026-01-06 10:30:00 - Candidate evaluation completed: score=75
```

**These are normal and indicate the API is working correctly.**

If you see error messages:
- Note the error message
- Check that your request has all required fields
- Verify field lengths (jobDescription ≥ 10 chars, transcript ≥ 20 chars)

---

## 🔗 URLs to Test

All these should work when server is running at `http://localhost:8080`:

**Direct Browser/cURL:**
```bash
curl http://localhost:8080/health
curl http://localhost:8080/
```

**Postman Collection:**
- All requests in the imported collection
- Edit `{{base_url}}` if your port is different

---

## ❓ Still Not Working?

### **Check These Things:**

1. **Is Java running?**
   ```bash
   tasklist | findstr java
   ```
   Should show: `java.exe` in the list

2. **Is port 8080 listening?**
   ```bash
   netstat -ano | findstr :8080
   ```
   Should show a connection on port 8080

3. **Can you reach the server?**
   ```bash
   curl http://localhost:8080/health
   ```
   Should return JSON, not "connection refused"

4. **Is Postman pointing to the right URL?**
   - Click **Variables** in Postman
   - Verify `base_url` = `http://localhost:8080`
   - No spaces, no trailing slash

5. **Is the request body valid JSON?**
   - Click **Body** tab in Postman request
   - Click **Beautify** button
   - All quotes should be straight ("), not curly ("")

---

## 📝 Next Steps

1. Start the server
2. Verify health check works
3. Test one evaluation request
4. Run all error case tests
5. Try different seniority levels
6. Check the response structure matches API contract

All requests should now work! 🎉

