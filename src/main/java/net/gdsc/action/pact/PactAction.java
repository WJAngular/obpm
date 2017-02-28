/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.action.pact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.gdsc.model.PactVO;
import net.gdsc.service.pact.IPactService;
import net.gdsc.service.pact.PactService;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName: PactAction 
 * @Description: 项目合同结算
 * @author: WUJING 
 * @date :2016-07-28 下午1:00:54 
 *  
 */
public class PactAction extends ActionSupport{

	/** 
	* @Fields serialVersionUID : TODO
	*/
	private static final long serialVersionUID = -9097464128423037119L;
	private static final Logger logger = Logger.getLogger(PactAction.class);
	private IPactService pactService = PactService.getInstance();
	private String proName ;
	private String proNo;
	private String primaryTable;
	private String result;
	private String isExist;  //0表示存在，1表示不存在
	public String doPact(){
		List<PactVO> pactList = new ArrayList<PactVO>();
		Map<String,String> map = new HashMap<String,String>();
		
		Float oldInCome = 0.00f;//以往收入总金额
		Float nowSum = 0.00f; //现合同金额
		try {
			pactList = pactService.queryHistory(proName, proNo, primaryTable);
			if(pactList == null || pactList.size() <=0 ){
				map.put("prInComeHis", "无以往历史数据!");
				map.put("isExist", "1");
			}else{
				for(int i = 0;i<pactList.size();i++){
					nowSum = Float.parseFloat(pactList.get(0).getPrFee());
					oldInCome += Float.parseFloat(pactList.get(i).getPrInCome());
				}
				map.put("oldInCome", String.valueOf(oldInCome));
				map.put("nowSum", String.valueOf(nowSum));
				map.put("isExist", "0");
			}
			JSONObject json = JSONObject.fromObject(map);
			result = json.toString();
		} catch (Exception e) {
			logger.error("查找"+primaryTable.split("_")[1]+"相关信息失败!"+e.getMessage());
			e.printStackTrace();
		}
		return SUCCESS;
	}
	/** 
	 * @return proName 
	 */
	public String getProName() {
		return proName;
	}
	/** 
	 * @param proName 要设置的 proName 
	 */
	public void setProName(String proName) {
		this.proName = proName;
	}
	/** 
	 * @return proNo 
	 */
	public String getProNo() {
		return proNo;
	}
	/** 
	 * @param proNo 要设置的 proNo 
	 */
	public void setProNo(String proNo) {
		this.proNo = proNo;
	}
	/** 
	 * @return primaryTable 
	 */
	public String getPrimaryTable() {
		return primaryTable;
	}
	/** 
	 * @param primaryTable 要设置的 primaryTable 
	 */
	public void setPrimaryTable(String primaryTable) {
		this.primaryTable = primaryTable;
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
}
