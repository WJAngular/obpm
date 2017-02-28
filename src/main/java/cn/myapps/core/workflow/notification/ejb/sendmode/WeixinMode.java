package cn.myapps.core.workflow.notification.ejb.sendmode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.workflow.notification.ejb.SendMode;
import cn.myapps.support.weixin.WeixinServiceProxy;

public class WeixinMode implements SendMode {

	public boolean send(String subject, SummaryCfgVO summaryCfg, Document doc,
			BaseUser responsible) throws Exception {
		throw new UnsupportedOperationException("method unsupport!");
	}

	public boolean send(SummaryCfgVO summaryCfg, Document doc,
			BaseUser responsible) throws Exception {
		throw new UnsupportedOperationException("method unsupport!");
	}

	public boolean send(String subject, String content, BaseUser responsible)
			throws Exception {
		throw new UnsupportedOperationException("method unsupport!");
	}

	public boolean send(String docid, String subject, String content,
			BaseUser responsible) throws Exception {
		throw new UnsupportedOperationException("method unsupport!");
	}

	public boolean send(String subject, String content, String receiver)
			throws Exception {
		throw new UnsupportedOperationException("method unsupport!");
	}

	public boolean send(String docid, String subject, String content,
			String receiver) throws Exception {
		
		throw new UnsupportedOperationException("method unsupport!");
	}

	public boolean send(String subject, String content, String receiver,
			boolean mass) throws Exception {
		throw new UnsupportedOperationException("method unsupport!");
	}

	public boolean send(String subject, String content, String receiver,
			String replyPrompt, String code, boolean mass) throws Exception {
		throw new UnsupportedOperationException("method unsupport!");
	}

	public boolean send(String docid, String subject, String content,
			String receiver, String replyPrompt, String code, boolean mass)
			throws Exception {
		throw new UnsupportedOperationException("method unsupport!");
	}

	public boolean send(String subject, String content, String receiver,
			Map<String, String> defineReply, boolean mass) throws Exception {
		throw new UnsupportedOperationException("method unsupport!");
	}

	public boolean send(String docid, String subject, String content,
			String receiver, Map<String, String> defineReply, boolean mass)
			throws Exception {
		throw new UnsupportedOperationException("method unsupport!");
	}

	public boolean send(String docid, String subject, String content,
			String receiver, boolean isNeedReply, boolean mass)
			throws Exception {
		throw new UnsupportedOperationException("method unsupport!");
	}

	public boolean send(String subject, SummaryCfgVO summaryCfg, Document doc,
			BaseUser responsible, boolean approval) throws Exception {
		
		throw new UnsupportedOperationException("method unsupport!");
	}
	
	/**
	 * 发生消息
	 * @param title 标题
	 * @param discription 描述
	 * @param touser 接收用户账号
	 * @param doc 文档对象
	 * @return
	 * @throws Exception
	 */
	public boolean send(String title,String description,String touser,Document doc) throws Exception {
		
		final String  topic = title;
		final String desc = description;
		final String to =touser;
		final Document document = doc;
		
		new Thread(new Runnable() {
			public void run() {
				try {
					WeixinServiceProxy.send(topic, desc, to, document);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		return true;
	}
	/**
	 * 发生消息
	 * @param title 标题
	 * @param discription 描述
	 * @param touser 接收用户账号
	 * @param doc 文档对象
	 * @param webUser 当前用户
	 * @return
	 * @throws Exception
	 */
	public boolean send(String title,String description,String flowTypeMessage,String touser,Document doc,WebUser webUser) throws Exception {
		
		//标题
		final String  topic = "["+title+"]" + description;
		//作者
		String auditUser = webUser.getName();
		//时间
		String time = getDateTimeStr(doc.getLastmodified());
		//动作
		String act = flowTypeMessage+"该流程";
		
		String tempDesc = auditUser + time + act;
		
		if("待办超期".equals(flowTypeMessage)){
			tempDesc= "待办已超期，请您尽快处理";
		}
		
		//拼接描述
		final String desc = tempDesc;
		
		
		final String to =touser;
		final Document document = doc;
		
		new Thread(new Runnable() {
			public void run() {
				try {
					WeixinServiceProxy.send(topic, desc, to, document);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		return true;
	}

	
	private String getDateTimeStr(Date date) {
		
		try{
			   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			   return "于"+format.format(date);
			}catch(Exception e){
				return "";
			}
			
	}

}
