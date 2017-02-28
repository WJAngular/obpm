package cn.myapps.core.validate.repository.action;

import java.util.Date;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.validate.repository.ejb.ValidateRepositoryProcess;
import cn.myapps.core.validate.repository.ejb.ValidateRepositoryVO;
import cn.myapps.util.ProcessFactory;

public class ValidateRepositoryAction extends BaseAction<ValidateRepositoryVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 默认构造方法
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ValidateRepositoryAction() throws Exception {
		super(ProcessFactory.createProcess(ValidateRepositoryProcess.class),
				new ValidateRepositoryVO());
	}

	/**
	 * 保存前做名称唯一性验证
	 * 修改者：Bluce
	 * 修改时间：2010－05－06
	 */
	public String doSave() {
		ParamsTable pt = getParams();
		String id = pt.getParameterAsString("content.id");
		String name = pt.getParameterAsString("content.name");
		try {
			if(!((ValidateRepositoryProcess)process).isValidateNameExist(id, name, application)){
				// 设置修改日期
				((ValidateRepositoryVO) getContent()).setLastmodifytime(new Date());
				return super.doSave();
			}
			else{
				return INPUT;
			}
		}catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
}
