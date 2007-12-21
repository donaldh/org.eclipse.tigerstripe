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
package org.eclipse.tigerstripe.workbench.ui.visualeditor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>Type Multiplicity</b></em>',
 * and utility methods for working with them. <!-- end-user-doc -->
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getTypeMultiplicity()
 * @model
 * @generated
 */
public final class TypeMultiplicity extends AbstractEnumerator {
	/**
	 * The '<em><b>NONE</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>NONE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #NONE_LITERAL
	 * @model literal=""
	 * @generated
	 * @ordered
	 */
	public static final int NONE = 0;

	/**
	 * The '<em><b>ARRAY</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ARRAY</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #ARRAY_LITERAL
	 * @model literal="[]"
	 * @generated
	 * @ordered
	 */
	public static final int ARRAY = 1;

	/**
	 * The '<em><b>ARRAYOFARRAY</b></em>' literal value. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ARRAYOFARRAY</b></em>' literal object
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #ARRAYOFARRAY_LITERAL
	 * @model literal="[][]"
	 * @generated
	 * @ordered
	 */
	public static final int ARRAYOFARRAY = 2;

	/**
	 * The '<em><b>NONE</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #NONE
	 * @generated
	 * @ordered
	 */
	public static final TypeMultiplicity NONE_LITERAL = new TypeMultiplicity(
			NONE, "NONE", "");

	/**
	 * The '<em><b>ARRAY</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #ARRAY
	 * @generated
	 * @ordered
	 */
	public static final TypeMultiplicity ARRAY_LITERAL = new TypeMultiplicity(
			ARRAY, "ARRAY", "[]");

	/**
	 * The '<em><b>ARRAYOFARRAY</b></em>' literal object. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #ARRAYOFARRAY
	 * @generated
	 * @ordered
	 */
	public static final TypeMultiplicity ARRAYOFARRAY_LITERAL = new TypeMultiplicity(
			ARRAYOFARRAY, "ARRAYOFARRAY", "[][]");

	/**
	 * An array of all the '<em><b>Type Multiplicity</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final TypeMultiplicity[] VALUES_ARRAY = new TypeMultiplicity[] {
			NONE_LITERAL, ARRAY_LITERAL, ARRAYOFARRAY_LITERAL, };

	/**
	 * A public read-only list of all the '<em><b>Type Multiplicity</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList(Arrays
			.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Type Multiplicity</b></em>' literal with the
	 * specified literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static TypeMultiplicity get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			TypeMultiplicity result = VALUES_ARRAY[i];
			if (result.toString().equals(literal))
				return result;
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Type Multiplicity</b></em>' literal with the
	 * specified name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static TypeMultiplicity getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			TypeMultiplicity result = VALUES_ARRAY[i];
			if (result.getName().equals(name))
				return result;
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Type Multiplicity</b></em>' literal with the
	 * specified integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static TypeMultiplicity get(int value) {
		switch (value) {
		case NONE:
			return NONE_LITERAL;
		case ARRAY:
			return ARRAY_LITERAL;
		case ARRAYOFARRAY:
			return ARRAYOFARRAY_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	private TypeMultiplicity(int value, String name, String literal) {
		super(value, name, literal);
	}

	public boolean equals(int mult) {
		if (this == TypeMultiplicity.NONE_LITERAL
				&& mult == TypeMultiplicity.NONE)
			return true;
		else if (this == TypeMultiplicity.ARRAY_LITERAL
				&& mult == TypeMultiplicity.ARRAY)
			return true;
		return false;
	}

} // TypeMultiplicity
