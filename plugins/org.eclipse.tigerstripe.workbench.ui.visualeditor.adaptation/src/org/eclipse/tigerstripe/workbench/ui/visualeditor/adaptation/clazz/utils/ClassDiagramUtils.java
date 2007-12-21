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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.utils;

import java.util.List;

import org.eclipse.tigerstripe.api.API;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IField;
import org.eclipse.tigerstripe.api.artifacts.model.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IEnumArtifact;
import org.eclipse.tigerstripe.api.external.model.IextModelComponent;
import org.eclipse.tigerstripe.api.external.model.IextType;
import org.eclipse.tigerstripe.api.external.model.artifacts.IArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextAssociationEnd.EChangeableEnum;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextAssociationEnd.EMultiplicity;
import org.eclipse.tigerstripe.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.core.model.ArtifactManager;
import org.eclipse.tigerstripe.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.core.util.Misc;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.core.util.Util;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ChangeableEnum;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.TypeMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility;

public class ClassDiagramUtils {

	public final static int ATTR_DOESNOT_EXIST = 0;

	public final static int ATTR_NEEDS_TYPE_UPDATE = 1;

	public final static int ATTR_FOUND_AS_REFERENCE = 2;

	public final static int ATTR_ALREADY_EXISTS_ASIS = 3;

	public final static int REF_DOESNOT_EXIST = 4;

	public final static int REF_NEEDS_TYPE_UPDATE = 5;

	public final static int REF_FOUND_AS_ATTRIBUTE = 6;

	public final static int REF_ALREADY_EXISTS_ASIS = 7;

	/**
	 * Returns the corresponding EMF visibility Valid values are: -
	 * {@link IextModelComponent#VISIBILITY_PRIVATE} -
	 * {@link IextModelComponent#VISIBILITY_PROTECTED} -
	 * {@link IextModelComponent#VISIBILITY_PUBLIC} -
	 * {@link IextModelComponent#VISIBILITY_PACKAGE}
	 * 
	 * otherwise returns {@link Visibility#PUBLIC_LITERAL}
	 * 
	 * @param tsVisibility
	 * @return
	 */
	public static Visibility toVisibility(int tsVisibility) {
		switch (tsVisibility) {
		case IextModelComponent.VISIBILITY_PACKAGE:
			return Visibility.PACKAGE_LITERAL;
		case IextModelComponent.VISIBILITY_PRIVATE:
			return Visibility.PRIVATE_LITERAL;
		case IextModelComponent.VISIBILITY_PROTECTED:
			return Visibility.PROTECTED_LITERAL;
		case IextModelComponent.VISIBILITY_PUBLIC:
			return Visibility.PUBLIC_LITERAL;
		}

		return Visibility.PUBLIC_LITERAL;
	}

	public static int fromVisibility(Visibility visibility) {
		switch (visibility.getValue()) {
		case Visibility.PACKAGE:
			return IextModelComponent.VISIBILITY_PACKAGE;
		case Visibility.PRIVATE:
			return IextModelComponent.VISIBILITY_PRIVATE;
		case Visibility.PROTECTED:
			return IextModelComponent.VISIBILITY_PROTECTED;
		case Visibility.PUBLIC:
			return IextModelComponent.VISIBILITY_PUBLIC;
		}
		return IextModelComponent.VISIBILITY_PUBLIC;

	}

	public static TypeMultiplicity toMultiplicity(int mult) {
		switch (mult) {
		case IextType.MULTIPLICITY_SINGLE:
			return (TypeMultiplicity.NONE_LITERAL);
		case IextType.MULTIPLICITY_MULTI:
			return (TypeMultiplicity.ARRAY_LITERAL);
		}
		return TypeMultiplicity.NONE_LITERAL;
	}

	public static int fromMultiplicity(TypeMultiplicity mult) {
		if (mult == TypeMultiplicity.NONE_LITERAL)
			return IextType.MULTIPLICITY_SINGLE;
		else
			return IextType.MULTIPLICITY_MULTI;
	}

	public static IField findIFieldByName(IAbstractArtifact iArtifact,
			String name) {
		for (IField field : iArtifact.getIFields()) {
			if (field.getName().equals(name))
				return field;
		}
		return null;
	}

	public static int lookForFieldAsAttribute(AbstractArtifact eArtifact,
			IField field) {
		List<Attribute> attributes = eArtifact.getAttributes();
		List<Reference> references = eArtifact.getReferences();
		for (Attribute attribute : attributes) {
			if (field.getName().equals(attribute.getName())) {
				if (typesEqual(field, attribute)
						&& visibilityEqual(field, attribute)
						&& attribute.isIsOrdered() == field.isOrdered()
						&& attribute.isIsUnique() == field.isUnique()
						&& ((attribute.getDefaultValue() == null && field
								.getDefaultValue() == null) || (attribute
								.getDefaultValue() != null && attribute
								.getDefaultValue().equals(
										field.getDefaultValue()))))
					return ATTR_ALREADY_EXISTS_ASIS;
				else
					return ATTR_NEEDS_TYPE_UPDATE;
			}
		}
		for (Reference reference : references) {
			if (field.getName().equals(reference.getName()))
				return ATTR_FOUND_AS_REFERENCE;
		}
		return ATTR_DOESNOT_EXIST;
	}

	public static int lookForFieldAsReference(AbstractArtifact eArtifact,
			IField field) {
		List<Attribute> attributes = eArtifact.getAttributes();
		List<Reference> references = eArtifact.getReferences();
		for (Reference reference : references) {
			if (field.getName().equals(reference.getName())) {
				if (typesEqual(field, reference))
					return REF_ALREADY_EXISTS_ASIS;
				else
					return REF_NEEDS_TYPE_UPDATE;
			}
		}
		for (Attribute attribute : attributes) {
			if (field.getName().equals(attribute.getName()))
				return REF_FOUND_AS_ATTRIBUTE;
		}
		return REF_DOESNOT_EXIST;
	}

