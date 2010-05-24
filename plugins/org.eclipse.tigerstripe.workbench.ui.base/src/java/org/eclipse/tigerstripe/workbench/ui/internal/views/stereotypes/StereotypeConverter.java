/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.ui.internal.views.stereotypes;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.impl.EAttributeImpl;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.impl.EEnumImpl;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IEntryListStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;

/**
 * @author Yuri Strot
 * 
 */
public class StereotypeConverter {

	private static EcoreFactory FACTORY = EcoreFactory.eINSTANCE;

	private static EcorePackage PACKAGE = EcorePackage.eINSTANCE;

	private int nextClassId = 0;

	private EPackage ePackage;

	public StereotypeConverter() {
		ePackage = FACTORY.createEPackage();
		ePackage.setEFactoryInstance(FACTORY.createEFactory());
	}

	public EObject createObject(IStereotypeInstance instance) {
		EClass eClass = createEClass(instance.getCharacterizingStereotype());
		DynamicEObjectImpl eObject = new DynamicEObjectImpl(eClass);
		return eObject;
	}

	private EClass createEClass(IStereotype prototype) {
		EClassImpl eClass = (EClassImpl) EcoreFactory.eINSTANCE.createEClass();
		eClass.setName("Stereotype");
		eClass.setClassifierID(nextClassId++);
		eClass.setAbstract(false);
		eClass.setInterface(false);

		ePackage.getEClassifiers().add(eClass);

		IStereotypeAttribute[] attributes = prototype.getAttributes();
		for (int i = 0; i < attributes.length; i++) {
			IStereotypeAttribute attribute = attributes[i];
			EAttributeImpl eAttr = createAttribute(attribute);
			eAttr.setFeatureID(i);
			eClass.getEStructuralFeatures().add(eAttr);
		}
		return eClass;
	}

	private EAttributeImpl createAttribute(IStereotypeAttribute attribute) {
		EAttributeImpl attr = (EAttributeImpl) EcoreFactory.eINSTANCE
				.createEAttribute();
		attr.setName(attribute.getName());
		attr.setEType(getEType(attribute));
		attr.setDefaultValueLiteral(attribute.getDefaultValue());
		attr.setLowerBound(0);
		attr.setUpperBound(attribute.isArray() ? -1 : 1);
		return attr;
	}

	private EClassifier getEType(IStereotypeAttribute attribute) {
		int kind = attribute.getKind();
		switch (kind) {
		case IStereotypeAttribute.STRING_ENTRY_KIND:
			return PACKAGE.getEString();
		case IStereotypeAttribute.CHECKABLE_KIND:
			return PACKAGE.getEBoolean();
		case IStereotypeAttribute.ENTRY_LIST_KIND:
			return createEnum((IEntryListStereotypeAttribute) attribute);
		default:
			throw new UnsupportedOperationException("Unknown attribute kind: "
					+ kind + " (" + attribute + ")");
		}
	}

	private EClassifier createEnum(IEntryListStereotypeAttribute attribute) {
		EEnumImpl e = (EEnumImpl) FACTORY.createEEnum();
		e.setClassifierID(nextClassId++);
		e.setName(attribute.getName());

		String[] values = attribute.getSelectableValues();
		for (int i = 0; i < values.length; i++) {
			String value = values[i];
			EEnumLiteral l = FACTORY.createEEnumLiteral();
			l.setLiteral(value);
			l.setName(value);
			l.setValue(i);
			e.getELiterals().add(l);
		}

		ePackage.getEClassifiers().add(e);

		return e;
	}

}
