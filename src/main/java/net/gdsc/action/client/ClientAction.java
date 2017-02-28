/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.action.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.gdsc.model.ClientVO;
import net.gdsc.service.client.ClientService;
import net.gdsc.service.client.IClientService;
import net.gdsc.util.DateUtil;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName: ClientAction 
 * @Description: CRM管理
 * @author: WUJING 
 * @date :2016-07-08 下午3:29:10 
 *  
 */
public class ClientAction extends ActionSupport{

	/** 
	* @Fields serialVersionUID : TODO
	*/
	private static final long serialVersionUID = 8861375165257176429L;
	
	private static final Logger logger = Logger.getLogger(ClientAction.class);
	
	private IClientService clientService = ClientService.getInstance();
	
	private ClientVO vo ;
	
	private String dataFlag;
	//重置客户编号
	private String resetNo;
	//是否存在  0表示存在、1表示不存在
	private String isExist ;
	
	private String result = "";
	
	private String isSuccess ; // 0表示新增成功、1表示新增失败
	/**
	 * 
	* @Title: doJumpClient 
	* @Description:跳转到新增用户界面
	* @param: @return 
	* @return: String
	* @throws
	 */
	public String doJumpClient(){
		setDataFlagBySql();
		return "jump";
	}
	/** 
	* @Title: setDataFlagBySql 
	* @Description: TODO
	* @param:  
	* @return: void
	* @throws 
	*/
	public void setDataFlagBySql() {
		String year = DateUtil.formatDate(new Date(),"yyyyMMdd");
		String flag = "CN"+year.substring(2);
		ClientVO client = null;
		try {
			client = clientService.findByDataFlag();
			if(client == null){
				dataFlag = flag + "00";
			}else{
				dataFlag = client.getClientNo();
			}
		} catch (Exception e) {
			logger.error("查找客户编号失败!"+e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * 
	* @Title: doAddClient 
	* @Description: 立项管理中新增客户基本信息
	* @param: @return 
	* @return: String
	* @throws
	 */
	public String doAddClient(){
		String clientNo = vo.getClientNo();
		ClientVO client = null;
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//判断客户编号是否存在？
			client = clientService.findByClientNo(clientNo);
			if(client != null){
				setDataFlagBySql();
				map.put("isSuccess","1");
				map.put("isExist","0");
				map.put("dataFlag", getDataFlag());
				JSONObject json = JSONObject.fromObject(map);//将map对象转换成json类型数据
				result = json.toString();
				return SUCCESS;
			}
			clientService.create(vo);
			map.put("isSuccess","0");
			map.put("isExist","1");
			map.put("dataFlag", "");
			JSONObject json = JSONObject.fromObject(map);//将map对象转换成json类型数据
			result = json.toString();
		} catch (Exception e) {
			logger.error("新增客户信息失败!"+e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}
	/**
	 * 
	* @Title: doClientNoValue 
	* @Description: 客户管理中设置编号的值
	* @param: @return 
	* @return: String
	* @throws
	 */
	public String doClientNoValue(){
		setDataFlagBySql();
		return SUCCESS;
	}
	
	public String doCheckNo(){
		ClientVO client = null;
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//判断客户编号是否存在？
			client = clientService.findByClientNo(resetNo);
			if(client != null){
				setDataFlagBySql();
				map.put("isExist","0");
				map.put("dataFlag", getDataFlag());
			}else{
				map.put("isExist","1");
				map.put("dataFlag", "");
			}
			JSONObject json = JSONObject.fromObject(map);//将map对象转换成json类型数据
			result = json.toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}
	
	/** 
	 * @return vo 
	 */
	public ClientVO getVo() {
		return vo;
	}
	/** 
	 * @param vo 要设置的 vo 
	 */
	public void setVo(ClientVO vo) {
		this.vo = vo;
	}
	/** 
	 * @return isSuccess 
	 */
	public String getIsSuccess() {
		return isSuccess;
	}
	/** 
	 * @param isSuccess 要设置的 isSuccess 
	 */
	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}
	/** 
	 * @return dataFlag 
	 */
	public String getDataFlag() {
		return dataFlag;
	}
	/** 
	 * @param dataFlag 要设置的 dataFlag 
	 */
	public void setDataFlag(String dataFlag) {
		this.dataFlag = dataFlag;
	}
	/** 
	 * @return resetNo 
	 */
	public String getResetNo() {
		return resetNo;
	}
	/** 
	 * @param resetNo 要设置的 resetNo 
	 */
	public void setResetNo(String resetNo) {
		this.resetNo = resetNo;
	}
	/** 
	 * @return isExist 
	 */
	public String getIsExist() {
		return isExist;
	}
	/** 
	 * @param isExist 要设置的 isExist 
	 */
	public void setIsExist(String isExist) {
		this.isExist = isExist;
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
