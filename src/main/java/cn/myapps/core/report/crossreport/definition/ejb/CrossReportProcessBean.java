package cn.myapps.core.report.crossreport.definition.ejb;

import java.util.Collection;
import java.util.Iterator;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.report.crossreport.definition.dao.CrossReportDAO;
public class CrossReportProcessBean extends AbstractDesignTimeProcessBean<CrossReportVO>
implements CrossReportProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6604469541427510445L;

	protected IDesignTimeDAO<CrossReportVO> getDAO() throws Exception {
		return (CrossReportDAO) DAOFactory.getDefaultDAO(CrossReportVO.class.getName());
	}
	
	public String getCrossReportVO(String id) throws Exception {
		CrossReportVO crossReportVO = (CrossReportVO)doView(id);
		StringBuffer sb = new StringBuffer();
		if(crossReportVO!=null){
			sb.append("{\"id\":\"").append(crossReportVO.getId()).append("\",");
			sb.append("\"name\":\"").append(crossReportVO.getName()).append("\",");
			sb.append("\"description\":\"").append(crossReportVO.getNote()).append("\"}");
		}
		return sb.toString();
	}
	
	public String getAllCrossReportVO(String applicationid,String moduleid, String flag,String userid)
	throws Exception {
		StringBuffer sb = new StringBuffer();
		ParamsTable params = new ParamsTable();
		params.setParameter("t_applicationid", applicationid);
		params.setParameter("t_type", "CustomizeReport");
		if(userid != null && !userid.equals("")&& !userid.equals("null")){
			params.setParameter("t_userid", userid);
		}else{
			params.setParameter("t_userid", "null");
		}
		if(moduleid != null && !moduleid.equals("")&& !moduleid.equals("null")){
			params.setParameter("t_module", moduleid);
		}
		DataPackage<CrossReportVO> dataPackage = doQuery(params);
		if(dataPackage.rowCount>0){
			sb.append("{");
			sb.append("\"views\":[");
			for (Iterator<CrossReportVO> ite = dataPackage.datas.iterator(); ite.hasNext();){
				CrossReportVO crossReportVO = (CrossReportVO)ite.next();
				sb.append("{\"id\":\"").append(crossReportVO.getId()).append("\",");
				sb.append("\"label\":\"").append(crossReportVO.getName()).append("\",");
				sb.append("\"description\":\"").append(crossReportVO.getNote()).append("\",");
				sb.append("\"selected\":").append(flag).append("},");
			}
			if(sb.lastIndexOf(",")!=-1){
				sb.deleteCharAt(sb.lastIndexOf(","));
			}
			sb.append("]}");
		}else{
			sb.append("");
		}
		return sb.toString();
		}

	public Collection<CrossReportVO> getCrossReportByModule(String application,
			String module) throws Exception {
		return ((CrossReportDAO) getDAO()).getCrossReportByModule(application, module);
	}
	
	/**
	 * 获取软件下的自定义报表集合
	 * @param applicationId
	 * 		软件id
	 * @return
	 * @throws Exception
	 */
	public Collection<CrossReportVO> getCustomizeReportsByApplication(String applicationId) throws Exception {
		return ((CrossReportDAO) getDAO()).getCustomizeReportsByApplication(applicationId);
	}

	public DataPackage<CrossReportVO> doQueryCrossReportByModule(String moduleId,
			ParamsTable params, int page, int pagelines) throws Exception {
		return ((CrossReportDAO) getDAO()).queryCrossReportByModule(moduleId, params, page, pagelines);
	}
	
	}
