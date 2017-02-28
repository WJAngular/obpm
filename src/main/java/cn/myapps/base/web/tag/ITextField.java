package cn.myapps.base.web.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.UIBean;

import com.opensymphony.xwork2.ognl.OgnlValueStack;

/**
 * @author Happy
 *
 */
public class ITextField extends UIBean {
    /**
     * The name of the default template for the TextFieldTag
     */
    final public static String TEMPLATE = "text";


    protected String maxlength;
    protected String readonly;
    protected String size;
    protected String placeholder;

    public ITextField(OgnlValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    protected String getDefaultTemplate() {
        return TEMPLATE;
    }

    protected void evaluateExtraParams() {
        super.evaluateExtraParams();

        if (size != null) {
            addParameter("size", findString(size));
        }

        if (maxlength != null) {
            addParameter("maxlength", findString(maxlength));
        }

        if (readonly != null) {
            addParameter("readonly", findValue(readonly, Boolean.class));
        }
        
        if (placeholder != null) {
            addParameter("placeholder", findString(placeholder));
        }
    }

    /**
     * HTML maxlength attribute
     * @ww.tagattribute required="false" type="Integer"
     */
    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

    /**
     * Deprecated. Use maxlength instead.
     * @ww.tagattribute required="false"
     */
    public void setMaxLength(String maxlength) {
        this.maxlength = maxlength;
    }

    /**
     * Whether the input is readonly
     * @ww.tagattribute required="false" type="Boolean" default="false"
     */
    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    /**
     * HTML size attribute
     * @ww.tagattribute required="false" type="Integer"
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * HTML placeholder attribute
     * @ww.tagattribute required="false" type="String"
     */
	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}
    
}
