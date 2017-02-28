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
public class ITextareaTag extends AbstractUITag {
    protected String cols;
    protected String readonly;
    protected String rows;
    protected String wrap;
    protected String placeholder;

    protected void populateParams() {
        super.populateParams();

        ITextArea textArea = ((ITextArea) component);
        textArea.setCols(cols);
        textArea.setReadonly(readonly);
        textArea.setRows(rows);
        textArea.setWrap(wrap);
        textArea.setPlaceholder(placeholder);
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public void setWrap(String wrap) {
        this.wrap = wrap;
    }

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public Component getBean(ValueStack stack, HttpServletRequest req,
			HttpServletResponse res) {
		 return new ITextArea((OgnlValueStack) stack, req, res);
	}
    

}

