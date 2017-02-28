/*******************************************************************************
 * Copyright (c) 2006, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package cn.myapps.core.helper.toc.impl;

import org.w3c.dom.Element;

import cn.myapps.core.helper.toc.ITopic;

public class Topic extends UAElement implements ITopic {

	public static final String NAME = "topic"; //$NON-NLS-1$
	public static final String ATTRIBUTE_HREF = "href"; //$NON-NLS-1$
	public static final String ATTRIBUTE_LABEL = "label"; //$NON-NLS-1$
	public static final String ATTRIBUTE_ICON = "icon"; //$NON-NLS-1$
	public static final String ATTRIBUTE_SORT = "sort"; //$NON-NLS-1$
	public static final String ATTRIBUTE_ID = "id"; //$NON-NLS-1$

	public Topic() {
		super(NAME);
	}

	public Topic(ITopic src) {
		super(NAME, src);
		setHref(src.getHref());
		setLabel(src.getLabel());
		setId(src.getId());
		appendChildren(src.getChildren());
	}
	
	public void setId(String id) {
		setAttribute(ATTRIBUTE_ID, id);
	}
	
	public String getId() {
		return getAttribute(ATTRIBUTE_ID);
	}
	
	public String getIcon() {
		return getAttribute(ATTRIBUTE_ICON);
	}

	public boolean isSorted() {
		return "true".equalsIgnoreCase(getAttribute(ATTRIBUTE_SORT)); //$NON-NLS-1$
	}

	public Topic(Element src) {
		super(src);
	}

	public String getHref() {
		return getAttribute(ATTRIBUTE_HREF);
	}

	public String getLabel() {
		return getAttribute(ATTRIBUTE_LABEL);
	}

	public ITopic[] getSubtopics() {
		return (ITopic[]) getChildren(ITopic.class);
	}

	public void setHref(String href) {
		setAttribute(ATTRIBUTE_HREF, href);
	}

	public void setLabel(String label) {
		setAttribute(ATTRIBUTE_LABEL, label);
	}
}
