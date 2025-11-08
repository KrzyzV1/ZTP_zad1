package zad1ZTP.service.impl;

import zad1ZTP.model.ForbiddenPhrase;
import zad1ZTP.repository.ForbiddenPhraseRepository;
import zad1ZTP.service.ForbiddenPhraseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForbiddenPhraseServiceImpl implements ForbiddenPhraseService {

    private final ForbiddenPhraseRepository repo;

    @Override
    @Transactional(readOnly = true)
    public List<ForbiddenPhrase> listAll() {
        return repo.findAll();
    }

    @Override
    @Transactional
    public ForbiddenPhrase add(ForbiddenPhrase phrase) {
        String normalized = phrase.getPhrase().trim().toLowerCase();
        if (repo.findByPhrase(normalized).isPresent()) {
            throw new RuntimeException("Phrase already exists");
        }
        phrase.setPhrase(normalized);
        return repo.save(phrase);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
