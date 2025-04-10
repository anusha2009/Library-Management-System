package com.library.librarysystem.util;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Custom cache key generator that extracts the username from the security context.
 */
@Component("authKeyGenerator")
public class AuthKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName(); 
    }
}

