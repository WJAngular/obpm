package cn.myapps.core.dynaform.view.ejb.editmode;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONArray;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.dynaform.document.dql.DQLASTUtil;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.mapping.TableMapping;
import cn.myapps.core.dynaform.view.ejb.EditMode;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.dynaform.view.ejb.condition.FilterConditionParser;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.DbTypeUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

/**
 * 
 * @author nicholas zhen
 * 
 */
public class DesignEditMode extends AbstractEditMode implements EditMode {
	public DesignEditMode(View view) {
		super(view);
	}

	public String getQueryString(ParamsTable params, WebUser user, Document sDoc) throws Exception {
		String relatedFormId = view.getRelatedForm();
		if(relatedFormId==null)
			return "";

		FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		Form relatedForm = (Form) formProcess.doView(relatedFormId);
		if(relatedForm==null){
			return "";
		}
		TableMapping tableMapping = relatedForm.getTableMapping();

		StringBuffer sqlTmp = new StringBuffer();
		
		sqlTmp.append("SELECT ").append(getSelectPart(tableMapping));
		sqlTmp.append(" FROM ").append(getInnerJoinPart(tableMapping));
		
		String authCondition = view.getAuthorityCondition();
		//有权限字段过滤条件时,拼接过滤条件
		if(!StringUtil.isBlank(authCondition)){
			sqlTmp.append(parseAuthCondition(params, user, authCondition, tableMapping));
			if(sqlTmp != null){
				StringBuffer tmp = new StringBuffer();
				tmp.append("SELECT * FROM (").append(sqlTmp.toString()).append(") d WHERE 1=1");
				sqlTmp.setLength(0);
				sqlTmp.append(tmp.toString());
			}
			boolean appendOr = false;
			if(sqlTmp.indexOf("AND")>=0){
				appendOr = true;
			}
			
			/**
			 * 2011-7-11
			 * 
			 * params参数集增加istmp的定义
			 * 
			 * @author keezzm
			 */
			String istmp = params.getParameterAsString("istmp");
			if (!StringUtil.isBlank(istmp)) {
				if(sqlTmp.indexOf("WHERE") > 0){
					sqlTmp.append(" AND d.ISTMP = " + istmp);
				}else {
					sqlTmp.append(" WHERE d.ISTMP = " + istmp);
				}
			}
			
			//有OR条件时,需要把数据先提取,然后再拼接子条件(如:视图的查询条件)的查询,否则子条件会不生效
			if(appendOr){
				sqlTmp.insert(0, "SELECT d.* FROM (");
				sqlTmp.insert(sqlTmp.length(), ")d WHERE 1=1 ");
			}
		}
		
//		StringBuffer conditionTmp = new StringBuffer();
		
		/*String authField = view.getAuth_fields();
		String scope = view.getAuthFieldScope();
		String auth_user = view.getAuth_user();*/
		/*if(View.AUTHFIELD_AUTHOR.equals(authField)){//作者
			sqlTmp.append(this.buildQueryString4Author(params, user, scope));
		}else if(View.AUTHFIELD_AUTHOR_DEFAULT_DEPT.equals(authField)){//作者默认部门
			sqlTmp.append(this.buildQueryString4AuthorDefaultDept(params, user, scope));
		}else if(View.AUTHFIELD_AUDITOR.equals(authField)){//流程当前处理人
			sqlTmp.append(this.buildQueryString4Auditor(params, user, scope, tableMapping));
		}*/
			
		String sql = sqlTmp.toString();

		String condition = view.getFilterCondition();

		// 判断有没有附加的字段Filter
		if (!StringUtil.isBlank(condition)) {
			FilterConditionParser parser = new FilterConditionParser(params, user, view);
			String sqlPart2 = parser.parseToSQL(condition);
			
			if(sql.toUpperCase().indexOf("WHERE")>0){
				sql += sqlPart2;
			}else{
				sql +=" WHERE 1=1 " +sqlPart2;
			}
			
		}
		//去重,DISTINCT在mssql里不可对ntext等字段进行去重
		//sql = "SELECT t.* FROM (" + sql + ") t WHERE t.id IN (SELECT MAX(d.id) FROM (" + sql + ") d GROUP BY d.id HAVING COUNT(*) > 1)";
		
		return sql;
	}
	
