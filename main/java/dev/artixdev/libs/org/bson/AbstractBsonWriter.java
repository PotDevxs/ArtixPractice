package dev.artixdev.libs.org.bson;

import java.io.Closeable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.Map.Entry;
import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.types.Decimal128;
import dev.artixdev.libs.org.bson.types.ObjectId;

public abstract class AbstractBsonWriter implements Closeable, BsonWriter {
   private final BsonWriterSettings settings;
   private final Stack<FieldNameValidator> fieldNameValidatorStack;
   private AbstractBsonWriter.State state;
   private AbstractBsonWriter.Context context;
   private int serializationDepth;
   private boolean closed;

   protected AbstractBsonWriter(BsonWriterSettings settings) {
      this(settings, new NoOpFieldNameValidator());
   }

   protected AbstractBsonWriter(BsonWriterSettings settings, FieldNameValidator validator) {
      this.fieldNameValidatorStack = new Stack();
      if (validator == null) {
         throw new IllegalArgumentException("Validator can not be null");
      } else {
         this.settings = settings;
         this.fieldNameValidatorStack.push(validator);
         this.state = AbstractBsonWriter.State.INITIAL;
      }
   }

   protected String getName() {
      return this.context.name;
   }

   protected boolean isClosed() {
      return this.closed;
   }

   protected void setState(AbstractBsonWriter.State state) {
      this.state = state;
   }

   protected AbstractBsonWriter.State getState() {
      return this.state;
   }

   protected AbstractBsonWriter.Context getContext() {
      return this.context;
   }

   protected void setContext(AbstractBsonWriter.Context context) {
      this.context = context;
   }

   protected abstract void doWriteStartDocument();

   protected abstract void doWriteEndDocument();

   protected abstract void doWriteStartArray();

   protected abstract void doWriteEndArray();

   protected abstract void doWriteBinaryData(BsonBinary var1);

   protected abstract void doWriteBoolean(boolean var1);

   protected abstract void doWriteDateTime(long var1);

   protected abstract void doWriteDBPointer(BsonDbPointer var1);

   protected abstract void doWriteDouble(double var1);

   protected abstract void doWriteInt32(int var1);

   protected abstract void doWriteInt64(long var1);

   protected abstract void doWriteDecimal128(Decimal128 var1);

   protected abstract void doWriteJavaScript(String var1);

   protected abstract void doWriteJavaScriptWithScope(String var1);

   protected abstract void doWriteMaxKey();

   protected abstract void doWriteMinKey();

   protected abstract void doWriteNull();

   protected abstract void doWriteObjectId(ObjectId var1);

   protected abstract void doWriteRegularExpression(BsonRegularExpression var1);

   protected abstract void doWriteString(String var1);

   protected abstract void doWriteSymbol(String var1);

   protected abstract void doWriteTimestamp(BsonTimestamp var1);

   protected abstract void doWriteUndefined();

   public void writeStartDocument(String name) {
      this.writeName(name);
      this.writeStartDocument();
   }

   public void writeStartDocument() {
      this.checkPreconditions("writeStartDocument", AbstractBsonWriter.State.INITIAL, AbstractBsonWriter.State.VALUE, AbstractBsonWriter.State.SCOPE_DOCUMENT, AbstractBsonWriter.State.DONE);
      if (this.context != null && this.context.name != null) {
         FieldNameValidator validator = ((FieldNameValidator)this.fieldNameValidatorStack.peek()).getValidatorForField(this.getName());
         this.fieldNameValidatorStack.push(validator);
         validator.start();
      }

      ++this.serializationDepth;
      if (this.serializationDepth > this.settings.getMaxSerializationDepth()) {
         throw new BsonSerializationException("Maximum serialization depth exceeded (does the object being serialized have a circular reference?).");
      } else {
         this.doWriteStartDocument();
         this.setState(AbstractBsonWriter.State.NAME);
      }
   }

