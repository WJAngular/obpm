/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.service.importexcel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.gdsc.dao.importexcel.ExcelImportDao;
import net.gdsc.dao.importexcel.IExcelImportDao;
import net.gdsc.model.FingerDataVO;
import net.gdsc.model.UserInfoVO;

import org.apache.log4j.Logger;

import cn.myapps.attendance.attendance.ejb.Attendance;
import cn.myapps.attendance.attendance.ejb.AttendanceDetail;
import cn.myapps.attendance.attendance.ejb.AttendanceDetailProcess;
import cn.myapps.attendance.attendance.ejb.AttendanceDetailProcessBean;
import cn.myapps.attendance.base.ejb.BaseProcess;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;

/** 
 * @ClassName: ExcelImportService 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-08-14 下午6:59:00 
 *  
 */
public class ExcelImportService implements IExcelImportService{
	
	private static final Logger logger = Logger.getLogger(ExcelImportService.class);
	private static IExcelImportDao excelImportDao;
	private BaseProcess<AttendanceDetail> process = null;
	private static ExcelImportService excelImportService;
	public static synchronized ExcelImportService getInstance(){
		if(excelImportService==null)
			excelImportService = new ExcelImportService();
		return excelImportService;
	}
	/** 
	* @Title: insertFingerVOs 
	* @Description: TODO
	* @param: @param fingers 
	* @throws 
	*/
	@Override
	public boolean insertFingerVOs(List<FingerDataVO> fingers,String type) {
		// TODO 自动生成的方法存根
		excelImportDao = ExcelImportDao.getInstance();
		boolean flag = false;
		try {
			flag = excelImportDao.insertFingerVOs(fingers,type);
		} catch (Exception e) {
			logger.error("操作数据异常，请联系管理员!",e);
			e.printStackTrace();
		}
		return flag;
	}
	/** 
	* @Title: getDates 
	* @Description: TODO
	* @param: @return 
	* @throws 
	*/
	@Override
	public List<String> getDates() {
		excelImportDao = ExcelImportDao.getInstance();
		List<String> dates = null;
		try {
			dates = excelImportDao.getDates();
		} catch (Exception e) {
			logger.error("操作数据异常，请联系管理员!",e);
			e.printStackTrace();
		}
		return dates;
	}
	
	public List<UserVO> getUserByDomain(){
		Collection<UserVO> users = null;
		List<UserVO> usersA = new ArrayList<UserVO>();
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			users = userProcess.queryByDomain("11e6-3d0d-ba351983-b6a6-2fcfcefd00c4");  //获取该域名id下面的所有用户
			for(UserVO user : users){//过滤不在职人员
				if(user.getDimission() == 1){  //1表示onjob(在职)
					usersA.add(user);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usersA;
	}
	/** 
	* @Title: getFingerVOs 
	* @Description: TODO
	* @param: @param date
	* @param: @param number
	* @param: @return 
	* @throws 
	*/
	@Override
	public List<FingerDataVO> getFingerVOs(String date, String number,String time,String type) {
		excelImportDao = ExcelImportDao.getInstance();
		List<FingerDataVO> fingers = null;
		try {
			fingers = excelImportDao.getFingerVOs(date,number,time,type);
		} catch (Exception e) {
			logger.error("操作数据异常，请联系管理员!",e);
			e.printStackTrace();
		}
		return fingers;
	}
	/** 
	* @Title: getDeparmentNameById 
	* @Description: TODO
	* @param: @param id
	* @param: @return 
	* @throws 
	*/
	@Override
	public DepartmentVO getDeparmentNameById(String id) {
		DepartmentVO department = null;
		try {
			DepartmentProcess departmentProcess = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			department = (DepartmentVO)departmentProcess.doView(id);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return department;
	}
	/** 
	* @Title: getUsers 
	* @Description: TODO
	* @param: @return 
	* @throws 
	*/
	@Override
	public List<UserInfoVO> getUsers() {
		excelImportDao = ExcelImportDao.getInstance();
		List<UserInfoVO> users = null;
		try {
			users = excelImportDao.getUsers();
		} catch (Exception e) {
			logger.error("操作数据异常，请联系管理员!",e);
			e.printStackTrace();
		}
		return users;
	}
	/** 
	* @Title: deleteTempTable 
	* @Description: TODO
	* @param:  
	* @throws 
	*/
	@Override
	public void deleteTempTable() {
		excelImportDao = ExcelImportDao.getInstance();
		try {
			excelImportDao.deleteTempTable();
		} catch (Exception e) {
			logger.error("操作数据异常，请联系管理员!",e);
			e.printStackTrace();
		}
	}
	/** 
	* @Title: getWecharAttendance 
	* @Description: TODO
	* @param: @param date
	* @param: @return 
	* @throws 
	*/
	@Override
	public List<AttendanceDetail> getWecharAttendance(String date) {
		List<AttendanceDetail> attendances = null;
		try {
			process = new AttendanceDetailProcessBean();
			attendances = ((AttendanceDetailProcess)process).findAttendanceDetailByDate(date);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attendances;
	}
	/** 
	* @Title: getFingerVOByUserNameAndDate 
	* @Description: TODO
	* @param: @param userName
	* @param: @param date
	* @param: @return 
	* @throws 
	*/
	@Override
	public FingerDataVO getFingerVOByUserNameAndDate(String jobNo,
			String date) {
		excelImportDao = ExcelImportDao.getInstance();
		FingerDataVO finger = null;
		try {
			finger = excelImportDao.getFingerVOByUserNameAndDate(jobNo, date);
		} catch (Exception e) {
			logger.error("操作数据异常，请联系管理员!",e);
			e.printStackTrace();
		}
		return finger;
	}
	/** 
	* @Title: getJobNo 
	* @Description: 获取工号
	* @param: @param userId
	* @param: @return 
	* @throws 
	*/
	@Override
	public String getJobNo(String userId) {
		String jobNo = null;
		try {
			UserProcess userProcess = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
			UserVO userVO = (UserVO)userProcess.doView(userId);
			jobNo = userVO.getField1();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jobNo;
	}
	/** 
	* @Title: updateFingerDataVOs 
	* @Description: TODO
	* @param: @param fingerWechats
	* @param: @return 
	* @throws 
	*/
	@Override
	public boolean updateFingerDataVOs(List<FingerDataVO> fingerWechats) {
		excelImportDao = ExcelImportDao.getInstance();
		boolean flag = false;
		try {
			flag = excelImportDao.updateFingerDataVOs(fingerWechats);
		} catch (Exception e) {
			logger.error("操作数据异常，请联系管理员!",e);
			e.printStackTrace();
		}
		return flag;
	}
}
