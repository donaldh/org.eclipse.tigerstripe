/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo.reader;

import org.eclipse.tigerstripe.metamodel.IField;
import org.eclipse.tigerstripe.metamodel.MetamodelFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifactTag;
import org.eclipse.tigerstripe.workbench.internal.core.model.Tag;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaField;

public class FieldReader extends ModelComponentReader {

	private final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.FIELD;

	private JavaField field = null;

	public FieldReader(JavaField field) {
		this.field = field;
	}

	public IField readField() throws TigerstripeException {
		// Start with class-level tags
		DocletTag[] dtags = field.getTags();
		for (int i = 0; i < dtags.length; i++) {
			Tag tag = new Tag(dtags[i]);
			tags.add(tag);
		}

		if (getTagsByName(MARKING_TAG).size() != 0) {
			IField result = MetamodelFactory.eINSTANCE.createIField();
			result.setName(field.getName());
			result.setComment(field.getComment());

			// FIXME: to the rest!

			return result;
		}

		throw new TigerstripeException("Not a TS field.");
	}
}