	public static Attribute findAttributeByName(AbstractArtifact eArtifact,
			String name) {
		if (name == null || name.length() == 0)
			return null;
		List<Attribute> attributes = eArtifact.getAttributes();
		for (Attribute attribute : attributes) {
			if (name.equals(attribute.getName()))
				return attribute;
		}
		return null;
	}

	public static Reference findReferenceByName(AbstractArtifact eArtifact,
			String name) {
		if (name == null || name.length() == 0)
			return null;
		List<Reference> references = eArtifact.getReferences();
		for (Reference reference : references) {
			if (name.equals(reference.getName()))
				return reference;
		}
		return null;
	}

	public static boolean visibilityEqual(IField field, Attribute attribute) {
		return field.getVisibility() == fromVisibility(attribute
				.getVisibility());
	}

	public static boolean typesEqual(IField field, Attribute attribute) {
		if (Misc.removeJavaLangString(field.getIType().getFullyQualifiedName())
				.equals(attribute.getType())) {
			AssocMultiplicity multiplicity = attribute.getTypeMultiplicity();
			if (multiplicity.equals(mapTypeMultiplicity(field.getIType()
					.getTypeMultiplicity())))
				return true;
		}
		return false;
	}

	public static boolean typesEqual(IField field, Reference reference) {
		if (reference.getZEnd() == null)
			return false;
		if (field.getIType().getFullyQualifiedName().equals(
				reference.getZEnd().getFullyQualifiedName())) {
			AssocMultiplicity multiplicity = reference.getTypeMultiplicity();
			if (multiplicity.equals(mapTypeMultiplicity(field.getIType()
					.getTypeMultiplicity())))
				return true;
		}
		return false;
	}

	public static AssocMultiplicity mapTypeMultiplicity(EMultiplicity eMult) {
		if (eMult == EMultiplicity.ONE)
			return AssocMultiplicity.ONE_LITERAL;
		else if (eMult == EMultiplicity.ONE_STAR)
			return AssocMultiplicity.ONE_STAR_LITERAL;
		else if (eMult == EMultiplicity.STAR)
			return AssocMultiplicity.STAR_LITERAL;
		else if (eMult == EMultiplicity.ZERO)
			return AssocMultiplicity.ZERO_LITERAL;
		else if (eMult == EMultiplicity.ZERO_ONE)
			return AssocMultiplicity.ZERO_ONE_LITERAL;
		else if (eMult == EMultiplicity.ZERO_STAR)
			return AssocMultiplicity.ZERO_STAR_LITERAL;

		throw new IllegalArgumentException("Invalid multiplicity " + eMult);
	}

	public static EMultiplicity mapTypeMultiplicity(AssocMultiplicity aMult) {
		if (aMult == AssocMultiplicity.ONE_LITERAL)
			return EMultiplicity.ONE;
		else if (aMult == AssocMultiplicity.ONE_STAR_LITERAL)
			return EMultiplicity.ONE_STAR;
		else if (aMult == AssocMultiplicity.STAR_LITERAL)
			return EMultiplicity.STAR;
		else if (aMult == AssocMultiplicity.ZERO_LITERAL)
			return EMultiplicity.ZERO;
		else if (aMult == AssocMultiplicity.ZERO_ONE_LITERAL)
			return EMultiplicity.ZERO_ONE;
		else if (aMult == AssocMultiplicity.ZERO_STAR_LITERAL)
			return EMultiplicity.ZERO_STAR;

		throw new IllegalArgumentException("Invalid multiplicity " + aMult);
	}

	/**
	 * Checks in the current active profile whether References are allowed or
	 * not (if allowed, returns true...if not, returns false)
	 * 
	 * @return
	 */
	public static boolean shouldDisplayReference() {
		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) API
				.getIWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);
		boolean displayReference = prop
				.getPropertyValue(IOssjLegacySettigsProperty.USEATTRIBUTES_ASREFERENCE);
		return displayReference;
	}

	public static boolean shouldPopulateAttribute(String attrType,
			ArtifactManager mgr) {

		if (!shouldDisplayReference())
			return true;

		IArtifact art = mgr.getArtifactByFullyQualifiedName(attrType, true,
				new TigerstripeNullProgressMonitor());
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

	public static boolean aggregationEquals(EAggregationEnum iAggr,
			AggregationEnum eAggr) {
		if (iAggr == EAggregationEnum.NONE
				&& eAggr == AggregationEnum.NONE_LITERAL)
			return true;
		else if (iAggr == EAggregationEnum.SHARED
				&& eAggr == AggregationEnum.SHARED_LITERAL)
			return true;
		else if (iAggr == EAggregationEnum.COMPOSITE
				&& eAggr == AggregationEnum.COMPOSITE_LITERAL)
			return true;
		else
			return false;
	}

	public static boolean changeableEquals(EChangeableEnum iChan,
			ChangeableEnum eChan) {
		if (iChan == EChangeableEnum.ADDONLY
				&& eChan == ChangeableEnum.ADD_ONLY_LITERAL)
			return true;
		else if (iChan == EChangeableEnum.FROZEN
				&& eChan == ChangeableEnum.FROZEN_LITERAL)
			return true;
		else if (iChan == EChangeableEnum.NONE
				&& eChan == ChangeableEnum.NONE_LITERAL)
			return true;
		else
			return false;
	}

}
