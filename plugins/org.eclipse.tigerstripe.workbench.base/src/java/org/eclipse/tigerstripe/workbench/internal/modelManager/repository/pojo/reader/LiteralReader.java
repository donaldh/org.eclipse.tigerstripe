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

import org.eclipse.tigerstripe.metamodel.ILiteral;
import org.eclipse.tigerstripe.metamodel.MetamodelFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifactTag;
import org.eclipse.tigerstripe.workbench.internal.core.model.Tag;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaField;

public class LiteralReader extends ModelComponentReader {

	final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.LITERAL;

	private JavaField field = null;

	public LiteralReader(JavaField field) {
		this.field = field;
	}

	public ILiteral readLiteral() throws TigerstripeException {
		// Start with class-level tags
		DocletTag[] dtags = field.getTags();
		for (int i = 0; i < dtags.length; i++) {
			Tag tag = new Tag(dtags[i]);
			tags.add(tag);
		}

		if (getTagsByName(MARKING_TAG).size() != 0) {
			ILiteral result = MetamodelFactory.eINSTANCE.createILiteral();
			result.setName(field.getName());
			result.setComment(field.getComment());

			// FIXME: to the rest!

			return result;
		}

		// Note that fields are also store as POJO attributes, so if this
		// JavaField
		// is not a literal it must be a Field or we throw an exception
		if (getFirstTagByName(FieldReader.MARKING_TAG) == null)
			throw new TigerstripeException("Illegal JavaField: " + this.field);

		// we're cool it's a field.
		return null;
	}
}
