package cn.myapps.core.dynaform.dts.metadata.action;

import java.util.Collection;

import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSourceProcess;
import cn.myapps.core.dynaform.dts.metadata.ejb.ITable;
import cn.myapps.core.dynaform.dts.metadata.ejb.MetadataProcess;
import cn.myapps.core.dynaform.dts.metadata.ejb.MetadataProcessBean;
import cn.myapps.util.ProcessFactory;


public class MetadataManageAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 656725241921497488L;
	
	private String datasourceId;
	
	private MetadataProcess process;
	
	private Collection<ITable> tables;

	public MetadataManageAction() {
		super();
		process = new MetadataProcessBean();
	}
	
	
	public String getDatasourceId() {
		return datasourceId;
	}



	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
	}



	public MetadataProcess getProcess() {
		return process;
	}



	public Collection<ITable> getTables() {
		return tables;
	}


	public void setTables(Collection<ITable> tables) {
		this.tables = tables;
	}


	/**
	 * 索引优化
	 * @return
	 */
	public String doIndexOptimization() {
		try {
			DataSourceProcess dsprocess = (DataSourceProcess) ProcessFactory.createProcess(DataSourceProcess.class);
			DataSource datasource = (DataSource) dsprocess.doView(getDatasourceId());
			process.doIndexOptimization(datasource);
			this.addActionMessage("优化完成");
			
			doList();
		} catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			e.printStackTrace();
			this.addFieldError("", e.getMessage());
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	public String doList(){
		try {
			DataSourceProcess dsprocess = (DataSourceProcess) ProcessFactory.createProcess(DataSourceProcess.class);
			DataSource datasource = (DataSource) dsprocess.doView(getDatasourceId());
			this.setTables(process.getAllTables(datasource));
		} catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			e.printStackTrace();
			this.addFieldError("", e.getMessage());
			return INPUT;
		}
		return SUCCESS;
	}

}
