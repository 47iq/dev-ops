package org.iq47.bot;

public class TelegramBotException extends RuntimeException {
    public TelegramBotException(String message) {
        super(message);
    }

    public TelegramBotException(String message, Throwable cause) {
        super(message, cause);
    }
}