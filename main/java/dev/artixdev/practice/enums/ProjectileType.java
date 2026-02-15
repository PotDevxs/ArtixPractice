package dev.artixdev.practice.enums;

public enum ProjectileType {
    
    ARROW("Arrow", true),
    SPECTRAL_ARROW("Spectral Arrow", true),
    TIPPED_ARROW("Tipped Arrow", true),
    SNOWBALL("Snowball", false),
    EGG("Egg", false),
    FIREBALL("Fireball", true),
    ENDER_PEARL("Ender Pearl", true),
    SPLASH_POTION("Splash Potion", false),
    LINGERING_POTION("Lingering Potion", false),
    TRIDENT("Trident", true),
    FIREWORK("Firework", false),
    FIREWORK_ROCKET("Firework Rocket", false),
    BLAZE_POWDER("Blaze Powder", false);

    private final String name;
    private boolean enabled;

    ProjectileType(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }
    
    public String getDisplayName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}