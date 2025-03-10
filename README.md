# URL Shortener
A simple URL shortening service built with Spring Boot and Kotlin.

## Overview
This URL shortener service allows you to convert long URLs into short, easy-to-share links. The service generates unique short codes using MD5 hashing and stores the mappings in a PostgreSQL database.

## Features

- Create short URLs from long ones
- Redirect users from short URLs to original destinations
- Prevent duplicate entries by reusing existing mappings
- Store URL mappings persistently in PostgreSQL

## API Endpoints
Create Short URL
```
POST http://localhost:8080/api/url
```
Request Body:
```
{
  "longUrl": "https://example.com/very/long/url/that/needs/shortening"
}
```
Response:
```
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "shortUrl": "http://localhost:8080/AbC123",
  "createdAt": "2025-03-10T12:30:45"
}
```
Redirect to Original URL
```
GET http://localhost:8080/{shortCode}
```

If the short code exists, the user is redirected to the original URL
If the short code doesn't exist, a 404 response is returned

## Getting Started
### Prerequisites

JDK 11 or higher
Docker and Docker Compose

### Running the Service

Run the entire application stack (including PostgreSQL and the URL shortener service):
```
docker compose up -d
```

### Environment Configuration
The PostgreSQL database is configured with the following defaults:

Username: user
Password: password
Database: shortener
Port: 5432
