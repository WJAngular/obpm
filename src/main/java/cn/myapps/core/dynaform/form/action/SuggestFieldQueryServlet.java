package cn.myapps.core.dynaform.form.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.Options;
import cn.myapps.core.dynaform.form.ejb.SuggestField;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.form.ejb.mapping.TableMapping;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.DbTypeUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.MemoryCacheUtil;

public class SuggestFieldQueryServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5717733496173672993L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doQuery(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doQuery(req, resp);
	}

	private void doQuery(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			ParamsTable params = ParamsTable.convertHTTP(request);
			WebUser user = (WebUser) request.getSession().getAttribute(
					Web.SESSION_ATTRIBUTE_FRONT_USER);
			String formFieldId = params.getParameterAsString("_formFieldId");
			String formid = params.getParameterAsString("_formid");
			String parentId = params.getParameterAsString("parentid");
			String docid = params.getParameterAsString("content.id");
			if (docid != null && docid.equals(parentId)) {
				parentId = null;
				params.removeParameter("parentid");
			}

			FormProcess formProcess = (FormProcess) ProcessFactory
					.createProcess(FormProcess.class);
			Form form = (Form) formProcess.doView(formid);
			SuggestField field = (SuggestField) form.findField(formFieldId);
			Document doc = null;

			if (field.getOptionsScript() != null
					&& field.getOptionsScript().trim().length() > 0) {
				if (!StringUtil.isBlank(docid)) {
					doc = (Document) MemoryCacheUtil.getFromPrivateSpace(docid,
							user);
					if (doc != null) {
						doc.setId(docid);
					}
				} else if (!StringUtil.isBlank(parentId)) {
					doc = (Document) user.getFromTmpspace(parentId);
				}
				doc = form.createDocument(doc, params, user);

				params.setParameter("keyword",
						params.getParameterAsString("__keyword"));

				IRunner runner = JavaScriptFactory.getInstance(
						params.getSessionid(),
						params.getParameterAsString("application"));
				runner.initBSFManager(doc, params, user,
						new ArrayList<ValidateMessage>());
				Options options = field.getOptions(runner);

				out.write(options.toJSON4SuggestField());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			out.close();
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String getQByDBType(String q, String applicationId)
			throws Exception {
		String dbType = DbTypeUtil.getDBType(applicationId);
		if (dbType.equals(DbTypeUtil.DBTYPE_MSSQL)) {
			q = q.replaceAll("%", "[%]");
			q = q.replaceAll("_", "[_]");
		} else {
			q = q.replaceAll("%", "\\\\%");
			q = q.replaceAll("_", "\\\\_");
		}
		return q;
	}
}
