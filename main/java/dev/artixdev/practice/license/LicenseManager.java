package dev.artixdev.practice.license;

import dev.artixdev.practice.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Validação de licença Artix.
 * Lê a chave de config.yml (License:) e valida contra a API.
 * Chave de desenvolvedor (bypass API): ARTIX-DEVELOPERS-USER-MAX
 */
public class LicenseManager {

    /** Chave mestre para devs: não passa pela API. */
    private static final String DEV_MASTER_KEY = "ARTIX-DEVELOPERS-USER-MAX";

    private static final Logger LOGGER = LogManager.getLogger(LicenseManager.class);
    private static final String API_URL = "https://artix.sideload.lol/api/license/validate";
    private static final int CONNECT_TIMEOUT_MS = 10_000;
    private static final int READ_TIMEOUT_MS = 10_000;

    private final JavaPlugin plugin;
    private final AtomicBoolean validated = new AtomicBoolean(false);
    private final AtomicLong lastSuccessAt = new AtomicLong(0L);
    private final AtomicReference<String> lastMessage = new AtomicReference<>("");

    public LicenseManager(Main main) {
        this.plugin = main.getPlugin();
    }

    /**
     * Obtém a chave de licença de config.yml (chave "License").
     */
    public String getLicenseKey() {
        FileConfiguration config = plugin.getConfig();
        if (config == null) return "";
        String key = config.getString("License", "").trim();
        return key != null ? key : "";
    }

    /**
     * Executado no enable: valida a licença. Se a key estiver vazia, placeholder ou inativa na API,
     * retorna false e o plugin deve ser desligado exibindo "Key Invalida".
     */
    public boolean runOnEnable() {
        String key = getLicenseKey();
        if (key == null || key.isEmpty() || "Sua Key".equalsIgnoreCase(key)) {
            lastMessage.set("Key Invalida");
            LOGGER.error("[ArtixPractice] Key Invalida. Configure uma licenca valida em config.yml (License:) e reinicie.");
            return false;
        }
        if (DEV_MASTER_KEY.equalsIgnoreCase(key)) {
            validated.set(true);
            lastSuccessAt.set(System.currentTimeMillis());
            lastMessage.set("Developer master key (bypass API)");
            LOGGER.info("License validated via developer master key.");
            return true;
        }
        try {
            ValidationResult result = validateOnce(API_URL, key, "Server", plugin.getDescription().getVersion());
            if (result != null && result.isValid()) {
                validated.set(true);
                lastSuccessAt.set(System.currentTimeMillis());
                lastMessage.set(result.getMessage());
                LOGGER.info("License validated successfully.");
                return true;
            }
            lastMessage.set("Key Invalida");
            String msg = result != null ? result.getMessage() : "Validation failed";
            LOGGER.error("[ArtixPractice] Key Invalida. " + msg);
            return false;
        } catch (Exception e) {
            lastMessage.set("Key Invalida");
            LOGGER.error("[ArtixPractice] Key Invalida. Erro ao verificar: " + e.getMessage());
            return false;
        }
    }

    public void onDisable() {
        validated.set(false);
    }

    public boolean isValidated() {
        return validated.get();
    }

    public long getLastSuccessAtFromCache() {
        return lastSuccessAt.get();
    }

    public String getLastMessage() {
        return lastMessage.get();
    }

    /**
     * Valida de forma assíncrona (para comando /artixlicense validate).
     */
    public void validateAsync(boolean fromCommand) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            String key = getLicenseKey();
            if (key == null || key.isEmpty()) {
                lastMessage.set("No license key in config.yml");
                validated.set(false);
                return;
            }
            if (DEV_MASTER_KEY.equalsIgnoreCase(key)) {
                validated.set(true);
                lastSuccessAt.set(System.currentTimeMillis());
                lastMessage.set("Developer master key (bypass API)");
                return;
            }
            try {
                ValidationResult result = validateOnce(API_URL, key, "Server", plugin.getDescription().getVersion());
                if (result != null && result.isValid()) {
                    validated.set(true);
                    lastSuccessAt.set(System.currentTimeMillis());
                    lastMessage.set(result.getMessage());
                } else {
                    validated.set(false);
                    lastMessage.set(result != null ? result.getMessage() : "Invalid");
                }
            } catch (Exception e) {
                validated.set(false);
                lastMessage.set(e.getMessage());
            }
        });
    }

    private static ValidationResult validateOnce(String apiUrl, String licenseKey, String serverName, String pluginVersion) throws Exception {
        String body = toJsonBody(licenseKey, serverName, pluginVersion);
        URL url = new URL(apiUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
        conn.setReadTimeout(READ_TIMEOUT_MS);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }
        int code = conn.getResponseCode();
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(code >= 200 && code < 300 ? conn.getInputStream() : conn.getErrorStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        String json = response.toString();
        boolean valid = code == 200 && json.toLowerCase().contains("\"valid\":true");
        return new ValidationResult(valid, json.isEmpty() ? "HTTP " + code : json);
    }

    private static String toJsonBody(String licenseKey, String serverName, String pluginVersion) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"licenseKey\":\"").append(escapeJson(licenseKey)).append('"');
        sb.append(",\"serverName\":\"").append(escapeJson(serverName)).append('"');
        sb.append(",\"pluginVersion\":\"").append(escapeJson(pluginVersion != null ? pluginVersion : "")).append("\"}");
        return sb.toString();
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    public static final class ValidationResult {
        private final boolean valid;
        private final String message;

        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message != null ? message : "";
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }
}
