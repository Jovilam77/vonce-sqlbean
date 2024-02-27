package cn.vonce.sql.model;

import cn.vonce.sql.annotation.SqlColumn;
import cn.vonce.sql.annotation.SqlDefaultValue;
import cn.vonce.sql.annotation.SqlId;
import cn.vonce.sql.annotation.SqlTable;
import cn.vonce.sql.enumerate.FillWith;

import java.util.Date;

/**
 * 文章表 对应的实体类
 */
@SqlTable("d_essay")
public class Essay {

    /**
     * id
     */
    @SqlId
    @SqlDefaultValue(with = FillWith.INSERT)
    @SqlColumn(value = "id")
    private String id;
    /**
     * 类别id
     */
    @SqlColumn(value = "categoryId")
    private Long categoryId;
    /**
     * 用户id
     */
    @SqlColumn(value = "userId" )
    private String userId;
    /**
     * 内容
     */
    @SqlColumn(value = "content" )
    private String content;
    /**
     * 创建时间
     */
    @SqlColumn(value = "creationTime" )
    private Date creationTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        return "Essay{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", content='" + content + '\'' +
                ", creationTime=" + creationTime +
                '}';
    }
}