import React, { useState } from 'react';
import { Routes, Route, Link, useNavigate } from 'react-router-dom';
import Login from './components/Login';
import Register from './components/Register';
import Feed from './components/Feed';

function App() {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  const handleLogout = () => {
    setUser(null);
    navigate('/login');
  };

  return (
    <div className="App">
      <nav style={{ padding: '10px', borderBottom: '1px solid #ccc', marginBottom: '20px' }}>
        {user ? (
          <>
            <span>Welcome, {user.fullName} (@{user.username}) </span>
            <button onClick={handleLogout}>Logout</button>
          </>
        ) : (
          <>
            <Link to="/login" style={{ marginRight: '10px' }}>Login</Link>
            <Link to="/register">Register</Link>
          </>
        )}
      </nav>

      <Routes>
        <Route path="/login" element={<Login setUser={setUser} />} />
        <Route path="/register" element={<Register />} />
        <Route path="/" element={user ? <Feed user={user} /> : <Login setUser={setUser} />} />
      </Routes>
    </div>
  );
}

export default App;