	//解析权限字段过滤条件,并拼接过滤条件
	@SuppressWarnings("unchecked")
	private String parseAuthCondition(ParamsTable params, WebUser user, String condition, TableMapping tableMapping) throws Exception{
		StringBuffer sql = new StringBuffer();
		boolean flag = false;
		JSONArray array = JSONArray.fromObject(condition);
		List<MorphDynaBean> list = (List<MorphDynaBean>) JSONArray.toCollection(array);
		for (Iterator<MorphDynaBean> iter = list.iterator(); iter.hasNext();) {
			MorphDynaBean bean = iter.next();
			String _authFields = (String) bean.get("_authFields");
			String _authFieldScope = (String) bean.get("_authFieldScope");
			
			if(View.AUTHFIELD_AUTHOR.equals(_authFields)){//作者
				sql.append(this.buildQueryString4Author(params, user, _authFieldScope));
			}else if(View.AUTHFIELD_AUTHOR_DEFAULT_DEPT.equals(_authFields)){//作者默认部门
				sql.append(this.buildQueryString4AuthorDefaultDept(params, user, _authFieldScope));
			}else if(View.AUTHFIELD_AUDITOR.equals(_authFields)){//流程当前处理人
				sql.append(this.buildQueryString4Auditor(params, user, _authFieldScope, tableMapping));
			}else if(View.AUTHFIELD_PROCESSED.equals(_authFields)){//处理过
				sql.insert(0, this.buildQueryString4Processed(params, user, _authFieldScope, tableMapping));
				flag = true;
			}else if(_authFields.contains("userField")){//单选用户选择框
				sql.append(this.buildQueryString4userField(_authFields, params, user, _authFieldScope, tableMapping));
			}else if(_authFields.contains("multiUserField")){//多选用户选择框
				sql.append(this.buildQueryString4multiUserField(_authFields, params, user, _authFieldScope, tableMapping));
			}else if(_authFields.contains("departmentField")){//部门选择框
				sql.append(this.buildQueryString4departmentField(_authFields, params, user, _authFieldScope, tableMapping));
			}else if(_authFields.contains("treeTepartmentField")){//树形部门选择框
				sql.append(this.buildQueryString4treeDepartmentField(_authFields, params, user, _authFieldScope, tableMapping));
			}
		}
		
		if(flag){
			return sql.toString();
		}else{
			String _sql = sql.toString();
			
			if(!StringUtil.isBlank(_sql)){
				_sql = _sql.substring(3);
				return " WHERE " + _sql;
			}
			
			return "";
			
		}
	}

	private String buildQueryString4Author(ParamsTable params, WebUser user,String scope) throws Exception{
		StringBuffer sql = new StringBuffer();
		if(View.AUTHFIELD_SCOPE_ITSELF.equals(scope)){//作者自身
			sql.append(" OR d.AUTHOR ='").append(user.getId()).append("'");		
		}else if(View.AUTHFIELD_SCOPE_AUTHOR_SUPERIOR.equals(scope)){//直属上级用户
			UserProcess process = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
			Collection<UserVO> lowerList = process.getUnderList(user.getId(), 1);
			StringBuffer ids = new StringBuffer("'"+user.getId()+"',");
			for(Iterator<UserVO> iter = lowerList.iterator();iter.hasNext();){
				UserVO u = iter.next();
				ids.append("'").append(u.getId()).append("',");
			}
			if(ids.length()>0) ids.setLength(ids.length()-1);
			if(ids.length()==0) ids.append("''");
			sql.append(" OR d.AUTHOR in(").append(ids).append(")");
			
		}else if(View.AUTHFIELD_SCOPE_AUTHOR_ALL_SUPERIOR.equals(scope)){//所有上级用户
			UserProcess process = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
			Collection<UserVO> lowerList = process.getUnderList(user.getId(), Integer.MAX_VALUE);
			StringBuffer ids = new StringBuffer("'"+user.getId()+"',");
			for(Iterator<UserVO> iter = lowerList.iterator();iter.hasNext();){
				UserVO u = iter.next();
				ids.append("'").append(u.getId()).append("',");
			}
			if(ids.length()>0) ids.setLength(ids.length()-1);
			if(ids.length()==0) ids.append("''");
			sql.append(" OR d.AUTHOR in(").append(ids).append(")");
			
		}else if(View.AUTHFIELD_SCOPE_AUTHOR_LOWER.equals(scope)){//直属下级用户
			String superior ="";
			if(user.getSuperior() !=null){
				superior = user.getSuperior().getId();
			}
			sql.append(" OR d.AUTHOR in('").append(superior).append("','").append(user.getId()).append("')");
		}else if(View.AUTHFIELD_SCOPE_AUTHOR_ALL_LOWER.equals(scope)){//所有下级用户
			StringBuffer superiorIds = new StringBuffer("'"+user.getId()+"',");
			UserVO superior = user.getSuperior();
			if(superior != null){
				superiorIds.append("'").append(superior.getId()).append("',");
				while((superior = superior.getSuperior()) != null){
					superiorIds.append("'").append(superior.getId()).append("',");
				}
				
			}
			
			if(superiorIds.length()>0) superiorIds.setLength(superiorIds.length()-1);
			if(superiorIds.length()==0) superiorIds.append("''");
			
			sql.append(" OR d.AUTHOR in(").append(superiorIds).append(")");
		}
		
		return sql.toString();
	}
	
