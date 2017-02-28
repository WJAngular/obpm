package cn.myapps.core.shortmessage.submission.action;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Web;
import cn.myapps.core.personalmessage.attachment.ejb.AttachmentProcess;
import cn.myapps.core.personalmessage.attachment.ejb.PM_AttachmentVO;
import cn.myapps.core.personalmessage.ejb.PersonalMessageProcess;
import cn.myapps.core.personalmessage.ejb.PersonalMessageVO;
import cn.myapps.core.personalmessage.ejb.VoteOptionsProcess;
import cn.myapps.core.personalmessage.ejb.VoteOptionsVO;
import cn.myapps.core.shortmessage.submission.ejb.SubmitMessageProcess;
import cn.myapps.core.shortmessage.submission.ejb.SubmitMessageVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.notification.ejb.sendmode.SMSModeProxy;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.http.ResponseUtil;

/**
 * @SuppressWarnings 不支持泛型
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
public class SubmitMessageAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2199658886883473982L;
	private String domain;

	public SubmitMessageAction() throws Exception {
		super(ProcessFactory.createProcess(SubmitMessageProcess.class), new SubmitMessageVO());
	}
	
	public String doRedirect() {
		return  SUCCESS;
	}

	public String doSave() {
		ParamsTable params = getParams();
		try {
			SubmitMessageVO vo = (SubmitMessageVO) getContent();
			WebUser user = getUser();
			
			if (user != null && user.getTelephone() != null && vo.getSender() == null) {
				vo.setSender(user.getTelephone());
			}
			if (!vo.getSubmission()) {
				SMSModeProxy sender = new SMSModeProxy(user);
				String receiver = vo.getReceiver();
				String sendtel = user.getTelephone();
				if (receiver != null) {
					vo.setReceiver(receiver.replaceAll(";", ","));
					vo.setSender(sendtel);
					vo.setReceiverUserID(params.getParameterAsString("receiverUserID"));
					vo.setReceiverName(params.getParameterAsString("receiverNames"));
					sender.send(vo);
					setContent(new SubmitMessageVO());
				} else {
					this.addFieldError("1", "{*[core.shortmessage.norecvlist]*}");
					return INPUT;
				}
			} else {
				this.addFieldError("1", "{*[core.shortmessage.nosender]*}");
				return INPUT;
			}
			return SUCCESS;
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	public String doList() {
		try {
			WebUser user = getUser();
			ParamsTable params = getParams();
			params.setParameter("s_domainid", user.getDomainid());
			setDatas(((SubmitMessageProcess) process).list(user, params));
			return SUCCESS;
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
	//查询发送消息，并返回JSON数据
	public void doQueryList() throws Exception {
		WebUser user = getUser();
		ParamsTable params = getParams();
		params.setParameter("s_domainid", user.getDomainid());
		if (user != null) {
			HttpServletResponse  res = ServletActionContext.getResponse();
			DataPackage<SubmitMessageVO> dp = ((SubmitMessageProcess) process).list(user, params);
			ResponseUtil.setJsonToResponse(res,	getJSON(dp));				
		}			
	}
	
	//DataPackage数据转JSON
	private String getJSON(DataPackage<SubmitMessageVO> dataPackage) {
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
			SubmitMessageVO sm = (SubmitMessageVO) iter.next();		
			JSONObject jobj = getSingleMessageJSON(sm);
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
	private JSONObject getSingleMessageJSON(SubmitMessageVO sm) throws Exception {
		JSONObject jobj = new JSONObject();
		jobj.put("docid", sm.getDocid());
		jobj.put("id", sm.getId());
		jobj.put("content", sm.getContent());
		jobj.put("sendDate", FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(sm.getSendDate()));
		jobj.put("sender", sm.getSender());
		jobj.put("receiver", sm.getReceiver());
		jobj.put("receiverName", sm.getReceiverName());
		jobj.put("receiverUserID", sm.getReceiverUserID());
		jobj.put("replyCode", sm.getReplyCode());
		jobj.put("contentType", sm.getContentType());
		jobj.put("submission", sm.getSubmission());
		jobj.put("failure", sm.isFailure());
		jobj.put("reply", sm.isReply());
		jobj.put("trash", sm.isTrash());
		jobj.put("draft", sm.isDraft());
		jobj.put("needReply", sm.isNeedReply());
		return jobj;
	}

	public String getWebUserSessionKey() {
		return Web.SESSION_ATTRIBUTE_FRONT_USER;
	}

	public void set_needReply(String value) {
		SubmitMessageVO vo = (SubmitMessageVO) getContent();
		if (value != null && value.trim().equals("true")) {
			vo.setNeedReply(true);
		} else {
			vo.setNeedReply(false);
		}
	}

	public String get_msgType() {
		SubmitMessageVO vo = (SubmitMessageVO) getContent();
		return "" + vo.getContentType();
	}

	public void set_msgType(String type) {
		if (type != null) {
			SubmitMessageVO vo = (SubmitMessageVO) getContent();
			vo.setContentType(Integer.parseInt(type.trim()));
		}
	}

	public String get_needReply() {
		SubmitMessageVO vo = (SubmitMessageVO) getContent();
		if (vo.isNeedReply()) {
			return "true";
		} else {
			return "false";
		}
	}

	public void set_mass(String value) {
		SubmitMessageVO vo = (SubmitMessageVO) getContent();
		if (value != null && value.trim().equals("true")) {
			vo.setMass(true);
		} else {
			vo.setMass(false);
		}
	}

	public String get_mass() {
		SubmitMessageVO vo = (SubmitMessageVO) getContent();
		if (vo.isMass()) {
			return "true";
		} else {
			return "false";
		}
	}

	public String getDomain() {
		if (domain != null && domain.trim().length() > 0) {
			return domain;
		} else {
			return (String) getContext().getSession().get(Web.SESSION_ATTRIBUTE_DOMAIN);
		}
	}

	public void setDomain(String domain) {
		this.domain = domain;
		getContent().setDomainid(domain);
	}
	
	/**
	 * 删除消息
	 */
	public String doDelete() {
		ParamsTable params = getParams();
		try {
			if (_selects != null && _selects.length > 0) {
				process.doRemove(_selects);
				addActionMessage("{*[delete.successful]*}");
			}
			return SUCCESS;
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
}
