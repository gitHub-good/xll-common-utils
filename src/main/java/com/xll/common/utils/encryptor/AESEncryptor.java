package com.xll.common.utils.encryptor;

import com.xll.common.utils.base.ConfigurationUtil;
import com.xll.common.utils.base.EncryptUtil;
import com.xll.common.utils.convertor.Base64Convertor;
import org.apache.commons.lang.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 *
 * @Author：xuliangliang
 * @Description：
 * @Date：3:17 下午 2020/3/15
 */
public class AESEncryptor implements Encryptor {
    private Base64Convertor base64 = new Base64Convertor();
    private SecretKey key;

    public AESEncryptor() {
        String masterkey = ConfigurationUtil.getInstance().getUtilityConfiguration("aes.masterkey");
        this.key = new SecretKeySpec(this.base64.parse(masterkey), "AES");

        assert this.key != null : "AES Secret Key initiate failed";

    }

    @Override
    public String encrypt(String plainText) throws EncryptionException {
        return this.encrypt(plainText, (String)null);
    }

    @Override
    public String encrypt(String plainText, String masterkey) throws EncryptionException {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            if (StringUtils.isBlank(masterkey)) {
                cipher.init(1, this.key);
            } else {
                cipher.init(1, new SecretKeySpec(this.base64.parse(masterkey), "AES"));
            }

            byte[] c = cipher.doFinal(plainText.getBytes("UTF-8"));
            return this.base64.format(c);
        } catch (NoSuchAlgorithmException var5) {
            var5.printStackTrace();
        } catch (NoSuchPaddingException var6) {
            var6.printStackTrace();
        } catch (InvalidKeyException var7) {
            var7.printStackTrace();
        } catch (IllegalBlockSizeException var8) {
            var8.printStackTrace();
        } catch (BadPaddingException var9) {
            var9.printStackTrace();
        } catch (UnsupportedEncodingException var10) {
            var10.printStackTrace();
        }

        return null;
    }

    @Override
    public String decrypt(String cipherText) throws EncryptionException {
        return this.decrypt(cipherText, (String)null);
    }

    @Override
    public String decrypt(String cipherText, String masterkey) throws EncryptionException {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            if (StringUtils.isBlank(masterkey)) {
                cipher.init(2, this.key);
            } else {
                cipher.init(2, new SecretKeySpec(this.base64.parse(masterkey), "AES"));
            }

            byte[] b = cipher.doFinal(this.base64.parse(cipherText));
            return new String(b, "UTF-8");
        } catch (NoSuchAlgorithmException var5) {
            var5.printStackTrace();
        } catch (NoSuchPaddingException var6) {
            var6.printStackTrace();
        } catch (InvalidKeyException var7) {
            var7.printStackTrace();
        } catch (IllegalBlockSizeException var8) {
            var8.printStackTrace();
        } catch (BadPaddingException var9) {
            var9.printStackTrace();
        } catch (UnsupportedEncodingException var10) {
            var10.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            SecureRandom random = EncryptUtil.getSecureRandom();
            String skeylen = ConfigurationUtil.getInstance().getUtilityConfiguration("aes.key.length");
            generator.init(Integer.parseInt(skeylen), random);
            SecretKey key = generator.generateKey();
            System.out.println("========== AES Key ==========");
            Base64Convertor base64 = new Base64Convertor();
            System.out.println("aes.masterkey=" + base64.format(key.getEncoded()));
        } catch (NumberFormatException var6) {
            var6.printStackTrace();
        } catch (NoSuchAlgorithmException var7) {
            var7.printStackTrace();
        }

    }
}