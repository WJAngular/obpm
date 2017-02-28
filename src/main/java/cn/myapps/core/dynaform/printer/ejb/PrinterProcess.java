package cn.myapps.core.dynaform.printer.ejb;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;

/**
 * @author Happy
 *
 */
public interface PrinterProcess extends IDesignTimeProcess<Printer> {
	
	/**根据表单id获取所有字段
	 * @param formid
	 * @return 返回XML字符串
	 */
	public String getFields(String formid);
	
	
	/**
	 * 根据表单id获取所有视图列表XML文件
	 * @param formid
	 * @return
	 */
	public String getSubViews(String formid,IRunner runner);
	
	/**
	 * 
	 * @param formid
	 * @return
	 * @throws Exception 
	 */
	public String getReportData(String id,String _formid,String _docid,String _flowid,WebUser user,ParamsTable params) throws Exception;
	
	public Printer findByFormid(String formid) throws Exception;
	
	/**
	 * 获取 视图数据集合
	 * @param view
	 * @param parentId
	 * @param line
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public DataPackage<Document> getViewDatas(View view,int line,WebUser user,ParamsTable params) throws Exception;
	
	/**
	 * 根据模块id获取Print对象的集合
	 * @param moduleid 模块id
	 * @return Print对象的集合
	 * @throws Exception
	 */
	public Collection<Printer> getPrinterByModule(String moduleid) throws Exception;
	
	/**
	 * 删除多个打印对象
	 * 
	 * @param printerList
	 *            打印对象集合
	 * @throws Exception
	 */
	public abstract void doRemove(Collection<Printer> printerList)
			throws Exception;

}
