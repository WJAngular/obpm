package cn.myapps.core.links.action;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.core.links.ejb.LinkProcess;
import cn.myapps.core.links.ejb.LinkVO;
import cn.myapps.util.ProcessFactory;

/**
 * @author Happy
 *
 */
public class LinkAction extends BaseAction<LinkVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6246619093006382857L;

	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public LinkAction() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(LinkProcess.class), new LinkVO());
	}
	
	
	/** 保存并新建 */
	public String doSaveAndNew() {
		try{
			if(!doValidateLink())return INPUT;
			if (getContent().getId() == null || getContent().getId().equals(""))
				process.doCreate(getContent());
			else
				process.doUpdate(getContent());
				this.addActionMessage("{*[Save_Success]*}");
				LinkVO link = new LinkVO();
				link.setApplicationid(application);
				setContent(new LinkVO());
				return SUCCESS;
			
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
	//保存
	public String doSave(){
		if(!doValidateLink())return INPUT;
		return super.doSave();
	}
	
	//校验link不能重名
	private boolean doValidateLink(){
		boolean isOK = true;
		LinkVO lVO = (LinkVO)getContent();
		if(lVO != null){
			String name = lVO.getName();
			try {
				LinkVO l = (LinkVO)DAOFactory.getDefaultDAO(LinkVO.class.getName()).findByName(name, lVO.getApplicationid());
				if(l != null && !l.getId().equals(lVO.getId())){
					isOK =  false;
					this.addFieldError("existName", "{*[Exist.same.name]*}(" + name + ")");
				}
			} catch (OBPMValidateException e) {
				this.addFieldError("1", e.getValidateMessage());
			}catch (Exception e) {
				this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
				e.printStackTrace();
			}
		}
		return isOK;
	}

}
