package com.github.italomded.recipesapi.builder;

import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import com.github.italomded.recipesapi.domain.user.Role;

import java.lang.reflect.Field;

public class ApplicationUserBuilder {
    private ApplicationUser applicationUser;

    private Field idField;

    private ApplicationUserBuilder() {
        try {
            idField = ApplicationUser.class.getDeclaredField("ID");
            idField.setAccessible(true);
            applicationUser = new ApplicationUser();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static ApplicationUserBuilder builder() {
        return new ApplicationUserBuilder();
    }

    public ApplicationUserBuilder withId(Long id) {
        try {
            idField.set(applicationUser, id);
            return this;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public ApplicationUserBuilder withPassword(String password) {
        applicationUser.setPassword(password);
        return this;
    }

    public ApplicationUserBuilder withRole(Role role) {
        applicationUser.setRole(role);
        return this;
    }

    public ApplicationUser build() {
        return applicationUser;
    }
}
