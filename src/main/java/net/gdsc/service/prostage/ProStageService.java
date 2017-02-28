/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.service.prostage;

import net.gdsc.dao.prostage.IProStageDao;
import net.gdsc.dao.prostage.ProStageDao;
import net.gdsc.model.StageVO;

import org.apache.log4j.Logger;

/** 
 * @ClassName: ProStageService 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-07-17 下午1:10:35 
 *  
 */
public class ProStageService implements IProStageService{
	private static final Logger logger = Logger.getLogger(ProStageService.class);
	private static IProStageDao proStageDao;
	private static ProStageService proStageService;
	public static synchronized ProStageService getInstance(){
		if(proStageService==null)
			proStageService = new ProStageService();
		return proStageService;
	}
	/** 
	* @Title: insertBatch 
	* @Description: TODO
	* @param: @param stage 
	* @throws 
	*/
	@Override
	public void insertBatch(StageVO stage) {
		proStageDao = ProStageDao.getInstance();
		try {
			proStageDao.insertBatch(stage);
		} catch (Exception e) {
			logger.error("操作数据异常，请联系管理员!",e);
			e.printStackTrace();
		}
	}
	/** 
	* @Title: delete 
	* @Description: TODO
	* @param: @param id 
	* @throws 
	*/
	@Override
	public void delete(String id) {
		proStageDao = ProStageDao.getInstance();
		try {
			proStageDao.delete(id);;
		} catch (Exception e) {
			logger.error("操作数据异常，请联系管理员!",e);
			e.printStackTrace();
		}
		
	}

}
