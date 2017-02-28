package cn.myapps.core.dynaform.signature.dao;

import java.util.List;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.dynaform.signature.ejb.Htmlsignature;

public interface HtmlsignatureDAO extends IDesignTimeDAO<Htmlsignature>{

	public List<Htmlsignature> queryAll() throws Exception;
	public List<Htmlsignature> queryById(String SignatureID,String DocumentID, String FormID) throws Exception;
	public List<Htmlsignature> queryByDocumentID(String DocumentID, String FormID) throws Exception;
	public void updateHtmlsignature(Htmlsignature htmlsignature) throws Exception;
	public void createHtmlsignature(String mDocumentID,String mSignatureID,String mSignature , String FormID) throws Exception;
}
