package cn.myapps.core.dynaform.document.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.dynaform.document.dql.MysqlSQLFunction;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.util.DbTypeUtil;
import cn.myapps.util.ObjectUtil;

public class MysqlDocStaticTblDAO extends AbstractDocStaticTblDAO implements DocumentDAO {
	public final static Logger log = Logger.getLogger(MysqlDocStaticTblDAO.class);

	public MysqlDocStaticTblDAO(Connection conn, String applicationId) throws Exception {
		super(conn, applicationId);
		dbType = "MY SQL: ";
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_MYSQL);
		sqlFuction = new MysqlSQLFunction();
	}

	/**
	 * 生成限制条件sql.
	 * 
	 * @param sql
	 *            sql语句
	 * @param page
	 *            当前页码
	 * @param lines
	 *            每页显示行数
	 * @return 生成限制条件sql语句字符串
	 */
	public String buildLimitString(String sql, int page, int lines) {
		if (lines == Integer.MAX_VALUE) {
			return sql;
		}

		int to = (page - 1) * lines;
		StringBuffer pagingSelect = new StringBuffer(100);
		String orderb = getOrderBy(sql.toUpperCase());
		pagingSelect.append("SELECT * FROM (");
		pagingSelect.append(sql);
		pagingSelect.append(" ) AS TB "+orderb+" LIMIT " + to + "," + lines);

		return pagingSelect.toString();
	}
	
	/**
	 * 从sql中获取order by语句(order by语句含表别名的，去掉表别名)
	 * @param sql
	 * @return order by语句(去掉表别名)
	 */
	private String getOrderBy(String sql){
		String rtn = "";
		int orderIndex;
		if((orderIndex = sql.indexOf(" ORDER BY ")) >= 0){
			String lastPart = sql.substring(orderIndex);
			int index = getFristRightParenthesis(lastPart);
			if(index > 0){
				String orderStr = lastPart.substring(0, index);
				StringBuffer alias = new StringBuffer();
				char [] orderChars = orderStr.toCharArray();
				boolean hasAlias = false;
				for (int i = 0; i < orderChars.length; i++) {
					if(orderChars[i] != ' ' && orderChars[i] != '('){
						if(orderChars[i] == '.')
							alias.append("\\");
						alias.append(orderChars[i]);
						if(orderChars[i] == '.'){
							hasAlias = true;
							break;
						}
					}else{
						alias.setLength(0);
					}
				}
				if(!hasAlias){
					alias.setLength(0);
				}
				rtn = orderStr.replaceAll(alias.toString(), "");
			}
		}
		return rtn;
	}
	
	/**
	 * 获取字串因缺少“(”的第一个“)”的下标,找不到返回字串长度
	 * @param str
	 * @return
	 */
	private int getFristRightParenthesis(String str){
		char [] chars = str.toCharArray();
		int index = 0;
		int num = 0;
		boolean find = false;
		for (int i = 0; i < chars.length; i++) {
			if(chars[i] == ')' && num == 0){
				index = i;
				find = true;
				break;
			}
			if(chars[i] == '(')
				num++;
			if(chars[i] == ')')
				num--;
		}
		if(find){
			return index;
		}
		return str.length();
	}

	public void setBaseProperties(Document doc, ResultSet rs) throws Exception {
		ResultSetMetaData metaData = rs.getMetaData();
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			String colName = metaData.getColumnLabel(i);
			if (!colName.startsWith("ITEM_")) {
				try {
					switch (metaData.getColumnType(i)) {

					case Types.LONGNVARCHAR:
					case Types.LONGVARCHAR:
					case Types.CLOB:
						ObjectUtil.setProperty(doc, colName, rs.getString(colName), false);
						break;
					case Types.CHAR:
					case Types.VARCHAR:
						ObjectUtil.setProperty(doc, colName, rs.getString(colName), false);
						break;

					case Types.NUMERIC:
					case Types.TINYINT:
					case Types.BIT:
					case Types.INTEGER:
						if (metaData.getPrecision(i) > 1 && metaData.getScale(i) == 0) {
							ObjectUtil.setProperty(doc, colName, Integer.valueOf(rs.getInt(colName)), false);
						} else if (metaData.getPrecision(i) == 1 && metaData.getScale(i) == 0) {
							ObjectUtil.setProperty(doc, colName, (rs.getInt(colName) == 0 ? Boolean.valueOf(false) : Boolean
									.valueOf(true)), false);
						} else {
							ObjectUtil.setProperty(doc, colName, new Double(rs.getDouble(colName)), false);
						}
						break;
					case Types.DECIMAL:
					case Types.DOUBLE:
						if (metaData.getScale(i) == 0) {
							ObjectUtil.setProperty(doc, colName, Integer.valueOf(rs.getInt(colName)), false);
						} else {
							ObjectUtil.setProperty(doc, colName, new Double(rs.getDouble(colName)), false);
						}

						break;

					case Types.FLOAT:
						ObjectUtil.setProperty(doc, colName, new Float(rs.getFloat(colName)), false);
						break;

					case Types.REAL:
					case Types.BOOLEAN:
						ObjectUtil.setProperty(doc, colName, Boolean.valueOf(rs.getBoolean(colName)), false);
						break;
					case Types.DATE:
					case Types.TIME:
					case Types.TIMESTAMP:
						ObjectUtil.setProperty(doc, colName, rs.getTimestamp(colName), false);
						break;
					default:
						ObjectUtil.setProperty(doc, colName, rs.getObject(colName), false);
					}
				} catch (Exception e) {
					log.warn(colName + " error: " + e.getMessage());
				}
				if("author_dept_index".equalsIgnoreCase(colName)){
					doc.setAuthorDeptIndex(rs.getString("author_dept_index"));
				}
			}
		}

		if (doc.getForm() != null && doc.getForm().getType() == Form.FORM_TYPE_NORMAL) {
			doc.setMappingId(doc.getId());
		}

		doc.setItems(createItems(rs, doc));
	}

	protected void setItemValue(ResultSet rs, int i, Item item) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columnType = metaData.getColumnType(i);

		// Set Item's type and value
		switch (columnType) {

		case Types.LONGNVARCHAR:
		case Types.LONGVARCHAR:
		case Types.CLOB:
			item.setType(Item.VALUE_TYPE_TEXT);
			item.setTextvalue(rs.getString(i));
			break;

		case Types.CHAR:
		case Types.VARCHAR:
			item.setType(Item.VALUE_TYPE_VARCHAR);
			item.setVarcharvalue(rs.getString(i));
			break;

		case Types.NUMERIC:
		case Types.INTEGER:
		case Types.DECIMAL:
		case Types.DOUBLE:
		case Types.FLOAT:
		case Types.BOOLEAN:
		case Types.REAL:
			item.setType(Item.VALUE_TYPE_NUMBER);
			item.setNumbervalue(new Double(rs.getDouble(i)));
			break;

		case Types.DATE:
		case Types.TIME:
		case Types.TIMESTAMP:
			item.setType(Item.VALUE_TYPE_DATE);
			try {
				item.setDatevalue(rs.getTimestamp(i));
			} catch (Exception e) {
				item.setDatevalue(null);
			}

			break;

		default:
			item.setType(Item.VALUE_TYPE_VARCHAR);
			item.setVarcharvalue(rs.getString(i));
		}
	}

	public boolean isExist(String id) throws Exception {
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT COUNT(*) FROM " + getFullTableName(_TBNAME) + " doc WHERE doc.ID=?";

			statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			rs = statement.executeQuery();

			if (rs.next()) {
				return rs.getLong(1) > 0;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
		return false;
	}
	
	public String getOrderFieldsToSqlString(String[] sortCols, String tName){
		String sortCol = "";
		for (int i = 0; i < sortCols.length; i++) {
			if (sortCols[i] != null && sortCols[i].trim().length() > 0) {
				String colsName = sortCols[i].substring(0, sortCols[i].indexOf(" "));//获取排序字段名
				sortCols[i] = compatibleFormat(sortCols[i]);//兼容原来的拼装格式
				if (tName != null && !tName.equals("")) {
					String field = tName + "." + colsName;
					if (i == sortCols.length - 1) {
						if(sortCols[i].endsWith("01")){
							if(sortCols[i].endsWith(" ASC01")){
								sortCol += "CONVERT(" + field + " USING gbk) COLLATE gbk_chinese_ci ASC";
							}else{
								sortCol += "CONVERT(" + field + " USING gbk) COLLATE gbk_chinese_ci DESC";
							}
						}else if(sortCols[i].endsWith("00")){
							sortCol += field + " " + sortCols[i].substring(sortCols[i].indexOf(" "), sortCols[i].length()-2);
						}else{
							sortCol += field + " " + sortCols[i].substring(sortCols[i].indexOf(" "));
						}
					} else {
						if(sortCols[i].endsWith("01")){
							if(sortCols[i].endsWith(" ASC01")){
								sortCol += "CONVERT(" + field + " USING gbk) COLLATE gbk_chinese_ci ASC,";
							}else{
								sortCol += "CONVERT(" + field + " USING gbk) COLLATE gbk_chinese_ci DESC,";
							}
						}else if(sortCols[i].endsWith("00")){
							sortCol += field + " " + sortCols[i].substring(sortCols[i].indexOf(" "), sortCols[i].length()-2) + ",";
						}else{
							sortCol += field + " " + sortCols[i].substring(sortCols[i].indexOf(" ")) + ",";
						}
					}
				} else {
					String field = colsName;
					if (i == sortCols.length - 1) {
						if(sortCols[i].endsWith("01")){
							if(sortCols[i].endsWith(" ASC01")){
								sortCol += "CONVERT(" + field + " USING gbk) COLLATE gbk_chinese_ci ASC";
							}else{
								sortCol += "CONVERT(" + field + " USING gbk) COLLATE gbk_chinese_ci DESC";
							}
						}else if(sortCols[i].endsWith("00")){
							sortCol += field + " " + sortCols[i].substring(sortCols[i].indexOf(" "), sortCols[i].length()-2);
						}else{
							sortCol += field + " " + sortCols[i].substring(sortCols[i].indexOf(" "));
						}
					} else {
						if(sortCols[i].endsWith("01")){
							if(sortCols[i].endsWith(" ASC01")){
								sortCol += "CONVERT(" + field + " USING gbk) COLLATE gbk_chinese_ci ASC,";
							}else{
								sortCol += "CONVERT(" + field + " USING gbk) COLLATE gbk_chinese_ci DESC,";
							}
						}else if(sortCols[i].endsWith("00")){
							sortCol += field + " " + sortCols[i].substring(sortCols[i].indexOf(" "), sortCols[i].length()-2) + ",";
						}else{
							sortCol += field + " " + sortCols[i].substring(sortCols[i].indexOf(" ")) + ",";
						}
					}
				}
			}
		}
		return sortCol;
	}
	
	void appendLimitString(String sql, int page, int lines) {
		int to = (page - 1) * lines;
		sql = sql+"  LIMIT " + to + "," + lines;
		
	}
}
