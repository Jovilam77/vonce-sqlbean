package cn.vonce.sql.enumerate;


import cn.vonce.sql.uitls.StringUtil;

/**
 * Sql 排序
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2018年4月16日下午7:14:23
 */
public enum SqlSort {

    ASC, DESC;

    public static SqlSort get(String sort) {
        if (StringUtil.isEmpty(sort)) {
            return SqlSort.ASC;
        }
        if (SqlSort.ASC.name().equals(sort.toUpperCase())) {
            return SqlSort.ASC;
        }
        return SqlSort.DESC;
    }

}
