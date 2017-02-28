package cn.myapps.util.converter;

import java.io.Serializable;


/**
 * 文件转换器
 * @author xiuwei
 *
 */
public interface Converter extends Serializable {
	
	
	/**
	 * 做一些初始化的准备
	 * @throws Exception
	 */
	public void init() throws Exception;
	
	/**
	 * 执行转换
	 * @param source
	 * 		源文件
	 * @param dest
	 * 		目标文件
	 * @param params
	 * 		参数
	 * @return
	 * @throws Exception
	 */
	public void convert(String source, String destPath,String fileName) throws Exception;

}
