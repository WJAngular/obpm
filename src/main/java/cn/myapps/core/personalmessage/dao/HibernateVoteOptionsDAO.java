package cn.myapps.core.personalmessage.dao;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.personalmessage.ejb.VoteOptionsVO;

public class HibernateVoteOptionsDAO extends HibernateBaseDAO<VoteOptionsVO> implements VoteOptionsDAO{

	public HibernateVoteOptionsDAO(String voClassName){
		super(voClassName);
	}
	
	public void doCreate(VoteOptionsVO vo) throws Exception {
		this.create(vo);
	}

	public ValueObject findVoteOptionsVOById(String id) throws Exception {
		return find(id);
	}

}
