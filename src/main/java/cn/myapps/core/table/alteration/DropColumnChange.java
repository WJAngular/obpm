package cn.myapps.core.table.alteration;

import cn.myapps.core.table.model.Column;
import cn.myapps.core.table.model.Table;

/**
 * @author  nicholas
 */
public class DropColumnChange extends ModelChange {
	private Column _sourceColumn;
	
	public DropColumnChange(Table _changedTable, Column _sourceColumn) {
		this._table = _changedTable;
		this._sourceColumn = _sourceColumn;
	}

	public Column getSourceColumn() {
		return _sourceColumn;
	}

	public String getErrorMessage(){
		
		return "{*[core.form.alteration.column.hasdata]*}"+_sourceColumn;
	}
}
