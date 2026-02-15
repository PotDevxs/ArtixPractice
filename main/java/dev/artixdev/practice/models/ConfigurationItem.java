package dev.artixdev.practice.models;

/**
 * Simple data holder for a single configuration entry used by {@link dev.artixdev.practice.interfaces.ConfigurationInterface}.
 */
public class ConfigurationItem {

    private String path;
    private Object value;

    public ConfigurationItem() {
    }

    public ConfigurationItem(String path, Object value) {
        this.path = path;
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
