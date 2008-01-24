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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.commands;

import java.util.List;

import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.model.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Parameter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.TypeMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.utils.ClassDiagramUtils;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.AbstractArtifactHelper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;

public class PostCreationAssociationClassArtifactUpdater extends
		PostCreationAssociationArtifactUpdater {

	public PostCreationAssociationClassArtifactUpdater(
			IAbstractArtifact iArtifact, AssociationClass eArtifact, Map map,
			ITigerstripeProject diagramProject) {
		super(iArtifact, eArtifact, map, diagramProject);
	}

	@Override
	public void updateConnections() throws TigerstripeException {
		// TODO Auto-generated method stub
		super.updateConnections();

		try {
			BaseETAdapter.setIgnoreNotify(true);

			AbstractArtifact eArtifact = ((AssociationClass) association)
					.getAssociatedClass();

			eArtifact.setIsReadonly(association.isIsReadonly());
			updateStereotype(eArtifact);

			MapHelper helper = new MapHelper(getMap());
			AbstractArtifactHelper aHelper = new AbstractArtifactHelper(
					eArtifact);

			// Create attributes
			for (IField field : getIArtifact().getIFields()) {
				try {
					// Attr should only be populated if type is, either
					// primitive type,
					// java scalar, String or EnumerationType
					// or if the References are disabled
					String attrType = field.getIType().getFullyQualifiedName();
					IArtifactManagerSession session = getDiagramProject()
							.getArtifactManagerSession();
					if (PostCreationAbstractArtifactUpdater
							.shouldPopulateAttribute(attrType, session)) {
						Attribute attr = VisualeditorFactory.eINSTANCE
								.createAttribute();
						eArtifact.getAttributes().add(attr);
						attr.setName(field.getName());
						attr.setType(Misc.removeJavaLangString(attrType));
						attr.setVisibility(ClassDiagramUtils.toVisibility(field
								.getVisibility()));
						attr.setDefaultValue(field.getDefaultValue());
						attr.setIsOrdered(field.isOrdered());
						attr.setIsUnique(field.isUnique());
						attr.setTypeMultiplicity(ClassDiagramUtils
								.mapTypeMultiplicity(field.getIType()
										.getTypeMultiplicity()));
						for (IStereotypeInstance instance : field
								.getStereotypeInstances()) {
							attr.getStereotypes().add(instance.getName());
						}
					} else {
						// So this is really a reference
						AbstractArtifact targetArt = helper
								.findAbstractArtifactFor(attrType);

						if (targetArt != null
								&& aHelper.findReferenceByName(field.getName()) == null) {
							Reference ref = VisualeditorFactory.eINSTANCE
									.createReference();
							ref.setName(field.getName());
							ref.setZEnd(targetArt);
							ref.setTypeMultiplicity(ClassDiagramUtils
									.mapTypeMultiplicity(field.getIType()
											.getTypeMultiplicity()));
							eArtifact.getReferences().add(ref);
						} else {
							// the target type is not on the diagram so we don't
							// care
							// The ref will be created when the other end is
							// dropped.
						}
					}
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}

			// Create Methods
			for (IMethod method : getIArtifact().getIMethods()) {
				String methodType = method.getReturnIType()
						.getFullyQualifiedName();
				String methodName = method.getName();
				Method meth = VisualeditorFactory.eINSTANCE.createMethod();
				meth.setName(methodName);
				if (method.isVoid()) {
					meth.setType("void");
				} else {
					meth.setType(Misc.removeJavaLangString(methodType));
				}
				meth.setIsAbstract(method.isAbstract());
				meth.setVisibility(ClassDiagramUtils.toVisibility(method
						.getVisibility()));

				for (IArgument arg : method.getIArguments()) {
					Parameter param = VisualeditorFactory.eINSTANCE
							.createParameter();
					param.setName(arg.getName());
					param.setType(Misc.removeJavaLangString(arg.getIType()
							.getFullyQualifiedName()));
					IType type = arg.getIType();
					TypeMultiplicity mult = TypeMultiplicity.NONE_LITERAL;
					if (type.getMultiplicity() == IType.MULTIPLICITY_MULTI) {
						mult = TypeMultiplicity.ARRAY_LITERAL;
					}
					param.setMultiplicity(mult);
					meth.getParameters().add(param);
				}

				for (IStereotypeInstance instance : method
						.getStereotypeInstances()) {
					meth.getStereotypes().add(instance.getName());
				}

				eArtifact.getMethods().add(meth);

			}

			internalUpdateIncomingConnections();
			internalUpdateOutgoingConnections();
		} finally {
			BaseETAdapter.setIgnoreNotify(false);

		}
	}

	protected void internalUpdateIncomingConnections()
			throws TigerstripeException {
		IArtifactManagerSession session = getDiagramProject()
				.getArtifactManagerSession();

		AbstractArtifact eArtifact = ((AssociationClass) association)
				.getAssociatedClass();
		MapHelper helper = new MapHelper(getMap());

		List<AbstractArtifact> eArtifacts = getMap().getArtifacts();
		for (AbstractArtifact eArt : eArtifacts) {
			AbstractArtifactHelper aHelper = new AbstractArtifactHelper(eArt);
			IAbstractArtifact mirror = session.getIArtifactByFullyQualifiedName(eArt
					.getFullyQualifiedName(), true);
			// Take care of attributes in other artifacts that should now
			// point to this new object
			if (mirror != null) {

				// take care of artifact extending this new object
				if (mirror.getExtendedArtifact() != null
						&& mirror.getExtendedArtifact()
								.getFullyQualifiedName().equals(
										getIArtifact().getFullyQualifiedName())) {
					eArt.setExtends(eArtifact);
				}

				for (IField field : mirror.getIFields()) {
					if (field.getIType().getFullyQualifiedName().equals(
							eArtifact.getFullyQualifiedName())) {
						// before we add this reference we need to make sure
						// it's not already
						// in the model as it could be part of a single
						// operation if both
						// artifacts involved in the association have been
						// dragged-n-dropped
						// together
						Reference zRef = aHelper.findReferenceByName(field
								.getName());
						if (zRef == null
								|| (zRef != null && zRef.getZEnd() == null)) {
							Reference ref = VisualeditorFactory.eINSTANCE
									.createReference();
							ref.setName(field.getName());
							ref.setZEnd(eArtifact);
							if (field.getIType().getMultiplicity() == IType.MULTIPLICITY_MULTI) {
								ref
										.setMultiplicity(TypeMultiplicity.ARRAY_LITERAL);
							} else {
								ref
										.setMultiplicity(TypeMultiplicity.NONE_LITERAL);
							}
							eArt.getReferences().add(ref);
						}
					}
				}
			}
		}
	}

	protected void internalUpdateOutgoingConnections()
			throws TigerstripeException {

		AbstractArtifact eArtifact = ((AssociationClass) association)
				.getAssociatedClass();
		// take care of extends
		// if the corresponding EObject is already in the eModel
		// set it, otherwise ignore
		if (getIArtifact().getExtendedArtifact() != null) {
			IAbstractArtifact iExtendedArtifact = getIArtifact()
					.getExtendedArtifact();
			Map map = (Map) eArtifact.eContainer();
			MapHelper helper = new MapHelper(map);
			AbstractArtifact eExtendedArtifact = helper
					.findAbstractArtifactFor(iExtendedArtifact);
			if (eExtendedArtifact != null) {
				eArtifact.setExtends(eExtendedArtifact);
			}
		}

	}

}
