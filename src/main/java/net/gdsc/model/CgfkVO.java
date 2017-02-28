/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.model;

/** 
 * @ClassName: CgfkVO 
 * @Description: 采购货款VO
 * @author: WUJING 
 * @date :2016-08-09 下午1:06:01 
 *  
 */
public class CgfkVO extends FundVO{
	
	private String surePayTime;//    ,[ITEM_确定支付时间]
	private String mature;//    ,[ITEM_是否到期]
	private String clienter;//    ,[ITEM_供应商名称]
	private String houseAndNo;//    ,[ITEM_入库情况]//,[ITEM_入库单号]
	private String bankNumber;//    ,[ITEM_银行帐号]
	private String bankName;//    ,[ITEM_开户银行]
	private String explain;//    ,[ITEM_用途说明]
	private String payType;//    ,[ITEM_支付方式]
	private String fax;//    ,[ITEM_税票情况]
	private String faxCondition;//    ,[ITEM_税票情况_条件]
	private String caiGou;//    ,[ITEM_采购部]
	private String arrival;//    ,[ITEM_到货情况]//    ,[ITEM_到货比例]
	private String warehouse;//    ,[ITEM_仓管]
	private String receivable;//    ,[ITEM_项目收款情况]
	private String check;//    ,[ITEM_货物安装验收情况]
	private String manager;//    ,[ITEM_项目经理]
	private String contract;//    ,[ITEM_采购合同金额]
	private String fareType;//    ,[ITEM_费用类型]
	private String operator;//    ,[ITEM_操作人]
	private String caiGouType;//    ,[ITEM_采购类型]
	private String no;//    ,[ITEM_编号]
	
	public CgfkVO() {
		super();
	}
	public CgfkVO(String surePayTime, String mature, String clienter,
			String houseAndNo, String bankNumber, String bankName,
			String explain, String payType, String fax, String faxCondition,
			String caiGou, String arrival, String warehouse, String receivable,
			String check, String manager, String contract, String fareType,
			String operator, String caiGouType, String no) {
		super();
		this.surePayTime = surePayTime;
		this.mature = mature;
		this.clienter = clienter;
		this.houseAndNo = houseAndNo;
		this.bankNumber = bankNumber;
		this.bankName = bankName;
		this.explain = explain;
		this.payType = payType;
		this.fax = fax;
		this.faxCondition = faxCondition;
		this.caiGou = caiGou;
		this.arrival = arrival;
		this.warehouse = warehouse;
		this.receivable = receivable;
		this.check = check;
		this.manager = manager;
		this.contract = contract;
		this.fareType = fareType;
		this.operator = operator;
		this.caiGouType = caiGouType;
		this.no = no;
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
	 * @return mature 
	 */
	public String getMature() {
		return mature;
	}
	/** 
	 * @param mature 要设置的 mature 
	 */
	public void setMature(String mature) {
		this.mature = mature;
	}
	/** 
	 * @return clienter 
	 */
	public String getClienter() {
		return clienter;
	}
	/** 
	 * @param clienter 要设置的 clienter 
	 */
	public void setClienter(String clienter) {
		this.clienter = clienter;
	}
	/** 
	 * @return houseAndNo 
	 */
	public String getHouseAndNo() {
		return houseAndNo;
	}
	/** 
	 * @param houseAndNo 要设置的 houseAndNo 
	 */
	public void setHouseAndNo(String houseAndNo) {
		this.houseAndNo = houseAndNo;
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
	 * @return payType 
	 */
	public String getPayType() {
		return payType;
	}
	/** 
	 * @param payType 要设置的 payType 
	 */
	public void setPayType(String payType) {
		this.payType = payType;
	}
	/** 
	 * @return fax 
	 */
	public String getFax() {
		return fax;
	}
	/** 
	 * @param fax 要设置的 fax 
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}
	/** 
	 * @return faxCondition 
	 */
	public String getFaxCondition() {
		return faxCondition;
	}
	/** 
	 * @param faxCondition 要设置的 faxCondition 
	 */
	public void setFaxCondition(String faxCondition) {
		this.faxCondition = faxCondition;
	}
	/** 
	 * @return caiGou 
	 */
	public String getCaiGou() {
		return caiGou;
	}
	/** 
	 * @param caiGou 要设置的 caiGou 
	 */
	public void setCaiGou(String caiGou) {
		this.caiGou = caiGou;
	}
	/** 
	 * @return arrival 
	 */
	public String getArrival() {
		return arrival;
	}
	/** 
	 * @param arrival 要设置的 arrival 
	 */
	public void setArrival(String arrival) {
		this.arrival = arrival;
	}
	/** 
	 * @return warehouse 
	 */
	public String getWarehouse() {
		return warehouse;
	}
	/** 
	 * @param warehouse 要设置的 warehouse 
	 */
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	/** 
	 * @return receivable 
	 */
	public String getReceivable() {
		return receivable;
	}
	/** 
	 * @param receivable 要设置的 receivable 
	 */
	public void setReceivable(String receivable) {
		this.receivable = receivable;
	}
	/** 
	 * @return check 
	 */
	public String getCheck() {
		return check;
	}
	/** 
	 * @param check 要设置的 check 
	 */
	public void setCheck(String check) {
		this.check = check;
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
	 * @return contract 
	 */
	public String getContract() {
		return contract;
	}
	/** 
	 * @param contract 要设置的 contract 
	 */
	public void setContract(String contract) {
		this.contract = contract;
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
	 * @return caiGouType 
	 */
	public String getCaiGouType() {
		return caiGouType;
	}
	/** 
	 * @param caiGouType 要设置的 caiGouType 
	 */
	public void setCaiGouType(String caiGouType) {
		this.caiGouType = caiGouType;
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
	* @Title: toString 
	* @param: @return 
	* @throws 
	*/
	@Override
	public String toString() {
		return "Cgfk [surePayTime=" + surePayTime + ", mature=" + mature
				+ ", clienter=" + clienter + ", houseAndNo=" + houseAndNo
				+ ", bankNumber=" + bankNumber + ", bankName=" + bankName
				+ ", explain=" + explain + ", payType=" + payType + ", fax="
				+ fax + ", faxCondition=" + faxCondition + ", caiGou=" + caiGou
				+ ", arrival=" + arrival + ", warehouse=" + warehouse
				+ ", receivable=" + receivable + ", check=" + check
				+ ", manager=" + manager + ", contract=" + contract
				+ ", fareType=" + fareType + ", operator=" + operator
				+ ", caiGouType=" + caiGouType + ", no=" + no + "]";
	}
}