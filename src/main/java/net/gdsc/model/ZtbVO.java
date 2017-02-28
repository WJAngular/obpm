/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.model;

/** 
 * @ClassName: ZtbVO 
 * @Description: 招投标费用申请
 * @author: WUJING 
 * @date :2016-08-10 下午2:46:12 
 *  
 */
public class ZtbVO extends FundVO{
	private String surePayTime;//    ,[ITEM_确定支付时间]
	private String accountName;//    ,[ITEM_开户名称]
	private String bankName;//    ,[ITEM_开户银行]
	private String bankNumber;//    ,[ITEM_银行帐号]
	private String remark;//    ,[ITEM_备注]
	private String operator;//    ,[ITEM_操作人]
	private String fareType;//    ,[ITEM_费用类型]
	private String manager;//    ,[ITEM_项目经理]
	private String payer;//    ,[ITEM_付款人名称]
	private String other;//    ,[ITEM_其他_]
	private String no;//    ,[ITEM_编号]
	private String explain;//    ,[ITEM_资金用途]
	
	public ZtbVO() {
		super();
		// TODO 自动生成的构造函数存根
	}
	public ZtbVO(String surePayTime, String accountName, String bankName,
			String bankNumber, String remark, String operator, String fareType,
			String manager, String payer, String other, String no,
			String explain) {
		super();
		this.surePayTime = surePayTime;
		this.accountName = accountName;
		this.bankName = bankName;
		this.bankNumber = bankNumber;
		this.remark = remark;
		this.operator = operator;
		this.fareType = fareType;
		this.manager = manager;
		this.payer = payer;
		this.other = other;
		this.no = no;
		this.explain = explain;
	}
	/** 
	 * @return surePayTime 
	 */
	public String getSurePayTime() {
		return surePayTime;
	}
	/** 
	 * @param surePayTime 要设置的 surePayTime 
	 */
	public void setSurePayTime(String surePayTime) {
		this.surePayTime = surePayTime;
	}
	/** 
	 * @return accountName 
	 */
	public String getAccountName() {
		return accountName;
	}
	/** 
	 * @param accountName 要设置的 accountName 
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	/** 
	 * @return bankName 
	 */
	public String getBankName() {
		return bankName;
	}
	/** 
	 * @param bankName 要设置的 bankName 
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	/** 
	 * @return bankNumber 
	 */
	public String getBankNumber() {
		return bankNumber;
	}
	/** 
	 * @param bankNumber 要设置的 bankNumber 
	 */
	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
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
	 * @return operator 
	 */
	public String getOperator() {
		return operator;
	}
	/** 
	 * @param operator 要设置的 operator 
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
	/** 
	 * @return fareType 
	 */
	public String getFareType() {
		return fareType;
	}
	/** 
	 * @param fareType 要设置的 fareType 
	 */
	public void setFareType(String fareType) {
		this.fareType = fareType;
	}
	/** 
	 * @return manager 
	 */
	public String getManager() {
		return manager;
	}
	/** 
	 * @param manager 要设置的 manager 
	 */
	public void setManager(String manager) {
		this.manager = manager;
	}
	/** 
	 * @return payer 
	 */
	public String getPayer() {
		return payer;
	}
	/** 
	 * @param payer 要设置的 payer 
	 */
	public void setPayer(String payer) {
		this.payer = payer;
	}
	/** 
	 * @return other 
	 */
	public String getOther() {
		return other;
	}
	/** 
	 * @param other 要设置的 other 
	 */
	public void setOther(String other) {
		this.other = other;
	}
	/** 
	 * @return no 
	 */
	public String getNo() {
		return no;
	}
	/** 
	 * @param no 要设置的 no 
	 */
	public void setNo(String no) {
		this.no = no;
	}
	/** 
	 * @return explain 
	 */
	public String getExplain() {
		return explain;
	}
	/** 
	 * @param explain 要设置的 explain 
	 */
	public void setExplain(String explain) {
		this.explain = explain;
	}
	/** 
	* @Title: toString 
	* @Description: TODO
	* @param: @return 
	* @throws 
	*/
	@Override
	public String toString() {
		return "ZtbVO [surePayTime=" + surePayTime + ", accountName="
				+ accountName + ", bankName=" + bankName + ", bankNumber="
				+ bankNumber + ", remark=" + remark + ", operator=" + operator
				+ ", fareType=" + fareType + ", manager=" + manager
				+ ", payer=" + payer + ", other=" + other + ", no=" + no
				+ ", explain=" + explain + "]";
	}
	
}
