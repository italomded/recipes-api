package com.github.italomded.recipesapi.builder;

import com.github.italomded.recipesapi.domain.user.ApplicationUser;

import java.lang.reflect.Field;

public class ApplicationUserBuilder {
    private ApplicationUser applicationUser;

    private Field idField;

    public ApplicationUserBuilder() {
        try {
            idField = ApplicationUser.class.getDeclaredField("ID");
            idField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public ApplicationUserBuilder create() {
        applicationUser = new ApplicationUser();
        return this;
    }

    public ApplicationUserBuilder withId(Long id) {
        try {
            idField.set(applicationUser, id);
            return this;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public ApplicationUser build() {
        ApplicationUser returnApplicationUser = applicationUser;
        applicationUser = null;
        return returnApplicationUser;
    }
}
