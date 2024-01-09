package com.fw.common.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.*;

public class SecurityUtil {
  private SecurityUtil() {
  }
  private static final String ALGORITHM = "AES";
  private static final int AES_KEY_SIZE = 256;
  private static final int GCM_IV_LENGTH = 12;
  private static final int GCM_TAG_LENGTH = 16;
  private static final String ENCRYPT_ALG = "AES/GCM/NoPadding";
  private static final String KEY_ALG = "PBKDF2WithHmacSHA256";

  public static Cipher encCipher;
  public static Cipher decCipher;

  static {
    try {
      SecretKeySpec keySpec = getKeyFromPassword("p@ssw0rd", "1@3$5^7*");
      byte[] IV = new byte[GCM_IV_LENGTH];
      SecureRandom random = new SecureRandom();
      random.nextBytes(IV);
      GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);
      encCipher = Cipher.getInstance(ENCRYPT_ALG);
      decCipher = Cipher.getInstance(ENCRYPT_ALG);
      encCipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);
      decCipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException
        | InvalidAlgorithmParameterException | NoSuchPaddingException e) {
      e.printStackTrace();
    }
  }

  public static String encryptDefault(String plainValue) throws Exception {
    byte[] cipherText = encCipher.doFinal(plainValue.getBytes());
    Encoder enc = Base64.getEncoder();
    return enc.encodeToString(cipherText);
  }

  public static String decryptDefault(String encryptValue) throws Exception {
    Decoder dec = Base64.getDecoder();
    byte[] cipherText = dec.decode(encryptValue);
    byte[] decryptedText = decCipher.doFinal(cipherText);
    return new String(decryptedText);
  }

  public static String encrypt(String plainValue) throws Exception {
    KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
    keyGenerator.init(AES_KEY_SIZE);
    SecretKey key = keyGenerator.generateKey();
    SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), ALGORITHM);
    byte[] IV = new byte[GCM_IV_LENGTH];
    SecureRandom random = new SecureRandom();
    random.nextBytes(IV);
    byte[] cipherText = encrypt(plainValue.getBytes(), keySpec, IV);
    Encoder enc = Base64.getEncoder();
    byte[] keyEncoded = key.getEncoded();
    String p1 = enc.encodeToString(keyEncoded);
    String p2 = enc.encodeToString(IV);
    String p3 = enc.encodeToString(cipherText);
    return String.format("%s.%s.%s", p1, p2, p3);
  }

  public static String decrypt(String encrypedValue) throws Exception {
    String parts[] = encrypedValue.split("\\.");
    if (parts.length != 3) {
      return null;
    }
    Decoder dec = Base64.getDecoder();
    byte[] b1 = dec.decode(parts[0]);
    byte[] b2 = dec.decode(parts[1]);
    byte[] b3 = dec.decode(parts[2]);
    SecretKey key = new SecretKeySpec(b1, 0, b1.length, ALGORITHM);
    SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), ALGORITHM);
    return decrypt(b3, keySpec, b2);
  }

  private static byte[] encrypt(byte[] plaintext, SecretKeySpec keySpec, byte[] IV)
      throws Exception {
    Cipher cipher = Cipher.getInstance(ENCRYPT_ALG);
    GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);
    return cipher.doFinal(plaintext);
  }

  private static String decrypt(byte[] cipherText, SecretKeySpec keySpec, byte[] IV)
      throws Exception {
    Cipher cipher = Cipher.getInstance(ENCRYPT_ALG);
    GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);
    cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);
    byte[] decryptedText = cipher.doFinal(cipherText);
    return new String(decryptedText);
  }

  private static SecretKeySpec getKeyFromPassword(String password, String salt)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALG);
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
    return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
  }

  public static void disableSSLVerification() {
    try {
      HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
      });
      TrustManager[] trustManager = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
          return null;
        }
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
      } };
      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustManager, null);
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
    }
  }
}
