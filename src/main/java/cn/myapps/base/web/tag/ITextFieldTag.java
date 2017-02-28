package cn.myapps.base.web.tag;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractUITag;

import com.opensymphony.xwork2.ognl.OgnlValueStack;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author Happy
 *
 */
public class ITextFieldTag extends AbstractUITag {
    protected String maxlength;
    protected String readonly;
    protected String size;
    protected String placeholder;

    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new ITextField((OgnlValueStack) stack, req, res);
    }

    protected void populateParams() {
        super.populateParams();

        ITextField textField = ((ITextField) component);
        textField.setMaxlength(maxlength);
        textField.setReadonly(readonly);
        textField.setSize(size);
        textField.setPlaceholder(placeholder);
    }

    /**
     * @deprecated please use {@link #setMaxlength} instead
     */
    public void setMaxLength(String maxlength) {
        this.maxlength = maxlength;
    }

    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    public void setSize(String size) {
        this.size = size;
    }

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}
    
    
}
