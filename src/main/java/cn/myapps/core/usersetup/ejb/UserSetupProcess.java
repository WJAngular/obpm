package cn.myapps.core.usersetup.ejb;

import cn.myapps.base.ejb.IDesignTimeProcess;

public interface UserSetupProcess extends IDesignTimeProcess<UserSetupVO> {
	
	
	public UserSetupVO getUserSetupByUserId(String uId) throws Exception;
	
}
