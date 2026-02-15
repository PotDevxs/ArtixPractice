package dev.artixdev.libs.com.mongodb.internal.dns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.InitialDirContext;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.spi.dns.DnsClient;
import dev.artixdev.libs.com.mongodb.spi.dns.DnsException;
import dev.artixdev.libs.com.mongodb.spi.dns.DnsWithResponseCodeException;

final class JndiDnsClient implements DnsClient {
   public List<String> getResourceRecordData(String name, String type) throws DnsException {
      InitialDirContext dirContext = createDnsDirContext();

      List<String> result;
      try {
         Attribute attribute = dirContext.getAttributes(name, new String[]{type}).get(type);
         if (attribute != null) {
            List<String> attributeValues = new ArrayList();
            NamingEnumeration<?> namingEnumeration = attribute.getAll();

            while(namingEnumeration.hasMore()) {
               attributeValues.add((String)namingEnumeration.next());
            }

            return attributeValues;
         }

         result = Collections.emptyList();
      } catch (NameNotFoundException nameNotFoundException) {
         throw new DnsWithResponseCodeException(nameNotFoundException.getMessage(), 3, nameNotFoundException);
      } catch (NamingException namingException) {
         throw new DnsException(namingException.getMessage(), namingException);
      } finally {
         try {
            dirContext.close();
         } catch (NamingException ignored) {
         }

      }

      return result;
   }

   private static InitialDirContext createDnsDirContext() {
      Hashtable<String, String> envProps = new Hashtable();
      envProps.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");

      try {
         return new InitialDirContext(envProps);
      } catch (NamingException namingException) {
         envProps.put("java.naming.provider.url", "dns:");

         try {
            return new InitialDirContext(envProps);
         } catch (NamingException ignored) {
            throw new MongoClientException("Unable to support mongodb+srv// style connections as the 'com.sun.jndi.dns.DnsContextFactory' class is not available in this JRE. A JNDI context is required for resolving SRV records.", namingException);
         }
      }
   }
}
