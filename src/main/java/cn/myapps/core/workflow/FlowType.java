/*
 * Created on 2005-3-16
 *
 *  To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cn.myapps.core.workflow;

import java.util.HashMap;

import cn.myapps.core.workflow.element.CompleteNode;
import cn.myapps.core.workflow.element.ManualNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.element.SuspendNode;

/**
 * @author Administrator
 * 
 *         To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class FlowType {

	// add by gusd
	// static final int FLOWSTATUS_OPEN_NOSTART = 0x00000001; //初始状态无当前结点

	// 流程状态
	// static final int CLOSE_END = 0x10000000;
	public static final int FLOWSTATUS_OPEN_NOSTART = 0x00000010; // 流程初始状态第一个结点为当前结点

	public static final int FLOWSTATUS_OPEN_RUN_RUNNING = 0x00000100; // 流程运转状态

	public static final int FLOWSTATUS_OPEN_RUN_SUSPEND = 0x00001000; // 流程挂起状态

	public static final int FLOWSTATUS_CLOSE_ABORT = 0x00010000; // 流程拒绝状态

	public static final int FLOWSTATUS_CLOSE_COMPLETE = 0x00100000; // 流程完成状态

	public static final int FLOWSTATUS_CLOSE_TERMINAT = 0x01000000; // 流程终止状态

	/**
	 * 开始节点
	 */
	public static final String START2RUNNING = "1";// 开始->手工

	public static final String START2AUTO = "2";// 开始->自动

	public static final String START2SUBFLOW = "42";// 开始->子流程

	/**
	 * 挂起节点
	 */
	public static final String SUSPEND2RUNNING = "3";// 挂起->手工

	public static final String SUSPEND2AUTO = "33";// 挂起->自动

	public static final String SUSPEND2SUBFLOW = "34";// 挂起->子流程

	/**
	 * 自动节点
	 */
	public static final String AUTO2RUNNING = "9";// 自动->运行

	public static final String AUTO2ABORT = "10";// 自动->取消

	public static final String AUTO2COMPLETE = "11";// 自动->完成

	public static final String AUTO2SUSPEND = "12";// 自动->暂停

	public static final String AUTO2TERMIATE = "13";// 自动->终止

	public static final String AUTO2AUTO = "14";// 自动->自动

	public static final String AUTO2SUBFLOW = "15";// 自动->子流程

	/**
	 * 子流程节点
	 */
	public static final String SUBFLOW2RUNNING = "20";// 子流程->运行

	public static final String SUBFLOW2ABORT = "21";// 子流程->取消

	public static final String SUBFLOW2COMPLETE = "22";// 子流程->完成

	public static final String SUBFLOW2SUSPEND = "23";// 子流程->暂停

	public static final String SUBFLOW2TERMIATE = "24";// 子流程->终止

	public static final String SUBFLOW2AUTO = "25";// 子流程->自动

	public static final String SUBFLOW2SUBFLOW = "26";// 子流程->子流程

	/**
	 * 手工节点
	 */
	public static final String RUNNING2SUSPEND = "4";// 挂起1

	public static final String RUNNING2ABORT = "6";// 取消2

	public static final String RUNNING2COMPLETE = "7";// 完成

	public static final String RUNNING2TERMIATE = "8";// 终止2

	public static final String RUNNING2RUNNING_NEXT = "80";// 运行/下一步

	public static final String RUNNING2RUNNING_BACK = "81";// 退回

	public static final String RUNNING2RUNNING_SELF = "82";// 自循环

	public static final String RUNNING2AUTO = "83";// 运行->自动

	public static final String RUNNING2SUBFLOW = "84";// 运行->子流程

	public static final String RUNNING2RUNNING_RETRACEMENT = "85";// 回撤
	
	public static final String RUNNING2RUNNING_INTERVENTION = "86";// 干预
	
	/**
	 * 超时自动提醒(到达或超过审批时限)
	 */
	public static final String RUNNING2RUNNING_TIMELIMIT = "87";
	
	public static final String RUNNING2RUNNING_HANDUP = "88";//挂起
	
	public static final String RUNNING2RUNNING_RECOVER = "89";//恢复
	
	public static final String SKIP = "90";//跳过
	
	// 流程动作
	public static final int NOTHING = 0; // 不处理

	public static final int NEXT = 1; // 下一步

	public static final int BACK = 2; // 回退

	public static final int SUSPEND = 3; // 暂停

	public static final int TERMIATE = 4; // 终止

	public static final int ABORT = 5; // 取消

	public static final int COMPLETE = 6; // 完成

	public static final int SUBFLOW = 7; // 完成

	public static final int RETRACEMENT = 8;// 回撤

	// 自动处理类型
	public static final String NOTDO = "0";// 不处理

	public static final String DONEXT = "1";// 自动流转

	public static final String DOTERMINAT = "2";// 自动终止

	public static final String DOBACK = "3";// 自动回退

	public static final String DOBACKTONODE = "4";// 自动回退至指定节点

	// 审核通过条件
	public static final String ORCONDITION = "0"; // 审核通过条件－－或－－任一审核通过

	public static final String GROUPANDCNDT = "1";// 审核通过条件－－与－－每组（分号隔开）须至少有一人审核才可通过

	public static final String ANDCONDITION = "2";// 审核通过条件－－与－－每组所有人均需审核才可通过

	public static final String ORAND = "3";// 审核通过条件－－自定义－－“与”“或”混合

	// 提醒策略
	public static final String REMAINDER_NOT = "0";// 不提醒

	public static final String REMAINDER_AFTER = "1";// 到达后提醒

	public static final String REMAINDER_BEFORE = "2";// 提前提醒
	
	public static final HashMap<String,String> FLOW_TYPE_MESSAGE;
	
	static {
		FLOW_TYPE_MESSAGE = new HashMap<String,String>();
		FLOW_TYPE_MESSAGE.put("1", "创建");
		FLOW_TYPE_MESSAGE.put("2", "终止");
		FLOW_TYPE_MESSAGE.put("3", "恢复");
		FLOW_TYPE_MESSAGE.put("4", "挂起");
		FLOW_TYPE_MESSAGE.put("5", "取消");
		FLOW_TYPE_MESSAGE.put("6", "取消");
		FLOW_TYPE_MESSAGE.put("7", "完成");
		FLOW_TYPE_MESSAGE.put("8", "终止");
		FLOW_TYPE_MESSAGE.put("80", "提交");
		FLOW_TYPE_MESSAGE.put("81", "回退");
		FLOW_TYPE_MESSAGE.put("82", "自循环");
		FLOW_TYPE_MESSAGE.put("85", "回撤");
		FLOW_TYPE_MESSAGE.put("86", "干预");
		FLOW_TYPE_MESSAGE.put("9", "挂起");
		FLOW_TYPE_MESSAGE.put("90", "跳过");
	}

	public static final String[] ACTIONCODES = { "1", "2", "3", "4", "5", "6",
			"7", "8", "80", "81", "82", "9" };

	public static final String[] ACTIONNAMES = { "{*[Start]*}",
			"{*[Terminate]*}", "{*[cn.myapps.core.workflow.running]*}", "{*[Suspend]*}",
			"{*[Cancel]*}", "{*[Cancel]*}", "{*[Complete]*}",
			"{*[Terminate]*}", "{*[cn.myapps.core.workflow.running]*}", "{*[cn.myapps.core.workflow.reject]*}",
			"{*[cn.myapps.core.workflow.loop_self]*}", "{*[Suspend]*}" };

	public static String getActionName(String code) {
		if (code == null || code.trim().length() <= 0) {
			return ACTIONCODES[0];
		}

		for (int i = 0; i < ACTIONCODES.length; i++) {
			if (code.equals(ACTIONCODES[i])) {
				return ACTIONNAMES[i];
			}
		}
		return "";
	}

	public static String getActionCode(String name) {
		if (name == null || name.trim().length() <= 0) {
			return ACTIONNAMES[0];
		}

		for (int i = 0; i < ACTIONNAMES.length; i++) {
			if (name.equals(ACTIONCODES[i])) {
				return ACTIONCODES[i];
			}
		}
		return "";
	}

	public static String getActionCode(Node node) {
		if (node instanceof SuspendNode) {
			return RUNNING2SUSPEND;
		} else if (node instanceof ManualNode) {
			return RUNNING2RUNNING_NEXT;
		} else if (node instanceof CompleteNode) {
			return RUNNING2COMPLETE;
		}

		return "";
	}

}
