package cn.myapps.core.report.oreport.action;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jfree.chart.JFreeChart;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.deploy.module.ejb.ModuleProcess;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.jfreechart.AreaChart;
import cn.myapps.core.jfreechart.BarChart;
import cn.myapps.core.jfreechart.ColumnChart;
import cn.myapps.core.jfreechart.JfreeChartUtil;
import cn.myapps.core.jfreechart.LineChart;
import cn.myapps.core.jfreechart.PieChart;
import cn.myapps.core.jfreechart.PlotChart;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.report.crossreport.definition.ejb.CrossReportProcess;
import cn.myapps.core.report.crossreport.definition.ejb.CrossReportVO;
import cn.myapps.core.report.oreport.ejb.OReportProcess;
import cn.myapps.core.report.oreport.ejb.OReportProcessBean;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;

import com.lowagie.text.pdf.AsianFontMapper;
import com.runqian.report4.dataset.DataSet;
import com.runqian.report4.dataset.Group;
import com.runqian.report4.dataset.Row;

public class OReportUtil {
	
	private final static String MNUMBER = "\"mfxarr\":[{\"label\":\"汇总\", \"data\":\"sum\"},{\"label\":\"最大值\", \"data\":\"max\"},{\"label\":\"最小值\", \"data\":\"min\"},{\"label\":\"平均数\", \"data\":\"avg\"},{\"label\":\"标准差\", \"data\":\"std\"},{\"label\":\"计数\", \"data\":\"count\"}]";
	private final static String MTEXT = "\"mfxarr\":[{\"label\":\"实际值\", \"data\":\"\"},{\"label\":\"计数\", \"data\":\"count\"}]";
	private final static String MDATE = "\"mfxarr\":[{\"label\":\"年\", \"data\":\"0\"},{\"label\":\"季度\", \"data\":\"1\"},{\"label\":\"季度&年\", \"data\":\"a0\"},{\"label\":\"月\", \"data\":\"2\"},{\"label\":\"月&年\", \"data\":\"a1\"},{\"label\":\"周\", \"data\":\"3\"},{\"label\":\"周&年\", \"data\":\"a2\"},{\"label\":\"工作日\", \"data\":\"4\"},{\"label\":\"全日期\", \"data\":\"date0\"},{\"label\":\"日\", \"data\":\"5\"},{\"label\":\"日期&时间\", \"data\":\"date1\"},{\"label\":\"小时\", \"data\":\"6\"}，{\"label\":\"计数\", \"data\":\"count\"}}]";
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

	
	/**
	 * 获得用户
	 * @return
	 * @throws Exception
	 */
	protected WebUser getAnonymousUser() throws Exception {
		UserVO vo = new UserVO();

		vo.getId();
		vo.setName("GUEST");
		vo.setLoginno("guest");
		vo.setLoginpwd("");
		vo.setRoles(null);
		vo.setEmail("");
		return new WebUser(vo);
	}

