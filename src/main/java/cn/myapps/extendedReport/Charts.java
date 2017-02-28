package cn.myapps.extendedReport;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import cn.myapps.core.counter.ejb.CounterProcessBean;
import cn.myapps.util.DateUtil;

public class Charts {

	public Charts() {
	}
	
	public JSONArray getDatas() throws UnsupportedEncodingException {
		JSONArray json;
		String sql;
		HttpServletRequest request = ServletActionContext.getRequest();
		String chartType = request.getParameter("chartType");
		String _name = request.getParameter("_name");
		String startTime = request.getParameter("_startTime");
		startTime = StringUtils.isBlank(startTime) ? "2000-01-01" : startTime;
		String endTime = request.getParameter("_endTime");
		endTime = StringUtils.isBlank(endTime) ? "2080-01-01" : endTime;
		String sub = request.getParameter("_sub");
		String jname = request.getParameter("_jname");
		String fullname = request.getParameter("_fullname");
		String deptmentSel = request.getParameter("_deptmentSel");
		String tradertypeSel = request.getParameter("_tradertypeSel");
		String areanameSel = request.getParameter("_areanameSel");
		String code = request.getParameter("_code");
		String erpname = request.getParameter("_empname");
		String gdcood = request.getParameter("_gdcood");
		String gdname = request.getParameter("_gdname");
		String gdspec = request.getParameter("_gdspec");
		String stname = request.getParameter("_stname");
		String unitname = request.getParameter("_unitname");
		String clientname = request.getParameter("clientname");
		String payType = request.getParameter("payType");

		json = null;
		sql = "";
		if ("bestSelling".equals(chartType)) {
			if (_name == null)
				sql = (new StringBuilder(
						"select top 20 GDTYPE,SUM(OQT) AS OQT,SUM(OAM) AS OAM,SUM(SQT) AS SQT,SUM(SAM) AS SAM,SUM(BQT) AS BQT,SUM(BAM) AS BAM,SUM(CAM) AS CAM,SUM(CBAM) AS CBAM from bi_sumgoods('"))
						.append(startTime).append("','").append(endTime)
						.append("') GROUP BY GDTYPE order by oam desc")
						.toString();
			else
				sql = (new StringBuilder(
						"SELECT GDTYPE,GCODE,GNAME,SPEC,UNIT,SUM(OQT) AS OQT,SUM(OAM) AS OAM,SUM(SQT) AS SQT,SUM(SAM) AS SAM,SUM(BQT) AS BQT,SUM(BAM) AS BAM,SUM(CAM) AS CAM,SUM(CBAM) AS CBAM from bi_sumgoods('"))
						.append(startTime).append("','").append(endTime)
						.append("')").append(" WHERE GDTYPE ='").append(_name)
						.append("' ")
						.append(" GROUP BY GDTYPE,GCODE,GNAME,SPEC,UNIT")
						.toString();
		} else if (!"cashBanTrend".equals(chartType))
			if ("goodsonhand".equals(chartType)) {
				if (_name == null)
					sql = "SELECT STNAME,SUM(ONHANDQTY) AS ONHANDQTY FROM BI_GOODSONHAND()  GROUP BY STNAME";
			} else if ("productType".equals(chartType)) {
				if (_name == null)
					sql = "SELECT TOP 20 GDTYPE,SUM(ONHANDQTY) AS ONHANDQTY FROM BI_GOODSONHAND() GROUP BY GDTYPE  ORDER BY ONHANDQTY DESC";
			} else if (!"subjectBalance".equals(chartType))
				if ("sumDep".equals(chartType)) {
					if (_name == null)
						sql = (new StringBuilder(
								"SELECT DEPTNAME,SUM(OQT) AS OQT,SUM(OAM) AS OAM,SUM(SQT) AS SQT,SUM(SAM) AS SAM,SUM(BQT) AS BQT,SUM(BAM) AS BAM,SUM(CAM) AS CAM,SUM(CBAM) AS CBAM  FROM  BI_SUMDEP('"))
								.append(startTime).append("','")
								.append(endTime).append("') ")
								.append("GROUP BY DEPTNAME").toString();
					else
						sql = (new StringBuilder(
								"SELECT BILLDATE,DEPTNAME,SUM(OQT) AS OQT,SUM(OAM) AS OAM,SUM(SQT) AS SQT, SUM(SAM) AS SAM,SUM(BQT) AS BQT,SUM(BAM) AS BAM,SUM(CAM) AS CAM,SUM(CBAM) AS CBAM FROM  BI_SUMDEP('"))
								.append(startTime).append("','")
								.append(endTime).append("') ")
								.append(" WHERE DEPTNAME ='").append(_name)
								.append("'  GROUP BY BILLDATE,DEPTNAME")
								.toString();
				} else if ("sumRep".equals(chartType)) {
					if (_name == null)
						sql = (new StringBuilder(
								"select REPNAME,SUM(OQT) AS OQT,SUM(OAM) AS OAM,SUM(SQT) AS SQT,SUM(SAM) AS SAM,SUM(BQT) AS BQT,SUM(BAM) AS BAM,SUM(CAM) AS CAM,SUM(CBAM) AS CBAM,SUM(OAM-CBAM) UOAM from dbo.BI_SUMREP('"))
								.append(startTime).append("','")
								.append(endTime).append("')  GROUP BY REPNAME")
								.toString();
					else
						sql = (new StringBuilder(
								"select REPNAME,BILLDATE,SUM(OQT) AS OQT,SUM(OAM) AS OAM,SUM(SQT) AS SQT,SUM(SAM) AS SAM,SUM(BQT) AS BQT,SUM(BAM) AS BAM,SUM(CAM) AS CAM,SUM(CBAM) AS CBAM,SUM(OAM-CBAM) UOAM from BI_SUMREP('"))
								.append(startTime).append("','")
								.append(endTime).append("') ")
								.append(" WHERE REPNAME = '").append(_name)
								.append("' GROUP BY REPNAME,BILLDATE")
								.toString();
				} else if ("sumTrader".equals(chartType)) {
					if (_name == null)
						sql = (new StringBuilder(
								"select TOP 20 TRADERNAME,SUM(OQT) AS OQT,SUM(OAM) AS OAM,SUM(SQT) AS SQT,SUM(SAM) AS SAM,SUM(BQT) AS BQT,SUM(BAM) AS BAM,SUM(CAM) AS CAM,SUM(CBAM) AS CBAM,SUM(OAM-CBAM) UOAM  from BI_SUMTRADER('"))
								.append(startTime)
								.append("','")
								.append(endTime)
								.append("')  GROUP BY TRADERNAME ORDER BY OAM DESC")
								.toString();
					else
						sql = (new StringBuilder(
								"select TRADERNAME,BILLDATE,SUM(OQT) AS OQT,SUM(OAM) AS OAM,SUM(SQT) AS SQT,SUM(SAM) AS SAM,SUM(BQT) AS BQT,SUM(BAM) AS BAM,SUM(CAM) AS CAM,SUM(CBAM) AS CBAM,SUM(CBAM)-SUM(OAM) AS UCBAM  from BI_SUMTRADER('"))
								.append(startTime).append("','")
								.append(endTime)
								.append("') where TRADERNAME = '")
								.append(_name)
								.append("'  GROUP BY TRADERNAME,BILLDATE")
								.toString();
				} else if ("supplier".equals(chartType)) {
					if (_name == null)
						sql = (new StringBuilder(
								"select TOP 20 TRADERNAME,SUM(OQT) AS OQT,SUM(OAM) AS OAM,SUM(RQT) AS RQT,SUM(RAM) AS RAM,SUM(BQT) AS BQT,SUM(BAM) AS BAM,SUM(CAM) AS CAM,SUM(CBAM) AS CBAM,SUM(OAM-CBAM) AS UOAM  from BI_SUMVENDOR('"))
								.append(startTime)
								.append("','")
								.append(endTime)
								.append("')  GROUP BY TRADERNAME ORDER BY OAM DESC")
								.toString();
					else
						sql = (new StringBuilder(
								"select  BILLDATE,TRADERNAME,SUM(OQT) AS OQT,SUM(OAM) AS OAM,SUM(RQT) AS RQT,SUM(RAM) AS RAM, SUM(BQT) AS BQT,SUM(BAM) AS BAM,SUM(CAM) AS CAM,SUM(CBAM) AS CBAM, SUM(OAM-CBAM) UOAM from BI_SUMVENDOR('"))
								.append(startTime).append("','")
								.append(endTime).append("')")
								.append(" WHERE TRADERNAME = '").append(_name)
								.append("'    GROUP BY BILLDATE,TRADERNAME")
								.toString();
				} else if ("wareHouse".equals(chartType)) {
					if (_name == null)
						sql = (new StringBuilder(
								"select ST,SUM(CQT) AS CQT from BI_SUMGOODSONHAND('"))
								.append(startTime).append("','")
								.append(endTime)
								.append("') where CQT >0 GROUP BY ST")
								.toString();
				} else if ("wldw".equals(chartType)) {
					if (_name == null)
						sql = "SELECT * FROM BI_L_TRADER() ORDER BY CODE";
					else
						sql = (new StringBuilder(
								"SELECT * FROM BI_L_TRADER() WHERE CODE='"))
								.append(_name).append("'").toString();
				} else if ("fckkc".equals(chartType)) {
					if (_name == null)
						sql = "SELECT GDCODE,GDNAME,GDSPEC,GDTYPE,STNAME,ONHANDQTY,UNITNAME,TOPQTY,DOWNQTY FROM BI_GOODSONHAND () ORDER BY GDCODE,STNAME";
					else
						sql = (new StringBuilder(
								"SELECT GDCODE,GDNAME,GDSPEC,GDTYPE,STNAME,ONHANDQTY,TOPQTY,DOWNQTY,UNITNAME FROM BI_GOODSONHAND () WHERE GDCODE = '"))
								.append(_name)
								.append("' ORDER BY GDCODE,STNAME").toString();
				} else if ("huopin".equals(chartType)) {
					if (_name == null)
						sql = "SELECT * FROM BI_L_GOODS() ORDER BY CODE";
					else
						sql = (new StringBuilder(
								"SELECT * FROM BI_L_GOODS() WHERE CODE='"))
								.append(_name).append("'").toString();
				} else if ("cnz".equals(chartType)) {
					if (_name == null)
						sql = "select * from  BI_AC_ACCOUNT";
					else
						sql = (new StringBuilder(
								"select * from  BI_AC_ACCOUNT WHERE ACCOUNTID='"))
								.append(_name).append("'").toString();
				} else if ("yings".equals(chartType)) {
					if (_name == null)
						sql = "SELECT CLIENTID AS TRADERID,CC AS CODE,CN AS NAME,CF AS FULLNAME,D AS DEPTNAME,SAM AS SAMT,IA AS INAMT,DA AS OUTAMT,MA AS CAMT FROM R_AR_ARSUM('2016-01-01','2016-01-01','2016-01-01') ORDER BY CC";
					else
						sql = (new StringBuilder(
								"SELECT CLIENTID AS TRADERID,CC AS CODE,CN AS NAME,CF AS FULLNAME,D AS DEPTNAME,SAM AS SAMT,IA AS INAMT,DA AS OUTAMT,MA AS CAMT FROM R_AR_ARSUM('2016-01-01','2016-01-01','2016-01-01') WHERE CC = '"))
								.append(_name).append("' ORDER BY CC")
								.toString();
				} else if ("yingf".equals(chartType))
					if (_name == null)
						sql = "SELECT VENDORID AS TRADERID,VC AS CODE,VN AS NAME,VF AS FULLNAME,D AS DEPTNAME,SA AS SAMT,IA AS INAMT,DA AS OUTAMT,MA AS CAMT FROM R_Ap_APSUM('2016-01-01','2016-01-01','2016-01-01') ORDER BY VC";
					else
						sql = (new StringBuilder(
								"SELECT VENDORID AS TRADERID,VC AS CODE,VN AS NAME,VF AS FULLNAME,D AS DEPTNAME,SA AS SAMT,IA AS INAMT,DA AS OUTAMT,MA AS CAMT FROM R_Ap_APSUM('2016-01-01','2016-01-01','2016-01-01')  WHERE VC ='"))
								.append(_name).append("' ORDER BY VC")
								.toString();
		if ("wldw".equals(chartType) && "wlsearch".equals(sub)) {
			sql = "SELECT * FROM BI_L_TRADER() WHERE  CODE IS NOT NULL";
			if (jname != "")
				sql = (new StringBuilder(String.valueOf(sql)))
						.append(" AND NAME LIKE'%").append(jname).append("%'")
						.toString();
			if (fullname != "")
				sql = (new StringBuilder(String.valueOf(sql)))
						.append(" AND FULLNAME LIKE'%").append(fullname)
						.append("%'").toString();
			if (deptmentSel != "")
				sql = (new StringBuilder(String.valueOf(sql)))
						.append(" AND DEPNAME LIKE'%").append(deptmentSel)
						.append("%'").toString();
			if (tradertypeSel != "")
				sql = (new StringBuilder(String.valueOf(sql)))
						.append(" AND TRADERTYPE LIKE'%").append(tradertypeSel)
						.append("%'").toString();
			if (areanameSel != "")
				sql = (new StringBuilder(String.valueOf(sql)))
						.append(" AND NAME LIKE'%").append(areanameSel)
						.append("%'").toString();
			if (erpname != "")
				sql = (new StringBuilder(String.valueOf(sql)))
						.append(" AND EMPNAME LIKE'%").append(erpname)
						.append("%'").toString();
			if (code != "")
				sql = (new StringBuilder(String.valueOf(sql)))
						.append(" AND CODE  LIKE'%").append(code).append("%'")
						.toString();
		}
		if ("fckkc".equals(chartType) && "cksearch".equals(sub)) {
			sql = "SELECT * FROM  BI_GOODSONHAND() WHERE  GDCODE IS NOT NULL ";
			if (gdcood != "" && gdcood.length() > 0)
				sql = (new StringBuilder(String.valueOf(sql)))
						.append(" AND GDCOOD LIKE '%").append(gdcood)
						.append("%'").toString();
			if (gdname != "" && gdname.length() > 0)
				sql = (new StringBuilder(String.valueOf(sql)))
						.append(" AND GDNAME LIKE '%").append(gdname)
						.append("%'").toString();
			if (gdspec != "" && gdspec.length() > 0)
				sql = (new StringBuilder(String.valueOf(sql)))
						.append(" AND GDSPEC LIKE '%").append(gdspec)
						.append("%'").toString();
			if (stname != "" && stname.length() > 0)
				sql = (new StringBuilder(String.valueOf(sql)))
						.append(" AND STNAME LIKE '%").append(stname)
						.append("%'").toString();
			if (unitname != "" && unitname.length() > 0)
				sql = (new StringBuilder(String.valueOf(sql)))
						.append(" AND UNITNAME LIKE '%").append(unitname)
						.append("%'").toString();
		}
		if ("huopin".equals(chartType) && "hpsearch".equals(sub)) {
			sql = "SELECT * FROM  BI_L_GOODS() WHERE  CODE IS NOT NULL ";
			if (jname != "" && jname.length() > 0)
				sql = (new StringBuilder(String.valueOf(sql)))
						.append(" AND NAME LIKE '%").append(jname).append("%'")
						.toString();
			if (fullname != "" && fullname.length() > 0)
				sql = (new StringBuilder(String.valueOf(sql)))
						.append(" AND NAME LIKE '%").append(fullname)
						.append("%'").toString();
			if (deptmentSel != "" && deptmentSel.length() > 0)
				sql = (new StringBuilder(String.valueOf(sql)))
						.append(" AND DEPNAME  LIKE '%").append(deptmentSel)
						.append("%'").toString();
			if (gdspec != "" && gdspec.length() > 0)
				sql = (new StringBuilder(String.valueOf(sql)))
						.append(" AND SPEC  LIKE '%").append(gdspec)
						.append("%'").toString();
			if (code != "" && code.length() > 0)
				sql = (new StringBuilder(String.valueOf(sql)))
						.append(" AND CODE  LIKE '%").append(code).append("%'")
						.toString();
		}
		if ("cnz".equals(chartType) && "cnzsearch".equals(sub))
			if (code != "" && code.length() > 0)
				sql = (new StringBuilder(
						"select BOOKDATE,CREDNO,CREDTYPE,BRIEF,SUBJECTCODE,TAKEMAN,CREDITAMT,DEBITAMT,BALANCE from  BI_AC_DayBook_DETAIL("))
						.append(code).append(",'").append(startTime)
						.append("','").append(endTime).append("')").toString();
			else
				sql = "select * from  BI_AC_ACCOUNT";
		if ("yings".equals(chartType) && "yingssearch".equals(sub)) {
			sql = (new StringBuilder(
					"SELECT CLIENTID AS TRADERID,CC AS CODE,CN AS NAME,CF AS FULLNAME,D AS DEPTNAME,SAM AS SAMT,IA AS INAMT,DA AS OUTAMT,MA AS CAMT FROM R_AR_ARSUM('"))
					.append(startTime).append("','").append(endTime)
					.append("','").append(endTime).append("')").toString();
			if (clientname != "" && clientname.length() > 0)
				sql = (new StringBuilder(String.valueOf(sql)))
						.append(" WHERE CC = '").append(clientname).append("'")
						.toString();
			sql = (new StringBuilder(String.valueOf(sql))).append(
					" ORDER BY CC").toString();
		}
		if ("yingf".equals(chartType) && "yingfsearch".equals(sub)) {
			sql = (new StringBuilder(
					"SELECT VENDORID AS TRADERID,VC AS CODE,VN AS NAME,VF AS FULLNAME,D AS DEPTNAME,SA AS SAMT,IA AS INAMT,DA AS OUTAMT,MA AS CAMT FROM R_Ap_APSUM('"))
					.append(startTime).append("','").append(endTime)
					.append("','").append(endTime).append("')").toString();
			if (clientname != "" && clientname.length() > 0)
				sql = (new StringBuilder(String.valueOf(sql)))
						.append(" WHERE VC ='").append(clientname).append("' ")
						.toString();
			sql = (new StringBuilder(String.valueOf(sql))).append(
					" ORDER BY VC").toString();
		}
		
		if ("yingsdetail".equals(payType))
			sql = (new StringBuilder(
					"SELECT CLIENTID AS TRADERID,BD AS BILLDATE,BC AS BILLCODE,CASE  WHEN CA>0 THEN '\u6536\u5165'  ELSE '\u652F\u51FA' END AS IOTYPE,CA AS SAMT,IA AS INAMT,DA AS OUTAMT, MA AS CAMT FROM R_AR_ARDETAIL('2016-01-01','2016-01-01','2016-01-01') WHERE CC = '"))
					.append(_name).append("' ORDER BY BD").toString();
		if ("yingfdetail".equals(payType))
			sql = (new StringBuilder(
					"SELECT VENDORID AS TRADERID,BD AS BILLDATE,BC AS BILLCODE, CASE  WHEN CA>0 THEN '\u5E94\u4ED8'  ELSE '\u5DF2\u4ED8' END AS IOTYPE,CA AS SAMT,IA AS INAMT,DA AS OUTAMT, MA AS MA FROM R_AP_APDETAIL('2016-01-01','2016-01-01','2016-01-01') WHERE VC = '"))
					.append(_name).append("' ORDER BY BD").toString();

		json = queryData(sql);
		return json;
	}
	

