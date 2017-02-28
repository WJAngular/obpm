package cn.myapps.core.email.email.action;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.constans.Web;
import cn.myapps.core.department.ejb.DepartmentProcessBean;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.email.attachment.ejb.Attachment;
import cn.myapps.core.email.attachment.ejb.AttachmentProcess;
import cn.myapps.core.email.email.ejb.Email;
import cn.myapps.core.email.email.ejb.EmailProcess;
import cn.myapps.core.email.email.ejb.EmailUser;
import cn.myapps.core.email.folder.action.EmailFolderHelper;
import cn.myapps.core.email.folder.ejb.EmailFolder;
import cn.myapps.core.email.folder.ejb.EmailFolderProcess;
import cn.myapps.core.email.util.Constants;
import cn.myapps.core.email.util.EmailConfig;
import cn.myapps.core.email.util.EmailProcessUtil;
import cn.myapps.core.email.util.Utility;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.http.ResponseUtil;


public class EmailAction extends BaseAction<Email> {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(EmailAction.class);
	
	private String[] _attids = null;
	private Collection<DepartmentVO> departments;
	
	private String readstate;
	
	public Collection<DepartmentVO> getDepartments() {
		return departments;
	}

	public void setDepartments(Collection<DepartmentVO> departments) {
		this.departments = departments;
	}
	public String getReadstate() {
		return readstate;
	}

	public void setReadstate(String readstate) {
		this.readstate = readstate;
	}

