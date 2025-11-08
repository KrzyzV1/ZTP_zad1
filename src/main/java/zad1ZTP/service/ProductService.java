package zad1ZTP.service;

import zad1ZTP.model.Product;
import zad1ZTP.model.ProductHistory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product create(Product product);
    Product update(Long id, Product product);
    void delete(Long id);
    Product getById(Long id);
    Page<Product> listAll(Pageable pageable);
    List<ProductHistory> getHistory(Long productId);
}
