package cn.myapps.km.comments.ejb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.base.ejb.NRunTimeProcess;

public interface CommentsProcess extends NRunTimeProcess<Comments>{
	
	/**
	 * 好评数
	 *
	 */
	public long countByGood(String fileID) throws Exception ;
	
	/**
	 * 差评数
	 *
	 */
	public long countByBad(String fileID) throws Exception ;

	public long countBy(String fileId,String userId) throws Exception;
	/**
	 * @param no
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void doCreate(NObject no,HttpServletRequest request,HttpServletResponse response) throws Exception;

	
}
