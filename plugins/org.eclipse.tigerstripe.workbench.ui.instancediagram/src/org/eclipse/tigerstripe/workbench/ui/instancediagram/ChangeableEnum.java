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
package org.eclipse.tigerstripe.workbench.ui.instancediagram;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>Changeable Enum</b></em>',
 * and utility methods for working with them. <!-- end-user-doc -->
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getChangeableEnum()
 * @model
 * @generated
 */
public final class ChangeableEnum extends AbstractEnumerator {
	/**
	 * The '<em><b>None</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>None</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #NONE_LITERAL
	 * @model name="none"
	 * @generated
	 * @ordered
	 */
	public static final int NONE = 0;

	/**
	 * The '<em><b>Frozen</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Frozen</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #FROZEN_LITERAL
	 * @model name="frozen"
	 * @generated
	 * @ordered
	 */
	public static final int FROZEN = 1;

	/**
	 * The '<em><b>Add Only</b></em>' literal value. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>Add Only</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #ADD_ONLY_LITERAL
	 * @model name="addOnly"
	 * @generated
	 * @ordered
	 */
	public static final int ADD_ONLY = 2;

	/**
	 * The '<em><b>None</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #NONE
	 * @generated
	 * @ordered
	 */
	public static final ChangeableEnum NONE_LITERAL = new ChangeableEnum(NONE,
			"none", "none");

	/**
	 * The '<em><b>Frozen</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #FROZEN
	 * @generated
	 * @ordered
	 */
	public static final ChangeableEnum FROZEN_LITERAL = new ChangeableEnum(
			FROZEN, "frozen", "frozen");

	/**
	 * The '<em><b>Add Only</b></em>' literal object. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #ADD_ONLY
	 * @generated
	 * @ordered
	 */
	public static final ChangeableEnum ADD_ONLY_LITERAL = new ChangeableEnum(
			ADD_ONLY, "addOnly", "addOnly");

	/**
	 * An array of all the '<em><b>Changeable Enum</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final ChangeableEnum[] VALUES_ARRAY = new ChangeableEnum[] {
			NONE_LITERAL, FROZEN_LITERAL, ADD_ONLY_LITERAL, };

	/**
	 * A public read-only list of all the '<em><b>Changeable Enum</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList(Arrays
			.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Changeable Enum</b></em>' literal with the
	 * specified literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static ChangeableEnum get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ChangeableEnum result = VALUES_ARRAY[i];
			if (result.toString().equals(literal))
				return result;
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Changeable Enum</b></em>' literal with the
	 * specified name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static ChangeableEnum getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ChangeableEnum result = VALUES_ARRAY[i];
			if (result.getName().equals(name))
				return result;
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Changeable Enum</b></em>' literal with the
	 * specified integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static ChangeableEnum get(int value) {
		switch (value) {
		case NONE:
			return NONE_LITERAL;
		case FROZEN:
			return FROZEN_LITERAL;
		case ADD_ONLY:
			return ADD_ONLY_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	private ChangeableEnum(int value, String name, String literal) {
		super(value, name, literal);
	}

} // ChangeableEnum
