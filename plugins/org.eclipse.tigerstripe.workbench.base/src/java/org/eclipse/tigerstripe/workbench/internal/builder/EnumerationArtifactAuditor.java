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
package org.eclipse.tigerstripe.workbench.internal.builder;

import java.util.HashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.metamodel.impl.IEnumArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjEnumSpecifics;

public class EnumerationArtifactAuditor extends AbstractArtifactAuditor {

	public void run(IProgressMonitor monitor) {

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
			if (artifact.getExtendedArtifact() instanceof IEnumArtifact) {
				IEnumArtifact extended = (IEnumArtifact) artifact
						.getExtendedArtifact();
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
							"Base type of extended "
									+ ArtifactMetadataFactory.INSTANCE
											.getMetadata(
													IEnumArtifactImpl.class
															.getName())
											.getLabel(artifact)
									+ " mismatched. Was expecting '"
									+ extendedBaseTypeStr + "', found='"
									+ baseTypeStr + "'.", getIResource(), 222);
				}
			}

			// See bug #80
			// Check that baseType is used on all labels
			for (ILiteral literal : artifact.getLiterals()) {
				IType labelType = literal.getType();

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
											+ literal.getName()
											+ "' in '"
											+ artifact.getFullyQualifiedName()
											+ "' is incompatible with the defined base type for this "
											+ ArtifactMetadataFactory.INSTANCE
													.getMetadata(
															IEnumArtifactImpl.class
																	.getName())
													.getLabel(artifact)
											+ " Artifact ( expected='"
											+ baseType.getFullyQualifiedName()
											+ "', found='" + labelFQN + "').",
									getIResource(), 222);
				}
			}

			// Check for identical values
			HashMap<String, String> valueDefinitionMap = new HashMap<String, String>();
			for (ILiteral literal : artifact.getLiterals()) {
				String value = literal.getValue();
				String name = literal.getName();
				if (valueDefinitionMap.containsKey(value)) {
					String otherDefName = (String) valueDefinitionMap
							.get(value);
					TigerstripeProjectAuditor.reportWarning(
							"The value ("
									+ value
									+ ") of label '"
									+ name
									+ "' is identical to value of label '"
									+ otherDefName
									+ "' in '"
									+ artifact.getFullyQualifiedName()
									+ "' "
									+ ArtifactMetadataFactory.INSTANCE
											.getMetadata(
													IEnumArtifactImpl.class
															.getName())
											.getLabel(artifact) + " Artifact.",
							getIResource(), 222);
				} else {
					valueDefinitionMap.put(value, name);
				}
			}
		}
	}
}
