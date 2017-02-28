/*
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cn.myapps.core.dynaform.document.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.dynaform.document.dql.OracleSQLFunction;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.util.DbTypeUtil;
import cn.myapps.util.ObjectUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

/**
 * 本系统的Document的查询语句运用自定义的DQL语句.DQL语句类似HQL语句.
 * <p>
 * DQL查询语句语法为:$formname=formname(模块表单名)+ 查询条件;
 * 
 * 例1: 查询付款费用模块下payment的Document的一条为广东省广州市的记录.
 * <p>
 * formname="付款费用/payment";条件为w="and 省份='广东省' and 城市='广州'",条件用"and" 连接起来.
 * 此时的DQL语句为$formname=formname+w . 此处的"付款费用"为模块名,"payment"该模块下的表单名.((表单名与动态表名同名)
 * <p>
 * 系统会将上述所得的DQL转为hibernate的HQL, 最后得出的SQL语句为"SELECT Item_省份,item_城市 FROM
 * tlk_payment where item_省份='广东省' and item_城市='广州'".
 * tlk_payment为动态表名(表名规则为前缀"tlk"+表单名). (动态表的字段名为前缀"item_"+表单字段名).
 * <p>
 * 如果查询语句中的字列有Document的属性时.要加上"$"+属性名,如:$id,$formname.
 * 有Document属性字段的DQL:$formname="付款费用/payment and $id='1000' and
 * $childs.id='1111'";id,chinlds为Document的属性名. Document的属性名有如下: ID, PARENT,
 * LASTMODIFIED, FORMNAME, STATE, AUDITDATE, AUTHOR, CREATED, FORMID, ISTMP,
 * FLOWID, VERSIONS, SORTID, APPLICATIONID, STATEINT, STATELABEL ".
 * 
 * <p>
 * 若查询语句中的字列有item字段时直接写item名.如上述的省份,城市.$formname="付款费用/payment and 省份='广东省 and
 * 城市='广州'".省份,城市为ITEM字段.
 * 
 * 
 * @author Marky
 * 
 */
public class OracleDocStaticTblDAO extends AbstractDocStaticTblDAO implements DocumentDAO {
	public final static Logger log = Logger.getLogger(OracleDocStaticTblDAO.class);

	// Connection connection;

