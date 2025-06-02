package org.iq47.bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @Mock
    private NotificationBot notificationBot;

    @InjectMocks
    private MessageController messageController;

    private MessageRequest validRequest;
    private MessageRequest invalidRequest;

    @BeforeEach
    void setUp() {
        validRequest = new MessageRequest("Valid message");
        invalidRequest = new MessageRequest(""); // Empty message will fail validation
    }

    @Test
    void sendMessage_ShouldReturnOk_WhenMessageIsValid() throws TelegramApiException {
        // Act
        ResponseEntity<String> response = messageController.sendMessage(validRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Message sent successfully", response.getBody());
        verify(notificationBot).sendMessageToAdmin(validRequest.getMessage());
    }

    @Test
    void sendMessage_ShouldThrowTelegramBotException_WhenTelegramApiFails() throws TelegramApiException {
        // Arrange
        doThrow(new TelegramApiException("API error")).when(notificationBot).sendMessageToAdmin(anyString());

        // Act & Assert
        TelegramBotException exception = assertThrows(TelegramBotException.class, () -> {
            messageController.sendMessage(validRequest);
        });

        assertEquals("Failed to send message", exception.getMessage());
        assertNotNull(exception.getCause());
    }

    @Test
    void handleTelegramBotException_ShouldReturnInternalServerError() {
        // Arrange
        TelegramBotException exception = new TelegramBotException("Test error");

        // Act
        ResponseEntity<String> response = messageController.handleTelegramBotException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Telegram bot error: Test error", response.getBody());
    }

    @Test
    void constructor_ShouldInitializeDependencies() {
        // Arrange
        NotificationBot mockBot = mock(NotificationBot.class);

        // Act
        MessageController controller = new MessageController(mockBot);

        // Assert
        assertNotNull(controller);
    }
}
