/**
 * <copyright>
 * </copyright>
 *
 * $Id: ERefByEnum.java,v 1.1 2008/02/14 23:58:00 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>ERef By Enum</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getERefByEnum()
 * @model
 * @generated
 */
public enum ERefByEnum implements Enumerator {
	/**
	 * The '<em><b>Non Applicable</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NON_APPLICABLE_VALUE
	 * @generated
	 * @ordered
	 */
	NON_APPLICABLE(0, "nonApplicable", "nonApplicable"),

	/**
	 * The '<em><b>Ref By Key</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #REF_BY_KEY_VALUE
	 * @generated
	 * @ordered
	 */
	REF_BY_KEY(1, "refByKey", "refByKey"),

	/**
	 * The '<em><b>Ref By Key Result</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #REF_BY_KEY_RESULT_VALUE
	 * @generated
	 * @ordered
	 */
	REF_BY_KEY_RESULT(2, "refByKeyResult", "refByKeyResult"),

	/**
	 * The '<em><b>Ref By Value</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #REF_BY_VALUE_VALUE
	 * @generated
	 * @ordered
	 */
	REF_BY_VALUE(3, "refByValue", "refByValue");

	/**
	 * The '<em><b>Non Applicable</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Non Applicable</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #NON_APPLICABLE
	 * @model name="nonApplicable"
	 * @generated
	 * @ordered
	 */
	public static final int NON_APPLICABLE_VALUE = 0;

	/**
	 * The '<em><b>Ref By Key</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Ref By Key</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #REF_BY_KEY
	 * @model name="refByKey"
	 * @generated
	 * @ordered
	 */
	public static final int REF_BY_KEY_VALUE = 1;

	/**
	 * The '<em><b>Ref By Key Result</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Ref By Key Result</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #REF_BY_KEY_RESULT
	 * @model name="refByKeyResult"
	 * @generated
	 * @ordered
	 */
	public static final int REF_BY_KEY_RESULT_VALUE = 2;

	/**
	 * The '<em><b>Ref By Value</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Ref By Value</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #REF_BY_VALUE
	 * @model name="refByValue"
	 * @generated
	 * @ordered
	 */
	public static final int REF_BY_VALUE_VALUE = 3;

	/**
	 * An array of all the '<em><b>ERef By Enum</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final ERefByEnum[] VALUES_ARRAY =
		new ERefByEnum[] {
			NON_APPLICABLE,
			REF_BY_KEY,
			REF_BY_KEY_RESULT,
			REF_BY_VALUE,
		};

	/**
	 * A public read-only list of all the '<em><b>ERef By Enum</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<ERefByEnum> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>ERef By Enum</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ERefByEnum get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ERefByEnum result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>ERef By Enum</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ERefByEnum getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ERefByEnum result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>ERef By Enum</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ERefByEnum get(int value) {
		switch (value) {
			case NON_APPLICABLE_VALUE: return NON_APPLICABLE;
			case REF_BY_KEY_VALUE: return REF_BY_KEY;
			case REF_BY_KEY_RESULT_VALUE: return REF_BY_KEY_RESULT;
			case REF_BY_VALUE_VALUE: return REF_BY_VALUE;
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
	private ERefByEnum(int value, String name, String literal) {
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
	
} //ERefByEnum
