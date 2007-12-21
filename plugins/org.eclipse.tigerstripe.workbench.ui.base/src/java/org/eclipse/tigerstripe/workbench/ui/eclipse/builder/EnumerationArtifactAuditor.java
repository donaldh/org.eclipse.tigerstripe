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
package org.eclipse.tigerstripe.workbench.ui.eclipse.builder;

import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ILabel;
import org.eclipse.tigerstripe.api.artifacts.model.IType;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IEnumArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IOssjEnumSpecifics;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.util.Misc;

public class EnumerationArtifactAuditor extends AbstractArtifactAuditor
		implements IArtifactAuditor {

	public EnumerationArtifactAuditor(IProject project,
			IAbstractArtifact artifact) {
		super(project, artifact);
	}

	@Override
	public void run(IProgressMonitor monitor) {
		super.run(monitor);

		IEnumArtifact artifact = (IEnumArtifact) getArtifact();
		IOssjEnumSpecifics specifics = (IOssjEnumSpecifics) artifact
				.getIStandardSpecifics();

		// Check baseType is set
		IType baseType = specifics.getBaseIType();
		if (baseType == null || baseType.getFullyQualifiedName() == null
				|| baseType.getFullyQualifiedName().length() == 0) {
			TigerstripeProjectAuditor.reportError("Base type not set on '"
					+ artifact.getFullyQualifiedName() + "'.", getIResource(),
					222);
		} else {

			// Bug #156
			if (artifact.getExtendedIArtifact() instanceof IEnumArtifact) {
				IEnumArtifact extended = (IEnumArtifact) artifact
						.getExtendedIArtifact();
				String baseFQN = baseType.getFullyQualifiedName();
				// Bug #885
				String baseTypeStr = Misc.removeJavaLangString(baseFQN);

				IOssjEnumSpecifics eSpecifics = (IOssjEnumSpecifics) extended
						.getIStandardSpecifics();
				String extendedBaseFQN = null;
				if (eSpecifics.getBaseIType() != null) {
					extendedBaseFQN = eSpecifics.getBaseIType()
							.getFullyQualifiedName();
				}
				// Bug #885
				String extendedBaseTypeStr = Misc
						.removeJavaLangString(extendedBaseFQN);
				TigerstripeRuntime.logInfoMessage(baseTypeStr + " "
						+ extendedBaseTypeStr);
				if (!baseTypeStr.equals(extendedBaseTypeStr)) {
					TigerstripeProjectAuditor.reportError(
							"Base type of extended Enumeration mismatched. Was expecting '"
									+ extendedBaseTypeStr + "', found='"
									+ baseTypeStr + "'.", getIResource(), 222);
				}
			}

			// See bug #80
			// Check that baseType is used on all labels
			ILabel[] labels = artifact.getILabels();

			for (int i = 0; i < labels.length; i++) {
				IType labelType = labels[i].getIType();

				String baseTypeStr = Misc.removeJavaLangString(baseType
						.getFullyQualifiedName());
				String labelTypeStr = Misc.removeJavaLangString(labelType
						.getFullyQualifiedName());

				if (labelType == null || !baseTypeStr.equals(labelTypeStr)) {
					String labelFQN = "undefined";
					if (labelType != null) {
						labelFQN = labelType.getFullyQualifiedName();
					}
					TigerstripeProjectAuditor
							.reportError(
									"The type of label '"
											+ labels[i].getName()
											+ "' in '"
											+ artifact.getFullyQualifiedName()
											+ "' is incompatible with the defined base type for this Enumeration Artifact ( expected='"
											+ baseType.getFullyQualifiedName()
											+ "', found='" + labelFQN + "').",
									getIResource(), 222);
				}
			}

			// Check for identical values
			HashMap valueDefinitionMap = new HashMap();
			for (int i = 0; i < labels.length; i++) {
				String value = labels[i].getValue();
				String name = labels[i].getName();
				if (valueDefinitionMap.containsKey(value)) {
					String otherDefName = (String) valueDefinitionMap
							.get(value);
					TigerstripeProjectAuditor.reportWarning("The value ("
							+ value + ") of label '" + name
							+ "' is identical to value of label '"
							+ otherDefName + "' in '"
							+ artifact.getFullyQualifiedName()
							+ "' Enumeration Artifact.", getIResource(), 222);
				} else {
					valueDefinitionMap.put(value, name);
				}
			}
		}
	}
}
