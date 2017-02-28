package cn.myapps.core.workflow.notification.ejb.sendmode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.shortmessage.submission.ejb.SubmitMessageVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.workflow.notification.ejb.SendMode;
import cn.myapps.util.CLoader;

/**
 * @author Happy
 *
 */
public class SMSModeProxy implements SendMode{
	
	private SendMode _sendMode;
	
	
	
	public SMSModeProxy() {
		super();
		
	}
	
	public SMSModeProxy(WebUser user) {
		super();
		getInstance(user);
		
	}
	
	public SMSModeProxy(String sign, WebUser user) {
		super();
		getInstance(sign,user);
		
	}
	
	public SMSModeProxy(String sign, String application, String domainid) {
		super();
		getInstance(sign, application, domainid);
		
	}
	
	public SMSModeProxy(String from, String sign, String application, String domainid) {
		super();
		getInstance(from, sign, application, domainid);
		
	}

	public SendMode getInstance() {
		if (_sendMode == null) {
			try {
				_sendMode = (SendMode) CLoader.initSMSSendMode().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return _sendMode;
	}
	
	public SendMode getInstance(WebUser user) {
		if (_sendMode == null) {
			try {
				_sendMode = (SendMode) CLoader.initSMSSendMode().getConstructor(WebUser.class).newInstance(user);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return _sendMode;
	}

	public SendMode getInstance(String sign, WebUser user) {
		if (_sendMode == null) {
			try {
				Class<?>[] argTypes = {String.class, WebUser.class};
				_sendMode = (SendMode) CLoader.initSMSSendMode().getConstructor(argTypes).newInstance(sign,user);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return _sendMode;
	}

	public SendMode getInstance(String sign, String application, String domainid) {
		if (_sendMode == null) {
			try {
				Class<?>[] argTypes = {String.class,String.class, String.class};
				_sendMode = (SendMode) CLoader.initSMSSendMode().getConstructor(argTypes).newInstance(sign,application,domainid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return _sendMode;
	}

	public SendMode getInstance(String from, String sign, String application, String domainid) {
		if (_sendMode == null) {
			try {
				Class<?>[] argTypes = {String.class,String.class, String.class,String.class};
				_sendMode = (SendMode) CLoader.initSMSSendMode().getConstructor(argTypes).newInstance(from, sign, application, domainid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return _sendMode;
	}
	
	public void setApplication(String application) {
		if(_sendMode !=null){
			try {
				Class clazz = _sendMode.getClass();
				Method m = clazz.getDeclaredMethod("setApplication", String.class);
				m.invoke(_sendMode, application);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} 
			
		}
	}

	public void setDomainid(String domainid) {
		if(_sendMode !=null){
			try {
				Class clazz = _sendMode.getClass();
				Method m = clazz.getDeclaredMethod("setDomainid", String.class);
				m.invoke(_sendMode, domainid);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} 
			
		}
	}
	
	public void setReceiverUserId(String receiverUserId) {
		if(_sendMode !=null){
			try {
				Class clazz = _sendMode.getClass();
				Method m = clazz.getDeclaredMethod("setReceiverUserId", String.class);
				m.invoke(_sendMode, receiverUserId);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} 
			
		}
	}


	public boolean send(String subject, SummaryCfgVO summaryCfg, Document doc,
			BaseUser responsible) throws Exception {
		return getInstance().send(subject, summaryCfg, doc, responsible);
	}

	public boolean send(SummaryCfgVO summaryCfg, Document doc,
			BaseUser responsible) throws Exception {
		return getInstance().send(summaryCfg, doc, responsible);
	}

	public boolean send(String subject, String content, BaseUser responsible)
			throws Exception {
		return getInstance().send(subject, content, responsible);
	}

	public boolean send(String docid, String subject, String content,
			BaseUser responsible) throws Exception {
		return getInstance().send(docid, subject, content, responsible);
	}

	public boolean send(String subject, String content, String receiver)
			throws Exception {
		return getInstance().send(subject, content, receiver);
	}

	public boolean send(String docid, String subject, String content,
			String receiver) throws Exception {
		return getInstance().send(docid, subject, content, receiver);
	}

	public boolean send(String subject, String content, String receiver,
			boolean mass) throws Exception {
		return getInstance().send(subject, content, receiver, mass);
	}

	public boolean send(String subject, String content, String receiver,
			String replyPrompt, String code, boolean mass) throws Exception {
		return getInstance().send(subject, content, receiver, replyPrompt, code, mass);
	}

	public boolean send(String docid, String subject, String content,
			String receiver, String replyPrompt, String code, boolean mass)
			throws Exception {
		return getInstance().send(docid, subject, content, receiver, replyPrompt,code, mass);
	}

	public boolean send(String subject, String content, String receiver,
			Map<String, String> defineReply, boolean mass) throws Exception {
		return getInstance().send(subject, content, receiver, defineReply, mass);
	}

	public boolean send(String docid, String subject, String content,
			String receiver, Map<String, String> defineReply, boolean mass)
			throws Exception {
		return getInstance().send(docid, subject, content, receiver, defineReply, mass);
	}

	public boolean send(String docid, String subject, String content,
			String receiver, boolean isNeedReply, boolean mass)
			throws Exception {
		return getInstance().send(docid, subject, content, receiver, isNeedReply, mass);
	}
	
	public boolean send(String subject, SummaryCfgVO summaryCfg, Document doc,
			BaseUser responsible, boolean approval) throws Exception {
		return getInstance().send(subject, summaryCfg, doc, responsible,approval);
	}
	
	public boolean send(SubmitMessageVO vo)throws Exception {
		getInstance();
		if(_sendMode !=null){
			try {
				Class clazz = _sendMode.getClass();
				Method m = clazz.getDeclaredMethod("send", SubmitMessageVO.class);
				return Boolean.parseBoolean((m.invoke(_sendMode, vo).toString()));
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} 
			
		}
		return false;
	}
	
	/**
	 * 发生一条不产生记录的信息
	 * @param account
	 * 		天翎短信平台帐号
	 * @param password
	 * 		天翎短信平台密码
	 * @param telephone
	 * 		手机号码
	 * @param smsContent
	 * 		短信内容
	 * @return
	 * 		返回发送结果代码
	 * @throws Exception
	 */
	public int sendWithoutLog(String account,String password,String telephone,String smsContent )throws Exception {
		Object sender = CLoader.initSMSSendMode().newInstance();
		try {
			Class clazz = sender.getClass();
			
			Method m = clazz.getDeclaredMethod("sendWithoutLog", new Class[]{String.class,String.class,String.class,String.class});
			Object result = m.invoke(sender,new Object[]{account,password,telephone,smsContent});
			if(result != null) return Integer.parseInt(result.toString());
		}catch (Exception e) {
			throw e;
		}  
			
		return 0;
	}

}
