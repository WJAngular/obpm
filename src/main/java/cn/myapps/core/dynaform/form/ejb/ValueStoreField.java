package cn.myapps.core.dynaform.form.ejb;

import java.util.Map;

import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;

/**
 * ValueStoreField 需要保存数据到数据库的域
 * 
 * @author nicholas
 * 
 */
public interface ValueStoreField {
	/**
	 * 获取Field的类型
	 * 
	 * @return Field的类型
	 */
	public String getFieldtype();

	/**
	 * 根据Form模版的组件内容结合Document中的ITEM存放的值,输出重定义后的html文本以网格显示
	 * 
	 * @param runner
	 *            动态脚本执行器
	 * @param doc
	 *            文档对象
	 * @param webUser
	 *            webUser
	 * @param columnOptionsCache           
	 *             缓存控件选项(选项设计模式构建的Options)的Map
	 *             key: fieldId
	 *             value: Options
	 * @return 重定义后的html文本
	 */
	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception;
	
}
