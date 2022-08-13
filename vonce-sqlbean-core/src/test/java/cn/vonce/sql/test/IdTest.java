package cn.vonce.sql.test;

import cn.vonce.sql.enumerate.IdType;
import cn.vonce.sql.processor.DefaultUniqueIdProcessor;
import cn.vonce.sql.processor.UniqueIdProcessor;
import cn.vonce.sql.uitls.StringUtil;

/**
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/2/28 10:19
 */
public class IdTest {

    public static void main(String[] args) {
//        UniqueIdProcessor uniqueIdProcessor = new DefaultUniqueIdProcessor();
//        System.out.println(uniqueIdProcessor.uniqueId(IdType.SNOWFLAKE_ID_16));
//        System.out.println(uniqueIdProcessor.uniqueId(IdType.SNOWFLAKE_ID_18));
//        System.out.println(uniqueIdProcessor.uniqueId(IdType.UUID));
        String name = "VARCHAR(255,2) BIGINT NOT NULL";
        String[] values = name.split(" ");
        System.out.println(StringUtil.getBracketContent(values[0]));
        System.out.println(StringUtil.getWord(values[0]));
    }

}
