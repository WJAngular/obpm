package cn.myapps.support.weixin.trial.action;

import java.util.HashMap;
import java.util.Map;
import org.apache.struts2.ServletActionContext;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Web;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.support.weixin.trial.ejb.WeixinTrialProcessBean;

import com.opensymphony.xwork2.ActionSupport;

public class WeixinTrialAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9077338491003901826L;
	protected static final String ACTION_RESULT_KEY = "status";
	protected static final int ACTION_RESULT_VALUE_SUCCESS = 1;
	protected static final int ACTION_RESULT_VALUE_FAULT = 0;

	protected static final String ACTION_MESSAGE_KEY = "message";
	protected static final String ACTION_DATA_KEY = "data";

	protected ParamsTable params;

	// 将会被Struts2序列化为JSON字符串的对象,用来作为控制层返回结果的容器
	protected Map<String, Object> dataMap = new HashMap<String, Object>();
	
	private String domainid;

	private String name;

	private String telephone;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getDomainid() {
		return domainid;
	}

	public void setDomainid(String domainid) {
		this.domainid = domainid;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public ParamsTable getParams() {
		if (params == null) {
			params = ParamsTable.convertHTTP(ServletActionContext.getRequest());

			params.setSessionid(ServletActionContext.getRequest().getSession()
					.getId());

			if (params.getParameter("_pagelines") == null) {
				params.setParameter("_pagelines", Web.DEFAULT_LINES_PER_PAGE);
			}
		}

		return params;
	}

	/**
	 * 添加Action处理结果
	 * 
	 * @param isSuccess
	 *            是否成功处理
	 * @param message
	 *            返回消息
	 * @param data
	 *            返回数据
	 */
	public void addActionResult(boolean isSuccess, String message, Object data) {
		dataMap.put(ACTION_RESULT_KEY, isSuccess ? ACTION_RESULT_VALUE_SUCCESS
				: ACTION_RESULT_VALUE_FAULT);
		dataMap.put(ACTION_MESSAGE_KEY, message);
		if (data != null) {
			dataMap.put(ACTION_DATA_KEY, data);
		}
	}

	public WeixinTrialAction() {
	}

	public String doRegister() {
		try {
			UserVO user = new WeixinTrialProcessBean().doRegister(domainid, name, telephone);
			addActionResult(true, user !=null? "注册成功，请扫描二维码关注“微信办公体验”企业号！": "您的手机号已完成注册，请扫描二维码关注“微信办公体验”企业号！", "");
		} catch (Exception e) {
			try {
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			addActionResult(false, "本周的试用名额已满！", null);
			e.printStackTrace();
		}
		return SUCCESS;
	}

}
