package cn.myapps.core.dynaform.signature.action;

import java.util.Collection;
import java.util.Iterator;

import cn.myapps.base.action.BaseHelper;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.signature.ejb.Htmlsignature;
import cn.myapps.core.dynaform.signature.ejb.HtmlsignatureProcess;
import cn.myapps.util.ProcessFactory;

/**
 * 
 * @author Alex
 * 
 */
public class SignatureHelper extends BaseHelper<Htmlsignature> {

	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public SignatureHelper() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(HtmlsignatureProcess.class));
	}

	/**
	 * 判断是否调用电子签章，批量签章按钮。
	 * 
	 * @param DocumentActivities
	 * @param ViewActivities
	 * @return
	 */
	//@SuppressWarnings("unchecked")
	public static Boolean signatureExistMethod(Collection<Activity> documentActivities,
			Collection<Activity> ViewActivities) {
		Boolean signatureExist = false;
		int signatureNum = 0;
		if (documentActivities != null && !documentActivities.isEmpty()) {
			for (Iterator<Activity> aiter = documentActivities.iterator(); aiter
					.hasNext();) {
				Activity act = (Activity) aiter.next();
//				BatchSignature activityType = new BatchSignature(act);
				if (act.getType() == ActivityType.SIGNATURE)
					signatureNum = signatureNum + 1;

			}
		}
		if (ViewActivities != null && !ViewActivities.isEmpty()) {
			for (Iterator<Activity> aiter = ViewActivities.iterator(); aiter.hasNext();) {
				Activity act = (Activity) aiter.next();
//				BatchSignature activityType = new BatchSignature(act);
				if (act.getType() == ActivityType.BATCHSIGNATURE)
					signatureNum = signatureNum + 1;

			}
		}
		if (signatureNum == 0) {
			signatureExist = false;
		} else {
			signatureExist = true;
		}
		return signatureExist;

	}

}
