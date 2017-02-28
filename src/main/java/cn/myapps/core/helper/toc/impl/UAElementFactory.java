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

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

import cn.myapps.core.helper.toc.IToc;
import cn.myapps.core.helper.toc.ITopic;
import cn.myapps.core.helper.toc.IUAElement;

/*
 * Constructs typed model elements from DOM elements or interface
 * implementations.
 */
public class UAElementFactory {

	private static final Class<?>[][] interfaceTable = new Class[][] {
			{ ITopic.class, Topic.class }, { IToc.class, Toc.class }, };

	private static final Map<String, Class<?>> classByElementName;

	static {
		classByElementName = Collections
				.synchronizedMap(new HashMap<String, Class<?>>());
		classByElementName.put(Toc.NAME, Toc.class);
		classByElementName.put(Topic.NAME, Topic.class);
	}

	public static UAElement newElement(Element element) {
		String name = element.getNodeName();
		Class<?> clazz = (Class<?>) classByElementName.get(name);
		if (clazz != null) {
			try {
				Constructor<?> constructor = clazz
						.getConstructor(new Class[] { Element.class });
				return (UAElement) constructor
						.newInstance(new Object[] { element });
			} catch (Exception e) {
				@SuppressWarnings("unused")
				String msg = "Error creating document model element"; //$NON-NLS-1$
			}
		}
		return new UAElement(element);
	}

	public static UAElement newElement(IUAElement src) {
		for (int i = 0; i < interfaceTable.length; ++i) {
			Class<?> interfaze = interfaceTable[i][0];
			Class<?> clazz = interfaceTable[i][1];
			if (interfaze.isAssignableFrom(src.getClass())) {
				try {
					Constructor<?> constructor = clazz
							.getConstructor(new Class[] { interfaze });
					return (UAElement) constructor
							.newInstance(new Object[] { src });
				} catch (Exception e) {
					e.printStackTrace();
					@SuppressWarnings("unused")
					String msg = "Error creating document model element"; //$NON-NLS-1$
				}
			}
		}
		return null;
	}
}
