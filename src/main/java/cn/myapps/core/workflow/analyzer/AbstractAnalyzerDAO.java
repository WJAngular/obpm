package cn.myapps.core.workflow.analyzer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;





import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.km.util.StringUtil;
import cn.myapps.util.DateUtil;

public abstract class AbstractAnalyzerDAO  implements AnalyzerDAO {

	Logger log = Logger.getLogger(AbstractAnalyzerDAO.class);

	protected Connection connection;

	protected String schema;

	protected String DBType = "Oracle :";// 标识数据库类型

	public AbstractAnalyzerDAO(Connection connection) {
		this.connection = connection;
	}

	public void create(ValueObject vo) throws Exception {
		throw new Exception("Method is not implemented.");

	}

	public void remove(String pk) throws Exception {
		throw new Exception("Method is not implemented.");

	}

	public void update(ValueObject vo) throws Exception {
		throw new Exception("Method is not implemented.");

	}

	public ValueObject find(String id) throws Exception {
		throw new Exception("Method is not implemented.");
	}

	// ///////////////////////////////////////////////

	/**
	 * 流程实例占比统计OK
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Collection<FlowAnalyzerVO> analyzerFlowAccounting(
			ParamsTable params, String dateRangeType, String showMode,
			WebUser user) throws Exception {

		String extCondition = "ALL".equalsIgnoreCase(showMode) ? ""
				: " AND AUDITOR='" + user.getId() + "'";

		String sql = "SELECT FLOWID,FLOWNAME,APPLICATIONID, COUNT(*) AMOUNT FROM "
				+ getFullTableName("T_RELATIONHIS")
				+ " WHERE ACTIONTIME >= ? AND ACTIONTIME <?"
				+ extCondition
				+ " GROUP BY FLOWID,FLOWNAME,APPLICATIONID";

		Collection<FlowAnalyzerVO> rtn = new ArrayList<FlowAnalyzerVO>();
		PreparedStatement statement = null;

		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);

			statement.setTimestamp(1, getDateRangeBegin(dateRangeType));
			statement.setTimestamp(2, getDateRangeEnd(dateRangeType));

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				FlowAnalyzerVO vo = new FlowAnalyzerVO();
				// 流程ID
				vo.addGroupColumn("FLOWID", rs.getString("FLOWID"));
				// 流程名称
				vo.addGroupColumn("FLOWNAME", rs.getString("FLOWNAME"));
				// 统计值
				vo.addResultField("AMOUNT", rs.getDouble("AMOUNT"));
				vo.setApplicationid(rs.getString("APPLICATIONID"));
				rtn.add(vo);
			}

			return rtn;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	/**
	 * 流程耗时占比
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public abstract Collection<FlowAnalyzerVO> analyzerFlowTimeConsumingAccounting(
			ParamsTable params, String dateRangeType, String showMode,
			WebUser user) throws Exception;

	/**
	 * 流程&节点耗时统计
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public abstract Collection<FlowAnalyzerVO> analyzerFlowAndNodeTimeConsuming(
			ParamsTable params, String dateRangeType, String showMode,
			WebUser user) throws Exception;

	/**
	 * 处理人耗时统计，TOP-X
	 * 
	 * @param params
	 * @param top
	 * @return
	 * @throws Exception
	 */
	public abstract Collection<FlowAnalyzerVO> analyzerActorTimeConsumingTopX(
			ParamsTable params, String dateRangeType, int top, String showMode,
			WebUser user) throws Exception;

