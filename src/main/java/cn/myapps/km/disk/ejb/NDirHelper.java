package cn.myapps.km.disk.ejb;

import java.util.Collection;

public class NDirHelper {
	
	/**
	 * 根据网盘获取网盘下所有的目录
	 * @param ndiskid
	 * @return
	 * @throws Exception
	 */
	public Collection<NDir> getNDirByNDisk(String ndiskid) throws Exception{
		try {
			NDirProcess process = new NDirProcessBean();
			return process.getNDirByNDisk(ndiskid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 根据网盘获取网盘下跟目录
	 * @param ndiskid
	 * @return
	 * @throws Exception
	 */
	public Collection<NDir> getRootNDirByNDisk(String ndiskid) throws Exception{
		try {
			NDirProcess process = new NDirProcessBean();
			return process.getUnderNDirByDisk(ndiskid, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