   public void writeEndDocument() {
      this.checkPreconditions("writeEndDocument", AbstractBsonWriter.State.NAME);
      BsonContextType contextType = this.getContext().getContextType();
      if (contextType != BsonContextType.DOCUMENT && contextType != BsonContextType.SCOPE_DOCUMENT) {
         this.throwInvalidContextType("WriteEndDocument", contextType, BsonContextType.DOCUMENT, BsonContextType.SCOPE_DOCUMENT);
      }

      if (this.context.getParentContext() != null && this.context.getParentContext().name != null) {
         ((FieldNameValidator)this.fieldNameValidatorStack.pop()).end();
      }

      --this.serializationDepth;
      this.doWriteEndDocument();
      if (this.getContext() != null && this.getContext().getContextType() != BsonContextType.TOP_LEVEL) {
         this.setState(this.getNextState());
      } else {
         this.setState(AbstractBsonWriter.State.DONE);
      }

   }

   public void writeStartArray(String name) {
      this.writeName(name);
      this.writeStartArray();
   }

   public void writeStartArray() {
      this.checkPreconditions("writeStartArray", AbstractBsonWriter.State.VALUE);
      if (this.context != null && this.context.name != null) {
         this.fieldNameValidatorStack.push(((FieldNameValidator)this.fieldNameValidatorStack.peek()).getValidatorForField(this.getName()));
      }

      ++this.serializationDepth;
      if (this.serializationDepth > this.settings.getMaxSerializationDepth()) {
         throw new BsonSerializationException("Maximum serialization depth exceeded (does the object being serialized have a circular reference?).");
      } else {
         this.doWriteStartArray();
         this.setState(AbstractBsonWriter.State.VALUE);
      }
   }

   public void writeEndArray() {
      this.checkPreconditions("writeEndArray", AbstractBsonWriter.State.VALUE);
      if (this.getContext().getContextType() != BsonContextType.ARRAY) {
         this.throwInvalidContextType("WriteEndArray", this.getContext().getContextType(), BsonContextType.ARRAY);
      }

      if (this.context.getParentContext() != null && this.context.getParentContext().name != null) {
         this.fieldNameValidatorStack.pop();
      }

      --this.serializationDepth;
      this.doWriteEndArray();
      this.setState(this.getNextState());
   }

   public void writeBinaryData(String name, BsonBinary binary) {
      Assertions.notNull("name", name);
      Assertions.notNull("value", binary);
      this.writeName(name);
      this.writeBinaryData(binary);
   }

   public void writeBinaryData(BsonBinary binary) {
      Assertions.notNull("value", binary);
      this.checkPreconditions("writeBinaryData", AbstractBsonWriter.State.VALUE, AbstractBsonWriter.State.INITIAL);
      this.doWriteBinaryData(binary);
      this.setState(this.getNextState());
   }

   public void writeBoolean(String name, boolean value) {
      this.writeName(name);
      this.writeBoolean(value);
   }

   public void writeBoolean(boolean value) {
      this.checkPreconditions("writeBoolean", AbstractBsonWriter.State.VALUE, AbstractBsonWriter.State.INITIAL);
      this.doWriteBoolean(value);
      this.setState(this.getNextState());
   }

   public void writeDateTime(String name, long value) {
      this.writeName(name);
      this.writeDateTime(value);
   }

   public void writeDateTime(long value) {
      this.checkPreconditions("writeDateTime", AbstractBsonWriter.State.VALUE, AbstractBsonWriter.State.INITIAL);
      this.doWriteDateTime(value);
      this.setState(this.getNextState());
   }

   public void writeDBPointer(String name, BsonDbPointer value) {
      Assertions.notNull("name", name);
      Assertions.notNull("value", value);
      this.writeName(name);
      this.writeDBPointer(value);
   }

   public void writeDBPointer(BsonDbPointer value) {
      Assertions.notNull("value", value);
      this.checkPreconditions("writeDBPointer", AbstractBsonWriter.State.VALUE, AbstractBsonWriter.State.INITIAL);
      this.doWriteDBPointer(value);
      this.setState(this.getNextState());
   }

   public void writeDouble(String name, double value) {
      this.writeName(name);
      this.writeDouble(value);
   }

   public void writeDouble(double value) {
      this.checkPreconditions("writeDBPointer", AbstractBsonWriter.State.VALUE, AbstractBsonWriter.State.INITIAL);
      this.doWriteDouble(value);
      this.setState(this.getNextState());
   }

   public void writeInt32(String name, int value) {
      this.writeName(name);
      this.writeInt32(value);
   }

