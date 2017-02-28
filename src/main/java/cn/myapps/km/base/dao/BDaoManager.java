package cn.myapps.km.base.dao;

import java.sql.Connection;

import cn.myapps.km.baike.category.dao.DB2CategoryDAO;
import cn.myapps.km.baike.category.dao.MssqlCategoryDAO;
import cn.myapps.km.baike.category.dao.MySqlCategoryDAO;
import cn.myapps.km.baike.category.dao.OracleCategoryDAO;
import cn.myapps.km.baike.content.dao.DB2EntryContentDAO;
import cn.myapps.km.baike.content.dao.DB2ReferenceMaterialDAO;
import cn.myapps.km.baike.content.dao.MssqlEntryContentDAO;
import cn.myapps.km.baike.content.dao.MssqlReferenceMaterialDAO;
import cn.myapps.km.baike.content.dao.MySqlEntryContentDAO;
import cn.myapps.km.baike.content.dao.MySqlReferenceMaterialDAO;
import cn.myapps.km.baike.content.dao.OracleEntryContentDAO;
import cn.myapps.km.baike.content.dao.OracleReferenceMaterialDAO;
import cn.myapps.km.baike.entry.dao.DB2EntryDAO;
import cn.myapps.km.baike.entry.dao.MssqlEntryDAO;
import cn.myapps.km.baike.entry.dao.MySqlEntryDAO;
import cn.myapps.km.baike.entry.dao.OracleEntryDAO;
import cn.myapps.km.baike.history.dao.DB2HistoryDAO;
import cn.myapps.km.baike.history.dao.HistoryDao;
import cn.myapps.km.baike.history.dao.MssqlHistoryDAO;
import cn.myapps.km.baike.history.dao.MySqlHistoryDAO;
import cn.myapps.km.baike.history.dao.OracleHistoryDAO;
import cn.myapps.km.baike.knowledge.dao.DB2KnowledgeAnswerDAO;
import cn.myapps.km.baike.knowledge.dao.DB2KnowledgeQuestionDAO;
import cn.myapps.km.baike.knowledge.dao.MssqlKnowledgeAnswerDAO;
import cn.myapps.km.baike.knowledge.dao.MssqlKnowledgeQuestionDAO;
import cn.myapps.km.baike.knowledge.dao.MySqlKnowledgeAnswerDAO;
import cn.myapps.km.baike.knowledge.dao.MySqlKnowledgeQuestionDAO;
import cn.myapps.km.baike.knowledge.dao.OracleKnowledgeAnswerDAO;
import cn.myapps.km.baike.knowledge.dao.OracleKnowledgeQuestionDAO;
import cn.myapps.km.baike.reason.dao.DB2RejectReasonDAO;
import cn.myapps.km.baike.reason.dao.MssqlRejectReasonDAO;
import cn.myapps.km.baike.reason.dao.MySqlRejectReasonDAO;
import cn.myapps.km.baike.reason.dao.OracleRejectReasonDAO;
import cn.myapps.km.baike.user.dao.DB2BUserAttributeDAO;
import cn.myapps.km.baike.user.dao.DB2BUserEntrySetDAO;
import cn.myapps.km.baike.user.dao.MssqlBUserAttributeDAO;
import cn.myapps.km.baike.user.dao.MssqlBUserEntrySetDAO;
import cn.myapps.km.baike.user.dao.MySqlBUserAttributeDAO;
import cn.myapps.km.baike.user.dao.MySqlBUserEntrySetDAO;
import cn.myapps.km.baike.user.dao.OracleBUserAttributeDAO;
import cn.myapps.km.baike.user.dao.OracleBUserEntrySetDAO;
import cn.myapps.km.util.NDataSource;

/**
 * @author jodg
 *
 */
public class BDaoManager {
	
	/**
	 * 根据数据源类型获取词条版本对应的DAO
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public static NRuntimeDAO getEntryVersionDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
		//	return new MssqlEntryVersionDAO(conn);
		}else if("ORACLE".equals(dbType)){
		//	return new OracleEntryVersionDAO(conn);
		}else if("DB2".equals(dbType)){
		//	return new DB2EntryVersionDAO(conn);
		}else if("MYSQL".equals(dbType)){
		//	return new MysqlEntryVersionDAO(conn);
		}
		return null;
	}
	
	/**
	 * 根据数据源类型获取词条对应的DAO
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public static NRuntimeDAO getEntryDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MssqlEntryDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleEntryDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2EntryDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlEntryDAO(conn);
		}
		return null;
	}
	
	/**
	 * 根据数据源类型获取词条内容对应的DAO
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public static NRuntimeDAO getEntryContentDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MssqlEntryContentDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleEntryContentDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2EntryContentDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlEntryContentDAO(conn);
		}
		return null;
	}
	
	
	/**
	 * 根据数据源类型获取知识问题对应的DAO
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public static NRuntimeDAO getKnowledgeQuestionDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MssqlKnowledgeQuestionDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleKnowledgeQuestionDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2KnowledgeQuestionDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlKnowledgeQuestionDAO(conn);
		}
		return null;
	}

	/**
	 * 根据数据源类型获取知识答案对应的DAO
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public static NRuntimeDAO getKnowledgeAnswerDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MssqlKnowledgeAnswerDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleKnowledgeAnswerDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2KnowledgeAnswerDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlKnowledgeAnswerDAO(conn);
		}
		return null;
	}
	
	/**
	 * 根据数据源类型获取用户属性对应的DAO
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public static NRuntimeDAO getBUserAttributeDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MssqlBUserAttributeDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleBUserAttributeDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2BUserAttributeDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlBUserAttributeDAO(conn);
		}
		return null;
	}
	
	
	/**
	 * 根据数据源类型获取用户词条关系对应的DAO
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public static NRuntimeDAO getBUserEntrySetDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MssqlBUserEntrySetDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleBUserEntrySetDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2BUserEntrySetDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlBUserEntrySetDAO(conn);
		}
		return null;
	}
	
	/**
	 * 根据数据源类型获取词条分类对应的DAO
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public static NRuntimeDAO getCategoryDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MssqlCategoryDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleCategoryDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2CategoryDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlCategoryDAO(conn);
		}
		return null;
	}

	public static NRuntimeDAO getReasonDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MssqlRejectReasonDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleRejectReasonDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2RejectReasonDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlRejectReasonDAO(conn);
		}
		return null;
	}

	public static HistoryDao getHistoryDAO(Connection conn) throws Exception {
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MssqlHistoryDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleHistoryDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2HistoryDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlHistoryDAO(conn);
		}
		return null;
	}
	
	/**
	 * 根据数据源类型获取参考资料对应的DAO
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public static NRuntimeDAO getReferenceMaterialDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MssqlReferenceMaterialDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleReferenceMaterialDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2ReferenceMaterialDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlReferenceMaterialDAO(conn);
		}
		return null;
	}
	/**
	 * 根据数据源类型获取参考驳回理由的DAO
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public static NRuntimeDAO getRejectReasonDAO(Connection conn) throws Exception{
		String dbType = NDataSource._dbTye;
		if("MSSQL".equals(dbType)){  
			return new MssqlRejectReasonDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleRejectReasonDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2RejectReasonDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlRejectReasonDAO(conn);
		}
		return null;
	}
	
}
