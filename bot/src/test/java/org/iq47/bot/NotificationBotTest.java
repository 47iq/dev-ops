package org.iq47.bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationBotTest {
    @Spy
    @InjectMocks
    private NotificationBot notificationBot = new NotificationBot("test_token", "test_bot");

    @Test
    void testConstructor() {
        assertEquals("test_token", notificationBot.getBotToken());
        assertEquals("test_bot", notificationBot.getBotUsername());
    }

    @Test
    void testOnUpdateReceivedWithStartCommand() throws TelegramApiException {
        Update update = createUpdateWithText("/start");

        notificationBot.onUpdateReceived(update);

        verify(notificationBot).sendWelcomeMessage(12345L);
        verify(notificationBot, never()).sendHelpMessage(any());
    }

    @Test
    void testOnUpdateReceivedWithHelpCommand() throws TelegramApiException {
        Update update = createUpdateWithText("/help");

        notificationBot.onUpdateReceived(update);

        verify(notificationBot).sendHelpMessage(12345L);
        verify(notificationBot, never()).sendWelcomeMessage(any());
    }

    @Test
    void testOnUpdateReceivedWithOtherText() throws TelegramApiException {
        Update update = createUpdateWithText("random text");

        notificationBot.onUpdateReceived(update);

        verify(notificationBot, never()).sendWelcomeMessage(any());
        verify(notificationBot, never()).sendHelpMessage(any());
    }

    @Test
    void testOnUpdateReceivedWithException() throws TelegramApiException {
        Update update = createUpdateWithText("/start");
        doThrow(new TelegramApiException("Send failed")).when(notificationBot).sendWelcomeMessage(any());

        notificationBot.onUpdateReceived(update);
    }

    @Test
    void testSendWelcomeMessage() throws TelegramApiException {
        try {
            notificationBot.sendWelcomeMessage(12345L);
        } catch (Exception ignored) { }

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(notificationBot).execute(messageCaptor.capture());

        SendMessage sentMessage = messageCaptor.getValue();
        assertEquals("12345", sentMessage.getChatId());
        assertEquals("Welcome to Notification Bot! Use /help for commands list.", sentMessage.getText());
    }

    @Test
    void testSendHelpMessage() throws TelegramApiException {

        try {
            notificationBot.sendHelpMessage(12345L);
        } catch (Exception ignored) { }

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(notificationBot).execute(messageCaptor.capture());

        SendMessage sentMessage = messageCaptor.getValue();
        assertEquals("12345", sentMessage.getChatId());
        assertTrue(sentMessage.getText().contains("Available commands"));
    }

    @Test
    void testSendMessageToAdmin() throws TelegramApiException {
        notificationBot.setAdminChatId("admin123");
        try {
            notificationBot.sendMessageToAdmin("Test message");
        } catch (Exception ignored) { }

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(notificationBot).execute(messageCaptor.capture());

        SendMessage sentMessage = messageCaptor.getValue();
        assertEquals("admin123", sentMessage.getChatId());
        assertEquals("Test message", sentMessage.getText());
    }

    @Test
    void testSendMessageToAdminWithException() throws TelegramApiException {
        notificationBot.setAdminChatId("admin123");
        doThrow(new TelegramApiException("Admin message failed")).when(notificationBot).execute(any(SendMessage.class));

        assertThrows(TelegramApiException.class, () -> {
            notificationBot.sendMessageToAdmin("Test message");
        });
    }

    private Update createUpdateWithText(String text) {
        Update update = new Update();
        Message message = new Message();
        message.setText(text);
        Chat chat = new Chat();
        chat.setId(12345L);
        message.setChat(chat);
        update.setMessage(message);
        return update;
    }

    @Test
    void onUpdateReceived_ShouldNotProcess_WhenNoMessage() throws TelegramApiException {
        // Arrange
        Update update = new Update(); // No message set

        // Act
        notificationBot.onUpdateReceived(update);

        // Assert
        verify(notificationBot, never()).sendWelcomeMessage(any());
        verify(notificationBot, never()).sendHelpMessage(any());
    }

    @Test
    void onUpdateReceived_ShouldNotProcess_WhenMessageHasNoText() throws TelegramApiException {
        // Arrange
        Update update = new Update();
        Message message = new Message(); // No text set
        update.setMessage(message);

        // Act
        notificationBot.onUpdateReceived(update);

        // Assert
        verify(notificationBot, never()).sendWelcomeMessage(any());
        verify(notificationBot, never()).sendHelpMessage(any());
    }

    @Test
    void onUpdateReceived_ShouldNotProcess_WhenMessageIsNull() throws TelegramApiException {
        // Arrange
        Update update = new Update();
        update.setMessage(null); // Explicit null message

        // Act
        notificationBot.onUpdateReceived(update);

        // Assert
        verify(notificationBot, never()).sendWelcomeMessage(any());
        verify(notificationBot, never()).sendHelpMessage(any());
    }

    @Test
    void onUpdateReceived_ShouldHandleException_WhenMessageProcessingFails() throws TelegramApiException {
        // Arrange
        Update update = new Update();
        Message message = new Message();
        message.setText("/start");
        message.setChat(new Chat());
        update.setMessage(message);

        doThrow(new TelegramApiException("Test exception")).when(notificationBot).sendWelcomeMessage(any());

        // Act
        notificationBot.onUpdateReceived(update);
    }
}