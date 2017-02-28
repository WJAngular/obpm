package cn.myapps.core.workflow.storage.runtime.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.workflow.element.ManualNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.util.CreateProcessException;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;

public class NodeRT extends ValueObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8021786894648625489L;
	
	public static int FLOWSTATE_NORMAL = 0; //流程状态  正常
	
	public static int FLOWSTATE_HANDUP = 1; //流程状态 挂起

	private String id;

	private String nodeid;

	private String name;

	private String statelabel;

	private String flowid;

	private String flowoption;

	private String docid;

	private String flowstatertid; // foreign key

	/**
	 * @deprecated since 2.6
	 */
	@Deprecated
	private String parentNodertid;

	private Collection<ActorRT> actorrts;

	private boolean notifiable = false;

	private int passCondition;

	private boolean passed;
	
	/**
	 * 分散/聚合令牌, 主要记录分散起始点的NodeRT1:NodeRT2
	 */
	private String splitToken;
	
	/**
	 * 抄送人
	 */
	private Collection<Circulator> circulators;
	
	/**
	 * 审批时限
	 */
	private Date deadline;
	
	private transient boolean complete = false;
	
	/**
	 * 是否顺序会签
	 */
	private boolean orderly = false;
	
	/**
	 * 审批人的审批序号
	 */
	private int position = 0;
	
	/**
	 * 流程状态 0:正常;1:挂起
	 */
	private int state = 0;
	
	/**
	 * 最后修改时间
	 */
	private Date lastProcessTime;
	
	private transient Node node;
	
	
	
	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	/**
	 * 催单次数
	 */
	private int reminderTimes;
	
	public Date getLastProcessTime() {
		return lastProcessTime;
	}

	public void setLastProcessTime(Date lastProcessTime) {
		this.lastProcessTime = lastProcessTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	/**
	 * 是否顺序会签
	 * @return
	 */
	public boolean isOrderly() {
		return orderly;
	}

	public void setOrderly(boolean orderly) {
		this.orderly = orderly;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	/**
	 * 获取抄送人
	 * @return
	 * @throws CreateProcessException 
	 */
	public Collection<Circulator> getCirculators() throws Exception {
		if(circulators == null){
			CirculatorProcess process = (CirculatorProcess) ProcessFactory.createRuntimeProcess(CirculatorProcess.class, this.getApplicationid());
			circulators = process.doQueryByNodeRtId(this.getId());
		}
		return circulators;
	}

	/**
	 * 设置抄送人
	 * @param circulators
	 */
	public void setCirculators(Collection<Circulator> circulators) {
		this.circulators = circulators;
	}

	public String getSplitToken() {
		if(splitToken ==null)
			return "";
		
		return splitToken;
	}

	public void setSplitToken(String splitToken) {
		this.splitToken = splitToken;
	}

	/**
	 * 获取过滤条件
	 * 
	 * @return 过滤条件
	 */
	public int getPassCondition() {
		return passCondition;
	}

	/**
	 * 设置过滤条件
	 * 
	 * @param passCondition
	 *            过滤条件
	 */
	public void setPassCondition(int passCondition) {
		this.passCondition = passCondition;
	}

	/**
	 * 构造方法,
	 * 
	 * @param doc
	 *            (Document)文档对象
	 * @param flowVO
	 *            流程对象
	 * @param node
	 *            节点对象
	 * @param flowoption
	 *            流程操作类型
	 * @throws Exception
	 */
	public NodeRT(FlowStateRT instance, Node node, String flowoption)
			throws Exception {
		setId(Sequence.getSequence());
		setDocid(instance.getDocid());
		setFlowid(instance.getFlowid());
		setNodeid(node.id);
		setNode(node);
		setName(node.name);
		setStatelabel(node.statelabel);
		setFlowoption(flowoption);
		setDomainid(instance.getDocument().getDomainid());
		setApplicationid(instance.getApplicationid());

//		if (doc.getState() != null) {
//			setFlowstatertid(doc.getState().getId());
//		}
		
		setFlowstatertid(instance.getId());

		if (node instanceof ManualNode) {
			setPassCondition(((ManualNode) node).getPassCondition());
			if(Integer.parseInt(((ManualNode) node).passcondition) == ManualNode.PASS_CONDITION_ORDERLY_AND){
				setOrderly(true);
			}
		}
	}

	public NodeRT() {

	}

	/**
	 * 获取节点标识
	 * 
	 * @return 节点标识
	 * @hibernate.id column="ID" generator-class="assigned"
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置节点标识
	 * 
	 * @return id 节点标识
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取节点名称
	 * 
	 * @return 节点名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置节点名称
	 * 
	 * @param name
	 *            节点名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取节点主键
	 * 
	 * @return 节点主键
	 */
	public String getNodeid() {
		return nodeid;
	}

	/**
	 * 设置节点主键
	 * 
	 * @param nodeid
	 *            节点主键
	 */
	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
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
	 * 获取处理人的集合（若开启会签，则返回不含顺序会签节点中还没有处理权限的处理人）
	 * 
	 * @return 处理人的集合
	 * @throws Exception
	 */
	public Collection<ActorRT> getActorrts() throws Exception {
		if (actorrts == null) {
			if(orderly){
				ActorRTProcess process = new ActorRTProcessBean(applicationid);
				actorrts = process.queryByNodeRT(id,position);
			}else
				actorrts = getAllActorrts();
		}

		return actorrts;
	}
	
	/**
	 * 获取所有处理人的集合（包含顺序会签节点中还没有处理权限的处理人）
	 * 
	 * @return 角色的集合
	 * @throws Exception
	 */
	public Collection<ActorRT> getAllActorrts() throws Exception {
		ActorRTProcess process = new ActorRTProcessBean(applicationid);
		return process.queryByNodeRT(id);
	}

	/**
	 * 设置角色的集合
	 * 
	 * @param actorrts
	 *            角色的集合
	 */
	public void setActorrts(Collection<ActorRT> actorrts) {
		this.actorrts = actorrts;
	}

	/**
	 * 获取文档标识
	 * 
	 * @return 文档标识
	 */
	public String getDocid() {
		return docid;
	}

	/**
	 * 设置文档标识
	 * 
	 * @param docid
	 *            文档标识
	 */
	public void setDocid(String docid) {
		this.docid = docid;
	}

	/**
	 * 获取待处理的角色列表
	 * 
	 * @return 返回待处理列表
	 * @throws Exception
	 */
	public Collection<ActorRT> getPendingActorRTList() throws Exception {
		Collection<ActorRT> rtn = new ArrayList<ActorRT>();

		for (Iterator<ActorRT> iterator = getActorrts().iterator(); iterator
				.hasNext();) {
			ActorRT actorRT = (ActorRT) iterator.next();
			if (actorRT.isPending()) {
				rtn.add(actorRT);
			}
		}

		return rtn;

	}

	/**
	 * 判断当前节点是否已通过，如果通过则表示可以提交至下一个节点
	 * 
	 * @param passed
	 *            是否已通过
	 */
	public void setPassed(boolean passed) {
		this.passed = passed;
	}

	/**
	 * 判断当前节点是否已通过，如果通过则表示可以提交至下一个节点
	 * 
	 * @return 是否已通过
	 * @throws Exception
	 */
	public boolean isPassed() throws Exception {
		return passed;
	}

	/**
	 * 判断是否包含角色
	 * 
	 * @param user
	 *            web用户
	 * @return
	 * @throws Exception 
	 */
	public boolean isContainInActors(WebUser user) throws Exception {
		boolean flag = false;
		Collection<ActorRT> actorrts = this.getActorrts();
		if (actorrts !=null && actorrts.isEmpty()) {
			outloop: for (Iterator<ActorRT> iter = actorrts.iterator(); iter
					.hasNext();) {
				ActorRT actrt = (ActorRT) iter.next();
				switch (actrt.getType()) {
				case Type.TYPE_DEPARTMENT:
					if (user.getDepartments() != null) {
						if (user.getDepartmentById(actrt.getActorid()) != null) {
							flag = true;
							break outloop;
						}
					}
					break;
				case Type.TYPE_ROLE:
					if (user.getRoles() != null) {
						if (user.getRoleById(actrt.getActorid()) != null) {
							flag = true;
							break outloop;
						}
					}
					break;
				case Type.TYPE_USER:
					if (user.getId() != null
							&& user.getId().equals(actrt.getActorid())) {
						flag = true;
						break outloop;
					}
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * 获取流程的状态标签
	 * 
	 * @return 流程的状态标签
	 */
	public String getStatelabel() {
		return statelabel;
	}

	/**
	 * 设置流程的状态标签
	 * 
	 * @param statelabel
	 *            流程的状态标签
	 */
	public void setStatelabel(String statelabel) {
		this.statelabel = statelabel;
	}

	/**
	 * 获取流程走向的类型(回退,前时,暂停等等)
	 * 
	 * @return 流程走向的类型
	 */
	public String getFlowoption() {
		return flowoption;
	}

	/**
	 * 设置流程走向的类型
	 * 
	 * @param flowoption
	 *            流程走向的类型
	 */
	public void setFlowoption(String flowoption) {
		this.flowoption = flowoption;
	}

	/**
	 * 获取关联流程的状态标识
	 * 
	 * @return 标识
	 */
	public String getFlowstatertid() {
		return flowstatertid;
	}

	/**
	 * 设置流程的状态标识
	 * 
	 * @param flowstatertid
	 *            标识
	 */
	public void setFlowstatertid(String flowstatertid) {
		this.flowstatertid = flowstatertid;
	}

	/**
	 * 获取是否提醒
	 * 
	 * @return 是否提醒
	 */
	public boolean isNotifiable() {
		return notifiable;
	}

	/**
	 * 设置是否提醒
	 * 
	 * @param notifiable
	 *            提醒
	 */
	public void setNotifiable(boolean notifiable) {
		this.notifiable = notifiable;
	}

	/**
	 * 获取流程处理角色有标识以逗号分开
	 * 
	 * @return 角色字符串以逗号分开
	 * @throws Exception
	 */
	public Collection<String> getActorIdList() throws Exception {
		Collection<String> rtn = new ArrayList<String>();
		if (getActorrts() != null && !getActorrts().isEmpty()) {
			for (Iterator<ActorRT> iterator = getActorrts().iterator(); iterator
					.hasNext();) {
				ActorRT actorRT = (ActorRT) iterator.next();
				rtn.add(actorRT.getActorid());
			}
		}
		return rtn;
	}

	/**
	 * 获取当前节点审批用户列表
	 * 
	 * @return 用户列表
	 * @throws Exception
	 */
	public Collection<BaseUser> getUserList() throws Exception {
		Collection<BaseUser> userList = new ArrayList<BaseUser>();
		for (Iterator<ActorRT> iterator2 = this.getPendingActorRTList()
				.iterator(); iterator2.hasNext();) {
			ActorRT actorRT = (ActorRT) iterator2.next();
			userList.addAll(actorRT.getAllUser());
		}

		return userList;
	}

	/**
	 * @deprecated since 2.6
	 */
	@Deprecated
	public String getParentNodertid() {
		return parentNodertid;
	}

	/**
	 * @deprecated since 2.6
	 */
	@Deprecated
	public void setParentNodertid(String parentNodertid) {
		this.parentNodertid = parentNodertid;
	}

	public boolean equals(Object obj) {
		if (obj != null && obj instanceof NodeRT) {
			NodeRT anObject = (NodeRT) obj;
			return this.getId().equals(anObject.getId());
		}

		return super.equals(obj);
	}

	public int hashCode() {
		int code = super.hashCode();
		if (this.getNodeid() != null && this.getName() != null) {
			code = this.getNodeid().hashCode() + this.getName().hashCode();
		}
		return code;
	}

	public int getReminderTimes() {
		return reminderTimes;
	}

	public void setReminderTimes(int reminderTimes) {
		this.reminderTimes = reminderTimes;
	}
	
}
