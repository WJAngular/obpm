package cn.myapps.rm.base.action;

import java.util.Map;

import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Web;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.rm.base.dao.DataPackage;
import cn.myapps.rm.base.ejb.BaseObject;
import cn.myapps.rm.base.ejb.RunTimeProcess;


/**
 * The abstract run-time action, it basic class for the action of run time
 * instance.
 * 
 */
public abstract class BaseAction<E> extends ActionSupport {
	/**
	 * The serial version uid.
	 */
	private static final long serialVersionUID = -8798588653224830361L;

	/**
	 * The inner value object
	 */
	protected BaseObject content = null;
	/**
	 * The inner data package.
	 */
	private DataPackage<E> datas = null;
	/**
	 * The inner run time process
	 */
	protected RunTimeProcess<E> process = null;
	/**
	 * The select ids
	 */
	protected String[] _selects = null;

	/**
	 * Retrieve WebUser Object.
	 * @return The current WebUser Object.
	 * @throws Exception
	 */
	public WebUser getUser() throws Exception {
		Map<String, Object> session = getContext().getSession();

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

			BaseObject contentVO = process.doView(id);
			setContent(contentVO);
		} catch (Exception e) {
			this.addActionError(e.getMessage());
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
	 * Get the VallueObject.
	 * 
	 * @return Returns the content.
	 */
	public BaseObject getContent() {
		return content;
	}

	/**
	 * Set the Value Object
	 * 
	 * @param content
	 *            The content to set.
	 */
	public void setContent(BaseObject content) {
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
	public abstract RunTimeProcess<E> getProcess();


	/**
	 * Get a anonymous user.
	 * 
	 * @return The anonymous user.
	 * @throws Exception
	 */
	private WebUser getAnonymousUser() throws Exception {
		WebUser vo = new WebUser(null);

		vo.setId("1234-5678-90");
		vo.setName("测试用户");
		vo.setLoginno("test");
		vo.setLoginpwd("");

		return vo;
	}
}
