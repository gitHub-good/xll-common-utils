package com.xll.common.utils.base;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

/**
 *
 * @Author：xuliangliang
 * @Description：
 * @Date：1:05 上午 2020/3/16
 */
public class ChineseCharToEn {
    private static final Logger logger = LoggerFactory.getLogger(ChineseCharToEn.class);
    private static final int[] li_SecPosValue = new int[]{1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086, 4390, 4558, 4684, 4925, 5249, 5590};
    private static final String[] lc_FirstLetter = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "w", "x", "y", "z"};
    static Pattern p = Pattern.compile("\"([^\"]+)\"");

    public ChineseCharToEn() {
    }

    public static String hanyuToPinyin(String name) {
        String pinyinName = "";
        char[] nameChar = name.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        for(int i = 0; i < nameChar.length; ++i) {
            if (nameChar[i] > 128) {
                try {
                    pinyinName = pinyinName + PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];
                } catch (Exception var6) {
                    var6.printStackTrace();
                }
            }
        }

        return pinyinName;
    }

    public static String getAllFirstLetterForChinese(String str) {
        if (str != null && str.trim().length() != 0) {
            StringBuffer _str = new StringBuffer();

            for(int j = 0; j < str.length(); ++j) {
                String ch = str.substring(j, j + 1);
                if (Pattern.compile("([一-龥]+)").matcher(ch).find()) {
                    String py = hanyuToPinyin(ch);
                    _str.append(py.substring(0, 1));
                }
            }

            return _str.toString();
        } else {
            return "";
        }
    }

    /**
     * 获取汉字字符串的拼音首字母，如：String str = "哈哈哈haha"; return "hhhhaha"
     * @param str
     * @return
     */
    public static String getAllFirstLetter(String str) {
        if (str != null && str.trim().length() != 0) {
            String _str = "";

            for(int i = 0; i < str.length(); ++i) {
                _str = _str + getFirstLetter(str.substring(i, i + 1));
            }

            return _str;
        } else {
            return "";
        }
    }

    public static String getFirstLetter(String chinese) {
        if (chinese != null && chinese.trim().length() != 0) {
            chinese = conversionStr(chinese, "GB2312", "ISO8859-1");
            if (chinese.length() > 1) {
                int li_SectorCode = chinese.charAt(0);
                int li_PositionCode = chinese.charAt(1);
                li_SectorCode = li_SectorCode - 160;
                li_PositionCode = li_PositionCode - 160;
                int li_SecPosCode = li_SectorCode * 100 + li_PositionCode;
                if (li_SecPosCode > 1600 && li_SecPosCode < 5590) {
                    for(int i = 0; i < 23; ++i) {
                        if (li_SecPosCode >= li_SecPosValue[i] && li_SecPosCode < li_SecPosValue[i + 1]) {
                            chinese = lc_FirstLetter[i];
                            break;
                        }
                    }
                } else {
                    chinese = conversionStr(chinese, "ISO8859-1", "GB2312");
                    chinese = chinese.substring(0, 1);
                }
            }

            return chinese;
        } else {
            return "";
        }
    }

    private static String conversionStr(String str, String charsetName, String toCharsetName) {
        try {
            str = new String(str.getBytes(charsetName), toCharsetName);
        } catch (UnsupportedEncodingException var4) {
            logger.debug("字符串编码转换异常：" + var4.getMessage());
        }

        return str;
    }

    public static void main(String[] args) {
        System.out.println("获取拼音首字母：" + getAllFirstLetter("大中国南昌中大china"));
        System.out.println("获取拼音字母：" + hanyuToPinyin("大中国南昌中大china"));
    }
}
