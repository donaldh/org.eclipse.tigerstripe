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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.commands;

import java.util.List;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramFactory;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.helpers.ClassInstanceHelper;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.helpers.InstanceDiagramMapHelper;

/**
 * When an Abstract artifact is dropped on a diagram, a post creation command is
 * issued to set up the newly created EObject with the values found in the
 * IAbstractArtifact being dropped.
 * 
 * This is subclassed for every specific Artifact type
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public abstract class PostCreationAbstractArtifactUpdater extends
		BasePostCreationElementUpdater {

	private ClassInstance eArtifact;

	public PostCreationAbstractArtifactUpdater(IAbstractArtifact iArtifact,
			ClassInstance eArtifact, InstanceMap map,
			ITigerstripeModelProject diagramProject) {
		super(iArtifact, map, diagramProject);
		this.eArtifact = eArtifact;
	}

	protected ClassInstance getEArtifact() {
		return this.eArtifact;
	}

	@Override
	public void updateConnections() throws TigerstripeException {

		InstanceDiagramMapHelper helper = new InstanceDiagramMapHelper(getMap());
		ClassInstanceHelper aHelper = new ClassInstanceHelper(eArtifact);

		// Create variables
		for (IField field : getIArtifact().getFields()) {
			try {
				// Attr should only be populated if type is, either
				// primitive type, java scalar, String or EnumerationType
				String attrType = field.getType().getFullyQualifiedName();
				IArtifactManagerSession session = getDiagramProject()
						.getArtifactManagerSession();
				if (shouldPopulateVariable(attrType, session)) {
					Variable attr = InstancediagramFactory.eINSTANCE
							.createVariable();
					attr.setName(field.getName());
					attr.setType(Misc.removeJavaLangString(attrType));
					eArtifact.getVariables().add(attr);

				}
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	public static boolean shouldPopulateVariable(String attrType,
			IArtifactManagerSession session) {

		// IArtifact art = session.getIArtifactByFullyQualifiedName(attrType);
		// if (art instanceof IPrimitiveTypeArtifact) {
		// return true;
		// } else if (art instanceof IEnumArtifact) {
		// return true;
		// } else if (art == null) {
		// return Util.isJavaScalarType(attrType)
		// || attrType.equals("java.lang.String");
		// }
		// don't want to add variables to the instance when dragged and dropped
		return false;
	}

	protected void internalUpdateIncomingConnections()
			throws TigerstripeException {
		IArtifactManagerSession session = getDiagramProject()
				.getArtifactManagerSession();

		InstanceDiagramMapHelper helper = new InstanceDiagramMapHelper(getMap());

		List<ClassInstance> eArtifacts = getMap().getClassInstances();
		for (ClassInstance eArt : eArtifacts) {
			ClassInstanceHelper aHelper = new ClassInstanceHelper(eArt);
			IAbstractArtifact mirror = session.getArtifactByFullyQualifiedName(eArt
					.getFullyQualifiedName(), true);
			// Take care of variables in other artifacts that should now
			// point to this new object
			if (mirror != null) {

				// take care of artifact extending this new object
				// if (mirror.getExtendedIArtifact() != null
				// && mirror.getExtendedIArtifact()
				// .getFullyQualifiedName().equals(
				// getIArtifact().getFullyQualifiedName())) {
				// eArt.setExtends(eArtifact);
				// }

				if (getIArtifact() instanceof IEnumArtifact)
					return; // no incoming ref on a Enum

			}
		}
	}

	protected void internalUpdateOutgoingConnections()
			throws TigerstripeException {
		// take care of extends
		// if the corresponding EObject is already in the eModel
		// set it, otherwise ignore
		// if (getIArtifact().getExtendedIArtifact() != null) {
		// IAbstractArtifact iExtendedArtifact = getIArtifact()
		// .getExtendedIArtifact();
		// Map map = (Map) eArtifact.eContainer();
		// MapHelper helper = new MapHelper(map);
		// AbstractClassInstanceArtifact eExtendedArtifact = helper
		// .findAbstractArtifactFor(iExtendedArtifact);
		// if (eExtendedArtifact != null) {
		// eArtifact.setExtends(eExtendedArtifact);
		// }
		// }

	}

}
