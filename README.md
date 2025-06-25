# Stripe Payment Integration – Java Backend Project

This project contains two microservices that together handle end-to-end Stripe payment processing using Spring Boot. Developed during a backend internship at HulkHire Tech.

## 📁 Projects

1. **Payment Processing Service**  
   - Handles order creation, transaction management, and database persistence.
   - Connects with Stripe Provider via REST API.

2. **Stripe Provider Service**  
   - Integrates directly with Stripe APIs to create sessions and process payments.
   - Exposes endpoints for client-side checkout integration and webhook handling.

## 💻 Tech Stack

- Java, Spring Boot
- Stripe API Integration
- Microservices Architecture
- MySQL
- REST APIs
- ActiveMQ (for async messaging)
- AWS EC2, RDS (for cloud deployment)

## ⚙️ Features

- Secure API design using HMAC SHA256 and environment variables
- Stripe Webhook handling
- Error handling and logging (SLF4J)
- MySQL + Spring JDBC for persistence
- Docker-ready structure (optional)
- CI/CD-friendly architecture

## 🚀 Deployment

- Services deployed on AWS EC2 with external MySQL DB via RDS
- Properties handled via `.properties` and environment variables (No keys pushed!)

## 🏆 Achievements

- Awarded **Star Performer** during internship
- Delivered clean code and real-time Stripe integration in production-like setup

## 📂 Folder Structure

## 👨‍💻 Author

**Kongari Sivaram**  
Java Backend Developer Intern @ HulkHire Tech  
[LinkedIn](https://www.linkedin.com/in/kongari-sivaram/)  
[Email](mailto:sivaramkongari@gmail.com)