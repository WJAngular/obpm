package cn.myapps.core.personalmessage.ejb;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.personalmessage.dao.VoteOptionsDAO;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;

public class VoteOptionsProcessBean extends
		AbstractDesignTimeProcessBean<VoteOptionsVO> implements
		VoteOptionsProcess{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected IDesignTimeDAO<VoteOptionsVO> getDAO() throws Exception {
		return (VoteOptionsDAO) DAOFactory.getDefaultDAO(VoteOptionsVO.class.getName());
	}
	
	public String doCreate(String options) throws Exception {
		if(!StringUtil.isBlank(options)){
			String ids = "";
			String[] optionsvalue = options.split(";");
			for(int i = 0;i < optionsvalue.length; i++){
				if(!StringUtil.isBlank(optionsvalue[i])){
					VoteOptionsVO vo = new VoteOptionsVO();
					String id = Sequence.getSequence();
					vo.setId(id);
					vo.setValue(optionsvalue[i]);
					((VoteOptionsDAO) getDAO()).doCreate(vo);
					ids = ids + id + ";";
				}
			}
			return (ids.endsWith(";"))?ids.substring(0, ids.length()-1):ids;
		}
		return null;
	}

	public ValueObject findVoteOptionsVOById(String id) throws Exception {
		return ((VoteOptionsDAO) getDAO()).findVoteOptionsVOById(id);
	}

}
