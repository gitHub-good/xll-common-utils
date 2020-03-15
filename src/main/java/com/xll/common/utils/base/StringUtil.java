package com.xll.common.utils.base;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil extends StringUtils {
    protected StringUtil() {
    }

    public static boolean zero(String inStr) {
        return null == inStr || inStr.length() <= 0;
    }

    public static boolean empty(String inStr) {
        return zero(inStr) || inStr.trim().equals("");
    }

    public static Boolean isNotBlank(Object obj) {
        Boolean flag = false;
        if (obj instanceof String) {
            if (isNotBlank((String)obj)) {
                flag = true;
            }
        } else if (null != obj) {
            flag = true;
        }

        return flag;
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static String toZeroSafe(String inStr) {
        return zero(inStr) ? "" : inStr;
    }

    public static String toZeroSafe(String inStr, String def) {
        return zero(inStr) ? def : inStr;
    }

    public static String toEmptySafe(String inStr) {
        return empty(inStr) ? "" : inStr;
    }

    public static String toEmptySafe(String inStr, String def) {
        return empty(inStr) ? def : inStr;
    }

    public static String trim(String inStr) {
        return empty(inStr) ? inStr : inStr.trim();
    }

    public static boolean equals(String s1, String s2) {
        return null == s1 ? false : s1.equals(s2);
    }

    public static boolean equalsIgnoreCase(String s1, String s2) {
        return null == s1 ? false : s1.equalsIgnoreCase(s2);
    }

    public static String toString(char[] array) {
        return String.valueOf(array);
    }

    public static String toString(Object obj) {
        return obj == null ? "" : String.valueOf(obj);
    }

    public static String[] changeObjectArray(Object[] objs) {
        String[] strs = new String[objs.length];
        System.arraycopy(objs, 0, strs, 0, objs.length);
        return strs;
    }

    public static String normalize(String str, String token, String delim) {
        if (empty(str)) {
            return "";
        } else {
            StringTokenizer tokenizer = new StringTokenizer(str, token);
            StringBuilder fixedBuilder = new StringBuilder();

            while(tokenizer.hasMoreTokens()) {
                if (fixedBuilder.length() == 0) {
                    fixedBuilder.append(tokenizer.nextToken());
                } else {
                    fixedBuilder.append(fixedBuilder);
                    fixedBuilder.append(delim);
                    fixedBuilder.append(token);
                    fixedBuilder.append(tokenizer.nextToken());
                }
            }

            if (str.indexOf(delim) == 0) {
                fixedBuilder.append(delim).append(token).append(fixedBuilder);
            }

            if (str.lastIndexOf(delim) == str.length() - 1) {
                fixedBuilder.append(fixedBuilder).append(delim).append(token);
            }

            return fixedBuilder.toString();
        }
    }

    public static String replace(String src, char charOld, String strNew) {
        if (null == src) {
            return src;
        } else if (null == strNew) {
            return src;
        } else {
            StringBuilder buf = new StringBuilder();
            int i = 0;

            for(int n = src.length(); i < n; ++i) {
                char c = src.charAt(i);
                if (c == charOld) {
                    buf.append(strNew);
                } else {
                    buf.append(c);
                }
            }

            return buf.toString();
        }
    }

    public static void replace(StringBuilder src, String strOld, String strNew) {
        if (null != src && src.length() > 0) {
            String s = replace(src.toString(), strOld, strNew);
            src.setLength(0);
            src.append(s);
        }
    }

    public static String replace(String src, String strOld, String strNew) {
        if (null == src) {
            return src;
        } else if (zero(strOld)) {
            return src;
        } else if (null == strNew) {
            return src;
        } else {
            return equals(strOld, strNew) ? src : src.replaceAll(strOld, strNew);
        }
    }

    public static String upperFirst(String s) {
        String str = null;
        if (null != s) {
            if (s.length() == 1) {
                str = s.toUpperCase();
            } else {
                str = s.substring(0, 1).toUpperCase() + s.substring(1);
            }
        }

        return str;
    }

    public static void upperFirst(StringBuilder sb) {
        if (null != sb && sb.length() > 0) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }

    }

    public static String lowerFirst(String s) {
        String str = null;
        if (null != s) {
            if (s.length() == 1) {
                str = s.toLowerCase();
            } else {
                str = s.substring(0, 1).toLowerCase() + s.substring(1);
            }
        }

        return str;
    }

    public static void lowerFirst(StringBuilder sb) {
        if (null != sb && sb.length() > 0) {
            sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        }

    }

    public static String getLastSuffix(String str, String delima) {
        if (zero(delima)) {
            return str;
        } else {
            String suffix = "";
            if (!zero(str)) {
                int index = str.lastIndexOf(delima);
                if (index >= 0) {
                    suffix = str.substring(index + delima.length());
                } else {
                    suffix = str;
                }
            }

            return suffix;
        }
    }

    public static String getLastPrefix(String src, String delima) {
        if (zero(delima)) {
            return src;
        } else {
            String prefix = "";
            if (!zero(src)) {
                int index = src.lastIndexOf(delima);
                if (index >= 0) {
                    prefix = src.substring(0, index);
                }
            }

            return prefix;
        }
    }

    public static String[] split(String str, String delim) {
        return !zero(str) && !zero(delim) ? str.split(delim) : new String[0];
    }

    public static String[] split(String str, String delim, boolean trim) {
        String[] set = split(str, delim);
        if (trim) {
            for(int i = 0; i < set.length; ++i) {
                set[i] = set[i].trim();
            }
        }

        return set;
    }

    public static String removeSequenceHeadingWordsIgnoreCase(String str, String[] words, String delim) throws Exception {
        if (!empty(str) && !ArrayUtil.empty(words)) {
            String[] set = split(str, delim);
            int setIndex = 0;

            for(int wordIndex = 0; setIndex < set.length && wordIndex < words.length; ++wordIndex) {
                String s = set[setIndex];
                String w = words[wordIndex];
                if (!empty(w)) {
                    if (!s.trim().equalsIgnoreCase(w.trim())) {
                        throw new Exception("no word '" + w + "' in the string '" + str + "' of index " + setIndex);
                    }

                    ++setIndex;
                }
            }

            return join(delim, setIndex, set);
        } else {
            return "";
        }
    }

    public static String join(String delim, int fromIndex, String... set) {
        if (null != set && set.length > 0 && fromIndex < set.length) {
            if (fromIndex < 0) {
                fromIndex = 0;
            }

            StringBuffer sb = new StringBuffer();
            sb.append(set[fromIndex]);

            for(int i = fromIndex + 1; i < set.length; ++i) {
                sb.append(delim);
                sb.append(set[i]);
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public static String join(String separator, Object... items) {
        String tmpKey = join(items, separator);
        tmpKey = subStrEndDiffStr(tmpKey, separator);
        tmpKey = subStrStartDiffStr(tmpKey, separator);
        tmpKey = replace(tmpKey, separator + "+", separator);
        return tmpKey;
    }

    public static StringBuilder replaceSpecialChar(String replaceContentSrc, String inputPrifx) {
        String oldReplaceContent = replaceContentSrc;
        StringBuilder builder = new StringBuilder();
        if (empty(replaceContentSrc)) {
            return builder;
        } else {
            String splitChar = new String("_");
            String replaceStrBegin = "<input type=\"text\" class=\"inputUnderLine2\" name=\"" + inputPrifx;
            String replaceStrMiddle = "\" id=\"" + inputPrifx + "Id";
            String replaceStrend = "\">&nbsp;&nbsp;&nbsp;";
            String beginChar = replaceContentSrc.substring(0, splitChar.length());
            if (equals(beginChar, splitChar)) {
                builder.append(replaceStrBegin + 0 + replaceStrMiddle + 0 + replaceStrend);
                oldReplaceContent = replaceContentSrc.substring(splitChar.length());
            }

            boolean flagReplace = false;
            String endChar = oldReplaceContent.substring(oldReplaceContent.length() - splitChar.length(), oldReplaceContent.length());
            if (equals(endChar, splitChar)) {
                oldReplaceContent = oldReplaceContent.substring(0, oldReplaceContent.length() - splitChar.length());
                flagReplace = true;
            }

            String[] splitStrs = split(oldReplaceContent, splitChar);
            int i;
            String q;
            if (flagReplace) {
                for(i = 0; i < splitStrs.length; ++i) {
                    q = splitStrs[i];
                    builder.append(q);
                    builder.append(replaceStrBegin + (i + 1) + replaceStrMiddle + (i + 1) + replaceStrend);
                }
            } else {
                for(i = 0; i < splitStrs.length; ++i) {
                    q = splitStrs[i];
                    builder.append(q);
                    if (i != splitStrs.length - 1) {
                        builder.append(replaceStrBegin + (i + 1) + replaceStrMiddle + (i + 1) + replaceStrend);
                    }
                }
            }

            return builder;
        }
    }

    public static int countStringNumber(String srcStr, String countStr) {
        int indexCount = 0;
        int index = 0;
        int count = 0;

        while(true) {
            index = srcStr.indexOf(countStr, indexCount);
            if (index == -1) {
                return count;
            }

            ++count;
            indexCount = index + countStr.length();
        }
    }

    public static String compareAddDiffArr(String[] arr1, String[] arr2) {
        String arr2Str = "";

        int j;
        for(j = 0; j < arr2.length; ++j) {
            if (j == 0) {
                arr2Str = arr2[j];
            } else {
                arr2Str = arr2Str + "," + arr2[j];
            }
        }

        arr2Str = arr2Str + ",";

        for(j = 0; j < arr1.length; ++j) {
            arr2Str = arr2Str.replace(arr1[j] + ",", "");
        }

        arr2Str = arr2Str.endsWith(",") ? arr2Str.substring(0, arr2Str.length() - 1) : arr2Str;
        return arr2Str;
    }

    public static String addStrDiffStr(String arr1, String arr2) {
        arr1 = arr1.replaceAll(" ", "");
        arr2 = arr2.replaceAll(" ", "");
        String[] arr1s = new String[0];
        String[] arr2s = new String[0];
        String str = "";
        if (arr1 != null && !arr1.equals("")) {
            arr1s = arr1.split(",");
        } else {
            arr1 = "";
        }

        if (arr2 != null && !arr2.equals("")) {
            arr2s = arr2.split(",");
        } else {
            arr2 = "";
        }

        String[] temp = new String[arr1s.length + arr2s.length];
        System.arraycopy(arr1s, 0, temp, 0, arr1s.length);
        System.arraycopy(arr2s, 0, temp, arr1s.length, arr2s.length);

        int i;
        int j;
        for(i = 0; i < temp.length; ++i) {
            if (temp[i] != "-1") {
                for(j = i + 1; j < temp.length; ++j) {
                    if (temp[i].equals(temp[j])) {
                        temp[j] = "-1";
                    }
                }
            }
        }

        i = 0;

        for(j = 0; j < temp.length && i < temp.length; ++j) {
            if (temp[i].equals("-1")) {
                --j;
            } else {
                str = str + temp[i];
                str = str + ", ";
            }

            ++i;
        }

        if (!str.equals("")) {
            str = str.replace("-1, ", "");
            str = str.replace(", -1", "");
            str = str.endsWith(", ") ? str.substring(0, str.length() - 2) : str;
        }

        return str;
    }

    public static String subStrEndDiffStr(String str, String ch) {
        str = toEmptySafe(str);
        ch = toEmptySafe(ch);
        return str.endsWith(ch) ? str.substring(0, str.length() - ch.length()) : str;
    }

    public static String subStrStartDiffStr(String str, String ch) {
        str = str.trim();
        ch = ch.trim();
        return str.startsWith(ch) ? str.substring(ch.length(), str.length()) : str;
    }

    public static String createUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String createSystemDataPrimaryKey() {
        return replace(createUUID(), "-", "");
    }

    public static String invertedCommaFilter(String source) {
        return source == null ? null : replace(source, "'", "\\'");
    }

    public static String fillSpacing(String source, String[] values, int level) {
        if (source.indexOf("{?}") != -1) {
            String source1 = source.substring(0, source.indexOf("{?}") + 3);
            source = replace(source1, "{?}", values[level]) + source.substring(source.indexOf("{?}") + 3, source.length());
            ++level;
            source = fillSpacing(source, values, level);
        }

        return source;
    }

    public static int getVarcharSpace(String str) {
        int space = 0;
        if (str != null && !str.equals("")) {
            for(int i = 0; i < str.length(); ++i) {
                if (str.substring(i, i + 1).matches("[一-龥]")) {
                    space += 12;
                } else {
                    space += 6;
                }
            }
        }

        return space;
    }

    public static String replaceXml(String xml) {
        String pat = "[\"\"](.*?)[\"\"]";
        String[] str = xml.split(pat);
        Pattern p = Pattern.compile(pat);
        Matcher m = p.matcher(xml);
        ArrayList values = new ArrayList();

        while(m.find()) {
            values.add(m.group().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
        }

        StringBuffer sf = new StringBuffer();

        for(int i = 0; i < str.length; ++i) {
            sf.append(str[i]);
            if (values.size() > i) {
                sf.append((String)values.get(i));
            }
        }

        return sf.toString();
    }

    public static String handelUrl(String url) {
        if (url == null) {
            return null;
        } else {
            url = url.trim();
            return !url.equals("") && !url.startsWith("http://") && !url.startsWith("https://") ? "http://" + url.trim() : url;
        }
    }

    public static String[] splitAndTrim(String str, String sep, String sep2) {
        if (StringUtils.isBlank(str)) {
            return null;
        } else {
            if (!StringUtils.isBlank(sep2)) {
                str = StringUtils.replace(str, sep2, sep);
            }

            String[] arr = StringUtils.split(str, sep);
            int i = 0;

            for(int len = arr.length; i < len; ++i) {
                arr[i] = arr[i].trim();
            }

            return arr;
        }
    }

    public static String txt2htm(String txt) {
        if (StringUtils.isBlank(txt)) {
            return txt;
        } else {
            StringBuilder sb = new StringBuilder((int)((double)txt.length() * 1.2D));
            boolean doub = false;

            for(int i = 0; i < txt.length(); ++i) {
                char c = txt.charAt(i);
                if (c == ' ') {
                    if (doub) {
                        sb.append(' ');
                        doub = false;
                    } else {
                        sb.append("&nbsp;");
                        doub = true;
                    }
                } else {
                    doub = false;
                    switch(c) {
                        case '\n':
                            sb.append("<br/>");
                            break;
                        case '"':
                            sb.append("&quot;");
                            break;
                        case '&':
                            sb.append("&amp;");
                            break;
                        case '<':
                            sb.append("&lt;");
                            break;
                        case '>':
                            sb.append("&gt;");
                            break;
                        default:
                            sb.append(c);
                    }
                }
            }

            return sb.toString();
        }
    }

    public static String textCut(String s, int len, String append) {
        if (s == null) {
            return null;
        } else {
            int slen = s.length();
            if (slen <= len) {
                return s;
            } else {
                int maxCount = len * 2;
                int count = 0;

                int i;
                for(i = 0; count < maxCount && i < slen; ++i) {
                    if (s.codePointAt(i) < 256) {
                        ++count;
                    } else {
                        count += 2;
                    }
                }

                if (i < slen) {
                    if (count > maxCount) {
                        --i;
                    }

                    if (!StringUtils.isBlank(append)) {
                        if (s.codePointAt(i - 1) < 256) {
                            i -= 2;
                        } else {
                            --i;
                        }

                        return s.substring(0, i) + append;
                    } else {
                        return s.substring(0, i);
                    }
                } else {
                    return s;
                }
            }
        }
    }

    public static String removeHtmlTagP(String inputString) {
        if (inputString == null) {
            return null;
        } else {
            String htmlStr = inputString;
            String textStr = "";

            try {
                String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
                String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
                String regEx_html = "<[^>]+>";
                Pattern p_script = Pattern.compile(regEx_script, 2);
                Matcher m_script = p_script.matcher(htmlStr);
                htmlStr = m_script.replaceAll("");
                Pattern p_style = Pattern.compile(regEx_style, 2);
                Matcher m_style = p_style.matcher(htmlStr);
                htmlStr = m_style.replaceAll("");
                htmlStr.replace("</p>", "\n");
                Pattern p_html = Pattern.compile(regEx_html, 2);
                Matcher m_html = p_html.matcher(htmlStr);
                htmlStr = m_html.replaceAll("");
                textStr = htmlStr;
            } catch (Exception var12) {
                var12.printStackTrace();
            }

            return textStr;
        }
    }

    public static String removeHtmlTag(String inputString) {
        if (inputString == null) {
            return null;
        } else {
            String htmlStr = inputString;
            String textStr = "";

            try {
                String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
                String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
                String regEx_html = "<[^>]+>";
                Pattern p_script = Pattern.compile(regEx_script, 2);
                Matcher m_script = p_script.matcher(htmlStr);
                htmlStr = m_script.replaceAll("");
                Pattern p_style = Pattern.compile(regEx_style, 2);
                Matcher m_style = p_style.matcher(htmlStr);
                htmlStr = m_style.replaceAll("");
                Pattern p_html = Pattern.compile(regEx_html, 2);
                Matcher m_html = p_html.matcher(htmlStr);
                htmlStr = m_html.replaceAll("");
                textStr = htmlStr;
            } catch (Exception var12) {
                var12.printStackTrace();
            }

            return textStr;
        }
    }

    public static boolean contains(String str, String search) {
        if (!StringUtils.isBlank(str) && !StringUtils.isBlank(search)) {
            String reg = StringUtils.replace(search, "*", ".*");
            Pattern p = Pattern.compile(reg);
            return p.matcher(str).matches();
        } else {
            return false;
        }
    }

    public static boolean containsKeyString(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        } else {
            return str.contains("'") || str.contains("\"") || str.contains("\r") || str.contains("\n") || str.contains("\t") || str.contains("\b") || str.contains("\f");
        }
    }

    public static String addCharForString(String str, int strLength, char c, int position) {
        int strLen = str.length();
        if (strLen < strLength) {
            while(strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                if (position == 1) {
                    sb.append(c).append(str);
                } else {
                    sb.append(str).append(c);
                }

                str = sb.toString();
                strLen = str.length();
            }
        }

        return str;
    }

    public static String replaceKeyString(String str) {
        return containsKeyString(str) ? str.replace("'", "\\'").replace("\"", "\\\"").replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t").replace("\b", "\\b").replace("\f", "\\f") : str;
    }

    public static String replaceString(String str) {
        return containsKeyString(str) ? str.replace("'", "\"").replace("\"", "\\\"").replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t").replace("\b", "\\b").replace("\f", "\\f") : str;
    }

    public static String getSuffix(String str) {
        int splitIndex = str.lastIndexOf(".");
        return str.substring(splitIndex + 1);
    }

    public static String stringFill(String source, int fillLength, char fillChar, boolean isLeftFill) {
        if (source != null && source.length() < fillLength) {
            StringBuilder result = new StringBuilder(fillLength);
            int len = fillLength - source.length();
            if (!isLeftFill) {
                result.append(source);

                while(len > 0) {
                    result.append(fillChar);
                    --len;
                }
            } else {
                while(len > 0) {
                    result.append(fillChar);
                    --len;
                }

                result.append(source);
            }

            return result.toString();
        } else {
            return source;
        }
    }

    public static void main(String[] args) {
        System.out.println(replaceKeyString("&nbsp;\r</p>"));
        System.out.println(join("-", "1", "", "3", "4", "5", "6", "7", "9"));
    }
}
