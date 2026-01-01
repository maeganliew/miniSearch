# Mini Search

A backend application for a TikTok-like video platform, supporting video CRUD operations, search, caching, and optional scoring for keyword relevance.

---

## **Tech Stack**

- **Backend:** Java (Spring Boot)
- **Database:** PostgreSQL  
- **Cache:** Redis  
- **Documentation:** Swagger
- **Deployment:** Railway

---

## **Project Structure**

mini-search/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/minisearch/
│       │       ├── config/        # Redis and cache configs
│       │       ├── controller/    # REST controllers (VideoController, SearchController)
│       │       ├── service/       # Business logic (VideoService, SearchService, AsyncService)
│       │       ├── repository/    # DB access (VideoRepository)
│       │       ├── model/         # Video entity
│       │       ├── dto/           # Request/Response DTOs
│       │       └── exception/     # Custom exceptions and handlers
│       └── resources/
│           └── application.properties
├── pom.xml
└── README.md

---

## **Getting Started**

### **1. Prerequisites**

- Java 17+
- PostgreSQL running locally
- Redis running locally
- Maven installed

### **2. Configure application.properties**

```properties
# Database configuration
# Database name: minisearch
spring.datasource.url=jdbc:postgresql://localhost:5432/minisearch
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password

# Redis configuration
spring.redis.host=localhost
spring.redis.port=6379
```

### **3. Run the Application**
```
mvn spring-boot:run
```

## **Swagger Documentation**
Swagger UI is available at:
```
https://minisearch-production.up.railway.app/swagger-ui/index.html
```

## **Example Usage (No Frontend)** 
Using **Postman/Curl**:
```
# Add a video
curl -X POST "http://localhost:8080/videos" \
-H "Content-Type: application/json" \
-d '{
  "title": "Funny Cat Video",
  "description": "A cat doing funny tricks",
  "tags": ["cat", "funny", "pets"],
  "uploaderId": 1,
  "uploadDate": "2026-01-01T12:00:00"
}'

# Search videos
curl "http://localhost:8080/search?q=cat&sort=views"
```

## **Notes**
- Caching via Redis is active for repeated search queries
- Async thumbnail placeholder generation is implemented when a video is added.  
- Simple keyword-based scoring is applied during search to rank results.

## **Future Improvements**
- Dockerize app and database for easier deployment
- Improve search scoring with Natural Language Processing / embeddings
- Add file upload and actual thumbnail generation

