package cn.myapps.core.workflow.statelabel.action;

import java.util.Iterator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Web;
import cn.myapps.core.workflow.statelabel.ejb.StateLabel;
import cn.myapps.core.workflow.statelabel.ejb.StateLabelProcess;
import cn.myapps.util.ProcessFactory;


/**
 * @see cn.myapps.base.action.BaseAction CommonInfoAction class.
 * @author Darvense
 * @since JDK1.4
 */
public class StateLabelAction extends BaseAction<StateLabel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6445416716227432447L;

	/**
	 * 
	 * CommonInfoAction structure function.
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public StateLabelAction() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(StateLabelProcess.class),
				new StateLabel());
	}

	public String doSelectState() throws Exception {
		ParamsTable params = getParams();
		String application = params.getParameterAsString("application");

		// 清除页面缓存操作
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);

		((StateLabelProcess) process).doQueryState(application);
		return SUCCESS;
	}

	@Override
	public String doSave() {
		StateLabel tempStateLabel = (StateLabel) this.getContent();
		boolean flag = false;
		StateLabel stateLabel = null;
		Iterator<StateLabel> it;
		//this.getParams().setParameter("s_name",this.getParams().getParameterAsString("content.name"));
		this.getParams().setParameter("s_value",
				this.getParams().getParameterAsString("content.value"));
		try {
				it = (((StateLabelProcess) process).doQuery(this.getParams())).datas
						.iterator();
				while (it.hasNext()) {
					stateLabel = (StateLabel) it.next();
					//根据id过滤判断是不是当前表单
					if (!tempStateLabel.getId().trim().equalsIgnoreCase(stateLabel.getId())) {
						this.addFieldError("1", "{*[cn.myapps.core.workflow.label.Exist]*}");
						flag = true;
						break;
					}
				}
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException(
					"{*[OBPMRuntimeException]*}", e));
			e.printStackTrace();
			return INPUT;
		}

		if (!flag) {
			return super.doSave();
		} else
			return INPUT;

	}

	/** 保存并新建 */
	public String doSaveAndNew() {
		StateLabel tempStateLabel = (StateLabel) this.getContent();
		boolean flag = false;
		StateLabel stateLabel = null;
		Iterator<StateLabel> it;
		this.getParams().setParameter("s_name",
				this.getParams().getParameterAsString("content.name"));
		this.getParams().setParameter("s_value",
				this.getParams().getParameterAsString("content.value"));

		try {
			it = (((StateLabelProcess) process).doQuery(this.getParams())).datas
					.iterator();
			while (it.hasNext()) {
				stateLabel = (StateLabel) it.next();
				if (null != stateLabel) {
					break;
				}
			}

			if (stateLabel != null) {
				if (tempStateLabel.getId() == null
						|| tempStateLabel.getId().trim().length() <= 0) {// 判断新建不能重名
					this.addFieldError("1", "{*[LabelExist]*}");
					flag = true;
				} else if (tempStateLabel.getValue().trim().equalsIgnoreCase(
						stateLabel.getValue())
						&& !tempStateLabel.getId().trim().equalsIgnoreCase(
								stateLabel.getId())) {// 修改不能重值
					this.addFieldError("1", "{*[LabelExist]*}");
					flag = true;
				}
			}
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			flag = true;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			flag = true;
		}

		if (!flag) {
			try {
				super.doSave();
				setContent(new StateLabel());
				return SUCCESS;
			} catch (Exception e) {
				this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
				e.printStackTrace();
				return INPUT;
			}
		} else {
			return INPUT;
		}
	}

	public String doList() {
		ParamsTable params = getParams();
		int lines = 10;
		Cookie[] cookies = ServletActionContext.getRequest().getCookies();
		for(Cookie cookie : cookies){
			if(Web.FILELIST_PAGELINE.equals(cookie.getName())){
				lines = Integer.parseInt(cookie.getValue());
			}
		    cookie.getName();
		    cookie.getValue();
		}
		params.removeParameter("_pagelines");
		params.setParameter("_pagelines", lines);
		this.getParams().setParameter("_orderby", "orderNo");
		return super.doList();
	}
}
