package cn.myapps.base.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import cn.myapps.base.dao.DataPackage;

/**
 * The page tag for page navigation in list page.
 */
public class PageNavTag extends TagSupport {

	private static final long serialVersionUID = 6338111746579488137L;

	/**
	 * @uml.property name="dpName"
	 */
	private String dpName;
	/**
	 * @uml.property name="css"
	 */
	private String css;

	/**
	 * @param datapackage
	 * @uml.property name="dpName"
	 */
	public void setDpName(String dpName) {
		this.dpName = dpName;
	}

	/**
	 * @return the css
	 * @uml.property name="css"
	 */
	public String getCss() {
		return css;
	}

	/**
	 * @param css
	 *            the css to set
	 * @uml.property name="css"
	 */
	public void setCss(String css) {
		this.css = css;
	}
	
	public enum PageLines{
		TEN(10),
		//FIFTEEN(15),
		TWENTY_FIVE(25),
		FIFTY(50),
		ONE_HUNDRED(100);
		
		private int code;
		
		private PageLines(int code){
			this.code = code;
		}
		
		public int getCode() {
			return code;
		}
		
		public static PageLines getByCode(int code) {
			PageLines[] tyeps = values();
			for (int i = 0; i < tyeps.length; i++) {
				if (tyeps[i].getCode() == code) {
					return tyeps[i];
				}
			}
			return TEN;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		DataPackage<?> bean = (DataPackage<?>) pageContext.getRequest().getAttribute(
				dpName);
		int currentPage = 0;
		int pagelines = 0;
		int pageCount = 0;
		int rowCount = 0;
		if (bean != null) {
			currentPage = bean.getPageNo();
			pagelines = bean.getLinesPerPage();
			pageCount = bean.getPageCount();
			rowCount = bean.getRowCount();
		}

		StringBuffer html = new StringBuffer();

		if (currentPage > 1) {
			if (css != null && css.trim().length() > 0) {
				html = html
						.append("<a class="
								+ css
								+ " href='javascript:showPreviousPage()'><<{*[PrevPage]*}</a>&nbsp;");
			} else {
				html = html
				.append("<a href='javascript:showPreviousPage()'><<{*[PrevPage]*}</a>&nbsp;");
			}

		}
		
		if(pageCount > 0){
			//首页
			if(currentPage >= 4){
				html.append("&nbsp;<a href='javascript:showPageNo("+1+")'>"+1+"</a>&nbsp;");
			}
			if(currentPage >= 5){
				html.append("&nbsp;...&nbsp;");
			}
			//当前页的前两页
			for (int i = currentPage - 2; i < currentPage; i++) {
				if(i > 0){
					html.append("&nbsp;<a href='javascript:showPageNo("+(i)+")'>"+(i)+"</a>&nbsp;");
				}
			}
			//当前页
			html.append("&nbsp;<font style='color:black;font-weight:bolder;'>"+currentPage+"</font>&nbsp;");
			//当前页的后两页
			for (int i = currentPage; i < currentPage + 2; i++) {
				if(i + 1 <= pageCount){
					html.append("&nbsp;<a href='javascript:showPageNo("+(i+1)+")'>"+(i+1)+"</a>&nbsp;");
				}
			}
			if(currentPage <= pageCount - 4){
				html.append("&nbsp;...&nbsp;");
			}
			//末页
			if(currentPage <= pageCount - 3){
				html.append("&nbsp;<a href='javascript:showPageNo("+pageCount+")'>"+pageCount+"</a>&nbsp;");
			}
		}

		if (currentPage < pageCount) {
			if (css != null && css.trim().length() > 0) {
				html = html
						.append("&nbsp;<a class="
								+ css
								+ " href='javascript:showNextPage()'>{*[NextPage]*}>></a>&nbsp;");
			} else {
				html = html
						.append("&nbsp;<a href='javascript:showNextPage()'>{*[NextPage]*}>></a>&nbsp;");
			}
		}
		
		int beginNo = (currentPage - 1) * pagelines;
		int endNo = currentPage * pagelines > rowCount ? rowCount : currentPage * pagelines;
		html.append("&nbsp;(" + (rowCount == 0 ? rowCount : (beginNo + 1)));
		if((beginNo + 1) < rowCount){
			html.append("-" + endNo + "条)");
		}
		html.append("&nbsp;&nbsp;总条数：").append(rowCount);
		
		html.append("&nbsp;&nbsp;|&nbsp;&nbsp;").append("{*[cn.myapps.base.web.tag]*}");
		for (int i = 0; i < PageLines.values().length; i++) {
			PageLines _pagelines = PageLines.values()[i];
			if(_pagelines.code == pagelines){
				if(i==PageLines.values().length-1){
					html.append(_pagelines.code+"&nbsp;");
				}else{
				html.append(_pagelines.code+"&nbsp;&nbsp;");}
			}else{
				if(i==PageLines.values().length-1){
					html.append("<a href='javascript:setCookie(\"fileList_pageLine\","+_pagelines.code+");changeShowNum()'>"+_pagelines.code+"</a>&nbsp;");
				}else{
				html.append("<a href='javascript:setCookie(\"fileList_pageLine\","+_pagelines.code+");changeShowNum()'>"+_pagelines.code+"</a>&nbsp;&nbsp;");}
			}
		}
		
//		html = html.append("{*[InPage]*}").append(currentPage).append(
//				"{*[Page]*}/{*[Total]*}").append(pageCount).append(
//				"{*[Pages]*}&nbsp;");

		try {
			pageContext.getOut().print(html.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return super.doEndTag();
	}
}
