package cn.myapps.core.domain.ejb;

import java.io.File;
import java.io.Serializable;

public interface ExpProcess extends Serializable{
	
	/**
	 * 创建企业域信息导出的Zip文件
	 * @param elements
	 * 			企业域信息的元素集合
	 * @param domain
	 * 			该企业域
	 * @return
	 * 			Zip文件
	 * @throws Exception
	 */
	public File createZipFile(ExpImpElements elements, String domain) throws Exception;
	
	/**
	 * 获取企业域信息的导出文件
	 * @param fileName
	 * 			文件名
	 * @return
	 * 			Zip文件
	 * @throws Exception
	 */
	public File getExportFile(String fileName) throws Exception;
}
