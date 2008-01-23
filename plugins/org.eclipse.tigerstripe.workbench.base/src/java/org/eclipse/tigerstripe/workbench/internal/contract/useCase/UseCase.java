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
package org.eclipse.tigerstripe.workbench.internal.contract.useCase;

import java.net.URI;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.useCase.IUseCase;
import org.eclipse.tigerstripe.workbench.internal.core.versioning.VersionAwareElement;

public class UseCase extends VersionAwareElement implements IUseCase {

	private final static String ELEMENT_TAG = "useCase";

	private String body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n\t<body>\n\t</body>\n</html>\n";

	private String xslRelPath = null;

	private boolean bodyIsXML = true;

	private String bodySchemaOrDTD = "";

	public boolean bodyIsXML() {
		return this.bodyIsXML;
	}

	public void setBodyIsXML(boolean bodyIsXML) {
		this.bodyIsXML = bodyIsXML;
	}

	public String getBodySchemaOrDTD() {
		return this.bodySchemaOrDTD;
	}

	/**
	 * Sets the schema or DTD file to use as guide for the body editor in case
	 * the body is expected to contain XML.
	 * 
	 * @param bodySchemaOrDTD -
	 *            the project relative path to the file.
	 */
	public void setBodySchemaOrDTD(String bodySchemaOrDTD) {
		this.bodySchemaOrDTD = bodySchemaOrDTD;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBody() {
		return body;
	}

	public UseCase(URI uri) {
		super(uri);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getElementTag() {
		return ELEMENT_TAG;
	}

	public String getXslRelPath() {
		return xslRelPath;
	}

	public void setXslRelPath(String xslRelPath) {
		this.xslRelPath = xslRelPath;
	}

	@Override
	protected void parseBody(Element rootElement) throws TigerstripeException {
		Element bodyElement = rootElement.element("body");
		Attribute bodyIsXMLAttr = bodyElement.attribute("bodyIsXML");
		Attribute bodySchemaOrDTDAttr = bodyElement
				.attribute("bodySchemaOrDTD");
		Attribute xslRelPathAttr = bodyElement.attribute("xslRelPath");
		if (xslRelPathAttr != null) {
			xslRelPath = xslRelPathAttr.getValue();
		}

		if (bodyIsXMLAttr != null) {
			bodyIsXML = Boolean.parseBoolean(bodyIsXMLAttr.getValue());
		}

		if (bodySchemaOrDTDAttr != null) {
			bodySchemaOrDTD = bodySchemaOrDTDAttr.getValue();
		}

		if (bodyElement != null)
			setBody(bodyElement.getText());
	}

	@Override
	public void appendBody(Element rootElement) throws TigerstripeException {
		Element bodyElement = rootElement.addElement("body");
		if (xslRelPath != null && xslRelPath.length() != 0) {
			bodyElement.addAttribute("xslRelPath", xslRelPath);
		}
		bodyElement.addAttribute("bodyIsXML", String.valueOf(bodyIsXML));
		if (bodySchemaOrDTD != null && bodySchemaOrDTD.length() != 0) {
			bodyElement.addAttribute("bodySchemaOrDTD", bodySchemaOrDTD);
		}
		bodyElement.addText(getBody());
	}

}
