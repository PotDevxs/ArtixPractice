package dev.artixdev.libs.com.mongodb.client.model.vault;

import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonValue;

@Beta({Beta.Reason.SERVER})
public class RangeOptions {
   private BsonValue min;
   private BsonValue max;
   private Long sparsity;
   private Integer precision;

   public RangeOptions min(@Nullable BsonValue min) {
      this.min = min;
      return this;
   }

   @Nullable
   public BsonValue getMin() {
      return this.min;
   }

   public RangeOptions max(@Nullable BsonValue max) {
      this.max = max;
      return this;
   }

   @Nullable
   public BsonValue getMax() {
      return this.max;
   }

   public RangeOptions sparsity(@Nullable Long sparsity) {
      this.sparsity = sparsity;
      return this;
   }

   @Nullable
   public Long getSparsity() {
      return this.sparsity;
   }

   public RangeOptions precision(@Nullable Integer precision) {
      this.precision = precision;
      return this;
   }

   @Nullable
   public Integer getPrecision() {
      return this.precision;
   }

   public String toString() {
      return "RangeOptions{min=" + this.min + ", max=" + this.max + ", sparsity=" + this.sparsity + ", precision=" + this.precision + '}';
   }
}
