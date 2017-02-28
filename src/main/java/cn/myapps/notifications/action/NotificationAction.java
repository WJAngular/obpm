package cn.myapps.notifications.action;

import java.io.IOException;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import cn.myapps.constans.Web;
import cn.myapps.core.user.action.WebUser;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class NotificationAction extends ActionSupport {

	public NotificationAction() {
		super();
	}
	
	
	/**
	 * 获取新消息的总数
	 */
	public void doGetNewMessageCount(){
		WebUser user = getUser();
		if(user !=null)
			try {
				ServletActionContext.getResponse().getWriter().print(NotificationHelper.getNewMessageCountByUser(user.getId()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}
	

	private WebUser getUser(){
		Map<String, Object> session = ActionContext.getContext().getSession();
		WebUser user = null;

		if (session != null && session.get(Web.SESSION_ATTRIBUTE_FRONT_USER) != null){
			user = (WebUser) session.get(Web.SESSION_ATTRIBUTE_FRONT_USER);
		}

		return user;
	}
}
