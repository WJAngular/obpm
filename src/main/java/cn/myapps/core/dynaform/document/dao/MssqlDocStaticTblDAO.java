/*
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cn.myapps.core.dynaform.document.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOException;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.dynaform.document.dql.MssqlSQLFunction;
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
public class MssqlDocStaticTblDAO extends AbstractDocStaticTblDAO implements
		DocumentDAO {
	public final static Logger log = Logger
			.getLogger(MssqlDocStaticTblDAO.class);

	public MssqlDocStaticTblDAO(Connection conn, String applicationId)
			throws Exception {
		super(conn, applicationId);
		dbType = "MS SQL Server: ";
		this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_MSSQL);
		sqlFuction = new MssqlSQLFunction();
	}

	/**
	 * 根据符合SQL执行语句以及应用标识查询并分页,返回文档的数据集合.
	 * 
	 * @param sql
	 * @param params
	 * @param page
	 *            当前页码
	 * @param lines
	 *            每页显示行数
	 * 
	 * @return 文档的数据集合
	 * @throws Exception
	 * @throws SQLException
	 * @throws DAOException
	 */
	public Collection<Document> queryBySQL(String sql, int page, int lines,
			String domainid) throws Exception, SQLException, DAOException {
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			ArrayList<Document> datas = new ArrayList<Document>();

			sql = buildLimitString(sql, page, lines);
			log.debug(dbType + sql);
			statement = connection.prepareStatement(sql);

			rs = statement.executeQuery();

			int databaseVersion = connection.getMetaData()
					.getDatabaseMajorVersion();
			if (9 <= databaseVersion) {
			} else {
				// JDBC1.0
				long emptylines = 1L * (page - 1) * lines;
				for (int i = 0; i < emptylines && rs.next(); i++) {
					// keep empty
				}
				// if (page>1)
				// rs.absolute((page-1)* lines); //JDBC2.0
			}

			for (int i = 0; i < lines && rs.next(); i++) {
				Document doc = new Document();
				setBaseProperties(doc, rs);
				datas.add(doc);
			}

			return datas;

		} catch (SQLException e) {
			throw e;
		} finally {
			PersistenceUtils.closeStatement(statement);
		}
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
	 * @throws SQLException
	 */
	public String buildLimitString(String sql, int page, int lines)
			throws SQLException {
		if (lines == Integer.MAX_VALUE) {
			return sql;
		}

		//在分页之前先排序
		StringBuffer buffer = new StringBuffer();
		String orderb = getOrderBy(sql.toString());
		if(!StringUtil.isBlank(orderb)){
			buffer.append("SELECT TOP ").append(Integer.MAX_VALUE).append(" * FROM (");
			buffer.append(sql);
			buffer.append(") tb_orderby ").append(orderb);
			sql=buffer.toString();
		}
		// int to = (page - 1) * lines;
		StringBuffer pagingSelect = new StringBuffer(100);

		int databaseVersion = connection.getMetaData()
				.getDatabaseMajorVersion();
		if (9 <= databaseVersion) {// 2005 row_number() over () 分页
			pagingSelect.append("SELECT TOP " + lines + " * FROM (");
			pagingSelect
					.append("SELECT ROW_NUMBER() OVER (ORDER BY DOMAINID) AS ROWNUMBER, TABNIC.* FROM (");
			pagingSelect.append(sql);
			pagingSelect.append(") TABNIC) TableNickname ");
			pagingSelect.append("WHERE ROWNUMBER>" + lines * (page - 1));

		} else {
			pagingSelect.append("SELECT TOP " + lines * page + " * FROM (");
			pagingSelect.append(sql);
			pagingSelect.append(") TABNIC");
		}
		
		/*//添加limit后重设order by语句,以保证排序正确（分页后排序）
		String orderby = getOrderBy(pagingSelect.toString());
		if(!StringUtil.isBlank(orderby)){
			String tempSql = pagingSelect.toString();
			pagingSelect.setLength(0);
			pagingSelect.append("SELECT TOP ").append(Integer.MAX_VALUE).
				append(" * FROM (").append(tempSql).append(") TABNIC ").append(orderby);
		}*/

		return pagingSelect.toString();
	}

	/**
	 * sortCols 排序字段数组 tName 表的别名
	 * **/
