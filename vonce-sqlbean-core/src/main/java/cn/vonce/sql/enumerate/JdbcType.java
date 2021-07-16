package cn.vonce.sql.enumerate;

/**
 * JdbcType枚举类
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 */
public enum JdbcType {

    NOTHING(0, 0, 0),

    TINYINT(1, 0, 0),

    SMALLINT(2, 0, 0),

    INTEGER(3, 0, 0),

    BIGINT(4, 0, 0),

    LONG(5, 0, 0),

    FLOAT(6, 0, 0),

    DOUBLE(7, 0, 0),

    DECIMAL(8, 10, 2),

    REAL(9, 10, 2),

    NUMERIC(10, 10, 2),

    MONEY(11, 10, 4),

    SMALLMONEY(12, 10, 4),

    DATE(13, 0, 0),

    TIME(14, 0, 0),

    DATETIME(15, 0, 0),

    DATETIME2(16, 0, 0),

    TIMESTAMP(17, 0, 0),

    CHAR(18, 255, 0),

    NCHAR(19, 255, 0),

    VARCHAR(20, 255, 0),

    VARCHAR2(21, 255, 0),

    NVARCHAR(22, 255, 0),

    TINYTEXT(23, 0, 0),

    TEXT(24, 0, 0),

    NTEXT(25, 0, 0),

    LONGTEXT(26, 0, 0),

    BIT(27, 0, 0),

    CLOB(28, 0, 0),

    NCLOB(29, 0, 0),

    BLOB(30, 0, 0),

    NBLOB(31, 0, 0);


    JdbcType(
            int code, int length, int decimal) {
        this.code = code;
        this.length = length;
        this.decimal = decimal;
    }

    private int code;
    private int length;
    private int decimal;


    public static JdbcType getType(int code) {
        for (JdbcType jdbcType : values()) {
            if (jdbcType.code == code) {
                return jdbcType;
            }
        }
        return null;
    }

    public static JdbcType getType(String name) {
        for (JdbcType jdbcType : values()) {
            if (jdbcType.name().equals(name)) {
                return jdbcType;
            }
        }
        return null;
    }

    public static String getTypeName(int code) {
        JdbcType jdbcType = getType(code);
        return jdbcType == null ? null : jdbcType.name();
    }

    public static int getCode(String name) {
        JdbcType jdbcType = getType(name);
        return jdbcType == null ? -1 : jdbcType.code;
    }

    public boolean isFloat() {
        if (this.code == NUMERIC.code || this.code == DECIMAL.code || this.code == FLOAT.code ||
                this.code == DOUBLE.code || this.code == MONEY.code || this.code == SMALLMONEY.code) {
            return true;
        }
        return false;
    }

    public int getCode() {
        return code;
    }

    public int getLength() {
        return length;
    }

    public int getDecimal() {
        return decimal;
    }

}
