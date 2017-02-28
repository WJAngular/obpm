package cn.myapps.core.dynaform.form.ejb;

import java.util.Date;

/**
 * word文档占用(正在被使用)对象
 * @author ivan
 *
 */
public class WordFieldIsEdit {
	
	/**
	 * 2分钟
	 */
	public final static int JOB_PEIROD = 2 * 60 * 1000; // 2分钟
	/**
	 * word文件名(id)
	 */
	private String wordid;
	
	/**
	 * 正在使用word文档的用户id
	 */
	private String userid;
	
	/**
	 * 正在使用word文档的用户名称
	 */
	private String username;
	
	/**
	 * 是否激活
	 */
	private boolean isEditAble;
	
	/**
	 * 激活时间
	 */
	private Date editTime;

	public String getWordid() {
		return wordid;
	}

	public void setWordid(String wordid) {
		this.wordid = wordid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isEditAble() {
		return isEditAble;
	}

	public void setEditAble(boolean isEditAble) {
		this.isEditAble = isEditAble;
	}

	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}

}
