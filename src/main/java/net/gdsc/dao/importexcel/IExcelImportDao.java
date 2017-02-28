/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.dao.importexcel;

import java.util.List;

import net.gdsc.model.FingerDataVO;
import net.gdsc.model.UserInfoVO;

/** 
 * @ClassName: IExcelImportDao 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-08-14 下午4:31:45 
 *  
 */
public interface IExcelImportDao {
	public boolean insertFingerVOs(List<FingerDataVO> fingers,String type);
	
	public List<String> getDates();
	
	public List<FingerDataVO> getFingerVOs(String date,String number,String time,String type);
	
	public List<UserInfoVO> getUsers();
	
	public void deleteTempTable();
	
	public FingerDataVO getFingerVOByUserNameAndDate(String jobNo,String date);
	
	public boolean updateFingerDataVOs(List<FingerDataVO> fingerWechats);
}
