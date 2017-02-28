/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.dao.bill;

import net.gdsc.model.BillVO;

/** 
 * @ClassName: IBillDao 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-07-26 上午10:40:21 
 *  
 */
public interface IBillDao {
	/**
	 * 
	* @Title: queryBillVO 
	* @Description: TODO
	* @param: @param id
	* @param: @param primaryTable
	* @param: @param childTable
	* @param: @return 
	* @return: BillVO
	* @throws
	 */
	public BillVO queryBillVO(String proNo,String primaryTable);
}
