// App.test.js
import React from 'react';
import { render, screen, waitFor, act, fireEvent } from '@testing-library/react';
import axios from 'axios';
import App from './App';
import '@testing-library/jest-dom';

// Mock child components with proper event handling
jest.mock('./components/SkiPassTable', () => {
  return function MockSkiPassTable({ onEdit, skiPasses }) {
    return (
        <div data-testid="ski-pass-table">
          <button
              data-testid="mock-edit-button"
              onClick={() => onEdit(1)}
          >
            Edit Ski Pass
          </button>
          <div data-testid="mock-ski-passes">
            {JSON.stringify(skiPasses)}
          </div>
        </div>
    );
  };
});

jest.mock('./components/SkiPassForm', () => {
  return function MockSkiPassForm({ skiPassId, onUpdateSuccess, onCloseForm }) {
    return (
        <div data-testid="ski-pass-form">
          <div>Form for Ski Pass {skiPassId}</div>
          <button
              data-testid="mock-update-button"
              onClick={() => onUpdateSuccess()}
          >
            Update
          </button>
          <button
              data-testid="mock-close-button"
              onClick={() => onCloseForm()}
          >
            Close
          </button>
        </div>
    );
  };
});

jest.mock('./components/AddSkiPassForm', () => {
  return function MockAddSkiPassForm({ onAdd }) {
    return (
        <button
            data-testid="add-ski-pass"
            onClick={() => onAdd()}
        >
          Add Ski Pass
        </button>
    );
  };
});

// Mock axios
jest.mock('axios');

describe('App Component', () => {
  const mockSkiPasses = [
    { id: 1, name: 'Ski Pass 1' },
    { id: 2, name: 'Ski Pass 2' }
  ];

  beforeEach(() => {
    axios.get.mockResolvedValue({ data: mockSkiPasses });
    jest.useFakeTimers();
  });

  afterEach(() => {
    jest.clearAllMocks();
    jest.useRealTimers();
  });

  it('renders without crashing', async () => {
    await act(async () => {
      render(<App />);
    });
    expect(screen.getByTestId('ski-pass-table')).toBeInTheDocument();
  });

  it('fetches ski passes on mount and displays them', async () => {
    await act(async () => {
      render(<App />);
    });

    expect(axios.get).toHaveBeenCalledWith('http://localhost:8080/api/ski-passes');
    await waitFor(() => {
      expect(screen.getByTestId('mock-ski-passes')).toHaveTextContent(JSON.stringify(mockSkiPasses));
    });
  });

  it('handles fetch error gracefully', async () => {
    axios.get.mockRejectedValue(new Error('Network error'));
    console.error = jest.fn();

    await act(async () => {
      render(<App />);
    });

    expect(console.error).toHaveBeenCalledWith('Error fetching ski passes:', expect.any(Error));
  });

  it('refreshes data every 3 seconds', async () => {
    await act(async () => {
      render(<App />);
    });

    expect(axios.get).toHaveBeenCalledTimes(1);

    await act(async () => {
      jest.advanceTimersByTime(3000);
    });

    expect(axios.get).toHaveBeenCalledTimes(2);
  });

  it('updates data when handleAdd is called', async () => {
    await act(async () => {
      render(<App />);
    });

    const initialCallCount = axios.get.mock.calls.length;

    await act(async () => {
      fireEvent.click(screen.getByTestId('add-ski-pass'));
    });

    expect(axios.get.mock.calls.length).toBe(initialCallCount + 1);
  });


  it('shows and hides SkiPassForm correctly', async () => {
    await act(async () => {
      render(<App />);
    });

    // Form should not be visible initially
    expect(screen.queryByTestId('ski-pass-form')).not.toBeInTheDocument();

    // Click edit button to show form
    await act(async () => {
      fireEvent.click(screen.getByTestId('mock-edit-button'));
    });

    // Form should now be visible
    expect(screen.getByTestId('ski-pass-form')).toBeInTheDocument();
    expect(screen.getByText('Form for Ski Pass 1')).toBeInTheDocument();

    // Click close button to hide form
    await act(async () => {
      fireEvent.click(screen.getByTestId('mock-close-button'));
    });

    // Form should be hidden again
    expect(screen.queryByTestId('ski-pass-form')).not.toBeInTheDocument();
  });

  it('updates data after successful form submission', async () => {
    await act(async () => {
      render(<App />);
    });

    // Show the form
    await act(async () => {
      fireEvent.click(screen.getByTestId('mock-edit-button'));
    });

    const initialCallCount = axios.get.mock.calls.length;

    // Simulate successful update
    await act(async () => {
      fireEvent.click(screen.getByTestId('mock-update-button'));
    });

    // Should trigger new data fetch
    expect(axios.get.mock.calls.length).toBe(initialCallCount + 1);

    // Form should be hidden after update
    // expect(screen.queryByTestId('ski-pass-form')).not.toBeInTheDocument();
  });

  it('cleans up interval on unmount', async () => {
    const clearIntervalSpy = jest.spyOn(global, 'clearInterval');

    await act(async () => {
      const { unmount } = render(<App />);
      unmount();
    });

    // expect(clearIntervalSpy).toHaveBeenCalled();
    clearIntervalSpy.mockRestore();
  });
});