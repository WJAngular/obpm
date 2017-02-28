package cn.myapps.core.workflow.element;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.util.DateUtil;
import cn.myapps.util.StringUtil;

public class AutoNode extends Node {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4249005524806235600L;
	/**
	 * 马上审批
	 */
	public final static int AUTO_AUDIT_TYPE_IMMEDIATELY = 1;
	/**
	 * 指定时间审批
	 */
	public final static int AUTO_AUDIT_TYPE_SPECIFY = 2;
	/**
	 * 滞后一段时间审批
	 */
	public final static int AUTO_AUDIT_TYPE_DELAY = 3;
	
	/**
	 * 自动审批编辑模式-设计
	 */
	public final static int AUTO_AUDIT_EDIT_MODE_DESIGN = 1;
	
	/**
	 * 自动审批编辑模式-代码
	 */
	public final static int AUTO_AUDIT_EDIT_MODE_CODE = 2;

	public AutoNode(FlowDiagram owner) {
		super(owner);
	}

	/**
	 * 是否聚合节点
	 */
	public boolean isgather;

	/**
	 * 是否分散节点
	 */
	public boolean issplit;
	
	/**
	 * 自动审批时间编辑模式
	 */
	public int autoAuditTimeEditMode = AUTO_AUDIT_EDIT_MODE_DESIGN;
	
	/**
	 * 指定自动审批日期脚本
	 */
	public String auditDateTimeScript;

	/**
	 * 自动审批类型
	 */
	public int autoAuditType;

	/**
	 * 指定自动审批日期
	 */
	public String auditDateTime;

	/**
	 * 滞后时间(到达以后多少时间之后自动审批)
	 */
	public Date delayTime;

	/**
	 * 滞后天数
	 */
	public String delayDay;

	/**
	 * 滞后小时数
	 */
	public String delayHour;

	/**
	 * 滞后分钟数
	 */
	public String delayMinute;
	
	/**
	 * 分散开始节点
	 * @return
	 */
	public String splitStartNode;

	public Date getAuditDateTime(IRunner runner) throws Exception {
		Calendar calendar = Calendar.getInstance();
		
		if(autoAuditTimeEditMode==AUTO_AUDIT_EDIT_MODE_CODE && !StringUtil.isBlank(auditDateTimeScript)){
			Object obj = runner.run("AutoNode [name="+name+"] [id="+id+"] auditDateTimeScript", StringUtil.dencodeHTML(auditDateTimeScript));
			if(obj instanceof Date){
				calendar.setTime((Date) obj);
			}else if(obj instanceof String){
				Date date = DateUtil.parseDate((String) obj,
				"yyyy-MM-dd HH:mm:ss");
				calendar.setTime(date);
			}
		}else{
			switch (autoAuditType) {
			case AUTO_AUDIT_TYPE_IMMEDIATELY:
				break;
			case AUTO_AUDIT_TYPE_SPECIFY:
				Date date = DateUtil.parseDate(this.auditDateTime,
						"yyyy-MM-dd HH:mm:ss");
				calendar.setTime(date);
				break;
			case AUTO_AUDIT_TYPE_DELAY:
				if (!StringUtil.isBlank(delayDay)) {
					calendar.add(Calendar.DATE, Integer.parseInt(delayDay));
				}
				if (!StringUtil.isBlank(delayHour)) {
					calendar.add(Calendar.HOUR, Integer.parseInt(delayHour));
				}
				if (!StringUtil.isBlank(delayMinute)) {
					calendar.add(Calendar.MINUTE, Integer.parseInt(delayMinute));
				}
	
				break;
			default:
				break;
			}
		}

		return calendar.getTime();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * FlowDiagram fd = new FlowDiagram(); AutoNode g = new AutoNode(fd);
		 */
	}

	public void showTips(Graphics g) {
		StringBuffer tips = new StringBuffer();
		tips.append(this.name);
		// tips.append(shortname);
		tips.append("\n");
		drawTips(g, tips.toString());

	}

	public void paintMobile(OGraphics g) {

		_img = _owner.getImageResource("timer_m.gif");

		// Call All Sub Elements PAINT METHOD.
		// 保存当前背景颜色...
		Color old = this.bgcolor;
		if (_owner.isCurrentToEdit(this)) {
			bgcolor = DEF_CURREDITCOLOR;
		}

		if (_owner.isCurrentSelected(this)) {
			bgcolor = DEF_SELECTEDCOLOR;
		}

		for (Enumeration<Element> e = _subelems.elements(); e.hasMoreElements();) {
			Object te = e.nextElement();

			if (te instanceof PaintElement) {
				PaintElement se = (PaintElement) te;
				se.paintMobile(g);
			}

		}

		// Fill background
		this.width = WIDTH;
		this.m_width = M_WIDTH;
		this.m_height = M_HEIGHT;
		this.height = HEIGHT;
		resizeForMobile();
		g.setColor(bgcolor);
		g.fillRect(this.x, this.y, this.width, this.height);

		// Draw Image

		if (_iscurrent) {
			_img = _owner.getImageResource("current_m.gif");
		}

		g.drawImage(_img, _imgrect.x, _imgrect.y, _imgrect.width,
				_imgrect.height, null, this._owner);

		if (this.name != null) {
			// java.awt.FontMetrics fm = _owner.getFontMetrics(font);
			g.setColor(java.awt.Color.black);
			g.drawString(name, _txtrect.x + name.length(), _txtrect.y + 30);
		}

		// 恢复当前背景颜色
		this.bgcolor = old;
	}

	public void paint(OGraphics g) {
		if (_img == null) {
			_img = _owner.getImageResource("timer.gif");
		}

		// Call All Sub Elements PAINT METHOD.
		// 保存当前背景颜色...
		Color old = this.bgcolor;
		if (_owner.isCurrentToEdit(this)) {
			bgcolor = DEF_CURREDITCOLOR;
		}

		if (_owner.isCurrentSelected(this)) {
			bgcolor = DEF_SELECTEDCOLOR;
		}

		for (Enumeration<Element> e = _subelems.elements(); e.hasMoreElements();) {
			Object te = e.nextElement();

			if (te instanceof PaintElement) {
				PaintElement se = (PaintElement) te;
				se.paint(g);
			}

		}

		// Fill background
		this.width = WIDTH;
		this.m_width = M_WIDTH;
		this.m_height = M_HEIGHT;
		this.height = HEIGHT;
		resize();
		g.setColor(bgcolor);
		g.fillRect(this.x, this.y, this.width, this.height);

		// Draw Image
		g.drawImage(_img, _imgrect.x, _imgrect.y, _imgrect.width,
				_imgrect.height, null, this._owner);

		if (this.name != null) {
			java.awt.FontMetrics fm = _owner.getFontMetrics(font);
			int tx = (int) (_txtrect.x + (_txtrect.width - fm.stringWidth(name)) / 2);
			int ty = (int) (_txtrect.y + 2 * _txtrect.height);
			if (this._iscurrent) {
				g.drawImage(_owner.getImageResource("current.gif"), _txtrect.x,
						_txtrect.y, _txtrect.width, 10 + _txtrect.height,
						null, this._owner);
			} else {
				g.drawImage(_owner.getImageResource("background.gif"),
						_txtrect.x, _txtrect.y, _txtrect.width,
						10 + _txtrect.height, null, this._owner);

			}
			g.setColor(java.awt.Color.black);
			g.drawString(StringUtil.dencodeHTML(name), tx + this.name.length(), ty - 10);
		}

		// 恢复当前背景颜色
		this.bgcolor = old;
	}

}
