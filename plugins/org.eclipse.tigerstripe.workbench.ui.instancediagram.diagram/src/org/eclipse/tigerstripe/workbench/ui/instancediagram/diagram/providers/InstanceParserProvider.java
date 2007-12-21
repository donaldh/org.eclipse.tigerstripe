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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.GetParserOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserProvider;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceAEndMultiplicityLowerBoEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceAEndNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceNamePackageArtifactNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceZEndMultiplicityLowerBoEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceZEndNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.ClassInstanceNamePackageArtifactNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.VariableEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.InstanceVisualIDRegistry;

/**
 * @generated
 */
public class InstanceParserProvider extends AbstractProvider implements
		IParserProvider {

	/**
	 * @generated
	 */
	private IParser variableVariable_2001Parser;

	/**
	 * @generated
	 */
	private IParser getVariableVariable_2001Parser() {
		if (variableVariable_2001Parser == null) {
			variableVariable_2001Parser = createVariableVariable_2001Parser();
		}
		return variableVariable_2001Parser;
	}

	/**
	 * @generated
	 */
	protected IParser createVariableVariable_2001Parser() {
		List features = new ArrayList(2);
		features.add(InstancediagramPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(InstancediagramPackage.eINSTANCE.getVariable()
				.getEStructuralFeature("value")); //$NON-NLS-1$
		InstanceStructuralFeaturesParser parser = new InstanceStructuralFeaturesParser(
				features);
		parser.setViewPattern("{0}={1}");
		parser.setEditPattern("{0}={1}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser classInstanceClassInstanceNamePackageArtifactName_4001Parser;

	/**
	 * @generated
	 */
	private IParser getClassInstanceClassInstanceNamePackageArtifactName_4001Parser() {
		if (classInstanceClassInstanceNamePackageArtifactName_4001Parser == null) {
			classInstanceClassInstanceNamePackageArtifactName_4001Parser = createClassInstanceClassInstanceNamePackageArtifactName_4001Parser();
		}
		return classInstanceClassInstanceNamePackageArtifactName_4001Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createClassInstanceClassInstanceNamePackageArtifactName_4001Parser() {
		List features = new ArrayList(3);
		features.add(InstancediagramPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(InstancediagramPackage.eINSTANCE.getInstance()
				.getEStructuralFeature("package")); //$NON-NLS-1$
		features.add(InstancediagramPackage.eINSTANCE.getInstance()
				.getEStructuralFeature("artifactName")); //$NON-NLS-1$
		InstanceNamePackageParser parser = new InstanceNamePackageParser(
				features);
		parser.setViewPattern("{2}:{1}.{0}");
		parser.setEditPattern("{2}:{1}.{0}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser associationInstanceAssociationInstanceNamePackageArtifactName_4002Parser;

	/**
	 * @generated
	 */
	private IParser getAssociationInstanceAssociationInstanceNamePackageArtifactName_4002Parser() {
		if (associationInstanceAssociationInstanceNamePackageArtifactName_4002Parser == null) {
			associationInstanceAssociationInstanceNamePackageArtifactName_4002Parser = createAssociationInstanceAssociationInstanceNamePackageArtifactName_4002Parser();
		}
		return associationInstanceAssociationInstanceNamePackageArtifactName_4002Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createAssociationInstanceAssociationInstanceNamePackageArtifactName_4002Parser() {
		List features = new ArrayList(3);
		features.add(InstancediagramPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(InstancediagramPackage.eINSTANCE.getInstance()
				.getEStructuralFeature("package")); //$NON-NLS-1$
		features.add(InstancediagramPackage.eINSTANCE.getInstance()
				.getEStructuralFeature("artifactName")); //$NON-NLS-1$
		InstanceStructuralFeaturesParser parser = new AssociationInstanceParser(
				features);
		parser.setViewPattern("{2}:{1}.{0}");
		parser.setEditPattern("{2}:{1}.{0}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser associationInstanceAssociationInstanceAEndMultiplicityLowerBoundAEndMultiplicityUpperBound_4003Parser;

	/**
	 * @generated
	 */
	private IParser getAssociationInstanceAssociationInstanceAEndMultiplicityLowerBoundAEndMultiplicityUpperBound_4003Parser() {
		if (associationInstanceAssociationInstanceAEndMultiplicityLowerBoundAEndMultiplicityUpperBound_4003Parser == null) {
			associationInstanceAssociationInstanceAEndMultiplicityLowerBoundAEndMultiplicityUpperBound_4003Parser = createAssociationInstanceAssociationInstanceAEndMultiplicityLowerBoundAEndMultiplicityUpperBound_4003Parser();
		}
		return associationInstanceAssociationInstanceAEndMultiplicityLowerBoundAEndMultiplicityUpperBound_4003Parser;
	}

	/**
	 * @generated
	 */
	protected IParser createAssociationInstanceAssociationInstanceAEndMultiplicityLowerBoundAEndMultiplicityUpperBound_4003Parser() {
		List features = new ArrayList(2);
		features.add(InstancediagramPackage.eINSTANCE.getAssociationInstance()
				.getEStructuralFeature("aEndMultiplicityLowerBound")); //$NON-NLS-1$
		features.add(InstancediagramPackage.eINSTANCE.getAssociationInstance()
				.getEStructuralFeature("aEndMultiplicityUpperBound")); //$NON-NLS-1$
		InstanceStructuralFeaturesParser parser = new InstanceStructuralFeaturesParser(
				features);
		parser.setViewPattern("{0}..{1}");
		parser.setEditPattern("{0}..{1}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser associationInstanceAssociationInstanceAEndName_4004Parser;

	/**
	 * @generated
	 */
	private IParser getAssociationInstanceAssociationInstanceAEndName_4004Parser() {
		if (associationInstanceAssociationInstanceAEndName_4004Parser == null) {
			associationInstanceAssociationInstanceAEndName_4004Parser = createAssociationInstanceAssociationInstanceAEndName_4004Parser();
		}
		return associationInstanceAssociationInstanceAEndName_4004Parser;
	}

	/**
	 * @generated
	 */
	protected IParser createAssociationInstanceAssociationInstanceAEndName_4004Parser() {
		InstanceStructuralFeatureParser parser = new InstanceStructuralFeatureParser(
				InstancediagramPackage.eINSTANCE.getAssociationInstance()
						.getEStructuralFeature("aEndName")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser associationInstanceAssociationInstanceZEndMultiplicityLowerBoundZEndMultiplicityUpperBound_4005Parser;

	/**
	 * @generated
	 */
	private IParser getAssociationInstanceAssociationInstanceZEndMultiplicityLowerBoundZEndMultiplicityUpperBound_4005Parser() {
		if (associationInstanceAssociationInstanceZEndMultiplicityLowerBoundZEndMultiplicityUpperBound_4005Parser == null) {
			associationInstanceAssociationInstanceZEndMultiplicityLowerBoundZEndMultiplicityUpperBound_4005Parser = createAssociationInstanceAssociationInstanceZEndMultiplicityLowerBoundZEndMultiplicityUpperBound_4005Parser();
		}
		return associationInstanceAssociationInstanceZEndMultiplicityLowerBoundZEndMultiplicityUpperBound_4005Parser;
	}

	/**
	 * @generated
	 */
	protected IParser createAssociationInstanceAssociationInstanceZEndMultiplicityLowerBoundZEndMultiplicityUpperBound_4005Parser() {
		List features = new ArrayList(2);
		features.add(InstancediagramPackage.eINSTANCE.getAssociationInstance()
				.getEStructuralFeature("zEndMultiplicityLowerBound")); //$NON-NLS-1$
		features.add(InstancediagramPackage.eINSTANCE.getAssociationInstance()
				.getEStructuralFeature("zEndMultiplicityUpperBound")); //$NON-NLS-1$
		InstanceStructuralFeaturesParser parser = new InstanceStructuralFeaturesParser(
				features);
		parser.setViewPattern("{0}..{1}");
		parser.setEditPattern("{0}..{1}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser associationInstanceAssociationInstanceZEndName_4006Parser;

	/**
	 * @generated
	 */
	private IParser getAssociationInstanceAssociationInstanceZEndName_4006Parser() {
		if (associationInstanceAssociationInstanceZEndName_4006Parser == null) {
			associationInstanceAssociationInstanceZEndName_4006Parser = createAssociationInstanceAssociationInstanceZEndName_4006Parser();
		}
		return associationInstanceAssociationInstanceZEndName_4006Parser;
	}

	/**
	 * @generated
	 */
	protected IParser createAssociationInstanceAssociationInstanceZEndName_4006Parser() {
		InstanceStructuralFeatureParser parser = new InstanceStructuralFeatureParser(
				InstancediagramPackage.eINSTANCE.getAssociationInstance()
						.getEStructuralFeature("zEndName")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	protected IParser getParser(int visualID) {
		switch (visualID) {
		case VariableEditPart.VISUAL_ID:
			return getVariableVariable_2001Parser();
		case ClassInstanceNamePackageArtifactNameEditPart.VISUAL_ID:
			return getClassInstanceClassInstanceNamePackageArtifactName_4001Parser();
		case AssociationInstanceNamePackageArtifactNameEditPart.VISUAL_ID:
			return getAssociationInstanceAssociationInstanceNamePackageArtifactName_4002Parser();
		case AssociationInstanceAEndMultiplicityLowerBoEditPart.VISUAL_ID:
			return getAssociationInstanceAssociationInstanceAEndMultiplicityLowerBoundAEndMultiplicityUpperBound_4003Parser();
		case AssociationInstanceAEndNameEditPart.VISUAL_ID:
			return getAssociationInstanceAssociationInstanceAEndName_4004Parser();
		case AssociationInstanceZEndMultiplicityLowerBoEditPart.VISUAL_ID:
			return getAssociationInstanceAssociationInstanceZEndMultiplicityLowerBoundZEndMultiplicityUpperBound_4005Parser();
		case AssociationInstanceZEndNameEditPart.VISUAL_ID:
			return getAssociationInstanceAssociationInstanceZEndName_4006Parser();
		}
		return null;
	}

	/**
	 * @generated
	 */
	public IParser getParser(IAdaptable hint) {
		String vid = (String) hint.getAdapter(String.class);
		if (vid != null)
			return getParser(InstanceVisualIDRegistry.getVisualID(vid));
		View view = (View) hint.getAdapter(View.class);
		if (view != null)
			return getParser(InstanceVisualIDRegistry.getVisualID(view));
		return null;
	}

	/**
	 * @generated
	 */
	public boolean provides(IOperation operation) {
		if (operation instanceof GetParserOperation) {
			IAdaptable hint = ((GetParserOperation) operation).getHint();
			if (InstanceElementTypes.getElement(hint) == null)
				return false;
			return getParser(hint) != null;
		}
		return false;
	}
}
