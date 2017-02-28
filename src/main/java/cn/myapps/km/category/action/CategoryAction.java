package cn.myapps.km.category.action;

import cn.myapps.km.base.action.AbstractRunTimeAction;
import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.ejb.NRunTimeProcess;
import cn.myapps.km.category.ejb.Category;
import cn.myapps.km.category.ejb.CategoryProcessBean;
import cn.myapps.util.StringUtil;

public class CategoryAction extends AbstractRunTimeAction<Category> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3779795799527511792L;
	
	private String targetId;
	
	

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public CategoryAction() {
		super();
		content = new Category();
	}

	public NRunTimeProcess<Category> getProcess() {
		return new CategoryProcessBean();
	}
	
	public String doNew(){
		try {
			content.setDomainId(getUser().getDomainid());
		} catch (Exception e) {
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	public String doSave(){
		try {
			Category category = (Category) getContent();
			
			if(StringUtil.isBlank(category.getId())){
				if(StringUtil.isBlank(category.getDomainId())) category.setDomainId(getUser().getDomainid());
				getProcess().doCreate(category);
			}else{
				getProcess().doUpdate(category);
			}
			addActionMessage("保存成功！");
			setTargetId(category.getId());
			
		} catch (Exception e) {
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	public String doDelete(){
		try {
			Category category = (Category) getContent();
			getProcess().doRemove(category.getId());
			addActionMessage("删除成功！");
		} catch (Exception e) {
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	public String doView(){
		try {
			ParamsTable params = getParams();
			String id = params.getParameterAsString("id");
			if(!StringUtil.isBlank(id)){
				Category category = (Category) getProcess().doView(id);
				setContent(category);
			}
		} catch (Exception e) {
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	public String doList(){
		try {
			
		} catch (Exception e) {
			return INPUT;
		}
		
		return SUCCESS;
	}

}
