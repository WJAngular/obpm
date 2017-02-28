package cn.myapps.core.baidumap;

import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.util.ProcessFactory;

public class BaiduMap {

	/**
	 * 获得地图的数据
	 * @param fieldId
	 * @return
	 */
	public static String getMapData(String fieldId,String applicationid){
		try {
			DocumentProcess documentPross = (DocumentProcess) (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,applicationid);
			Document doc =(Document)documentPross.doView(fieldId.split("_")[0]);
			if(doc!=null){
				Item item = doc.findItem(fieldId.split("_")[1]);
				if(item!=null && item.getValue()!=null){
					return item.getValue().toString();
				}else{
					return "";
				}
			}else{
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
}
