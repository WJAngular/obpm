package cn.myapps.qm.answer.action;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;
import cn.myapps.qm.answer.ejb.AnswerProcess;
import cn.myapps.qm.answer.ejb.AnswerProcessBean;
import cn.myapps.qm.answer.ejb.AnswerVO;
import cn.myapps.qm.base.action.BaseAction;

/**
 * 
 * @author ahan
 *
 */
public class AnswerServiceAction extends BaseAction<AnswerVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 702536774630501028L;

	public AnswerServiceAction() {
		super();
		content = new AnswerVO();
		process = new AnswerProcessBean();
	}


	/**
	 * 获取问卷答案
	 * 
	 * @return
	 */
	public String doView() {
		try {
				ParamsTable params = getParams();
				String id = params.getParameterAsString("id");
				AnswerVO answer = (((AnswerProcess) process).findByQuestionnaireIdAndUserId(id,getUser()));
				addActionResult(true, "", answer);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		return SUCCESS;
	}


	/**
	 * 提交答卷
	 */
	public String doSave() {
		try {
			AnswerVO answerVO = (AnswerVO) getContent();
			if(StringUtil.isBlank(answerVO.getQuestionnaire_id())){
				addActionResult(false, "", null);
				return SUCCESS;
			}
			AnswerVO answer = (((AnswerProcess) process).findByQuestionnaireIdAndUserId(answerVO.getQuestionnaire_id(),getUser()));
			if(answer!=null){
				addActionResult(false, "请勿重复提交", null);
				return SUCCESS;
			}
			if(StringUtil.isBlank(answerVO.getId())){
				WebUser user = getUser();
				answerVO.setUserId(user.getId());
				answerVO.setUserName(user.getName());
				((AnswerProcess) process).doSave(answerVO);
			}
			addActionResult(true, "", null);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		return SUCCESS;
	}
	
	/**
	 * 获取填空题报表数据
	 * @return
	 */
	public String doInputReportForm() {
		try {
			ParamsTable params = getParams();
			String id = params.getParameterAsString("id");
			String q_Id = params.getParameterAsString("q_id");
			String currpageStr = params.getParameterAsString("currpage");
			String pagelinesStr = params.getParameterAsString("pagelines");
			int currpage = StringUtil.isBlank(currpageStr)? 1 : Integer.parseInt(currpageStr);
			int pagelines = StringUtil.isBlank(pagelinesStr)? Integer.MAX_VALUE : Integer.parseInt(pagelinesStr);
			
			DataPackage<AnswerVO> vo = ((AnswerProcess) process).findInputReport(id,q_Id,currpage,pagelines);
			addActionResult(true, "", vo);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		return SUCCESS;

	}
}
