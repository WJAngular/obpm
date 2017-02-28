package cn.myapps.qm.answer.action;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie2.text.pdf.BaseFont;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.qm.answer.ejb.AnswerProcess;
import cn.myapps.qm.answer.ejb.AnswerProcessBean;
import cn.myapps.qm.answer.ejb.AnswerVO;
import cn.myapps.qm.base.action.BaseAction;
import cn.myapps.qm.base.ejb.BaseProcess;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireProcess;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireProcessBean;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireVO;
import cn.myapps.util.StringUtil;
import cn.myapps.util.http.HttpRequestDeviceUtils;



public class AnswerAction extends BaseAction<AnswerVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4457002675610528513L;

	private String question_id;

	private String s_title;

	private String s_jumpType;
	
	public AnswerAction() {
		super();
		content = new AnswerVO();
		process = new AnswerProcessBean();
	}

	/**
	 * 获取首页待填问卷
	 * 
	 * @return
	 */
	public String doHomePage() {
		try {
			ParamsTable params = getParams();

			setDatas(process.doQuery(params, getUser()));

			HttpServletRequest request = ServletActionContext.getRequest();
			if (HttpRequestDeviceUtils.isMobileDevice(request) == true) {
				return "mobile";
			} else {
				return SUCCESS;
			}
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			e.printStackTrace();
			return INPUT;
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
	}

	/**
	 * 获取问卷
	 * 
	 * @return
	 */
	public String doView() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			ParamsTable params = getParams();
			//这个参数id是QUESTIONNAIRE的id
			String id = params.getParameterAsString("id");
			setContent(((AnswerProcess) process).findByQuestionnaireIdAndUserId(id,getUser()));
			if (HttpRequestDeviceUtils.isMobileDevice(request) == true) {
				return "mobile";
			} else {
				return SUCCESS;
			}
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			e.printStackTrace();
			return INPUT;
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
	}

	public String doList() {
		try {
			ParamsTable params = getParams();

			String s_title = params.getParameterAsString("s_title");
			if (s_title == null)
				s_title = "";

			String _currpage = params.getParameterAsString("_currpage");
			String _pagelines = params.getParameterAsString("_pagelines");

			int page = (_currpage != null && _currpage.length() > 0) ? Integer
					.parseInt(_currpage) : 1;
			int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer
					.parseInt(_pagelines) : Integer.MAX_VALUE;

			setDatas(((AnswerProcess) process).doQueryByFilter(s_title, page,
					lines, getUser()));
			setS_title(s_title);

			HttpServletRequest request = ServletActionContext.getRequest();
			if (HttpRequestDeviceUtils.isMobileDevice(request) == true) {
				return "mobile";
			} else {
				return SUCCESS;
			}
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
	}

	public String showResultList() {
		try {
			ParamsTable params = getParams();
			// 问卷id
			String questionnaire_id = params.getParameterAsString("id");
			// 问题id
			String question_id = params.getParameterAsString("q_id");
			setDatas(((AnswerProcess) process)
					.doViewForQuestionnaire(questionnaire_id));
			setQuestion_id(question_id);

			HttpServletRequest request = ServletActionContext.getRequest();
			if (HttpRequestDeviceUtils.isMobileDevice(request) == true) {
				return "mobile";
			} else {
				return SUCCESS;
			}
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
	}

	/**
	 * 问卷编辑
	 */
	public String doEdit() {
		try {
			ParamsTable params = getParams();
			String id = params.getParameterAsString("id");
			setContent(process.doView(id));

			HttpServletRequest request = ServletActionContext.getRequest();
			if (HttpRequestDeviceUtils.isMobileDevice(request) == true) {
				return "mobile";
			} else {
				return SUCCESS;
			}
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
	}

	/**
	 * 新建问卷
	 */
	public String doNew() {
		try {
			AnswerVO answer = new AnswerVO();

			setContent(answer);
			
			HttpServletRequest request = ServletActionContext.getRequest();
			if (HttpRequestDeviceUtils.isMobileDevice(request) == true) {
				return "mobile";
			} else {
				return SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
	}

	/**
	 * 保存问卷
	 */
	public String doSave() {
		try {
			AnswerVO answer = (AnswerVO) getContent();
			answer.setDomainid(getUser().getDomainid());
			BaseProcess<QuestionnaireVO> q_process = new QuestionnaireProcessBean();
			if (StringUtil.isBlank(answer.getId())) {
				WebUser user = getUser();
				answer.setUserId(user.getId());
				answer.setUserName(user.getName());
				String defaultDepartmentId = user.getDefaultDepartment();
				if(!StringUtil.isBlank(defaultDepartmentId)){
					DepartmentVO defaultDepartment = user.getDepartmentById(defaultDepartmentId);
					answer.setUserDepartment(defaultDepartment.getName());
				}
				((AnswerProcess) process).doSave(answer);

				((QuestionnaireProcess) q_process).addPaticipate(answer.getQuestionnaire_id());
				
			}

			answer = (AnswerVO) process.doView(answer.getId());

			setContent(answer);

			HttpServletRequest request = ServletActionContext.getRequest();
			if (HttpRequestDeviceUtils.isMobileDevice(request) == true) {
				return "mobile";
			} else {
				return SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
	}

	//todo begin
	public void exportPDF() throws IOException, DocumentException{
		try{
			HttpServletResponse response=ServletActionContext.getResponse();
			//HttpServletRequest request=ServletActionContext.getRequest();
			
			//test
			ParamsTable params = getParams();
			String content = params.getParameterAsString("content");
//			System.out.println("这是content"+content);
			String c="<?xml version='1.0' encoding='UTF-8'?><html><head><meta http-equiv='content-type' content='text/html; charset=UTF-8' />"
					 +"<style type='text/css' >body {font-family: SimSun;}</style></head><body>"
					+content+"</body></html>";
//			System.out.println("这是c"+c);
			String pdfName = "result";
			response.setHeader("Content-disposition", "attachment;filename="+pdfName);     
            response.setContentType("application/pdf");     
            OutputStream os = response.getOutputStream();
			ITextRenderer renderer = new ITextRenderer();  

//			String url="http://localhost:8080/obpm/qm/answer/result.jsp";
//			String url="http://localhost:8080/obpm/qm/Test.html";
//			String durl=request.getRequestURL().toString();
//			String url=durl.replace("answer/download.action", "Test.jsp");
//			renderer.setDocument(url); 
			
			renderer.setDocumentFromString(c);
			                 
			ITextFontResolver fontResolver = renderer.getFontResolver();
			fontResolver.addFont("C:/Windows/fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);  
		    renderer.layout();     
			renderer.createPDF(os);     
			os.close();     
		}catch(Exception e){
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
	}
	//todo end
	
	public String getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}

	public String getS_title() {
		return s_title;
	}

	public void setS_title(String s_title) {
		this.s_title = s_title;
	}

	public String getS_jumpType() {
		return s_jumpType;
	}

	public void setS_jumpType(String s_jumpType) {
		this.s_jumpType = s_jumpType;
	}
}
