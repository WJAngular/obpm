package cn.myapps.core.workflow.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.action.ImpropriateException;

/**
 * 流程流转令牌校验器，负责流程流转过程中的令牌发放和验证，用于防止文档在同一时刻多次流转。
 * 
 * @author Happy
 *
 */
public class FlowTicketValidator {
	
	/**
	 * 正在执行流程提交事务的document
	 */
	private static Map<String,String> flowTicketPool = new ConcurrentHashMap<String, String>();
	private static Map<String,ThreadLocal<Integer>> ticketWeightPool = new ConcurrentHashMap<String, ThreadLocal<Integer>>();
	
	
	public static void valid(Document doc) throws Exception {
		
		boolean isAllow = false;
		
		String flowTicket = doc.getFlowTicket();//文档中的流程令牌
		String _flowTicket = flowTicketPool.get(doc.getId());//流程令牌池
		
		if(flowTicket !=null && _flowTicket !=null && flowTicket.equals(_flowTicket)){
			isAllow = true;
		}
		
		if(flowTicket !=null && _flowTicket == null){
			flowTicketPool.put(doc.getId(), flowTicket);
			isAllow = true;
		}
		
		if(flowTicket==null && _flowTicket ==null){
			String token = Thread.currentThread().getName()+":"+Thread.currentThread().getId();
			doc.setFlowTicket(token);
			flowTicketPool.put(doc.getId(), token);
			isAllow = true;
		}
		
		int weight = getTicketWeight(doc.getId());
		weight++;
		setTicketWeight(doc.getId(),weight);
		
		if(!isAllow){
			throw new OBPMValidateException("{*[core.form.submit.norepeat]*}", new ImpropriateException("{*[core.form.submit.norepeat]*}")) ;
		}
		
	}

	public static void flush(Document doc) throws Exception {
		int weight = getTicketWeight(doc.getId());
		weight--;

		if (weight == 0) {
			String flowTicket = doc.getFlowTicket();//文档中的流程令牌
			String _flowTicket = flowTicketPool.get(doc.getId());//流程令牌池
			if(flowTicket !=null && _flowTicket !=null && flowTicket.equals(_flowTicket)){
				flowTicketPool.remove(doc.getId());
			}
		}

		setTicketWeight(doc.getId(),weight);
	}
	
	private static int getTicketWeight(String docId) {
		ThreadLocal<Integer> ticketWeight = ticketWeightPool.get(docId);
		if(ticketWeight ==null){
			ticketWeight = new ThreadLocal<Integer>();
			ticketWeightPool.put(docId, ticketWeight);
		}
		Integer weight = (Integer) ticketWeight.get();
		if (weight != null) {
			return weight.intValue();
		} else {
			return 0;
		}
	}

	private static void setTicketWeight(String docId,int weight) {
		ThreadLocal<Integer> ticketWeight = ticketWeightPool.get(docId);
		if(ticketWeight ==null){
			ticketWeight = new ThreadLocal<Integer>();
			ticketWeightPool.put(docId, ticketWeight);
		}
		ticketWeight.set(Integer.valueOf(weight));
	}

}
