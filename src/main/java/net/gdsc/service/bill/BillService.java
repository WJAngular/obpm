/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.service.bill;

import net.gdsc.dao.bill.BillDao;
import net.gdsc.dao.bill.IBillDao;
import net.gdsc.model.BillVO;

import org.apache.log4j.Logger;

/** 
 * @ClassName: BillService 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-07-26 上午11:23:42 
 *  
 */
public class BillService implements IBillService{
	private static final Logger logger = Logger.getLogger(BillService.class);
	private static IBillDao billDao;
	private static BillService billService;
	public static synchronized BillService getInstance(){
		if(billService==null)
			billService = new BillService();
		return billService;
	}
	/** 
	* @Title: queryBillVO 
	* @Description: 查询结算VO
	* @param: @param id
	* @param: @param primaryTable
	* @param: @param childTable
	* @param: @return 
	* @throws 
	*/
	@Override
	public BillVO queryBillVO(String proNo,String primaryTable) {
		BillVO bill = null;
		billDao = BillDao.getInstance();
		try {
			bill = billDao.queryBillVO(proNo,primaryTable);
		} catch (Exception e) {
			logger.error("操作数据异常，请联系管理员!",e);
			e.printStackTrace();
		}
		return bill;
	}

}
