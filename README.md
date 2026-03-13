# Dentist Appointment Management System

A Java-based web application built with **Spring Boot** that streamlines dental clinic operations and improves patient scheduling.

## Features

### Patient Portal
- **Secure Login & Registration** – patients create accounts and authenticate securely
- **View Availability** – browse available dentists and open time slots
- **Book Appointments** – schedule appointments with chosen dentist, date, time, and treatment type
- **Manage Appointments** – view appointment history and cancel pending/confirmed bookings
- **Personal Profile** – update contact details, address, date of birth, and medical history

### Admin Dashboard
- **Clinic Overview** – statistics on total appointments, today's schedule, and patient count
- **Appointment Management** – view all appointments, filter by date, and update appointment status
- **Daily Schedules** – browse the full schedule for any date, organized by dentist
- **Patient Records** – view all registered patients and their detailed profiles

## Technology Stack

| Layer | Technology |
|-------|------------|
| Language | Java 17 |
| Framework | Spring Boot 3.2 |
| Security | Spring Security (BCrypt) |
| Persistence | Spring Data JPA + H2 (in-memory) |
| Templating | Thymeleaf |
| Build | Maven |

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+

### Run the Application

```bash
mvn spring-boot:run
```

The application starts on **http://localhost:8080**.

### Default Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin` | `admin123` |
| Patient | *(self-register at `/register`)* | — |

### Running Tests

```bash
mvn test
```

## Project Structure

```
src/
├── main/
│   ├── java/com/dentist/appointment/
│   │   ├── config/          # Security & data initializer
│   │   ├── controller/      # AuthController, PatientController, AdminController
│   │   ├── model/           # User, Patient, Dentist, Appointment entities
│   │   ├── repository/      # Spring Data JPA repositories
│   │   └── service/         # Business logic services
│   └── resources/
│       ├── templates/        # Thymeleaf HTML templates
│       │   ├── patient/      # Patient portal pages
│       │   └── admin/        # Admin dashboard pages
│       └── static/css/       # Stylesheet
└── test/                     # Integration tests
```
