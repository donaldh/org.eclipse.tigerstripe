/**
 * <copyright>
 * </copyright>
 *
 * $Id: DocumentationPackage.java,v 1.1 2008/11/05 19:53:22 edillon Exp $
 */
package org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.DocumentationFactory
 * @model kind="package"
 * @generated
 */
public interface DocumentationPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "documentation";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///org/eclipse/tigerstripe/samples/eclipseSummit08/annotations/documentation.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "d";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DocumentationPackage eINSTANCE = org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.impl.DocumentationPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.impl.DocumentationImpl <em>Documentation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.impl.DocumentationImpl
	 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.impl.DocumentationPackageImpl#getDocumentation()
	 * @generated
	 */
	int DOCUMENTATION = 0;

	/**
	 * The feature id for the '<em><b>Author</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTATION__AUTHOR = 0;

	/**
	 * The feature id for the '<em><b>Content</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTATION__CONTENT = 1;

	/**
	 * The number of structural features of the '<em>Documentation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTATION_FEATURE_COUNT = 2;


	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.Documentation <em>Documentation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Documentation</em>'.
	 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.Documentation
	 * @generated
	 */
	EClass getDocumentation();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.Documentation#getAuthor <em>Author</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Author</em>'.
	 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.Documentation#getAuthor()
	 * @see #getDocumentation()
	 * @generated
	 */
	EAttribute getDocumentation_Author();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.Documentation#getContent <em>Content</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Content</em>'.
	 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.Documentation#getContent()
	 * @see #getDocumentation()
	 * @generated
	 */
	EAttribute getDocumentation_Content();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DocumentationFactory getDocumentationFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.impl.DocumentationImpl <em>Documentation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.impl.DocumentationImpl
		 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.impl.DocumentationPackageImpl#getDocumentation()
		 * @generated
		 */
		EClass DOCUMENTATION = eINSTANCE.getDocumentation();

		/**
		 * The meta object literal for the '<em><b>Author</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENTATION__AUTHOR = eINSTANCE.getDocumentation_Author();

		/**
		 * The meta object literal for the '<em><b>Content</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENTATION__CONTENT = eINSTANCE.getDocumentation_Content();

	}

} //DocumentationPackage
