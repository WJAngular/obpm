package cn.myapps.core.personalmessage.ejb;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IDesignTimeProcess;

public interface VoteOptionsProcess extends
		IDesignTimeProcess<VoteOptionsVO>{

	public abstract String doCreate(String options) throws Exception;
	
	public ValueObject findVoteOptionsVOById(String id)throws Exception;
}
