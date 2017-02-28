/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.service.importexcel;

import java.util.List;

import net.gdsc.model.FingerDataVO;
import net.gdsc.model.UserInfoVO;
import cn.myapps.attendance.attendance.ejb.AttendanceDetail;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.user.ejb.UserVO;

/** 
 * @ClassName: IExcelImportService 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-08-14 下午6:58:26 
 *  
 */
public interface IExcelImportService {
	public boolean insertFingerVOs(List<FingerDataVO> fingers,String type);
	
	public List<String> getDates();
	
	public List<UserVO> getUserByDomain();
	
	public DepartmentVO getDeparmentNameById(String id);
	
	public List<FingerDataVO> getFingerVOs(String date,String number,String time,String type);
	
	public List<UserInfoVO> getUsers();
	
	public List<AttendanceDetail> getWecharAttendance(String date);
	
	public void deleteTempTable();
	
	public FingerDataVO getFingerVOByUserNameAndDate(String jobNo,String date);
	
	public String getJobNo(String userId);
	
	public boolean updateFingerDataVOs(List<FingerDataVO> fingerWechats);
}
