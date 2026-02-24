package dev.artixdev.practice.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Estilo moderno para menus (tooltips com título em vermelho, descrição com |, ação em verde).
 * Uso: ModernMenuStyle.tooltip("Title", "Line 1", "Line 2").status("• Unlocked").action("Click to open.")
 */
public final class ModernMenuStyle {

    private ModernMenuStyle() {}

    /** Prefixo para linhas de descrição (barra vertical) */
    public static final String DESC_PREFIX = "&7| ";
    /** Prefixo para status/estado atual (bullet verde) */
    public static final String STATUS_PREFIX = "&a• ";
    /** Prefixo para ação no final (verde) */
    public static final String ACTION_PREFIX = "&a";

    /**
     * Constrói lore no estilo moderno: título em vermelho, descrição com |, opcional status e ação.
     * @param title Título do item (será aplicado &c&l se não tiver cor)
     * @param descriptionLines Linhas de descrição (cada uma recebe DESC_PREFIX)
     * @param status Linha de status opcional (ex: "• Unrestricted") - pode ser null
     * @param actionTexto Texto de ação no final (ex: "Click to open.") - pode ser null
     * @return Lista de linhas prontas para ItemBuilder.lore()
     */
    public static List<String> tooltip(String title, String[] descriptionLines, String status, String actionTexto) {
        List<String> lore = new ArrayList<>();
        if (title != null && !title.isEmpty()) {
            String t = title.startsWith("&") ? title : "&c&l" + title;
            lore.add(t);
        }
        if (descriptionLines != null) {
            for (String line : descriptionLines) {
                if (line != null) lore.add(DESC_PREFIX + line);
            }
        }
        if (status != null && !status.isEmpty()) {
            lore.add("");
            lore.add(status.startsWith("&") ? status : STATUS_PREFIX + status);
        }
        if (actionTexto != null && !actionTexto.isEmpty()) {
            lore.add("");
            lore.add((actionTexto.startsWith("&") ? actionTexto : ACTION_PREFIX + actionTexto));
        }
        return lore;
    }

    /** Versão com descrição variável. */
    public static List<String> tooltip(String title, String line1, String line2, String status, String action) {
        return tooltip(title, line2 != null ? new String[]{line1, line2} : new String[]{line1}, status, action);
    }

    /** Apenas título + descrição + ação (sem status). */
    public static List<String> tooltip(String title, String[] descriptionLines, String actionTexto) {
        return tooltip(title, descriptionLines, null, actionTexto);
    }

    /** Título + uma linha de descrição + ação. */
    public static List<String> tooltip(String title, String description, String actionTexto) {
        return tooltip(title, new String[]{description}, null, actionTexto);
    }

    /** Lore do botão fechar (sem título; usar name "&c&lClose" no item). */
    public static List<String> closeButtonLore() {
        return Arrays.asList(DESC_PREFIX + "Return to the previous menu.", "", "&cClick to close.");
    }
}
