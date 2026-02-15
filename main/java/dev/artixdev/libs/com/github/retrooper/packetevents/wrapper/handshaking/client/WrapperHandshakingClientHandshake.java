package dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.handshaking.client;

import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.exception.InvalidHandshakeException;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.ConnectionState;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperHandshakingClientHandshake extends PacketWrapper<WrapperHandshakingClientHandshake> {
   private int protocolVersion;
   private ClientVersion clientVersion;
   private String serverAddress;
   private int serverPort;
   private ConnectionState nextConnectionState;

   public WrapperHandshakingClientHandshake(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperHandshakingClientHandshake(int protocolVersion, String serverAddress, int serverPort, ConnectionState nextConnectionState) {
      super((PacketTypeCommon)PacketType.Handshaking.Client.HANDSHAKE);
      this.protocolVersion = protocolVersion;
      this.clientVersion = ClientVersion.getById(protocolVersion);
      this.serverAddress = serverAddress;
      this.serverPort = serverPort;
      this.nextConnectionState = nextConnectionState;
   }

   public void read() {
      try {
         this.protocolVersion = this.readVarInt();
         this.clientVersion = ClientVersion.getById(this.protocolVersion);
         this.serverAddress = this.readString(32767);
         this.serverPort = this.readUnsignedShort();
         int nextStateIndex = this.readVarInt();
         this.nextConnectionState = ConnectionState.getById(nextStateIndex);
      } catch (Exception e) {
         throw new InvalidHandshakeException();
      }
   }

   public void write() {
      this.writeVarInt(this.protocolVersion);
      this.writeString(this.serverAddress, 32767);
      this.writeShort(this.serverPort);
      this.writeVarInt(this.nextConnectionState.ordinal());
   }

   public void copy(WrapperHandshakingClientHandshake wrapper) {
      this.protocolVersion = wrapper.protocolVersion;
      this.clientVersion = wrapper.clientVersion;
      this.serverAddress = wrapper.serverAddress;
      this.serverPort = wrapper.serverPort;
      this.nextConnectionState = wrapper.nextConnectionState;
   }

   public int getProtocolVersion() {
      return this.protocolVersion;
   }

   public void setProtocolVersion(int protocolVersion) {
      this.protocolVersion = protocolVersion;
      this.clientVersion = ClientVersion.getById(protocolVersion);
   }

   public ClientVersion getClientVersion() {
      return this.clientVersion;
   }

   public void setClientVersion(ClientVersion clientVersion) {
      this.clientVersion = clientVersion;
      this.protocolVersion = clientVersion.getProtocolVersion();
   }

   public String getServerAddress() {
      return this.serverAddress;
   }

   public void setServerAddress(String serverAddress) {
      this.serverAddress = serverAddress;
   }

   public int getServerPort() {
      return this.serverPort;
   }

   public void setServerPort(int serverPort) {
      this.serverPort = serverPort;
   }

   public ConnectionState getNextConnectionState() {
      return this.nextConnectionState;
   }

   public void setNextConnectionState(ConnectionState nextConnectionState) {
      this.nextConnectionState = nextConnectionState;
   }
}
