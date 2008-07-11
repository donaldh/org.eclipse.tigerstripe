/**
 * <copyright>
 * </copyright>
 *
 * $Id: ModelFactoryImpl.java,v 1.4 2008/07/11 06:34:47 ystrot Exp $
 */
package org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.tigerstripe.annotation.ui.diagrams.model.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ModelFactoryImpl extends EFactoryImpl implements ModelFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ModelFactory init() {
		try {
			ModelFactory theModelFactory = (ModelFactory)EPackage.Registry.INSTANCE.getEFactory("http:///org/eclipse/tigerstripe/annotation/ui/diagrams/model.ecore"); 
			if (theModelFactory != null) {
				return theModelFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ModelFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case ModelPackage.ANNOTATION_NODE: return createAnnotationNode();
			case ModelPackage.META_ANNOTATION_NODE: return createMetaAnnotationNode();
			case ModelPackage.META_VIEW_ANNOTATIONS: return createMetaViewAnnotations();
			case ModelPackage.VIEW_LOCATION_NODE: return createViewLocationNode();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnnotationNode createAnnotationNode() {
		AnnotationNodeImpl annotationNode = new AnnotationNodeImpl();
		return annotationNode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MetaAnnotationNode createMetaAnnotationNode() {
		MetaAnnotationNodeImpl metaAnnotationNode = new MetaAnnotationNodeImpl();
		return metaAnnotationNode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MetaViewAnnotations createMetaViewAnnotations() {
		MetaViewAnnotationsImpl metaViewAnnotations = new MetaViewAnnotationsImpl();
		return metaViewAnnotations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ViewLocationNode createViewLocationNode() {
		ViewLocationNodeImpl viewLocationNode = new ViewLocationNodeImpl();
		return viewLocationNode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelPackage getModelPackage() {
		return (ModelPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ModelPackage getPackage() {
		return ModelPackage.eINSTANCE;
	}

} //ModelFactoryImpl
