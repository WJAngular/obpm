package cn.myapps.core.overview;

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.dts.excelimport.config.ejb.IMPMappingConfigProcess;
import cn.myapps.core.dynaform.dts.excelimport.config.ejb.IMPMappingConfigVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

import com.lowagie.text.Cell;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;

/**
 * Excel导入的pdf表格生成
 * 
 * 2.6版本新增的类
 * 
 * @author keezzm
 *
 */
public class ExcelImportConfigOverview implements IOverview {

	public Table buildOverview(String applicationId) throws Exception {
		Table table = new Table(1);
		table.setPadding(2);
		table.setSpacing(0);
		table.setBorderWidth(1);
		table.setWidth(100);
		if (!StringUtil.isBlank(applicationId)) {
			BaseFont bfChinese = BaseFont.createFont("STSongStd-Light",
					"UniGB-UCS2-H", false);
			Font fontChinese = new Font(bfChinese, 10, Font.NORMAL, Color.BLACK);
			Cell ec = new Cell();
			ec.setBackgroundColor(Color.gray);
			ec.addElement(new Paragraph("Excel导入配置：", fontChinese));
			table.addCell(ec);

			IMPMappingConfigProcess impp = (IMPMappingConfigProcess) ProcessFactory
					.createProcess(IMPMappingConfigProcess.class);
			Collection<IMPMappingConfigVO> impMappingConfigs = impp
					.doSimpleQuery(new ParamsTable(), applicationId);
			if (impMappingConfigs != null && impMappingConfigs.size() > 0) {
				ec = new Cell();
				Table impTable = new Table(2);
				impTable.setPadding(0);
				impTable.setSpacing(0);
				impTable.setBorderWidth(1);
				impTable.setWidth(96);

				// 表头
				Cell cell = new Cell();
				cell.setBackgroundColor(Color.gray);
				cell.addElement(new Paragraph("名称", fontChinese));
				impTable.addCell(cell);

				cell = new Cell();
				cell.setBackgroundColor(Color.gray);
				cell.addElement(new Paragraph("上传模板", fontChinese));
				impTable.addCell(cell);

				Iterator<IMPMappingConfigVO> it = impMappingConfigs.iterator();
				while (it.hasNext()) {
					IMPMappingConfigVO impc = it.next();
					if (impc != null) {
						// 名称
						cell = new Cell();
						String impName = impc.getName();
						cell.addElement(new Paragraph(impName != null ? impName
								: "", fontChinese));
						impTable.addCell(cell);

						// 路径
						cell = new Cell();
						String impPath = impc.getPath();
						cell.addElement(new Paragraph(impPath != null ? impPath
								: "", fontChinese));
						impTable.addCell(cell);

						// xml
						String xml = impc.getXml();
						if (!StringUtil.isBlank(xml)) {
							cell = new Cell();
							cell.setColspan(2);
							cell.addElement(new Paragraph("代码：\n"
									+ (xml != null ? xml : ""), fontChinese));
							impTable.addCell(cell);
						}

					}
				}
				ec.addElement(impTable);
				table.addCell(ec);
			}
		}
		return table;
	}

}
