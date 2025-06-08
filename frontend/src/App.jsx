import React, { useEffect, useState } from 'react';
import './App2.css';

function App() {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [dob, setDob] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    fetchStudents();
  }, []);

  const fetchStudents = () => {
    setLoading(true);
    fetch('http://localhost:8081/api/v1/student')
      .then(res => res.json())
      .then(data => {
        setStudents(data);
        setLoading(false);
      })
      .catch(() => setLoading(false));
  };

  const addStudent = () => {
    if (!name || !email || !dob) {
      setError('Please fill in all fields.');
      return;
    }
    setError('');
    fetch('http://localhost:8081/api/v1/student', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, email, dob }),
    })
      .then(res => {
        if (!res.ok) throw new Error('Failed to add');
        return res.json();
      })
      .then(() => {
        setName('');
        setEmail('');
        setDob('');
        fetchStudents();
      })
      .catch(() => setError('Failed to add student.'));
  };

  const deleteStudent = (id) => {
    fetch(`http://localhost:8081/api/v1/student/${id}`, {
      method: 'DELETE',
    })
      .then(res => {
        if (!res.ok) throw new Error('Failed to delete');
        fetchStudents();
      })
      .catch(() => setError('Failed to delete student.'));
  };

  if (loading) {
    return <div className="container"><h2>Loading...</h2></div>;
  }

  return (
    <div className="container">
      <h1>Students List</h1>

      <div className="form-row">
        <input
          type="text"
          placeholder="Name"
          value={name}
          onChange={e => setName(e.target.value)}
        />
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={e => setEmail(e.target.value)}
        />
        <input
  type="text"
  placeholder="YYYY-MM-DD"
  value={dob}
  onChange={e => setDob(e.target.value)}
/>

        <button onClick={addStudent}>Add Student</button>
      </div>

      {error && <div className="error">{error}</div>}

      <ul>
        {students.map(student => (
          <li key={student.id}>
            <span>{student.name} - {student.email}</span>
            <button onClick={() => deleteStudent(student.id)}>Delete</button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;
