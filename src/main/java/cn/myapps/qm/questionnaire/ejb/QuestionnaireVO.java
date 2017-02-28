package cn.myapps.qm.questionnaire.ejb;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.struts2.json.annotations.JSON;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.user.action.WebUser;

public class QuestionnaireVO extends ValueObject {

	private static final long serialVersionUID = -4733366854421086977L;

	// 问卷状态
	public final static int STATUS_ALL = -1; // 所有
	public final static int STATUS_NORMAL = 0; // 草稿
	public final static int STATUS_PUBLISH = 1; // 已发布
	public final static int STATUS_RECOVER = 2; // 已回收

	// 问卷中心问卷类型
	public final static int TYPE_PUBLISH = 0; // 我发布的
	public final static int TYPE_PARTAKE = 1; // 我参与的

	/**
	 * 发布用户
	 */
	public static String SCOPE_USER = "user";

	/**
	 * 发布部门
	 */
	public static String SCOPE_DEPT = "dept";

	/**
	 * 发布角色
	 */
	public static String SCOPE_ROLE = "role";

	/**
	 * 部门+角色
	 */
	public static String SCOPE_DEPTANDROLE = "deptAndrole";

	/**
	 * 问卷题目
	 */
	private String content;

	/**
	 * 问卷标题
	 */
	private String title;

	/**
	 * 问卷说明
	 */
	private String explains;

	/**
	 * 创建者
	 */
	private String creator;

	/**
	 * 创建者姓名
	 */
	private String creatorName;

	/**
	 * 创建者部门ID
	 */
	private String creatorDeptId;

	/**
	 * 创建者部门名字
	 */
	private String creatorDeptName;

	/**
	 * 参与者id
	 */
	private String actorIds;

	/**
	 * 参与者名称
	 */
	private String actorNames;

	/**
	 * 创建日期
	 */
	private Date createDate;

	/**
	 * 问卷发布状态
	 */
	private int status;

	/**
	 * 统计数据json
	 */
	private String chartJson;

	/**
	 * 总分
	 */
	private int score;

	/**
	 * 发布范围id
	 */
	private String ownerIds;

	/**
	 * 发布范围名称
	 */
	private String ownerNames;

	/**
	 * 类型
	 */
	private String scope;

	/**
	 * 发布时间
	 */
	private Date publishDate;

	/**
	 * 问卷填写状态
	 */
	private int fillStatus;

	/**
	 * 问卷参与人总数
	 */
	private int participantTotal;

	/**
	 * 问卷填写人总数
	 */
	private int answerTotal;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getActorIds() {
		return actorIds;
	}

	public void setActorIds(String actorIds) {
		this.actorIds = actorIds;
	}

	public String getActorNames() {
		return actorNames;
	}

	public void setActorNames(String actorNames) {
		this.actorNames = actorNames;
	}

	@JSON(format = "yyyy-MM-dd")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getChartJson() {
		return chartJson;
	}

	public void setChartJson(String chartJson) {
		this.chartJson = chartJson;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getOwnerIds() {
		return ownerIds;
	}

	public void setOwnerIds(String ownerIds) {
		this.ownerIds = ownerIds;
	}

	public String getOwnerNames() {
		return ownerNames;
	}

	public void setOwnerNames(String ownerNames) {
		this.ownerNames = ownerNames;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public int getParticipantTotal() {
		return participantTotal;
	}

	public void setParticipantTotal(int participantTotal) {
		this.participantTotal = participantTotal;
	}

	public int getAnswerTotal() {
		return answerTotal;
	}

	public void setAnswerTotal(int answerTotal) {
		this.answerTotal = answerTotal;
	}

	/**
	 * 判断是否发布给该用户
	 * 
	 * @param user
	 * @return
	 */
	@Deprecated
	public boolean validatePublish(WebUser user) {
		boolean flag = false;

		// 当acotrIds有值时赋值给ownerIds
		if (this.scope == null) {
			if (actorIds != null && actorIds.length() > 0) {
				this.setScope("user");
				this.ownerIds = actorIds;
				this.ownerNames = actorNames;
			}
		}

		if (this.scope != null) {
			if (SCOPE_USER.equals(this.scope)) {
				if (this.ownerIds.indexOf(user.getId()) >= 0) {
					flag = true;
				}
			} else if (SCOPE_ROLE.equals(this.scope)) {
				Collection<RoleVO> roles = user.getRoles();
				for (Iterator<RoleVO> iter = roles.iterator(); iter.hasNext();) {
					RoleVO role = iter.next();
					if (this.ownerIds.indexOf(role.getId()) >= 0) {
						flag = true;
						break;
					}
				}
			} else if (SCOPE_DEPT.equals(this.scope)) {
				Collection<DepartmentVO> depts = user.getDepartments();
				for (Iterator<DepartmentVO> iter = depts.iterator(); iter
						.hasNext();) {
					DepartmentVO department = iter.next();
					if (this.ownerIds.indexOf(department.getId()) >= 0) {
						flag = true;
						break;
					}
				}
			} else if (SCOPE_DEPTANDROLE.equals(this.scope)) {
				String[] ownerId = this.ownerIds.split(";;");
				Collection<DepartmentVO> depts = user.getDepartments();
				// 先判断用户部门是否符合情况
				boolean departFlag = false;
				for (Iterator<DepartmentVO> iter = depts.iterator(); iter
						.hasNext();) {
					DepartmentVO department = iter.next();
					if (ownerId[0].indexOf(department.getId()) >= 0) {
						departFlag = true;
						break;
					}
				}

				if (departFlag) {
					Collection<RoleVO> roles = user.getRoles();
					for (Iterator<RoleVO> iter = roles.iterator(); iter
							.hasNext();) {
						RoleVO role = iter.next();
						if (ownerId[1].indexOf(role.getId()) >= 0) {
							flag = true;
							break;
						}
					}
				}
			}
		}
		return flag;
	}

	@JSON(format="yyyy-MM-dd")
	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		if (publishDate == null) {
			this.publishDate = this.createDate;
		} else {
			this.publishDate = publishDate;
		}
	}

	public String getCreatorDeptId() {
		return creatorDeptId;
	}

	public void setCreatorDeptId(String creatorDeptId) {
		this.creatorDeptId = creatorDeptId;
	}

	public String getCreatorDeptName() {
		return creatorDeptName;
	}

	public void setCreatorDeptName(String creatorDeptName) {
		this.creatorDeptName = creatorDeptName;
	}

	public int getFillStatus() {
		return fillStatus;
	}

	public void setFillStatus(int fillStatus) {
		this.fillStatus = fillStatus;
	}

	public String getExplains() {
		return explains;
	}

	public void setExplains(String explains) {
		this.explains = explains;
	}
}
