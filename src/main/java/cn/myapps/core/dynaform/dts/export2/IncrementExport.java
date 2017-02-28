package cn.myapps.core.dynaform.dts.export2;

import java.util.Iterator;

import org.apache.log4j.Logger;

import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.dts.exp.mappingconfig.ejb.MappingConfig;
import cn.myapps.core.user.action.WebUser;

public class IncrementExport extends ExportBase {

	public IncrementExport(MappingConfig mappingconfig, String application, WebUser user) {
		super(mappingconfig, application, user);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Logger log = Logger.getLogger(ExportBase.class);

	protected long getTotalLine(MappingConfig mfg, String dql, DocumentProcess process) throws Exception {

		long totalLine = 0;
		if (mappingconfig.getLastRun() == null)
			totalLine = process.getNeedExportDocumentTotal(dql, null, domainid);
		else
			totalLine = process.getNeedExportDocumentTotal(dql, mappingconfig.getLastRun(), domainid);
		return totalLine;
	}

	protected Iterator<Document> getAppointedRows(MappingConfig mfg, int page, int lines, String dql, DocumentProcess process)
			throws Exception {
		return process.queryByDQLAndDocumentLastModifyDate(dql, mappingconfig.getLastRun(), page, lines, domainid);
	}

}
