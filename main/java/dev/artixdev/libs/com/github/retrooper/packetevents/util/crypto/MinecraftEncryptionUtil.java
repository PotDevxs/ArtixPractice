package dev.artixdev.libs.com.github.retrooper.packetevents.util.crypto;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MinecraftEncryptionUtil {
   public static byte[] decryptRSA(PrivateKey privateKey, byte[] data) {
      return decrypt("RSA/ECB/PKCS1Padding", privateKey, data);
   }

   public static byte[] encryptRSA(PublicKey publicKey, byte[] data) {
      return encrypt("RSA/ECB/PKCS1Padding", publicKey, data);
   }

   public static byte[] decrypt(Cipher cipher, byte[] data) {
      try {
         return cipher.doFinal(data);
      } catch (BadPaddingException | IllegalBlockSizeException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static byte[] decrypt(String algorithm, PrivateKey privateKey, byte[] data) {
      try {
         Cipher cipher = Cipher.getInstance(algorithm);
         cipher.init(2, privateKey);
         return cipher.doFinal(data);
      } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static byte[] encrypt(String algorithm, PublicKey publicKey, byte[] data) {
      try {
         Cipher cipher = Cipher.getInstance(algorithm);
         cipher.init(1, publicKey);
         return cipher.doFinal(data);
      } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static byte[] encrypt(Cipher cipher, byte[] data) {
      return decrypt(cipher, data);
   }

   public static PublicKey publicKey(byte[] bytes) {
      try {
         EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(bytes);
         KeyFactory keyFactory = KeyFactory.getInstance("RSA");
         return keyFactory.generatePublic(encodedKeySpec);
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }
}
