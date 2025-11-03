# A Human Resource Payroll Management System
A FYP project during degree study
- Project designed with multi-layered architecture design which having the system, process and experience layer for the API design.

## Tools & Frameworks
### Programming Languages
| Programming Language | Version |
| :------------------- | :------ |
| Java | 21 |
| Python | 3.12 |
| Javascript | - |
| Groovy | - |

### Back-end Microservices
1. payroll-hrms-service

| Tools             | Description                          |
|:------------------|:-------------------------------------|
| SpringBoot        | Spring Boot version 3                |
| Spring Security   | Spring Security Framework            |
| MariaDB           | MariaDB Database server              |
| Hibernate         | Object-Relational Mapping (ORM) tool |
| MyBatis           | Object-Relational Mapping (ORM) tool |
| MyBatis Generator | MyBatis mapper generator             |
| Apache Tomcat     | Apache tomcat server                 |
| Lombok            | Getter/Setter library                |
| JWT               | Json Web Token                       |
| OpenAPI           | API documentation generator          |
| Caffeine          | Java caching library                 |

2. payroll-user-service

| Tools | Description |
| :---- | :---------- |
| FastAPI | Python HTTP framework |
| Pydantic | Python data validation library |
| MariaDB | MariaDB Database server |
| Redis | Redis in-memory database |
| SQLAlchemy | Object-Relational Mapping (ORM) tool |
| Uvicorn | ASGI web server |
| PyJWT | Python Json Web Token library |
| Asyncio | Python asynchorize library for concurrent programming |
| theine | Python caching library |

3. payroll-product-management (To be implement)
- A project which will manage the product related features such as order management, stock management and customer related system.
4. payroll-time-management (To be implement)
- A project which gathered the employee leave application, attendance taking, attendance report generation system
5. payroll-accounting-management (To be implement)
- The core project for the payroll HRMS system which gathered the payroll accounting, tax management, salary management and more. 

### Front-end
| Tools | Description |
| :---- | :---------- |
| React.js | React.js version 19 web UI framework |
| React native | Mobile application UI framework |
| Axios | Promise-based HTTP framework |
| Momentjs | Datetime parsing library |

# Project architecture the Human Resource Payroll Management System
<img src="./doc/resources/payroll_architecture.jpg?v=2" alt="payroll-architecture" style="width:100%; height: 650px;"/>
*** Greyed icon to be implement