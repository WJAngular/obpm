package cn.myapps.core.datamap.runtime.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.datamap.runtime.ejb.DataMapTemplate;


/**
 * @author Happy
 *
 */
public abstract class AbstractDataMapTemplateDAO {
	Logger LOG = Logger.getLogger(AbstractDataMapTemplateDAO.class);

	protected Connection connection;

	protected String schema;

	protected String dbType = "Oracle: ";


	public AbstractDataMapTemplateDAO(Connection connection) {
		this.connection = connection;
	}
	

	public void create(ValueObject obj) throws Exception {
		DataMapTemplate vo = (DataMapTemplate) obj;
		PreparedStatement statement = null;

		String sql = "INSERT INTO "
				+ getFullTableName("T_DATAMAPTEMPLATE")
				+ "(ID,DATAMAPCFG_ID,CULEFIELD,CULEFIELD2, TEMPLATE, CONTENT, DOMAINID, APPLICATIONID)";
		sql += " VALUES(?,?,?,?,?,?,?,?)";
		LOG.debug(dbType + sql);
		try {
			statement = connection.prepareStatement(sql);
			setParameters(statement, vo);
			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	private int setParameters(PreparedStatement statement, DataMapTemplate vo)
			throws SQLException {
		int paramterIndex = 0;
		statement.setObject(++paramterIndex, vo.getId());
		statement.setObject(++paramterIndex, vo.getDatamapCfgId());
		statement.setObject(++paramterIndex, vo.getCuleField());
		statement.setObject(++paramterIndex, vo.getCuleField2());
		statement.setObject(++paramterIndex, vo.getTemplate());
		statement.setObject(++paramterIndex, vo.getContent());
		statement.setObject(++paramterIndex, vo.getDomainid());
		statement.setObject(++paramterIndex, vo.getApplicationid());
		
		return paramterIndex;
	}

	public ValueObject find(String id) throws Exception {
		String sql = "SELECT * FROM " + getFullTableName("T_DATAMAPTEMPLATE")
				+ " WHERE ID=?";
		PreparedStatement statement = null;
		LOG.debug(dbType + sql);
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				DataMapTemplate vo = new DataMapTemplate();
				setProperties(rs, vo);
				return vo;
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}

	public void setProperties(ResultSet rs, DataMapTemplate vo)
			throws Exception {
		vo.setId(rs.getString("ID"));
		vo.setDatamapCfgId(rs.getString("DATAMAPCFG_ID"));
		vo.setCuleField(rs.getString("CULEFIELD"));
		vo.setTemplate(rs.getString("TEMPLATE"));
		vo.setContent(rs.getString("CONTENT"));
		vo.setDomainid(rs.getString("DOMAINID"));
		vo.setApplicationid(rs.getString("APPLICATIONID"));
	}

	public void remove(String pk) throws Exception {
		String sql = "DELETE FROM " + getFullTableName("T_DATAMAPTEMPLATE")
				+ " WHERE ID = '" + pk + "'";
		LOG.debug(dbType + sql);
		Statement statement = connection.createStatement();
		try {
			statement.addBatch(sql);
			statement.executeBatch();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}


	public void update(ValueObject obj) throws Exception {
		DataMapTemplate vo = (DataMapTemplate) obj;
		String sql = "UPDATE "
				+ getFullTableName("T_DATAMAPTEMPLATE")
				+ " SET ID=?,DATAMAPCFG_ID=?,CULEFIELD=?,CULEFIELD2=?,TEMPLATE=?, CONTENT=?, DOMAINID=?, APPLICATIONID=? WHERE ID=?";
		LOG.debug(dbType + sql);
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			int parameterIndex = setParameters(statement, vo);
			statement.setString(++parameterIndex, vo.getId());
			statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}
	public String getFullTableName(String tblname) {
		if (this.schema != null && !this.schema.trim().equals("")) {
			return this.schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}
}
