package cn.myapps.qm.questionnaire.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.km.util.StringUtil;
import cn.myapps.qm.answer.ejb.AnswerVO;
import cn.myapps.qm.base.action.BaseAction;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireProcess;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireProcessBean;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireVO;
import cn.myapps.util.http.HttpRequestDeviceUtils;

public class QuestionnaireAction extends BaseAction<QuestionnaireVO> {

	private static final long serialVersionUID = -8418557993257269217L;

	private String s_title;
	// 10/22添加 已经完成的问卷 By toby
	protected DataPackage<QuestionnaireVO> doneDatas = null;

	public QuestionnaireAction() {
		super();
		content = new QuestionnaireVO();
		process = new QuestionnaireProcessBean();
	}

	/**
	 * 问卷列表
	 * 
	 * @return
	 */
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

			setDatas(((QuestionnaireProcess) process).doQueryByFilter(s_title,
					page, lines, getUser()));
			setS_title(s_title);

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
	 * 删除问卷
	 * 
	 * @return
	 */
	public String doDelete() {
		try {
			String[] _select = get_selects();
			process.doRemove(_select);
			return SUCCESS;
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
			QuestionnaireVO question = ((QuestionnaireProcessBean) process)
					.doNew();
			setContent(question);

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
			QuestionnaireVO question = (QuestionnaireVO) getContent();
			WebUser user = getUser();
			if (StringUtil.isBlank(question.getCreator())) {
				question.setCreator(user.getId());
				question.setCreatorName(user.getName());
				question.setCreateDate(new Date());
				String departmentID = user.getDefaultDepartment();
				question.setCreatorDeptId(departmentID);
				String departmentName = "";
				DepartmentVO departmentVO = user.getDepartmentById(departmentID);
				if(null!=departmentVO)
				    departmentName = departmentVO.getName();
				question.setCreatorDeptName(departmentName);
			}
			((QuestionnaireProcess) process).doSave(question);

			setContent(question);

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
	 * 再次编辑问卷
	 */
	public String doRebuild() {
		try {
			QuestionnaireVO question = (QuestionnaireVO) getContent();
			WebUser user = getUser();
			if (StringUtil.isBlank(question.getCreator())) {
				question.setCreator(user.getId());
				question.setCreatorName(user.getName());
				question.setCreateDate(new Date());
				String departmentID = user.getDefaultDepartment();
				question.setCreatorDeptId(departmentID);
				String departmentName = "";
				DepartmentVO departmentVO = user.getDepartmentById(departmentID);
				if(null!=departmentVO)
				    departmentName = departmentVO.getName();
				question.setCreatorDeptName(departmentName);
			}
			((QuestionnaireProcess) process).doReBuild(question);
			((QuestionnaireProcess) process).doSave(question);

			setContent(question);

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

	public String doShowResult() {
		try {
			ParamsTable params = getParams();
			String id = params.getParameterAsString("id");
			setContent(((QuestionnaireProcessBean) process).doShowResult(id,
					getUser()));

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
	 * 发布问卷
	 * 
	 * @return
	 */
	public String doPublish() {
		try {
			QuestionnaireVO question = (QuestionnaireVO) getContent();
			WebUser user = getUser();
			if (StringUtil.isBlank(question.getCreator())) {
				question.setCreator(user.getId());
				question.setCreatorName(user.getName());
				question.setCreateDate(new Date());
				String departmentID = user.getDefaultDepartment();
				question.setCreatorDeptId(departmentID);
				String departmentName = "";
				DepartmentVO departmentVO = user.getDepartmentById(departmentID);
				if(null!=departmentVO)
				    departmentName = departmentVO.getName();
				question.setCreatorDeptName(departmentName);
			}
			question.setDomainid(user.getDomainid());
			question.setStatus(1);
			question.setPublishDate(new Date());
			((QuestionnaireProcess) process).doSave(question);

			setContent(question);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
	}

	public String doRecover() {
		try {
			ParamsTable params = getParams();
			String id = params.getParameterAsString("id");
			if (id != null) {
				QuestionnaireVO question = (QuestionnaireVO) process.doView(id);
				if (question != null) {
					question.setDomainid(getUser().getDomainid());
					question.setStatus(QuestionnaireVO.STATUS_RECOVER);
					((QuestionnaireProcess) process).doSave(question);
					ServletActionContext.getRequest().setAttribute("DATA",
							"SUCCESS");
				}
			}
			HttpServletRequest request = ServletActionContext.getRequest();
			if (HttpRequestDeviceUtils.isMobileDevice(request) == true) {
				return "mobile";
			} else {
				return SUCCESS;
			}
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			ServletActionContext.getRequest().setAttribute("DATA", "ERROR");
			return INPUT;
		} catch (Exception e) {
			e.printStackTrace();
			ServletActionContext.getRequest().setAttribute("DATA", "ERROR");
			return INPUT;
		}
	}

	/**
	 * 根据id发布问卷
	 * 
	 * @return
	 */
	public String doPublishforId() {
		try {
			ParamsTable params = getParams();
			String id = params.getParameterAsString("id");
			String scope = params.getParameterAsString("scope");
			String ownerIds = params.getParameterAsString("ownerIds");
			String ownerNames = params.getParameterAsString("ownerNames");
			if (id != null) {
				QuestionnaireVO question = (QuestionnaireVO) process.doView(id);
				if (question != null) {
					question.setStatus(QuestionnaireVO.STATUS_PUBLISH);
					question.setOwnerIds(ownerIds);
					question.setOwnerNames(ownerNames);
					question.setScope(scope);
					question.setPublishDate(new Date());
					((QuestionnaireProcess) process).doSave(question);
					ServletActionContext.getRequest().setAttribute("DATA",
							"SUCCESS");
				}
			}
			HttpServletRequest request = ServletActionContext.getRequest();
			if (HttpRequestDeviceUtils.isMobileDevice(request) == true) {
				return "mobile";
			} else {
				return SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ServletActionContext.getRequest().setAttribute("DATA", "ERROR");
		}
		return SUCCESS;
	}

	/**
	 * 获取首页待填问卷
	 * 
	 * @return
	 */
	public String doHomePage() {
		try {

			setDatas(((QuestionnaireProcess) process)
					.doQueryByPublish(getUser()));
			HttpServletRequest request = ServletActionContext.getRequest();
			if (HttpRequestDeviceUtils.isMobileDevice(request) == true) {
				// doList Begin
				setDoneDatas(((QuestionnaireProcess) process)
						.doQueryByPublishDone(getUser()));
				// doList End
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
	 * 创建新答卷
	 * 
	 * @return
	 */
	public String doNewAnswer() {
		try {
			ParamsTable params = getParams();
			String id = params.getParameterAsString("id");
			QuestionnaireVO question = (QuestionnaireVO) process.doView(id);
			AnswerVO answer = new AnswerVO();
			answer.setQuestionnaire_id(question.getId());
			answer.setTitle(question.getTitle());
			answer.setContent(question.getContent());
			answer.setQuestionnaire_id(id);

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
	 * 统计投票数
	 * 
	 * @return
	 */
	public String doVoteNumber() {
		try {
			ParamsTable params = getParams();
			String id = params.getParameterAsString("id");
			String holder_id = params.getParameterAsString("holder_id");
			String result = ((QuestionnaireProcessBean) process)
					.doShowVoteNumber(id, holder_id, getUser());
			ServletActionContext.getRequest().setAttribute("DATA", result);
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

	public String getS_title() {
		return s_title;
	}

	public void setS_title(String s_title) {
		this.s_title = s_title;
	}

	public DataPackage<QuestionnaireVO> getDoneDatas() {
		return doneDatas;
	}

	public void setDoneDatas(DataPackage<QuestionnaireVO> doneDatas) {
		this.doneDatas = doneDatas;
	}

}
