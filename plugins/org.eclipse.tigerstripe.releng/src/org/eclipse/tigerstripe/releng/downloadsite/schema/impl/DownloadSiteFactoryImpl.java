/**
 * <copyright>
 * </copyright>
 *
 * $Id: DownloadSiteFactoryImpl.java,v 1.1 2008/04/05 14:00:36 edillon Exp $
 */
package org.eclipse.tigerstripe.releng.downloadsite.schema.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.eclipse.tigerstripe.releng.downloadsite.schema.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DownloadSiteFactoryImpl extends EFactoryImpl implements DownloadSiteFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DownloadSiteFactory init() {
		try {
			DownloadSiteFactory theDownloadSiteFactory = (DownloadSiteFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/tigerstripe/schemas/BuildsSchema"); 
			if (theDownloadSiteFactory != null) {
				return theDownloadSiteFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DownloadSiteFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DownloadSiteFactoryImpl() {
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
			case DownloadSitePackage.BUILD: return createBuild();
			case DownloadSitePackage.BUNDLE: return createBundle();
			case DownloadSitePackage.COMPONENT: return createComponent();
			case DownloadSitePackage.DEPENDENCY: return createDependency();
			case DownloadSitePackage.DOWNLOAD_SITE: return createDownloadSite();
			case DownloadSitePackage.DOWNLOAD_SITE_ELEMENT: return createDownloadSiteElement();
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
			case DownloadSitePackage.BUILD_TYPE:
				return createBuildTypeFromString(eDataType, initialValue);
			case DownloadSitePackage.BUILD_TYPE_OBJECT:
				return createBuildTypeObjectFromString(eDataType, initialValue);
			case DownloadSitePackage.TSTAMP:
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
			case DownloadSitePackage.BUILD_TYPE:
				return convertBuildTypeToString(eDataType, instanceValue);
			case DownloadSitePackage.BUILD_TYPE_OBJECT:
				return convertBuildTypeObjectToString(eDataType, instanceValue);
			case DownloadSitePackage.TSTAMP:
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
	public DownloadSite createDownloadSite() {
		DownloadSiteImpl downloadSite = new DownloadSiteImpl();
		return downloadSite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DownloadSiteElement createDownloadSiteElement() {
		DownloadSiteElementImpl downloadSiteElement = new DownloadSiteElementImpl();
		return downloadSiteElement;
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
		return createBuildTypeFromString(DownloadSitePackage.Literals.BUILD_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertBuildTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertBuildTypeToString(DownloadSitePackage.Literals.BUILD_TYPE, instanceValue);
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
	public DownloadSitePackage getDownloadSitePackage() {
		return (DownloadSitePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static DownloadSitePackage getPackage() {
		return DownloadSitePackage.eINSTANCE;
	}

} //DownloadSiteFactoryImpl
