import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 30000, // 30 second timeout
});

/**
 * Evaluates an interview and returns candidate/interviewer assessment
 * @param {Object} payload - Evaluation request payload
 * @returns {Promise} Response with candidate and optional interviewer evaluation
 */
export async function evaluateInterview(payload) {
  try {
    console.log("Sending evaluation request to:", `${API_BASE_URL}/api/v1/evaluate/full`);
    console.log("Payload:", payload);

    const res = await apiClient.post("/api/v1/evaluate/full", payload);

    console.log("Evaluation response received:", res.data);
    return res.data;
  } catch (err) {
    console.error("Error evaluating interview:", err);

    // Provide detailed error message
    if (err.response) {
      // Server responded with error status
      const message = err.response.data?.message || err.response.statusText || "Server error";
      throw new Error(`API Error (${err.response.status}): ${message}`);
    } else if (err.request) {
      // Request made but no response
      throw new Error("No response from server. Make sure the backend is running on http://localhost:8080");
    } else {
      // Error in request setup
      throw new Error(err.message || "Failed to evaluate interview");
    }
  }
}

/**
 * Health check endpoint
 */
export async function checkHealth() {
  try {
    const res = await apiClient.get("/health");
    return res.data;
  } catch (err) {
    console.error("Health check failed:", err);
    throw err;
  }
}
