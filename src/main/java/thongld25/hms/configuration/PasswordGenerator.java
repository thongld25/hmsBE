package thongld25.hms.configuration;

import java.security.SecureRandom;
import java.util.Base64;

public class PasswordGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int PASSWORD_LENGTH = 8;  // Độ dài mật khẩu

    public static String generateRandomPassword() {
        byte[] bytes = new byte[PASSWORD_LENGTH];
        RANDOM.nextBytes(bytes);
        return Base64.getEncoder().withoutPadding().encodeToString(bytes).substring(0, PASSWORD_LENGTH);
    }
}
