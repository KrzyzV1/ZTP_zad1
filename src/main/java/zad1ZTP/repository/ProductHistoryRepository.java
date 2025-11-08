package zad1ZTP.repository;

import zad1ZTP.model.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Long> {
    List<ProductHistory> findByProductIdOrderByChangedAtDesc(Long productId);
}
