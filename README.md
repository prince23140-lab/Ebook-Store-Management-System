# EbookStore Management System

A comprehensive Spring Boot-based EbookStore Management System that handles user management, book catalog, shopping cart, orders, payments, and reviews with Rwandan administrative location integration.

## 🚀 Features

### Core Features
- **User Management** - Customer and Admin roles with complete CRUD operations
- **Book Catalog** - Comprehensive book management with categories
- **Shopping Cart** - Add, update, and manage cart items
- **Order Processing** - Complete order lifecycle management
- **Payment Integration** - Multiple payment methods support
- **Review System** - User ratings and comments for books
- **Location Management** - Rwandan administrative hierarchy integration

### Technical Features
- **RESTful API** - Fully documented REST endpoints
- **Spring Data JPA** - Advanced query methods with pagination and sorting
- **PostgreSQL** - Robust relational database
- **UUID Primary Keys** - Secure identifier system
- **Enum Support** - Type-safe status and role management
- **Comprehensive Validation** - Data integrity and business logic validation

## 🛠️ Technology Stack

- **Backend Framework**: Spring Boot 3.5.7
- **Database**: PostgreSQL with Hibernate JPA
- **Java Version**: Java 25
- **Build Tool**: Maven
- **API Documentation**: RESTful endpoints with Postman collection

## 📋 Prerequisites

Before running this application, ensure you have:

- **Java 25** or later
- **Maven 3.6** or later
- **PostgreSQL 12** or later
- **Postman** (for API testing)

**!ER Diagram Image**

<img width="1441" height="809" alt="image" src="https://github.com/user-attachments/assets/223ad4bb-d930-48b9-9b9b-6b01bc055ed6" />


## 🗄️ Database Setup

### 1. PostgreSQL Configuration
```sql
-- Create database
CREATE DATABASE ebook_db;

-- Create user (optional)
CREATE USER postgres WITH PASSWORD 'root';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE ebook_db TO postgres;
```

### 2. Application Properties

<img width="676" height="372" alt="image" src="https://github.com/user-attachments/assets/ad079117-3460-49d1-b896-98611ba3e4d6" />

### 🏙️ Rwanda Location Hierarchy
Province (5)
 └── District (30+)
     └── Sector (400+)
         └── Cell (2,000+)
             └── Village (14,000+)

---
## 🏗️ Project Structure

```
src/main/java/auca/ac/rw/ebook/
├── controller/          # REST API Controllers
│   ├── UserController.java
│   ├── BookController.java
│   ├── CategoryController.java
│   ├── CartController.java
│   ├── OrderController.java
│   ├── OrderDetailController.java
│   ├── PaymentController.java
│   ├── ReviewController.java
│   └── LocationController.java
├── model/              # Entity Classes
│   ├── User.java
│   ├── Book.java
│   ├── Category.java
│   ├── Cart.java
│   ├── Order.java
│   ├── OrderDetail.java
│   ├── Payment.java
│   ├── Review.java
│   ├── Location.java
│   └── enums/
│       ├── EUserRole.java
│       ├── EOrderStatus.java
│       ├── EPaymentMethod.java
│       └── EPaymentStatus.java
├── repository/         # Data Access Layer
│   ├── UserRepository.java
│   ├── BookRepository.java
│   ├── CategoryRepository.java
│   ├── CartRepository.java
│   ├── OrderRepository.java
│   ├── OrderDetailRepository.java
│   ├── PaymentRepository.java
│   ├── ReviewRepository.java
│   └── LocationRepository.java
├── service/           # Business Logic Layer
│   ├── UserService.java
│   ├── BookService.java
│   ├── CategoryService.java
│   ├── CartService.java
│   ├── OrderService.java
│   ├── OrderDetailService.java
│   ├── PaymentService.java
│   ├── ReviewService.java
│   └── LocationService.java
└── EbookApplication.java  # Main Application Class
```

## 🚀 Running the Application


### 1. Run the Application
```bash
# Run using Maven
mvn spring-boot:run

# Or run the JAR file
java -jar target/ebook-0.0.1-SNAPSHOT.jar
```