//	public String getOrderFieldsToSqlString(String[] sortCols, String tName) {
//		String sortCol = "";
//		for (int i = 0; i < sortCols.length; i++) {
//			if (sortCols[i] != null && sortCols[i].trim().length() > 0) {
//				if (tName != null && !tName.equals("")) {
//					if (i == sortCols.length - 1) {
//						if(sortCols[i].contains("AuditorNames")){
//							sortCol += "cast(" + tName + "." + sortCols[i].substring(0, 13) + "as varchar(5000)) " + sortCols[i].substring(13);
//						}else{
//							sortCol += tName + "." + sortCols[i];
//						}
//					} else {
//						if(sortCols[i].contains("AuditorNames")){
//							sortCol += "cast(" + tName + "." + sortCols[i].substring(0, 13) + "as varchar(5000)) " + sortCols[i].substring(13) + ",";
//						}else{
//							sortCol += tName + "." + sortCols[i] + ",";
//						}
//					}
//				} else {
//					if (i == sortCols.length - 1) {
//						sortCol += sortCols[i];
//					} else {
//						sortCol += sortCols[i] + ",";
//					}
//				}
//			}
//		}
//		return sortCol;
//	}
	
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
		String[] sortCols =null;
		String sortStatus = "";
		String orderby = "";

		if (params != null) {
			if(params.getParameter("_sortCol") != null && !params.getParameter("_sortCol").equals("")){
				sortCols = (String[]) params.getParameter("_sortCol");
			}
			sortStatus = (String) params.getParameter("_sortStatus");
			orderby = (String) params.getParameterAsString("_orderby");
		}

		buffer.append("SELECT top " + Integer.MAX_VALUE + " * FROM (" + sql
				+ ") ordtb");

		if (sortCols != null && sortCols.length> 0) {
			buffer.append(" ORDER BY ");
			buffer.append(this.getOrderFieldsToSqlString(sortCols, "ordtb", params));
			boolean flag = true;
			for(int i=0;i<sortCols.length;i++){
				if(sortCols[i].startsWith("Id")){
					flag = false;
				}
			}
			if(flag){
				buffer.append(", ordtb.ID");
			}
			
		} else if (orderby != null && orderby.trim().length() > 0) {
			buffer.append(" ORDER BY ordtb." + orderby);
			buffer.append(StringUtil.isBlank(sortStatus) ? "" : " "
					+ sortStatus); // 增加排序方式,
			// ASC、DESC
			buffer.append(", ordtb.ID");
		} 
		/*else if (-1 == sql.toUpperCase().indexOf("ORDER BY")) {
			buffer.append(" ORDER BY ordtb.ID");
		}*/

		return buffer.toString();
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

	public void setItemValue(ResultSet rs, int i, Item item)
			throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columnType = metaData.getColumnType(i);

		// Set Item's type and value
		switch (columnType) {

		case Types.LONGVARCHAR:
		case Types.LONGNVARCHAR:
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
			String sql = "SELECT COUNT(*) _ROWCOUNT FROM "
					+ getFullTableName(_TBNAME) + " doc WHERE doc.ID=?";

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
	 * 获取处理中SQL语句
	 * 
	 * @param actorId
	 * @return
	 */
	protected String getProcessingSQL(String actorId) {
		String processingSQL = "select distinct doc.id DOCID,doc.formid FORMID,fs.flowid FLOWID,pen.flowname FLOWNAME,pen.lastprocesstime LASTPROCESSTIME,pen.firstprocesstime FIRSTPROCESSTIME,doc.statelabel STATELABEL,convert(varchar(1000),pen.summary) as SUBJECT,convert(varchar(2000),doc.AUDITORNAMES) as AUDITORNAMES,convert(varchar(2000),doc.AUDITORLIST) as AUDITORLIST,doc.APPLICATIONID,doc.DOMAINID "
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
		String processedSQL = "select distinct doc.id DOCID,doc.formid FORMID,fs.flowid FLOWID,rhis.flowname FLOWNAME,pen.lastprocesstime LASTPROCESSTIME,pen.firstprocesstime FIRSTPROCESSTIME,doc.statelabel STATELABEL,convert(varchar(1000),pen.summary) as SUBJECT,convert(varchar(2000),doc.AUDITORNAMES) as AUDITORNAMES,convert(varchar(2000),doc.AUDITORLIST) as AUDITORLIST,doc.APPLICATIONID,doc.DOMAINID "
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
								addCast = "cast(" + addCast + " as varchar(5000))";
								break;
							}
							//日期类型字段处理
							if ((colsName.equalsIgnoreCase("ITEM_"+field.getName()) && Item.VALUE_TYPE_DATE.equals(field.getFieldtype()))
									|| colsName.equalsIgnoreCase("Created") || colsName.equalsIgnoreCase("AuditDate") || colsName.equalsIgnoreCase("LastModified")) {
								if(sortCols[i].endsWith("01"))
									sortCols[i] = sortCols[i].substring(0, sortCols[i].length()-2);
								break;
							}
						}
					}
					if (i == sortCols.length - 1) {
						if(sortCols[i].endsWith("01")){
							if(sortCols[i].endsWith(" ASC01")){
								sortCol += addCast + " COLLATE Chinese_PRC_CS_AS_KS_WS ASC";
							}else{
								sortCol += addCast + " COLLATE Chinese_PRC_CS_AS_KS_WS DESC";
							}
						}else if(sortCols[i].endsWith("00")){
							sortCol += addCast + " " + sortCols[i].substring(sortCols[i].indexOf(" "), sortCols[i].length()-2);
						}else{
							sortCol += addCast + " " + sortCols[i].substring(sortCols[i].indexOf(" "));
						}
					} else {
						if(sortCols[i].endsWith("01")){
							if(sortCols[i].endsWith(" ASC01")){
								sortCol += addCast + " COLLATE Chinese_PRC_CS_AS_KS_WS ASC,";
							}else{
								sortCol += addCast + " COLLATE Chinese_PRC_CS_AS_KS_WS DESC,";
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
								addCast = "cast(" + addCast + " as varchar(5000))";
								break;
							}
							//日期类型字段处理
							if ((colsName.equalsIgnoreCase("ITEM_"+field.getName()) && Item.VALUE_TYPE_DATE.equals(field.getFieldtype()))
									|| colsName.equalsIgnoreCase("Created") || colsName.equalsIgnoreCase("AuditDate") || colsName.equalsIgnoreCase("LastModified")) {
								if(sortCols[i].endsWith("01"))
									sortCols[i] = sortCols[i].substring(0, sortCols[i].length()-2);
								break;
							}
						}
					}
					if (i == sortCols.length - 1) {
						if(sortCols[i].endsWith("01")){
							if(sortCols[i].endsWith(" ASC01")){
								sortCol += addCast + " COLLATE Chinese_PRC_CS_AS_KS_WS ASC";
							}else{
								sortCol += addCast + " COLLATE Chinese_PRC_CS_AS_KS_WS DESC";
							}
						}else if(sortCols[i].endsWith("00")){
							sortCol += addCast + " " + sortCols[i].substring(sortCols[i].indexOf(" "), sortCols[i].length()-2);
						}else{
							sortCol += addCast + " " + sortCols[i].substring(sortCols[i].indexOf(" "));
						}
					} else {
						if(sortCols[i].endsWith("01")){
							if(sortCols[i].endsWith(" ASC01")){
								sortCol += addCast + " COLLATE Chinese_PRC_CS_AS_KS_WS ASC,";
							}else{
								sortCol += addCast + " COLLATE Chinese_PRC_CS_AS_KS_WS DESC,";
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
	 * 解析iscript的存储过程脚本
	 * 
	 * 2.6新增的方法
	 * 
	 * @param procedure
	 * @return exec procedureName(?,?)形式的表达字符串
	 */
	@Override
	protected String parseProcedure(String procedure) throws Exception {
		StringBuffer p = new StringBuffer();
		if (!procedure.trim().startsWith("exec "))
			throw new OBPMValidateException("missing 'exec' at the beginning of [" + procedure + "]");
		int index1 = procedure.indexOf('(');
		int index2 = procedure.lastIndexOf(')');
		if (index1 < 0 && index2 > -1) {
			throw new OBPMValidateException("missing '(' on [" + procedure + "]");
		} else if (index1 > -1 && index2 < 0) {
			throw new OBPMValidateException("missing ')' on [" + procedure + "]");
		} else if (index1 > -1 && index2 > -1) {
			p.append(procedure.substring(procedure.indexOf('{') + 1, index1).trim());
			p.append('(');
			String[] parametersAsArray = procedure.substring(index1 + 1, index2).split(",");
			for (int i = 0; i < parametersAsArray.length; i++) {
				p.append('?');
				if (i < parametersAsArray.length - 1)
					p.append(',');
			}
			p.append(')');
		} else {
			p.append(procedure.substring(procedure.indexOf('{') + 1).trim());
		}

		return p.toString();
	}
	
	/**
	 * 增加domainid条件
	 * @param sql
	 * @param domainid
	 * @return
	 */
	protected String addDomainid(String sql, String domainid){
		if(!StringUtil.isBlank(sql)){
			sql = sql.toUpperCase();
			if (sql.lastIndexOf(" ORDER BY ") >= 0) {
				
				sql = this.addTop(sql);
				
			}
			sql = " SELECT * FROM (" + sql
				+ ") table_0 WHERE table_0.DOMAINID ='" + domainid + "'";
		}
		
		return sql;
	}
	
	protected String addDomainid4queryDOCBySQL(String sql, String domainid) {
		sql = this.addDomainid(sql, domainid);
		String orderby = getOrderBy(sql);
		return sql + orderby;
	}
	
	/**
	 * mssql数据库sql含有order by时为select语句添加top
	 * @param sql
	 * @return 添加top后的sql语句
	 */
	protected String addTop(String sql){
		sql = sql.toUpperCase();
		if (sql.lastIndexOf(" ORDER BY ") >= 0) {
			if(sql.lastIndexOf("*") >= 0){
				String tempSql = sql;
				int selectIndex;
				while((selectIndex = tempSql.indexOf("SELECT ")) >= 0){
					int fromIndex = tempSql.indexOf(" FROM ");
					if(fromIndex<=selectIndex){
						tempSql = tempSql.substring(fromIndex + 6);
						continue;
					}
					String str = tempSql.substring(selectIndex, fromIndex);
					
					String start = sql.substring(0, sql.indexOf(str));
					String end = sql.substring(sql.indexOf(str) + str.length());
					sql = start + changeSelect(str) + end;
					
					tempSql = tempSql.substring(fromIndex + 6);
				}
			}
		}
		return sql;
	}
	
	/**
	 * select语句添加top
	 * @param str
	 * @return
	 */
	private String changeSelect(String str){
		str = str.toUpperCase();
//		if(str.indexOf("*") > 0){
			if(str.indexOf(" TOP ") < 0){
				if(str.indexOf(" DISTINCT ") > 0){
					//修复当sql过滤模式有distinct关键词时错误
					str = str.replaceAll("SELECT DISTINCT", "SELECT DISTINCT TOP 100 PERCENT ");
				}else{
					str = str.replaceAll("SELECT ", "SELECT TOP 100 PERCENT ");
				}
				
			}
//		}
		return str;
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
	
	/**
	 * 我的工作添加排序
	 * @param sql
	 * @return
	 */
	protected String bulidWorkOrderString(String sql){
		if(StringUtil.isBlank(sql)){
			return sql;
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT TOP 2147483647 * FROM (");
		buffer.append(sql);
		buffer.append(") tb_orderby ORDER BY tb_orderby.LASTPROCESSTIME DESC");
		return buffer.toString();
	}
	
	void appendLimitString(String sql, int page, int lines) {
		try {
			sql = buildLimitString(sql, page, lines);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
