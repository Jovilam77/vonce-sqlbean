package cn.vonce.sql.enumerate;

public enum JdbcType {

    NULL(0, 0, 0),

    TINYINT(1, 0, 0),

    SMALLINT(2, 0, 0),

    MEDIUMINT(3, 0, 0),

    INTEGER(4, 0, 0),

    BIGINT(5, 0, 0),

    LONG(6, 0, 0),

    FLOAT(7, 0, 0),

    DOUBLE(8, 0, 0),

    DECIMAL(9, 10, 2),

    NUMERIC(10, 10, 2),

    DATE(11, 0, 0),

    TIME(12, 0, 0),

    YEAR(13, 0, 0),

    DATETIME(14, 0, 0),

    DATETIME2(15, 0, 0),

    SMALLDATETIME(16, 0, 0),

    DATETIMEOFFSET(17, 0, 0),

    TIMESTAMP(18, 0, 0),

    CHAR(19, 255, 0),

    NCHAR(20, 255, 0),

    VARCHAR(21, 255, 0),

    VARCHAR2(22, 255, 0),

    NVARCHAR(23, 255, 0),

    TINYBLOB(24, 0, 0),

    TINYTEXT(25, 0, 0),

    BLOB(26, 0, 0),

    TEXT(27, 0, 0),

    NTEXT(28, 0, 0),

    MEDIUMBLOB(29, 0, 0),

    MEDIUMTEXT(30, 0, 0),

    LONGBLOB(31, 0, 0),

    LONGTEXT(32, 0, 0),

    RAW(33, 0, 0),

    LONGRAW(34, 0, 0),

    CLOB(35, 0, 0),

    NCLOB(36, 0, 0),

    BFILE(37, 0, 0),

    ROWID(38, 0, 0),

    NROWID(39, 0, 0),

    NUMBER(40, 0, 0),

    REAL(41, 10, 2),

    BINARY(42, 0, 0),

    VARBINARY(43, 0, 0),

    MONEY(44, 0, 0),

    SMALLMONEY(45, 0, 0),

    IMAGE(46, 0, 0),

    BIT(47, 1, 0);


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
        if (this.code == NUMERIC.code || this.code == DECIMAL.code || this.code == REAL.code ||
                this.code == FLOAT.code || this.code == DOUBLE.code) {
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