   public void writeInt32(int value) {
      this.checkPreconditions("writeInt32", AbstractBsonWriter.State.VALUE);
      this.doWriteInt32(value);
      this.setState(this.getNextState());
   }

   public void writeInt64(String name, long value) {
      this.writeName(name);
      this.writeInt64(value);
   }

   public void writeInt64(long value) {
      this.checkPreconditions("writeInt64", AbstractBsonWriter.State.VALUE);
      this.doWriteInt64(value);
      this.setState(this.getNextState());
   }

   public void writeDecimal128(Decimal128 value) {
      Assertions.notNull("value", value);
      this.checkPreconditions("writeInt64", AbstractBsonWriter.State.VALUE);
      this.doWriteDecimal128(value);
      this.setState(this.getNextState());
   }

   public void writeDecimal128(String name, Decimal128 value) {
      Assertions.notNull("name", name);
      Assertions.notNull("value", value);
      this.writeName(name);
      this.writeDecimal128(value);
   }

   public void writeJavaScript(String name, String code) {
      Assertions.notNull("name", name);
      Assertions.notNull("value", code);
      this.writeName(name);
      this.writeJavaScript(code);
   }

   public void writeJavaScript(String code) {
      Assertions.notNull("value", code);
      this.checkPreconditions("writeJavaScript", AbstractBsonWriter.State.VALUE);
      this.doWriteJavaScript(code);
      this.setState(this.getNextState());
   }

   public void writeJavaScriptWithScope(String name, String code) {
      Assertions.notNull("name", name);
      Assertions.notNull("value", code);
      this.writeName(name);
      this.writeJavaScriptWithScope(code);
   }

   public void writeJavaScriptWithScope(String code) {
      Assertions.notNull("value", code);
      this.checkPreconditions("writeJavaScriptWithScope", AbstractBsonWriter.State.VALUE);
      this.doWriteJavaScriptWithScope(code);
      this.setState(AbstractBsonWriter.State.SCOPE_DOCUMENT);
   }

   public void writeMaxKey(String name) {
      this.writeName(name);
      this.writeMaxKey();
   }

   public void writeMaxKey() {
      this.checkPreconditions("writeMaxKey", AbstractBsonWriter.State.VALUE);
      this.doWriteMaxKey();
      this.setState(this.getNextState());
   }

   public void writeMinKey(String name) {
      this.writeName(name);
      this.writeMinKey();
   }

   public void writeMinKey() {
      this.checkPreconditions("writeMinKey", AbstractBsonWriter.State.VALUE);
      this.doWriteMinKey();
      this.setState(this.getNextState());
   }

   public void writeName(String name) {
      Assertions.notNull("name", name);
      if (this.state != AbstractBsonWriter.State.NAME) {
         this.throwInvalidState("WriteName", AbstractBsonWriter.State.NAME);
      }

      FieldNameValidator fieldNameValidator = (FieldNameValidator)this.fieldNameValidatorStack.peek();
      if (!fieldNameValidator.validate(name)) {
         throw new IllegalArgumentException(fieldNameValidator.getValidationErrorMessage(name));
      } else {
         this.doWriteName(name);
         this.context.name = name;
         this.state = AbstractBsonWriter.State.VALUE;
      }
   }

   protected void doWriteName(String name) {
   }

   public void writeNull(String name) {
      this.writeName(name);
      this.writeNull();
   }

   public void writeNull() {
      this.checkPreconditions("writeNull", AbstractBsonWriter.State.VALUE);
      this.doWriteNull();
      this.setState(this.getNextState());
   }

   public void writeObjectId(String name, ObjectId objectId) {
      Assertions.notNull("name", name);
      Assertions.notNull("value", objectId);
      this.writeName(name);
      this.writeObjectId(objectId);
   }

   public void writeObjectId(ObjectId objectId) {
      Assertions.notNull("value", objectId);
      this.checkPreconditions("writeObjectId", AbstractBsonWriter.State.VALUE);
      this.doWriteObjectId(objectId);
      this.setState(this.getNextState());
   }

   public void writeRegularExpression(String name, BsonRegularExpression regularExpression) {
      Assertions.notNull("name", name);
      Assertions.notNull("value", regularExpression);
      this.writeName(name);
      this.writeRegularExpression(regularExpression);
   }

