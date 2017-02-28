package cn.myapps.core.workflow.element.mapping;

import java.util.Set;
import java.util.TreeSet;

import cn.myapps.util.xml.XmlUtil;

public class FieldMappingUtil {
	
	
	public String parseObject(TreeSet<FieldMappingItem> fieldMappings) {
		return XmlUtil.toXml(fieldMappings);
	}
	
	
	public Set<?> parseXML(String fieldMappingXML) {
		return (Set<?>) XmlUtil.toOjbect(fieldMappingXML);
	}
}
