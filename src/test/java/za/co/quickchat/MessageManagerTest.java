package za.co.quickchat;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MessageManagerTest {
    @TempDir Path tempDir;
    private MessageManager manager;
    private Message m1, m2, m3, m4, m5;

    @BeforeEach void setUp() {
        manager = new MessageManager(tempDir.resolve("stored-messages.json"));
        m1 = new Message("0000000001", "+27838884567", "Did you get the cake?", "kyl_1", 1);
        m2 = new Message("0000000002", "+27838884567", "Where are you? You are late! I have asked you to be on time.", "kyl_1", 2);
        m3 = new Message("0000000003", "+27838884567", "It is dinner time!", "kyl_1", 3);
        m4 = new Message("0000000004", "+27838884567", "It is dinner time!", "kyl_1", 4);
        m5 = new Message("0000000005", "+27838884567", "Ok, I am leaving without you.", "kyl_1", 5);
        manager.add(m1, "SENT"); manager.add(m2, "SENT"); manager.add(m3, "SENT"); manager.add(m4, "SENT");
        manager.add(m5, "STORED");
    }

    @Test void sentMessagesArrayCorrectlyPopulated() {
        assertEquals(4, manager.getSentMessagesArray().length);
        assertEquals("Did you get the cake?", manager.getSentMessagesArray()[0].getMessage());
        assertEquals("It is dinner time!", manager.getSentMessagesArray()[3].getMessage());
    }
    @Test void longestMessageIsFound() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                manager.longestStoredMessage().orElseThrow().getMessage());
    }
    @Test void searchByMessageIdReturnsRecipientAndMessage() {
        Message found = manager.searchById("0000000004").orElseThrow();
        assertEquals("+27838884567", found.getRecipient());
        assertEquals("It is dinner time!", found.getMessage());
    }
    @Test void searchByRecipientReturnsMatchingMessages() {
        List<Message> found = manager.searchByRecipient("+27838884567");
        assertEquals(5, found.size());
        assertTrue(found.stream().anyMatch(m -> m.getMessage().equals("Ok, I am leaving without you.")));
    }
    @Test void deleteMessageUsingHash() {
        assertTrue(manager.deleteByHash(m5.getMessageHash()));
        assertEquals(0, manager.getStoredMessagesArray().length);
    }
    @Test void reportContainsMessageHashRecipientAndMessage() {
        String report = manager.displayReport();
        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient: +27838884567"));
        assertTrue(report.contains("Did you get the cake?"));
    }
    @Test void storedMessagesAreWrittenToAndReadFromJson() throws Exception {
        Path json = tempDir.resolve("stored-messages.json");
        assertTrue(Files.exists(json));
        Message[] loaded = manager.getStoredMessagesArray();
        assertEquals(1, loaded.length);
        assertEquals("Ok, I am leaving without you.", loaded[0].getMessage());
        MessageManager reloaded = new MessageManager(json);
        assertEquals(1, reloaded.getStoredMessagesArray().length);
    }
    @Test void arraysForHashesAndIdsArePopulated() {
        assertEquals(5, manager.getMessageHashesArray().length);
        assertEquals(5, manager.getMessageIdsArray().length);
    }
}
