package com.xll.common.utils.base;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Encrypt {
    public static final String MD5_ENCODEING = "utf-8";
    private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public Md5Encrypt() {
    }

    private static String getMD5ofStr(byte[] digest) {
        String digestHexStr = "";

        for(int i = 0; i < 16; ++i) {
            digestHexStr = digestHexStr + byteHEX(digest[i]);
        }

        return digestHexStr;
    }

    private static String byteHEX(byte ib) {
        char[] ob = new char[]{DIGITS[ib >>> 4 & 15], DIGITS[ib & 15]};
        String s = new String(ob);
        return s;
    }

    public static String md5(String text, String encoding) {
        MessageDigest msgDigest = null;

        try {
            msgDigest = MessageDigest.getInstance("MD5");
            msgDigest.reset();
        } catch (NoSuchAlgorithmException var5) {
            throw new IllegalStateException("System doesn't support MD5 algorithm.");
        }

        try {
            msgDigest.update(text.getBytes(encoding));
        } catch (UnsupportedEncodingException var4) {
            throw new IllegalStateException("System doesn't support your  EncodingException.");
        }

        return getMD5ofStr(msgDigest.digest());
    }

    public static String md5(String str) {
        if (str == null) {
            return null;
        } else {
            MessageDigest messageDigest = null;

            try {
                messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.reset();
                messageDigest.update(str.getBytes("UTF-8"));
            } catch (NoSuchAlgorithmException var5) {
                return str;
            } catch (UnsupportedEncodingException var6) {
                return str;
            }

            byte[] byteArray = messageDigest.digest();
            StringBuffer md5StrBuff = new StringBuffer();

            for(int i = 0; i < byteArray.length; ++i) {
                if (Integer.toHexString(255 & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(255 & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(255 & byteArray[i]));
                }
            }

            return md5StrBuff.toString();
        }
    }
}
