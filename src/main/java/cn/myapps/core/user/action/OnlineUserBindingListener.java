/*
 * Created on 2005-4-25
 *
 *  To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cn.myapps.core.user.action;

/**
 * @author Administrator
 * 
 *  To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
import java.io.Serializable;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import cn.myapps.util.sequence.Sequence;
import cn.myapps.util.sequence.SequenceException;

public class OnlineUserBindingListener implements HttpSessionBindingListener,Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3253297619552047288L;
	WebUser _user;

	public OnlineUserBindingListener(WebUser user) {
		_user = user;
	}

	public void valueBound(HttpSessionBindingEvent httpSessionBindingEvent) {
		String onlineUserid = null;
		try {
			onlineUserid = Sequence.getTimeSequence();
		} catch (SequenceException e) {
			onlineUserid = Sequence.getUUID();
		}
		if (this._user != null) {
			this._user.setOnlineUserid(onlineUserid);
		}
		OnlineUsers.add(onlineUserid, _user);
	}

	public void valueUnbound(HttpSessionBindingEvent httpSessionBindingEvent) {
		if (this._user != null) {
			OnlineUsers.remove(this._user.getOnlineUserid());
		}
	}
}
