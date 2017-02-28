package cn.myapps.core.dynaform.summary.ejb;


import java.util.Collection;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.dynaform.summary.dao.SummaryCfgDAO;

/**
 * @author Happy
 *
 */
public class SummaryCfgProcessBean extends
		AbstractDesignTimeProcessBean<SummaryCfgVO> implements SummaryCfgProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1598156119769582486L;

	@Override
	protected IDesignTimeDAO<SummaryCfgVO> getDAO() throws Exception {
		return (SummaryCfgDAO) DAOFactory.getDefaultDAO(SummaryCfgVO.class.getName());
	}

	public Collection<SummaryCfgVO> doQueryByFormId(String formId) throws Exception {
		return ((SummaryCfgDAO)this.getDAO()).queryByFormId(formId);
	}

	public SummaryCfgVO doViewByFormIdAndScope(String formId, int scope) throws Exception{
		return ((SummaryCfgDAO)this.getDAO()).findByFormIdAndScope(formId, scope);
	}

	public DataPackage<SummaryCfgVO> doQueryHomePageSummaryCfgs(
			ParamsTable params) throws Exception {
		return ((SummaryCfgDAO)this.getDAO()).queryHomePageSummaryCfgs(params);
	}

	public boolean isExistWithSameTitle(String title, String applicationId)
			throws Exception {
		return ((SummaryCfgDAO)this.getDAO()).isExistWithSameTitle(title, applicationId);
	}

	public void doCreateOrUpdate(ValueObject vo) throws Exception {
		try {
			validateSummaryCfg((SummaryCfgVO)vo);
			super.doCreateOrUpdate(vo);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	/**
	 * 校验待保存的摘要
	 * @param vo
	 * @throws Exception
	 */
	private void validateSummaryCfg(SummaryCfgVO vo) throws Exception {
		try {
			if(vo.getScope()==SummaryCfgVO.SCOPE_PENDING){
				SummaryCfgVO that = doViewByFormIdAndScope(vo.getFormId(), SummaryCfgVO.SCOPE_PENDING);
				if(that != null && !that.getId().equals(vo.getId())){
					throw new OBPMValidateException("{*[cn.myapps.core.dynaform.summary.do_save_error]*}");
				}
			}
			ParamsTable p = new ParamsTable();
			p.setParameter("t_title", vo.getTitle());
			Collection<SummaryCfgVO> scvo_list = doSimpleQuery(p, vo.getApplicationid());
			if (!scvo_list.isEmpty()) {
				SummaryCfgVO scvo = scvo_list.iterator().next();
				if (!scvo.getId().equals(vo.getId())) {
					throw new OBPMValidateException("{*[cn.myapps.core.dynaform.summary.do_save_error_title]*}");
				}
			}
			if(vo.getScope() == 6){
				ParamsTable p2 = new ParamsTable();
				p2.setParameter("t_formid", vo.getFormId());
				p2.setParameter("t_scope", 0);
				Collection<SummaryCfgVO> scpendvo_list =doSimpleQuery(p2, vo.getApplicationid());
				if(scpendvo_list.isEmpty()){
					throw new OBPMValidateException("{*[cn.myapps.core.dynaform.summary.do_save_error_summary]*}");
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	
}
