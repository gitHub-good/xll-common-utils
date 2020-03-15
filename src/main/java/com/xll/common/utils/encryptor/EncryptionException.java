package com.xll.common.utils.encryptor;

/**
 *
 * @Author：xuliangliang
 * @Description：加密异常处理
 * @Date：3:17 下午 2020/3/15
 */
public class EncryptionException extends Exception {

    public EncryptionException() {
    }

    public EncryptionException(String arg0) {
        super(arg0);
    }

    public EncryptionException(Throwable arg0) {
        super(arg0);
    }

    public EncryptionException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
