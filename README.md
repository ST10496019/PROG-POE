# QuickChat – Programming 1A PoE (PROG5121)

## Project Description

QuickChat is a Java-based console messaging application developed for the Programming 1A Portfolio of Evidence (PoE). The application allows users to register, log in and manage messages through a simple menu-driven interface.

The project was developed using Java and Apache NetBeans with Maven.

## Technologies Used

- Java 17
- Apache NetBeans IDE
- Maven
- JUnit 5
- Gson (JSON processing)
- GitHub
- GitHub Actions

## Features

### User Registration and Login
- Register a new user.
- Validate usernames and passwords.
- Validate South African cellphone numbers.
- Log in using registered account details.

### Message Management
- Create and send messages.
- Generate unique message IDs.
- Generate message hashes.
- Validate message length.
- Send, disregard or store messages.

### Message Storage and Searching
- Store messages in a JSON file.
- Display stored messages.
- Search for messages using message IDs.
- Search for messages using recipient details.
- Find the longest message.
- Delete messages using their message hashes.
- Generate a message report.

### Testing
- JUnit 5 unit tests.
- Automated testing using GitHub Actions.

## Project Requirements

To run this application, you need:

- JDK 17 or later.
- Apache NetBeans IDE.
- Maven support.
- An internet connection for the initial Maven dependency downloads.

## How to Run the Application

1. Download or clone this repository.
2. Open Apache NetBeans.
3. Select **File → Open Project**.
4. Select the QuickChatPoE project folder.
5. Allow Maven to download the required dependencies.
6. Right-click the project and select **Clean and Build**.
7. Run the `QuickChatApp.java` main class.
8. Follow the instructions displayed in the console.

## Running the Unit Tests

1. Open the project in Apache NetBeans.
2. Right-click the project.
3. Select **Test**.
4. Check the test results to confirm whether the tests pass.

Alternatively, run the following Maven command:

```bash
mvn clean test
```

## JSON Storage

The application uses JSON to store messages. Google's Gson library is used to process JSON data.

Stored messages are saved in:

`data/stored-messages.json`

## GitHub Actions

GitHub Actions is used to automate the testing process. The workflow runs the project's unit tests when changes are pushed to the repository or when a pull request is created.

## Author

**Programming 1A – PROG5121**

**Project:** QuickChat PoE

**Development Environment:** Apache NetBeans

## Conclusion

This project demonstrates the use of Java programming concepts, including object-oriented programming, methods, arrays, loops, conditional statements, input validation, file handling, JSON processing and unit testing. It combines the different parts of the Programming 1A PoE into a console-based messaging application.
