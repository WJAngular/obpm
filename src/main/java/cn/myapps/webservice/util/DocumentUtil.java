package cn.myapps.webservice.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.webservice.model.SimpleDocument;

/**
 * DocumentService工具类
 * @author ivan
 *
 */
public class DocumentUtil {
	/**
	 * 转换DataPackage中的Document为SimpleDocument
	 * 
	 * @param dataPackage
	 *            数据集
	 */
	public static Collection<SimpleDocument> convertToSimpleDatas(DataPackage<Document> dataPackage) {
		Collection<SimpleDocument> datas = new ArrayList<SimpleDocument>();

		for (Iterator<Document> iterator = dataPackage.getDatas().iterator(); iterator.hasNext();) {
			Document document = (Document) iterator.next();
			SimpleDocument sDocument = new SimpleDocument();
			sDocument.setId(document.getId());
			sDocument.setFlowStateId(document.getStateid());
			sDocument.setAuditorNames(document.getAuditorNames());
			sDocument.setStateLabel(document.getStateLabel());
			for (Iterator<?> iterator2 = document.getItems().iterator(); iterator2.hasNext();) {
				Item item = (Item) iterator2.next();

				sDocument.getItems().put(item.getName(), item.getValue());
			}
			datas.add(sDocument);
		}

		return datas;

		// simpledatapackage.setDatas(datas);
	}
}
