package dev.artixdev.libs.com.mongodb.internal.connection;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

final class InetAddressUtils {
   private static final int IPV4_PART_COUNT = 4;
   private static final int IPV6_PART_COUNT = 8;
   private static final char IPV4_DELIMITER = '.';
   private static final char IPV6_DELIMITER = ':';

   private InetAddressUtils() {
   }

   static InetAddress forString(String ipString) {
      byte[] addr = ipStringToBytes(ipString);
      if (addr == null) {
         throw new IllegalArgumentException(ipString + " IP address is incorrect");
      } else {
         return bytesToInetAddress(addr);
      }
   }

   static boolean isInetAddress(String ipString) {
      return ipStringToBytes(ipString) != null;
   }

   @Nullable
   static byte[] ipStringToBytes(String ipStringParam) {
      String ipString = ipStringParam;
      boolean hasColon = false;
      boolean hasDot = false;
      int percentIndex = -1;

      for(int i = 0; i < ipString.length(); ++i) {
         char c = ipString.charAt(i);
         if (c == '.') {
            hasDot = true;
         } else if (c == ':') {
            if (hasDot) {
               return null;
            }

            hasColon = true;
         } else {
            if (c == '%') {
               percentIndex = i;
               break;
            }

            if (Character.digit(c, 16) == -1) {
               return null;
            }
         }
      }

      if (hasColon) {
         if (hasDot) {
            ipString = convertDottedQuadToHex(ipString);
            if (ipString == null) {
               return null;
            }
         }

         if (percentIndex != -1) {
            ipString = ipString.substring(0, percentIndex);
         }

         return textToNumericFormatV6(ipString);
      } else if (hasDot) {
         return percentIndex != -1 ? null : textToNumericFormatV4(ipString);
      } else {
         return null;
      }
   }

   private static boolean hasCorrectNumberOfOctets(String sequence) {
      int matches = 3;

      int index;
      for(index = 0; matches-- > 0; ++index) {
         index = sequence.indexOf(46, index);
         if (index == -1) {
            return false;
         }
      }

      return sequence.indexOf(46, index) == -1;
   }

   private static int countIn(CharSequence sequence, char character) {
      int count = 0;

      for(int i = 0; i < sequence.length(); ++i) {
         if (sequence.charAt(i) == character) {
            ++count;
         }
      }

      return count;
   }

   @Nullable
   private static byte[] textToNumericFormatV4(String ipString) {
      if (!hasCorrectNumberOfOctets(ipString)) {
         return null;
      } else {
         byte[] bytes = new byte[4];
         int start = 0;

         for(int i = 0; i < 4; ++i) {
            int end = ipString.indexOf(46, start);
            if (end == -1) {
               end = ipString.length();
            }

            try {
               bytes[i] = parseOctet(ipString, start, end);
            } catch (NumberFormatException ignored) {
               return null;
            }

            start = end + 1;
         }

         return bytes;
      }
   }

   @Nullable
   private static byte[] textToNumericFormatV6(String ipString) {
      int delimiterCount = countIn(ipString, ':');
      if (delimiterCount >= 2 && delimiterCount <= 8) {
         int partsSkipped = 8 - (delimiterCount + 1);
         boolean hasSkip = false;

         for(int i = 0; i < ipString.length() - 1; ++i) {
            if (ipString.charAt(i) == ':' && ipString.charAt(i + 1) == ':') {
               if (hasSkip) {
                  return null;
               }

               hasSkip = true;
               ++partsSkipped;
               if (i == 0) {
                  ++partsSkipped;
               }

               if (i == ipString.length() - 2) {
                  ++partsSkipped;
               }
            }
         }

         if (ipString.charAt(0) == ':' && ipString.charAt(1) != ':') {
            return null;
         } else if (ipString.charAt(ipString.length() - 1) == ':' && ipString.charAt(ipString.length() - 2) != ':') {
            return null;
         } else if (hasSkip && partsSkipped <= 0) {
            return null;
         } else if (!hasSkip && delimiterCount + 1 != 8) {
            return null;
         } else {
            ByteBuffer rawBytes = ByteBuffer.allocate(16);

            try {
               int start = 0;
               if (ipString.charAt(0) == ':') {
                  start = 1;
               }

               int end;
               for(; start < ipString.length(); start = end + 1) {
                  end = ipString.indexOf(58, start);
                  if (end == -1) {
                     end = ipString.length();
                  }

                  if (ipString.charAt(start) == ':') {
                     for(int i = 0; i < partsSkipped; ++i) {
                        rawBytes.putShort((short)0);
                     }
                  } else {
                     rawBytes.putShort(parseHextet(ipString, start, end));
                  }
               }
            } catch (NumberFormatException ignored) {
               return null;
            }

            return rawBytes.array();
         }
      } else {
         return null;
      }
   }

   @Nullable
   private static String convertDottedQuadToHex(String ipString) {
      int lastColon = ipString.lastIndexOf(58);
      String initialPart = ipString.substring(0, lastColon + 1);
      String dottedQuad = ipString.substring(lastColon + 1);
      byte[] quad = textToNumericFormatV4(dottedQuad);
      if (quad == null) {
         return null;
      } else {
         String penultimate = Integer.toHexString((quad[0] & 255) << 8 | quad[1] & 255);
         String ultimate = Integer.toHexString((quad[2] & 255) << 8 | quad[3] & 255);
         return initialPart + penultimate + ":" + ultimate;
      }
   }

   private static byte parseOctet(String ipString, int start, int end) {
      int length = end - start;
      if (length > 0 && length <= 3) {
         if (length > 1 && ipString.charAt(start) == '0') {
            throw new NumberFormatException("IP address octal representation is not supported");
         } else {
            int octet = 0;

            for(int i = start; i < end; ++i) {
               octet *= 10;
               int digit = Character.digit(ipString.charAt(i), 10);
               if (digit < 0) {
                  throw new NumberFormatException();
               }

               octet += digit;
            }

            if (octet > 255) {
               throw new NumberFormatException();
            } else {
               return (byte)octet;
            }
         }
      } else {
         throw new NumberFormatException();
      }
   }

   private static short parseHextet(String ipString, int start, int end) {
      int length = end - start;
      if (length > 0 && length <= 4) {
         int hextet = 0;

         for(int i = start; i < end; ++i) {
            hextet <<= 4;
            hextet |= Character.digit(ipString.charAt(i), 16);
         }

         return (short)hextet;
      } else {
         throw new NumberFormatException();
      }
   }

   private static InetAddress bytesToInetAddress(byte[] addr) {
      try {
         return InetAddress.getByAddress(addr);
      } catch (UnknownHostException e) {
         throw new AssertionError(e);
      }
   }
}
