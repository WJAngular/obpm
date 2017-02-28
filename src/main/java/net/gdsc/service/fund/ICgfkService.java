/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.service.fund;


/** 
 * @ClassName: ICgfkService 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-08-09 下午4:02:27 
 *  
 */
public interface ICgfkService {
	public Object queryCgfkByNoAndTableName(String docNo,String primaryTable);
}
