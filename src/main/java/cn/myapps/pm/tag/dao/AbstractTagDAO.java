package cn.myapps.pm.tag.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.pm.base.dao.AbstractBaseDAO;
import cn.myapps.pm.tag.ejb.Tag;
import cn.myapps.pm.util.ConnectionManager;

public abstract class AbstractTagDAO extends AbstractBaseDAO {

	private static final Logger log = Logger.getLogger(AbstractTagDAO.class);

	public AbstractTagDAO(Connection conn) throws Exception {
		super(conn);
		this.tableName = "PM_TAG";
	}

	public ValueObject create(ValueObject vo) throws Exception {
		Tag tag = (Tag) vo;

		PreparedStatement stmt = null;

		String sql = "INSERT INTO " + getFullTableName(tableName)
				+ " (ID,NAME,DOMAIN_ID) values (?,?,?)";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, tag.getId());
			stmt.setString(2, tag.getName());
			stmt.setString(3, tag.getDomainid());
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}

		return vo;

	}

	public ValueObject find(String id) throws Exception {
		PreparedStatement stmt = null;

		String sql = "SELECT * FROM " + getFullTableName(tableName)
				+ " WHERE ID=?";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			Tag tag = null;
			if (rs.next()) {
				tag = new Tag();
				setProperties(tag, rs);
			}
			rs.close();
			return tag;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
	}

	public void remove(String pk) throws Exception {
		PreparedStatement stmt = null;

		String sql = "DELETE FROM " + getFullTableName(tableName)
				+ " WHERE ID=?";

		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, pk);
			log.debug(sql);
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}

	}

	public ValueObject update(ValueObject vo) throws Exception {
		Tag tag = (Tag) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE " + getFullTableName(tableName)
				+ " SET NAME=?,DOMAIN_ID=? WHERE ID=?";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);

			stmt.setString(1, tag.getName());
			stmt.setString(2, tag.getDomainid());
			stmt.setString(3, tag.getId());
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return vo;
	}

	public Collection<?> simpleQuery(ParamsTable params, WebUser user)
			throws Exception {
		Collection<Tag> rtn = new ArrayList<Tag>();

		PreparedStatement stmt = null;

		String sql = "SELECT * FROM " + getFullTableName(tableName)
				+ " WHERE DOMAIN_ID=? ORDER BY ID DESC";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, user.getDomainid());
			ResultSet rs = stmt.executeQuery();
			Tag tag = null;
			while (rs.next()) {
				tag = new Tag();
				setProperties(tag, rs);
				rtn.add(tag);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
		return rtn;

	}

	void setProperties(Tag tag, ResultSet rs) throws Exception {
		try {
			tag.setId(rs.getString("ID"));
			tag.setName(rs.getString("NAME"));
			tag.setDomainid(rs.getString("DOMAIN_ID"));
		} catch (SQLException e) {
			throw e;
		}

	}

}
