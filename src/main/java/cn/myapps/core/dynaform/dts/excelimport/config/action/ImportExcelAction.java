package cn.myapps.core.dynaform.dts.excelimport.config.action;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Environment;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityParent;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.dts.excelimport.ExcelMappingDiagram;
import cn.myapps.core.dynaform.dts.excelimport.Factory;
import cn.myapps.core.dynaform.dts.excelimport.config.ImpExcelException;
import cn.myapps.core.dynaform.dts.excelimport.config.ImpExcelToDoc;
import cn.myapps.core.dynaform.dts.excelimport.config.ejb.IMPMappingConfigProcess;
import cn.myapps.core.dynaform.dts.excelimport.config.ejb.IMPMappingConfigVO;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;



public class ImportExcelAction extends ActionSupport implements SessionAware,ServletRequestAware {

	private static final long serialVersionUID = 8112854671952248684L;

	private String _path;

	private String _impmappingconfigid;

	private String _msg;
	
	private Map session;
	
	private HttpServletRequest request;
	
	private ParamsTable _params = null;

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
		if(_params==null && this.request != null){
			_params = getParamsTable();
		}
	}

	public ImportExcelAction() {
		super();
	}

	public String get_path() {
		return _path;
	}

	public void set_path(String path) {
		_path = path;
	}

	public String get_impmappingconfigid() {
		return _impmappingconfigid;
	}

	public void set_impmappingconfigid(String impmappingconfigid) {
		_impmappingconfigid = impmappingconfigid;
	}

	public String get_msg() {
		return _msg;
	}

	public void set_msg(String msg) {
		_msg = msg;
	}
	
	private ParamsTable getParamsTable(){
		ParamsTable params = new ParamsTable();
		if(this.request != null){
			
			// 设置request参数
			Map m = request.getParameterMap();
			Iterator<Entry<String, Object>> iter = m.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Object> entry = iter.next();
				String name = entry.getKey();
				Object value = entry.getValue();
				try {
					if (value instanceof String[])
						if (((String[]) value).length > 1)
							params.setParameter(name, value);
						else
							params.setParameter(name, ((String[]) value)[0]);
					else
						params.setParameter(name, value);
				} catch (Exception e) {
					LOG.warn("Set parameter: " + name + " failed, the value is: " + value);
					e.printStackTrace();
				}
			}
			
			if(!StringUtil.isBlank(request.getParameter("sessionid"))){
				params.setSessionid(request.getParameter("sessionid"));
			}
		}
		
		return params;
		
	}
	
	public ParamsTable get_params() {
		if(_params ==null){
			_params = getParamsTable();
		}
		return _params;
	}

	public String improtExcelToDocument() throws Exception {
		Environment evt = Environment.getInstance();
		String excelPath = evt.getRealPath(_path);

		if (!excelPath.toLowerCase().endsWith(".xls") && !excelPath.toLowerCase().endsWith(".xlsx")) {
			this.addFieldError("fileTypeError", "{*[core.dts.excelimport.config.cannotimport]*}");
			return SUCCESS;
		}
		IMPMappingConfigProcess process = (IMPMappingConfigProcess) ProcessFactory.createProcess(IMPMappingConfigProcess.class);
		IMPMappingConfigVO vo = (IMPMappingConfigVO) process.doView(this._impmappingconfigid);

		ExcelMappingDiagram em = Factory.trnsXML2Dgrm(vo.getXml());
		ImpExcelToDoc imp = new ImpExcelToDoc(excelPath, em);
		try {
			WebUser user = (WebUser) session.get(getWebUserSessionKey());
			ParamsTable params = get_params();
			params.setParameter("application", vo.getApplicationid());
			_msg = imp.creatDocument(user, params, vo.getApplicationid());
			
			//Excel导入动作执行后脚本
			runAfterActionScript(params, user);
			
		} catch (ImpExcelException iee) {
			Collection<String> errors = iee.getRowErrors();
			int count = 0;
			for (Iterator<String> iterator = errors.iterator(); iterator.hasNext();) {
				String error = iterator.next();
				this.addFieldError(Integer.toString(count), error);
				count++;
			}
			
			//Excel导入动作执行后脚本
			WebUser user = (WebUser) session.get(getWebUserSessionKey());
			ParamsTable params = get_params();
			runAfterActionScript(params, user);
			return INPUT;
		} catch (OBPMValidateException e) {
			this.addFieldError("error", e.getValidateMessage());
			return INPUT;
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}finally{
			//Excel导入成功后删除文档	2015-1-21
			File file = new File(excelPath);
			if (file.exists() && file.isFile()) {
	            file.delete();
	        }
			JavaScriptFactory.clear();
		}

		return SUCCESS;
	}

	private void runAfterActionScript(ParamsTable params, WebUser user) throws Exception{
		String _activityid = params.getParameterAsString("_activityid");
		String _viewid = params.getParameterAsString("_viewid");
		if(!StringUtil.isBlank(_activityid)){
			ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			ActivityParent actParent = (ActivityParent) viewProcess.doView(_viewid);
			
			if (actParent != null && _activityid != null && _activityid.trim().length() > 0) {
				Activity act = actParent.findActivity(_activityid);
				
				// 运行后脚本
				if (!StringUtil.isBlank(act.getAfterActionScript())) {
					IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), actParent.getApplicationid()); 
					runner.initBSFManager(new Document(), params, user, new java.util.ArrayList<ValidateMessage>());
					StringBuffer label = new StringBuffer();
					label.append("Activity Action(").append(act.getId()).append(")." + act.getName()).append("afterActionScript");
					runner.run(label.toString(), act.getAfterActionScript());
				}
			}
		}
	}

	/**
	 * 获取RunTime用户
	 */
	public String getWebUserSessionKey() {
		return Web.SESSION_ATTRIBUTE_FRONT_USER;
	}

	public void setSession(Map session) {
		this.session = session;
		
	}
}

