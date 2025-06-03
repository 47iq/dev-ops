package org.iq47.devops.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TelegramBotClientTest {

    private RestTemplate restTemplate;
    private TelegramBotClient telegramBotClient;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        telegramBotClient = new TelegramBotClient(restTemplate);

        // Прямо устанавливаем поле botApiUrl через reflection, т.к. @Value не работает вне Spring
        var botApiUrlField = TelegramBotClient.class.getDeclaredFields()[1];
        botApiUrlField.setAccessible(true);
        try {
            botApiUrlField.set(telegramBotClient, "http://fake-bot-api.com");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void sendMessageToBot_successfulResponse_returnsResponseBody() {
        String testMessage = "Hello Bot!";
        String expectedResponse = "Message received";

        ResponseEntity<String> mockResponse = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        when(restTemplate.exchange(
                eq("http://fake-bot-api.com/api/v1/message"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponse);

        String result = telegramBotClient.sendMessageToBot(testMessage);

        assertEquals(expectedResponse, result);

        // Проверяем, что в теле отправляется правильное сообщение
        ArgumentCaptor<HttpEntity<MessageRequest>> captor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(
                eq("http://fake-bot-api.com/api/v1/message"),
                eq(HttpMethod.POST),
                captor.capture(),
                eq(String.class)
        );

        MessageRequest actualRequest = captor.getValue().getBody();
        assertNotNull(actualRequest);
        assertEquals(testMessage, actualRequest.getMessage());
    }

    @Test
    void sendMessageToBot_non2xxResponse_throwsException() {
        ResponseEntity<String> mockResponse = new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponse);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                telegramBotClient.sendMessageToBot("fail"));

        assertTrue(exception.getMessage().contains("Failed to send message"));
    }

    @Test
    void sendMessageToBot_exceptionThrown_throwsWrappedException() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenThrow(new RuntimeException("Connection error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                telegramBotClient.sendMessageToBot("any message"));

        assertTrue(exception.getMessage().contains("Error communicating with Telegram bot API"));
        assertTrue(exception.getCause() instanceof RuntimeException);
    }
}
