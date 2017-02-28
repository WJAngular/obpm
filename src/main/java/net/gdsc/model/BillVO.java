/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.model;


/** 
 * @ClassName: BillVO 
 * @Description: 项目结算模型
 * @author: WUJING 
 * @date :2016-07-26 上午9:18:53 
 *  
 */
public class BillVO extends ObjectVO{
	
	private String matterName;//    ,[ITEM_事项名称]
	private String matterNo;//    ,[ITEM_事项编号]
	private String cAmount;//    ,[ITEM_合同金额]
	private String cAmountRevenue;//    ,[ITEM_合同已收入金额]
	private String taxMaterialNot;//    ,[ITEM_不含税材料总额]
	private String taxMaterialNotExp;//    ,[ITEM_不含税材料已开支]
	private String taxMaterialAmount;//    ,[ITEM_材料含税总额]
	private String taxMaterialExp;//    ,[ITEM_含税材料已开支]
	private String materialSum;//    ,[ITEM_材料合同总计]
	private String materialPaySum;//    ,[ITEM_材料已开支总额]
	private String constructionOneSum;//    ,[ITEM_施工费1总额]
	private String constructionOnePay;//    ,[ITEM_施工费1已开支]
	private String constructionTwoSum;//    ,[ITEM_施工费2总额]
	private String constructionTwoPay;//    ,[ITEM_施工费2已开支]
	private String financialDifSum;//    ,[ITEM_财务费用差额总额]   //已更改  不用
	private String financialSum;//    ,[ITEM_财务费用总额]         //已更改  不用
	private String biddingSum;//    ,[ITEM_招投标费用总额]
	private String biddingPay;//    ,[ITEM_招投标费用开支]
	private String sporadicSum;//    ,[ITEM_零星费用总额]
	private String sporadicPay;//    ,[ITEM_零星费用开支]
	private String costASum;//    ,[ITEM_费用小计A总额]
	private String costAPay;//    ,[ITEM_费用小计A开支]
	private String cDiff;//    ,[ITEM_合同差额]
	private String taxNotDiff;//    ,[ITEM_不含税差额]
	private String materialSumDiff;//    ,[ITEM_材料总计差额]
	private String constructionOneDiff;//    ,[ITEM_施工费1差额]
	private String constructionTwoDiff;//    ,[ITEM_施工费2差额]
	private String taxDiff;//    ,[ITEM_含税差额]
	private String costOneDiff;//    ,[ITEM_费用小计1差额]
	private String entertainmentCost;//    ,[ITEM_招待费用金额]
	private String EntertainmentPay;//    ,[ITEM_招待费用开支]
	private String lumpCost;//    ,[ITEM_包干业务费用金额]
	private String lumpPay;//    ,[ITEM_包干业务费用开支]
	private String dividedCost;//    ,[ITEM_分成业务费用金额]
	private String dividedPay;//    ,[ITEM_分成业务费用开支]
	private String processCost;//    ,[ITEM_过程业务费用金额]
	private String processPay;//    ,[ITEM_过程业务费用开支]
	private String biddingDiff;//    ,[ITEM_招投标费用差额]
	private String sporadicDiff;//    ,[ITEM_零星费用差额]
	private String hospitalityDiff;//    ,[ITEM_招待费用差额]
	private String lumpDiff;//    ,[ITEM_包干业务费用差额]
	private String dividedDiff;//    ,[ITEM_分成业务费用差额]
	private String processDiff;//    ,[ITEM_过程业务费用差额]
	private String costBSum;//    ,[ITEM_费用小计B总额]
	private String costBPay;//    ,[ITEM_费用小计B开支]
	private String costBDiff;//    ,[ITEM_费用小计B差额]
	private String costTotal;//    ,[ITEM_总成本]
	private String payTotal;//    ,[ITEM_总开支]
	private String diffTotal;//    ,[ITEM_总差额]
	private String grossProfitSum;//    ,[ITEM_项目总毛利润]
	private String grossProfitCurr;//    ,[ITEM_当前毛利]
	private String pendPay;//    ,[ITEM_待付款]
	private String fundAfterExpen;//    ,[ITEM_减开支后资金]
	private String marginSum; //    ,[ITEM_保证金占用金额]
	private String marginPay;//    ,[ITEM_保证金占用开支]
	private String marginDiff;//    ,[ITEM_保证金占用差额]
	private String otherSum;//    ,[ITEM_其他占用金额]
	private String otherPay;//    ,[ITEM_其他占用开支]
	private String otherDiff;//    ,[ITEM_其他占用差额]
	private String occupationSum;//    ,[ITEM_占用合计金额]
	private String occupationPay;//    ,[ITEM_占用合计开支]
	private String occupationDiff;//    ,[ITEM_占用合计差额]
	private String occupationAfter;//    ,[ITEM_减占用后资金]
	
	//新增字段
	private String taxDiffSumCW;//,[ITEM_税金差额总额]
	private String taxDiffPayCW;//,[ITEM_税金差额支出]
	private String taxDiffCW;//,[ITEM_税金差额]
		
	private String taxSum;//,[ITEM_税金总额]
	private String taxSumPay;//,[ITEM_税金总额支出]
	private String taxSumDiff;//,[ITEM_税金总额差额]
		
