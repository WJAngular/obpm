package cn.myapps.core.dynaform.dts.datasource.action;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSourceProcess;
import cn.myapps.core.dynaform.dts.datasource.ejb.metadata.TableMetadata;
import cn.myapps.core.dynaform.form.ejb.BaseFormProcessBean;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.FormProcessBean;
import cn.myapps.core.dynaform.form.ejb.FormTableProcessBean;
import cn.myapps.core.dynaform.form.ejb.mapping.TableMapping;
import cn.myapps.core.table.model.Column;
import cn.myapps.core.table.model.Table;
import cn.myapps.core.tree.Node;
import cn.myapps.util.DbTypeUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.http.ResponseUtil;
import cn.myapps.util.json.JsonUtil;


/**
 * @SuppressWarnings 此处的DataSourceAction类不能用泛型
 * @author nicholas
 */
@SuppressWarnings("unchecked")
public class DataSourceAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5819253404268664164L;

	private TableMetadata tableMetadata;
	private String selectId;

	public static Map _dblist = new HashMap();
	
	/**
	 * 数据源使用类型：jdbc/jndi
	 */
	public static Map _typelist = new LinkedHashMap();

	static {
		_dblist.put(0, "");
		_dblist.put(DataSource.DB_ORACLE, "Oracle");
		_dblist.put(DataSource.DB_SQLSERVER, "SQLServer");
		_dblist.put(DataSource.DB_DB2, "DB2");
		_dblist.put(DataSource.DB_MYSQL, "MYSQL");
		_dblist.put(DataSource.DB_HSQL, "HSQL");
		
		_typelist.put(DataSource.TYPE_JDBC, DataSource.TYPE_JDBC);
		_typelist.put(DataSource.TYPE_JNDI, DataSource.TYPE_JNDI);
	}

	public DataSourceAction() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(DataSourceProcess.class),
				new DataSource());
	}

	/**
	 * @return the _dblist
	 * @uml.property name="_dblist"
	 */
	public Map get_dblist() {
		return _dblist;
	}
	
	/**
	 * @param _dblist
	 *            the _dblist to set
	 * @uml.property name="_dblist"
	 */
	public void set_dblist(Map _dblist) {
		DataSourceAction._dblist = _dblist;
	}
	

	public static Map get_typelist() {
		return _typelist;
	}

	public static void set_typelist(Map _typelist) {
		DataSourceAction._typelist = _typelist;
	}

	public TableMetadata getTableMetadata() {
		return this.tableMetadata;
	}

	public String getSelectId() {
		return selectId;
	}

	public void setSelectId(String selectId) {
		this.selectId = selectId;
	}

	public void setTableMetadata(TableMetadata tableMetadata) {
		this.tableMetadata = tableMetadata;
	}

	protected boolean validatIsvalid(DataSource dataSource) {
		Connection conn = null;
		try {
			if (DataSource.TYPE_JDBC.equals(dataSource.getUseType())) {
				Class.forName(dataSource.getDriverClass()).newInstance();
				conn = DriverManager.getConnection(dataSource.getUrl(), dataSource
						.getUsername(), dataSource.getPassword());
				if (StringUtil.isBlank(DbTypeUtil.getSchema(conn, dataSource
						.getDbTypeName()))) {
					this.addFieldError("no_datasource", "{*[No_Database]*}");
					return false;
				}
				return true;
			} else if (DataSource.TYPE_JNDI.equals(dataSource.getUseType())) {
				conn = dataSource.getJNDIConnection();
				if (StringUtil.isBlank(DbTypeUtil.getSchema(conn, dataSource
						.getDbTypeName()))) {
					this.addFieldError("no_datasource", "{*[No_Database]*}");
					return false;
				}
				return true;
			}	
		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[cn.myapps.core.dynaform.dts.datasource.validat_is_valid]*}", e.getMessage() ,e));
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 保存
	 * 
	 * @return
	 */
	public String doSave() {
		boolean flag = false;
		DataSource dataSource = (DataSource) (this.getContent());
		/*
		 * if(StringUtil.isBlank(dataSource.getApplicationid())){
		 * this.addFieldError("applicationidNotexist",
		 * "{*[save.current.application]*}"); return INPUT; }
		 */
		if (!validatIsvalid(dataSource))
			return INPUT;
		ParamsTable params = this.getParams();
		params.setParameter("t_name", getParams().getParameter("content.name"));
		params.setParameter("i_dbType", getParams().getParameter(
				"content.dbType"));
		try {
			DataPackage dataPackage = (DataPackage) process.doQuery(params);
			if (dataPackage.rowCount > 0) {
				Collection collection = dataPackage.datas;
				for (Iterator ite = collection.iterator(); ite.hasNext();) {
					DataSource dataSource1 = (DataSource) ite.next();
					if (dataSource1 != null) {
						if (dataSource1.getId() == null
								|| dataSource1.getId().trim().length() <= 0) {// 判断新建不能重名
							this.addFieldError("1", "{*[DataSourceExist]*}");
							flag = true;
							break;
						} else if (dataSource1.getDbType() == dataSource
								.getDbType()
								&& !dataSource1.getId().trim()
										.equalsIgnoreCase(dataSource.getId())) {// 修改不能重名
							this.addFieldError("1", "{*[DataSourceExist]*}");
							flag = true;
							break;
						}
					}
				}
			}
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

		if (!flag) {
			return super.doSave();
		} else {
			return INPUT;
		}
	}

	public String doDelete() {
		
		return super.doDelete();
	}

	public String doList() {
		try {
			String application = getParams()
					.getParameterAsString("application");
			String s_application = getParams().getParameterAsString(
					"s_application");
			if ((application == null || application.trim().equals(""))
					&& (s_application == null || s_application.trim()
							.equals(""))) {
				this.setDatas(new DataPackage());
			} else {
				return super.doList();
			}
			return SUCCESS;
		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	/**
	 * 获取当前应用的所有datasource,并转化为树节点形式(json表达格式)
	 * 
	 * 2.6新增
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doGetMetadata() throws Exception {
		String application = getParams().getParameterAsString("application");
		String subNodes = getParams().getParameterAsString("subNodes");
		Collection<Node> childrenNodes = new ArrayList<Node>();
		if (subNodes == null || "".equals(subNodes)) {
			if (!StringUtil.isBlank(application)) {
				DataSourceProcess dp = (DataSourceProcess) ProcessFactory
						.createProcess(DataSourceProcess.class);
				Collection<DataSource> dts = dp
						.doSimpleQuery(null, application);
				Iterator<DataSource> it = dts.iterator();
				while (it.hasNext()) {
					DataSource ds = it.next();
					ApplicationProcess ap = (ApplicationProcess) ProcessFactory
							.createProcess(ApplicationProcess.class);
					ApplicationVO app = (ApplicationVO) ap.doView(application);
					if (ds != null) {
						Node node = new Node();
						node.setId(ds.getId());
						node.setData(ds.getName());//
						node.addAttr("name", ds.getName());
						node.addAttr("appication", ds.getApplicationid());
						node.addAttr("datasourceId", ds.getId());
						node.addAttr("curNode", "isDts");
						if (app != null
								&& app.getDatasourceid().equals(ds.getId())) {
							node.addAttr("rel", "db_selected");
						} else {
							node.addAttr("rel", "db");
						}
						node.addAttr("subNodes", "isTables");
						node.setState(Node.STATE_CLOSED);
						childrenNodes.add(node);
					}
				}
			}
		} else if ("isTables".equals(subNodes)) {
			FormProcess fp = (FormProcess) ProcessFactory
					.createProcess(FormProcess.class);
			Collection<Form> forms = fp.doSimpleQuery(null, application);
			// long e1 = System.currentTimeMillis();
			String datasourceId = getParams().getParameterAsString(
					"datasourceId");

			DataSourceProcess dp = (DataSourceProcess) ProcessFactory
					.createProcess(DataSourceProcess.class);
			DataSource ds = (DataSource) dp.doView(datasourceId);
			Collection<Table> tables = DbTypeUtil.getTables(null, ds
					.getDbTypeName(), ds.getConnection());
			// 指定数据源的所有的表集合
			Collection<String> table_names = new ArrayList<String>();
			Iterator<Table> it_table = tables.iterator();
			// 初始化指定数据源的所有的表
			while (it_table.hasNext()) {
				Table table = it_table.next();
				if (table != null) {
					table_names.add(table.getName());
				}
			}
			Iterator<Form> it = forms.iterator();
			while (it.hasNext()) {
				Form form = it.next();
				if (form != null) {
					TableMapping tableMapping = new TableMapping(form);
					// 过滤掉数据源(指定数据库)不存在的表<表单>
					if (table_names.contains(tableMapping.getTableName())) {
						Node node = new Node();
						node.setId(form.getId());
						node.setData(tableMapping.getTableName());//
						node.addAttr("name", tableMapping.getTableName());
						node.addAttr("formId", form.getId());
						node.addAttr("datasourceId", datasourceId);
						node.addAttr("curNode", "isTable");
						node.addAttr("rel", "table");
						node.addAttr("subNodes", "none");
						node.addAttr("appication", form.getApplicationid());
						node.setState(Node.STATE_CLOSED);
						childrenNodes.add(node);
					}
				}
			}
		}
		ResponseUtil.setJsonToResponse(ServletActionContext.getResponse(),
				JsonUtil.collection2Json(childrenNodes));
		return NONE;
	}

	/**
	 * 2.6新增
	 * 
	 * 浏览元数据
	 * 
	 * @return
	 */
	public String doViewMetadata() {
		this.tableMetadata = new TableMetadata();
		String datasourceId = getParams().getParameterAsString("datasourceId");
		String formId = getParams().getParameterAsString("formId");
		try {
			if (!StringUtil.isBlank(datasourceId)) {
				DataSourceProcess dp = (DataSourceProcess) ProcessFactory
						.createProcess(DataSourceProcess.class);
				DataSource ds = (DataSource) dp.doView(datasourceId);
				tableMetadata.setDatasource(ds);
			}
			if (!StringUtil.isBlank(formId)) {
				FormProcess fp = (FormProcess) ProcessFactory
						.createProcess(FormProcess.class);
				Form form = (Form) fp.doView(formId);
				tableMetadata.setForm(form);
			}
			return SUCCESS;
		} catch (OBPMValidateException e) {
			this.addFieldError("error", "error"+e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	/**
	 * 2.6新增
	 * 
	 * 校验数据源是否被应用引用
	 * 
	 * @return
	 */
	private boolean validateDataSourceBeUsed() {
		try {
			if (!StringUtil.isBlank(selectId)) {
				this._selects = selectId.split(";");
				if (this._selects != null) {
					ApplicationProcess apb = (ApplicationProcess) ProcessFactory
							.createProcess(ApplicationProcess.class);
					ApplicationVO applicationVO = (ApplicationVO) apb
							.doView(application);
					DataSource dataSource = applicationVO.getDataSourceDefine();
					for (int i = 0; i < _selects.length; i++) {
						if (_selects[i].equals(dataSource.getId())) {
							this.addFieldError("{*[DataSource]*}", dataSource
									.getName()
									+ " {*[Resource.has.been.cited]*}");
							return true;
						}
					}
				} else {
					this.addFieldError("{*[DataSource]*}",
							"{*[please.select.datasource]*}");
					return true;
				}
			} else {
				this.addFieldError("{*[DataSource]*}",
						"{*[please.select.datasource]*}");
				return true;
			}

		} catch (OBPMValidateException e) {
			this.addFieldError("error", e.getValidateMessage());
			return true;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return true;
		}
		return false;
	}

	/**
	 * 2.6新增
	 * 
	 * 删除选择的数据源
	 * 
	 * @return
	 */
	public String doDeleteDataSource() {
		if (validateDataSourceBeUsed()) {
			return INPUT;
		}
		return super.doDelete();
	}

	/**
	 * 2.6新增
	 * 
	 * 同表单数据结构
	 * 
	 * @return
	 */
	public String doSysFormAndTable() {
		String datasourceId = getParams().getParameterAsString("datasourceId");
		String formId = getParams().getParameterAsString("formId");
		try {
			if (!StringUtil.isBlank(formId)
					&& !StringUtil.isBlank(datasourceId)) {
				FormProcess fp = new FormProcessBean();
				BaseFormProcessBean<Form> bp = (BaseFormProcessBean<Form>) fp;
				Form form = (Form) fp.doView(formId);
				Collection<Form> forms = new ArrayList<Form>();
				Collection<Form> superior_form = bp.getSuperiors(form);
				forms.add(form);
				forms.addAll(superior_form);
				DataSourceProcess dp = (DataSourceProcess) ProcessFactory
						.createProcess(DataSourceProcess.class);
				DataSource ds = (DataSource) dp.doView(datasourceId);
				Iterator<Form> it_f = forms.iterator();
				while (it_f.hasNext()) {
					Form f = it_f.next();
					if (f != null && ds != null) {
						TableMapping tableMapping = new TableMapping(f);
						Table table = DbTypeUtil.getTable(tableMapping
								.getTableName(), ds.getDbTypeName(), ds
								.getConnection());
						Collection<Column> columns = table.getColumns();
						Collection<String> columnNames = new ArrayList<String>();
						Iterator<Column> it = columns.iterator();
						while (it.hasNext()) {
							Column column = it.next();
							if (column != null) {
								columnNames.add(column.getName());
							}
						}
						Collection<FormField> fields = f.getFields();
						Collection<FormField> fields_old = new ArrayList<FormField>();
						Iterator<FormField> it_field = fields.iterator();
						while (it_field.hasNext()) {
							FormField field = it_field.next();
							if (field != null
									&& columnNames.contains(tableMapping
											.getColumnName(field.getName()))) {
								fields_old.add(field);
							}
						}
						Form form_old = (Form) f.clone();
						form_old.removeAllField(form_old.getAllFields());
						form_old.addAllField(fields_old);
						FormTableProcessBean ftp = new FormTableProcessBean(f
								.getApplicationid());
						ftp.setDatasourceId(datasourceId);
						ftp.synDynaTable(f, form_old);

					}
				}
				this.addActionMessage("{*[Synchronize.successful]*}");
				return SUCCESS;
			}
		} catch (OBPMValidateException e) {
			this.addFieldError("synFail",
					"{*[Synchronize.fail]*}[{*[Details]*}:" + e.getValidateMessage()
							+ "]");
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("synFail",
					"{*[Synchronize.fail]*}[{*[Details]*}:" + e.getMessage()
					+ "]",e));
			e.printStackTrace();
			return INPUT;
		}
		return INPUT;
	}

}
