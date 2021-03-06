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

import java.util.Collection;
import java.util.List;

import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
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
	private final boolean hideExtends;

	public PostCreationAbstractArtifactUpdater(IAbstractArtifact iArtifact,
			AbstractArtifact eArtifact, Map map,
			ITigerstripeModelProject diagramProject, boolean hideExtends) {
		super(iArtifact, map, diagramProject);
		this.eArtifact = eArtifact;
		this.hideExtends = hideExtends;
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
		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile()
				.getProperty(IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);
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
			for (IField field : getIArtifact().getFields()) {
				try {
					// Attr should only be populated if type is, either
					// primitive type,
					// java scalar, String or EnumerationType
					// or if the References are disabled
					String attrType = field.getType().getFullyQualifiedName();
					IArtifactManagerSession session = getDiagramProject()
							.getArtifactManagerSession();
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
								.mapTypeMultiplicity(field.getType()
										.getTypeMultiplicity()));
						for (IStereotypeInstance instance : field
								.getStereotypeInstances()) {
							attr.getStereotypes().add(instance.getName());
						}
						attr.setField(field);
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
									.mapTypeMultiplicity(field.getType()
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
			for (IMethod method : getIArtifact().getMethods()) {
				String methodType = method.getReturnType()
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
						.mapTypeMultiplicity(method.getReturnType()
								.getTypeMultiplicity()));
				meth.setIsAbstract(method.isAbstract());
				meth.setVisibility(ClassDiagramUtils.toVisibility(method
						.getVisibility()));

				for (IArgument arg : method.getArguments()) {
					Parameter param = VisualeditorFactory.eINSTANCE
							.createParameter();
					param.setName(arg.getName());
					param.setType(Misc.removeJavaLangString(arg.getType()
							.getFullyQualifiedName()));
					param.setIsOrdered(arg.isOrdered());
					param.setIsUnique(arg.isUnique());
					param.setDefaultValue(arg.getDefaultValue());
					// IType type = arg.getIType();
					param.setTypeMultiplicity(ClassDiagramUtils
							.mapTypeMultiplicity(arg.getType()
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
				meth.setMethod(method);
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
		try {
			IAbstractArtifact art = session
					.getArtifactByFullyQualifiedName(attrType);
			if (art instanceof IPrimitiveTypeArtifact)
				return true;
			else if (art instanceof IEnumArtifact)
				return true;
			else if (art == null)
				return Util.isJavaScalarType(attrType)
						|| attrType.equals("java.lang.String")
						|| attrType.equals("String");

			return false;
		} catch (TigerstripeException t) {
			return false;
		}
	}

	protected void internalUpdateIncomingConnections()
			throws TigerstripeException {
		IArtifactManagerSession session = getDiagramProject()
				.getArtifactManagerSession();

		MapHelper helper = new MapHelper(getMap());

		List<AbstractArtifact> eArtifacts = getMap().getArtifacts();
		for (AbstractArtifact eArt : eArtifacts) {
			AbstractArtifactHelper aHelper = new AbstractArtifactHelper(eArt);
			IAbstractArtifact mirror = session.getArtifactByFullyQualifiedName(
					eArt.getFullyQualifiedName(), true);
			// Take care of attributes in other artifacts that should now
			// point to this new object
			if (mirror != null) {

				// take care of artifact extending this new object
				if (mirror.getExtendedArtifact() != null
						&& mirror.getExtendedArtifact().getFullyQualifiedName()
								.equals(getIArtifact().getFullyQualifiedName())) {
					// when DnD reset the state of the Hide/Show Extends
					// property
					// to avoid confusion for the user.

					NamedElementPropertiesHelper namedHelper = new NamedElementPropertiesHelper(
							eArt);
					namedHelper.setProperty(
							NamedElementPropertiesHelper.ARTIFACT_HIDE_EXTENDS,
							Boolean.toString(hideExtends));

					if (hideExtends) {
						eArt.setExtends(null);
					} else {
						eArt.setExtends(eArtifact);
					}
				}

				// take care of artifacts implementing this new object
				if (mirror.getImplementedArtifacts().size() != 0) {
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

				for (IField field : mirror.getFields()) {
					if (field.getType().getFullyQualifiedName()
							.equals(eArtifact.getFullyQualifiedName())
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
									.mapTypeMultiplicity(field.getType()
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
		if (getIArtifact().getExtendedArtifact() != null) {
			IAbstractArtifact iExtendedArtifact = getIArtifact()
					.getExtendedArtifact();
			AbstractArtifact eExtendedArtifact = helper
					.findAbstractArtifactFor(iExtendedArtifact);
			if (eExtendedArtifact != null) {
				if (hideExtends) {
					eArtifact.setExtends(null);
				} else {
					eArtifact.setExtends(eExtendedArtifact);
				}
			}
		}

		// Take care of Implements
		if (getIArtifact().getImplementedArtifacts().size() != 0) {
			Collection<IAbstractArtifact> arts = getIArtifact()
					.getImplementedArtifacts();
			for (IAbstractArtifact art : arts) {
				AbstractArtifact eArt = helper.findAbstractArtifactFor(art);
				if (eArt != null) {
					eArtifact.getImplements().add(eArt);
				}
			}
		}
	}
}
