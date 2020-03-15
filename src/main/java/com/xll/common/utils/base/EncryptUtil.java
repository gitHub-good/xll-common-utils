package com.xll.common.utils.base;

import com.xll.common.utils.convertor.Base64Convertor;
import com.xll.common.utils.encryptor.EncryptionException;
import com.xll.common.utils.encryptor.Encryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EncryptUtil {
    private static final Logger logger = LoggerFactory.getLogger(EncryptUtil.class);
    public static final String ENCRYPTOR_PREFIX = "encryptor.";
    public static final int DEFAULT_ITERATIONS = 1024;
    public static final String DEFAULT_ENCRYPT_ALGORITHM = "AES";
    public static final String DEFAULT_SIGNATURE_ALGORITHM = "SHA1withDSA";
    public static final String DEFAULT_RANDOM_ALGORITHM = "SHA1PRNG";
    public static final String DEFAULT_HASH_ALGORITHM = "SHA-512";
    public static final String DEFAULT_DIGEST_ALGORITHM = "MD5";
    private static EncryptUtil instance = null;
    private Encryptor encryptor = null;
    private Base64Convertor base64Convertor = new Base64Convertor();
    private ThreadLocal<MessageDigest> hasherContext = new ThreadLocal();
    private ThreadLocal<MessageDigest> digesterContext = new ThreadLocal();

    private EncryptUtil() {
        String encryptMethod = ConfigurationUtil.getInstance().getUtilityConfiguration("nebula.encrypt.algorithm");
        String encryptClassKey = "encryptor." + (encryptMethod == null ? "AES" : encryptMethod);
        String encryptClass = ConfigurationUtil.getInstance().getUtilityConfiguration(encryptClassKey);
        if (encryptClass == null) {
            logger.error("Encryptor initialization fail. No registration found for {} so no further encrypt/decrypt operation can be success.", encryptClassKey);
        }

        try {
            this.encryptor = (Encryptor)Class.forName(encryptClass).newInstance();
        } catch (Exception var5) {
            var5.printStackTrace();
            logger.error("Encryptor initialization fail. {} can not be initialized.", encryptClassKey);
        }

    }

    public static synchronized EncryptUtil getInstance() {
        if (instance == null) {
            instance = new EncryptUtil();
        }

        return instance;
    }

    public String hash(String plainText, String salt) {
        return this.hash(plainText, salt, 1024);
    }

    public String hash(String plainText, String salt, int iterations) {
        byte[] bytes = this.hashNative(plainText, salt, iterations);
        return bytes == null ? null : this.base64Convertor.format(bytes);
    }

    private MessageDigest getHasher() {
        String hashAlgorithm = ConfigurationUtil.getInstance().getUtilityConfiguration("nebula.hash.algorithm");

        try {
            return MessageDigest.getInstance(hashAlgorithm == null ? "SHA-512" : hashAlgorithm);
        } catch (NoSuchAlgorithmException var3) {
            logger.error("No algorithm found for digest with name {}", hashAlgorithm == null ? "SHA-512" : hashAlgorithm);
            logger.error("Digester for Hash initialzation fail, so no further hash operation can be success");
            return null;
        }
    }

    private MessageDigest getDigester() {
        String digestAlgorithm = ConfigurationUtil.getInstance().getUtilityConfiguration("nebula.digest.algorithm");

        try {
            return MessageDigest.getInstance(digestAlgorithm == null ? "MD5" : digestAlgorithm);
        } catch (NoSuchAlgorithmException var3) {
            logger.error("No algorithm found for digest with name {}", digestAlgorithm == null ? "MD5" : digestAlgorithm);
            logger.error("Digester initialzation fail, so no further digest operation can be success");
            return null;
        }
    }

    private byte[] hashNative(String plainText, String salt, int iterations) {
        if (plainText == null) {
            return null;
        } else {
            try {
                MessageDigest hasher = (MessageDigest)this.hasherContext.get();
                if (hasher == null) {
                    hasher = this.getHasher();
                    this.hasherContext.set(hasher);
                }

                hasher.reset();
                if (salt != null) {
                    hasher.update(salt.getBytes("UTF-8"));
                }

                hasher.update(plainText.getBytes("UTF-8"));
                byte[] bytes = hasher.digest();

                for(int i = 0; i < iterations; ++i) {
                    hasher.reset();
                    bytes = hasher.digest(bytes);
                }

                return bytes;
            } catch (UnsupportedEncodingException var7) {
                logger.error("Get Bytes for digest data error with UTF-8");
                throw new RuntimeException("Get Bytes for digest data error with UTF-8");
            }
        }
    }

    public String encrypt(String plainText) throws EncryptionException {
        if (this.encryptor == null) {
            throw new RuntimeException("Encryptor does not initiate properly.");
        } else {
            return this.encryptor.encrypt(plainText);
        }
    }

    public String decrypt(String cipherText) throws EncryptionException {
        if (this.encryptor == null) {
            throw new RuntimeException("Encryptor does not initiate properly.");
        } else {
            return this.encryptor.decrypt(cipherText);
        }
    }

    public String encrypt(String plainText, String masterkey) throws EncryptionException {
        if (this.encryptor == null) {
            throw new RuntimeException("Encryptor does not initiate properly.");
        } else {
            return this.encryptor.encrypt(plainText, masterkey);
        }
    }

    public String decrypt(String cipherText, String masterkey) throws EncryptionException {
        if (this.encryptor == null) {
            throw new RuntimeException("Encryptor does not initiate properly.");
        } else {
            return this.encryptor.decrypt(cipherText, masterkey);
        }
    }

    public String sign(String data) {
        return null;
    }

    public boolean verifySign(String signature, String data) {
        return false;
    }

    public String digest(String data) {
        if (data == null) {
            return null;
        } else {
            try {
                MessageDigest digester = (MessageDigest)this.digesterContext.get();
                if (digester == null) {
                    digester = this.getDigester();
                    this.digesterContext.set(digester);
                }

                digester.reset();
                return StringUtils.bytes2String(digester.digest(data.getBytes("UTF-8")));
            } catch (UnsupportedEncodingException var3) {
                logger.error("Get Bytes for digest data error with UTF-8");
                throw new RuntimeException("Get Bytes for digest data error with UTF-8");
            }
        }
    }

    public static SecureRandom getSecureRandom() {
        String randomAlgorithm = ConfigurationUtil.getInstance().getUtilityConfiguration("nebula.random.algorithm");

        try {
            if (randomAlgorithm == null) {
                randomAlgorithm = "SHA1PRNG";
            }

            SecureRandom random = SecureRandom.getInstance(randomAlgorithm);
            return random;
        } catch (NoSuchAlgorithmException var2) {
            throw new RuntimeException("Get SecureRandom Implementation Error with Algorithem " + randomAlgorithm);
        }
    }

    public String base64Encode(String source) {
        return org.apache.commons.lang3.StringUtils.isBlank(source) ? null : this.base64Convertor.format(source.getBytes());
    }

    public String base64Decode(String source) {
        return org.apache.commons.lang3.StringUtils.isBlank(source) ? null : new String(this.base64Convertor.parse(source));
    }

}
