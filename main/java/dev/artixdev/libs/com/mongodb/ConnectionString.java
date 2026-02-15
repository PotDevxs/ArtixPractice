package dev.artixdev.libs.com.mongodb;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.internal.dns.DefaultDnsResolver;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.com.mongodb.spi.dns.DnsClient;
import dev.artixdev.libs.org.bson.UuidRepresentation;

public class ConnectionString {
   private static final String MONGODB_PREFIX = "mongodb://";
   private static final String MONGODB_SRV_PREFIX = "mongodb+srv://";
   private static final Set<String> ALLOWED_OPTIONS_IN_TXT_RECORD = new HashSet(Arrays.asList("authsource", "replicaset", "loadbalanced"));
   private static final Logger LOGGER = Loggers.getLogger("uri");
   private final MongoCredential credential;
   private final boolean isSrvProtocol;
   private final List<String> hosts;
   private final String database;
   private final String collection;
   private final String connectionString;
   private Integer srvMaxHosts;
   private String srvServiceName;
   private Boolean directConnection;
   private Boolean loadBalanced;
   private ReadPreference readPreference;
   private WriteConcern writeConcern;
   private Boolean retryWrites;
   private Boolean retryReads;
   private ReadConcern readConcern;
   private Integer minConnectionPoolSize;
   private Integer maxConnectionPoolSize;
   private Integer maxWaitTime;
   private Integer maxConnectionIdleTime;
   private Integer maxConnectionLifeTime;
   private Integer maxConnecting;
   private Integer connectTimeout;
   private Integer socketTimeout;
   private Boolean sslEnabled;
   private Boolean sslInvalidHostnameAllowed;
   private String proxyHost;
   private Integer proxyPort;
   private String proxyUsername;
   private String proxyPassword;
   private String requiredReplicaSetName;
   private Integer serverSelectionTimeout;
   private Integer localThreshold;
   private Integer heartbeatFrequency;
   private String applicationName;
   private List<MongoCompressor> compressorList;
   private UuidRepresentation uuidRepresentation;
   private static final Set<String> GENERAL_OPTIONS_KEYS = new LinkedHashSet();
   private static final Set<String> AUTH_KEYS = new HashSet();
   private static final Set<String> READ_PREFERENCE_KEYS = new HashSet();
   private static final Set<String> WRITE_CONCERN_KEYS = new HashSet();
   private static final Set<String> COMPRESSOR_KEYS = new HashSet();
   private static final Set<String> ALL_KEYS = new HashSet();
   private static final Set<String> TRUE_VALUES;
   private static final Set<String> FALSE_VALUES;

   public ConnectionString(String connectionString) {
      this(connectionString, (DnsClient)null);
   }

