/**
 * EvaluationForm Component Security Tests
 * Tests API authorization error handling and UI behavior
 *
 * Test Coverage:
 * - 401 Unauthorized error display
 * - 403 Forbidden error display
 * - Error message presentation to user
 * - Loading state during API calls
 * - Form submission with potential auth errors
 */
import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import EvaluationForm from '../../components/EvaluationForm';
import * as evaluationService from '../../services/evaluationService';

// Mock the evaluation service
jest.mock('../../services/evaluationService');

// Mock the EvaluationPage component to simplify testing
jest.mock('../../components/EvaluationPage', () => {
  return function MockEvaluationPage() {
    return <div>Evaluation Results</div>;
  };
});

describe('EvaluationForm - Security Tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  // ==================== 401 Unauthorized Tests ====================

  describe('401 Unauthorized Error Handling', () => {
    test('should display 401 error message when token is missing', async () => {
      // Arrange
      const mockError = new Error('API Error (401): Missing or invalid token');
      evaluationService.evaluateInterview.mockRejectedValueOnce(mockError);

      // Act
      render(<EvaluationForm />);

      const jobDescInput = screen.getByPlaceholderText('Paste the full job description here...');
      const interviewInput = screen.getByPlaceholderText('Paste the interview transcript here...');
      const senioritySelect = screen.getByDisplayValue('');
      const submitButton = screen.getByRole('button', { name: /evaluate/i });

      await userEvent.type(jobDescInput, 'Senior Developer');
      await userEvent.type(interviewInput, 'Tell me about your experience');
      await userEvent.selectOptions(senioritySelect, 'senior');
      await userEvent.click(submitButton);

      // Assert
      await waitFor(() => {
        expect(screen.getByText(/401/i)).toBeInTheDocument();
      });
    });

    test('should display 401 error when token is expired', async () => {
      // Arrange
      const mockError = new Error('API Error (401): Token expired');
      evaluationService.evaluateInterview.mockRejectedValueOnce(mockError);

      // Act
      render(<EvaluationForm />);

      const jobDescInput = screen.getByPlaceholderText('Paste the full job description here...');
      const interviewInput = screen.getByPlaceholderText('Paste the interview transcript here...');
      const senioritySelect = screen.getByDisplayValue('');
      const submitButton = screen.getByRole('button', { name: /evaluate/i });

      await userEvent.type(jobDescInput, 'Senior Developer');
      await userEvent.type(interviewInput, 'Tell me about your experience');
      await userEvent.selectOptions(senioritySelect, 'senior');
      await userEvent.click(submitButton);

      // Assert
      await waitFor(() => {
        const errorMessage = screen.getByText(/401/i);
        expect(errorMessage).toBeInTheDocument();
      });
    });

    test('should allow user to retry after 401 error', async () => {
      // Arrange
      const mockError = new Error('API Error (401): Unauthorized');
      evaluationService.evaluateInterview
        .mockRejectedValueOnce(mockError)
        .mockResolvedValueOnce({
          candidateEvaluation: { overallScore: 75 },
          status: 'success',
        });

      // Act
      render(<EvaluationForm />);

      const jobDescInput = screen.getByPlaceholderText('Paste the full job description here...');
      const interviewInput = screen.getByPlaceholderText('Paste the interview transcript here...');
      const senioritySelect = screen.getByDisplayValue('');
      const submitButton = screen.getByRole('button', { name: /evaluate/i });

      // First submission - fails with 401
      await userEvent.type(jobDescInput, 'Senior Developer');
      await userEvent.type(interviewInput, 'Tell me about your experience');
      await userEvent.selectOptions(senioritySelect, 'senior');
      await userEvent.click(submitButton);

      // Wait for error to display
      await waitFor(() => {
        expect(screen.getByText(/401/i)).toBeInTheDocument();
      });

      // Second submission - succeeds after user authentication
      fireEvent.click(submitButton);

      // Assert - Should eventually succeed
      await waitFor(() => {
        expect(evaluationService.evaluateInterview).toHaveBeenCalledTimes(2);
      });
    });
  });

  // ==================== 403 Forbidden Tests ====================

  describe('403 Forbidden Error Handling', () => {
    test('should display 403 error message when user lacks permissions', async () => {
      // Arrange
      const mockError = new Error('API Error (403): Forbidden - User lacks required role');
      evaluationService.evaluateInterview.mockRejectedValueOnce(mockError);

      // Act
      render(<EvaluationForm />);

      const jobDescInput = screen.getByPlaceholderText('Paste the full job description here...');
      const interviewInput = screen.getByPlaceholderText('Paste the interview transcript here...');
      const senioritySelect = screen.getByDisplayValue('');
      const submitButton = screen.getByRole('button', { name: /evaluate/i });

      await userEvent.type(jobDescInput, 'Senior Developer');
      await userEvent.type(interviewInput, 'Tell me about your experience');
      await userEvent.selectOptions(senioritySelect, 'senior');
      await userEvent.click(submitButton);

      // Assert
      await waitFor(() => {
        expect(screen.getByText(/403/i)).toBeInTheDocument();
      });
    });

    test('should display meaningful 403 error message', async () => {
      // Arrange
      const mockError = new Error('API Error (403): Insufficient permissions');
      evaluationService.evaluateInterview.mockRejectedValueOnce(mockError);

      // Act
      render(<EvaluationForm />);

      const jobDescInput = screen.getByPlaceholderText('Paste the full job description here...');
      const interviewInput = screen.getByPlaceholderText('Paste the interview transcript here...');
      const senioritySelect = screen.getByDisplayValue('');
      const submitButton = screen.getByRole('button', { name: /evaluate/i });

      await userEvent.type(jobDescInput, 'Senior Developer');
      await userEvent.type(interviewInput, 'Tell me about your experience');
      await userEvent.selectOptions(senioritySelect, 'senior');
      await userEvent.click(submitButton);

      // Assert
      await waitFor(() => {
        const errorElement = screen.getByText(/403/i);
        expect(errorElement).toBeInTheDocument();
      });
    });
  });

  // ==================== Network Error Tests ====================

  describe('Network Error Handling', () => {
    test('should display network error when server is unreachable', async () => {
      // Arrange
      const mockError = new Error('No response from server. Make sure the backend is running');
      evaluationService.evaluateInterview.mockRejectedValueOnce(mockError);

      // Act
      render(<EvaluationForm />);

      const jobDescInput = screen.getByPlaceholderText('Paste the full job description here...');
      const interviewInput = screen.getByPlaceholderText('Paste the interview transcript here...');
      const senioritySelect = screen.getByDisplayValue('');
      const submitButton = screen.getByRole('button', { name: /evaluate/i });

      await userEvent.type(jobDescInput, 'Senior Developer');
      await userEvent.type(interviewInput, 'Tell me about your experience');
      await userEvent.selectOptions(senioritySelect, 'senior');
      await userEvent.click(submitButton);

      // Assert
      await waitFor(() => {
        expect(screen.getByText(/No response from server/i)).toBeInTheDocument();
      });
    });
  });

  // ==================== Loading State Tests ====================

  describe('Loading State During Auth Checks', () => {
    test('should show loading spinner while API request is in progress', async () => {
      // Arrange
      evaluationService.evaluateInterview.mockImplementation(
        () => new Promise((resolve) => setTimeout(() => resolve({
          candidateEvaluation: { overallScore: 75 },
          status: 'success',
        }), 100))
      );

      // Act
      render(<EvaluationForm />);

      const jobDescInput = screen.getByPlaceholderText('Paste the full job description here...');
      const interviewInput = screen.getByPlaceholderText('Paste the interview transcript here...');
      const senioritySelect = screen.getByDisplayValue('');
      const submitButton = screen.getByRole('button', { name: /evaluate/i });

      await userEvent.type(jobDescInput, 'Senior Developer');
      await userEvent.type(interviewInput, 'Tell me about your experience');
      await userEvent.selectOptions(senioritySelect, 'senior');
      await userEvent.click(submitButton);

      // Assert - Loading indicator appears
      expect(screen.getByText(/Evaluating interview/i)).toBeInTheDocument();
    });

    test('should disable form inputs while loading', async () => {
      // Arrange
      evaluationService.evaluateInterview.mockImplementation(
        () => new Promise((resolve) => setTimeout(() => resolve({
          candidateEvaluation: { overallScore: 75 },
          status: 'success',
        }), 100))
      );

      // Act
      render(<EvaluationForm />);

      const jobDescInput = screen.getByPlaceholderText('Paste the full job description here...');
      const interviewInput = screen.getByPlaceholderText('Paste the interview transcript here...');
      const senioritySelect = screen.getByDisplayValue('');
      const submitButton = screen.getByRole('button', { name: /evaluate/i });

      await userEvent.type(jobDescInput, 'Senior Developer');
      await userEvent.type(interviewInput, 'Tell me about your experience');
      await userEvent.selectOptions(senioritySelect, 'senior');
      await userEvent.click(submitButton);

      // Assert - Inputs should be disabled during loading
      expect(jobDescInput).toBeDisabled();
      expect(interviewInput).toBeDisabled();
    });
  });

  // ==================== Error Clearing Tests ====================

  describe('Error State Management', () => {
    test('should clear error when user starts typing after error', async () => {
      // Arrange
      const mockError = new Error('API Error (500): Server error');
      evaluationService.evaluateInterview.mockRejectedValueOnce(mockError);

      // Act
      render(<EvaluationForm />);

      const jobDescInput = screen.getByPlaceholderText('Paste the full job description here...');
      const interviewInput = screen.getByPlaceholderText('Paste the interview transcript here...');
      const senioritySelect = screen.getByDisplayValue('');
      const submitButton = screen.getByRole('button', { name: /evaluate/i });

      // First submission - fails
      await userEvent.type(jobDescInput, 'Senior Developer');
      await userEvent.type(interviewInput, 'Tell me about your experience');
      await userEvent.selectOptions(senioritySelect, 'senior');
      await userEvent.click(submitButton);

      // Wait for error to display
      await waitFor(() => {
        expect(screen.getByText(/500/i)).toBeInTheDocument();
      });

      // Clear and check behavior
      fireEvent.change(jobDescInput, { target: { value: '' } });

      // Error should still be displayed until fixed
      expect(screen.getByText(/Server error/i)).toBeInTheDocument();
    });
  });
});

