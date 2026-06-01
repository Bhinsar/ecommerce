# 🛒 ShoopingCart — Spring Boot & Thymeleaf E-Commerce Platform

[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.4-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Enabled-darkgreen?style=flat-square&logo=thymeleaf)](https://www.thymeleaf.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Enabled-blue?style=flat-square&logo=postgresql)](https://www.postgresql.org/)
[![Spring Security](https://img.shields.io/badge/Spring_Security-Enabled-green?style=flat-square&logo=springsecurity)](https://spring.io/projects/spring-security)

**ShoopingCart** is a production-ready, full-stack MVC e-commerce platform built with **Spring Boot** on the backend and interactive **Thymeleaf** templates on the frontend. It features secure customer authentication, role-based page authorization, product and category management with file uploading, an intuitive shopping cart workflow, and automated account locking to safeguard accounts against brute-force logins.

---

## 🚀 Key Features

### 👤 Customer & Shopping Experience
*   **🔒 Secure Authentication**: Robust user authentication backed by Spring Security, using BCrypt for password hashing.
*   **🛒 Shopping Cart**: Seamless addition and removal of items, with dynamic quantity adjustments and automatic calculation of total prices.
*   **📂 Structured Browsing**: View all products, filter products by distinct categories, and load detailed product descriptions and stock levels.
*   **📧 Password Recovery**: Request a password reset link sent to your registered email address using Spring Boot Starter Mail (SMTP) and secure UUID tokens.

### 🛡️ Security & Account Protection
*   **🔒 Account Lockout Policy**: Tracks and limits failed login attempts. If a user exceeds **10 failed attempts**, the account locks automatically.
*   **🔓 Auto-Unlock Mechanism**: The account remains locked until the cooldown timer expires, providing security against brute-force attacks.
*   **🏷️ Role-Based Access Control**: Strict route categorization where `/user/**` requires the `ROLE_USER` role, and `/admin/**` requires the `ROLE_ADMIN` role.

### 💼 Admin Management Panel
*   **📊 Category Manager**: Create, view, update, and delete product categories. Includes dedicated category image upload capabilities.
*   **📦 Product Inventory**: Manage items by cataloging their names, descriptions, prices, discounts, stock levels, and uploading product images.
*   **👥 User Account Operations**: Monitor and manage registered users. Admins can lock, unlock, enable, or disable any customer account.

---

## 🛠️ Technology Stack

*   **Core Framework**: Spring Boot 3.3.4 (Java 21)
*   **Template Engine**: Thymeleaf (HTML5/CSS3)
*   **Database & ORM**: PostgreSQL, Spring Data JPA, Hibernate
*   **Security**: Spring Security, BCrypt Hashing, DaoAuthenticationProvider
*   **Email Engine**: Spring Boot Starter Mail (SMTP Integration)
*   **Utilities**: Lombok, Spring Boot DevTools, Maven Wrapper

---

## 📂 Directory Structure

```text
ecommerce/
├── .mvn/                       # Maven Wrapper configuration files
├── src/
│   ├── main/
│   │   ├── java/com/example/ecommerce/
│   │   │   ├── config/         # Security, user details services, and custom authentication handlers
│   │   │   ├── controller/     # MVC controllers (AdminController, HomeController, UserController)
│   │   │   ├── modul/          # JPA entity models (Cart, Category, Product, Users)
│   │   │   ├── repository/     # Spring Data JPA repositories (CartRepo, CategoryRepo, ProductRepo, UserRepo)
│   │   │   ├── service/        # Services (CartService, CategoryService, CommService, ProductService, UserService)
│   │   │   └── EcommerceApplication.java # Spring Boot main startup application
│   │   └── resources/
│   │       ├── static/         # Static assets (images under /img, stylesheets, scripts)
│   │       ├── templates/      # HTML UI templates (nav bar fragments, admin & user panels, login & register)
│   │       └── application.properties # Main application properties configuration file
│   └── test/                   # Unit and integration test suites
├── mvnw                        # Maven Wrapper executable for Linux/macOS
├── mvnw.cmd                    # Maven Wrapper executable for Windows
├── pom.xml                     # Maven project descriptor and dependencies
└── README.md                   # Project documentation
```

---

## ⚙️ Configuration

### 1. Database Configuration
Open [application.properties](file:///e:/ecommerce/src/main/resources/application.properties) and update your database credentials to match your PostgreSQL server:

```properties
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> [!NOTE]  
> Create a database named `ecommerce` in your PostgreSQL instance before launching the application.

### 2. SMTP Mail Configurations
To enable password reset emails, append the following properties to [application.properties](file:///e:/ecommerce/src/main/resources/application.properties):

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_gmail@gmail.com
spring.mail.password=your_gmail_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

> [!TIP]  
> If using Gmail, make sure to generate and use a **Google App Password** instead of your raw Gmail password.

### 3. File Upload Paths Configuration (CRITICAL)
> [!WARNING]  
> The application uses a hardcoded directory for saving uploaded files (such as category, product, and profile images):
> - `D:/ecommerce/src/main/resources/static/img`
>
> If you do not have a `D:` drive or your project directory is located elsewhere, you **MUST** update this path to prevent uploads from crashing:
> 1. In [AdminController.java](file:///e:/ecommerce/src/main/java/com/example/ecommerce/controller/AdminController.java) (lines 85 and 150)
> 2. In [HomeController.java](file:///e:/ecommerce/src/main/java/com/example/ecommerce/controller/HomeController.java) (line 135)
>
> Update them to match your local project's static image folder (e.g., `e:/ecommerce/src/main/resources/static/img` or absolute path on your system).

---

## 🏃 Quick Start

### 1. Set Up Database
Create the target database in your local PostgreSQL terminal:
```sql
CREATE DATABASE ecommerce;
```

### 2. Build and Run
Execute the application using the Maven wrapper in the project directory:

#### On Windows:
```cmd
mvnw.cmd spring-boot:run
```

#### On Linux / macOS:
```bash
chmod +x mvnw
./mvnw spring-boot:run
```

Once started, the application will run locally at **`http://localhost:8080`**.

---

## 🔌 Core MVC Routes

### Public Routes
*   `GET /` — Home page showing latest products.
*   `GET /products` — All products catalog categorized.
*   `GET /product/{categoryName}` — Filters products by selected category.
*   `GET /view/{productId}` — Product page for details.
*   `GET /signin` — Login interface.
*   `GET /register` — User signup interface.

### Secured User Routes (`/user/**`)
*   `GET /user/cart` — Customer shopping cart view.
*   `GET /user/addCart` — Adds an item to the shopping cart.
*   `POST /user/cartQuantityUpdate` — Updates item quantity.
*   `GET /user/cartDelete` — Deletes item from cart.

### Secured Admin Routes (`/admin/**`)
*   `GET /admin/category` — Manage categories (Create / Delete).
*   `GET /admin/product` — Product add interface.
*   `GET /admin/allproduct` — Manage products (Edit / Update / Delete).
*   `GET /admin/users` — View all users and toggle active/disabled states.
