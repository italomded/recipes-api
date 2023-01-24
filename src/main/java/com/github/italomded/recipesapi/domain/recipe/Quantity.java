package com.github.italomded.recipesapi.domain.recipe;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class Quantity {
    @Column(nullable = false) @Getter @Setter
    private Double amount;
    @Column(nullable = false) @Enumerated(EnumType.STRING) @Getter @Setter
    private Measure measure;
}
