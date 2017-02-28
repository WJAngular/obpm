package cn.myapps.core.widget.action;

import java.util.Map;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.widget.ejb.PageWidget;
import cn.myapps.core.widget.ejb.PageWidgetProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;

public class PageWidgetAction extends BaseAction<PageWidget> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1983975360616682254L;
	
	private String _isExit;
	
	private String summaryName;
	
	public String get_isExit() {
		return _isExit;
	}

	public void set_isExit(String _isExit) {
		this._isExit = _isExit;
	}
	
	

	public String getSummaryName() {
		return summaryName;
	}

	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public PageWidgetAction() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(PageWidgetProcess.class), new PageWidget());
	}
	
	public String doNew(){
		try {
			PageWidget vo = (PageWidget) getContent();
			vo.setPublished(true);
			setContent(vo);
		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}
	
	
	
	@Override
	public String doEdit() {
		try {
			Map params = getContext().getParameters();

			String id = ((String[]) params.get("id"))[0];
			PageWidget vo = (PageWidget)process.doView(id);
			if(vo != null && PageWidget.TYPE_SUMMARY.equals(vo.getType())){
				SummaryCfgProcess summaryProcess = (SummaryCfgProcess)ProcessFactory.createProcess(SummaryCfgProcess.class);
				SummaryCfgVO summary = (SummaryCfgVO)summaryProcess.doView(vo.getActionContent());
				if(summary != null) this.summaryName = summary.getTitle();
			}
			
			setContent(vo);
		} catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

		return SUCCESS;
	}

	public String doSaveAndExit(){
		this.set_isExit("yes");
		return super.doSave();
	}
	

}
