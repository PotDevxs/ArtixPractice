package dev.artixdev.practice.packets;

import com.comphenix.protocol.events.PacketContainer;
import io.netty.buffer.ByteBuf;
import org.bukkit.entity.Player;

/**
 * Abstract Packet
 * Abstract class for packet handling
 */
public abstract class AbstractPacket {
   
   private int packetId;
   
   /**
    * Get packet ID
    * @return packet ID
    */
   public int getPacketId() {
      return this.packetId;
   }
   
   /**
    * Set packet ID
    * @param packetId the packet ID
    */
   public void setPacketId(int packetId) {
      this.packetId = packetId;
   }
   
   /**
    * Send packet to player
    * @param player the player
    */
   public abstract void sendPacket(Player player);
   
   /**
    * Write packet to buffer
    * @param buffer the buffer
    */
   public abstract void writePacket(ByteBuf buffer);
   
   /**
    * Read packet from buffer
    * @param buffer the buffer
    */
   public abstract void readPacket(ByteBuf buffer);
   
   /**
    * Get packet container
    * @return packet container
    */
   public abstract PacketContainer getPacketContainer();
   
   /**
    * Check if packet is valid
    * @return true if valid
    */
   public boolean isValid() {
      return packetId >= 0;
   }
   
   /**
    * Get packet name
    * @return packet name
    */
   public String getPacketName() {
      return this.getClass().getSimpleName();
   }
   
   /**
    * Get packet info
    * @return packet info
    */
   public String getPacketInfo() {
      return String.format("Packet: %s, ID: %d, Valid: %s", 
         getPacketName(), getPacketId(), isValid());
   }
   
   /**
    * Clone packet
    * @return cloned packet
    */
   public abstract AbstractPacket clone();
   
   /**
    * Check if packet equals another
    * @param other the other packet
    * @return true if equal
    */
   public boolean equals(AbstractPacket other) {
      if (other == null) {
         return false;
      }
      
      return this.packetId == other.packetId && 
             this.getClass().equals(other.getClass());
   }
   
   /**
    * Get packet hash
    * @return packet hash
    */
   public int getPacketHash() {
      return java.util.Objects.hash(packetId, getClass());
   }
   
   /**
    * Get packet size
    * @return packet size in bytes
    */
   public int getPacketSize() {
      // This would be implemented based on your packet system
      return 0;
   }
   
   /**
    * Compress packet
    * @return compressed packet
    */
   public AbstractPacket compress() {
      // This would be implemented based on your packet system
      return this;
   }
   
   /**
    * Decompress packet
    * @return decompressed packet
    */
   public AbstractPacket decompress() {
      // This would be implemented based on your packet system
      return this;
   }
   
   /**
    * Check if packet is compressed
    * @return true if compressed
    */
   public boolean isCompressed() {
      // This would be implemented based on your packet system
      return false;
   }
   
   /**
    * Get packet priority
    * @return packet priority
    */
   public int getPriority() {
      // This would be implemented based on your packet system
      return 0;
   }
   
   /**
    * Set packet priority
    * @param priority the priority
    */
   public void setPriority(int priority) {
      // This would be implemented based on your packet system
   }
   
   /**
    * Check if packet is high priority
    * @return true if high priority
    */
   public boolean isHighPriority() {
      return getPriority() > 5;
   }
   
   /**
    * Check if packet is low priority
    * @return true if low priority
    */
   public boolean isLowPriority() {
      return getPriority() < 5;
   }
   
   /**
    * Get packet timestamp
    * @return packet timestamp
    */
   public long getTimestamp() {
      return System.currentTimeMillis();
   }
   
   /**
    * Check if packet is expired
    * @param timeout the timeout in milliseconds
    * @return true if expired
    */
   public boolean isExpired(long timeout) {
      return (System.currentTimeMillis() - getTimestamp()) > timeout;
   }
   
   /**
    * Get packet age
    * @return packet age in milliseconds
    */
   public long getAge() {
      return System.currentTimeMillis() - getTimestamp();
   }
   
   /**
    * Validate packet
    * @return true if valid
    */
   public boolean validate() {
      return isValid() && packetId >= 0;
   }
   
   /**
    * Get packet error
    * @return packet error or null
    */
   public String getError() {
      if (!isValid()) {
         return "Invalid packet ID";
      }
      
      if (packetId < 0) {
         return "Negative packet ID";
      }
      
      return null;
   }
   
   /**
    * Get packet status
    * @return packet status
    */
   public String getStatus() {
      if (isValid()) {
         return "Valid";
      } else {
         return "Invalid: " + getError();
      }
   }
   
   /**
    * Log packet info
    */
   public void logInfo() {
      System.out.println(getPacketInfo());
   }
   
   /**
    * Log packet error
    */
   public void logError() {
      if (!isValid()) {
         System.err.println("Packet Error: " + getError());
      }
   }
   
   @Override
   public String toString() {
      return getPacketInfo();
   }
   
   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null || getClass() != obj.getClass()) return false;
      
      AbstractPacket that = (AbstractPacket) obj;
      return equals(that);
   }
   
   @Override
   public int hashCode() {
      return getPacketHash();
   }
}
