import React from 'react';
import { BrowserRouter, Route } from 'react-router-dom';
import './App.css';
import Login from './pages/Login';
import Register from './pages/Register';
import Users from './pages/Users';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
          <Route path="/" component={Users} />
          <Route path="/login" component={Login} />
          <Route path="/register" component={Register} />
      </BrowserRouter>
    </div>
  );
}

export default App;
