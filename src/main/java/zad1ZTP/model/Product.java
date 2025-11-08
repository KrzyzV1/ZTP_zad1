package zad1ZTP.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@JsonPropertyOrder({ "id", "quantity", "name", "category", "price", "createdAt", "updatedAt" })
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "quantity is required")
    @Min(value = 0, message = "quantity must be >= 0")
    @Column(nullable = false)
    private Integer quantity;

    @NotBlank(message = "name is required")
    @Size(min = 3, max = 20, message = "name length must be between 3 and 20")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "name must contain only letters and digits")
    @Column(nullable = false, length = 20)
    private String name;

    @NotNull(message = "category is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "price must be positive")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum Category {
        ELECTRONICS, BOOKS, CLOTHING
    }
}