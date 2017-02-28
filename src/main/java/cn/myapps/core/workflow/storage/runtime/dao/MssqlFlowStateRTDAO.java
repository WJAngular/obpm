package cn.myapps.core.workflow.storage.runtime.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.myapps.base.dao.ValueObject;

/**
 * 
 * @author Chris
 * 
 */
public class MssqlFlowStateRTDAO extends AbstractFlowStateRTDAO implements
		FlowStateRTDAO {

	public MssqlFlowStateRTDAO(Connection conn) throws Exception {
		super(conn);
		dbTag = "MS SQL SERVER: ";
		try {
			ResultSet rs = conn.getMetaData().getSchemas();
			if (rs != null) {
				if (rs.next())
					this.schema = rs.getString(1).trim().toUpperCase();
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	public void create(ValueObject vo) throws Exception {
		create(vo, new MssqlNodeRTDAO(connection));
	}

	public void remove(String id) throws Exception {

		remove(id, new MssqlActorRTDAO(connection), new MssqlNodeRTDAO(
				connection));
	}

	public void update(ValueObject vo) throws Exception {

		update(vo, new MssqlNodeRTDAO(connection));
	}
}
