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

import java.util.Properties;

import org.eclipse.tigerstripe.metamodel.ERefByEnum;
import org.eclipse.tigerstripe.metamodel.IField;
import org.eclipse.tigerstripe.metamodel.IType;
import org.eclipse.tigerstripe.metamodel.MetamodelFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifactTag;
import org.eclipse.tigerstripe.workbench.internal.core.model.Tag;

import com.thoughtworks.qdox.model.JavaField;

public class FieldReader extends ModelComponentReader {

	final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.FIELD;

	private JavaField field = null;

	public FieldReader(JavaField field) {
		this.field = field;
	}

	public IField readField() throws TigerstripeException {

		// Build up the list of tags for this field
		extractTags(field.getTags());

		Tag mTag = getFirstTagByName(MARKING_TAG);

		if (mTag != null) {
			IField result = MetamodelFactory.eINSTANCE.createIField();
			result.setName(field.getName());
			result.setComment(field.getComment());

			// the visibility
			result
					.setVisibility(ReaderUtils.toVisibility(field
							.getModifiers()));

			// All of the modifiers
			Properties prop = mTag.getProperties();
			result.setRefBy(ReaderUtils.toRefByEnum(prop.getProperty("ref-by",
					"value")));
			result.setOptional(Boolean.parseBoolean(prop.getProperty(
					"isOptional", "false")));
			result.setReadOnly(Boolean.parseBoolean(prop.getProperty(
					"isReadOnly", "false")));
			result.setOrdered(Boolean.parseBoolean(prop.getProperty(
					"isOrdered", "false")));
			result.setUnique(Boolean.parseBoolean(prop.getProperty("isUnique",
					"false")));

			String defaultValue = prop.getProperty("defaultValue", null);
			if (defaultValue != null) {
				result.setDefaultValue(xmlEncode.decode(defaultValue));
			}

			// The target type
			String typeStr = field.getType().getValue();
			String typeMultiplicity = prop
					.getProperty("typeMultiplicity", null);
			IType type = MetamodelFactory.eINSTANCE.createIType();
			type.setFullyQualifiedName(typeStr);
			type.setMultiplicity(ReaderUtils.toMultiplicity(typeMultiplicity));
			result.setType(type);

			readStereotypes();

			return result;
		}
		// Note that fields are also store as POJO attributes, so if this
		// JavaField
		// is not a field it must be a Literal or we throw an exception
		if (getFirstTagByName(LiteralReader.MARKING_TAG) == null)
			throw new TigerstripeException("Illegal JavaField: " + this.field);

		// we're cool it's a literal.
		return null;
	}
}
