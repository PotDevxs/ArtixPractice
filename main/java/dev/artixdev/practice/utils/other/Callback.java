package dev.artixdev.practice.utils.other;

import com.google.common.base.Preconditions;

@FunctionalInterface
public interface Callback<T> {
    /**
     * Call the callback with a value
     * @param value the value to pass to the callback
     */
    void call(T value);
    
    /**
     * Abstract class implementation for backward compatibility
     */
    @Deprecated
    abstract class AbstractCallback<T> implements Callback<T> {
        private final T firstValue;
        private final T secondValue;
        private final CallbackType callbackType;
        private final CallbackPriority priority;

        public AbstractCallback(T firstValue, T secondValue, CallbackType callbackType, CallbackPriority priority) {
            this.firstValue = Preconditions.checkNotNull(firstValue, "First value cannot be null");
            this.secondValue = Preconditions.checkNotNull(secondValue, "Second value cannot be null");
            this.callbackType = Preconditions.checkNotNull(callbackType, "Callback type cannot be null");
            this.priority = Preconditions.checkNotNull(priority, "Callback priority cannot be null");
        }

        public T getFirstValue() {
            return firstValue;
        }

        public T getSecondValue() {
            return secondValue;
        }

        public CallbackType getCallbackType() {
            return callbackType;
        }

        public CallbackPriority getPriority() {
            return priority;
        }

        public abstract void execute();

        public abstract boolean isValid();
        
        @Override
        public void call(T value) {
            execute();
        }
    }
}