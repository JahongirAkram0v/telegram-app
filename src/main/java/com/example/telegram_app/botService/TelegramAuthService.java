package com.example.telegram_app.botService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

public class TelegramAuthService {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String TELEGRAM_BOT_TOKEN = dotenv.get("TELEGRAM_BOT_TOKEN");
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final long AUTH_DATA_LIFESPAN_SECONDS = 3600;

    public static boolean verifyInitData(String initData) {
        if (TELEGRAM_BOT_TOKEN == null || TELEGRAM_BOT_TOKEN.isEmpty()) return false;
        if (initData == null || initData.isEmpty()) return false;

        Map<String, String> data = parseInitData(initData);
        String receivedHash = data.remove("hash");
        if (receivedHash == null) return false;

        String authDateStr = data.get("auth_date");
        if (authDateStr != null) {
            long authDate = Long.parseLong(authDateStr);
            long now = System.currentTimeMillis() / 1000;
            if (now - authDate > AUTH_DATA_LIFESPAN_SECONDS) return false;
        }

        List<String> keys = new ArrayList<>(data.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            sb.append(keys.get(i)).append("=").append(data.get(keys.get(i)));
            if (i < keys.size() - 1) sb.append("\n");
        }

        byte[] secretKey = hmacSha256("WebAppData".getBytes(StandardCharsets.UTF_8),
                TELEGRAM_BOT_TOKEN.getBytes(StandardCharsets.UTF_8));
        byte[] expectedHash = hmacSha256(secretKey, sb.toString().getBytes(StandardCharsets.UTF_8));

        return MessageDigest.isEqual(expectedHash, hexToBytes(receivedHash));
    }

    public static Long extractUserId(String initData) {
        try {
            Map<String, String> data = parseInitData(initData);
            String userJson = data.get("user");
            if (userJson == null) return null;
            JsonNode node = MAPPER.readTree(userJson);
            return node.has("id") ? node.get("id").asLong() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private static Map<String, String> parseInitData(String initData) {
        Map<String, String> map = new HashMap<>();
        for (String part : initData.split("&")) {
            String[] kv = part.split("=", 2);
            if (kv.length == 2) {
                map.put(URLDecoder.decode(kv[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
            }
        }
        return map;
    }

    private static byte[] hmacSha256(byte[] key, byte[] data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            return mac.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] out = new byte[len / 2];
        for (int i = 0; i < len; i += 2)
            out[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        return out;
    }
}
