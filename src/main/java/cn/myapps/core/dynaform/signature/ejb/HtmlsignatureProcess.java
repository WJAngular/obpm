package cn.myapps.core.dynaform.signature.ejb;

import java.util.List;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.user.action.WebUser;
/**
 * 
 * @author Alex
 *
 */
public interface HtmlsignatureProcess<E> extends IDesignTimeProcess<E> {

	public List<E> queryAll() throws Exception;
	public List<E> queryById(String SignatureID,String DocumentID, String FormID) throws Exception;
	public List<E> queryByDocumentID(String DocumentID, String FormID) throws Exception;
	public void updateHtmlsignature(Htmlsignature htmlsignature) throws Exception;
	public void createHtmlsignature(String mDocumentID,String mSignatureID,String mSignature , String FormID) throws Exception;

	public void getDocument(ParamsTable params, WebUser user) throws Exception;

	public void getBatchDocument(ParamsTable params) throws Exception;

	public void saveSignature(ParamsTable params) throws Exception;

	public void getNowTime() throws Exception;

	public void deleSignature(ParamsTable params) throws Exception;

	public void loadSignature(ParamsTable params) throws Exception;

	public void showSignature(ParamsTable params) throws Exception;

	public void getSignatureData(ParamsTable params) throws Exception;

	public void signatureKey(ParamsTable params) throws Exception;

	public void putSignatureData(ParamsTable params) throws Exception;

	public void saveHistory(ParamsTable params) throws Exception;
}
