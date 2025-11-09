package zad1ZTP.service;

import zad1ZTP.model.Product;
import zad1ZTP.model.ProductHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Product create(Product product);
    Product update(Long id, Product product);
    void delete(Long id);
    Product getById(Long id);
    List<ProductHistory> getHistory(Long productId);
    Page<Product> listAll(Pageable pageable);
}
