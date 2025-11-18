# WatchCrew Backend API

Spring Boot application for WatchCrew - a movie recommendation and social platform.

## Architecture

This project follows **Hexagonal Architecture** (Ports and Adapters) principles:

- **Domain Layer**: Core business entities (`com.watchcrew.domain.model`)
- **Application Layer**: Use cases and business logic (`com.watchcrew.application`)
  - **Ports (In)**: Interfaces defining what the application can do
  - **Services**: Implementation of use cases
  - **Ports (Out)**: Interfaces for external dependencies
- **Adapter Layer**: External communication
  - **Input Adapters**: REST controllers, WebSocket handlers
  - **Output Adapters**: MongoDB repositories, External API clients

## Features

- **Authentication & Authorization** 🆕
  - JWT-based authentication
  - Secure user registration and login
  - BCrypt password encryption
  - Stateless session management

- **User Management**
  - User registration with email and preferences
  - Friend management
  - Personal movie lists

- **Movie Discovery**
  - Fetch latest movies based on user preferences
  - Search movies
  - Get similar movie recommendations
  - Integration with TMDB API

- **Social Features**
  - Recommend movies to friends
  - Real-time messaging via WebSocket
  - Movie reviews and ratings
  - Custom movie lists

- **Ratings & Reviews**
  - Rate movies (1-10 scale)
  - Write and manage reviews
  - View aggregate ratings

- **API Documentation** 🆕
  - Interactive Swagger UI at `/swagger-ui.html`
  - OpenAPI 3.0 specification

## Prerequisites

- Java 21
- Maven 3.8+
- MongoDB 4.4+
- TMDB API Key (from https://www.themoviedb.org/settings/api)

## Configuration

### MongoDB Setup
```bash
# Start MongoDB locally
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

### Environment Configuration

Copy the example configuration file and update with your values:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Update `application.properties` with your configuration:
```properties
# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/watchcrew

# TMDB API
tmdb.api.key=your_actual_api_key_here
tmdb.api.base-url=https://api.themoviedb.org/3

# JWT Configuration (Generate a secure 256-bit secret)
jwt.secret=your-256-bit-secret-key-here-minimum-32-characters-for-hs512
jwt.expiration=86400000
```

⚠️ **Security Note**: Never commit `application.properties` to version control! It's already in `.gitignore`.

## Running the Application

```bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

### Access Swagger Documentation
Visit `http://localhost:8080/swagger-ui.html` to explore and test the API interactively.

## API Endpoints

### Authentication 🆕
- `POST /api/auth/register` - Register new user (returns JWT token)
- `POST /api/auth/login` - Login user (returns JWT token)

### Users
- `GET /api/users/{userId}` - Get user by ID (requires authentication)
- `GET /api/users/username/{username}` - Get user by username
- `PUT /api/users/{userId}/preferences` - Update user preferences
- `POST /api/users/{userId}/friends/{friendId}` - Add friend
- `GET /api/users/{userId}/friends` - Get user's friends
- `POST /api/users/{userId}/movies/{movieId}` - Add movie to user's list

### Movies
- `GET /api/movies/{movieId}` - Get movie by ID
- `GET /api/movies/latest?genres=ACTION,COMEDY` - Get latest movies by genres
- `GET /api/movies/user/{userId}/recommendations` - Get personalized recommendations
- `GET /api/movies/{movieId}/similar` - Get similar movies
- `GET /api/movies/search?query=inception` - Search movies

### Ratings
- `POST /api/ratings?userId={userId}` - Rate a movie
- `GET /api/ratings/movie/{movieId}` - Get all ratings for a movie
- `GET /api/ratings/user/{userId}` - Get user's ratings
- `GET /api/ratings/movie/{movieId}/average` - Get average rating

### Reviews
- `POST /api/reviews?userId={userId}&username={username}` - Create review
- `GET /api/reviews/movie/{movieId}` - Get movie reviews
- `PUT /api/reviews/{reviewId}?userId={userId}` - Update review
- `DELETE /api/reviews/{reviewId}?userId={userId}` - Delete review

### Recommendations
- `POST /api/recommendations?fromUserId={userId}&fromUsername={username}` - Recommend movie to friend
- `GET /api/recommendations/pending/{userId}` - Get pending recommendations
- `PUT /api/recommendations/{recommendationId}/view` - Mark as viewed

### Messages
- `POST /api/messages?fromUserId={userId}&fromUsername={username}` - Send message
- `GET /api/messages/conversation?userId1={id1}&userId2={id2}` - Get conversation
- `GET /api/messages/unread/{userId}` - Get unread messages

### Movie Lists
- `POST /api/movie-lists?userId={userId}` - Create custom list
- `GET /api/movie-lists/user/{userId}` - Get user's lists
- `POST /api/movie-lists/{listId}/movies/{movieId}?userId={userId}` - Add movie to list
- `DELETE /api/movie-lists/{listId}?userId={userId}` - Delete list

## WebSocket Support

Connect to WebSocket at `/ws` endpoint for real-time messaging:

```javascript
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
  stompClient.subscribe('/user/queue/messages', function(message) {
    console.log('Received:', JSON.parse(message.body));
  });
  
  stompClient.send("/app/chat.send", {}, JSON.stringify({
    toUserId: 'recipient-id',
    content: 'Hello!'
  }));
});
```

## Data Models

### Genre Enum
ACTION, ADULT, ADVENTURE, ANIMATION, BIOGRAPHY, COMEDY, CRIME, DOCUMENTARY, DRAMA, FAMILY, FANTASY, FILM_NOIR, GAME_SHOW, HISTORY, HORROR, MUSICAL, MUSIC, MYSTERY, NEWS, REALITY_TV, ROMANCE, SCI_FI, SHORT, SPORT, TALK_SHOW, THRILLER, WAR, WESTERN

## Next Steps

1. **Security**: Implement Spring Security with JWT authentication
2. **Validation**: Add request validation with `@Valid` annotations
3. **Error Handling**: Create global exception handler
4. **Caching**: Add Redis for caching movie data
5. **Testing**: Write unit and integration tests
6. **Documentation**: Add Swagger/OpenAPI documentation
7. **Monitoring**: Integrate actuator and monitoring tools

## License

MIT
