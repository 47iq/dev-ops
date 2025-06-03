package org.iq47.devops.controller;

import org.iq47.devops.model.SkiPass;
import org.iq47.devops.rest.TelegramBotClient;
import org.iq47.devops.service.SkiPassService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class SkiPassControllerTest {

    private SkiPassService skiPassService;
    private TelegramBotClient telegramBotClient;
    private SkiPassController skiPassController;

    @BeforeEach
    void setUp() {
        skiPassService = mock(SkiPassService.class);
        telegramBotClient = mock(TelegramBotClient.class);
        skiPassController = new SkiPassController(skiPassService, telegramBotClient);
    }

    @Test
    void getAllSkiPasses_returnsList() {
        List<SkiPass> expected = Arrays.asList(new SkiPass(), new SkiPass());
        when(skiPassService.getAllSkiPasses()).thenReturn(expected);

        List<SkiPass> result = skiPassController.getAllSkiPasses();

        assertEquals(expected, result);
        verify(skiPassService).getAllSkiPasses();
        verifyNoInteractions(telegramBotClient);
    }

    @Test
    void getSkiPassById_found_returnsEntity() {
        SkiPass skiPass = new SkiPass();
        skiPass.setId(1L);
        when(skiPassService.getSkiPassById(1L)).thenReturn(Optional.of(skiPass));

        ResponseEntity<SkiPass> response = skiPassController.getSkiPassById(1L);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(skiPass, response.getBody());
        verify(telegramBotClient).sendMessageToBot("Requested ski-pass id: 1");
    }

    @Test
    void getSkiPassById_notFound_returns404() {
        when(skiPassService.getSkiPassById(999L)).thenReturn(Optional.empty());

        ResponseEntity<SkiPass> response = skiPassController.getSkiPassById(999L);

        assertEquals(404, response.getStatusCodeValue());
        verify(telegramBotClient).sendMessageToBot("Requested ski-pass id: 999");
    }

    @Test
    void createSkiPass_returnsCreatedPass() {
        SkiPass input = new SkiPass();
        SkiPass saved = new SkiPass();
        saved.setId(5L);

        when(skiPassService.createSkiPass(input)).thenReturn(saved);

        SkiPass result = skiPassController.createSkiPass(input);

        assertEquals(saved, result);
        verify(skiPassService).createSkiPass(input);
        verify(telegramBotClient).sendMessageToBot("Created ski-pass id: 5");
    }

    @Test
    void updateSkiPass_returnsUpdatedPass() {
        SkiPass input = new SkiPass();
        SkiPass updated = new SkiPass();
        updated.setId(10L);

        when(skiPassService.updateSkiPass(10L, input)).thenReturn(updated);

        SkiPass result = skiPassController.updateSkiPass(10L, input);

        assertEquals(updated, result);
        verify(skiPassService).updateSkiPass(10L, input);
        verify(telegramBotClient).sendMessageToBot("Updated ski-pass id: 10");
    }

    @Test
    void deleteSkiPass_returnsNoContent() {
        ResponseEntity<Void> response = skiPassController.deleteSkiPass(3L);

        assertEquals(204, response.getStatusCodeValue());
        verify(skiPassService).deleteSkiPass(3L);
        verify(telegramBotClient).sendMessageToBot("Deleted ski-pass id: 3");
    }
}
