package org.iq47.devops.controller;

import org.iq47.devops.model.SkiPass;
import org.iq47.devops.service.SkiPassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ski-passes")
@CrossOrigin(origins = "http://localhost:3000")  // Разрешаем доступ с этого фронтенд сервера
public class SkiPassController {
    @Autowired
    private SkiPassService skiPassService;

    @GetMapping
    public List<SkiPass> getAllSkiPasses() {
        return skiPassService.getAllSkiPasses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkiPass> getSkiPassById(@PathVariable Long id) {
        return skiPassService.getSkiPassById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SkiPass createSkiPass(@RequestBody SkiPass skiPass) {
        return skiPassService.createSkiPass(skiPass);
    }

    @PutMapping("/{id}")
    public SkiPass updateSkiPass(@PathVariable Long id, @RequestBody SkiPass skiPass) {
        return skiPassService.updateSkiPass(id, skiPass);
    }
}