	private String taxManagerSum;//,[ITEM_税金管理费总额]
	private String taxManagerPay;//,[ITEM_税金管理费支出]
	private String taxManagerDiff;//,[ITEM_税金管理费差额]
		
	private String otherOneSum;//,[ITEM_其他费用1总额]
	private String otherOnePay;//,[ITEM_其他费用1支出]
	private String otherOneDiff;//,[ITEM_其他费用1差额]
		
	private String otherTwoSum;//,[ITEM_其他费用2]
	private String otherTwoPay;//,[ITEM_其他费用2支出]
	private String otherTwoDiff;//,[ITEM_其他费用2差额]
	
	//维护费
	private String maintenanceSum ;//,[ITEM_维护费总额]
	private String maintenancePay;//,[ITEM_维护费开支]
	private String maintenanceDiff;// ,[ITEM_维护费差额]
	
	public BillVO() {
		super();
		// TODO 自动生成的构造函数存根
	}
	
	public BillVO(String matterName, String matterNo, String cAmount,
			String cAmountRevenue, String taxMaterialNot,
			String taxMaterialNotExp, String taxMaterialAmount,
			String taxMaterialExp, String materialSum, String materialPaySum,
			String constructionOneSum, String constructionOnePay,
			String constructionTwoSum, String constructionTwoPay,
			String financialDifSum, String financialSum, String biddingSum,
			String biddingPay, String sporadicSum, String sporadicPay,
			String costASum, String costAPay, String cDiff, String taxNotDiff,
			String materialSumDiff, String constructionOneDiff,
			String constructionTwoDiff, String taxDiff, String costOneDiff,
			String entertainmentCost, String entertainmentPay, String lumpCost,
			String lumpPay, String dividedCost, String dividedPay,
			String processCost, String processPay, String biddingDiff,
			String sporadicDiff, String hospitalityDiff, String lumpDiff,
			String dividedDiff, String processDiff, String costBSum,
			String costBPay, String costBDiff, String costTotal,
			String payTotal, String diffTotal, String grossProfitSum,
			String grossProfitCurr, String pendPay, String fundAfterExpen,
			String marginSum, String marginPay, String marginDiff,
			String otherSum, String otherPay, String otherDiff,
			String occupationSum, String occupationPay, String occupationDiff,
			String occupationAfter, String taxDiffSumCW, String taxDiffPayCW,
			String taxDiffCW, String taxSum, String taxSumPay,
			String taxSumDiff, String taxManagerSum, String taxManagerPay,
			String taxManagerDiff, String otherOneSum, String otherOnePay,
			String otherOneDiff, String otherTwoSum, String otherTwoPay,
			String otherTwoDiff, String maintenanceSum, String maintenancePay,
			String maintenanceDiff) {
		super();
		this.matterName = matterName;
		this.matterNo = matterNo;
		this.cAmount = cAmount;
		this.cAmountRevenue = cAmountRevenue;
		this.taxMaterialNot = taxMaterialNot;
		this.taxMaterialNotExp = taxMaterialNotExp;
		this.taxMaterialAmount = taxMaterialAmount;
		this.taxMaterialExp = taxMaterialExp;
		this.materialSum = materialSum;
		this.materialPaySum = materialPaySum;
		this.constructionOneSum = constructionOneSum;
		this.constructionOnePay = constructionOnePay;
		this.constructionTwoSum = constructionTwoSum;
		this.constructionTwoPay = constructionTwoPay;
		this.financialDifSum = financialDifSum;
		this.financialSum = financialSum;
		this.biddingSum = biddingSum;
		this.biddingPay = biddingPay;
		this.sporadicSum = sporadicSum;
		this.sporadicPay = sporadicPay;
		this.costASum = costASum;
		this.costAPay = costAPay;
		this.cDiff = cDiff;
		this.taxNotDiff = taxNotDiff;
		this.materialSumDiff = materialSumDiff;
		this.constructionOneDiff = constructionOneDiff;
		this.constructionTwoDiff = constructionTwoDiff;
		this.taxDiff = taxDiff;
		this.costOneDiff = costOneDiff;
		this.entertainmentCost = entertainmentCost;
		EntertainmentPay = entertainmentPay;
		this.lumpCost = lumpCost;
		this.lumpPay = lumpPay;
		this.dividedCost = dividedCost;
		this.dividedPay = dividedPay;
		this.processCost = processCost;
		this.processPay = processPay;
		this.biddingDiff = biddingDiff;
		this.sporadicDiff = sporadicDiff;
		this.hospitalityDiff = hospitalityDiff;
		this.lumpDiff = lumpDiff;
		this.dividedDiff = dividedDiff;
		this.processDiff = processDiff;
		this.costBSum = costBSum;
		this.costBPay = costBPay;
		this.costBDiff = costBDiff;
		this.costTotal = costTotal;
		this.payTotal = payTotal;
		this.diffTotal = diffTotal;
		this.grossProfitSum = grossProfitSum;
		this.grossProfitCurr = grossProfitCurr;
		this.pendPay = pendPay;
		this.fundAfterExpen = fundAfterExpen;
		this.marginSum = marginSum;
		this.marginPay = marginPay;
		this.marginDiff = marginDiff;
		this.otherSum = otherSum;
		this.otherPay = otherPay;
		this.otherDiff = otherDiff;
		this.occupationSum = occupationSum;
		this.occupationPay = occupationPay;
		this.occupationDiff = occupationDiff;
		this.occupationAfter = occupationAfter;
		this.taxDiffSumCW = taxDiffSumCW;
		this.taxDiffPayCW = taxDiffPayCW;
		this.taxDiffCW = taxDiffCW;
		this.taxSum = taxSum;
		this.taxSumPay = taxSumPay;
		this.taxSumDiff = taxSumDiff;
		this.taxManagerSum = taxManagerSum;
		this.taxManagerPay = taxManagerPay;
		this.taxManagerDiff = taxManagerDiff;
		this.otherOneSum = otherOneSum;
		this.otherOnePay = otherOnePay;
		this.otherOneDiff = otherOneDiff;
		this.otherTwoSum = otherTwoSum;
		this.otherTwoPay = otherTwoPay;
		this.otherTwoDiff = otherTwoDiff;
		this.maintenanceSum = maintenanceSum;
		this.maintenancePay = maintenancePay;
		this.maintenanceDiff = maintenanceDiff;
	}




