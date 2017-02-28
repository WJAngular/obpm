package cn.myapps.core.dynaform.printer.dao;

import java.util.Collection;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.dynaform.printer.ejb.Printer;

/**
 * @author Happy
 *
 */
public interface PrinterDAO extends IDesignTimeDAO<Printer> {
	
	public Printer findByFormId(String formid) throws Exception;
	
	public Collection<Printer> getPrinterByModule(String moduleid) throws Exception;

}
