package cn.myapps.core.personalmessage;

import cn.myapps.core.personalmessage.ejb.MessageBody;
import cn.myapps.core.personalmessage.ejb.PersonalMessageProcess;
import cn.myapps.core.personalmessage.ejb.PersonalMessageVO;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;

public class PersonalMessageTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String testuserId = "11de-c13a-0cf76f8b-a3db-1bc87eaaad4c";
		String happyId = "11e1-7332-b2cc06c5-8112-3bbdf6cee0c4";
		String applicationId ="11e1-7326-36a83451-9f50-bd5c844c7a70";
		UserProcess userProcess = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
		PersonalMessageProcess process = (PersonalMessageProcess) ProcessFactory.createProcess(PersonalMessageProcess.class);
		UserVO sender = (UserVO) userProcess.doView(testuserId);
		PersonalMessageVO vo = new PersonalMessageVO();
		vo.setSenderId(testuserId);
		vo.setDomainid(sender.getDomainid());
		vo.setApplicationid(applicationId);
		vo.setReceiverId(happyId+";");
		vo.setType("0");
		
		MessageBody body = new MessageBody();
		body.setTitle("标题");
		body.setContent("neoronga  youbuyou!");
		body.setApplicationid(applicationId);
		body.setDomainid(sender.getDomainid());
		vo.setBody(body);
		process.doCreateByUserIds(new String[]{happyId}, vo);

	}

}
