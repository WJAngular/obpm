/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.model;

import java.util.Date;

/**
 * @ClassName: FingerDataVO
 * @Description: 指纹数据模型
 * @author: WUJING
 * @date :2016-08-14 上午10:22:50
 * 
 */
public class FingerDataVO extends ObjectVO {
	private String deparmentFinger;// ,[ITEM_部门]
	private String nameFinger;// ,[ITEM_姓名]
	private String numberFinger;// ,[ITEM_考勤号码]
	private String dateFinger;// ,[ITEM_日期时间]
	private String recorderFinger;// ,[ITEM_记录状态]
	private String machineFinger;// ,[ITEM_机器号]
	private Date extractDateFinger;// ,[ITEM_提取日期]
	private String extractTimeFinger;// ,[ITEM_提取时间]
	private String compareTypeFinger;// ,[ITEM_对比方式]
	private String statusFinger;// ,[ITEM_分析状态]
	private String weekDay;  //,[ITEM_星期]
	
	private String goRecorder ;//,[ITEM_上班状态]
	private String offRecorder ;//,[ITEM_下班状态]
	private String goTime ; //,[ITEM_上班时间]
	private String offTime;//,[ITEM_下班时间]
	private int scount ;//,[ITEM_应打卡次数]
	private int rcount;//,[ITEM_实际打卡次数]
	private int ncount;//,[ITEM_未打卡次数]
	
	public FingerDataVO() {
		super();
		// TODO 自动生成的构造函数存根
	}
	
	public FingerDataVO(String deparmentFinger, String nameFinger,
			String numberFinger, String dateFinger, String recorderFinger,
			String machineFinger, Date extractDateFinger,
			String extractTimeFinger, String compareTypeFinger,
			String statusFinger, String weekDay, String goRecorder,
			String offRecorder, String goTime, String offTime, int scount,
			int rcount, int ncount) {
		super();
		this.deparmentFinger = deparmentFinger;
		this.nameFinger = nameFinger;
		this.numberFinger = numberFinger;
		this.dateFinger = dateFinger;
		this.recorderFinger = recorderFinger;
		this.machineFinger = machineFinger;
		this.extractDateFinger = extractDateFinger;
		this.extractTimeFinger = extractTimeFinger;
		this.compareTypeFinger = compareTypeFinger;
		this.statusFinger = statusFinger;
		this.weekDay = weekDay;
		this.goRecorder = goRecorder;
		this.offRecorder = offRecorder;
		this.goTime = goTime;
		this.offTime = offTime;
		this.scount = scount;
		this.rcount = rcount;
		this.ncount = ncount;
	}

	/** 
	 * @return deparmentFinger 
	 */
	public String getDeparmentFinger() {
		return deparmentFinger;
	}

	/** 
	 * @param deparmentFinger 要设置的 deparmentFinger 
	 */
	public void setDeparmentFinger(String deparmentFinger) {
		this.deparmentFinger = deparmentFinger;
	}

	/** 
	 * @return nameFinger 
	 */
	public String getNameFinger() {
		return nameFinger;
	}

	/** 
	 * @param nameFinger 要设置的 nameFinger 
	 */
	public void setNameFinger(String nameFinger) {
		this.nameFinger = nameFinger;
	}

	/** 
	 * @return numberFinger 
	 */
	public String getNumberFinger() {
		return numberFinger;
	}

	/** 
	 * @param numberFinger 要设置的 numberFinger 
	 */
	public void setNumberFinger(String numberFinger) {
		this.numberFinger = numberFinger;
	}

	/** 
	 * @return dateFinger 
	 */
	public String getDateFinger() {
		return dateFinger;
	}

	/** 
	 * @param dateFinger 要设置的 dateFinger 
	 */
	public void setDateFinger(String dateFinger) {
		this.dateFinger = dateFinger;
	}

	/** 
	 * @return recorderFinger 
	 */
	public String getRecorderFinger() {
		return recorderFinger;
	}

	/** 
	 * @param recorderFinger 要设置的 recorderFinger 
	 */
	public void setRecorderFinger(String recorderFinger) {
		this.recorderFinger = recorderFinger;
	}

	/** 
	 * @return machineFinger 
	 */
	public String getMachineFinger() {
		return machineFinger;
	}

	/** 
	 * @param machineFinger 要设置的 machineFinger 
	 */
	public void setMachineFinger(String machineFinger) {
		this.machineFinger = machineFinger;
	}