	private String buildQueryString4AuthorDefaultDept(ParamsTable params, WebUser user,String scope) throws Exception{
		
		if(StringUtil.isBlank(user.getDefaultDepartment())){
			throw new OBPMValidateException("您没有设置默认部门，系统无法检索数据。");
		}
		
		StringBuffer sql = new StringBuffer();
		String defaultDeptIndex ="";//用户默认部门的索引编号
		DepartmentProcess process = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
		defaultDeptIndex = ((DepartmentVO)process.doView(user.getDefaultDepartment())).getIndexCode();
		
		if(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_DEFAULT.equals(scope)){//作者默认部门
			sql.append(" OR d.AUTHOR_DEPT_INDEX ='").append(defaultDeptIndex).append("'");
			
		}else if(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_ALL_SUPERIOR.equals(scope)){//所有上级部门
			sql.append(" OR d.AUTHOR_DEPT_INDEX like '").append(defaultDeptIndex).append("_%'");
			
		}else if(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_ALL_LOWER.equals(scope)){//所有下级部门
			String[] s = defaultDeptIndex.split("_");
			StringBuffer part = new StringBuffer("'" + s[0] + "',");
			if (s.length > 1) {
				StringBuffer temp = new StringBuffer(s[0]);
				for (int i = 1; i < s.length-1; i++) {
					temp.append("_").append(s[i]);
					part.append("'").append(temp).append("',");
				}
			}
			if(part.length()>0) part.setLength(part.length()-1);
			if(part.length()==0) part.append("''");
			sql.append(" OR d.AUTHOR_DEPT_INDEX in (").append(part).append(")");
			
		}else if(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_LINE_SUPERIOR.equals(scope)){//直属上级部门
			sql.append(" OR d.AUTHOR_DEPT_INDEX like '").append(defaultDeptIndex).append("_____________________________________'");
			
		}else if(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_LINE_LOWER.equals(scope)){//直属下级部门
			if(defaultDeptIndex.contains("_")){
				sql.append(" OR d.AUTHOR_DEPT_INDEX ='").append(defaultDeptIndex.substring(0,defaultDeptIndex.lastIndexOf("_"))).append("'");
			}else {
				sql.append(" OR d.AUTHOR_DEPT_INDEX ='").append(defaultDeptIndex).append("'");
			}
			
		}
		
		return sql.toString();
	}
	
	private String buildQueryString4Auditor(ParamsTable params, WebUser user,String scope,TableMapping tableMapping) throws Exception{
		StringBuffer sql = new StringBuffer();
		if(View.AUTHFIELD_SCOPE_ITSELF.equals(scope)){
			sql.append(" OR (d.ID IN(SELECT DOC_ID FROM ").append(tableMapping.getTableName(DQLASTUtil.TABLE_TYPE_AUTH))
				.append(" AUTH WHERE AUTH.VALUE = '").append(user.getId()).append("'))");
		}
		return sql.toString();
	}
	
	private String buildQueryString4Processed(ParamsTable params, WebUser user, String scope, TableMapping tableMapping) throws Exception{
		StringBuffer sql = new StringBuffer();
		String applicationId = params.getParameterAsString("application");
		if(StringUtil.isBlank(applicationId)){
			applicationId = tableMapping.getApplicationId();
		}
		if(View.AUTHFIELD_SCOPE_ITSELF.equals(scope)){
			sql.append(getProcessedSQL(user.getId(), applicationId));
		}
		return sql.toString();
	}
	
