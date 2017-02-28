package cn.myapps.core.scheduler.dao;

import java.util.Collection;
import java.util.Date;

import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.myapps.base.dao.HibernateBaseDAO;
import cn.myapps.core.scheduler.ejb.TriggerVO;

public class HibernateTriggerDAO extends HibernateBaseDAO<TriggerVO> implements TriggerDAO {
	public HibernateTriggerDAO(String voClassName) {
		super(voClassName);
	}

	public Collection<TriggerVO> getStandbyTrigger(long interval)
			throws Exception {
		String hql = "from " + _voClazzName+" vo where vo.state!='"+TriggerVO.STATE_STAND_BY+"' and vo.deadline<="+(interval+new Date().getTime());
		
		return getDatas(hql);
	}
	
	/**
	 * 判断触发器中的任务是否已被取消
	 * @param id
	 * 		TriggerVO 的 id
	 * @return
	 */
	public boolean isCancel(String id) throws Exception{
		String hql = "from " + _voClazzName+" vo where vo.id='"+id+"'";
		return getTotalLines(hql)==0;
		
	}
	
	/**
	 * 把所有Trigger的Standby状态更新回Pending状态
	 * @throws Exception
	 */
	public void updateStandbyState2WaitingState() throws Exception {
		Session session = currentSession();
		Transaction tx = currentSession().beginTransaction();
		try {
			ScrollableResults results = session.createQuery("from "+_voClazzName+" where state='"+TriggerVO.STATE_STAND_BY+"'").scroll(ScrollMode.FORWARD_ONLY); 
			int count=0;
			while ( results.next() ) {
				TriggerVO trigger = (TriggerVO) results.get(0);
				trigger.setState(TriggerVO.STATE_WAITING);
				if ( ++count % 20 == 0 ) {
					session.flush();
					session.clear();
					}
			}
			tx.commit(); 
		} catch (Exception e) {
			tx.rollback();
			throw e;
		}finally{
			session.close();
		}
	}
	
	public void removeByToken(String token) throws Exception {
		Session session = currentSession();
		Transaction tx = currentSession().beginTransaction();
		try {
			 String hql = "delete from " + _voClazzName+" vo where vo.token ='"+token+"'";
			 Query query =session.createQuery(hql);
			 query.executeUpdate();
			 tx.commit(); 
		} catch (Exception e) {
			tx.rollback();
			throw e;
		}
	}
}
