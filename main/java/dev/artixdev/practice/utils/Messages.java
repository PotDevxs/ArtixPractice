package dev.artixdev.practice.utils;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.MessagesConfig;

/**
 * Acesso centralizado às mensagens do messages.yml.
 * Mensagens são colorizadas com &.
 */
public final class Messages {

    private Messages() {}

    private static MessagesConfig getConfig() {
        Main main = Main.getInstance();
        if (main == null || main.getConfigManager() == null) return null;
        return main.getConfigManager().getMessagesConfig();
    }

    /** Retorna a mensagem colorizada, ou fallback se o config não estiver carregado. */
    public static String get(String key) {
        MessagesConfig config = getConfig();
        if (config == null) return ChatUtils.colorize("&7[" + key + "]");
        return ChatUtils.colorize(config.get(key));
    }

    /** Retorna a mensagem com placeholders substituídos e colorizada. */
    public static String get(String key, String... placeholderPairs) {
        MessagesConfig config = getConfig();
        if (config == null) return ChatUtils.colorize("&7[" + key + "]");
        return ChatUtils.colorize(config.get(key, placeholderPairs));
    }

    /** Retorna o prefixo configurado (colorizado). */
    public static String getPrefix() {
        MessagesConfig config = getConfig();
        if (config == null) return ChatUtils.colorize("&8[&bPractice&8] &r");
        return ChatUtils.colorize(config.getPrefix());
    }
}