	/** 
	 * @return matterName 
	 */
	public String getMatterName() {
		return matterName;
	}
	/** 
	 * @param matterName 要设置的 matterName 
	 */
	public void setMatterName(String matterName) {
		this.matterName = matterName;
	}
	/** 
	 * @return matterNo 
	 */
	public String getMatterNo() {
		return matterNo;
	}
	/** 
	 * @param matterNo 要设置的 matterNo 
	 */
	public void setMatterNo(String matterNo) {
		this.matterNo = matterNo;
	}
	/** 
	 * @return cAmount 
	 */
	public String getcAmount() {
		return cAmount;
	}
	/** 
	 * @param cAmount 要设置的 cAmount 
	 */
	public void setcAmount(String cAmount) {
		this.cAmount = cAmount;
	}
	/** 
	 * @return cAmountRevenue 
	 */
	public String getcAmountRevenue() {
		return cAmountRevenue;
	}
	/** 
	 * @param cAmountRevenue 要设置的 cAmountRevenue 
	 */
	public void setcAmountRevenue(String cAmountRevenue) {
		this.cAmountRevenue = cAmountRevenue;
	}
	/** 
	 * @return taxMaterialNot 
	 */
	public String getTaxMaterialNot() {
		return taxMaterialNot;
	}
	/** 
	 * @param taxMaterialNot 要设置的 taxMaterialNot 
	 */
	public void setTaxMaterialNot(String taxMaterialNot) {
		this.taxMaterialNot = taxMaterialNot;
	}
	/** 
	 * @return taxMaterialNotExp 
	 */
	public String getTaxMaterialNotExp() {
		return taxMaterialNotExp;
	}
	/** 
	 * @param taxMaterialNotExp 要设置的 taxMaterialNotExp 
	 */
	public void setTaxMaterialNotExp(String taxMaterialNotExp) {
		this.taxMaterialNotExp = taxMaterialNotExp;
	}
	/** 
	 * @return taxMaterialAmount 
	 */
	public String getTaxMaterialAmount() {
		return taxMaterialAmount;
	}
	/** 
	 * @param taxMaterialAmount 要设置的 taxMaterialAmount 
	 */
	public void setTaxMaterialAmount(String taxMaterialAmount) {
		this.taxMaterialAmount = taxMaterialAmount;
	}
	/** 
	 * @return taxMaterialExp 
	 */
	public String getTaxMaterialExp() {
		return taxMaterialExp;
	}
	/** 
	 * @param taxMaterialExp 要设置的 taxMaterialExp 
	 */
	public void setTaxMaterialExp(String taxMaterialExp) {
		this.taxMaterialExp = taxMaterialExp;
	}
	/** 
	 * @return materialSum 
	 */
	public String getMaterialSum() {
		return materialSum;
	}
	/** 
	 * @param materialSum 要设置的 materialSum 
	 */
	public void setMaterialSum(String materialSum) {
		this.materialSum = materialSum;
	}
	/** 
	 * @return materialPaySum 
	 */
	public String getMaterialPaySum() {
		return materialPaySum;
	}
	/** 
	 * @param materialPaySum 要设置的 materialPaySum 
	 */
	public void setMaterialPaySum(String materialPaySum) {
		this.materialPaySum = materialPaySum;
	}
	/** 
	 * @return constructionOneSum 
	 */
	public String getConstructionOneSum() {
		return constructionOneSum;
	}
	/** 
	 * @param constructionOneSum 要设置的 constructionOneSum 
	 */
	public void setConstructionOneSum(String constructionOneSum) {
		this.constructionOneSum = constructionOneSum;
	}
	/** 
	 * @return constructionOnePay 
	 */
	public String getConstructionOnePay() {
		return constructionOnePay;
	}
	/** 
	 * @param constructionOnePay 要设置的 constructionOnePay 
	 */
	public void setConstructionOnePay(String constructionOnePay) {
		this.constructionOnePay = constructionOnePay;
	}
	/** 
	 * @return constructionTwoSum 
	 */
	public String getConstructionTwoSum() {
		return constructionTwoSum;
	}
	/** 
	 * @param constructionTwoSum 要设置的 constructionTwoSum 
	 */
	public void setConstructionTwoSum(String constructionTwoSum) {
		this.constructionTwoSum = constructionTwoSum;
	}
	/** 
	 * @return constructionTwoPay 
	 */
	public String getConstructionTwoPay() {
		return constructionTwoPay;
	}
	/** 
	 * @param constructionTwoPay 要设置的 constructionTwoPay 
	 */
	public void setConstructionTwoPay(String constructionTwoPay) {
		this.constructionTwoPay = constructionTwoPay;
	}
	/** 
	 * @return financialDifSum 
	 */
	public String getFinancialDifSum() {
		return financialDifSum;
	}
	/** 
	 * @param financialDifSum 要设置的 financialDifSum 
	 */
	public void setFinancialDifSum(String financialDifSum) {
		this.financialDifSum = financialDifSum;
	}
	/** 
	 * @return financialSum 
	 */
	public String getFinancialSum() {
		return financialSum;
	}
	/** 
	 * @param financialSum 要设置的 financialSum 
	 */
	public void setFinancialSum(String financialSum) {
		this.financialSum = financialSum;
	}
	/** 
	 * @return biddingSum 
	 */
	public String getBiddingSum() {
		return biddingSum;
	}
	/** 
	 * @param biddingSum 要设置的 biddingSum 
	 */
	public void setBiddingSum(String biddingSum) {
		this.biddingSum = biddingSum;
	}
	/** 
	 * @return biddingPay 
	 */
	public String getBiddingPay() {
		return biddingPay;
	}
	/** 
	 * @param biddingPay 要设置的 biddingPay 
	 */
	public void setBiddingPay(String biddingPay) {
		this.biddingPay = biddingPay;
	}
	/** 
	 * @return sporadicSum 
	 */
	public String getSporadicSum() {
		return sporadicSum;
	}
	/** 
	 * @param sporadicSum 要设置的 sporadicSum 
	 */
	public void setSporadicSum(String sporadicSum) {
		this.sporadicSum = sporadicSum;
	}
	/** 
	 * @return sporadicPay 
	 */
	public String getSporadicPay() {
		return sporadicPay;
	}
	/** 
	 * @param sporadicPay 要设置的 sporadicPay 
	 */
	public void setSporadicPay(String sporadicPay) {
		this.sporadicPay = sporadicPay;
	}
	/** 
	 * @return costASum 
	 */
	public String getCostASum() {
		return costASum;
	}
	/** 
	 * @param costASum 要设置的 costASum 
	 */
	public void setCostASum(String costASum) {
		this.costASum = costASum;
	}
	/** 
	 * @return costAPay 
	 */
	public String getCostAPay() {
		return costAPay;
	}
	/** 
	 * @param costAPay 要设置的 costAPay 
	 */
	public void setCostAPay(String costAPay) {
		this.costAPay = costAPay;
	}
	/** 
	 * @return cDiff 
	 */
	public String getcDiff() {
		return cDiff;
	}
	/** 
	 * @param cDiff 要设置的 cDiff 
	 */
	public void setcDiff(String cDiff) {
		this.cDiff = cDiff;
	}
	/** 
	 * @return taxNotDiff 
	 */
	public String getTaxNotDiff() {
		return taxNotDiff;
	}
	/** 
	 * @param taxNotDiff 要设置的 taxNotDiff 
	 */
	public void setTaxNotDiff(String taxNotDiff) {
		this.taxNotDiff = taxNotDiff;
	}
	/** 
	 * @return materialSumDiff 
	 */
	public String getMaterialSumDiff() {
		return materialSumDiff;
	}
	/** 
	 * @param materialSumDiff 要设置的 materialSumDiff 
	 */
	public void setMaterialSumDiff(String materialSumDiff) {
		this.materialSumDiff = materialSumDiff;
	}
	/** 
	 * @return constructionOneDiff 
	 */
	public String getConstructionOneDiff() {
		return constructionOneDiff;
	}
	/** 
	 * @param constructionOneDiff 要设置的 constructionOneDiff 
	 */
	public void setConstructionOneDiff(String constructionOneDiff) {
		this.constructionOneDiff = constructionOneDiff;
	}
	/** 
	 * @return constructionTwoDiff 
	 */
	public String getConstructionTwoDiff() {
		return constructionTwoDiff;
	}
	/** 
	 * @param constructionTwoDiff 要设置的 constructionTwoDiff 
	 */
	public void setConstructionTwoDiff(String constructionTwoDiff) {
		this.constructionTwoDiff = constructionTwoDiff;
	}
	/** 
	 * @return taxDiff 
	 */
	public String getTaxDiff() {
		return taxDiff;
	}
	/** 
	 * @param taxDiff 要设置的 taxDiff 
	 */
	public void setTaxDiff(String taxDiff) {
		this.taxDiff = taxDiff;
	}
	/** 
	 * @return costOneDiff 
	 */
	public String getCostOneDiff() {
		return costOneDiff;
	}
	/** 
	 * @param costOneDiff 要设置的 costOneDiff 
	 */
	public void setCostOneDiff(String costOneDiff) {
		this.costOneDiff = costOneDiff;
	}
	/** 
	 * @return entertainmentCost 
	 */
	public String getEntertainmentCost() {
		return entertainmentCost;
	}
	/** 
	 * @param entertainmentCost 要设置的 entertainmentCost 
	 */
	public void setEntertainmentCost(String entertainmentCost) {
		this.entertainmentCost = entertainmentCost;
	}
	/** 
	 * @return entertainmentPay 
	 */
	public String getEntertainmentPay() {
		return EntertainmentPay;
	}
	/** 
	 * @param entertainmentPay 要设置的 entertainmentPay 
	 */
	public void setEntertainmentPay(String entertainmentPay) {
		EntertainmentPay = entertainmentPay;
	}
	/** 
	 * @return lumpCost 
	 */
	public String getLumpCost() {
		return lumpCost;
	}
	/** 
	 * @param lumpCost 要设置的 lumpCost 
	 */
	public void setLumpCost(String lumpCost) {
		this.lumpCost = lumpCost;
	}
	/** 
	 * @return lumpPay 
	 */
	public String getLumpPay() {
		return lumpPay;
	}
	/** 
	 * @param lumpPay 要设置的 lumpPay 
	 */
	public void setLumpPay(String lumpPay) {
		this.lumpPay = lumpPay;
	}
	/** 
	 * @return dividedCost 
	 */
	public String getDividedCost() {
		return dividedCost;
	}
	/** 
	 * @param dividedCost 要设置的 dividedCost 
	 */
	public void setDividedCost(String dividedCost) {
		this.dividedCost = dividedCost;
	}
	/** 
	 * @return dividedPay 
	 */
	public String getDividedPay() {
		return dividedPay;
	}
	/** 
	 * @param dividedPay 要设置的 dividedPay 
	 */
	public void setDividedPay(String dividedPay) {
		this.dividedPay = dividedPay;
	}
	/** 
	 * @return processCost 
	 */
	public String getProcessCost() {
		return processCost;
	}
	/** 
	 * @param processCost 要设置的 processCost 
	 */
	public void setProcessCost(String processCost) {
		this.processCost = processCost;
	}
	/** 
	 * @return processPay 
	 */
	public String getProcessPay() {
		return processPay;
	}
	/** 
	 * @param processPay 要设置的 processPay 
	 */
	public void setProcessPay(String processPay) {
		this.processPay = processPay;
	}
	/** 
	 * @return biddingDiff 
	 */
	public String getBiddingDiff() {
		return biddingDiff;
	}
	/** 
	 * @param biddingDiff 要设置的 biddingDiff 
	 */
	public void setBiddingDiff(String biddingDiff) {
		this.biddingDiff = biddingDiff;
	}
	/** 
	 * @return sporadicDiff 
	 */
	public String getSporadicDiff() {
		return sporadicDiff;
	}
	/** 
	 * @param sporadicDiff 要设置的 sporadicDiff 
	 */
	public void setSporadicDiff(String sporadicDiff) {
		this.sporadicDiff = sporadicDiff;
	}
	/** 
	 * @return hospitalityDiff 
	 */
	public String getHospitalityDiff() {
		return hospitalityDiff;
	}
	/** 
	 * @param hospitalityDiff 要设置的 hospitalityDiff 
	 */
	public void setHospitalityDiff(String hospitalityDiff) {
		this.hospitalityDiff = hospitalityDiff;
	}
	/** 
	 * @return lumpDiff 
	 */
	public String getLumpDiff() {
		return lumpDiff;
	}
	/** 
	 * @param lumpDiff 要设置的 lumpDiff 
	 */
	public void setLumpDiff(String lumpDiff) {
		this.lumpDiff = lumpDiff;
	}
	/** 
	 * @return dividedDiff 
	 */
	public String getDividedDiff() {
		return dividedDiff;
	}
	/** 
	 * @param dividedDiff 要设置的 dividedDiff 
	 */
	public void setDividedDiff(String dividedDiff) {
		this.dividedDiff = dividedDiff;
	}
	/** 
	 * @return processDiff 
	 */
	public String getProcessDiff() {
		return processDiff;
	}
	/** 
	 * @param processDiff 要设置的 processDiff 
	 */
	public void setProcessDiff(String processDiff) {
		this.processDiff = processDiff;
	}
	/** 
	 * @return costBSum 
	 */
	public String getCostBSum() {
		return costBSum;
	}
	/** 
	 * @param costBSum 要设置的 costBSum 
	 */
	public void setCostBSum(String costBSum) {
		this.costBSum = costBSum;
	}
	/** 
	 * @return costBPay 
	 */
	public String getCostBPay() {
		return costBPay;
	}
	/** 
	 * @param costBPay 要设置的 costBPay 
	 */
	public void setCostBPay(String costBPay) {
		this.costBPay = costBPay;
	}
	/** 
	 * @return costBDiff 
	 */
	public String getCostBDiff() {
		return costBDiff;
	}
	/** 
	 * @param costBDiff 要设置的 costBDiff 
	 */
	public void setCostBDiff(String costBDiff) {
		this.costBDiff = costBDiff;
	}
	/** 
	 * @return costTotal 
	 */
	public String getCostTotal() {
		return costTotal;
	}
	/** 
	 * @param costTotal 要设置的 costTotal 
	 */
	public void setCostTotal(String costTotal) {
		this.costTotal = costTotal;
	}
	/** 
	 * @return payTotal 
	 */
	public String getPayTotal() {
		return payTotal;
	}
	/** 
	 * @param payTotal 要设置的 payTotal 
	 */
	public void setPayTotal(String payTotal) {
		this.payTotal = payTotal;
	}
	/** 
	 * @return diffTotal 
	 */
	public String getDiffTotal() {
		return diffTotal;
	}
	/** 
	 * @param diffTotal 要设置的 diffTotal 
	 */
	public void setDiffTotal(String diffTotal) {
		this.diffTotal = diffTotal;
	}
	/** 
	 * @return grossProfitSum 
	 */
	public String getGrossProfitSum() {
		return grossProfitSum;
	}
	/** 
	 * @param grossProfitSum 要设置的 grossProfitSum 
	 */
	public void setGrossProfitSum(String grossProfitSum) {
		this.grossProfitSum = grossProfitSum;
	}
	/** 
	 * @return grossProfitCurr 
	 */
	public String getGrossProfitCurr() {
		return grossProfitCurr;
	}
	/** 
	 * @param grossProfitCurr 要设置的 grossProfitCurr 
	 */
	public void setGrossProfitCurr(String grossProfitCurr) {
		this.grossProfitCurr = grossProfitCurr;
	}
	/** 
	 * @return pendPay 
	 */
	public String getPendPay() {
		return pendPay;
	}
	/** 
	 * @param pendPay 要设置的 pendPay 
	 */
	public void setPendPay(String pendPay) {
		this.pendPay = pendPay;
	}
	/** 
	 * @return fundAfterExpen 
	 */
	public String getFundAfterExpen() {
		return fundAfterExpen;
	}
	/** 
	 * @param fundAfterExpen 要设置的 fundAfterExpen 
	 */
	public void setFundAfterExpen(String fundAfterExpen) {
		this.fundAfterExpen = fundAfterExpen;
	}
	/** 
	 * @return marginSum 
	 */
	public String getMarginSum() {
		return marginSum;
	}
	/** 
	 * @param marginSum 要设置的 marginSum 
	 */
	public void setMarginSum(String marginSum) {
		this.marginSum = marginSum;
	}
	/** 
	 * @return marginPay 
	 */
	public String getMarginPay() {
		return marginPay;
	}
	/** 
	 * @param marginPay 要设置的 marginPay 
	 */
	public void setMarginPay(String marginPay) {
		this.marginPay = marginPay;
	}
	/** 
	 * @return marginDiff 
	 */
	public String getMarginDiff() {
		return marginDiff;
	}
	/** 
	 * @param marginDiff 要设置的 marginDiff 
	 */
	public void setMarginDiff(String marginDiff) {
		this.marginDiff = marginDiff;
	}
	/** 
	 * @return otherSum 
	 */
	public String getOtherSum() {
		return otherSum;
	}
	/** 
	 * @param otherSum 要设置的 otherSum 
	 */
	public void setOtherSum(String otherSum) {
		this.otherSum = otherSum;
	}
	/** 
	 * @return otherPay 
	 */
	public String getOtherPay() {
		return otherPay;
	}
	/** 
	 * @param otherPay 要设置的 otherPay 
	 */
	public void setOtherPay(String otherPay) {
		this.otherPay = otherPay;
	}
	/** 
	 * @return otherDiff 
	 */
	public String getOtherDiff() {
		return otherDiff;
	}
	/** 
	 * @param otherDiff 要设置的 otherDiff 
	 */
	public void setOtherDiff(String otherDiff) {
		this.otherDiff = otherDiff;
	}
	/** 
	 * @return occupationSum 
	 */
	public String getOccupationSum() {
		return occupationSum;
	}
	/** 
	 * @param occupationSum 要设置的 occupationSum 
	 */
	public void setOccupationSum(String occupationSum) {
		this.occupationSum = occupationSum;
	}
	/** 
	 * @return occupationPay 
	 */
	public String getOccupationPay() {
		return occupationPay;
	}
	/** 
	 * @param occupationPay 要设置的 occupationPay 
	 */
	public void setOccupationPay(String occupationPay) {
		this.occupationPay = occupationPay;
	}
	/** 
	 * @return occupationDiff 
	 */
	public String getOccupationDiff() {
		return occupationDiff;
	}
	/** 
	 * @param occupationDiff 要设置的 occupationDiff 
	 */
	public void setOccupationDiff(String occupationDiff) {
		this.occupationDiff = occupationDiff;
	}
	/** 
	 * @return occupationAfter 
	 */
	public String getOccupationAfter() {
		return occupationAfter;
	}
	/** 
	 * @param occupationAfter 要设置的 occupationAfter 
	 */
	public void setOccupationAfter(String occupationAfter) {
		this.occupationAfter = occupationAfter;
	}
	/** 
	 * @return taxDiffSumCW 
	 */
	public String getTaxDiffSumCW() {
		return taxDiffSumCW;
	}
	/** 
	 * @param taxDiffSumCW 要设置的 taxDiffSumCW 
	 */
	public void setTaxDiffSumCW(String taxDiffSumCW) {
		this.taxDiffSumCW = taxDiffSumCW;
	}
	/** 
	 * @return taxDiffPayCW 
	 */
	public String getTaxDiffPayCW() {
		return taxDiffPayCW;
	}
	/** 
	 * @param taxDiffPayCW 要设置的 taxDiffPayCW 
	 */
	public void setTaxDiffPayCW(String taxDiffPayCW) {
		this.taxDiffPayCW = taxDiffPayCW;
	}
	/** 
	 * @return taxDiffCW 
	 */
	public String getTaxDiffCW() {
		return taxDiffCW;
	}
	/** 
	 * @param taxDiffCW 要设置的 taxDiffCW 
	 */
	public void setTaxDiffCW(String taxDiffCW) {
		this.taxDiffCW = taxDiffCW;
	}
	/** 
	 * @return taxSum 
	 */
	public String getTaxSum() {
		return taxSum;
	}
	/** 
	 * @param taxSum 要设置的 taxSum 
	 */
	public void setTaxSum(String taxSum) {
		this.taxSum = taxSum;
	}
	/** 
	 * @return taxSumPay 
	 */
	public String getTaxSumPay() {
		return taxSumPay;
	}
	/** 
	 * @param taxSumPay 要设置的 taxSumPay 
	 */
	public void setTaxSumPay(String taxSumPay) {
		this.taxSumPay = taxSumPay;
	}
	/** 
	 * @return taxSumDiff 
	 */
	public String getTaxSumDiff() {
		return taxSumDiff;
	}
	/** 
	 * @param taxSumDiff 要设置的 taxSumDiff 
	 */
	public void setTaxSumDiff(String taxSumDiff) {
		this.taxSumDiff = taxSumDiff;
	}
	/** 
	 * @return taxManagerSum 
	 */
	public String getTaxManagerSum() {
		return taxManagerSum;
	}
	/** 
	 * @param taxManagerSum 要设置的 taxManagerSum 
	 */
	public void setTaxManagerSum(String taxManagerSum) {
		this.taxManagerSum = taxManagerSum;
	}
	/** 
	 * @return taxManagerPay 
	 */
	public String getTaxManagerPay() {
		return taxManagerPay;
	}
	/** 
	 * @param taxManagerPay 要设置的 taxManagerPay 
	 */
	public void setTaxManagerPay(String taxManagerPay) {
		this.taxManagerPay = taxManagerPay;
	}
	/** 
	 * @return taxManagerDiff 
	 */
	public String getTaxManagerDiff() {
		return taxManagerDiff;
	}
	/** 
	 * @param taxManagerDiff 要设置的 taxManagerDiff 
	 */
	public void setTaxManagerDiff(String taxManagerDiff) {
		this.taxManagerDiff = taxManagerDiff;
	}
	/** 
	 * @return otherOneSum 
	 */
	public String getOtherOneSum() {
		return otherOneSum;
	}
	/** 
	 * @param otherOneSum 要设置的 otherOneSum 
	 */
	public void setOtherOneSum(String otherOneSum) {
		this.otherOneSum = otherOneSum;
	}
	/** 
	 * @return otherOnePay 
	 */
	public String getOtherOnePay() {
		return otherOnePay;
	}
	/** 
	 * @param otherOnePay 要设置的 otherOnePay 
	 */
	public void setOtherOnePay(String otherOnePay) {
		this.otherOnePay = otherOnePay;
	}
	/** 
	 * @return otherOneDiff 
	 */
	public String getOtherOneDiff() {
		return otherOneDiff;
	}
	/** 
	 * @param otherOneDiff 要设置的 otherOneDiff 
	 */
	public void setOtherOneDiff(String otherOneDiff) {
		this.otherOneDiff = otherOneDiff;
	}
	/** 
	 * @return otherTwoSum 
	 */
	public String getOtherTwoSum() {
		return otherTwoSum;
	}
	/** 
	 * @param otherTwoSum 要设置的 otherTwoSum 
	 */
	public void setOtherTwoSum(String otherTwoSum) {
		this.otherTwoSum = otherTwoSum;
	}
	/** 
	 * @return otherTwoPay 
	 */
	public String getOtherTwoPay() {
		return otherTwoPay;
	}
	/** 
	 * @param otherTwoPay 要设置的 otherTwoPay 
	 */
	public void setOtherTwoPay(String otherTwoPay) {
		this.otherTwoPay = otherTwoPay;
	}
	/** 
	 * @return otherTwoDiff 
	 */
	public String getOtherTwoDiff() {
		return otherTwoDiff;
	}
	/** 
	 * @param otherTwoDiff 要设置的 otherTwoDiff 
	 */
	public void setOtherTwoDiff(String otherTwoDiff) {
		this.otherTwoDiff = otherTwoDiff;
	}


