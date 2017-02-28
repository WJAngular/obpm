/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.dao.prostage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.gdsc.model.StageVO;
import net.gdsc.util.ConnectionManager;
import net.gdsc.util.StringUtil;

import org.apache.log4j.Logger;

import cn.myapps.util.sequence.Sequence;

/** 
 * @ClassName: ProStage 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-07-17 上午11:44:28 
 *  
 */
public class ProStageDao implements IProStageDao{
	
	private static final Logger logger = Logger.getLogger(ProStageDao.class);
	
	private static ProStageDao proStageDao;
	
	public static synchronized ProStageDao getInstance(){
		if(proStageDao==null)
			proStageDao = new ProStageDao();
		return proStageDao;
	}

	/** 
	* @Title: insertBatch 
	* @Description: 批量新增
	* @param: @param stage 
	* @throws 
	*/
	@Override
	public void insertBatch(StageVO stage) {
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String[] prostage = null;
		List<String> uuids = new ArrayList<String>();
		StringBuffer sql = new StringBuffer("INSERT INTO [TLK_项目进程状态]([PARENT],[FORMNAME],[CREATED],[FORMID],[APPLICATIONID],[DOMAINID]," +
				"[ITEM_项目名称_],[ITEM_项目编号_],[ITEM_项目阶段_],[ITEM_客户名称_],[ITEM_开始时间_],[ITEM_结束时间_],[ID],[ITEM_备注_],[OPTIONITEM],[ITEM_完成状况],[ITEM_排序])");
		
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		logger.info(sql);
		try {
			stmt = conn.prepareStatement(sql.toString());
			prostage = stage.getProStage().split("->");
			for(int i = 0 ;i<prostage.length ; i++){
				int paramterIndex = 0;
				String uuid = Sequence.getSequence();
				uuids.add(uuid);
				setStageVOObject(stage,uuid);
				stmt.setString(++paramterIndex,stage.getParentId());
				stmt.setString(++paramterIndex,stage.getFormname());
				
				stmt.setTimestamp(++paramterIndex, (Timestamp) stage.getCreated());
				stmt.setString(++paramterIndex, stage.getFormid());
				
				stmt.setString(++paramterIndex, stage.getApplicationid());
				
				stmt.setString(++paramterIndex, stage.getDomainid());
				
				//注册内容
				stmt.setString(++paramterIndex, stage.getProName());
				stmt.setString(++paramterIndex, stage.getProNo());
				stmt.setString(++paramterIndex, prostage[i]);
				stmt.setString(++paramterIndex, stage.getClientName());
				stmt.setString(++paramterIndex, stage.getStartTime());
				stmt.setString(++paramterIndex, stage.getEndTime());
				stmt.setString(++paramterIndex, stage.getId());
				stmt.setString(++paramterIndex, stage.getRemark());
				stmt.setString(++paramterIndex, stage.getOptionitem());
				stmt.setString(++paramterIndex, stage.getStatus());
				stmt.setInt(++paramterIndex, i+1);
				stmt.addBatch();
			}
			stmt.executeBatch();
			createDocument(stage,conn,stmt,uuids);
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
	 * 封装StageVO 
	 * @param o
	 */
	private void setStageVOObject(StageVO stage,String uuid){
		stage.setParent(stage.getParentId());
		stage.setCreated(new Timestamp(new Date().getTime()));
		stage.setFormname("综合项目管理系统/事项管理/项目信息管理/立项登记/项目进程状态");
		stage.setFormid("11e6-4199-ab7e4648-97fc-f566a63fc7e6");
		stage.setApplicationid("11e6-3d0f-5d78a4dd-b6a6-2fcfcefd00c4");
		stage.setDomainid("11e6-3d0d-ba351983-b6a6-2fcfcefd00c4");
		stage.setId(uuid);
	}
	
	/**
	 * 级联新增T_Document
	 * @param doc
	 */
	public void createDocument(StageVO stage,Connection conn,PreparedStatement stmt,List<String> uuids) {
		if (stage == null) {
			logger.error("操作数据异常，请联系管理员!");
			return;
		}
		StringBuffer sql = new StringBuffer("INSERT INTO[T_DOCUMENT]([ID],[FORMNAME],[CREATED],[FORMID],[APPLICATIONID],[DOMAINID],[OPTIONITEM]"
				+ ",[MAPPINGID])");

		sql.append(" VALUES(?,?,?,?,?,?,?,?)");
		logger.info(sql.toString());
		try {
			String[] prostage = null;
			stmt = conn.prepareStatement(sql.toString());
			prostage = stage.getProStage().split("->");
			for(int i = 0;i<prostage.length;i++){
				int paramterIndex = 0;
				stmt.setString(++paramterIndex, uuids.get(i));                                   //id 对应 TLK表单id
				stmt.setString(++paramterIndex, stage.getFormname());    //固定的 TLK 表单模块
				stmt.setObject(++paramterIndex, stage.getCreated());                                //表单创建时间
				stmt.setString(++paramterIndex, stage.getFormid());    //表单id 固定的
				stmt.setString(++paramterIndex, stage.getApplicationid());
				stmt.setString(++paramterIndex, stage.getDomainid());
				stmt.setString(++paramterIndex, stage.getOptionitem());
				stmt.setString(++paramterIndex, uuids.get(i));
				stmt.addBatch();
			}
			stmt.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 
	* @Title: delete 
	* @Description: TODO
	* @param: @param id 
	* @throws 
	*/
	@Override
	public void delete(String id) {
		if(StringUtil.isNotBlank(id)){
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement stmt = null;
			ResultSet rs = null;
			List<String> uuids = new ArrayList<String>();
			String selectSql = "select * from TLK_项目进程状态  where PARENT = ?";
			logger.info(selectSql);
			try {
				stmt = conn.prepareStatement(selectSql);
				stmt.setString(1, id);
				rs = stmt.executeQuery();
				while(rs.next()){
					uuids.add(rs.getString("ID"));
				}
				deleteProStage(conn, stmt, rs, id);
				deleteDocument(conn, stmt, rs, uuids);
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
	}
	/**
	 * 
	* @Title: deleteAll 
	* @Description: 删除项目进程状态的相关信息
	* @param: @param conn
	* @param: @param stmt
	* @param: @param rs 
	* @return: void
	* @throws
	 */
	public void deleteProStage(Connection conn,PreparedStatement stmt,ResultSet rs,String id){
		String deleteSql = "delete from TLK_项目进程状态   where PARENT = ?";
		logger.info(deleteSql);
		try {
			stmt = conn.prepareStatement(deleteSql);
			stmt.setString(1, id);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	* @Title: deleteDocument 
	* @Description: 删除相应的document
	* @param: @param conn
	* @param: @param stmt
	* @param: @param rs
	* @param: @param uuids
	* @param: @param id 
	* @return: void
	* @throws
	 */
	public void deleteDocument(Connection conn,PreparedStatement stmt,ResultSet rs,List<String> uuids){
		String deletDocument = "delete from T_DOCUMENT where MAPPINGID = ?";
		logger.info(deletDocument);
		try {
			stmt = conn.prepareStatement(deletDocument);
			if(uuids != null && uuids.size() > 0){
				for(String uuid : uuids){
					stmt.setString(1,uuid );
					stmt.executeUpdate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
