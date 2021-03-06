package com.example.aes.Utils;

/**
 * Created by lenovo on 2017/2/22.
 */
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
public class AESUtils {
    /**
     * 使用指定的密码对明文进行加密
     * @param seed 密码,必须是16,24或者32位.
     * @param cleartext 需要加密的明文
     * @return 密文
     * @throws Exception
     */
    public static String encrypt(String seed, String cleartext) throws Exception {
        //byte[] rawKey = getRawKey(seed.getBytes());
        byte[] rawKey = seed.getBytes();
        byte[] result = encrypt(rawKey, cleartext.getBytes());
        return toHex(result);
    }

    /**
     * 解密
     * @param seed 密码,必须和加密时使用的密码相同
     * @param encrypted 加密后的密文
     * @return 明文
     * @throws Exception
     */
    public static String decrypt(String seed, String encrypted) throws Exception {
        //byte[] rawKey = getRawKey(seed.getBytes());
        byte[] rawKey = seed.getBytes();
        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        //创建密码生成器KeyGenerator对象
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        //创建设备安全随机数随机数
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        //设置密码和安全随机数
        kgen.init(128, sr); // 192 and 256 bits may not be available
        //调用SecretKey的generateKey( )方法创建SecretKey密码对象
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }


    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }
    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length()/2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2*buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }
    private final static String HEX = "0123456789ABCDEF";
    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
    }
}
