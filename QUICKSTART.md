# Quick Start Guide

## Prerequisites Checklist

- [ ] Java 21 installed (`java -version`)
- [ ] Maven 3.8+ installed (`mvn -version`)
- [ ] MongoDB running (local or Docker)
- [ ] TMDB API key obtained from https://www.themoviedb.org/settings/api

## Setup Steps

### 1. Clone and Navigate
```bash
cd watchcrew-be
```

### 2. Configure TMDB API Key

**Option A: Environment Variable (Recommended)**
```bash
# Linux/Mac
export TMDB_API_KEY=your_actual_api_key_here

# Windows PowerShell
$env:TMDB_API_KEY="your_actual_api_key_here"

# Windows CMD
set TMDB_API_KEY=your_actual_api_key_here
```

**Option B: Edit application.properties**
```properties
tmdb.api.key=your_actual_api_key_here
```

### 3. Start MongoDB

**Option A: Using Docker (Recommended)**
```bash
docker run -d -p 27017:27017 --name watchcrew-mongodb mongo:latest
```

**Option B: Local Installation**
- Ensure MongoDB is running on `localhost:27017`
- Database `watchcrew` will be created automatically

### 4. Build the Project
```bash
./mvnw clean install
```

**Windows:**
```powershell
.\mvnw.cmd clean install
```

### 5. Run the Application
```bash
./mvnw spring-boot:run
```

**Windows:**
```powershell
.\mvnw.cmd spring-boot:run
```

### 6. Verify It's Running

Open browser or use curl:
```bash
curl http://localhost:8080/api/movies/latest?genres=ACTION
```

You should see movie data returned (if TMDB API is configured).

## Testing the API

### Quick Test Sequence

1. **Register a user:**
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "preferences": ["ACTION", "SCI_FI"]
  }'
```

Save the returned `id` for next steps.

2. **Get movie recommendations:**
```bash
curl -X GET "http://localhost:8080/api/movies/user/{userId}/recommendations"
```

3. **Search for a movie:**
```bash
curl -X GET "http://localhost:8080/api/movies/search?query=matrix"
```

## Common Issues & Solutions

### Issue: "TMDB API key not configured"
**Solution:** Set the TMDB_API_KEY environment variable or update application.properties

### Issue: "Connection refused" to MongoDB
**Solution:** 
- Ensure MongoDB is running: `docker ps` (should see mongodb container)
- Or start it: `docker start watchcrew-mongodb`
- Check connection string in application.properties

### Issue: Port 8080 already in use
**Solution:** 
- Change port in application.properties: `server.port=8081`
- Or kill the process using port 8080

### Issue: Build fails with "cannot find symbol"
**Solution:**
- Ensure Java 21 is being used: `java -version`
- Clean and rebuild: `./mvnw clean install`
- Check Lombok is configured in your IDE

## IDE Setup

### IntelliJ IDEA
1. Open project as Maven project
2. Enable Lombok plugin: Settings → Plugins → Install "Lombok"
3. Enable annotation processing: Settings → Build → Compiler → Annotation Processors → Enable

### VS Code
1. Install "Extension Pack for Java"
2. Install "Spring Boot Extension Pack"
3. Install "Lombok Annotations Support"

### Eclipse
1. Import as Maven project
2. Install Lombok: Download from https://projectlombok.org/ and run the installer
3. Restart Eclipse

## Development Workflow

### Hot Reload (Spring DevTools)
Add to pom.xml for auto-restart on code changes:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

### MongoDB GUI
- **MongoDB Compass**: https://www.mongodb.com/products/compass
- Connect to: `mongodb://localhost:27017`
- Database: `watchcrew`

### API Testing Tools
- **Postman**: Import curl commands from API_EXAMPLES.md
- **Insomnia**: Alternative to Postman
- **curl**: Command-line testing (examples in API_EXAMPLES.md)

## Monitoring & Logs

### View Application Logs
```bash
tail -f logs/spring.log
```

### Check MongoDB Collections
```bash
docker exec -it watchcrew-mongodb mongosh
use watchcrew
show collections
db.users.find().pretty()
```

### Health Check
```bash
curl http://localhost:8080/actuator/health
```
(Requires Spring Actuator - uncomment in pom.xml)

## Next Steps

1. ✅ API is running
2. 📖 Read ARCHITECTURE.md to understand the structure
3. 🧪 Try examples from API_EXAMPLES.md
4. 🔒 Implement authentication (Spring Security + JWT)
5. ✍️ Add request validation
6. 📝 Add Swagger documentation
7. 🧪 Write tests
8. 🚀 Deploy to cloud

## Support & Resources

- **TMDB API Docs**: https://developers.themoviedb.org/3
- **Spring Boot Docs**: https://docs.spring.io/spring-boot/
- **MongoDB Docs**: https://docs.mongodb.com/
- **WebSocket/STOMP**: https://docs.spring.io/spring-framework/reference/web/websocket.html

## Project Structure Quick Reference

```
Key Files:
├── pom.xml                         # Dependencies
├── application.properties          # Configuration
├── WatchCrewApplication.java       # Main entry point
├── domain/model/                   # Business entities
├── application/service/            # Business logic
├── adapter/in/web/controller/      # REST endpoints
└── adapter/out/external/           # External API integration
```

Happy coding! 🎬🍿
