package cn.myapps.mobile2.contacts;

import java.util.Iterator;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.department.ejb.DepartmentProcessBean;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.usergroup.ejb.UserGroupProcess;
import cn.myapps.core.usergroup.ejb.UserGroupSetProcess;
import cn.myapps.core.usergroup.ejb.UserGroupVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class MbContactsXMLBuilder {

	public static String toContactGroupXML(WebUser user, ParamsTable params) {
		StringBuffer sb = new StringBuffer();
		try {
			UserGroupProcess usergroupProcess = (UserGroupProcess) ProcessFactory.createProcess(UserGroupProcess.class);
			DataPackage<UserGroupVO> datas = usergroupProcess.getUserGroupsByUser(user.getId());
			DomainVO domain = user.getDomain();
			sb.append("<").append(MobileConstant2.TAG_CONTACTDATA);
			sb.append(" ").append(MobileConstant2.ATT_DOMAINID).append("='").append(domain.getId()).append("'");
			sb.append(" ").append(MobileConstant2.ATT_DOMAINNAME).append("='").append(domain.getName()).append("'");
			sb.append(">");

			if (datas != null && datas.datas.size() > 0) {
				for (UserGroupVO groupvo : datas.datas) {
					sb.append("<").append("CONTACTGROUP");
					sb.append(" ").append(MobileConstant2.ATT_TYPE).append("='").append("group").append("'");
					sb.append(" ").append(MobileConstant2.ATT_ID).append("='").append(groupvo.getId()).append("'");
					sb.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(groupvo.getName()).append("'");
					sb.append("/>");
				}
			}
			sb.append("</").append(MobileConstant2.TAG_CONTACTDATA).append(">");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String toContactXML(WebUser user, ParamsTable params) {
		StringBuffer sb = new StringBuffer();
		try {
			String id = params.getParameterAsString("_groupid");

			UserGroupSetProcess usergroupsetProcess = (UserGroupSetProcess) ProcessFactory
					.createProcess(UserGroupSetProcess.class);
			DataPackage<UserVO> datas = null;

			if ("all".equals(id)) {
				params.setParameter("t_domainid", user.getDomainid());
				params.setParameter("sm_dimission", 1);
				UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
				datas = process.doQuery(params);
			} else {
				datas = usergroupsetProcess.getUserByGroup(id, params);
			}

			sb.append("<").append("CONTACTGROUP");
			sb.append(" ").append(MobileConstant2.ATT_ID).append("='").append(id).append("'");
			if (datas != null && datas.rowCount > 0) {
				sb.append(" ").append(MobileConstant2.ATT_SIZE).append("='").append(datas.rowCount).append("'");
			}
			sb.append(">");

			if (datas != null && datas.rowCount > 0) {
				for (Iterator<UserVO> iter = datas.datas.iterator(); iter.hasNext();) {
					try {
						UserVO userVO = iter.next();
						StringBuffer userSB = new StringBuffer();
						String name = userVO.getName();
						String email = userVO.getEmail();
						DepartmentProcessBean departmentProcess = new DepartmentProcessBean();
						DepartmentVO department = (DepartmentVO) departmentProcess
								.doView(userVO.getDefaultDepartment());
						String departmentName = "";
						if (department != null) {
							departmentName = department.getName();
						}
						String telephone = userVO.getTelephone();
						userSB.append("<").append(MobileConstant2.TAG_CONTACT);
						userSB.append(" ").append(MobileConstant2.ATT_IMAGENAME).append("='").append("IMG001.PNG")
								.append("'");
						userSB.append(" ").append(MobileConstant2.ATT_LASTNAME).append("='").append(name).append("'")
								.append(">");
						userSB.append("<").append(MobileConstant2.TAG_FIELD);
						userSB.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append("{*[Mobile]*}")
								.append("'");
						userSB.append(" ").append(MobileConstant2.ATT_CALL).append("='").append("true").append("'");
						userSB.append(" ").append(MobileConstant2.ATT_SMS).append("='").append("true").append("'");
						userSB.append(" ").append(MobileConstant2.ATT_NAME).append("='").append("MOBILE").append("'")
								.append(">");
						userSB.append((StringUtil.isBlank(telephone) ? "" : telephone));
						userSB.append("</").append(MobileConstant2.TAG_FIELD).append(">");
						userSB.append("<").append(MobileConstant2.TAG_FIELD);
						userSB.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append("{*[email.address]*}")
								.append("'");
						userSB.append(" ").append(MobileConstant2.ATT_MAIL).append("='").append("true").append("'");
						userSB.append(" ").append(MobileConstant2.ATT_NAME).append("='").append("MAIL").append("'")
								.append(">");
						userSB.append((StringUtil.isBlank(email) ? "" : email));
						userSB.append("</").append(MobileConstant2.TAG_FIELD).append(">");
						userSB.append("<").append(MobileConstant2.TAG_FIELD).append(" ");
						userSB.append(MobileConstant2.ATT_LABEL).append("='").append("{*[Department]*}").append("'");
						userSB.append(" ").append(MobileConstant2.ATT_NAME).append("='").append("DEPARTMENT")
								.append("'").append(">");
						userSB.append((StringUtil.isBlank(departmentName) ? "" : departmentName));
						userSB.append("</").append(MobileConstant2.TAG_FIELD).append(">");
						userSB.append("</").append(MobileConstant2.TAG_CONTACT).append(">");

						sb.append(userSB);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			sb.append("</").append("CONTACTGROUP").append(">");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String toMobileXML(Document doc, WebUser user, ParamsTable params) throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			DomainVO domain = user.getDomain();
			if (domain != null) {
				sb.append("<").append(MobileConstant2.TAG_CONTACTDATA);
				sb.append(" ").append(MobileConstant2.ATT_DOMAINID).append("='").append(domain.getId()).append("'");
				sb.append(" ").append(MobileConstant2.ATT_DOMAINNAME).append("='").append(domain.getName()).append("'")
						.append(">");

				params = new ParamsTable();
				params.setParameter("t_domainid", user.getDomainid());
				params.setParameter("_orderby", "orderByNo");
				DataPackage<UserVO> datas = process.doQuery(params);
				if (datas != null && datas.rowCount > 0) {
					for (Iterator<UserVO> it = datas.datas.iterator(); it.hasNext();) {
						UserVO userVO = it.next();
						try {
							StringBuffer userSB = new StringBuffer();
							String name = userVO.getName();
							String email = userVO.getEmail();
							DepartmentProcessBean departmentProcess = new DepartmentProcessBean();
							DepartmentVO department = (DepartmentVO) departmentProcess.doView(userVO
									.getDefaultDepartment());
							String departmentName = department.getName();
							String telephone = userVO.getTelephone();
							userSB.append("<").append(MobileConstant2.TAG_CONTACT);
							userSB.append(" ").append(MobileConstant2.ATT_IMAGENAME).append("='").append("IMG001.PNG")
									.append("'");
							userSB.append(" ").append(MobileConstant2.ATT_LASTNAME).append("='").append(name)
									.append("'").append(">");
							userSB.append("<").append(MobileConstant2.TAG_FIELD);
							userSB.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append("{*[Mobile]*}")
									.append("'");
							userSB.append(" ").append(MobileConstant2.ATT_CALL).append("='").append("true").append("'");
							userSB.append(" ").append(MobileConstant2.ATT_SMS).append("='").append("true").append("'");
							userSB.append(" ").append(MobileConstant2.ATT_NAME).append("='").append("MOBILE")
									.append("'").append(">");
							userSB.append((StringUtil.isBlank(telephone) ? "" : telephone));
							userSB.append("</").append(MobileConstant2.TAG_FIELD).append(">");
							userSB.append("<").append(MobileConstant2.TAG_FIELD);
							userSB.append(" ").append(MobileConstant2.ATT_LABEL).append("='")
									.append("{*[email.address]*}").append("'");
							userSB.append(" ").append(MobileConstant2.ATT_MAIL).append("='").append("true").append("'");
							userSB.append(" ").append(MobileConstant2.ATT_NAME).append("='").append("MAIL").append("'")
									.append(">");
							userSB.append((StringUtil.isBlank(email) ? "" : email));
							userSB.append("</").append(MobileConstant2.TAG_FIELD).append(">");
							userSB.append("<").append(MobileConstant2.TAG_FIELD).append(" ");
							userSB.append(MobileConstant2.ATT_LABEL).append("='").append("{*[Department]*}")
									.append("'");
							userSB.append(" ").append(MobileConstant2.ATT_NAME).append("='").append("DEPARTMENT")
									.append("'").append(">");
							userSB.append((StringUtil.isBlank(departmentName) ? "" : departmentName));
							userSB.append("</").append(MobileConstant2.TAG_FIELD).append(">");
							userSB.append("</").append(MobileConstant2.TAG_CONTACT).append(">");

							sb.append(userSB);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				sb.append("</").append(MobileConstant2.TAG_CONTACTDATA).append(">");
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
