package cn.myapps.core.dynaform.form.ejb;

import org.htmlparser.Parser;

public class TemplateParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static Form parseTemplate(Form form, String template)
			throws Exception {
		Parser parser = new Parser();

		if (template == null) {
			template = "";
		}

		template = template.replaceAll("\\[计算插入模板\\]</MARQUEE>", "");

		parser.setInputHTML(template);
		TemplateProcessVisitor visitor = new TemplateProcessVisitor(form);
		parser.visitAllNodesWith(visitor);

		Form form2 = visitor.getResult();
		return form2;
	}

}
