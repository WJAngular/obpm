/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package cn.myapps.core.helper.toc.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class manages reuse of DOM parsers. It will keep reusing the same
 * DocumentBuilder unless it is being used elsewhere in which case a new one is
 * allocated.
 */

public class DocumentReader {

	private static class ManagedBuilder {
		public DocumentBuilder builder;
		public boolean inUse;
	}

	private ManagedBuilder cachedBuilder;

	public UAElement read(InputStream in) throws IOException, SAXException, ParserConfigurationException {
		return read(in, null);
	}

	public UAElement read(InputStream in, String charset) throws IOException, SAXException,
			ParserConfigurationException {
		ManagedBuilder managedBuilder = getManagedBuilder();
		InputSource input = null;
		if (charset != null) {
			input = new InputSource(new InputStreamReader(in, charset));
		} else {
			input = new InputSource(in);
		}
		Document document = managedBuilder.builder.parse(input);
		managedBuilder.inUse = false;
		prepareDocument(document);
		return UAElementFactory.newElement(document.getDocumentElement());
	}

	/**
	 * Allows subclasses to process the DOM before creating a UA element
	 */
	protected void prepareDocument(Document document) {
	}

	private synchronized ManagedBuilder getManagedBuilder() throws FactoryConfigurationError,
			ParserConfigurationException {
		if (cachedBuilder == null || cachedBuilder.inUse) {
			cachedBuilder = createManagedBuilder();
		}
		cachedBuilder.inUse = true;
		return cachedBuilder;
	}

	private ManagedBuilder createManagedBuilder() throws FactoryConfigurationError, ParserConfigurationException {
		ManagedBuilder managedBuilder = new ManagedBuilder();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		managedBuilder.builder = factory.newDocumentBuilder();
		return managedBuilder;
	}
}
