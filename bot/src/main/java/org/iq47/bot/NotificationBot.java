package org.iq47.bot;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Getter
@Slf4j
public class NotificationBot extends TelegramLongPollingBot {

    private final String botToken;
    private final String botUsername;

    @Value("${telegram.admin.username}")
    private String adminChatId;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(this);
        }
        catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }


    public NotificationBot(String botToken, String botUsername) {
        this.botToken = botToken;
        this.botUsername = botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            try {
                if (messageText.startsWith("/start")) {
                    sendWelcomeMessage(chatId);
                } else if (messageText.startsWith("/help")) {
                    sendHelpMessage(chatId);
                }
            } catch (TelegramApiException e) {
                log.error("Error occurred: " + e.getMessage());
            }
        }
    }

    private void sendWelcomeMessage(Long chatId) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Welcome to Notification Bot! Use /help for commands list.");
        execute(message);
    }

    private void sendHelpMessage(Long chatId) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Available commands:\n/start - Welcome message\n/help - This help");
        execute(message);
    }

    public void sendMessageToAdmin(String text) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(adminChatId);
        message.setText(text);
        execute(message);
    }
}