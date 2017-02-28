package cn.myapps.attendance.rule.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.attendance.base.dao.AbstractBaseDAO;
import cn.myapps.attendance.location.ejb.Location;
import cn.myapps.attendance.rule.ejb.Rule;
import cn.myapps.attendance.util.ConnectionManager;

public abstract class AbstractRuleDAO extends AbstractBaseDAO {

	private static final Logger log = Logger.getLogger(AbstractRuleDAO.class);

	public AbstractRuleDAO(Connection conn) throws Exception {
		super(conn);
		this.tableName = "AM_RULE";
	}

	public ValueObject create(ValueObject vo) throws Exception {
		Rule rule = (Rule) vo;

		PreparedStatement stmt = null;

		String sql = "INSERT INTO " + getFullTableName(tableName)
				+ " (ID,NAME,ORGANIZATION_TYPE,ORGANIZATIONS,ORGANIZATIONS_TEXT,RANGES,LOCATIONS_TEXT,DOMAIN_ID,MULTI_PERIOD) values (?,?,?,?,?,?,?,?,?)";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, rule.getId());
			stmt.setString(2, rule.getName());
			stmt.setInt(3, rule.getOrganizationType());
			stmt.setString(4, rule.getOrganizations());
			stmt.setString(5, rule.getOrganizationsText());
			stmt.setInt(6, rule.getRange());
			stmt.setString(7, rule.getLocationsText());
			stmt.setString(8, rule.getDomainid());
			stmt.setBoolean(9, rule.getMultiPeriod());
			stmt.execute();
			createOrUpdateRuleLocationSet(rule);
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
			Rule rule = null;
			if (rs.next()) {
				rule = new Rule();
				setProperties(rule, rs);
			}
			rs.close();
			return rule;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
	}

	public void remove(String pk) throws Exception {
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;

		String sql = "DELETE FROM " + getFullTableName(tableName)
				+ " WHERE ID=?";

		String sql2 = "DELETE FROM " + getFullTableName("AM_RULE_LOCATION_SET")
				+ " WHERE RULE_ID=?";
		
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, pk);
			log.debug(sql);
			stmt.execute();
			stmt2 = connection.prepareStatement(sql2);
			stmt2.setString(1, pk);
			log.debug(sql2);
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
			ConnectionManager.closeStatement(stmt2);
		}

	}

	public ValueObject update(ValueObject vo) throws Exception {
		Rule rule = (Rule) vo;
		PreparedStatement stmt = null;

		String sql = "UPDATE " + getFullTableName(tableName)
				+ " SET NAME=?,ORGANIZATION_TYPE=?,ORGANIZATIONS=?,ORGANIZATIONS_TEXT=?,RANGES=?,LOCATIONS_TEXT=?,DOMAIN_ID=? ,MULTI_PERIOD=? WHERE ID=?";

		log.debug(sql);
		try {
			stmt = connection.prepareStatement(sql);

			stmt.setString(1, rule.getName());
			stmt.setInt(2, rule.getOrganizationType());
			stmt.setString(3, rule.getOrganizations());
			stmt.setString(4, rule.getOrganizationsText());
			stmt.setInt(5, rule.getRange());
			stmt.setString(6, rule.getLocationsText());
			stmt.setString(7, rule.getDomainid());
			stmt.setBoolean(8, rule.getMultiPeriod());
			stmt.setString(9, rule.getId());
			stmt.execute();
			createOrUpdateRuleLocationSet(rule);
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
		Collection<Rule> rtn = new ArrayList<Rule>();

		PreparedStatement stmt = null;

		String sql = "SELECT * FROM " + getFullTableName(tableName)
				+ " WHERE DOMAIN_ID=? ORDER BY ID DESC";

		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, user.getDomainid());
			ResultSet rs = stmt.executeQuery();
			Rule rule = null;
			while (rs.next()) {
				rule = new Rule();
				setProperties(rule, rs);
				rtn.add(rule);
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
	
	public DataPackage<?> query(ParamsTable params,WebUser user) throws Exception {
		
		DataPackage<Rule> datas = new DataPackage<Rule>();
		Collection<Rule> list = new ArrayList<Rule>();
		
		int page = params.getParameterAsInteger("page");
		int lines = params.getParameterAsInteger("rows");

		PreparedStatement stmt = null;
		PreparedStatement countStmt = null;

		String sql = "SELECT * FROM " + getFullTableName(tableName)
				+ " WHERE DOMAIN_ID=? ";
		
		String countSQL =  "SELECT count(*) FROM " + getFullTableName(tableName)
				+ " WHERE DOMAIN_ID=?";

		sql = buildLimitString(sql, page, lines, "ID", "DESC");
		log.info(sql);
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, user.getDomainid());
			ResultSet rs = stmt.executeQuery();
			Rule rule = null;
			while (rs.next()) {
				rule = new Rule();
				setProperties(rule, rs);
				list.add(rule);
			}
			datas.setDatas(list);
			rs.close();
			
			countStmt = connection.prepareStatement(countSQL);
			countStmt.setString(1, user.getDomainid());
			ResultSet cr = countStmt.executeQuery();
			if(cr.next()){
				datas.setRowCount(cr.getInt(1));
			}
			cr.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
			ConnectionManager.closeStatement(countStmt);
		}
		return datas;
	} 
	
	private void createOrUpdateRuleLocationSet(Rule rule) throws Exception{
		PreparedStatement deleteStmt = null;
		PreparedStatement stmt = null;
		
		String deleteSql = "DELETE FROM " + getFullTableName("AM_RULE_LOCATION_SET")
				+ " WHERE RULE_ID=?";
				

		String sql = "INSERT INTO "
				+ getFullTableName("AM_RULE_LOCATION_SET")
				+ " (RULE_ID,LOCATION_ID,DOMAIN_ID) values (?,?,?)";
		
		try {
			log.info(deleteSql);
			deleteStmt = connection.prepareStatement(deleteSql);
			deleteStmt.setString(1, rule.getId());
			deleteStmt.execute();
			
			if(rule.getLocations().isEmpty()) return;
			stmt = connection.prepareStatement(sql);
			log.info(sql);
			for(Location location : rule.getLocations()){
				stmt.setString(1, rule.getId());
				stmt.setString(2, location.getId());
				stmt.setString(3, rule.getDomainid());
				stmt.addBatch();
			}
			stmt.executeBatch();
		} catch (Exception e) {
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
			ConnectionManager.closeStatement(deleteStmt);
		}
	}
	
	public ValueObject findBySQL(String sql) throws Exception {
		Statement stmt = null;


		log.info(sql);
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(sql);
			Rule rule = null;
			if (rs.next()) {
				rule = new Rule();
				setProperties(rule, rs);
			}
			rs.close();
			return rule;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.closeStatement(stmt);
		}
	}

	void setProperties(Rule rule, ResultSet rs) throws Exception {
		try {
			rule.setId(rs.getString("ID"));
			rule.setName(rs.getString("NAME"));
			rule.setOrganizationType(rs.getInt("ORGANIZATION_TYPE"));
			rule.setOrganizations(rs.getString("ORGANIZATIONS"));
			rule.setOrganizationsText(rs.getString("ORGANIZATIONS_TEXT"));
			rule.setRange(rs.getInt("RANGES"));
			rule.setLocationsText(rs.getString("LOCATIONS_TEXT"));
			rule.setDomainid(rs.getString("DOMAIN_ID"));
			rule.setMultiPeriod(rs.getBoolean("MULTI_PERIOD"));
		} catch (SQLException e) {
			throw e;
		}

	}

}
