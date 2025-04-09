package org.iq47.devops.module;


import org.iq47.devops.model.SkiPass;
import org.iq47.devops.repository.SkiPassRepository;
import org.iq47.devops.service.SkiPassService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;

public class SkiPassServiceTest {
    private SkiPassService skiPassService;
    private SkiPassRepository skiPassRepository;

    @BeforeEach
    void setUp() {
        skiPassRepository = Mockito.mock(SkiPassRepository.class);
        skiPassService = new SkiPassService(skiPassRepository);
    }

    @Test
    void testGetSkiPassById() {
        SkiPass skiPass = new SkiPass();
        skiPass.setId(2L);
        Mockito.when(skiPassRepository.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(skiPassRepository.findById(2L)).thenReturn(Optional.of(skiPass));
        assertAll(
                () -> Assertions.assertEquals(Optional.empty(), skiPassService.getSkiPassById(1L)),
                () -> Assertions.assertEquals(Optional.of(skiPass), skiPassService.getSkiPassById(2L))
        );
    }

    @Test
    void testCreateSkiPass() {
        SkiPass skiPass = new SkiPass();
        skiPass.setId(2L);
        Mockito.when(skiPassRepository.save(skiPass)).thenReturn(skiPass);
        assertAll(
                () -> Assertions.assertEquals(skiPass, skiPassService.createSkiPass(skiPass))
        );
    }

    @Test
    void testUpdateSkiPass() {
        SkiPass skiPass = new SkiPass();
        Mockito.when(skiPassRepository.save(skiPass)).thenReturn(skiPass);
        skiPass = skiPassService.updateSkiPass(2L, skiPass);
        SkiPass finalSkiPass = skiPass;
        assertAll(
                () -> Assertions.assertEquals(finalSkiPass.getId(), 2L)
        );
    }

    @Test
    void testDeleteSkiPass() {
        Mockito.doNothing().when(skiPassRepository).deleteById(1L);
        skiPassService.deleteSkiPass(1L);
    }
}
