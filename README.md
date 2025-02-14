# Project Name

Which uses the full Spring stack, including Spring Templates, Spring MVC, Spring Core, Spring Boot, Spring Data and Spring Security. The project aims to create a web application with good architecture and security. In the future, it is planned to add Docker for interaction with microservices.

## Technologies

- **Spring Core**: Basic functionality and configuration of the application.
- **Spring Boot**: Simplifying the project setup and deployment.
- **Spring MVC**: Request processing and routing based on MVC design patterns.
- **Spring Data**: Simplified work with the database, using repositories to interact with data.
- **Spring Security**: Security, authentication and authorization.
- **Spring Templates**: Using templates for dynamic generation of HTML pages.


## Installation

1. Clone the repository:
```bash
git clone https://github.com/Lokrip/full-stack-application
```

2. Go to the project directory:
```bash
cd full-stack-application
```

3. Build the project with Maven:
```bash
mvn clean install
```

4. Run the application:
```bash
mvn spring-boot:run
```

## Docker

The project will soon be supplemented with Docker support for convenient interaction with microservices. With Docker, you can deploy containers for each microservice and link them for effective interaction.

1. Build the Docker image:
```bash
docker build -t project-name .
```

2. Run the container:
```bash
docker run -p 8080:8080 project-name
```

## Security

- The project uses Spring Security for security.
- User authentication and roles are enabled to restrict access to different parts of the application.

