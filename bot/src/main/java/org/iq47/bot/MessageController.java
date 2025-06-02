package org.iq47.bot;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class MessageController {

    private final NotificationBot notificationBot;
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    public MessageController(NotificationBot notificationBot) {
        this.notificationBot = notificationBot;
    }

    @Operation(summary = "Send message to admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid message format"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/message")
    public ResponseEntity<String> sendMessage(@Valid @RequestBody MessageRequest request) {
        try {
            logger.info("Attempting to send message to admin: {}", request.getMessage());
            notificationBot.sendMessageToAdmin(request.getMessage());
            return ResponseEntity.ok("Message sent successfully");
        } catch (TelegramApiException e) {
            logger.error("Failed to send Telegram message", e);
            throw new TelegramBotException("Failed to send message", e);
        }
    }

    @ExceptionHandler(TelegramBotException.class)
    public ResponseEntity<String> handleTelegramBotException(TelegramBotException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Telegram bot error: " + e.getMessage());
    }
}