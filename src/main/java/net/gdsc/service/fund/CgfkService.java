/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.service.fund;

import net.gdsc.dao.fund.CgfkDao;
import net.gdsc.dao.fund.ICgfkDao;
import net.gdsc.model.CgfkVO;
import net.gdsc.model.LxfVO;
import net.gdsc.model.WlkVO;
import net.gdsc.model.YwfVO;
import net.gdsc.model.ZdfVO;
import net.gdsc.model.ZtbVO;

import org.apache.log4j.Logger;

import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;

/** 
 * @ClassName: CgfkService 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-08-09 下午4:02:39 
 *  
 */
public class CgfkService implements ICgfkService{
	
	private static final Logger logger = Logger.getLogger(CgfkService.class);
	private static ICgfkDao cgfkDao;
	private static CgfkService cgfkService;
	public static synchronized CgfkService getInstance(){
		if(cgfkService==null)
			cgfkService = new CgfkService();
		return cgfkService;
	}
	/** 
	* @Title: queryCgfkByNoAndTableName 
	* @Description: TODO
	* @param: @param docNo
	* @param: @param primaryTable
	* @param: @return 
	* @throws 
	*/
	@Override
	public Object queryCgfkByNoAndTableName(String docNo, String primaryTable) {
		CgfkVO cgfkVO = null;
		YwfVO ywfVO = null;
		ZtbVO ztbVO = null;
		ZdfVO zdfVO = null;
		LxfVO lxfVO = null;
		WlkVO wlkVO = null;
		Object object = null;
		UserVO user = null;
		DepartmentVO department = null;
		cgfkDao = CgfkDao.getInstance();
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
            DepartmentProcess departmentProcess = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
            if(primaryTable.indexOf("采购货款") > -1){cgfkVO = (CgfkVO)cgfkDao.queryCgfkByNoAndTableName(docNo, primaryTable);}
            if(primaryTable.indexOf("招投标") > -1){ztbVO = (ZtbVO)cgfkDao.queryCgfkByNoAndTableName(docNo, primaryTable);}
            if(primaryTable.indexOf("业务费") > -1){ywfVO = (YwfVO)cgfkDao.queryCgfkByNoAndTableName(docNo, primaryTable);}
            if(primaryTable.indexOf("招待费") > -1){zdfVO = (ZdfVO)cgfkDao.queryCgfkByNoAndTableName(docNo, primaryTable);}
            if(primaryTable.indexOf("零星费") > -1){lxfVO = (LxfVO)cgfkDao.queryCgfkByNoAndTableName(docNo, primaryTable);}
            if(primaryTable.indexOf("往来款") > -1){wlkVO = (WlkVO)cgfkDao.queryCgfkByNoAndTableName(docNo, primaryTable);}
            //业务费申请ID转为name
			if(primaryTable.indexOf("业务费") > -1 && ywfVO != null){
				if(!"".equals(ywfVO.getDepartName())){//申请部门的值为其id，则将id转换成name
					department = (DepartmentVO)departmentProcess.doView(ywfVO.getDepartName());
					ywfVO.setDepartName(department.getName());
				}
			}
			//往来款申请ID转为name
			if(primaryTable.indexOf("往来款") > -1 && wlkVO != null){
				if(!"".equals(wlkVO.getDepartName())){//申请部门的值为其id，则将id转换成name
					department = (DepartmentVO)departmentProcess.doView(wlkVO.getDepartName());
					wlkVO.setDepartName(department.getName());
				}
			}
			//零星费申请ID转为name
			if(primaryTable.indexOf("零星费") > -1 && lxfVO != null){
				if(!"".equals(lxfVO.getDepartName())){//申请部门的值为其id，则将id转换成name
					department = (DepartmentVO)departmentProcess.doView(lxfVO.getDepartName());
					lxfVO.setDepartName(department.getName());
				}
			}
			//招待费申请ID转为name
			if(primaryTable.indexOf("招待费") > -1 && zdfVO != null){
				if(!"".equals(zdfVO.getDepartName())){//申请部门的值为其id，则将id转换成name
					department = (DepartmentVO)departmentProcess.doView(zdfVO.getDepartName());
					zdfVO.setDepartName(department.getName());
				}
			}
			//招投标申请ID转为name
			if(primaryTable.indexOf("招投标") > -1 && ztbVO != null){
				if(!"".equals(ztbVO.getDepartName())){//申请部门的值为其id，则将id转换成name
					department = (DepartmentVO)departmentProcess.doView(ztbVO.getDepartName());
					ztbVO.setDepartName(department.getName());
				}
//				if(!"".equals(ztbVO.getManager())){ //项目经理
//					user = (UserVO) userProcess.doView(ztbVO.getManager());
//					ztbVO.setManager(user.getName());
//				}
			}
			//采购货款申请
			if(primaryTable.indexOf("采购货款") > -1 && cgfkVO != null){
				if(!"".equals(cgfkVO.getDepartName())){//申请部门的值为其id，则将id转换成name
					department = (DepartmentVO)departmentProcess.doView(cgfkVO.getDepartName());
					cgfkVO.setDepartName(department.getName());
				}
				if(!"".equals(cgfkVO.getWarehouse())){ //仓管责任人   一个人
					user = (UserVO) userProcess.doView(cgfkVO.getWarehouse());
					cgfkVO.setWarehouse(user.getName());
				}
				if(!"".equals(cgfkVO.getCaiGou())){ //采购部责任人   一个人
					user = (UserVO) userProcess.doView(cgfkVO.getCaiGou());
					cgfkVO.setCaiGou(user.getName());
				}
//				if(!"".equals(cgfkVO.getManager())){ //项目经理 
//					if(cgfkVO.getManager().indexOf(";") > -1){   //  如果项目经理多个人
//						String[] managers = null;
//						UserVO u = null;
//						String m = null;
//						managers = cgfkVO.getManager().split(";");
//						StringBuffer buffer = new StringBuffer();
//						for(int i = 0 ;i < managers.length;i++){
//							u = (UserVO) userProcess.doView(managers[i]);
//							buffer.append(u.getName()).append(",");
//							if(i == (managers.length - 1)){
//								m = buffer.toString().substring(0, buffer.toString().length() - 1);
//							}
//						}
//						cgfkVO.setManager(m);
//					}else{
//						user = (UserVO) userProcess.doView(cgfkVO.getManager());
//						cgfkVO.setManager(user.getName());
//					}
//				}
			}
			
			if(primaryTable.indexOf("采购货款") > -1){object = cgfkVO;}
			if(primaryTable.indexOf("招投标") > -1){object = ztbVO;}
			if(primaryTable.indexOf("业务费") > -1){object = ywfVO;}
			if(primaryTable.indexOf("招待费") > -1){object = zdfVO;}
			if(primaryTable.indexOf("零星费") > -1){object = lxfVO;}
			if(primaryTable.indexOf("往来款") > -1){object = wlkVO;}
			
		} catch (Exception e) {
			logger.error("操作数据异常，请联系管理员!",e);
			e.printStackTrace();
		}
		return object;
	}

}