	private String buildQueryString4userField(String _authFields, ParamsTable params, WebUser user,String scope, TableMapping tableMapping) throws Exception{
		StringBuffer sql = new StringBuffer();
		String columnName = tableMapping.getColumnName(_authFields.substring(_authFields.indexOf("_") + 1));
		if(View.AUTHFIELD_SCOPE_ITSELF.equals(scope)){//用户自身
//			sql.append(" OR d." + columnName + "='").append(user.getId()).append("'");
			sql.append(" OR " + "cast(d."+columnName +" AS varchar(2000)) ='").append(user.getId()).append("'");
		}else if(View.AUTHFIELD_SCOPE_AUTHOR_SUPERIOR.equals(scope)){//直属上级用户可见
			UserProcess process = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
			Collection<UserVO> lowerList = process.getUnderList(user.getId(), 1);
			StringBuffer ids = new StringBuffer("'"+user.getId()+"',");
			for(Iterator<UserVO> iter = lowerList.iterator();iter.hasNext();){
				UserVO u = iter.next();
				ids.append("'").append(u.getId()).append("',");
			}
			if(ids.length()>0) ids.setLength(ids.length()-1);
			if(ids.length()==0) ids.append("''");
			sql.append(" OR d." + columnName + " in(").append(ids).append(")");
			
		}else if(View.AUTHFIELD_SCOPE_AUTHOR_ALL_SUPERIOR.equals(scope)){//所有上级用户可见
			UserProcess process = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
			Collection<UserVO> lowerList = process.getUnderList(user.getId(), Integer.MAX_VALUE);
			StringBuffer ids = new StringBuffer("'"+user.getId()+"',");
			for(Iterator<UserVO> iter = lowerList.iterator();iter.hasNext();){
				UserVO u = iter.next();
				ids.append("'").append(u.getId()).append("',");
			}
			if(ids.length()>0) ids.setLength(ids.length()-1);
			if(ids.length()==0) ids.append("''");
			sql.append(" OR d." + columnName + " in(").append(ids).append(")");
			
		}else if(View.AUTHFIELD_SCOPE_AUTHOR_LOWER.equals(scope)){//直属下级用户可见
			String superior ="";
			if(user.getSuperior() !=null){
				superior = user.getSuperior().getId();
			}
			sql.append(" OR d." + columnName + " in('").append(superior).append("','").append(user.getId()).append("')");
		}else if(View.AUTHFIELD_SCOPE_AUTHOR_ALL_LOWER.equals(scope)){//所有下级用户可见
			StringBuffer superiorIds = new StringBuffer("'"+user.getId()+"',");
			UserVO superior = user.getSuperior();
			if(superior != null){
				superiorIds.append("'").append(superior.getId()).append("',");
				while((superior = superior.getSuperior()) != null){
					superiorIds.append("'").append(superior.getId()).append("',");
				}
				
			}
			
			if(superiorIds.length()>0) superiorIds.setLength(superiorIds.length()-1);
			if(superiorIds.length()==0) superiorIds.append("''");
			
			sql.append(" OR d." + columnName + " in(").append(superiorIds).append(")");
		}
		
		return sql.toString();
	}
	
	private String buildQueryString4multiUserField(String _authFields, ParamsTable params, WebUser user,String scope, TableMapping tableMapping) throws Exception{
		StringBuffer sql = new StringBuffer();
		String columnName = tableMapping.getColumnName(_authFields.substring(_authFields.indexOf("_") + 1));
		if(View.AUTHFIELD_SCOPE_ITSELF.equals(scope)){//用户自身
			sql.append(" OR d." + columnName + " like '%").append(user.getId()).append("%'");
		}
		return sql.toString();
	}
	
