import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Login = ({ setUser }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // Login endpoint'ine istek atıyoruz
      const response = await axios.post('http://localhost:3000/auth/login',
        { email, password }
      );

      const { token, user } = response.data;

      // Token'ı localStorage'a kaydedebiliriz (opsiyonel ama iyi pratik)
      localStorage.setItem('token', token);

      // JWT Auth için header oluşturuyoruz
      const authHeader = 'Bearer ' + token;

      // Kullanıcı bilgisini state'e kaydediyoruz
      setUser({ ...user, authHeader });

      // Ve sonraki istekler için axios default header'ını ayarlıyoruz
      axios.defaults.headers.common['Authorization'] = authHeader;

      navigate('/');
    } catch (err) {
      setError('Invalid email or password');
      console.error(err);
    }
  };

  return (
    <div style={{ maxWidth: '400px', margin: '0 auto' }}>
      <h2>Login</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: '10px' }}>
          <label>Email:</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            style={{ width: '100%', padding: '5px' }}
          />
        </div>
        <div style={{ marginBottom: '10px' }}>
          <label>Password:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            style={{ width: '100%', padding: '5px' }}
          />
        </div>
        <button type="submit" style={{ padding: '5px 10px' }}>Login</button>
      </form>
    </div>
  );
};

export default Login;