	/**
	 * 默认构造方法
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public EmailAction() throws Exception {
		super(ProcessFactory.createProcess(EmailProcess.class), new Email());
	}

	@Override
	public String doList() {
		try {
			setCurrentEmailProcess();
			ParamsTable params = getParams();
			EmailUser user = getEmailUser();
			String domainId = user.getDomainid();
			if(domainId != null){
				departments = new DepartmentProcessBean().queryByDomain(domainId);
			}
			String folderid = params.getParameterAsString("folderid");
			EmailFolder folder = null;
			EmailFolderProcess folderProcess = getEmailFolderProcess();
			if (Utility.isBlank(folderid)) {
				folder = folderProcess.getEmailFolderByOwnerId(Constants.DEFAULT_FOLDER_INBOX, Constants.SYSTEM_FOLDER_ID);
				folderid = folder.getId();
			} else {
				folder = folderProcess.getEmailFolderById(folderid);
			}
			if (folder == null) {
				throw new OBPMValidateException(getMultiLanguage("core.email.folder.null"));
			}
			ServletActionContext.getRequest().setAttribute("folder", folder);
			
			if("read".equals(this.getReadstate())){
				params.setParameter("unread", false);
			}else if("unread".equals(this.getReadstate())){
				params.setParameter("unread", true);
			}
			
			setDatas(((EmailProcess)process).getEmailsByFolderUser(folderid, params, user));
			String type = params.getParameterAsString("type");
			if (!Utility.isBlank(type) && type.equals("0")) {
				return "main";
			}
		} catch (OBPMValidateException e) {
			LOG.error(e.getValidateMessage());
			addFieldError("", e.getValidateMessage());
			return ERROR;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			LOG.error(e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}
	
	@Override
	public String doView() {
		try {
			setCurrentEmailProcess();
			ParamsTable params = getParams();
			String id = params.getParameterAsString("id");
			String folderid = params.getParameterAsString("folderid");
			EmailFolderProcess folderProcess = getEmailFolderProcess();
			EmailFolder folder = folderProcess.getEmailFolderById(folderid);
			if (folder != null) {
				Email email = ((EmailProcess)process).getEmailByID(id, folder);
				if (email != null) {
					setContent(email);
					ServletActionContext.getRequest().setAttribute("email", email);
					if (Constants.DEFAULT_FOLDER_DRAFTS.equals(email.getEmailFolder().getName())) {
						AttachmentProcess ap = (AttachmentProcess) ProcessFactory.createProcess(AttachmentProcess.class);
						ParamsTable table = new ParamsTable();
						if (!EmailConfig.isInternalEmail()) {
							table.setParameter("sm_emailid", email.getId());
							Collection<Attachment> attachments = ap.doSimpleQuery(table);
							email.getEmailBody().setAttachments(toSet(attachments));
						}
						if (!email.isRead()) {
							email.setRead(true);
							email.setReadDate(new Date());
							((EmailProcess)process).doUpdate(email);
						}
						return "drafts";
					}
					if (!email.isRead()) {
						email.setRead(true);
						email.setReadDate(new Date());
						((EmailProcess)process).doUpdate(email);
					}
					return SUCCESS;
				}
			}
			ServletActionContext.getRequest().setAttribute(ERROR, getMultiLanguage("page.obj.dofailure"));
			return ERROR;
		} catch (OBPMValidateException e) {
			LOG.warn(e);
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			LOG.warn(e);
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
	@Override
	public String doNew() {
		try {
			setCurrentEmailProcess();
			ParamsTable params = getParams();
			String type = params.getParameterAsString("type");
			String emailid = params.getParameterAsString("id");
			String folderid = params.getParameterAsString("folderid");
			EmailFolderProcess folderProcess = getEmailFolderProcess();
			EmailFolder folder = folderProcess.getEmailFolderById(folderid);
			if (!Utility.isBlank(type) && folder != null) {
				Email email = (Email) ((EmailProcess)process).getEmailByID(emailid, folder);
				setEmailContent(type, email);
			}
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			LOG.debug(e);
		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			LOG.debug(e);
		}
		return SUCCESS;
	}
	
	@Override
	public String doSave() {
		HttpServletResponse response = ServletActionContext.getResponse();
		String errorMsg = "";
		try {
			setCurrentEmailProcess();
			Email email = (Email) getContent();
			EmailUser user = getEmailUser();
			EmailFolderProcess folderProcess = getEmailFolderProcess();
			EmailFolder folder = (EmailFolder) folderProcess.getEmailFolderById(email.getEmailFolder().getId());
			if (folder == null) {
				folder = EmailFolderHelper.createEmptyEmailFolder();
			}
			if (email.getEmailBody() == null) {
				throw new OBPMValidateException();
			}
			this.addAttachmentsByIds(email);
			boolean sentBox = false;
			if (folder.getName().equals(Constants.DEFAULT_FOLDER_DRAFTS)) {
				email.setEmailFolder(folder);
				email.setEmailUser(user);
				((EmailProcess)process).doSaveEmail(email, folder);
				String text = getMultiLanguage("core.email.save.drafts.success");
				ResponseUtil.setTextToResponse(response, "INFO*" + Utility.getDateToString() + " " + text + "！*" + email.getId() + "*" + email.getEmailBody().getId());
				return null;
			} else if (folder.getName().equals(Constants.DEFAULT_FOLDER_SENT)) {
				//if (!Utility.isBlank(email.getId())) {
					//Email temp = (Email) ((EmailProcess)process).doView(email.getId());
					//if (!folder.getName().equals(temp.getEmailFolder().getName())) {
						//((EmailProcess)process).doUpdate(email);
					//}
				//}
				sentBox = true;
			}
			if (!checkEmailAddress(email)) {
				throw new OBPMValidateException("Email address error");
			}
			email.setEmailFolder(folder);
			((EmailProcess)process).sendEmail(email, user, sentBox);
			
			//this.saveAttachmentsByIds(email.getEmailBody());
			return SUCCESS;
		} catch (OBPMValidateException e) {
			LOG.warn(e);
			errorMsg += e.getValidateMessage();
		}catch (Exception e) {
			LOG.warn(e);
			errorMsg += e.getMessage();
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		ServletActionContext.getRequest().setAttribute(ERROR, errorMsg + getMultiLanguage("core.email.save.sent.error"));
		return ERROR;
	}
	
	@Override
	public String doDelete() {
		//HttpServletResponse response = ServletActionContext.getResponse();
		try {
			setCurrentEmailProcess();
			ParamsTable params = getParams();
			String folderid = params.getParameterAsString("folderid");
			EmailFolderProcess folderProcess = getEmailFolderProcess();
			EmailFolder folder = folderProcess.getEmailFolderById(folderid);
			if (_selects != null && folder != null) {
				((EmailProcess)process).doRemoveByFolder(_selects, folder);
			}
			//ResponseUtil.setTextToResponse(response, "邮件删除成功！");
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			LOG.warn(e);
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			LOG.warn(e);
		}
		//ResponseUtil.setTextToResponse(response, "邮件删除失败！");
		return SUCCESS;
	}
	
	public String doMoveTo() {
		try {
			setCurrentEmailProcess();
			ParamsTable params = getParams();
			HttpServletResponse response = ServletActionContext.getResponse();
			String currFolderid = params.getParameterAsString("folderid");
			String folderid = params.getParameterAsString("toid");
			EmailFolderProcess folderProcess = getEmailFolderProcess();
			EmailFolder folder = folderProcess.getEmailFolderById(folderid);
			EmailFolder currFolder = folderProcess.getEmailFolderById(currFolderid);
			if (folder == null || currFolder == null) {
				ResponseUtil.setTextToResponse(response, getMultiLanguage("core.email.move.error"));
				return null;
			} else {
				((EmailProcess)process).doMoveTo(get_selects(), currFolder, folder);
			}
		} catch (OBPMValidateException e) {
			LOG.warn(e);
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			LOG.warn(e);
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String doMark() {
		try {
			setCurrentEmailProcess();
			String mark = getParams().getParameterAsString("mark");
			String folderid = getParams().getParameterAsString("folderid");
			EmailFolder folder = new EmailFolder();
			folder.setId(folderid);
			doMarkOperation(Integer.parseInt(mark), folder);
		} catch (OBPMValidateException e) {
			HttpServletResponse response = ServletActionContext.getResponse();
			ResponseUtil.setTextToResponse(response, e.getValidateMessage());
			return null;
		}catch (Exception e) {
			HttpServletResponse response = ServletActionContext.getResponse();
			ResponseUtil.setTextToResponse(response, getMultiLanguage("core.email.mark.error"));
			this.setRuntimeException(new OBPMRuntimeException("{*[core.email.mark.error]*}",e));
			e.printStackTrace();
			return null;
		}
		return SUCCESS;
	}
	
	private void doMarkOperation(int mark, EmailFolder folder) throws Exception {
		switch (mark) {
		case 0: // 未读
			((EmailProcess)process).doUpdateMarkRead(_selects, false, folder);
			break;
			
		case 1: // 已读
			((EmailProcess)process).doUpdateMarkRead(_selects, true, folder);
			break;
		}
	}
	
	public String doToRecy() {
		//HttpServletResponse response = ServletActionContext.getResponse();
		try {
			setCurrentEmailProcess();
			ParamsTable params = getParams();
			String folderid = params.getParameterAsString("folderid");
			if (!Utility.isBlank(folderid)) {
				((EmailProcess)process).doToRecy(_selects, folderid);
				//ResponseUtil.setTextToResponse(response, "邮件删除成功！");
			}
		} catch (OBPMValidateException e) {
			LOG.warn(e);
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			LOG.warn(e);
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		//ResponseUtil.setTextToResponse(response, "删除失败！");
		return SUCCESS;
	}
	
	public String doGetEmailContent() {
		try {
			ParamsTable params = getParams();
			//String folderid = params.getParameterAsString("folderid");
			String emailid = params.getParameterAsString("id");
			Email email = (Email) ((EmailProcess)process).doView(emailid);
			ServletActionContext.getRequest().setAttribute("emailContent", getEmailContent(email));
		}catch (OBPMValidateException e) {
			LOG.warn(e);
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			LOG.warn(e);
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	private String getEmailContent(Email email) {
		StringBuffer html = new StringBuffer();
		html.append(email.getEmailBody().getContent()).append("<br></br>");
		return html.toString();
	}
	
	private void setEmailContent(String type, Email email) throws Exception {
		StringBuffer html = new StringBuffer();
		html.append("<br></br>");
		html.append("<div align=\"left\">");
		String cc = email.getEmailBody().getCc();
		String user = email.getEmailBody().getFrom();
		Email content = (Email) getContent();
		if(type.equals("forward")){
			Collection<Attachment> attachments = email.getEmailBody().getAttachments();//原先邮件所带的附件
			
			//回复转发的邮件带附件
			if(attachments.size()>0){
				Set<Attachment> ats=new HashSet<Attachment>();
				Iterator<Attachment> it = attachments.iterator();
				AttachmentProcess attachmentProcess = (AttachmentProcess) ProcessFactory.createProcess(AttachmentProcess.class);
				while (it.hasNext()) {
					Attachment attachment = (Attachment) it.next();
					//根据原来的附件创建一个新的附件
					Attachment at = new Attachment();
					at.setRealFileName(attachment.getRealFileName());
					at.setFileName(attachment.getFileName());
					at.setSize(attachment.getSize());
					at.setPath(attachment.getPath());
					attachmentProcess.doCreate(at);
					ats.add(at);
				}
				//把新附件带上
				content.getEmailBody().setAttachments(ats);
			}
		}
		
		content.getEmailBody().setCc(!StringUtil.isBlank(cc) ? cc : null);
		content.getEmailBody().setTo(!StringUtil.isBlank(user) ? user : null);
		if (type.equals("reply")) {
			content.getEmailBody().setSubject(getMultiLanguage("Reply") + ": " + email.getEmailBody().getSubject());
			//content.getEmailBody().setTo(email.getEmailBody().getFrom());
			//html.append("- - - - - - - - " + getMultiLanguage("core.email.reply.info") + " - - - - - - - -");
			html.append("<div style='border:none; border-top:solid #B5C4DF 1.0pt; padding:3.0pt 0cm 0cm 0cm' />");
			content.setReply(true);
		} else if (type.equals("forward")) {
			content.getEmailBody().setSubject(getMultiLanguage("core.email.transport") + ": " + email.getEmailBody().getSubject());
			//content.getEmailBody().setTo(email.getEmailBody().getFrom());
			//html.append("- - - - - - - - " + getMultiLanguage("core.email.transport.info") + " - - - - - - - -");
			html.append("<div style='border:none; border-top:solid #B5C4DF 1.0pt; padding:3.0pt 0cm 0cm 0cm' />");
			content.setForward(true);
		}
		
		html.append("<p style=\"line-height: 22px;\">");
		html.append("<b>").append(getMultiLanguage("core.email.from") + ":</b> ").append(email.getEmailBody().getFrom()).append("<br>");
		html.append("<b>").append(getMultiLanguage("SendDate") + ":</b> ").append(Utility.getDateToString(email.getEmailBody().getSendDate())).append("<br>");
		html.append("<b>").append(getMultiLanguage("core.email.to") + ":</b> ").append(email.getEmailBody().getTo()).append("</br>");
		html.append("<b>").append(getMultiLanguage("Subject") + ":</b> ").append(email.getEmailBody().getSubject()).append("</p>");
		html.append("<p>").append(email.getEmailBody().getContent()).append("</p>");
		html.append("</div>");
		content.getEmailBody().setContent(html.toString());
		setContent(content);
	}
	
	@Override
	public String getWebUserSessionKey() {
		return Web.SESSION_ATTRIBUTE_FRONT_USER;
	}
	
	public EmailUser getEmailUser() throws Exception {
		EmailUser emailUser = getUser().getEmailUser();
		if (emailUser == null) {
			throw new OBPMValidateException("Email user can't login!");
		}
		return emailUser;
	}
	
	/**
	 * 设置当前业务处理Bean
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void setCurrentEmailProcess() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		this.process = (IDesignTimeProcess<Email>) EmailProcessUtil.createProcess(EmailProcess.class, request);
	}
	
	private EmailFolderProcess getEmailFolderProcess() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		return (EmailFolderProcess) EmailProcessUtil.createProcess(EmailFolderProcess.class, request);
	}

	/**
	 * @param attids the _attids to set
	 */
	public void set_attids(String[] attids) {
		_attids = attids;
	}
	
