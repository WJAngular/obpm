/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.dao.bill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.gdsc.model.BillVO;
import net.gdsc.util.ConnectionManager;

import org.apache.log4j.Logger;

/** 
 * @ClassName: BillDao 
 * @Description: 结算表
 * @author: WUJING 
 * @date :2016-07-26 上午10:40:31 
 *  
 */
public class BillDao implements IBillDao{
	
private static final Logger logger = Logger.getLogger(BillDao.class);
	
	private static BillDao billDao;
	
	public static synchronized BillDao getInstance(){
		if(billDao==null)
			billDao = new BillDao();
		return billDao;
	}
	public BillVO queryBillVO(String proNo,String primaryTable){
		BillVO billVO = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = ConnectionManager.getConnection();
		String sql = "select * from "+primaryTable+" where ITEM_事项编号   = '"+proNo+"'";
		logger.info(sql);
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				billVO = setBillVOParam(rs);
			}
			return billVO;
		} catch (Exception e) {
			logger.error(e);
		}finally{
			try{
				if(rs!=null) rs.close();
				ConnectionManager.closeStatement(stmt);
				ConnectionManager.closeConnection();
			}catch(Exception e){
				logger.error("请联系管理员",e);
			}
		}
		return null;
	}
	
	public BillVO setBillVOParam(ResultSet rs){
		BillVO billVO = new BillVO();
		try {
			billVO.setApplicationid(rs.getString("Applicationid"));
			billVO.setId(rs.getString("ID"));
			billVO.setAuditdate(rs.getTimestamp("Auditdate"));
			billVO.setAuditorlist(rs.getString("Auditorlist"));
			billVO.setAuditornames(rs.getString("auditornames"));
			billVO.setAudituser(rs.getString("audituser"));
			billVO.setAuthor(rs.getString("author"));
			billVO.setAuthor_dept_index(rs.getString("author_dept_index"));
			billVO.setCreated(rs.getTimestamp("created"));
			billVO.setDomainid(rs.getString("domainid"));
			billVO.setFormid(rs.getString("formid"));
			billVO.setFormname(rs.getString("formname"));
			billVO.setIstmp(rs.getBoolean("istmp"));
			billVO.setLastflowoperation(rs.getString("lastflowoperation"));
			billVO.setLastmodified(rs.getTimestamp("lastmodified"));
			billVO.setLastmodifier(rs.getString("lastmodifier"));
			billVO.setParent(rs.getString("parent"));
			billVO.setPrevauditnode(rs.getString("prevauditnode"));
			billVO.setPrevaudituser(rs.getString("prevaudituser"));
			billVO.setState(rs.getString("state"));
			billVO.setStateint(rs.getInt("stateint"));
			billVO.setStatelabel(rs.getString("statelabel"));
			billVO.setStatelabelinfo(rs.getString("statelabelinfo"));
			billVO.setVersions(rs.getInt("versions"));
			
			billVO.setMatterName(rs.getString("ITEM_事项名称"));
			billVO.setMatterNo(rs.getString("ITEM_事项编号"));
			billVO.setcAmount(rs.getString("ITEM_合同金额"));
			billVO.setcAmountRevenue(rs.getString("ITEM_合同已收入金额"));
			billVO.setTaxMaterialNot(rs.getString("ITEM_不含税材料总额"));
			billVO.setTaxMaterialNotExp(rs.getString("ITEM_不含税材料已开支"));
			billVO.setTaxMaterialAmount(rs.getString("ITEM_材料含税总额"));
			billVO.setTaxMaterialExp(rs.getString("ITEM_含税材料已开支"));
			billVO.setMaterialSum(rs.getString("ITEM_材料合同总计"));
			billVO.setMaterialPaySum(rs.getString("ITEM_材料已开支总额"));
			billVO.setConstructionOneSum(rs.getString("ITEM_施工费1总额"));
			billVO.setConstructionOnePay(rs.getString("ITEM_施工费1已开支"));
			billVO.setConstructionTwoSum(rs.getString("ITEM_施工费2总额"));
			billVO.setConstructionTwoPay(rs.getString("ITEM_施工费2已开支"));
//			billVO.setFinancialDifSum(rs.getString("ITEM_财务费用差额总额"));
//			billVO.setFinancialSum(rs.getString("ITEM_财务费用总额"));
			billVO.setBiddingSum(rs.getString("ITEM_招投标费用总额"));
			billVO.setBiddingPay(rs.getString("ITEM_招投标费用开支"));
			billVO.setSporadicSum(rs.getString("ITEM_零星费用总额"));
			billVO.setSporadicPay(rs.getString("ITEM_零星费用开支"));
			billVO.setCostASum(rs.getString("ITEM_费用小计A总额"));
			billVO.setCostAPay(rs.getString("ITEM_费用小计A开支"));
			billVO.setcDiff(rs.getString("ITEM_合同差额"));
			billVO.setTaxNotDiff(rs.getString("ITEM_不含税差额"));
			billVO.setMaterialSumDiff(rs.getString("ITEM_材料总计差额"));
			billVO.setConstructionOneDiff(rs.getString("ITEM_施工费1差额"));
			billVO.setConstructionTwoDiff(rs.getString("ITEM_施工费2差额"));
			billVO.setTaxDiff(rs.getString("ITEM_含税差额"));
			billVO.setCostOneDiff(rs.getString("ITEM_费用小计1差额"));
			billVO.setEntertainmentCost(rs.getString("ITEM_招待费用金额"));
			billVO.setEntertainmentPay(rs.getString("ITEM_招待费用开支"));
			billVO.setLumpCost(rs.getString("ITEM_包干业务费用金额"));
			billVO.setLumpPay(rs.getString("ITEM_包干业务费用开支"));
			billVO.setDividedCost(rs.getString("ITEM_分成业务费用金额"));
			billVO.setDividedPay(rs.getString("ITEM_分成业务费用开支"));
			billVO.setProcessCost(rs.getString("ITEM_过程业务费用金额"));
			billVO.setProcessPay(rs.getString("ITEM_过程业务费用开支"));
			billVO.setBiddingDiff(rs.getString("ITEM_招投标费用差额"));
			billVO.setSporadicDiff(rs.getString("ITEM_零星费用差额"));
			billVO.setHospitalityDiff(rs.getString("ITEM_招待费用差额"));
			billVO.setLumpDiff(rs.getString("ITEM_包干业务费用差额"));
			billVO.setDividedDiff(rs.getString("ITEM_分成业务费用差额"));
			billVO.setProcessDiff(rs.getString("ITEM_过程业务费用差额"));
			billVO.setCostBSum(rs.getString("ITEM_费用小计B总额"));
			billVO.setCostBPay(rs.getString("ITEM_费用小计B开支"));
			billVO.setCostBDiff(rs.getString("ITEM_费用小计B差额"));
			billVO.setCostTotal(rs.getString("ITEM_总成本"));
			billVO.setPayTotal(rs.getString("ITEM_总开支"));
			billVO.setDiffTotal(rs.getString("ITEM_总差额"));
			billVO.setGrossProfitSum(rs.getString("ITEM_项目总毛利润"));
			billVO.setGrossProfitCurr(rs.getString("ITEM_当前毛利"));
			billVO.setPendPay(rs.getString("ITEM_待付款"));
			billVO.setFundAfterExpen(rs.getString("ITEM_减开支后资金"));
			billVO.setMarginSum(rs.getString("ITEM_保证金占用金额"));
			billVO.setMarginPay(rs.getString("ITEM_保证金占用开支"));
			billVO.setMarginDiff(rs.getString("ITEM_保证金占用差额"));
			billVO.setOtherSum(rs.getString("ITEM_其他占用金额"));
			billVO.setOtherPay(rs.getString("ITEM_其他占用开支"));
			billVO.setOtherDiff(rs.getString("ITEM_其他占用差额"));
			billVO.setOccupationSum(rs.getString("ITEM_占用合计金额"));
			billVO.setOccupationPay(rs.getString("ITEM_占用合计开支"));
			billVO.setOccupationDiff(rs.getString("ITEM_占用合计差额"));
			billVO.setOccupationAfter(rs.getString("ITEM_减占用后资金"));
			
			//新增字段
			billVO.setTaxDiffSumCW(rs.getString("ITEM_税金差额总额"));
			billVO.setTaxDiffPayCW(rs.getString("ITEM_税金差额支出"));
			billVO.setTaxDiffCW(rs.getString("ITEM_税金差额"));
			billVO.setTaxSum(rs.getString("ITEM_税金总额"));
			billVO.setTaxSumPay(rs.getString("ITEM_税金总额支出"));
			billVO.setTaxSumDiff(rs.getString("ITEM_税金总额差额"));
			billVO.setTaxManagerSum(rs.getString("ITEM_税金管理费总额"));
			billVO.setTaxManagerPay(rs.getString("ITEM_税金管理费支出"));
			billVO.setTaxManagerDiff(rs.getString("ITEM_税金管理费差额"));
			billVO.setOtherOneSum(rs.getString("ITEM_其他费用1总额"));
			billVO.setOtherOnePay(rs.getString("ITEM_其他费用1支出"));
			billVO.setOtherOneDiff(rs.getString("ITEM_其他费用1差额"));
			billVO.setOtherTwoSum(rs.getString("ITEM_其他费用2"));
			billVO.setOtherTwoPay(rs.getString("ITEM_其他费用2支出"));
			billVO.setOtherTwoDiff(rs.getString("ITEM_其他费用2差额"));
			
			billVO.setMaintenanceSum(rs.getString("ITEM_维护费总额"));
			billVO.setMaintenancePay(rs.getString("ITEM_维护费开支"));
			billVO.setMaintenanceDiff(rs.getString("ITEM_维护费差额"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return billVO;
	}
	
}
