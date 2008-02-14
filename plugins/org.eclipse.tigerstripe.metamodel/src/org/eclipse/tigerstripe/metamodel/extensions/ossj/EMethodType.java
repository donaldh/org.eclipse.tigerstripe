/**
 * <copyright>
 * </copyright>
 *
 * $Id: EMethodType.java,v 1.1 2008/02/14 23:58:00 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.ossj;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>EMethod Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getEMethodType()
 * @model
 * @generated
 */
public enum EMethodType implements Enumerator {
	/**
	 * The '<em><b>CRUD CREATE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CRUD_CREATE_VALUE
	 * @generated
	 * @ordered
	 */
	CRUD_CREATE(0, "CRUD_CREATE", "CRUD_CREATE"),

	/**
	 * The '<em><b>CRUD GET</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CRUD_GET_VALUE
	 * @generated
	 * @ordered
	 */
	CRUD_GET(1, "CRUD_GET", "CRUD_GET"),

	/**
	 * The '<em><b>CRUD SET</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CRUD_SET_VALUE
	 * @generated
	 * @ordered
	 */
	CRUD_SET(2, "CRUD_SET", "CRUD_SET"),

	/**
	 * The '<em><b>CRUD REMOVE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CRUD_REMOVE_VALUE
	 * @generated
	 * @ordered
	 */
	CRUD_REMOVE(3, "CRUD_REMOVE", "CRUD_REMOVE"),

	/**
	 * The '<em><b>CUSTOM</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CUSTOM_VALUE
	 * @generated
	 * @ordered
	 */
	CUSTOM(-1, "CUSTOM", "CUSTOM");

	/**
	 * The '<em><b>CRUD CREATE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>CRUD CREATE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CRUD_CREATE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int CRUD_CREATE_VALUE = 0;

	/**
	 * The '<em><b>CRUD GET</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>CRUD GET</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CRUD_GET
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int CRUD_GET_VALUE = 1;

	/**
	 * The '<em><b>CRUD SET</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>CRUD SET</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CRUD_SET
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int CRUD_SET_VALUE = 2;

	/**
	 * The '<em><b>CRUD REMOVE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>CRUD REMOVE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CRUD_REMOVE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int CRUD_REMOVE_VALUE = 3;

	/**
	 * The '<em><b>CUSTOM</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>CUSTOM</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CUSTOM
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int CUSTOM_VALUE = -1;

	/**
	 * An array of all the '<em><b>EMethod Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final EMethodType[] VALUES_ARRAY =
		new EMethodType[] {
			CRUD_CREATE,
			CRUD_GET,
			CRUD_SET,
			CRUD_REMOVE,
			CUSTOM,
		};

	/**
	 * A public read-only list of all the '<em><b>EMethod Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<EMethodType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>EMethod Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static EMethodType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			EMethodType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>EMethod Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static EMethodType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			EMethodType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>EMethod Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static EMethodType get(int value) {
		switch (value) {
			case CRUD_CREATE_VALUE: return CRUD_CREATE;
			case CRUD_GET_VALUE: return CRUD_GET;
			case CRUD_SET_VALUE: return CRUD_SET;
			case CRUD_REMOVE_VALUE: return CRUD_REMOVE;
			case CUSTOM_VALUE: return CUSTOM;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EMethodType(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
	
} //EMethodType
