package cn.myapps.core.style.repository.action;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.style.repository.ejb.StyleRepositoryProcess;
import cn.myapps.core.style.repository.ejb.StyleRepositoryVO;
import cn.myapps.util.ProcessFactory;


public class StyleRepositoryAction extends BaseAction<StyleRepositoryVO> {


	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private String tipsmessage=null;

	/**
	 * StyleRepositoryAction 构造函数
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @see cn.myapps.base.action.BaseAction#BaseAction(BaseProcess,
	 *      ValueObject)
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public StyleRepositoryAction() throws Exception {
		super(ProcessFactory.createProcess(StyleRepositoryProcess.class),
				new StyleRepositoryVO());
	}

	/**
	 * 保存样式库,保存前验证样式名是否唯一
	 * @author Bluce
	 * @return 处理成功返回"SUCCESS",否则抛出错误
	 * @throws Exception
	 */
	public String doSave() {
		ParamsTable pt = getParams();
		String id = pt.getParameterAsString("content.id");
		String name = pt.getParameterAsString("content.name");
		try {
		
			if(!((StyleRepositoryProcess)process).isStyleNameExist(id, name, application)){
				((StyleRepositoryVO) getContent()).setLastmodifytime(new Date());
				return super.doSave();
			}
			else{
				return INPUT;
			}
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
	
	public String checkExitName(){
		try{
			HttpServletResponse response = ServletActionContext.getResponse();
			String name=this.getParams().getParameterAsString("name");
			String application=this.getParams().getParameterAsString("application");
			boolean nameExit=process.checkExitName(name,application);
			if(!nameExit){
				this.setTipsmessage("false");
				response.getWriter().write(this.getTipsmessage());
			}
			else{
				this.setTipsmessage("true");
				response.getWriter().write(this.getTipsmessage());
			}
			return SUCCESS;

		}catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
	public String getTipsmessage() {
		return tipsmessage;
	}
	public void setTipsmessage(String tipsmessage) {
		this.tipsmessage = tipsmessage;
	}
	
	public String doDelete() {
		try {
			if (_selects != null)
				process.doRemove(_selects);

			addActionMessage("{*[delete.successful]*}");
			return SUCCESS;
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			if(e.getMessage().indexOf("Could not execute JDBC batch update")>-1){
				addFieldError("", "{*[style.has.been.cited]*}");
			}else{
				LOG.error("doDelete", e);
				this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
				e.printStackTrace();
			}
			return INPUT;
		}
	}
}