	private String buildQueryString4departmentField(String _authFields, ParamsTable params, WebUser user, String scope, TableMapping tableMapping) throws Exception{

		if(StringUtil.isBlank(user.getDefaultDepartment())){
			throw new OBPMValidateException("您没有设置默认部门，系统无法检索数据。");
		}
		
		StringBuffer sql = new StringBuffer();
		String columnName = tableMapping.getColumnName(_authFields.substring(_authFields.indexOf("_") + 1));
		String defaultDeptIndex ="";//用户默认部门的索引编号
		DepartmentProcess process = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
		DepartmentVO thisDepartment = (DepartmentVO)process.doView(user.getDefaultDepartment());
		defaultDeptIndex = thisDepartment.getIndexCode();
		
		if(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_DEFAULT.equals(scope)){//用户默认部门
			sql.append(" OR d." + columnName + "='").append(user.getDefaultDepartment()).append("'");
			
		}else if(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_ALL_SUPERIOR.equals(scope)){//所有上级部门
			StringBuffer queryStr = new StringBuffer();
 			Collection<DepartmentVO> depts = process.getUnderDeptList(user.getDefaultDepartment(), Integer.MAX_VALUE, false);
			for(Iterator<DepartmentVO> it = depts.iterator(); it.hasNext();){
				DepartmentVO department = it.next();
				queryStr.append("'").append(department.getId()).append("',");
			}
			if(queryStr.length()>1)
				queryStr.setLength(queryStr.length()-1);
			if(queryStr.length() == 0)
				queryStr.append("''");
				
			sql.append(" OR d." + columnName + " in (").append(queryStr.toString()).append(")");
		}else if(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_ALL_LOWER.equals(scope)){//所有下级部门
			String[] s = defaultDeptIndex.split("_");
			StringBuffer part = new StringBuffer();
			if (s.length > 0) {
				for (int i = 0; i < s.length-1; i++) {
					part.append("'").append(s[i]).append("',");
				}
				if(part.length()>0) part.setLength(part.length()-1);
				
			}
			if(part.length()==0) part.append("''");
			sql.append(" OR d." + columnName + " in (").append(part).append(")");
			
		}else if(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_LINE_SUPERIOR.equals(scope)){//直属上级部门
			StringBuffer queryStr = new StringBuffer();
 			Collection<DepartmentVO> depts = process.getUnderDeptList(user.getDefaultDepartment(), 1, false);
			for(Iterator<DepartmentVO> it = depts.iterator(); it.hasNext();){
				DepartmentVO department = it.next();
				queryStr.append("'").append(department.getId()).append("',");
			}
			if(queryStr.length()>1)
				queryStr.setLength(queryStr.length()-1);
			if(queryStr.length() == 0)
				queryStr.append("''");
			
			sql.append(" OR d." + columnName + " in (").append(queryStr.toString()).append(")");
			
		}else if(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_LINE_LOWER.equals(scope)){//直属下级部门
			String[] s = defaultDeptIndex.split("_");
			StringBuffer part = new StringBuffer();
			if(s.length > 1){
				part.append(s[s.length-2]);
			}
			if(part.length() == 0) part.append("");
			sql.append(" OR d." + columnName + "='").append(part).append("'");
		}
		
		return sql.toString();
	}
	
	private Object buildQueryString4treeDepartmentField(String authFields,
			ParamsTable params, WebUser user, String scope,TableMapping tableMapping) throws Exception{
		StringBuffer sql = new StringBuffer();
		if(StringUtil.isBlank(user.getDefaultDepartment())){
			throw new OBPMValidateException("您没有设置默认部门，系统无法检索数据。");
		}
		
		String columnName = tableMapping.getColumnName(authFields.substring(authFields.indexOf("_") + 1));
		if(View.AUTHFIELD_SCOPE_AUTHOR_DEPT_DEFAULT.equals(scope)){
			sql.append(" OR d." + columnName + " like '%").append(user.getDefaultDepartment()).append("%'");
		}
		
		return sql.toString();
	}
	
	/**
	 * 获取SQL关联部分
	 * 
	 * @param tableMapping
	 *            表关系映射
	 * @param tableType
	 *            表类型
	 * @return
	 */
	private String getInnerJoinPart(TableMapping tableMapping) {
		int tableType = DQLASTUtil.TABEL_TYPE_CONTENT;

		String sql = "";
		if (tableMapping.getFormType() == Form.FORM_TYPE_NORMAL) {
			sql += tableMapping.getTableName(tableType) + " d";
		} else {
			sql = DQLASTUtil._TBNAME + " d";
			sql += " INNER JOIN " + tableMapping.getTableName(tableType) + " m";
			sql += " ON d.MAPPINGID=m." + tableMapping.getPrimaryKeyName();
		}

		return sql;
	}

