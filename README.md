A comprehensive banking application built with Spring Boot, featuring user authentication, account management, transactions, loans, and automated notifications.

# Features

## User Management

- User registration with role-based access (Customer, Employee, Admin)
- JWT-based authentication and authorization
- Secure password hashing with BCrypt
- Role-specific endpoint protection

## Account Management

- Create multiple accounts per user (Savings/Checking)
- View account balances and details
- Account-to-account transfers
- Scheduled transfers for future dates

## Transaction Processing

- Deposits and withdrawals
- Complete transaction history
- Real-time balance updates
- Transaction auditing with timestamps

## Loan System

- Multiple loan types:

- Mortgage Loans (30-year, 6.4% APR)
- Auto Loans (7-year, 6.8% APR)
- Student Loans (10-year, 6.39% APR)
- Personal Loans (7-year, 13.74% APR)


- Automated monthly payment schedules
- Manual and automatic payment options
- Payment tracking and history
- Late payment penalty system

## Branch Management

- Create and manage bank branches
- Assign employees to branches
- Register customers to specific branches
- Branch-specific operations tracking

## Notifications & Reports

- Email notifications for:

- Account creation
- Transactions (deposits/withdrawals)
- Transfers and reversals
- Loan payments and due dates
- Branch assignments


- Automated monthly account statements (PDF)
On-demand account reports by date range

## Administrative Features

- Transfer reversal capabilities
- View all system transactions
- User and account management
- Notification history tracking

#  Tech Stack

## Backend Framework:

- Java 17
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- Spring Mail

## Database:

- MySQL 8.x
- Hibernate ORM

## Security:

- JWT (JSON Web Tokens)
- BCrypt password encryption
- Role-based access control (@PreAuthorize)

## Additional Technologies:

- Maven (dependency management)
- Lombok (boilerplate reduction)
- JSON (org.json library for reporting)
- Jakarta Validation (input validation)
- Swagger/OpenAPI (API documentation)

## Scheduling:

- Spring @Scheduled (cron jobs for automated tasks)
- @Async (asynchronous email processing)

# Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- SMTP server access (for email notifications)

## Getting Started
### 1. Clone the Repository
- git clone https://github.com/ejohchisimdi-collab/Banking-api-v1.git
- cd Baking-api-vi
### 2. Configure Environment Variables
- Create a .env file in the project root:
- pring.datasource.url=jdbc:mysql:// localhost:3306/banking_db
- spring.datasource.username=your_mysql_username
- spring.datasource.password=your_mysql_password

- spring.mail.username=your_email@gmail.com
- spring.mail.password=your_app_password

- jwt.Secret=YourSecretKeyMustBeAtLeast32CharactersLongForHMAC
- jwt.Expiration=86400000
- Note: For Gmail, use an App Password instead of your regular password.
### 3. Create Database
- sqlCREATE DATABASE banking_db;
- Spring Boot will automatically create the tables on first run.
### 4. Build and Run
- click on the triple dots in the run configuration in intellij.
- click on edit
- In environment variables give the link for the .env file

# Run the application
- mvn spring-boot:run
- The application will start on `http://localhost:8081`

Authentication Flow

Register a user:
```
POST /users/register
{
  "userName": "john_doe",
  "name": "John",
  "roles": "Customer",
  "contactInfo": "john@example.com",
  "password": "securePassword123"
}
```
Login to receive JWT token:
```
POST /users/login
{
  "userName": "john_doe",
  "password": "securePassword123"
}
```
 # Security Features

- JWT Authentication: Stateless authentication using JSON Web Tokens
- Password Encryption: BCrypt hashing for secure password storage
- Role-Based Authorization: Three user roles with specific permissions:

- Customer: Account and transaction operations
- Employee: View transactions, assist customers
- Admin: Full system access, user management


- Method-Level Security: @PreAuthorize annotations on endpoints
- User-Specific Access: Users can only access their own data

# Automated Tasks
- The system runs scheduled tasks:

- Hourly:

- Process automatic loan payments
- Execute scheduled transfers Monthly (Last Day):

- Generate and email account statements
- Check for overdue loan payments
- Apply late payment penalties



# Email Notifications
## Users receive automated emails for:

- Account creation confirmation
- Deposit/withdrawal confirmations
- Transfer notifications
- Loan application approval
- Payment confirmations
- Overdue payment alerts
- Monthly account statements (with PDF attachment)

# Testing
- Run the test suite:
- mvn test
## Test coverage includes:

- Account service operations
- Transaction processing
- Loan calculations and payments
- Transfer operations
- User authentication
- Branch management

# License
- This project is open source and available under the MIT License.
## Author
Chisimdi Ejoh