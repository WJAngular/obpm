package cn.myapps.core.dynaform.signature.dao;

import java.util.List;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.dynaform.signature.ejb.Htmlhistory;

public interface HtmlhistoryDAO extends IDesignTimeDAO<Htmlhistory>{

	public List<Htmlhistory> queryAll() throws Exception;
	
	public List<Htmlhistory> queryById(String id) throws Exception;
	
	public void createHtmlhistory(Htmlhistory htmlhistory) throws Exception;
}
