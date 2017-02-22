package com.example.aes;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.LruCache;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aes.Utils.AESUtils;
import com.example.aes.Utils.Base64Utils;
import com.example.aes.Utils.DESUtils;
import com.example.aes.Utils.MD5Utils;
import com.example.aes.Utils.RSAUtils;
import com.example.aes.Utils.ThirdDESUtils;

import java.security.Key;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private LruCache<String,Bitmap> lruCache;

    private EditText editText;
    private TextView textView_encrypt;
    private TextView textView_decrypt;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        textView_encrypt = (TextView) findViewById(R.id.textView_encrypt);
        textView_decrypt = (TextView) findViewById(R.id.textView_decrypt);

    }


    /**
     * AES 加解密
     * @param view
     */
    public void aes(View view){

    /*
       key(就是用于加解密的密码)的长度必须是16位或者24位或者32位字符作为seed,否则报异常:
       java.security.InvalidKeyException: Key length not 128/192/256 bits.

     */
        //String key = "111111111111111";//16位
        //String key = "jwtasr199212345678909090";//24位
        String key = "jiangweitaoanshouren123456789101";//32位
        String text = null;
        str = editText.getText().toString();
        try {
            text = AESUtils.encrypt(key, str);
            textView_encrypt.setText("加密后:"+text);
            System.out.println("加密后:"+text);
            text = AESUtils.decrypt(key, text);
            textView_decrypt.setText("解密后:"+text);
            System.out.println("解密后:"+text);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * DES加密
     * @param view
     */
    public void des(View view){
        /**
         * 密码至少是8个字节(8个字符或者三个汉字),否则报异常:
         * java.security.InvalidKeyException: key too short
         */
        String key = "安守仁";//三个汉字相当于9个字节
        str = editText.getText().toString();
        try {
            String text = DESUtils.encrypt(str, key);
            textView_encrypt.setText("加密后:"+text);
            System.out.println("===加密结果是=="+ DESUtils.encrypt(str, key));
            text = DESUtils.decrypt(DESUtils.encrypt(str, key), key);
            textView_decrypt.setText("解密后:"+text);
            System.out.println("==解密结果是==="+DESUtils.decrypt(DESUtils.encrypt(str, key), key));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 3DES加解密
     * @param view
     */
    public void thirdDES(View view){
        // String password = "abcd1234";
        String password = "jwtasr";

        String  data =editText.getText().toString();

        try {
            String encrypt= ThirdDESUtils.Encrypt3DES(data,password);
            textView_encrypt.setText("加密后:"+encrypt);
            System.out.println("加密后字符串:"+encrypt);
            String decrypt=ThirdDESUtils.Decrypt3DES(encrypt,password);
            textView_decrypt.setText("解密后："+decrypt);
            System.out.println("解密字符串:"+decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * rsa 非对称加密算法加解密
     * @param view
     */
    public void rsa(View view){
        String data =editText.getText().toString();
        try {
            RSAUtils rsa = new RSAUtils();
            Map<String, Object> map = rsa.genKeyPair();
            Key priKey = rsa.getPrivateKey(map);
            Key pubKey = rsa.getPublicKey(map);
            //得到加密后的字节数组
            byte[] enRsaBytes = rsa.encrypt(pubKey, data.getBytes());
            String encrypt= Base64Utils.encode(enRsaBytes);
            System.out.println("加密后==" + encrypt);
            textView_encrypt.setText("加密后:"+encrypt);

            String decrypt=new String(rsa.decrypt(priKey,enRsaBytes));
            textView_decrypt.setText("解密后:" +decrypt);
            System.out.println("解密后==" +decrypt);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * md5加密,需要注意的是:md5加密不可逆（或者说MD5本身就不是加密方式）
     * @param view
     */
    public void md5(View view){
        String data =editText.getText().toString();
        String md5= MD5Utils.getMD5String(data);
        System.out.println("==md5="+md5);
        textView_encrypt.setText("加密后:"+md5);
        textView_decrypt.setText("MD5并非加密方式，所以不能解密");
    }

    /**
     * base64加密解密
     * 未完成，encode与decode似乎写反了
     * 以及解密出来的结果和预想值不同
     * 失败
     * @param view
     */
    public void base64(View view){

        String password = "jwtasr";

        String  data =editText.getText().toString();

        try {
            byte [] encrypt = Base64Utils.decode(data);
            String encrypt_str= (Base64Utils.decode(data)).toString();
            textView_encrypt.setText("加密后:"+encrypt_str);
            System.out.println("加密后字符串:"+encrypt);
            String decrypt=Base64Utils.encode(encrypt);
            textView_decrypt.setText("解密后："+decrypt);
            System.out.println("解密字符串:"+decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
