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
package org.eclipse.tigerstripe.core.model.importing.db;

import java.io.FileWriter;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.IImportCheckpointDetails;
import org.eclipse.tigerstripe.core.model.importing.AbstractImportCheckpoint;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElement;
import org.eclipse.tigerstripe.core.model.importing.db.annotables.DBAnnotableDatatype;
import org.eclipse.tigerstripe.core.model.importing.db.annotables.DBAnnotableElement;
import org.eclipse.tigerstripe.core.model.importing.db.annotables.DBAnnotableElementAttribute;

public class DBImportCheckpoint extends AbstractImportCheckpoint {

	public static final String TYPE = DBImportCheckpoint.class
			.getCanonicalName();
	public static final String VERSION = "1.0";

	public DBImportCheckpoint() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void read(Document document) throws TigerstripeException {
		try {
			parseAnnotables(document);
		} catch (TigerstripeException e) {
			throw new TigerstripeException(
					"Error while parsing DBImportCheckpoint: " + e.getMessage(),
					e);
		}

		resolveParents();
	}

	@Override
	protected AnnotableElement extractAnnotable(Element annoElm)
			throws TigerstripeException {
		DBAnnotableElement result = new DBAnnotableElement(null);

		extractAnnotableElementCore(result, annoElm);
		extractDBSpecificCore(result, annoElm);
		extractAttributes(result, annoElm);
		return result;
	}

	protected void extractAttributes(DBAnnotableElement anno, Element annoElm) {
		for (Element elm : (List<Element>) annoElm.elements(ATTRIBUTE_TAG)) {
			DBAnnotableElementAttribute attr = new DBAnnotableElementAttribute();
			attr.setName(elm.attributeValue(NAME_TAG));
			DBAnnotableDatatype type = new DBAnnotableDatatype();
			type.setName(elm.attributeValue(FQN_TAG));
			attr.setType(type);
			attr.setDimensions(Integer.parseInt(elm
					.attributeValue(DIMENSION_TAG)));

			anno.addAnnotableElementAttribute(attr);
		}
	}

	public String getHumanReadableType() {
		return "Database Import Checkpoint";
	}

	private void extractDBSpecificCore(DBAnnotableElement anno, Element annoElm) {
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
