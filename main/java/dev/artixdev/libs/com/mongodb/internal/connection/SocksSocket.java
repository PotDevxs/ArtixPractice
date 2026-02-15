package dev.artixdev.libs.com.mongodb.internal.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ProxySettings;
import dev.artixdev.libs.com.mongodb.internal.time.Timeout;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class SocksSocket extends Socket {
   private static final byte SOCKS_VERSION = 5;
   private static final byte RESERVED = 0;
   private static final byte PORT_LENGTH = 2;
   private static final byte AUTHENTICATION_SUCCEEDED_STATUS = 0;
   public static final String IP_PARSING_ERROR_SUFFIX = " is not an IP string literal";
   private static final byte USER_PASSWORD_SUB_NEGOTIATION_VERSION = 1;
   private InetSocketAddress remoteAddress;
   private final ProxySettings proxySettings;
   @Nullable
   private final Socket socket;

   public SocksSocket(ProxySettings proxySettings) {
      this((Socket)null, proxySettings);
   }

   public SocksSocket(@Nullable Socket socket, ProxySettings proxySettings) {
      Assertions.assertNotNull(proxySettings.getHost());
      if (socket != null) {
         Assertions.assertFalse(socket.isConnected());
      }

      this.socket = socket;
      this.proxySettings = proxySettings;
   }

   public void connect(SocketAddress endpoint, int timeoutMs) throws IOException {
      Assertions.isTrueArgument("timeoutMs", timeoutMs >= 0);

      try {
         Timeout timeout = toTimeout(timeoutMs);
         InetSocketAddress unresolvedAddress = (InetSocketAddress)endpoint;
         Assertions.assertTrue(unresolvedAddress.isUnresolved());
         this.remoteAddress = unresolvedAddress;
         InetSocketAddress proxyAddress = new InetSocketAddress((String)Assertions.assertNotNull(this.proxySettings.getHost()), this.proxySettings.getPort());
         if (this.socket != null) {
            this.socket.connect(proxyAddress, remainingMillis(timeout));
         } else {
            super.connect(proxyAddress, remainingMillis(timeout));
         }

         SocksSocket.SocksAuthenticationMethod authenticationMethod = this.performNegotiation(timeout);
         this.authenticate(authenticationMethod, timeout);
         this.sendConnect(timeout);
      } catch (SocketException e) {
         try {
            this.close();
         } catch (Exception suppressed) {
            e.addSuppressed(suppressed);
         }

         throw e;
      }
   }

   private void sendConnect(Timeout timeout) throws IOException {
      String host = this.remoteAddress.getHostName();
      int port = this.remoteAddress.getPort();
      byte[] bytesOfHost = host.getBytes(StandardCharsets.US_ASCII);
      int hostLength = bytesOfHost.length;
      byte[] ipAddress = null;
      SocksSocket.AddressType addressType;
      if (DomainNameUtils.isDomainName(host)) {
         addressType = SocksSocket.AddressType.DOMAIN_NAME;
      } else {
         ipAddress = createByteArrayFromIpAddress(host);
         addressType = this.determineAddressType(ipAddress);
      }

      byte[] bufferSent = createBuffer(addressType, hostLength);
      bufferSent[0] = 5;
      bufferSent[1] = SocksSocket.SocksCommand.CONNECT.getCommandNumber();
      bufferSent[2] = 0;
      switch(addressType) {
      case DOMAIN_NAME:
         bufferSent[3] = SocksSocket.AddressType.DOMAIN_NAME.getAddressTypeNumber();
         bufferSent[4] = (byte)hostLength;
         System.arraycopy(bytesOfHost, 0, bufferSent, 5, hostLength);
         addPort(bufferSent, 5 + hostLength, port);
         break;
      case IP_V4:
         bufferSent[3] = SocksSocket.AddressType.IP_V4.getAddressTypeNumber();
         System.arraycopy(ipAddress, 0, bufferSent, 4, ipAddress.length);
         addPort(bufferSent, 4 + ipAddress.length, port);
         break;
      case IP_V6:
         bufferSent[3] = SocksSocket.AddressType.DOMAIN_NAME.getAddressTypeNumber();
         System.arraycopy(ipAddress, 0, bufferSent, 4, ipAddress.length);
         addPort(bufferSent, 4 + ipAddress.length, port);
         break;
      default:
         Assertions.fail();
      }

      OutputStream outputStream = this.getOutputStream();
      outputStream.write(bufferSent);
      outputStream.flush();
      this.checkServerReply(timeout);
   }

   private static void addPort(byte[] bufferSent, int index, int port) {
      bufferSent[index] = (byte)(port >> 8);
      bufferSent[index + 1] = (byte)port;
   }

   private static byte[] createByteArrayFromIpAddress(String host) throws SocketException {
      byte[] bytes = InetAddressUtils.ipStringToBytes(host);
      if (bytes == null) {
         throw new SocketException(host + " is not an IP string literal");
      } else {
         return bytes;
      }
   }

   private SocksSocket.AddressType determineAddressType(byte[] ipAddress) {
      if (ipAddress.length == SocksSocket.AddressType.IP_V4.getLength()) {
         return SocksSocket.AddressType.IP_V4;
      } else if (ipAddress.length == SocksSocket.AddressType.IP_V6.getLength()) {
         return SocksSocket.AddressType.IP_V6;
      } else {
         throw Assertions.fail();
      }
   }

   private static byte[] createBuffer(SocksSocket.AddressType addressType, int hostLength) {
      switch(addressType) {
      case DOMAIN_NAME:
         return new byte[7 + hostLength];
      case IP_V4:
         return new byte[6 + SocksSocket.AddressType.IP_V4.getLength()];
      case IP_V6:
         return new byte[6 + SocksSocket.AddressType.IP_V6.getLength()];
      default:
         throw Assertions.fail();
      }
   }

   private void checkServerReply(Timeout timeout) throws IOException {
      byte[] data = this.readSocksReply(4, timeout);
      SocksSocket.ServerReply reply = SocksSocket.ServerReply.of(data[1]);
      if (reply == SocksSocket.ServerReply.REPLY_SUCCEEDED) {
         switch(SocksSocket.AddressType.of(data[3])) {
         case DOMAIN_NAME:
            byte hostNameLength = this.readSocksReply(1, timeout)[0];
            this.readSocksReply(hostNameLength + 2, timeout);
            break;
         case IP_V4:
            this.readSocksReply(SocksSocket.AddressType.IP_V4.getLength() + 2, timeout);
            break;
         case IP_V6:
            this.readSocksReply(SocksSocket.AddressType.IP_V6.getLength() + 2, timeout);
            break;
         default:
            throw Assertions.fail();
         }

      } else {
         throw new ConnectException(reply.getMessage());
      }
   }

   private void authenticate(SocksSocket.SocksAuthenticationMethod authenticationMethod, Timeout timeout) throws IOException {
      if (authenticationMethod == SocksSocket.SocksAuthenticationMethod.USERNAME_PASSWORD) {
         byte[] bytesOfUsername = ((String)Assertions.assertNotNull(this.proxySettings.getUsername())).getBytes(StandardCharsets.UTF_8);
         byte[] bytesOfPassword = ((String)Assertions.assertNotNull(this.proxySettings.getPassword())).getBytes(StandardCharsets.UTF_8);
         int usernameLength = bytesOfUsername.length;
         int passwordLength = bytesOfPassword.length;
         byte[] command = new byte[3 + usernameLength + passwordLength];
         command[0] = 1;
         command[1] = (byte)usernameLength;
         System.arraycopy(bytesOfUsername, 0, command, 2, usernameLength);
         command[2 + usernameLength] = (byte)passwordLength;
         System.arraycopy(bytesOfPassword, 0, command, 3 + usernameLength, passwordLength);
         OutputStream outputStream = this.getOutputStream();
         outputStream.write(command);
         outputStream.flush();
         byte[] authResult = this.readSocksReply(2, timeout);
         byte authStatus = authResult[1];
         if (authStatus != 0) {
            throw new ConnectException("Authentication failed. Proxy server returned status: " + authStatus);
         }
      }

   }

   private SocksSocket.SocksAuthenticationMethod performNegotiation(Timeout timeout) throws IOException {
      SocksSocket.SocksAuthenticationMethod[] authenticationMethods = this.getSocksAuthenticationMethods();
      int methodsCount = authenticationMethods.length;
      byte[] bufferSent = new byte[2 + methodsCount];
      bufferSent[0] = 5;
      bufferSent[1] = (byte)methodsCount;

      for(int i = 0; i < methodsCount; ++i) {
         bufferSent[2 + i] = authenticationMethods[i].getMethodNumber();
      }

      OutputStream outputStream = this.getOutputStream();
      outputStream.write(bufferSent);
      outputStream.flush();
      byte[] handshakeReply = this.readSocksReply(2, timeout);
      if (handshakeReply[0] != 5) {
         throw new ConnectException("Remote server doesn't support socks version 5 Received version: " + handshakeReply[0]);
      } else {
         byte authMethodNumber = handshakeReply[1];
         if (authMethodNumber == -1) {
            throw new ConnectException("None of the authentication methods listed are acceptable. Attempted methods: " + Arrays.toString(authenticationMethods));
         } else if (authMethodNumber == SocksSocket.SocksAuthenticationMethod.NO_AUTH.getMethodNumber()) {
            return SocksSocket.SocksAuthenticationMethod.NO_AUTH;
         } else if (authMethodNumber == SocksSocket.SocksAuthenticationMethod.USERNAME_PASSWORD.getMethodNumber()) {
            return SocksSocket.SocksAuthenticationMethod.USERNAME_PASSWORD;
         } else {
            throw new ConnectException("Proxy returned unsupported authentication method: " + authMethodNumber);
         }
      }
   }

   private SocksSocket.SocksAuthenticationMethod[] getSocksAuthenticationMethods() {
      SocksSocket.SocksAuthenticationMethod[] authMethods;
      if (this.proxySettings.getUsername() != null) {
         authMethods = new SocksSocket.SocksAuthenticationMethod[]{SocksSocket.SocksAuthenticationMethod.NO_AUTH, SocksSocket.SocksAuthenticationMethod.USERNAME_PASSWORD};
      } else {
         authMethods = new SocksSocket.SocksAuthenticationMethod[]{SocksSocket.SocksAuthenticationMethod.NO_AUTH};
      }

      return authMethods;
   }

   private static Timeout toTimeout(int timeoutMs) {
      return timeoutMs == 0 ? Timeout.infinite() : Timeout.startNow((long)timeoutMs, TimeUnit.MILLISECONDS);
   }

   private static int remainingMillis(Timeout timeout) throws IOException {
      if (timeout.isInfinite()) {
         return 0;
      } else {
         int remaining = Math.toIntExact(timeout.remaining(TimeUnit.MILLISECONDS));
         if (remaining > 0) {
            return remaining;
         } else {
            throw new SocketTimeoutException("Socket connection timed out");
         }
      }
   }

   private byte[] readSocksReply(int length, Timeout timeout) throws IOException {
      InputStream inputStream = this.getInputStream();
      byte[] data = new byte[length];
      int received = 0;
      int originalTimeout = this.getSoTimeout();

      try {
         while(received < length) {
            int remaining = remainingMillis(timeout);
            this.setSoTimeout(remaining);
            int count = inputStream.read(data, received, length - received);
            if (count < 0) {
               throw new ConnectException("Malformed reply from SOCKS proxy server");
            }

            received += count;
         }
      } finally {
         this.setSoTimeout(originalTimeout);
      }

      return data;
   }

   public void close() throws IOException {
      Socket autoClosed = this.socket;

      try {
         super.close();
      } catch (Throwable e) {
         if (autoClosed != null) {
            try {
               autoClosed.close();
            } catch (Throwable suppressed) {
               e.addSuppressed(suppressed);
            }
         }

         throw e;
      }

      if (autoClosed != null) {
         autoClosed.close();
      }

   }

   public void setSoTimeout(int timeout) throws SocketException {
      if (this.socket != null) {
         this.socket.setSoTimeout(timeout);
      } else {
         super.setSoTimeout(timeout);
      }

   }

   public int getSoTimeout() throws SocketException {
      return this.socket != null ? this.socket.getSoTimeout() : super.getSoTimeout();
   }

   public void bind(SocketAddress bindpoint) throws IOException {
      if (this.socket != null) {
         this.socket.bind(bindpoint);
      } else {
         super.bind(bindpoint);
      }

   }

   public InetAddress getInetAddress() {
      return this.socket != null ? this.socket.getInetAddress() : super.getInetAddress();
   }

   public InetAddress getLocalAddress() {
      return this.socket != null ? this.socket.getLocalAddress() : super.getLocalAddress();
   }

   public int getPort() {
      return this.socket != null ? this.socket.getPort() : super.getPort();
   }

   public int getLocalPort() {
      return this.socket != null ? this.socket.getLocalPort() : super.getLocalPort();
   }

   public SocketAddress getRemoteSocketAddress() {
      return this.socket != null ? this.socket.getRemoteSocketAddress() : super.getRemoteSocketAddress();
   }

   public SocketAddress getLocalSocketAddress() {
      return this.socket != null ? this.socket.getLocalSocketAddress() : super.getLocalSocketAddress();
   }

   public SocketChannel getChannel() {
      return this.socket != null ? this.socket.getChannel() : super.getChannel();
   }

   public void setTcpNoDelay(boolean on) throws SocketException {
      if (this.socket != null) {
         this.socket.setTcpNoDelay(on);
      } else {
         super.setTcpNoDelay(on);
      }

   }

   public boolean getTcpNoDelay() throws SocketException {
      return this.socket != null ? this.socket.getTcpNoDelay() : super.getTcpNoDelay();
   }

   public void setSoLinger(boolean on, int linger) throws SocketException {
      if (this.socket != null) {
         this.socket.setSoLinger(on, linger);
      } else {
         super.setSoLinger(on, linger);
      }

   }

   public int getSoLinger() throws SocketException {
      return this.socket != null ? this.socket.getSoLinger() : super.getSoLinger();
   }

   public void sendUrgentData(int data) throws IOException {
      if (this.socket != null) {
         this.socket.sendUrgentData(data);
      } else {
         super.sendUrgentData(data);
      }

   }

   public void setOOBInline(boolean on) throws SocketException {
      if (this.socket != null) {
         this.socket.setOOBInline(on);
      } else {
         super.setOOBInline(on);
      }

   }

   public boolean getOOBInline() throws SocketException {
      return this.socket != null ? this.socket.getOOBInline() : super.getOOBInline();
   }

   public void setSendBufferSize(int size) throws SocketException {
      if (this.socket != null) {
         this.socket.setSendBufferSize(size);
      } else {
         super.setSendBufferSize(size);
      }

   }

   public int getSendBufferSize() throws SocketException {
      return this.socket != null ? this.socket.getSendBufferSize() : super.getSendBufferSize();
   }

   public void setReceiveBufferSize(int size) throws SocketException {
      if (this.socket != null) {
         this.socket.setReceiveBufferSize(size);
      } else {
         super.setReceiveBufferSize(size);
      }

   }

   public int getReceiveBufferSize() throws SocketException {
      return this.socket != null ? this.socket.getReceiveBufferSize() : super.getReceiveBufferSize();
   }

   public void setKeepAlive(boolean on) throws SocketException {
      if (this.socket != null) {
         this.socket.setKeepAlive(on);
      } else {
         super.setKeepAlive(on);
      }

   }

   public boolean getKeepAlive() throws SocketException {
      return this.socket != null ? this.socket.getKeepAlive() : super.getKeepAlive();
   }

   public void setTrafficClass(int tc) throws SocketException {
      if (this.socket != null) {
         this.socket.setTrafficClass(tc);
      } else {
         super.setTrafficClass(tc);
      }

   }

   public int getTrafficClass() throws SocketException {
      return this.socket != null ? this.socket.getTrafficClass() : super.getTrafficClass();
   }

   public void setReuseAddress(boolean on) throws SocketException {
      if (this.socket != null) {
         this.socket.setReuseAddress(on);
      } else {
         super.setReuseAddress(on);
      }

   }

   public boolean getReuseAddress() throws SocketException {
      return this.socket != null ? this.socket.getReuseAddress() : super.getReuseAddress();
   }

   public void shutdownInput() throws IOException {
      if (this.socket != null) {
         this.socket.shutdownInput();
      } else {
         super.shutdownInput();
      }

   }

   public void shutdownOutput() throws IOException {
      if (this.socket != null) {
         this.socket.shutdownOutput();
      } else {
         super.shutdownOutput();
      }

   }

   public boolean isConnected() {
      return this.socket != null ? this.socket.isConnected() : super.isConnected();
   }

   public boolean isBound() {
      return this.socket != null ? this.socket.isBound() : super.isBound();
   }

   public boolean isClosed() {
      return this.socket != null ? this.socket.isClosed() : super.isClosed();
   }

   public boolean isInputShutdown() {
      return this.socket != null ? this.socket.isInputShutdown() : super.isInputShutdown();
   }

   public boolean isOutputShutdown() {
      return this.socket != null ? this.socket.isOutputShutdown() : super.isOutputShutdown();
   }

   public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
      if (this.socket != null) {
         this.socket.setPerformancePreferences(connectionTime, latency, bandwidth);
      } else {
         super.setPerformancePreferences(connectionTime, latency, bandwidth);
      }

   }

   public InputStream getInputStream() throws IOException {
      return this.socket != null ? this.socket.getInputStream() : super.getInputStream();
   }

   public OutputStream getOutputStream() throws IOException {
      return this.socket != null ? this.socket.getOutputStream() : super.getOutputStream();
   }

   private static enum SocksAuthenticationMethod {
      NO_AUTH(0),
      USERNAME_PASSWORD(2);

      private final byte methodNumber;

      private SocksAuthenticationMethod(int methodNumber) {
         this.methodNumber = (byte)methodNumber;
      }

      public byte getMethodNumber() {
         return this.methodNumber;
      }

      // $FF: synthetic method
      private static SocksSocket.SocksAuthenticationMethod[] $values() {
         return new SocksSocket.SocksAuthenticationMethod[]{NO_AUTH, USERNAME_PASSWORD};
      }
   }

   static enum AddressType {
      IP_V4(1, 4),
      IP_V6(4, 16),
      DOMAIN_NAME(3, -1) {
         public byte getLength() {
            throw Assertions.fail();
         }
      };

      private final byte length;
      private final byte addressTypeNumber;

      private AddressType(int addressTypeNumber, int length) {
         this.addressTypeNumber = (byte)addressTypeNumber;
         this.length = (byte)length;
      }

      static SocksSocket.AddressType of(byte signedAddressType) throws ConnectException {
         int addressTypeNumber = Byte.toUnsignedInt(signedAddressType);
         SocksSocket.AddressType[] var2 = values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            SocksSocket.AddressType addressType = var2[var4];
            if (addressTypeNumber == addressType.getAddressTypeNumber()) {
               return addressType;
            }
         }

         throw new ConnectException("Reply from SOCKS proxy server contains wrong address type Address type: " + addressTypeNumber);
      }

      byte getLength() {
         return this.length;
      }

      byte getAddressTypeNumber() {
         return this.addressTypeNumber;
      }

      // $FF: synthetic method
      private static SocksSocket.AddressType[] $values() {
         return new SocksSocket.AddressType[]{IP_V4, IP_V6, DOMAIN_NAME};
      }

      // $FF: synthetic method
      AddressType(int x2, int x3, Object x4) {
         this(x2, x3);
      }
   }

   static enum SocksCommand {
      CONNECT(1);

      private final byte value;

      private SocksCommand(int value) {
         this.value = (byte)value;
      }

      public byte getCommandNumber() {
         return this.value;
      }

      // $FF: synthetic method
      private static SocksSocket.SocksCommand[] $values() {
         return new SocksSocket.SocksCommand[]{CONNECT};
      }
   }

   static enum ServerReply {
      REPLY_SUCCEEDED(0, "Succeeded"),
      GENERAL_FAILURE(1, "General SOCKS5 server failure"),
      NOT_ALLOWED(2, "Connection is not allowed by ruleset"),
      NET_UNREACHABLE(3, "Network is unreachable"),
      HOST_UNREACHABLE(4, "Host is unreachable"),
      CONN_REFUSED(5, "Connection has been refused"),
      TTL_EXPIRED(6, "TTL is expired"),
      CMD_NOT_SUPPORTED(7, "Command is not supported"),
      ADDR_TYPE_NOT_SUP(8, "Address type is not supported");

      private final int replyNumber;
      private final String message;

      private ServerReply(int replyNumber, String message) {
         this.replyNumber = replyNumber;
         this.message = message;
      }

      static SocksSocket.ServerReply of(byte byteStatus) throws ConnectException {
         int status = Byte.toUnsignedInt(byteStatus);
         SocksSocket.ServerReply[] var2 = values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            SocksSocket.ServerReply serverReply = var2[var4];
            if (status == serverReply.replyNumber) {
               return serverReply;
            }
         }

         throw new ConnectException("Unknown reply field. Reply field: " + status);
      }

      public String getMessage() {
         return this.message;
      }

      // $FF: synthetic method
      private static SocksSocket.ServerReply[] $values() {
         return new SocksSocket.ServerReply[]{REPLY_SUCCEEDED, GENERAL_FAILURE, NOT_ALLOWED, NET_UNREACHABLE, HOST_UNREACHABLE, CONN_REFUSED, TTL_EXPIRED, CMD_NOT_SUPPORTED, ADDR_TYPE_NOT_SUP};
      }
   }
}
