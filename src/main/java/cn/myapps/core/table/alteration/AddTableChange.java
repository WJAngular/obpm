package cn.myapps.core.table.alteration;

import cn.myapps.core.table.model.Table;

/**
 * 
 * @author nicholas
 * 
 */
public class AddTableChange extends ModelChange {

	public AddTableChange(Table _newTable) {
		this._table = _newTable;
	}

	public String getErrorMessage(){
		return "{*[core.form.alteration.table.name.exist]*}" + this._table.getName();
	}
	
}
