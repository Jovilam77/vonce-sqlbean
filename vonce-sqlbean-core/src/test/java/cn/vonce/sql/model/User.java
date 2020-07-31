package cn.vonce.sql.model;

import cn.vonce.sql.annotation.SqlColumn;
import cn.vonce.sql.annotation.SqlId;
import cn.vonce.sql.annotation.SqlTable;

/**
 * 用户表 对应的实体类
 */
@SqlTable("d_user")
public class User {

    /**
     * id
     */
    @SqlId
    @SqlColumn(value = "id")
    private String id;
    /**
     * 账号
     */
    @SqlColumn(value = "username")
    private String username;
    /**
     * 头像
     */
    @SqlColumn(value = "headPortrait")
    private String headPortrait;
    /**
     * 昵称
     */
    @SqlColumn(value = "nickname")
    private String nickname;
    /**
     * 性别
     */
    @SqlColumn(value = "gender")
    private Integer gender;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", headPortrait='" + headPortrait + '\'' +
                ", nickname='" + nickname + '\'' +
                ", gender=" + gender +
                '}';
    }
}