package cn.myapps.core.dynaform.form;

import java.util.Collection;
import java.util.Iterator;

import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.util.ProcessFactory;

public class ReStoreForms {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new ReStoreForms().reStoreForms();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Collection<Form> findAllForms() throws Exception {
		FormProcess process = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Collection<Form> rtn = process.doSimpleQuery(null);
		return rtn;
	}

	private void reStoreForms() throws Exception {
		FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Collection<Form> forms = findAllForms();
		for (Iterator<Form> iter = forms.iterator(); iter.hasNext();) {
			Form form = iter.next();
			formProcess.doUpdate(form);
		}
	}

}
