package cn.vonce.sql.model.union;

import cn.vonce.sql.annotation.SqlJoin;
import cn.vonce.sql.annotation.SqlUnion;
import cn.vonce.sql.enumerate.JoinType;
import cn.vonce.sql.model.Essay;
import cn.vonce.sql.model.User;

/**
 * 文章表 联表查询
 */
@SqlUnion
public class EssayUnion extends Essay {

    /**
     * 查询文章发布的用户对象
     */
    @SqlJoin(mainKeyword = "userId", isBean = true)
    private User user;

    /**
     * 查询文章的类别名称
     */
    @SqlJoin(value = "name", type = JoinType.LEFT_JOIN, table = "d_essay_category", tableKeyword = "id", mainKeyword = "categoryId")
    private String categoryName;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "EssayUnion{" +
                "user=" + user +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }

}
