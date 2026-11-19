package za.co.quickchat;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/** Console entry point for the QuickChat application. */
public class QuickChatApp {
    private final Scanner input = new Scanner(System.in);
    private final Login login = new Login();
    private final MessageManager manager = new MessageManager(Path.of("data", "stored-messages.json"));
    private int messageCounter = 0;

    public static void main(String[] args) {
        new QuickChatApp().run();
    }

    public void run() {
        System.out.println("========== QuickChat Registration ==========");
        registerAndLogin();
        if (!isLoggedIn()) return;
        System.out.println("\nWelcome to QuickChat.");
        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = input.nextLine().trim();
            switch (choice) {
                case "1" -> sendMessages();
                case "2" -> System.out.println("Coming Soon.");
                case "3" -> running = false;
                case "4" -> storedMessagesMenu();
                default -> System.out.println("Please select a valid menu option.");
            }
        }
        System.out.println("Total messages sent: " + manager.returnTotalMessagesSent());
        System.out.println("Thank you for using QuickChat.");
    }

    private boolean loggedIn;
    private void registerAndLogin() {
        System.out.print("Enter first name: "); String first = input.nextLine().trim();
        System.out.print("Enter last name: "); String last = input.nextLine().trim();
        System.out.print("Create username (must contain _ and be max 5 characters): "); String user = input.nextLine().trim();
        System.out.print("Create password: "); String pass = input.nextLine();
        System.out.print("Enter cellphone number (+27 followed by 9 digits): "); String cell = input.nextLine().trim();
        String registration = login.registerUser(user, pass, cell, first, last);
        System.out.println(registration);
        if (!registration.equals("User registered successfully.")) return;
        System.out.print("\nLogin username: "); String loginUser = input.nextLine().trim();
        System.out.print("Login password: "); String loginPass = input.nextLine();
        loggedIn = login.loginUser(loginUser, loginPass);
        System.out.println(login.returnLoginStatus(loggedIn));
    }

    private boolean isLoggedIn() { return loggedIn; }

    private void printMainMenu() {
        System.out.println("\n1) Send Messages\n2) Show recently sent messages\n3) Quit\n4) Stored Messages");
        System.out.print("Choose an option: ");
    }

    private void sendMessages() {
        System.out.print("How many messages would you like to enter? ");
        int number;
        try { number = Integer.parseInt(input.nextLine().trim()); }
        catch (NumberFormatException ex) { System.out.println("Please enter a valid whole number."); return; }
        if (number < 1) { System.out.println("Enter at least one message."); return; }
        for (int i = 0; i < number; i++) {
            System.out.println("\nMessage " + (i + 1) + " of " + number);
            System.out.print("Recipient cellphone (+27 followed by 9 digits): ");
            String recipient = input.nextLine().trim();
            Message validator = new Message();
            String recipientStatus = validator.checkRecipientCell(recipient);
            if (!recipientStatus.startsWith("Cell phone number successfully")) {
                System.out.println(recipientStatus); i--; continue;
            }
            System.out.print("Enter message (maximum 250 characters): ");
            String body = input.nextLine();
            if (body.length() > 250) {
                System.out.println("Message exceeds 250 characters by " + (body.length() - 250) + "; please reduce the size.");
                i--; continue;
            }
            messageCounter++;
            Message message = new Message(recipient, body, login.getUsername(), messageCounter);
            System.out.println("Message ready to send.");
            System.out.println("Message ID: " + message.getMessageId());
            System.out.println("Message Hash: " + message.getMessageHash());
            System.out.println("1) Send Message\n2) Disregard Message\n3) Store Message to send later");
            System.out.print("Choose an option: ");
            int action;
            try { action = Integer.parseInt(input.nextLine().trim()); }
            catch (NumberFormatException ex) { System.out.println("Invalid option; message disregarded."); manager.add(message, "DISREGARDED"); continue; }
            String status = switch (action) { case 1 -> "SENT"; case 2 -> "DISREGARDED"; case 3 -> "STORED"; default -> ""; };
            System.out.println(message.SentMessage(action));
            if (status.isEmpty()) { i--; continue; }
            manager.add(message, status);
            if ("SENT".equals(status)) System.out.println(message.printMessages());
        }
        System.out.println("Total messages sent: " + manager.returnTotalMessagesSent());
    }

    private void storedMessagesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Stored Messages ---");
            System.out.println("1) Display sender and recipient\n2) Display longest stored message\n3) Search by message ID\n4) Search by recipient\n5) Delete by message hash\n6) Display message report\n0) Back");
            System.out.print("Choose an option: ");
            String choice = input.nextLine().trim();
            switch (choice) {
                case "1" -> System.out.println(manager.displayStoredSendersAndRecipients());
                case "2" -> System.out.println(manager.longestStoredMessage().map(Message::printMessages).orElse("No stored messages."));
                case "3" -> { System.out.print("Enter message ID: "); String id = input.nextLine().trim();
                    Optional<Message> found = manager.searchById(id);
                    System.out.println(found.map(m -> "Recipient: " + m.getRecipient() + "\nMessage: " + m.getMessage()).orElse("Message not found.")); }
                case "4" -> { System.out.print("Enter recipient number: "); String recipient = input.nextLine().trim();
                    List<Message> found = manager.searchByRecipient(recipient);
                    if (found.isEmpty()) System.out.println("No messages found for this recipient.");
                    else found.forEach(m -> System.out.println(m.printMessages())); }
                case "5" -> { System.out.print("Enter message hash: "); String hash = input.nextLine().trim();
                    System.out.println(manager.deleteByHash(hash) ? "Message successfully deleted." : "Message hash not found."); }
                case "6" -> System.out.println(manager.displayReport());
                case "0" -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }
}
