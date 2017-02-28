/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.action.fund;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.gdsc.model.CgfkVO;
import net.gdsc.model.LxfVO;
import net.gdsc.model.WlkVO;
import net.gdsc.model.YwfVO;
import net.gdsc.model.ZdfVO;
import net.gdsc.model.ZtbVO;
import net.gdsc.service.fund.CgfkService;
import net.gdsc.service.fund.ICgfkService;
import net.gdsc.util.UnicodeUtil;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName: FundAction 
 * @Description: 财务管理->资金申请中单个数据导出Excel功能
 * @author: WUJING 
 * @date :2016-08-09 上午11:31:39 
 *  
 */
public class FundAction extends ActionSupport{

	/** 
	* @Fields serialVersionUID : TODO
	*/
	private static final long serialVersionUID = 3026426460556210395L;
	private static final Logger logger = Logger.getLogger(FundAction.class);
	
	private ICgfkService cgfkService = CgfkService.getInstance();
	
	private String fileName;
	
	private InputStream downloadFile;
	/**
	 * 
	* @Title: dofundExcelTem 
	* @Description: 临时资金管理 单个excel导出
	* @param: @return 
	* @return: String
	* @throws
	 */
	public String dofundExcelTem(){
		try {
			HttpServletRequest request= ServletActionContext.getRequest();
			HttpServletResponse response= ServletActionContext.getResponse();
			String docNo = URLDecoder.decode(request.getParameter("docNo"), "utf-8");
			String primaryTable = URLDecoder.decode(request.getParameter("primaryTable"), "utf-8");
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/download;charset=utf-8");
            ByteArrayOutputStream output = new ByteArrayOutputStream();    
            XLSTransformer transformer = new XLSTransformer();
            Map<String, Object> params = new HashMap<String, Object>();
            CgfkVO cgfk = null;
            YwfVO ywf = null;
            ZtbVO ztb = null;
            ZdfVO zdf = null;
            LxfVO lxf = null;
            WlkVO wlk = null;
            
            //fileName对于action配置文件中 <param name="contentDisposition">attachment;filename="${fileName}"</param>
            fileName=UnicodeUtil.unicodeToGbk(docNo + ".xls");
            //模版路径
            String srcPath = request.getSession().getServletContext().getRealPath("/")+ "gdsc"+File.separator+"resource" + File.separator + "exceltemplate" + File.separator;
            String srcUrl = null;
            if(primaryTable.indexOf("采购货款") > -1){
            	srcUrl = srcPath+"cghk.xls";
            	cgfk = (CgfkVO)cgfkService.queryCgfkByNoAndTableName(docNo, primaryTable);
            	params.put("cgfk", cgfk);
            }
            if(primaryTable.indexOf("招投标") > -1){
            	srcUrl = srcPath+"ztb.xls";
            	ztb = (ZtbVO)cgfkService.queryCgfkByNoAndTableName(docNo, primaryTable);
            	params.put("ztb", ztb);
            }
            if(primaryTable.indexOf("业务费") > -1){
            	srcUrl = srcPath+"ywf.xls";
            	ywf = (YwfVO)cgfkService.queryCgfkByNoAndTableName(docNo, primaryTable);
            	params.put("ywf", ywf);
            }
            if(primaryTable.indexOf("招待费") > -1){
            	srcUrl = srcPath+"zdf.xls";
            	zdf = (ZdfVO)cgfkService.queryCgfkByNoAndTableName(docNo, primaryTable);
            	params.put("zdf", zdf);
            }
            if(primaryTable.indexOf("零星费") > -1){
            	srcUrl = srcPath+"lxf.xls";
            	lxf = (LxfVO)cgfkService.queryCgfkByNoAndTableName(docNo, primaryTable);
            	params.put("lxf", lxf);
            }
            if(primaryTable.indexOf("往来款") > -1){
            	srcUrl = srcPath+"wlk.xls";
            	wlk = (WlkVO)cgfkService.queryCgfkByNoAndTableName(docNo, primaryTable);
            	params.put("wlk", wlk);
            }
            //保存文件之后的实际路径
            String targePath = request.getSession().getServletContext().getRealPath("/")+ "gdsc"+File.separator+"resource" + File.separator + "excelsavepath" + File.separator;
            String targetUrl = targePath + fileName;
            File file = null;
            transformer.transformXLS(srcUrl, params, targetUrl);
            FileInputStream fis = new FileInputStream(new File(targetUrl));
            byte[] b = new byte[1024];
            while (fis.read(b) != -1) {
            	output.write(b);
            }
            byte[] ba = output.toByteArray(); 
            //downloadFile 对应与action配置文件中 <param name="inputName">downloadFile</param>
            downloadFile = new ByteArrayInputStream(ba); 
            output.flush();
            fis.close();
            output.close();
            logger.info("excel导出成功");
            //导出之后，则将文件删除
            file = new File(targetUrl);
            if(file.exists()){
            	file.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParsePropertyException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/** 
	 * @return fileName 
	 */
	public String getFileName() {
		return fileName;
	}

	/** 
	 * @param fileName 要设置的 fileName 
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/** 
	 * @return downloadFile 
	 */
	public InputStream getDownloadFile() {
		return downloadFile;
	}

	/** 
	 * @param downloadFile 要设置的 downloadFile 
	 */
	public void setDownloadFile(InputStream downloadFile) {
		this.downloadFile = downloadFile;
	}
}
