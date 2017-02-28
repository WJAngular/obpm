/*******************************************************************************
 * Copyright (c) 2007, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package cn.myapps.core.helper.toc.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import cn.myapps.core.helper.toc.IUAElement;

/*
 * Base class for UA model elements.
 */
public class UAElement implements IUAElement {

	private static final String ELEMENT_FILTER = "filter"; //$NON-NLS-1$
	private static final String ATTRIBUTE_FILTER = "filter"; //$NON-NLS-1$
	private static final String ATTRIBUTE_NAME = "name"; //$NON-NLS-1$
	private static final String ATTRIBUTE_VALUE = "value"; //$NON-NLS-1$

	private static DocumentBuilder builder = null;
	private static Document document = null;

	private Element element;
	private UAElement parent;
	protected List<UAElement> children;
	private Filter[] filters;
	private IUAElement src;

	private static class Filter {
		public Filter(String name, String value, boolean isNegated) {
//			this.name = name;
//			this.value = value;
//			this.isNegated = isNegated;
		}

//		String name;
//		String value;
//		boolean isNegated;
	}

	public UAElement(Element element) {
		this.element = element;
	}

	public UAElement(String name) {
		this.element = getDocument().createElement(name);
	}

	public UAElement(String name, IUAElement src) {
		this(name);
		if (src instanceof UAElement) {
			copyFilters(src);
		} else {
			this.src = src;
		}
	}

	private void copyFilters(IUAElement src) {
		UAElement sourceElement = (UAElement) src;
		String filter = sourceElement.getAttribute(ATTRIBUTE_FILTER);
		if (filter != null && filter.length() > 0) {
			this.setAttribute(ATTRIBUTE_FILTER, filter);
		}
		filters = sourceElement.getFilterElements();
		this.src = sourceElement.src;
	}

	private Filter[] getFilterElements() {
		if (filters == null) {
			List<Filter> list = new ArrayList<Filter>();
			if (element.hasChildNodes()) {
				Node node = element.getFirstChild();
				while (node != null) {
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						String elementKind = node.getNodeName();
						if (ELEMENT_FILTER.equals(elementKind)) {
							Element filter = (Element) node;
							String filterName = filter
									.getAttribute(ATTRIBUTE_NAME);
							String value = filter.getAttribute(ATTRIBUTE_VALUE);
							if (filterName.length() > 0 && value.length() > 0) {
								boolean isNegated = false;
								if (value.startsWith("!")) { //$NON-NLS-1$
									isNegated = true;
									value = value.substring(1);
								}
								if (filterName.length() > 0
										&& value.length() > 0) {
									list.add(new Filter(filterName, value,
											isNegated));
								}
							}
						}
					}
					node = node.getNextSibling();
				}
			}
			filters = (Filter[]) list.toArray(new Filter[list.size()]);
		}
		return filters;
	}

	public void appendChild(UAElement uaElementToAppend) {
		importElement(uaElementToAppend);
		element.appendChild(uaElementToAppend.element);
		uaElementToAppend.parent = this;

		if (children != null) {
			children.add(uaElementToAppend);
		}
	}

	public void appendChildren(IUAElement[] children) {
		for (int i = 0; i < children.length; i++) {
			appendChild(children[i] instanceof UAElement ? (UAElement) children[i]
					: UAElementFactory.newElement(children[i]));
		}
	}

	/*
	 * This method is synchronized to fix Bug 232169. When modifying this source
	 * be careful not to introduce any logic which could possibly cause this
	 * thread to block.
	 */
	synchronized public String getAttribute(String name) {
		String value = element.getAttribute(name);
		if (value != null && value.length() > 0) {
			return value;
		}
		return null;
	}

	/*
	 * This method is synchronized to fix Bug 230037. A review of the code
	 * indicated that there was no path which could get blocked and cause
	 * deadlock. When modifying this source be careful not to introduce any
	 * logic which could possibly cause this thread to block.
	 */
	public synchronized IUAElement[] getChildren() {
		if (children == null) {
			if (element.hasChildNodes()) {
				children = new ArrayList<UAElement>(4);
				Node node = element.getFirstChild();
				while (node != null) {
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						UAElement uaElement = UAElementFactory
								.newElement((Element) node);
						if (uaElement != null) {
							uaElement.parent = this;
							children.add(uaElement);
						}
					}
					node = node.getNextSibling();
				}
			} else {
				return new UAElement[0];
			}
		}
		return (UAElement[]) children.toArray(new UAElement[children.size()]);
	}

	public Object getChildren(Class<?> clazz) {
		IUAElement[] children = getChildren();
		if (children.length > 0) {
			List<IUAElement> list = new ArrayList<IUAElement>();
			for (int i = 0; i < children.length; ++i) {
				IUAElement child = children[i];
				if (clazz.isAssignableFrom(child.getClass())) {
					list.add(child);
				}
			}
			return list.toArray((Object[]) Array
					.newInstance(clazz, list.size()));
		}
		return Array.newInstance(clazz, 0);
	}

	public String getElementName() {
		return element.getNodeName();
	}

	private static Document getDocument() {
		synchronized (UAElement.class) {
			if (document == null) {
				if (builder == null) {
					try {
						builder = DocumentBuilderFactory.newInstance()
								.newDocumentBuilder();
					} catch (ParserConfigurationException e) {
						@SuppressWarnings("unused")
						String msg = "Error creating document builder"; //$NON-NLS-1$
					}
				}
				document = builder.newDocument();
			}
		}
		return document;
	}

	public UAElement getParentElement() {
		return parent;
	}

	public void insertBefore(UAElement newChild, UAElement refChild) {
		importElement(newChild);
		element.insertBefore(newChild.element, refChild.element);
		newChild.parent = this;
		getChildren();
		if (children != null) {
			int index = children.indexOf(refChild);
			if (index < 0) {
				// cache is now invalid
				children = null;
			} else {
				children.add(index, newChild);
			}
		}
	}

	public void removeChild(UAElement elementToRemove) {

		element.removeChild(elementToRemove.element);
		elementToRemove.parent = null;

		if (children != null) {
			if (!children.remove(elementToRemove)) {
				// cache is now invalid
				children = null;
			}
		}
	}

	public void setAttribute(String name, String value) {
		element.setAttribute(name, value);
	}

	private void importElement(UAElement uaElementToImport) {
		Element elementToImport = uaElementToImport.element;
		Document ownerDocument = element.getOwnerDocument();
		if (!ownerDocument.equals(elementToImport.getOwnerDocument())) {
			elementToImport = (Element) ownerDocument.importNode(
					elementToImport, true);
			uaElementToImport.children = null;
		} else {
			if (elementToImport.getParentNode() != null) {
				elementToImport = (Element) ownerDocument.importNode(
						elementToImport, true);
				uaElementToImport.children = null;
			} else {
			}
		}
		uaElementToImport.element = elementToImport;
	}

	public Element getElement() {
		return element;
	}

}
