/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.model;

/** 
 * @ClassName: ZdfVO 
 * @Description: 招待费申请
 * @author: WUJING 
 * @date :2016-08-10 下午3:17:54 
 *  
 */
public class ZdfVO extends FundVO{
	private String object;//    ,[ITEM_招待对象_招待]
	private String post;//    ,[ITEM_职务_招待]
	private String number;//    ,[ITEM_招待人数_招待]
	private String way;//    ,[ITEM_招待方式_招待]
	private String address;//    ,[ITEM_招待地点_招待]
	private String budgetSum;//    ,[ITEM_业务招待费总预算_招待]
	private String purpose;//    ,[ITEM_招待目的_招待]
	private String operator;//    ,[ITEM_操作人]
	private String fareType;//    ,[ITEM_费用类型]
	private String no;//    ,[ITEM_编号]
	private String baoXiaoNo;//    ,[ITEM_系统报销单编号]
	
	public ZdfVO() {
		super();
		// TODO 自动生成的构造函数存根
	}
	
	public ZdfVO(String object, String post, String number, String way,
			String address, String budgetSum, String purpose, String operator,
			String fareType, String no, String baoXiaoNo) {
		super();
		this.object = object;
		this.post = post;
		this.number = number;
		this.way = way;
		this.address = address;
		this.budgetSum = budgetSum;
		this.purpose = purpose;
		this.operator = operator;
		this.fareType = fareType;
		this.no = no;
		this.baoXiaoNo = baoXiaoNo;
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
	 * @return post 
	 */
	public String getPost() {
		return post;
	}
	/** 
	 * @param post 要设置的 post 
	 */
	public void setPost(String post) {
		this.post = post;
	}
	/** 
	 * @return number 
	 */
	public String getNumber() {
		return number;
	}
	/** 
	 * @param number 要设置的 number 
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	/** 
	 * @return way 
	 */
	public String getWay() {
		return way;
	}
	/** 
	 * @param way 要设置的 way 
	 */
	public void setWay(String way) {
		this.way = way;
	}
	/** 
	 * @return address 
	 */
	public String getAddress() {
		return address;
	}
	/** 
	 * @param address 要设置的 address 
	 */
	public void setAddress(String address) {
		this.address = address;
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
	 * @return purpose 
	 */
	public String getPurpose() {
		return purpose;
	}
	/** 
	 * @param purpose 要设置的 purpose 
	 */
	public void setPurpose(String purpose) {
		this.purpose = purpose;
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
	 * @return baoXiaoNo 
	 */
	public String getBaoXiaoNo() {
		return baoXiaoNo;
	}
	/** 
	 * @param baoXiaoNo 要设置的 baoXiaoNo 
	 */
	public void setBaoXiaoNo(String baoXiaoNo) {
		this.baoXiaoNo = baoXiaoNo;
	}
	/** 
	* @Title: toString 
	* @Description: TODO
	* @param: @return 
	* @throws 
	*/
	@Override
	public String toString() {
		return "ZdfVO [object=" + object + ", post=" + post + ", number="
				+ number + ", way=" + way + ", address=" + address
				+ ", budgetSum=" + budgetSum + ", purpose=" + purpose
				+ ", operator=" + operator + ", fareType=" + fareType + ", no="
				+ no + ", baoXiaoNo=" + baoXiaoNo + "]";
	}
	
}
