package br.com.italomded.recipesapi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class Quantity {
    @Column(nullable = false)
    private Double amount;
    @Column(nullable = false) @Enumerated(EnumType.STRING)
    private Measure measure;
}
