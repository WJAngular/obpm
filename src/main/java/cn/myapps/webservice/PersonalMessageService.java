package cn.myapps.webservice;

import cn.myapps.core.personalmessage.ejb.MessageBody;
import cn.myapps.core.personalmessage.ejb.PersonalMessageProcess;
import cn.myapps.core.personalmessage.ejb.PersonalMessageVO;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;

public class PersonalMessageService {
	
	/**
	 * 发送站内短信
	 * @param title
	 * 		标题
	 * @param content
	 * 		内容
	 * @param senderId
	 * 		发送者ID
	 * @param receiverIds
	 * 		接受者Id 多个接受者用“;”号隔开
	 * @param applicationId
	 * 		软件id
	 * @return
	 * 		0成功发送， -1发送失败
	 */
	public int send(String title,String content,String senderId,String receiverIds,String applicationId) {
		int result = -1;
		try {
			UserProcess userProcess = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
			PersonalMessageProcess process = (PersonalMessageProcess) ProcessFactory.createProcess(PersonalMessageProcess.class);
			UserVO sender = (UserVO) userProcess.doView(senderId);
			PersonalMessageVO vo = new PersonalMessageVO();
			vo.setSenderId(senderId);
			vo.setDomainid(sender.getDomainid());
			vo.setApplicationid(applicationId);
			vo.setReceiverId(receiverIds);
			vo.setType("0");
			
			MessageBody body = new MessageBody();
			body.setTitle(title);
			body.setContent(content);
			body.setApplicationid(applicationId);
			body.setDomainid(sender.getDomainid());
			vo.setBody(body);
			process.doCreateByUserIds(receiverIds.split(";"), vo);
			result = 0;
		} catch (Exception e) {
			
		}
		
		return result;
	}

}
