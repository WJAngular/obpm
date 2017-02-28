package cn.myapps.core.expimp.imp.ejb;

import java.io.File;

import com.opensymphony.xwork2.ValidationAware;


public interface ImpProcess {
	public ValidationAware doImportValidate(ImpSelect select, File importFile)
			throws Exception;

	public void doImport(ImpSelect select, File importFile) throws Exception;

}
