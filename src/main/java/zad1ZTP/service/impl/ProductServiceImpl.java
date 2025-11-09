package zad1ZTP.service.impl;

import zad1ZTP.exception.ResourceNotFoundException;
import zad1ZTP.exception.ValidationException;
import zad1ZTP.model.ForbiddenPhrase;
import zad1ZTP.model.Product;
import zad1ZTP.model.ProductHistory;
import zad1ZTP.repository.ForbiddenPhraseRepository;
import zad1ZTP.repository.ProductHistoryRepository;
import zad1ZTP.repository.ProductRepository;
import zad1ZTP.service.ProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ForbiddenPhraseRepository forbiddenPhraseRepository;
    private final ProductHistoryRepository historyRepository;

    private static final Map<Product.Category, BigDecimal[]> PRICE_RANGE = Map.of(
            Product.Category.ELECTRONICS, new BigDecimal[]{BigDecimal.valueOf(50), BigDecimal.valueOf(10000)},
            Product.Category.BOOKS, new BigDecimal[]{BigDecimal.valueOf(15), BigDecimal.valueOf(300)},
            Product.Category.CLOTHING, new BigDecimal[]{BigDecimal.valueOf(30), BigDecimal.valueOf(2000)}
    );

    @Override
    @Transactional
    public Product create(Product product) {
        validateName(product.getName(), null);
        validatePrice(product.getPrice(), product.getCategory());
        validateQuantity(product.getQuantity());
        checkForbidden(product.getName());

        if (productRepository.existsByNameIgnoreCase(product.getName())) {
            throw new ValidationException("name", "A product with this name already exists");
        }

        Product saved = productRepository.save(product);

        saveHistory(saved, "created", null, snapshot(saved));

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> listAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }


    @Override
    @Transactional
    public Product update(Long id, Product product) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        // zachowanie starych wartości
        String oldName = existing.getName();
        BigDecimal oldPrice = existing.getPrice();
        Integer oldQty = existing.getQuantity();
        Product.Category oldCat = existing.getCategory();

        // Name
        if (!existing.getName().equalsIgnoreCase(product.getName())) {
            validateName(product.getName(), id);
            productRepository.findByNameIgnoreCase(product.getName())
                    .ifPresent(p -> {
                        if (!p.getId().equals(id)) {
                            throw new ValidationException("name", "A product with this name already exists");
                        }
                    });
            checkForbidden(product.getName());
            existing.setName(product.getName());
        }

        // Category
        existing.setCategory(product.getCategory());

        // Price
        validatePrice(product.getPrice(), existing.getCategory());
        existing.setPrice(product.getPrice());

        // Quantity
        validateQuantity(product.getQuantity());
        existing.setQuantity(product.getQuantity());

        Product saved = productRepository.save(existing);

        // zapisz do historii tylko zmienione pola
        if (!oldName.equals(saved.getName())) {
            saveHistory(saved, "name", oldName, saved.getName());
        }
        if (oldPrice == null || saved.getPrice() == null || oldPrice.compareTo(saved.getPrice()) != 0) {
            saveHistory(saved, "price", oldPrice == null ? null : oldPrice.toString(), saved.getPrice().toString());
        }
        if (!oldQty.equals(saved.getQuantity())) {
            saveHistory(saved, "quantity", oldQty == null ? null : oldQty.toString(), saved.getQuantity().toString());
        }
        if (oldCat != saved.getCategory()) {
            saveHistory(saved, "category", oldCat == null ? null : oldCat.name(), saved.getCategory().name());
        }

        return saved;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
        productRepository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductHistory> getHistory(Long productId) {
        return historyRepository.findByProduct_IdOrderByChangedAtDesc(productId);
    }

    // ---------- helpers ----------

    private void saveHistory(Product product, String field, String oldValue, String newValue) {
        ProductHistory h = ProductHistory.builder()
                .product(product)
                .changedAt(LocalDateTime.now())
                .fieldName(field)
                .oldValue(oldValue)
                .newValue(newValue)
                .build();
        historyRepository.save(h);
    }

    private String snapshot(Product p) {
        return String.format("id=%s;name=%s;category=%s;price=%s;quantity=%s",
                p.getId(), p.getName(), p.getCategory() == null ? null : p.getCategory().name(),
                p.getPrice() == null ? null : p.getPrice().toString(),
                p.getQuantity());
    }

    private void validateName(String name, Long selfId) {
        if (name == null || name.isBlank()) throw new ValidationException("name", "Name must not be blank");
        if (name.length() < 3 || name.length() > 20) throw new ValidationException("name", "Name length must be between 3 and 20 characters");
        if (!name.matches("^[A-Za-z0-9]+$")) throw new ValidationException("name", "Name must contain only letters and digits");
    }

    private void validatePrice(BigDecimal price, Product.Category category) {
        if (price == null) throw new ValidationException("price", "Price is required");
        BigDecimal[] range = PRICE_RANGE.get(category);
        if (range == null) throw new ValidationException("price", "Price range not defined for category");
        if (price.compareTo(range[0]) < 0 || price.compareTo(range[1]) > 0) {
            throw new ValidationException("price",
                    String.format("Price must be between %s and %s for %s", range[0], range[1], category.name()));
        }
    }

    private void validateQuantity(Integer qty) {
        if (qty == null) throw new ValidationException("quantity", "Quantity is required");
        if (qty < 0) throw new ValidationException("quantity", "Quantity must be greater than or equal to 0");
    }

    private void checkForbidden(String name) {
        if (name == null) return;
        String lower = name.toLowerCase();
        List<String> phrases = forbiddenPhraseRepository.findAll()
                .stream().map(ForbiddenPhrase::getPhrase).collect(Collectors.toList());
        for (String p : phrases) {
            if (p == null) continue;
            if (lower.contains(p.toLowerCase())) {
                throw new ValidationException("name", "Name contains forbidden phrase: " + p);
            }
        }
    }
}