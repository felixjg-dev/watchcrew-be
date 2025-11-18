# API Testing Examples

## Environment Setup

```bash
# Set TMDB API Key
export TMDB_API_KEY=your_actual_tmdb_api_key

# Start MongoDB
docker run -d -p 27017:27017 --name mongodb mongo:latest

# Run the application
./mvnw spring-boot:run
```

## Example API Calls

### 1. Register a User

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123",
    "preferences": ["ACTION", "SCI_FI", "THRILLER"]
  }'
```

**Response:**
```json
{
  "id": "507f1f77bcf86cd799439011",
  "username": "john_doe",
  "preferences": ["ACTION", "SCI_FI", "THRILLER"],
  "friendIds": [],
  "myMovieIds": [],
  "customListIds": []
}
```

### 2. Get Latest Movies by Preferences

```bash
curl -X GET "http://localhost:8080/api/movies/latest?genres=ACTION,SCI_FI&page=0&size=10"
```

### 3. Search Movies

```bash
curl -X GET "http://localhost:8080/api/movies/search?query=inception&page=0&size=10"
```

### 4. Get Personalized Recommendations

```bash
curl -X GET "http://localhost:8080/api/movies/user/507f1f77bcf86cd799439011/recommendations?page=0&size=10"
```

### 5. Add Friend

```bash
curl -X POST http://localhost:8080/api/users/507f1f77bcf86cd799439011/friends/507f1f77bcf86cd799439012
```

### 6. Rate a Movie

```bash
curl -X POST "http://localhost:8080/api/ratings?userId=507f1f77bcf86cd799439011" \
  -H "Content-Type: application/json" \
  -d '{
    "movieId": "60d5ec49f1a4c8b9d8e1f2a3",
    "score": 8.5
  }'
```

### 7. Write a Review

```bash
curl -X POST "http://localhost:8080/api/reviews?userId=507f1f77bcf86cd799439011&username=john_doe" \
  -H "Content-Type: application/json" \
  -d '{
    "movieId": "60d5ec49f1a4c8b9d8e1f2a3",
    "content": "Great movie! The action scenes were incredible."
  }'
```

### 8. Recommend Movie to Friend

```bash
curl -X POST "http://localhost:8080/api/recommendations?fromUserId=507f1f77bcf86cd799439011&fromUsername=john_doe" \
  -H "Content-Type: application/json" \
  -d '{
    "friendId": "507f1f77bcf86cd799439012",
    "movieId": "60d5ec49f1a4c8b9d8e1f2a3",
    "message": "You have to watch this!"
  }'
```

### 9. Get Pending Recommendations

```bash
curl -X GET http://localhost:8080/api/recommendations/pending/507f1f77bcf86cd799439012
```

### 10. Create Custom Movie List

```bash
curl -X POST "http://localhost:8080/api/movie-lists?userId=507f1f77bcf86cd799439011" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "My Favorites",
    "description": "Movies I love to rewatch",
    "movieIds": []
  }'
```

### 11. Add Movie to Custom List

```bash
curl -X POST "http://localhost:8080/api/movie-lists/60d5ec49f1a4c8b9d8e1f2a4/movies/60d5ec49f1a4c8b9d8e1f2a3?userId=507f1f77bcf86cd799439011"
```

### 12. Send Message to Friend

```bash
curl -X POST "http://localhost:8080/api/messages?fromUserId=507f1f77bcf86cd799439011&fromUsername=john_doe" \
  -H "Content-Type: application/json" \
  -d '{
    "toUserId": "507f1f77bcf86cd799439012",
    "content": "Hey, check out this movie recommendation!"
  }'
```

### 13. Get Conversation

```bash
curl -X GET "http://localhost:8080/api/messages/conversation?userId1=507f1f77bcf86cd799439011&userId2=507f1f77bcf86cd799439012"
```

### 14. Update User Preferences

```bash
curl -X PUT http://localhost:8080/api/users/507f1f77bcf86cd799439011/preferences \
  -H "Content-Type: application/json" \
  -d '{
    "preferences": ["ACTION", "COMEDY", "ADVENTURE", "SCI_FI"]
  }'
```

### 15. Get Similar Movies

```bash
curl -X GET "http://localhost:8080/api/movies/60d5ec49f1a4c8b9d8e1f2a3/similar?page=0&size=10"
```

### 16. Get Movie Reviews

```bash
curl -X GET http://localhost:8080/api/reviews/movie/60d5ec49f1a4c8b9d8e1f2a3
```

### 17. Get Average Rating for Movie

```bash
curl -X GET http://localhost:8080/api/ratings/movie/60d5ec49f1a4c8b9d8e1f2a3/average
```

### 18. Add Movie to My List

```bash
curl -X POST http://localhost:8080/api/users/507f1f77bcf86cd799439011/movies/60d5ec49f1a4c8b9d8e1f2a3
```

### 19. Get User's Movie Lists

```bash
curl -X GET http://localhost:8080/api/movie-lists/user/507f1f77bcf86cd799439011
```

### 20. Get Unread Messages

```bash
curl -X GET http://localhost:8080/api/messages/unread/507f1f77bcf86cd799439011
```

## WebSocket Connection Example (JavaScript)

```javascript
// Connect to WebSocket
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);
    
    // Subscribe to receive messages
    stompClient.subscribe('/user/queue/messages', function(message) {
        const msg = JSON.parse(message.body);
        console.log('Received message:', msg);
    });
    
    // Send a message
    stompClient.send("/app/chat.send", {}, JSON.stringify({
        toUserId: '507f1f77bcf86cd799439012',
        content: 'Hello via WebSocket!'
    }));
});
```

## Available Genres

- ACTION
- ADULT
- ADVENTURE
- ANIMATION
- BIOGRAPHY
- COMEDY
- CRIME
- DOCUMENTARY
- DRAMA
- FAMILY
- FANTASY
- FILM_NOIR
- GAME_SHOW
- HISTORY
- HORROR
- MUSICAL
- MUSIC
- MYSTERY
- NEWS
- REALITY_TV
- ROMANCE
- SCI_FI
- SHORT
- SPORT
- TALK_SHOW
- THRILLER
- WAR
- WESTERN

## Testing Tips

1. **Get a TMDB API Key**: Sign up at https://www.themoviedb.org/settings/api
2. **Use Postman**: Import these curl commands for easier testing
3. **Check MongoDB**: Use MongoDB Compass to view data
4. **Enable Debug Logging**: Check application logs for TMDB API calls
5. **Test WebSocket**: Use a WebSocket client or the provided JavaScript example
