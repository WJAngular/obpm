package cn.myapps.core.datamap.definition.ejb;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.deploy.module.ejb.ModuleVO;

/**
 * 数据地图模板配置
 * @author Happy
 *
 */
public class DataMapCfgVO extends ValueObject {
	private static final long serialVersionUID = -2964048985690974224L;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 关联表单
	 */
	private String relatedForm;
	/**
	 * 线索字段
	 */
	private String clueField;
	/**
	 * 线索字段2
	 */
	private String clueField2;
	/**
	 * 摘要字段
	 */
	private String summaryField;
	
	/**
	 *关联流程 
	 */
	private String relatedFlow;
	
	/**
	 * 流程状态颜色映射配置
	 */
	private String stateLableColorMapping;
	
	/**
	 * 是否显示流程进度条
	 */
	private boolean showFlowProgressbar = false; 
	/**
	 * 所属模块
	 */
	private ModuleVO module;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRelatedForm() {
		return relatedForm;
	}
	public void setRelatedForm(String relatedForm) {
		this.relatedForm = relatedForm;
	}
	public String getClueField() {
		return clueField;
	}
	public void setClueField(String clueField) {
		this.clueField = clueField;
	}
	public String getSummaryField() {
		return summaryField;
	}
	public void setSummaryField(String summaryField) {
		this.summaryField = summaryField;
	}
	public String getRelatedFlow() {
		return relatedFlow;
	}
	public void setRelatedFlow(String relatedFlow) {
		this.relatedFlow = relatedFlow;
	}
	public String getStateLableColorMapping() {
		return stateLableColorMapping;
	}
	public void setStateLableColorMapping(String stateLableColorMapping) {
		this.stateLableColorMapping = stateLableColorMapping;
	}
	public boolean isShowFlowProgressbar() {
		return showFlowProgressbar;
	}
	public void setShowFlowProgressbar(boolean showFlowProgressbar) {
		this.showFlowProgressbar = showFlowProgressbar;
	}
	public ModuleVO getModule() {
		return module;
	}
	public void setModule(ModuleVO module) {
		this.module = module;
	}
	public String getClueField2() {
		return clueField2;
	}
	public void setClueField2(String clueField2) {
		this.clueField2 = clueField2;
	}

}