   public ConnectionString(String connectionString, @Nullable DnsClient dnsClient) {
      this.connectionString = connectionString;
      boolean isMongoDBProtocol = connectionString.startsWith("mongodb://");
      this.isSrvProtocol = connectionString.startsWith("mongodb+srv://");
      if (!isMongoDBProtocol && !this.isSrvProtocol) {
         throw new IllegalArgumentException(String.format("The connection string is invalid. Connection strings must start with either '%s' or '%s", "mongodb://", "mongodb+srv://"));
      } else {
         String unprocessedConnectionString;
         if (isMongoDBProtocol) {
            unprocessedConnectionString = connectionString.substring("mongodb://".length());
         } else {
            unprocessedConnectionString = connectionString.substring("mongodb+srv://".length());
         }

         int idx = unprocessedConnectionString.indexOf("/");
         String userAndHostInformation;
         if (idx == -1) {
            if (unprocessedConnectionString.contains("?")) {
               throw new IllegalArgumentException("The connection string contains options without trailing slash");
            }

            userAndHostInformation = unprocessedConnectionString;
            unprocessedConnectionString = "";
         } else {
            userAndHostInformation = unprocessedConnectionString.substring(0, idx);
            unprocessedConnectionString = unprocessedConnectionString.substring(idx + 1);
         }

         String userName = null;
         char[] password = null;
         idx = userAndHostInformation.lastIndexOf("@");
         String hostIdentifier;
         if (idx > 0) {
            String userInfo = userAndHostInformation.substring(0, idx).replace("+", "%2B");
            hostIdentifier = userAndHostInformation.substring(idx + 1);
            int colonCount = this.countOccurrences(userInfo, ":");
            if (userInfo.contains("@") || colonCount > 1) {
               throw new IllegalArgumentException("The connection string contains invalid user information. If the username or password contains a colon (:) or an at-sign (@) then it must be urlencoded");
            }

            if (colonCount == 0) {
               userName = this.urldecode(userInfo);
            } else {
               idx = userInfo.indexOf(":");
               if (idx == 0) {
                  throw new IllegalArgumentException("No username is provided in the connection string");
               }

               userName = this.urldecode(userInfo.substring(0, idx));
               password = this.urldecode(userInfo.substring(idx + 1), true).toCharArray();
            }
         } else {
            if (idx == 0) {
               throw new IllegalArgumentException("The connection string contains an at-sign (@) without a user name");
            }

            hostIdentifier = userAndHostInformation;
         }

         List<String> unresolvedHosts = Collections.unmodifiableList(this.parseHosts(Arrays.asList(hostIdentifier.split(","))));
         if (this.isSrvProtocol) {
            if (unresolvedHosts.size() > 1) {
               throw new IllegalArgumentException("Only one host allowed when using mongodb+srv protocol");
            }

            if (((String)unresolvedHosts.get(0)).contains(":")) {
               throw new IllegalArgumentException("Host for when using mongodb+srv protocol can not contain a port");
            }
         }

         this.hosts = unresolvedHosts;
         idx = unprocessedConnectionString.indexOf("?");
         String nsPart;
         if (idx == -1) {
            nsPart = unprocessedConnectionString;
            unprocessedConnectionString = "";
         } else {
            nsPart = unprocessedConnectionString.substring(0, idx);
            unprocessedConnectionString = unprocessedConnectionString.substring(idx + 1);
         }

         if (nsPart.length() > 0) {
            nsPart = this.urldecode(nsPart);
            idx = nsPart.indexOf(".");
            if (idx < 0) {
               this.database = nsPart;
               this.collection = null;
            } else {
               this.database = nsPart.substring(0, idx);
               this.collection = nsPart.substring(idx + 1);
            }

            MongoNamespace.checkDatabaseNameValidity(this.database);
         } else {
            this.database = null;
            this.collection = null;
         }

         String txtRecordsQueryParameters = this.isSrvProtocol ? (new DefaultDnsResolver(dnsClient)).resolveAdditionalQueryParametersFromTxtRecords((String)unresolvedHosts.get(0)) : "";
         Map<String, List<String>> connectionStringOptionsMap = this.parseOptions(unprocessedConnectionString);
         Map<String, List<String>> txtRecordsOptionsMap = this.parseOptions(txtRecordsQueryParameters);
         if (!ALLOWED_OPTIONS_IN_TXT_RECORD.containsAll(txtRecordsOptionsMap.keySet())) {
            throw new MongoConfigurationException(String.format("A TXT record is only permitted to contain the keys %s, but the TXT record for '%s' contains the keys %s", ALLOWED_OPTIONS_IN_TXT_RECORD, unresolvedHosts.get(0), txtRecordsOptionsMap.keySet()));
         } else {
            Map<String, List<String>> combinedOptionsMaps = this.combineOptionsMaps(txtRecordsOptionsMap, connectionStringOptionsMap);
            if (this.isSrvProtocol && !combinedOptionsMaps.containsKey("tls") && !combinedOptionsMaps.containsKey("ssl")) {
               combinedOptionsMaps.put("tls", Collections.singletonList("true"));
            }

            this.translateOptions(combinedOptionsMaps);
            if (!this.isSrvProtocol && this.srvMaxHosts != null) {
               throw new IllegalArgumentException("srvMaxHosts can only be specified with mongodb+srv protocol");
            } else if (!this.isSrvProtocol && this.srvServiceName != null) {
               throw new IllegalArgumentException("srvServiceName can only be specified with mongodb+srv protocol");
            } else {
               if (this.directConnection != null && this.directConnection) {
                  if (this.isSrvProtocol) {
                     throw new IllegalArgumentException("Direct connections are not supported when using mongodb+srv protocol");
                  }

                  if (this.hosts.size() > 1) {
                     throw new IllegalArgumentException("Direct connections are not supported when using multiple hosts");
                  }
               }

               if (this.loadBalanced != null && this.loadBalanced) {
                  if (this.directConnection != null && this.directConnection) {
                     throw new IllegalArgumentException("directConnection=true can not be specified with loadBalanced=true");
                  }

                  if (this.requiredReplicaSetName != null) {
                     throw new IllegalArgumentException("replicaSet can not be specified with loadBalanced=true");
                  }

                  if (this.hosts.size() > 1) {
                     throw new IllegalArgumentException("Only one host can be specified with loadBalanced=true");
                  }

                  if (this.srvMaxHosts != null && this.srvMaxHosts > 0) {
                     throw new IllegalArgumentException("srvMaxHosts can not be specified with loadBalanced=true");
                  }
               }

               if (this.requiredReplicaSetName != null && this.srvMaxHosts != null && this.srvMaxHosts > 0) {
                  throw new IllegalArgumentException("srvMaxHosts can not be specified with replica set name");
               } else {
                  this.validateProxyParameters();
                  this.credential = this.createCredentials(combinedOptionsMaps, userName, password);
                  this.warnOnUnsupportedOptions(combinedOptionsMaps);
               }
            }
         }
      }
   }

   private Map<String, List<String>> combineOptionsMaps(Map<String, List<String>> txtRecordsOptionsMap, Map<String, List<String>> connectionStringOptionsMap) {
      Map<String, List<String>> combinedOptionsMaps = new HashMap(txtRecordsOptionsMap);
      combinedOptionsMaps.putAll(connectionStringOptionsMap);
      return combinedOptionsMaps;
   }

   private void warnOnUnsupportedOptions(Map<String, List<String>> optionsMap) {
      Iterator var2 = optionsMap.keySet().iterator();

      while(var2.hasNext()) {
         String key = (String)var2.next();
         if (!ALL_KEYS.contains(key) && LOGGER.isWarnEnabled()) {
            LOGGER.warn(String.format("Connection string contains unsupported option '%s'.", key));
         }
      }

   }

