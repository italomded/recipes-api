package com.github.italomded.recipesapi.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@EqualsAndHashCode(of = {"ID"})
@Entity(name = "image")
public class Image {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Getter
    private Long ID;

    @Column(nullable = false) @Getter @Setter
    private byte[] imageBytes;
    @JoinColumn(nullable = false) @ManyToOne @Getter
    private Recipe recipe;

    public Image(byte[] imageBytes, Recipe recipe) {
        this.imageBytes = imageBytes;
        this.recipe = recipe;
    }
}
