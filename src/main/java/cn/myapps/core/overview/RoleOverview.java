package cn.myapps.core.overview;

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

import com.lowagie.text.Cell;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;

/**
 * 角色的pdf表格生成
 * 
 * 2.6版本新增的类
 * 
 * @author keezzm
 * 
 */
public class RoleOverview implements IOverview {

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
			Cell rc = new Cell();
			rc.setBackgroundColor(Color.gray);
			rc.addElement(new Paragraph("角色：", fontChinese));
			table.addCell(rc);

			RoleProcess rp = (RoleProcess) ProcessFactory
					.createProcess(RoleProcess.class);
			Collection<RoleVO> rs = rp.doSimpleQuery(new ParamsTable(),
					applicationId);
			if (rs != null) {
				rc = new Cell();
				Table rTable = new Table(1);
				rTable.setWidth(96);
				rTable.setPadding(0);
				rTable.setSpacing(0);
				rTable.setBorderWidth(0);

				Iterator<RoleVO> it_role = rs.iterator();
				while (it_role.hasNext()) {
					RoleVO role = it_role.next();
					if (role != null) {
						// 名称
						String rName = role.getName();
						Cell cell = new Cell();
						cell.addElement(new Paragraph("名称："
								+ (rName != null ? rName : ""), fontChinese));
						//rTable.addCell(cell);
						// 角色用户
						cell.addElement(new Paragraph("包含的用户：\n", fontChinese));
						Table uTable = new Table(4);
						uTable.setWidth(100);
						uTable.setPadding(0);
						uTable.setSpacing(0);
						uTable.setBorderWidth(1);

						// 表头
						Cell uc = new Cell();
						uc.setBackgroundColor(Color.gray);
						uc.addElement(new Paragraph("用户名", fontChinese));
						uTable.addCell(uc);

						uc = new Cell();
						uc.setBackgroundColor(Color.gray);
						uc.addElement(new Paragraph("账号", fontChinese));
						uTable.addCell(uc);

						uc = new Cell();
						uc.setBackgroundColor(Color.gray);
						uc.addElement(new Paragraph("电子邮箱", fontChinese));
						uTable.addCell(uc);

						uc = new Cell();
						uc.setBackgroundColor(Color.gray);
						uc.addElement(new Paragraph("手机", fontChinese));
						uTable.addCell(uc);

						Collection<UserVO> users = role.getUsers();
						if (users != null) {
							Iterator<UserVO> it_user = users.iterator();
							while (it_user.hasNext()) {
								UserVO user = it_user.next();
								if (user != null) {
									uc = new Cell();
									// 用户名
									String username = user.getName();
									uc = new Cell();
									uc.addElement(new Paragraph(
											username != null ? username : "",
											fontChinese));
									uTable.addCell(uc);

									// 账号
									String loginno = user.getLoginno();
									uc = new Cell();
									uc.addElement(new Paragraph(
											loginno != null ? loginno : "",
											fontChinese));
									uTable.addCell(uc);

									// 电子邮箱
									String email = user.getEmail();
									uc = new Cell();
									uc.addElement(new Paragraph(
											email != null ? email : "",
											fontChinese));
									uTable.addCell(uc);

									// 手机
									String telephone = user.getTelephone();
									uc = new Cell();
									uc.addElement(new Paragraph(
											telephone != null ? telephone : "",
											fontChinese));
									uTable.addCell(uc);
								}
							}
						}
						cell.addElement(uTable);
						rTable.addCell(cell);
					}
				}
				rc.addElement(rTable);
				table.addCell(rc);
			}
		}
		return table;
	}

}