	/**
	 * 获取软件下所有企业
	 * @param applicationid
	 * @return
	 */
	public String getDomain(String applicationid){
		StringBuffer sb = new StringBuffer();
		try{
			ApplicationProcess ap = (ApplicationProcess)ProcessFactory.createProcess(ApplicationProcess.class);
			ApplicationVO apvo = (ApplicationVO)ap.doView(applicationid);
			if(apvo!=null && apvo.getDomains()!=null && apvo.getDomains().size()>0){
				sb.append("{\"domain\":[");
				sb.append("{\"label\":\"请选择\",\"id\":\"\"},");
				for (Iterator<DomainVO> itedvo = apvo.getDomains().iterator(); itedvo.hasNext();) {
					DomainVO dvo = (DomainVO)itedvo.next();
					sb.append("{\"label\":\"").append(dvo.getName()).append("\",\"id\":\"").append(dvo.getId()).append("\"},");
				}
				if(sb.toString().indexOf(",")>-1){
					sb.deleteCharAt(sb.lastIndexOf(","));
				}
				sb.append("]}");
				return sb.toString();
			}else{
				return "{\"icon\":\"assets/warning.png\",\"message\":\"没有企业域\"}";
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	public String getDepartmentByDomain(String domainid){
		try{
			StringBuffer sb = new StringBuffer();
			if(!StringUtil.isBlank(domainid)){
				DepartmentProcess dp = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
				Collection<DepartmentVO> departmentlist = dp.queryByDomain(domainid);
				if(!(departmentlist.size() >0)){
					return "{\"icon\":\"assets/warning.png\",\"message\":\"没部门\"}";
				}
				sb.append("{\"department\":[");
				sb.append("{\"label\":\"请选择\",\"id\":\"\"},");
				for (Iterator<DepartmentVO> itdpvo = departmentlist.iterator(); itdpvo.hasNext();) {
					DepartmentVO deo = (DepartmentVO)itdpvo.next();
					sb.append("{\"label\":\"").append(deo.getName()).append("\",\"id\":\"").append(deo.getId()).append("\"},");
				}
				if(sb.toString().indexOf(",")>-1){
					sb.deleteCharAt(sb.lastIndexOf(","));
				}
				sb.append("]}");
			}
			return sb.toString();
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 企业域下所有用户
	 */
	public String getUserByDepartment(String departmentid){
		try {
			StringBuffer sb = new StringBuffer();
			if(!StringUtil.isBlank(departmentid)){
				UserProcess up = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
				Collection<UserVO> userlist = up.queryByDepartment(departmentid);
				if(!(userlist.size() >0)){
					return "{\"icon\":\"assets/warning.png\",\"message\":\"没用户\"}";
				}
				sb.append("{\"user\":[");
				sb.append("{\"label\":\"请选择\",\"id\":\"\"},");
				for (Iterator<UserVO> ituservo = userlist.iterator(); ituservo.hasNext();) {
					UserVO deo = (UserVO)ituservo.next();
					sb.append("{\"label\":\"").append(deo.getName()).append("\",\"id\":\"").append(deo.getId()).append("\"},");
				}
				if(sb.toString().indexOf(",")>-1){
					sb.deleteCharAt(sb.lastIndexOf(","));
				}
				sb.append("]}");
			}
			return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	
	/**
	 * 软件编号获得该软件下所有模块
	 * @param applicationid
	 * @return
	 * @throws Exception
	 */
	public String getModuleByApplictionid(String applicationid,String moduleid){
		StringBuffer sb = new StringBuffer();
		String temp = "";
		Collection<ModuleVO> datas;
		try{
			ModuleProcess moduleProcess=(ModuleProcess) ProcessFactory.createProcess(ModuleProcess.class);
			if(moduleid != null && !moduleid.equals("") && !moduleid.equals("null")){
				datas = new ArrayList<ModuleVO>();
				ModuleVO moduleVO = (ModuleVO)moduleProcess.doView(moduleid);
				datas.add(moduleVO);
			}else{
				datas = moduleProcess.getModuleByApplication(applicationid);
			}
			if(datas.size()>0){
				sb.append("{\"module\":[");
				sb.append("{\"label\":\"请选择\",\"id\":\"\"},");
				for (Iterator<ModuleVO> iterator = datas.iterator(); iterator.hasNext();) {
					ModuleVO moduleVO = (ModuleVO)iterator.next();
						if(temp.equals("")){
							temp = moduleVO.getId();
						}
						sb.append("{\"label\":\"").append(moduleVO.getName()).append("\",\"id\":\"").append(moduleVO.getId()).append("\"},");
				}
				
				if(sb.toString().indexOf(",")>-1&&!temp.equals("")){
					sb.deleteCharAt(sb.lastIndexOf(","));
				}
				sb.append("]");
				sb.append("}");
			}
			return sb.toString();
		}catch(Exception e){
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""+e.getMessage()+"\"}";
		}
	}
	
	//视图公共部分
	protected String getViewCommon(String moduleid,String applicationid ){
		try{
			ViewProcess viewProcess=(ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			Collection<View> datas = viewProcess.getViewsByModule(moduleid, applicationid);
			if(datas.size()>0){
				StringBuffer sb = new StringBuffer();
				sb.append("\"view\":[");
				sb.append("{\"label\":\"请选择\",\"id\":\"\"},");
				for (Iterator<View> iterator = datas.iterator(); iterator.hasNext();) {
					View view = (View)iterator.next();
					sb.append("{\"label\":\"").append(view.getName()).append("\",");
//					sb.append("\"editMode\":\"").append(view.getEditMode()).append("\",");
//					sb.append("\"query\":\"").append(view.getEditModeType().getQueryString(new ParamsTable(), getAnonymousUser(), new Document())).append("\",");
					sb.append("\"id\":\"").append(view.getId()).append("\"},");
				}
				if(sb.toString().indexOf(",")>-1){
					sb.deleteCharAt(sb.lastIndexOf(","));
				}
				sb.append("]");
				return sb.toString();
			}else{
				return "";
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""+e.getMessage()+"\"}";
		}
	}
	
	/**
	 * 根据模块的编号获得该模块下所有的视图
	 * @param moduleid
	 * @return
	 * @throws Exception
	 */
	public String getViewByModuleid(String moduleid,String applicationid){
		StringBuffer sb = new StringBuffer();
		String str = getViewCommon(moduleid,applicationid);
		if(!StringUtil.isBlank(str)){
			sb.append("{");
			sb.append(str);
			sb.append("}");
		}
		return sb.toString();
	}
	

	/**
	 * 根据视图数组来获得列
	 * @param arr
	 * @return
	 * @throws Exception
	 */
	public String getViewColumnsByViewArray(String viewid) throws Exception{
		try{
			ViewProcess viewProcess=(ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			if(!StringUtil.isBlank(viewid)){
				View view = (View)viewProcess.doView(viewid);
				Collection<Column> valueFields = view.getColumns();
				if(valueFields.size()>0){
					StringBuffer sb = new StringBuffer();
					sb.append("{\"node\":[");
					for (Iterator<Column> iterator = valueFields.iterator(); iterator.hasNext();) {
						Column column = (Column) iterator.next();
						if(Column.COLUMN_TYPE_FIELD.equals(column.getType()) || Column.COLUMN_TYPE_SCRIPT.equals(column.getType())){
//						if (column != null && column.getType() != null && column.getType().equals("COLUMN_TYPE_FIELD") 
							sb.append("{");
							sb.append("\"label\":\"").append(column.getName()).append("\",");
							sb.append("\"value\":\"").append(column.getName()).append("\",");
							sb.append("\"selected\":").append(false).append(",");
							sb.append("\"fieldType\":\"").append(column.getFormField().getFieldtype()).append("\",");
							if(column.getType() != null
									&& column.getFormField() != null && column.getFormField().getFieldtype() != null 
									&& !column.getFormField().getFieldtype().equals("VALUE_TYPE_TEXT")) {
							if (column.getFormField().getFieldtype().equals("VALUE_TYPE_NUMBER")) {
								sb.append("\"icon\":\"assets/mxlist/number.png\",");
								sb.append("\"mfx\":\"sum\"");
							} else if (column.getFormField().getFieldtype().equals("VALUE_TYPE_DATE")) {
								sb.append("\"icon\":\"assets/mxlist/date.png\",");
								sb.append("\"mfx\":\"0\"");
							} else if (column.getFormField().getFieldtype().equals("VALUE_TYPE_VARCHAR")) {
								sb.append("\"icon\":\"assets/mxlist/text.png\",");
								sb.append("\"mfx\":\"\"");
							} else {
								sb.append("\"icon\":\"assets/mxlist/text.png\",");
								sb.append("\"mfx\":\"\"");
							}}else{
								sb.append("\"icon\":\"assets/mxlist/text.png\",");
								sb.append("\"mfx\":\"\"");
							}
							sb.append("},");
						}
					}
					if(sb.lastIndexOf(",")!=-1){
						sb.deleteCharAt(sb.lastIndexOf(","));
					}else{
						return "";
					}
					sb.append("]}");
					return sb.toString();
				}else{
					return "";
				}
			}else{
				return "";
			}
		}catch(Exception e){
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""+e.getMessage()+"\"}";
		}
	}

	/**
	 * 根据json数组来获得点击生成报表返回数据
	 *
	 * @param json Flex返回的JSON字符串
	 * @return 处理后的图表JSON
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static JFreeChart getCreateChar(String json,String chartType,ParamsTable params){
		//System.out.println("InfoJson\n"+json);
		try{
			if(!StringUtil.isBlank(json)){
				JSONObject jo = JSONObject.fromObject(json);
				
				String applicationid = jo.getString("applicationid");
				String viewid = jo.getString("viewid");
				String viewLabel = jo.getString("viewLabel");
				String domainid = params.getParameterAsString("domainid");
				String jsonData = params.getParameterAsString("jsonData");
				JSONArray data = new JSONArray();
				if(jsonData !=""){
					JSONArray jd = JSONArray.fromObject(jsonData);
					data = jd;
				}
				String userid = jo.getString("userid");
//				userid="11de-c13a-0cf76f8b-a3db-1bc87eaaad4c";
				JSONArray yColumns = null;
				JSONArray filters = null;
				
				JSONObject xColumn = jo.getJSONObject("xColumn");
				
				if(jo.containsKey("yColumn")){
					yColumns = jo.getJSONArray("yColumn");
				}
				
				if(jo.containsKey("fColumn")){
					filters = jo.getJSONArray("fColumn");
				}
				UserProcess up = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
				WebUser user = up.getWebUserInstance(userid);
				if(user==null){
					SuperUserProcess sup = (SuperUserProcess)ProcessFactory.createProcess(SuperUserProcess.class);
					user = sup.getWebUserInstance(userid);
				}
				OReportProcess op = new OReportProcessBean(applicationid);
//				List<Map<String, String>> data = (List<Map<String, String>>)op.getCustomizeOReportData(viewid, domainid, xColumn, yColumns, filters, user,params);
				Map<String, Object> map = op.singleColumnHandle(xColumn, yColumns);
				JSONObject xCol = (JSONObject)map.get("xCol");
				JSONArray yCols = (JSONArray)map.get("yCols");
				List<String[]> hashSet = op.getNoDupContent(data,xCol, yCols);
				if(chartType.equals("LineChart")){
					return LineChart.createChart(viewLabel, xCol, yCols, data, hashSet);
				}else if(chartType.equals("BarChart")){
					return BarChart.createChart(viewLabel, xCol, yCols, data, hashSet);
				}else if(chartType.equals("AreaChart")){
					return AreaChart.createChart(viewLabel, xCol, yCols, data, hashSet);
				}else if(chartType.equals("ColumnChart")){
					return ColumnChart.createChart(viewLabel, xCol, yCols, data, hashSet);
				}else if(chartType.equals("PlotChart")){
					return PlotChart.createChart(viewLabel, xCol, yCols, data, hashSet);
				}else if(chartType.equals("PieChart")){
					return PieChart.createChart(viewLabel, xCol, yCols, data, hashSet);
				}else{
					return null;
				}
			}else{
				return new JFreeChart(null);
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 将图表保存为pdf
	 * @param json
	 * @param chartType
	 * @return
	 */
	public String saveChartAsPDF(String json,String chartType,String filter,String domainid,String jsonData){
		try{
			JSONObject jo = JSONObject.fromObject(json);
			ParamsTable params = new ParamsTable();
			params.setParameter("domainid",domainid);
			params.setParameter("jsonData", jsonData);
			if(filter !=null && !filter.equals("")&& !filter.equals("null")){
				JSONObject jsonObject = JSONObject.fromObject(filter);
				for(int i=0;i<jsonObject.names().size();i++){
					String key = jsonObject.names().getString(i);
					if(jsonObject.get(key)!=null && !jsonObject.get(key).equals("") && !jsonObject.get(key).equals("null")){
						params.setParameter(key,jsonObject.get(key));
					}
				}

			}
			JfreeChartUtil.saveChartAsPDF(jo.getString("viewLabel"),getCreateChar(json,chartType,params), 800, 300, new AsianFontMapper("STSong-Light","UniGB-UCS2-H"));
			return "{\"name\":\""+jo.getString("viewLabel")+"\"}";
		}catch(Exception e){
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""+e.getMessage()+"\"}";
		}
	}
	
	
	/**
	 * 保存视图
	 * @param applicationid
	 * @param id
	 * @param name
	 * @param description
	 * @param type
	 * @param json
	 * @param operateJson
	 * @return
	 */
	public String doSaveViewVO(String applicationid,String moduleid,String id,String name,String description,String json,String userid){
		try{
			CrossReportVO crossReportVO = null;
			CrossReportProcess viewVOProcess=(CrossReportProcess) ProcessFactory.createProcess(CrossReportProcess.class);
			if(id==null || id.equals("")){
				crossReportVO = new CrossReportVO();
				id = UUID.randomUUID().toString();
				crossReportVO.setId(id);
				crossReportVO.setApplicationid(applicationid);
				crossReportVO.setModule(moduleid);
				crossReportVO.setName(name);
				crossReportVO.setNote(description);
				crossReportVO.setJson(json);
				crossReportVO.setType("CustomizeReport");
				if(userid !=null && !userid.equals("")){
					crossReportVO.setUserid(userid);
				}else{
					crossReportVO.setUserid("null");
				}
				viewVOProcess.doCreate(crossReportVO);
			}else{
				crossReportVO = (CrossReportVO)viewVOProcess.doView(id);
				if(name!=null&&!name.equals("")){
					crossReportVO.setName(name);
				}
				if(description!=null&&!description.equals("")){
					crossReportVO.setNote(description);
				}
				if(json!=null&&!json.equals("")){
					crossReportVO.setJson(json);
				}
				if(userid!=null&&!userid.equals("")){
					crossReportVO.setUserid(userid);
				}
				viewVOProcess.doUpdate(crossReportVO);
			}
			return "{\"icon\":\"assets/ok.png\",\"message\":\"报表 '"+crossReportVO.getName()+"' 保存成功\",\"viewVOid\":\""+id+"\"}";
		}catch(Exception e){
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""+e.getMessage()+"\"}";
		}
		
	}
	
	
	/**
	 * 获取所有视图
	 * @param applicationid
	 * @param flag
	 * @return
	 */
	public String getAllViewVO(String applicationid,String moduleid,String flag,String userid){
		try{
			CrossReportProcess viewVOProcess=(CrossReportProcess) ProcessFactory.createProcess(CrossReportProcess.class);
			return viewVOProcess.getAllCrossReportVO(applicationid,moduleid, flag,userid);
		}catch(Exception e){
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""+e.getMessage()+"\"}";
		}
	}
	
	
	
	//获得创建图表的JSON
	public String getCreateCharJson(String id){
		try{
			CrossReportProcess viewVOProcess=(CrossReportProcess) ProcessFactory.createProcess(CrossReportProcess.class);
			return ((CrossReportVO)viewVOProcess.doView(id)).getJson();
		}catch(Exception e){
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""+e.getMessage()+"\"}";
		}
	}
	
	
	/**
	 * 根据id获得视图
	 * @param id
	 * @return
	 */
	public String getViewVO(String id){
		try{
			CrossReportProcess viewVOProcess=(CrossReportProcess) ProcessFactory.createProcess(CrossReportProcess.class);
			return viewVOProcess.getCrossReportVO(id);
		}catch(Exception e){
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""+e.getMessage()+"\"}";
		}
	}
	
	
	/**
	 * 删除视图
	 * @param id
	 * @return
	 */
	public String deleteViewVO(String id){
		try{
			CrossReportProcess viewVOProcess=(CrossReportProcess) ProcessFactory.createProcess(CrossReportProcess.class);
			CrossReportVO viewvo = (CrossReportVO)viewVOProcess.doView(id);
			viewVOProcess.doRemove(viewvo);
			return "{\"icon\":\"assets/ok.png\",\"message\":\"报表 '"+viewvo.getName()+"' 删除成功\"}";
		}catch(Exception e){
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""+e.getMessage()+"\"}";
		}
	}
	
	
	/**
	 * 删除多个视图
	 * @param id
	 * @return
	 */
	public String deleteViewVO(String[] ids){
		try{
			CrossReportProcess viewVOProcess=(CrossReportProcess) ProcessFactory.createProcess(CrossReportProcess.class);
			for(int i =0;i<ids.length;i++){
				viewVOProcess.doRemove(ids[i]);
			}
			return "{\"icon\":\"assets/ok.png\",\"message\":\"报表删除成功\"}";
		}catch(Exception e){
			e.printStackTrace();
			return "{\"icon\":\"assets/warning.png\",\"message\":\""+e.getMessage()+"\"}";
		}
	}
	
	
	/**
	 * 获取自定义报表的数据集
	 * @param viewid
	 * 		视图Id
	 * @param userid
	 * 		用户Id
	 * @param json
	 * 		报表模版
	 * @param filter
	 * 		过滤信息
	 * @param page
	 * 		当前页码
	 * @return
	 * 	返回JSON格式的字符串
	 */
	public static String getDataSet(String viewid,String userid,String json,String filter,int page){
		if(!StringUtil.isBlank(userid)){
			try{
				UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
				UserVO user = (UserVO) userProcess.doView(userid);
				WebUser webUser = new WebUser(user);
				return getDataSet(viewid, webUser, json, filter, page);
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 获取自定义报表的数据集
	 * @param viewid
	 * 		视图Id
	 * @param webUser
	 * 		用户
	 * @param json
	 * 		报表模版
	 * @param filter
	 * 		过滤信息
	 * @param page
	 * 		当前页码
	 * @return
	 * 	返回JSON格式的字符串
	 */
	public static String getDataSet(String viewid,WebUser webUser,String json,String filter,int page){
		DataSet ds = new DataSet("ds1");
		List<Map<String, String>> data = new ArrayList<Map<String,String>>();
		List<List<Map<String,String>>> mainList = new ArrayList<List<Map<String,String>>>();
		List<String> xAxisList = new ArrayList<String>();
		ParamsTable params = new ParamsTable();
		View view = null;
		
		if(!StringUtil.isBlank(viewid)){
			try{
				ViewProcess viewprocess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
				view = (View) viewprocess.doView(viewid);
			}catch(Exception e2){
				e2.printStackTrace();
			}
		}
		JSONArray yColumns = null;
		JSONObject xColumn = null;
		if(!StringUtil.isBlank(json)){
			JSONObject jo = JSONObject.fromObject(json);
			
			xColumn = jo.getJSONObject("xColumn");
			
			if(jo.containsKey("yColumn")){
				yColumns = jo.getJSONArray("yColumn");
			}
		}
		if(!StringUtil.isBlank(filter) ){
			JSONObject jsonObject = JSONObject.fromObject(filter);
			for(int i=0;i<jsonObject.names().size();i++){
				String key = jsonObject.names().getString(i);
				if(jsonObject.get(key)!=null && !jsonObject.get(key).equals("") && !jsonObject.get(key).equals("null")){
					params.setParameter(key,jsonObject.get(key));
				}
			}
		}
		if(view != null){
			Document searchDocument = null;
			if (view.getSearchForm() != null) {
				try {
					searchDocument = view.getSearchForm().createDocument(params, webUser);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				searchDocument = new Document();
			}
			try {
				int line = 0;
				if(page == 0){
					line = 18000;
				}else{
					line = 100;
				}
				int k = 0;
				DataPackage<Document> datas = null;
				do{
					ds=new DataSet("ds1");
					for (Iterator<Column> iter = view.getColumns().iterator(); iter.hasNext();) {
						Column column = (Column) iter.next();
						ds.addCol(column.getName());// 设置数据集的字段
					}
					datas = view.getViewTypeImpl().getViewDatasPage(params, k+1,line , webUser, searchDocument);
					for (Iterator<Document> iter = datas.datas.iterator(); iter.hasNext();) {
						Document doc = (Document) iter.next();
						IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), view.getApplicationid());
						try {
							runner.initBSFManager(doc, params, webUser,new ArrayList<ValidateMessage>());
						} catch (Exception e) {
							e.printStackTrace();
						}
						Row rr = ds.addRow();
						int j = 0;
						Iterator<Column> iter1 = view.getColumns().iterator();
						while (iter1.hasNext()) {
							Column col = (Column) iter1.next();
							String result = null;
							try {
								result = (String) col.getTextString(doc, runner,webUser);
							} catch (Exception e) {
								e.printStackTrace();
							}
							rr.setData(j + 1, result);
							j++;
						}
					}
					mainList = createMap(xColumn, yColumns ,ds ,mainList,xAxisList);
				}while(page == 0 && datas!= null && datas.rowCount >= (k+1)*line);
				
				for(int listsize = 0;listsize<mainList.size();listsize++){
					data.add(mainList.get(listsize).get(0));
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					PersistenceUtils.closeSessionAndConnection();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return JsonUtil.collection2Json(data);
	}
	
	/**
	 * 创建显示数据
	 * @param x
	 * @param y
	 */
	public static List<List<Map<String, String>>> createMap(JSONObject x,JSONArray y,DataSet ds,List<List<Map<String,String>>> mainList,List<String> xAxisList){
		if(ds != null && ds.getColCount()>0){
			String xx = x.getString("label");
			int i = ds.getColNo(xx); // dataset x轴的列数
			Group g = ds.getRootGroup();
			int rn = g.getRowCount();
			for(int j = 0 ;j<rn;j++){
				Map<String,List<Map<String, String>>> oneMap = new LinkedHashMap<String, List<Map<String, String>>>();
				List<Map<String, String>> twoList = new ArrayList<Map<String,String>>();
				Map<String,String>  twoMap = new LinkedHashMap<String, String>();
				String xA = (String) ds.getData(j+1,i);  //获取第j行x轴数据
				String xResult = "";
				if(x.getString("fieldtype").equals("VALUE_TYPE_DATE")){
					String xtype = x.getString("fx");
					if(!StringUtil.isBlank(xA)){
						//处理日期类型的显示形式
						xResult = dateResult(xA,xtype);
					}else{
						xResult = "";
					}
					
				}else{
					xResult = xA;
				}
				twoMap.put("xAxis", xResult);
				for(int k = 0;k<y.size();k++){
					String yy = y.getJSONObject(k).getString("label");
					int yCol = ds.getColNo(yy);     //y轴列数
					String yA = (String) ds.getData(j+1,yCol);
					twoMap.put("yAxis"+k, yA);
				}
				if(!xAxisList.contains(xResult)){    //判断xlist集合中是否有该参数 有则加入队列 没有则新建队列
					twoList.add(twoMap);
					oneMap.put(xA, twoList);
					mainList.add(twoList);
					xAxisList.add(xResult);
				}else{					
					for(int n=0;n<xAxisList.size() ;n++){
						if(xAxisList.get(n).equals(xResult)){
							twoList = mainList.get(n);
							twoList.add(twoMap);
						}
					}
				}
			}
			//循环统计数据
			mainList = createData(mainList,xAxisList,y);
		}
		return mainList;
	}
	
	//处理日期类型的显示形式
	public static String dateResult(String xA,String xtype){
		String result = "";
		Date d = new Date();
		try{
			d = DateFormat.getDateInstance().parse(xA);
//			d = DateUtil.parseDateTime(xA);
		}catch (Exception e) {
			System.out.println("Unparseable date: " + xA);
		}
		SimpleDateFormat sdf = null;
		SimpleDateFormat sdf2 =null;
		if(xtype.startsWith("date")){
			int type = Integer.parseInt(xtype.substring(4));
			switch(type){
				case 0: 
					sdf = new SimpleDateFormat("yyyy-MM-dd");
					break;
				case 1:
					sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					break;
				case 2:
					sdf = new SimpleDateFormat("yyyy-MM");
					break;
				default:
					sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					break;
			}
			result = sdf.format(d);
		}else if(xtype.startsWith("a")){
			int type = Integer.parseInt(xtype.substring(1));
			switch(type){
				case 0: //季度年
				case 1: //月年
					sdf = new SimpleDateFormat("yyyy");
					sdf2 = new SimpleDateFormat("MM");
					break;
				case 2: //周年
					sdf = new SimpleDateFormat("yyyy");
					sdf2 = new SimpleDateFormat("ww");
					break;
			}
			String time = sdf.format(d);   //年
			String time2 = sdf2.format(d);  //季度，月，周
			int year = Integer.parseInt(time);
			int other = Integer.parseInt(time2);
			switch(type){
				case 0: //季度年
					int quarter = (other+2)/3;
					result = String.valueOf(quarter + (year*4));
					break;
				case 1: //月年
					result = String.valueOf(other + (year*12));
					break;
				case 2: //周年
					result = String.valueOf(other + (year*53));
					break;
			}
		}else {
			int type = Integer.parseInt(xtype);
			switch(type){
				case 0:  //年
					sdf = new SimpleDateFormat("yyyy");
					break;
				case 1:  //季度
					sdf = new SimpleDateFormat("MM");
					break;
				case 2:  //月
					sdf = new SimpleDateFormat("MM");
					break;
				case 3:  //周
					sdf = new SimpleDateFormat("ww");
					break;
				case 4:  //日
					sdf = new SimpleDateFormat("DD");
					break;
				case 5:  //小时
					sdf = new SimpleDateFormat("HH");
					break;
			}
			String time = sdf.format(d);
			if(type == 1){
				int quarter = Integer.parseInt(time);
				result = String.valueOf(((quarter+2)/3));
			}else{
				result = time;
			}
		}
		return result;
	}
	
	/**
	 * 组合数据
	 * @param oneList
	 * @param xlist
	 * @param y
	 */
	public static List<List<Map<String, String>>> createData(List<List<Map<String, String>>> oneList,List<String> xlist,JSONArray y){
		List<List<Map<String, String>>> newOneList = new ArrayList<List<Map<String,String>>>();   
		String ytype = "";
		for(int i = 0;i<oneList.size();i++){
			List<Map<String, String>> list = new ArrayList<Map<String,String>>();
			List<Map<String,String>> twoList = oneList.get(i);
			Map<String,String> map = new LinkedHashMap<String, String>();
			map.put("xAxis",xlist.get(i));
			for(int j=0;j<y.size();j++){
				JSONObject yn = (JSONObject) y.get(j);
				ytype = yn.getString("fx");
				double b = 0;
				if(ytype.equals("max") || ytype.equals("min")){
					b = maxAndMin(twoList,j,ytype);
				}else if(ytype.equals("sum") || ytype.equals("avg")){
					b = sumAndAvg(twoList,j,ytype);
				}else if(ytype.equals("std")){
					b = std(twoList, j, ytype);
				}else{
					int size = twoList.size();
					map.put("yAxis"+j, Integer.toString(size));
					continue;
				}
				map.put("yAxis"+j, DECIMAL_FORMAT.format(b));
			}
			list.add(map);
			newOneList.add(list);
		}
		return newOneList;
	}
	
	/**
	 * 数据计算
	 * @param twoList
	 * @param number
	 * @param type
	 */
	public static double sumAndAvg(List<Map<String,String>> twoList,int number,String type){
		String yCol = "yAxis"+number;
		List<Double> dList = new ArrayList<Double>();
		double result = 0;
		double sum = 0;
		double avg = 0;
		for(int i=0;i<twoList.size();i++){
			Map<String,String> twoMap = twoList.get(i);
			String parseString = twoMap.get(yCol);
			if(StringUtil.isNumber(parseString)){
				double yn = Double.parseDouble(twoMap.get(yCol));
				dList.add(yn);
				sum +=yn;
			}
		}if(type.equals("sum")){
			result = sum;
		}else{
			avg = sum / twoList.size();
			result = avg;
		}
		return result;
	}
	
	//最大值最小值
	public static double maxAndMin(List<Map<String,String>> twoList,int number,String type){
		String yCol = "yAxis"+number;
		double result = 0;
		for(int i=0;i<twoList.size();i++){
			Map<String,String> twoMap = twoList.get(i);
			String parseString = twoMap.get(yCol);
			if(StringUtil.isNumber(parseString)){
				double yn = Double.parseDouble(twoMap.get(yCol));
				if(type.equals("max")){
					if(yn > result){
						result = yn;
					}
				}else if(type.equals("min")){
					if( i==0 || yn < result){
						result = yn;
					}
				}
			}
		}
		return result;
	}
	
	//标准差
	public static double std(List<Map<String,String>> twoList,int number,String type){
		String yCol = "yAxis"+number;
		List<Double> dList = new ArrayList<Double>();
		double result = 0;
		double sum = 0;
		double avg = 0;
		double std = 0;
		for(int i=0;i<twoList.size();i++){
			Map<String,String> twoMap = twoList.get(i);
			String parseString = twoMap.get(yCol);
			if(StringUtil.isNumber(parseString)){
				double yn = Double.parseDouble(twoMap.get(yCol));
				dList.add(yn);
				sum +=yn;
			}
		}
		avg = sum / twoList.size();
		for(int j=0;j<dList.size();j++){
			double d = dList.get(j);
			if(avg >d){
				std += (avg - d)*(avg - d);
			}else{
				std += (d - avg)*(d - avg);
			}
		}
		result = Math.sqrt(std/dList.size());
		return result;
	}
	
	public static String getDataPageNumber(String viewid,String userid) throws Exception{
//		System.out.println("number Waste Time: " + (System.currentTimeMillis()) + "(ms)");
		WebUser webUser = null;
		View view = null;
		long number =0;
		if(!StringUtil.isBlank(userid)){
			try{
				UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
				UserVO user = (UserVO) userProcess.doView(userid);
				webUser = new WebUser(user);
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
		if(!StringUtil.isBlank(viewid)){
			try{
				ViewProcess viewprocess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
				view = (View) viewprocess.doView(viewid);
			}catch(Exception e2){
				e2.printStackTrace();
			}
		}
		if(view !=null){
			Document searchDocument = null;
			if (view.getSearchForm() != null) {
				try {
					searchDocument = view.getSearchForm().createDocument(new ParamsTable(), webUser);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				searchDocument = new Document();
			}
			number = view.getViewTypeImpl().countViewDatas(new ParamsTable(),webUser, searchDocument); 
		}
		
		return "{\"number\":\""+number+"\",\"lines\":\"18000\"}";
	}
	
	public static void main(String[] agrs) {
		String viewid = "11e1-70fb-29b38b22-a6e0-cb1a55305004";
		String userid = "11de-c13a-0cf76f8b-a3db-1bc87eaaad4c";
		String domainid = "11de-c138-782d2f26-9a62-8bacb70a86e1";
//		getDataSet(viewid,userid,domainid);
	}
	
	
}
