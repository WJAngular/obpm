package cn.myapps.base.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IRunTimeProcess;
import cn.myapps.constans.Web;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserVO;

/**
 * The abstract run-time action, it basic class for the action of run time
 * instance.
 * 
 */
public abstract class AbstractRunTimeAction<E> extends ActionSupport {
	/**
	 * The serial version uid.
	 */
	private static final long serialVersionUID = -8798588653224830361L;

    /**
     * Show dynaform page to the end user.
     */
    public static final String FORM = "form";
    
    /**
     * Show view page to the end user.
     */
    public static final String VIEW = "view";
    
	/**
	 * The inner value object
	 */
	protected ValueObject content = null;
	/**
	 * The inner data package.
	 */
	private DataPackage<E> datas = null;
	/**
	 * The inner run time process
	 */
	protected IRunTimeProcess<E> process = null;
	/**
	 * The select ids
	 */
	protected String[] _selects = null;
	/**
	 * The dmain id
	 */
	protected String domain;
	/**
	 * The application id
	 */
	protected String application;
	
	/**
	 * 运行时异常
	 */
	private OBPMRuntimeException runtimeException;

	/**
	 * Retrieve WebUser Object.
	 * 
	 * @SuppressWarnings WebWork不支持泛型
	 * @return The current WebUser Object.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public WebUser getUser() throws Exception {
		Map session = getContext().getSession();

		WebUser user = null;

		if (session == null || session.get(Web.SESSION_ATTRIBUTE_FRONT_USER) == null)
			user = getAnonymousUser();
		else
			user = (WebUser) session.get(Web.SESSION_ATTRIBUTE_FRONT_USER);

		return user;
	}

	/**
	 * Retrieve the ParamsTable
	 * 
	 * @return ParamsTable
	 */
	public ParamsTable getParams() {
		ParamsTable pm = ParamsTable.convertHTTP(ServletActionContext.getRequest());

		// put the domain id to parameters table.
		if (getDomain() != null)
			pm.setParameter("domainid", getDomain());

		// put the page line count id to parameters table.
		if (pm.getParameter("_pagelines") == null)
			pm.setParameter("_pagelines", Web.DEFAULT_LINES_PER_PAGE);

		return pm;
	}

	/**
	 * The action for show a object value.
	 * 
	 * @SuppressWarnings WebWork不支持泛型
	 * @return "SUCCESS" when action run successfully, "ERROR" otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String doView() throws Exception {

		try {
			Map params = getContext().getParameters();

			String[] ids = (String[]) (params.get("id"));
			String id = (ids != null && ids.length > 0) ? ids[0] : null;

			ValueObject contentVO = process.doView(id);
			setContent(contentVO);
		}catch (OBPMValidateException e) {
			this.addActionError(e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

		return SUCCESS;
	}

	/**
	 * The action for query the object values.
	 * 
	 * @return "SUCCESS" when action run successfully, "ERROR" otherwise.
	 * @throws Exception
	 */
	public String doList() throws Exception {
		datas = this.getProcess().doQuery(getParams(), getUser());

		return SUCCESS;
	}

	/**
	 * Get the DataPackage
	 * 
	 * @return the DataPackage
	 */
	public DataPackage<E> getDatas() {
		return datas;
	}

	/**
	 * Set the DataPackage
	 * 
	 * @param datas
	 *            the datas to set
	 */
	public void setDatas(DataPackage<E> datas) {
		this.datas = datas;
	}

	/**
	 * Get the selects items.
	 * 
	 * @return the selects.
	 */
	public String[] get_selects() {
		return _selects;
	}

	/**
	 * Set the selects items.
	 * 
	 * @param selects
	 */
	public void set_selects(String[] selects) {
		this._selects = selects;
	}

	/**
	 * Get the ActionContext
	 * 
	 * @return ActionContext
	 */
	public static ActionContext getContext() {
		return ActionContext.getContext();
	}
	
	/**
	 * Get the HttpServletRequest
	 * 
	 * @return the HttpServletRequest.
	 */
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}
	
	/**
	 * Get the HttpServletResponse
	 * 
	 * @return the HttpServletResponse.
	 */
	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	/**
	 * Get the VallueObject.
	 * 
	 * @return Returns the content.
	 */
	public ValueObject getContent() {
		return content;
	}

	/**
	 * Set the Value Object
	 * 
	 * @param content
	 *            The content to set.
	 */
	public void setContent(ValueObject content) {
		this.content = content;
	}

	/**
	 * Get the session id
	 * 
	 * @return the session id.
	 */
	public String getSessionid() {
		return ServletActionContext.getRequest().getSession().getId();
	}

	/**
	 * The abstract method to get the process.
	 * 
	 * @return The run time process
	 */
	public abstract IRunTimeProcess<E> getProcess();

	/**
	 * Get the domain.
	 * 
	 * @return The domain.
	 */
	public String getDomain() {
		if (domain != null && domain.trim().length() > 0)
			return domain;

		try {
			return getUser().getDomainid();
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Set the domain
	 * 
	 * @param The
	 *            domain to set.
	 */
	public void setDomain(String domain) {
		this.domain = domain;
		getContent().setDomainid(domain);
	}

	/**
	 * Get the application
	 * 
	 * @return
	 */
	public String getApplication() {
		return application;
	}

	/**
	 * Set the application
	 * 
	 * @param The
	 *            application to set.
	 */
	public void setApplication(String application) {
		this.application = application;
		getContent().setApplicationid(application);
	}
	

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

	/**
	 * Get a anonymous user.
	 * 
	 * @return The anonymous user.
	 * @throws Exception
	 */
	private WebUser getAnonymousUser() throws Exception {
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
