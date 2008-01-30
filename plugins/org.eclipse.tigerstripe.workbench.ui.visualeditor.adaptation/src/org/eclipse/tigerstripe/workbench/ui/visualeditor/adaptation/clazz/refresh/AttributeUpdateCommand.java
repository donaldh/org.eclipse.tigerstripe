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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.ETAdapter;
import org.eclipse.tigerstripe.workbench.internal.api.TigerstripeLicenseException;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.TypeMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.etadapter.ETAdapterFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.utils.ClassDiagramUtils;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.AbstractArtifactHelper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;

public class AttributeUpdateCommand extends AbstractArtifactUpdateCommand {

	public AttributeUpdateCommand(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) {
		super(eArtifact, iArtifact);
	}

	private void migrateMultiplicities(AbstractArtifact eArtifact) {
		for (Attribute eAttr : (List<Attribute>) eArtifact.getAttributes()) {
			if (!eAttr.eIsSet(VisualeditorPackage.eINSTANCE
					.getTypedElement_TypeMultiplicity())) {
				if (eAttr.getMultiplicity() == TypeMultiplicity.NONE_LITERAL
						&& eAttr.getTypeMultiplicity() != AssocMultiplicity.ONE_LITERAL) {
					eAttr.setTypeMultiplicity(AssocMultiplicity.ONE_LITERAL);
					eAttr.eUnset(VisualeditorPackage.eINSTANCE
							.getTypedElement_Multiplicity());
				} else if (eAttr.getMultiplicity() == TypeMultiplicity.ARRAY_LITERAL
						&& eAttr.getTypeMultiplicity() != AssocMultiplicity.ZERO_STAR_LITERAL) {
					eAttr
							.setTypeMultiplicity(AssocMultiplicity.ZERO_STAR_LITERAL);
					eAttr.eUnset(VisualeditorPackage.eINSTANCE
							.getTypedElement_Multiplicity());
				}
			}
		}
		for (Reference eRef : (List<Reference>) eArtifact.getReferences()) {
			if (!eRef.eIsSet(VisualeditorPackage.eINSTANCE
					.getReference_TypeMultiplicity())) {
				if (eRef.getMultiplicity() == TypeMultiplicity.NONE_LITERAL
						&& eRef.getTypeMultiplicity() != AssocMultiplicity.ONE_LITERAL) {
					eRef.setTypeMultiplicity(AssocMultiplicity.ONE_LITERAL);
					eRef.eUnset(VisualeditorPackage.eINSTANCE
							.getReference_Multiplicity());
				} else if (eRef.getMultiplicity() == TypeMultiplicity.ARRAY_LITERAL
						&& eRef.getTypeMultiplicity() != AssocMultiplicity.ZERO_STAR_LITERAL) {
					eRef
							.setTypeMultiplicity(AssocMultiplicity.ZERO_STAR_LITERAL);
					eRef.eUnset(VisualeditorPackage.eINSTANCE
							.getReference_Multiplicity());
				}
			}
		}
	}