	private void addAttachmentsByIds(Email email) {
		try {
			AttachmentProcess process = (AttachmentProcess) ProcessFactory.createProcess(AttachmentProcess.class);
			if (_attids != null) 
				for (int i = 0; i < _attids.length; i++) {
					Attachment temp = (Attachment) process.doView(_attids[i]);
					if (temp == null) {
						continue;
					}
					//if (EmailConfig.isInternalEmail()) {
						email.getEmailBody().addAttachment(temp);
					//}
				}
		} catch (OBPMValidateException e) {
			LOG.warn(e);
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			LOG.warn(e);
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
	}
	
	private Set<Attachment> toSet(Collection<Attachment> attachments) {
		Set<Attachment> result = new HashSet<Attachment>();
		for (Attachment att : attachments) {
			result.add(att);
		}
		return result;
	}
	
	private boolean checkEmailAddress(Email email) {
		if (email.getEmailBody().getTo() == null) {
			return false;
		}
		String strings[] = email.getEmailBody().getTo().split(";");
		if (strings.length > 1) {
			for (int i = 0; i < strings.length; i++) {
				String address = Constants.emailShowAddressToAddress(strings[i]);
				if (!Utility.checkEmailAddress(address)) {
					return false;
				}
			}
		} else {
			strings = email.getEmailBody().getTo().split(",");
			for (int i = 0; i < strings.length; i++) {
				String address = Constants.emailShowAddressToAddress(strings[i]);
				if (!Utility.checkEmailAddress(address)) {
					return false;
				}
			}
		}
		return true;
	}
	
}
