package com.library.librarysystem.util;

import com.library.librarysystem.model.User;
import com.library.librarysystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utility class to retrieve the currently authenticated user,
 * with caching to avoid repeated DB lookups.
 */
@Component
public class AuthUtil {

    @Autowired
    private UserService userService;

    /**
     * Returns the currently authenticated user from the DB.
     * Cached using the username as the key.
     *
     * @return Authenticated User entity
     */
    @Cacheable(value = "currentUser", keyGenerator = "authKeyGenerator")
    public User getCurrentUser() {
        System.out.println("Fetching user from DB");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(auth.getName());
    }

}