   public void writeRegularExpression(BsonRegularExpression regularExpression) {
      Assertions.notNull("value", regularExpression);
      this.checkPreconditions("writeRegularExpression", AbstractBsonWriter.State.VALUE);
      this.doWriteRegularExpression(regularExpression);
      this.setState(this.getNextState());
   }

   public void writeString(String name, String value) {
      Assertions.notNull("name", name);
      Assertions.notNull("value", value);
      this.writeName(name);
      this.writeString(value);
   }

   public void writeString(String value) {
      Assertions.notNull("value", value);
      this.checkPreconditions("writeString", AbstractBsonWriter.State.VALUE);
      this.doWriteString(value);
      this.setState(this.getNextState());
   }

   public void writeSymbol(String name, String value) {
      Assertions.notNull("name", name);
      Assertions.notNull("value", value);
      this.writeName(name);
      this.writeSymbol(value);
   }

   public void writeSymbol(String value) {
      Assertions.notNull("value", value);
      this.checkPreconditions("writeSymbol", AbstractBsonWriter.State.VALUE);
      this.doWriteSymbol(value);
      this.setState(this.getNextState());
   }

   public void writeTimestamp(String name, BsonTimestamp value) {
      Assertions.notNull("name", name);
      Assertions.notNull("value", value);
      this.writeName(name);
      this.writeTimestamp(value);
   }

   public void writeTimestamp(BsonTimestamp value) {
      Assertions.notNull("value", value);
      this.checkPreconditions("writeTimestamp", AbstractBsonWriter.State.VALUE);
      this.doWriteTimestamp(value);
      this.setState(this.getNextState());
   }

   public void writeUndefined(String name) {
      this.writeName(name);
      this.writeUndefined();
   }

   public void writeUndefined() {
      this.checkPreconditions("writeUndefined", AbstractBsonWriter.State.VALUE);
      this.doWriteUndefined();
      this.setState(this.getNextState());
   }

   protected AbstractBsonWriter.State getNextState() {
      return this.getContext().getContextType() == BsonContextType.ARRAY ? AbstractBsonWriter.State.VALUE : AbstractBsonWriter.State.NAME;
   }

   protected boolean checkState(AbstractBsonWriter.State[] validStates) {
      for (AbstractBsonWriter.State cur : validStates) {
         if (cur == this.getState()) {
            return true;
         }
      }

      return false;
   }

   protected void checkPreconditions(String methodName, AbstractBsonWriter.State... validStates) {
      if (this.isClosed()) {
         throw new IllegalStateException("BsonWriter is closed");
      } else {
         if (!this.checkState(validStates)) {
            this.throwInvalidState(methodName, validStates);
         }

      }
   }

   protected void throwInvalidContextType(String methodName, BsonContextType actualContextType, BsonContextType... validContextTypes) {
      String validContextTypesString = StringUtils.join(" or ", validContextTypes);
      throw new BsonInvalidOperationException(String.format("%s can only be called when ContextType is %s, not when ContextType is %s.", methodName, validContextTypesString, actualContextType));
   }

   protected void throwInvalidState(String methodName, AbstractBsonWriter.State... validStates) {
      String typeName;
      if ((this.state == AbstractBsonWriter.State.INITIAL || this.state == AbstractBsonWriter.State.SCOPE_DOCUMENT || this.state == AbstractBsonWriter.State.DONE) && !methodName.startsWith("end") && !methodName.equals("writeName")) {
         typeName = methodName.substring(5);
         if (typeName.startsWith("start")) {
            typeName = typeName.substring(5);
         }

         String article = "A";
         if (Arrays.asList('A', 'E', 'I', 'O', 'U').contains(typeName.charAt(0))) {
            article = "An";
         }

         throw new BsonInvalidOperationException(String.format("%s %s value cannot be written to the root level of a BSON document.", article, typeName));
      } else {
         typeName = StringUtils.join(" or ", validStates);
         throw new BsonInvalidOperationException(String.format("%s can only be called when State is %s, not when State is %s", methodName, typeName, this.state));
      }
   }

   public void close() {
      this.closed = true;
   }

   public void pipe(BsonReader reader) {
      Assertions.notNull("reader", reader);
      this.pipeDocument(reader, (List)null);
   }

