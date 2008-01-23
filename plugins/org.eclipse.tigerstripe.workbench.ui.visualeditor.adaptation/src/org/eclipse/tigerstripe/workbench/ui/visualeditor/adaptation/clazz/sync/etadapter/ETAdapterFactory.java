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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.etadapter;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.ETAdapter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NotificationArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.ClassDiagramSynchronizer;

public class ETAdapterFactory {

	public static ETAdapter makeETAdapterFor(EObject object,
			IModelUpdater modelUpdater, ClassDiagramSynchronizer synchronizer)
			throws TigerstripeException {

		if (object instanceof Map)
			return new MapETAdapter((Map) object, modelUpdater, synchronizer);
		else if (object instanceof ManagedEntityArtifact)
			return new ManagedEntityETAdapter((ManagedEntityArtifact) object,
					modelUpdater, synchronizer);
		else if (object instanceof AssociationClass)
			return new AssociationClassETAdapter((AssociationClass) object,
					modelUpdater, synchronizer);
		else if (object instanceof Association)
			return new AssociationETAdapter((Association) object, modelUpdater,
					synchronizer);
		else if (object instanceof DatatypeArtifact)
			return new DatatypeETAdapter((DatatypeArtifact) object,
					modelUpdater, synchronizer);
		else if (object instanceof NotificationArtifact)
			return new NotificationETAdapter((NotificationArtifact) object,
					modelUpdater, synchronizer);
		else if (object instanceof Enumeration)
			return new EnumerationETAdapter((Enumeration) object, modelUpdater,
					synchronizer);
		else if (object instanceof NamedQueryArtifact)
			return new QueryETAdapter((NamedQueryArtifact) object,
					modelUpdater, synchronizer);
		else if (object instanceof UpdateProcedureArtifact)
			return new UpdateProcedureETAdapter(
					(UpdateProcedureArtifact) object, modelUpdater,
					synchronizer);
		else if (object instanceof ExceptionArtifact)
			return new ExceptionETAdapter((ExceptionArtifact) object,
					modelUpdater, synchronizer);
		else if (object instanceof SessionFacadeArtifact)
			return new SessionFacadeETAdapter((SessionFacadeArtifact) object,
					modelUpdater, synchronizer);
		else if (object instanceof Attribute)
			return new AttributeETAdapter((Attribute) object, modelUpdater,
					synchronizer);
		else if (object instanceof Literal)
			return new LiteralETAdapter((Literal) object, modelUpdater,
					synchronizer);
		else if (object instanceof Reference)
			return new ReferenceETAdapter((Reference) object, modelUpdater,
					synchronizer);
		else if (object instanceof Method)
			return new MethodETAdapter((Method) object, modelUpdater,
					synchronizer);
		else if (object instanceof Dependency)
			return new DependencyETAdapter((Dependency) object, modelUpdater,
					synchronizer);
		else if (object instanceof AssociationClassClass)
			return new AssociationClassClassETAdapter(
					(AssociationClassClass) object, modelUpdater, synchronizer);
		else if (object instanceof DiagramProperty)
			return new DiagramPropertyETAdapter((DiagramProperty) object,
					modelUpdater, synchronizer);

		throw new TigerstripeException("Can't create ETAdapter for " + object);
	}
}
