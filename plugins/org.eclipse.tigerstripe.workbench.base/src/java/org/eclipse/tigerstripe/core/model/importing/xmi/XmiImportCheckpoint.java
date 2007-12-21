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
package org.eclipse.tigerstripe.core.model.importing.xmi;

import java.io.FileWriter;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.IImportCheckpointDetails;
import org.eclipse.tigerstripe.core.model.importing.AbstractImportCheckpoint;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElement;

public class XmiImportCheckpoint extends AbstractImportCheckpoint {

	public static final String TYPE = XmiImportCheckpoint.class
			.getCanonicalName();
	public static final String VERSION = "1.0";

	public XmiImportCheckpoint() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void read(Document document) throws TigerstripeException {
		try {
			parseAnnotables(document);
		} catch (TigerstripeException e) {
			throw new TigerstripeException(
					"Error while parsing XmiImportCheckpoint: "
							+ e.getMessage(), e);
		}

		resolveParents();
	}

	@Override
	protected AnnotableElement extractAnnotable(Element annoElm)
			throws TigerstripeException {
		XMIAnnotableElement result = new XMIAnnotableElement();

		extractAnnotableElementCore(result, annoElm);
		extractXmiSpecificCore(result, annoElm);
		extractAttributes(result, annoElm);
		return result;
	}

	protected void extractAttributes(XMIAnnotableElement anno, Element annoElm) {
		for (Element elm : (List<Element>) annoElm.elements(ATTRIBUTE_TAG)) {
			XMIAnnotableElementAttribute attr = new XMIAnnotableElementAttribute();
			attr.setName(elm.attributeValue(NAME_TAG));
			XMIAnnotableDatatype type = new XMIAnnotableDatatype();
			type.setName(elm.attributeValue(FQN_TAG));
			attr.setType(type);
			attr.setDimensions(Integer.parseInt(elm
					.attributeValue(DIMENSION_TAG)));

			anno.addAnnotableElementAttribute(attr);
		}
	}

	private void extractXmiSpecificCore(XMIAnnotableElement anno,
			Element annoElm) {
		// nothing for now
	}

	public String getHumanReadableType() {
		return "UML Import Checkpoint";
	}

	@Override
	public void write(IImportCheckpointDetails details, FileWriter writer)
			throws TigerstripeException {
		setDetails(details);
		Document document = createDocument();

		// addDetails( document );
		addAnnotables(document);

		writeCheckpoint(document, writer);
	}
}