	/** 
	 * @return extractDateFinger 
	 */
	public Date getExtractDateFinger() {
		return extractDateFinger;
	}

	/** 
	 * @param extractDateFinger 要设置的 extractDateFinger 
	 */
	public void setExtractDateFinger(Date extractDateFinger) {
		this.extractDateFinger = extractDateFinger;
	}

	/** 
	 * @return extractTimeFinger 
	 */
	public String getExtractTimeFinger() {
		return extractTimeFinger;
	}

	/** 
	 * @param extractTimeFinger 要设置的 extractTimeFinger 
	 */
	public void setExtractTimeFinger(String extractTimeFinger) {
		this.extractTimeFinger = extractTimeFinger;
	}

	/** 
	 * @return compareTypeFinger 
	 */
	public String getCompareTypeFinger() {
		return compareTypeFinger;
	}

	/** 
	 * @param compareTypeFinger 要设置的 compareTypeFinger 
	 */
	public void setCompareTypeFinger(String compareTypeFinger) {
		this.compareTypeFinger = compareTypeFinger;
	}

	/** 
	 * @return statusFinger 
	 */
	public String getStatusFinger() {
		return statusFinger;
	}

	/** 
	 * @param statusFinger 要设置的 statusFinger 
	 */
	public void setStatusFinger(String statusFinger) {
		this.statusFinger = statusFinger;
	}

	/** 
	 * @return weekDay 
	 */
	public String getWeekDay() {
		return weekDay;
	}

	/** 
	 * @param weekDay 要设置的 weekDay 
	 */
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

	/** 
	 * @return goRecorder 
	 */
	public String getGoRecorder() {
		return goRecorder;
	}

	/** 
	 * @param goRecorder 要设置的 goRecorder 
	 */
	public void setGoRecorder(String goRecorder) {
		this.goRecorder = goRecorder;
	}

	/** 
	 * @return offRecorder 
	 */
	public String getOffRecorder() {
		return offRecorder;
	}

	/** 
	 * @param offRecorder 要设置的 offRecorder 
	 */
	public void setOffRecorder(String offRecorder) {
		this.offRecorder = offRecorder;
	}

	/** 
	 * @return goTime 
	 */
	public String getGoTime() {
		return goTime;
	}

	/** 
	 * @param goTime 要设置的 goTime 
	 */
	public void setGoTime(String goTime) {
		this.goTime = goTime;
	}

	/** 
	 * @return offTime 
	 */
	public String getOffTime() {
		return offTime;
	}

	/** 
	 * @param offTime 要设置的 offTime 
	 */
	public void setOffTime(String offTime) {
		this.offTime = offTime;
	}

	/** 
	 * @return scount 
	 */
	public int getScount() {
		return scount;
	}

	/** 
	 * @param scount 要设置的 scount 
	 */
	public void setScount(int scount) {
		this.scount = scount;
	}

	/** 
	 * @return rcount 
	 */
	public int getRcount() {
		return rcount;
	}

	/** 
	 * @param rcount 要设置的 rcount 
	 */
	public void setRcount(int rcount) {
		this.rcount = rcount;
	}

	/** 
	 * @return ncount 
	 */
	public int getNcount() {
		return ncount;
	}

	/** 
	 * @param ncount 要设置的 ncount 
	 */
	public void setNcount(int ncount) {
		this.ncount = ncount;
	}

	/** 
	* @Title: toString 
	* @Description: TODO
	* @param: @return 
	* @throws 
	*/
	@Override
	public String toString() {
		return "FingerDataVO [deparmentFinger=" + deparmentFinger
				+ ", nameFinger=" + nameFinger + ", numberFinger="
				+ numberFinger + ", dateFinger=" + dateFinger
				+ ", recorderFinger=" + recorderFinger + ", machineFinger="
				+ machineFinger + ", extractDateFinger=" + extractDateFinger
				+ ", extractTimeFinger=" + extractTimeFinger
				+ ", compareTypeFinger=" + compareTypeFinger
				+ ", statusFinger=" + statusFinger + ", weekDay=" + weekDay
				+ ", goRecorder=" + goRecorder + ", offRecorder=" + offRecorder
				+ ", goTime=" + goTime + ", offTime=" + offTime + ", scount="
				+ scount + ", rcount=" + rcount + ", ncount=" + ncount + "]";
	}
}