	private String getSelectPart(TableMapping tableMapping) {
		String sql = "";
		if (tableMapping.getFormType() == Form.FORM_TYPE_NORMAL) {
			sql += "d.*";
		} else {
			sql += "d.*," + tableMapping.getColumnListString();
		}

		return sql;
	}

	/**
	 * 根据在视图中定义的部门级别返回当前用户可看的部门列表
	 * 
	 * @param dept
	 *            视图中定义的部门列表
	 * @param user
	 *            当前用户
	 * @return 用户可看的部门列表
	 * @throws Exception
	 * @deprecated 2.5vSP4改造视图权限配置后 此方法废弃
	 */
	@Deprecated
	private String getDepartmentList(String dept, WebUser user) throws Exception {

		StringBuffer deptList = new StringBuffer();
		if (dept.equalsIgnoreCase("superior")) {// 上级部门
			String lowerDepartmentList = user.getLowerDepartmentList(false);
			if(lowerDepartmentList !=null){
				deptList.append(user.getDeptlist()).append(",");
				deptList.append(lowerDepartmentList);
			}
			/*if (user.getSuperiorDepartmentList() != null) {
				deptList = user.getSuperiorDepartmentList();
			}*/
		} else if (dept.equalsIgnoreCase("lower")) {// 下级部门
			String superiorDepartmentList = user.getSuperiorDepartmentList();
			if(superiorDepartmentList!=null){
				deptList.append(user.getDeptlist()).append(",");
				deptList.append(superiorDepartmentList);
			}
			/*if (user.getLowerDepartmentList() != null) {
				deptList = user.getLowerDepartmentList(true);// 排除同级部门
			}*/
		} else if (dept.equalsIgnoreCase("self")) {// 同级部门
			if (user.getDepartments() != null) {
				Collection<DepartmentVO> depts = user.getDepartments();
				for (Iterator<DepartmentVO> iterator = depts.iterator(); iterator.hasNext();) {
					DepartmentVO vo = (DepartmentVO) iterator.next();
					deptList.append("'").append(vo.getId()).append("'").append(",");
				}

				while (deptList.toString().endsWith(",")) {
					deptList.setLength(deptList.length() - 1);
				}
			}
		}
		if (StringUtil.isBlank(deptList.toString())) {
			return "''";
		}
		return deptList.toString();
	}
	
	/**
	 * 设置所查所有数据总计
	 * @param params
	 * @param user
	 * @param fieldName
	 * @param _formid
	 * @param domainid
	 * @return
	 */
	public double getSumTotal(ParamsTable params, WebUser user, String fieldName, String _formid ,String domainid){
		double total = 0;
		try {
			DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, view
					.getApplicationid());
			String sql = getQueryString4SumTotal(params, user, fieldName, _formid); //拼接查询总计的语句sql
			
			//根据sql查询总计
			total = dp.sumBySQL(sql, domainid);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return total;
	}
	
