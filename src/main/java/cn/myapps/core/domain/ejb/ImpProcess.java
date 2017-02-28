package cn.myapps.core.domain.ejb;

import java.io.File;
import java.io.Serializable;

public interface ImpProcess extends Serializable{
	
	/**
	 * 企业域信息的导入
	 * @param impFile
	 * 			所导入的文件
	 * @throws Exception
	 */
	public void doImport(File impFile) throws Exception;
}
