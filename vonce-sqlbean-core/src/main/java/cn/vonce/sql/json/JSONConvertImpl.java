package cn.vonce.sql.json;

import java.util.List;

/**
 * 默认的JSON转换器
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2024/11/25 19:06
 */
public class JSONConvertImpl implements JSONConvert {

    @Override
    public Object parse(String json) {
        try {
            return JSONParser.parse(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T parseObject(String json, Class<T> clazz) {
        return JSONObject.parseObject(json, clazz);
    }

    @Override
    public <T> List<T> parseArray(String json, Class<T> clazz) {
        return JSONArray.parseArray(json, clazz);
    }

    @Override
    public String toJSONString(Object object) {
        return JSONParser.toJSONString(object);
    }

}
