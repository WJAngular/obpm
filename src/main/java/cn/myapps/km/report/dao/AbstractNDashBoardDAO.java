package cn.myapps.km.report.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.log.ejb.Logs;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.report.ejb.NDashBoardVO;
import cn.myapps.km.report.ejb.ReportItem;
import cn.myapps.km.util.DateUtil;
import cn.myapps.km.util.PersistenceUtils;
import cn.myapps.util.StringUtil;

public abstract class AbstractNDashBoardDAO {
	Logger log = Logger.getLogger(getClass());
	
	protected String dbTag = "MS SQL SERVER: ";

	protected String schema = "";

	protected Connection connection;
	
	public AbstractNDashBoardDAO(Connection conn) throws Exception{
		this.connection = conn;
	}
	
	public DataPackage<NDashBoardVO> getSumDepartmentUpLoad(String startDate,String endDate,String columns) throws Exception{
		PreparedStatement stmt = null;
		DataPackage<NDashBoardVO> result = new DataPackage<NDashBoardVO>();
		String sql = "SELECT DEPARTMENT,COUNT(*) AS COUNT FROM "+getFullTableName("KM_NFILE") +" WHERE DEPARTMENT IS NOT NULL AND  CREATEDATE <=?";
		if(startDate != ""){
			sql += " AND CREATEDATE >?";
		}
		sql += " GROUP BY DEPARTMENT_ID,DEPARTMENT";
		sql = buildLimitString(sql, 1, Integer.valueOf(columns), "COUNT", "DESC");
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setTimestamp(1, new Timestamp(DateUtil.parseDate(endDate).getTime()));
			if (startDate != "") {
				Timestamp ts = new Timestamp(DateUtil.parseDate(startDate).getTime());
				stmt.setTimestamp(2, ts);
			}
			ResultSet rs = stmt.executeQuery();
			int rowCount = 0;
			while (rs.next()) {
				NDashBoardVO nDashBoard = new NDashBoardVO();
				try{
					nDashBoard.setName(rs.getString("DEPARTMENT"));
					nDashBoard.setValue(rs.getDouble("COUNT"));
				}catch(Exception e){
					e.printStackTrace();
				}
				result.getDatas().add(nDashBoard);
				rowCount++;
			}
			result.rowCount = rowCount;
			rs.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	/**
	 * 人员上传量排名统计表
	 * @throws Exception
	 */
	public DataPackage<NDashBoardVO> getSumUserUpLoad(String startDate,String endDate,String columns) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<NDashBoardVO> result = new DataPackage<NDashBoardVO>();
		String sql = "SELECT CREATOR,COUNT(*) AS COUNT FROM "+getFullTableName("KM_NFILE") +" WHERE CREATEDATE <=?";
		if(startDate != ""){
			sql += " AND CREATEDATE >?";
		}
		sql += " GROUP BY CREATORID,CREATOR";
		sql = buildLimitString(sql, 1, Integer.valueOf(columns), "COUNT", "DESC");
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setTimestamp(1, new Timestamp(DateUtil.parseDate(endDate).getTime()));
			if (startDate != "") {
				Timestamp ts = new Timestamp(DateUtil.parseDate(startDate).getTime());
				stmt.setTimestamp(2, ts);
			}
			ResultSet rs = stmt.executeQuery();
			int rowCount = 0;
			while (rs.next()) {
				NDashBoardVO nDashBoard = new NDashBoardVO();
				try{
					nDashBoard.setName(rs.getString("CREATOR"));
					nDashBoard.setValue(rs.getDouble("COUNT"));
				}catch(Exception e){
					e.printStackTrace();
				}
				result.getDatas().add(nDashBoard);
				rowCount++;
			}
			result.rowCount = rowCount;
			rs.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	/**
	 * 文档览排名统计表
	 * @throws Exception
	 */
	public DataPackage<NDashBoardVO> getSumFilePreview(String startDate,String endDate,String columns) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<NDashBoardVO> result = new DataPackage<NDashBoardVO>();
		String sql = "SELECT COUNT(*) AS COUNT,FILE_NAME FROM "+getFullTableName("KM_LOGS") +" WHERE OPERATION_TYPE='VIEW' AND OPERATIONDATE<=?";
		if(startDate != ""){
			sql += " AND OPERATIONDATE>=?";
		}
		sql += " GROUP BY FILE_ID,FILE_NAME";
		sql = buildLimitString(sql, 1, Integer.valueOf(columns), "COUNT", "DESC");
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setTimestamp(1, new Timestamp(DateUtil.parseDate(endDate).getTime()));
			if (startDate != "") {
				Timestamp ts = new Timestamp(DateUtil.parseDate(startDate).getTime());
				stmt.setTimestamp(2, ts);
			}
			ResultSet rs = stmt.executeQuery();
			int rowCount = 0;
			while (rs.next()) {
				NDashBoardVO nDashBoard = new NDashBoardVO();
				try{
					nDashBoard.setName(rs.getString("FILE_NAME"));
					nDashBoard.setValue(rs.getDouble("COUNT"));
				}catch(Exception e){
					e.printStackTrace();
				}
				result.getDatas().add(nDashBoard);
				rowCount++;
			}
			result.rowCount = rowCount;
			rs.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	public DataPackage<NDashBoardVO> getSumCategoryUpload(String startDate,String endDate,String columns) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<NDashBoardVO> result = new DataPackage<NDashBoardVO>();
		String sqlstr = "SELECT COUNT(*) AS COUNT,ROOT_CATEGORY_ID AS CATEGORY_ID FROM " + getFullTableName("KM_NFILE") + " WHERE CREATEDATE <?";
		if(startDate != ""){
			sqlstr += " AND CREATEDATE >=?";
		}
		sqlstr += " GROUP BY ROOT_CATEGORY_ID";
		
		String sql = "SELECT B.NAME AS NAME,A.COUNT AS COUNT FROM ("+sqlstr+") AS A,"+getFullTableName("KM_CATEGORY") + " AS B WHERE A.CATEGORY_ID = B.ID";
		sql = buildLimitString(sql, 1, Integer.valueOf(columns), "COUNT", "DESC");
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setTimestamp(1, new Timestamp(DateUtil.parseDate(endDate).getTime()));
			if (startDate != "") {
				Timestamp ts = new Timestamp(DateUtil.parseDate(startDate).getTime());
				stmt.setTimestamp(2, ts);
			}
			ResultSet rs = stmt.executeQuery();
			int rowCount = 0;
			while (rs.next()) {
				NDashBoardVO nDashBoard = new NDashBoardVO();
				try{
					nDashBoard.setName(rs.getString("NAME"));
					nDashBoard.setValue(rs.getDouble("COUNT"));
					
				}catch(Exception e){
					e.printStackTrace();
				}
				rowCount++;
				result.getDatas().add(nDashBoard);
			}
			result.rowCount = rowCount;
			rs.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	/**
	 * 文档下载排名统计表
	 * @throws Exception
	 */
	public DataPackage<NDashBoardVO> getSumFileDownLoad(String startDate,String endDate,String columns) throws Exception {
		PreparedStatement stmt = null;
		DataPackage<NDashBoardVO> result = new DataPackage<NDashBoardVO>();
		String sql = "SELECT COUNT(*) AS COUNT,FILE_NAME FROM "+getFullTableName("KM_LOGS") +" WHERE OPERATION_TYPE='DOWNLOAD' AND OPERATIONDATE<?";
		if(startDate != ""){
			sql +=" AND OPERATIONDATE>=?";
		}
		sql += " GROUP BY FILE_ID,FILE_NAME";
		sql = buildLimitString(sql, 1, Integer.valueOf(columns), "COUNT", "DESC");
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setTimestamp(1, new Timestamp(DateUtil.parseDate(endDate).getTime()));
			if (startDate != "") {
				Timestamp ts = new Timestamp(DateUtil.parseDate(startDate).getTime());
				stmt.setTimestamp(2, ts);
			}
			ResultSet rs = stmt.executeQuery();
			int rowCount = 0;
			while (rs.next()) {
				NDashBoardVO nDashBoard = new NDashBoardVO();
				try{
					nDashBoard.setName(rs.getString("FILE_NAME"));
					nDashBoard.setValue(rs.getDouble("COUNT"));
					
				}catch(Exception e){
					e.printStackTrace();
				}
				result.getDatas().add(nDashBoard);
				rowCount ++ ;
			}
			result.rowCount = rowCount;
			rs.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(stmt);
		}
	}
	
