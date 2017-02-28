package cn.myapps.core.scheduler.ejb;

import java.util.Date;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.util.StringUtil;
import cn.myapps.util.xml.XmlUtil;

/**
 * 触发器
 * @author Happy
 *
 */
public class TriggerVO extends ValueObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6394847080123921770L;
	
	/*人工节点定时审批任务类型*/
	public static final int JOB_TYPE_MANUAL_NODE_TIMING = 1;
	/*自动节点定时审批任务类型*/
	public static final int JOB_TYPE_AUTO_NODE_TIMING = 2;
	/*人工节点跳过审批任务类型*/
	public static final int JOB_TYPE_MANUAL_NODE_SKIP = 3;
	
	/*就绪中*/
	public static final String STATE_STAND_BY = "standby";
	/*失败*/
	public static final String STATE_FAIL = "fail";
	/*等待*/
	public static final String STATE_WAITING = "waiting";
	
	/**
	 * 触发器的唯一标识，根据任务类型和任务数据生成
	 */
	private String token;
	
	/**
	 * 任务类型
	 */
	private int jobType;
	
	/**
	 * 任务
	 */
	private Job job;
	
	/**
	 * 任务对象的XML描述
	 */
	private String jobData;
	
	/**
	 * 触发时机
	 */
	private long deadline;
	
	/**
	 * 运行状态
	 */
	private String state = STATE_WAITING;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getDeadline() {
		return deadline;
	}

	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getJobType() {
		return jobType;
	}

	public void setJobType(int jobType) {
		this.jobType = jobType;
	}

	public String getJobData() {
		return jobData;
	}

	public void setJobData(String jobData) {
		this.jobData = jobData;
	}

	public Job getJob() {
		if(job ==null && !StringUtil.isBlank(jobData)){
			job = (Job) XmlUtil.toOjbect(jobData);
		}
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}
	
	/**
	 * 获得触发延迟时间（毫秒）
	 * @return
	 */
	public long getExecuteDelay(){
		return getDeadline()-new Date().getTime();
	}

	public TriggerVO() {
		super();
	}

	public TriggerVO(Job job,long deadline) {
		if(job == null) return;
		this.job = job;
		this.deadline = deadline;
		if(job instanceof ManualNodeTimingApprovalJob ){
			this.jobType = JOB_TYPE_MANUAL_NODE_TIMING;
			this.setToken(generateManualNodeTimingApprovalJobToken(((ManualNodeTimingApprovalJob)job).getDocId(),
					((ManualNodeTimingApprovalJob)job).getFlowStateId(),
					((ManualNodeTimingApprovalJob)job).getNodeId()));
		}else if(job instanceof AutoNodeTimingApprovalJob ){
			this.jobType = JOB_TYPE_AUTO_NODE_TIMING;
			this.setToken(generateAutoNodeTimingApprovalJobToken(((AutoNodeTimingApprovalJob)job).getDocId(),
					((AutoNodeTimingApprovalJob)job).getFlowStateId(),
					((AutoNodeTimingApprovalJob)job).getNodeId()));
		}else if(job instanceof ManualNodeSkipApprovalJob ){
			this.jobType = JOB_TYPE_MANUAL_NODE_SKIP;
			this.setToken(generateAutoNodeTimingApprovalJobToken(((ManualNodeSkipApprovalJob)job).getDocId(),
					((ManualNodeSkipApprovalJob)job).getFlowStateId(),
					((ManualNodeSkipApprovalJob)job).getNodeId()));
		}
		
		this.setJobData(XmlUtil.toXml(job));
		
	}
	
	/**
	 * 生成手工节点定时审批任务的Token
	 * @param docId
	 * 		文档Id
	 * @param flowStateId
	 * 		流程实例Id
	 * @param nodertId
	 * 		节点Id
	 * @return
	 */
	public static String generateManualNodeTimingApprovalJobToken(String docId,String flowStateId,String nodertId){
		return JOB_TYPE_MANUAL_NODE_TIMING+"_"+docId+"_"+flowStateId+"_"+nodertId;
	}
	
	/**
	 * 生成自动节点定时审批任务的Token
	 * @param docId
	 * 		文档Id
	 * @param flowStateId
	 * 		流程实例Id
	 * @param nodertId
	 * 		节点Id
	 * @return
	 */
	public static String generateAutoNodeTimingApprovalJobToken(String docId,String flowStateId,String nodertId){
		return JOB_TYPE_AUTO_NODE_TIMING+"_"+docId+"_"+flowStateId+"_"+nodertId;
	}
	/**
	 * 生成人工节点跳过审批任务的Token
	 * @param docId
	 * 		文档Id
	 * @param flowStateId
	 * 		流程实例Id
	 * @param nodertId
	 * 		节点Id
	 * @return
	 */
	public static String generateManualNodeSkipApprovalJobToken(String docId,String flowStateId,String nodertId){
		return JOB_TYPE_MANUAL_NODE_SKIP+"_"+docId+"_"+flowStateId+"_"+nodertId;
	}
	
	

}
