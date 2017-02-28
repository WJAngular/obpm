package cn.myapps.pm.task.ejb;

import java.io.Serializable;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import cn.myapps.util.sequence.Sequence;
import cn.myapps.util.sequence.SequenceException;

/**
 * 任务备注
 * @author Happy
 *
 */
public class Remark implements Serializable {

	private static final long serialVersionUID = -8137665829322259302L;
	
	private String id;
	/**
	 * 备注内容
	 */
	private String content;
	
	/**
	 *创建日期 
	 */
	private Date createDate;

	/**
	 *创建人
	 */
	private String createRemarkUser;
	
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Remark(){
		super();
	}
	
	public Remark(String content){
		try {
			this.id = Sequence.getSequence();
		} catch (SequenceException e) {
			e.printStackTrace();
		}
		this.content = content;
		this.createDate = new Date();
	}

	public Remark(String remarkContent, String name) {
		try {
			this.id = Sequence.getSequence();
		} catch (SequenceException e) {
			e.printStackTrace();
		}
		this.content = remarkContent;
		this.createRemarkUser=name;
		this.createDate = new Date();
	}

	public String getCreateRemarkUser() {
		return createRemarkUser;
	}

	public void setCreateRemarkUser(String createRemarkUser) {
		this.createRemarkUser = createRemarkUser;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@JSON(format="yyyy-MM-dd HH:mm")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
