package cn.myapps.core.datamap.runtime.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.datamap.definition.ejb.DataMapCfgProcess;
import cn.myapps.core.datamap.definition.ejb.DataMapCfgVO;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.document.ejb.DocumentProcessBean;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.element.FlowDiagram;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.element.StartNode;
import cn.myapps.core.workflow.engine.StateMachineHelper;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;
import cn.myapps.util.CreateProcessException;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;

/**
 * @author Happy
 *
 */
public class DataMapTemplate extends ValueObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4311822468727690875L;
	/**
	 * 数据地图主键
	 */
	private String datamapCfgId;
	/**
	 * 线索字段值
	 */
	private String culeField;
	/**
	 * 线索字段2值
	 */
	private String culeField2;
	/**
	 * 图形模板
	 */
	private String template;
	
	/**
	 * 地图内容
	 */
	private String content;
	
	public String getDatamapCfgId() {
		return datamapCfgId;
	}
	public void setDatamapCfgId(String datamapCfgId) {
		this.datamapCfgId = datamapCfgId;
	}
	public String getCuleField() {
		return culeField;
	}
	public void setCuleField(String culeField) {
		this.culeField = culeField;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getContent() {
		
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	public String getCuleField2() {
		return culeField2;
	}
	public void setCuleField2(String culeField2) {
		this.culeField2 = culeField2;
	}
	/**
	 * 生成数据地图内容
	 * @return
	 * 	内容（xml）
	 * @throws Exception
	 */
	public String generateDataMapContent() throws Exception{
		DocumentProcess docProcessBean = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, this.getApplicationid());
		DataMapCfgProcess dataMapCfgProcess = (DataMapCfgProcess) ProcessFactory.createProcess(DataMapCfgProcess.class);
		
		org.dom4j.Document document = DocumentHelper.createDocument();
		org.dom4j.Document template = DocumentHelper.parseText(this.getTemplate());
		
		DataMapCfgVO datamapCfg = (DataMapCfgVO) dataMapCfgProcess.doView(getDatamapCfgId());
		
		Element content = document.addElement("report");
		content.addAttribute("title", template.getRootElement().attributeValue("title"));
		
		List<Element> documentsElement = template.selectNodes("//report/document");
		for(Iterator<Element> it = documentsElement.iterator();it.hasNext();){
			Element documentElement = it.next();
			String id = documentElement.attributeValue("docid");
			Document doc= (Document) docProcessBean.doView(id);
			if(doc != null) {
				Element item = content.addElement("document");
				item.addAttribute("docid", id);
				item.addAttribute("summary", doc.getItemValueAsString(datamapCfg.getSummaryField()));
				item.addAttribute("linecolor", documentElement.attributeValue("linecolor"));
				item.addAttribute("startX", documentElement.attributeValue("startX"));
				item.addAttribute("startY", documentElement.attributeValue("startY"));
				item.addAttribute("endX", documentElement.attributeValue("endX"));
				item.addAttribute("endY", documentElement.attributeValue("endY"));
				item.addAttribute("thickness", documentElement.attributeValue("thickness"));
				item.addAttribute("fontsize", documentElement.attributeValue("fontsize"));
				item.addAttribute("fontcolor", documentElement.attributeValue("fontcolor"));
				item.addAttribute("backgroundcolor", getBackgroundColor(doc, datamapCfg,documentElement.attributeValue("backgroundcolor")));
				
				//流程进度灯----------------------------------------------------------
				if(datamapCfg.isShowFlowProgressbar() && doc.getState() != null){
					FlowStateRT instance = doc.getState();
					BillDefiVO flowVO = instance.getFlowVO();
					FlowDiagram fd = flowVO.toFlowDiagram();
					
					Collection<RelationHIS> hisList = StateMachineHelper.getAllRelationHIS(doc.getId(), flowVO.getId(), doc.getApplicationid());
					RelationHIS lastHis = (RelationHIS) hisList.toArray()[hisList.size()-1];
					Collection<Node> nodes = new ArrayList<Node>();
					Collection<StartNode> startNodes = fd.getStartNodeList();
					for (Iterator<StartNode> iterator = startNodes.iterator(); iterator
							.hasNext();) {
						StartNode startNode = (StartNode) iterator.next();
						getNextNodes(nodes,fd,startNode);
					}
					
					if(instance.isComplete()){//流程走完
						ArrayList<String> nodeids = new ArrayList<String>();//已经添加过的节点
						for (Iterator<RelationHIS> iterator = hisList.iterator(); iterator
								.hasNext();) {
							RelationHIS his = iterator.next();
							if(FlowType.RUNNING2RUNNING_BACK.equals(his.getFlowOperation()) || FlowType.RUNNING2RUNNING_RETRACEMENT.equals(his.getFlowOperation())){
								continue;
							}
							if(nodeids.contains(his.getEndnodeid())) continue;
							Element state = item.addElement("state");
							state.addAttribute("stateLable", his.getEndnodename());
							state.addAttribute("isHighlight", "true");
							nodeids.add(his.getEndnodeid());
						}
					}else{
						boolean falg =true;
						for (Iterator<Node> iterator = nodes.iterator(); iterator.hasNext();) {
							Node node = iterator.next();
							Element state = item.addElement("state");
							state.addAttribute("stateLable", node.name);
							String isHighlight = "false";
							if(falg){
								isHighlight = isHighlight(hisList, node);
							}
							state.addAttribute("isHighlight", isHighlight);
							if(node.id.equals(lastHis.getEndnodeid())) falg = false;
						}
					}
				}
				
				//流程进度灯-----------------------end
				
			}
		}
		
//		System.out.println(document.asXML());
		return document.asXML();
	}
	
	private Collection<Node> getNextNodes(Collection<Node> nodes, FlowDiagram fd,Node currNode){
		Collection<Node> list = fd.getNextNodeList(currNode.id,null,null,null);
		if(list == null) return nodes;
		for (Iterator<Node> iterator = list.iterator(); iterator.hasNext();) {
			Node node = (Node) iterator.next();
			nodes.add(node);
			getNextNodes(nodes,fd,node);
		}
		return nodes;
	}
	
	private String isHighlight(Collection<RelationHIS> hisList,Node currNode){
		
		for (Iterator<RelationHIS> iterator = hisList.iterator(); iterator.hasNext();) {
			RelationHIS his = iterator.next();
			if(FlowType.RUNNING2RUNNING_BACK.equals(his.getFlowOperation()) || FlowType.RUNNING2RUNNING_RETRACEMENT.equals(his.getFlowOperation())){
				continue;
			}
			if(his.getEndnodeid().equals(currNode.id)) return "true";
		}
		return "false";
	}
	
	private String getBackgroundColor(Document doc,DataMapCfgVO datamapCfg,String def){
		if(StringUtil.isBlank(datamapCfg.getStateLableColorMapping())) return def;
		Collection<Object> qs = JsonUtil.toCollection(datamapCfg.getStateLableColorMapping(), JSONObject.class);
		Iterator<Object> iterator = qs.iterator();
		while (iterator.hasNext()) {
			JSONObject object = JSONObject.fromObject(iterator.next());
			String stateLable = (String) object.get("stateLabel");
			if(doc.getStateLabel().indexOf(stateLable)>-1){
				return (String) object.get("color");
			}
		}
		
		return def;
	}
}
