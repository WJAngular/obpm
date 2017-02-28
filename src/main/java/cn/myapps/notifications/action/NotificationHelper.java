package cn.myapps.notifications.action;

import java.util.concurrent.ConcurrentHashMap;
import cn.myapps.core.personalmessage.ejb.PersonalMessageProcess;
import cn.myapps.util.ProcessFactory;

public class NotificationHelper {
	
	/**
	 * 储存用户新消息总数的MAP对象
	 * key -用户Id
	 * value -新消息总数
	 */
	public static ConcurrentHashMap<String, Integer> newMsgCountMap = new ConcurrentHashMap<String, Integer>();
	
	
	
	/**
	 * 根据传入的用户Id获取新消息总数
	 * @param userId
	 * @return
	 */
	public static int getNewMessageCountByUser(String userId){
		//Integer count = newMsgCountMap.get(userId);
		//if(count == null){
		int count = 0;
			try {
				PersonalMessageProcess process = (PersonalMessageProcess)ProcessFactory.createProcess(PersonalMessageProcess.class);
				count = process.countNewMessages(userId);
				//newMsgCountMap.put(userId, count);
			} catch (Exception e) {
				e.printStackTrace();
			}
		//}
		
		return count;
	}
	
	/**
	 * 根据传入的用户id增加新消息总数（自加一）
	 * @param userId
	 */
	public static void addNewMessageCount4User(String userId){
		Integer count = newMsgCountMap.get(userId);
		if(count == null){
			try {
				PersonalMessageProcess process = (PersonalMessageProcess)ProcessFactory.createProcess(PersonalMessageProcess.class);
				count = process.countNewMessages(userId);
				newMsgCountMap.put(userId, count+1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			newMsgCountMap.put(userId, count+1);
		}
	}
	
	/**
	 * 根据传入的用户id减少新消息总数（自减一）
	 * @param userId
	 */
	public static void subtractNewMessageCount4User(String userId){
		Integer count = newMsgCountMap.get(userId);
		if(count == null){
			try {
				PersonalMessageProcess process = (PersonalMessageProcess)ProcessFactory.createProcess(PersonalMessageProcess.class);
				count = process.countNewMessages(userId);
				newMsgCountMap.put(userId, count);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			newMsgCountMap.put(userId, count-1);
		}
	}
	

	
	
	public static void main(String[] args) {
	}

}
