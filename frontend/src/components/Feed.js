import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Feed = ({ user }) => {
  const [tweets, setTweets] = useState([]);
  const [newTweet, setNewTweet] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchTweets();
  }, [user.id]);

  const fetchTweets = async () => {
    try {
      // README'deki endpoint: /tweet/findByUserId
      const response = await axios.get(`http://localhost:3000/tweet/findByUserId/${user.id}`);
      setTweets(response.data);
      setLoading(false);
    } catch (err) {
      console.error('Error fetching tweets:', err);
      setLoading(false);
    }
  };

  const handlePostTweet = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:3000/tweet', { content: newTweet });
      setNewTweet('');
      fetchTweets(); // Listeyi yenile
    } catch (err) {
      console.error('Error posting tweet:', err);
    }
  };

  const handleDeleteTweet = async (tweetId) => {
    if (window.confirm('Are you sure you want to delete this tweet?')) {
      try {
        await axios.delete(`http://localhost:3000/tweet/${tweetId}`);
        fetchTweets();
      } catch (err) {
        console.error('Error deleting tweet:', err);
      }
    }
  };

  return (
    <div style={{ maxWidth: '600px', margin: '0 auto' }}>
      <h2>My Feed</h2>

      <div style={{ marginBottom: '20px', padding: '10px', border: '1px solid #ccc' }}>
        <form onSubmit={handlePostTweet}>
          <textarea
            value={newTweet}
            onChange={(e) => setNewTweet(e.target.value)}
            placeholder="What's happening?"
            maxLength={280}
            style={{ width: '100%', height: '80px', marginBottom: '10px' }}
            required
          />
          <button type="submit">Tweet</button>
        </form>
      </div>

      {loading ? (
        <p>Loading tweets...</p>
      ) : (
        <div>
          {tweets.length === 0 ? (
            <p>No tweets yet.</p>
          ) : (
            tweets.map((tweet) => (
              <div key={tweet.id} style={{ border: '1px solid #eee', padding: '10px', marginBottom: '10px', borderRadius: '5px' }}>
                <div style={{ fontWeight: 'bold', marginBottom: '5px' }}>
                  {tweet.user ? tweet.user.fullName : 'Unknown User'}
                  <span style={{ color: '#888', fontWeight: 'normal' }}> @{tweet.user ? tweet.user.username : ''}</span>
                </div>
                <p>{tweet.content}</p>
                <div style={{ fontSize: '0.8em', color: '#888', marginTop: '10px' }}>
                  {new Date(tweet.createdAt).toLocaleString()}
                  <button
                    onClick={() => handleDeleteTweet(tweet.id)}
                    style={{ float: 'right', color: 'red', border: 'none', background: 'none', cursor: 'pointer' }}
                  >
                    Delete
                  </button>
                </div>
              </div>
            ))
          )}
        </div>
      )}
    </div>
  );
};

export default Feed;
