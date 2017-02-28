package cn.myapps.core.workflow.element;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Enumeration;

import cn.myapps.util.StringUtil;

public class SuspendNode extends Node {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4923459309608331383L;

	public SuspendNode(FlowDiagram owner) {
		super(owner);

	}

	public void paint(OGraphics g) {
		if (_img == null) {
			_img = _owner.getImageResource("suspend.gif");
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

		// java.awt.FontMetrics fm = _owner.getFontMetrics(font);
		// _iconrect.y += 10;
		// g.drawImage(_owner.getImageResource("suspend.gif"), _iconrect.x,
		// _iconrect.y, 10, 10, null, this._owner);

		// 恢复当前背景颜色
		this.bgcolor = old;
	}

	public void paintMobile(OGraphics g) {
		_img = _owner.getImageResource("suspend_m.gif");

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

	public void showTips(Graphics g) {

		StringBuffer tips = new StringBuffer();

		tips.append(this.name);
		// tips.append(shortname);
		// tips.append("\n");
		drawTips(g, tips.toString());

	}

}
