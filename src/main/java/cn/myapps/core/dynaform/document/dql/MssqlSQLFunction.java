package cn.myapps.core.dynaform.document.dql;

public class MssqlSQLFunction extends AbstractSQLFunction implements
		SQLFunction {

	public String toChar(String field, String patten) {
		if (patten.trim().equalsIgnoreCase("yyyy-MM-dd")) {
			return "CONVERT(nvarchar(10), " + field + ", 120) ";
		}
		return "CONVERT(nvarchar(19), " + field + ", 120) ";
	}

	public String toDate(String field, String patten) {
		return "CONVERT(datetime, " + field + ", 120) ";
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
		StringBuffer pagingSelect = new StringBuffer(100);
		pagingSelect.append("SELECT TOP " + endline + " * FROM (");
		pagingSelect
				.append("SELECT ROW_NUMBER() OVER (ORDER BY DOMAINID) AS ROWNUMBER, TABNIC.* FROM (");
		pagingSelect.append(sql);
		pagingSelect.append(") TABNIC) TableNickname ");
		pagingSelect.append("WHERE ROWNUMBER>" + endline * (startline - 1));
		return pagingSelect.toString();
	}

}
