package cn.myapps.km.disk.ejb;

import cn.myapps.km.org.ejb.NUser;

public class NFileHelper {
	public boolean isSharedInPublic(String nfileid, NUser user) throws Exception{
		boolean flag = false;
		NDirProcess nDirProcess = new NDirProcessBean();
		NDiskProcess nDiskProcess = new NDiskProcessBean();
		NFileProcess nFileProcess = new NFileProcessBean();
		
		try {
			NDisk disk = nDiskProcess.getPublicDisk(user.getDomainid());
			NDir ndir = nDirProcess.getPublicShareNDir(disk.getId());
			
			flag = nFileProcess.isSharedInThisNdir(ndir.getId(), nfileid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}
}