	public OracleDocStaticTblDAO(Connection conn, String applicationId) throws Exception {
		super(conn, applicationId);
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_ORACLE);
		sqlFuction = new OracleSQLFunction();
	}

	public boolean isExist(String id) throws Exception {
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT count(*) FROM " + getFullTableName("T_DOCUMENT") + " doc WHERE doc.id=?";

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
	 * 生成限制条件sql.并把orderby 放到num之后
	 * 
	 * @param sql
	 *            sql语句
	 * @param page
	 *            当前页码
	 * @param lines
	 *            每页显示行数
	 * @param orderby
	 *            orderby 语句
	 * @return 生成限制条件sql语句字符串
	 */
	public String buildLimitString(String sql, String orderby, int page, int lines) {
		if (lines == Integer.MAX_VALUE) {
			return sql;
		}

		int from = (page - 1) * lines;
		int to = page * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		pagingSelect.append(sql);
		pagingSelect.append(" ) row_ where rownum <= ");
		pagingSelect.append(to);
		pagingSelect.append(") where rownum_ > ");
		pagingSelect.append(from);
		if (orderby != null && !orderby.trim().equals(""))
			pagingSelect.append(orderby);

		return pagingSelect.toString();
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
		return buildLimitString(sql, null, page, lines);
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
					case Types.INTEGER:
						if (metaData.getPrecision(i) > 1 && metaData.getScale(i) == 0) {
							ObjectUtil.setProperty(doc, colName, Integer.valueOf(rs.getInt(colName)), false);
						} else if (metaData.getPrecision(i) == 1 && metaData.getScale(i) == 0) {
							ObjectUtil.setProperty(doc, colName, (rs.getInt(colName) == 1 ? Boolean.valueOf(true) : Boolean
									.valueOf(false)), false);
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
						ObjectUtil.setProperty(doc, colName, new Double(rs.getFloat(colName)), false);
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

	/**
	 * 为预编译(PreparedStatement)Document的item. 根据item的值类型,设置相应的预编译参数.
	 * 
	 * @param ps
	 *            PreparedStatement
	 * @param doc
	 *            文档
	 * @param indx
	 *            参数次序
	 * @param fd
	 *            item
	 * @throws Exception
	 */
	protected void prepItemData(java.sql.PreparedStatement ps, Document doc,
			int indx, Item fd) throws Exception {

		try {

			String fieldType = fd.getType();

			if (fieldType.equals(Item.VALUE_TYPE_NUMBER)) { // Double
				Double value = fd.getNumbervalue();
				if (value == null || value.isNaN()) {
					ps.setDouble(indx, 0);
				} else {
					ps.setDouble(indx, value.doubleValue());
				}
				return;
			}

			if (fieldType.equals(Item.VALUE_TYPE_VARCHAR)) { // String
				String value = fd.getVarcharvalue();
				if (value == null)
					value = "";
				ps.setString(indx, value);
				return;
			}

			if (fieldType.equals(Item.VALUE_TYPE_DATE)) { // Date
				Date value = fd.getDatevalue();
				if (value == null) {
					ps.setNull(indx, Types.DATE);
				} else {
					ps.setTimestamp(indx, new Timestamp(value.getTime()));
				}

				return;
			}

			if (fieldType.equals(Item.VALUE_TYPE_TEXT)) { // TEXT
				String text = fd.getTextvalue();
				ps.setString(indx, StringUtil.encodeHTML(text));//need encode for Clob field
				return;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("table " + doc.getFormname()
					+ " ERR FIELD NAME->" + fd.getName());
			throw new OBPMValidateException("prepData Error!!!");
		}

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
			item.setTextvalue(StringUtil.dencodeHTML(rs.getString(i)));
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


	/**
	 * 生成order by 条件.
	 * 
	 * @param sql
	 *            sql语句
	 * @param params
	 *            参数
	 * @return 生成order by 条件sql语句字符串
	 */
	protected String bulidOrderString(String sql, ParamsTable params) {
		StringBuffer buffer = new StringBuffer();
		String[] sortCols = null;
		String sortStatus = "";
		String orderby = "";

		if (params != null) {
			if(params.getParameter("_sortCol") != null && !params.getParameter("_sortCol").equals("")){
				sortCols = (String[]) params.getParameter("_sortCol");
			}
			sortStatus = (String) params.getParameter("_sortStatus");
			orderby = (String) params.getParameterAsString("_orderby");
		}

		buffer.append("SELECT * FROM (" + sql + ") ordtb");
		if (sortCols != null && sortCols.length > 0) {
			buffer.append(" ORDER BY " + this.getOrderFieldsToSqlString(sortCols, "ordtb" ,params));
			buffer.append(", ordtb.ID");
		} else if (orderby != null && orderby.trim().length() > 0) {
			buffer.append(" ORDER BY ordtb." + orderby + " ");
			buffer.append(StringUtil.isBlank(sortStatus) ? "" : " " + sortStatus); // 增加排序方式,
			// ASC、DESC
			buffer.append(", ordtb.ID");
		} else if (-1 == sql.toUpperCase().indexOf("ORDER BY")) {
			buffer.append(" ORDER BY ordtb.ID");
		}

		return buffer.toString();
	}
	
	protected String getProcessingSQL(String actorId) {
		String processingSQL = "select distinct doc.id DOCID,doc.formid FORMID,fs.flowid FLOWID,pen.flowname FLOWNAME,pen.lastprocesstime LASTPROCESSTIME,pen.firstprocesstime FIRSTPROCESSTIME,doc.statelabel STATELABEL,to_char(substr(pen.summary,1,1000)) as SUBJECT,to_char(substr(doc.AUDITORNAMES,1,2000)) as AUDITORNAMES,to_char(substr(doc.AUDITORLIST,1,2000)) as AUDITORLIST,doc.APPLICATIONID,doc.DOMAINID "
				+ " from  "
				+ getFullTableName(_TBNAME)
				+ "  doc,"
				+ getFullTableName("t_flow_intervention")
				+ " pen,"
				+ getFullTableName("t_flowstatert")
				+ " fs,"
				+ getFullTableName("t_nodert")
				+ " node,"
				+ getFullTableName("t_actorrt")
				+ " actor "
				+ " where "
				+ "doc.id = pen.id and doc.id =fs.docid and fs.id =node.flowstatert_id and  node.id = actor.nodert_id and "
				+ "doc.parent is null and doc.state is not null and "
				+ "doc.statelabel is not null "
				+ " and actor.actorid in ('"
				+ actorId
				+ "')";

		return processingSQL;
	}
	
	protected String getProcessedSQL(String actorId) {
		String processedSQL = "select distinct doc.id DOCID,doc.formid FORMID,fs.flowid FLOWID,rhis.flowname FLOWNAME,pen.lastprocesstime LASTPROCESSTIME,pen.firstprocesstime FIRSTPROCESSTIME,doc.statelabel STATELABEL,to_char(substr(pen.summary,1,1000)) as SUBJECT,to_char(substr(doc.AUDITORNAMES,1,2000)) as AUDITORNAMES,to_char(substr(doc.AUDITORLIST,1,2000)) as AUDITORLIST,doc.APPLICATIONID,doc.DOMAINID "
			+ " from  "
			+ getFullTableName(_TBNAME)
			+ "  doc,"
			+ getFullTableName("t_actorhis")
			+ " ahis,"
			+ getFullTableName("t_relationhis")
			+ " rhis,"
			+ getFullTableName("t_flow_intervention")
			+ " pen,"
			+ getFullTableName("t_flowstatert")
			+ " fs"
			+ " where "
			+ "doc.id=rhis.docid and rhis.id = ahis.nodehis_id and doc.id = pen.id and doc.id =fs.docid and "
			+ "doc.parent is null and doc.state is not null and "
			+ "doc.statelabel is not null "
			+ " and ahis.actorid in ('"
			+ actorId
			+ "')";

	return processedSQL;
	}
	
	public String getProcessingCountSql(String actorId) {
		String processingCountSql = "select pen.docid from (select distinct docid,flowid from "
			+ getFullTableName("t_nodert")
			+ " where id in (select nodert_id from "
			+ getFullTableName("t_actorrt")
			+ " where actorid = '"+actorId+"')) fs,"
			+ getFullTableName("t_flow_intervention")
			+ " pen where fs.docid = pen.docid";
		
		return processingCountSql;
	}
	
	public String getProcessedCountSql(String actorId) {
		String processedCountSql = "select pen.docid from (select distinct docid,flowid from "
				+ getFullTableName("t_relationhis")
				+ " where id in (select nodehis_id from "
				+ getFullTableName("t_actorhis")
				+ " where actorid = '"+actorId+"')) fs,"
				+ getFullTableName("t_flow_intervention")
				+ " pen where fs.docid = pen.docid";
		
		return processedCountSql;
	}
	
	/**
	 * sortCols 排序字段数组 tName 表的别名
	 * 
	 */
	public String getOrderFieldsToSqlString(String[] sortCols, String tName ,ParamsTable params){
		String sortCol = "";
		String viewid = params.getParameterAsString("_viewid");
		Form form = null;
		try {//获取视图相关表单
			ViewProcess vp =(ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			View view = (View)vp.doView(viewid);
			FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			form = (Form) fp.doView(view.getRelatedForm());
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < sortCols.length; i++) {
			if (sortCols[i] != null && sortCols[i].trim().length() > 0) {
				String colsName = sortCols[i].substring(0, sortCols[i].indexOf(" "));//获取排序字段名
				sortCols[i] = compatibleFormat(sortCols[i]);//兼容原来的拼装格式
				if (tName != null && !tName.equals("")) {
					String addCast = tName + "." + colsName;
					if (form != null) {
						Collection<FormField> colls = form.getAllFields();
						for (Iterator<FormField> iter = colls.iterator(); iter.hasNext();) {
							FormField field = (FormField) iter.next();
							//大文本类型字段处理
							if ((colsName.equalsIgnoreCase("ITEM_"+field.getName()) && Item.VALUE_TYPE_TEXT.equals(field.getFieldtype()))
									|| colsName.equalsIgnoreCase("AuditorNames")) {
								addCast = "cast(" + addCast + " as varchar2(4000))";
								break;
							}
						}
					}
					if (i == sortCols.length - 1) {
						if(sortCols[i].endsWith("01")){
							if(sortCols[i].endsWith(" ASC01")){
								sortCol += "CONVERT(" + addCast + ",'ZHS16GBK','UTF8') ASC";
							}else{
								sortCol += "CONVERT(" + addCast + ",'ZHS16GBK','UTF8') DESC";
							}
						}else if(sortCols[i].endsWith("00")){
							sortCol += addCast + " " + sortCols[i].substring(sortCols[i].indexOf(" "), sortCols[i].length()-2);
						}else{
							sortCol += addCast + " " + sortCols[i].substring(sortCols[i].indexOf(" "));
						}
					} else {
						if(sortCols[i].endsWith("01")){
							if(sortCols[i].endsWith(" ASC01")){
								sortCol += "CONVERT(" + addCast + ",'ZHS16GBK','UTF8') ASC,";
							}else{
								sortCol += "CONVERT(" + addCast + ",'ZHS16GBK','UTF8') DESC,";
							}
						}else if(sortCols[i].endsWith("00")){
							sortCol += addCast + " " + sortCols[i].substring(sortCols[i].indexOf(" "), sortCols[i].length()-2) + ",";
						}else{
							sortCol += addCast + " " + sortCols[i].substring(sortCols[i].indexOf(" ")) + ",";
						}
					}
				} else {
					String addCast = colsName;
					if (form != null) {
						Collection<FormField> colls = form.getAllFields();
						for (Iterator<FormField> iter = colls.iterator(); iter.hasNext();) {
							FormField field = (FormField) iter.next();
							//大文本类型字段处理
							if ((colsName.equalsIgnoreCase("ITEM_"+field.getName()) && Item.VALUE_TYPE_TEXT.equals(field.getFieldtype()))
									|| colsName.equalsIgnoreCase("AuditorNames")) {
								addCast = "cast(" + addCast + " as varchar2(4000))";
								break;
							}
						}
					}
					if (i == sortCols.length - 1) {
						if(sortCols[i].endsWith("01")){
							if(sortCols[i].endsWith(" ASC01")){
								sortCol += "CONVERT(" + addCast + ",'ZHS16GBK','UTF8') ASC";
							}else{
								sortCol += "CONVERT(" + addCast + ",'ZHS16GBK','UTF8') DESC";
							}
						}else if(sortCols[i].endsWith("00")){
							sortCol += addCast + " " + sortCols[i].substring(sortCols[i].indexOf(" "), sortCols[i].length()-2);
						}else{
							sortCol += addCast + " " + sortCols[i].substring(sortCols[i].indexOf(" "));
						}
					} else {
						if(sortCols[i].endsWith("01")){
							if(sortCols[i].endsWith(" ASC01")){
								sortCol += "CONVERT(" + addCast + ",'ZHS16GBK','UTF8') ASC,";
							}else{
								sortCol += "CONVERT(" + addCast + ",'ZHS16GBK','UTF8') DESC,";
							}
						}else if(sortCols[i].endsWith("00")){
							sortCol += addCast + " " + sortCols[i].substring(sortCols[i].indexOf(" "), sortCols[i].length()-2) + ",";
						}else{
							sortCol += addCast + " " + sortCols[i].substring(sortCols[i].indexOf(" ")) + ",";
						}
					}
				}
			}
		}
		return sortCol;
	}
	
	/**
	 * 通过存储过程查询
	 * 
	 * 2.6版本新增存储过程调用方法
	 * 
	 * @param procedure
	 * @param params
	 * @param page
	 * @param lines
	 * @param domainid
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Document> queryByProcedure(final String procedure, final ParamsTable params, int page,
			int lines, String domainid) throws Exception {
		CallableStatement statement = null;
		ResultSet rs = null;
		Map<Integer,Object> map = null;
		try {
			ArrayList<Document> datas = new ArrayList<Document>();

			// 解析procedure字符串,解析成{call procedureName(?,?,?)}形式
			String proce = parseProcedure(procedure);
			// 预编译存储过程proce
			statement = connection.prepareCall(proce);
			// 输出日志
			log.debug(dbType + proce);
			// 解析参数,返回的Map<参数index,返回类型>
			if (proce.indexOf('(') > -1 && proce.indexOf(')') > -1) {
				map = parseParametersInOracle(statement, procedure, params, page, lines, domainid);
			}
			// 运行该存储过程
			statement.execute();
			if(map != null && map.size() > 0){
				Set<Map.Entry<Integer, Object>> set = map.entrySet();
		        for (Iterator<Map.Entry<Integer, Object>> it = set.iterator(); it.hasNext();) {
		            Map.Entry<Integer, Object> entry = (Map.Entry<Integer, Object>) it.next();
		            if("package".equals(entry.getValue())){
		    			// 处理返回结果
		    			rs = (ResultSet) statement.getObject(entry.getKey());
		            }
		        }
			}

			while (rs != null && rs.next()) {
				Document doc = new Document();
				setBaseProperties(doc, rs);
				datas.add(doc);
			}

			DataPackage<Document> dpg = new DataPackage<Document>();
			sort(datas, params);

			ArrayList<Document> datasNeed = new ArrayList<Document>();
			if (!procedure.contains("#lines")) {
				for (int i = 0; i < lines; i++) {
					int index = i + (page - 1) * lines;
					if (index < datas.size())
						datasNeed.add(datas.get(index));
					else
						break;
				}
			}
			dpg.datas = datasNeed;
			dpg.rowCount = datas.size();
			dpg.linesPerPage = lines;
			dpg.pageNo = page;
			return dpg;

		} catch (SQLException e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
	}
	
	public long countByProcedure(String procedure, String domainid)
			throws Exception {
		CallableStatement statement = null;
		ResultSet rs = null;
		Map<Integer, Object> map = null;

		// 解析procedure字符串,解析成{call procedureName(?,?,?)}形式,mssql解析成exec
		// procedureName(?,?,?)形式
		String proce = parseProcedure(procedure);
		// 预编译存储过程proce
		statement = connection.prepareCall(proce,
				ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		// 输出日志
		log.debug(dbType + proce);
		// 解析参数
		if (proce.indexOf('(') > -1 && proce.indexOf(')') > -1) {
			map = parseParametersInOracle(statement, procedure,
					new ParamsTable(), 1, Integer.MAX_VALUE, domainid);
		}
		// 运行该存储过程
		statement.execute();
		if (map != null && map.size() > 0) {
			Set<Map.Entry<Integer, Object>> set = map.entrySet();
			for (Iterator<Map.Entry<Integer, Object>> it = set.iterator(); it
					.hasNext();) {
				Map.Entry<Integer, Object> entry = (Map.Entry<Integer, Object>) it
						.next();
				if ("package".equals(entry.getValue())) {
					// 处理返回结果
					rs = (ResultSet) statement.getObject(entry.getKey());
				}
			}
		}
		// 处理返回结果
		int rowCount = 0;
		while (rs != null && rs.next()) {
			rowCount++;
		}
		return rowCount;
	}
	
	/**
	 * 解析存储过程脚本的参数
	 * 
	 * 2.6版本新增的方法
	 * 
	 * @param statement
	 * @param procedure
	 * @param params
	 * @param page
	 * @param lines
	 * @param domainid
	 * @throws Exception
	 */
	private Map<Integer,Object> parseParametersInOracle(CallableStatement statement, String procedure, ParamsTable params, int page,
			int lines, String domainid) throws Exception {
		Map<Integer,Object> map = new HashMap<Integer, Object>();
		int index1 = procedure.indexOf("(");
		int index2 = procedure.lastIndexOf(")");
		String parameters = procedure.substring(index1 + 1, index2);
		String[] paramsAsArray = parameters.split(",");
		if (paramsAsArray != null && paramsAsArray.length > 0) {
			for (int i = 0; i < paramsAsArray.length; i++) {
				String[] p = paramsAsArray[i].split(":");
				if (p != null && p.length == 2) {
					String type = p[0].trim();
					String value = p[1].trim();
					if ("String".equalsIgnoreCase(type)) {
						// #curDomain,#sortCol,#sortStatus,#orderby为系统自带变量,用户定义时不能与此相冲突
						if ("#curDomain".equals(value)) {
							statement.setString(i + 1, domainid);
						} else if ("#sortCol".equals(value)) {
							statement.setString(i + 1, params.getParameterAsString("_sortCol"));
						} else if ("#sortStatus".equals(value)) {
							statement.setString(i + 1, params.getParameterAsString("_sortStatus"));
						} else if ("#orderby".equals(value)) {
							statement.setString(i + 1, params.getParameterAsString("_orderby"));
						} else {
							statement.setString(i + 1, value);
						}
					} else if ("int".equalsIgnoreCase(type)) {
						// #curPage为系统自带变量,用户定义时不能与此相冲突
						if ("#curPage".equals(value)) {
							statement.setInt(i + 1, page);
						} else if ("#lines".equals(value)) {// #lines为系统自带变量,用户定义时不能与此相冲突
							statement.setInt(i + 1, lines);
						} else {
							statement.setInt(i + 1, Integer.valueOf(value));
						}
					} else if ("Date".equalsIgnoreCase(type) || "java.sql.Date".equalsIgnoreCase(type)) {
						statement.setDate(i + 1, java.sql.Date.valueOf(value));
					} else if ("Time".equalsIgnoreCase(type) || "java.sql.Time".equalsIgnoreCase(type)) {
						statement.setTime(i + 1, java.sql.Time.valueOf(value));
					} else if ("Timestamp".equalsIgnoreCase(type) || "java.sql.Timestamp".equalsIgnoreCase(type)) {
						statement.setTimestamp(i + 1, java.sql.Timestamp.valueOf(value));
					} else if ("double".equalsIgnoreCase(type)) {
						statement.setDouble(i + 1, Double.valueOf(value));
					} else if ("float".equalsIgnoreCase(type)) {
						statement.setFloat(i + 1, Float.valueOf(value));
					} else if ("long".equalsIgnoreCase(type)) {
						statement.setLong(i + 1, Long.valueOf(value));
					} else if("out".equalsIgnoreCase(type)){
						//注册输出参数
						if("package".equalsIgnoreCase(value)){
							statement.registerOutParameter(i + 1, oracle.jdbc.OracleTypes.CURSOR);
							map.put(i + 1, "package");
						}
					} else {
						throw new OBPMValidateException("not support type [" + type + "]");
					}
				}else if(p != null){
					throw new OBPMValidateException("parameter [" + paramsAsArray[i] + "] format is not correct");
				}
			}
		}
		return map;
	}
	
	void appendLimitString(String sql, int page, int lines) {
		sql = buildLimitString(sql, page, lines);
		
	}
}
