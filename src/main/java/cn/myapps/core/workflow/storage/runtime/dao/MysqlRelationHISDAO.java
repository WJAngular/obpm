package cn.myapps.core.workflow.storage.runtime.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 
 * @author Chris
 * 
 */
public class MysqlRelationHISDAO extends AbstractRelationHISDAO implements
		RelationHISDAO {

	public MysqlRelationHISDAO(Connection conn) throws Exception {
		super(conn);
		dbTag = "MY SQL: ";
		if (conn != null) {
			try {
				this.schema = conn.getMetaData().getURL().trim().toUpperCase();
				if (this.schema.indexOf("?USE") > 0) {
					this.schema = this.schema.substring(this.schema
							.lastIndexOf("/") + 1, this.schema.indexOf("?USE"));
				} else {
					this.schema = this.schema.substring(this.schema
							.lastIndexOf("/") + 1);
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

}
