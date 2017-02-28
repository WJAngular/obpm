package cn.myapps.init;

import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;

/**
 * The user initialization object.
 */
public class InitUserInfo implements IInitialization {

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.init.IInitialization#run()
	 */
	public void run() throws InitializationException {
		try {
			SuperUserProcess process = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);
			//CalendarProcess cldprocess = (CalendarProcess) ProcessFactory.createProcess(CalendarProcess.class);
			
			// DataPackage dp = process.doQuery(new ParamsTable(), null);
			if (process.isEmpty()) {

				SuperUserVO user = new SuperUserVO();
				user.setId(Sequence.getSequence());
				user.setSortId(Sequence.getTimeSequence());
				user.setApplicationid(null);
				user.setName("Admin");
				user.setLoginno("admin");
				user.setLoginpwd("teemlink");
				user.setSuperAdmin(true);
				user.setDomainPermission(SuperUserVO.NORMAL_DOMAIN);
				user.setStatus(1);
//				if (cldprocess!=null){
//					CalendarVO cld=(CalendarVO)cldprocess.doViewByName("标准日历");
//					user.setCalendarType(cld.getId());
//				}
				process.doCreate(user);
			}
		} catch (Exception e) {
			throw new InitializationException(e.getMessage());
		}
	}
}
