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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ChangeableEnum;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NotificationArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Parameter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.TypeMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class VisualeditorFactoryImpl extends EFactoryImpl implements
		VisualeditorFactory {
	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public static VisualeditorFactory init() {
		try {
			VisualeditorFactory theVisualeditorFactory = (VisualeditorFactory) EPackage.Registry.INSTANCE
					.getEFactory("null");
			if (theVisualeditorFactory != null)
				return theVisualeditorFactory;
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new VisualeditorFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public VisualeditorFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case VisualeditorPackage.NAMED_ELEMENT:
			return createNamedElement();
		case VisualeditorPackage.QUALIFIED_NAMED_ELEMENT:
			return createQualifiedNamedElement();
		case VisualeditorPackage.ABSTRACT_ARTIFACT:
			return createAbstractArtifact();
		case VisualeditorPackage.MANAGED_ENTITY_ARTIFACT:
			return createManagedEntityArtifact();
		case VisualeditorPackage.DATATYPE_ARTIFACT:
			return createDatatypeArtifact();
		case VisualeditorPackage.NOTIFICATION_ARTIFACT:
			return createNotificationArtifact();
		case VisualeditorPackage.NAMED_QUERY_ARTIFACT:
			return createNamedQueryArtifact();
		case VisualeditorPackage.ENUMERATION:
			return createEnumeration();
		case VisualeditorPackage.UPDATE_PROCEDURE_ARTIFACT:
			return createUpdateProcedureArtifact();
		case VisualeditorPackage.EXCEPTION_ARTIFACT:
			return createExceptionArtifact();
		case VisualeditorPackage.SESSION_FACADE_ARTIFACT:
			return createSessionFacadeArtifact();
		case VisualeditorPackage.ASSOCIATION:
			return createAssociation();
		case VisualeditorPackage.ASSOCIATION_CLASS:
			return createAssociationClass();
		case VisualeditorPackage.TYPED_ELEMENT:
			return createTypedElement();
		case VisualeditorPackage.ATTRIBUTE:
			return createAttribute();
		case VisualeditorPackage.METHOD:
			return createMethod();
		case VisualeditorPackage.LITERAL:
			return createLiteral();
		case VisualeditorPackage.PARAMETER:
			return createParameter();
		case VisualeditorPackage.MAP:
			return createMap();
		case VisualeditorPackage.REFERENCE:
			return createReference();
		case VisualeditorPackage.DEPENDENCY:
			return createDependency();
		case VisualeditorPackage.ASSOCIATION_CLASS_CLASS:
			return createAssociationClassClass();
		case VisualeditorPackage.DIAGRAM_PROPERTY:
			return createDiagramProperty();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName()
					+ "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
		case VisualeditorPackage.AGGREGATION_ENUM:
			return createAggregationEnumFromString(eDataType, initialValue);
		case VisualeditorPackage.CHANGEABLE_ENUM:
			return createChangeableEnumFromString(eDataType, initialValue);
		case VisualeditorPackage.ASSOC_MULTIPLICITY:
			return createAssocMultiplicityFromString(eDataType, initialValue);
		case VisualeditorPackage.TYPE_MULTIPLICITY:
			return createTypeMultiplicityFromString(eDataType, initialValue);
		case VisualeditorPackage.VISIBILITY:
			return createVisibilityFromString(eDataType, initialValue);
		default:
			throw new IllegalArgumentException("The datatype '"
					+ eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
		case VisualeditorPackage.AGGREGATION_ENUM:
			return convertAggregationEnumToString(eDataType, instanceValue);
		case VisualeditorPackage.CHANGEABLE_ENUM:
			return convertChangeableEnumToString(eDataType, instanceValue);
		case VisualeditorPackage.ASSOC_MULTIPLICITY:
			return convertAssocMultiplicityToString(eDataType, instanceValue);
		case VisualeditorPackage.TYPE_MULTIPLICITY:
			return convertTypeMultiplicityToString(eDataType, instanceValue);
		case VisualeditorPackage.VISIBILITY:
			return convertVisibilityToString(eDataType, instanceValue);
		default:
			throw new IllegalArgumentException("The datatype '"
					+ eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NamedElement createNamedElement() {
		NamedElementImpl namedElement = new NamedElementImpl();
		return namedElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public QualifiedNamedElement createQualifiedNamedElement() {
		QualifiedNamedElementImpl qualifiedNamedElement = new QualifiedNamedElementImpl();
		return qualifiedNamedElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AbstractArtifact createAbstractArtifact() {
		AbstractArtifactImpl abstractArtifact = new AbstractArtifactImpl();
		return abstractArtifact;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ManagedEntityArtifact createManagedEntityArtifact() {
		ManagedEntityArtifactImpl managedEntityArtifact = new ManagedEntityArtifactImpl();
		return managedEntityArtifact;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DatatypeArtifact createDatatypeArtifact() {
		DatatypeArtifactImpl datatypeArtifact = new DatatypeArtifactImpl();
		return datatypeArtifact;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationArtifact createNotificationArtifact() {
		NotificationArtifactImpl notificationArtifact = new NotificationArtifactImpl();
		return notificationArtifact;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NamedQueryArtifact createNamedQueryArtifact() {
		NamedQueryArtifactImpl namedQueryArtifact = new NamedQueryArtifactImpl();
		return namedQueryArtifact;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Enumeration createEnumeration() {
		EnumerationImpl enumeration = new EnumerationImpl();
		return enumeration;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public UpdateProcedureArtifact createUpdateProcedureArtifact() {
		UpdateProcedureArtifactImpl updateProcedureArtifact = new UpdateProcedureArtifactImpl();
		return updateProcedureArtifact;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ExceptionArtifact createExceptionArtifact() {
		ExceptionArtifactImpl exceptionArtifact = new ExceptionArtifactImpl();
		return exceptionArtifact;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SessionFacadeArtifact createSessionFacadeArtifact() {
		SessionFacadeArtifactImpl sessionFacadeArtifact = new SessionFacadeArtifactImpl();
		return sessionFacadeArtifact;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Association createAssociation() {
		AssociationImpl association = new AssociationImpl();
		return association;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AssociationClass createAssociationClass() {
		AssociationClassImpl associationClass = new AssociationClassImpl();
		return associationClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public TypedElement createTypedElement() {
		TypedElementImpl typedElement = new TypedElementImpl();
		return typedElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Attribute createAttribute() {
		AttributeImpl attribute = new AttributeImpl();
		return attribute;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Method createMethod() {
		MethodImpl method = new MethodImpl();
		return method;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Literal createLiteral() {
		LiteralImpl literal = new LiteralImpl();
		return literal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Parameter createParameter() {
		ParameterImpl parameter = new ParameterImpl();
		return parameter;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Map createMap() {
		MapImpl map = new MapImpl();
		return map;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Reference createReference() {
		ReferenceImpl reference = new ReferenceImpl();
		return reference;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Dependency createDependency() {
		DependencyImpl dependency = new DependencyImpl();
		return dependency;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AssociationClassClass createAssociationClassClass() {
		AssociationClassClassImpl associationClassClass = new AssociationClassClassImpl();
		return associationClassClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DiagramProperty createDiagramProperty() {
		DiagramPropertyImpl diagramProperty = new DiagramPropertyImpl();
		return diagramProperty;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AggregationEnum createAggregationEnumFromString(EDataType eDataType,
			String initialValue) {
		AggregationEnum result = AggregationEnum.get(initialValue);
		if (result == null)
			throw new IllegalArgumentException("The value '" + initialValue
					+ "' is not a valid enumerator of '" + eDataType.getName()
					+ "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertAggregationEnumToString(EDataType eDataType,
			Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ChangeableEnum createChangeableEnumFromString(EDataType eDataType,
			String initialValue) {
		ChangeableEnum result = ChangeableEnum.get(initialValue);
		if (result == null)
			throw new IllegalArgumentException("The value '" + initialValue
					+ "' is not a valid enumerator of '" + eDataType.getName()
					+ "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertChangeableEnumToString(EDataType eDataType,
			Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AssocMultiplicity createAssocMultiplicityFromString(
			EDataType eDataType, String initialValue) {
		AssocMultiplicity result = AssocMultiplicity.get(initialValue);
		if (result == null)
			throw new IllegalArgumentException("The value '" + initialValue
					+ "' is not a valid enumerator of '" + eDataType.getName()
					+ "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertAssocMultiplicityToString(EDataType eDataType,
			Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public TypeMultiplicity createTypeMultiplicityFromString(
			EDataType eDataType, String initialValue) {
		TypeMultiplicity result = TypeMultiplicity.get(initialValue);
		if (result == null)
			throw new IllegalArgumentException("The value '" + initialValue
					+ "' is not a valid enumerator of '" + eDataType.getName()
					+ "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertTypeMultiplicityToString(EDataType eDataType,
			Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Visibility createVisibilityFromString(EDataType eDataType,
			String initialValue) {
		Visibility result = Visibility.get(initialValue);
		if (result == null)
			throw new IllegalArgumentException("The value '" + initialValue
					+ "' is not a valid enumerator of '" + eDataType.getName()
					+ "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertVisibilityToString(EDataType eDataType,
			Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public VisualeditorPackage getVisualeditorPackage() {
		return (VisualeditorPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static VisualeditorPackage getPackage() {
		return VisualeditorPackage.eINSTANCE;
	}

} // VisualeditorFactoryImpl
