package cn.myapps.core.dynaform.printer.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.HibernateSQLUtils;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.action.FormHelper;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.IncludeField;
import cn.myapps.core.dynaform.form.ejb.IncludedView;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.printer.dao.PrinterDAO;
import cn.myapps.core.dynaform.printer.util.PrinterUtils;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.MemoryCacheUtil;


/**
 * @author Happy
 *
 */
public class PrinterProcessBean extends AbstractDesignTimeProcessBean<Printer> implements
		PrinterProcess {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 712365638612148766L;
	
	protected IDesignTimeDAO<Printer> getDAO() throws Exception {
		return (PrinterDAO) DAOFactory.getDefaultDAO(Printer.class.getName());
	}

	public String getFields(String formid) {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		try {
			map = (LinkedHashMap<String, String>) new FormHelper().getFields4Print(formid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		return PrinterUtils.getInstance().getFields(map);
	}

	public String getReportData(String id,String _formid,String _docid,String _flowid,WebUser user,ParamsTable params) throws Exception {
		Printer printer = (Printer)this.doView(id);
		//Document doc = (Document)MemoryCacheUtil.getFromPrivateSpace(_docid,user);
		DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,printer.getApplicationid());
		Document doc = (Document) dp.doView(_docid);
		if(doc == null){
			doc = (Document)MemoryCacheUtil.getFromPrivateSpace(_docid, user);
		}
		return PrinterUtils.getInstance().getReportData(printer.getTemplate(),doc,_formid,_flowid,user,params);
	}

	public Printer findByFormid(String formid) throws Exception {
		return ((PrinterDAO)getDAO()).findByFormId(formid);
	}

	/*根据模块id获取Print对象的集合
	 * @see cn.myapps.core.dynaform.printer.ejb.PrinterProcess#getPrinterByModule(java.lang.String)
	 */
	public Collection<Printer> getPrinterByModule(String moduleid) throws Exception {
		return ((PrinterDAO)getDAO()).getPrinterByModule(moduleid);
	}

	/* (non-Javadoc)
	 * @see cn.myapps.core.dynaform.printer.ejb.PrinterProcess#getSubViews(java.lang.String)
	 */
	public String getSubViews(String formid,IRunner runner) {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		try {
			FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			Form form = (Form) fp.doView(formid);
			if (form != null) {
				Collection<FormField> fields = form.getAllFields();
				for (Iterator<FormField> iter = fields.iterator(); iter.hasNext();) {
					FormField field = iter.next();
					if (field instanceof IncludeField) {
						IncludeField includeField = (IncludeField) field;
						if(includeField.getIncludeType().equals(IncludeField.INCLUDE_TYPE_VIEW)){
							IncludedView viewField = new IncludedView(includeField);
							View view = (View) viewField.getValueObject(runner);
							if (view != null) {
								map.put(view.getName(),view.getId());
							}
							
						}
						
					}
					
				}
			}

			} catch (Exception e) {
				e.printStackTrace();
			}
		
		return PrinterUtils.getInstance().getSubViews(map);
	}

	/* (non-Javadoc)
	 * @see cn.myapps.core.dynaform.printer.ejb.PrinterProcess#getViewDatas(cn.myapps.core.dynaform.view.ejb.View, java.lang.String, int)
	 */
	public DataPackage<Document> getViewDatas(View view,int lines,WebUser user,ParamsTable params) throws Exception{
		DataPackage<Document> datas = null;
		
		//params.setParameter("_sortStatus", view.getOrderFieldAndOrderTypeArr());
		String[] orderfields =view.getDefaultOrderFieldArr();
		params.setParameter("_sortCol",orderfields);
		
		
		
		DocumentProcess dp =(DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,(view.getApplicationid()));
		ViewProcess vp = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
		int page =1;
		String parentid = params.getParameterAsString("parentid");
		
		
		HibernateSQLUtils sqlUtils = new HibernateSQLUtils();
		if (view.getEditMode().equals(View.EDIT_MODE_DESIGN)) { // DESIGN
			String sql = vp.getQueryString(view, params,
					user, null);
			if (!StringUtil.isBlank(parentid)) {
				sql = sqlUtils.appendCondition(sql, "PARENT = '" + parentid
						+ "'");
			}

			if (view.isPagination())
					datas = dp.queryBySQLPage(sql, params, page, lines, user.getDomainid());
			else
					datas = dp.queryBySQL(sql, params, user.getDomainid());

		}
		
		else if (view.getEditMode().equals(View.EDIT_MODE_CODE_DQL)) { // CODE(DQL)
			String colSort = params.getParameterAsString("_sortCol");

			if (!StringUtil.isBlank(colSort)) {
				String relatedFormid = view.getRelatedForm();
				if (!StringUtil.isBlank(relatedFormid)) {
					FormProcess formProcess = (FormProcess) ProcessFactory
							.createProcess(FormProcess.class);
					Form relatedForm = (Form) formProcess.doView(relatedFormid);
					FormField field = relatedForm.findFieldByName(relatedForm
							.getTableMapping().getFieldName(colSort));
					if (field != null) {
						String fieldType = field.getFieldtype();
						params.setParameter("fieldType", fieldType);
					}
				}
			}

			String dql = getQueryString(view.getFilterScript(),view,params,user);
			if (dql != null && dql.trim().length() > 0) {
				if (!StringUtil.isBlank(parentid))
					dql += " and $parent.$id='" + parentid + "'";

				if (view.isPagination()) {
					datas = dp.queryByDQLPage(dql, params, page, lines,
							user.getDomainid());
				} else {
					datas = dp.queryByDQL(dql, params, user.getDomainid());
				}
			}
		} 
		
		else if (view.getEditMode().equals(View.EDIT_MODE_CODE_SQL)) { // CODE(SQL)
			String sql = getQueryString(view.getSqlFilterScript(),view,params,user);
			if (!StringUtil.isBlank(parentid) ) {
				sql = sqlUtils.appendCondition(sql, "PARENT = '" + parentid
						+ "'");
			}

			if (sql != null && sql.trim().length() > 0) {
				if (view.isPagination()) {
					datas = dp.queryBySQLPage(sql, params, page, lines,
							user.getDomainid());
				} else {
					datas = dp.queryBySQL(sql, params, user.getDomainid());
				}
			}
		}
		
		return datas;
	}
	
	private String getQueryString(String js,View view,ParamsTable params,WebUser user) throws Exception {

		StringBuffer label = new StringBuffer();
		label.append("VIEW(").append(view.getId())
				.append(")." + view.getName()).append(".FilterScript");
		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(),
				view.getApplicationid());
		runner.initBSFManager(null, params, user,
				new ArrayList<ValidateMessage>());
		Object result = runner.run(label.toString(), js);
		if (result != null && result instanceof String) {
			return (String) result;
		}
		return null;
	}

}