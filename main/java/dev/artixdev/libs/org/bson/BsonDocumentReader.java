package dev.artixdev.libs.org.bson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import dev.artixdev.libs.org.bson.types.Decimal128;
import dev.artixdev.libs.org.bson.types.ObjectId;

public class BsonDocumentReader extends AbstractBsonReader {
   private BsonValue currentValue;

   public BsonDocumentReader(BsonDocument document) {
      this.setContext(new BsonDocumentReader.Context((BsonDocumentReader.Context)null, BsonContextType.TOP_LEVEL, document));
      this.currentValue = document;
   }

   protected BsonBinary doReadBinaryData() {
      return this.currentValue.asBinary();
   }

   protected byte doPeekBinarySubType() {
      return this.currentValue.asBinary().getType();
   }

   protected int doPeekBinarySize() {
      return this.currentValue.asBinary().getData().length;
   }

   protected boolean doReadBoolean() {
      return this.currentValue.asBoolean().getValue();
   }

   protected long doReadDateTime() {
      return this.currentValue.asDateTime().getValue();
   }

   protected double doReadDouble() {
      return this.currentValue.asDouble().getValue();
   }

   protected void doReadEndArray() {
      this.setContext(this.getContext().getParentContext());
   }

   protected void doReadEndDocument() {
      this.setContext(this.getContext().getParentContext());
      switch(this.getContext().getContextType()) {
      case ARRAY:
      case DOCUMENT:
         this.setState(AbstractBsonReader.State.TYPE);
         break;
      case TOP_LEVEL:
         this.setState(AbstractBsonReader.State.DONE);
         break;
      default:
         throw new BSONException("Unexpected ContextType.");
      }

   }

   protected int doReadInt32() {
      return this.currentValue.asInt32().getValue();
   }

   protected long doReadInt64() {
      return this.currentValue.asInt64().getValue();
   }

   public Decimal128 doReadDecimal128() {
      return this.currentValue.asDecimal128().getValue();
   }

   protected String doReadJavaScript() {
      return this.currentValue.asJavaScript().getCode();
   }

   protected String doReadJavaScriptWithScope() {
      return this.currentValue.asJavaScriptWithScope().getCode();
   }

   protected void doReadMaxKey() {
   }

   protected void doReadMinKey() {
   }

   protected void doReadNull() {
   }

   protected ObjectId doReadObjectId() {
      return this.currentValue.asObjectId().getValue();
   }

   protected BsonRegularExpression doReadRegularExpression() {
      return this.currentValue.asRegularExpression();
   }

   protected BsonDbPointer doReadDBPointer() {
      return this.currentValue.asDBPointer();
   }

   protected void doReadStartArray() {
      BsonArray array = this.currentValue.asArray();
      this.setContext(new BsonDocumentReader.Context(this.getContext(), BsonContextType.ARRAY, array));
   }

   protected void doReadStartDocument() {
      BsonDocument document;
      if (this.currentValue.getBsonType() == BsonType.JAVASCRIPT_WITH_SCOPE) {
         document = this.currentValue.asJavaScriptWithScope().getScope();
      } else {
         document = this.currentValue.asDocument();
      }

      this.setContext(new BsonDocumentReader.Context(this.getContext(), BsonContextType.DOCUMENT, document));
   }

   protected String doReadString() {
      return this.currentValue.asString().getValue();
   }

   protected String doReadSymbol() {
      return this.currentValue.asSymbol().getSymbol();
   }

   protected BsonTimestamp doReadTimestamp() {
      return this.currentValue.asTimestamp();
   }

   protected void doReadUndefined() {
   }

   protected void doSkipName() {
   }

   protected void doSkipValue() {
   }

