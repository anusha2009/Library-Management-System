# Library Management System

A role-based RESTful Library Management System built with **Java 21**, **Spring Boot 3**, **Spring Security**, **H2 (file-based)**, and **Docker**. The system allows **librarians** to manage books and view borrow histories, and **members** to borrow/return books and search/view books.


## Table of Contents

- [Features](#features)
- [Architecture & Design](#architecture--design)
- [Technologies Used](#technologies-used)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Security](#security)
- [Database & Data Persistence](#database--data-persistence)
- [Postman Collection](#postman-collection)
- [Setup Instructions](#setup-instructions)
- [Documentation](#documentation)
- [Future Improvements](#future-improvements)

## Features

- Role-based access control: **Librarian** and **Member**
- Users have details such as username, email, phone number, role (Member or Librarian)
- Allows Librarians to manage books and view borrow history
- The system supports adding, updating, and removing books from the library
- Members are allowed to view books, search for books, borrow and return books
- Each book has details such as title, author, ISBN, publication year, and availability status
- Security: Password encoding, in-DB auth, secure endpoints
- Persistent H2 DB via Docker volume
- Clean, modular code with full test suite
- Easily testable via Postman collection

## Architecture & Design

This application is structured using a standard **layered architecture**:

- **Controller Layer**: REST API endpoints for interaction
- **Service Layer**: Business logic and rule enforcement
- **Repository Layer**: Interacts with the database using Spring Data JPA
- **Model Layer**: Core domain models representing Users, Books, and Borrow Records


### Entities

### User

Represents a user of the system, who can either have a role **Librarian** or a role **Member**

- **Librarians** can manage books, view borrowing history and all member priviliges
- **Members** can view books, search for books, borrow and return books

Key Fields:
- `username` (unique login ID)
- `password` (securely hashed)
- `role` (`ROLE_MEMBER` or `ROLE_LIBRARIAN`)
- `firstName`, `lastName`, `email`, `phoneNumber`

Relationships:
- `One to Many` with `BorrowRecord`
- User can have multiple borrow records

### Book

Represents a physical book in the library. Each book can be borrowed by a member, subject to availability.

Key Fields:
- `title`, `author` 
- `isbn` (unique)
- `publicationYear`
- `available` (whether it's currently borrowed or not)

Relationships:
- `One to Many` with `BorrowRecord`
- A book can be borrowed multiple times

### BorrowRecord

Logs each borrowing event, tying together a **member**, a **book**, and a **librarian**.

Key Fields:
- `member`: the user who borrows the book
- `librarian`: the librarian who issued the book
- `book`: the book being borrowed
- `borrowDate` and `returnDate`
- `returned`: flag to indicate if the book is returned

Relationships:
- `Many to One` with `User` and `Book`
- Multiple borrow records can be associated with a user and a book


## Technologies Used

| Stack        | Tools                                   |
|--------------|-----------------------------------------|
| Language     | Java 21                                 |
| Framework    | Spring Boot 3.4.4, Spring Security      |
| Database     | H2 (file-based with volume persistence) |
| Build Tool   | Maven                                   |
| Auth         | Basic Auth, BCrypt encoded              |
| Testing      | JUnit 5, Mockito, SpringBootTest        |
| Deployment   | Docker & Docker Compose                 |

## API Endpoints

### Auth & Registration

- `POST /register/member`
- `POST /register/librarian`

### Books

| Method | Endpoint              | Role        | Description              |
|--------|-----------------------|-------------|--------------------------|
| GET    | /api/books            | All         | List all books           |
| GET    | /api/books/search     | All         | Search by title/author   |
| GET    | /api/books/{id}       | All         | Get book by ID           |
| POST   | /api/books            | Librarian   | Add book                 |
| PUT    | /api/books/{id}       | Librarian   | Update book              |
| DELETE | /api/books/{id}       | Librarian   | Delete book              |

### Borrow Records

| Method | Endpoint                          | Role       | Description                    |
|--------|-----------------------------------|------------|--------------------------------|
| POST   | /api/borrow/borrow                | All        | Borrow a book                  |
| POST   | /api/borrow/return/{recordId}     | All        | Return a book                  |
| GET    | /api/borrow/history/member/{id}   | Librarian  | Borrow history by member ID    |
| GET    | /api/borrow/history/book/{id}     | Librarian  | Borrow history by book ID      |

## Testing

Tests include:
- Unit tests for service logic
- Integration tests for controller endpoints
- Role-based access verification


## Database & Data Persistence

- Uses **H2 (file-based)** mode for persistent storage
- Path: `./data/librarydb.mv.db` (auto created)
- Volume mapped in Docker as `h2-data`

Data remains intact across Docker restarts, **without manual backup**.

## Security

Role-based access control is enforced via **Spring Security**:

- Endpoints require authentication via HTTP Basic Auth
- Only **Librarians** can add/update/delete books or view borrow history
- Passwords are securely encoded using **BCrypt** during registration

## Postman Collection

[Download here](./Library%20System%20API.postman_collection.json)

- Includes **all endpoints**
- Authorization via Basic Auth
- Sample bodies for all API endpoints

## Setup Instructions

### 1. Clone the Repo
```bash
git clone https://github.com/anusha2009/Library-Management-System.git
cd Library-Management-System
```

### 2. Build the Project
```bash
./mvnw clean install
```

### 3. Build and Run Using Docker Compose

```bash
docker compose up --build
```

### 4. Access URLs

| Service     | URL                                     |
|-------------|-----------------------------------------|
| App         | http://localhost:8080                   |
| H2 Console  | http://localhost:8080/h2-console        |

- H2 Console credentials:
    - JDBC URL : jdbc:h2:file:./data/librarydb
    - Username : sa
    - Password : (leave blank)

Data is persisted using a named Docker volume `h2-data`.

### 5. Test APIs

Test all APIs using the Postman Collection added [here](./Library%20System%20API.postman_collection.json)

All the necessary API body, parameters, authorization is included in the postman collection and this is the flow of testing the APIs:

1. UserController:
    - `Register Users`
        - `POST /register/librarian` – Register a Librarian user
        - `POST /register/member` – Register a Member user

2. BookController:
    - `Add Books`
        - `POST /api/books` – Add a book (LIBRARIAN only)
    - `List Books`
        - `GET /api/books` – List all books
        - `GET /api/books/{id}` – View a single book by ID
    - `Update Books`
        - `PUT /api/books/{id}` – Update a book (LIBRARIAN only)
    - `Search Books`
        - `GET /api/books/search?keyword` - Search books by title or author
    - `Delete Books`
        - `DELETE /api/books/{id}` – Delete a book (LIBRARIAN only)
    - `Add Books`
        - `POST /api/books` – Test this endpoint one more time so that we can test borrow controller 

3. BorrowController
    - `Borrow Books`
        - `POST /api/borrow/borrow?memberId={memberId}&bookId={bookId}&librarianId={librarianId}`  - Member borrows a book 
    - `Return Books`
        - `POST /api/borrow/return?recordId={borrowRecordId}` - Member returns a book
    - `Get Borrow Records`
        - `GET /api/borrow/history/book/{bookId}` - Borrow history of a book (LIBRARIAN only)
        - `GET /api/borrow/history/member/{memberId}` - Borrow history of a member (LIBRARIAN only)

## Documentation 

- The documentation of the project is added [here](./LibraryManagementSystem.pdf)

## Future Improvements

- Switch to **MySQL/PostgreSQL** in production
- Add **DTO layer** for cleaner API responses
- Stricter role based access control
- Develop and implement a front-end component to interact with the application

