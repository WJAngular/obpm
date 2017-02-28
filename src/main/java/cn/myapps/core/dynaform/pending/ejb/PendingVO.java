package cn.myapps.core.dynaform.pending.ejb;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.constans.Environment;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;

public class PendingVO extends ValueObject {

	private static final long serialVersionUID = 6411478815524365134L;

	private String formid;

	private String formname;

	/**
	 * 创建日期
	 */
	private Date created;

	/**
	 * 最后一次修改日期
	 */
	private Date lastmodified;

	/**
	 * 最后审核时间
	 */
	private Date auditdate;

	/**
	 * 作者
	 */
	private BaseUser author;

	/**
	 * 流程状态(从T_FLOWSTATERT表中获取)
	 */
	private FlowStateRT state;

	/**
	 * 最后一次修改用户
	 */
	private String lastmodifier;

	/**
	 * 当前审核人名称(以分","号隔开)
	 */
	private String auditorNames;

	/**
	 * 最后审核人姓名
	 */
	private String audituser;

	/**
	 * 流程ID
	 */
	private String flowid;

	/**
	 * 流程状态标识
	 */
	private String stateLabel;

	/**
	 * 最后一次流程处理的代码
	 */
	private String lastFlowOperation;

	/**
	 * 根据Remind的FormMapping所生成的汇总信息
	 */
	private String summary;

	private String auditorList;
	
	private String docId;
	
	

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getAuditorList() {
		return auditorList;
	}

	public void setAuditorList(String auditorList) {
		this.auditorList = auditorList;
	}

	/**
	 * 获取生成Reminder的FormMapping所生成的汇总信息
	 * 
	 * @return 生成的汇总信息
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * 设置Reminder的FormMapping所生成的汇总信息 格式: 字段1;字段2;字段3
	 * 
	 * @param summary
	 *            字段值
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * 获取表单标识
	 * 
	 * @return 表单标识
	 */
	public String getFormid() {
		return formid;
	}

	/**
	 * 设置表单标识
	 * 
	 * @param formid
	 */
	public void setFormid(String formid) {
		this.formid = formid;
	}

	/**
	 * 获取创建日期
	 * 
	 * @return 创建日期
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * 设置创建日期
	 * 
	 * @param created
	 *            日期型
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * 获取最后修改时间
	 * 
	 * @return 修改时间
	 */
	public Date getLastmodified() {
		return lastmodified;
	}

	/**
	 * 设置最后修改时间
	 * 
	 * @param lastmodified
	 *            时间日期型
	 */
	public void setLastmodified(Date lastmodified) {
		this.lastmodified = lastmodified;
	}

	/**
	 * 获取最后审核时间
	 * 
	 * @return 最后审核时间
	 */
	public Date getAuditdate() {
		return auditdate;
	}

	/**
	 * 设置最后审核时间
	 * 
	 * @param auditdate
	 *            最后审核时间
	 */
	public void setAuditdate(Date auditdate) {
		this.auditdate = auditdate;
	}

	/**
	 * 获取关联的表单名
	 * 
	 * @return 表单名
	 */
	public String getFormname() {
		return formname;
	}

	/**
	 * 设置关联的表单名
	 * 
	 * @param formname
	 *            表单名
	 */
	public void setFormname(String formname) {
		this.formname = formname;
	}

	/**
	 * 获取作者
	 * 
	 * @return 作者名
	 */
	public BaseUser getAuthor() {
		return author;
	}

	/**
	 * 设置作者
	 * 
	 * @param author
	 *            作者名
	 */
	public void setAuthor(BaseUser author) {
		this.author = author;
	}

	/**
	 * 获取当前审核人名称(以分","号隔开)
	 * 
	 * @return 当前审核人名称(以分","号隔开)
	 */
	public String getAuditorNames() {
		return auditorNames;
	}

	/**
	 * 设置当前审核人名称(以分","号隔开)
	 * 
	 * @param auditorNames
	 */
	public void setAuditorNames(String auditorNames) {
		this.auditorNames = auditorNames;
	}

	/**
	 * 获取最后审核人姓名
	 * 
	 * @return 最后审核人姓名
	 */
	public String getAudituser() {
		return audituser;
	}

	/**
	 * 设置最后审核人姓名
	 * 
	 * @param audituser
	 *            最后审核人姓名
	 */
	public void setAudituser(String audituser) {
		this.audituser = audituser;
	}

	/**
	 * 获取流程标识
	 * 
	 * @return 流程标识
	 */
	public String getFlowid() {
		return flowid;
	}

	/**
	 * 设置流程标识
	 * 
	 * @param flowid
	 *            流程标识
	 */
	public void setFlowid(String flowid) {
		this.flowid = flowid;
	}

	/**
	 * 获取流程状态标识
	 * 
	 * @return 流程状态标识
	 */
	public String getStateLabel() {
		return stateLabel;
	}

