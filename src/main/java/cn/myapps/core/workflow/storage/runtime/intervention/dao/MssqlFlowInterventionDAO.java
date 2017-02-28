package cn.myapps.core.workflow.storage.runtime.intervention.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.HibernateSQLUtils;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.storage.runtime.intervention.ejb.FlowInterventionVO;
import cn.myapps.util.DbTypeUtil;
import cn.myapps.util.StringUtil;

public class MssqlFlowInterventionDAO extends AbstractFlowInterventionDAO implements FlowInterventionDAO {

	public MssqlFlowInterventionDAO(Connection connection) {
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
		lines =10;
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

	public DataPackage<FlowInterventionVO> queryByFilter(ParamsTable params, WebUser user)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		
		Long _currpage = params.getParameterAsLong("_currpage");
		Long _pagelines = params.getParameterAsLong("_pagelines");
		int currpage = _currpage !=null && _currpage != 0 ? _currpage.intValue() : 1;
		int pagelines = _pagelines !=null && _pagelines != 0 ? _pagelines.intValue() : 10;

		String _orderby = params.getParameterAsString("_orderby");
		
		String whereBlock = buildWhereBlock(params);

		
		sql.append("SELECT top " + Integer.MAX_VALUE
				+ " * FROM T_FLOW_INTERVENTION WHERE 1=1");
		sql.append(whereBlock);

		sql.append(" ORDER BY ").append(
				StringUtil.isBlank(_orderby) ? "ID" : _orderby.toUpperCase());

		return queryBySQLPage(sql.toString(), currpage, pagelines, params.getParameterAsString("domainid"));
			
	}

	public DataPackage<FlowInterventionVO> queryBySQLPage(String sql, int page,
			int lines, String domainid) throws Exception {

		HibernateSQLUtils sqlUtil = new HibernateSQLUtils();
		sql = sqlUtil.appendCondition(sql, "DOMAINID ='" + domainid + "'");

		PreparedStatement statement = null;
		DataPackage<FlowInterventionVO> dpg = new DataPackage<FlowInterventionVO>();

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

			Collection<FlowInterventionVO> datas = new ArrayList<FlowInterventionVO>();
			for (int i = 0; i < lines && rs.next(); i++) {
				FlowInterventionVO vo = new FlowInterventionVO();
				setProperties(rs, vo);
				datas.add(vo);
			}

			dpg.datas = datas;

			return dpg;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public String assemblyTime(String time) {
		return "cast('"+time+"' as datetime)";
	}
}
