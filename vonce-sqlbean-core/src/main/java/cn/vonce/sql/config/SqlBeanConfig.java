package cn.vonce.sql.config;

import cn.vonce.sql.enumerate.JsonType;
import cn.vonce.sql.processor.DefaultUniqueIdProcessor;
import cn.vonce.sql.processor.UniqueIdProcessor;

import java.io.Serializable;

/**
 * SqlBean配置
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2018年6月12日下午2:48:12
 */
public class SqlBeanConfig implements Serializable {

    public SqlBeanConfig() {

    }

    private Boolean toUpperCase;
    private UniqueIdProcessor uniqueIdProcessor;
    private Boolean autoCreate;
    private JsonType jsonType;

    public Boolean getToUpperCase() {
        return toUpperCase;
    }

    public void setToUpperCase(Boolean toUpperCase) {
        if (this.toUpperCase == null) {
            this.toUpperCase = toUpperCase;
        }
    }

    public UniqueIdProcessor getUniqueIdProcessor() {
        if (uniqueIdProcessor == null) {
            uniqueIdProcessor = new DefaultUniqueIdProcessor();
        }
        return uniqueIdProcessor;
    }

    public void setUniqueIdProcessor(UniqueIdProcessor uniqueIdProcessor) {
        if (this.uniqueIdProcessor == null) {
            this.uniqueIdProcessor = uniqueIdProcessor;
        }
    }

    public boolean getAutoCreate() {
        if (autoCreate == null) {
            autoCreate = true;
        }
        return autoCreate;
    }

    public void setAutoCreate(boolean autoCreate) {
        if (this.autoCreate == null) {
            this.autoCreate = autoCreate;
        }
    }

    public JsonType getJsonType() {
        return jsonType;
    }

    public void setJsonType(JsonType jsonType) {
        if (this.jsonType == null) {
            this.jsonType = jsonType;
        }
    }

}
