/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.service.prostage;

import net.gdsc.model.StageVO;

/** 
 * @ClassName: IProStageService 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-07-17 下午1:09:36 
 *  
 */
public interface IProStageService {
	/**
	 * 
	* @Title: insertBatch 
	* @Description: 批量新增StageVO
	* @param: @param stage 
	* @return: void
	* @throws
	 */
	public void insertBatch(StageVO stage);
	/**
	 * 
	* @Title: delete 
	* @Description: 删除相应的项目阶段状态
	* @param: @param id 
	* @return: void
	* @throws
	 */
	public void delete(String id);
}
