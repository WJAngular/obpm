package cn.myapps.core.personalmessage.action;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Web;
import cn.myapps.core.personalmessage.attachment.ejb.AttachmentProcess;
import cn.myapps.core.personalmessage.attachment.ejb.PM_AttachmentVO;
import cn.myapps.core.personalmessage.ejb.MessageBody;
import cn.myapps.core.personalmessage.ejb.MessageBodyProcess;
import cn.myapps.core.personalmessage.ejb.PersonalMessageProcess;
import cn.myapps.core.personalmessage.ejb.PersonalMessageVO;
import cn.myapps.core.personalmessage.ejb.VoteOptionsProcess;
import cn.myapps.core.personalmessage.ejb.VoteOptionsProcessBean;
import cn.myapps.core.personalmessage.ejb.VoteOptionsVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.http.ResponseUtil;

import com.opensymphony.xwork2.ActionContext;

/**
 * @SuppressWarnings 不支持泛型
 * @author Administrator
 * 
 */
@SuppressWarnings("unchecked")
public class PersonalMessageAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2199658886883473982L;

	private String operation = "";

	private static final Logger LOG = Logger.getLogger(PersonalMessageAction.class);

	public PersonalMessageAction() throws Exception {
		super(ProcessFactory.createProcess(PersonalMessageProcess.class),
				new PersonalMessageVO());
	}

	public String doSave() {
		try {
			PersonalMessageVO pmVO = (PersonalMessageVO) getContent();
			WebUser user = getWebUser();
			ParamsTable param = getParams();
			
			if (pmVO != null) {
				if (user != null && pmVO.getBody() != null) {
					pmVO.setSenderId(user.getId());
					pmVO.getBody().setDomainid(user.getDomainid());
					pmVO.setDomainid(user.getDomainid());
				}
				if(PersonalMessageVO.MESSAGE_TYPE_ANNOUNCEMENT.equals(pmVO.getType())){
					int _titularType = param.getParameterAsInteger("_titularType");
					if(_titularType==1){
						pmVO.setSendTitular(param.getParameterAsString("_titularDept"));
					}else{
						pmVO.setSendTitular(user.getName());
					}
				}
				
				if (StringUtil.isBlank(pmVO.getSenderId())) {
					this.addFieldError("1", "{*[could.not.find.sender]*}");
					return INPUT;
				}
				String ids = param.getParameterAsString("receiverid");
				String attachmentIds = param.getParameterAsString("attachments");
				
				if (ids != null) {
					if(!StringUtil.isBlank(attachmentIds)){
						pmVO.setAttachmentId(attachmentIds);
					}
					
					if(!StringUtil.isBlank(pmVO.getType()) && pmVO.getType().equalsIgnoreCase("2")){
						String voteOptionsVOIds = ((VoteOptionsProcess) new VoteOptionsProcessBean()).doCreate(pmVO.getVoteOptions());
						if(!StringUtil.isBlank(voteOptionsVOIds)){
							pmVO.setVoteOptionsId(voteOptionsVOIds);
						}
					}
					pmVO.setReceiverId(ids);
					String[] idArray = ids.split(";");// 多个用户用“;”分隔
					((PersonalMessageProcess) process).doCreateByUserIds(idArray, pmVO);
					HttpServletRequest request = (HttpServletRequest) this.getContext().get(ServletActionContext.HTTP_REQUEST); 
					request.setAttribute("retTag", "success");
					return SUCCESS;
				} else {
					this.addFieldError("1", "{*[could.not.find.sender]*}");
					return INPUT;
				}
			} else {
				this.addFieldError("1", "{*[could.not.find.sender]*}");
				return INPUT;
			}

		}catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	private WebUser getWebUser() throws Exception {
		return (WebUser) getContext().getSession().get(Web.SESSION_ATTRIBUTE_FRONT_USER);
	}
	
	public String doQuery() {
		ParamsTable params = getParams();
		params.setParameter("application", this.getApplication());
		Map<String, String> request = (Map<String, String>) ActionContext
				.getContext().get("request");
		request.put("application", this.getApplication());
		return SUCCESS;
	}
	
	public void doSearchInbox() throws Exception {
		ParamsTable params = getParams();
		params.setParameter("_orderby", "sendDate");
		params.setParameter("_desc", "desc");
		params.removeParameter("domain");
		WebUser user = getWebUser();
		if (user != null) {
			HttpServletResponse  res = ServletActionContext.getResponse();
			DataPackage<PersonalMessageVO> dp = ((PersonalMessageProcess) process).doInbox(user.getId(), params);
			ResponseUtil.setJsonToResponse(res,	getJson(dp));				
		}				
		
	}

	public String doRead() throws Exception {
		Map<String, String> request = (Map<String, String>) ActionContext
				.getContext().get("request");
		request.put("operation", "read");
		request.put("isRead", "true");
		WebUser user = getWebUser();
		if (user != null) {
			setDatas(((PersonalMessageProcess) process).doNoRead(user.getId(),
					getParams()));
		}
		return SUCCESS;
	}
	
	/**
	 * 根据消息类型跳转不同的页面
	 * @return
	 * @throws Exception
	 */
	public String doRedirect() throws Exception {
		ParamsTable params = getParams();
		String messageType = params.getParameterAsString("messageType");
		String msgId = params.getParameterAsString("id");
		HttpServletRequest request = (HttpServletRequest) this.getContext().get(ServletActionContext.HTTP_REQUEST); 
		request.setAttribute("id", msgId);
		if(StringUtils.equals(messageType, "0")) {
			return "system";
		} else if(StringUtils.equals(messageType, "1")) {
			return "user";
		} else if(StringUtils.equals(messageType, "2")) {
			return "announcement";
		} else if(StringUtils.equals(messageType, "3")) {
			return "vote";
		} else {
			return SUCCESS;
		}
	}

	/**
	 * 获取单条消息展示
	 * @throws Exception
	 */
	public void doShow() throws Exception {
		ParamsTable params = getParams();
		params.setParameter("_orderby", "sendDate");
		params.setParameter("_desc", "desc");
		params.removeParameter("domain");
		String msgid = params.getParameterAsString("id");
		String isRead = params.getParameterAsString("read");

		PersonalMessageVO pmVO  = null;
		if (!StringUtil.isBlank(msgid)) {
			if (!StringUtil.isBlank(isRead)) {
				pmVO = (PersonalMessageVO) process.doView(msgid);
				PersonalMessageProcess pmProcess = (PersonalMessageProcess) ProcessFactory.createProcess(PersonalMessageProcess.class);
				String allReader = pmProcess.doFindAllReader(pmVO.getSenderId(),pmVO.getReceiverId(),pmVO.getBody().getId());
				if(allReader != null){
					String[] allReaders = allReader.split(";");
					if(allReaders.length > 1){
						pmVO.setIsReadReceiverId(allReaders[0]);
						pmVO.setNoReadReceiverId(allReaders[1]);
					}else if(allReaders.length > 0){
						pmVO.setIsReadReceiverId(allReaders[0]);
					}
				}
				if (isRead.equalsIgnoreCase("false") || !pmVO.isRead()) {
					//pmVO.setRead(true);
					process.doUpdate(pmVO);
				}
				setContent(pmVO);
			} else {
				MessageBodyProcess bodyProcess = (MessageBodyProcess) ProcessFactory.createProcess(MessageBodyProcess.class);
				MessageBody body = (MessageBody) bodyProcess.doView(msgid);
				((PersonalMessageVO) getContent()).setBody(body);
				pmVO = (PersonalMessageVO) this.getContent();
			}
		}
	
		WebUser user = getWebUser();
		if (user != null) {
			HttpServletResponse  res = ServletActionContext.getResponse();
			ResponseUtil.setJsonToResponse(res,	getSingleMessageJSON(pmVO).toString());	
		}				
	}

	public String doReply() throws Exception {
		return this.doSave();
	}

	public String doCount() {
		PersonalMessageHelper pmh = new PersonalMessageHelper();
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			WebUser user = getWebUser();
			if (user != null) {
				ResponseUtil.setTextToResponse(response, pmh.countMessage(user
						.getId())
						+ "");
				return null;
			}
		}catch (OBPMValidateException e) {
			LOG.debug(e);
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			LOG.debug(e);
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		ResponseUtil.setTextToResponse(response, "0");
		return null;
	}
	
	public String doVote() throws Exception{
		ParamsTable params = getParams();
		params.setParameter("messageType", "3");
		try {
			WebUser user = getWebUser();
			if(StringUtil.isBlank(user.getId())){
				this.addFieldError("1", "{*[could.not.find.sender]*}");
				return INPUT;
			}
			if(StringUtil.isBlank(getContent().getId())){
				this.addFieldError("1", "{*[could.not.find.message]*}");
				return INPUT;
			}

			ParamsTable param = getParams();
			String checkedOptionsId = param.getParameterAsString("content.checkedOptionsId");
			if(StringUtil.isBlank(checkedOptionsId)){
				this.addFieldError("1", "{*[select_one_at_least]*}");
				return INPUT;
			}
			PersonalMessageProcess pmProcess = (PersonalMessageProcess) ProcessFactory
				.createProcess(PersonalMessageProcess.class);
			pmProcess.doVote(getContent().getId(), checkedOptionsId, user.getId());
			
			addActionMessage("{*[vote.successful]*}");
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getApplication() {
		return null;
	}
	
	/**
	 * 查询消息列表
	 * message type: 0 系统消息； 1 用户消息； 2 公告； 3投票
	 * @throws Exception
	 */
	public void doQueryMessageList() throws Exception {
		ParamsTable params = getParams();
		params.setParameter("_orderby", "sendDate");
		params.setParameter("_desc", "desc");
		params.removeParameter("domain");
		WebUser user = getWebUser();
		if (user != null) {
			HttpServletResponse  res = ServletActionContext.getResponse();
			DataPackage<PersonalMessageVO> dp = ((PersonalMessageProcess) process).doInbox(user.getId(), params);
			ResponseUtil.setJsonToResponse(res,	getJson(dp));				
		}					
	}
	
	/**
	 * 删除消息
	 */
	public String doDelete() {
		ParamsTable params = getParams();
		String messageType = params.getParameterAsString("messageType");
		try {
			if (_selects != null && _selects.length > 0) {
				process.doRemove(_selects);
				addActionMessage("{*[delete.successful]*}");
			}
			if(StringUtils.equals(messageType, "0")) {
				return "system";
			} else if(StringUtils.equals(messageType, "1")) {
				return "user";
			} else if(StringUtils.equals(messageType, "2")) {
				return "announcement";
			} else if(StringUtils.equals(messageType, "3")) {
				return "vote";
			} else {
				return SUCCESS;
			}
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
	/**
	 * 定义消息的JSON结构
	 * @param dataPackage
	 * @return
	 */
	private String getJson(DataPackage<PersonalMessageVO> dataPackage) {
		JSONObject jsonObj = new JSONObject();
		try{
			jsonObj.put("rowCount", dataPackage.getRowCount());
			jsonObj.put("linesPerPage", dataPackage.getLinesPerPage());
			jsonObj.put("pageNo", dataPackage.getPageNo());
			jsonObj.put("pageCount", dataPackage.getPageCount());
			jsonObj.put("datas", getMessageJson(dataPackage.getDatas()));		
		}catch(Exception e){
			e.printStackTrace();
		}
		return jsonObj.toString();		
	}
	
	/**
	 * 定义消息的JSON结构
	 * @param coll
	 * @return
	 * @throws Exception
	 */
	private JSONArray getMessageJson(Collection coll) throws Exception{
		JSONArray jsonArray = new JSONArray();
		for(java.util.Iterator iter =  coll.iterator(); iter.hasNext();) {
			PersonalMessageVO pm = (PersonalMessageVO) iter.next();		
			JSONObject jobj = getSingleMessageJSON(pm);
			jsonArray.add(jobj);
		}		
		return jsonArray;		
	}
	
	/**
	 * 定义消息的JSON结构
	 * @param pm
	 * @return
	 * @throws Exception
	 */
	private JSONObject getSingleMessageJSON(PersonalMessageVO pm) throws Exception {
		JSONObject jobj = new JSONObject();
		
		jobj.put("attachmentIds", StringUtils.defaultIfEmpty(pm.getAttachmentId(), ""));
		AttachmentProcess attachmentProcess = (AttachmentProcess)ProcessFactory.createProcess(AttachmentProcess.class);
		if(pm.getAttachmentId() != null) {
			String[] attachmentIds = pm.getAttachmentId().split(";");
			String attachmentNames = "";
			for(String attachmentId : attachmentIds) {
				PM_AttachmentVO attachmentVO = (PM_AttachmentVO)(attachmentProcess.doView(attachmentId));
				attachmentNames += attachmentVO.getFileName() + ";";
			}
			jobj.put("attachmentNames",  attachmentNames);
		}
		jobj.put("receiverId", pm.getReceiverId());
		jobj.put("senderId", pm.getSenderId());
		UserProcess usrProcess = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
		UserVO usrVO = (UserVO) ((UserProcess) usrProcess).doView(pm.getSenderId());	
		if(usrVO != null) {
			jobj.put("senderName", usrVO.getName());
		} else {
			jobj.put("senderName", "admin");				
		}
		jobj.put("id", pm.getId());
		jobj.put("read", pm.isRead());
		jobj.put("ownerId", pm.getOwnerId());
		jobj.put("sendDate", FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(pm.getSendDate()));	
		jobj.put("body", getMessageBodyJSON(pm.getBody()));
		jobj.put("type", pm.getType());
		String voteOptionsId = pm.getVoteOptionsId();
		jobj.put("voteOptionsId", voteOptionsId);
		if(voteOptionsId != null) {
			String voteOptions = "";
			VoteOptionsProcess voteProcess = (VoteOptionsProcess)ProcessFactory.createProcess(VoteOptionsProcess.class);
			for(String voteOptionId : voteOptionsId.split(";")) {
				VoteOptionsVO voteVO = (VoteOptionsVO) voteProcess.doView(voteOptionId);
				voteOptions += voteVO.getValue() + ";";				
			}
			voteOptions = voteOptions.substring(0, voteOptions.lastIndexOf(';'));
			jobj.put("voteOptions", voteOptions);
		}
		jobj.put("checkedOptionsId", pm.getCheckedOptionsId());
		jobj.put("radioOrCheckbox", pm.getRadioOrCheckbox());
		jobj.put("attachments", pm.getAttachments());
		jobj.put("isReadReceiverId", pm.getIsReadReceiverId());
		jobj.put("noReadReceiverId", pm.getNoReadReceiverId());
		jobj.put("sendTitular", pm.getSendTitular());
		return jobj;
	}
	
	/**
	 * 定义消息的JSON结构
	 * @param body
	 * @return
	 */
	private JSONObject getMessageBodyJSON(MessageBody body) {
		JSONObject jobj = new JSONObject();
		jobj.put("id", body.getId());
		jobj.put("title", body.getTitle());
		jobj.put("type", body.getType());
		jobj.put("content", body.getContent());
		return jobj;
	}
	
	public void doReadMessage(){
		
		try {
			ParamsTable params = getParams();
			String id = params.getParameterAsString("id");
			PersonalMessageVO vo = (PersonalMessageVO)process.doView(id);
			if(vo!= null){
				process.doUpdate(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
