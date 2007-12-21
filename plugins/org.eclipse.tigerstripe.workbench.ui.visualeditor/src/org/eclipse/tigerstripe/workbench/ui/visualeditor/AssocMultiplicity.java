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
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>Assoc Multiplicity</b></em>',
 * and utility methods for working with them. <!-- end-user-doc -->
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssocMultiplicity()
 * @model
 * @generated
 */
public final class AssocMultiplicity extends AbstractEnumerator {
	/**
	 * The '<em><b>ONE</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ONE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #ONE_LITERAL
	 * @model literal="1"
	 * @generated
	 * @ordered
	 */
	public static final int ONE = 0;

	/**
	 * The '<em><b>ZERO</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ZERO</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #ZERO_LITERAL
	 * @model literal="0"
	 * @generated
	 * @ordered
	 */
	public static final int ZERO = 1;

	/**
	 * The '<em><b>ZERO ONE</b></em>' literal value. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>ZERO ONE</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #ZERO_ONE_LITERAL
	 * @model literal="0..1"
	 * @generated
	 * @ordered
	 */
	public static final int ZERO_ONE = 2;

	/**
	 * The '<em><b>ZERO STAR</b></em>' literal value. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>ZERO STAR</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #ZERO_STAR_LITERAL
	 * @model literal="0..*"
	 * @generated
	 * @ordered
	 */
	public static final int ZERO_STAR = 3;

	/**
	 * The '<em><b>ONE STAR</b></em>' literal value. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>ONE STAR</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #ONE_STAR_LITERAL
	 * @model literal="1..*"
	 * @generated
	 * @ordered
	 */
	public static final int ONE_STAR = 4;

	/**
	 * The '<em><b>STAR</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>STAR</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #STAR_LITERAL
	 * @model literal="*"
	 * @generated
	 * @ordered
	 */
	public static final int STAR = 5;

	/**
	 * The '<em><b>ONE</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #ONE
	 * @generated
	 * @ordered
	 */
	public static final AssocMultiplicity ONE_LITERAL = new AssocMultiplicity(
			ONE, "ONE", "1");

	/**
	 * The '<em><b>ZERO</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #ZERO
	 * @generated
	 * @ordered
	 */
	public static final AssocMultiplicity ZERO_LITERAL = new AssocMultiplicity(
			ZERO, "ZERO", "0");

	/**
	 * The '<em><b>ZERO ONE</b></em>' literal object. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #ZERO_ONE
	 * @generated
	 * @ordered
	 */
	public static final AssocMultiplicity ZERO_ONE_LITERAL = new AssocMultiplicity(
			ZERO_ONE, "ZERO_ONE", "0..1");

	/**
	 * The '<em><b>ZERO STAR</b></em>' literal object. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #ZERO_STAR
	 * @generated
	 * @ordered
	 */
	public static final AssocMultiplicity ZERO_STAR_LITERAL = new AssocMultiplicity(
			ZERO_STAR, "ZERO_STAR", "0..*");

	/**
	 * The '<em><b>ONE STAR</b></em>' literal object. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #ONE_STAR
	 * @generated
	 * @ordered
	 */
	public static final AssocMultiplicity ONE_STAR_LITERAL = new AssocMultiplicity(
			ONE_STAR, "ONE_STAR", "1..*");

	/**
	 * The '<em><b>STAR</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #STAR
	 * @generated
	 * @ordered
	 */
	public static final AssocMultiplicity STAR_LITERAL = new AssocMultiplicity(
			STAR, "STAR", "*");

	/**
	 * An array of all the '<em><b>Assoc Multiplicity</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final AssocMultiplicity[] VALUES_ARRAY = new AssocMultiplicity[] {
			ONE_LITERAL, ZERO_LITERAL, ZERO_ONE_LITERAL, ZERO_STAR_LITERAL,
			ONE_STAR_LITERAL, STAR_LITERAL, };

	/**
	 * A public read-only list of all the '<em><b>Assoc Multiplicity</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList(Arrays
			.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Assoc Multiplicity</b></em>' literal with the
	 * specified literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static AssocMultiplicity get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			AssocMultiplicity result = VALUES_ARRAY[i];
			if (result.toString().equals(literal))
				return result;
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Assoc Multiplicity</b></em>' literal with the
	 * specified name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static AssocMultiplicity getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			AssocMultiplicity result = VALUES_ARRAY[i];
			if (result.getName().equals(name))
				return result;
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Assoc Multiplicity</b></em>' literal with the
	 * specified integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static AssocMultiplicity get(int value) {
		switch (value) {
		case ONE:
			return ONE_LITERAL;
		case ZERO:
			return ZERO_LITERAL;
		case ZERO_ONE:
			return ZERO_ONE_LITERAL;
		case ZERO_STAR:
			return ZERO_STAR_LITERAL;
		case ONE_STAR:
			return ONE_STAR_LITERAL;
		case STAR:
			return STAR_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	private AssocMultiplicity(int value, String name, String literal) {
		super(value, name, literal);
	}

} // AssocMultiplicity
