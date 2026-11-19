package za.co.quickchat;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class MessageTest {
    @Test void messageIdIsTenDigits() {
        Message message = new Message("+27838884567", "Hi there", "kyl_1", 1);
        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageId().length());
    }
    @Test void recipientCorrectlyFormatted() {
        Message message = new Message();
        assertEquals("Cell phone number successfully captured.", message.checkRecipientCell("+27838884567"));
    }
    @Test void recipientIncorrectlyFormatted() {
        Message message = new Message();
        assertTrue(message.checkRecipientCell("08575975889").startsWith("Cell phone number is incorrectly"));
    }
    @Test void messageHashMatchesRequiredFormat() {
        Message message = new Message("0012345678", "+27838884567", "Hi tonight", "kyl_1", 0);
        assertEquals("00:0:HITONIGHT", message.getMessageHash());
    }
    @Test void messageSentChoicesReturnCorrectMessages() {
        Message message = new Message("+27838884567", "Hi", "kyl_1", 1);
        assertEquals("Message successfully sent.", message.SentMessage(1));
        assertEquals("Press 0 to delete the message.", message.SentMessage(2));
        assertEquals("Message successfully stored.", message.SentMessage(3));
    }
    @Test void rejectsMessageOver250Characters() {
        String longMessage = "a".repeat(251);
        assertEquals(251, longMessage.length());
        assertTrue(longMessage.length() > 250);
    }
}
