package cn.myapps.pm.tag.action;

import java.util.Collection;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.pm.base.action.BaseAction;
import cn.myapps.pm.tag.ejb.Tag;
import cn.myapps.pm.tag.ejb.TagProcess;
import cn.myapps.pm.tag.ejb.TagProcessBean;

public class TagAction extends BaseAction<Tag> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8320027337841030881L;
	
	public TagAction() {
		super();
		content = new Tag();
		process = new TagProcessBean();
	}
	
	public String doCreate(){
		try {
			WebUser user = getUser();
			Tag tag = (Tag) getContent();
			tag.setDomainid(user.getDomainid());
			
			tag = (Tag) process.doCreate(tag);
			addActionResult(true, "添加成功", tag);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
		
	}
	
	public String doDelete(){
		try {
			process.doRemove(get_selects());
			addActionResult(true, "删除成功", null);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	public String doQuery() {
		try {
			
			Collection<Tag> list = ((TagProcess)process).doSimpleQuery(getParams(), getUser());
			addActionResult(true, "", list);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			if(!(e instanceof OBPMValidateException)){
				e.printStackTrace();
			}
		}
		
		return SUCCESS;
	}
	

}
