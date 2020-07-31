package cn.vonce.sql.test;

import cn.vonce.sql.enumerate.GenerateType;
import cn.vonce.sql.processor.DefaultUniqueIdProcessor;
import cn.vonce.sql.processor.UniqueIdProcessor;

/**
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/2/28 10:19
 */
public class IdTest {

    public static void main(String[] args) {
        UniqueIdProcessor uniqueIdProcessor = new DefaultUniqueIdProcessor();
        System.out.println(uniqueIdProcessor.uniqueId(GenerateType.SNOWFLAKE_ID_16));
        System.out.println(uniqueIdProcessor.uniqueId(GenerateType.SNOWFLAKE_ID_18));
        System.out.println(uniqueIdProcessor.uniqueId(GenerateType.UUID));
    }

}
