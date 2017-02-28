package cn.myapps.core.dynaform.document.dql;

import java.net.URL;

import antlr.Tool;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// JavaCodeGenerator.

		// ANTLR.compiler.
		URL url = Test.class.getClassLoader().getResource("");

		Tool
				.main(new String[] {
						"-o",
						"C:/Java/workspace/gb4.0/src/cn/myapps/core/dynaform/document/dql",
						url.getPath()
								+ "cn/myapps/core/dynaform/document/dql/dql.g" });
		// Tool.main(new
		// String[]{url.getPath()+"cn/myapps/core/dynaform/document/dql/dql.g"});
	}

}
