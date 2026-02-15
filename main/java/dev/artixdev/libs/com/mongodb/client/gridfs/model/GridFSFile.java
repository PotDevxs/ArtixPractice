package dev.artixdev.libs.com.mongodb.client.gridfs.model;

import java.util.Date;
import java.util.Objects;
import dev.artixdev.libs.com.mongodb.MongoGridFSException;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.libs.org.bson.types.ObjectId;

public final class GridFSFile {
   private final BsonValue id;
   private final String filename;
   private final long length;
   private final int chunkSize;
   private final Date uploadDate;
   private final Document metadata;

   public GridFSFile(BsonValue id, String filename, long length, int chunkSize, Date uploadDate, @Nullable Document metadata) {
      this.id = (BsonValue)Assertions.notNull("id", id);
      this.filename = (String)Assertions.notNull("filename", filename);
      this.length = (Long)Assertions.notNull("length", length);
      this.chunkSize = (Integer)Assertions.notNull("chunkSize", chunkSize);
      this.uploadDate = (Date)Assertions.notNull("uploadDate", uploadDate);
      this.metadata = metadata != null && metadata.isEmpty() ? null : metadata;
   }

   public ObjectId getObjectId() {
      if (!this.id.isObjectId()) {
         throw new MongoGridFSException("Custom id type used for this GridFS file");
      } else {
         return this.id.asObjectId().getValue();
      }
   }

   public BsonValue getId() {
      return this.id;
   }

   public String getFilename() {
      return this.filename;
   }

   public long getLength() {
      return this.length;
   }

   public int getChunkSize() {
      return this.chunkSize;
   }

   public Date getUploadDate() {
      return this.uploadDate;
   }

   @Nullable
   public Document getMetadata() {
      return this.metadata;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         GridFSFile that = (GridFSFile)o;
         if (!Objects.equals(this.id, that.id)) {
            return false;
         } else if (!this.filename.equals(that.filename)) {
            return false;
         } else if (this.length != that.length) {
            return false;
         } else if (this.chunkSize != that.chunkSize) {
            return false;
         } else if (!this.uploadDate.equals(that.uploadDate)) {
            return false;
         } else {
            return Objects.equals(this.metadata, that.metadata);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.id != null ? this.id.hashCode() : 0;
      result = 31 * result + this.filename.hashCode();
      result = 31 * result + (int)(this.length ^ this.length >>> 32);
      result = 31 * result + this.chunkSize;
      result = 31 * result + this.uploadDate.hashCode();
      result = 31 * result + (this.metadata != null ? this.metadata.hashCode() : 0);
      return result;
   }

   public String toString() {
      return "GridFSFile{id=" + this.id + ", filename='" + this.filename + '\'' + ", length=" + this.length + ", chunkSize=" + this.chunkSize + ", uploadDate=" + this.uploadDate + ", metadata=" + this.metadata + '}';
   }
}
