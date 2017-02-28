package cn.myapps.mobile2.service;

import cn.myapps.core.macro.runner.JsMessage;
import cn.myapps.core.table.constants.MobileConstant2;

public class MbJsMessageXMLBuilder {
	public static String toMobileXML(JsMessage message){
		StringBuffer sb = new StringBuffer();
		sb.append("<").append(MobileConstant2.TAG_JSMESSAGE).append(" ");
		sb.append(MobileConstant2.ATT_TYPE).append(" = \"").append(message.getType()).append("\">");
		sb.append(message.getContent());
		sb.append("</").append(MobileConstant2.TAG_JSMESSAGE).append(">");
		return sb.toString();
	}
}
