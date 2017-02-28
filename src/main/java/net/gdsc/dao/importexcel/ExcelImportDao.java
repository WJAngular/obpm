/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.dao.importexcel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.gdsc.model.FingerDataVO;
import net.gdsc.model.UserInfoVO;
import net.gdsc.util.ConnectionManager;
import net.gdsc.util.DateUtil;

import org.apache.log4j.Logger;

import cn.myapps.util.sequence.Sequence;

/** 
 * @ClassName: ExcelImportDao 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-08-14 下午4:34:08 
 *  
 */
public class ExcelImportDao implements IExcelImportDao{
	
	private static final Logger logger = Logger.getLogger(ExcelImportDao.class);
	
	private static ExcelImportDao excelImportDao;
	
	public static synchronized ExcelImportDao getInstance(){
		if(excelImportDao==null)
			excelImportDao = new ExcelImportDao();
		return excelImportDao;
	}
	/** 
	* @Title: insertFingerVOs 
	* @Description: TODO
	* @param: @param fingers 
	* @throws 
	*/
	@Override
	public boolean insertFingerVOs(List<FingerDataVO> fingers,String type) {
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> uuids = new ArrayList<String>();
		StringBuffer sql = null;
		if("1".equals(type)){
			sql = new StringBuffer("INSERT INTO [TLK_考勤数据中间表]([PARENT],[FORMNAME],[CREATED],[FORMID],[APPLICATIONID],[DOMAINID]," +
					"[ITEM_部门],[ITEM_姓名],[ITEM_考勤号码],[ITEM_日期时间],[ITEM_记录状态],[ITEM_机器号],[ID],[ITEM_提取日期],[OPTIONITEM],[ITEM_提取时间],[ITEM_对比方式],[ITEM_分析状态],[ITEM_星期])");
			
			sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		}else if("2".equals(type)){
			sql = new StringBuffer("INSERT INTO [TLK_考勤表]([PARENT],[FORMNAME],[CREATED],[FORMID],[APPLICATIONID],[DOMAINID]," +
					"[ITEM_部门],[ITEM_姓名],[ITEM_考勤号码],[ITEM_日期时间],[ITEM_记录状态],[ITEM_机器号],[ID],[ITEM_提取日期],[OPTIONITEM],[ITEM_提取时间],[ITEM_对比方式],[ITEM_分析状态],[ITEM_星期],[ITEM_上班状态],[ITEM_下班状态],[ITEM_上班时间],[ITEM_下班时间],[ITEM_应打卡次数],[ITEM_实际打卡次数],[ITEM_未打卡次数])");
			
			sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		}
		logger.info(sql);
		try {
			stmt = conn.prepareStatement(sql.toString());
			for(int i = 0 ;i<fingers.size() ; i++){
				int paramterIndex = 0;
				String uuid = Sequence.getSequence();
				uuids.add(uuid);
				FingerDataVO finger = fingers.get(i);
				setFingerDataVOObject(finger,uuid,type);
				stmt.setString(++paramterIndex,finger.getParent());
				stmt.setString(++paramterIndex,finger.getFormname());
				
				stmt.setTimestamp(++paramterIndex, (Timestamp)finger.getCreated());
				stmt.setString(++paramterIndex, finger.getFormid());
				
				stmt.setString(++paramterIndex, finger.getApplicationid());
				
				stmt.setString(++paramterIndex, finger.getDomainid());
				
				//考勤数据内容    当状态为1时
				stmt.setString(++paramterIndex, finger.getDeparmentFinger());
				stmt.setString(++paramterIndex, finger.getNameFinger());
				stmt.setString(++paramterIndex, finger.getNumberFinger());
				stmt.setString(++paramterIndex, finger.getDateFinger());
				stmt.setString(++paramterIndex, finger.getRecorderFinger());
				stmt.setString(++paramterIndex, finger.getMachineFinger());
				stmt.setString(++paramterIndex, finger.getId());
				stmt.setDate(++paramterIndex, new java.sql.Date(finger.getExtractDateFinger().getTime()));
				stmt.setString(++paramterIndex, finger.getOptionitem());
				stmt.setString(++paramterIndex, finger.getExtractTimeFinger());
				stmt.setString(++paramterIndex, finger.getCompareTypeFinger());
				stmt.setString(++paramterIndex, finger.getStatusFinger());
				stmt.setString(++paramterIndex, finger.getWeekDay());
				
				if("2".equals(type)){
					stmt.setString(++paramterIndex, finger.getGoRecorder());
					stmt.setString(++paramterIndex, finger.getOffRecorder());
					stmt.setString(++paramterIndex, finger.getGoTime());
					stmt.setString(++paramterIndex, finger.getOffTime());
					stmt.setInt(++paramterIndex, finger.getScount());
					stmt.setInt(++paramterIndex, finger.getRcount());
					stmt.setInt(++paramterIndex, finger.getNcount());
				}
				stmt.addBatch();
			}
			stmt.executeBatch();
			createDocument(fingers,conn,stmt,uuids);
			return true;
		} catch (Exception e) {
			logger.error("操作数据异常，请联系管理员!",e);
			return false;
		} finally {
			try{
				if(rs!=null) rs.close();
				ConnectionManager.closeStatement(stmt);
				ConnectionManager.closeConnection();
			}catch(Exception e){
				logger.error("请联系管理员",e);
			}
		}
	}
	
