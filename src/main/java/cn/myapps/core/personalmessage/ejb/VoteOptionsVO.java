package cn.myapps.core.personalmessage.ejb;

import java.io.Serializable;

import cn.myapps.base.dao.ValueObject;

public class VoteOptionsVO extends ValueObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String value;
	
	private String voters;
	
	private int votes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getVoters() {
		return voters;
	}

	public void setVoters(String voters) {
		this.voters = voters;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

}