	/** 
	 * @return maintenanceSum 
	 */
	public String getMaintenanceSum() {
		return maintenanceSum;
	}


	/** 
	 * @param maintenanceSum 要设置的 maintenanceSum 
	 */
	public void setMaintenanceSum(String maintenanceSum) {
		this.maintenanceSum = maintenanceSum;
	}


	/** 
	 * @return maintenancePay 
	 */
	public String getMaintenancePay() {
		return maintenancePay;
	}


	/** 
	 * @param maintenancePay 要设置的 maintenancePay 
	 */
	public void setMaintenancePay(String maintenancePay) {
		this.maintenancePay = maintenancePay;
	}


	/** 
	 * @return maintenanceDiff 
	 */
	public String getMaintenanceDiff() {
		return maintenanceDiff;
	}


	/** 
	 * @param maintenanceDiff 要设置的 maintenanceDiff 
	 */
	public void setMaintenanceDiff(String maintenanceDiff) {
		this.maintenanceDiff = maintenanceDiff;
	}

	/** 
	* @Title: toString 
	* @Description: TODO
	* @param: @return 
	* @throws 
	*/
	@Override
	public String toString() {
		return "BillVO [matterName=" + matterName + ", matterNo=" + matterNo
				+ ", cAmount=" + cAmount + ", cAmountRevenue=" + cAmountRevenue
				+ ", taxMaterialNot=" + taxMaterialNot + ", taxMaterialNotExp="
				+ taxMaterialNotExp + ", taxMaterialAmount="
				+ taxMaterialAmount + ", taxMaterialExp=" + taxMaterialExp
				+ ", materialSum=" + materialSum + ", materialPaySum="
				+ materialPaySum + ", constructionOneSum=" + constructionOneSum
				+ ", constructionOnePay=" + constructionOnePay
				+ ", constructionTwoSum=" + constructionTwoSum
				+ ", constructionTwoPay=" + constructionTwoPay
				+ ", financialDifSum=" + financialDifSum + ", financialSum="
				+ financialSum + ", biddingSum=" + biddingSum + ", biddingPay="
				+ biddingPay + ", sporadicSum=" + sporadicSum
				+ ", sporadicPay=" + sporadicPay + ", costASum=" + costASum
				+ ", costAPay=" + costAPay + ", cDiff=" + cDiff
				+ ", taxNotDiff=" + taxNotDiff + ", materialSumDiff="
				+ materialSumDiff + ", constructionOneDiff="
				+ constructionOneDiff + ", constructionTwoDiff="
				+ constructionTwoDiff + ", taxDiff=" + taxDiff
				+ ", costOneDiff=" + costOneDiff + ", entertainmentCost="
				+ entertainmentCost + ", EntertainmentPay=" + EntertainmentPay
				+ ", lumpCost=" + lumpCost + ", lumpPay=" + lumpPay
				+ ", dividedCost=" + dividedCost + ", dividedPay=" + dividedPay
				+ ", processCost=" + processCost + ", processPay=" + processPay
				+ ", biddingDiff=" + biddingDiff + ", sporadicDiff="
				+ sporadicDiff + ", hospitalityDiff=" + hospitalityDiff
				+ ", lumpDiff=" + lumpDiff + ", dividedDiff=" + dividedDiff
				+ ", processDiff=" + processDiff + ", costBSum=" + costBSum
				+ ", costBPay=" + costBPay + ", costBDiff=" + costBDiff
				+ ", costTotal=" + costTotal + ", payTotal=" + payTotal
				+ ", diffTotal=" + diffTotal + ", grossProfitSum="
				+ grossProfitSum + ", grossProfitCurr=" + grossProfitCurr
				+ ", pendPay=" + pendPay + ", fundAfterExpen=" + fundAfterExpen
				+ ", marginSum=" + marginSum + ", marginPay=" + marginPay
				+ ", marginDiff=" + marginDiff + ", otherSum=" + otherSum
				+ ", otherPay=" + otherPay + ", otherDiff=" + otherDiff
				+ ", occupationSum=" + occupationSum + ", occupationPay="
				+ occupationPay + ", occupationDiff=" + occupationDiff
				+ ", occupationAfter=" + occupationAfter + ", taxDiffSumCW="
				+ taxDiffSumCW + ", taxDiffPayCW=" + taxDiffPayCW
				+ ", taxDiffCW=" + taxDiffCW + ", taxSum=" + taxSum
				+ ", taxSumPay=" + taxSumPay + ", taxSumDiff=" + taxSumDiff
				+ ", taxManagerSum=" + taxManagerSum + ", taxManagerPay="
				+ taxManagerPay + ", taxManagerDiff=" + taxManagerDiff
				+ ", otherOneSum=" + otherOneSum + ", otherOnePay="
				+ otherOnePay + ", otherOneDiff=" + otherOneDiff
				+ ", otherTwoSum=" + otherTwoSum + ", otherTwoPay="
				+ otherTwoPay + ", otherTwoDiff=" + otherTwoDiff
				+ ", maintenanceSum=" + maintenanceSum + ", maintenancePay="
				+ maintenancePay + ", maintenanceDiff=" + maintenanceDiff + "]";
	}
	
}