	protected String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}

	protected void setProperties(ResultSet rs, FlowAnalyzerVO vo)
			throws Exception {
		// vo.setId(rs.getString("ID"));
		// 流程ID
		vo.addGroupColumn("FLOWID", rs.getString("FLOWID"));
		// 流程名称
		vo.addGroupColumn("FLOWNAME", rs.getString("FLOWNAME"));
		// 开始节点名称
		vo.addGroupColumn("STARTNODENAME", rs.getString("STARTNODENAME"));
		// 结束节点名称
		vo.addGroupColumn("ENDNODENAME", rs.getString("ENDNODENAME"));
		// 统计值
		vo.addResultField("AMOUNT", rs.getDouble("AMOUNT"));

		// 处理人ID
		vo.addGroupColumn("AUDITOR", rs.getString("AUDITOR"));
		// 文档ID
		vo.addGroupColumn("DOCID", rs.getString("DOCID"));

		vo.setApplicationid(rs.getString("APPLICATIONID"));
	}

	protected Timestamp getDateRangeBegin(String dateRangeType) {
		if (dateRangeType != null) {
			Calendar cld = Calendar.getInstance();
			if (dateRangeType.equalsIgnoreCase("TODAY")) {
			} else if (dateRangeType.equalsIgnoreCase("THISWEEK")) {
				cld.set(Calendar.DAY_OF_WEEK, 1);
			} else if (dateRangeType.equalsIgnoreCase("THISMONTH")) {
				cld.set(Calendar.DAY_OF_MONTH, 1);
			} else if (dateRangeType.equalsIgnoreCase("THISYEAR")) {
				cld.set(Calendar.DAY_OF_YEAR, 1);
			}
			cld.set(Calendar.HOUR, 0);
			cld.set(Calendar.MINUTE, 0);
			cld.set(Calendar.SECOND, 0);
			cld.set(Calendar.MILLISECOND, 0);

			log.info(cld.getTime());

			return new Timestamp(cld.getTimeInMillis());
		}
		return null;
	}

	protected Timestamp getDateRangeEnd(String dateRangeType) {
		if (dateRangeType != null) {
			Calendar cld = Calendar.getInstance();
			if (dateRangeType.equalsIgnoreCase("TODAY")) {
				cld.roll(Calendar.DATE, 1);
			} else if (dateRangeType.equalsIgnoreCase("THISWEEK")) {
				cld.roll(Calendar.WEEK_OF_YEAR, 1);
				cld.set(Calendar.DAY_OF_WEEK, 1);
			} else if (dateRangeType.equalsIgnoreCase("THISMONTH")) {
				cld.roll(Calendar.MONTH, 1);
				cld.set(Calendar.DAY_OF_MONTH, 1);
			} else if (dateRangeType.equalsIgnoreCase("THISYEAR")) {
				cld.roll(Calendar.YEAR, 1);
				cld.set(Calendar.DAY_OF_YEAR, 1);
			}

			cld.set(Calendar.HOUR, 0);
			cld.set(Calendar.MINUTE, 0);
			cld.set(Calendar.SECOND, 0);
			cld.set(Calendar.MILLISECOND, 0);

			log.info(cld.getTime());
			return new Timestamp(cld.getTimeInMillis());
		}
		return null;
	}
	
	public Collection<HashMap> queryConsuming(ParamsTable params,String dateRangeType,String showMode,
			WebUser user) throws Exception{
		
		String startdate = params.getParameterAsString("startdate");
		String enddate = params.getParameterAsString("enddate");
		
		String sql = buildQueryConsumingSql(params,dateRangeType,showMode,user);
		
		Collection<HashMap> rtn = new ArrayList<HashMap>();
		Collection<HashMap> rtn1 = new ArrayList<HashMap>();
		HashMap targetData = new HashMap();
		HashMap target = new HashMap();
		JSONArray xAxis = new JSONArray();
		JSONArray yAxis = new JSONArray();
		JSONArray num = new JSONArray();
		DecimalFormat df = new DecimalFormat("0.0");
		PreparedStatement statement = null;
		log.info(sql); 
		try {
			statement = connection.prepareStatement(sql);
			
			int parameterIndex = 1;
			if(!StringUtil.isBlank(startdate))
				statement.setTimestamp(parameterIndex++, new Timestamp(DateUtil.parseDate(startdate).getTime()));
			if(!StringUtil.isBlank(enddate))
				statement.setTimestamp(parameterIndex++, new Timestamp(DateUtil.parseDate(enddate).getTime()));
			
			ResultSet rs = statement.executeQuery();
			int index = 1;
			while (rs.next()) {
				HashMap<String,String> item = new HashMap<String,String>();
				item.put("core", String.valueOf(index++));
				item.put("flowname",rs.getString("FLOWNAME"));
				if("1".equals(rs.getString("COMPLETE"))){
					item.put("statelabel","已完成");
				}else{
					item.put("statelabel","未完成");
				}
				item.put("consuming",String.valueOf(df.format(rs.getDouble("CONSUMING"))));
				item.put("number",String.valueOf(rs.getInt("NUM")));
				item.put("consumingday",String.valueOf(df.format(rs.getDouble("CONSUMING")/24)));
				rtn1.add(item);
				xAxis.add(rs.getString("FLOWNAME"));
				yAxis.add(df.format(rs.getDouble("CONSUMING")));
				num.add(rs.getInt("NUM"));
			}
			rs.close();
			target.put("xAxis", xAxis);
			target.put("yAxis", yAxis);
			target.put("num", num);
			targetData.put("table", rtn1);
			targetData.put("echart", target);
			rtn.add(targetData);

			return rtn;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}
	
	protected abstract String buildQueryConsumingSql(ParamsTable params,
			String dateRangeType, String showMode, WebUser user)throws Exception;

	public Collection<HashMap> queryNode(ParamsTable params,String dateRangeType,String showMode,
		WebUser user) throws Exception{
		
		String startdate = params.getParameterAsString("startdate");
		String enddate = params.getParameterAsString("enddate");
		
		String sql = buildQueryNodeSql(params,dateRangeType,showMode,user);
		
		Collection<HashMap> rtn = new ArrayList<HashMap>();
		Collection<HashMap> rtn1 = new ArrayList<HashMap>();
		HashMap targetData = new HashMap();
		HashMap target = new HashMap();
		JSONArray xAxis = new JSONArray();
		JSONArray yAxis = new JSONArray();
		JSONArray num = new JSONArray();
		DecimalFormat df = new DecimalFormat("0.0");
		PreparedStatement statement = null;

		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			
			int parameterIndex = 1;
			if(!StringUtil.isBlank(startdate))
				statement.setTimestamp(parameterIndex++, new Timestamp(DateUtil.parseDate(startdate).getTime()));
			if(!StringUtil.isBlank(enddate))
				statement.setTimestamp(parameterIndex++, new Timestamp(DateUtil.parseDate(enddate).getTime()));

			ResultSet rs = statement.executeQuery();
			int index = 1;
			while (rs.next()) {
				HashMap item = new HashMap();
				item.put("core", String.valueOf(index++));
				item.put("flowname",rs.getString("FLOWNAME"));
				if("1".equals(rs.getString("COMPLETE"))){
					item.put("statelabel","已完成");
				}else{
					item.put("statelabel","未完成");
				}
				item.put("nodename",rs.getString("ENDNODENAME"));
				item.put("nodeconsuming",String.valueOf(df.format(rs.getDouble("CONSUMING"))));
				item.put("nodenumber",String.valueOf(rs.getInt("NUM")));
				item.put("nodeconsumingday",String.valueOf(df.format(rs.getDouble("CONSUMING")/24)));
				rtn1.add(item);
				xAxis.add(rs.getString("ENDNODENAME"));
				yAxis.add(df.format(rs.getDouble("CONSUMING")));
				num.add(rs.getInt("NUM"));
			}
			rs.close();
			target.put("xAxis", xAxis);
			target.put("yAxis", yAxis);
			target.put("num", num);
			targetData.put("table", rtn1);
			targetData.put("echart", target);
			rtn.add(targetData);

			return rtn;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	protected abstract String buildQueryNodeSql(ParamsTable params, String dateRangeType,
			String showMode, WebUser user)throws Exception;

	public Collection<HashMap> queryNames(ParamsTable params,String showMode,
		WebUser user) throws Exception{
		String startdate = params.getParameterAsString("startdate");
		String enddate = params.getParameterAsString("enddate");

		String sql = buildQueryNamesSql(params,showMode,user);
		
		Collection<HashMap> rtn = new ArrayList<HashMap>();
		Collection<HashMap> rtn1 = new ArrayList<HashMap>();
		HashMap targetData = new HashMap();
		HashMap target = new HashMap();
		JSONArray xAxis = new JSONArray();
		JSONArray yAxis = new JSONArray();
		JSONArray num = new JSONArray();
		DecimalFormat df = new DecimalFormat("0.0");
		PreparedStatement statement = null;
	
		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			
			int parameterIndex = 1;
			if(!StringUtil.isBlank(startdate))
				statement.setTimestamp(parameterIndex++, new Timestamp(DateUtil.parseDate(startdate).getTime()));
			if(!StringUtil.isBlank(enddate))
				statement.setTimestamp(parameterIndex++, new Timestamp(DateUtil.parseDate(enddate).getTime()));

			ResultSet rs = statement.executeQuery();
			int index = 1;
			while (rs.next()) {
				HashMap item = new HashMap();
				item.put("core", String.valueOf(index++));
				item.put("nodename",rs.getString("ENDNODENAME"));
				item.put("initiator",rs.getString("INITIATOR"));
				item.put("nodenumber",String.valueOf(rs.getInt("NUM")));
				if("1".equals(rs.getString("COMPLETE"))){
					item.put("statelabel","已完成");
				}else{
					item.put("statelabel","未完成");
				}
				item.put("nodeconsuming",String.valueOf(df.format(rs.getDouble("CONSUMING"))));
				item.put("nodeconsumingday",String.valueOf(df.format(rs.getDouble("CONSUMING")/24)));
				rtn1.add(item);
				xAxis.add(rs.getString("INITIATOR"));
				yAxis.add(df.format(rs.getDouble("CONSUMING")));
				num.add(rs.getInt("NUM"));
			}
			rs.close();
			target.put("xAxis", xAxis);
			target.put("yAxis", yAxis);
			target.put("num", num);
			targetData.put("table", rtn1);
			targetData.put("echart", target);
			rtn.add(targetData);
	
			return rtn;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}
	
	protected abstract String buildQueryNamesSql(ParamsTable params, String showMode, WebUser user)throws Exception;

	public Collection<HashMap> queryDetailed(ParamsTable params,String showMode,
		WebUser user) throws Exception{
	
		String startdate = params.getParameterAsString("startdate");
		String enddate = params.getParameterAsString("enddate");

		String sql = buildQueryDetailedSql(params,showMode,user);
		
		
		Collection<HashMap> rtn = new ArrayList<HashMap>();
		DecimalFormat df = new DecimalFormat("0.0");
		PreparedStatement statement = null;
	
		log.info(sql);
		try {
			statement = connection.prepareStatement(sql);
			
			int parameterIndex = 1;
			if(!StringUtil.isBlank(startdate))
				statement.setTimestamp(parameterIndex++, new Timestamp(DateUtil.parseDate(startdate).getTime()));
			if(!StringUtil.isBlank(enddate))
				statement.setTimestamp(parameterIndex++, new Timestamp(DateUtil.parseDate(enddate).getTime()));

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				HashMap item = new HashMap();
				item.put("flowname",rs.getString("FLOWNAME"));
				if("1".equals(rs.getString("COMPLETE"))){
					item.put("statelabel","已完成");
				}else{
					item.put("statelabel","未完成");
				}
				item.put("nodename",rs.getString("ENDNODENAME"));
				item.put("initiator",rs.getString("INITIATOR"));
				item.put("starttime",rs.getString("ACTIONTIME"));
				item.put("endtime",rs.getString("PROCESSTIME"));
				item.put("nodeconsuming",String.valueOf(df.format(rs.getDouble("CONSUMING"))));
				item.put("nodeconsumingday",String.valueOf(df.format(rs.getDouble("CONSUMING")/24)));
				rtn.add(item);
			}
			rs.close();
	
			return rtn;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	protected abstract String buildQueryDetailedSql(ParamsTable params, String showMode,
			WebUser user) throws Exception;

}