   private void translateOptions(Map<String, List<String>> optionsMap) {
      boolean tlsInsecureSet = false;
      boolean tlsAllowInvalidHostnamesSet = false;
      Iterator var4 = GENERAL_OPTIONS_KEYS.iterator();

      while(var4.hasNext()) {
         String key = (String)var4.next();
         String value = this.getLastValue(optionsMap, key);
         if (value != null) {
            byte var8 = -1;
            switch(key.hashCode()) {
            case -1901656698:
               if (key.equals("heartbeatfrequencyms")) {
                  var8 = 21;
               }
               break;
            case -1691284607:
               if (key.equals("maxpoolsize")) {
                  var8 = 0;
               }
               break;
            case -1120722955:
               if (key.equals("retryreads")) {
                  var8 = 24;
               }
               break;
            case -1004105558:
               if (key.equals("replicaset")) {
                  var8 = 17;
               }
               break;
            case -973631383:
               if (key.equals("srvservicename")) {
                  var8 = 29;
               }
               break;
            case -908890758:
               if (key.equals("readconcernlevel")) {
                  var8 = 18;
               }
               break;
            case -827816886:
               if (key.equals("sslinvalidhostnameallowed")) {
                  var8 = 13;
               }
               break;
            case -793183188:
               if (key.equals("appname")) {
                  var8 = 22;
               }
               break;
            case -705696786:
               if (key.equals("loadbalanced")) {
                  var8 = 27;
               }
               break;
            case -595131068:
               if (key.equals("proxyusername")) {
                  var8 = 10;
               }
               break;
            case -571921813:
               if (key.equals("maxidletimems")) {
                  var8 = 2;
               }
               break;
            case -475408106:
               if (key.equals("proxyhost")) {
                  var8 = 8;
               }
               break;
            case -475169809:
               if (key.equals("proxyport")) {
                  var8 = 9;
               }
               break;
            case -377025657:
               if (key.equals("directconnection")) {
                  var8 = 26;
               }
               break;
            case -227268324:
               if (key.equals("retrywrites")) {
                  var8 = 23;
               }
               break;
            case 114188:
               if (key.equals("ssl")) {
                  var8 = 15;
               }
               break;
            case 114939:
               if (key.equals("tls")) {
                  var8 = 16;
               }
               break;
            case 75372350:
               if (key.equals("serverselectiontimeoutms")) {
                  var8 = 19;
               }
               break;
            case 218461469:
               if (key.equals("connecttimeoutms")) {
                  var8 = 6;
               }
               break;
            case 353482524:
               if (key.equals("maxconnecting")) {
                  var8 = 4;
               }
               break;
            case 410888631:
               if (key.equals("tlsallowinvalidhostnames")) {
                  var8 = 12;
               }
               break;
            case 466333662:
               if (key.equals("srvmaxhosts")) {
                  var8 = 28;
               }
               break;
            case 718842184:
               if (key.equals("uuidrepresentation")) {
                  var8 = 25;
               }
               break;
            case 819239827:
               if (key.equals("maxlifetimems")) {
                  var8 = 3;
               }
               break;
            case 859445428:
               if (key.equals("sockettimeoutms")) {
                  var8 = 7;
               }
               break;
            case 887568137:
               if (key.equals("proxypassword")) {
                  var8 = 11;
               }
               break;
            case 1100926679:
               if (key.equals("tlsinsecure")) {
                  var8 = 14;
               }
               break;
            case 1241735942:
               if (key.equals("localthresholdms")) {
                  var8 = 20;
               }
               break;
            case 1524396427:
               if (key.equals("waitqueuetimeoutms")) {
                  var8 = 5;
               }
               break;
            case 1926250095:
               if (key.equals("minpoolsize")) {
                  var8 = 1;
               }
            }

            switch(var8) {
            case 0:
               this.maxConnectionPoolSize = this.parseInteger(value, "maxpoolsize");
               break;
            case 1:
               this.minConnectionPoolSize = this.parseInteger(value, "minpoolsize");
               break;
            case 2:
               this.maxConnectionIdleTime = this.parseInteger(value, "maxidletimems");
               break;
            case 3:
               this.maxConnectionLifeTime = this.parseInteger(value, "maxlifetimems");
               break;
            case 4:
               this.maxConnecting = this.parseInteger(value, "maxConnecting");
               break;
            case 5:
               this.maxWaitTime = this.parseInteger(value, "waitqueuetimeoutms");
               break;
            case 6:
               this.connectTimeout = this.parseInteger(value, "connecttimeoutms");
               break;
            case 7:
               this.socketTimeout = this.parseInteger(value, "sockettimeoutms");
               break;
            case 8:
               this.proxyHost = value;
               break;
            case 9:
               this.proxyPort = this.parseInteger(value, "proxyPort");
               break;
            case 10:
               this.proxyUsername = value;
               break;
            case 11:
               this.proxyPassword = value;
               break;
            case 12:
               this.sslInvalidHostnameAllowed = this.parseBoolean(value, "tlsAllowInvalidHostnames");
               tlsAllowInvalidHostnamesSet = true;
               break;
            case 13:
               this.sslInvalidHostnameAllowed = this.parseBoolean(value, "sslinvalidhostnameallowed");
               tlsAllowInvalidHostnamesSet = true;
               break;
            case 14:
               this.sslInvalidHostnameAllowed = this.parseBoolean(value, "tlsinsecure");
               tlsInsecureSet = true;
               break;
            case 15:
               this.initializeSslEnabled("ssl", value);
               break;
            case 16:
               this.initializeSslEnabled("tls", value);
               break;
            case 17:
               this.requiredReplicaSetName = value;
               break;
            case 18:
               this.readConcern = new ReadConcern(ReadConcernLevel.fromString(value));
               break;
            case 19:
               this.serverSelectionTimeout = this.parseInteger(value, "serverselectiontimeoutms");
               break;
            case 20:
               this.localThreshold = this.parseInteger(value, "localthresholdms");
               break;
            case 21:
               this.heartbeatFrequency = this.parseInteger(value, "heartbeatfrequencyms");
               break;
            case 22:
               this.applicationName = value;
               break;
            case 23:
               this.retryWrites = this.parseBoolean(value, "retrywrites");
               break;
            case 24:
               this.retryReads = this.parseBoolean(value, "retryreads");
               break;
            case 25:
               this.uuidRepresentation = this.createUuidRepresentation(value);
               break;
            case 26:
               this.directConnection = this.parseBoolean(value, "directconnection");
               break;
            case 27:
               this.loadBalanced = this.parseBoolean(value, "loadbalanced");
               break;
            case 28:
               this.srvMaxHosts = this.parseInteger(value, "srvmaxhosts");
               if (this.srvMaxHosts < 0) {
                  throw new IllegalArgumentException("srvMaxHosts must be >= 0");
               }
               break;
            case 29:
               this.srvServiceName = value;
            }
         }
      }

      if (tlsInsecureSet && tlsAllowInvalidHostnamesSet) {
         throw new IllegalArgumentException("tlsAllowInvalidHostnames or sslInvalidHostnameAllowed set along with tlsInsecure is not allowed");
      } else {
         this.writeConcern = this.createWriteConcern(optionsMap);
         this.readPreference = this.createReadPreference(optionsMap);
         this.compressorList = this.createCompressors(optionsMap);
      }
   }

   private void initializeSslEnabled(String key, String value) {
      Boolean booleanValue = this.parseBoolean(value, key);
      if (this.sslEnabled != null && !this.sslEnabled.equals(booleanValue)) {
         throw new IllegalArgumentException("Conflicting tls and ssl parameter values are not allowed");
      } else {
         this.sslEnabled = booleanValue;
      }
   }

