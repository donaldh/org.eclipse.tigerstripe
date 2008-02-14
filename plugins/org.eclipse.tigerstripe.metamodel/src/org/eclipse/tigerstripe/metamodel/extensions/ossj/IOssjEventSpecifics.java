/**
 * <copyright>
 * </copyright>
 *
 * $Id: IOssjEventSpecifics.java,v 1.1 2008/02/14 23:58:00 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.ossj;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IOssj Event Specifics</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEventSpecifics#isSingleExtensionType <em>Single Extension Type</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEventSpecifics#getEventDescriptorEntries <em>Event Descriptor Entries</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEventSpecifics#getCustomEventDescriptorEntries <em>Custom Event Descriptor Entries</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIOssjEventSpecifics()
 * @model
 * @generated
 */
public interface IOssjEventSpecifics extends IOssjArtifactSpecifics {
	/**
	 * Returns the value of the '<em><b>Single Extension Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Single Extension Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Single Extension Type</em>' attribute.
	 * @see #setSingleExtensionType(boolean)
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIOssjEventSpecifics_SingleExtensionType()
	 * @model
	 * @generated
	 */
	boolean isSingleExtensionType();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEventSpecifics#isSingleExtensionType <em>Single Extension Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Single Extension Type</em>' attribute.
	 * @see #isSingleExtensionType()
	 * @generated
	 */
	void setSingleExtensionType(boolean value);

	/**
	 * Returns the value of the '<em><b>Event Descriptor Entries</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IEventDescriptorEntry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Event Descriptor Entries</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Event Descriptor Entries</em>' reference list.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIOssjEventSpecifics_EventDescriptorEntries()
	 * @model
	 * @generated
	 */
	EList<IEventDescriptorEntry> getEventDescriptorEntries();

	/**
	 * Returns the value of the '<em><b>Custom Event Descriptor Entries</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IEventDescriptorEntry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Custom Event Descriptor Entries</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Custom Event Descriptor Entries</em>' reference list.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIOssjEventSpecifics_CustomEventDescriptorEntries()
	 * @model
	 * @generated
	 */
	EList<IEventDescriptorEntry> getCustomEventDescriptorEntries();

} // IOssjEventSpecifics
