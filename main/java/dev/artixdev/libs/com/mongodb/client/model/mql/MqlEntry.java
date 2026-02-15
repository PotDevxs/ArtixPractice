package dev.artixdev.libs.com.mongodb.client.model.mql;

import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.annotations.Sealed;

@Sealed
@Beta({Beta.Reason.CLIENT})
public interface MqlEntry<T extends MqlValue> extends MqlValue {
   MqlString getKey();

   T getValue();

   MqlEntry<T> setValue(T var1);

   MqlEntry<T> setKey(MqlString var1);
}