	/**
	 * 订单选项查询
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public JSONArray getDatas4OrderOptions() throws UnsupportedEncodingException {
		JSONArray json = null;
		String sql = "";
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String name = request.getParameter("name");

		if ("vendor".equals(name))	//供应商查询
			sql = "SELECT CODE as VENDORNO, NAME FROM l_trader";
		
		if ("deptname".equals(name))	//部门查询
			sql = "SELECT NAME FROM  L_DEPARTMENT";
		
		if ("empname".equals(name))				//业务员查询
			sql = "SELECT CODE,NAME FROM L_EMPLOY";
		
		if ("tradertype".equals(name))	//类别查询
			sql = "SELECT NAME FROM  L_TRADERTYPE";
		
		if ("areaname".equals(name))	//地区查询
			sql = "SELECT  NAME FROM L_AREA  ORDER BY LCODE";
		
		if ("goodsname".equals(name))			//产品查询
			sql = "SELECT top 100 CODE AS GOODSCODE, NAME, SPEC, PPRICE, UNIT FROM L_GOODS where spec is not null ORDER  BY CODE";
		
		if ("accoutname".equals(name))		//银行出纳账户查询
			sql = "select ACCOUNTID, NAME  from ac_account AC";
		
		if ("bankdetail".equals(name))			//出纳账户详细资料
			sql = (new StringBuilder(
					"select BOOKDATE,CREDNO,CREDTYPE,BRIEF,SUBJECTCODE,TAKEMAN,CREDITAMT,DEBITAMT,BALANCE from  BI_AC_DayBook_DETAIL("))
					.append(name).append(",'2001-01-01','2016-01-01')")
					.toString();
		
		if ("clientname".equals(name))		//客户
			sql = "SELECT CODE,NAME FROM  L_TRADER";
		
		json = queryData(sql);
		return json;
	}
	/**
	 * 查询
	 * @return
	 */
	public JSONArray orderQuery(){
		JSONArray json = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		String orderType = request.getParameter("orderType");	//类型--采购或销售,purchase/sale
		String active = request.getParameter("active");			//操作类型--插入/更新/删除，insert/update/delete
		String subActive = request.getParameter("subActive");	//子表操作类型--插入/更新/删除，insert/update/delete

		String pBillcode = request.getParameter("pBillcode");	//主表billcode
		String pBillid = request.getParameter("pBillid");	//主表billid
		
		//查询条件参数
		String startTime = request.getParameter("_startTime");	//开始时间
		startTime = StringUtils.isBlank(startTime) ? "2000-01-01" : startTime;
		String endTime = request.getParameter("_endTime");
		endTime = StringUtils.isBlank(endTime) ? "2080-01-01" : endTime;
		String deptname = request.getParameter("deptname");
		String empname = request.getParameter("empname");
		String orderNum = request.getParameter("orderNum");
		String vendor = request.getParameter("vendor");

		// 销售订单
		String clientno = request.getParameter("clientno");
		String sql = "";
		
		if ("purchase".equals(orderType)) { // 采购订单
			if (subActive == null){	//主表
				if("search".equals(active)){
					sql = "select BILLID,BILLCODE,BILLDATE,REPID,EMPID,EMPNAME,DEPID,DEPNAME,VENDORID,VENDORNAME,WUSERNO,WUSERNAME,BILLAMT,REMARK from BI_P_ORDER where BILLID IS NOT NULL";
					if (orderNum != "" && orderNum.length() > 0)
						sql = (new StringBuilder(String.valueOf(sql)))
								.append(" AND BILLCODE LIKE '%")
								.append(orderNum).append("%'").toString();
					if (vendor != "" && vendor.length() > 0)
						sql = (new StringBuilder(String.valueOf(sql)))
								.append(" AND VENDORNAME LIKE '%")
								.append(vendor).append("%'").toString();
					if (deptname != "" && deptname.length() > 0)
						sql = (new StringBuilder(String.valueOf(sql)))
								.append(" AND DEPNAME LIKE '%")
								.append(deptname).append("%'").toString();
					if (empname != "" && empname.length() > 0)
						sql = (new StringBuilder(String.valueOf(sql)))
								.append(" AND EMPNAME LIKE '%")
								.append(empname).append("%'").toString();
					sql = (new StringBuilder(String.valueOf(sql)))
							.append(" AND BILLDATE > '").append(startTime)
							.append("' AND BILLDATE < '").append(endTime)
							.append("'").toString();
				} else if ("".equals(active))
					if (pBillid == null)
						sql = "select BILLID,BILLCODE,BILLDATE,REPID,EMPID,EMPNAME,DEPID,DEPNAME,VENDORID,VENDORCODE,VENDORNAME,WUSERNO,WUSERNAME,BILLAMT,REMARK from BI_P_ORDER order by BILLID";
					else
						sql = (new StringBuilder(
								"select BILLID,BILLCODE,BILLDATE,REPID,EMPID,EMPNAME,DEPID,DEPNAME,VENDORID,VENDORCODE,VENDORNAME,WUSERNO,WUSERNAME,BILLAMT,REMARK from BI_P_ORDER where BILLID = '"))
								.append(pBillid).append("'").toString();
			}else{	//子表
				if (pBillcode != null)
					sql = (new StringBuilder(
							"exec BI_P_ORDERD_SELECT '")).append(pBillcode)
							.append("'").toString();
			}
		}else if("sale".equals(orderType)){
			if (subActive == null){	//主表
				if ("search".equals(active)) {
					sql = "select BILLID,BILLCODE,BILLDATE,REPID,EMPID,CLIENTID,CLIENTNAME,DEPID,DEPNAME,WUSERNO,WUSERNAME,BILLAMT,REMARK from BI_S_ORDER where BILLID IS NOT NULL";
					if (orderNum != "" && orderNum.length() > 0)
						sql = (new StringBuilder(String.valueOf(sql)))
								.append(" AND BILLCODE LIKE '%")
								.append(orderNum).append("%'").toString();
					if (clientno != null && !"".equals(clientno)
							&& clientno.length() > 0)
						sql = (new StringBuilder(String.valueOf(sql)))
								.append(" AND clientid LIKE '%")
								.append(clientno).append("%'").toString();
					if (deptname != "" && deptname.length() > 0)
						sql = (new StringBuilder(String.valueOf(sql)))
								.append(" AND DEPNAME LIKE '%")
								.append(deptname).append("%'").toString();
					if (empname != "" && empname.length() > 0)
						sql = (new StringBuilder(String.valueOf(sql)))
								.append(" AND EMPID LIKE '%")
								.append(empname).append("%'").toString();
					sql = (new StringBuilder(String.valueOf(sql)))
							.append(" AND BILLDATE > '").append(startTime)
							.append("' AND BILLDATE < '").append(endTime)
							.append("'").toString();
				} else if ("".equals(active)){
					if (pBillid == null)
						sql = "SELECT BILLID,BILLCODE,BILLDATE,REPID,REPNAME,EMPID,CLIENTID,CLIENTNAME,DEPID,DEPNAME,WUSERNO,WUSERNAME,BILLAMT,REMARK FROM  BI_S_ORDER";
					else
						sql = (new StringBuilder(
								"SELECT BILLID,BILLCODE,BILLDATE,REPID,REPNAME,EMPID,CLIENTID,CLIENTNAME,DEPID,DEPNAME,WUSERNO,WUSERNAME,BILLAMT,REMARK FROM  BI_S_ORDER where BILLID = '"))
								.append(pBillid).append("'").toString();
				}
			}else{	//子表
				if (pBillcode != null)
					sql = (new StringBuilder(
							"exec BI_S_ORDERD_SELECT '")).append(pBillcode)
							.append("'").toString();
			}
		}
		try {
			json = queryData(sql);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public String countNext2(String headText, boolean isYear, boolean isMonth, boolean isDay, int digit) throws ParseException, Exception {
		CounterProcessBean process = new CounterProcessBean("erporder");
		String countLabel = headText;
		
		if (isYear) {
			countLabel += DateUtil.format(DateUtil.getToday(), "yy");
		}
		if (isMonth) {
			countLabel += DateUtil.format(DateUtil.getToday(), "MM");
		}
		if (isDay) {
			countLabel += DateUtil.format(DateUtil.getToday(), "dd");
		}
		
		int count = process.getNextValue(countLabel, "erporder", "erporder");
		String val = "";
		
		if (count < 10) {
			for (int temp = 1; temp <= digit - 1; temp++) {
				val += "0";
			}
			val += count;
		} else if (count < 100) {
			for (int temp = 1; temp <= digit - 2; temp++) {
				val += "0";
			}
			val += count;
		} else if (count < 1000) {
			for (int temp = 1; temp <= digit - 3; temp++) {
				val += "0";
			}
			val += count;
		} else if (count < 10000) {
			for (int temp = 1; temp <= digit - 4; temp++) {
				val += "0";
			}
			val += count;
		} else if (count < 100000) {
			for (int temp = 1; temp <= digit - 5; temp++) {
				val += "0";
			}
			val += count;
		} else if (count < 1000000) {
			for (int temp = 1; temp <= digit - 6; temp++) {
				val += "0";
			}
			val += count;
		} else if (count < 10000000) {
			for (int temp = 1; temp <= digit - 7; temp++) {
				val += "0";
			}
			val += count;
		} else
			val += count;
		
		String retvar = countLabel + val;
		return retvar;
	}
	/**
	 * 插入
	 * @return
	 * @throws Exception 
	 * @throws ParseException 
	 */
	public String orderInsert() throws ParseException, Exception{
		String rtn = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		String orderType = request.getParameter("orderType");	//类型--采购或销售,purchase/sale
		String subActive = request.getParameter("subActive");	//子表操作类型--插入/更新/删除，insert/update/delete
		
		//--子表
		String orderBillDate = request.getParameter("order.BILLDATE");
		String subITEMNO = request.getParameter("sub.ITEMNO");
		String subGOODSCODE = request.getParameter("sub.GOODSCODE");
		String subQTY = request.getParameter("sub.QTY");
		String subUNITNAME = request.getParameter("sub.UNITNAME");
		String subPRICE = request.getParameter("sub.PRICE");
		
		//查询条件参数
		String startTime = request.getParameter("_startTime");	//开始时间
		startTime = StringUtils.isBlank(startTime) ? "2000-01-01" : startTime;
		String endTime = request.getParameter("_endTime");
		endTime = StringUtils.isBlank(endTime) ? "2080-01-01" : endTime;
		String deptname = request.getParameter("deptname");
		String empname = request.getParameter("empname");
		String remark = request.getParameter("remark");
		String vendorCode = request.getParameter("vendorcode");

		// 销售订单
		String clientno = request.getParameter("clientno");
		String wusername = request.getParameter("WUSERNAME");

		String pBillcode = request.getParameter("BILLCODE");
//		request.getParameter("BILLCODE");
//		String pBillcode = countNext2("PO-", true, true, true, 4);
		
		
		String sql = "";

		if ("purchase".equals(orderType)) { // 采购订单
			if (subActive == null){	//主表
				sql = (new StringBuilder("exec BI_P_ORDER_INSERT '"))
						.append(deptname).append("','")
						.append(vendorCode).append("','")
						.append(pBillcode).append("','")
						.append(orderBillDate).append("','")
						.append(remark).append("','").append(empname)
						.append("'").toString();
			}else{//子表
				sql = (new StringBuilder(
						"exec BI_P_ORDERD_INSERT '")).append(pBillcode)
						.append("',").append(subITEMNO)
						.append(",'").append(subGOODSCODE)
						.append("','").append(subUNITNAME)
						.append("',").append(subQTY)
						.append(",").append(subPRICE).toString();
			}
		}else if("sale".equals(orderType)){
			if (subActive == null){	//主表
				sql = (new StringBuilder("exec BI_S_ORDER_INSERT '"))
						.append(deptname).append("','")
						.append(clientno).append("','")
						.append(pBillcode).append("','")
						.append(orderBillDate).append("','")
						.append(remark).append("','").append(wusername)
						.append("'").toString();
			}else{//子表
				sql = (new StringBuilder(
						"exec BI_S_ORDERD_INSERT '")).append(pBillcode)
						.append("',").append(subITEMNO)
						.append(",'").append(subGOODSCODE)
						.append("','").append(subUNITNAME)
						.append("',").append(subQTY)
						.append(",").append(subPRICE).toString();
			}
		}
		
		rtn = updateData(sql);
		return rtn;
	}

	/**
	 * 更新
	 * @return
	 */
	public String orderUpdate(){
		String rtn = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		String orderType = request.getParameter("orderType");	//类型--采购或销售,purchase/sale
		String subActive = request.getParameter("subActive");	//子表操作类型--插入/更新/删除，insert/update/delete
		
		//--子表
		String orderBillDate = request.getParameter("order.BILLDATE");
		String subBillcode = request.getParameter("sub.BILLCODE");
		String subITEMNO = request.getParameter("sub.ITEMNO");
		String subGOODSCODE = request.getParameter("sub.GOODSCODE");
		String subQTY = request.getParameter("sub.QTY");
		String subUNITNAME = request.getParameter("sub.UNITNAME");
		String subPRICE = request.getParameter("sub.PRICE");
		
		//查询条件参数
		String startTime = request.getParameter("_startTime");	//开始时间
		startTime = StringUtils.isBlank(startTime) ? "2000-01-01" : startTime;
		String endTime = request.getParameter("_endTime");
		endTime = StringUtils.isBlank(endTime) ? "2080-01-01" : endTime;
		String deptname = request.getParameter("deptname");
		String empname = request.getParameter("empname");
		String remark = request.getParameter("remark");
		String orderNum = request.getParameter("orderNum");
		String vendorCode = request.getParameter("vendorcode");

		// 销售订单
		String clientno = request.getParameter("clientno");
		String pBillcode = request.getParameter("BILLCODE");
		String sql = "";
		if ("purchase".equals(orderType)) { // 采购订单
			if (subActive == null){	//主表
				sql = (new StringBuilder("exec BI_P_ORDER_UPDATE '"))
						.append(deptname).append("','")
						.append(vendorCode).append("','")
						.append(pBillcode).append("','")
						.append(orderBillDate).append("','")
						.append(remark).append("','")
						.append(empname).append("'").toString();
			}else{//子表
				sql = (new StringBuilder(
						"exec BI_P_ORDERD_UPDATE '")).append(subBillcode)
						.append("',").append(subITEMNO)
						.append(",'").append(subGOODSCODE)
						.append("','").append(subUNITNAME)
						.append("',").append(subQTY).append(",")
						.append(subPRICE).toString();
			}
		}else if("sale".equals(orderType)){
			if (subActive == null){	//主表
				sql = (new StringBuilder("exec BI_S_ORDER_UPDATE '"))
						.append(deptname).append("','")
						.append(clientno).append("','")
						.append(pBillcode).append("','")
						.append(orderBillDate).append("','")
						.append(remark).append("','")
						.append(empname).append("'")
						.toString();
			}else{//子表
				sql = (new StringBuilder("exec BI_S_ORDERD_UPDATE '"))
						.append(subBillcode).append("',").append(subITEMNO)
						.append(",'").append(subGOODSCODE)
						.append("','").append(subUNITNAME)
						.append("',").append(subQTY).append(",")
						.append(subPRICE).toString();
			}
		}
		rtn = updateData(sql);
		return rtn;
	}

	/**
	 * 删除
	 * @return
	 */
	public String orderDelete(){
		String rtn = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		String orderType = request.getParameter("orderType");	//类型--采购或销售,purchase/sale
		String subActive = request.getParameter("subActive");	//子表操作类型--插入/更新/删除，insert/update/delete
		//--子表
		String subBillcode = request.getParameter("sub.BILLCODE");
		String subITEMNO = request.getParameter("sub.ITEMNO");
		
		//查询条件参数
		String startTime = request.getParameter("_startTime");	//开始时间
		startTime = StringUtils.isBlank(startTime) ? "2000-01-01" : startTime;
		String endTime = request.getParameter("_endTime");
		endTime = StringUtils.isBlank(endTime) ? "2080-01-01" : endTime;
		String orderNum = request.getParameter("orderNum");

		String sql = "";
		if ("purchase".equals(orderType)) { // 采购订单
			if (subActive == null){
				sql = (new StringBuilder("exec BI_P_ORDER_DELETE '"))
						.append(orderNum).append("'").toString();
			}else{
				sql = (new StringBuilder(
						"exec BI_P_ORDERD_DELETE '")).append(subBillcode)
						.append("',").append(subITEMNO).toString();
			}
		}else if("sale".equals(orderType)){
			if (subActive == null){
				sql = (new StringBuilder("exec BI_S_ORDER_DELETE '"))
						.append(orderNum).append("'").toString();
			}else{
				sql = (new StringBuilder("exec BI_S_ORDERD_DELETE '"))
						.append(subBillcode).append("',").append(subITEMNO)
						.toString();
			}
		}
		rtn = updateData(sql);
		
		return rtn;
	}
	/**
	 * 执行更新sql语句
	 * @param sql
	 * @return
	 * @throws  
	 */
	public String updateData(String sql){
		String rtn = null;
		Connection conn;
		Statement stmt;
		try {
			conn = NDataSource.getConnection();
			stmt = conn.createStatement();
			if (!"".equals(sql)) {
				stmt.executeUpdate(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return rtn;
		}
		return "success";
	}
	
	/**
	 * 调用接口查询ERP数据
	 * @param sql
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public JSONArray queryData(String sql) throws UnsupportedEncodingException {

		JSONArray json = null;
		Connection conn;
		Statement stmt;
		
		conn = null;
		stmt = null;
		try {
			conn = NDataSource.getConnection();
			stmt = conn.createStatement();

			if (!"".equals(sql)) {
				ResultSet rs = stmt.executeQuery(sql);

				Collection<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				ResultSetMetaData meteDate = rs.getMetaData();
				int columnCount = meteDate.getColumnCount();
				while (rs.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					for (int i = 1; i <= columnCount; i++) {
						String columnLabel = meteDate.getColumnLabel(i);
						Object obj = rs.getObject(columnLabel);
						map.put(columnLabel, obj);
					}
					list.add(map);
				}

				rs.close();

				json = JSONArray.fromObject(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return json;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return json;
	}
}