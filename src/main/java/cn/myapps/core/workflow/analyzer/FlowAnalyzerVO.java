package cn.myapps.core.workflow.analyzer;

import java.util.ArrayList;

import cn.myapps.base.dao.ValueObject;

public class FlowAnalyzerVO extends ValueObject {

	private static final long serialVersionUID = -5200865232095755165L;

	private ArrayList<AnalyzerField> resultFields;

	public ArrayList<AnalyzerField> getResultFields() {
		if (resultFields == null)
			resultFields = new ArrayList<AnalyzerField>();
		return resultFields;
	}

	public void setResultFields(ArrayList<AnalyzerField> resultFields) {
		this.resultFields = resultFields;
	}

	private ArrayList<GroupColumn> groupColumns;

	public ArrayList<GroupColumn> getGroupColumns() {
		if (groupColumns == null)
			groupColumns = new ArrayList<GroupColumn>();
		return groupColumns;
	}

	public String getGroupColumnValue(String columnName) {
		for (GroupColumn gc : getGroupColumns()) {
			if (gc.columnName != null
					&& gc.columnName.equalsIgnoreCase(columnName)) {
				return gc.value;
			}
		}
		return null;
	}

	public double getResultFieldValue(String fieldName) {
		for (AnalyzerField af : getResultFields()) {
			if (af.fieldName != null
					&& af.fieldName.equalsIgnoreCase(fieldName)) {
				return af.value;
			}
		}
		return 0.0;
	}

	public void setGroupColumns(ArrayList<GroupColumn> groupColumns) {
		this.groupColumns = groupColumns;
	}

	void addGroupColumn(String columnName, String value) {
		this.getGroupColumns().add(new GroupColumn(columnName, value));
	}

	void addResultField(String fieldName, double value) {
		this.getResultFields().add(new AnalyzerField(fieldName, value));
	}

	public static class GroupColumn {
		GroupColumn(String columnName, String value) {
			this.columnName = columnName;
			this.value = value;
		}

		public String columnName;
		public String value;
	}

	public static class AnalyzerField {
		AnalyzerField(String fieldName, double value) {
			this.fieldName = fieldName;
			this.value = value;
		}

		public String fieldName;
		public double value;
	}

}
