package cn.myapps.core.xmpp.notification;

import java.lang.reflect.InvocationTargetException;

import cn.myapps.core.xmpp.XMPPNotification;
import cn.myapps.util.ObjectUtil;

/**
 * IQ格式： <notification xmlns="obpm:iq:notification"> <menu
 * xmlns="obpm:iq:notification:menu"> <action>update</action> </menu>
 * </notification>
 * 
 * @author keezzm
 * 
 * @last modified by keezzm
 * 
 */
public class MenuNotification extends XMPPNotification {

	public final static String ACTION_CREATE = "create";
	public final static String ACTION_UPDATE = "update";
	public final static String ACTION_REMOVE = "remove";

	private String action;

	public MenuNotification() {
	}

	public MenuNotification(String action) {
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public Object clone() {
		MenuNotification clone = new MenuNotification(this.action);
		try {
			ObjectUtil.copyProperties(clone, this);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return clone;
	}

	@Override
	public String getInnerXML() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<menu xmlns=\"obpm:iq:notification:menu\">");
		buffer.append("<action>" + this.action + "</action>");
		buffer.append("</menu>");

		return buffer.toString();
	}

	public static MenuNotification newInstance() {
		return new MenuNotification();
	}

	public static MenuNotification newInstance(String action) {
		return new MenuNotification(action);
	}

	@Override
	public XMPPNotification getClone(String serviceName){
		return null;
	}

	@Override
	public String getSenderName() throws Exception {
		return null;
	}

	@Override
	public String getSenderPassword() throws Exception {
		return null;
	}

	@Override
	public String toStrReceivers() {
		return null;
	}

}
