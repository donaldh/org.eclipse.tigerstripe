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
package org.eclipse.tigerstripe.repository.metamodel.pojo.writer;

import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.metamodel.impl.IAssociationArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IAssociationClassArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IDatatypeArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IDependencyArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IEnumArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IEventArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IExceptionArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IManagedEntityArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IPrimitiveTypeImpl;
import org.eclipse.tigerstripe.metamodel.impl.IQueryArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IUpdateProcedureArtifactImpl;
import org.eclipse.tigerstripe.repository.manager.ModelCoreException;

public class ArtifactWriterFactory {

	public final static ArtifactWriterFactory INSTANCE = new ArtifactWriterFactory();

	@SuppressWarnings("unchecked")
	private Class[] artifactClasses = { IManagedEntityArtifactImpl.class,
			IDatatypeArtifactImpl.class, IExceptionArtifactImpl.class,
			IEnumArtifactImpl.class, IAssociationArtifactImpl.class,
			IAssociationClassArtifactImpl.class, IEventArtifactImpl.class,
			IQueryArtifactImpl.class, ISessionArtifactImpl.class,
			IPrimitiveTypeImpl.class, IUpdateProcedureArtifactImpl.class,
			IDependencyArtifactImpl.class };

	private AbstractArtifactWriter[] writers = {
			new ManagedEntityArtifactWriter(), new DatatypeArtifactWriter(),
			new ExceptionArtifactWriter(), new EnumArtifactWriter(),
			new AssociationArtifactWriter(),
			new AssociationClassArtifactWriter(), new EventArtifactWriter(),
			new QueryArtifactWriter(), new SessionArtifactWriter(),
			new PrimitiveTypeArtifactWriter(),
			new UpdateProcedureArtifactWriter(), new DependencyArtifactWriter() };

	private ArtifactWriterFactory() {
		// EMPTY
	}

	public <T extends IAbstractArtifact> AbstractArtifactWriter getWriter(
			Class<T> artifactClass) throws ModelCoreException {
		for (int index = 0; index < artifactClasses.length; index++) {
			if (artifactClasses[index] == artifactClass) {
				return writers[index];
			}
		}

		throw new ModelCoreException("No writer for "
				+ artifactClass.getCanonicalName());
	}
}
