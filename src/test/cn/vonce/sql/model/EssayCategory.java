package cn.vonce.sql.model;

import cn.vonce.sql.annotation.SqlColumn;
import cn.vonce.sql.annotation.SqlConstant;
import cn.vonce.sql.annotation.SqlId;
import cn.vonce.sql.annotation.SqlTable;

import java.util.Date;

/**
 * 文章类别 对应的实体类表
 */
@SqlConstant
@SqlTable("d_essay_category")
public class EssayCategory {

    @SqlId
    @SqlColumn(value = "id")
    private Long id;

    @SqlColumn(value = "name")
    private String name;

    @SqlColumn(value = "creationTime")
    private Date creationTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        return "EssayCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creationTime=" + creationTime +
                '}';
    }
}