	public String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}
	
	protected abstract String buildLimitString(String sql, int page, int lines,
			String orderbyFile, String orderbyMode) throws SQLException ;
	
	public void create(NObject vo) throws Exception {
	}

	public void remove(String pk) throws Exception {
	}

	public void update(NObject vo) throws Exception {
	}

	public NObject find(String id) throws Exception {
		return null;
	}
	
	/**
	 * 检索报表
	 * @param page
	 * 		当前页码
	 * @param pageLines
	 * 		每页数据量
	 * @param rootCategoryId
	 * 		根类别
	 * @param subCategoryId
	 * 		子类别
	 * @param operationType
	 * 		行为类型
	 * @param departmentId
	 * 		部门id
	 * @param userId
	 * 		用户id
	 * @return
	 * 		ReportItem集合
	 * @throws Exception
	 */
	public DataPackage<ReportItem> query(int page, int pageLines,String domainId,String rootCategoryId,String subCategoryId, String operationType,String departmentId,String userId,String startDate,String endDate,NUser user)throws Exception{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DataPackage<ReportItem> datas = new DataPackage<ReportItem>();
		Collection<ReportItem> items = new ArrayList<ReportItem>();
		/*String sql = "SELECT log.OPERATION_TYPE,log.FILE_ID,log.FILE_NAME,log.USER_NAME,log.OPERATIONDATE,log.USER_IP,log.OPERATION_CONTENT,file.CREATOR,log.DEPARTMENT_NAME,rcat.NAME AS ROOTCATEGORY,scat.NAME AS SUBCATEGORY " +
				"FROM " + getFullTableName("KM_LOGS") + " log," + getFullTableName("KM_NFILE") + " file," + getFullTableName("KM_CATEGORY") + " rcat ," + getFullTableName("KM_CATEGORY") + " scat " +
				"WHERE log.FILE_ID=file.id AND rcat.ID = file.ROOT_CATEGORY_ID AND scat.ID = file.SUB_CATEGORY_ID " +
				"AND file.DOMAIN_ID=? ";*/
		String sql="SELECT f.OPERATION_TYPE,f.FILE_ID,f.FILE_NAME,f.USER_NAME,f.OPERATIONDATE,f.USER_IP,f.OPERATION_CONTENT,f.CREATOR,rcat.NAME AS ROOTCATEGORY,scat.NAME AS SUBCATEGORY  ,f.DEPARTMENT_NAME  from   (SELECT log.OPERATION_TYPE,log.FILE_ID,log.FILE_NAME,log.USER_NAME,log.OPERATIONDATE,log.USER_IP,log.OPERATION_CONTENT,fil.CREATOR,fil.ROOT_CATEGORY_ID,fil.SUB_CATEGORY_ID,log.DEPARTMENT_NAME FROM "+"" +
		getFullTableName("KM_LOGS") + " log," + getFullTableName("KM_NFILE")+ " fil "+" WHERE log.FILE_ID=fil.id AND fil.DOMAIN_ID=? ";
		
		if(!user.isPublicDiskAdmin()){
			sql +=" AND log.DEPARTMENT_ID ='"+user.getDefaultDepartment()+"'";
		}
		
		if(!StringUtil.isBlank(startDate)){
			sql +=" AND log.OPERATIONDATE >='"+startDate+"'";
		}
		if(!StringUtil.isBlank(endDate)){
			sql +=" AND log.OPERATIONDATE <='"+endDate+"'";
		}
		if(!StringUtil.isBlank(rootCategoryId)){
			sql +=" AND fil.ROOT_CATEGORY_ID ='"+rootCategoryId+"'";
		}
		if(!StringUtil.isBlank(subCategoryId)){
			sql +=" AND fil.SUB_CATEGORY_ID ='"+subCategoryId+"'";
		}
		if(!StringUtil.isBlank(operationType)){
			sql +=" AND log.OPERATION_TYPE ='"+operationType+"'";
		}
		if(!StringUtil.isBlank(departmentId)){
			sql +=" AND log.DEPARTMENT_ID ='"+departmentId+"'";
		}
		if(!StringUtil.isBlank(userId)){
			String[] userIds = userId.split(";");
			if(userIds.length>1){
				StringBuffer p = new StringBuffer();
				for (int i = 0; i < userIds.length; i++) {
					p.append("'"+userIds[i]+"',");
				}
				p.setLength(p.length()-1);
				sql +=" AND log.USER_ID IN ("+p.toString()+")";
			}else{
				sql +=" AND log.USER_ID ='"+userId+"'";
			}
			
		}
		
		sql+=")  f  left   join "+ getFullTableName("KM_CATEGORY") + " rcat "+"    on   rcat.ID = f.ROOT_CATEGORY_ID   left   join "+ getFullTableName("KM_CATEGORY") + " scat " +"    on   scat.ID = f.SUB_CATEGORY_ID order by f.OPERATIONDATE desc";
		
		//拼接MySql的分页语句
		String querySql = sql;//buildLimitString(sql, page, pageLines);
		
		log.info(querySql);
		try {
			stmt = connection.prepareStatement(querySql);
			stmt.setString(1, domainId);
			rs = stmt.executeQuery();
			while(rs.next()){
				ReportItem item = new ReportItem();
				item.setOperationType(rs.getString("OPERATION_TYPE"));
				item.setFileId(rs.getString("FILE_ID"));
				item.setFileName(rs.getString("FILE_Name"));
				item.setUserName(rs.getString("USER_NAME"));
				item.setOperationDate(rs.getTimestamp("OPERATIONDATE"));
				item.setUserIp(rs.getString("USER_IP"));
				item.setOperationContent(rs.getString("OPERATION_CONTENT"));
				item.setCreator(rs.getString("CREATOR"));
				item.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
				item.setRootCategory(rs.getString("ROOTCATEGORY"));
				item.setSubCategory(rs.getString("SUBCATEGORY"));
				
				items.add(item);
			}
			datas.datas = items;
			datas.linesPerPage = pageLines;
			datas.rowCount = rs.getRow();//(int) countBySQL(sql, filename,operationtype,userid);
			datas.pageNo = page;
			return datas;
		}catch (SQLException e) {
			throw e;
		} finally {
			if(rs != null) rs.close();
			PersistenceUtils.closeStatement(stmt);
		}
	
	}
	
	
}
