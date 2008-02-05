/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.versioning;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URI;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.project.IProjectSession;
import org.eclipse.tigerstripe.workbench.internal.api.versioning.IBaseElement;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;

public abstract class BaseElement implements IBaseElement {

	private String description = DEFAULT_DESCRIPTION;

	private String name = DEFAULT_NAME;

	private URI uri;

	public BaseElement(URI uri) {

		if (uri == null)
			return;

		setURI(uri);
	}

	public void parse(URI uri) throws TigerstripeException {
		if (uri == null)
			throw new TigerstripeException("Invalid URI:" + uri.toASCIIString());

		// try and load the content if the URI is a valid baseElement
		File targetFile = new File(uri);
		if (targetFile.exists() && targetFile.canRead()) {
			FileReader reader = null;
			try {
				reader = new FileReader(targetFile);
				parse(reader);
			} catch (IOException e) {
				throw new TigerstripeException("Couldn't parse "
						+ e.getMessage(), e);
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					// ignore here
				}
			}
		}
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public URI getURI() {
		return this.uri;
	}

	public void setURI(URI uri) {
		this.uri = uri;
	}

	public void doSave() throws TigerstripeException {
		if (uri == null)
			throw new TigerstripeException("No URI");
		doSaveAs(this.uri);
	}

	public void doSaveAs(URI uri) throws TigerstripeException {
		File targetFile = new File(uri);

		if (targetFile.exists() && !targetFile.canWrite())
			throw new TigerstripeException("File " + uri.toASCIIString()
					+ " is read-only.");

		FileWriter writer = null;
		try {
			writer = new FileWriter(targetFile);
			writer.write(asText());
		} catch (IOException e) {
			throw new TigerstripeException("Couldn't perform save operation:"
					+ e.getMessage(), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// ignore here
				}
			}
		}
	}

	/**
	 * Returns the XML tag that will be used as the root XML element
	 * 
	 * This needs to be implemented by extending classes.
	 * 
	 * @return
	 */
	protected abstract String getElementTag();

	/**
	 * Creates the document for everybody to use
	 * 
	 */
	private Document initializeDocument() {
		Document document = DocumentFactory.getInstance().createDocument();
		Element rootElem = DocumentFactory.getInstance().createElement(
				getElementTag());
		document.setRootElement(rootElem);
		return document;
	}

	public void parse(Reader reader) throws TigerstripeException {

		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(reader);

			parseHeader(document.getRootElement());
			parseBody(document.getRootElement());
		} catch (DocumentException e) {
			throw new TigerstripeException("Couldn't parse profile: "
					+ e.getMessage(), e);
		}
	}

	protected abstract void parseBody(Element rootElement)
			throws TigerstripeException;

	protected abstract void appendBody(Element rootElement)
			throws TigerstripeException;

	protected void parseHeader(Element rootElement) throws TigerstripeException {
		if (!rootElement.getName().equals(getElementTag()))
			throw new TigerstripeException("Not a valid " + getElementTag());

		String name = rootElement.attributeValue("name");
		if (name == null) {
			name = DEFAULT_NAME;
		}
		setName(name);

		String description = rootElement.element("description").getText();
		if (description == null) {
			description = DEFAULT_DESCRIPTION;
		}
		setDescription(description);
	}

	/**
	 * Returns the XML representation of this
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public final String asText() throws TigerstripeException {
		Document document = initializeDocument();
		Element rootElement = document.getRootElement();

		appendHeader(rootElement);
		appendBody(rootElement);

		OutputFormat format = OutputFormat.createPrettyPrint();
		StringWriter sWriter = new StringWriter();
		XMLWriter xWriter = new XMLWriter(sWriter, format);
		try {
			xWriter.write(document);
			return sWriter.toString();
		} catch (IOException e) {
			TigerstripeRuntime.logErrorMessage("Error while formatting XML: "
					+ e.getMessage(), e);
			return document.asXML();
		}
	}

	protected void appendHeader(Element rootElement) {
		rootElement.addAttribute("name", getName());
		rootElement.addElement("description").setText(getDescription());
	}

	public IAbstractTigerstripeProject getContainingProject()
			throws TigerstripeException {
		File file = new File(getURI());
		if (file != null && file.exists() && file.isFile()) {
			File containingFolder = file.getParentFile();
			if (containingFolder != null && containingFolder.isDirectory()) {

				boolean containsDescriptor = false;
				while (!containsDescriptor) {
					String[] files = containingFolder.list();
					for (String f : files) {
						if (ITigerstripeConstants.PROJECT_DESCRIPTOR.equals(f)) {
							containsDescriptor = true;
							break;
						}
					}

					if (containsDescriptor)
						break;
					else if (containingFolder.getParentFile() != null
							&& containingFolder.getParentFile().isDirectory()) {
						containingFolder = containingFolder.getParentFile();
					} else
						return null;
				}

					IProjectSession session = InternalTigerstripeCore.getDefaultProjectSession();
					IAbstractTigerstripeProject tsProject = session
							.makeTigerstripeProject(containingFolder.toURI(),
									null);
					return tsProject;
				
			}
		}
		return null;
	}
}
