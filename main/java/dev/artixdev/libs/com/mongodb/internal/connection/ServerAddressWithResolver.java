package dev.artixdev.libs.com.mongodb.internal.connection;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import dev.artixdev.libs.com.mongodb.MongoSocketException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.com.mongodb.spi.dns.InetAddressResolver;
import dev.artixdev.libs.com.mongodb.spi.dns.InetAddressResolverProvider;

final class ServerAddressWithResolver extends ServerAddress {
   private static final long serialVersionUID = 1L;
   @Nullable
   private static final InetAddressResolver DEFAULT_INET_ADDRESS_RESOLVER = (InetAddressResolver)StreamSupport.stream(ServiceLoader.load(InetAddressResolverProvider.class).spliterator(), false).findFirst().map(InetAddressResolverProvider::create).orElse(null);
   @Nullable
   private final transient InetAddressResolver resolver;

   ServerAddressWithResolver(ServerAddress serverAddress, @Nullable InetAddressResolver inetAddressResolver) {
      super(serverAddress.getHost(), serverAddress.getPort());
      this.resolver = inetAddressResolver == null ? DEFAULT_INET_ADDRESS_RESOLVER : inetAddressResolver;
   }

   public InetSocketAddress getSocketAddress() {
      return this.resolver == null ? super.getSocketAddress() : (InetSocketAddress)this.getSocketAddresses().get(0);
   }

   public List<InetSocketAddress> getSocketAddresses() {
      if (this.resolver == null) {
         return super.getSocketAddresses();
      } else {
         try {
            return (List)this.resolver.lookupByName(this.getHost()).stream().map((inetAddress) -> {
               return new InetSocketAddress(inetAddress, this.getPort());
            }).collect(Collectors.toList());
         } catch (UnknownHostException unknownHostException) {
            throw new MongoSocketException(unknownHostException.getMessage(), this, unknownHostException);
         }
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         if (!super.equals(o)) {
            return false;
         } else {
            ServerAddressWithResolver that = (ServerAddressWithResolver)o;
            return Objects.equals(this.resolver, that.resolver);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{super.hashCode(), this.resolver});
   }
}
