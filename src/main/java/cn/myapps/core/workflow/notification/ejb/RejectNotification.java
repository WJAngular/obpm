package cn.myapps.core.workflow.notification.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHISProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHISProcessBean;

public class RejectNotification extends Notification {
	private int responsibleType;

	/**
	 * 构造方法
	 * 
	 * @param applicationid
	 *            应用标识
	 */
	public RejectNotification(String applicationid) {
		this.applicationid = applicationid;
	}

	/**
	 * 获取通知类型
	 * 
	 * @return 通知类型
	 */
	public int getResponsibleType() {
		return responsibleType;
	}

	/**
	 * 设置通知类型
	 * 
	 * @param responsibleType
	 *            通知类型
	 */
	public void setResponsibleType(int responsibleType) {
		this.responsibleType = responsibleType;
	}

	/**
	 * 发送信息
	 */
	public void send() throws Exception {
		RelationHISProcess rhisProcess = new RelationHISProcessBean(
				getApplicationid());

		Collection<BaseUser> responsibles = new ArrayList<BaseUser>();

		switch (responsibleType) {
		case NotificationConstant.RESPONSIBLE_TYPE_SUBMITTER:
			RelationHIS rhis = rhisProcess.doViewLast(document.getId(),
					document.getFlowid(), FlowType.RUNNING2RUNNING_NEXT);
			Collection<ActorHIS> actorHISList = rhis.getActorhiss();
			for (Iterator<ActorHIS> iterator = actorHISList.iterator(); iterator
					.hasNext();) {
				ActorHIS actorHIS = (ActorHIS) iterator.next();
				responsibles.addAll(actorHIS.getAllUser());
			}
			break;
		case NotificationConstant.RESPONSIBLE_TYPE_AUTHOR:
			responsibles.add((UserVO) document.getAuthor());
			break;
		case NotificationConstant.RESPONSIBLE_TYPE_ALL:
			responsibles.addAll(this.responsibles);
			break;

		default:
			break;
		}
		setResponsibles(responsibles);
		super.send();
	}
}
