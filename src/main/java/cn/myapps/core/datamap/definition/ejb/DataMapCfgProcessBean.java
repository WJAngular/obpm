package cn.myapps.core.datamap.definition.ejb;

import java.util.Collection;
import java.util.Iterator;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.datamap.definition.dao.DataMapCfgDAO;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.Option;
import cn.myapps.core.dynaform.form.ejb.Options;
import cn.myapps.core.dynaform.form.ejb.SelectField;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class DataMapCfgProcessBean extends
		AbstractDesignTimeProcessBean<DataMapCfgVO> implements
		DataMapCfgProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8043657130211132007L;

	@Override
	protected IDesignTimeDAO<DataMapCfgVO> getDAO() throws Exception {
		return (DataMapCfgDAO) DAOFactory.getDefaultDAO(DataMapCfgVO.class.getName());
	}

	public String getClueFields(String datamapCfgId,ParamsTable params,WebUser user,String applicationId) throws Exception {
		FormProcess formProcess = (FormProcess)ProcessFactory.createProcess(FormProcess.class);
		DataMapCfgVO vo = (DataMapCfgVO) this.doView(datamapCfgId);
		
		Form form = (Form) formProcess.doView(vo.getRelatedForm());
		FormField clueField = null;
		Collection<FormField>  fields = form.getAllFields();
		for (Iterator<FormField> iterator = fields.iterator(); iterator.hasNext();) {
			FormField formField = (FormField) iterator.next();
			if(vo.getClueField().equals(formField.getName())){
				clueField = formField;
				break;
			}
		}
		StringBuffer sb = new StringBuffer();
		if(clueField != null && clueField instanceof SelectField){
			if(!StringUtil.isBlank(vo.getClueField2()) ){
				sb.append("<options isClueField2=\"true\">\n");
			}else{
				sb.append("<options>\n");
			}
			sb.append(buildClueFieldsXML((SelectField) clueField, params, user, applicationId));
			sb.append("</options>");
		}
		return sb.toString();
	}
	
	private String buildClueFieldsXML(SelectField field,ParamsTable params,WebUser user,String applicationId) throws Exception {
		StringBuffer xml = new StringBuffer();
		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), applicationId);
		runner.initBSFManager(null, params, user, null);
		Options options = getOptions(field, runner);
		if(options != null){
			Iterator<Option> iter = options.getOptions().iterator();
			while (iter.hasNext()) {
				Option element = (Option) iter.next();
				if (element.getValue() != null) {
					xml.append("<option key=\"").append(element.getOption()).append("\" value=\"").append(element.getValue()).append("\" />\n");
				}
			}
		}
		return xml.toString();
	}
	
	private Options getOptions(SelectField field,IRunner runner) throws Exception{
		Object result = null;
		Options options = null;
		if (field.getOptionsScript() != null && field.getOptionsScript().trim().length() > 0) {

			result = runner.run(field.getScriptLable("OptionsScript"), StringUtil.dencodeHTML(field.getOptionsScript()));
			if (result != null && result instanceof String) {

				String[] strlst = ((String) result).split(";");
				options = new Options();
				for (int i = 0; i < strlst.length; i++) {
					options.add(strlst[i], strlst[i]);
				}
			} else if (result instanceof Options) {
				options = (Options) result;
			}
		}
		return options;
	}

	public String getSummaryFields(String datamapCfgId, String clueValue,String clueValue2,String applicationId,String domainId)
			throws Exception {
		
		DocumentProcess docProcess = (DocumentProcess)ProcessFactory.createRuntimeProcess(DocumentProcess.class, applicationId);
//		ViewProcess viewProcess = (ViewProcess)ProcessFactory.createProcess(ViewProcess.class);
		FormProcess formProcess = (FormProcess)ProcessFactory.createProcess(FormProcess.class);
		DataMapCfgVO vo = (DataMapCfgVO) this.doView(datamapCfgId);
		
//		View view  = (View) viewProcess.doView(vo.getRelatedView());
		Form form = (Form) formProcess.doView(vo.getRelatedForm());
		String dql = "$formname='"+form.getFullName()+"' and "+vo.getClueField()+"='"+clueValue+"'";
		if(!StringUtil.isBlank(vo.getClueField2()) && !StringUtil.isBlank(clueValue2)){
			dql += " and " + vo.getClueField2() + " ='" + clueValue2 + "'";
		}
		DataPackage<Document> datas = docProcess.queryByDQLPage(dql, 1, Integer.MAX_VALUE, domainId);
		if(datas != null ){
			return buildSummaryFieldsXML(datas.datas, vo.getSummaryField());
		}
		
		return null;
	}
	
	private String buildSummaryFieldsXML(Collection<Document> docs,String summaryField) throws Exception {
		StringBuffer xml = new StringBuffer();
		xml.append("<documents>\n");
		for (Iterator<Document> iterator = docs.iterator(); iterator.hasNext();) {
			Document document = (Document) iterator.next();
			xml.append("<document summary=\"").append(document.getItemValueAsString(summaryField)).append("\" docid=\"").append(document.getId()).append("\" formid=\"").append(document.getFormid()).append("\" />\n");
		}
		xml.append("</documents>");
		return xml.toString();
	}

}
