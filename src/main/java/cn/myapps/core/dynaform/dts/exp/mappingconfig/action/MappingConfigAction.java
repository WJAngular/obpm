package cn.myapps.core.dynaform.dts.exp.mappingconfig.action;

import java.util.Collection;
import java.util.Iterator;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.core.deploy.module.ejb.ModuleProcess;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSourceProcess;
import cn.myapps.core.dynaform.dts.exp.Export_Erro_type;
import cn.myapps.core.dynaform.dts.exp.mappingconfig.ejb.MappingConfig;
import cn.myapps.core.dynaform.dts.exp.mappingconfig.ejb.MappingConfigProcess;
import cn.myapps.core.dynaform.dts.export2.ExportAll;
import cn.myapps.core.dynaform.dts.export2.ExportBase;
import cn.myapps.core.dynaform.dts.export2.IncrementExport;
import cn.myapps.util.ProcessFactory;

/**
 * @author nicholas
 */
public class MappingConfigAction extends BaseAction<MappingConfig> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public MappingConfigAction() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(MappingConfigProcess.class),
				new MappingConfig());
	}

	private String _ds;

	private String _moduleid;

	private String exportDocsNum = null;

	static int docsnum = -1;

	/**
	 * @return the _ds
	 * @uml.property name="_ds"
	 */
	public String get_ds() {
		MappingConfig mc = (MappingConfig) getContent();
		if (mc.getDatasource() != null)
			return mc.getDatasource().getId();
		else
			return null;
	}

	/**
	 * @param _ds
	 *            the _ds to set
	 * @uml.property name="_ds"
	 */
	public void set_ds(String _ds) {
		this._ds = _ds;
	}

	public String doSave() {
		try {
			MappingConfig mc = (MappingConfig) getContent();

			if (_ds == null || _ds.trim().length() < 1) {
				this.addFieldError("1",
						"{*[core.dts.exp.mappingconfig.datasource]*}");
				return SUCCESS;
			}
			DataSourceProcess dp = (DataSourceProcess) ProcessFactory
					.createProcess((DataSourceProcess.class));
			DataSource ds = (DataSource) dp.doView(_ds);
			if (ds != null) {
				mc.setDatasource(ds);
			} else {
				mc.setDatasource(null);
			}

			boolean flag = true;

			// 查找是否有想同的表名
			Collection<MappingConfig> repeatName = ((MappingConfigProcess) process)
					.getMCByTableName(mc.getTablename(), getApplication());

			// 如果表名相同，又在同一个datasource则不允许新建，反之则可以新建
			for (Iterator<MappingConfig> iter = repeatName.iterator(); iter.hasNext();) {
				MappingConfig temp = (MappingConfig) iter.next();

				if (temp.getId() != null
						&& temp.getDatasource() != null
						&& temp.getDatasource().getId().equals(
								mc.getDatasource().getId())
						&& !temp.getId().equals(mc.getId())) {
					this.addFieldError("2",
							"{*[core.dts.exp.mappingconfig.exist]*}");
					flag = false;
				}
			}

			if (get_moduleid() != null && !get_moduleid().equals("")) {
				ModuleProcess mp = (ModuleProcess) ProcessFactory
						.createProcess(ModuleProcess.class);
				ModuleVO mv = (ModuleVO) mp.doView(this.get_moduleid());
				mc.setModule(mv);
			}
			if (flag) {
				return super.doSave();
			} else {
				return SUCCESS;
			}
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	public String exportAllDocument() throws Exception {

		return export(false);
	}

	public String incrementExportDocument() throws Exception {
		return export(true);
	}

	public String export(boolean isIncrement) throws Exception {

		if (_selects != null && _selects.length > 0) {

			for (int i = 0; i < _selects.length; i++) {

				MappingConfig mc = (MappingConfig) this.process
						.doView(_selects[i]);
				ExportBase eb = null;

				if (isIncrement)
					eb = new IncrementExport(mc, getApplication(), getUser());
				else
					eb = new ExportAll(mc, getApplication(), getUser());

				String result = eb.exportDocument();

				if (result != null
						&& !result.equals(Export_Erro_type.ERROR_TYPE_02)
						&& !result.equals(Export_Erro_type.ERROR_TYPE_03)
						&& !result.equals(Export_Erro_type.ERROR_TYPE_04)) {
					docsnum += Integer.parseInt(result);
				}
				this.addActionError(result);
			}
			if (docsnum > -1)
				docsnum += 1;
		}
		return SUCCESS;
	}

	/**
	 * @return the exportDocsNum
	 * @uml.property name="exportDocsNum"
	 */
	public String getExportDocsNum() {
		if (docsnum > -1) {
			this.exportDocsNum = String.valueOf(docsnum);
			docsnum = -1;
		}
		return this.exportDocsNum;

	}

	/**
	 * @param exportDocsNum
	 *            the exportDocsNum to set
	 * @uml.property name="exportDocsNum"
	 */
	public void setExportDocsNum(String exportDocsNum) {
		this.exportDocsNum = exportDocsNum;
	}

	/**
	 * @return the _moduleid
	 * @uml.property name="_moduleid"
	 */
	public String get_moduleid() {
		MappingConfig mc = (MappingConfig) getContent();
		if (mc.getModule() != null) {
			_moduleid = mc.getModule().getId();
		}

		return _moduleid;
	}

	/**
	 * @param _moduleid
	 *            the _moduleid to set
	 * @uml.property name="_moduleid"
	 */
	public void set_moduleid(String _moduleid) {
		this._moduleid = _moduleid;
	}

}
