/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.service.client;

import net.gdsc.dao.client.ClientDao;
import net.gdsc.dao.client.IClientDao;
import net.gdsc.model.ClientVO;

import org.apache.log4j.Logger;

/** 
 * @ClassName: ClientService 
 * @Description: 客户服务端 
 * @author: WUJING 
 * @date :2016-07-08 下午9:58:34 
 *  
 */
public class ClientService implements IClientService{
	
	private static final Logger logger = Logger.getLogger(ClientService.class);
	private static IClientDao clientDao;
	private static ClientService clientService;
	public static synchronized ClientService getInstance(){
		if(clientService==null)
			clientService = new ClientService();
		return clientService;
	}
	/** 
	* @Title: create 
	* @Description: TODO
	* @param: @param clientVO 
	* @throws 
	*/
	@Override
	public void create(ClientVO clientVO) {
		clientDao = ClientDao.getInstance();
		try {
			clientDao.create(clientVO);
		} catch (Exception e) {
			logger.error("操作数据异常，请联系管理员!",e);
			e.printStackTrace();
		}
	}
	/** 
	* @Title: findByDataFlag 
	* @Description: TODO
	* @param: @return 
	* @throws 
	*/
	@Override
	public ClientVO findByDataFlag() {
		ClientVO client = null;
		clientDao = ClientDao.getInstance();
		try {
			client = clientDao.findByDataFlag();
		} catch (Exception e) {
			logger.error("操作数据异常，请联系管理员!",e);
			e.printStackTrace();
		}
		return client;
	}
	/** 
	* @Title: findByClientNo 
	* @Description: TODO
	* @param: @param clientNo
	* @param: @return 
	* @throws 
	*/
	@Override
	public ClientVO findByClientNo(String clientNo) {
		ClientVO client = null;
		clientDao = ClientDao.getInstance();
		try {
			client = clientDao.findByClientNo(clientNo);
		} catch (Exception e) {
			logger.error("操作数据异常，请联系管理员!",e);
			e.printStackTrace();
		}
		return client;
	}
	
}
