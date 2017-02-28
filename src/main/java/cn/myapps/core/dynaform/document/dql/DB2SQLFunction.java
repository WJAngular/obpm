package cn.myapps.core.dynaform.document.dql;

public class DB2SQLFunction extends AbstractSQLFunction implements SQLFunction {

	public String toChar(String field, String patten) {
		if (patten.trim().equalsIgnoreCase("yyyy-mm-dd")) {
			return "YEAR(" + field + ")-MONTH(" + field + ")-DAYOFMONTH("
					+ field + ")";
		}
		return "TO_CHAR(" + field + ",'yyyy-mm-dd')";
	}

	public String toDate(String field, String patten) {
		return "DATE(" + field + ")";
	}
	
	@Override
	public String addCast(String field) {
		return  "CAST(" + field + " AS VARCHAR(2000))";
	}

	/**
	 * 
	 * 获取分页写法
	 * 
	 * @param sql
	 *            ,开始行号,结束行号
	 * @param patten
	 *            格式
	 * @return 包含了分页写法的sql
	 */
	public String getLimitString(String sql, int startline, int endline) {
		return "SELECT * FROM ( SELECT ROW_.*, ROW_NUMBER() OVER() AS _ROW FROM ( "
				+ sql
				+ ") ROW_ ) WHERE _ROW < "
				+ endline
				+ " AND _ROW >= "
				+ startline;
	}

}
