# Temperature Converter — JavaFX Application

## 1. Assignment Description

Simple temperature converter with JavaFX UI.

### Requirements

- Develop a graphical user interface using JavaFX.
- Convert temperatures between Celsius and Fahrenheit.
- Display the converted temperature.
- Store conversion records in a MariaDB database.
- Display previous conversions in a history list.
- Implement database access using JDBC and DAO classes.
- Create unit tests using JUnit 5.
- Generate code coverage reports using JaCoCo.
- Automate building, testing, and Docker deployment using Jenkins.
- Run the application in Docker using Xming as an X server.
- Publish the Docker image to Docker Hub.

### Deliverables

- [GitHub repository](https://github.com/unnap/OTP1_inclassAssignment3/)
- Docker Hub image: `tunnap/otp1:latest`
- Screenshots demonstrating the application running through Xming.

## 2. Technologies & Tools Used

| Technology | Purpose |
|---|---|
| Java 21 | Application development |
| JavaFX | Graphical user interface |
| MariaDB | Relational database |
| JDBC | Database connectivity |
| Maven | Dependency management and building |
| JUnit 5 | Unit testing |
| Mockito | Mocking database dependencies |
| JaCoCo | Code coverage analysis |
| Jenkins | CI/CD pipeline |
| Docker | Application containerization |
| Docker Compose | Running application and database containers |
| Xming | Displaying JavaFX from a Linux Docker container on Windows |
| IntelliJ IDEA | Development environment |
| Git & GitHub | Version control and source code hosting |
| Docker Hub | Docker image repository |
| ChatGPT (OpenAI) | AI-assisted development, code generation, debugging, troubleshooting, and README documentation |

## 3. Design Approach & Implementation Method

### 3.1 Application Structure

The application follows a simple layered design that separates the user interface, temperature calculations, data models, and database operations.

**Main.java**

Creates the JavaFX interface and handles user interactions.

**TemperatureConverter.java**

Contains the original temperature conversion methods and the `isExtremeTemperature()` method. This class was kept unchanged.

**TempCalculator.java**

Uses the original `TemperatureConverter` methods to perform conversions based on the selected temperature unit.

**TemperatureUnit.java**

Represents a temperature unit with an ID, name, and symbol.

**TempRecord.java**

Represents a temperature conversion record containing the input value, original unit, output value, and target unit.

**DBConnection.java**

Establishes a JDBC connection to the MariaDB database using environment variables for configuration.

**TemperatureUnitDAO.java**

Retrieves temperature units from the database.

**TempRecordDAO.java**

Saves conversion records and retrieves conversion history from the database.

**Launcher.java**

Starts the JavaFX application and provides a command-line option for checking database connectivity without opening the graphical interface.

### 3.2 Graphical User Interface

The JavaFX interface contains:

- A text field for entering a temperature.
- A dropdown menu for selecting Celsius or Fahrenheit.
- A Convert button.
- A label displaying the conversion result.
- A list showing previous conversions retrieved from MariaDB.

When the Convert button is pressed, the application reads the entered value and selected unit, performs the conversion, saves the result to the database, and refreshes the conversion history.

The result label changes color based on the `isExtremeTemperature()` method:

- **Red:** Temperature below -40°C or above 50°C.
- **Black:** Temperature within the normal range defined by the method.

For Fahrenheit input, the temperature is converted to Celsius before checking whether it is extreme.

### 3.3 Database Design

MariaDB is used to store temperature units and conversion history.

Two related tables were created.

**TemperatureUnit**

| Column | Description |
|---|---|
| id | Primary key, auto-increment |
| name | Temperature unit name |
| symbol | Temperature unit symbol |

The table contains three temperature units:

| Name | Symbol |
|---|---|
| Celsius | °C |
| Fahrenheit | °F |
| Kelvin | K |

Kelvin is included in the database for possible future functionality, but the current interface supports only Celsius and Fahrenheit.

**TempRecord**

| Column | Description |
|---|---|
| id | Primary key, auto-increment |
| input_value | Original temperature |
| original_unit_id | Foreign key referencing TemperatureUnit |
| output_value | Converted temperature |
| target_unit_id | Foreign key referencing TemperatureUnit |

The two tables have a one-to-many relationship through the unit references in `TempRecord`.

Using separate tables prevents unnecessary duplication of temperature unit information and maintains referential integrity.

### 3.4 Docker Implementation

A multi-stage Dockerfile is used to build the Java application with Maven and run it using Java 21.

The runtime image includes the Linux libraries required by JavaFX.

Docker Compose is used to run two services:

1. A MariaDB container that stores temperature units and conversion history.
2. A JavaFX application container that connects to MariaDB through JDBC.

The database uses a persistent Docker volume so conversion history is retained when containers are restarted.

Xming is used to display the JavaFX window from the Linux application container on Windows.

### 3.5 Jenkins Pipeline

The Jenkinsfile automates the following stages:

1. Checkout the source code from GitHub.
2. Compile the project and execute unit tests using Maven.
3. Generate and archive JaCoCo coverage reports.
4. Build the Docker image.
5. Start a MariaDB container and verify that the application can connect to the database.
6. Push the Docker image to Docker Hub.

Jenkins credentials are used for the database test password and Docker Hub authentication, avoiding hard-coded credentials in the Jenkinsfile.

## 4. Testing & Quality Assurance

### 4.1 Automated Testing

JUnit 5 is used to test the application classes.

Mockito is used to simulate JDBC operations when testing the DAO classes without requiring a running database.

The tests cover temperature calculations, model classes, database connection behavior, DAO operations, and result formatting.

### 4.2 Test Cases and Expected Results

| Test case | Expected result |
|---|---|
| Convert 20°C to Fahrenheit | 68°F |
| Convert 68°F to Celsius | 20°C |
| Convert 0°C to Fahrenheit | 32°F |
| Convert 100°C to Fahrenheit | 212°F |
| Check extreme temperature at 60°C | Extreme temperature detected |
| Check normal temperature at 20°C | Not extreme |
| Check extreme temperature at -50°C | Extreme temperature detected |
| Enter an invalid temperature | Error message displayed |
| Select Celsius or Fahrenheit | Correct conversion direction selected |
| Save a temperature conversion | New record inserted into MariaDB |
| Retrieve conversion history | Saved records displayed in history list |
| Retrieve temperature units | Units loaded from MariaDB |
| Test DAO operations with Mockito | Expected JDBC operations verified |

### 4.3 Running Automated Tests

Run the tests using Maven:

`mvn clean test`

To run the tests and generate a JaCoCo coverage report:

`mvn clean verify`

JUnit test results are available in:

`target/surefire-reports/`

The JaCoCo HTML report is generated at:

`target/site/jacoco/index.html`

The report provides information about which parts of the code were executed during automated testing.

### 4.4 Manual Testing

Manual testing is used to verify functionality that cannot be fully demonstrated by unit tests alone.

The main verification steps are:

1. Launch the JavaFX application.
2. Enter a valid temperature.
3. Select Celsius or Fahrenheit.
4. Press Convert and verify the result.
5. Confirm that extreme temperatures appear in red.
6. Confirm that normal temperatures appear in black.
7. Verify that a new conversion appears in the history list.
8. Restart the application and verify that database records remain available.
9. Run the application using Docker and Xming.
10. Verify the Docker database connection through the Jenkins pipeline.

### 4.5 CI/CD Verification

Jenkins runs the Maven build and automated tests before building the Docker image.

A Docker database smoke test is used to check that the application can connect to MariaDB and retrieve the required temperature units.

The final pipeline stage publishes the application image to Docker Hub after the previous stages have completed successfully.

## 5. How to Run

### 5.1 Prerequisites

The following software is required for local development:

- Java JDK 21
- Apache Maven
- MariaDB
- Git
- IntelliJ IDEA or another Java IDE

For containerized execution:

- Docker Desktop
- Docker Compose
- Xming or another compatible X server

For CI/CD:

- Jenkins
- Java 21 and Maven configured in Jenkins
- Docker access from Jenkins
- Required database and Docker Hub credentials

### 5.2 Clone the Repository

Clone the repository:

`git clone https://github.com/unnap/OTP1_inclassAssignment3.git`

Navigate into the project directory:

`cd OTP1_inclassAssignment3`

### 5.3 Configure MariaDB

Create the database and tables using the SQL script:

`mariadb -u root -p < database/schema.sql`

The script creates the `temperature_db` database and the `TemperatureUnit` and `TempRecord` tables.

Configure the application's database connection using these environment variables:

| Variable | Purpose |
|---|---|
| DB_URL | MariaDB JDBC connection URL |
| DB_USER | Database username |
| DB_PASSWORD | Database password |

For local execution, the default database URL is:

`jdbc:mariadb://localhost:3306/temperature_db`

The application database user must have appropriate permissions to read and insert records.

### 5.4 Compile and Run Locally

From the project directory, run:

`mvn clean verify`

Start the JavaFX application:

`mvn javafx:run`

The application should open a graphical window displaying the temperature input field, unit selector, conversion button, result label, and conversion history.

### 5.5 Run Using Docker Compose

First, make sure Docker Desktop is running.

Create a local `.env` file containing the required database passwords.

Start Xming and configure the X server to accept connections from Docker. X11 access should be restricted to trusted local connections.

Build and start the application:

`docker compose up --build`

Docker Compose starts MariaDB, initializes the database on first setup, and launches the JavaFX application.

To stop the containers without deleting the database volume:

`docker compose down`

**Important:** Running `docker compose down -v` also removes the database volume and deletes stored conversion history.

### 5.6 Run the Docker Database Check

To verify database connectivity without opening the JavaFX window:

`docker compose run --rm app --check-db`

A successful check should print:

`Database connection and units OK`

### 5.7 Run Through Jenkins

1. Configure Java 21 and Maven in **Manage Jenkins → Tools**.
2. Configure a Docker Hub credential with the ID `dockerID`.
3. Configure a MariaDB test password credential with the ID `mariadb-test-password`.
4. Create a Jenkins Pipeline job connected to this GitHub repository.
5. Configure the job to use the repository's `Jenkinsfile`.
6. Select **Build Now**.
7. Review the Console Output to verify that all stages complete successfully.

The Jenkinsfile uses Docker Compose for the database smoke test and publishes the built application image to Docker Hub.

### 5.8 Docker Hub

The application image is published as:

`tunnap/otp1:latest`

Docker Hub repository:

https://hub.docker.com/r/tunnap/otp1

The image contains the JavaFX application. The MariaDB database is provided separately when running the project using Docker Compose.

## AI Assistance

ChatGPT (OpenAI) was used as a development assistant throughout this assignment. It assisted with generating and reviewing Java code, designing the MariaDB database structure, configuring Maven, Docker, and Jenkins, troubleshooting errors, and preparing documentation.

The generated code was manually reviewed, integrated into the project, and tested by the student. Final implementation decisions and verification were the student's responsibility.