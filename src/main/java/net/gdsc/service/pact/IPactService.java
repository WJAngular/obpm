/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.service.pact;

import java.util.List;

import net.gdsc.model.PactVO;

/** 
 * @ClassName: IPactService 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-07-28 下午2:58:41 
 *  
 */
public interface IPactService {
	/**
	 * 
	* @Title: queryHistory 
	* @Description: 查询结算中项目合同
	* @param: @param proName 项目名称
	* @param: @param proNo 项目编号
	* @param: @param primaryTable 主表
	* @param: @return 
	* @return: List<PactVO>
	* @throws
	 */
	public List<PactVO> queryHistory(String proName,String proNo,String primaryTable);

}
