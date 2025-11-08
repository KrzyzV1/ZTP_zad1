package zad1ZTP.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "forbidden_phrases")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ForbiddenPhrase {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String phrase; // store normalized (lowercase) value
}
