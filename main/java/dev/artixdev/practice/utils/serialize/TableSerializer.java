package dev.artixdev.practice.utils.serialize;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import dev.artixdev.libs.com.google.gson.JsonDeserializationContext;
import dev.artixdev.libs.com.google.gson.JsonDeserializer;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.com.google.gson.JsonParseException;
import dev.artixdev.libs.com.google.gson.JsonSerializationContext;
import dev.artixdev.libs.com.google.gson.JsonSerializer;

public class TableSerializer implements JsonDeserializer<Table<?, ?, ?>>, JsonSerializer<Table> {
    
    public JsonElement serialize(Table table, Type type, JsonSerializationContext context) {
        return context.serialize(table.rowMap());
    }

    public Table deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        Type[] typeArguments = ((ParameterizedType)type).getActualTypeArguments();
        Type parameterizedType = hashMapOf(typeArguments[0], hashMapOf(typeArguments[1], typeArguments[2]).getType()).getType();
        Map<?, Map<?, ?>> map = (Map)context.deserialize(json, parameterizedType);
        Table<Object, Object, Object> table = HashBasedTable.create();
        Iterator<?> rowIterator = map.keySet().iterator();

        while(rowIterator.hasNext()) {
            Object rowKey = rowIterator.next();
            Map<?, ?> rowMap = (Map)map.get(rowKey);
            Iterator<?> columnIterator = rowMap.keySet().iterator();

            while(columnIterator.hasNext()) {
                Object columnKey = columnIterator.next();
                Object value = rowMap.get(columnKey);
                table.put(rowKey, columnKey, value);
            }
        }

        return table;
    }

    static <K, V> TypeToken<HashMap<K, V>> hashMapOf(Type key, Type value) {
        TypeParameter<K> newKeyTypeParameter = new TypeParameter<K>() {
        };
        TypeParameter<V> newValueTypeParameter = new TypeParameter<V>() {
        };
        return new TypeToken<HashMap<K, V>>() {
        }.where(newKeyTypeParameter, (TypeToken)key).where(newValueTypeParameter, (TypeToken)value);
    }
}
