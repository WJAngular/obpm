package cn.myapps.core.workflow.notification.ejb;

import java.util.Collection;
import java.util.Iterator;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.notification.ejb.sendmode.EmailMode;
import cn.myapps.core.workflow.notification.ejb.sendmode.PersonalMessageMode;
import cn.myapps.core.workflow.notification.ejb.sendmode.SMSModeProxy;
import cn.myapps.core.workflow.notification.ejb.sendmode.WeixinMode;
import cn.myapps.util.ProcessFactory;

/**
 * 待办通知
 * 
 * @author Nicholas
 * 
 */
public class Notification {
	
	/**
	 * 节点对象
	 */
	protected Node node;
	
	protected Collection<BaseUser> responsibles; // 通知的接收人

	protected SendMode[] sendModes; // 发送方式

	protected String applicationid;

	/** 短信审批 0-不开启 1-开启 */
	private String smsApproval;

	/**
	 * 是否已发送
	 */
	protected boolean sended = false;

	protected Document document;

	/**
	 * 提醒标题
	 */
	protected String subject;
	
	/**
	 * 流程处理信息
	 */
	protected String  flowTypeMessage;
	
	public String getFlowTypeMessage() {
		return flowTypeMessage;
	}

	public void setFlowTypeMessage(String flowTypeMessage) {
		this.flowTypeMessage = flowTypeMessage;
	}

	/**
	 * 提醒内容模板ID（对应SummaryCfg ID）
	 */
	protected String template;

	/**
	 * 获取发送方式
	 * 
	 * @return 发送方式
	 */
	public SendMode[] getSendModes() {
		return sendModes;
	}

	/**
	 * 设置发送方式
	 * 
	 * @param sendModes
	 *            发送方式
	 */
	public void setSendModes(SendMode[] sendModes) {
		this.sendModes = sendModes;
	}
	
	/**
	 * 当前用户
	 */
	private WebUser webUser;
	
	/**
	 * 参数
	 */
	private ParamsTable _params;

	public Notification() {
	}

