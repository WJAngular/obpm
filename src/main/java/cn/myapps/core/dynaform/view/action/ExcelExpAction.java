package cn.myapps.core.dynaform.view.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityParent;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;


public class ExcelExpAction extends ActionSupport implements SessionAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2957697229617791141L;
	
	@SuppressWarnings("unchecked")
	private Map session; 

	protected String _sortCol;

	protected String _viewid;
	
	protected Document currentDocument;
	
	protected String filename;
	
	protected String isExpSub;

	public String getIsExpSub() {
		return isExpSub;
	}
	/**
	 * 运行时异常
	 */
	private OBPMRuntimeException runtimeException;
	/**
	 * 获取运行时异常
	 * @return
	 */
	public OBPMRuntimeException getRuntimeException() {
		return runtimeException;
	}

	/**
	 * 设置运行时异常
	 * @param runtimeException
	 */
	public void setRuntimeException(OBPMRuntimeException runtimeException) {
		this.runtimeException = runtimeException;
	}

	public void setIsExpSub(String isExpSub) {
		this.isExpSub = isExpSub;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String get_viewid() {
		return _viewid;
	}

	public void set_viewid(String viewid) {
		_viewid = viewid;
	}

	public String get_sortCol() {
		return _sortCol;
	}

	public void set_sortCol(String sortCol) {
		_sortCol = sortCol;
	}

	protected ParamsTable params;
	
	@SuppressWarnings("unchecked")
	public void setSession(Map session) {
		this.session = session;
	}
	
	/*public void setRequest(Map<String, Object> request) {
        this.request = request;
    }*/

	public ExcelExpAction() throws ClassNotFoundException {
		super();
	}
	
	/**
	 * 导出Document到Excel
	 * 
	 * @return "SUCESS"表示成功处理.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String expDocToExcel() throws Exception {
		try {
			ParamsTable parasm = (ParamsTable) session.get("_params");
			session.remove("_params");
			parasm.setParameter("filename", filename);
			parasm.setParameter("isExpSub", isExpSub);
			ViewProcess process = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			changeOrderBy(parasm,process);
			String fileName = process.expDocToExcel(get_viewid(), getUser(), parasm);
			session.put("excelFileName", fileName);
			
			//Excel导出执行运行后脚本
			 
			String _activityid = parasm.getParameterAsString("_activityid");
			String viewid = parasm.getParameterAsString("_viewid");
			if (!StringUtil.isBlank(viewid)) {
				ActivityParent actParent = (ActivityParent) process.doView(_viewid);
				if (actParent != null && _activityid != null && _activityid.trim().length() > 0) {
					Activity act = actParent.findActivity(_activityid);
					
					// 运行后脚本
					if (!StringUtil.isBlank(act.getAfterActionScript())) {
						IRunner runner = JavaScriptFactory.getInstance(parasm.getSessionid(), actParent.getApplicationid()); 
						runner.initBSFManager((Document)parasm.getParameter("_document"), parasm, getUser(), new java.util.ArrayList<ValidateMessage>());
						StringBuffer label = new StringBuffer();
						label.append("Activity Action(").append(act.getId()).append(")." + act.getName()).append("afterActionScript");
						runner.run(label.toString(), act.getAfterActionScript());
					}
				}
			}
			return SUCCESS;
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			e.printStackTrace();
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}finally{
			JavaScriptFactory.clear();
		}
	}
	
	/**
	 * Get WebUser Object.
	 * 
	 * @SuppressWarnings WebWork不支持泛型
	 * 
	 * @return WebUser Object user
	 * @throws Exception
	 */
	public WebUser getUser() throws Exception {
		WebUser user = (WebUser) session.get(getWebUserSessionKey());

		if (user == null) {
			user = getAnonymousUser();
		}

		return user;
	}
	
	public void changeOrderBy(ParamsTable params,ViewProcess process) throws Exception{
		View view = (View) process.doView(_viewid);
		if (params.getParameter("_sortCol") == null || params.getParameter("_sortCol").equals("")) {
			setOrder(params, view.getDefaultOrderFieldArr());
			// params.setParameter("_sortStatus",
			// view.getOrderFieldAndOrderTypeArr());
		} else {
			String[] colFields = new String[1];
			String fieldName = view.getFormFieldNameByColsName(params.getParameterAsString("_sortCol"));
			String sortStatus = params.getParameterAsString("_sortStatus");
			colFields[0] = fieldName + " " + sortStatus;
			setOrder(params, colFields);
		}
	}
	
	public void setOrder(ParamsTable params, String[] orderFields) {
		params.setParameter("_sortCol", orderFields);
		set_sortCol(this.get_sortCol());
	}
	
	/**
	 * 获取RunTime用户
	 */
	public String getWebUserSessionKey() {
		return Web.SESSION_ATTRIBUTE_FRONT_USER;
	}
	
	/**
	 * Get a anonymous user.
	 * 
	 * @return The anonymous user.
	 * @throws Exception
	 */
	protected WebUser getAnonymousUser() throws Exception {
		UserVO vo = new UserVO();

		vo.getId();
		vo.setName("GUEST");
		vo.setLoginno("guest");
		vo.setLoginpwd("");
		vo.setRoles(null);
		vo.setEmail("");

		return new WebUser(vo);
	}

}
