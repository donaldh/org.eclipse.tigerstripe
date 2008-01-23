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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NotificationArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.UpdateProcedureArtifact;

/**
 * A refresher factory that will provide a refresher for the given EObject
 * 
 * @author Eric Dillon
 * 
 */
public class RefresherFactory {

	private static RefresherFactory instance;

	private RefresherFactory() {
	}

	public static RefresherFactory getInstance() {
		if (instance == null) {
			instance = new RefresherFactory();
		}
		return instance;
	}

	public IEObjectRefresher getDefaultRefresher(
			TransactionalEditingDomain editingDomain,
			QualifiedNamedElement element) throws TigerstripeException {
		if (element instanceof ManagedEntityArtifact)
			return new AbstractArtifactRefresher(editingDomain);
		else if (element instanceof DatatypeArtifact)
			return new AbstractArtifactRefresher(editingDomain);
		else if (element instanceof NamedQueryArtifact)
			return new QueryArtifactRefresher(editingDomain);
		else if (element instanceof Enumeration)
			return new EnumArtifactRefresher(editingDomain);
		else if (element instanceof UpdateProcedureArtifact)
			return new AbstractArtifactRefresher(editingDomain);
		else if (element instanceof ExceptionArtifact)
			return new ExceptionArtifactRefresher(editingDomain);
		else if (element instanceof NotificationArtifact)
			return new AbstractArtifactRefresher(editingDomain);
		else if (element instanceof SessionFacadeArtifact)
			return new SessionFacadeArtifactRefresher(editingDomain);
		else if (element instanceof AssociationClass)
			return new AbstractArtifactRefresher(editingDomain);
		else if (element instanceof Dependency)
			return new AbstractArtifactRefresher(editingDomain);
		else if (element instanceof Association)
			return new AbstractArtifactRefresher(editingDomain);
		else if (element instanceof AssociationClassClass)
			return new AbstractArtifactRefresher(editingDomain);
		else if (element instanceof AssociationClassClass)
			/*
			 * if it's an AssociationClassClass return a null (the update of the
			 * AssociationClassClass was handled by the update of the
			 * AssociationClass it belongs to)
			 */
			return null;

		throw new TigerstripeException("Element "
				+ element.getFullyQualifiedName()
				+ " couldn't be refreshed, unknown IEObjectRefresher type ("
				+ element.getClass().getName());
	}
}
