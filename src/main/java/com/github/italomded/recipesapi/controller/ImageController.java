package com.github.italomded.recipesapi.controller;

import com.github.italomded.recipesapi.dto.form.ImageForm;
import com.github.italomded.recipesapi.service.ImageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Long> editImage(@PathVariable long id, @RequestBody @Valid ImageForm form) {
        Long idEdited = imageService.editImage(id, form);
        return ResponseEntity.ok(idEdited);
    }

    @PostMapping("{recipeId}")
    public ResponseEntity<Long> createImage(@PathVariable long recipeId, @RequestBody @Valid ImageForm form) {
        Long idCreated = imageService.createImage(recipeId, form);
        URI location = URI.create(String.format("/api/recipe/%d", idCreated));
        return ResponseEntity.created(location).body(idCreated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteImage(@PathVariable long id) {
        imageService.deleteImage(id);
        return ResponseEntity.ok().build();
    }
}
