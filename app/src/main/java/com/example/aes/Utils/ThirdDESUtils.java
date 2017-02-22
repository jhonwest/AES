package com.example.aes.Utils;

/**
 * Created by lenovo on 2017/2/22.
 */
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ThirdDESUtils {
    /**
     * 3des 解密
     * @param value 加密后产生的密文
     * @param key 和加密时使用的密码相同
     * @return 明文
     * @throws Exception
     */
    public static String Decrypt3DES(String value, String key) throws Exception {
        byte[] b = decryptMode(GetKeyBytes(key), Base64.decode(value,Base64.DEFAULT));
        return new String(b);

    }


    /**
     * 3des 加密
     * @param value 需要加密的数据
     * @param key 加密时使用的密码
     * @return 密文
     * @throws Exception
     */
    public static String Encrypt3DES(String value, String key) throws Exception {
        String str = byte2Base64(encryptMode(GetKeyBytes(key), value.getBytes()));
        return str;

    }



    /**
     计算24位长的密码byte值,首先对原始密钥做MD5算hash值，再用前8位数据对应补全后8位
     */
    public static byte[] GetKeyBytes(String strKey) throws Exception {
        if (null == strKey || strKey.length() < 1)
            throw new Exception("key is null or empty!");


        java.security.MessageDigest alg = java.security.MessageDigest.getInstance("MD5");

        alg.update(strKey.getBytes());

        byte[] bkey = alg.digest();

        System.out.println("md5key.length=" + bkey.length);

        System.out.println("md5key=" + byte2hex(bkey));

        int start = bkey.length;

        byte[] bkey24 = new byte[24];

        for (int i = 0; i < start; i++) {
            bkey24[i] = bkey[i];
        }

        for (int i = start; i < 24; i++) {//为了与.net16位key兼容
            bkey24[i] = bkey[i - start];

        }

        System.out.println("byte24key.length=" + bkey24.length);
        System.out.println("byte24key=" + byte2hex(bkey24));

        return bkey24;

    }



    private static final String Algorithm = "DESede"; //定义 加密算法,可用 DES,DESede,Blowfish


    /**
     * 根据密码字节数组和需要加密的内容得到加密模式对应的字节数组
     * @param keybyte 原始密码对应的字节数组,长度为24字节
     * @param src 被加密数据对应的字节数组
     * @return 加密模式对应的字节数组
     */
    public static byte[] encryptMode(byte[] keybyte, byte[] src) {

        try {

            //生成密钥

            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm); //加密

            Cipher c1 = Cipher.getInstance(Algorithm);

            c1.init(Cipher.ENCRYPT_MODE, deskey);

            return c1.doFinal(src);

        } catch (java.security.NoSuchAlgorithmException e1) {

            e1.printStackTrace();

        } catch (javax.crypto.NoSuchPaddingException e2) {

            e2.printStackTrace();

        } catch (java.lang.Exception e3) {

            e3.printStackTrace();

        }

        return null;

    }

    /**
     * 使用密码字节数组和密文对应的字节数组得到解密模式对应的字节数组
     * @param keybyte keybyte为加密密钥，长度为24字节
     * @param src 密文对应的字节数组
     * @return 解密模式对应的字节数组
     */
    public static byte[] decryptMode(byte[] keybyte, byte[] src) {

        try { //生成密钥

            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

            //解密

            Cipher c1 = Cipher.getInstance(Algorithm);

            c1.init(Cipher.DECRYPT_MODE, deskey);

            return c1.doFinal(src);

        } catch (java.security.NoSuchAlgorithmException e1) {

            e1.printStackTrace();

        } catch (javax.crypto.NoSuchPaddingException e2) {

            e2.printStackTrace();

        } catch (java.lang.Exception e3) {

            e3.printStackTrace();

        }

        return null;

    }




    /**
     * 将字节数组转换成base64编码
     * @param b 字节数组
     * @return 字节数组对应的base64位字符串
     */
    public static String byte2Base64(byte[] b) {
        return Base64.encodeToString(b,Base64.DEFAULT);
    }


    /**
     * 将字节数组转换成十六进制字符串
     * @param b 字节数组
     * @return 十六进制字符串
     */
    public static String byte2hex(byte[] b) {

        String hs = "";

        String stmp = "";

        for (int n = 0; n < b.length; n++) {

            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));

            if (stmp.length() == 1)

                hs = hs + "0" + stmp;

            else

                hs = hs + stmp;

            if (n < b.length - 1)

                hs = hs + ":";

        }

        return hs.toUpperCase();

    }
}
