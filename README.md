# 🎬 Movie Reservation System

A scalable and production-inspired backend system for managing movie ticket reservations, built using **Spring Boot**, **PostgreSQL**, **Redis**, and **Apache Kafka**.

This project demonstrates modern backend engineering concepts including:

- 🔐 JWT Authentication & Authorization
- ⚡ Redis Caching
- 📩 Kafka Event Streaming
- 🗄️ PostgreSQL Persistence
- 🎭 Theatre & Show Management
- 🎟️ Seat Reservation Handling
- 🔄 Concurrent Booking Management
- 🛡️ Spring Security Integration

---

# 🚀 Tech Stack

| Technology | Purpose |
|---|---|
| ☕ Java 17 | Core Programming Language |
| 🌱 Spring Boot | Backend Framework |
| 🔐 Spring Security | Authentication & Authorization |
| 🗄️ PostgreSQL | Relational Database |
| ⚡ Redis | Caching / Fast Access |
| 📩 Apache Kafka | Event Streaming |
| 🧩 Hibernate / JPA | ORM |
| 🐳 Docker | Running Redis & Kafka |
| 📦 Maven | Dependency Management |

---

# ✨ Features

## 👤 Authentication & Authorization
- JWT-based authentication
- Role-based access control
- Secure API endpoints

---

## 🎥 Movie Management
- Add movies
- Manage movie details
- Schedule shows

---

## 🏢 Theatre Management
- Manage theatres
- Configure screens & shows

---

## 🎟️ Reservation System
- Book movie tickets
- Seat reservation support
- Prevent concurrent booking conflicts

---

## 📩 Kafka Integration
- Event-driven reservation activities
- Kafka Producer & Consumer integration
- Async activity monitoring

---

## ⚡ Redis Integration
- High-speed caching
- Distributed synchronization support
- Optimized backend operations

---

## 📊 Monitoring
- Spring Boot Actuator endpoints enabled
- Health monitoring support

---

# 🏗️ Project Architecture

```text
Client
   ↓
REST Controllers
   ↓
Service Layer
   ↓
Repository Layer
   ↓
PostgreSQL
```

Additional integrations:

```text
Redis  → Caching & Fast Access
Kafka  → Event Streaming & Async Processing
```

---

# 📂 Project Structure

```text
src/main/java
│
├── config          # Security, Kafka, Redis configs
├── controller      # REST APIs
├── dto             # Request / Response DTOs
├── entity          # JPA Entities
├── repository      # Database layer
├── security        # JWT & authentication logic
├── service         # Business logic
└── exceptions      # Custom Exceptions
```

---

# ⚙️ Prerequisites

Before running the project, install:

- ☕ Java 17+
- 🐘 PostgreSQL
- 🐳 Docker Desktop
- 📦 Maven

---

# 🐘 PostgreSQL Setup

Create database:

```sql
CREATE DATABASE movie_reservation;
```

---

# ⚙️ Configure application.properties

Update your `application.properties` file:

```properties
spring.application.name=theatre

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/movie_reservation
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379

# JWT
jwt.secret.key=YOUR_SECRET_KEY

# Kafka
spring.kafka.bootstrap-servers=localhost:9092

spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer

spring.kafka.consumer.group-id=theatre-monitoring-group
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer

spring.kafka.consumer.properties.spring.json.trusted.packages=com.project.movie.reservation.entity,com.project.movie.reservation.dto

# Actuator
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always
```

---

# 🐳 Docker Setup

## ⚡ Start Redis

```bash
docker run -d --name redis -p 6379:6379 redis
```

---

## 🌐 Create Kafka Network

```bash
docker network create kafka-net
```

---

## 🧩 Start ZooKeeper

```bash
docker run -d --name zookeeper --network kafka-net -p 2181:2181 -e ZOOKEEPER_CLIENT_PORT=2181 confluentinc/cp-zookeeper
```

---

## 📩 Start Kafka

```bash
docker run -d --name kafka --network kafka-net -p 9092:9092 -e KAFKA_BROKER_ID=1 -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 confluentinc/cp-kafka:7.4.0
```

---

# ▶️ How To Run The Project

## 1️⃣ Clone Repository

```bash
git clone https://github.com/Puneeth225/MovieReservationSystem.git
```

---

## 2️⃣ Navigate To Project

```bash
cd MovieReservationSystem
```

---

## 3️⃣ Start Docker Containers

### Start Redis

```bash
docker start redis
```

### Start ZooKeeper

```bash
docker start zookeeper
```

### Start Kafka

```bash
docker start kafka
```

---

## 4️⃣ Verify Running Containers

```bash
docker ps
```

You should see:

- redis
- zookeeper
- kafka

running successfully.

---

## 5️⃣ Run Spring Boot Application

### Using Maven

```bash
mvn spring-boot:run
```

---

## 6️⃣ Access Application

Application runs on:

```text
http://localhost:8080
```

---

# 🧪 API Testing

You can test APIs using:

- 📮 Postman

---

# 📚 Concepts Demonstrated

- 🔐 Authentication & Authorization
- ⚡ Caching with Redis
- 📩 Event-driven Architecture
- 🧵 Concurrent Booking Handling
- 🗄️ Relational Database Design
- 🛡️ Secure Backend APIs
- 📦 RESTful Service Design
- 🚀 Scalable Backend Practices

---

# 👨‍💻 Author

## Puneeth Sharma

- GitHub: https://github.com/Puneeth225

---

# ⭐ If you like this project

Give it a ⭐ on GitHub!
