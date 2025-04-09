package org.iq47.devops.service;

import lombok.RequiredArgsConstructor;
import org.iq47.devops.model.SkiPass;
import org.iq47.devops.repository.SkiPassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SkiPassService {
    private final SkiPassRepository skiPassRepository;

    public List<SkiPass> getAllSkiPasses() {
        return skiPassRepository.findAll();
    }

    public Optional<SkiPass> getSkiPassById(Long id) {
        return skiPassRepository.findById(id);
    }

    public SkiPass createSkiPass(SkiPass skiPass) {
        return skiPassRepository.save(skiPass);
    }

    public SkiPass updateSkiPass(Long id, SkiPass skiPass) {
        skiPass.setId(id);
        return skiPassRepository.save(skiPass);
    }

    public void deleteSkiPass(Long id) {
        skiPassRepository.deleteById(id);
    }
}

