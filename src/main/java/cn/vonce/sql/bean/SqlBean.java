package cn.vonce.sql.bean;//package cn.vonce.sql.bean;
//
//import cn.vonce.sql.config.SqlBeanConfig;
//
//import java.io.*;
//
///**
// * SqlBean
// *
// * @author Jovi
// * @version 1.0
// * @email 766255988@qq.com
// * @date 2017年8月18日上午9:00:19
// */
//public class SqlBean implements Serializable {
//
//    /**
//     *
//     */
//    private static final long serialVersionUID = 1L;
//
//    private SqlBean() {
//    }
//
//    private Select select = null;
//    private Update update = null;
//    private Delete delete = null;
//    private Insert insert = null;
//    private SqlBeanConfig sqlBeanConfig = new SqlBeanConfig();
//
//    public Select getSelect() {
//        return select;
//    }
//
//    protected void setSelect(Select select) {
//        this.select = select;
//    }
//
//    public Update getUpdate() {
//        return update;
//    }
//
//    protected void setUpdate(Update update) {
//        this.update = update;
//    }
//
//    public Delete getDelete() {
//        return delete;
//    }
//
//    protected void setDelete(Delete delete) {
//        this.delete = delete;
//    }
//
//    public Insert getInsert() {
//        return insert;
//    }
//
//    protected void setInsert(Insert insert) {
//        this.insert = insert;
//    }
//
//    public SqlBeanConfig getSqlBeanConfig() {
//        return sqlBeanConfig;
//    }
//
//    public void setSqlBeanConfig(SqlBeanConfig sqlBeanConfig) {
//        this.sqlBeanConfig = sqlBeanConfig;
//        if (sqlBeanConfig.getDbType() != null) {
//            if (select != null) {
//                this.select.setDbType(sqlBeanConfig.getDbType());
//            }
//            if (update != null) {
//                this.update.setDbType(sqlBeanConfig.getDbType());
//            }
//            if (delete != null) {
//                this.delete.setDbType(sqlBeanConfig.getDbType());
//            }
//        }
//    }
//
//    public static class Select extends cn.vonce.sql.bean.Select {
//
//        private static final long serialVersionUID = 1L;
//
//        @Override
//        public SqlBean build() {
//            SqlBean sqlBean = new SqlBean();
//            sqlBean.setSelect(this);
//            return sqlBean;
//        }
//    }
//
//    public static class Update extends cn.vonce.sql.bean.Update {
//
//        private static final long serialVersionUID = 1L;
//
//        @Override
//        public SqlBean build() {
//            SqlBean sqlBean = new SqlBean();
//            sqlBean.setUpdate(this);
//            return sqlBean;
//        }
//    }
//
//    public static class Delete extends cn.vonce.sql.bean.Delete {
//
//        private static final long serialVersionUID = 1L;
//
//        @Override
//        public SqlBean build() {
//            SqlBean sqlBean = new SqlBean();
//            sqlBean.setDelete(this);
//            return sqlBean;
//        }
//    }
//
//    public static class Insert extends cn.vonce.sql.bean.Insert {
//
//        private static final long serialVersionUID = 1L;
//
//        @Override
//        public SqlBean build() {
//            SqlBean sqlBean = new SqlBean();
//            sqlBean.setInsert(this);
//            return sqlBean;
//        }
//
//    }
//
//    /**
//     * 复制对象
//     *
//     * @return
//     * @throws IOException
//     * @throws ClassNotFoundException
//     * @author Jovi
//     * @date 2017年8月18日上午8:53:55
//     */
//    public Object copy() throws IOException, ClassNotFoundException {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(bos);
//        oos.writeObject(this);
//        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
//        return ois.readObject();
//    }
//
//
//}
