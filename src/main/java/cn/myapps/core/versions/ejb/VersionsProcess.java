package cn.myapps.core.versions.ejb;

import java.util.Collection;

import cn.myapps.base.ejb.IDesignTimeProcess;

public interface VersionsProcess  extends IDesignTimeProcess<VersionsVO>{
	
	/**
	 * 记录obpm版本信息
	 * @param vo
	 * @throws Exception
	 */
	public void doRecordVersions(VersionsVO vo) throws Exception;

	/**
	 * 查询obpm版本信息
	 * @param name 版本名称(如：R2.5-sp8)
	 * @param number 版本号(如：16913)
	 * @param type 升级类型 	1：程序(源码)升级
	 * @return
	 * @throws Exception
	 */
	public Collection<VersionsVO> queryByVersionAndType(String name, String number, int type) throws Exception;
	
	/**
	 * 查询obpm版本信息
	 * @param type 升级类型 	1：程序(源码)升级   2：数据升级
	 * @return
	 * @throws Exception
	 */
	public Collection<VersionsVO> queryByType(int type) throws Exception;
	
	/**
	 * 查找obpm版本信息
	 * @param type 升级类型 	1：程序(源码)升级   2：数据升级
	 * @return
	 * @throws Exception
	 */
	public VersionsVO findCurrVersionByType(int type) throws Exception;
	
}
