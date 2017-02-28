/*
 * Created on 2005-2-10
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cn.myapps.core.macro.util;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.dynaform.form.ejb.Option;
import cn.myapps.core.dynaform.form.ejb.Options;
import cn.myapps.core.workflow.engine.StateMachineUtil;
import cn.myapps.core.workflow.utility.CommonUtil;
import cn.myapps.core.workflow.utility.Sequence;
import cn.myapps.util.DateUtil;
import cn.myapps.util.StringList;
import cn.myapps.util.StringUtil;
import cn.myapps.util.pdf.PdfUtil;
import cn.myapps.util.property.PropertyUtil;

/**
 * @author zhouty
 * 
 *  To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Tools {

	public static StringList createStringList() {
		return new StringList();
	}

	public static Options createOptions() {
		return new Options();
	}

	public static Option createOption() {
		return new Option();
	}

	public static final StringUtil STRING_UTIL = new StringUtil();

	public static final DateUtil DATE_UTIL = new DateUtil();

	public static final CommonUtil COMMON_UTIL = new CommonUtil();

	public static final Sequence SEQ_UTIL = new Sequence();

	public static final StateMachineUtil STATE_MACHINE_UTIL = new StateMachineUtil();

	public static final PropertyUtil PROP_UTIL = new PropertyUtil();

	public static final PdfUtil PDF_UTIL = new PdfUtil();
	
	public static final PersistenceUtils PERSISTENCE_UTIL = new PersistenceUtils();

	// public static final EmailUtil EMAIL_UTIL = new EmailUtil();

}
