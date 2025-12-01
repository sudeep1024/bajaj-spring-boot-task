# Bajaj HealthRx Hiring Test - Spring Boot Application

This project is a Spring Boot application designed to solve the Bajaj HealthRx hiring assignment.

## Features
- Automatically triggers on startup.
- Generates a webhook by calling the Bajaj API.
- Solves the SQL problem (Question 1 for odd registration numbers).
- Submits the solution to the received webhook URL using the provided access token.

## Prerequisites
- Java 17 or higher
- Maven

## How to Run

1. **Build the project:**
   ```bash
   mvn clean package
   ```

2. **Run the JAR:**
   ```bash
   java -jar target/hiring-test-0.0.1-SNAPSHOT.jar
   ```

## SQL Solution (Question 1)
The application submits the following SQL query:

```sql
SELECT t.DEPARTMENT_NAME, t.SALARY, t.EMPLOYEE_NAME, t.AGE
FROM (
    SELECT
        d.DEPARTMENT_NAME,
        SUM(p.AMOUNT) AS SALARY,
        CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS EMPLOYEE_NAME,
        TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE,
        RANK() OVER (PARTITION BY d.DEPARTMENT_ID ORDER BY SUM(p.AMOUNT) DESC) as rnk
    FROM EMPLOYEE e
    JOIN DEPARTMENT d ON e.DEPARTMENT_ID = d.DEPARTMENT_ID
    JOIN PAYMENTS p ON e.EMP_ID = p.EMP_ID
    WHERE DAY(p.PAYMENT_TIME) <> 1
    GROUP BY d.DEPARTMENT_ID, d.DEPARTMENT_NAME, e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, e.DOB
) t
WHERE t.rnk = 1
```

## Submission Details
- **Name:** Sudeep
- **RegNo:** 22BCE2751
- **Email:** sudeep@example.com

## GitHub Repository Structure
- `src/`: Source code
- `pom.xml`: Maven configuration
- `target/hiring-test-0.0.1-SNAPSHOT.jar`: The executable JAR file.

**Note:** When pushing to GitHub, ensure the `target` folder is included if you need to provide the raw JAR link, or upload it as a Release asset.
