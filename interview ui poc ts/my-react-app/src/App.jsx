import EvaluationForm from "./components/EvaluationForm";
import nisumLogo from "./assets/nisum_logo.png";
import "./index.css";

export default function App() {
  return (
    <>
      <header className="header">
        <div className="header-content">
          <img
            src={nisumLogo}
            alt="Nisum Technologies"
            className="logo"
            style={{ width: "100px", height: "100px" }}
          />
          <h1>Interview Evaluation Dashboard</h1>
        </div>
      </header>

      <main className="container">
        <EvaluationForm />
      </main>
    </>
  );
}
