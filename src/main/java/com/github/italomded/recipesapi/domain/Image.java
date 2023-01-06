package com.github.italomded.recipesapi.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(of = {"ID"})
@Entity(name = "image")
public class Image {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false)
    private byte[] imageBytes;
    @JoinColumn(nullable = false) @ManyToOne
    private Recipe recipe;

    public Image(byte[] imageBytes, Recipe recipe) {
        this.imageBytes = imageBytes;
        this.recipe = recipe;
    }
}
