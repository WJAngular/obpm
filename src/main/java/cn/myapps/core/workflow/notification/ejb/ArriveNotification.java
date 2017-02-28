package cn.myapps.core.workflow.notification.ejb;

import java.util.Iterator;

import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.workflow.element.ManualNode;

/**
 * 流程到达通知
 * @author Happy
 *
 */
public class ArriveNotification extends Notification {
	
	

	public ArriveNotification(String applicationid) {
		super(applicationid);
	}

	@Override
	public void send() throws Exception {
		
		ManualNode currNode = (ManualNode) node;
		boolean falg = true;
		if(currNode.isAllowSkip && (currNode.getPassCondition()==ManualNode.PASS_CONDITION_OR || (currNode.getPassCondition()!=ManualNode.PASS_CONDITION_OR && this.responsibles.size()==1))){
			for (Iterator<BaseUser> iterator = this.responsibles.iterator(); iterator.hasNext();) {
				BaseUser user = iterator.next();
				if(user.getId().equals(this.getWebUser().getId())){
					falg = false;
				}
			}
		}
		if(falg){
			super.send();
		}
		
	}
	
	

}
