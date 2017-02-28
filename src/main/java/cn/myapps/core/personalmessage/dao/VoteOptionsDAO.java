package cn.myapps.core.personalmessage.dao;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.personalmessage.ejb.VoteOptionsVO;

public interface VoteOptionsDAO extends IDesignTimeDAO<VoteOptionsVO>{
	
	public void doCreate(VoteOptionsVO voteoptionsvo)throws Exception;
	
	public ValueObject findVoteOptionsVOById(String id)throws Exception;

}
