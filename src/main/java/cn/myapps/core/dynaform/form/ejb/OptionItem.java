package cn.myapps.core.dynaform.form.ejb;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;

public interface OptionItem {
	
	/**
	 * 添加冗余字段数据
	 * 
	 * @param doc
	 *            Document 对象
	 * @param item 
	 * @param value
	 *            field值
	 * @return 创建ITEM
	 * 
	 */
	public Document createSystemOptionItem(Document doc, ParamsTable params, Item item);
	/**
	 * 根据系统冗余字段，获取相对应的显示值
	 * 
	 * @param doc
	 *            Document 对象
	 * @param value
	 *            field值
	 * @return 创建ITEM
	 * 
	 */
	public Object getValueFromSystemOptionItem(Document doc,Object Value);

}
