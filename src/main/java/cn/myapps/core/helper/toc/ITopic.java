/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package cn.myapps.core.helper.toc;

/**
 * An <code>ITopic</code> is one topic in the table of contents, which may
 * contain subtopics.
 * 
 * @since 2.0
 */
public interface ITopic extends IUAElement, IHelpResource {
	/**
	 * This is element name used for topic in XML files.
	 */
	public final static String TOPIC = "topic"; //$NON-NLS-1$

	/**
	 * Obtains the topics contained in this node.
	 * 
	 * @return Array of ITopic
	 */
	public ITopic[] getSubtopics();

	public void setId(String id);

	public String getId();
}
