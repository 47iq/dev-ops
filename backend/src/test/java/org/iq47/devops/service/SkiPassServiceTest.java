package org.iq47.devops.service;

import org.iq47.devops.model.SkiPass;
import org.iq47.devops.repository.SkiPassRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SkiPassServiceTest {

    private SkiPassRepository skiPassRepository;
    private SkiPassService skiPassService;

    @BeforeEach
    void setUp() {
        skiPassRepository = mock(SkiPassRepository.class);
        skiPassService    = new SkiPassService(skiPassRepository);
    }

    /* ---------- getAllSkiPasses() ---------- */

    @Test
    void getAllSkiPasses_returnsListFromRepo() {
        List<SkiPass> expected = Arrays.asList(new SkiPass(), new SkiPass());
        when(skiPassRepository.findAll()).thenReturn(expected);

        List<SkiPass> result = skiPassService.getAllSkiPasses();

        assertEquals(expected, result);
        verify(skiPassRepository).findAll();
    }

    /* ---------- getSkiPassById() ---------- */

    @Test
    void getSkiPassById_whenFound_returnsOptionalWithEntity() {
        SkiPass pass = new SkiPass();
        pass.setId(5L);
        when(skiPassRepository.findById(5L)).thenReturn(Optional.of(pass));

        Optional<SkiPass> result = skiPassService.getSkiPassById(5L);

        assertTrue(result.isPresent());
        assertEquals(pass, result.get());
        verify(skiPassRepository).findById(5L);
    }

    @Test
    void getSkiPassById_whenNotFound_returnsEmptyOptional() {
        when(skiPassRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<SkiPass> result = skiPassService.getSkiPassById(99L);

        assertTrue(result.isEmpty());
        verify(skiPassRepository).findById(99L);
    }

    /* ---------- createSkiPass() ---------- */

    @Test
    void createSkiPass_delegatesToSave() {
        SkiPass input  = new SkiPass();
        SkiPass saved  = new SkiPass();
        saved.setId(1L);

        when(skiPassRepository.save(input)).thenReturn(saved);

        SkiPass result = skiPassService.createSkiPass(input);

        assertEquals(saved, result);
        verify(skiPassRepository).save(input);
    }

    /* ---------- updateSkiPass() ---------- */

    @Test
    void updateSkiPass_setsIdAndSaves() {
        SkiPass updateDto = new SkiPass();       // без id
        SkiPass saved     = new SkiPass();
        saved.setId(7L);

        // сохраняем то, что repo вернёт после save()
        when(skiPassRepository.save(any(SkiPass.class))).thenReturn(saved);

        SkiPass result = skiPassService.updateSkiPass(7L, updateDto);

        // проверяем, что id был проставлен
        ArgumentCaptor<SkiPass> captor = ArgumentCaptor.forClass(SkiPass.class);
        verify(skiPassRepository).save(captor.capture());
        assertEquals(7L, captor.getValue().getId());

        assertEquals(saved, result);
    }

    /* ---------- deleteSkiPass() ---------- */

    @Test
    void deleteSkiPass_callsRepositoryDelete() {
        skiPassService.deleteSkiPass(4L);

        verify(skiPassRepository).deleteById(4L);
        verifyNoMoreInteractions(skiPassRepository);
    }
}
