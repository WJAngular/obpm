package cn.myapps.core.dynaform.view.action;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityParent;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.dynaform.view.ejb.ViewProcessBean;
import cn.myapps.core.dynaform.view.ejb.ViewType;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.permission.ejb.PermissionProcess;
import cn.myapps.core.privilege.operation.ejb.OperationVO;
import cn.myapps.core.privilege.res.ejb.ResVO;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.tree.DocumentTree;
import cn.myapps.core.tree.Node;
import cn.myapps.core.tree.Tree;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.http.ResponseUtil;


public class ViewRunTimeAction extends ViewAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 970248216937958653L;
	
	private String docid;
	
	private String columnId;
	
	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public String getDocid() {
		return docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}

	public ViewRunTimeAction() throws ClassNotFoundException {
		super();
	}

	public String getWebUserSessionKey() {
		return Web.SESSION_ATTRIBUTE_FRONT_USER;
	}


	public String doSearch() {
		try {
			WebUser user = getUser();
			Document sDoc = getSearchDocument(view);

			DocumentTree tree = new DocumentTree(view, getParams(), user, sDoc);
			tree.search();

			ResponseUtil.setJsonToResponse(ServletActionContext.getResponse(), tree.toSearchJSON());
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

		return SUCCESS;
	}
	
	/**
	 * 查询表单
	 * 
	 * @return
	 */
	public String displaySearchForm() {
		try {
			Document searchDocument = getSearchDocument((View) getContent());
			// 设置Action属性
			setCurrentDocument(searchDocument);
			setParent(null);
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}
	
	/**
	 * 显示视图view数据列表(前台调用)
	 * 
	 * @return result.
	 * @throws Exception
	 */
	public String doDisplayView() throws Exception {
		try {
			WebUser user = getUser();
			ParamsTable params = getParams();
			Collection<RoleVO> roles = user.getRoles();
			
			boolean allow = true;
			ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			PermissionProcess process = (PermissionProcess) ProcessFactory.createProcess(PermissionProcess.class);
			
			View view = (View)viewProcess.doView(_viewid);
			
			//String skin = (String) ActionContext.getContext().getSession().get("SKINTYPE");
			String from = params.getParameterAsString("_from");
			if("includedView".equalsIgnoreCase(from) && view.getOpenType()!=View.OPEN_TYPE_DIV){
				view = (View) BeanUtils.cloneBean(view);
				view.setOpenType(View.OPEN_TYPE_DIV);
				view.setViewType(View.VIEW_TYPE_NORMAL);
				setContent(view);
			}
			
			
			if(!View.PERMISSION_TYPE_PUBLIC.equals(view.getPermissionType())){
				allow = process.check(roles, this._viewid, this._viewid, OperationVO.FORM_VIEW_ALLOW_OPEN, ResVO.VIEW_TYPE, application);
			}
			if(!allow){
				return "forbid";
			}
			return getSuccessResult((View) getContent(), DO_DISPLAY_VIEW);
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return getInputResult((View) getContent());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return getInputResult((View) getContent());
		}
	}

	
	public String getChildren() {
		try {
			WebUser user = getUser();
			Document sDoc = getSearchDocument(view);

			// 添加关联条件
			ViewType viewType = view.getViewTypeImpl();
			Tree<Document> tree = new DocumentTree(view, getParams(), user, sDoc);
			// 解析并获取子文档
			Collection<Document> children = viewType.getViewDatas(getParams(), user, sDoc).getDatas();
			tree.parse(children);

			Collection<Node> childNodes = tree.getChildNodes();
			for (Iterator<Node> iterator = childNodes.iterator(); iterator.hasNext();) {
				Node node = (Node) iterator.next();
				node.addAttr("viewid", get_viewid()); // 当前视图ID
				node.addAttr("curr_node", node.getAttr().get("nodeValue"));
				node.addAttr("super_node_fieldName", viewType.getColumnMapping().get("superior_Node").getFieldName());
			}

			ResponseUtil.setJsonToResponse(ServletActionContext.getResponse(), tree.toJSON());
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

		return NONE;
	}
	
	/**
	 * 显示子表单视图view数据列表(前台调用)
	 * 
	 * @return result.
	 * @throws Exception
	 */
	public String doSubFormView() throws Exception {
		try {
			displayByView((View) getContent());
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}
	
	/**
	 * 显示视图view数据列表(前台视图控件调用)
	 * 
	 * @return result.
	 * @throws Exception
	 */
	public String doDialogView() throws Exception {
		try {
			ParamsTable params = getParams();
			String viewId = params.getParameterAsString("_viewid");
			ViewProcess process = new ViewProcessBean();
			View v = (View)process.doView(viewId);
			setContent(v);
			return getSuccessResult((View) getContent(), DO_DIALOG_VIEW);
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return getInputResult((View) getContent());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return getInputResult((View) getContent());
		}
	}
	
	/**
	 * 导出Document到Excel
	 * 
	 * @return "SUCESS"表示成功处理.
	 * @throws Exception
	 */
	public String expDocToExcel() throws Exception {
		try {
			ParamsTable parasm = getParams();
			changeOrderBy(parasm);

			String fileName = ((ViewProcess) process).expDocToExcel(get_viewid(), getUser(), parasm);
			ServletActionContext.getRequest().setAttribute("excelFileName", fileName);
			
			//Excel导出执行运行后脚本
			 
			String _activityid = parasm.getParameterAsString("_activityid");
			String viewid = parasm.getParameterAsString("_viewid");
			if (!StringUtil.isBlank(viewid)) {
				ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
				ActivityParent actParent = (ActivityParent) viewProcess.doView(_viewid);
				if (actParent != null && _activityid != null && _activityid.trim().length() > 0) {
					Activity act = actParent.findActivity(_activityid);
					
					// 运行后脚本
					if (!StringUtil.isBlank(act.getAfterActionScript())) {
						IRunner runner = JavaScriptFactory.getInstance(parasm.getSessionid(), actParent.getApplicationid()); 
						runner.initBSFManager(getCurrentDocument(), params, getUser(), new java.util.ArrayList<ValidateMessage>());
						StringBuffer label = new StringBuffer();
						label.append("Activity Action(").append(act.getId()).append(")." + act.getName()).append("afterActionScript");
						runner.run(label.toString(), act.getAfterActionScript());
					}
				}
			}
			return SUCCESS;
		}  catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
	/**
	 * 视图打印
	 */
	public String printDoDisplayView() throws Exception {
		try {
			View view = (View) new ViewProcessBean().doView(this.getContent().getId());
			//将分页功能去掉
			view.setPagelines(null);
			view.setPagination(false);
			
			String rtn = getSuccessResult(view, DO_DISPLAY_VIEW);
			ServletActionContext.getRequest().setAttribute("datas", datas);
			return rtn;
		}catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return getInputResult((View) getContent());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return getInputResult((View) getContent());
		}
	}
	
	public void runScript() {
		try {
			String actionScript = "";
			String colName = "";
			ParamsTable params = this.getParams();
			DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, application);
			Document doc = (Document) dp.doView(docid);
			View view = (View) getContent();
			
			Iterator<Column> it = view.getColumns().iterator();
			while(it.hasNext()){
				Column col = it.next();
				if(Column.BUTTON_TYPE_SCRIPT.equals(col.getButtonType()) && col.getId().equals(columnId)){
					actionScript = col.getActionScript();
					colName = col.getName();
				}
			}
			
			
			if(!StringUtil.isBlank(actionScript)){
				IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), application); 
				runner.initBSFManager(doc, params, getUser(), new java.util.ArrayList<ValidateMessage>());
				StringBuffer label = new StringBuffer();
				label.append("Button Action:[viewName:").append(view.getName()).append("][colName:").append(colName).append("]").append(columnId).append("ActionScript");
				Object result = runner.run(label.toString(), actionScript);
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setContentType("text/html;charset=utf8");
				response.setCharacterEncoding("utf-8");
				if(result!=null){
					response.getWriter().write(String.valueOf(result));
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
