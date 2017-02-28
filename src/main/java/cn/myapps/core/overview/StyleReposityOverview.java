package cn.myapps.core.overview;

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.style.repository.ejb.StyleRepositoryProcess;
import cn.myapps.core.style.repository.ejb.StyleRepositoryVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

import com.lowagie.text.Cell;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;

/**
 * 样式库的pdf表格生成
 * 
 * 2.6版本新增的类
 * 
 * @author keezzm
 *
 */
public class StyleReposityOverview implements IOverview {

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
			Cell sc = new Cell();
			sc.setBackgroundColor(Color.gray);
			sc.addElement(new Paragraph("样式库：", fontChinese));
			table.addCell(sc);

			sc = new Cell();
			StyleRepositoryProcess srp = (StyleRepositoryProcess) ProcessFactory
					.createProcess(StyleRepositoryProcess.class);
			Collection<StyleRepositoryVO> styleRepositories = srp
					.doSimpleQuery(new ParamsTable(), applicationId);
			if (styleRepositories != null && styleRepositories.size() > 0) {
				Table srTable = new Table(2);
				srTable.setPadding(0);
				srTable.setSpacing(0);
				srTable.setBorderWidth(1);
				srTable.setWidth(96);

				// 表头
				Cell cell = new Cell();
				cell.setBackgroundColor(Color.gray);
				cell.addElement(new Paragraph("名称", fontChinese));
				srTable.addCell(cell);

				cell = new Cell();
				cell.setBackgroundColor(Color.gray);
				cell.addElement(new Paragraph("版本", fontChinese));
				srTable.addCell(cell);

				Iterator<StyleRepositoryVO> it = styleRepositories.iterator();
				while (it.hasNext()) {
					StyleRepositoryVO styleRepository = it.next();
					// 名称
					cell = new Cell();
					String srName = styleRepository.getName();
					cell.addElement(new Paragraph(srName != null ? srName : "",
							fontChinese));
					srTable.addCell(cell);
					// 版本
					cell = new Cell();
					int version = styleRepository.getVersion();
					cell.addElement(new Paragraph(String.valueOf(version),
							fontChinese));
					srTable.addCell(cell);
					// 内容
					cell = new Cell();
					cell.setColspan(2);
					String content = styleRepository.getContent();
					cell.addElement(new Paragraph("内容：\n"
							+ (content != null ? content : ""), fontChinese));
					srTable.addCell(cell);
				}
				sc.addElement(srTable);
				table.addCell(sc);
			}
		}
		return table;
	}

}
