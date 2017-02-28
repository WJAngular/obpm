package cn.myapps.core.xmpp.notification;

import java.util.Collection;
import java.util.Iterator;

import org.jivesoftware.smack.packet.Packet;

import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.core.xmpp.XMPPConfig;
import cn.myapps.core.xmpp.XMPPNotification;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;
import cn.myapps.util.StringUtil;

/**
 * IQ格式：  <notification xmlns="obpm:iq:notification">
 *				<pending xmlns="obpm:iq:notification:pending">
 *					<action>update</action>
 *				</pending>
 *			</notification>
 * @author znicholas
 *
 */
public class PendingNotification extends XMPPNotification {
	public final static String ACTION_CREATE = "create";
	public final static String ACTION_UPDATE = "update";
	public final static String ACTION_REMOVE = "remove";
	
	public final static String CURRENT_USER = "currentUser";
	
	/**
	 * 软件域
	 */
	private String application;
	/**
	 * 待办主题
	 */
	private String subject;
	/**
	 * 待办内容
	 */
	private String summary;
	/**
	 * 待办路径
	 */
	private String pendingUrl;
	
	/**
	 * 当前文档
	 */
	private Document doc;
	
	/**
	 * 需通知的节点角色
	 */
	private String[] currNodeIds;
	
	/**
	 * 当前用户
	 */
	private BaseUser currentUser;
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	protected String action;
	
	
	public String[] getNextNodeIds() {
		return currNodeIds;
	}

	public void setNextNodeIds(String[] currNodeIds) {
		this.currNodeIds = currNodeIds;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	@Override
	public String getSenderName() throws Exception {
		return getUserName(sender);
	}

	@Override
	public String getSenderPassword() throws Exception {
		return getPassword(sender);
	}

	public String getPendingUrl() {
		return pendingUrl;
	}

	public void setPendingUrl(String pendingUrl) {
		this.pendingUrl = pendingUrl;
	}

	public BaseUser getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(BaseUser currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * 获取整完整内容，表单摘要标题+生成的摘要内容
	 * 
	 * @return
	 */
	public String getContent() {
		return "[" + getSubject() + "] " + getSummary();
	}

	public PendingNotification(Document doc, String action,String[] currNodeIds,BaseUser user) {
		super();
		this.action = action;
		this.doc = doc;
		this.currNodeIds = currNodeIds;
		this.currentUser = user;
		
		try {
			// 系统管理员为发送者
			SuperUserProcess superUserProcess = (SuperUserProcess) ProcessFactory.createProcess(SuperUserProcess.class);
			sender = superUserProcess.getDefaultAdmin();
			
			SummaryCfgProcess summaryCfgProcess = (SummaryCfgProcess) ProcessFactory
					.createProcess(SummaryCfgProcess.class);
			if (!StringUtil.isBlank(doc.getFormid())) {
				SummaryCfgVO summary = summaryCfgProcess.doViewByFormIdAndScope(doc.getFormid(),SummaryCfgVO.SCOPE_PENDING);
				if (summary != null) {
					ApplicationProcess appProcess = (ApplicationProcess) ProcessFactory
					.createProcess(ApplicationProcess.class);
					ApplicationVO app = (ApplicationVO)appProcess.doView(summary.getApplicationid());
					this.application = app.getName() + "##" + app.getApplicationid();
					this.subject = summary.getTitle();
					this.summary = summary.toText(doc, user);
					this.pendingUrl = "/portal/dynaform/document/view.action?_docid="
						+doc.getId()+"&amp;_formid="+doc.getFormid()
						+"&amp;_backURL=/portal/dispatch/homepage.jsp";
				} else {
					this.subject = "";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getInnerXML() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<pending xmlns=\"obpm:iq:notification:pending\">");
		buffer.append("<receivers>" + toStrReceivers() + "</receivers>");
		buffer.append("<application>" + getApplication() + "</application>");
		buffer.append("<summary>" + getContent() + "</summary>");
		buffer.append("<pendingUrl>" + getPendingUrl() + "</pendingUrl>");
		buffer.append("<action>" + getAction() + "</action>");
		buffer.append("</pending>");

		return buffer.toString();
	}
	
	@Override
	public XMPPNotification getClone(String serviceName) {
		PendingNotification clone = new PendingNotification(this.doc, this.action,this.currNodeIds,this.currentUser);
		clone.setPacketID(Packet.nextID());
		String senderName = null;
		try {
			senderName = getSenderName();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		clone.setFrom(senderName + "@" + serviceName);
		clone.setTo("obpm." + serviceName);
		clone.setType(Type.SET);
		/*try {
			ObjectUtil.copyProperties(clone, this);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}*/

		return clone;
	}
	
	@Override
	public BaseUser getSender() {
		return super.getSender();
	}
	
	@Override
	public Collection<BaseUser> getReceivers() {
		if(this.receivers.isEmpty()){
			try {
				FlowStateRT instance = doc.getState();
				Collection<NodeRT> nodes = instance.getNoderts();
				for (Iterator<NodeRT> iterator = nodes.iterator(); iterator.hasNext();) {
					NodeRT nodeRT = iterator.next();
					if(this.currNodeIds!=null && this.currNodeIds[0].equals(CURRENT_USER) && checkFristNode(nodeRT.getUserList())){
						receivers.add(this.currentUser);
						this.action = ACTION_REMOVE;
						break;
					}else{
						for (int i = 0; i < currNodeIds.length; i++) {
							if(nodeRT.getNodeid().equals(currNodeIds[i])){
								receivers.addAll(nodeRT.getUserList());
								break;
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return this.receivers;
	}

	/**
	 * 测试是否为开始节点，取消发送remove的IQ包
	 * @param userList
	 * @return
	 */
	private boolean checkFristNode(Collection<BaseUser> userList){
		boolean flag = true;
		for (Iterator iterator = userList.iterator(); iterator.hasNext();) {
			BaseUser baseUser = (BaseUser) iterator.next();
			if(baseUser.getId().equals(this.currentUser.getId())){
				return false;
			}
		}
		return flag;
	}
	
	@Override
	public String toStrReceivers() {
		getReceivers();
		StringBuffer strReceivers = new StringBuffer();
		try{
			for (Iterator<BaseUser> iterator = receivers.iterator(); iterator.hasNext();) {
				BaseUser user = (BaseUser) iterator.next();
				if(user != null){
					strReceivers.append(getUserName(user)).append(";");
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return strReceivers.toString();
	}
	
	/**
	 * 获取用户完整登录账号
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private String getUserName(BaseUser user) throws Exception {
		String userName = "";
		String separator = XMPPConfig.getInstance().getUsernameDomainSeparator();

		if (user.isAdmin()) {
			userName = user.getLoginno();
		} else {// 企业用户
			userName = user.getLoginno() + separator + user.getDomain().getName();
		}

		return encodeUserName(userName);
	}

	/**
	 * 对特殊字符进行编码
	 * 
	 * @param username
	 * @return
	 */
	private String encodeUserName(String username) {
		username = username.replace("\\", "\\5c");
		username = username.replace("@", "\\40");

		return username;
	}
	
	/**
	 * 获取用户密码
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private String getPassword(BaseUser user) throws Exception {
		String password = "";

		if (user.isAdmin()) {
			password = user.getLoginpwd();
		} else {// 企业用户
			password = Security.decryptPassword(user.getLoginpwd());
		}

		return password;
	}
}
