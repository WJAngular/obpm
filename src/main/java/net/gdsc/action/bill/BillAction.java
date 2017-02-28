/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.action.bill;

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

import net.gdsc.model.BillVO;
import net.gdsc.service.bill.BillService;
import net.gdsc.service.bill.IBillService;
import net.gdsc.util.UnicodeUtil;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName: BillAction 
 * @Description: 项目结算excel导出功能
 * @author: WUJING 
 * @date :2016-07-26 上午9:28:36 
 *  
 */
public class BillAction extends ActionSupport{
	
	/** 
	* @Fields serialVersionUID : 序列号
	*/
	private static final long serialVersionUID = -3922588008190840034L;
	private static final Logger logger = Logger.getLogger(BillAction.class);
	
	private IBillService billService = BillService.getInstance();
	//文件名称
	private String fileName;
	
	private InputStream downloadFile;
	
	public String doBill(){
		try {
			HttpServletRequest request= ServletActionContext.getRequest();
			HttpServletResponse response= ServletActionContext.getResponse();
			String primaryTable = URLDecoder.decode(request.getParameter("primaryTable"), "utf-8");
			String proNo = URLDecoder.decode(request.getParameter("proNo"), "utf-8");
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/download;charset=utf-8");
            ByteArrayOutputStream output = new ByteArrayOutputStream();    
            XLSTransformer transformer = new XLSTransformer();
            Map<String, Object> params = new HashMap<String, Object>();
            BillVO bill = null;
            //fileName对于action配置文件中 <param name="contentDisposition">attachment;filename="${fileName}"</param>
            fileName=UnicodeUtil.unicodeToGbk(proNo + ".xls");
            bill = billService.queryBillVO(proNo,primaryTable);
            params.put("bill", bill);
            //模版路径
            String srcPath = request.getSession().getServletContext().getRealPath("/")+ "gdsc"+File.separator+"resource" + File.separator + "exceltemplate" + File.separator;
            String srcUrl = srcPath+"xmjs.xls";
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
