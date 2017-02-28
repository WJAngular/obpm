package cn.myapps.core.dynaform.signature.ejb;

import java.util.List;

import cn.myapps.base.ejb.IDesignTimeProcess;

/**
 * 
 * @author Alex
 * 
 */
public interface HtmlhistoryProcess extends IDesignTimeProcess<Htmlhistory> {

	public List<Htmlhistory> queryAll() throws Exception;

	public List<Htmlhistory> queryById(String id) throws Exception;

	public void createHtmlhistory(Htmlhistory htmlhistory) throws Exception;
}
