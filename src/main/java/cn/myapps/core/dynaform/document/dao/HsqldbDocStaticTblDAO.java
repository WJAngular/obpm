package cn.myapps.core.dynaform.document.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.dynaform.document.dql.HsqldbSQLFunction;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.util.DbTypeUtil;
import cn.myapps.util.ObjectUtil;

public class HsqldbDocStaticTblDAO extends AbstractDocStaticTblDAO implements
		DocumentDAO {
	public final static Logger log = Logger
			.getLogger(HsqldbDocStaticTblDAO.class);

	public HsqldbDocStaticTblDAO(Connection conn, String applicationId)
			throws Exception {
		super(conn, applicationId);
		dbType = "HypersonicSQL: ";
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_HSQLDB);
		sqlFuction = new HsqldbSQLFunction();
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
		int ind = sql.indexOf("ORDER BY");
		String orderby = "";
		if (ind > 0) {
			orderby = sql.substring(ind);
			sql = sql.substring(0, ind);
		}

		sql = sql.substring(6);

		pagingSelect.append("SELECT LIMIT " + to + " " + lines + "");
		pagingSelect.append(sql);
		pagingSelect.append(orderby);

		return pagingSelect.toString();
	}

	public void setBaseProperties(Document doc, ResultSet rs) throws Exception {
		ResultSetMetaData metaData = rs.getMetaData();
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			String colName = metaData.getColumnName(i);
			if (!colName.startsWith("ITEM_")) {
				try {
					switch (metaData.getColumnType(i)) {
					
					case Types.LONGNVARCHAR:
					case Types.LONGVARCHAR:
					case Types.CLOB:
						ObjectUtil.setProperty(doc, colName, rs
								.getString(colName), false);
						break;
					case Types.CHAR:
					case Types.VARCHAR:
						ObjectUtil.setProperty(doc, colName, rs
								.getString(colName), false);
						break;

					case Types.NUMERIC:
					case Types.INTEGER:
					case Types.BIT:
						if (metaData.getPrecision(i) > 1
								&& metaData.getScale(i) == 0) {
							ObjectUtil.setProperty(doc, colName, Integer
									.valueOf(rs.getInt(colName)), false);
						} else if (metaData.getPrecision(i) == 1
								&& metaData.getScale(i) == 0) {
							ObjectUtil.setProperty(doc, colName, (rs
									.getInt(colName) == 0 ? Boolean
									.valueOf(false) : Boolean.valueOf(true)),
									false);
						} else {
							ObjectUtil.setProperty(doc, colName, new Double(rs
									.getDouble(colName)), false);
						}
						break;
					case Types.DECIMAL:
					case Types.DOUBLE:
						if (metaData.getScale(i) == 0) {
							ObjectUtil.setProperty(doc, colName, Integer
									.valueOf(rs.getInt(colName)), false);
						} else {
							ObjectUtil.setProperty(doc, colName, new Double(rs
									.getDouble(colName)), false);
						}
						break;

					case Types.FLOAT:
						ObjectUtil.setProperty(doc, colName, new Float(rs
								.getFloat(colName)), false);
						break;

					case Types.REAL:
					case Types.BOOLEAN:
						ObjectUtil.setProperty(doc, colName, Boolean.valueOf(rs
								.getBoolean(colName)), false);
						break;
					case Types.DATE:
					case Types.TIME:
					case Types.TIMESTAMP:
						ObjectUtil.setProperty(doc, colName, rs
								.getTimestamp(colName), false);
						break;
					default:
						ObjectUtil.setProperty(doc, colName, rs
								.getObject(colName), false);
					}
				} catch (Exception e) {
					log.warn(colName + " error: " + e.getMessage());
				}
				if("author_dept_index".equalsIgnoreCase(colName)){
					doc.setAuthorDeptIndex(rs.getString("author_dept_index"));
				}
			}
		}

		if (doc.getForm() != null
				&& doc.getForm().getType() == Form.FORM_TYPE_NORMAL) {
			doc.setMappingId(doc.getId());
		}

		doc.setItems(createItems(rs, doc));
	}

	protected void setItemValue(ResultSet rs, int i, Item item)
			throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columnType = metaData.getColumnType(i);

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
			item.setDatevalue(rs.getTimestamp(i));
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
			String sql = "SELECT count(*) FROM " + getFullTableName(_TBNAME)
					+ " doc WHERE doc.id=?";

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
	
	/**
	 * sortCols 排序字段数组 tName 表的别名
	 * **/
	public String getOrderFieldsToSqlString(String[] sortCols, String tName) {
		String sortCol = "";
		for (int i = 0; i < sortCols.length; i++) {
			if (sortCols[i] != null && sortCols[i].trim().length() > 0) {
				sortCols[i] = compatibleFormat(sortCols[i]);//兼容原来的拼装格式
				//Hsqldb不支持按中文拼音排序,去掉列属性sortStandard("00","01")
				if(sortCols[i].endsWith("00") || sortCols[i].endsWith("01")){
					sortCols[i] = sortCols[i].substring(0, sortCols[i].length()-2);
				}
				if (tName != null && !tName.equals("")) {
					if (i == sortCols.length - 1) {
						sortCol += tName + "." + sortCols[i];
					} else {
						sortCol += tName + "." + sortCols[i] + ",";
					}
				} else {
					if (i == sortCols.length - 1) {
						sortCol += sortCols[i];
					} else {
						sortCol += sortCols[i] + ",";
					}
				}
			}
		}
		return sortCol;
	}
	
	void appendLimitString(String sql, int page, int lines) {
		sql = buildLimitString(sql, page, lines);
		
	}
}
