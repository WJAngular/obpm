package cn.myapps.core.workflow.storage.definition.ejb;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.counter.ejb.CounterProcessBean;
import cn.myapps.core.workflow.storage.definition.dao.BillDefiDAO;
import cn.myapps.util.ObjectUtil;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;
import cn.myapps.util.sequence.Sequence;

public class BillDefiProcessBean extends
		AbstractDesignTimeProcessBean<BillDefiVO> implements BillDefiProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6215310765110250933L;
	
	protected IDesignTimeDAO<BillDefiVO> getDAO() throws Exception {
		return (BillDefiDAO) DAOFactory.getDefaultDAO(BillDefiVO.class.getName());
	}

	public Collection<BillDefiVO> getBillDefiByModule(String moduleid)
			throws Exception {
		return ((BillDefiDAO) getDAO()).getBillDefiByModule(moduleid);
	}

	public ValueObject doView(String pk) throws Exception {
		return super.doView(pk);
	}

	public BillDefiVO doViewBySubject(String subject, String applicationId)
			throws Exception {
		return ((BillDefiDAO) getDAO()).findBySubject(subject, applicationId);
	}

	public boolean isSubjectExisted(BillDefiVO vo) throws Exception {
		BillDefiVO po = (BillDefiVO) doView(vo.getId());
		if (po == null || !vo.getSubject().equals(po.getSubject())) {
			if (!StringUtil.isBlank(vo.getSubject())) {
				ParamsTable params = new ParamsTable();
				params.setParameter("t_subject", vo.getSubject());
				Collection<BillDefiVO> billDefiList = doSimpleQuery(params, vo
						.getApplicationid());
				if (!billDefiList.isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void doCopyFlow(String[] flowIds) throws Exception{
		try {
			for(int i=0; i<flowIds.length; i++){
				
				BillDefiVO oldFlow = (BillDefiVO) doView(flowIds[i]);
				
				BillDefiVO newFlow = new BillDefiVO();
				ObjectUtil.copyProperties(newFlow, oldFlow);
				newFlow.setId(Sequence.getSequence());
				int count = new CounterProcessBean(newFlow.getApplicationid()).getNextValue("FLOW_" + oldFlow.getSubject(), newFlow.getApplicationid(), newFlow.getDomainid());
				newFlow.setSubject(oldFlow.getSubject() + count);
				
				doCreate(newFlow);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void updateOpinion(String pk,String opinion) throws Exception {
		//先获取数据库中流程的常用意见
		BillDefiVO bi = (BillDefiVO)super.doView(pk);
		String opinionjson = bi.getOpinion();
		if(opinionjson==null||opinionjson.equals(""))
			opinionjson = "{}";
		
		//把流程中的常用意见转换成map，并把新增的常用意见添加进去
		Map<String, Object> map = JsonUtil.toMap(opinionjson);
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put(Sequence.getSequence(), opinion);
		map.putAll(map1);
		opinionjson = JsonUtil.toJson(map);
		bi.setOpinion(opinionjson);
		doUpdate(bi);
	}
	
	public void removeOpinion(String pk,String opinionid) throws Exception {
		//先获取数据库中流程的常用意见
		BillDefiVO bi = (BillDefiVO)super.doView(pk);
		String opinionjson = bi.getOpinion();
		if(opinionjson==null||opinionjson.equals("")){
			opinionjson = "{}";
			return;
		}
		//把流程中的常用意见转换成map，并把要移除的常用意见去掉
		Map<String, Object> map = JsonUtil.toMap(opinionjson);
		map.remove(opinionid);
		opinionjson = JsonUtil.toJson(map);
		bi.setOpinion(opinionjson);
		doUpdate(bi);
	}
}