	/**
	 * 封装FingerDataVO 
	 * @param o
	 */
	private void setFingerDataVOObject(FingerDataVO finger,String uuid,String type){
		finger.setParent(finger.getParent());
		finger.setCreated(new Timestamp(new Date().getTime()));
		if("1".equals(type)){
			finger.setFormname("综合项目管理系统/人事管理/考勤管理/考勤数据/考勤数据表单");
			finger.setFormid("11e6-6126-ee664588-9305-c9c685791f52");
		}else if("2".equals(type)){
			finger.setFormname("综合项目管理系统/人事管理/考勤管理/考勤数据/考勤表");
			finger.setFormid("11e6-6b60-926a962b-a331-959d0e705096");
		}
		finger.setApplicationid("11e6-3d0f-5d78a4dd-b6a6-2fcfcefd00c4");
		finger.setDomainid("11e6-3d0d-ba351983-b6a6-2fcfcefd00c4");
		finger.setId(uuid);
	}
	
	/**
	 * 级联新增T_Document
	 * @param doc
	 */
	public void createDocument(List<FingerDataVO> fingers,Connection conn,PreparedStatement stmt,List<String> uuids) {
		if (fingers == null || fingers.size() <=0) {
			logger.error("操作数据异常，请联系管理员!");
			return;
		}
		StringBuffer sql = new StringBuffer("INSERT INTO[T_DOCUMENT]([ID],[FORMNAME],[CREATED],[FORMID],[APPLICATIONID],[DOMAINID],[OPTIONITEM]"
				+ ",[MAPPINGID])");

		sql.append(" VALUES(?,?,?,?,?,?,?,?)");
		logger.info(sql.toString());
		try {
			stmt = conn.prepareStatement(sql.toString());
			for(int i = 0;i<fingers.size();i++){
				int paramterIndex = 0;
				FingerDataVO finger = fingers.get(i);
				stmt.setString(++paramterIndex, uuids.get(i));                                   //id 对应 TLK表单id
				stmt.setString(++paramterIndex, finger.getFormname());    //固定的 TLK 表单模块
				stmt.setObject(++paramterIndex, finger.getCreated());                                //表单创建时间
				stmt.setString(++paramterIndex, finger.getFormid());    //表单id 固定的
				stmt.setString(++paramterIndex, finger.getApplicationid());
				stmt.setString(++paramterIndex, finger.getDomainid());
				stmt.setString(++paramterIndex, finger.getOptionitem());
				stmt.setString(++paramterIndex, uuids.get(i));
				stmt.addBatch();
			}
			stmt.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** 
	* @Title: getDates 
	* @Description: 获取日期list
	* @param: @return 
	* @throws 
	*/
	@Override
	public List<String> getDates() {
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> datas = new ArrayList<String>();
		String date = null;
		String sql = "select distinct ITEM_提取日期  from TLK_考勤数据中间表  order by ITEM_提取日期  ASC";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				date = setDateParam(rs);
				datas.add(date);
			}
			return datas;
		} catch (Exception e) {
			logger.error(e);
		}finally{
			try{
				if(rs!=null) rs.close();
				ConnectionManager.closeStatement(stmt);
				ConnectionManager.closeConnection();
			}catch(Exception e){
				logger.error("请联系管理员",e);
			}
		}
		return null;
	}
	
