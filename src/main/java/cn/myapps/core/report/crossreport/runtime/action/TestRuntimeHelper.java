package cn.myapps.core.report.crossreport.runtime.action;

import junit.framework.TestCase;
import cn.myapps.core.dynaform.document.dql.DQLASTUtil;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.util.ProcessFactory;

public class TestRuntimeHelper extends TestCase {
	//private static final Logger LOG = Logger.getLogger(TestRuntimeHelper.class);

	public void testFF() throws Exception {
		FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form relatedForm = (Form) formProcess.doView("01b84c4b-9d2c-b660-b931-67dafc398d21");

		StringBuffer sqlTmp = new StringBuffer();
		sqlTmp.append("SELECT AUTH_DOC.* FROM ").append(DQLASTUtil.TBL_PREFIX).append(relatedForm.getName());
		sqlTmp.append(" AUTH_DOC WHERE AUTH_DOC.ISTMP = 0 ");

		//CrossReportProcessBean bean = new CrossReportProcessBean();
	//	CrossReportVO vo = (CrossReportVO) bean.doView("11de-ab45-785569af-a3d4-5fbcee0c0c12");
		//if (vo != null)
			//LOG.info(sqlTmp + FilterConditionParser.parseToSQL(vo.getFormCondition(), new ParamsTable(), new WebUser(new UserVO()), new View()));
	}

}
