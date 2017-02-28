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
import java.util.Collection;
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
import net.gdsc.service.importexcel.ExcelImportService;
import net.gdsc.service.importexcel.IExcelImportService;
import net.gdsc.util.DateUtil;
import net.gdsc.util.StringUtil;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.myapps.core.user.ejb.UserVO;

import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName: ExcelImportTestAction 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-08-26 上午8:30:44 
 *  
 */
public class ExcelImportTestAction extends ActionSupport{
	
	/** 
	* @Fields serialVersionUID : TODO
	*/
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ExcelImportTestAction.class);
	public String result;
	public IExcelImportService excelImportService = ExcelImportService.getInstance();
	public String doImportExcel(){
		HttpServletRequest request= ServletActionContext.getRequest();
		String path = request.getParameter("path");
		FileInputStream file = null;
		List<FingerDataVO> list = new ArrayList<FingerDataVO>();
		List<FingerDataVO> listAll = new ArrayList<FingerDataVO>();
		boolean flagA = false;
		boolean flagB = false;
		long timeA = 0L;
		long timeB = 0L;
		long timeC = 0L;
		long timeD = 0L;
		long timeE = 0L;
		List<String> dates = null;
		Collection<UserVO> users = null;
		try {
			if(StringUtil.isBlank(path)){
				return null;
			}else{
				file = new FileInputStream(request.getSession().getServletContext().getRealPath("")+path+".xls");//本地电脑端代码
//				file = new FileInputStream(request.getSession().getServletContext().getRealPath("")+path);  //该处为服务器端代码
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
	                	 timeB = DateUtil.parseDate("08:30:00","HH:mm:ss").getTime();
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
            		if(timeA > timeB && timeA <= timeE){
            			finger.setStatusFinger("迟到");
            		}else if(timeA > timeE && timeA < timeC){
            			finger.setStatusFinger("早退");
            		}else{
            			finger.setStatusFinger("正常");
            		}
            	}else{//非星期六情况
            		if(timeA > timeB && timeA < timeC){//介于08:30:00 与 12：00：00
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
            		users = excelImportService.getUserByDomain();//获取该域名下所有的用户集合
            		String number = null;
            		FingerDataVO fingerLess = null;
            		FingerDataVO fingerMore = null;
            		FingerDataVO aFinger = null;
            		String lessTime = DateUtil.formatDate(DateUtil.parseDate("14:30:00","HH:mm:ss"),"HH:mm:ss");
            		String moreTime = DateUtil.formatDate(DateUtil.parseDate("16:30:00","HH:mm:ss"),"HH:mm:ss");
            		if(!"".equals(dates)){
            			for(String date : dates){
            				for(UserVO user : users){
            					aFinger = new FingerDataVO();
            					number = user.getField1();//获取工号
            					aFinger.setNumberFinger(number);//工号
            					aFinger.setDateFinger(date);////日期：2016-08-23
            					aFinger.setNameFinger(user.getName()); //姓名
            					aFinger.setDeparmentFinger(user.getDefaultDepartment()); //部门名称
            					aFinger.setWeekDay(DateUtil.getChineseWeek(DateUtil.parseDateTime(date)));//设置星期
            					aFinger.setScount(2);//应打卡次数
            					fingerLess = excelImportService.getFingerVOs(date, number,lessTime,"less").get(0);
            					if(fingerLess != null){
            						aFinger.setGoTime(fingerLess.getExtractTimeFinger()); //上班时间
            						aFinger.setGoRecorder(fingerLess.getStatusFinger());  //上班状态
            						aFinger.setRcount(1);//实际打卡
            					}else{
            						aFinger.setGoTime("未打卡"); //上班时间
            						aFinger.setGoRecorder("未打卡");  //上班状态
            						aFinger.setRcount(0);//实际打卡
            					}
            					fingerMore = excelImportService.getFingerVOs(date, number, moreTime, "more").get(0);
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
//            		logger.info("插入"+listAll.size()+"条数据!");
//            		result = "成功导入"+listAll.size()+"条数据!";
            	}else{
            		result = "导入数据失败,请检查文档模版格式或联系管理员!";
            	}
            	if(flagB){
            		logger.info("插入"+listAll.size()+"条数据!");
            		result = "成功导入"+listAll.size()+"条数据!";
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
