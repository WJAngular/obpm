/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.model;

/** 
 * @ClassName: PactVO 
 * @Description: 结算中项目合同模型
 * @author: WUJING 
 * @date :2016-07-28 下午2:23:25 
 *  
 */
public class PactVO extends ObjectVO{
//    ,[ITEM_事项名称]
//    ,[ITEM_事项编号]
//    ,[ITEM_编号]
//    ,[ITEM_报账人]
//    ,[ITEM_凭证号]
//    ,[ITEM_现合同金额]
//    ,[ITEM_以往合同金额]
//    ,[ITEM_新增合同金额]
//    ,[ITEM_本次收入金额]
//    ,[ITEM_以往收入总金额]
//    ,[ITEM_余额]
//    ,[ITEM_操作人]
//    ,[ITEM_日期]
//    ,[ITEM_以往收入明细]
//    ,[ITEM_备注]
	private String prMatter;
	private String prMatterNo;//事项编号
	private String prNo ;//编号
	private String prBaozhang;
	private String prDocumentNo;
	private String prFee;
	private String prFeeNew;
	private String prFeeOld;
	private String prInCome;
	private String prInComeOld;
	private String prBalance;
	private String prOperator;
	private String prDate;
	private String prInComeHis;
	private String prRemark;
	
	public PactVO() {
		super();
		// TODO 自动生成的构造函数存根
	}
	
	public PactVO(String prMatter, String prMatterNo, String prNo,
			String prBaozhang, String prDocumentNo, String prFee,
			String prFeeNew,String prFeeOld, String prInCome, String prInComeOld,
			String prBalance, String prOperator, String prDate,
			String prInComeHis, String prRemark) {
		super();
		this.prMatter = prMatter;
		this.prMatterNo = prMatterNo;
		this.prNo = prNo;
		this.prBaozhang = prBaozhang;
		this.prDocumentNo = prDocumentNo;
		this.prFee = prFee;
		this.prFeeNew = prFeeNew;
		this.prFeeOld = prFeeOld;
		this.prInCome = prInCome;
		this.prInComeOld = prInComeOld;
		this.prBalance = prBalance;
		this.prOperator = prOperator;
		this.prDate = prDate;
		this.prInComeHis = prInComeHis;
		this.prRemark = prRemark;
	}

	/** 
	 * @return prMatter 
	 */
	public String getPrMatter() {
		return prMatter;
	}
	/** 
	 * @param prMatter 要设置的 prMatter 
	 */
	public void setPrMatter(String prMatter) {
		this.prMatter = prMatter;
	}
	/** 
	 * @return prMatterNo 
	 */
	public String getPrMatterNo() {
		return prMatterNo;
	}
	/** 
	 * @param prMatterNo 要设置的 prMatterNo 
	 */
	public void setPrMatterNo(String prMatterNo) {
		this.prMatterNo = prMatterNo;
	}
	/** 
	 * @return prNo 
	 */
	public String getPrNo() {
		return prNo;
	}
	/** 
	 * @param prNo 要设置的 prNo 
	 */
	public void setPrNo(String prNo) {
		this.prNo = prNo;
	}
	/** 
	 * @return prBaozhang 
	 */
	public String getPrBaozhang() {
		return prBaozhang;
	}
	/** 
	 * @param prBaozhang 要设置的 prBaozhang 
	 */
	public void setPrBaozhang(String prBaozhang) {
		this.prBaozhang = prBaozhang;
	}
	/** 
	 * @return prDocumentNo 
	 */
	public String getPrDocumentNo() {
		return prDocumentNo;
	}
	/** 
	 * @param prDocumentNo 要设置的 prDocumentNo 
	 */
	public void setPrDocumentNo(String prDocumentNo) {
		this.prDocumentNo = prDocumentNo;
	}
	/** 
	 * @return prFee 
	 */
	public String getPrFee() {
		return prFee;
	}
	/** 
	 * @param prFee 要设置的 prFee 
	 */
	public void setPrFee(String prFee) {
		this.prFee = prFee;
	}
	/** 
	 * @return prFeeNew 
	 */
	public String getPrFeeNew() {
		return prFeeNew;
	}
	/** 
	 * @param prFeeNew 要设置的 prFeeNew 
	 */
	public void setPrFeeNew(String prFeeNew) {
		this.prFeeNew = prFeeNew;
	}
	/** 
	 * @return prInCome 
	 */
	public String getPrInCome() {
		return prInCome;
	}
	/** 
	 * @param prInCome 要设置的 prInCome 
	 */
	public void setPrInCome(String prInCome) {
		this.prInCome = prInCome;
	}
	/** 
	 * @return prInComeOld 
	 */
	public String getPrInComeOld() {
		return prInComeOld;
	}
	/** 
	 * @param prInComeOld 要设置的 prInComeOld 
	 */
	public void setPrInComeOld(String prInComeOld) {
		this.prInComeOld = prInComeOld;
	}
	/** 
	 * @return prBalance 
	 */
	public String getPrBalance() {
		return prBalance;
	}
	/** 
	 * @param prBalance 要设置的 prBalance 
	 */
	public void setPrBalance(String prBalance) {
		this.prBalance = prBalance;
	}
	/** 
	 * @return prOperator 
	 */
	public String getPrOperator() {
		return prOperator;
	}
	/** 
	 * @param prOperator 要设置的 prOperator 
	 */
	public void setPrOperator(String prOperator) {
		this.prOperator = prOperator;
	}
	/** 
	 * @return prDate 
	 */
	public String getPrDate() {
		return prDate;
	}
	/** 
	 * @param prDate 要设置的 prDate 
	 */
	public void setPrDate(String prDate) {
		this.prDate = prDate;
	}
	/** 
	 * @return prInComeHis 
	 */
	public String getPrInComeHis() {
		return prInComeHis;
	}
	/** 
	 * @param prInComeHis 要设置的 prInComeHis 
	 */
	public void setPrInComeHis(String prInComeHis) {
		this.prInComeHis = prInComeHis;
	}
	/** 
	 * @return prRemark 
	 */
	public String getPrRemark() {
		return prRemark;
	}
	/** 
	 * @param prRemark 要设置的 prRemark 
	 */
	public void setPrRemark(String prRemark) {
		this.prRemark = prRemark;
	}

	/** 
	 * @return prFeeOld 
	 */
	public String getPrFeeOld() {
		return prFeeOld;
	}

	/** 
	 * @param prFeeOld 要设置的 prFeeOld 
	 */
	public void setPrFeeOld(String prFeeOld) {
		this.prFeeOld = prFeeOld;
	}

	/** 
	* @Title: toString 
	* @Description: TODO
	* @param: @return 
	* @throws 
	*/
	@Override
	public String toString() {
		return "PactVO [prMatter=" + prMatter + ", prMatterNo=" + prMatterNo
				+ ", prNo=" + prNo + ", prBaozhang=" + prBaozhang
				+ ", prDocumentNo=" + prDocumentNo + ", prFee=" + prFee
				+ ", prFeeNew=" + prFeeNew + ", prFeeOld=" + prFeeOld
				+ ", prInCome=" + prInCome + ", prInComeOld=" + prInComeOld
				+ ", prBalance=" + prBalance + ", prOperator=" + prOperator
				+ ", prDate=" + prDate + ", prInComeHis=" + prInComeHis
				+ ", prRemark=" + prRemark + "]";
	}
	
}