   public void pipe(BsonReader reader, List<BsonElement> extraElements) {
      Assertions.notNull("reader", reader);
      Assertions.notNull("extraElements", extraElements);
      this.pipeDocument(reader, extraElements);
   }

   protected void pipeExtraElements(List<BsonElement> extraElements) {
      Assertions.notNull("extraElements", extraElements);
      Iterator<BsonElement> iterator = extraElements.iterator();

      while(iterator.hasNext()) {
         BsonElement cur = iterator.next();
         this.writeName(cur.getName());
         this.pipeValue(cur.getValue());
      }

   }

   protected boolean abortPipe() {
      return false;
   }

   private void pipeDocument(BsonReader reader, List<BsonElement> extraElements) {
      reader.readStartDocument();
      this.writeStartDocument();

      do {
         if (reader.readBsonType() == BsonType.END_OF_DOCUMENT) {
            reader.readEndDocument();
            if (extraElements != null) {
               this.pipeExtraElements(extraElements);
            }

            this.writeEndDocument();
            return;
         }

         this.writeName(reader.readName());
         this.pipeValue(reader);
      } while(!this.abortPipe());

   }

   private void pipeJavascriptWithScope(BsonReader reader) {
      this.writeJavaScriptWithScope(reader.readJavaScriptWithScope());
      this.pipeDocument(reader, (List)null);
   }

   private void pipeValue(BsonReader reader) {
      switch(reader.getCurrentBsonType()) {
      case DOCUMENT:
         this.pipeDocument(reader, (List)null);
         break;
      case ARRAY:
         this.pipeArray(reader);
         break;
      case DOUBLE:
         this.writeDouble(reader.readDouble());
         break;
      case STRING:
         this.writeString(reader.readString());
         break;
      case BINARY:
         this.writeBinaryData(reader.readBinaryData());
         break;
      case UNDEFINED:
         reader.readUndefined();
         this.writeUndefined();
         break;
      case OBJECT_ID:
         this.writeObjectId(reader.readObjectId());
         break;
      case BOOLEAN:
         this.writeBoolean(reader.readBoolean());
         break;
      case DATE_TIME:
         this.writeDateTime(reader.readDateTime());
         break;
      case NULL:
         reader.readNull();
         this.writeNull();
         break;
      case REGULAR_EXPRESSION:
         this.writeRegularExpression(reader.readRegularExpression());
         break;
      case JAVASCRIPT:
         this.writeJavaScript(reader.readJavaScript());
         break;
      case SYMBOL:
         this.writeSymbol(reader.readSymbol());
         break;
      case JAVASCRIPT_WITH_SCOPE:
         this.pipeJavascriptWithScope(reader);
         break;
      case INT32:
         this.writeInt32(reader.readInt32());
         break;
      case TIMESTAMP:
         this.writeTimestamp(reader.readTimestamp());
         break;
      case INT64:
         this.writeInt64(reader.readInt64());
         break;
      case DECIMAL128:
         this.writeDecimal128(reader.readDecimal128());
         break;
      case MIN_KEY:
         reader.readMinKey();
         this.writeMinKey();
         break;
      case DB_POINTER:
         this.writeDBPointer(reader.readDBPointer());
         break;
      case MAX_KEY:
         reader.readMaxKey();
         this.writeMaxKey();
         break;
      default:
         throw new IllegalArgumentException("unhandled BSON type: " + reader.getCurrentBsonType());
      }

   }

   private void pipeDocument(BsonDocument value) {
      this.writeStartDocument();
      Iterator<Entry<String, BsonValue>> iterator = value.entrySet().iterator();

      while(iterator.hasNext()) {
         Entry<String, BsonValue> cur = iterator.next();
         this.writeName((String)cur.getKey());
         this.pipeValue((BsonValue)cur.getValue());
      }

      this.writeEndDocument();
   }

   private void pipeArray(BsonReader reader) {
      reader.readStartArray();
      this.writeStartArray();

      do {
         if (reader.readBsonType() == BsonType.END_OF_DOCUMENT) {
            reader.readEndArray();
            this.writeEndArray();
            return;
         }

         this.pipeValue(reader);
      } while(!this.abortPipe());

   }

   private void pipeArray(BsonArray array) {
      this.writeStartArray();
      Iterator<BsonValue> iterator = array.iterator();

      while(iterator.hasNext()) {
         BsonValue cur = iterator.next();
         this.pipeValue(cur);
      }

      this.writeEndArray();
   }

