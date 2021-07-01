package cn.vonce.sql.uitls;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * 字符串工具类
 */
public class StringUtil {

    /**
     * 占位符前缀: "${"
     */
    public static final String PLACEHOLDER_PREFIX = "${";
    /**
     * 占位符后缀: "}"
     */
    public static final String PLACEHOLDER_SUFFIX = "}";

    private final static String UNDERLINE = "_";

    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }

    public static boolean isBlank(Object str) {
        if (isEmpty(str)) {
            return true;
        }
        if (str instanceof CharSequence) {
            CharSequence cs = (CharSequence) str;
            int strLen;
            if (cs != null && (strLen = cs.length()) != 0) {
                for (int i = 0; i < strLen; ++i) {
                    if (!Character.isWhitespace(cs.charAt(i))) {
                        return false;
                    }
                }

                return true;
            } else {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotBlank(Object str) {
        return !isBlank(str);
    }

    /**
     * 生成UUID
     */
    public static String getUUid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成一个几位的随机数
     *
     * @param length 随机数的长度
     */
    public static String randomCode(int length) {
        String base = "1234567890";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 将一段文本的占位符替换 如：${name} to zhangsan
     *
     * @param text   要替换的文本
     * @param params map.key = name,map.value = zhangsan
     * @return 替换后的文本
     */
    public static String resolvePlaceholders(String text, Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return text;
        }
        StringBuffer buf = new StringBuffer(text);
        int startIndex = buf.indexOf(PLACEHOLDER_PREFIX);
        while (startIndex != -1) {
            int endIndex = buf.indexOf(PLACEHOLDER_SUFFIX, startIndex + PLACEHOLDER_PREFIX.length());
            if (endIndex != -1) {
                String placeholder = buf.substring(startIndex + PLACEHOLDER_PREFIX.length(), endIndex);
                int nextIndex = endIndex + PLACEHOLDER_SUFFIX.length();
                try {
                    String propVal = params.get(placeholder);
                    if (propVal != null) {
                        buf.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX.length(), propVal);
                        nextIndex = startIndex + propVal.length();
                    } else {
                        //LOG.warn("Could not resolve placeholder '" + placeholder + "' in [" + text + "] ");
                    }
                } catch (Exception ex) {
                    //LOG.warn("Could not resolve placeholder '" + placeholder + "' in [" + text + "]: " + ex);
                }
                startIndex = buf.indexOf(PLACEHOLDER_PREFIX, nextIndex);
            } else {
                startIndex = -1;
            }
        }
        return buf.toString();
    }

    /***
     * 下划线命名转为驼峰命名
     *
     * @param para
     *        下划线命名的字符串
     */

    public static String underlineToHump(String para) {
        StringBuilder result = new StringBuilder();
        String a[] = para.split(UNDERLINE);
        for (String s : a) {
            if (!para.contains(UNDERLINE)) {
                result.append(s);
                continue;
            }
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */

    public static String humpToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;//定位
        if (!para.contains(UNDERLINE)) {
            for (int i = 0; i < para.length(); i++) {
                if (Character.isUpperCase(para.charAt(i)) && (i - 1 > 0 ? Character.isLowerCase(para.charAt(i - 1)) : false)) {
                    sb.insert(i + temp, UNDERLINE);
                    temp += 1;
                }
            }
        }
        return sb.toString().toLowerCase();
    }

}
