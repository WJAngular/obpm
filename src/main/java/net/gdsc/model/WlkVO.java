/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.model;

/** 
 * @ClassName: WlkVO 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-08-10 下午4:03:45 
 *  
 */
public class WlkVO extends FundVO{
	private String surePayTime;//    ,[ITEM_确定支付时间]
	private String fundUser;//    ,[ITEM_资金用途]
	private String explain;//    ,[ITEM_用途说明]
	private String operator;//    ,[ITEM_操作人]
	private String fareType;//    ,[ITEM_费用类型]
	private String no;//    ,[ITEM_编号]
	private String contract;  //    ,[ITEM_合同价]
	private String profit;  //,[ITEM_利润]
	private String budget; //,[ITEM_是否有预算]
	private String payType;//,[ITEM_付款方式]
	private String sum;//,[ITEM_往来款总金额]
	private String object ;//[ITEM_对象]
	
	public WlkVO() {
		super();
		// TODO 自动生成的构造函数存根
	}
	
	public WlkVO(String surePayTime, String fundUser, String explain,
			String operator, String fareType, String no, String contract,
			String profit, String budget, String payType, String sum,String object) {
		super();
		this.surePayTime = surePayTime;
		this.fundUser = fundUser;
		this.explain = explain;
		this.operator = operator;
		this.fareType = fareType;
		this.no = no;
		this.contract = contract;
		this.profit = profit;
		this.budget = budget;
		this.payType = payType;
		this.sum = sum;
		this.object = object;
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
	 * @return fundUser 
	 */
	public String getFundUser() {
		return fundUser;
	}
	/** 
	 * @param fundUser 要设置的 fundUser 
	 */
	public void setFundUser(String fundUser) {
		this.fundUser = fundUser;
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
	 * @return profit 
	 */
	public String getProfit() {
		return profit;
	}

	/** 
	 * @param profit 要设置的 profit 
	 */
	public void setProfit(String profit) {
		this.profit = profit;
	}

	/** 
	 * @return budget 
	 */
	public String getBudget() {
		return budget;
	}

	/** 
	 * @param budget 要设置的 budget 
	 */
	public void setBudget(String budget) {
		this.budget = budget;
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
	 * @return sum 
	 */
	public String getSum() {
		return sum;
	}

	/** 
	 * @param sum 要设置的 sum 
	 */
	public void setSum(String sum) {
		this.sum = sum;
	}

	/** 
	 * @return object 
	 */
	public String getObject() {
		return object;
	}

	/** 
	 * @param object 要设置的 object 
	 */
	public void setObject(String object) {
		this.object = object;
	}

	/** 
	* @Title: toString 
	* @Description: TODO
	* @param: @return 
	* @throws 
	*/
	@Override
	public String toString() {
		return "WlkVO [surePayTime=" + surePayTime + ", fundUser=" + fundUser
				+ ", explain=" + explain + ", operator=" + operator
				+ ", fareType=" + fareType + ", no=" + no + ", contract="
				+ contract + ", profit=" + profit + ", budget=" + budget
				+ ", payType=" + payType + ", sum=" + sum +", object=" + object + "]";
	}

}
