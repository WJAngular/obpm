package cn.myapps.core.workflow.storage.runtime.proxy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.HibernateSQLUtils;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.storage.runtime.proxy.ejb.WorkflowProxyVO;
import cn.myapps.util.DbTypeUtil;
import cn.myapps.util.StringUtil;

public class MssqlWorkflowProxyDAO extends AbstractWorkflowProxyDAO implements WorkflowProxyDAO {

	public MssqlWorkflowProxyDAO(Connection connection) {
		super(connection);
		dbType = "MS SQL Server: ";
		this.schema = DbTypeUtil.getSchema(connection, DbTypeUtil.DBTYPE_MSSQL);
	}

	/**
	 * 生成限制条件sql.
	 * 
	 * @param sql
	 *            sql语句
	 * @param page
	 *            当前页码
	 * @param lines
	 *            每页显示行数
	 * @return 生成限制条件sql语句字符串
	 */
	public String buildLimitString(String sql, int page, int lines) {
		if (lines == Integer.MAX_VALUE) {
			return sql;
		}

		// int to = (page - 1) * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		int databaseVersion = 0;
		try {
			databaseVersion = connection.getMetaData()
					.getDatabaseMajorVersion();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (9 <= databaseVersion) {// 2005 row_number() over () 分页
			pagingSelect.append("SELECT TOP " + lines + " * FROM (");
			pagingSelect
					.append("SELECT ROW_NUMBER() OVER (ORDER BY DOMAINID) AS ROWNUMBER, TABNIC.* FROM (");
			pagingSelect.append(sql);
			pagingSelect.append(") TABNIC) TableNickname ");
			pagingSelect.append("WHERE ROWNUMBER>" + lines * (page - 1));

		} else {
			pagingSelect.append("SELECT TOP " + lines * page + " * FROM (");
			pagingSelect.append(sql);
			pagingSelect.append(") TABNIC");
		}

		return pagingSelect.toString();
	}

	public DataPackage<WorkflowProxyVO> queryByFilter(ParamsTable params, WebUser user)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		
		Long _currpage = params.getParameterAsLong("_currpage");
		Long _pagelines = params.getParameterAsLong("_pagelines");
		int currpage = _currpage != null ? _currpage.intValue() : 1;
		int pagelines = _pagelines != null ? _pagelines.intValue() : 10;

		String _orderby = params.getParameterAsString("_orderby");
		
		String whereBlock = buildWhereBlock(params,user);

		
		sql.append("SELECT top " + Integer.MAX_VALUE
				+ " * FROM T_FLOW_PROXY WHERE 1=1");
		sql.append(whereBlock);

		sql.append(" ORDER BY ").append(
				StringUtil.isBlank(_orderby) ? "ID" : _orderby.toUpperCase());

		return queryBySQLPage(sql.toString(), currpage, pagelines, user
				.getDomainid());
	}

	public DataPackage<WorkflowProxyVO> queryBySQLPage(String sql, int page,
			int lines, String domainid) throws Exception {

		HibernateSQLUtils sqlUtil = new HibernateSQLUtils();
		sql = sqlUtil.appendCondition(sql, "DOMAINID ='" + domainid + "'");

		PreparedStatement statement = null;
		DataPackage<WorkflowProxyVO> dpg = new DataPackage<WorkflowProxyVO>();

		dpg.rowCount = (int) countBySQL(sql, domainid);
		dpg.linesPerPage = lines;
		dpg.pageNo = page;

		sql = this.buildLimitString(sql, page, lines);
		LOG.debug(dbType + sql);

		try {
			statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			int databaseVersion = connection.getMetaData()
					.getDatabaseMajorVersion();
			if (9 <= databaseVersion) {
			} else {
				// JDBC1.0
				for (int i = 0; i < (page - 1) * lines && rs.next(); i++) {
					// keep empty
				}
			}

			Collection<WorkflowProxyVO> datas = new ArrayList<WorkflowProxyVO>();
			for (int i = 0; i < lines && rs.next(); i++) {
				WorkflowProxyVO vo = new WorkflowProxyVO();
				setProperties(rs, vo);
				datas.add(vo);
			}

			dpg.datas = datas;

			return dpg;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}
	
	/**
	 * 判断用户是否代理流程
	 * @param userId
	 * 		当前用户id
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean isFlowAgent(String userId) throws Exception {
		PreparedStatement statement = null;
		ResultSet rs = null;
		int databaseVersion = 0;
		try {
			databaseVersion = connection.getMetaData()
					.getDatabaseMajorVersion();
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuffer pagingSelect = new StringBuffer(100);
		if (9 <= databaseVersion) {// 2005 row_number() over () 分页
			pagingSelect.append("SELECT TOP 1 * FROM ( SELECT ROW_NUMBER() OVER (ORDER BY ID) AS ROWNUMBER,* FROM ");
			pagingSelect.append(getFullTableName("T_FLOW_PROXY"));
			pagingSelect.append(" WHERE AGENTS LIKE '%");
			pagingSelect.append(userId);
			pagingSelect.append("%') T WHERE ROWNUMBER>0");
		} else {
			pagingSelect.append("SELECT TOP 1 * FROM ");
			pagingSelect.append(getFullTableName("T_FLOW_PROXY"));
			pagingSelect.append(" WHERE T.AGENTS LIKE '%"+userId+"%'");
		}
		
		try {
			statement = connection.prepareStatement(pagingSelect.toString());
			rs = statement.executeQuery();
			
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
		return false;
	}
}