### 2. Verify Startup
You should see:
```
Tomcat initialized with port 8081 (http)
Started EbookApplication in X.XXX seconds
```

### Run Image:

<img width="1322" height="316" alt="image" src="https://github.com/user-attachments/assets/611a4823-475a-4cfd-b534-fe224cd356f8" />




## 📚 API Endpoints

### Users Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/user/save` | Create new user |
| GET | `/api/user/all` | Get all users |
| GET | `/api/user/{id}` | Get user by ID |
| GET | `/api/user/email/{email}` | Get user by email |
| GET | `/api/user/role/{role}` | Get users by role |
| GET | `/api/user/province/{province}` | Get users by province |
| PUT | `/api/user/update` | Update user |
| DELETE | `/api/user/delete/{id}` | Delete user |

### Books Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/book/save` | Add new book |
| GET | `/api/book/all` | Get all books |
| GET | `/api/book/{id}` | Get book by ID |
| GET | `/api/book/title/{title}` | Get book by title |
| GET | `/api/book/author/{author}` | Get books by author |
| GET | `/api/book/category/{categoryId}` | Get books by category |
| PUT | `/api/book/update` | Update book |
| DELETE | `/api/book/delete/{id}` | Delete book |

### Categories Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/category/save` | Create category |
| GET | `/api/category/all` | Get all categories |
| GET | `/api/category/{id}` | Get category by ID |
| GET | `/api/category/name/{name}` | Get category by name |
| PUT | `/api/category/update` | Update category |
| DELETE | `/api/category/delete/{id}` | Delete category |

### Cart Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/cart/save` | Add item to cart |
| GET | `/api/cart/user/{userId}` | Get user's cart |
| GET | `/api/cart/user/{userId}/total-price` | Calculate cart total |
| PUT | `/api/cart/update` | Update cart item |
| DELETE | `/api/cart/delete/{id}` | Remove cart item |
| DELETE | `/api/cart/user/{userId}/clear` | Clear user cart |

### Orders Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/order/save` | Create order |
| GET | `/api/order/all` | Get all orders |
| GET | `/api/order/user/{userId}` | Get user orders |
| GET | `/api/order/status/{status}` | Get orders by status |
| PUT | `/api/order/update` | Update order |
| PUT | `/api/order/{orderId}/status/{status}` | Update order status |
| DELETE | `/api/order/delete/{id}` | Delete order |

### Payments Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/payment/save` | Create payment |
| GET | `/api/payment/all` | Get all payments |
| GET | `/api/payment/user/{userId}` | Get user payments |
| PUT | `/api/payment/{paymentId}/process` | Process payment |
| DELETE | `/api/payment/delete/{id}` | Delete payment |

### Reviews Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/review/save` | Add review |
| GET | `/api/review/all` | Get all reviews |
| GET | `/api/review/book/{bookId}` | Get book reviews |
| GET | `/api/review/user/{userId}` | Get user reviews |
| PUT | `/api/review/update` | Update review |
| DELETE | `/api/review/delete/{id}` | Delete review |

### Locations Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/location/save` | Create location |
| GET | `/api/location/all` | Get all locations |
| GET | `/api/location/province/{province}` | Get locations by province |
| GET | `/api/location/users/by-province/{province}` | Get users by province |
| PUT | `/api/location/update` | Update location |
| DELETE | `/api/location/delete/{id}` | Delete location |

## 🧪 Testing with Postman

### 1. Import Postman Collection
1. Open Postman
2. Click **Import**
3. Use the provided collection JSON file

### 2. Testing Sequence
Follow this exact order for testing:

1. **Create Locations** → **Create Users** → **Create Categories** → **Create Books**
2. **Add to Cart** → **Create Orders** → **Add Order Details**
3. **Process Payments** → **Add Reviews**

### 3. Sample Test Data

#### Create Location:

<img width="855" height="664" alt="image" src="https://github.com/user-attachments/assets/a3fa309b-a722-40fa-aeec-70a4ebf9e59a" />


#### Create User:

<img width="844" height="629" alt="image" src="https://github.com/user-attachments/assets/a1da9996-7314-47c5-a8d7-020195bfcc5f" />


#### Create Book:

