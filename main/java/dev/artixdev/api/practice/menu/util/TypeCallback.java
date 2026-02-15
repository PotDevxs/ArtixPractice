package dev.artixdev.api.practice.menu.util;

import java.io.Serializable;

public interface TypeCallback<T> extends Serializable {
   void callback(T var1);
}