   private List<MongoCompressor> createCompressors(Map<String, List<String>> optionsMap) {
      String compressors = "";
      Integer zlibCompressionLevel = null;
      Iterator var4 = COMPRESSOR_KEYS.iterator();

      while(var4.hasNext()) {
         String key = (String)var4.next();
         String value = this.getLastValue(optionsMap, key);
         if (value != null) {
            if (key.equals("compressors")) {
               compressors = value;
            } else if (key.equals("zlibcompressionlevel")) {
               zlibCompressionLevel = Integer.parseInt(value);
            }
         }
      }

      return this.buildCompressors(compressors, zlibCompressionLevel);
   }

   private List<MongoCompressor> buildCompressors(String compressors, @Nullable Integer zlibCompressionLevel) {
      List<MongoCompressor> compressorsList = new ArrayList();
      String[] var4 = compressors.split(",");
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String cur = var4[var6];
         if (cur.equals("zlib")) {
            MongoCompressor zlibCompressor = MongoCompressor.createZlibCompressor();
            if (zlibCompressionLevel != null) {
               zlibCompressor = zlibCompressor.withProperty("LEVEL", zlibCompressionLevel);
            }

            compressorsList.add(zlibCompressor);
         } else if (cur.equals("snappy")) {
            compressorsList.add(MongoCompressor.createSnappyCompressor());
         } else if (cur.equals("zstd")) {
            compressorsList.add(MongoCompressor.createZstdCompressor());
         } else if (!cur.isEmpty()) {
            throw new IllegalArgumentException("Unsupported compressor '" + cur + "'");
         }
      }

