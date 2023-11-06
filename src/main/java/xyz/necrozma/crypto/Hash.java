package xyz.necrozma.crypto;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

public class Hash {
    private static final Argon2PasswordEncoder arg2SpringSecurity = new Argon2PasswordEncoder(16, 32, 1, 60000, 10);
    public static String hash(String input) {
        return arg2SpringSecurity.encode(input);
    }

    public static boolean verify(String input, String hash) {
        return arg2SpringSecurity.matches(input, hash);
    }
}
