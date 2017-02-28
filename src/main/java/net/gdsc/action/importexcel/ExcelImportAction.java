/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.action.importexcel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import net.gdsc.model.FingerDataVO;
import net.gdsc.model.UserInfoVO;
import net.gdsc.service.importexcel.ExcelImportService;
import net.gdsc.service.importexcel.IExcelImportService;
import net.gdsc.util.DateUtil;
import net.gdsc.util.StringUtil;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.myapps.attendance.attendance.ejb.AttendanceDetail;

import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName: ExcelImportAction 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-08-14 上午10:01:54 
 *  
 */
public class ExcelImportAction extends ActionSupport{

	
	/** 
	* @Fields serialVersionUID : TODO
	*/
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ExcelImportAction.class);
	public String result;
	public IExcelImportService excelImportService = ExcelImportService.getInstance();
	public String doImportExcel(){
		HttpServletRequest request= ServletActionContext.getRequest();
		String path = request.getParameter("path");
		FileInputStream file = null;
		List<FingerDataVO> list = new ArrayList<FingerDataVO>();
		List<FingerDataVO> listAll = new ArrayList<FingerDataVO>();
		List<FingerDataVO> fingerWechats = new ArrayList<FingerDataVO>();
		StringBuffer buff = new StringBuffer();
		boolean flagA = false;
		boolean flagB = false;
		boolean flagC = false;
		boolean flagD = false;
		long timeA = 0L;
		long timeB = 0L;
		long timeC = 0L;
		long timeD = 0L;
		long timeE = 0L;
		List<String> dates = null;
		List<UserInfoVO> users = null;
		try {
			if(StringUtil.isBlank(path)){
				return null;
			}else{
//				file = new FileInputStream(request.getSession().getServletContext().getRealPath("")+path+".xls");//本地电脑端代码
				file = new FileInputStream(request.getSession().getServletContext().getRealPath("")+path);  //该处为服务器端代码
			}
            Workbook wwb = Workbook.getWorkbook(file);
            Sheet sheet = wwb.getSheet(0);
//            int rowNum = sheet.getRows()-1;
            FingerDataVO finger = null;
            Cell[] cells;
            for (int i = 1; i < sheet.getRows(); i++) {
            	finger = new FingerDataVO();
                cells = sheet.getRow(i);
                if (cells.length == 0) {
                    break;
                }
                String deparment = cells[0].getContents();
                String name = cells[1].getContents();
                String number = cells[2].getContents();
                finger.setDeparmentFinger(deparment);
                finger.setNameFinger(name);
                finger.setNumberFinger(number);
            	if(cells[3].getType() == CellType.DATE){
	           		 DateCell dc = (DateCell)cells[3];
	           		 Date date = dc.getDate();	//获取单元格的date类型
	           		 // JXL读取Excel日期时间多出了8个小时。 所以获取的日期时间需要调整时区  
	           		 TimeZone zone = TimeZone.getTimeZone("GMT");
                     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                     sdf.setTimeZone(zone);
                     String sDate = sdf.format(date);
	           		 if(date != null){
	           			 finger.setDateFinger(sDate);
	           			 finger.setWeekDay(DateUtil.getChineseWeek(DateUtil.parseDateTime(sDate)));//星期
	                 	 finger.setExtractDateFinger(DateUtil.parseDate(finger.getDateFinger(),"yyyy-MM-dd"));
	                	 finger.setExtractTimeFinger(DateUtil.formatDate(DateUtil.parseDate(finger.getDateFinger(),"yyyy-MM-dd HH:mm:ss"),"HH:mm:ss"));
	                	 timeA = DateUtil.parseDate(finger.getExtractTimeFinger(),"HH:mm:ss").getTime(); 
	                	 timeB = DateUtil.parseDate("08:31:00","HH:mm:ss").getTime();
	                	 timeC = DateUtil.parseDate("12:00:00","HH:mm:ss").getTime();
	                	 timeD = DateUtil.parseDate("18:00:00","HH:mm:ss").getTime();
	                	 timeE = DateUtil.parseDate("10:30:00","HH:mm:ss").getTime();
	           		 }
            	}
                String recorder = cells[4].getContents();
                String machine = cells[5].getContents();
                String compareType = cells[7].getContents();
                finger.setRecorderFinger(recorder);
                finger.setMachineFinger(machine);
                finger.setCompareTypeFinger(compareType);
            	if("星期六".equals(finger.getWeekDay())){
            		if(timeA >= timeB && timeA <= timeE){
            			finger.setStatusFinger("迟到");
            		}else if(timeA > timeE && timeA < timeC){
            			finger.setStatusFinger("早退");
            		}else{
            			finger.setStatusFinger("正常");
            		}
            	}else{//非星期六情况
            		if(timeA >= timeB && timeA < timeC){//介于08:31:00 与 12：00：00
            			finger.setStatusFinger("迟到");
            		}else if(timeA > timeC && timeA < timeD){
            			finger.setStatusFinger("早退");
            		}else{
            			finger.setStatusFinger("正常");
            		}
            	}
                list.add(finger);
            }
            if(list != null && list.size() >0){
            	flagA = excelImportService.insertFingerVOs(list,"1");
            	if(flagA){
            		dates = excelImportService.getDates();//获取不重复的日期 list
            		users = excelImportService.getUsers();
            		List<AttendanceDetail> attendances = null;
            		String number = null;
            		FingerDataVO fingerLess = null;
            		FingerDataVO fingerMore = null;
            		FingerDataVO aFinger = null;
            		String departmentName = null;
            		List<FingerDataVO> fingersLess = null;
            		List<FingerDataVO> fingersMore = null;
            		FingerDataVO fingerWechat = null;
            		String offRecorder = null;
            		String goRecorder = null;
            		Date signin = null;
            		Date signout = null;
            		String jobNo = null;
            		String userId = null;
            		String lessTime = DateUtil.formatDate(DateUtil.parseDate("14:30:00","HH:mm:ss"),"HH:mm:ss");
            		String moreTime = DateUtil.formatDate(DateUtil.parseDate("16:30:00","HH:mm:ss"),"HH:mm:ss");
            		String weekEndTime = DateUtil.formatDate(DateUtil.parseDate("11:00:00","HH:mm:ss"),"HH:mm:ss");
            		if(dates != null && dates.size() > 0){
            			for(String date : dates){  //日期
            				for(UserInfoVO user : users){
            					aFinger = new FingerDataVO();
            					number = user.getNo();//获取工号
            					aFinger.setNumberFinger(number);//工号
            					aFinger.setExtractDateFinger(DateUtil.parseDate(date,"yyyy-MM-dd"));
            					aFinger.setDateFinger(date);////日期：2016-08-23
            					aFinger.setNameFinger(user.getName()); //姓名
            					departmentName = excelImportService.getDeparmentNameById(user.getDepartment())!=null?excelImportService.getDeparmentNameById(user.getDepartment()).getName():"";
            					aFinger.setDeparmentFinger(departmentName); //部门名称
            					aFinger.setWeekDay(DateUtil.getChineseWeek(DateUtil.parseDate(date)));//设置星期
            					aFinger.setScount(2);//应打卡次数
            					fingersLess = excelImportService.getFingerVOs(date, number,lessTime,"less");
            					if(fingersLess != null && fingersLess.size() > 0){
            						fingerLess = fingersLess.get(0);
            					}else{
            						fingerLess = null;
            					}
            					if(fingerLess != null){
            						aFinger.setGoTime(fingerLess.getExtractTimeFinger()); //上班时间
            						aFinger.setGoRecorder(fingerLess.getStatusFinger());  //上班状态
            						aFinger.setRcount(1);//实际打卡
            					}else{
            						aFinger.setGoTime("未打卡"); //上班时间
            						aFinger.setGoRecorder("未打卡");  //上班状态
            						aFinger.setRcount(0);//实际打卡
            					}
            					if("星期六".equals(aFinger.getWeekDay())){//周六时状态
            						fingersMore = excelImportService.getFingerVOs(date, number, weekEndTime, "more");
            					}else{//非周六状态
            						fingersMore = excelImportService.getFingerVOs(date, number, moreTime, "more");
            					}
            					if(fingersMore != null && fingersMore.size() > 0){
            						fingerMore = fingersMore.get(0);
            					}else{
            						fingerMore = null;
            					}
            					if(fingerMore != null){
            						aFinger.setOffTime(fingerMore.getExtractTimeFinger()); //下班时间
            						aFinger.setOffRecorder(fingerMore.getStatusFinger());  //下班状态
            						aFinger.setRcount(aFinger.getRcount()+1);//实际打卡
            					}else{
            						aFinger.setOffTime("未打卡");
            						aFinger.setOffRecorder("未打卡");
            						aFinger.setRcount(aFinger.getRcount()+0);//实际打卡
            					}
            					aFinger.setNcount(aFinger.getScount() - aFinger.getRcount());  //未打卡次数
            					listAll.add(aFinger);
            				}
            			}
            		}
            		flagB = excelImportService.insertFingerVOs(listAll,"2");
            		if(flagB){
            			for(String date : dates){
            				//微信考勤记录
            				attendances = excelImportService.getWecharAttendance(date);
            				if(attendances != null && attendances.size() > 0){
            					for(AttendanceDetail attendance : attendances){
            						//首先判断打卡机中是否存在该用户的考勤记录
            						userId = attendance.getUserId();
            						jobNo = excelImportService.getJobNo(userId);//获取工号
            						signin = attendance.getSigninTime();
            						signout = attendance.getSignoutTime();
            						fingerWechat = excelImportService.getFingerVOByUserNameAndDate(jobNo, date);
            						if(fingerWechat != null){
            							goRecorder = fingerWechat.getGoRecorder();
            							offRecorder = fingerWechat.getOffRecorder();
            							if("未打卡".equals(goRecorder) && "未打卡".equals(offRecorder)){//上下班均未打卡
            								if(signin != null){//签到
            									fingerWechat.setGoTime(DateUtil.formatDate(signin,"HH:mm:ss"));
            									fingerWechat.setRcount(fingerWechat.getRcount() + 1);
            									setGoRecoder(fingerWechat,attendance);
            									flagD = true;
            								}
            								if(signout != null){//签退
            									fingerWechat.setOffTime(DateUtil.formatDate(signout,"HH:mm:ss"));
            									fingerWechat.setRcount(fingerWechat.getRcount() + 1);
            									setOffRecoder(fingerWechat,attendance);
            									flagD = true;
            								}
            							}else if(!"未打卡".equals(goRecorder) && "未打卡".equals(offRecorder)){//上班打卡，下班不打卡 
            								if(signout != null){//签退
            									fingerWechat.setOffTime(DateUtil.formatDate(signout,"HH:mm:ss"));
            									fingerWechat.setRcount(fingerWechat.getRcount() + 1);
            									setOffRecoder(fingerWechat,attendance);
            									flagD = true;
            								}
            							}else if("未打卡".equals(goRecorder) && !"未打卡".equals(offRecorder)){//上班不打卡，下班打卡
            								if(signin != null){//签到
            									fingerWechat.setGoTime(DateUtil.formatDate(signin,"HH:mm:ss"));
            									fingerWechat.setRcount(fingerWechat.getRcount() + 1);
            									setGoRecoder(fingerWechat,attendance);
            									flagD = true;
            								}
            							}
        								fingerWechat.setNcount(fingerWechat.getScount() - fingerWechat.getRcount());
        								fingerWechats.add(fingerWechat);
            						}
            					}
            				}
            			}
            		  //将微信考勤的信息，进行批量更新到考勤表中
            			if(flagD){
            				flagC = excelImportService.updateFingerDataVOs(fingerWechats);
            			}
            		}
            	}else{
            		result = "导入数据失败,请检查文档模版格式或联系管理员!";
            	}
            	if(flagB){
            		for(int i=0;i<fingerWechats.size();i++){
            			if(i == fingerWechats.size() - 1){
            				buff.append(fingerWechats.get(i).getNameFinger());
            			}else{
            				buff.append(fingerWechats.get(i).getNameFinger()).append(",");
            			}
            		}
            		excelImportService.deleteTempTable();//删除中间表数据    TLK_考勤数据中间表
            		logger.info("插入"+listAll.size()+"条数据!");
            		result = "成功导入"+listAll.size()+"条打卡机数据,"+fingerWechats.size()+"条微信考勤数据,微信考勤员工有:"+buff.toString();
            	}else{
            		result = "导入数据失败,请检查文档模版格式或联系管理员!";
            	}
            }
        } catch (FileNotFoundException e) {
        	result = "导入数据异常,请检查文档模版格式或联系管理员!";
            e.printStackTrace();
        } catch (BiffException e) {
        	result = "导入数据异常,请检查文档模版格式或联系管理员!";
            e.printStackTrace();
        } catch (IOException e) {
        	result = "导入数据异常,请检查文档模版格式或联系管理员!";
            e.printStackTrace();
        }
		return SUCCESS;
	}

	/** 
	* @Title: setGoRecoder 
	* @Description: 设置上班状态 
	* @param: @param fingerWechat
	* @param: @param attendance 
	* @return: void
	* @throws 
	*/
	public void setGoRecoder(FingerDataVO fingerWechat, AttendanceDetail attendance) {
		if(attendance.getStatus() == -1){
			fingerWechat.setGoRecorder("迟到");
		}else if(attendance.getStatus() == 1){
			fingerWechat.setGoRecorder("正常");
		}else if(attendance.getStatus() == -2){
			fingerWechat.setGoRecorder("正常");
		}else if(attendance.getStatus() == -3){
			fingerWechat.setGoRecorder("迟到");
		}else if(attendance.getStatus() == -4){
			fingerWechat.setGoRecorder("地点异常");
		}
	}
	/** 
	 * @Title: setOffRecoder 
	 * @Description: 设置下班状态 
	 * @param: @param fingerWechat
	 * @param: @param attendance 
	 * @return: void
	 * @throws 
	 */
	public void setOffRecoder(FingerDataVO fingerWechat, AttendanceDetail attendance) {
		if(attendance.getStatus() == -1){
			fingerWechat.setOffRecorder("正常");
		}else if(attendance.getStatus() == 1){
			fingerWechat.setOffRecorder("正常");
		}else if(attendance.getStatus() == -2){
			fingerWechat.setOffRecorder("早退");
		}else if(attendance.getStatus() == -3){
			fingerWechat.setOffRecorder("早退");
		}else if(attendance.getStatus() == -4){
			fingerWechat.setOffRecorder("地点异常");
		}
	}

	/** 
	 * @return result 
	 */
	public String getResult() {
		return result;
	}

	/** 
	 * @param result 要设置的 result 
	 */
	public void setResult(String result) {
		this.result = result;
	}

}
