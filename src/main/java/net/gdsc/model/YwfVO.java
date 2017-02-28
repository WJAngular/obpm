/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.model;

/** 
 * @ClassName: YwfVO 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-08-10 上午9:34:36 
 *  
 */
public class YwfVO extends FundVO{
	private String profit;//    ,[ITEM_利润]
	private String budget;//    ,[ITEM_是否有预算]
	private String explain;//    ,[ITEM_情况说明]
	private String operator;//    ,[ITEM_操作人]
	private String budgetSum;//    ,[ITEM_业务费总预算]
	private String agreement;//    ,[ITEM_合同价]
	private String fareType;//    ,[ITEM_费用类型]
	private String object;//    ,[ITEM_对象]
	private String payType;//    ,[ITEM_付款方式]
	private String no;//    ,[ITEM_编号]
	
	public YwfVO() {
		super();
		// TODO 自动生成的构造函数存根
	}
	
	public YwfVO(String profit, String budget, String explain, String operator,
			String budgetSum, String agreement, String fareType, String object,
			String payType, String no) {
		super();
		this.profit = profit;
		this.budget = budget;
		this.explain = explain;
		this.operator = operator;
		this.budgetSum = budgetSum;
		this.agreement = agreement;
		this.fareType = fareType;
		this.object = object;
		this.payType = payType;
		this.no = no;
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
	 * @return budgetSum 
	 */
	public String getBudgetSum() {
		return budgetSum;
	}
	/** 
	 * @param budgetSum 要设置的 budgetSum 
	 */
	public void setBudgetSum(String budgetSum) {
		this.budgetSum = budgetSum;
	}
	/** 
	 * @return agreement 
	 */
	public String getAgreement() {
		return agreement;
	}
	/** 
	 * @param agreement 要设置的 agreement 
	 */
	public void setAgreement(String agreement) {
		this.agreement = agreement;
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
	* @Description: TODO
	* @param: @return 
	* @throws 
	*/
	@Override
	public String toString() {
		return "YwfVO [profit=" + profit + ", budget=" + budget + ", explain="
				+ explain + ", operator=" + operator + ", budgetSum="
				+ budgetSum + ", agreement=" + agreement + ", fareType="
				+ fareType + ", object=" + object + ", payType=" + payType
				+ ", no=" + no + "]";
	}
	
}
