package com.xll.common.utils.encryptor;

public interface Encryptor {
    String encrypt(String var1) throws EncryptionException;

    String decrypt(String var1) throws EncryptionException;

    String encrypt(String var1, String var2) throws EncryptionException;

    String decrypt(String var1, String var2) throws EncryptionException;
}
