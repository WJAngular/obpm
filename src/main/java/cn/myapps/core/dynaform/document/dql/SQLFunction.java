package cn.myapps.core.dynaform.document.dql;

public interface SQLFunction {

	/**
	 * 
	 * 组成日期转换函数，转换字段field为数据日期
	 * 
	 * @param field
	 *            字段名
	 * @param patten
	 *            格式
	 * @return 组成日期转换函数后的字串
	 */
	public String toDate(String field, String patten);

	/**
	 * 
	 * 组成字符串转换函数，转换字段field为数据为格式字符串
	 * 
	 * @param field
	 *            字段名
	 * @param patten
	 *            格式
	 * @return 转换字段field为数据为格式字符串
	 */
	public String toChar(String field, String patten);
	
	/**
	 * 
	 * 大字段类型转换函数，添加cast函数
	 * 
	 * @param field
	 *            字段名
	 *            
	 * @return 添加cast函数后的字串
	 */
	public String addCast(String field);

	/**
	 * 组成小写转换函数
	 * 
	 * @param s
	 * @return s字串小写转换后的字串
	 */
	public String lower(String s);

	/**
	 * 组成大写转换函数
	 * 
	 * @param s
	 * @return s字串大写转换后的字串
	 */
	public String upper(String s);

	/**
	 * 获取分页写法
	 * 
	 * @param sql
	 *            ,开始行号,结束行号
	 * @return 包含了分页写法的sql
	 */
	public String getLimitString(String sql, int startline, int endline);

	/**
	 * 获取查询条件中为空值的写法
	 * 
	 * @param
	 * @return
	 */
	public String getWhereClauseNullString(String coulumName);

}
