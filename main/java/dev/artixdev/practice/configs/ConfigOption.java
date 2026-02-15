package dev.artixdev.practice.configs;

public class ConfigOption<T> {
    
    private final String name;
    private final String comment;
    private final String path;
    private final int priority;
    private final String defaultValue;
    private T value;
    private final Class<T> type;

    public ConfigOption(String name, String comment, String path, int priority, String defaultValue, Class<T> type) {
        this.name = name;
        this.comment = comment;
        this.path = path;
        this.priority = priority;
        this.defaultValue = defaultValue;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getPath() {
        return path;
    }

    public int getPriority() {
        return priority;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Class<T> getType() {
        return type;
    }
}
