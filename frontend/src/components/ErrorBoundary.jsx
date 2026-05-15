import React from 'react';

export default class ErrorBoundary extends React.Component {
  state = { hasError: false };

  static getDerivedStateFromError() {
    return { hasError: true };
  }

  render() {
    if (this.state.hasError) {
      return <main className="error-state">TQSport đang gặp lỗi hiển thị. Vui lòng thử lại.</main>;
    }

    return this.props.children;
  }
}

