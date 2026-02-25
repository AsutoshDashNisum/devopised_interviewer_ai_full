/**
 * Test Utilities for React Components
 * Provides helper functions for rendering and testing components
 * with common setup requirements
 */
import React from 'react';
import { render as rtlRender } from '@testing-library/react';

/**
 * Custom render function that sets up common test environment
 * Can be extended to include providers (Redux, Theme, etc.)
 */
function render(ui, options = {}) {
  return rtlRender(ui, {
    ...options,
  });
}

/**
 * Mocks authentication by setting token in localStorage
 */
export function mockAuthToken(token = 'test-jwt-token') {
  localStorage.setItem('authToken', token);
  return token;
}

/**
 * Clears authentication by removing token from localStorage
 */
export function clearAuthToken() {
  localStorage.removeItem('authToken');
}

/**
 * Sets up authenticated user state
 */
export function setupAuthenticatedUser(userData = {}) {
  const defaultUser = {
    id: '123',
    username: 'testuser',
    email: 'test@example.com',
    roles: ['USER'],
    ...userData,
  };
  localStorage.setItem('user', JSON.stringify(defaultUser));
  return defaultUser;
}

/**
 * Clears user data from storage
 */
export function clearAuthenticatedUser() {
  localStorage.removeItem('user');
}

/**
 * Mock API response helper
 */
export function createMockResponse(data, status = 200) {
  return Promise.resolve({
    data,
    status,
    statusText: 'OK',
  });
}

/**
 * Mock API error response helper
 */
export function createMockErrorResponse(message, status = 500) {
  return Promise.reject({
    response: {
      data: { error: message },
      status,
      statusText: 'Error',
    },
    message,
  });
}

/**
 * Waits for async operations to complete
 */
export async function waitForAsync() {
  return new Promise((resolve) => setTimeout(resolve, 0));
}

// Re-export everything from React Testing Library
export * from '@testing-library/react';

// Override render with custom render
export { render };

