package cn.myapps.qm.base.dao;

import java.sql.Connection;

import cn.myapps.pm.util.ConnectionManager;
import cn.myapps.qm.answer.dao.DB2AnswerDAO;
import cn.myapps.qm.answer.dao.MsSqlAnswerDAO;
import cn.myapps.qm.answer.dao.MySqlAnswerDAO;
import cn.myapps.qm.answer.dao.OracleAnswerDAO;
import cn.myapps.qm.questionnaire.dao.DB2QuestionnaireDAO;
import cn.myapps.qm.questionnaire.dao.MsSqlQuestionnaireDAO;
import cn.myapps.qm.questionnaire.dao.MySqlQuestionnaireDAO;
import cn.myapps.qm.questionnaire.dao.OracleQuestionnaireDAO;

public class DaoManager {
	

	public static BaseDAO getQuestionnaireDAO(Connection conn) throws Exception{
		String dbType = ConnectionManager.dbType;
		
		if("MSSQL".equals(dbType)){  
			return new MsSqlQuestionnaireDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleQuestionnaireDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2QuestionnaireDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlQuestionnaireDAO(conn);
		}
		
		return null;
	}
	
	public static BaseDAO getAnswer(Connection conn) throws Exception{
		String dbType = ConnectionManager.dbType;
		
		if("MSSQL".equals(dbType)){  
			return new MsSqlAnswerDAO(conn);
		}else if("ORACLE".equals(dbType)){
			return new OracleAnswerDAO(conn);
		}else if("DB2".equals(dbType)){
			return new DB2AnswerDAO(conn);
		}else if("MYSQL".equals(dbType)){
			return new MySqlAnswerDAO(conn);
		}
		
		return null;
	}
	
	
	
	
}
