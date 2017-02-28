/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.dao.client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import net.gdsc.model.ClientVO;
import net.gdsc.util.ConnectionManager;
import net.gdsc.util.DateUtil;

import org.apache.log4j.Logger;

import cn.myapps.util.sequence.Sequence;


/** 
 * @ClassName: ClientDao 
 * @Description: 客户DAO
 * @author: WUJING 
 * @date :2016-07-08 下午3:34:33 
 *  
 */
public class ClientDao implements IClientDao{
	
	private static final Logger logger = Logger.getLogger(ClientDao.class);
	
	private static ClientDao clientDao;
	
	public static synchronized ClientDao getInstance(){
		if(clientDao==null)
			clientDao = new ClientDao();
		return clientDao;
	}
	
	public void create(ClientVO clientVO){
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer("INSERT INTO [tlk_客户管理]([FORMNAME],[CREATED],[FORMID],[APPLICATIONID],[DOMAINID]," +
				"[ITEM_客户名称],[ITEM_客户性质],[ITEM_所属行业],[ITEM_联系人],[ITEM_联系电话],[ITEM_传真],[ITEM_E_MAIL]," +
				"[ITEM_联系地址],[ITEM_URL网址],[ITEM_法人代表],[ITEM_经营范围],[ITEM_描述],[ID],[ITEM_客户编号],[ITEM_合作伙伴性质])");
		
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		logger.info(sql);
		try {
			String uuid = Sequence.getSequence();
			setClientVO(clientVO,uuid);
			stmt = conn.prepareStatement(sql.toString());

			int paramterIndex = 0;
			stmt.setString(++paramterIndex,clientVO.getFormname());
			
			stmt.setTimestamp(++paramterIndex, (Timestamp) clientVO.getCreated());
			stmt.setString(++paramterIndex, clientVO.getFormid());
			
			stmt.setString(++paramterIndex, clientVO.getApplicationid());
			
			stmt.setString(++paramterIndex, clientVO.getDomainid());
			
			//注册内容
			stmt.setString(++paramterIndex, clientVO.getClientName());
			stmt.setString(++paramterIndex, clientVO.getClientNature());
			stmt.setString(++paramterIndex, clientVO.getClientProfession());
			stmt.setString(++paramterIndex, clientVO.getClienter());
			stmt.setString(++paramterIndex, clientVO.getClientTelephone());
			stmt.setString(++paramterIndex, clientVO.getClientFax());
			stmt.setString(++paramterIndex, clientVO.getClientEmail());
			stmt.setString(++paramterIndex, clientVO.getClientAddress());
			stmt.setString(++paramterIndex, clientVO.getClientUrl());
			stmt.setString(++paramterIndex, clientVO.getClientCorporate());
			stmt.setString(++paramterIndex, clientVO.getClientScope());
			stmt.setString(++paramterIndex,clientVO.getDescription());
			stmt.setString(++paramterIndex, clientVO.getId());
			stmt.setString(++paramterIndex, clientVO.getClientNo());
			stmt.setString(++paramterIndex, clientVO.getPartner());
			stmt.executeUpdate();
			createDocument(clientVO,conn,stmt);
		} catch (Exception e) {
			logger.error("操作数据异常，请联系管理员!",e);
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
	 * 封装RegisterVo 除了注册表单外的字段
	 * @param o
	 */
	private void setClientVO(ClientVO clientVO,String uuid){
		clientVO.setParent(null);
		clientVO.setCreated(new Timestamp(new Date().getTime()));
		clientVO.setFormname("综合项目管理系统/项目管理/客户管理/客户管理");
		clientVO.setFormid("11e6-4253-db9654dd-b5bd-ed52628c16b2");
		clientVO.setApplicationid("11e6-3d0f-5d78a4dd-b6a6-2fcfcefd00c4");
		clientVO.setDomainid("11e6-3d0d-ba351983-b6a6-2fcfcefd00c4");
		clientVO.setId(uuid);
	}
	
	/**
	 * 级联新增T_Document
	 * @param doc
	 */
	public void createDocument(ClientVO vo,Connection conn,PreparedStatement stmt) {
		if (vo == null) {
			logger.error("操作数据异常，请联系管理员!");
			return;
		}
		StringBuffer sql = new StringBuffer("INSERT INTO[T_DOCUMENT]([ID],[FORMNAME],[CREATED],[FORMID],[APPLICATIONID],[DOMAINID]"
				+ ",[MAPPINGID])");

		sql.append(" VALUES(?,?,?,?,?,?,?)");
		logger.info(sql.toString());
		try {
			int paramterIndex = 0;
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(++paramterIndex, vo.getId());                                   //id 对应 TLK表单id
			stmt.setString(++paramterIndex, vo.getFormname());    //固定的 TLK 表单模块
			stmt.setObject(++paramterIndex, vo.getCreated());                                //表单创建时间
			stmt.setString(++paramterIndex, vo.getFormid());    //表单id 固定的
			stmt.setString(++paramterIndex, vo.getApplicationid());
			stmt.setString(++paramterIndex, vo.getDomainid());
			stmt.setString(++paramterIndex, vo.getId());
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 
	* @Title: find 
	* @Description: TODO
	* @param: @param data
	* @param: @return 
	* @throws 
	*/
	@Override
	public ClientVO findByDataFlag() {
		ClientVO client = null;
		String year = DateUtil.formatDate(new Date(),"yyyyMMdd");
		String flag = "CN"+year.substring(2);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = ConnectionManager.getConnection();
		String sql = "select * from TLK_客户管理  where ITEM_客户编号  like "+"'"+flag+"%' and ITEM_客户编号 = (select MAX(ITEM_客户编号) from TLK_客户管理)";
		logger.info(sql);
		try {
			stmt = conn.prepareStatement(sql);
			//stmt.setString(1, flag);
			rs = stmt.executeQuery();
			while(rs.next()){
				client = setClientDefault(rs);
			}
			return client;
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
	* @Title: setClientDefault 
	* @Description: TODO
	* @param: @param rs
	* @param: @throws SQLException 
	* @return: void
	* @throws 
	*/
	public ClientVO setClientDefault(ResultSet rs) throws SQLException {
		ClientVO client = new ClientVO();
		client.setApplicationid(rs.getString("Applicationid"));
		client.setId(rs.getString("ID"));
		client.setAuditdate(rs.getTimestamp("Auditdate"));
		client.setAuditorlist(rs.getString("Auditorlist"));
		client.setAuditornames(rs.getString("auditornames"));
		client.setAudituser(rs.getString("audituser"));
		client.setAuthor(rs.getString("author"));
		client.setAuthor_dept_index(rs.getString("author_dept_index"));
		client.setCreated(rs.getTimestamp("created"));
		client.setDomainid(rs.getString("domainid"));
		client.setFormid(rs.getString("formid"));
		client.setFormname(rs.getString("formname"));
		client.setIstmp(rs.getBoolean("istmp"));
		client.setLastflowoperation(rs.getString("lastflowoperation"));
		client.setLastmodified(rs.getTimestamp("lastmodified"));
		client.setLastmodifier(rs.getString("lastmodifier"));
		client.setParent(rs.getString("parent"));
		client.setPrevauditnode(rs.getString("prevauditnode"));
		client.setPrevaudituser(rs.getString("prevaudituser"));
		client.setState(rs.getString("state"));
		client.setStateint(rs.getInt("stateint"));
		client.setStatelabel(rs.getString("statelabel"));
		client.setStatelabelinfo(rs.getString("statelabelinfo"));
		client.setVersions(rs.getInt("versions"));
		
		client.setClientName(rs.getString("ITEM_客户名称"));
		client.setClientNo(rs.getString("ITEM_客户编号"));
		client.setClientNature(rs.getString("ITEM_客户性质"));
		client.setClientProfession(rs.getString("ITEM_所属行业"));
		client.setClienter(rs.getString("ITEM_联系人"));
		client.setClientTelephone(rs.getString("ITEM_联系电话"));
		client.setClientFax(rs.getString("ITEM_传真"));
		client.setClientEmail(rs.getString("ITEM_E_MAIL"));
		client.setClientAddress(rs.getString("ITEM_联系地址"));
		client.setClientUrl(rs.getString("ITEM_URL网址"));
		client.setClientCorporate(rs.getString("ITEM_法人代表"));
		client.setClientScope(rs.getString("ITEM_经营范围"));
		client.setDescription(rs.getString("ITEM_描述"));
		client.setPartner(rs.getString("ITEM_合作伙伴性质"));
		
		return client;
	}

	/** 
	* @Title: findByClientNo 
	* @Description: TODO
	* @param: @return 
	* @throws 
	*/
	@Override
	public ClientVO findByClientNo(String clientNo) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ClientVO client = null;
		Connection conn = ConnectionManager.getConnection();
		String sql = "select * from TLK_客户管理 where ITEM_客户编号 = ?";
		logger.info(sql);
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, clientNo);
			rs = stmt.executeQuery();
			if(rs.next()){
				client = setClientDefault(rs);
			}
			return client;
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

}
