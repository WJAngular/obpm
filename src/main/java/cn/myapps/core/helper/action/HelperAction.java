package cn.myapps.core.helper.action;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.struts2.ServletActionContext;
import org.xml.sax.SAXException;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.constans.Web;
import cn.myapps.core.helper.ejb.HelperProcess;
import cn.myapps.core.helper.ejb.HelperVO;
import cn.myapps.core.helper.html.BuildHelpHtml;
import cn.myapps.util.ProcessFactory;


public class HelperAction extends BaseAction<HelperVO> {
	
	private static final long serialVersionUID = 1091280028799001741L;

	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HelperAction() throws Exception {
		super(ProcessFactory.createProcess(HelperProcess.class), new HelperVO());
	}

	public HelperVO getHelperByname(String urlname, String application) throws Exception {
		if (urlname != null && urlname.trim().length() > 0) {
			HelperVO helper = null;
			helper = ((HelperProcess) process).getHelperByName(urlname, application);
			return helper;
		}
		return null;
	}

	public String doSave() {

		return super.doSave();
	}

	public String doHelpTreeIndex() throws IOException {
		String html = "";
		String searchParem = this.getParams().getParameterAsString("seachParem");
		HelpHelper hhelper = new HelpHelper();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		String language = (String) ServletActionContext.getRequest().getSession()
				.getAttribute(Web.SESSION_ATTRIBUTE_USERLANGUAGE);
		try {
			html = hhelper.getHelpTreeIndex(searchParem, language);
		} catch (ParserConfigurationException e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		} catch (SAXException e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		if (html != "") {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(html);
		}
		return html;
	}

	//Iscript帮助
	public String getIscriptHelp()throws IOException{
		String iscriptHtml = "";
		HelpHelper hhelper = new HelpHelper();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		String language = (String) ServletActionContext.getRequest().getSession()
				.getAttribute(Web.SESSION_ATTRIBUTE_USERLANGUAGE);
		try {
			iscriptHtml = hhelper.getIscriptHelp(language);
		} catch (ParserConfigurationException e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		} catch (SAXException e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		if (iscriptHtml != "") {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(iscriptHtml);
		}
		return iscriptHtml;
	}
	
	public String getTipicHrefById() throws IOException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, SAXException, ParserConfigurationException {
		HttpServletResponse response = ServletActionContext.getResponse();
		HelpHelper hhelper = new HelpHelper();
		String html = "";
		String id = this.getParams().getParameterAsString("helpid");
		String language = (String) ServletActionContext.getRequest().getSession()
				.getAttribute(Web.SESSION_ATTRIBUTE_USERLANGUAGE);

		html = hhelper.getTopicHrefByIdAndLanguage(id, language);
		if (html != "") {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(html);
		}
		return html;
	}
	
	public String doBuildHelp()throws Exception{
//		System.out.println("#################################");
//		System.out.println("提示：action调用成功....");
		try{
			BuildHelpHtml.getDocument();
		}catch(Exception e){
			this.addActionError("生成文件失败");
			e.printStackTrace();
			return INPUT;
		}
		
		this.addActionMessage("帮助文件生成成功");
		return SUCCESS;
	}
}
