package cn.myapps.core.report.oreport.ejb;


import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.ejb.IRunTimeProcess;
import cn.myapps.core.user.action.WebUser;

public interface OReportProcess extends IRunTimeProcess<Object> {
	
	/**
	 * 获得定制图表数据
	 * 
	 * @param jo json相关参数
	 * @param user 对应用户
	 * @return JSON字符串
	 * @throws Exception
	 */
	public Collection<Map<String, String>> getCustomizeOReportData(String viewid, String domainid, JSONObject xcolumn, JSONArray ycolumns, JSONArray filter, WebUser user,ParamsTable params) throws Exception;
	
	/**
	 * 获取过滤器项
	 * @param viewid 视图ID
	 * @param domainid 域ID
	 * @param xcolumn X轴
	 * @param ycolumns Y轴
	 * @param filter 过滤器JSONObject对象
	 * @param user 用户
	 * @return JSON字符串
	 * @throws Exception
	 */
	public String getFilterItems(String viewid, String domainid, JSONObject xcolumn, JSONArray ycolumns, JSONObject filter, WebUser user,ParamsTable params) throws Exception;
	
	/**
	 * 获得指定列最大值
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int getMaxColumnValue(String sql) throws Exception;
	
	/**
	 * 获取所有不重复并经过排序的值
	 * @param data
	 * @param yCount
	 * @return
	 */
	public List<String[]> getNoDupContent(Collection<Map<String, String>> data, JSONObject xCol, JSONArray yCols);
	
	/**
	 * 单个列构造成两个列
	 * @param xCol
	 * @param yCols
	 * @return
	 */
	public Map<String, Object> singleColumnHandle(JSONObject xCol, JSONArray yCols);
	
}
