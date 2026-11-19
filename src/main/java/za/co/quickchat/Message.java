package za.co.quickchat;

import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/** Represents one QuickChat message and its supporting operations. */
public class Message {
    private static final AtomicInteger ID_SEQUENCE = new AtomicInteger(0);
    // Regex format follows ICASA Numbering Plan Regulations (2016): country code +27 and
    // national significant number without the domestic trunk prefix 0.
    // Source: https://www.icasa.org.za/uploads/files/NumberingPlanReg.pdf
    private static final Pattern RECIPIENT_PATTERN = Pattern.compile("^\\+27[0-9]{9}$");
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Gson GSON = new Gson();

    private String messageId;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;
    private String sender;
    private String status;

    public Message() { }

    public Message(String recipient, String message, String sender, int messageNumber) {
        this.messageId = generateMessageId();
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        this.sender = sender;
        this.status = "PENDING";
        this.messageHash = createMessageHash();
    }

    /** Constructor with a supplied ID, useful for deterministic test cases and imported records. */
    public Message(String messageId, String recipient, String message, String sender, int messageNumber) {
        this.messageId = messageId;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        this.sender = sender;
        this.status = "PENDING";
        this.messageHash = createMessageHash();
    }

    private String generateMessageId() {
        // A ten-digit ID is generated for each message.
        return String.format(Locale.ROOT, "%010d", RANDOM.nextInt(1_000_000_000) + 1_000_000_000);
    }

    public boolean checkMessageID() {
        return messageId != null && messageId.length() <= 10;
    }

    public String checkRecipientCell(String number) {
        return number != null && RECIPIENT_PATTERN.matcher(number).matches()
                ? "Cell phone number successfully captured."
                : "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String createMessageHash() {
        if (messageId == null || message == null || message.isBlank()) return "";
        String[] words = message.trim().split("\\s+");
        String first = words[0].replaceAll("[^A-Za-z0-9]", "");
        String last = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");
        return (messageId.substring(0, 2) + ":" + messageNumber + ":" + first + last)
                .toUpperCase(Locale.ROOT);
    }

    public String SentMessage(int choice) {
        switch (choice) {
            case 1 -> { status = "SENT"; return "Message successfully sent."; }
            case 2 -> { status = "DISREGARDED"; return "Press 0 to delete the message."; }
            case 3 -> { status = "STORED"; return "Message successfully stored."; }
            default -> { return "Invalid message option."; }
        }
    }

    public String printMessages() {
        return "Message ID: " + messageId + System.lineSeparator()
                + "Message Hash: " + messageHash + System.lineSeparator()
                + "Recipient: " + recipient + System.lineSeparator()
                + "Message: " + message;
    }

    public int returnTotalMessagess() { return "SENT".equals(status) ? 1 : 0; }

    /** Stores a message as JSON. Gson reference: https://github.com/google/gson */
    public void storeMessage(Path file) throws IOException {
        Files.writeString(file, GSON.toJson(this));
    }

    public static Message readMessage(Path file) throws IOException {
        return GSON.fromJson(Files.readString(file), Message.class);
    }

    public String getMessageId() { return messageId; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public String getMessageHash() { return messageHash; }
    public String getSender() { return sender; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
