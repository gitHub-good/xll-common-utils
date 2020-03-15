package com.xll.common.utils.base;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EncodingUtil {
    public static final String CHARSET_ISO88591 = "ISO-8859-1";
    public static final String CHARSET_88591 = "8859_1";
    public static final String CHARSET_GB18030 = "GB18030";
    public static final String CHARSET_GB2312 = "GB18030";
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String CHARSET_UTF16 = "UTF-16";
    public static final String CHARSET_UNICODEBIGUNMARKED = "UnicodeBigUnmarked";
    public static final String CHARSET_USASCII = "US-ASCII";
    public static final String ENCODING_DEFAULT = "ISO-8859-1";
    private static Map<String, String> theLangToEncodingMap = new HashMap();
    private static final char[] digits;

    public static boolean isChinese(String txt) throws UnsupportedEncodingException {
        byte[] bytes = new byte[0];

        try {
            bytes = txt.getBytes("ISO-8859-1");

            for(int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {
                    return true;
                }
            }

            return false;
        } catch (UnsupportedEncodingException var3) {
            return false;
        }
    }

    public static boolean isChinese(char message) {
        boolean isChinese = false;
        if (message < 0 || message > 127) {
            isChinese = true;
        }

        return isChinese;
    }

    public static String getSystemEncoding() {
        String lang = Locale.getDefault().getLanguage();
        String enc = (String)theLangToEncodingMap.get(lang);
        return StringUtil.zero(enc) ? "ISO-8859-1" : enc;
    }

    public static boolean sameSystemEncoding(String enc) {
        return enc.equals(getSystemEncoding());
    }

    public static String convert(String str, String srcEnc, String dstEnc) {
        if (str == null) {
            return "";
        } else {
            try {
                if (needConvert(str, srcEnc, dstEnc)) {
                    str = new String(str.getBytes(srcEnc), dstEnc);
                }
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return str;
        }
    }

    public static boolean needConvert(String str, String srcEnc, String dstEnc) throws Exception {
        if (StringUtil.empty(str)) {
            return false;
        } else {
            return !ObjectUtil.equals(srcEnc, dstEnc);
        }
    }

    public static String ISO88591toGB2312(String gbStr) {
        if (gbStr != null) {
            try {
                gbStr = new String(gbStr.getBytes("ISO-8859-1"), "GB18030");
            } catch (Throwable var2) {
                return "";
            }
        }

        return gbStr;
    }

    public static String GB2312toISO88591(String uStr) {
        if (uStr != null) {
            try {
                uStr = new String(uStr.getBytes("GB18030"), "ISO-8859-1");
            } catch (Throwable var2) {
                return "";
            }
        }

        return uStr;
    }

    public static String GB18030toISO88591(String uStr) {
        if (uStr != null) {
            try {
                uStr = new String(uStr.getBytes("GB18030"), "ISO-8859-1");
            } catch (Throwable var2) {
                return "";
            }
        }

        return uStr;
    }

    public static String toISO88591(String uStr) {
        return toEncoding(uStr, "ISO-8859-1");
    }

    public static String toGB2312(String uStr) {
        return toEncoding(uStr, "GB18030");
    }

    public static String toUnicode(String str, String currEncoding) {
        if (str != null) {
            try {
                str = new String(str.getBytes(currEncoding));
            } catch (Throwable var3) {
                return "";
            }
        }

        return str;
    }

    public static String toUnicodeKeep(String str, String currEncoding) {
        if (str != null) {
            try {
                str = new String(str.getBytes(currEncoding));
            } catch (Throwable var3) {
                return str;
            }
        }

        return str;
    }

    public static String toEncoding(String str, String destEncoding) {
        if (str != null) {
            try {
                str = new String(str.getBytes(), destEncoding);
            } catch (Throwable var3) {
                return "";
            }
        }

        return str;
    }

    public static String toEncodingKeep(String str, String destEncoding) {
        if (str != null) {
            try {
                str = new String(str.getBytes(), destEncoding);
            } catch (Throwable var3) {
                return str;
            }
        }

        return str;
    }

    public static byte[] toByteArray(String str, String srcEncoding) {
        try {
            return str.getBytes(srcEncoding);
        } catch (UnsupportedEncodingException var3) {
            return str.getBytes();
        }
    }

    public static String URLEncoderToUTF8(String str) {
        if (StringUtil.empty(str)) {
            return "";
        } else {
            try {
                str = URLEncoder.encode(str, "UTF-8");
                str = StringUtil.replace(str, "%", "~");
            } catch (UnsupportedEncodingException var2) {
                var2.printStackTrace();
            }

            return str;
        }
    }

    public static String URLDecodeFromUTF8(String str) {
        try {
            str = StringUtil.replace(str, "~", "%");
            str = URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            var2.printStackTrace();
        }

        return str;
    }

    static {
        theLangToEncodingMap.put("zh", "GB18030");
        theLangToEncodingMap.put("en", "ISO-8859-1");
        digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    }
}
