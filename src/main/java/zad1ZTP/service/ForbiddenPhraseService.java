package zad1ZTP.service;

import zad1ZTP.model.ForbiddenPhrase;
import java.util.List;

public interface ForbiddenPhraseService {
    List<ForbiddenPhrase> listAll();
    ForbiddenPhrase add(ForbiddenPhrase phrase);
    void delete(Long id);
}
