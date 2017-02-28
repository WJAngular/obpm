/**
 * SimpleDocument.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.myapps.webservice.model;

import java.util.HashMap;

public class SimpleDocument implements java.io.Serializable {

	private static final long serialVersionUID = -5684538455736958085L;

	private java.lang.String id;

	private java.util.Map<Object, Object> items = new HashMap<Object, Object>();

	private java.lang.String stateLabel;
	
	private java.lang.String flowStateId;
	
	private java.lang.String auditorNames;

	public SimpleDocument() {
	}

	/**
	 * 获取标识
	 * 
	 * @return id 标识
	 */
	public java.lang.String getId() {
		return id;
	}

	/**
	 * 设置文档
	 * 
	 * @param id
	 *            文档
	 */
	public void setId(java.lang.String id) {
		this.id = id;
	}

	/**
	 * 获取元素集合
	 * 
	 * @return 元素集合
	 */
	public java.util.Map<Object, Object> getItems() {
		return items;
	}

	/**
	 * 设置元素集合
	 * 
	 * @param items
	 *            元素集合
	 */
	public void setItems(java.util.Map<Object, Object> items) {
		this.items = items;
	}

	/**
	 * 获取文档状态
	 * 
	 * @return stateLabel 文档状态
	 */
	public java.lang.String getStateLabel() {
		return stateLabel;
	}

	/**
	 * 设置文档状态
	 * 
	 * @param stateLabel
	 *            　文档状态
	 */
	public void setStateLabel(java.lang.String stateLabel) {
		this.stateLabel = stateLabel;
	}

	public java.lang.String getFlowStateId() {
		return flowStateId;
	}

	public void setFlowStateId(java.lang.String flowStateId) {
		this.flowStateId = flowStateId;
	}

	public java.lang.String getAuditorNames() {
		return auditorNames;
	}

	public void setAuditorNames(java.lang.String auditorNames) {
		this.auditorNames = auditorNames;
	}
	
}
