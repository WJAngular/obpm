/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.model;

/** 
 * @ClassName: LxfVO 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-08-10 下午3:59:27 
 *  
 */
public class LxfVO extends FundVO{
	private String surePayTime;//    ,[ITEM_确定支付时间]
	private String fundUser;//    ,[ITEM_资金用途]
	private String explain;//    ,[ITEM_用途说明]
	private String operator;//    ,[ITEM_操作人]
	private String fareType;//    ,[ITEM_费用类型]
	private String lingXiType;//    ,[ITEM_零星费用类型]
	private String no;//    ,[ITEM_编号]
	
	public LxfVO() {
		super();
		// TODO 自动生成的构造函数存根
	}
	public LxfVO(String surePayTime, String fundUser, String explain,
			String operator, String fareType, String lingXiType, String no) {
		super();
		this.surePayTime = surePayTime;
		this.fundUser = fundUser;
		this.explain = explain;
		this.operator = operator;
		this.fareType = fareType;
		this.lingXiType = lingXiType;
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
	 * @return lingXiType 
	 */
	public String getLingXiType() {
		return lingXiType;
	}
	/** 
	 * @param lingXiType 要设置的 lingXiType 
	 */
	public void setLingXiType(String lingXiType) {
		this.lingXiType = lingXiType;
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
		return "LxfVO [surePayTime=" + surePayTime + ", fundUser=" + fundUser
				+ ", explain=" + explain + ", operator=" + operator
				+ ", fareType=" + fareType + ", lingXiType=" + lingXiType
				+ ", no=" + no + "]";
	}
	
}
