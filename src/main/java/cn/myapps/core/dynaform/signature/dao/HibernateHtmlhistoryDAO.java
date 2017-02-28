package cn.myapps.core.dynaform.signature.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.dynaform.signature.ejb.Htmlhistory;

public class HibernateHtmlhistoryDAO extends HibernateBaseDAO<Htmlhistory> implements HtmlhistoryDAO {

	public HibernateHtmlhistoryDAO(String voClassName){
		super(voClassName);
	}
	public List<Htmlhistory> queryAll() throws Exception {
		
		String hql = "from Htmlhistory ";
		return (List<Htmlhistory>)getDatas(hql);
	}

	public List<Htmlhistory> queryById(String id) throws Exception {
		
		return null;
	}
	public void createHtmlhistory(Htmlhistory htmlhistory) throws Exception {
		Session session = HibernateBaseDAO.currentSession();
		Transaction ts = null;
		try{
			ts = session.beginTransaction();
			session.save(htmlhistory);
			ts.commit();
		}catch(Exception e){
			if(ts!=null)ts.rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
		
	}

	

}
