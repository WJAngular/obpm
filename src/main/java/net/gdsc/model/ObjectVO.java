/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.model;

import java.util.Date;



/** 
 * @ClassName: ObjectVO 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-07-08 下午3:18:46 
 *  
 */
public class ObjectVO {
//	标识
	private String id;
//	父文档ID
	private String parent;
//	记录最后修改时间
	private Date lastmodified;
//	表单名
	private String formname;
//	表t_flowstatert的主键标识
	private String state;
//	获取最后审核人用户ID
	private String audituser;
//	流程最后处理时间
	private Date auditdate;
//	建单者对应的用户ID
	private String author;
//	建单者对应的默认部门ID
	private String author_dept_index;
//	表单创建时间
	private Date created;
//	表单ID
	private String formid;
//	是否为临时文档
	private Boolean istmp;
//	表单版本
	private int versions;
//	应用ID
	private String applicationid;
//	流程状态
	private int stateint;
//	流程当前状态标签
	private String statelabel;
//	流程当前审批人名称
	private String auditornames;
//	表单最后流程操作
	private String lastflowoperation;
//	最后修改用户
	private String lastmodifier;
//	域标识ID
	private String domainid;
//	获取当前审批人用户ID列表
	private String auditorlist;
//	流程状态的详细信息
	private String statelabelinfo;
//	流程状态 上一步节点
	private String prevauditnode;
//	流程状态 上一步的处理人
	private String prevaudituser;
	
	private String optionitem;
	
	public ObjectVO() {
		this.id = null;
		this.parent = null;
		this.lastmodified = null;
		this.formname = null;
		this.state = null;
		this.audituser = null;
		this.auditdate = null;
		this.author = null;
		this.author_dept_index = null;
		this.created = null;
		this.formid = null;
		this.istmp = null;
		this.versions = 0;
		this.applicationid = null;
		this.stateint = 0;
		this.statelabel = null;
		this.auditornames = null;
		this.lastflowoperation = null;
		this.lastmodifier = null;
		this.domainid = null;
		this.auditorlist = null;
		this.statelabelinfo = null;
		this.prevauditnode = null;
		this.prevaudituser = null;
		this.optionitem = null;
	}
	//	 get/set
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the parent
	 */
	public String getParent() {
		return parent;
	}
	/**
	 * @param parent the parent to set
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}
	/**
	 * @return the lastmodified
	 */
	public Date getLastmodified() {
		return lastmodified;
	}
	/**
	 * @param lastmodified the lastmodified to set
	 */
	public void setLastmodified(Date lastmodified) {
		this.lastmodified = lastmodified;
	}
	/**
	 * @return the formname
	 */
	public String getFormname() {
		return formname;
	}
	/**
	 * @param formname the formname to set
	 */
	public void setFormname(String formname) {
		this.formname = formname;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the audituser
	 */
	public String getAudituser() {
		return audituser;
	}
	/**
	 * @param audituser the audituser to set
	 */
	public void setAudituser(String audituser) {
		this.audituser = audituser;
	}
	/**
	 * @return the auditdate
	 */
	public Date getAuditdate() {
		return auditdate;
	}
	/**
	 * @param auditdate the auditdate to set
	 */
	public void setAuditdate(Date auditdate) {
		this.auditdate = auditdate;
	}
	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @return the author_dept_index
	 */
	public String getAuthor_dept_index() {
		return author_dept_index;
	}
	/**
	 * @param author_dept_index the author_dept_index to set
	 */
	public void setAuthor_dept_index(String author_dept_index) {
		this.author_dept_index = author_dept_index;
	}
	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}
	/**
	 * @return the formid
	 */
	public String getFormid() {
		return formid;
	}
	/**
	 * @param formid the formid to set
	 */
	public void setFormid(String formid) {
		this.formid = formid;
	}
	/**
	 * @return the istmp
	 */
	public Boolean getIstmp() {
		return istmp;
	}
	/**
	 * @param istmp the istmp to set
	 */
	public void setIstmp(Boolean istmp) {
		this.istmp = istmp;
	}
	/**
	 * @return the versions
	 */
	public int getVersions() {
		return versions;
	}
	/**
	 * @param versions the versions to set
	 */
	public void setVersions(int versions) {
		this.versions = versions;
	}
	/**
	 * @return the applicationid
	 */
	public String getApplicationid() {
		return applicationid;
	}
	/**
	 * @param applicationid the applicationid to set
	 */
	public void setApplicationid(String applicationid) {
		this.applicationid = applicationid;
	}
	/** 
	 * @return optionitem 
	 */
	public String getOptionitem() {
		return optionitem;
	}
	/** 
	 * @param optionitem 要设置的 optionitem 
	 */
	public void setOptionitem(String optionitem) {
		this.optionitem = optionitem;
	}
	/**
	 * @return the stateint
	 */
	public int getStateint() {
		return stateint;
	}
	/**
	 * @param stateint the stateint to set
	 */
	public void setStateint(int stateint) {
		this.stateint = stateint;
	}
	/**
	 * @return the statelabel
	 */
	public String getStatelabel() {
		return statelabel;
	}
	/**
	 * @param statelabel the statelabel to set
	 */
	public void setStatelabel(String statelabel) {
		this.statelabel = statelabel;
	}
	/**
	 * @return the auditornames
	 */
	public String getAuditornames() {
		return auditornames;
	}
	/**
	 * @param auditornames the auditornames to set
	 */
	public void setAuditornames(String auditornames) {
		this.auditornames = auditornames;
	}
	/**
	 * @return the lastflowoperation
	 */
	public String getLastflowoperation() {
		return lastflowoperation;
	}
	/**
	 * @param lastflowoperation the lastflowoperation to set
	 */
	public void setLastflowoperation(String lastflowoperation) {
		this.lastflowoperation = lastflowoperation;
	}
	/**
	 * @return the lastmodifier
	 */
	public String getLastmodifier() {
		return lastmodifier;
	}
	/**
	 * @param lastmodifier the lastmodifier to set
	 */
	public void setLastmodifier(String lastmodifier) {
		this.lastmodifier = lastmodifier;
	}
	/**
	 * @return the domainid
	 */
	public String getDomainid() {
		return domainid;
	}
	/**
	 * @param domainid the domainid to set
	 */
	public void setDomainid(String domainid) {
		this.domainid = domainid;
	}
	/**
	 * @return the auditorlist
	 */
	public String getAuditorlist() {
		return auditorlist;
	}
	/**
	 * @param auditorlist the auditorlist to set
	 */
	public void setAuditorlist(String auditorlist) {
		this.auditorlist = auditorlist;
	}
	/**
	 * @return the statelabelinfo
	 */
	public String getStatelabelinfo() {
		return statelabelinfo;
	}
	/**
	 * @param statelabelinfo the statelabelinfo to set
	 */
	public void setStatelabelinfo(String statelabelinfo) {
		this.statelabelinfo = statelabelinfo;
	}
	/**
	 * @return the prevauditnode
	 */
	public String getPrevauditnode() {
		return prevauditnode;
	}
	/**
	 * @param prevauditnode the prevauditnode to set
	 */
	public void setPrevauditnode(String prevauditnode) {
		this.prevauditnode = prevauditnode;
	}
	/**
	 * @return the prevaudituser
	 */
	public String getPrevaudituser() {
		return prevaudituser;
	}
	/**
	 * @param prevaudituser the prevaudituser to set
	 */
	public void setPrevaudituser(String prevaudituser) {
		this.prevaudituser = prevaudituser;
	}
	
	
}
