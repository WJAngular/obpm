package cn.myapps.base.web.listener;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import cn.myapps.constans.Web;
import cn.myapps.core.email.email.action.EmailUserHelper;
import cn.myapps.core.user.action.OnlineUsers;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.OBPMSessionContext;

public class SessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		OBPMSessionContext.getInstance().addSession(session);
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		this.removeOtherObject(session);
		/**
		 * User logout, Clean all the object in session.
		 */
		Enumeration<?> enumeration = session.getAttributeNames();
		while (enumeration.hasMoreElements()) {
			String element = (String) enumeration.nextElement();
			session.removeAttribute(element);
		}
		OBPMSessionContext.getInstance().removeSession(session);
	}
	
	/**
	 * 移除关联HttpSession却保存在其它集合内的对象。
	 * @see cn.myapps.core.user.action.OnlineUserBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
	 * @param session
	 */
	private void removeOtherObject(HttpSession session) {
		// 退出邮件系统
		EmailUserHelper.logoutEmailSystem(session);
		// 移除在线用户
		WebUser webUser = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		if (webUser != null) {
			OnlineUsers.remove(webUser.getOnlineUserid());
		}
		webUser = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_USER);
		if (webUser != null) {
			OnlineUsers.remove(webUser.getOnlineUserid());
		}
	}

}
