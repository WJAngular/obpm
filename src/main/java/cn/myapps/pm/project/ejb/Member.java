package cn.myapps.pm.project.ejb;

/**
 * 项目成员
 * @author Happy
 *
 */
public class Member {
	
	private String userId;
	
	private String userName;
	
	/**
	 * 成员类型
	 */
	private int memberType;
	/**
	 * 参与者
	 */
	public static final int MEMBER_REGULAR = 0;
	/**
	 * 关注者
	 */
	public static final int MEMBER_FOLLOWER = 2;
	/**
	 * 项目经理
	 */
	public static final int MEMBER_MANAGER = 1;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getMemberType() {
		return memberType;
	}

	public void setMemberType(int memberType) {
		this.memberType = memberType;
	}

	

}
