package dev.artixdev.libs.com.mongodb.internal.dns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;
import dev.artixdev.libs.com.mongodb.MongoConfigurationException;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.com.mongodb.spi.dns.DnsClient;
import dev.artixdev.libs.com.mongodb.spi.dns.DnsClientProvider;
import dev.artixdev.libs.com.mongodb.spi.dns.DnsWithResponseCodeException;

public final class DefaultDnsResolver implements DnsResolver {
   private static final DnsClient DEFAULT_DNS_CLIENT = (DnsClient)StreamSupport.stream(ServiceLoader.load(DnsClientProvider.class).spliterator(), false).findFirst().map(DnsClientProvider::create).orElse(new JndiDnsClient());
   private final DnsClient dnsClient;

   public DefaultDnsResolver() {
      this(DEFAULT_DNS_CLIENT);
   }

   public DefaultDnsResolver(@Nullable DnsClient dnsClient) {
      this.dnsClient = dnsClient == null ? DEFAULT_DNS_CLIENT : dnsClient;
   }

   public List<String> resolveHostFromSrvRecords(String srvHost, String srvServiceName) {
      String srvHostDomain = srvHost.substring(srvHost.indexOf(46) + 1);
      List<String> srvHostDomainParts = Arrays.asList(srvHostDomain.split("\\."));
      List<String> hosts = new ArrayList();
      String resourceName = "_" + srvServiceName + "._tcp." + srvHost;

      try {
         List<String> srvAttributeValues = this.dnsClient.getResourceRecordData(resourceName, "SRV");
         if (srvAttributeValues != null && !srvAttributeValues.isEmpty()) {
            Iterator var8 = srvAttributeValues.iterator();

            while(var8.hasNext()) {
               String srvRecord = (String)var8.next();
               String[] split = srvRecord.split(" ");
               String resolvedHost = split[3].endsWith(".") ? split[3].substring(0, split[3].length() - 1) : split[3];
               String resolvedHostDomain = resolvedHost.substring(resolvedHost.indexOf(46) + 1);
               if (!sameParentDomain(srvHostDomainParts, resolvedHostDomain)) {
                  throw new MongoConfigurationException(String.format("The SRV host name '%s' resolved to a host '%s 'that is not in a sub-domain of the SRV host.", srvHost, resolvedHost));
               }

               hosts.add(resolvedHost + ":" + split[2]);
            }

            return hosts;
         } else {
            throw new MongoConfigurationException(String.format("No SRV records available for '%s'.", resourceName));
         }
      } catch (Exception e) {
         throw new MongoConfigurationException(String.format("Failed looking up SRV record for '%s'.", resourceName), e);
      }
   }

   private static boolean sameParentDomain(List<String> srvHostDomainParts, String resolvedHostDomain) {
      List<String> resolvedHostDomainParts = Arrays.asList(resolvedHostDomain.split("\\."));
      return srvHostDomainParts.size() > resolvedHostDomainParts.size() ? false : resolvedHostDomainParts.subList(resolvedHostDomainParts.size() - srvHostDomainParts.size(), resolvedHostDomainParts.size()).equals(srvHostDomainParts);
   }

   public String resolveAdditionalQueryParametersFromTxtRecords(String host) {
      try {
         List<String> attributeValues = this.dnsClient.getResourceRecordData(host, "TXT");
         if (attributeValues != null && !attributeValues.isEmpty()) {
            if (attributeValues.size() > 1) {
               throw new MongoConfigurationException(String.format("Multiple TXT records found for host '%s'.  Only one is permitted", host));
            } else {
               return ((String)attributeValues.get(0)).replaceAll("\\s", "");
            }
         } else {
            return "";
         }
      } catch (DnsWithResponseCodeException e) {
         if (e.getResponseCode() != 3) {
            throw new MongoConfigurationException("Failed looking up TXT record for host " + host, e);
         } else {
            return "";
         }
      } catch (Exception e) {
         throw new MongoConfigurationException("Failed looking up TXT record for host " + host, e);
      }
   }
}
