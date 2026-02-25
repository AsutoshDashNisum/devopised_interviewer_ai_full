import { useState } from "react";
import CandidateEvaluation from "./CandidateEvaluation";
import InterviewerEvaluation from "./InterviewerEvaluation";

export default function EvaluationPage({ data, onReset }) {
  const [evaluationType, setEvaluationType] = useState(null);

  if (!data) return null;

  // Show evaluation type selector if not chosen yet
  if (!evaluationType) {
    return (
      <div className="card">
        <h2>Select Evaluation Type</h2>
        <p className="subtitle">Choose what you want to review</p>

        <div className="evaluation-type-selector">
          <div
            className="evaluation-type-option"
            onClick={() => setEvaluationType("candidate")}
          >
            <div className="option-icon">👤</div>
            <h3>Candidate Only</h3>
            <p>Review candidate assessment, skills, strengths, and gaps</p>
            <button className="primary-btn">View Candidate</button>
          </div>

          <div
            className="evaluation-type-option"
            onClick={() => setEvaluationType("full")}
          >
            <div className="option-icon">👥</div>
            <h3>Full Evaluation</h3>
            <p>Review both candidate and interviewer assessments</p>
            <button className="primary-btn">View Full Evaluation</button>
          </div>
        </div>

        <div className="footer-actions">
          <button className="secondary-btn" onClick={onReset}>
            ← New Evaluation
          </button>
        </div>
      </div>
    );
  }

  // Show selected evaluation type
  return (
    <div className="results-container">
      <CandidateEvaluation data={data.candidate} />
      {evaluationType === "full" && data.interviewer && (
        <InterviewerEvaluation data={data.interviewer} />
      )}

      <div className="footer-actions">
        <button className="secondary-btn" onClick={() => setEvaluationType(null)}>
          ← Back to Selection
        </button>
        <button className="secondary-btn" onClick={onReset}>
          ← New Evaluation
        </button>
      </div>
    </div>
  );
}
