package cn.myapps.km.baike.content.action;


import java.util.Collection;
import java.util.Date;

import cn.myapps.km.baike.content.ejb.ReferenceMaterial;
import cn.myapps.km.baike.content.ejb.ReferenceMaterialProcessBean;
import cn.myapps.km.base.action.AbstractRunTimeAction;
import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.ejb.NRunTimeProcess;

import com.opensymphony.xwork2.ActionContext;

public class ReferenceMaterialAction extends AbstractRunTimeAction<ReferenceMaterial>{

	private static final long serialVersionUID = -6221669399674723887L;

	@Override
	public NRunTimeProcess<ReferenceMaterial> getProcess() {
		// TODO Auto-generated method stub
		return new ReferenceMaterialProcessBean();
	}

	public ReferenceMaterialAction(){
		super();
		this.process=getProcess();
	}
	
	/**
	 * 词条内容的Id
	 */
	private String entryContentId;
	
	/**
	 * 文章名
	 */
	private String articleName;
	
	/**
	 * URL
	 */
	private String url;
	
	/**
	 * 网站名
	 */
	private String webName;
	
	/**
	 * 发表日期
	 */
	private Date publishDate;
	
	/**
	 * 引用日期
	 */
	private Date referenceDate;


	/**
	 * 参考资料
	 */
	private ReferenceMaterial referenceMaterial;
	
	/**
	 * 参考资料
	 */
	private Collection<ReferenceMaterial> referenceMaterials;
	

	
	public String getEntryContentId() {
		return entryContentId;
	}

	public void setEntryContentId(String entryContentId) {
		this.entryContentId = entryContentId;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWebName() {
		return webName;
	}

	public void setWebName(String webName) {
		this.webName = webName;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	
	public Collection<ReferenceMaterial> getReferenceMaterials() {
		return referenceMaterials;
	}
	public Date getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(Date referenceDate) {
		this.referenceDate = referenceDate;
	}
	
	
	public ReferenceMaterial getReferenceMaterial() {
		return referenceMaterial;
	}

	public void setReferenceMaterial(ReferenceMaterial referenceMaterial) {
		this.referenceMaterial = referenceMaterial;
	}

	public void setReferenceMaterials(
			Collection<ReferenceMaterial> referenceMaterials) {
		this.referenceMaterials = referenceMaterials;
	}

	/**
	 * 根据编号查询用户
	 * @return
	 */
	public String doView(){
		try {
			//this.getRequest().put("Econtent", content);
			((ActionContext) new ParamsTable().getHttpRequest()).put("ReferenceMaterial", referenceMaterial);
			return "findId";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 增加用户
	 * @return
	 */
	public String doCreate(){
		try {
			((ReferenceMaterialProcessBean)getProcess()).doCreate(referenceMaterial);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 删除用户
	 * @return
	 */
	public String doRemove(){
		try {
			((ReferenceMaterialProcessBean)getProcess()).doRemove(referenceMaterial.getId());
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 修改用户
	 * @return
	 */
	public String doUpdate(){
		try {
			((ReferenceMaterialProcessBean)getProcess()).doUpdate(referenceMaterial);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	
	public String doList(){
		try {
			referenceMaterials =((ReferenceMaterialProcessBean)getProcess()).getReferenceMaterials(entryContentId);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
}
