/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.service.pact;

import java.util.ArrayList;
import java.util.List;

import net.gdsc.dao.pact.IPactDao;
import net.gdsc.dao.pact.PactDao;
import net.gdsc.model.PactVO;

import org.apache.log4j.Logger;

/** 
 * @ClassName: PactService 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-07-28 下午2:58:53 
 *  
 */
public class PactService implements IPactService{
	private static final Logger logger = Logger.getLogger(PactService.class);
	private static IPactDao pactDao;
	private static PactService pactService;
	public static synchronized PactService getInstance(){
		if(pactService==null)
			pactService = new PactService();
		return pactService;
	}
	/** 
	* @Title: queryHistory 
	* @Description: TODO
	* @param: @param proName
	* @param: @param proNo
	* @param: @param primaryTable
	* @param: @return 
	* @throws 
	*/
	@Override
	public List<PactVO> queryHistory(String proName, String proNo,
			String primaryTable) {
		List<PactVO> pactList = new ArrayList<PactVO>();
		pactDao = PactDao.getInstance();
		try {
			pactList = pactDao.queryHistory(proName, proNo, primaryTable);
		} catch (Exception e) {
			logger.error("操作数据异常，请联系管理员!",e);
			e.printStackTrace();
		}
		return pactList;
	}

}
