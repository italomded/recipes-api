package com.github.italomded.recipesapi.controller;

import com.github.italomded.recipesapi.domain.Image;
import com.github.italomded.recipesapi.dto.ImageDTO;
import com.github.italomded.recipesapi.dto.form.ImageForm;
import com.github.italomded.recipesapi.service.ImageService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/image")
public class ImageController {
    private ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PutMapping("{id}")
    public ResponseEntity<ImageDTO> editImage(@PathVariable long id, @RequestBody @Valid ImageForm form) {
        Image image = imageService.editImage(id, form);
        return ResponseEntity.ok(new ImageDTO(image));
    }

    @PostMapping("{recipeId}")
    public ResponseEntity<ImageDTO> createImage(@PathVariable long recipeId, @RequestBody @Valid ImageForm form, UriComponentsBuilder uriComponentsBuilder) {
        Image image = imageService.createImage(recipeId, form);
        URI location = uriComponentsBuilder.path("/api/recipe/{id}").buildAndExpand(image.getRecipe().getID()).toUri();
        return ResponseEntity.created(location).body(new ImageDTO(image));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteImage(@PathVariable long id) {
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
