package cn.myapps.core.dynaform.dts.export2;

import java.util.Iterator;

import org.apache.log4j.Logger;

import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.dts.exp.mappingconfig.ejb.MappingConfig;
import cn.myapps.core.user.action.WebUser;

public class ExportAll extends ExportBase {

	public ExportAll(MappingConfig mappingconfig, String application, WebUser user) {
		super(mappingconfig, application, user);
	}

	private static final long serialVersionUID = 1L;

	static Logger log = Logger.getLogger(ExportBase.class);

	protected long getTotalLine(MappingConfig mfg, String dql, DocumentProcess process) throws Exception {
		return process.getNeedExportDocumentTotal(dql, null, domainid);
	}

	protected Iterator<Document> getAppointedRows(MappingConfig mfg, int page, int lines, String dql, DocumentProcess process)
			throws Exception {
		return process.iteratorLimitByDQL(dql, page, lines, domainid);
	}

}