	/**
	 * 设置流程状态标识
	 * 
	 * @param stateLabel
	 *            流程状态标识
	 */
	public void setStateLabel(String stateLabel) {
		this.stateLabel = stateLabel;
	}

	/**
	 * 获取最后一次流程处理的操作
	 * 
	 * @return 最后一次流程处理的操作
	 */
	public String getLastFlowOperation() {
		return lastFlowOperation;
	}

	/**
	 * 设置 最后一次流程处理的操作
	 * 
	 * @param lastFlowOperation
	 *            流程操作
	 */
	public void setLastFlowOperation(String lastFlowOperation) {
		this.lastFlowOperation = lastFlowOperation;
	}

	/**
	 * 获取流程状态(从T_FLOWSTATERT表中获取)
	 * 
	 * @return 流程状态
	 */
	public FlowStateRT getState() {
		return state;
	}

	/**
	 * 设置流程状态(从T_FLOWSTATERT表中获取)
	 * 
	 * @param state
	 *            流程状态(从T_FLOWSTATERT表中获取)
	 */
	public void setState(FlowStateRT state) {
		this.state = state;
	}

	/**
	 * 获取最后一次修改用户
	 * 
	 * @return 用户登陆名
	 */
	public String getLastmodifier() {
		return lastmodifier;
	}

	/**
	 * 最后最后一次修改用户
	 * 
	 * @param lastmodifier
	 *            用户登陆名
	 */
	public void setLastmodifier(String lastmodifier) {
		this.lastmodifier = lastmodifier;
	}

	/**
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public String toHtmlText(WebUser user, String summaryCfgId) throws Exception {
		boolean isread = false;
		StringBuffer html = new StringBuffer();
		Collection<ActorRT> actors = this.getState().getActors();
		for (Iterator<ActorRT> iter = actors.iterator(); iter.hasNext();) {
			ActorRT actor = (ActorRT) iter.next();
			if (user.getId().equals(actor.getActorid()) && actor.getIsread()) {
				isread = true;
				break;
			}
		}
		html.append("<input moduleType='pending' type='hidden'");
		html.append(" id='" + this.getDocId() + "'");
		html.append(" formId='" + this.getFormid() + "'");
		html.append(" summaryCfgId='" + summaryCfgId + "'");
		html.append(" Summary='" + this.getSummary() + "'");
		if (isread) {
			html.append(" isread='true'");
		}
		html.append(" />");
		return html.toString();

	}
	
	public boolean isRead(WebUser user) throws Exception {
		boolean isread = false;
		if(this.getState() != null){
			Collection<ActorRT> actors = this.getState().getActors();
			for (Iterator<ActorRT> iter = actors.iterator(); iter.hasNext();) {
				ActorRT actor = (ActorRT) iter.next();
				if (user.getId().equals(actor.getActorid()) && actor.getIsread()) {
					isread = true;
					break;
				}
			}
		}
		return isread;
	}
	
	public boolean isDeletable(WebUser user) throws Exception {
		boolean deletable = false;
		if (FlowType.START2RUNNING.equals(this.getLastFlowOperation())) {
			if(this.getState() != null){
				Collection<ActorRT> actors = this.getState().getActors();
				for (Iterator<ActorRT> iter = actors.iterator(); iter.hasNext();) {
					ActorRT actor = (ActorRT) iter.next();
					if (user.getId().equals(actor.getActorid())) {
						deletable = true;
						break;
					}
				}
			}
			
		}
		return deletable;
	}
	
	/**
	 * 拼装手机端待办XML
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public String toMbXMLText(WebUser user, String summaryCfgId,String title) throws Exception {
		boolean isread = false;
		StringBuffer html = new StringBuffer();
		if(this.getState() != null){
			Collection<ActorRT> actors = this.getState().getActors();
			for (Iterator<ActorRT> iter = actors.iterator(); iter.hasNext();) {
				ActorRT actor = (ActorRT) iter.next();
				if (user.getId().equals(actor.getActorid()) && actor.getIsread()) {
					isread = true;
					break;
				}
			}
			if (isread) {
				html.append("<"+MobileConstant.TAG_PENDING+" "+MobileConstant.ATT_ID+"='"+this.getDocId()+"' "+MobileConstant.ATT_FORMID+"='"+this.formid+"' "+MobileConstant.ATT_SUMMARYCFGID+"='"+summaryCfgId+"'>");
				html.append("["+title+"](已读)"+this.getSummary()+"</"+MobileConstant.TAG_PENDING+">");
			} else {
				html.append("<"+MobileConstant.TAG_PENDING+" "+MobileConstant.ATT_ID+"='"+this.getDocId()+"' "+MobileConstant.ATT_FORMID+"='"+this.formid+"' "+MobileConstant.ATT_SUMMARYCFGID+"='"+summaryCfgId+"'>");
				html.append("["+title+"](未读)"+this.getSummary()+"</"+MobileConstant.TAG_PENDING+">");
			}
		}

		return html.toString();

	}

}
