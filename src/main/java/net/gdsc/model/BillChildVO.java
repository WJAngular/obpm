/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.model;

/** 
 * @ClassName: BillChildVO 
 * @Description: 项目结算模型子模型
 * @author: WUJING 
 * @date :2016-07-26 上午11:34:48 
 *  
 */
public class BillChildVO {
	//子表字段
//  ,[ITEM_金额]
//  ,[ITEM_日期]
//  ,[ITEM_备注]
//  ,[ITEM_事项编号]
//  ,[ITEM_费用编号]
//	,[ITEM_总金额]
//	,[ITEM_凭证号]
//	,[ITEM_报账人]
//  ,[ITEM_事项名称]
	private String childMoney;
	private String childDate;
	private String childRemark;
	private String childMatter;
	private String childNo;
	private String childFeeNo;
	private String childSumMoney;
	private String childDocumentNo;
	private String childBaozheng;
	private String childName;
	
	public BillChildVO() {
		super();
		// TODO 自动生成的构造函数存根
	}
	
	public BillChildVO(String childMoney, String childDate, String childRemark,
			String childMatter, String childNo, String childFeeNo,
			String childSumMoney, String childDocumentNo, String childBaozheng,
			String childName) {
		super();
		this.childMoney = childMoney;
		this.childDate = childDate;
		this.childRemark = childRemark;
		this.childMatter = childMatter;
		this.childNo = childNo;
		this.childFeeNo = childFeeNo;
		this.childSumMoney = childSumMoney;
		this.childDocumentNo = childDocumentNo;
		this.childBaozheng = childBaozheng;
		this.childName = childName;
	}

	/** 
	 * @return childMoney 
	 */
	public String getChildMoney() {
		return childMoney;
	}
	/** 
	 * @param childMoney 要设置的 childMoney 
	 */
	public void setChildMoney(String childMoney) {
		this.childMoney = childMoney;
	}
	/** 
	 * @return childDate 
	 */
	public String getChildDate() {
		return childDate;
	}
	/** 
	 * @param childDate 要设置的 childDate 
	 */
	public void setChildDate(String childDate) {
		this.childDate = childDate;
	}
	/** 
	 * @return childRemark 
	 */
	public String getChildRemark() {
		return childRemark;
	}
	/** 
	 * @param childRemark 要设置的 childRemark 
	 */
	public void setChildRemark(String childRemark) {
		this.childRemark = childRemark;
	}
	/** 
	 * @return childMatter 
	 */
	public String getChildMatter() {
		return childMatter;
	}
	/** 
	 * @param childMatter 要设置的 childMatter 
	 */
	public void setChildMatter(String childMatter) {
		this.childMatter = childMatter;
	}
	/** 
	 * @return childNo 
	 */
	public String getChildNo() {
		return childNo;
	}
	/** 
	 * @param childNo 要设置的 childNo 
	 */
	public void setChildNo(String childNo) {
		this.childNo = childNo;
	}
	/** 
	 * @return childBaozheng 
	 */
	public String getChildBaozheng() {
		return childBaozheng;
	}
	/** 
	 * @param childBaozheng 要设置的 childBaozheng 
	 */
	public void setChildBaozheng(String childBaozheng) {
		this.childBaozheng = childBaozheng;
	}
	/** 
	 * @return childFeeNo 
	 */
	public String getChildFeeNo() {
		return childFeeNo;
	}
	/** 
	 * @param childFeeNo 要设置的 childFeeNo 
	 */
	public void setChildFeeNo(String childFeeNo) {
		this.childFeeNo = childFeeNo;
	}
	/** 
	 * @return childSumMoney 
	 */
	public String getChildSumMoney() {
		return childSumMoney;
	}
	/** 
	 * @param childSumMoney 要设置的 childSumMoney 
	 */
	public void setChildSumMoney(String childSumMoney) {
		this.childSumMoney = childSumMoney;
	}
	/** 
	 * @return childDocumentNo 
	 */
	public String getChildDocumentNo() {
		return childDocumentNo;
	}
	/** 
	 * @param childDocumentNo 要设置的 childDocumentNo 
	 */
	public void setChildDocumentNo(String childDocumentNo) {
		this.childDocumentNo = childDocumentNo;
	}
	/** 
	 * @return childName 
	 */
	public String getChildName() {
		return childName;
	}
	/** 
	 * @param childName 要设置的 childName 
	 */
	public void setChildName(String childName) {
		this.childName = childName;
	}

	/** 
	* @Title: toString 
	* @Description: TODO
	* @param: @return 
	* @throws 
	*/
	@Override
	public String toString() {
		return "BillChildVO [childMoney=" + childMoney + ", childDate="
				+ childDate + ", childRemark=" + childRemark + ", childMatter="
				+ childMatter + ", childNo=" + childNo + ", childFeeNo="
				+ childFeeNo + ", childSumMoney=" + childSumMoney
				+ ", childDocumentNo=" + childDocumentNo + ", childBaozheng="
				+ childBaozheng + ", childName=" + childName + "]";
	}
	
}