	/**
	 * 构造方法
	 * 
	 * @param applicationid
	 *            应用标识
	 */
	public Notification(String applicationid) {
		this.applicationid = applicationid;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	/**
	 * 发送信息给用户
	 * 
	 * @throws Exception
	 */
	public void send() throws Exception {
		if (sendModes == null) {
			return;
		}

		if (responsibles != null && !responsibles.isEmpty()) {
			for (Iterator<BaseUser> iterator = responsibles.iterator(); iterator
					.hasNext();) {
				BaseUser responsible = (BaseUser) iterator.next();
				if(responsible !=null) send(responsible);
					
			}
		}
	}

	/**
	 * 发送信息给用户
	 * 
	 * @param responsible
	 * @throws Exception
	 */
	protected void send(BaseUser responsible) throws Exception {
		SummaryCfgVO summary = getSummaryCfg();
		if (summary == null) return;

		SendMode sendMode = null; 
		String content = summary.toSummay(getDocument(), getWebUser());
		for (int i = 0; i < sendModes.length; i++) {
			sendMode = sendModes[i];
			boolean flag = false;
			if (sendMode instanceof SMSModeProxy) {
				SMSModeProxy smsMode = (SMSModeProxy) sendMode;
				smsMode.setDomainid(responsible.getDomainid());
				smsMode.setReceiverUserId(responsible.getId());
				flag = smsMode.send(subject, content, responsible.getTelephone(), true);//(subject, summary, document, responsible, true);
			}else if (sendMode instanceof EmailMode) {
				String handleUrl = ((EmailMode)sendMode).getHandleUrl(document);
				String EmailContent = content + "</br><hr>" + "<a href='" + handleUrl + "'" +">" + "马上处理" + "</a>";
				flag = sendMode.send(subject,EmailContent,responsible);
			}else if(sendMode instanceof WeixinMode){
				if(webUser == null){
					webUser = new WebUser(responsible);
				}
				
				flag = ((WeixinMode)sendMode).send(subject,content,flowTypeMessage,responsible.getLoginno(),getDocument(),webUser);
			} else {
				flag = sendMode.send(subject,content,responsible);
			}
			if (flag) setSended(true);
		}
	}

	/**
	 * 根据发送的模板,查询出待办对象 待办是指发送出内容的模板
	 * 
	 * @return 待办对象
	 * @throws Exception
	 */
	public SummaryCfgVO getSummaryCfg() throws Exception {
		SummaryCfgProcess process = (SummaryCfgProcess) ProcessFactory
				.createProcess(SummaryCfgProcess.class);
		return (SummaryCfgVO) process.doView(getTemplate());
	}

	/**
	 * 获取通知的接收人
	 * 
	 * @return 通知的接收人
	 */
	public Collection<BaseUser> getResponsibles() {
		return responsibles;
	}

	/**
	 * 设置通知的接收人
	 * 
	 * @param responsibles
	 *            通知的接收人
	 */
	public void setResponsibles(Collection<BaseUser> responsibles) {
		this.responsibles = responsibles;
	}

	/**
	 * 获取应用标识
	 * 
	 * @return 应用标识
	 */
	public String getApplicationid() {
		return applicationid;
	}

	/**
	 * 设置应用标识
	 * 
	 * @param applicationid
	 *            应用标识
	 */
	public void setApplicationid(String applicationid) {
		this.applicationid = applicationid;
	}

	/**
	 * 获取是否已发送
	 * 
	 * @return 是否已发送
	 */
	public boolean isSended() {
		return sended;
	}

	/**
	 * 获取是否已发送
	 * 
	 * @param sended
	 *            是否已发送
	 */
	public void setSended(boolean sended) {
		this.sended = sended;
	}

	/**
	 * 根据发送模式编号，设置发送模式
	 * 
	 * @param sendModeCodes
	 *            发送模式编号
	 */
	public void setSendModeCodes(int[] sendModeCodes) {
		if (sendModeCodes == null) {
			this.sendModes = new SendMode[0];
			return;
		}
		// 设置发送模式
		SendMode[] sendModes = new SendMode[sendModeCodes.length];
		for (int i = 0; i < sendModeCodes.length; i++) {
			switch (sendModeCodes[i]) {
			case NotificationConstant.SEND_MODE_EMAIL:
				sendModes[i] = new EmailMode();
				break;
			case NotificationConstant.SEND_MODE_SMS:
				sendModes[i] = new SMSModeProxy("", applicationid, null);
				break;
			case NotificationConstant.SEND_MODE_PERSONALMESSAGE:
				sendModes[i] = new PersonalMessageMode();
				break;
			case NotificationConstant.SEND_MODE_WEIXIN:
				sendModes[i] = new WeixinMode();
				break;
			default:
				break;
			}
		}

		this.sendModes = sendModes;
	}

	/**
	 * 获取发送模板
	 * 
	 * @return 发送模板
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * 设置发送模板
	 * 
	 * @param template
	 *            发送模板
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * 获取文档对象
	 * 
	 * @return 文档对象
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * 设置文档对象
	 * 
	 * @param document
	 *            文档对象
	 */
	public void setDocument(Document document) {
		this.document = document;
	}

	public WebUser getWebUser() {
		return webUser;
	}

	public void setWebUser(WebUser webUser) {
		this.webUser = webUser;
	}

	public ParamsTable get_params() {
		return _params;
	}

	public void set_params(ParamsTable params) {
		_params = params;
	}
	

	/**
	 * 获取发送的主题
	 * 
	 * @return 发送的主题
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * 设置发送的主题
	 * 
	 * @param subject
	 *            发送的主题
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * 短信审批
	 * 
	 * @return 0-不开启 1-开启
	 */
	public String getSmsApproval() {
		return smsApproval;
	}

	/**
	 * 短信审批 0-不开启 1-开启
	 * 
	 * @param smsApproval
	 */
	public void setSmsApproval(String smsApproval) {
		this.smsApproval = smsApproval;
	}

}
