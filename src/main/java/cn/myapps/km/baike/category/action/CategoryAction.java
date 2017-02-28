package cn.myapps.km.baike.category.action;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;


import cn.myapps.km.baike.category.ejb.Category;
import cn.myapps.km.baike.category.ejb.CategoryProcess;
import cn.myapps.km.baike.category.ejb.CategoryProcessBean;
import cn.myapps.km.base.action.AbstractRunTimeAction;
import cn.myapps.km.base.ejb.NRunTimeProcess;

/** 
 * @author abel
 * 用户表的基本操作
 *
 */
public class CategoryAction extends AbstractRunTimeAction<Category>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1602445838173521958L;

	/**
	 * 分类
	 */
	private Category category;
	
	/**
	 * 词条id
	 */
	private String entryId;
	
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	

	public String getEntryId() {
		return entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	public CategoryAction(){
		super();
		this.process=getProcess();
	}
	
	/**
	 * 创建词条分类
	 * @return
	 * @throws Exception
	 */
	public String doSave() throws Exception {
		try {
			process.doCreate(category);
			return SUCCESS;
		} catch(Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 通过词条id查找词条所属类别
	 * @return
	 * @throws Exception
	 */
	public void doFindByEntryId() throws Exception {
		try {
			Category category =  ((CategoryProcess)process).doFindByEntryId(entryId);
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			String jsonObject = "{\"categoryName\":\""+category.getName()+"\"}";
			response.getWriter().write(jsonObject);
			//return SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return ERROR;
		}
	}
	
	
	/**
	 * 查找所有分类
	 */
	public String doList() throws Exception {
		try {
			this.getDatas().setDatas(((CategoryProcess)process).doQuery());
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		
	}
	
	@Override
	public NRunTimeProcess<Category> getProcess() {
		// TODO Auto-generated method stub
		return new CategoryProcessBean();
	}
}

