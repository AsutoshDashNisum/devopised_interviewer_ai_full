import { useState } from "react";
import EvaluationPage from "./EvaluationPage";
import { evaluateInterview } from "../services/evaluationService";

export default function EvaluationForm() {
  const [formData, setFormData] = useState({
    jobDescription: "",
    interviewTranscript: "",
    seniority: "",
    evaluateInterviewer: true
  });

  const [response, setResponse] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  if (response) {
    return <EvaluationPage data={response} onReset={() => setResponse(null)} />;
  }

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validate form
    if (!formData.jobDescription.trim()) {
      setError("Please enter a job description");
      return;
    }
    if (!formData.interviewTranscript.trim()) {
      setError("Please enter an interview transcript");
      return;
    }
    if (!formData.seniority) {
      setError("Please select a seniority level");
      return;
    }

    setLoading(true);
    setError(null);

    try {
      console.log("Form submission - calling evaluateInterview...");
      const res = await evaluateInterview(formData);
      console.log("Raw response from API:", res);

      // Transform the backend response to match component expectations
      const transformedResponse = {
        candidate: res.candidateEvaluation || res.candidate,
        interviewer: res.interviewerEvaluation || res.interviewer,
        status: res.status,
        evaluatedAt: res.evaluatedAt
      };

      console.log("Transformed response:", transformedResponse);
      setResponse(transformedResponse);
    } catch (err) {
      console.error("Error in handleSubmit:", err);
      setError(err.message || "Failed to evaluate interview. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className="card" onSubmit={handleSubmit}>
      <h2>New Evaluation</h2>
      <p className="subtitle">
        Input the job context and interview transcript to generate an AI assessment.
      </p>

      {error && <div className="error-message"><p>{error}</p></div>}
      {loading && <div className="loading"><div className="spinner"></div><p>Evaluating interview... This may take a moment.</p></div>}

      <div className="form-grid">
        <div className="form-group">
          <label>
            <span>📄</span> Job Description <span style={{color: 'red'}}>*</span>
          </label>
          <textarea
            name="jobDescription"
            placeholder="Paste the full job description here..."
            value={formData.jobDescription}
            onChange={e => setFormData({ ...formData, jobDescription: e.target.value })}
            disabled={loading}
          />
        </div>

        <div className="form-group">
          <label>
            <span>📋</span> Interview Transcript <span style={{color: 'red'}}>*</span>
          </label>
          <textarea
            name="interviewTranscript"
            placeholder="Paste the interview transcript here..."
            value={formData.interviewTranscript}
            onChange={e => setFormData({ ...formData, interviewTranscript: e.target.value })}
            disabled={loading}
          />
        </div>
      </div>

      <div className="footer">
        <div className="form-group small">
          <label>Seniority Level <span style={{color: 'red'}}>*</span></label>
          <select
            name="seniority"
            value={formData.seniority}
            onChange={e => setFormData({ ...formData, seniority: e.target.value })}
            disabled={loading}
          >
            <option value="">Select level</option>
            <option value="junior">Junior (0–2 years)</option>
            <option value="mid">Mid (2–5 years)</option>
            <option value="senior">Senior (5–10 years)</option>
          </select>
        </div>

        <div className="actions">
          <label className="checkbox">
            <input
              type="checkbox"
              checked={formData.evaluateInterviewer}
              onChange={() =>
                setFormData({ ...formData, evaluateInterviewer: !formData.evaluateInterviewer })
              }
              disabled={loading}
            />
            Evaluate Interviewer
          </label>

          <button
            type="submit"
            className="primary-btn"
            disabled={loading}
          >
            {loading ? "Evaluating..." : "Evaluate Interview"}
          </button>
        </div>
      </div>
    </form>
  );
}
