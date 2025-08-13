package cn.vonce.sql.uitls;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Md5工具类
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2023/1/13 11:02
 */
public class Md5Util {

    public static String encode(String content) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 调用update方法计算MD5函数(参数：将密码串转换为操作系统的字节编码)
            md.update(content.getBytes());
            // digest()最后返回md5的hash值，返回值为8位的字符串，但此方法要先调用update
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值,数值从1开始
            // BigInteger会把0省略掉，需补全至32位，重写一个方法将16位数转换为32位数
            String md5 = new BigInteger(1, md.digest()).toString(16);
            return fillMD5(md5);
        } catch (Exception e) {
            throw new RuntimeException("MD5加密错误：" + e.getMessage(), e);
        }
    }

    // 将16位数转为32位
    private static String fillMD5(String md5) {
        return md5.length() == 32 ? md5 : fillMD5("0" + md5);
    }

}
