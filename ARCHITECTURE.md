# WatchCrew Backend - Architecture Overview

## Project Structure

```
watchcrew-be/
├── src/main/java/com/watchcrew/
│   ├── WatchCrewApplication.java           # Main application entry point
│   │
│   ├── domain/model/                       # Domain Layer - Core Business Entities
│   │   ├── User.java                       # User entity with preferences & friends
│   │   ├── Movie.java                      # Movie entity with TMDB data
│   │   ├── Rating.java                     # User ratings (1-10 scale)
│   │   ├── Review.java                     # User reviews for movies
│   │   ├── Recommendation.java             # Movie recommendations between friends
│   │   ├── Message.java                    # User-to-user messages
│   │   ├── MovieList.java                  # Custom user movie lists
│   │   └── Genre.java                      # Genre enum (28 types)
│   │
│   ├── application/                        # Application Layer - Business Logic
│   │   ├── port/in/                        # Input Ports (Use Case Interfaces)
│   │   │   ├── UserUseCase.java
│   │   │   ├── MovieQuery.java
│   │   │   ├── RatingUseCase.java
│   │   │   ├── ReviewUseCase.java
│   │   │   ├── RecommendationUseCase.java
│   │   │   ├── CommunicationUseCase.java
│   │   │   └── MovieListUseCase.java
│   │   │
│   │   ├── port/out/                       # Output Ports (Repository Interfaces)
│   │   │   ├── UserRepository.java
│   │   │   ├── MovieRepository.java
│   │   │   ├── RatingRepository.java
│   │   │   ├── ReviewRepository.java
│   │   │   ├── RecommendationRepository.java
│   │   │   ├── MessageRepository.java
│   │   │   ├── MovieListRepository.java
│   │   │   └── ExternalMovieApiPort.java   # Interface for external API
│   │   │
│   │   └── service/                        # Service Implementations
│   │       ├── UserService.java
│   │       ├── MovieService.java
│   │       ├── RatingService.java
│   │       ├── ReviewService.java
│   │       ├── RecommendationService.java
│   │       ├── CommunicationService.java
│   │       └── MovieListService.java
│   │
│   ├── adapter/                            # Adapter Layer - External Communication
│   │   ├── in/                             # Input Adapters
│   │   │   ├── web/
│   │   │   │   ├── controller/             # REST Controllers
│   │   │   │   │   ├── UserController.java
│   │   │   │   │   ├── MovieController.java
│   │   │   │   │   ├── RatingController.java
│   │   │   │   │   ├── ReviewController.java
│   │   │   │   │   ├── RecommendationController.java
│   │   │   │   │   ├── MessageController.java
│   │   │   │   │   └── MovieListController.java
│   │   │   │   │
│   │   │   │   └── dto/                    # Data Transfer Objects
│   │   │   │       ├── UserRegistrationRequest.java
│   │   │   │       ├── UserResponse.java
│   │   │   │       ├── MovieResponse.java
│   │   │   │       ├── RatingRequest.java
│   │   │   │       ├── RatingResponse.java
│   │   │   │       ├── ReviewRequest.java
│   │   │   │       ├── ReviewResponse.java
│   │   │   │       ├── RecommendationRequest.java
│   │   │   │       ├── RecommendationResponse.java
│   │   │   │       ├── MessageRequest.java
│   │   │   │       ├── MessageResponse.java
│   │   │   │       ├── MovieListRequest.java
│   │   │   │       └── MovieListResponse.java
│   │   │   │
│   │   │   └── websocket/                  # WebSocket Controllers
│   │   │       └── WebSocketMessageController.java
│   │   │
│   │   └── out/                            # Output Adapters
│   │       └── external/
│   │           └── TmdbMovieApiAdapter.java # TMDB API integration
│   │
│   └── config/                             # Configuration Classes
│       ├── WebSocketConfig.java            # WebSocket configuration
│       ├── CorsConfig.java                 # CORS configuration
│       └── RestTemplateConfig.java         # HTTP client configuration
│
└── src/main/resources/
    └── application.properties              # Application configuration
```

## Hexagonal Architecture Layers

### 1. Domain Layer (Center)
- **Pure business logic** - no external dependencies
- Contains entity models representing core business concepts
- Framework-agnostic - can be tested without Spring

