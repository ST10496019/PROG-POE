# QuickChat – Programming 1A PoE (PROG5121)

A Java 17 Maven **console application** developed for Apache NetBeans. It combines registration/login, message handling, JSON storage, message searching/deletion, JUnit tests and GitHub Actions CI.

## Requirements
- JDK 17 or later
- Apache NetBeans with Maven support
- Internet connection the first time Maven downloads dependencies

## Open in Apache NetBeans
1. Extract the project ZIP.
2. Open Apache NetBeans.
3. Select **File → Open Project**.
4. Select the extracted `QuickChatPoE` folder (the folder containing `pom.xml`).
5. Wait for Maven dependencies to load.
6. Right-click the project and choose **Clean and Build**.
7. Right-click `QuickChatApp.java` and choose **Run File**, or run the project main class `za.co.quickchat.QuickChatApp`.

## Run the unit tests
- Right-click the project and choose **Test**, or run `mvn clean test` in the project folder.
- JUnit 5 tests are in `src/test/java/za/co/quickchat`.
- GitHub Actions runs the same tests on pushes and pull requests.

## Features
- Registration validation: username, password and South African international cellphone number.
- Login authentication and welcome/error messages.
- Console menu: send messages, recently sent placeholder, quit, and stored-message tools.
- Message IDs, message hashes, 250-character validation and send/disregard/store choices.
- Runtime message collections and arrays for sent, disregarded, stored, hashes and IDs.
- JSON persistence in `data/stored-messages.json` using Gson.
- Search by message ID and recipient, longest-message lookup, delete by hash and message report.

## JSON library attribution
Stored message JSON is handled using Google's Gson library: https://github.com/google/gson (Apache License 2.0). Maven downloads Gson through the dependency in `pom.xml`.

## GitHub submission reminder
The brief requires at least six commits for each project part and a final video presentation with your own voice showing the application running and explaining the logic and flow. Those account/submission actions must be completed in your own GitHub/Arc/YouTube accounts; this project ZIP does not claim they have been done.
