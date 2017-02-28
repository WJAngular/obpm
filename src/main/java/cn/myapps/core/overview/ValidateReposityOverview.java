package cn.myapps.core.overview;

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.validate.repository.ejb.ValidateRepositoryProcess;
import cn.myapps.core.validate.repository.ejb.ValidateRepositoryVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

import com.lowagie.text.Cell;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;

/**
 * 校验库的pdf表格生成
 * 
 * 2.6版本新增的类
 * 
 * @author keezzm
 *
 */
public class ValidateReposityOverview implements IOverview {

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
			Cell vc = new Cell();
			vc.setBackgroundColor(Color.gray);
			vc.addElement(new Paragraph("校验库：", fontChinese));
			table.addCell(vc);

			vc = new Cell();
			ValidateRepositoryProcess vrp = (ValidateRepositoryProcess) ProcessFactory
					.createProcess(ValidateRepositoryProcess.class);
			Collection<ValidateRepositoryVO> validateRepositories = vrp
					.doSimpleQuery(new ParamsTable(), applicationId);
			if (validateRepositories != null && validateRepositories.size() > 0) {
				Table vrTable = new Table(2);
				vrTable.setPadding(0);
				vrTable.setSpacing(0);
				vrTable.setBorderWidth(1);
				vrTable.setWidth(96);

				// 表头
				Cell cell = new Cell();
				cell.setBackgroundColor(Color.gray);
				cell.addElement(new Paragraph("名称", fontChinese));
				vrTable.addCell(cell);

				cell = new Cell();
				cell.setBackgroundColor(Color.gray);
				cell.addElement(new Paragraph("版本", fontChinese));
				vrTable.addCell(cell);

				Iterator<ValidateRepositoryVO> it = validateRepositories
						.iterator();
				while (it.hasNext()) {
					ValidateRepositoryVO validateRepository = it.next();
					// 名称
					cell = new Cell();
					String vrName = validateRepository.getName();
					cell.addElement(new Paragraph(vrName != null ? vrName : "",
							fontChinese));
					vrTable.addCell(cell);
					// 版本
					cell = new Cell();
					int version = validateRepository.getVersion();
					cell.addElement(new Paragraph(String.valueOf(version),
							fontChinese));
					vrTable.addCell(cell);
					// 内容
					cell = new Cell();
					cell.setColspan(2);
					String content = validateRepository.getContent();
					cell.addElement(new Paragraph("内容：\n"
							+ (content != null ? content : ""), fontChinese));
					vrTable.addCell(cell);
				}
				vc.addElement(vrTable);
				table.addCell(vc);
			}
		}
		return table;
	}

}
