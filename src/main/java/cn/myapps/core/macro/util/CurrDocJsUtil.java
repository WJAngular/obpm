package cn.myapps.core.macro.util;

import cn.myapps.core.dynaform.document.ejb.Document;


public class CurrDocJsUtil {
	private Document _currDoc;

	public CurrDocJsUtil(Document currDoc) {
		_currDoc = currDoc;
	}
	
	public Object getCurrDoc() {
		return _currDoc;
	}

	public static void main(String[] args) {
	}
}