   public BsonType readBsonType() {
      if (this.getState() != AbstractBsonReader.State.INITIAL && this.getState() != AbstractBsonReader.State.SCOPE_DOCUMENT) {
         if (this.getState() != AbstractBsonReader.State.TYPE) {
            this.throwInvalidState("ReadBSONType", new AbstractBsonReader.State[]{AbstractBsonReader.State.TYPE});
         }

         switch(this.getContext().getContextType()) {
         case ARRAY:
            this.currentValue = this.getContext().getNextValue();
            if (this.currentValue == null) {
               this.setState(AbstractBsonReader.State.END_OF_ARRAY);
               return BsonType.END_OF_DOCUMENT;
            }

            this.setState(AbstractBsonReader.State.VALUE);
            break;
         case DOCUMENT:
            Entry<String, BsonValue> currentElement = this.getContext().getNextElement();
            if (currentElement == null) {
               this.setState(AbstractBsonReader.State.END_OF_DOCUMENT);
               return BsonType.END_OF_DOCUMENT;
            }

            this.setCurrentName((String)currentElement.getKey());
            this.currentValue = (BsonValue)currentElement.getValue();
            this.setState(AbstractBsonReader.State.NAME);
            break;
         default:
            throw new BSONException("Invalid ContextType.");
         }

         this.setCurrentBsonType(this.currentValue.getBsonType());
         return this.getCurrentBsonType();
      } else {
         this.setCurrentBsonType(BsonType.DOCUMENT);
         this.setState(AbstractBsonReader.State.VALUE);
         return this.getCurrentBsonType();
      }
   }

   public BsonReaderMark getMark() {
      return new BsonDocumentReader.Mark();
   }

   protected BsonDocumentReader.Context getContext() {
      return (BsonDocumentReader.Context)super.getContext();
   }

   protected class Context extends AbstractBsonReader.Context {
      private BsonDocumentReader.BsonDocumentMarkableIterator<Entry<String, BsonValue>> documentIterator;
      private BsonDocumentReader.BsonDocumentMarkableIterator<BsonValue> arrayIterator;

      protected Context(BsonDocumentReader.Context parentContext, BsonContextType contextType, BsonArray array) {
         super(parentContext, contextType);
         this.arrayIterator = new BsonDocumentReader.BsonDocumentMarkableIterator(array.iterator());
      }

      protected Context(BsonDocumentReader.Context parentContext, BsonContextType contextType, BsonDocument document) {
         super(parentContext, contextType);
         this.documentIterator = new BsonDocumentReader.BsonDocumentMarkableIterator(document.entrySet().iterator());
      }

      public Entry<String, BsonValue> getNextElement() {
         return this.documentIterator.hasNext() ? (Entry)this.documentIterator.next() : null;
      }

      protected void mark() {
         if (this.documentIterator != null) {
            this.documentIterator.mark();
         } else {
            this.arrayIterator.mark();
         }

         if (this.getParentContext() != null) {
            ((BsonDocumentReader.Context)this.getParentContext()).mark();
         }

      }

      protected void reset() {
         if (this.documentIterator != null) {
            this.documentIterator.reset();
         } else {
            this.arrayIterator.reset();
         }

         if (this.getParentContext() != null) {
            ((BsonDocumentReader.Context)this.getParentContext()).reset();
         }

      }

      public BsonValue getNextValue() {
         return this.arrayIterator.hasNext() ? (BsonValue)this.arrayIterator.next() : null;
      }
   }

   protected class Mark extends AbstractBsonReader.Mark {
      private final BsonValue currentValue;
      private final BsonDocumentReader.Context context;

      protected Mark() {
         super();
         this.currentValue = BsonDocumentReader.this.currentValue;
         this.context = BsonDocumentReader.this.getContext();
         this.context.mark();
      }

      public void reset() {
         super.reset();
         BsonDocumentReader.this.currentValue = this.currentValue;
         BsonDocumentReader.this.setContext(this.context);
         this.context.reset();
      }
   }

   private static class BsonDocumentMarkableIterator<T> implements Iterator<T> {
      private final Iterator<T> baseIterator;
      private final List<T> markIterator = new ArrayList();
      private int curIndex;
      private boolean marking;

      protected BsonDocumentMarkableIterator(Iterator<T> baseIterator) {
         this.baseIterator = baseIterator;
         this.curIndex = 0;
         this.marking = false;
      }

      protected void mark() {
         this.marking = true;
      }

      protected void reset() {
         this.curIndex = 0;
         this.marking = false;
      }

      public boolean hasNext() {
         return this.baseIterator.hasNext() || this.curIndex < this.markIterator.size();
      }

      public T next() {
         T value;
         if (this.curIndex < this.markIterator.size()) {
            value = this.markIterator.get(this.curIndex);
            if (this.marking) {
               ++this.curIndex;
            } else {
               this.markIterator.remove(0);
            }
         } else {
            value = this.baseIterator.next();
            if (this.marking) {
               this.markIterator.add(value);
               ++this.curIndex;
            }
         }

         return value;
      }

      public void remove() {
      }
   }
}
