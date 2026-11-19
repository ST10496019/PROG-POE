package za.co.quickchat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Maintains message collections and persists stored messages in JSON. */
public class MessageManager {
    private final List<Message> sent = new ArrayList<>();
    private final List<Message> disregarded = new ArrayList<>();
    private final List<Message> stored = new ArrayList<>();
    private final List<String> messageHashes = new ArrayList<>();
    private final List<String> messageIds = new ArrayList<>();
    private final Path jsonFile;
    private static final Gson GSON = new Gson();
    private static final Type MESSAGE_LIST_TYPE = new TypeToken<ArrayList<Message>>() { }.getType();

    public MessageManager(Path jsonFile) {
        this.jsonFile = jsonFile;
        loadStoredMessages();
    }

    public void add(Message message, String status) {
        message.setStatus(status);
        switch (status) {
            case "SENT" -> sent.add(message);
            case "DISREGARDED" -> disregarded.add(message);
            case "STORED" -> stored.add(message);
            default -> throw new IllegalArgumentException("Unknown message status: " + status);
        }
        if (!messageHashes.contains(message.getMessageHash())) messageHashes.add(message.getMessageHash());
        if (!messageIds.contains(message.getMessageId())) messageIds.add(message.getMessageId());
        if ("STORED".equals(status)) saveStoredMessages();
    }

    private void saveStoredMessages() {
        try {
            Path parent = jsonFile.getParent();
            if (parent != null) Files.createDirectories(parent);
            Files.writeString(jsonFile, GSON.toJson(stored));
        } catch (IOException ex) {
            System.out.println("Could not save stored messages: " + ex.getMessage());
        }
    }

    private void loadStoredMessages() {
        if (!Files.exists(jsonFile)) return;
        try {
            List<Message> loaded = GSON.fromJson(Files.readString(jsonFile), MESSAGE_LIST_TYPE);
            if (loaded != null) {
                stored.addAll(loaded);
                for (Message message : loaded) {
                    if (!messageHashes.contains(message.getMessageHash())) messageHashes.add(message.getMessageHash());
                    if (!messageIds.contains(message.getMessageId())) messageIds.add(message.getMessageId());
                }
            }
        } catch (Exception ex) {
            System.out.println("Could not read stored messages: " + ex.getMessage());
        }
    }

    public String displayStoredSendersAndRecipients() {
        if (stored.isEmpty()) return "No stored messages.";
        return stored.stream().map(m -> "Sender: " + m.getSender() + " | Recipient: " + m.getRecipient())
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public Optional<Message> longestStoredMessage() {
        return allMessages().stream().max((a, b) -> Integer.compare(a.getMessage().length(), b.getMessage().length()));
    }

    public Optional<Message> searchById(String id) {
        return allMessages().stream().filter(m -> m.getMessageId().equals(id)).findFirst();
    }

    public List<Message> searchByRecipient(String recipient) {
        return allMessages().stream().filter(m -> m.getRecipient().equals(recipient)).collect(Collectors.toList());
    }

    public boolean deleteByHash(String hash) {
        boolean removed = stored.removeIf(m -> m.getMessageHash().equalsIgnoreCase(hash));
        if (!removed) removed = sent.removeIf(m -> m.getMessageHash().equalsIgnoreCase(hash));
        if (removed) {
            messageHashes.removeIf(h -> h.equalsIgnoreCase(hash));
            saveStoredMessages();
        }
        return removed;
    }

    public String displayReport() {
        List<Message> messages = allMessages();
        if (messages.isEmpty()) return "No messages available.";
        return messages.stream().map(Message::printMessages).collect(Collectors.joining(
                System.lineSeparator() + "------------------------------" + System.lineSeparator()));
    }

    private List<Message> allMessages() {
        List<Message> all = new ArrayList<>();
        all.addAll(sent); all.addAll(disregarded); all.addAll(stored);
        return all;
    }

    public int returnTotalMessagesSent() { return sent.size(); }
    public Message[] getSentMessagesArray() { return sent.toArray(new Message[0]); }
    public Message[] getDisregardedMessagesArray() { return disregarded.toArray(new Message[0]); }
    public Message[] getStoredMessagesArray() { return stored.toArray(new Message[0]); }
    public String[] getMessageHashesArray() { return messageHashes.toArray(new String[0]); }
    public String[] getMessageIdsArray() { return messageIds.toArray(new String[0]); }
    public List<Message> getSentMessages() { return new ArrayList<>(sent); }
    public List<Message> getStoredMessages() { return new ArrayList<>(stored); }
}
