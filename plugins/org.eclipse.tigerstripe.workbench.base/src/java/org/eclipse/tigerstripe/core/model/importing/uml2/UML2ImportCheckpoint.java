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
package org.eclipse.tigerstripe.core.model.importing.uml2;

import java.io.FileWriter;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.IImportCheckpointDetails;
import org.eclipse.tigerstripe.core.model.importing.AbstractImportCheckpoint;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElement;
import org.eclipse.tigerstripe.core.model.importing.uml2.annotables.UML2AnnotableDatatype;
import org.eclipse.tigerstripe.core.model.importing.uml2.annotables.UML2AnnotableElement;
import org.eclipse.tigerstripe.core.model.importing.uml2.annotables.UML2AnnotableElementAttribute;

public class UML2ImportCheckpoint extends AbstractImportCheckpoint {

	public static final String TYPE = UML2ImportCheckpoint.class
			.getCanonicalName();

	public static final String VERSION = "1.0";

	public UML2ImportCheckpoint() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void read(Document document) throws TigerstripeException {
		try {
			parseAnnotables(document);
		} catch (TigerstripeException e) {
			throw new TigerstripeException(
					"Error while parsing UML2ImportCheckpoint: "
							+ e.getMessage(), e);
		}

		resolveParents();
	}

	@Override
	protected AnnotableElement extractAnnotable(Element annoElm)
			throws TigerstripeException {
		UML2AnnotableElement result = new UML2AnnotableElement(null);

		extractAnnotableElementCore(result, annoElm);
		extractUML2SpecificCore(result, annoElm);
		extractAttributes(result, annoElm);
		return result;
	}

	protected void extractAttributes(UML2AnnotableElement anno, Element annoElm) {
		for (Element elm : (List<Element>) annoElm.elements(ATTRIBUTE_TAG)) {
			UML2AnnotableElementAttribute attr = new UML2AnnotableElementAttribute();
			attr.setName(elm.attributeValue(NAME_TAG));
			UML2AnnotableDatatype type = new UML2AnnotableDatatype();
			type.setName(elm.attributeValue(FQN_TAG));
			attr.setType(type);
			attr.setDimensions(Integer.parseInt(elm
					.attributeValue(DIMENSION_TAG)));

			anno.addAnnotableElementAttribute(attr);
		}
	}

	public String getHumanReadableType() {
		return "UML2 Import Checkpoint";
	}

	private void extractUML2SpecificCore(UML2AnnotableElement anno,
			Element annoElm) {
		// nothing for now
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
