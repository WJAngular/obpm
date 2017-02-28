package cn.myapps.core.shortmessage.received.action;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.beanutils.PropertyUtils;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.constans.Web;
import cn.myapps.core.shortmessage.received.ejb.ReceivedMessageProcess;
import cn.myapps.core.shortmessage.received.ejb.ReceivedMessageVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
/**
 * @SuppressWarnings 不支持泛型
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
public class ReceivedMessageAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2199658886883473982L;

	public ReceivedMessageAction() throws Exception {
		super(ProcessFactory.createProcess(ReceivedMessageProcess.class), new ReceivedMessageVO());
	}

	public String doSave() {
		try {
			ReceivedMessageVO pmVO = (ReceivedMessageVO) getContent();
			WebUser user = getWebUser();
			ParamsTable param = getParams();

			if (user != null && pmVO.getSender() == null) {
				pmVO.setSender(user.getTelephone());
			}
			String ids = param.getParameterAsString("receiver");
			Collection<ValueObject> receiverList = new ArrayList<ValueObject>();
			if (ids != null) {
				String[] idArray = ids.split(",");
				for (int i = 0; i < idArray.length; i++) {
					String id = idArray[i];
					ReceivedMessageVO receiverVO = new ReceivedMessageVO();
					PropertyUtils.copyProperties(receiverVO, pmVO);
					receiverVO.setReceiver(id);
					receiverList.add(receiverVO);
				}
				((ReceivedMessageProcess) process).doCreate(receiverList);
				return SUCCESS;
			} else {
				this.addFieldError("1", "没有找到收件人");
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

	private WebUser getWebUser() throws Exception {
		return (WebUser) getContext().getSession().get(Web.SESSION_ATTRIBUTE_FRONT_USER);
	}
}
