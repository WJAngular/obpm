/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.action.prostage;

import java.util.HashMap;
import java.util.Map;

import net.gdsc.model.StageVO;
import net.gdsc.service.prostage.IProStageService;
import net.gdsc.service.prostage.ProStageService;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName: ProStage 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-07-16 下午8:49:04 
 *  
 */
public class ProStageAction extends ActionSupport{

	/** 
	* @Fields serialVersionUID : TODO
	*/
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(ProStageAction.class);
	
	private IProStageService proStageService = ProStageService.getInstance();
	
	private String result;
	
	public StageVO stage;
	/**
	 * 
	* @Title: doProstageJsp 
	* @Description: 项目阶段页面跳转
	* @param: @return 
	* @return: String
	* @throws
	 */
	public String doProstageJsp(){
		
		return SUCCESS;
	}
	/**
	 * 
	* @Title: doProstage 
	* @Description: 项目阶段明细新增
	* @param: @return 
	* @return: String
	* @throws
	 */
	public String doProstage(){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			if("hasValue".equals(stage.getFlag())){
				proStageService.delete(stage.getParentId());
			}
			proStageService.insertBatch(stage);
			map.put("insertBatch", "0");
			JSONObject json = JSONObject.fromObject(map);
			result = json.toString();
		} catch (Exception e) {
			logger.error("批量新增项目进程阶段明细出错!"+e.getMessage());
			e.printStackTrace();
			map.put("insertBatch", "1");
			JSONObject json = JSONObject.fromObject(map);
			result = json.toString();
			return ERROR;
		}
		return SUCCESS;
	}
	/** 
	 * @return stage 
	 */
	public StageVO getStage() {
		return stage;
	}
	/** 
	 * @param stage 要设置的 stage 
	 */
	public void setStage(StageVO stage) {
		this.stage = stage;
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
