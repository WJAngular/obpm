package cn.myapps.core.dynaform.form.ejb;

import java.util.HashMap;
import java.util.Map;

public class FormfieldValidateScript {
	
	//非空校验
	public static final String checkEmptyScript = "function checkEmpty(fieldName,description) {" +
								"var doc = $CURRDOC.getCurrDoc();" +
								"var value = doc.getItemValueAsString(fieldName);" + 
								"var returnStr = \"\";" + 
								"if(value==null || value.trim().length()<=0) {" + 
								"returnStr = \"'\"+description+\"'{*[core.dynaform.form.formfield.validate.checkEmpty_tip]*}\";" + 
								"}" + 
								"return returnStr;" + 
								"}";
	
	//邮箱校验
	public static final String checkEmailScript = "function checkEmail(fieldName,description){" +
								"var doc = $CURRDOC.getCurrDoc();" + 
								"var value = doc.getItemValueAsString(fieldName);" +
								"var returnStr = \"\";" +
								"var emailreg = /^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$/;" +
								"if(!emailreg.test(value) && value != \"\"){" +
								"if(description != \"\"){" +
								"returnStr = \"'\"+description+\"'{*[core.dynaform.form.formfield.validate.checkEmail_tip]*}\";}" +
								"else{" +
								"returnStr = \"'\"+fieldName+\"'{*[core.dynaform.form.formfield.validate.checkEmail_tip]*}\";}" +
								"}" +
								"return returnStr;" +
								"}";
	
	//身份证校验
	public static final String checkIDcardScript = "function checkIDcard(fieldName,description){" +
								"var doc = $CURRDOC.getCurrDoc();" +
								"var value = doc.getItemValueAsString(fieldName);" +
								"var returnStr = \"\";" +
								"if(value != \"\"){" +
								"value = value.replace(\" \",\"\");" +
								"returnStr = checkIdcardform(value);" +
								"} return returnStr;}" +
								"function checkIdcardform(idcard) {" +
								"var Errors = new Array(\"{*[core.dynaform.form.formfield.validate.checkIDcard_tip1]*}\", \"{*[core.dynaform.form.formfield.validate.checkIDcard_tip2]*}\", \"{*[core.dynaform.form.formfield.validate.checkIDcard_tip3]*}\", \"{*[core.dynaform.form.formfield.validate.checkIDcard_tip4]*}\", \"{*[core.dynaform.form.formfield.validate.checkIDcard_tip5]*}\");" +
								"var area={11:\"北京\",12:\"天津\",13:\"河北\",14:\"山西\",15:\"内蒙古\",21:\"辽宁\",22:\"吉林\",23:\"黑龙江\",31:\"上海\",32:\"江苏\",33:\"浙江\",34:\"安徽\",35:\"福建\",36:\"江西\",37:\"山东\", 41:\"河南\",42:\"湖北\",43:\"湖南\",44:\"广东\",45:\"广西\",46:\"海南\",50:\"重庆\",51:\"四川\",52:\"贵州\",53:\"云南\",54:\"西藏\",61:\"陕西\",62:\"甘肃\",63:\"青海\",64:\"宁夏\",65:\"新疆\",71:\"台湾\",81:\"香港\",82:\"澳门\",91:\"国外\"};" +
								"var idcard, Y, JYM;" +
								"var S, M;" +
								"var idcard_array = new Array();" +
								"idcard_array = idcard.split(\"\");" +
								"switch (idcard.length()) {" +
								"case 15:" +
								"if (area[parseInt(idcard.substr(0, 2))] == null) return Errors[4];" +
								"if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0 || ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0)) {" +
								"ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;" +
								"} else {" +
								"ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;}" +
								"if (ereg.test(idcard)) return \"\";" +
								"else return Errors[2];" +
								"break;" +
								"case 18:" +
								"if (area[parseInt(idcard.substr(0, 2))] == null) return Errors[4];" +
								"if (parseInt(idcard.substr(6, 4)) % 4 == 0 || (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard.substr(6, 4)) % 4 == 0)) {" +
								"ereg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;" +
								"} else {" +
								"ereg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;}" +
								"if (ereg.test(idcard)) {" +
								"var  sigma = 0;" +
								"var a = [7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2];" +
								"var w = [\"1\",\"0\",\"X\",\"9\",\"8\",\"7\",\"6\",\"5\",\"4\",\"3\",\"2\"];" +
								"for (var i = 0; i < 17; i++) {" +
								"var ai = parseInt(idcard.substring(i, i + 1));" +
								"var wi = a[i];" +
								"sigma += ai * wi;}" +
								"var number = sigma % 11;" +
								"var check_number = w[number];" +
								"if (idcard.substr(17).equals(check_number)) {" +
								"return \"\";" +
								"} else {return Errors[3];}" +
								"} else return Errors[2];" +
								"break;" +
								"default:" +
								"return Errors[1];" +
								"break;}}";
	
	//手机号码校验
	public static final String checkPhoneScript =  "function checkEmpty(fieldName,description) {" +
								"var doc = $CURRDOC.getCurrDoc();" +
								"var value = doc.getItemValueAsString(fieldName);" + 
								"var returnStr = \"\";" + 
								"if(value != \"\" && value.length() != 11) {" + 
								"if(description != \"\"){" +
								"returnStr = \"'\"+description+\"'{*[core.dynaform.form.formfield.validate.checkPhone_tip]*}\";}" +
								"else{" +
								"returnStr = \"'\"+fieldName+\"'{*[core.dynaform.form.formfield.validate.checkPhone_tip]*}\";}" +
								"}" +
								"return returnStr;" + 
								"}";
	
	//数据唯一校验
	public static final String checkFieldUniqueScript = "function checkFieldUnique(fieldName,description){" +
								"var doc = $CURRDOC.getCurrDoc();" +
								"var value = doc.getItemValueAsString(fieldName);" +
								"var application = $WEB.getApplication();" +
								"var returnStr = \"\";" +
								"if(value != null && value.trim().length() > 0) {" + 
								"var dql = \"$formname=\'\" + doc.getFormname() + \"\' and \" + fieldName + \" LIKE \'\" + value.replace(\"'\",\"''\") + \"\' and $id <> \'\"+doc.getId()+\"\'\";" +
								"var dpg = queryByDQL(dql);" +
								"if(dpg.size()>0){" +
								"if(description != \"\"){" +
								"returnStr = \"'\"+description+\"'{*[core.dynaform.form.formfield.validate.checkFieldUnique_tip]*}\";}" +
								"else{" +
								"returnStr = \"'\"+fieldName+\"'{*[core.dynaform.form.formfield.validate.checkFieldUnique_tip]*}\";}}" +
								"return returnStr;}}";
	
	/**
	 * 系统自带校验脚本
	 */
	public static final Map<String, String> validateScripts = new HashMap<String, String>();
	static{
		validateScripts.put("core.dynaform.form.formfield.validate.checkEmpty_system", checkEmptyScript);
		validateScripts.put("core.dynaform.form.formfield.validate.checkEmail_system", checkEmailScript);
		validateScripts.put("core.dynaform.form.formfield.validate.checkIDcard_system", checkIDcardScript);
		validateScripts.put("core.dynaform.form.formfield.validate.checkPhone_system", checkPhoneScript);
		validateScripts.put("core.dynaform.form.formfield.validate.checkFieldUnique_system", checkFieldUniqueScript);
	}
	
	public static Map<String, String> getValidateScripts(){
		return validateScripts;
	}
}
