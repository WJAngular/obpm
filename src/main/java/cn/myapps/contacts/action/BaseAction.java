package cn.myapps.contacts.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Web;
import cn.myapps.core.user.action.WebUser;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public abstract class BaseAction extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7695261530943981903L;
	/**
	 * 
	 */
	protected static final String ACTION_RESULT_KEY = "status";
	protected static final int ACTION_RESULT_VALUE_SUCCESS = 1;
	protected static final int ACTION_RESULT_VALUE_FAULT = 0;
	
	protected static final String ACTION_MESSAGE_KEY = "message";
	protected static final String ACTION_DATA_KEY = "data";
	
	
	
	
	//将会被Struts2序列化为JSON字符串的对象,用来作为控制层返回结果的容器
	protected Map<String, Object> dataMap = new HashMap<String, Object>();

	protected String[] _selects = null;
	
	protected ParamsTable params;
	
	protected DataPackage<?> datas = null;
	
	protected Collection<?> list = null;
	
	public String[] get_selects() {
		return _selects;
	}
	public void set_selects(String[] selects) {
		_selects = selects;
	}
	public ParamsTable getParams() {
		if (params == null) {
			params = ParamsTable.convertHTTP(ServletActionContext.getRequest());

			params.setSessionid(ServletActionContext.getRequest().getSession().getId());

			if (params.getParameter("_pagelines") == null){
				params.setParameter("_pagelines", Web.DEFAULT_LINES_PER_PAGE);
			}
		}

		return params;
	}
	public DataPackage<?> getDatas() {
		return datas;
	}
	public void setDatas(DataPackage<?> datas) {
		this.datas = datas;
	}
	
	public WebUser getUser(){
		Map<String,Object> session = ActionContext.getContext().getSession();

		WebUser user = null;

		if (session == null || session.get(Web.SESSION_ATTRIBUTE_FRONT_USER) == null)
			user = getAnonymousUser();
		else
			user = (WebUser) session.get(Web.SESSION_ATTRIBUTE_FRONT_USER);

		return user;
	}
	
	 /**
     * Struts2序列化指定属性时，必须有该属性的getter方法
     * @return
     */
    public Map<String, Object> getDataMap() {
        return dataMap;
    }
    
	public Collection<?> getList() {
		return list;
	}
	public void setList(Collection<?> list) {
		this.list = list;
	}
	/**
     * 添加Action处理结果
     * @param isSuccess
     * 		是否成功处理
     * @param message
     * 		返回消息
     * @param data
     * 		返回数据
     */
    public void addActionResult(boolean isSuccess,String message,Object data){
    	dataMap.put(ACTION_RESULT_KEY, isSuccess?ACTION_RESULT_VALUE_SUCCESS : ACTION_RESULT_VALUE_FAULT );
    	dataMap.put(ACTION_MESSAGE_KEY, message);
    	if(data != null){
    		dataMap.put(ACTION_DATA_KEY, data);
    	}
    }
	
	private WebUser getAnonymousUser(){
		WebUser vo = new WebUser();
		vo.setId("1234-5678-90");
		vo.setName("测试用户");
		vo.setLoginno("test");
		vo.setLoginpwd("");

		return vo;
	}
	
	
	

}
