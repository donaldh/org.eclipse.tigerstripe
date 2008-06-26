/**
 * <copyright>
 * </copyright>
 *
 * $Id: ModelPackage.java,v 1.3 2008/06/26 12:46:46 ystrot Exp $
 */
package org.eclipse.tigerstripe.annotation.core.test.model;

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
 * @see org.eclipse.tigerstripe.annotation.core.test.model.ModelFactory
 * @model kind="package"
 * @generated
 */
public interface ModelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "model";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///org/eclipse/tigerstripe/annotation/core/test/model.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.tigerstripe.annotation.core.test.model";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ModelPackage eINSTANCE = org.eclipse.tigerstripe.annotation.core.test.model.impl.ModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.core.test.model.impl.AuthorImpl <em>Author</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.core.test.model.impl.AuthorImpl
	 * @see org.eclipse.tigerstripe.annotation.core.test.model.impl.ModelPackageImpl#getAuthor()
	 * @generated
	 */
	int AUTHOR = 0;

	/**
	 * The feature id for the '<em><b>First Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AUTHOR__FIRST_NAME = 0;

	/**
	 * The feature id for the '<em><b>Last Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AUTHOR__LAST_NAME = 1;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AUTHOR__DATE = 2;

	/**
	 * The number of structural features of the '<em>Author</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AUTHOR_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.core.test.model.impl.HibernateImpl <em>Hibernate</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.core.test.model.impl.HibernateImpl
	 * @see org.eclipse.tigerstripe.annotation.core.test.model.impl.ModelPackageImpl#getHibernate()
	 * @generated
	 */
	int HIBERNATE = 1;

	/**
	 * The feature id for the '<em><b>Persistance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIBERNATE__PERSISTANCE = 0;

	/**
	 * The number of structural features of the '<em>Hibernate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIBERNATE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.core.test.model.impl.MimeTypeImpl <em>Mime Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.core.test.model.impl.MimeTypeImpl
	 * @see org.eclipse.tigerstripe.annotation.core.test.model.impl.ModelPackageImpl#getMimeType()
	 * @generated
	 */
	int MIME_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Mime Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIME_TYPE__MIME_TYPE = 0;

	/**
	 * The number of structural features of the '<em>Mime Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIME_TYPE_FEATURE_COUNT = 1;


	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.core.test.model.impl.ProjectDescriptionImpl <em>Project Description</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.core.test.model.impl.ProjectDescriptionImpl
	 * @see org.eclipse.tigerstripe.annotation.core.test.model.impl.ModelPackageImpl#getProjectDescription()
	 * @generated
	 */
	int PROJECT_DESCRIPTION = 3;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT_DESCRIPTION__DESCRIPTION = 0;

	/**
	 * The number of structural features of the '<em>Project Description</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT_DESCRIPTION_FEATURE_COUNT = 1;

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.core.test.model.Author <em>Author</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Author</em>'.
	 * @see org.eclipse.tigerstripe.annotation.core.test.model.Author
	 * @generated
	 */
	EClass getAuthor();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.core.test.model.Author#getFirstName <em>First Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>First Name</em>'.
	 * @see org.eclipse.tigerstripe.annotation.core.test.model.Author#getFirstName()
	 * @see #getAuthor()
	 * @generated
	 */
	EAttribute getAuthor_FirstName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.core.test.model.Author#getLastName <em>Last Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Last Name</em>'.
	 * @see org.eclipse.tigerstripe.annotation.core.test.model.Author#getLastName()
	 * @see #getAuthor()
	 * @generated
	 */
	EAttribute getAuthor_LastName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.core.test.model.Author#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see org.eclipse.tigerstripe.annotation.core.test.model.Author#getDate()
	 * @see #getAuthor()
	 * @generated
	 */
	EAttribute getAuthor_Date();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.core.test.model.Hibernate <em>Hibernate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Hibernate</em>'.
	 * @see org.eclipse.tigerstripe.annotation.core.test.model.Hibernate
	 * @generated
	 */
	EClass getHibernate();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.core.test.model.Hibernate#isPersistance <em>Persistance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Persistance</em>'.
	 * @see org.eclipse.tigerstripe.annotation.core.test.model.Hibernate#isPersistance()
	 * @see #getHibernate()
	 * @generated
	 */
	EAttribute getHibernate_Persistance();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.core.test.model.MimeType <em>Mime Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Mime Type</em>'.
	 * @see org.eclipse.tigerstripe.annotation.core.test.model.MimeType
	 * @generated
	 */
	EClass getMimeType();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.core.test.model.MimeType#getMimeType <em>Mime Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mime Type</em>'.
	 * @see org.eclipse.tigerstripe.annotation.core.test.model.MimeType#getMimeType()
	 * @see #getMimeType()
	 * @generated
	 */
	EAttribute getMimeType_MimeType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.core.test.model.ProjectDescription <em>Project Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Project Description</em>'.
	 * @see org.eclipse.tigerstripe.annotation.core.test.model.ProjectDescription
	 * @generated
	 */
	EClass getProjectDescription();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.core.test.model.ProjectDescription#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.eclipse.tigerstripe.annotation.core.test.model.ProjectDescription#getDescription()
	 * @see #getProjectDescription()
	 * @generated
	 */
	EAttribute getProjectDescription_Description();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ModelFactory getModelFactory();

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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.core.test.model.impl.AuthorImpl <em>Author</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.core.test.model.impl.AuthorImpl
		 * @see org.eclipse.tigerstripe.annotation.core.test.model.impl.ModelPackageImpl#getAuthor()
		 * @generated
		 */
		EClass AUTHOR = eINSTANCE.getAuthor();

		/**
		 * The meta object literal for the '<em><b>First Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AUTHOR__FIRST_NAME = eINSTANCE.getAuthor_FirstName();

		/**
		 * The meta object literal for the '<em><b>Last Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AUTHOR__LAST_NAME = eINSTANCE.getAuthor_LastName();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AUTHOR__DATE = eINSTANCE.getAuthor_Date();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.core.test.model.impl.HibernateImpl <em>Hibernate</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.core.test.model.impl.HibernateImpl
		 * @see org.eclipse.tigerstripe.annotation.core.test.model.impl.ModelPackageImpl#getHibernate()
		 * @generated
		 */
		EClass HIBERNATE = eINSTANCE.getHibernate();

		/**
		 * The meta object literal for the '<em><b>Persistance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HIBERNATE__PERSISTANCE = eINSTANCE.getHibernate_Persistance();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.core.test.model.impl.MimeTypeImpl <em>Mime Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.core.test.model.impl.MimeTypeImpl
		 * @see org.eclipse.tigerstripe.annotation.core.test.model.impl.ModelPackageImpl#getMimeType()
		 * @generated
		 */
		EClass MIME_TYPE = eINSTANCE.getMimeType();

		/**
		 * The meta object literal for the '<em><b>Mime Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MIME_TYPE__MIME_TYPE = eINSTANCE.getMimeType_MimeType();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.core.test.model.impl.ProjectDescriptionImpl <em>Project Description</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.core.test.model.impl.ProjectDescriptionImpl
		 * @see org.eclipse.tigerstripe.annotation.core.test.model.impl.ModelPackageImpl#getProjectDescription()
		 * @generated
		 */
		EClass PROJECT_DESCRIPTION = eINSTANCE.getProjectDescription();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROJECT_DESCRIPTION__DESCRIPTION = eINSTANCE.getProjectDescription_Description();

	}

} //ModelPackage
