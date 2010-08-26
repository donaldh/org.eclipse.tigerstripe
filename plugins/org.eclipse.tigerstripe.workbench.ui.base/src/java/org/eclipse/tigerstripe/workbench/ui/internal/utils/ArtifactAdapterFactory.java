/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.utils;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.IArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.IArtifactFormLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.association.AssociationArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.association.AssociationArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.associationClass.AssociationClassArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.associationClass.AssociationClassArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.datatype.DatatypeArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.datatype.DatatypeArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.dependency.DependencyArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.dependency.DependencyArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.entity.EntityArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.entity.EntityArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.enumeration.EnumArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.enumeration.EnumArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.event.EventArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.event.EventArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.exception.ExceptionArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.exception.ExceptionArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.packageArtifact.PackageArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.packageArtifact.PackageArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.query.QueryArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.query.QueryArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.session.SessionArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.session.SessionArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.updateProcedure.UpdateProcedureArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.updateProcedure.UpdateProcedureArtifactLabelProvider;

public class ArtifactAdapterFactory implements IAdapterFactory {

	@SuppressWarnings("unchecked")
	public Object getAdapter(Object artifact, Class adapterType) {
		if (artifact instanceof IAssociationArtifact) {
			if (IArtifactFormContentProvider.class
					.isAssignableFrom(adapterType)) {
				return new AssociationArtifactFormContentProvider();
			} else if (IArtifactFormLabelProvider.class
					.isAssignableFrom(adapterType)) {
				return new AssociationArtifactLabelProvider();
			}
		} else if (artifact instanceof IAssociationClassArtifact) {
			if (IArtifactFormContentProvider.class
					.isAssignableFrom(adapterType)) {
				return new AssociationClassArtifactFormContentProvider();
			} else if (IArtifactFormLabelProvider.class
					.isAssignableFrom(adapterType)) {
				return new AssociationClassArtifactLabelProvider();
			}
		} else if (artifact instanceof IDatatypeArtifact) {
			if (IArtifactFormContentProvider.class
					.isAssignableFrom(adapterType)) {
				return new DatatypeArtifactFormContentProvider();
			} else if (IArtifactFormLabelProvider.class
					.isAssignableFrom(adapterType)) {
				return new DatatypeArtifactLabelProvider();
			}
		} else if (artifact instanceof IDependencyArtifact) {
			if (IArtifactFormContentProvider.class
					.isAssignableFrom(adapterType)) {
				return new DependencyArtifactFormContentProvider();
			} else if (IArtifactFormLabelProvider.class
					.isAssignableFrom(adapterType)) {
				return new DependencyArtifactLabelProvider();
			}
		} else if (artifact instanceof IManagedEntityArtifact) {
			if (IArtifactFormContentProvider.class
					.isAssignableFrom(adapterType)) {
				return new EntityArtifactFormContentProvider();
			} else if (IArtifactFormLabelProvider.class
					.isAssignableFrom(adapterType)) {
				return new EntityArtifactLabelProvider();
			}
		} else if (artifact instanceof IEnumArtifact) {
			if (IArtifactFormContentProvider.class
					.isAssignableFrom(adapterType)) {
				return new EnumArtifactFormContentProvider();
			} else if (IArtifactFormLabelProvider.class
					.isAssignableFrom(adapterType)) {
				return new EnumArtifactLabelProvider();
			}
		} else if (artifact instanceof IEventArtifact) {
			if (IArtifactFormContentProvider.class
					.isAssignableFrom(adapterType)) {
				return new EventArtifactFormContentProvider();
			} else if (IArtifactFormLabelProvider.class
					.isAssignableFrom(adapterType)) {
				return new EventArtifactLabelProvider();
			}
		} else if (artifact instanceof IExceptionArtifact) {
			if (IArtifactFormContentProvider.class
					.isAssignableFrom(adapterType)) {
				return new ExceptionArtifactFormContentProvider();
			} else if (IArtifactFormLabelProvider.class
					.isAssignableFrom(adapterType)) {
				return new ExceptionArtifactLabelProvider();
			}
		} else if (artifact instanceof IPackageArtifact) {
			if (IArtifactFormContentProvider.class
					.isAssignableFrom(adapterType)) {
				return new PackageArtifactFormContentProvider();
			} else if (IArtifactFormLabelProvider.class
					.isAssignableFrom(adapterType)) {
				return new PackageArtifactLabelProvider();
			}
		} else if (artifact instanceof IQueryArtifact) {
			if (IArtifactFormContentProvider.class
					.isAssignableFrom(adapterType)) {
				return new QueryArtifactFormContentProvider();
			} else if (IArtifactFormLabelProvider.class
					.isAssignableFrom(adapterType)) {
				return new QueryArtifactLabelProvider();
			}
		} else if (artifact instanceof ISessionArtifact) {
			if (IArtifactFormContentProvider.class
					.isAssignableFrom(adapterType)) {
				return new SessionArtifactFormContentProvider();
			} else if (IArtifactFormLabelProvider.class
					.isAssignableFrom(adapterType)) {
				return new SessionArtifactLabelProvider();
			}
		} else if (artifact instanceof IUpdateProcedureArtifact) {
			if (IArtifactFormContentProvider.class
					.isAssignableFrom(adapterType)) {
				return new UpdateProcedureArtifactFormContentProvider();
			} else if (IArtifactFormLabelProvider.class
					.isAssignableFrom(adapterType)) {
				return new UpdateProcedureArtifactLabelProvider();
			}
		}
		return null;
	}

	public Class<?>[] getAdapterList() {
		return new Class[] { IArtifactFormContentProvider.class,
				IArtifactFormLabelProvider.class };
	}
}
