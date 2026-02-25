export default function CandidateEvaluation({ data }) {
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
          <h2>Candidate Evaluation</h2>
          <p className="subtitle">Performance assessment and skill breakdown</p>
        </div>
      </div>

      <div className="score-display-new">
        <div className="score-item">
          <p className="score-label">Overall Score</p>
          <p className="score-value-large">{displayScore}</p>
        </div>
        <div className="score-item">
          <p className="score-label">Technical</p>
          <p className="score-value-large">{getDisplayScore(data.technicalScore)}</p>
        </div>
        <div className="score-item">
          <p className="score-label">Recommendation</p>
          <p className={`recommendation-badge ${(data.verdict || data.hiringRecommendation || "").toLowerCase().replace(/_/g, ' ')}`}>
            {(data.verdict || data.hiringRecommendation || "Pending").toUpperCase().replace(/_/g, ' ')}
          </p>
        </div>
      </div>

      {data.summary && (
        <div className="info-box">
          <h4>Summary</h4>
          <p>{data.summary}</p>
        </div>
      )}

      {data.seniorityAlignment && (
        <div className="info-box">
          <h4>Seniority Alignment</h4>
          <p>{data.seniorityAlignment}</p>
        </div>
      )}

      {data.jdFit && (
        <div className="info-box">
          <h4>Job Description Fit</h4>
          <p>{data.jdFit}</p>
        </div>
      )}

      {/* Skills Evaluation Table */}
      {(data.skills || data.skillsEvaluation) && (data.skills || data.skillsEvaluation).length > 0 && (
        <div className="skills-section">
          <h4>Skills Evaluation</h4>
          <div className="skills-table-wrapper">
            <table className="skills-table">
              <thead>
                <tr>
                  <th>Skill</th>
                  <th>Score</th>
                  <th>Evidence</th>
                </tr>
              </thead>
              <tbody>
                {(data.skills || data.skillsEvaluation).map((skill, idx) => (
                  <tr key={idx}>
                    <td className="skill-name-cell">{skill.name || skill.skill || skill.skillName}</td>
                    <td className="skill-score-cell">
                      <span className="score-badge">
                        {getDisplayScore(skill.score || skill.proficiency || 0)}
                        <span className="score-out-of">/10</span>
                      </span>
                    </td>
                    <td className="skill-comment-cell">
                      {skill.evidence || skill.comment || skill.description || "N/A"}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Communication and Problem Solving Scores */}
      {(data.communicationScore || data.problemSolvingScore) && (
        <div className="scores-grid">
          {data.communicationScore && (
            <div className="score-metric">
              <h4>Communication Score</h4>
              <div className="metric-score">{getDisplayScore(data.communicationScore)}/10</div>
              <p>How well the candidate articulated ideas and explained their approach.</p>
            </div>
          )}
          {data.problemSolvingScore && (
            <div className="score-metric">
              <h4>Problem Solving Score</h4>
              <div className="metric-score">{getDisplayScore(data.problemSolvingScore)}/10</div>
              <p>Ability to analyze problems and find effective solutions.</p>
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

      {(data.gaps || data.weaknesses || data.areasOfImprovement) && (data.gaps || data.weaknesses || data.areasOfImprovement).length > 0 && (
        <div className="assessment-box">
          <div className="assessment-header warning">
            <span>⚠</span>
            <h4>Areas for Improvement</h4>
          </div>
          <ul className="assessment-list">
            {(data.gaps || data.weaknesses || data.areasOfImprovement).map((gap, idx) => (
              <li key={idx}>{gap}</li>
            ))}
          </ul>
        </div>
      )}

      {data.riskAreas && data.riskAreas.length > 0 && (
        <div className="assessment-box">
          <div className="assessment-header danger">
            <span>✕</span>
            <h4>Risk Areas</h4>
          </div>
          <ul className="assessment-list">
            {data.riskAreas.map((risk, idx) => (
              <li key={idx}>{risk}</li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