	/**
	 * 拼接总计的查询语句
	 * @param params
	 * @param user
	 * @param fieldName
	 * @param _formid
	 * @return
	 */
	public String getQueryString4SumTotal(ParamsTable params, WebUser user, String fieldName, String _formid){
		StringBuffer sql = new StringBuffer();
		try {
			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			Form relateForm = (Form) formProcess.doView(_formid);
			View view = (View) viewProcess.doView(params.getParameterAsArray("_viewid")[0]);
			TableMapping tableMapping = relateForm.getTableMapping();
			
			String authCondition = view.getAuthorityCondition();
			
			//有权限字段过滤条件时,拼接过滤条件
			StringBuffer sqlTmp = new StringBuffer();
			sqlTmp.append(" SELECT ").append(getSelectPart(tableMapping));
			sqlTmp.append(" FROM ").append(getInnerJoinPart(tableMapping));
			if(!StringUtil.isBlank(authCondition)){
				sqlTmp.append(parseAuthCondition(params, user, authCondition, tableMapping));
				if(sqlTmp != null){
					StringBuffer tmp = new StringBuffer();
					tmp.append("( SELECT * FROM (").append(sqlTmp.toString()).append(") ");
					sqlTmp.setLength(0);
					sqlTmp.append(tmp.toString());
				}
				boolean appendOr = false;
				if(sqlTmp.indexOf("AND")>=0){
					appendOr = true;
				}
				
				/**
				 * 2011-7-11
				 * 
				 * params参数集增加istmp的定义
				 * 
				 * @author keezzm
				 */
				String istmp = params.getParameterAsString("istmp");
				if (!StringUtil.isBlank(istmp)) {
					if(sqlTmp.indexOf("WHERE") > 0){
						sqlTmp.append(" AND d.ISTMP = " + istmp);
					}else {
						sqlTmp.append(" WHERE d.ISTMP = " + istmp);
					}
				}
				
				//有OR条件时,需要把数据先提取,然后再拼接子条件(如:视图的查询条件)的查询,否则子条件会不生效
				if(appendOr){
					sqlTmp.insert(0, "SELECT d.* FROM (");
					sqlTmp.insert(sqlTmp.length(), ") d WHERE 1=1 ");
				}
				sqlTmp.append(" c )");
				
			}
			sql.append("SELECT ").append(" d.* ");
			sql.append(" FROM ");
			if(!StringUtil.isBlank(authCondition)){
				sql.append(sqlTmp.toString()).append(" d");
			}else{
				sql.append(tableMapping.getTableName()).append(" d");
			}
			sql.append(" WHERE 1 = 1");
			
			//拼接权限条件
			String authField = view.getAuth_fields();
			String scope = view.getAuthFieldScope();
			String auth_user = view.getAuth_user();
			if(View.AUTHFIELD_AUTHOR.equals(authField)){//作者
				sql.append(this.buildQueryString4Author(params, user, scope));
			}else if(View.AUTHFIELD_AUTHOR_DEFAULT_DEPT.equals(authField)){//作者默认部门
				sql.append(this.buildQueryString4AuthorDefaultDept(params, user, scope));
			}else if(View.AUTHFIELD_AUDITOR.equals(authField)){//流程当前处理人
				sql.append(this.buildQueryString4Auditor(params, user, scope, tableMapping));
			}
			
			// 作者条件
			if (!StringUtil.isBlank(auth_user)) {
				boolean appendOr = false;
				if(sql.indexOf("AND")>=0){
					sql.append(" OR ");
					appendOr = true;
				}else{
					sql.append(" AND ");
				}
				sql.append(" d.AUTHOR ='").append(user.getId()).append("'");
				//有OR条件时,需要把数据先提取,然后再拼接子条件(如:视图的查询条件)的查询,否则子条件会不生效
				if(appendOr){
					sql.insert(0, "SELECT d.* FROM (");
					sql.insert(sql.length(), ")d WHERE 1=1 ");
					
				}
			}
			
			if(params.getParameterAsBoolean("isRelate") 
					&& !StringUtil.isBlank(params.getParameterAsString("parentid"))){
				sql.append(" AND PARENT='").append(params.getParameterAsString("parentid")).append("' ");
			}
			
			sql.insert(0, "SELECT SUM(cast(" + tableMapping.getColumnName(fieldName) + " as decimal(38,10))) FROM (");
			sql.insert(sql.length(), ")d WHERE 1=1 ");
			//有查询条件时,拼接查询条件
			String condition = view.getFilterCondition();
			if(!StringUtil.isBlank(condition)){
				FilterConditionParser parser = new FilterConditionParser(params, user, view);
				String sqlPart2 = parser.parseToSQL(condition);
				sql.append(sqlPart2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sql.toString();
	}
	
	/**
	 * 获取已处理SQL语句
	 * 
	 * @param actorId
	 * @return
	 */
	protected String getProcessedSQL(String actorId, String applicationId) throws Exception{
		String dbType = DbTypeUtil.getDBType(applicationId);
		String schema = getSchema(applicationId);
		String processedSQL = "";
		
		if(dbType.equals(DbTypeUtil.DBTYPE_MYSQL)){
			processedSQL = getProcessedSQLByMYSQL(actorId, schema);
		}else if(dbType.equals(DbTypeUtil.DBTYPE_MSSQL)){
			processedSQL = getProcessedSQLByMSSQL(actorId, schema);
		}else if(dbType.equals(DbTypeUtil.DBTYPE_ORACLE)){
			processedSQL = getProcessedSQLByORACLE(actorId, schema);
		}else if(dbType.equals(DbTypeUtil.DBTYPE_DB2)){
			processedSQL = getProcessedSQLByDB2(actorId, schema);
		}
		
		String rtnSql = " WHERE D.ID IN(" + processedSQL + " ) ";
		return rtnSql;
	}
	
	private String getProcessedSQLByDB2(String actorId, String schema) {
		String processedSQL = "select distinct doc.id DOCID "
			+ " from  "
			+ getFullTableName("T_DOCUMENT", schema)
			+ "  doc,"
			+ getFullTableName("t_actorhis", schema)
			+ " ahis,"
			+ getFullTableName("t_relationhis", schema)
			+ " rhis,"
			+ getFullTableName("t_flow_intervention", schema)
			+ " pen,"
			+ getFullTableName("t_flowstatert", schema)
			+ " fs"
			+ " where "
			+ "doc.id=rhis.docid and rhis.id = ahis.nodehis_id and doc.id = pen.id and doc.id =fs.docid and "
			+ "doc.parent is null and doc.state is not null and "
			+ "doc.statelabel is not null "
			+ " and ahis.actorid in ('"
			+ actorId
			+ "')";

		return processedSQL;
	}

	private String getProcessedSQLByORACLE(String actorId, String schema) {
		String processedSQL = "select distinct doc.id DOCID "
			+ " from  "
			+ getFullTableName("T_DOCUMENT", schema)
			+ "  doc,"
			+ getFullTableName("t_actorhis", schema)
			+ " ahis,"
			+ getFullTableName("t_relationhis", schema)
			+ " rhis,"
			+ getFullTableName("t_flow_intervention", schema)
			+ " pen,"
			+ getFullTableName("t_flowstatert", schema)
			+ " fs"
			+ " where "
			+ "doc.id=rhis.docid and rhis.id = ahis.nodehis_id and doc.id = pen.id and doc.id =fs.docid and "
			+ "doc.parent is null and doc.state is not null and "
			+ "doc.statelabel is not null "
			+ " and ahis.actorid in ('"
			+ actorId
			+ "')";

		return processedSQL;
	}

	private String getProcessedSQLByMSSQL(String actorId, String schema) {
		String processedSQL = "select distinct doc.id DOCID "
			+ " from  "
			+ getFullTableName("T_DOCUMENT", schema)
			+ "  doc,"
			+ getFullTableName("t_actorhis", schema)
			+ " ahis,"
			+ getFullTableName("t_relationhis", schema)
			+ " rhis,"
			+ getFullTableName("t_flow_intervention", schema)
			+ " pen,"
			+ getFullTableName("t_flowstatert", schema)
			+ " fs"
			+ " where "
			+ "doc.id=rhis.docid and rhis.id = ahis.nodehis_id and doc.id = pen.id and doc.id =fs.docid and "
			+ "doc.parent is null and doc.state is not null and "
			+ "doc.statelabel is not null "
			+ " and ahis.actorid in ('"
			+ actorId
			+ "')";

		return processedSQL;
	}

	private String getProcessedSQLByMYSQL(String actorId, String schema) {
		String processedSQL = "select distinct doc.id DOCID "
			+ " from  "
			+ getFullTableName("T_DOCUMENT", schema)
			+ "  doc,"
			+ getFullTableName("t_actorhis", schema)
			+ " ahis,"
			+ getFullTableName("t_relationhis", schema)
			+ " rhis,"
			+ getFullTableName("t_flow_intervention", schema)
			+ " pen,"
			+ getFullTableName("t_flowstatert", schema)
			+ " fs"
			+ " where "
			+ "doc.id=rhis.docid and rhis.id = ahis.nodehis_id and doc.id = pen.id and doc.id =fs.docid and "
			+ "doc.parent is null and doc.state is not null and "
			+ "doc.statelabel is not null "
			+ " and ahis.actorid = '"
			+ actorId + "'";

		return processedSQL;
	}

	private String getFullTableName(String tblname, String schema) {
		if (schema != null && !schema.trim().equals("")) {
			return schema.trim().toUpperCase() + "."
					+ tblname.trim().toUpperCase();
		}
		return tblname.trim().toUpperCase();
	}
	
	private String getSchema(String applicationId) throws Exception{
		return DbTypeUtil.getSchema(applicationId);
	}
}
