package cn.vonce.sql.json;

import java.util.List;

/**
 * JSON转换接口
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2024/11/21 15:32
 */
public interface JSONConvert {

    Object parse(String json);

    <T> T parseObject(String json, Class<T> clazz);

    <T> List<T> parseArray(String json, Class<T> clazz);

    String toJSONString(Object object);

}
