/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.service.client;

import net.gdsc.model.ClientVO;

/** 
 * @ClassName: IClientService 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-07-08 下午9:58:51 
 *  
 */
public interface IClientService {
	/**
	 * 
	* @Title: create 
	* @Description: 新增客户基本信息
	* @param: @param clientVO 
	* @return: void
	* @throws
	 */
	public void create(ClientVO clientVO);
	/**
	 * 
	* @Title: findByDataFlag 
	* @Description: 设置客户编号
	* @param: @return 
	* @return: List<ClientVO>
	* @throws
	 */
	public ClientVO findByDataFlag();
	/**
	 * 
	* @Title: findByClientNo 
	* @Description: 查找客户编号是否存在
	* @param: @return 
	* @return: ClientVO
	* @throws
	 */
	public ClientVO findByClientNo(String clientNo);
}
