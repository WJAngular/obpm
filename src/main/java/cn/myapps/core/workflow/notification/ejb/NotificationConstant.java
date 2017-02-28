package cn.myapps.core.workflow.notification.ejb;

public interface NotificationConstant {
	/**
	 * 到达通知
	 */
	public final static String STRTAGERY_ARRIVE = "arrive";
	/**
	 * 过期的通知
	 */
	public final static String STRTAGERY_OVERDUE = "overdue";
	/**
	 * 退回通知
	 */
	public final static String STRTAGERY_REJECT = "reject";
	/**
	 * 催办通知
	 */
	public final static String STRTAGERY_REMINDER = "reminder";
	/**
	 * 发送通知
	 * 
	 */
	public final static String STRTAGERY_SEND = "send";
	/**
	 * 抄送通知
	 * */
	public final static String STRTAGERY_CARBONCOPY = "carbonCopy";
	/**
	 * 邮件发送方式
	 */
	public final static int SEND_MODE_EMAIL = 0;
	/**
	 * 手机短信发送方式
	 */
	public final static int SEND_MODE_SMS = 1;
	/**
	 * 站内短信发送方式
	 */
	public final static int SEND_MODE_PERSONALMESSAGE = 2;
	/**
	 * 微信发送方式
	 */
	public final static int SEND_MODE_WEIXIN = 3;
	/**
	 * 已时间计时
	 */
	public final static int TIME_UNIT_HOUR = 0;
	/**
	 * 已天数计时
	 */
	public final static int TIME_UNIT_DAY = 1;
	/**
	 * 提交人
	 */
	public final static int RESPONSIBLE_TYPE_SUBMITTER = 0x00000001; // 提交人
	/**
	 * 作者
	 */
	public final static int RESPONSIBLE_TYPE_AUTHOR = 0x00000010; // 作者
	/**
	 * 所有当前审批人
	 */
	public final static int RESPONSIBLE_TYPE_ALL = 0x00000100; // 所有当前审批人
	/**
	 * 30分钟
	 */
	public final static int JOB_PEIROD = 30 * 60 * 1000; // 30分钟
	
	public final static int EDIT_MODE_DESIGN = 0;//设计模式
	
	public final static int EDIT_MODE_SCRIPT = 1;//脚本模式
}
