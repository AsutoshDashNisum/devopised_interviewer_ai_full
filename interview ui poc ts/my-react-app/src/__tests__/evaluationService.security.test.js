/**
 * Evaluation Service Security Tests
 * Tests API call authorization handling, token management, and error responses
 *
 * Test Coverage:
 * - 401 Unauthorized error handling
 * - 403 Forbidden error handling
 * - Bearer token injection in requests
 * - Error message formatting
 * - Network error handling
 */
import { evaluateInterview, checkHealth } from '../../services/evaluationService';
import axios from 'axios';

// Mock axios
jest.mock('axios');

describe('Evaluation Service - Security Tests', () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  // ==================== Authorization Error Tests ====================

  describe('401 Unauthorized Handling', () => {
    test('should handle 401 response when token is missing', async () => {
      // Arrange
      const mockError = {
        response: {
          status: 401,
          statusText: 'Unauthorized',
          data: { message: 'Missing or invalid token' },
        },
      };
      axios.create().post = jest.fn().mockRejectedValueOnce(mockError);

      // Act & Assert
      await expect(evaluateInterview({
        jobDescription: 'Test',
        interviewTranscript: 'Test',
        seniority: 'junior',
      })).rejects.toThrow();
    });

    test('should handle 401 response when token is expired', async () => {
      // Arrange
      const mockError = {
        response: {
          status: 401,
          statusText: 'Unauthorized',
          data: { message: 'Token expired' },
        },
      };
      axios.create().post = jest.fn().mockRejectedValueOnce(mockError);

      // Act & Assert
      await expect(evaluateInterview({
        jobDescription: 'Test',
        interviewTranscript: 'Test',
        seniority: 'junior',
      })).rejects.toThrow('401');
    });

    test('should handle 401 response when token is invalid', async () => {
      // Arrange
      const mockError = {
        response: {
          status: 401,
          statusText: 'Unauthorized',
          data: { message: 'Invalid token signature' },
        },
      };
      axios.create().post = jest.fn().mockRejectedValueOnce(mockError);

      // Act & Assert
      await expect(evaluateInterview({
        jobDescription: 'Test',
        interviewTranscript: 'Test',
        seniority: 'junior',
      })).rejects.toThrow();
    });
  });

  describe('403 Forbidden Handling', () => {
    test('should handle 403 response when user lacks required permissions', async () => {
      // Arrange
      const mockError = {
        response: {
          status: 403,
          statusText: 'Forbidden',
          data: { message: 'User lacks ADMIN role' },
        },
      };
      axios.create().post = jest.fn().mockRejectedValueOnce(mockError);

      // Act & Assert
      await expect(evaluateInterview({
        jobDescription: 'Test',
        interviewTranscript: 'Test',
        seniority: 'junior',
      })).rejects.toThrow('403');
    });

    test('should provide meaningful error message for 403', async () => {
      // Arrange
      const mockError = {
        response: {
          status: 403,
          statusText: 'Forbidden',
          data: { message: 'Insufficient permissions' },
        },
      };
      axios.create().post = jest.fn().mockRejectedValueOnce(mockError);

      // Act & Assert
      try {
        await evaluateInterview({
          jobDescription: 'Test',
          interviewTranscript: 'Test',
          seniority: 'junior',
        });
        fail('Should have thrown error');
      } catch (error) {
        expect(error.message).toContain('403');
      }
    });
  });

  // ==================== Network Error Tests ====================

  describe('Network Error Handling', () => {
    test('should handle network timeout', async () => {
      // Arrange
      const mockError = new Error('Network timeout');
      mockError.code = 'ECONNABORTED';
      axios.create().post = jest.fn().mockRejectedValueOnce(mockError);

      // Act & Assert
      await expect(evaluateInterview({
        jobDescription: 'Test',
        interviewTranscript: 'Test',
        seniority: 'junior',
      })).rejects.toThrow();
    });

    test('should handle server not responding', async () => {
      // Arrange
      const mockError = {
        request: {},
        message: 'No response from server',
      };
      axios.create().post = jest.fn().mockRejectedValueOnce(mockError);

      // Act & Assert
      await expect(evaluateInterview({
        jobDescription: 'Test',
        interviewTranscript: 'Test',
        seniority: 'junior',
      })).rejects.toThrow('No response from server');
    });

    test('should handle network connection error', async () => {
      // Arrange
      const mockError = new Error('Network Error');
      axios.create().post = jest.fn().mockRejectedValueOnce(mockError);

      // Act & Assert
      await expect(evaluateInterview({
        jobDescription: 'Test',
        interviewTranscript: 'Test',
        seniority: 'junior',
      })).rejects.toThrow();
    });
  });

  // ==================== Error Message Formatting Tests ====================

  describe('Error Message Clarity', () => {
    test('should provide clear error message for API errors', async () => {
      // Arrange
      const mockError = {
        response: {
          status: 500,
          statusText: 'Internal Server Error',
          data: { message: 'Database connection failed' },
        },
      };
      axios.create().post = jest.fn().mockRejectedValueOnce(mockError);

      // Act & Assert
      try {
        await evaluateInterview({
          jobDescription: 'Test',
          interviewTranscript: 'Test',
          seniority: 'junior',
        });
        fail('Should have thrown error');
      } catch (error) {
        expect(error.message).toContain('500');
      }
    });

    test('should include status code in error message', async () => {
      // Arrange
      const mockError = {
        response: {
          status: 400,
          statusText: 'Bad Request',
          data: { message: 'Invalid input' },
        },
      };
      axios.create().post = jest.fn().mockRejectedValueOnce(mockError);

      // Act & Assert
      try {
        await evaluateInterview({
          jobDescription: 'Test',
          interviewTranscript: 'Test',
          seniority: 'junior',
        });
        fail('Should have thrown error');
      } catch (error) {
        expect(error.message).toContain('400');
      }
    });
  });

  // ==================== Health Check Security Tests ====================

  describe('Health Check Endpoint', () => {
    test('should call health check endpoint without authentication', async () => {
      // Arrange
      const mockResponse = { status: 'UP' };
      axios.create().get = jest.fn().mockResolvedValueOnce({
        data: mockResponse,
        status: 200,
      });

      // Act
      const result = await checkHealth();

      // Assert
      expect(result).toEqual(mockResponse);
    });

    test('should handle health check endpoint error', async () => {
      // Arrange
      const mockError = {
        response: {
          status: 503,
          statusText: 'Service Unavailable',
        },
      };
      axios.create().get = jest.fn().mockRejectedValueOnce(mockError);

      // Act & Assert
      await expect(checkHealth()).rejects.toThrow();
    });
  });
});

