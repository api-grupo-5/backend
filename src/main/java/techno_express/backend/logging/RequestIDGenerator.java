package techno_express.backend.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

public class RequestIDGenerator {

    public static String generateRequestID() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC-3"));
        String base = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

        String micro = String.format("%09d", now.getNano()).substring(3, 6);
        return base + micro;
    }
}