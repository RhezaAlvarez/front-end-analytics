import React from 'react'
import axios from 'axios'

class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false };
  }

  static getDerivedStateFromError(error) {
    // Update state so the next render will show the fallback UI.
    return { hasError: true };
  }

  componentDidCatch(error, errorInfo) {
    // Send error log to backend
    this.logErrorToBackend(error, errorInfo);
  }

  async logErrorToBackend(error, errorInfo) {
    try {
      const logMessage = {
        message: error.message,
        stackTrace: error.stack,
        componentStack: errorInfo.componentStack,
      };
      await axios.post('http://localhost:8091/sample/log-error', logMessage);
    } catch (logError) {
      console.error('Failed to log error to backend:', logError);
    }
  }

  render() {
    if (this.state.hasError) {
      // You can render any custom fallback UI
      return <h1>Something went wrong.</h1>;
    }

    return this.props.children; 
  }
}

export default ErrorBoundary;


// class ErrorBoundary extends React.Component {
//   constructor(props) {
//     super(props)
//     this.state = { hasError: false }
//   }

//   static getDerivedStateFromError(error) {
//     return { hasError: true }
//   }

//   render() {
//     if (this.state.hasError) {
//       return <h1>Something went wrong.</h1>
//     }
//     return this.props.children
//   }
// }

// export default ErrorBoundary;
