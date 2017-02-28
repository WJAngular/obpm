/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.model;

/** 
 * @ClassName: FundVO 
 * @Description: 临时资金父VO
 * @author: WUJING 
 * @date :2016-08-09 下午12:59:17 
 *  
 */
public class FundVO extends ObjectVO{
	private String departName;  //部门
	private String applyDate;	//申请日期
	private String proName;     //项目名称
	private String proNo;//项目编号
	private String applyBig;    //申请金额大写
	private String applySmall;	//申请金额小写
	private String approveBig;  //批准金额大写
	private String approveSmall; //批准金额小写
	private String managerAdvice ;//总经理意见
	private String clerk;//业务员
	private String balance;//余额
	private String applyFund;//本次申请金额
	private String spendFund;//已支出金额
	
	public FundVO() {
		super();
	}
	
	public FundVO(String departName, String applyDate, String proName,
			String proNo, String applyBig, String applySmall,
			String approveBig, String approveSmall, String managerAdvice,
			String clerk, String balance, String applyFund, String spendFund) {
		super();
		this.departName = departName;
		this.applyDate = applyDate;
		this.proName = proName;
		this.proNo = proNo;
		this.applyBig = applyBig;
		this.applySmall = applySmall;
		this.approveBig = approveBig;
		this.approveSmall = approveSmall;
		this.managerAdvice = managerAdvice;
		this.clerk = clerk;
		this.balance = balance;
		this.applyFund = applyFund;
		this.spendFund = spendFund;
	}



	/** 
	 * @return departName 
	 */
	public String getDepartName() {
		return departName;
	}
	/** 
	 * @param departName 要设置的 departName 
	 */
	public void setDepartName(String departName) {
		this.departName = departName;
	}
	
	/** 
	 * @return applyDate 
	 */
	public String getApplyDate() {
		return applyDate;
	}
	/** 
	 * @param applyDate 要设置的 applyDate 
	 */
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
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
	 * @return applyBig 
	 */
	public String getApplyBig() {
		return applyBig;
	}
	/** 
	 * @param applyBig 要设置的 applyBig 
	 */
	public void setApplyBig(String applyBig) {
		this.applyBig = applyBig;
	}
	/** 
	 * @return applySmall 
	 */
	public String getApplySmall() {
		return applySmall;
	}
	/** 
	 * @param applySmall 要设置的 applySmall 
	 */
	public void setApplySmall(String applySmall) {
		this.applySmall = applySmall;
	}
	/** 
	 * @return approveBig 
	 */
	public String getApproveBig() {
		return approveBig;
	}
	/** 
	 * @param approveBig 要设置的 approveBig 
	 */
	public void setApproveBig(String approveBig) {
		this.approveBig = approveBig;
	}
	/** 
	 * @return approveSmall 
	 */
	public String getApproveSmall() {
		return approveSmall;
	}
	/** 
	 * @param approveSmall 要设置的 approveSmall 
	 */
	public void setApproveSmall(String approveSmall) {
		this.approveSmall = approveSmall;
	}
	/** 
	 * @return managerAdvice 
	 */
	public String getManagerAdvice() {
		return managerAdvice;
	}
	/** 
	 * @param managerAdvice 要设置的 managerAdvice 
	 */
	public void setManagerAdvice(String managerAdvice) {
		this.managerAdvice = managerAdvice;
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
	 * @return clerk 
	 */
	public String getClerk() {
		return clerk;
	}



	/** 
	 * @param clerk 要设置的 clerk 
	 */
	public void setClerk(String clerk) {
		this.clerk = clerk;
	}



	/** 
	 * @return balance 
	 */
	public String getBalance() {
		return balance;
	}



	/** 
	 * @param balance 要设置的 balance 
	 */
	public void setBalance(String balance) {
		this.balance = balance;
	}



	/** 
	 * @return applyFund 
	 */
	public String getApplyFund() {
		return applyFund;
	}



	/** 
	 * @param applyFund 要设置的 applyFund 
	 */
	public void setApplyFund(String applyFund) {
		this.applyFund = applyFund;
	}



	/** 
	 * @return spendFund 
	 */
	public String getSpendFund() {
		return spendFund;
	}



	/** 
	 * @param spendFund 要设置的 spendFund 
	 */
	public void setSpendFund(String spendFund) {
		this.spendFund = spendFund;
	}

	/** 
	* @Title: toString 
	* @Description: TODO
	* @param: @return 
	* @throws 
	*/
	@Override
	public String toString() {
		return "FundVO [departName=" + departName + ", applyDate=" + applyDate
				+ ", proName=" + proName + ", proNo=" + proNo + ", applyBig="
				+ applyBig + ", applySmall=" + applySmall + ", approveBig="
				+ approveBig + ", approveSmall=" + approveSmall
				+ ", managerAdvice=" + managerAdvice + ", clerk=" + clerk
				+ ", balance=" + balance + ", applyFund=" + applyFund
				+ ", spendFund=" + spendFund + "]";
	}

}
