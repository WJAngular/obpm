package cn.myapps.core.table.alteration;

import cn.myapps.core.table.model.Column;
import cn.myapps.core.table.model.Table;

/**
 * 
 * @author nicholas
 * 
 */
public class AddColumnChange extends ModelChange {
	
	public AddColumnChange(Table _changedTable, Column _targetColumn) {
		this._table = _changedTable;
		this._targetColumn = _targetColumn;
	}

	public String getErrorMessage()  {
		 return "{*[core.form.alteration.column.name.exist]*}"+_targetColumn.getName();
	}
}
