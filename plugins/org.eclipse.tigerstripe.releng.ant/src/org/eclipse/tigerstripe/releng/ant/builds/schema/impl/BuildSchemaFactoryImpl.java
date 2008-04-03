/**
 * <copyright>
 * </copyright>
 *
 * $Id: BuildSchemaFactoryImpl.java,v 1.1 2008/04/03 21:45:32 edillon Exp $
 */
package org.eclipse.tigerstripe.releng.ant.builds.schema.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.eclipse.tigerstripe.releng.ant.builds.schema.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class BuildSchemaFactoryImpl extends EFactoryImpl implements BuildSchemaFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static BuildSchemaFactory init() {
		try {
			BuildSchemaFactory theBuildSchemaFactory = (BuildSchemaFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/tigerstripe/schemas/BuildsSchema"); 
			if (theBuildSchemaFactory != null) {
				return theBuildSchemaFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new BuildSchemaFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BuildSchemaFactoryImpl() {
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
			case BuildSchemaPackage.BUILD: return createBuild();
			case BuildSchemaPackage.BUILD_ELEMENT: return createBuildElement();
			case BuildSchemaPackage.BUILDS: return createBuilds();
			case BuildSchemaPackage.BUNDLE: return createBundle();
			case BuildSchemaPackage.COMPONENT: return createComponent();
			case BuildSchemaPackage.DEPENDENCY: return createDependency();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case BuildSchemaPackage.BUILD_TYPE:
				return createBuildTypeFromString(eDataType, initialValue);
			case BuildSchemaPackage.BUILD_TYPE_OBJECT:
				return createBuildTypeObjectFromString(eDataType, initialValue);
			case BuildSchemaPackage.TSTAMP:
				return createTStampFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case BuildSchemaPackage.BUILD_TYPE:
				return convertBuildTypeToString(eDataType, instanceValue);
			case BuildSchemaPackage.BUILD_TYPE_OBJECT:
				return convertBuildTypeObjectToString(eDataType, instanceValue);
			case BuildSchemaPackage.TSTAMP:
				return convertTStampToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Build createBuild() {
		BuildImpl build = new BuildImpl();
		return build;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BuildElement createBuildElement() {
		BuildElementImpl buildElement = new BuildElementImpl();
		return buildElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Builds createBuilds() {
		BuildsImpl builds = new BuildsImpl();
		return builds;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Bundle createBundle() {
		BundleImpl bundle = new BundleImpl();
		return bundle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Component createComponent() {
		ComponentImpl component = new ComponentImpl();
		return component;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Dependency createDependency() {
		DependencyImpl dependency = new DependencyImpl();
		return dependency;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BuildType createBuildTypeFromString(EDataType eDataType, String initialValue) {
		BuildType result = BuildType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertBuildTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BuildType createBuildTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createBuildTypeFromString(BuildSchemaPackage.Literals.BUILD_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertBuildTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertBuildTypeToString(BuildSchemaPackage.Literals.BUILD_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String createTStampFromString(EDataType eDataType, String initialValue) {
		return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTStampToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BuildSchemaPackage getBuildSchemaPackage() {
		return (BuildSchemaPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static BuildSchemaPackage getPackage() {
		return BuildSchemaPackage.eINSTANCE;
	}

} //BuildSchemaFactoryImpl
