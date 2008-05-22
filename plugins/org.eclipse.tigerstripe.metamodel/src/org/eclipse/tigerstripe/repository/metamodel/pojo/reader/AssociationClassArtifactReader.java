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

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.metamodel.IAssociationClassArtifact;
import org.eclipse.tigerstripe.metamodel.MetamodelFactory;
import org.eclipse.tigerstripe.metamodel.impl.IAssociationClassArtifactImpl;
import org.eclipse.tigerstripe.repository.manager.KeyService;
import org.eclipse.tigerstripe.repository.manager.ModelCoreException;
import org.eclipse.tigerstripe.repository.metamodel.pojo.utils.AbstractArtifactTag;
import org.eclipse.tigerstripe.repository.metamodel.pojo.utils.Util;

import com.thoughtworks.qdox.model.JavaClass;

public class AssociationClassArtifactReader extends ArtifactReader {

	public final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.ASSOCIATIONCLASS;

	protected String getMarkingTag() {
		return MARKING_TAG;
	}

	public IAbstractArtifact readArtifact() throws ModelCoreException {
		IAssociationClassArtifact result = MetamodelFactory.eINSTANCE
				.createIAssociationClassArtifact();

		internalRead(result, class_);
		return result;
	}

	protected void internalRead(IAbstractArtifact artifact, JavaClass class_)
			throws ModelCoreException {
		super.internalRead(artifact);
	}

	@Override
	protected IAbstractArtifact makeProxyArtifact(String parentClass) {
		try {
			IAssociationClassArtifact result = MetamodelFactory.eINSTANCE
					.createIAssociationClassArtifact();
			result.setName(Util.nameOf(parentClass));
			if (Util.packageOf(parentClass).length() != 0)
				result.setPackage(Util.packageOf(parentClass));

			URI uri = URI.createGenericURI(SCHEME, result.eClass()
					.getInstanceClassName(), (String) KeyService.INSTANCE
					.getKey(result));

			((IAssociationClassArtifactImpl) result).eSetProxyURI(uri);
			return result;
		} catch (ModelCoreException e) {
			return null;
		}
	}
}
