<#include "/${parameters.templateDir}/wwfull/form-validate.ftl" />
<#include "/${parameters.templateDir}/simple/form.ftl" />
<table width="90%" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td align="left" class="message">�û��б�</td>
          <td align="right">
            <input type="button" class="button-cmd" alt="�ύ" value="�ύ" onclick="formItem.action='save.action';formItem.submit()" /> 
	    <input type="button" class="button-cmd" alt="����" value="����" onclick="formItem.action='list.action';formItem.submit()" />
	  </td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td><table width="100%" class="${parameters.cssClass?default('content-table')?html}"<#rt/>
<#if parameters.cssStyle?exists> style="${parameters.cssStyle?html}"<#rt/>
</#if>
>
