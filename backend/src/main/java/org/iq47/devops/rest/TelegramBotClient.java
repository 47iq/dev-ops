package org.iq47.devops.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramBotClient {

    private final RestTemplate restTemplate;

    @Value("${telegram.bot.api.url:http://localhost:8081}")
    private String botApiUrl;

    public TelegramBotClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String sendMessageToBot(String message) {
        String url = botApiUrl + "/api/v1/message";

        // Create request body
        MessageRequest request = new MessageRequest(message);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create HTTP entity
        HttpEntity<MessageRequest> entity = new HttpEntity<>(request, headers);

        try {
            // Send POST request
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to send message. Status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error communicating with Telegram bot API: " + e.getMessage(), e);
        }
    }
}