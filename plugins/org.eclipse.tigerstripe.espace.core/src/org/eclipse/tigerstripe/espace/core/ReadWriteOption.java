/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.espace.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * @author Yuri Strot
 * @model
 */
public enum ReadWriteOption implements Enumerator {

	/**
	 * @model name="ReadWrite"
	 */
	READ_WRITE(0, "ReadWrite", "ReadWrite"),

	/**
	 * @model name="ReadOnly"
	 */
	READ_ONLY(1, "ReadOnly", "ReadOnly"),

	/**
	 * @model name="ReadOnlyOverride"
	 */
	READ_ONLY_OVERRIDE(2, "ReadOnlyOverride", "ReadOnlyOverride");

	/**
	 * The '<em><b>Read Write</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Read Write</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #READ_WRITE
	 * @model name="ReadWrite"
	 * @generated
	 * @ordered
	 */
	public static final int READ_WRITE_VALUE = 0;
	/**
	 * The '<em><b>Read Only</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Read Only</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #READ_ONLY
	 * @model name="ReadOnly"
	 * @generated
	 * @ordered
	 */
	public static final int READ_ONLY_VALUE = 1;
	/**
	 * The '<em><b>Read Only Override</b></em>' literal value. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Read Only Override</b></em>' literal object
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #READ_ONLY_OVERRIDE
	 * @model name="ReadOnlyOverride"
	 * @generated
	 * @ordered
	 */
	public static final int READ_ONLY_OVERRIDE_VALUE = 2;
	/**
	 * An array of all the '<em><b>Read Write Option</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final ReadWriteOption[] VALUES_ARRAY = new ReadWriteOption[] {
			READ_WRITE, READ_ONLY, READ_ONLY_OVERRIDE, };
	/**
	 * A public read-only list of all the '<em><b>Read Write Option</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List<ReadWriteOption> VALUES = Collections
			.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Read Write Option</b></em>' literal with the
	 * specified literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static ReadWriteOption get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ReadWriteOption result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Read Write Option</b></em>' literal with the
	 * specified name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static ReadWriteOption getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ReadWriteOption result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Read Write Option</b></em>' literal with the
	 * specified integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static ReadWriteOption get(int value) {
		switch (value) {
		case READ_WRITE_VALUE:
			return READ_WRITE;
		case READ_ONLY_VALUE:
			return READ_ONLY;
		case READ_ONLY_OVERRIDE_VALUE:
			return READ_ONLY_OVERRIDE;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private final int value;
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private final String name;
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	private ReadWriteOption(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getLiteral() {
		return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string
	 * representation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
}
