package cn.myapps.core.dynaform.view.ejb.condition;

public interface ConditionVisitor {
	void visitColumnType(String columnType);

	void visitColumn(String column);

	void visitDateValue(String value, String datePattern);

	void visitStringValue(String value);

	void visitNumberValue(String value);
	
	void visitTextValue(String value);

	public void visitOperator(String operator);

	public String getConditions();
}