### 2. Application Layer (Middle)
- **Use Cases** - define what the application can do
- **Services** - implement business logic
- **Ports** - interfaces that define contracts:
  - **Input Ports (Use Cases)**: What the application offers
  - **Output Ports (Repositories)**: What the application needs

### 3. Adapter Layer (Outside)
- **Input Adapters**: How external world communicates with app
  - REST Controllers (HTTP/JSON)
  - WebSocket Controllers (real-time messaging)
- **Output Adapters**: How app communicates with external systems
  - MongoDB Repositories (data persistence)
  - TMDB API Client (external movie data)

## Key Features Implementation

### User Management
- **Register users** with genre preferences
- **Friend system** - add/remove friends
- **Personal movie list** - track watched movies
- **Custom lists** - create themed movie collections

### Movie Discovery
- **Fetch latest movies** filtered by user preferences
- **Search movies** by title/keywords
- **Similar movies** - get recommendations based on a movie
- **Personalized recommendations** - based on user preferences
- **Integration with TMDB API** for real movie data

### Social Features
- **Recommend movies to friends** with personal messages
- **Real-time messaging** via WebSocket
- **View pending recommendations** from friends
- **Conversation history** between users

### Ratings & Reviews
- **Rate movies** on 1-10 scale
- **Write reviews** with text content
- **View aggregate ratings** for any movie
- **Personal rating/review history**

## Data Flow Examples

### Example 1: Get Personalized Movie Recommendations

```
1. Client → REST Controller (GET /api/movies/user/{userId}/recommendations)
2. Controller → UserService (getUserById)
3. UserService → UserRepository → MongoDB
4. Controller → MovieService (getRecommendedMoviesBasedOnGenres)
5. MovieService → TmdbMovieApiAdapter (fetchLatestMoviesByGenres)
6. TmdbMovieApiAdapter → TMDB API (HTTP request)
7. TMDB API → TmdbMovieApiAdapter (movie data)
8. MovieService → Controller → Client (movie recommendations)
```

### Example 2: Send Real-time Message

```
1. Client → WebSocket (STOMP message to /app/chat.send)
2. WebSocketController → CommunicationService (sendMessage)
3. CommunicationService → MessageRepository → MongoDB (persist message)
4. WebSocketController → SimpMessagingTemplate
5. SimpMessagingTemplate → Recipient Client (via /user/{userId}/queue/messages)
```

### Example 3: Rate a Movie

```
1. Client → REST Controller (POST /api/ratings)
2. Controller → RatingService (rateMovie)
3. RatingService → RatingRepository → MongoDB (save rating)
4. RatingService → MovieRepository → MongoDB (update average rating)
5. Controller → Client (rating response)
```

## Technology Stack

- **Framework**: Spring Boot 3.5.7
- **Language**: Java 21
- **Database**: MongoDB
- **Real-time**: WebSocket with STOMP
- **External API**: TMDB (The Movie Database)
- **Build Tool**: Maven
- **Libraries**:
  - Lombok (reduce boilerplate)
  - Spring Data MongoDB (database access)
  - Spring WebSocket (real-time communication)
  - RestTemplate (HTTP client)

## Benefits of Hexagonal Architecture

1. **Testability**: Core business logic independent of frameworks
2. **Flexibility**: Easy to swap implementations (e.g., change from MongoDB to PostgreSQL)
3. **Maintainability**: Clear separation of concerns
4. **Domain-focused**: Business logic at the center
5. **Technology-agnostic**: Domain doesn't know about Spring, MongoDB, etc.

## Future Enhancements

1. **Security**: 
   - JWT authentication
   - Password hashing (BCrypt)
   - Role-based access control

2. **Validation**:
   - Request validation with @Valid
   - Custom validators for business rules

3. **Error Handling**:
   - Global exception handler
   - Custom error responses
   - Logging and monitoring

4. **Performance**:
   - Redis caching for movie data
   - Pagination for large result sets
   - Database indexing

5. **Documentation**:
   - Swagger/OpenAPI integration
   - API documentation generation

6. **Testing**:
   - Unit tests for services
   - Integration tests for repositories
   - E2E tests for controllers

7. **Deployment**:
   - Docker containerization
   - CI/CD pipeline
   - Cloud deployment (AWS/Azure/GCP)
