package cn.myapps.core.table.alteration;

import cn.myapps.core.table.model.Column;
import cn.myapps.core.table.model.Table;

/**
 * @author  nicholas
 */
public class ColumnDataTransferChange extends ModelChange {

	/**
	 * data source column
	 */
	private Column _sourceColumn;

	public ColumnDataTransferChange(Table _changedTable, Column _sourceColumn,
			Column _targetColumn) {
		this._table = _changedTable;
		this._sourceColumn = _sourceColumn;
		this._targetColumn = _targetColumn;
	}

	public Column getSourceColumn() {
		return _sourceColumn;
	}

	public String getErrorMessage(){
		return null;
	}
}