   private void pipeJavascriptWithScope(BsonJavaScriptWithScope javaScriptWithScope) {
      this.writeJavaScriptWithScope(javaScriptWithScope.getCode());
      this.pipeDocument(javaScriptWithScope.getScope());
   }

   private void pipeValue(BsonValue value) {
      switch(value.getBsonType()) {
      case DOCUMENT:
         this.pipeDocument(value.asDocument());
         break;
      case ARRAY:
         this.pipeArray(value.asArray());
         break;
      case DOUBLE:
         this.writeDouble(value.asDouble().getValue());
         break;
      case STRING:
         this.writeString(value.asString().getValue());
         break;
      case BINARY:
         this.writeBinaryData(value.asBinary());
         break;
      case UNDEFINED:
         this.writeUndefined();
         break;
      case OBJECT_ID:
         this.writeObjectId(value.asObjectId().getValue());
         break;
      case BOOLEAN:
         this.writeBoolean(value.asBoolean().getValue());
         break;
      case DATE_TIME:
         this.writeDateTime(value.asDateTime().getValue());
         break;
      case NULL:
         this.writeNull();
         break;
      case REGULAR_EXPRESSION:
         this.writeRegularExpression(value.asRegularExpression());
         break;
      case JAVASCRIPT:
         this.writeJavaScript(value.asJavaScript().getCode());
         break;
      case SYMBOL:
         this.writeSymbol(value.asSymbol().getSymbol());
         break;
      case JAVASCRIPT_WITH_SCOPE:
         this.pipeJavascriptWithScope(value.asJavaScriptWithScope());
         break;
      case INT32:
         this.writeInt32(value.asInt32().getValue());
         break;
      case TIMESTAMP:
         this.writeTimestamp(value.asTimestamp());
         break;
      case INT64:
         this.writeInt64(value.asInt64().getValue());
         break;
      case DECIMAL128:
         this.writeDecimal128(value.asDecimal128().getValue());
         break;
      case MIN_KEY:
         this.writeMinKey();
         break;
      case DB_POINTER:
         this.writeDBPointer(value.asDBPointer());
         break;
      case MAX_KEY:
         this.writeMaxKey();
         break;
      default:
         throw new IllegalArgumentException("unhandled BSON type: " + value.getBsonType());
      }

   }

   public static enum State {
      INITIAL,
      NAME,
      VALUE,
      SCOPE_DOCUMENT,
      DONE,
      CLOSED;

      // $FF: synthetic method
      private static AbstractBsonWriter.State[] $values() {
         return new AbstractBsonWriter.State[]{INITIAL, NAME, VALUE, SCOPE_DOCUMENT, DONE, CLOSED};
      }
   }

   public class Context {
      private final AbstractBsonWriter.Context parentContext;
      private final BsonContextType contextType;
      private String name;

      public Context(AbstractBsonWriter.Context from) {
         this.parentContext = from.parentContext;
         this.contextType = from.contextType;
      }

      public Context(AbstractBsonWriter.Context parentContext, BsonContextType contextType) {
         this.parentContext = parentContext;
         this.contextType = contextType;
      }

      public AbstractBsonWriter.Context getParentContext() {
         return this.parentContext;
      }

      public BsonContextType getContextType() {
         return this.contextType;
      }

      public AbstractBsonWriter.Context copy() {
         return AbstractBsonWriter.this.new Context(this);
      }
   }

   protected class Mark {
      private final AbstractBsonWriter.Context markedContext;
      private final AbstractBsonWriter.State markedState;
      private final String currentName;
      private final int serializationDepth;

      protected Mark() {
         this.markedContext = AbstractBsonWriter.this.context.copy();
         this.markedState = AbstractBsonWriter.this.state;
         this.currentName = AbstractBsonWriter.this.context.name;
         this.serializationDepth = AbstractBsonWriter.this.serializationDepth;
      }

      protected void reset() {
         AbstractBsonWriter.this.setContext(this.markedContext);
         AbstractBsonWriter.this.setState(this.markedState);
         AbstractBsonWriter.this.context.name = this.currentName;
         AbstractBsonWriter.this.serializationDepth = this.serializationDepth;
      }
   }
}
