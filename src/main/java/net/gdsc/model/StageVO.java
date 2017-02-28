/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.model;

/** 
 * @ClassName: StageVO 
 * @Description: 项目阶段明细 
 * @author: WUJING 
 * @date :2016-07-16 下午1:59:02 
 *  
 */
public class StageVO extends ObjectVO{
//  ,[ITEM_项目名称_]
//  ,[ITEM_项目编号_]
//  ,[ITEM_项目阶段_]
//  ,[ITEM_客户名称_]
//  ,[ITEM_开始时间_]
//  ,[ITEM_结束时间_]
//	,[ITEM_完成状况]
//  ,[ITEM_备注_]
//  ,[ITEM_排序]
	private String id;
	private String proName;
	private String proNo;
	private String proStage;
	private String clientName;
	private String startTime;
	private String endTime;
	private String parentId;
	private String status;
	private String remark;
	private int sort;
	private String flag;
	
	public StageVO() {
		super();
		// TODO 自动生成的构造函数存根
	}
	
	public StageVO(String id, String proName, String proNo, String proStage,
			String clientName, String startTime, String endTime,
			String parentId,String status, String remark,int sort,String flag) {
		super();
		this.id = id;
		this.proName = proName;
		this.proNo = proNo;
		this.proStage = proStage;
		this.clientName = clientName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.parentId = parentId;
		this.status = status;
		this.remark = remark;
		this.sort = sort;
		this.flag = flag;
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
	 * @return proStage 
	 */
	public String getProStage() {
		return proStage;
	}
	/** 
	 * @param proStage 要设置的 proStage 
	 */
	public void setProStage(String proStage) {
		this.proStage = proStage;
	}
	/** 
	 * @return clientName 
	 */
	public String getClientName() {
		return clientName;
	}
	/** 
	 * @param clientName 要设置的 clientName 
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	/** 
	 * @return startTime 
	 */
	public String getStartTime() {
		return startTime;
	}
	/** 
	 * @param startTime 要设置的 startTime 
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/** 
	 * @return endTime 
	 */
	public String getEndTime() {
		return endTime;
	}
	/** 
	 * @param endTime 要设置的 endTime 
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/** 
	 * @return id 
	 */
	public String getId() {
		return id;
	}
	/** 
	 * @param id 要设置的 id 
	 */
	public void setId(String id) {
		this.id = id;
	}
	/** 
	 * @return parentId 
	 */
	public String getParentId() {
		return parentId;
	}
	/** 
	 * @param parentId 要设置的 parentId 
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	/** 
	 * @return remark 
	 */
	public String getRemark() {
		return remark;
	}
	/** 
	 * @param remark 要设置的 remark 
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** 
	 * @return status 
	 */
	public String getStatus() {
		return status;
	}

	/** 
	 * @param status 要设置的 status 
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/** 
	 * @return sort 
	 */
	public int getSort() {
		return sort;
	}

	/** 
	 * @param sort 要设置的 sort 
	 */
	public void setSort(int sort) {
		this.sort = sort;
	}

	/** 
	 * @return flag 
	 */
	public String getFlag() {
		return flag;
	}

	/** 
	 * @param flag 要设置的 flag 
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}

	/** 
	* @Title: toString 
	* @Description: TODO
	* @param: @return 
	* @throws 
	*/
	@Override
	public String toString() {
		return "StageVO [id=" + id + ", proName=" + proName + ", proNo="
				+ proNo + ", proStage=" + proStage + ", clientName="
				+ clientName + ", startTime=" + startTime + ", endTime="
				+ endTime + ", parentId=" + parentId + ", status=" + status
				+ ", remark=" + remark + ", sort=" + sort+ ", flag=" + flag+"]";
	}
	
}
