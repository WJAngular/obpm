package cn.myapps.qm.questionnaire.action;



import java.io.File;
import java.util.Date;

import org.apache.struts2.ServletActionContext;
import org.jfree.util.Log;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.qm.answer.ejb.AnswerVO;
import cn.myapps.qm.base.action.BaseAction;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireProcess;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireProcessBean;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireVO;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.DefaultProperty;
/**
 * 
 * @author ahan
 *
 */
public class QuestionServiceAction extends BaseAction<QuestionnaireVO> {

	private static final long serialVersionUID = 1884974075510421174L;
	
	public QuestionServiceAction() {
		super();
		content = new QuestionnaireVO();
		process = new QuestionnaireProcessBean();
	}

	/**
	 * 待填写问卷
	 * 
	 * @return
	 */
	public String doList() {
		try {
			//传入参数
			//currpage 需要获取的当前页数 默认为1
			//pagelines 页面列表大小 默认为50
			ParamsTable params = getParams();
			String title = params.getParameterAsString("title");
			if (title == null)
				title = "";
			String _currpage = params.getParameterAsString("_currpage");
			String _pagelines = params.getParameterAsString("_pagelines");

			int page = (_currpage != null && _currpage.length() > 0) ? Integer
					.parseInt(_currpage) : 1;
			int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer
					.parseInt(_pagelines) : 50;
			DataPackage<QuestionnaireVO> dataPackage = ((QuestionnaireProcess) process)
					.doQueryMyPartake(title, AnswerVO.STATUS_FILLING, page, lines, getUser());

			addActionResult(true, "", dataPackage);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		return SUCCESS;
	}

	/**
	 * 删除问卷
	 * 
	 * @return
	 */
	public String doDelete() {
		try {
			ParamsTable params = getParams();
			String[] _select = params.getParameterAsArray("_select");
			process.doRemove(_select);
			addActionResult(true, "", null);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		return SUCCESS;
	};

	/**
	 * 问卷编辑
	 */
	public String doEdit() {
		ParamsTable params = getParams();
		String id = params.getParameterAsString("id");
		try {
			QuestionnaireVO vo = (QuestionnaireVO) process.doView(id);
			if (vo == null)
				throw new Exception("找不到记录");
			addActionResult(true, "", vo);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			addActionResult(false, e.getMessage(), null);
		}
		return SUCCESS;
	}
	
	public String doShowResult() {
		try {
			ParamsTable params = getParams();
			String id = params.getParameterAsString("id");
			QuestionnaireVO vo = ((QuestionnaireProcessBean) process).doShowResult(id,
					getUser());
			addActionResult(true, "", vo);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		return SUCCESS;

	}
	
	/**
	 * 获取报表数据
	 * @return
	 */
	public String doReportForm() {
		try {
			ParamsTable params = getParams();
			String id = params.getParameterAsString("id");
			QuestionnaireVO vo = ((QuestionnaireProcessBean) process).showReportForm(id);
			addActionResult(true, "", vo);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		return SUCCESS;

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
			
			addActionResult(true, "", question);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		return SUCCESS;
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
			question = (QuestionnaireVO) ((QuestionnaireProcess) process).doSave(question);

			addActionResult(true, "", question);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
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

			addActionResult(true, "", result);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		return SUCCESS;
	}

	/**
	 * 问卷中心页面
	 * 
	 * @return
	 */
	public String questionnairCenter() {
		try {
			// 传入参数
			// title 需要搜索的标题
			// type 值为0或1 0代表 我发布的问卷 1代表我参与的问卷
			// currpage 需要获取的当前页数 默认为1
			// pagelines 页面列表大小 默认为50
			// status 问卷分类参数，
			// ------当type为我发布的问卷时
			// ---------status==0代表草稿 ==1代表已发布 ==2代表已回收
			// ------当type为我参与的问卷时
			// ---------status==1代表待填写的问卷 ==2代表已填写的问卷
			ParamsTable params = getParams();
			String title = params.getParameterAsString("title");
			if (title == null)
				title = "";
			String _type = params.getParameterAsString("type");
			String _currpage = params.getParameterAsString("_currpage");
			String _pagelines = params.getParameterAsString("_pagelines");
			String _status = params.getParameterAsString("status");

			int type = QuestionnaireVO.TYPE_PUBLISH;
			if (!StringUtil.isBlank(_type)) {
				type = Integer.parseInt(_type);
			}
			int page = (_currpage != null && _currpage.length() > 0) ? Integer
					.parseInt(_currpage) : 1;
			int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer
					.parseInt(_pagelines) : 50;

			int status = QuestionnaireVO.STATUS_ALL;
			if (!StringUtil.isBlank(_status)) {
				status = Integer.parseInt(_status);
			}

			DataPackage<QuestionnaireVO> dataPackage = null;
			
			if (QuestionnaireVO.TYPE_PUBLISH == type) {
				dataPackage = ((QuestionnaireProcess) process)
						.doQueryMyPublish(title, status, page, lines, getUser());
			} else if (QuestionnaireVO.TYPE_PARTAKE == type) {
				dataPackage = ((QuestionnaireProcess) process)
						.doQueryMyPartake(title, status, page, lines, getUser());
			} else {
				throw new Exception("必须传入正确的参数type");
			}
			addActionResult(true, null, dataPackage);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		return SUCCESS;
	}
	
	/**
	 * 回收问卷
	 * @return
	 */
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
				}
			}
			addActionResult(true, null, null);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
    /**
     * 根据附近url删除对应的图片
     * 
     * @return
     */
    public String doDeleteAttachment() {
	try {
	    ParamsTable params = getParams();
	    String url = params.getParameterAsString("url");

	    // 服务器目录的绝对路径
	    String realPath = ServletActionContext.getServletContext().getRealPath("");

	    // 存放此文件的绝对路径
	    String savePath = realPath + url;
	    
	    //调查问卷的存储目录
	    String uploadPath = DefaultProperty.getProperty("myapps.qm.upload.path","/uploads/qm/");
	    if(!savePath.contains(uploadPath)){
		throw new Exception("非法的文件路径");
	    }
	    File file = new File(savePath);
	    
	    // 删除文件
	    file.delete();

	    addActionResult(true, null, "");
	} catch (Exception e) {
	    Log.error(e.getMessage());
	    addActionResult(false, e.getMessage(), null);
	    if (!(e instanceof OBPMValidateException)) {
		e.printStackTrace();
	    }
	}
	return SUCCESS;
    }
}