<img width="846" height="541" alt="image" src="https://github.com/user-attachments/assets/ad4c4dc9-880c-4089-b2d8-dc7a780e25de" />


## 📊 Database Schema

### Key Entities and Relationships

#### One-to-One Relationships:
- **Order ↔ Payment** - One order has one payment

#### One-to-Many Relationships:
- **Location → User** - One location can have many users
- **User → Cart** - One user can have many cart items
- **User → Order** - One user can have many orders
- **User → Payment** - One user can make many payments
- **User → Review** - One user can write many reviews
- **Category → Book** - One category can have many books
- **Book → Cart** - One book can be in many carts
- **Book → OrderDetail** - One book can be in many order details
- **Book → Review** - One book can have many reviews
- **Order → OrderDetail** - One order can have many order details

#### Many-to-Many Relationships (via association entities):
- **User ↔ Book** through **Cart** and **Review**
- **Order ↔ Book** through **OrderDetail**

## 🎯 Midterm Requirements Implementation

### ✅ Mandatory Requirements Met:

1. **5+ Well-Defined Classes**: 9 entities implemented
2. **Complete CRUD Operations**: All entities have full CRUD
3. **JPA Repository Methods**: 
   - `findBy...` queries
   - `existsBy...` queries

<img width="1251" height="708" alt="image" src="https://github.com/user-attachments/assets/e691a059-3e50-474e-a8f3-27f373f23743" />

  
   -   
   - Sorting and Pagination
   - Custom query methods
4. **Rwandan Location Table**: Full hierarchy implemented
   - Province → District → Sector → Cell → Village
5. **User-Location Relationship**: API endpoints for province-based queries
6. **All Three Relationship Types**:
   - One-to-One (Order-Payment)
   - One-to-Many (User-Orders, Category-Books, etc.)
   - Many-to-Many (User-Book through Cart/Review)

## 🔧 Configuration

### Application Properties

<img width="676" height="372" alt="image" src="https://github.com/user-attachments/assets/ad079117-3460-49d1-b896-98611ba3e4d6" />


### Enum Types
- **UserRole**: `CUSTOMER`, `ADMIN`
- **OrderStatus**: `PENDING`, `PAID`, `COMPLETED`, `CANCELLED`
- **PaymentMethod**: `CREDIT_CARD`, `MOBILE_MONEY`, `PAYPAL`
- **PaymentStatus**: `PENDING`, `SUCCESSFUL`, `FAILED`

## 🐛 Troubleshooting

### Common Issues and Solutions:

1. **Application won't start**:
   - Check PostgreSQL is running
   - Verify database credentials in application.properties
   - Ensure port 8081 is available

2. **UUID format errors**:
   - Use proper UUID format in requests
   - Get actual IDs from previous responses

3. **Foreign key constraint errors**:
   - Always create parent entities first
   - Follow the testing sequence

4. **Enum value errors**:
   - Use exact enum values: `CUSTOMER`, `ADMIN`, etc.


### Logs and Debugging
- Check Spring Boot console for detailed error messages
- Enable debug logging: `logging.level.auca.ac.rw.ebook=DEBUG`
- Verify Hibernate SQL queries in console

## 📈 Future Enhancements

### Planned Features:
- **Authentication & Authorization** - JWT-based security
- **File Upload** - Book covers and PDF files
- **Email Notifications** - Order confirmations
- **Advanced Search** - Full-text search capabilities
- **Analytics Dashboard** - Sales and user analytics
- **Mobile App** - React Native client

### Technical Improvements:
- **Caching** - Redis integration for performance
- **API Documentation** - Swagger/OpenAPI
- **Docker** - Containerization
- **Testing** - Comprehensive unit and integration tests
- **Monitoring** - Spring Boot Actuator

## 👥 Development Team

- **Prince Nziza** - Full Stack Developer
- **Course**: Web Technology
- **Institution**: AUCA University
- **Academic Project**: Midterm Assessment

## 📄 License

This project is developed for academic purposes as part of the Web Technology course at AUCA University.

## 🤝 Contributing

1. Fork the project
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

## 📞 Support

For technical support or questions about this project, please contact the development team or refer to the Spring Boot documentation.

---

