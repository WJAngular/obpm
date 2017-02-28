package cn.myapps.core.dynaform.form.ejb;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import cn.myapps.constans.Environment;
import cn.myapps.constans.Web;
import cn.myapps.core.user.action.WebUser;

public class WordFieldHelper {

	/**
	 * 检查word文档是否可编辑
	 * @param wordid
	 * @return 
	 *  1.可编辑，返回(null）
	 *  2.不可编辑，返回(当前正在编辑word文档的用户名称)
	 * @throws Exception
	 */
	public static String checkWordFieldIsEdit(String wordid,
			HttpServletRequest request)throws Exception {
		
		String result = null;
		WebUser user = (WebUser) request.getSession().getAttribute(
				Web.SESSION_ATTRIBUTE_FRONT_USER);
		WordFieldIsEdit wordEdit = Environment.wordFieldIsEdit.get(wordid);
		if(wordEdit != null){
			if(!wordEdit.getUserid().equals(user.getId()))
				result = wordEdit.getUsername();
		}
		
		doCreateOrUpdateWordFieldIsEdit(wordid,request);
		
		return result;
	}
	
	/**
	 * 创建或更新word文档占用对象
	 * @param wordid
	 * @param request
	 * @throws Exception
	 */
	public static void doCreateOrUpdateWordFieldIsEdit(String wordid, 
			HttpServletRequest request)throws Exception {
		
		WebUser user = (WebUser) request.getSession().getAttribute(
				Web.SESSION_ATTRIBUTE_FRONT_USER);
		WordFieldIsEdit wordEdit = Environment.wordFieldIsEdit.get(wordid);
		if(wordEdit == null){
			WordFieldIsEdit wordEditVO = new WordFieldIsEdit();
			wordEditVO.setWordid(wordid);
			wordEditVO.setUserid(user.getId());
			wordEditVO.setUsername(user.getName());
			wordEditVO.setEditAble(false);
			wordEditVO.setEditTime(new Date());
			Environment.wordFieldIsEdit.put(wordid, wordEditVO);
		}else{
			//当前正在编辑word文档的用户才有更新权限
			if(wordEdit.getUserid().equals(user.getId())){
				wordEdit.setWordid(wordid);
				wordEdit.setEditAble(false);
				wordEdit.setEditTime(new Date());
				Environment.wordFieldIsEdit.remove(wordid);
				Environment.wordFieldIsEdit.put(wordid, wordEdit);
			}
		}
	}
	
	/**
	 * 退出编辑
	 * @param wordid
	 * @param request
	 * @throws Exception
	 */
	public static void doExixt(String wordid, 
			HttpServletRequest request)throws Exception {
		WebUser user = (WebUser) request.getSession().getAttribute(
				Web.SESSION_ATTRIBUTE_FRONT_USER);
		WordFieldIsEdit wordEdit = Environment.wordFieldIsEdit.get(wordid);
		if(wordEdit != null){
			if(wordEdit.getUserid().equals(user.getId())){
				Environment.wordFieldIsEdit.remove(wordid);
			}
		}
	}
	
}
