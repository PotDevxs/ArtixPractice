package dev.artixdev.practice.models;

import java.util.Collections;
import java.util.List;

/**
 * Produto do shop (shop.yml). type: TITLE, RESET_KDR, RESET_ELO, RESET_WINS_LOSSES, RESET_WINSTREAK, RESET_STATS.
 */
public class ShopProduct {

    private final String id;
    private final String type;
    private final String titleId;
    private final int price;
    private final String displayName;
    private final String material;
    private final List<String> description;

    public ShopProduct(String id, String type, String titleId, int price, String displayName, String material, List<String> description) {
        this.id = id;
        this.type = type == null ? "" : type.toUpperCase();
        this.titleId = titleId;
        this.price = Math.max(0, price);
        this.displayName = displayName == null ? id : displayName;
        this.material = material == null ? "PAPER" : material;
        this.description = description == null ? Collections.emptyList() : description;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    /** For TITLE type: PlayerTitle id. */
    public String getTitleId() {
        return titleId;
    }

    public int getPrice() {
        return price;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getMaterial() {
        return material;
    }

    public List<String> getDescription() {
        return description;
    }

    public boolean isTitle() {
        return "TITLE".equals(type);
    }

    public boolean isResetKdr() {
        return "RESET_KDR".equals(type);
    }

    public boolean isResetElo() {
        return "RESET_ELO".equals(type);
    }

    public boolean isResetWinsLosses() {
        return "RESET_WINS_LOSSES".equals(type);
    }

    public boolean isResetWinstreak() {
        return "RESET_WINSTREAK".equals(type);
    }

    public boolean isResetStats() {
        return "RESET_STATS".equals(type);
    }
}
