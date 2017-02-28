package cn.myapps.core.table.alteration;

import cn.myapps.core.table.model.Column;
import cn.myapps.core.table.model.Table;

/**
 * @author  nicholas
 */
public abstract class ModelChange {

	protected Table _table;

	protected Column _targetColumn;
	
	protected boolean stopOnError = true;

	public abstract String getErrorMessage();
	
	/**
	 * @return  the stopOnError
	 * @uml.property  name="stopOnError"
	 */
	public boolean isStopOnError() {
		return stopOnError;
	}

	public Table getTable() {
		return _table;
	}

	public Column getTargetColumn() {
		return _targetColumn;
	}
}
