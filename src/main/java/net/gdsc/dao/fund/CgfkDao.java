/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.dao.fund;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.gdsc.model.CgfkVO;
import net.gdsc.model.LxfVO;
import net.gdsc.model.WlkVO;
import net.gdsc.model.YwfVO;
import net.gdsc.model.ZdfVO;
import net.gdsc.model.ZtbVO;
import net.gdsc.util.StringUtil;
import net.gdsc.util.ConnectionManager;

import org.apache.log4j.Logger;


/** 
 * @ClassName: CgfkDao 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-08-09 下午2:36:54 
 *  
 */
public class CgfkDao implements ICgfkDao{
	
	private static final Logger logger = Logger.getLogger(CgfkDao.class);
	
	private static CgfkDao cgfkDao;
	
	public static synchronized CgfkDao getInstance(){
		if(cgfkDao==null)
			cgfkDao = new CgfkDao();
		return cgfkDao;
	}
	/** 
	* @Title: queryCgfk 
	* @Description: TODO
	* @param: @param docId
	* @param: @param primaryTable
	* @param: @return 
	* @throws 
	*/
	@Override
	public Object queryCgfkByNoAndTableName(String docNo, String primaryTable) {
		CgfkVO cgfk = null;
		YwfVO ywf = null;
		ZtbVO ztb = null;
		ZdfVO zdf = null;
		LxfVO lxf = null;
		WlkVO wlk = null;
		Object object = null;
		PreparedStatement stmt = null;
		String docId = null;
		ResultSet rs = null;
		String attitude = null;
		Connection conn = ConnectionManager.getConnection();
		String sql = "select * from "+primaryTable+" where ITEM_编号  = '"+docNo+"'";
		logger.info(sql);
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				if(primaryTable.indexOf("采购货款") > -1){cgfk = setCgfkParam(rs);docId = cgfk.getId();}
				if(primaryTable.indexOf("招投标") > -1){ztb = setZtbParam(rs);docId = ztb.getId();}
				if(primaryTable.indexOf("业务费") > -1){ywf = setYwfParam(rs);docId = ywf.getId();}
				if(primaryTable.indexOf("招待费") > -1){zdf = setZdfParam(rs);docId = zdf.getId();}
				if(primaryTable.indexOf("零星费") > -1){lxf = setLxfParam(rs);docId = lxf.getId();}
				if(primaryTable.indexOf("往来款") > -1){wlk = setWlkParam(rs);docId = wlk.getId();}
			}
			if(!"".equals(docId)){  //T_ACTORHIS
				attitude = getAttitudeByDocId(docId,stmt,rs,conn);
			}
			if(attitude != null){  //如果流程结束之后，则设置总经理的意见，否则不设置
				if(primaryTable.indexOf("采购货款") > -1){cgfk.setManagerAdvice(attitude);}
				if(primaryTable.indexOf("招投标") > -1){ztb.setManagerAdvice(attitude);}
				if(primaryTable.indexOf("业务费") > -1){ywf.setManagerAdvice(attitude);}
				if(primaryTable.indexOf("招待费") > -1){zdf.setManagerAdvice(attitude);}
				if(primaryTable.indexOf("零星费") > -1){lxf.setManagerAdvice(attitude);}
				if(primaryTable.indexOf("往来款") > -1){wlk.setManagerAdvice(attitude);}
				
			}
			if(primaryTable.indexOf("采购货款") > -1){object = cgfk;}
			if(primaryTable.indexOf("招投标") > -1){object = ztb;}
			if(primaryTable.indexOf("业务费") > -1){object = ywf;}
			if(primaryTable.indexOf("招待费") > -1){object = zdf;}
			if(primaryTable.indexOf("零星费") > -1){object = lxf;}
			if(primaryTable.indexOf("往来款") > -1){object = wlk;}
			return object;
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
	/**
	 * 
	* @Title: setCgfkParam 
	* @Description: 采购货款参数
	* @param: @param rs
	* @param: @return 
	* @return: CgfkVO
	* @throws
	 */
	public CgfkVO setCgfkParam(ResultSet rs){
		CgfkVO cgfk = new CgfkVO();
		String arrival = null;
		String houseAndNo = null;
		try {
			cgfk.setApplicationid(rs.getString("Applicationid"));
			cgfk.setId(rs.getString("ID"));
			cgfk.setAuditdate(rs.getTimestamp("Auditdate"));
			cgfk.setAuditorlist(rs.getString("Auditorlist"));
			cgfk.setAuditornames(rs.getString("auditornames"));
			cgfk.setAudituser(rs.getString("audituser"));
			cgfk.setAuthor(rs.getString("author"));
			cgfk.setAuthor_dept_index(rs.getString("author_dept_index"));
			cgfk.setCreated(rs.getTimestamp("created"));
			cgfk.setDomainid(rs.getString("domainid"));
			cgfk.setFormid(rs.getString("formid"));
			cgfk.setFormname(rs.getString("formname"));
			cgfk.setIstmp(rs.getBoolean("istmp"));
			cgfk.setLastflowoperation(rs.getString("lastflowoperation"));
			cgfk.setLastmodified(rs.getTimestamp("lastmodified"));
			cgfk.setLastmodifier(rs.getString("lastmodifier"));
			cgfk.setParent(rs.getString("parent"));
			cgfk.setPrevauditnode(rs.getString("prevauditnode"));
			cgfk.setPrevaudituser(rs.getString("prevaudituser"));
			cgfk.setState(rs.getString("state"));
			cgfk.setStateint(rs.getInt("stateint"));
			cgfk.setStatelabel(rs.getString("statelabel"));
			cgfk.setStatelabelinfo(rs.getString("statelabelinfo"));
			cgfk.setVersions(rs.getInt("versions"));
			
			cgfk.setSurePayTime(StringUtil.isBlank(rs.getString("ITEM_确定支付时间"))?rs.getString("ITEM_确定支付时间"):rs.getString("ITEM_确定支付时间").substring(0,10));
			cgfk.setMature(rs.getString("ITEM_是否到期"));
			cgfk.setClienter(rs.getString("ITEM_供应商名称"));
			houseAndNo = rs.getString("ITEM_入库情况");
			if("已入库".equals(houseAndNo)){
				cgfk.setHouseAndNo(houseAndNo+",入库单号("+rs.getString("ITEM_入库单号")+")");
			}else if("未入库".equals(houseAndNo)){
				cgfk.setHouseAndNo(houseAndNo);
			}
			cgfk.setBankNumber(rs.getString("ITEM_银行帐号"));
			cgfk.setBankName(rs.getString("ITEM_开户银行"));
			cgfk.setExplain(rs.getString("ITEM_用途说明"));
			cgfk.setPayType(rs.getString("ITEM_支付方式"));
			cgfk.setFax(rs.getString("ITEM_税票情况"));
			cgfk.setFaxCondition(rs.getString("ITEM_税票情况_条件"));
			cgfk.setCaiGou(rs.getString("ITEM_采购部"));
			arrival = rs.getString("ITEM_到货情况");
			if("已到".equals(arrival)){
				cgfk.setArrival(arrival+",到货比例("+rs.getString("ITEM_到货比例")+")");
			}else if("未到".equals(arrival)){
				cgfk.setArrival(arrival);
			}
			cgfk.setWarehouse(rs.getString("ITEM_仓管"));
			cgfk.setReceivable(rs.getString("ITEM_项目收款情况"));
			cgfk.setCheck(rs.getString("ITEM_货物安装验收情况"));
			cgfk.setManager(rs.getString("ITEM_项目经理"));
			cgfk.setContract(rs.getString("ITEM_采购合同金额"));
			cgfk.setFareType(rs.getString("ITEM_费用类型"));
			cgfk.setOperator(rs.getString("ITEM_操作人"));
			cgfk.setCaiGouType(rs.getString("ITEM_采购类型"));
			cgfk.setNo(rs.getString("ITEM_编号"));
			cgfk.setDepartName(rs.getString("ITEM_申请部门"));
			cgfk.setApplyDate(StringUtil.isBlank(rs.getString("ITEM_申请日期"))?rs.getString("ITEM_申请日期"):rs.getString("ITEM_申请日期").substring(0,10));
			cgfk.setProName(rs.getString("ITEM_项目名称"));
			cgfk.setProNo(rs.getString("ITEM_项目编号"));
			cgfk.setApplyBig(rs.getString("ITEM_申请金额大"));
			cgfk.setApplySmall(rs.getString("ITEM_申请金额小"));
			cgfk.setApproveBig(rs.getString("ITEM_批准金额大"));
			cgfk.setApproveSmall(rs.getString("ITEM_批准金额小"));
			cgfk.setClerk(rs.getString("ITEM_业务员"));
			cgfk.setBalance(rs.getString("ITEM_余额"));
			cgfk.setApplyFund(rs.getString("ITEM_本次申请金额"));
			cgfk.setSpendFund(rs.getString("ITEM_已支出金额"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cgfk;
	}
	/**
	 * 
	* @Title: setLxfParam 
	* @Description: 零星费用申请
	* @param: @param rs
	* @param: @return 
	* @return: LxfVO
	* @throws
	 */
	public LxfVO setLxfParam(ResultSet rs){
		LxfVO lxf = new LxfVO();
		try {
			lxf.setApplicationid(rs.getString("Applicationid"));
			lxf.setId(rs.getString("ID"));
			lxf.setAuditdate(rs.getTimestamp("Auditdate"));
			lxf.setAuditorlist(rs.getString("Auditorlist"));
			lxf.setAuditornames(rs.getString("auditornames"));
			lxf.setAudituser(rs.getString("audituser"));
			lxf.setAuthor(rs.getString("author"));
			lxf.setAuthor_dept_index(rs.getString("author_dept_index"));
			lxf.setCreated(rs.getTimestamp("created"));
			lxf.setDomainid(rs.getString("domainid"));
			lxf.setFormid(rs.getString("formid"));
			lxf.setFormname(rs.getString("formname"));
			lxf.setIstmp(rs.getBoolean("istmp"));
			lxf.setLastflowoperation(rs.getString("lastflowoperation"));
			lxf.setLastmodified(rs.getTimestamp("lastmodified"));
			lxf.setLastmodifier(rs.getString("lastmodifier"));
			lxf.setParent(rs.getString("parent"));
			lxf.setPrevauditnode(rs.getString("prevauditnode"));
			lxf.setPrevaudituser(rs.getString("prevaudituser"));
			lxf.setState(rs.getString("state"));
			lxf.setStateint(rs.getInt("stateint"));
			lxf.setStatelabel(rs.getString("statelabel"));
			lxf.setStatelabelinfo(rs.getString("statelabelinfo"));
			lxf.setVersions(rs.getInt("versions"));
			
			lxf.setSurePayTime(StringUtil.isBlank(rs.getString("ITEM_确定支付时间"))?rs.getString("ITEM_确定支付时间"):rs.getString("ITEM_确定支付时间").substring(0,10));
			lxf.setFundUser(rs.getString("ITEM_资金用途"));
			lxf.setExplain(rs.getString("ITEM_用途说明"));
			lxf.setFareType(rs.getString("ITEM_费用类型"));
			lxf.setOperator(rs.getString("ITEM_操作人"));
			lxf.setLingXiType(rs.getString("ITEM_零星费用类型"));
			lxf.setNo(rs.getString("ITEM_编号"));
			lxf.setDepartName(rs.getString("ITEM_申请部门"));
			lxf.setApplyDate(StringUtil.isBlank(rs.getString("ITEM_申请日期"))?rs.getString("ITEM_申请日期"):rs.getString("ITEM_申请日期").substring(0,10));
			lxf.setProName(rs.getString("ITEM_项目名称"));
			lxf.setProNo(rs.getString("ITEM_项目编号"));
			lxf.setApplyBig(rs.getString("ITEM_申请金额大"));
			lxf.setApplySmall(rs.getString("ITEM_申请金额小"));
			lxf.setApproveBig(rs.getString("ITEM_批准金额大"));
			lxf.setApproveSmall(rs.getString("ITEM_批准金额小"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lxf;
	}
	/**
	 * 
	* @Title: setWlkParam 
	* @Description: 往来款申请
	* @param: @param rs
	* @param: @return 
	* @return: WlkVO
	* @throws
	 */
	public WlkVO setWlkParam(ResultSet rs){
		WlkVO wlk = new WlkVO();
		try {
			wlk.setApplicationid(rs.getString("Applicationid"));
			wlk.setId(rs.getString("ID"));
			wlk.setAuditdate(rs.getTimestamp("Auditdate"));
			wlk.setAuditorlist(rs.getString("Auditorlist"));
			wlk.setAuditornames(rs.getString("auditornames"));
			wlk.setAudituser(rs.getString("audituser"));
			wlk.setAuthor(rs.getString("author"));
			wlk.setAuthor_dept_index(rs.getString("author_dept_index"));
			wlk.setCreated(rs.getTimestamp("created"));
			wlk.setDomainid(rs.getString("domainid"));
			wlk.setFormid(rs.getString("formid"));
			wlk.setFormname(rs.getString("formname"));
			wlk.setIstmp(rs.getBoolean("istmp"));
			wlk.setLastflowoperation(rs.getString("lastflowoperation"));
			wlk.setLastmodified(rs.getTimestamp("lastmodified"));
			wlk.setLastmodifier(rs.getString("lastmodifier"));
			wlk.setParent(rs.getString("parent"));
			wlk.setPrevauditnode(rs.getString("prevauditnode"));
			wlk.setPrevaudituser(rs.getString("prevaudituser"));
			wlk.setState(rs.getString("state"));
			wlk.setStateint(rs.getInt("stateint"));
			wlk.setStatelabel(rs.getString("statelabel"));
			wlk.setStatelabelinfo(rs.getString("statelabelinfo"));
			wlk.setVersions(rs.getInt("versions"));
			
			wlk.setFundUser(rs.getString("ITEM_资金用途"));
			wlk.setExplain(rs.getString("ITEM_用途说明"));
			wlk.setFareType(rs.getString("ITEM_费用类型"));
			wlk.setOperator(rs.getString("ITEM_操作人"));
			wlk.setNo(rs.getString("ITEM_编号"));
			wlk.setDepartName(rs.getString("ITEM_申请部门"));
			wlk.setApplyDate(StringUtil.isBlank(rs.getString("ITEM_申请日期"))?rs.getString("ITEM_申请日期"):rs.getString("ITEM_申请日期").substring(0,10));
			wlk.setProName(rs.getString("ITEM_项目名称"));
			wlk.setProNo(rs.getString("ITEM_项目编号"));
			wlk.setApplyBig(rs.getString("ITEM_申请金额大"));
			wlk.setApplySmall(rs.getString("ITEM_申请金额小"));
			wlk.setApproveBig(rs.getString("ITEM_批准金额大"));
			wlk.setApproveSmall(rs.getString("ITEM_批准金额小"));
			wlk.setClerk(rs.getString("ITEM_业务员"));
			wlk.setObject(rs.getString("ITEM_对象"));
			wlk.setContract(rs.getString("ITEM_合同价"));
			wlk.setProfit(rs.getString("ITEM_利润"));
			wlk.setBudget(rs.getString("ITEM_是否有预算"));
			wlk.setPayType(rs.getString("ITEM_付款方式"));
			wlk.setSum(rs.getString("ITEM_往来款总金额"));
			wlk.setSpendFund(rs.getString("ITEM_已支出金额"));
			wlk.setBalance(rs.getString("ITEM_余额"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wlk;
	}
	/**
	 * 
	* @Title: setZdfParam 
	* @Description: 招待费申请
	* @param: @param rs
	* @param: @return 
	* @return: ZdfVO
	* @throws
	 */
	public ZdfVO setZdfParam(ResultSet rs){
		ZdfVO zdf = new ZdfVO();
		try {
			zdf.setApplicationid(rs.getString("Applicationid"));
			zdf.setId(rs.getString("ID"));
			zdf.setAuditdate(rs.getTimestamp("Auditdate"));
			zdf.setAuditorlist(rs.getString("Auditorlist"));
			zdf.setAuditornames(rs.getString("auditornames"));
			zdf.setAudituser(rs.getString("audituser"));
			zdf.setAuthor(rs.getString("author"));
			zdf.setAuthor_dept_index(rs.getString("author_dept_index"));
			zdf.setCreated(rs.getTimestamp("created"));
			zdf.setDomainid(rs.getString("domainid"));
			zdf.setFormid(rs.getString("formid"));
			zdf.setFormname(rs.getString("formname"));
			zdf.setIstmp(rs.getBoolean("istmp"));
			zdf.setLastflowoperation(rs.getString("lastflowoperation"));
			zdf.setLastmodified(rs.getTimestamp("lastmodified"));
			zdf.setLastmodifier(rs.getString("lastmodifier"));
			zdf.setParent(rs.getString("parent"));
			zdf.setPrevauditnode(rs.getString("prevauditnode"));
			zdf.setPrevaudituser(rs.getString("prevaudituser"));
			zdf.setState(rs.getString("state"));
			zdf.setStateint(rs.getInt("stateint"));
			zdf.setStatelabel(rs.getString("statelabel"));
			zdf.setStatelabelinfo(rs.getString("statelabelinfo"));
			zdf.setVersions(rs.getInt("versions"));
			
			zdf.setObject(rs.getString("ITEM_招待对象_招待"));
			zdf.setPost(rs.getString("ITEM_职务_招待"));
			zdf.setNumber(rs.getString("ITEM_招待人数_招待"));
			zdf.setWay(rs.getString("ITEM_招待方式_招待"));
			zdf.setAddress(rs.getString("ITEM_招待地点_招待"));
			zdf.setBudgetSum(rs.getString("ITEM_业务招待费总预算_招待"));
			zdf.setPurpose(rs.getString("ITEM_招待目的_招待"));
			zdf.setFareType(rs.getString("ITEM_费用类型"));
			zdf.setOperator(rs.getString("ITEM_操作人"));
			zdf.setNo(rs.getString("ITEM_编号"));
			zdf.setDepartName(rs.getString("ITEM_申请部门_招待"));
			zdf.setApplyDate(StringUtil.isBlank(rs.getString("ITEM_申请日期_招待"))?rs.getString("ITEM_申请日期_招待"):rs.getString("ITEM_申请日期_招待").substring(0,10));
			zdf.setProName(rs.getString("ITEM_项目名称_招待"));
			zdf.setProNo(rs.getString("ITEM_项目编号"));
			zdf.setApplyBig(rs.getString("ITEM_申请金额大_招待"));
			zdf.setApplySmall(rs.getString("ITEM_申请金额小_招待"));
			zdf.setApproveBig(rs.getString("ITEM_批准金额大_招待"));
			zdf.setApproveSmall(rs.getString("ITEM_批准金额小_招待"));
			zdf.setBalance(rs.getString("ITEM_余额_招待"));
			zdf.setApplyFund(rs.getString("ITEM_本次申请金额_招待"));
			zdf.setSpendFund(rs.getString("ITEM_已支出金额_招待"));
			zdf.setBaoXiaoNo(rs.getString("ITEM_系统报销单编号"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return zdf;
	}
	/**
	 * 
	* @Title: setZtbParam 
	* @Description: 招投标申请
	* @param: @param rs
	* @param: @return 
	* @return: ZtbVO
	* @throws
	 */
	public ZtbVO setZtbParam(ResultSet rs){
		ZtbVO ztb = new ZtbVO();
		String explain = null;
		try {
			ztb.setApplicationid(rs.getString("Applicationid"));
			ztb.setId(rs.getString("ID"));
			ztb.setAuditdate(rs.getTimestamp("Auditdate"));
			ztb.setAuditorlist(rs.getString("Auditorlist"));
			ztb.setAuditornames(rs.getString("auditornames"));
			ztb.setAudituser(rs.getString("audituser"));
			ztb.setAuthor(rs.getString("author"));
			ztb.setAuthor_dept_index(rs.getString("author_dept_index"));
			ztb.setCreated(rs.getTimestamp("created"));
			ztb.setDomainid(rs.getString("domainid"));
			ztb.setFormid(rs.getString("formid"));
			ztb.setFormname(rs.getString("formname"));
			ztb.setIstmp(rs.getBoolean("istmp"));
			ztb.setLastflowoperation(rs.getString("lastflowoperation"));
			ztb.setLastmodified(rs.getTimestamp("lastmodified"));
			ztb.setLastmodifier(rs.getString("lastmodifier"));
			ztb.setParent(rs.getString("parent"));
			ztb.setPrevauditnode(rs.getString("prevauditnode"));
			ztb.setPrevaudituser(rs.getString("prevaudituser"));
			ztb.setState(rs.getString("state"));
			ztb.setStateint(rs.getInt("stateint"));
			ztb.setStatelabel(rs.getString("statelabel"));
			ztb.setStatelabelinfo(rs.getString("statelabelinfo"));
			ztb.setVersions(rs.getInt("versions"));
			
			ztb.setSurePayTime(StringUtil.isBlank(rs.getString("ITEM_确定支付时间"))?rs.getString("ITEM_确定支付时间"):rs.getString("ITEM_确定支付时间").substring(0,10));
			ztb.setAccountName(rs.getString("ITEM_开户名称"));
			ztb.setBankName(rs.getString("ITEM_开户银行"));
			ztb.setBankNumber(rs.getString("ITEM_银行帐号"));
			ztb.setRemark(rs.getString("ITEM_备注"));
			ztb.setOperator(rs.getString("ITEM_操作人"));
			ztb.setFareType(rs.getString("ITEM_费用类型"));
			ztb.setManager(rs.getString("ITEM_项目经理"));
			ztb.setPayer(rs.getString("ITEM_付款人名称"));
			ztb.setNo(rs.getString("ITEM_编号"));
			explain = rs.getString("ITEM_资金用途");
			if("其它".equals(explain)){
				ztb.setExplain(explain+"("+rs.getString("ITEM_其他_")+")");
			}else{
				ztb.setExplain(rs.getString("ITEM_资金用途"));
			}
			ztb.setDepartName(rs.getString("ITEM_申请部门"));
			ztb.setApplyDate(StringUtil.isBlank(rs.getString("ITEM_申请日期"))?rs.getString("ITEM_申请日期"):rs.getString("ITEM_申请日期").substring(0,10));
			ztb.setProName(rs.getString("ITEM_项目名称"));
			ztb.setProNo(rs.getString("ITEM_项目编号"));
			ztb.setApplyBig(rs.getString("ITEM_申请金额大"));
			ztb.setApplySmall(rs.getString("ITEM_申请金额小"));
			ztb.setApproveBig(rs.getString("ITEM_批准金额大"));
			ztb.setApproveSmall(rs.getString("ITEM_批准金额小"));
			ztb.setClerk(rs.getString("ITEM_业务员"));
			ztb.setBalance(rs.getString("ITEM_余额"));
			ztb.setApplyFund(rs.getString("ITEM_本次申请金额"));
			ztb.setSpendFund(rs.getString("ITEM_已支出金额"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ztb;
	}
	/**
	 * 
	* @Title: setYwfParam 
	* @Description: 业务费资金申请参数
	* @param: @param rs
	* @param: @return 
	* @return: YwfVO
	* @throws
	 */
	public YwfVO setYwfParam(ResultSet rs){
		YwfVO yef = new YwfVO();
		try {
			yef.setApplicationid(rs.getString("Applicationid"));
			yef.setId(rs.getString("ID"));
			yef.setAuditdate(rs.getTimestamp("Auditdate"));
			yef.setAuditorlist(rs.getString("Auditorlist"));
			yef.setAuditornames(rs.getString("auditornames"));
			yef.setAudituser(rs.getString("audituser"));
			yef.setAuthor(rs.getString("author"));
			yef.setAuthor_dept_index(rs.getString("author_dept_index"));
			yef.setCreated(rs.getTimestamp("created"));
			yef.setDomainid(rs.getString("domainid"));
			yef.setFormid(rs.getString("formid"));
			yef.setFormname(rs.getString("formname"));
			yef.setIstmp(rs.getBoolean("istmp"));
			yef.setLastflowoperation(rs.getString("lastflowoperation"));
			yef.setLastmodified(rs.getTimestamp("lastmodified"));
			yef.setLastmodifier(rs.getString("lastmodifier"));
			yef.setParent(rs.getString("parent"));
			yef.setPrevauditnode(rs.getString("prevauditnode"));
			yef.setPrevaudituser(rs.getString("prevaudituser"));
			yef.setState(rs.getString("state"));
			yef.setStateint(rs.getInt("stateint"));
			yef.setStatelabel(rs.getString("statelabel"));
			yef.setStatelabelinfo(rs.getString("statelabelinfo"));
			yef.setVersions(rs.getInt("versions"));
			
			yef.setExplain(rs.getString("ITEM_情况说明"));
			yef.setPayType(rs.getString("ITEM_付款方式"));
			yef.setFareType(rs.getString("ITEM_费用类型"));
			yef.setOperator(rs.getString("ITEM_操作人"));
			yef.setProfit(rs.getString("ITEM_利润"));
			yef.setBudget(rs.getString("ITEM_是否有预算"));
			yef.setBudgetSum(rs.getString("ITEM_业务费总预算"));
			yef.setAgreement(rs.getString("ITEM_合同价"));
			yef.setObject(rs.getString("ITEM_对象"));
			
			yef.setDepartName(rs.getString("ITEM_申请部门"));
			yef.setApplyDate(StringUtil.isBlank(rs.getString("ITEM_申请日期"))?rs.getString("ITEM_申请日期"):rs.getString("ITEM_申请日期").substring(0,10));
			yef.setProNo(rs.getString("ITEM_项目编号"));
			yef.setProName(rs.getString("ITEM_项目名称"));
			yef.setApplyBig(rs.getString("ITEM_申请金额大"));
			yef.setApplySmall(rs.getString("ITEM_申请金额小"));
			yef.setApproveBig(rs.getString("ITEM_批准金额大"));
			yef.setApproveSmall(rs.getString("ITEM_批准金额小"));
			yef.setBalance(rs.getString("ITEM_余额"));
			yef.setApplyFund(rs.getString("ITEM_本次申请金额"));
			yef.setSpendFund(rs.getString("ITEM_已支出金额"));
			yef.setNo(rs.getString("ITEM_编号"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return yef;
	}
	/**
	 * 
	* @Title: getAttitudeByDocId 
	* @Description: 根据DocId来获取流程中的标签意见
	* @param: @param docId
	* @param: @param stmt
	* @param: @param rs
	* @param: @param conn
	* @param: @return 
	* @return: String
	* @throws
	 */
	public String getAttitudeByDocId(String docId,PreparedStatement stmt,ResultSet rs,Connection conn){
		String attitude = null;
		String label = null;
		String sql = "select * from T_ACTORHIS where DOC_ID  = '"+docId+"' order by PROCESSTIME desc";
		logger.info(sql);
		try {
			label = getStateLabelByDocId(docId,stmt,rs,conn);
			if("结束".equals(label)){ //判断该标签是否结束标签
				stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery();
				rs.next(); //为了获取第一个数据
				attitude = rs.getString("ATTITUDE");
			}
			return attitude;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
	 /**
	  * 
	 * @Title: getStateLabelByDocId 
	 * @Description: 根据DocId来获取标签状态 
	 * @param: @param docId
	 * @param: @param stmt
	 * @param: @param rs
	 * @param: @param conn
	 * @param: @return 
	 * @return: String
	 * @throws
	  */
	public String getStateLabelByDocId(String docId,PreparedStatement stmt,ResultSet rs,Connection conn){
		String sql = "select * from T_FLOWSTATERT where DOCID = '"+docId+"'";
		logger.info(sql);
		String label = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				label = rs.getString("STATELABEL");
			}
			return label;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
}
