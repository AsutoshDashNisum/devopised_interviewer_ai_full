/**
 * Mock Axios for testing API calls
 * Used to mock HTTP responses without making real API requests
 */
import axios from 'axios';

jest.mock('axios');

/**
 * Mocks a successful API response
 */
export const mockAxiosSuccess = (data, status = 200) => {
  axios.mockResolvedValueOnce({
    data,
    status,
    statusText: 'OK',
  });
};

/**
 * Mocks an API error response
 */
export const mockAxiosError = (message, status = 500) => {
  axios.mockRejectedValueOnce({
    response: {
      data: { error: message },
      status,
      statusText: 'Error',
    },
    message,
  });
};

/**
 * Mocks 401 Unauthorized response
 */
export const mockAxiosUnauthorized = () => {
  mockAxiosError('Unauthorized', 401);
};

/**
 * Mocks 403 Forbidden response
 */
export const mockAxiosForbidden = () => {
  mockAxiosError('Forbidden', 403);
};

/**
 * Clears all axios mocks
 */
export const clearAxiosMocks = () => {
  axios.mockClear();
};

export default axios;

