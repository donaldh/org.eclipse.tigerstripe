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
package org.eclipse.tigerstripe.repository.metamodel.pojo.reader;

import org.eclipse.tigerstripe.metamodel.IMethod;
import org.eclipse.tigerstripe.metamodel.MetamodelFactory;
import org.eclipse.tigerstripe.repository.manager.ModelCoreException;
import org.eclipse.tigerstripe.repository.metamodel.pojo.utils.AbstractArtifactTag;
import org.eclipse.tigerstripe.repository.metamodel.pojo.utils.Tag;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaMethod;

public class MethodReader extends ModelComponentReader {

	private final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.METHOD;

	private JavaMethod method = null;

	public MethodReader(JavaMethod method) {
		this.method = method;
	}

	public IMethod readMethod() throws ModelCoreException {
		// Start with class-level tags
		DocletTag[] dtags = method.getTags();
		for (int i = 0; i < dtags.length; i++) {
			Tag tag = new Tag(dtags[i]);
			tags.add(tag);
		}

		if (getTagsByName(MARKING_TAG).size() != 0) {
			IMethod result = MetamodelFactory.eINSTANCE.createIMethod();
			result.setName(method.getName());
			result.setComment(method.getComment());

			// FIXME: to the rest!

			return result;
		}

		throw new ModelCoreException("Not a TS method.");
	}
}
