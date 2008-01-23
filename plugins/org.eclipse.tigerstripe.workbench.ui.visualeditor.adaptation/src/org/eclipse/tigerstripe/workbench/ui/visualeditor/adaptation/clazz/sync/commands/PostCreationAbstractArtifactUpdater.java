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

import org.eclipse.tigerstripe.api.API;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.model.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.model.IField;
import org.eclipse.tigerstripe.api.model.IMethod;
import org.eclipse.tigerstripe.api.model.IMethod.IArgument;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IEnumArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.core.util.Misc;
import org.eclipse.tigerstripe.core.util.Util;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Parameter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.utils.ClassDiagramUtils;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.AbstractArtifactHelper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.NamedElementPropertiesHelper;

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

	private AbstractArtifact eArtifact;

	protected void resetHideExtends(AbstractArtifact eArtifact) {
		NamedElementPropertiesHelper helper = new NamedElementPropertiesHelper(
				eArtifact);
		helper.setProperty(NamedElementPropertiesHelper.ARTIFACT_HIDE_EXTENDS,
				"false");
	}

	public PostCreationAbstractArtifactUpdater(IAbstractArtifact iArtifact,
			AbstractArtifact eArtifact, Map map,
			ITigerstripeProject diagramProject) {
		super(iArtifact, map, diagramProject);
		this.eArtifact = eArtifact;
	}

	protected AbstractArtifact getEArtifact() {
		return this.eArtifact;
	}

	/**
	 * Checks in the current active profile whether References are allowed or
	 * not
	 * 
	 * @return
	 */
	private static boolean shouldDisplayReference() {
		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) API
				.getIWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);
		boolean displayReference = prop
				.getPropertyValue(IOssjLegacySettigsProperty.USEATTRIBUTES_ASREFERENCE);
		return displayReference;
	}

	@Override
	public void updateConnections() throws TigerstripeException {

		try {
			BaseETAdapter.setIgnoreNotify(true);

			MapHelper helper = new MapHelper(getMap());
			AbstractArtifactHelper aHelper = new AbstractArtifactHelper(
					eArtifact);

			eArtifact.setIsAbstract(getIArtifact().isAbstract());
			eArtifact.setIsReadonly(getIArtifact().isReadonly());
			updateStereotype(eArtifact);

			// Create attributes
			for (IField field : getIArtifact().getIFields()) {
				try {
					// Attr should only be populated if type is, either
					// primitive type,
					// java scalar, String or EnumerationType
					// or if the References are disabled
					String attrType = field.getIType().getFullyQualifiedName();
					IArtifactManagerSession session = getDiagramProject()
							.getIArtifactManagerSession();
					if (shouldPopulateAttribute(attrType, session)) {
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
				meth.setDefaultValue(method.getDefaultReturnValue());
				meth.setIsOrdered(method.isOrdered());
				meth.setIsUnique(method.isUnique());
				if (method.isVoid()) {
					meth.setType("void");
				} else {
					meth.setType(Misc.removeJavaLangString(methodType));
				}
				meth.setTypeMultiplicity(ClassDiagramUtils
						.mapTypeMultiplicity(method.getReturnIType()
								.getTypeMultiplicity()));
				meth.setIsAbstract(method.isAbstract());
				meth.setVisibility(ClassDiagramUtils.toVisibility(method
						.getVisibility()));

				for (IArgument arg : method.getIArguments()) {
					Parameter param = VisualeditorFactory.eINSTANCE
							.createParameter();
					param.setName(arg.getName());
					param.setType(Misc.removeJavaLangString(arg.getIType()
							.getFullyQualifiedName()));
					param.setIsOrdered(arg.isOrdered());
					param.setIsUnique(arg.isUnique());
					param.setDefaultValue(arg.getDefaultValue());
					// IType type = arg.getIType();
					param.setTypeMultiplicity(ClassDiagramUtils
							.mapTypeMultiplicity(arg.getIType()
									.getTypeMultiplicity()));

					// review stereotypes
					for (IStereotypeInstance stereo : arg
							.getStereotypeInstances()) {
						param.getStereotypes().add(stereo.getName());
					}

					meth.getParameters().add(param);
				}

				for (IStereotypeInstance instance : method
						.getStereotypeInstances()) {
					meth.getStereotypes().add(instance.getName());
				}

				eArtifact.getMethods().add(meth);

			}

			internalUpdateOutgoingConnections();
			internalUpdateIncomingConnections();

		} finally {
			BaseETAdapter.setIgnoreNotify(false);

		}
	}

	public static boolean shouldPopulateAttribute(String attrType,
			IArtifactManagerSession session) {

		if (!shouldDisplayReference())
			return true;

		IAbstractArtifact art = session.getIArtifactByFullyQualifiedName(attrType);
		if (art instanceof IPrimitiveTypeArtifact)
			return true;
		else if (art instanceof IEnumArtifact)
			return true;
		else if (art == null)
			return Util.isJavaScalarType(attrType)
					|| attrType.equals("java.lang.String")
					|| attrType.equals("String");

		return false;
	}

	protected void internalUpdateIncomingConnections()
			throws TigerstripeException {
		IArtifactManagerSession session = getDiagramProject()
				.getArtifactManagerSession();

		MapHelper helper = new MapHelper(getMap());

		List<AbstractArtifact> eArtifacts = getMap().getArtifacts();
		for (AbstractArtifact eArt : eArtifacts) {
			AbstractArtifactHelper aHelper = new AbstractArtifactHelper(eArt);
			IAbstractArtifact mirror = (IAbstractArtifact) session
					.getIArtifactByFullyQualifiedName(eArt
							.getFullyQualifiedName(), true);
			// Take care of attributes in other artifacts that should now
			// point to this new object
			if (mirror != null) {

				// take care of artifact extending this new object
				if (mirror.getExtendedIArtifact() != null
						&& mirror.getExtendedIArtifact()
								.getFullyQualifiedName().equals(
										getIArtifact().getFullyQualifiedName())) {
					// when DnD reset the state of the Hide/Show Extends
					// property
					// to avoid confusion for the user.
					resetHideExtends(eArt);
					eArt.setExtends(eArtifact);
				}

				// take care of artifacts implementing this new object
				if (mirror.getImplementedArtifacts().length != 0) {
					for (IAbstractArtifact iArt : mirror
							.getImplementedArtifacts()) {
						if (iArt.getFullyQualifiedName().equals(
								getIArtifact().getFullyQualifiedName())) {
							eArt.getImplements().add(eArtifact);
						}
					}
				}

				if (getIArtifact() instanceof IEnumArtifact) {
					continue; // no incoming ref on a Enum
				}

				for (IField field : mirror.getIFields()) {
					if (field.getIType().getFullyQualifiedName().equals(
							eArtifact.getFullyQualifiedName())
							&& shouldDisplayReference() // Bug 929
					) {
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
							ref.setTypeMultiplicity(ClassDiagramUtils
									.mapTypeMultiplicity(field.getIType()
											.getTypeMultiplicity()));
							eArt.getReferences().add(ref);
						}
					}
				}
			}
		}
	}

	protected void internalUpdateOutgoingConnections()
			throws TigerstripeException {

		Map map = (Map) eArtifact.eContainer();
		MapHelper helper = new MapHelper(map);

		// take care of extends
		// if the corresponding EObject is already in the eModel
		// set it, otherwise ignore
		if (getIArtifact().getExtendedIArtifact() != null) {
			IAbstractArtifact iExtendedArtifact = getIArtifact()
					.getExtendedIArtifact();
			AbstractArtifact eExtendedArtifact = helper
					.findAbstractArtifactFor(iExtendedArtifact);
			if (eExtendedArtifact != null) {
				eArtifact.setExtends(eExtendedArtifact);
			}
		}

		// Take care of Implements
		if (getIArtifact().getImplementedArtifacts().length != 0) {
			IAbstractArtifact[] arts = getIArtifact().getImplementedArtifacts();
			for (IAbstractArtifact art : arts) {
				AbstractArtifact eArt = helper.findAbstractArtifactFor(art);
				if (eArt != null) {
					eArtifact.getImplements().add(eArt);
				}
			}
		}

	}
}
