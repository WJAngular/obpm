package cn.myapps.pm.task.ejb;

import java.io.Serializable;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import cn.myapps.util.sequence.Sequence;
import cn.myapps.util.sequence.SequenceException;

/**
 * 子任务
 * 
 * @author Happy
 *
 */
public class SubTask implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4710690402458196058L;
	public static final int STATUS_COMPLETE = 1;
	public static final int STATUS_UNCOMPLETE = 0;
	
	private String id;
	
	/**
	 * 任务名称
	 */
	private String name;
	
	/**
	 * 创建日期
	 */
	private Date createDate;
	
	/**
	 * 完成状态
	 */
	private int status;
	
	public SubTask(){
		super();
	}
	
	public SubTask(String name){
		try {
			this.id = Sequence.getSequence();
		} catch (SequenceException e) {
			e.printStackTrace();
		}
		this.name = name;
		this.createDate = new Date();
		this.status = STATUS_UNCOMPLETE;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@JSON(format="yyyy-MM-dd HH:mm")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
