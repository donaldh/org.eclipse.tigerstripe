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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.dnd;

import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;

/**
 * @generated
 */
public class InstanceElementTypeMapper {

	/**
	 * @generated
	 */
	public static final IElementType Map_79 = getElementType("org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.Map_79"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Variable_2001 = getElementType("org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.Variable_2001"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType ClassInstance_1001 = getElementType("org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.ClassInstance_1001"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType AssociationInstance_3001 = getElementType("org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.AssociationInstance_3001"); //$NON-NLS-1$

	/**
	 * @generated NOT
	 */
	private static IElementType getElementType(String id) {
		return ElementTypeRegistry.getInstance().getType(id);
	}

	protected static IElementType mapToElementType(Object obj) {
		if (obj instanceof IManagedEntityArtifact
				|| obj instanceof IDatatypeArtifact
				|| obj instanceof IEnumArtifact
				|| obj instanceof IQueryArtifact
				|| obj instanceof IUpdateProcedureArtifact
				|| obj instanceof IExceptionArtifact
				|| obj instanceof ISessionArtifact
				|| obj instanceof IEventArtifact
				|| obj instanceof IAssociationClassArtifact)
			return ClassInstance_1001;
		else if (obj instanceof IAssociationArtifact
				|| obj instanceof IDependencyArtifact)
			return AssociationInstance_3001;
		return null;
	}

}
