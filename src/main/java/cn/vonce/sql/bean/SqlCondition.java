package cn.vonce.sql.bean;

import cn.vonce.sql.enumerate.SqlLogic;
import cn.vonce.sql.enumerate.SqlOperator;

import java.io.Serializable;

/**
 * Sql where条件形态
 * 
 * @author Jovi
 * @email 766255988@qq.com
 * @version 1.0
 * @date 2018年4月16日下午7:15:10
 */
public class SqlCondition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SqlCondition() {
		super();
	}

	public SqlCondition(String field, Object value) {
		super();
		this.field = field;
		this.value = value;
	}

	public SqlCondition(String field, Object value, String operator) {
		super();
		this.field = field;
		this.value = value;
		this.operator = operator;
	}

	public SqlCondition(SqlLogic sqlLogic, String field, Object value, String operator) {
		super();
		this.sqlLogic = sqlLogic;
		this.field = field;
		this.value = value;
		this.operator = operator;
	}

	public SqlCondition(String field, Object value, SqlOperator sqlOperator) {
		super();
		this.field = field;
		this.value = value;
		this.sqlOperator = sqlOperator;
	}

	public SqlCondition(SqlLogic sqlLogic, String field, Object value, SqlOperator sqlOperator) {
		super();
		this.sqlLogic = sqlLogic;
		this.field = field;
		this.value = value;
		this.sqlOperator = sqlOperator;
	}

	private String field;
	private Object value;
	private String operator;
	private SqlOperator sqlOperator;
	private SqlLogic sqlLogic;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public SqlOperator getSqlOperator() {
		return sqlOperator;
	}

	public void setSqlOperator(SqlOperator sqlOperator) {
		this.sqlOperator = sqlOperator;
	}

	public SqlLogic getSqlLogic() {
		return sqlLogic;
	}

	public void setSqlLogic(SqlLogic sqlLogic) {
		this.sqlLogic = sqlLogic;
	}

}
