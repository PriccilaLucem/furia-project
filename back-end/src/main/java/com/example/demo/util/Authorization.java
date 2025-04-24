package com.example.demo.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class Authorization {
    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.verifyer().verify(hashedPassword.toCharArray(), password.toCharArray()).verified;
    }
}
