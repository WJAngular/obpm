/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.dao.pact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.gdsc.model.BillVO;
import net.gdsc.model.PactVO;
import net.gdsc.util.ConnectionManager;
import net.gdsc.util.StringUtil;

import org.apache.log4j.Logger;

/** 
 * @ClassName: PactDao 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-07-28 下午2:29:34 
 *  
 */
public class PactDao implements IPactDao{
	
	private static final Logger logger = Logger.getLogger(PactDao.class);
	
	private static PactDao pactDao;
	
	public static synchronized PactDao getInstance(){
		if(pactDao==null)
			pactDao = new PactDao();
		return pactDao;
	}

	/** 
	* @Title: queryHistory 
	* @Description: 查询以往的项目合同相关金额(合同总金额、收入金额、余额等）
	* @param: @param proName 项目名称
	* @param: @param proNo  项目编号
	* @param: @param primaryTable  主表
	* @param: @return 
	* @throws 
	*/
	@Override
	public List<PactVO> queryHistory(String proName, String proNo,
			String primaryTable) {
		if(StringUtil.isBlank(proNo) || StringUtil.isBlank(primaryTable)){
			return null;
		}
		PactVO pactVO = null;
		List<PactVO> packList = new ArrayList<PactVO>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = ConnectionManager.getConnection();
		String sql = "select * from "+primaryTable+" where ITEM_事项编号 = '"+proNo+"' order by ITEM_日期  desc";
		logger.info(sql);
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				pactVO = setPactVOParam(rs);
				packList.add(pactVO);
			}
			return packList;
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
	
	public PactVO setPactVOParam(ResultSet rs){
		PactVO pactVO = new PactVO();
		try {
			pactVO.setApplicationid(rs.getString("Applicationid"));
			pactVO.setId(rs.getString("ID"));
			pactVO.setAuditdate(rs.getTimestamp("Auditdate"));
			pactVO.setAuditorlist(rs.getString("Auditorlist"));
			pactVO.setAuditornames(rs.getString("auditornames"));
			pactVO.setAudituser(rs.getString("audituser"));
			pactVO.setAuthor(rs.getString("author"));
			pactVO.setAuthor_dept_index(rs.getString("author_dept_index"));
			pactVO.setCreated(rs.getTimestamp("created"));
			pactVO.setDomainid(rs.getString("domainid"));
			pactVO.setFormid(rs.getString("formid"));
			pactVO.setFormname(rs.getString("formname"));
			pactVO.setIstmp(rs.getBoolean("istmp"));
			pactVO.setLastflowoperation(rs.getString("lastflowoperation"));
			pactVO.setLastmodified(rs.getTimestamp("lastmodified"));
			pactVO.setLastmodifier(rs.getString("lastmodifier"));
			pactVO.setParent(rs.getString("parent"));
			pactVO.setPrevauditnode(rs.getString("prevauditnode"));
			pactVO.setPrevaudituser(rs.getString("prevaudituser"));
			pactVO.setState(rs.getString("state"));
			pactVO.setStateint(rs.getInt("stateint"));
			pactVO.setStatelabel(rs.getString("statelabel"));
			pactVO.setStatelabelinfo(rs.getString("statelabelinfo"));
			pactVO.setVersions(rs.getInt("versions"));
			
			pactVO.setPrMatter((rs.getString("ITEM_事项名称")));
			pactVO.setPrNo(((rs.getString("ITEM_编号"))));
			pactVO.setPrMatterNo(rs.getString("ITEM_事项编号"));
			pactVO.setPrBaozhang(rs.getString("ITEM_报账人"));
			pactVO.setPrDocumentNo((rs.getString("ITEM_凭证号")));
			pactVO.setPrFee((rs.getString("ITEM_现合同金额")));
			pactVO.setPrFeeOld((rs.getString("ITEM_以往合同金额")));
			pactVO.setPrFeeNew(rs.getString("ITEM_新增合同金额"));
			pactVO.setPrInCome(rs.getString("ITEM_本次收入金额"));
			pactVO.setPrInComeOld(rs.getString("ITEM_以往收入总金额"));
			pactVO.setPrBalance(rs.getString("ITEM_余额"));
			pactVO.setPrOperator(rs.getString("ITEM_操作人"));
			pactVO.setPrDate(rs.getString("ITEM_日期"));
			pactVO.setPrInComeHis(rs.getString("ITEM_以往收入明细"));
			pactVO.setPrRemark(rs.getString("ITEM_备注"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pactVO;
	}
}
