package cn.myapps.core.privilege.res.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.permission.ejb.PermissionProcess;
import cn.myapps.core.permission.ejb.PermissionVO;
import cn.myapps.core.privilege.res.ejb.ResProcess;
import cn.myapps.core.privilege.res.ejb.ResVO;
import cn.myapps.core.tree.Node;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.http.ResponseUtil;
import cn.myapps.util.json.JsonUtil;

/**
 * @SuppressWarnings 不支持泛型
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
public class ResAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Collection<Node> childNodes = new ArrayList<Node>();// 树的孩子节点
	
	public ResAction() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(ResProcess.class),
				new ResVO());
	}

	/** 保存并新建 */
	public String doSaveAndNew() {
		super.doSave();
		ResVO resourcesVO = new ResVO();
		resourcesVO.setType(-1);
		setContent(resourcesVO);
		return SUCCESS;
	}

	/**
	 * 显示资源树形结构
	 */
	public void resourcesTree() throws Exception {
		ParamsTable params = getParams();
		childNodes = ((ResProcess) process).getResTree(params);
		ResponseUtil.setJsonToResponse(ServletActionContext.getResponse(),
				JsonUtil.collection2Json(childNodes));
	}

	/**
	 * 删除
	 */
	public String doDelete() {
		try {
			PermissionProcess permissionProcess = (PermissionProcess) ProcessFactory.createProcess(PermissionProcess.class);
			ParamsTable params = this.getParams();
			for(int i=0;i<this.get_selects().length;i++){
			params.setParameter("s_res_id", this.get_selects()[i]);
			DataPackage<PermissionVO> dataPackage=permissionProcess.doQuery(params);
			if(dataPackage.rowCount>0){
				for (Iterator<PermissionVO> iterator = dataPackage.datas.iterator(); iterator.hasNext();) {
					PermissionVO permission = iterator.next();
					if (permission.getResId() != null && permission.getResId().equals(get_selects()[i])) {
						permissionProcess.doRemove(permission);
					}
				}
			}
			}
			return super.doDelete();
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

}
