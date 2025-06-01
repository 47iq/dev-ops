package org.iq47.devops.controller;

import lombok.RequiredArgsConstructor;
import org.iq47.devops.model.SkiPass;
import org.iq47.devops.rest.TelegramBotClient;
import org.iq47.devops.service.SkiPassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ski-passes")
@CrossOrigin(origins = "http://localhost:3000")  // Allows access from this frontend server
public class SkiPassController {
    private final SkiPassService skiPassService;
    private final TelegramBotClient telegramBotClient;


    @GetMapping
    public List<SkiPass> getAllSkiPasses() {
        return skiPassService.getAllSkiPasses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkiPass> getSkiPassById(@PathVariable Long id) {
        telegramBotClient.sendMessageToBot("Requested ski-pass id: " + id);
        return skiPassService.getSkiPassById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SkiPass createSkiPass(@RequestBody SkiPass skiPass) {
        SkiPass created = skiPassService.createSkiPass(skiPass);
        telegramBotClient.sendMessageToBot("Created ski-pass id: " + created.getId());
        return created;
    }

    @PutMapping("/{id}")
    public SkiPass updateSkiPass(@PathVariable Long id, @RequestBody SkiPass skiPass) {
        SkiPass updated = skiPassService.updateSkiPass(id, skiPass);
        telegramBotClient.sendMessageToBot("Updated ski-pass id: " + updated.getId());
        return updated;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkiPass(@PathVariable Long id) {
        skiPassService.deleteSkiPass(id);
        telegramBotClient.sendMessageToBot("Deleted ski-pass id: " + id);
        return ResponseEntity.noContent().build();
    }
}