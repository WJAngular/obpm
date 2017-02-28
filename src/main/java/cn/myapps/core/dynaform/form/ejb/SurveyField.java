package cn.myapps.core.dynaform.form.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

/**
 * 调查控件
 * @author Happy
 *
 */
public class SurveyField extends FormField implements ValueStoreField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7461186182830819978L;
	
	
	/**
	 * 问卷脚本
	 */
	protected String questionScript;
	
	

	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType)
			throws Exception {
		StringBuffer html = new StringBuffer();

		int displayType = getDisplayType(doc, runner, webUser, permissionType);
		
		if(doc != null) {
			if(displayType==PermissionType.HIDDEN){
				return getHiddenValue();
			}else{
				html.append(toHtml(doc, runner, webUser, displayType));
			}
			
			
		}
		
		return html.toString();
	}
	
	public String toHtml(Document doc, IRunner runner, WebUser webUser,int displayType)throws Exception {
		StringBuffer html = new StringBuffer();
		
		Object value = null;
		Item item = doc.findItem(this.getName());
		if (item != null){
			value = item.getValue();
		}
		value = value != null ? value : "";
		
		html.append("<input moduleType='surveyField' type='hidden' id='").append(doc.getId()+"_"+getId())
		.append("' name='").append(getName()).append("' ")
		.append("value='").append(value).append("' ")
		.append("data-questions='").append(runQuestionScript(runner, doc, webUser)).append("' ")
		.append("data-display-type='").append(displayType).append("' ")
		.append("data-discription='").append(getDiscript()).append("' />");
		
		return html.toString();
	}
	
	
	/**
	 * 运行问卷脚本，返回JSON格式字符串
	 * @param runner
	 * @param doc
	 * @param webUser
	 * @return
	 * @throws Exception
	 */
	public String runQuestionScript (IRunner runner, Document doc, WebUser webUser)throws Exception {
		
		List<Question> questions = getQuestions(runner);
		
		return JSONArray.fromObject(questions).toString();
	}
	
	public List<Question> getQuestions(IRunner runner) throws Exception {
		List<Question> questions = new ArrayList<Question>();
		Object result = null;
		if (getQuestionScript() != null && getQuestionScript().trim().length() > 0) {

			result = runner.run(getScriptLable("QuestionScript"), StringUtil.dencodeHTML(getQuestionScript()));
			if (result instanceof Collection) {
				questions.addAll((Collection<? extends Question>) result);
				
			}else if(result instanceof Question){
				questions.add((Question) result);
			}

		}
		return questions;
	}

	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser,
			Map<String, Options> columnOptionsCache) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String getQuestionScript() {
		return questionScript;
	}

	public void setQuestionScript(String questionScript) {
		this.questionScript = questionScript;
	}

}
