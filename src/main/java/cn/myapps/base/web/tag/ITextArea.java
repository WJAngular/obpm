package cn.myapps.base.web.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.UIBean;

import com.opensymphony.xwork2.ognl.OgnlValueStack;

/**
 * @author Happy
 *
 */
public class ITextArea extends UIBean {
    final public static String TEMPLATE = "textarea";

    protected String cols;
    protected String readonly;
    protected String rows;
    protected String wrap;
    protected String placeholder;

    public ITextArea(OgnlValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    protected String getDefaultTemplate() {
        return TEMPLATE;
    }

    public void evaluateExtraParams() {
        super.evaluateExtraParams();

        if (readonly != null) {
            addParameter("readonly", findValue(readonly, Boolean.class));
        }

        if (cols != null) {
            addParameter("cols", findString(cols));
        }

        if (rows != null) {
            addParameter("rows", findString(rows));
        }

        if (wrap != null) {
            addParameter("wrap", findString(wrap));
        }
        if (placeholder != null) {
            addParameter("placeholder", findString(placeholder));
        }
    }

    /**
     * HTML cols attribute
     * @ww.tagattribute required="false" type="Integer"
     */
    public void setCols(String cols) {
        this.cols = cols;
    }

    /**
     * Whether the textarea is readonly
     * @ww.tagattribute required="false" type="Boolean" default="false"
     */
    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    /**
     * HTML rows attribute
     * @ww.tagattribute required="false" type="Integer"
     */
    public void setRows(String rows) {
        this.rows = rows;
    }

    /**
     * HTML wrap attribute
     * @ww.tagattribute required="false" type="String"
     */
    public void setWrap(String wrap) {
        this.wrap = wrap;
    }

	/**
     * HTML placeholder attribute
     * @ww.tagattribute required="false" type="String"
     */
	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}
    
}
