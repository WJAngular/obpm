package cn.myapps.core.overview;

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSourceProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

import com.lowagie.text.Cell;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;

/**
 * 数据源的pdf表格生成
 * 
 * 2.6版本新增的类
 * 
 * @author keezzm
 *
 */
public class DataSourceOverview implements IOverview {

	public Table buildOverview(String applicationId) throws Exception {

		Table dTable = new Table(1);
		dTable.setPadding(2);
		dTable.setSpacing(0);
		dTable.setBorderWidth(1);
		dTable.setWidth(100);
		if (!StringUtil.isBlank(applicationId)) {
			BaseFont bfChinese = BaseFont.createFont("STSongStd-Light",
					"UniGB-UCS2-H", false);
			Font fontChinese = new Font(bfChinese, 10, Font.NORMAL, Color.BLACK);

			Cell dc = new Cell();
			dc.setBackgroundColor(Color.gray);
			dc.addElement(new Paragraph("数据源：", fontChinese));
			dTable.addCell(dc);

			// 数据源信息
			dc = new Cell();

			Table dsTable = new Table(5);
			dsTable.setWidth(96);
			dsTable.setPadding(0);
			dsTable.setSpacing(0);
			dsTable.setBorderWidth(1);
			// 数据源表头
			Cell cell = new Cell();
			cell.setBackgroundColor(Color.gray);
			cell.addElement(new Paragraph("名称 ", fontChinese));
			dsTable.addCell(cell);
			cell = new Cell();
			cell.setBackgroundColor(Color.gray);
			cell.addElement(new Paragraph("数据驱动类 ", fontChinese));
			dsTable.addCell(cell);
			cell = new Cell();
			cell.setBackgroundColor(Color.gray);
			cell.addElement(new Paragraph("地址 ", fontChinese));
			dsTable.addCell(cell);
			cell = new Cell();
			cell.setBackgroundColor(Color.gray);
			cell.addElement(new Paragraph("连接池大小 ", fontChinese));
			dsTable.addCell(cell);
			cell = new Cell();
			cell.setBackgroundColor(Color.gray);
			cell.addElement(new Paragraph("连接超时时间", fontChinese));
			dsTable.addCell(cell);

			// 数据源列表
			DataSourceProcess dp = (DataSourceProcess) ProcessFactory
					.createProcess(DataSourceProcess.class);
			Collection<DataSource> dts = dp.doSimpleQuery(new ParamsTable(),
					applicationId);
			if (dts != null) {
				Iterator<DataSource> it = dts.iterator();
				while (it.hasNext()) {
					DataSource dt = it.next();
					if (dt != null) {
						String dName = dt.getName();
						String driver = dt.getDriverClass();
						String url = dt.getUrl();
						String pSize = dt.getPoolsize();
						String timeOut = dt.getTimeout();

						cell = new Cell();
						cell.addElement(new Paragraph(dName != null ? dName
								: "", fontChinese));
						dsTable.addCell(cell);
						cell = new Cell();
						cell.addElement(new Paragraph(driver != null ? driver
								: "", fontChinese));
						dsTable.addCell(cell);
						cell = new Cell();
						cell.addElement(new Paragraph(url != null ? url : "",
								fontChinese));
						dsTable.addCell(cell);
						cell = new Cell();
						cell.addElement(new Paragraph(pSize != null ? pSize
								: "", fontChinese));
						dsTable.addCell(cell);
						cell = new Cell();
						cell.addElement(new Paragraph(timeOut != null ? timeOut
								: "", fontChinese));
						dsTable.addCell(cell);
					}
				}
			}
			dc.addElement(dsTable);
			dTable.addCell(dc);
		}
		return dTable;
	}

}
