package teratail_java.q369407;

import java.security.*;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.*;

public class decrypted2 {
  public static void main(String[] args) throws Exception {
    //byte[] myIv = Base64.getDecoder().decode("1xF2aTB8KP9JW4Dl");

    //byte[] b = cipherEncrypt("my secret data".getBytes(Charset.forName("Shift_JIS")), "authtag", myIv);
    //System.out.println("b="+Arrays.toString(b));

    //byte[] cipherText = Base64.getDecoder().decode("NzCmNt2+BO9xBOkdD6g=");
    //System.out.println("b="+Arrays.toString(cipherText));

    //sampleGcmAesEnctipt();

    q369407();
  }

  static void q369407() throws Exception {
    byte[] cipherText = Base64.getDecoder().decode("NzCmNt2+BO9xBOkdD6g="); // encrypted data
    byte[] myIv = Base64.getDecoder().decode("1xF2aTB8KP9JW4Dl"); // iv
    byte[] auth_tag = Base64.getDecoder().decode("m0m6f6XX6p+JxsfRtoa4DQ=="); // auth_tag
    int myTLen = 128; // auth_tagビット長
    byte[] secretKey = Base64.getDecoder().decode("3O3oUShs7SYm92SJGBHY4iwUDppMQctXDWMMzmMYBnU"); // secretKey

    System.out.println("cipherText: length="+cipherText.length+", "+Arrays.toString(cipherText));
    System.out.println("myIv: length="+myIv.length+", "+Arrays.toString(myIv));
    System.out.println("auth_tag: length="+auth_tag.length+", "+Arrays.toString(auth_tag));
    System.out.println("secretKey: length="+secretKey.length+", "+Arrays.toString(secretKey));

    SecretKeySpec myKey = new SecretKeySpec(secretKey, "AES");

    GCMParameterSpec myParams = new GCMParameterSpec(myTLen, myIv);
    Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
    c.init(Cipher.DECRYPT_MODE, myKey, myParams);

    System.out.println("blocksize="+c.getBlockSize());

    // 暗号化後文字列とauth_tagを連結する
    byte[] ciTxt_tag = new byte[cipherText.length + auth_tag.length];
    System.arraycopy(cipherText, 0, ciTxt_tag, 0, cipherText.length);
    System.arraycopy(auth_tag, 0, ciTxt_tag, cipherText.length, auth_tag.length);

    c.updateAAD(auth_tag);
    byte[] recoveredText = c.doFinal(cipherText);

    System.out.println("復号後の文字列： " + new String(recoveredText));
  }

  //暗号化
  //buf 暗号化するデータ
  //key 暗号化鍵
  //iv  IV(初期化ベクトル)
  static byte[] cipherEncrypt(byte[] buf, String keyStr, byte[] iv){
    //秘密鍵を構築します
    byte[] key = new byte[16];
    byte[] keyBytes = keyStr.getBytes();
    System.arraycopy(keyBytes, 0, key, 0, Math.min(buf.length, keyBytes.length));
    SecretKeySpec sksSpec = new SecretKeySpec(key, "AES");
    //IV(初期化ベクトル)を構築します
    //IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
    GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
    try {
      //暗号化を行うアルゴリズム、モード、パディング方式を指定します
      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      //初期化します
      cipher.init(Cipher.ENCRYPT_MODE, sksSpec, gcmSpec);
      //暗号化します
      return cipher.doFinal(buf);
    } catch (NoSuchAlgorithmException
        | NoSuchPaddingException
        | InvalidKeyException
        | InvalidAlgorithmParameterException
        | IllegalBlockSizeException
        | BadPaddingException e) {
      e.printStackTrace();
    }
    return null;
  }

  static void sampleGcmAesEnctipt() {
    byte[] key = Base64.getDecoder().decode("3O3oUShs7SYm92SJGBHY4iwUDppMQctXDWMMzmMYBnU"); // secretKey
    SecretKey myKey = new SecretKeySpec(key, "AES");

    byte[] plainText = "my secret data".getBytes();
    int myTLen = 128;
    byte[] myIv = Base64.getDecoder().decode("1xF2aTB8KP9JW4Dl"); // iv
    //byte[] auth_tag = Base64.getDecoder().decode("m0m6f6XX6p+JxsfRtoa4DQ=="); // auth_tag

    try {
      GCMParameterSpec myParams = new GCMParameterSpec(myTLen, myIv);
      Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
      c.init(Cipher.ENCRYPT_MODE, myKey, myParams);
      //c.updateAAD(auth_tag);
      byte[] cipherText = new byte[c.getOutputSize(plainText.length)];
      int actualOutputLen = c.doFinal(plainText, 0, plainText.length, cipherText);

      System.out.println("actualOutputLen="+actualOutputLen);
      System.out.println("cipherText="+Arrays.toString(cipherText));
      System.out.println("base64="+new String(Base64.getEncoder().encode(cipherText)));

      c.init(Cipher.DECRYPT_MODE, myKey, myParams);
      //c.updateAAD(auth_tag);
      byte[] recoveredText = c.doFinal(cipherText, 0, actualOutputLen);
      System.out.println("decrypt='"+new String(recoveredText)+"'");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //複合化
  //buf 暗号化されたデータ
  //key 暗号化鍵
  //iv  IV(初期化ベクトル)
  static byte[] cipherDecrypt(byte[] buf, String key, String iv){

    //秘密鍵を構築します
    SecretKeySpec sksSpec = new SecretKeySpec(key.getBytes(), "AES");
    //IV(初期化ベクトル)を構築します
    IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
    try {
      //暗号化を行うアルゴリズム、モード、パディング方式を指定します
      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      //初期化します
      cipher.init(Cipher.DECRYPT_MODE, sksSpec, ivSpec);
      //複合化します
      return cipher.doFinal(buf);
    } catch (NoSuchAlgorithmException
        | NoSuchPaddingException
        | InvalidKeyException
        | InvalidAlgorithmParameterException
        | IllegalBlockSizeException
        | BadPaddingException e) {
      e.printStackTrace();
    }
    return null;
  }
}