      return Collections.unmodifiableList(compressorsList);
   }

   @Nullable
   private WriteConcern createWriteConcern(Map<String, List<String>> optionsMap) {
      String w = null;
      Integer wTimeout = null;
      Boolean safe = null;
      Boolean journal = null;
      Iterator var6 = WRITE_CONCERN_KEYS.iterator();

      while(var6.hasNext()) {
         String key = (String)var6.next();
         String value = this.getLastValue(optionsMap, key);
         if (value != null) {
            byte var10 = -1;
            switch(key.hashCode()) {
            case -1858790352:
               if (key.equals("wtimeoutms")) {
                  var10 = 2;
               }
               break;
            case -1419464905:
               if (key.equals("journal")) {
                  var10 = 3;
               }
               break;
            case 119:
               if (key.equals("w")) {
                  var10 = 1;
               }
               break;
            case 3522445:
               if (key.equals("safe")) {
                  var10 = 0;
               }
            }

            switch(var10) {
            case 0:
               safe = this.parseBoolean(value, "safe");
               break;
            case 1:
               w = value;
               break;
            case 2:
               wTimeout = Integer.parseInt(value);
               break;
            case 3:
               journal = this.parseBoolean(value, "journal");
            }
         }
      }

      return this.buildWriteConcern(safe, w, wTimeout, journal);
   }

   @Nullable
   private ReadPreference createReadPreference(Map<String, List<String>> optionsMap) {
      String readPreferenceType = null;
      List<TagSet> tagSetList = new ArrayList();
      long maxStalenessSeconds = -1L;
      Iterator var6 = READ_PREFERENCE_KEYS.iterator();

      while(true) {
         while(true) {
            String key;
            String value;
            do {
               if (!var6.hasNext()) {
                  return this.buildReadPreference(readPreferenceType, tagSetList, maxStalenessSeconds);
               }

               key = (String)var6.next();
               value = this.getLastValue(optionsMap, key);
            } while(value == null);

            byte var10 = -1;
            switch(key.hashCode()) {
            case 511239818:
               if (key.equals("readpreferencetags")) {
                  var10 = 2;
               }
               break;
            case 995905651:
               if (key.equals("maxstalenessseconds")) {
                  var10 = 1;
               }
               break;
            case 1796707057:
               if (key.equals("readpreference")) {
                  var10 = 0;
               }
            }

            switch(var10) {
            case 0:
               readPreferenceType = value;
               break;
            case 1:
               maxStalenessSeconds = (long)this.parseInteger(value, "maxstalenessseconds");
               break;
            case 2:
               Iterator var11 = ((List)optionsMap.get(key)).iterator();

               while(var11.hasNext()) {
                  String cur = (String)var11.next();
                  TagSet tagSet = this.getTags(cur.trim());
                  tagSetList.add(tagSet);
               }
            }
         }
      }
   }

   private UuidRepresentation createUuidRepresentation(String value) {
      if (value.equalsIgnoreCase("unspecified")) {
         return UuidRepresentation.UNSPECIFIED;
      } else if (value.equalsIgnoreCase("javaLegacy")) {
         return UuidRepresentation.JAVA_LEGACY;
      } else if (value.equalsIgnoreCase("csharpLegacy")) {
         return UuidRepresentation.C_SHARP_LEGACY;
      } else if (value.equalsIgnoreCase("pythonLegacy")) {
         return UuidRepresentation.PYTHON_LEGACY;
      } else if (value.equalsIgnoreCase("standard")) {
         return UuidRepresentation.STANDARD;
      } else {
         throw new IllegalArgumentException("Unknown uuid representation: " + value);
      }
   }

   @Nullable
   private MongoCredential createCredentials(Map<String, List<String>> optionsMap, @Nullable String userName, @Nullable char[] password) {
      AuthenticationMechanism mechanism = null;
      String authSource = null;
      String gssapiServiceName = null;
      String authMechanismProperties = null;
      Iterator var8 = AUTH_KEYS.iterator();

      while(var8.hasNext()) {
         String key = (String)var8.next();
         String value = this.getLastValue(optionsMap, key);
         if (value != null) {
            byte var12 = -1;
            switch(key.hashCode()) {
            case -1388615741:
               if (key.equals("authsource")) {
                  var12 = 1;
               }
               break;
            case -909924051:
               if (key.equals("gssapiservicename")) {
                  var12 = 2;
               }
               break;
            case -497917263:
               if (key.equals("authmechanism")) {
                  var12 = 0;
               }
               break;
            case -349370716:
               if (key.equals("authmechanismproperties")) {
                  var12 = 3;
               }
            }

            switch(var12) {
            case 0:
               if (value.equals("MONGODB-CR")) {
                  if (userName == null) {
                     throw new IllegalArgumentException("username can not be null");
                  }

                  LOGGER.warn("Deprecated MONGDOB-CR authentication mechanism used in connection string");
               } else {
                  mechanism = AuthenticationMechanism.fromMechanismName(value);
               }
               break;
            case 1:
               if (value.equals("")) {
                  throw new IllegalArgumentException("authSource can not be an empty string");
               }

               authSource = value;
               break;
            case 2:
               gssapiServiceName = value;
               break;
            case 3:
               authMechanismProperties = value;
            }
         }
      }

      MongoCredential credential = null;
      if (mechanism != null) {
         credential = this.createMongoCredentialWithMechanism(mechanism, userName, password, authSource, gssapiServiceName);
      } else if (userName != null) {
         credential = MongoCredential.createCredential(userName, this.getAuthSourceOrDefault(authSource, this.database != null ? this.database : "admin"), password);
      }

      if (credential != null && authMechanismProperties != null) {
         String[] var17 = authMechanismProperties.split(",");
         int var18 = var17.length;

         for(int var11 = 0; var11 < var18; ++var11) {
            String part = var17[var11];
            String[] mechanismPropertyKeyValue = part.split(":");
            if (mechanismPropertyKeyValue.length != 2) {
               throw new IllegalArgumentException(String.format("The connection string contains invalid authentication properties. '%s' is not a key value pair", part));
            }

            String key = mechanismPropertyKeyValue[0].trim().toLowerCase();
            String value = mechanismPropertyKeyValue[1].trim();
            if (key.equals("canonicalize_host_name")) {
               credential = credential.withMechanismProperty(key, Boolean.valueOf(value));
            } else {
               credential = credential.withMechanismProperty(key, value);
            }
         }
      }

      return credential;
   }

   private MongoCredential createMongoCredentialWithMechanism(AuthenticationMechanism mechanism, String userName, @Nullable char[] password, @Nullable String authSource, @Nullable String gssapiServiceName) {
      String mechanismAuthSource;
      switch(mechanism) {
      case PLAIN:
         mechanismAuthSource = this.getAuthSourceOrDefault(authSource, this.database != null ? this.database : "$external");
         break;
      case GSSAPI:
      case MONGODB_X509:
         mechanismAuthSource = this.getAuthSourceOrDefault(authSource, "$external");
         if (!mechanismAuthSource.equals("$external")) {
            throw new IllegalArgumentException(String.format("Invalid authSource for %s, it must be '$external'", mechanism));
         }
         break;
      default:
         mechanismAuthSource = this.getAuthSourceOrDefault(authSource, this.database != null ? this.database : "admin");
      }

      MongoCredential credential;
      switch(mechanism) {
      case PLAIN:
         credential = MongoCredential.createPlainCredential(userName, mechanismAuthSource, password);
         break;
      case GSSAPI:
         credential = MongoCredential.createGSSAPICredential(userName);
         if (gssapiServiceName != null) {
            credential = credential.withMechanismProperty("SERVICE_NAME", gssapiServiceName);
         }

         if (password != null && LOGGER.isWarnEnabled()) {
            LOGGER.warn("Password in connection string not used with MONGODB_X509 authentication mechanism.");
         }
         break;
      case MONGODB_X509:
         if (password != null) {
            throw new IllegalArgumentException("Invalid mechanism, MONGODB_x509 does not support passwords");
         }

         credential = MongoCredential.createMongoX509Credential(userName);
         break;
      case SCRAM_SHA_1:
         credential = MongoCredential.createScramSha1Credential(userName, mechanismAuthSource, password);
         break;
      case SCRAM_SHA_256:
         credential = MongoCredential.createScramSha256Credential(userName, mechanismAuthSource, password);
         break;
      case MONGODB_AWS:
         credential = MongoCredential.createAwsCredential(userName, password);
         break;
      default:
         throw new UnsupportedOperationException(String.format("The connection string contains an invalid authentication mechanism'. '%s' is not a supported authentication mechanism", mechanism));
      }

      return credential;
   }

   private String getAuthSourceOrDefault(@Nullable String authSource, String defaultAuthSource) {
      return authSource != null ? authSource : defaultAuthSource;
   }

   @Nullable
   private String getLastValue(Map<String, List<String>> optionsMap, String key) {
      List<String> valueList = (List)optionsMap.get(key);
      return valueList == null ? null : (String)valueList.get(valueList.size() - 1);
   }

   private Map<String, List<String>> parseOptions(String optionsPart) {
      Map<String, List<String>> optionsMap = new HashMap();
      if (optionsPart.length() == 0) {
         return optionsMap;
      } else {
         String[] var3 = optionsPart.split("&|;");
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String part = var3[var5];
            if (part.length() != 0) {
               int idx = part.indexOf("=");
               if (idx < 0) {
                  throw new IllegalArgumentException(String.format("The connection string contains an invalid option '%s'. '%s' is missing the value delimiter eg '%s=value'", optionsPart, part, part));
               }

               String key = part.substring(0, idx).toLowerCase();
               String value = part.substring(idx + 1);
               List<String> valueList = (List)optionsMap.get(key);
               if (valueList == null) {
                  valueList = new ArrayList(1);
               }

               ((List)valueList).add(this.urldecode(value));
               optionsMap.put(key, valueList);
            }
         }

         if (optionsMap.containsKey("wtimeout") && !optionsMap.containsKey("wtimeoutms")) {
            optionsMap.put("wtimeoutms", (List)optionsMap.remove("wtimeout"));
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn("Uri option 'wtimeout' has been deprecated, use 'wtimeoutms' instead.");
            }
         }

         String legacySecondaryOkOption = "slaveok";
         String legacySecondaryOk = this.getLastValue(optionsMap, legacySecondaryOkOption);
         if (legacySecondaryOk != null && !optionsMap.containsKey("readpreference")) {
            String readPreference = Boolean.TRUE.equals(this.parseBoolean(legacySecondaryOk, legacySecondaryOkOption)) ? "secondaryPreferred" : "primary";
            optionsMap.put("readpreference", Collections.singletonList(readPreference));
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Uri option '%s' has been deprecated, use 'readpreference' instead.", legacySecondaryOkOption));
            }
         }

         if (optionsMap.containsKey("j") && !optionsMap.containsKey("journal")) {
            optionsMap.put("journal", (List)optionsMap.remove("j"));
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn("Uri option 'j' has been deprecated, use 'journal' instead.");
            }
         }

         return optionsMap;
      }
   }

   @Nullable
   private ReadPreference buildReadPreference(@Nullable String readPreferenceType, List<TagSet> tagSetList, long maxStalenessSeconds) {
      if (readPreferenceType != null) {
         if (tagSetList.isEmpty() && maxStalenessSeconds == -1L) {
            return ReadPreference.valueOf(readPreferenceType);
         } else {
            return maxStalenessSeconds == -1L ? ReadPreference.valueOf(readPreferenceType, tagSetList) : ReadPreference.valueOf(readPreferenceType, tagSetList, maxStalenessSeconds, TimeUnit.SECONDS);
         }
      } else if (tagSetList.isEmpty() && maxStalenessSeconds == -1L) {
         return null;
      } else {
         throw new IllegalArgumentException("Read preference mode must be specified if either read preference tags or max staleness is specified");
      }
   }

   @Nullable
   private WriteConcern buildWriteConcern(@Nullable Boolean safe, @Nullable String w, @Nullable Integer wTimeout, @Nullable Boolean journal) {
      WriteConcern retVal = null;
      if (w == null && wTimeout == null && journal == null) {
         if (safe != null) {
            if (safe) {
               retVal = WriteConcern.ACKNOWLEDGED;
            } else {
               retVal = WriteConcern.UNACKNOWLEDGED;
            }
         }

         return retVal;
      } else {
         if (w == null) {
            retVal = WriteConcern.ACKNOWLEDGED;
         } else {
            try {
               retVal = new WriteConcern(Integer.parseInt(w));
            } catch (NumberFormatException e) {
               retVal = new WriteConcern(w);
            }
         }

         if (wTimeout != null) {
            retVal = retVal.withWTimeout((long)wTimeout, TimeUnit.MILLISECONDS);
         }

         if (journal != null) {
            retVal = retVal.withJournal(journal);
         }

         return retVal;
      }
   }

   private TagSet getTags(String tagSetString) {
      List<Tag> tagList = new ArrayList();
      if (tagSetString.length() > 0) {
         String[] var3 = tagSetString.split(",");
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String tag = var3[var5];
            String[] tagKeyValuePair = tag.split(":");
            if (tagKeyValuePair.length != 2) {
               throw new IllegalArgumentException(String.format("The connection string contains an invalid read preference tag. '%s' is not a key value pair", tagSetString));
            }

            tagList.add(new Tag(tagKeyValuePair[0].trim(), tagKeyValuePair[1].trim()));
         }
      }

      return new TagSet(tagList);
   }

   @Nullable
   private Boolean parseBoolean(String input, String key) {
      String trimmedInput = input.trim().toLowerCase();
      if (TRUE_VALUES.contains(trimmedInput)) {
         if (!trimmedInput.equals("true")) {
            LOGGER.warn(String.format("Deprecated boolean value '%s' in the connection string for '%s'. Replace with 'true'", trimmedInput, key));
         }

         return true;
      } else if (FALSE_VALUES.contains(trimmedInput)) {
         if (!trimmedInput.equals("false")) {
            LOGGER.warn(String.format("Deprecated boolean value '%s' in the connection string for '%s'. Replace with'false'", trimmedInput, key));
         }

         return false;
      } else {
         LOGGER.warn(String.format("Ignoring unrecognized boolean value '%s' in the connection string for '%s'. Replace with either 'true' or 'false'", trimmedInput, key));
         return null;
      }
   }

   private int parseInteger(String input, String key) {
      try {
         return Integer.parseInt(input);
      } catch (NumberFormatException e) {
         throw new IllegalArgumentException(String.format("The connection string contains an invalid value for '%s'. '%s' is not a valid integer", key, input));
      }
   }

   private List<String> parseHosts(List<String> rawHosts) {
      if (rawHosts.size() == 0) {
         throw new IllegalArgumentException("The connection string must contain at least one host");
      } else {
         List<String> hosts = new ArrayList();

         String host;
         for (String hostItem : rawHosts) {
            host = hostItem;
            if (host.length() == 0) {
               throw new IllegalArgumentException(String.format("The connection string contains an empty host '%s'. ", rawHosts));
            }

            if (host.endsWith(".sock")) {
               host = this.urldecode(host);
            } else {
               int idx;
               if (host.startsWith("[")) {
                  if (!host.contains("]")) {
                     throw new IllegalArgumentException(String.format("The connection string contains an invalid host '%s'. IPv6 address literals must be enclosed in '[' and ']' according to RFC 2732", host));
                  }

                  idx = host.indexOf("]:");
                  if (idx != -1) {
                     this.validatePort(host, host.substring(idx + 2));
                  }
               } else {
                  idx = this.countOccurrences(host, ":");
                  if (idx > 1) {
                     throw new IllegalArgumentException(String.format("The connection string contains an invalid host '%s'. Reserved characters such as ':' must be escaped according RFC 2396. Any IPv6 address literal must be enclosed in '[' and ']' according to RFC 2732.", host));
                  }

                  if (idx == 1) {
                     this.validatePort(host, host.substring(host.indexOf(":") + 1));
                  }
               }
            }
            hosts.add(host);
         }

         Collections.sort(hosts);
         return hosts;
      }
   }

   private void validatePort(String host, String port) {
      boolean invalidPort = false;

      try {
         int portInt = Integer.parseInt(port);
         if (portInt <= 0 || portInt > 65535) {
            invalidPort = true;
         }
      } catch (NumberFormatException e) {
         invalidPort = true;
      }

      if (invalidPort) {
         throw new IllegalArgumentException(String.format("The connection string contains an invalid host '%s'. The port '%s' is not a valid, it must be an integer between 0 and 65535", host, port));
      }
   }

   private void validateProxyParameters() {
      if (this.proxyHost == null) {
         if (this.proxyPort != null) {
            throw new IllegalArgumentException("proxyPort can only be specified with proxyHost");
         }

         if (this.proxyUsername != null) {
            throw new IllegalArgumentException("proxyUsername can only be specified with proxyHost");
         }

         if (this.proxyPassword != null) {
            throw new IllegalArgumentException("proxyPassword can only be specified with proxyHost");
         }
      }

      if (this.proxyPort != null && (this.proxyPort < 0 || this.proxyPort > 65535)) {
         throw new IllegalArgumentException("proxyPort should be within the valid range (0 to 65535)");
      } else {
         if (this.proxyUsername != null) {
            if (this.proxyUsername.isEmpty()) {
               throw new IllegalArgumentException("proxyUsername cannot be empty");
            }

            if (this.proxyUsername.getBytes(StandardCharsets.UTF_8).length >= 255) {
               throw new IllegalArgumentException("username's length in bytes cannot be greater than 255");
            }
         }

         if (this.proxyPassword != null) {
            if (this.proxyPassword.isEmpty()) {
               throw new IllegalArgumentException("proxyPassword cannot be empty");
            }

            if (this.proxyPassword.getBytes(StandardCharsets.UTF_8).length >= 255) {
               throw new IllegalArgumentException("password's length in bytes cannot be greater than 255");
            }
         }

         if (this.proxyUsername == null ^ this.proxyPassword == null) {
            throw new IllegalArgumentException("Both proxyUsername and proxyPassword must be set together. They cannot be set individually");
         }
      }
   }

   private int countOccurrences(String haystack, String needle) {
      return haystack.length() - haystack.replace(needle, "").length();
   }

   private String urldecode(String input) {
      return this.urldecode(input, false);
   }

   private String urldecode(String input, boolean password) {
      try {
         return URLDecoder.decode(input, StandardCharsets.UTF_8.name());
      } catch (UnsupportedEncodingException e) {
         if (password) {
            throw new IllegalArgumentException("The connection string contained unsupported characters in the password.");
         } else {
            throw new IllegalArgumentException(String.format("The connection string contained unsupported characters: '%s'.Decoding produced the following error: %s", input, e.getMessage()));
         }
      }
   }

   @Nullable
   public String getUsername() {
      return this.credential != null ? this.credential.getUserName() : null;
   }

   @Nullable
   public char[] getPassword() {
      return this.credential != null ? this.credential.getPassword() : null;
   }

   public boolean isSrvProtocol() {
      return this.isSrvProtocol;
   }

   @Nullable
   public Integer getSrvMaxHosts() {
      return this.srvMaxHosts;
   }

   @Nullable
   public String getSrvServiceName() {
      return this.srvServiceName;
   }

   public List<String> getHosts() {
      return this.hosts;
   }

   @Nullable
   public String getDatabase() {
      return this.database;
   }

   @Nullable
   public String getCollection() {
      return this.collection;
   }

   @Nullable
   public Boolean isDirectConnection() {
      return this.directConnection;
   }

   @Nullable
   public Boolean isLoadBalanced() {
      return this.loadBalanced;
   }

   public String getConnectionString() {
      return this.connectionString;
   }

   @Nullable
   public MongoCredential getCredential() {
      return this.credential;
   }

   @Nullable
   public ReadPreference getReadPreference() {
      return this.readPreference;
   }

   @Nullable
   public ReadConcern getReadConcern() {
      return this.readConcern;
   }

   @Nullable
   public WriteConcern getWriteConcern() {
      return this.writeConcern;
   }

   @Nullable
   public Boolean getRetryWritesValue() {
      return this.retryWrites;
   }

   @Nullable
   public Boolean getRetryReads() {
      return this.retryReads;
   }

   @Nullable
   public Integer getMinConnectionPoolSize() {
      return this.minConnectionPoolSize;
   }

   @Nullable
   public Integer getMaxConnectionPoolSize() {
      return this.maxConnectionPoolSize;
   }

   @Nullable
   public Integer getMaxWaitTime() {
      return this.maxWaitTime;
   }

   @Nullable
   public Integer getMaxConnectionIdleTime() {
      return this.maxConnectionIdleTime;
   }

   @Nullable
   public Integer getMaxConnectionLifeTime() {
      return this.maxConnectionLifeTime;
   }

   @Nullable
   public Integer getMaxConnecting() {
      return this.maxConnecting;
   }

   @Nullable
   public Integer getConnectTimeout() {
      return this.connectTimeout;
   }

   @Nullable
   public Integer getSocketTimeout() {
      return this.socketTimeout;
   }

   @Nullable
   public Boolean getSslEnabled() {
      return this.sslEnabled;
   }

   @Nullable
   public String getProxyHost() {
      return this.proxyHost;
   }

   @Nullable
   public Integer getProxyPort() {
      return this.proxyPort;
   }

   @Nullable
   public String getProxyUsername() {
      return this.proxyUsername;
   }

   @Nullable
   public String getProxyPassword() {
      return this.proxyPassword;
   }

   @Nullable
   public Boolean getSslInvalidHostnameAllowed() {
      return this.sslInvalidHostnameAllowed;
   }

   @Nullable
   public String getRequiredReplicaSetName() {
      return this.requiredReplicaSetName;
   }

   @Nullable
   public Integer getServerSelectionTimeout() {
      return this.serverSelectionTimeout;
   }

   @Nullable
   public Integer getLocalThreshold() {
      return this.localThreshold;
   }

   @Nullable
   public Integer getHeartbeatFrequency() {
      return this.heartbeatFrequency;
   }

   @Nullable
   public String getApplicationName() {
      return this.applicationName;
   }

   public List<MongoCompressor> getCompressorList() {
      return this.compressorList;
   }

   @Nullable
   public UuidRepresentation getUuidRepresentation() {
      return this.uuidRepresentation;
   }

   public String toString() {
      return this.connectionString;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ConnectionString that = (ConnectionString)o;
         return this.isSrvProtocol == that.isSrvProtocol && Objects.equals(this.directConnection, that.directConnection) && Objects.equals(this.credential, that.credential) && Objects.equals(this.hosts, that.hosts) && Objects.equals(this.database, that.database) && Objects.equals(this.collection, that.collection) && Objects.equals(this.readPreference, that.readPreference) && Objects.equals(this.writeConcern, that.writeConcern) && Objects.equals(this.retryWrites, that.retryWrites) && Objects.equals(this.retryReads, that.retryReads) && Objects.equals(this.readConcern, that.readConcern) && Objects.equals(this.minConnectionPoolSize, that.minConnectionPoolSize) && Objects.equals(this.maxConnectionPoolSize, that.maxConnectionPoolSize) && Objects.equals(this.maxWaitTime, that.maxWaitTime) && Objects.equals(this.maxConnectionIdleTime, that.maxConnectionIdleTime) && Objects.equals(this.maxConnectionLifeTime, that.maxConnectionLifeTime) && Objects.equals(this.maxConnecting, that.maxConnecting) && Objects.equals(this.connectTimeout, that.connectTimeout) && Objects.equals(this.socketTimeout, that.socketTimeout) && Objects.equals(this.proxyHost, that.proxyHost) && Objects.equals(this.proxyPort, that.proxyPort) && Objects.equals(this.proxyUsername, that.proxyUsername) && Objects.equals(this.proxyPassword, that.proxyPassword) && Objects.equals(this.sslEnabled, that.sslEnabled) && Objects.equals(this.sslInvalidHostnameAllowed, that.sslInvalidHostnameAllowed) && Objects.equals(this.requiredReplicaSetName, that.requiredReplicaSetName) && Objects.equals(this.serverSelectionTimeout, that.serverSelectionTimeout) && Objects.equals(this.localThreshold, that.localThreshold) && Objects.equals(this.heartbeatFrequency, that.heartbeatFrequency) && Objects.equals(this.applicationName, that.applicationName) && Objects.equals(this.compressorList, that.compressorList) && Objects.equals(this.uuidRepresentation, that.uuidRepresentation) && Objects.equals(this.srvServiceName, that.srvServiceName) && Objects.equals(this.srvMaxHosts, that.srvMaxHosts);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.credential, this.isSrvProtocol, this.hosts, this.database, this.collection, this.directConnection, this.readPreference, this.writeConcern, this.retryWrites, this.retryReads, this.readConcern, this.minConnectionPoolSize, this.maxConnectionPoolSize, this.maxWaitTime, this.maxConnectionIdleTime, this.maxConnectionLifeTime, this.maxConnecting, this.connectTimeout, this.socketTimeout, this.sslEnabled, this.sslInvalidHostnameAllowed, this.requiredReplicaSetName, this.serverSelectionTimeout, this.localThreshold, this.heartbeatFrequency, this.applicationName, this.compressorList, this.uuidRepresentation, this.srvServiceName, this.srvMaxHosts, this.proxyHost, this.proxyPort, this.proxyUsername, this.proxyPassword});
   }

   static {
      GENERAL_OPTIONS_KEYS.add("minpoolsize");
      GENERAL_OPTIONS_KEYS.add("maxpoolsize");
      GENERAL_OPTIONS_KEYS.add("waitqueuetimeoutms");
      GENERAL_OPTIONS_KEYS.add("connecttimeoutms");
      GENERAL_OPTIONS_KEYS.add("maxidletimems");
      GENERAL_OPTIONS_KEYS.add("maxlifetimems");
      GENERAL_OPTIONS_KEYS.add("maxconnecting");
      GENERAL_OPTIONS_KEYS.add("sockettimeoutms");
      GENERAL_OPTIONS_KEYS.add("ssl");
      GENERAL_OPTIONS_KEYS.add("tls");
      GENERAL_OPTIONS_KEYS.add("tlsinsecure");
      GENERAL_OPTIONS_KEYS.add("sslinvalidhostnameallowed");
      GENERAL_OPTIONS_KEYS.add("tlsallowinvalidhostnames");
      GENERAL_OPTIONS_KEYS.add("proxyhost");
      GENERAL_OPTIONS_KEYS.add("proxyport");
      GENERAL_OPTIONS_KEYS.add("proxyusername");
      GENERAL_OPTIONS_KEYS.add("proxypassword");
      GENERAL_OPTIONS_KEYS.add("replicaset");
      GENERAL_OPTIONS_KEYS.add("readconcernlevel");
      GENERAL_OPTIONS_KEYS.add("serverselectiontimeoutms");
      GENERAL_OPTIONS_KEYS.add("localthresholdms");
      GENERAL_OPTIONS_KEYS.add("heartbeatfrequencyms");
      GENERAL_OPTIONS_KEYS.add("retrywrites");
      GENERAL_OPTIONS_KEYS.add("retryreads");
      GENERAL_OPTIONS_KEYS.add("appname");
      GENERAL_OPTIONS_KEYS.add("uuidrepresentation");
      GENERAL_OPTIONS_KEYS.add("directconnection");
      GENERAL_OPTIONS_KEYS.add("loadbalanced");
      GENERAL_OPTIONS_KEYS.add("srvmaxhosts");
      GENERAL_OPTIONS_KEYS.add("srvservicename");
      COMPRESSOR_KEYS.add("compressors");
      COMPRESSOR_KEYS.add("zlibcompressionlevel");
      READ_PREFERENCE_KEYS.add("readpreference");
      READ_PREFERENCE_KEYS.add("readpreferencetags");
      READ_PREFERENCE_KEYS.add("maxstalenessseconds");
      WRITE_CONCERN_KEYS.add("safe");
      WRITE_CONCERN_KEYS.add("w");
      WRITE_CONCERN_KEYS.add("wtimeoutms");
      WRITE_CONCERN_KEYS.add("journal");
      AUTH_KEYS.add("authmechanism");
      AUTH_KEYS.add("authsource");
      AUTH_KEYS.add("gssapiservicename");
      AUTH_KEYS.add("authmechanismproperties");
      ALL_KEYS.addAll(GENERAL_OPTIONS_KEYS);
      ALL_KEYS.addAll(AUTH_KEYS);
      ALL_KEYS.addAll(READ_PREFERENCE_KEYS);
      ALL_KEYS.addAll(WRITE_CONCERN_KEYS);
      ALL_KEYS.addAll(COMPRESSOR_KEYS);
      TRUE_VALUES = new HashSet(Arrays.asList("true", "yes", "1"));
      FALSE_VALUES = new HashSet(Arrays.asList("false", "no", "0"));
   }
}
