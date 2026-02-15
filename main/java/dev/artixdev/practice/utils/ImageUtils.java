package dev.artixdev.practice.utils;

import java.awt.Font;
import java.awt.image.BufferedImage;

public final class ImageUtils {

    private ImageUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static BufferedImage createTextImage(Font font, String text, int color) {
        // Create buffered image with text
        // Implementation would depend on specific requirements
        return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    }
}