	@Override
	public void updateEArtifact(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) {
		// First remove anything the eArtifact contains
		// eArtifact.getAttributes().clear();
		// eArtifact.getReferences().clear();

		migrateMultiplicities(eArtifact);

		// Then recreate either an attribute or a reference in the eArtifact
		// for what we have in the iArtifact
		for (IField field : iArtifact.getFields()) {
			// The choice on Attribute or Reference is made on the type
			// of the IField

			IType type = field.getType();
			if (type != null) {
				String typeStr = type.getFullyQualifiedName();
				if (!type.isPrimitive() && !typeStr.equals("String")) {
					String packageStr = getPackagePart(typeStr);
					if (packageStr.length() == 0) {
						packageStr = iArtifact.getPackage();
						typeStr = packageStr + "." + typeStr;
					}
				}
				if (ClassDiagramUtils
						.shouldPopulateAttribute(
								typeStr,
								((org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact) iArtifact)
										.getArtifactManager())) {

					// let's check that this attribute doesn't exist
					// if it exists as the same attribute, let it be
					// if its type needs to be changed let's change it
					// if it existed as a Reference let's remove it
					int state = ClassDiagramUtils.lookForFieldAsAttribute(
							eArtifact, field);
					Attribute eAttribute = null;
					switch (state) {
					case ClassDiagramUtils.ATTR_ALREADY_EXISTS_ASIS:
						eAttribute = ClassDiagramUtils.findAttributeByName(
								eArtifact, field.getName());
						break;
					case ClassDiagramUtils.ATTR_NEEDS_TYPE_UPDATE:
						// update type only
						eAttribute = ClassDiagramUtils.findAttributeByName(
								eArtifact, field.getName());
						if (!Misc.removeJavaLangString(typeStr).equals(
								eAttribute.getType())) {
							eAttribute.setType(Misc
									.removeJavaLangString(typeStr));
						}
						break;
					case ClassDiagramUtils.ATTR_FOUND_AS_REFERENCE:
						// delete the ref and create attribute
						Reference ref = ClassDiagramUtils.findReferenceByName(
								eArtifact, field.getName());
						eArtifact.getReferences().remove(ref);
					case ClassDiagramUtils.ATTR_DOESNOT_EXIST:
						// Create as an attribute
						eAttribute = VisualeditorFactory.eINSTANCE
								.createAttribute();
						eArtifact.getAttributes().add(eAttribute);
						eAttribute.setName(field.getName());
						eAttribute.setType(Misc.removeJavaLangString(typeStr));
						eAttribute.setDefaultValue(field.getDefaultValue());
						eAttribute.setIsOrdered(field.isOrdered());
						eAttribute.setIsUnique(field.isUnique());
						try {
							TigerstripeProject project = ((org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact) iArtifact)
									.getArtifactManager().getTSProject();

							ITigerstripeProject tsProject = (ITigerstripeProject) TigerstripeCore
									.getDefaultProjectSession()
									.makeTigerstripeProject(
											project.getBaseDir().toURI(), null);
							if (tsProject != null && !isInitialRefresh()) {
								// FIXME: EXTRA ETADAPTER
								// FIXME: module project
								// note: it will be null for a module
								ETAdapter adapter = ETAdapterFactory
										.makeETAdapterFor(eAttribute, tsProject
												.getArtifactManagerSession()
												.getIModelUpdater(), null);
							}
						} catch (TigerstripeException e) {
							EclipsePlugin.log(e);
						} catch (TigerstripeLicenseException e) {
							EclipsePlugin.log(e);
						}

					}
					// other parameters assocated with the attribute
					if (eAttribute != null) {
						if (state != ClassDiagramUtils.ATTR_ALREADY_EXISTS_ASIS) {
							if (eAttribute.getTypeMultiplicity() != ClassDiagramUtils
									.mapTypeMultiplicity(type
											.getTypeMultiplicity())) {
								eAttribute
										.setTypeMultiplicity(ClassDiagramUtils
												.mapTypeMultiplicity(type
														.getTypeMultiplicity()));
							}

							if (eAttribute.getVisibility() != ClassDiagramUtils
									.toVisibility(field.getVisibility()))
								eAttribute.setVisibility(ClassDiagramUtils
										.toVisibility(field.getVisibility()));
							if ((eAttribute.getDefaultValue() == null && field
									.getDefaultValue() != null)
									|| (eAttribute.getDefaultValue() != null && !eAttribute
											.getDefaultValue().equals(
													field.getDefaultValue()))) {
								if (field.getDefaultValue() == null) {
									eAttribute
											.eUnset(VisualeditorPackage.eINSTANCE
													.getTypedElement_DefaultValue());
								} else {
									eAttribute.setDefaultValue(field
											.getDefaultValue());
								}
							}

							if (eAttribute.isIsOrdered() != field.isOrdered()) {
								eAttribute.setIsOrdered(field.isOrdered());
							}

							if (eAttribute.isIsUnique() != field.isUnique()) {
								eAttribute.setIsUnique(field.isUnique());
							}
						}
						if (eAttribute.getStereotypes().size() != field
								.getStereotypeInstances().size()) {
							// not even the same number of args, let's redo the
							// list
							eAttribute.getStereotypes().clear();
							for (IStereotypeInstance stereo : field
									.getStereotypeInstances()) {
								eAttribute.getStereotypes().add(
										stereo.getName());
							}
						} else {
							// same number of stereotypes let's see if they all
							// match
							List<String> eStereotypes = eAttribute.getStereotypes();
							Iterator<String> eStereo = eStereotypes.iterator();
							Collection<IStereotypeInstance> iStereotypes = field.getStereotypeInstances();
							for (IStereotypeInstance iStereo : iStereotypes) {
								String eStereotypeName = eStereo.next();
								String iStereotypeName = iStereo.getName();
								
								if (!eStereotypeName.equals(iStereotypeName)) {
									eAttribute.getStereotypes().remove(eStereo);
									eAttribute.getStereotypes().add(iStereotypeName);
								}
							}
						}
					}

				} else {
					// create as a reference
					AbstractArtifactHelper aHelper = new AbstractArtifactHelper(
							eArtifact);
					int state = ClassDiagramUtils.lookForFieldAsReference(
							eArtifact, field);
					MapHelper helper = new MapHelper((Map) eArtifact
							.eContainer());

					switch (state) {
					case ClassDiagramUtils.REF_ALREADY_EXISTS_ASIS:
						AbstractArtifact theZEndArt = helper
								.findAbstractArtifactFor(typeStr);
						Reference rf = ClassDiagramUtils.findReferenceByName(
								eArtifact, field.getName());

						if (rf.getTypeMultiplicity() != ClassDiagramUtils
								.mapTypeMultiplicity(type.getTypeMultiplicity()))
							rf.setTypeMultiplicity(ClassDiagramUtils
									.mapTypeMultiplicity(type
											.getTypeMultiplicity()));
						if (theZEndArt != null)
							break;
						else {
							Reference theRef = ClassDiagramUtils
									.findReferenceByName(eArtifact, field
											.getName());
							eArtifact.getReferences().remove(theRef);
						}
						break;
					case ClassDiagramUtils.REF_NEEDS_TYPE_UPDATE:
						Reference targetRef = ClassDiagramUtils
								.findReferenceByName(eArtifact, field.getName());
						AbstractArtifact targetZEndArt = helper
								.findAbstractArtifactFor(typeStr);
						if (targetZEndArt != null) {
							targetRef.setZEnd(targetZEndArt);
							targetRef.setTypeMultiplicity(ClassDiagramUtils
									.mapTypeMultiplicity(field.getType()
											.getTypeMultiplicity()));
							targetRef.setIsOrdered(field.isOrdered());
							targetRef.setIsUnique(field.isUnique());
						} else {
							eArtifact.getReferences().remove(targetRef);
						}
						break;
					case ClassDiagramUtils.REF_FOUND_AS_ATTRIBUTE:
						// need to delete the attribute and possibly create the
						// ref
						Attribute attr = ClassDiagramUtils.findAttributeByName(
								eArtifact, field.getName());
						eArtifact.getAttributes().remove(attr);
					case ClassDiagramUtils.REF_DOESNOT_EXIST:
						// The reference can only be created if the referenced
						// element is found on the map
						AbstractArtifact zEndArt = helper
								.findAbstractArtifactFor(typeStr);
						if (zEndArt != null) {
							Reference ref = VisualeditorFactory.eINSTANCE
									.createReference();
							ref.setName(field.getName());

							ref.setZEnd(zEndArt);
							ref.setIsOrdered(field.isOrdered());
							ref.setIsUnique(field.isUnique());
							ref.setTypeMultiplicity(ClassDiagramUtils
									.mapTypeMultiplicity(field.getType()
											.getTypeMultiplicity()));

							try {
								TigerstripeProject project = ((org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact) iArtifact)
										.getArtifactManager().getTSProject();

								ITigerstripeProject tsProject = (ITigerstripeProject) TigerstripeCore
										.getDefaultProjectSession()
										.makeTigerstripeProject(
												project.getBaseDir().toURI(),
												null);
								if (tsProject != null && !isInitialRefresh()) {
									// note: it will be null for a module
									// FIXME: EXTRA ETADAPTER
									// FIXME: module project
									ETAdapter adapter = ETAdapterFactory
											.makeETAdapterFor(
													ref,
													tsProject
															.getArtifactManagerSession()
															.getIModelUpdater(),
													null);
								}
							} catch (TigerstripeException e) {
								EclipsePlugin.log(e);
							} catch (TigerstripeLicenseException e) {
								EclipsePlugin.log(e);
							}

							eArtifact.getReferences().add(ref);
						}
					}
				}
			}
		}

		// Now make sure we don't have anything on the eArtifact that is not on
		// the
		// iArtifact
		List<Attribute> attributesToRemove = new ArrayList<Attribute>();
		List<Reference> referencesToRemove = new ArrayList<Reference>();
		for (Attribute attribute : (List<Attribute>) eArtifact.getAttributes()) {
			IField field = ClassDiagramUtils.findIFieldByName(iArtifact,
					attribute.getName());
			if (field == null) {
				attributesToRemove.add(attribute);
			}
		}

		for (Reference reference : (List<Reference>) eArtifact.getReferences()) {
			IField field = ClassDiagramUtils.findIFieldByName(iArtifact,
					reference.getName());
			if (field == null) {
				referencesToRemove.add(reference);
			}
		}

		if (attributesToRemove.size() != 0)
			eArtifact.getAttributes().removeAll(attributesToRemove);
		if (referencesToRemove.size() != 0)
			eArtifact.getReferences().removeAll(referencesToRemove);

	}

	private String getPackagePart(String fullyQualifiedName) {
		int idx = fullyQualifiedName.lastIndexOf(".");
		if (idx > 0)
			return fullyQualifiedName.substring(0, idx);
		return "";
	}

	public void redo() {
		// TODO Auto-generated method stub
	}

}