	public String setDateParam(ResultSet rs){
		String date = null;
		try {
			date = DateUtil.formatDate(rs.getDate("ITEM_提取日期"),"yyyy-MM-dd");	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return date;
	}
	/** 
	* @Title: getFingerVOs 
	* @Description: TODO
	* @param: @param date
	* @param: @param number
	* @param: @return 
	* @throws 
	*/
	@Override
	public List<FingerDataVO> getFingerVOs(String date, String number,String time,String type) {
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<FingerDataVO> datas = new ArrayList<FingerDataVO>();
		FingerDataVO finger = null;
		String sql = null;
		if("less".equals(type)){
			sql = "select * from TLK_考勤数据中间表  where convert(varchar(100),ITEM_提取日期,23) = '"+date+"' and ITEM_考勤号码 = '"+number+"' and ITEM_提取时间 < '"+time+"' order by ITEM_提取时间  ASC";
		}else if("more".equals(type)){
			sql = "select * from TLK_考勤数据中间表  where convert(varchar(100),ITEM_提取日期,23) = '"+date+"' and ITEM_考勤号码 = '"+number+"' and ITEM_提取时间 > '"+time+"' order by ITEM_提取时间  ASC";
		}
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				finger = setFingerVOParam(rs);
				datas.add(finger);
			}
			return datas;
		} catch (Exception e) {
			logger.error(e);
		}finally{
			try{
				if(rs!=null) rs.close();
				ConnectionManager.closeStatement(stmt);
				ConnectionManager.closeConnection();
			}catch(Exception e){
				logger.error("请联系管理员",e);
			}
		}
		return null;
	}
	
	public FingerDataVO setFingerVOParam(ResultSet rs){
		FingerDataVO fingervo = new FingerDataVO();
		try {
			fingervo.setApplicationid(rs.getString("Applicationid"));
			fingervo.setId(rs.getString("ID"));
			fingervo.setAuditdate(rs.getTimestamp("Auditdate"));
			fingervo.setAuditorlist(rs.getString("Auditorlist"));
			fingervo.setAuditornames(rs.getString("auditornames"));
			fingervo.setAudituser(rs.getString("audituser"));
			fingervo.setAuthor(rs.getString("author"));
			fingervo.setAuthor_dept_index(rs.getString("author_dept_index"));
			fingervo.setCreated(rs.getTimestamp("created"));
			fingervo.setDomainid(rs.getString("domainid"));
			fingervo.setFormid(rs.getString("formid"));
			fingervo.setFormname(rs.getString("formname"));
			fingervo.setIstmp(rs.getBoolean("istmp"));
			fingervo.setLastflowoperation(rs.getString("lastflowoperation"));
			fingervo.setLastmodified(rs.getTimestamp("lastmodified"));
			fingervo.setLastmodifier(rs.getString("lastmodifier"));
			fingervo.setParent(rs.getString("parent"));
			fingervo.setPrevauditnode(rs.getString("prevauditnode"));
			fingervo.setPrevaudituser(rs.getString("prevaudituser"));
			fingervo.setState(rs.getString("state"));
			fingervo.setStateint(rs.getInt("stateint"));
			fingervo.setStatelabel(rs.getString("statelabel"));
			fingervo.setStatelabelinfo(rs.getString("statelabelinfo"));
			fingervo.setVersions(rs.getInt("versions"));
			
			fingervo.setDeparmentFinger(rs.getString("ITEM_部门"));
			fingervo.setNameFinger(rs.getString("ITEM_姓名"));
			fingervo.setNumberFinger(rs.getString("ITEM_考勤号码"));
			fingervo.setDateFinger(rs.getString("ITEM_日期时间"));
			fingervo.setRecorderFinger(rs.getString("ITEM_记录状态"));
			fingervo.setMachineFinger(rs.getString("ITEM_机器号"));
			fingervo.setExtractDateFinger(rs.getTimestamp("ITEM_提取日期"));
			fingervo.setExtractTimeFinger(rs.getString("ITEM_提取时间"));
			fingervo.setCompareTypeFinger(rs.getString("ITEM_对比方式"));
			fingervo.setStatusFinger(rs.getString("ITEM_分析状态"));
			fingervo.setWeekDay(rs.getString("ITEM_星期"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fingervo;
	}
	/** 
	* @Title: getUsers 
	* @Description: 获取员工基本信息
	* @param: @return 
	* @throws 
	*/
	@Override
	public List<UserInfoVO> getUsers() {
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<UserInfoVO> users = new ArrayList<UserInfoVO>();
		UserInfoVO user = null;
		String sql = "select * from TLK_员工信息登记资料  where ITEM_状态 != '离职';";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				user = setUserInfo(rs);
				users.add(user);
			}
			return users;
		} catch (Exception e) {
			logger.error(e);
		}finally{
			try{
				if(rs!=null) rs.close();
				ConnectionManager.closeStatement(stmt);
				ConnectionManager.closeConnection();
			}catch(Exception e){
				logger.error("请联系管理员",e);
			}
		}
		return null;
	}
	public UserInfoVO setUserInfo(ResultSet rs){
		UserInfoVO user = new UserInfoVO();
		try {
			user.setApplicationid(rs.getString("Applicationid"));
			user.setId(rs.getString("ID"));
			user.setAuditdate(rs.getTimestamp("Auditdate"));
			user.setAuditorlist(rs.getString("Auditorlist"));
			user.setAuditornames(rs.getString("auditornames"));
			user.setAudituser(rs.getString("audituser"));
			user.setAuthor(rs.getString("author"));
			user.setAuthor_dept_index(rs.getString("author_dept_index"));
			user.setCreated(rs.getTimestamp("created"));
			user.setDomainid(rs.getString("domainid"));
			user.setFormid(rs.getString("formid"));
			user.setFormname(rs.getString("formname"));
			user.setIstmp(rs.getBoolean("istmp"));
			user.setLastflowoperation(rs.getString("lastflowoperation"));
			user.setLastmodified(rs.getTimestamp("lastmodified"));
			user.setLastmodifier(rs.getString("lastmodifier"));
			user.setParent(rs.getString("parent"));
			user.setPrevauditnode(rs.getString("prevauditnode"));
			user.setPrevaudituser(rs.getString("prevaudituser"));
			user.setState(rs.getString("state"));
			user.setStateint(rs.getInt("stateint"));
			user.setStatelabel(rs.getString("statelabel"));
			user.setStatelabelinfo(rs.getString("statelabelinfo"));
			user.setVersions(rs.getInt("versions"));
			
			user.setName(rs.getString("ITEM_姓名"));
			user.setSex(rs.getString("ITEM_性别"));
			user.setPhoto(rs.getString("ITEM_个人照片"));
			user.setNameEn(rs.getString("ITEM_英文名"));
			user.setNation(rs.getString("ITEM_民族"));
			user.setBirth(rs.getString("ITEM_出生日期"));
			user.setBirthPlace(rs.getString("ITEM_籍贯"));
			user.setAge(rs.getString("ITEM_年龄"));
			user.setPolitical(rs.getString("ITEM_政治面貌"));
			user.setGraduationTime(rs.getString("ITEM_毕业时间"));
			user.setEducation(rs.getString("ITEM_学历"));
			user.setSchool(rs.getString("ITEM_毕业院校"));
			user.setProfession(rs.getString("ITEM_专业"));
			user.setCert(rs.getString("ITEM_身份证"));
			user.setMarriage(rs.getString("ITEM_婚姻状况"));
			user.setBlood(rs.getString("ITEM_血型"));
			user.setNo(rs.getString("ITEM_员工编号"));
			user.setCategory(rs.getString("ITEM_类别"));
			user.setJobTitle(rs.getString("ITEM_职称"));
			user.setPost(rs.getString("ITEM_职务"));
			user.setWageLevel(rs.getString("ITEM_工资级别"));
			user.setBankName(rs.getString("ITEM_开户银行"));
			user.setWageNo(rs.getString("ITEM_工资账号"));
			user.setEntryDate(rs.getString("ITEM_入职日期"));
			user.setPositiveDate(rs.getString("ITEM_转正日期"));
			user.setSeparationDate(rs.getString("ITEM_离职日期"));
			user.setTelephoneOne(rs.getString("ITEM_手机1"));
			user.setCompanyTel(rs.getString("ITEM_公司电话"));
			user.setFax(rs.getString("ITEM_传真"));
			user.setHomePhone(rs.getString("ITEM_家庭电话"));
			user.setEmail(rs.getString("ITEM_邮箱"));
			user.setHomeProvince(rs.getString("ITEM_家庭省"));
			user.setHomeCity(rs.getString("ITEM_家庭市"));
			user.setHomeZone(rs.getString("ITEM_家庭区"));
			user.setHomeCode(rs.getString("ITEM_家庭地址邮编"));
			user.setCertCode(rs.getString("ITEM_身份证地址邮编"));
			user.setOtherDescription(rs.getString("ITEM_其他描述"));
			user.setStatus(rs.getString("ITEM_状态"));
			user.setUserId(rs.getString("ITEM_用户ID"));
			user.setDepartment(rs.getString("ITEM_部门"));
			user.setHomeAddress(rs.getString("ITEM_家庭详细地址"));
			user.setCertProvince(rs.getString("ITEM_身份证省"));
			user.setCertCity(rs.getString("ITEM_身份证市"));
			user.setCertZone(rs.getString("ITEM_身份证区"));
			user.setCertAddress(rs.getString("ITEM_身份证详细地址"));
			user.setTelephoneTwo(rs.getString("ITEM_手机2"));
			user.setMarriedCount(rs.getString("ITEM_已婚子女"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	/** 
	* @Title: deleteTempTable 
	* @Description: TODO
	* @param:  
	* @throws 
	*/
	@Override
	public void deleteTempTable() {
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "delete from TLK_考勤数据中间表";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.execute();
		} catch (Exception e) {
			logger.error(e);
		}finally{
			try{
				if(rs!=null) rs.close();
				ConnectionManager.closeStatement(stmt);
				ConnectionManager.closeConnection();
			}catch(Exception e){
				logger.error("请联系管理员",e);
			}
		}
	}
	/** 
	* @Title: getFingerVOByUserNameAndDate 
	* @Description: TODO
	* @param: @param userName
	* @param: @param date
	* @param: @return 
	* @throws 
	*/
	@Override
	public FingerDataVO getFingerVOByUserNameAndDate(String jobNo,
			String date) {
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FingerDataVO finger = null;
		String sql = "select * from TLK_考勤表 m where m.ITEM_提取日期 = ? and m.ITEM_考勤号码 = ?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, date);
			stmt.setString(2, jobNo);
			rs = stmt.executeQuery();
			while(rs.next()){
				finger = setWechatFingerVOParam(rs);
			}
			return finger;
		} catch (Exception e) {
			logger.error(e);
		}finally{
			try{
				if(rs!=null) rs.close();
				ConnectionManager.closeStatement(stmt);
				ConnectionManager.closeConnection();
			}catch(Exception e){
				logger.error("请联系管理员",e);
			}
		}
		return null;
	}
	
	public FingerDataVO setWechatFingerVOParam(ResultSet rs){
		FingerDataVO fingervo = new FingerDataVO();
		try {
			fingervo.setApplicationid(rs.getString("Applicationid"));
			fingervo.setId(rs.getString("ID"));
			fingervo.setAuditdate(rs.getTimestamp("Auditdate"));
			fingervo.setAuditorlist(rs.getString("Auditorlist"));
			fingervo.setAuditornames(rs.getString("auditornames"));
			fingervo.setAudituser(rs.getString("audituser"));
			fingervo.setAuthor(rs.getString("author"));
			fingervo.setAuthor_dept_index(rs.getString("author_dept_index"));
			fingervo.setCreated(rs.getTimestamp("created"));
			fingervo.setDomainid(rs.getString("domainid"));
			fingervo.setFormid(rs.getString("formid"));
			fingervo.setFormname(rs.getString("formname"));
			fingervo.setIstmp(rs.getBoolean("istmp"));
			fingervo.setLastflowoperation(rs.getString("lastflowoperation"));
			fingervo.setLastmodified(rs.getTimestamp("lastmodified"));
			fingervo.setLastmodifier(rs.getString("lastmodifier"));
			fingervo.setParent(rs.getString("parent"));
			fingervo.setPrevauditnode(rs.getString("prevauditnode"));
			fingervo.setPrevaudituser(rs.getString("prevaudituser"));
			fingervo.setState(rs.getString("state"));
			fingervo.setStateint(rs.getInt("stateint"));
			fingervo.setStatelabel(rs.getString("statelabel"));
			fingervo.setStatelabelinfo(rs.getString("statelabelinfo"));
			fingervo.setVersions(rs.getInt("versions"));
			
			fingervo.setDeparmentFinger(rs.getString("ITEM_部门"));
			fingervo.setNameFinger(rs.getString("ITEM_姓名"));
			fingervo.setNumberFinger(rs.getString("ITEM_考勤号码"));
			fingervo.setDateFinger(rs.getString("ITEM_日期时间"));
			fingervo.setRecorderFinger(rs.getString("ITEM_记录状态"));
			fingervo.setMachineFinger(rs.getString("ITEM_机器号"));
			fingervo.setExtractDateFinger(rs.getTimestamp("ITEM_提取日期"));
			fingervo.setExtractTimeFinger(rs.getString("ITEM_提取时间"));
			fingervo.setCompareTypeFinger(rs.getString("ITEM_对比方式"));
			fingervo.setStatusFinger(rs.getString("ITEM_分析状态"));
			fingervo.setWeekDay(rs.getString("ITEM_星期"));
			fingervo.setGoRecorder(rs.getString("ITEM_上班状态"));
			fingervo.setGoTime(rs.getString("ITEM_上班时间"));
			fingervo.setOffRecorder(rs.getString("ITEM_下班状态"));
			fingervo.setOffTime(rs.getString("ITEM_下班时间"));
			fingervo.setScount(rs.getInt("ITEM_应打卡次数"));
			fingervo.setRcount(rs.getInt("ITEM_实际打卡次数"));
			fingervo.setNcount(rs.getInt("ITEM_未打卡次数"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fingervo;
	}
	
	/** 
	* @Title: updateFingerDataVOs 
	* @Description: TODO
	* @param: @param fingerWechats
	* @param: @return 
	* @throws 
	*/
	@Override
	public boolean updateFingerDataVOs(List<FingerDataVO> fingerWechats) {
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "update TLK_考勤表 set ITEM_上班时间 = ?,ITEM_上班状态 = ?,ITEM_下班时间 = ?,ITEM_下班状态 = ?,ITEM_实际打卡次数 = ?,ITEM_未打卡次数 = ? where ID = ?";
		try {
			if(fingerWechats != null &&  fingerWechats.size() > 0){
				stmt = conn.prepareStatement(sql);
				for(int i = 0 ; i<fingerWechats.size();i++){
					stmt.setString(1, fingerWechats.get(i).getGoTime());
					stmt.setString(2, fingerWechats.get(i).getGoRecorder());
					stmt.setString(3, fingerWechats.get(i).getOffTime());
					stmt.setString(4, fingerWechats.get(i).getOffRecorder());
					stmt.setInt(5, fingerWechats.get(i).getRcount());
					stmt.setInt(6, fingerWechats.get(i).getNcount());
					stmt.setString(7, fingerWechats.get(i).getId());
					stmt.addBatch();
				}
				stmt.executeBatch();
			}
			return true;
		} catch (Exception e) {
			logger.error("操作数据异常，请联系管理员!",e);
			return false;
		}finally{
			try{
				if(rs!=null) rs.close();
				ConnectionManager.closeStatement(stmt);
				ConnectionManager.closeConnection();
			}catch(Exception e){
				logger.error("请联系管理员",e);
			}
		}
	}
}
