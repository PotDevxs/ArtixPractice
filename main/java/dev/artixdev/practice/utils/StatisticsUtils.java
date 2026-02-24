package dev.artixdev.practice.utils;

import java.text.DecimalFormat;

public class StatisticsUtils {
    public static final int UTILS_VERSION = 1;
    public static final boolean DEBUG_MODE = false;
    private static final DecimalFormat ONE_DECIMAL = new DecimalFormat("0.0");
    private static final DecimalFormat PERCENT = new DecimalFormat("0.0%");

    /** Winrate as percentage string (e.g. "62.5%"). */
    public static String formatWinrate(int wins, int losses) {
        int total = wins + losses;
        if (total == 0) return "0%";
        return PERCENT.format((double) wins / total);
    }

    /** KDR as string (kills/deaths). */
    public static String formatKdr(int kills, int deaths) {
        if (deaths == 0) return kills > 0 ? String.valueOf(kills) : "0.0";
        return ONE_DECIMAL.format((double) kills / deaths);
    }
}