package cn.myapps.core.helper.ejb;

import cn.myapps.base.ejb.IDesignTimeProcess;

public interface HelperProcess extends IDesignTimeProcess<HelperVO>{
	
	public HelperVO getHelperByName(String urlname, String application) throws Exception;
}
