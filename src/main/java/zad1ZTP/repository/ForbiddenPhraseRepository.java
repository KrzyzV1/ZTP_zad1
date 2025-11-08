package zad1ZTP.repository;

import zad1ZTP.model.ForbiddenPhrase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ForbiddenPhraseRepository extends JpaRepository<ForbiddenPhrase, Long> {
    Optional<ForbiddenPhrase> findByPhrase(String phrase);
}
