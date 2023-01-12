package com.github.italomded.recipesapi.controller;

import com.github.italomded.recipesapi.dto.form.RecipeEditForm;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("Hello")
public class HelloController {

    @PostMapping
    public String helloWorld(@Valid @RequestBody RecipeEditForm recipeEditForm) {
        System.out.println(recipeEditForm.title());
        System.out.println(recipeEditForm.description());
        return "Hello World!";
    }

}
