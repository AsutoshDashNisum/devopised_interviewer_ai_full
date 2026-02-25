export default function InterviewerEvaluation({ data }) {
  if (!data) return null;

  // Convert overall score from /100 to /10 if needed
  const getDisplayScore = (score) => {
    if (!score) return 0;
    // If score is > 10, divide by 10 to convert from /100 to /10
    return score > 10 ? Math.round(score / 10) : score;
  };

  const displayScore = getDisplayScore(data.overallScore);

  return (
    <div className="card">
      <div className="evaluation-header">
        <div>
          <h2>Interviewer Evaluation</h2>
          <p className="subtitle">Interview conduct and effectiveness assessment</p>
        </div>
      </div>

      <div className="score-display-new">
        <div className="score-item">
          <p className="score-label">Overall Score</p>
          <p className="score-value-large">{displayScore}</p>
        </div>
        <div className="score-item">
          <p className="score-label">Effectiveness</p>
          <p className="effectiveness-badge">
            {data.effectiveness || data.performanceRating || "Pending"}
          </p>
        </div>
      </div>

      {data.summary && (
        <div className="info-box">
          <h4>Summary</h4>
          <p>{data.summary}</p>
        </div>
      )}

      {/* Interviewer Score Metrics */}
      {(data.questionQuality || data.communicationClarity || data.biasRisk) && (
        <div className="scores-grid">
          {data.questionQuality && (
            <div className="score-metric">
              <h4>Question Quality</h4>
              <div className="metric-score">{data.questionQuality}/10</div>
              <p>Quality and relevance of questions asked</p>
            </div>
          )}
          {data.communicationClarity && (
            <div className="score-metric">
              <h4>Communication Clarity</h4>
              <div className="metric-score">{data.communicationClarity}/10</div>
              <p>How clearly the interviewer communicated</p>
            </div>
          )}
          {data.biasRisk && (
            <div className="score-metric">
              <h4>Bias Risk Assessment</h4>
              <div className="metric-score" style={{ textTransform: 'uppercase', fontSize: '20px', color: getBiasRiskColor(data.biasRisk) }}>
                {data.biasRisk}
              </div>
              <p>Potential bias risks identified</p>
            </div>
          )}
        </div>
      )}

      {data.strengths && data.strengths.length > 0 && (
        <div className="assessment-box">
          <div className="assessment-header success">
            <span>✓</span>
            <h4>Strengths</h4>
          </div>
          <ul className="assessment-list">
            {data.strengths.map((strength, idx) => (
              <li key={idx}>{strength}</li>
            ))}
          </ul>
        </div>
      )}

      {(data.improvements || data.areasForImprovement || data.recommendations) &&
        (data.improvements || data.areasForImprovement || data.recommendations).length > 0 && (
          <div className="assessment-box">
            <div className="assessment-header warning">
              <span>📈</span>
              <h4>Areas for Improvement</h4>
            </div>
            <ul className="assessment-list">
              {(data.improvements || data.areasForImprovement || data.recommendations).map(
                (area, idx) => (
                  <li key={idx}>{area}</li>
                )
              )}
            </ul>
          </div>
        )}
    </div>
  );
}

// Helper function to get color based on bias risk level
function getBiasRiskColor(risk) {
  const riskLevel = (risk || "").toLowerCase();
  switch (riskLevel) {
    case "low":
      return "#16a34a"; // Green
    case "medium":
      return "#d97706"; // Orange
    case "high":
      return "#dc2626"; // Red
    default:
      return "#64748b"; // Gray
  }
}
