/**
 * <copyright>
 * </copyright>
 *
 * $Id: EMultiplicity.java,v 1.1 2008/02/14 23:58:00 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>EMultiplicity</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getEMultiplicity()
 * @model
 * @generated
 */
public enum EMultiplicity implements Enumerator {
	/**
	 * The '<em><b>One</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ONE_VALUE
	 * @generated
	 * @ordered
	 */
	ONE(0, "one", "one"),

	/**
	 * The '<em><b>Zero</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ZERO_VALUE
	 * @generated
	 * @ordered
	 */
	ZERO(1, "zero", "zero"),

	/**
	 * The '<em><b>Zero one</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ZERO_ONE_VALUE
	 * @generated
	 * @ordered
	 */
	ZERO_ONE(2, "zero_one", "zero_one"),

	/**
	 * The '<em><b>Zero star</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ZERO_STAR_VALUE
	 * @generated
	 * @ordered
	 */
	ZERO_STAR(3, "zero_star", "zero_star"),

	/**
	 * The '<em><b>One star</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ONE_STAR_VALUE
	 * @generated
	 * @ordered
	 */
	ONE_STAR(4, "one_star", "one_star"),

	/**
	 * The '<em><b>Star</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #STAR_VALUE
	 * @generated
	 * @ordered
	 */
	STAR(5, "star", "star");

	/**
	 * The '<em><b>One</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>One</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ONE
	 * @model name="one"
	 * @generated
	 * @ordered
	 */
	public static final int ONE_VALUE = 0;

	/**
	 * The '<em><b>Zero</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Zero</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ZERO
	 * @model name="zero"
	 * @generated
	 * @ordered
	 */
	public static final int ZERO_VALUE = 1;

	/**
	 * The '<em><b>Zero one</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Zero one</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ZERO_ONE
	 * @model name="zero_one"
	 * @generated
	 * @ordered
	 */
	public static final int ZERO_ONE_VALUE = 2;

	/**
	 * The '<em><b>Zero star</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Zero star</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ZERO_STAR
	 * @model name="zero_star"
	 * @generated
	 * @ordered
	 */
	public static final int ZERO_STAR_VALUE = 3;

	/**
	 * The '<em><b>One star</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>One star</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ONE_STAR
	 * @model name="one_star"
	 * @generated
	 * @ordered
	 */
	public static final int ONE_STAR_VALUE = 4;

	/**
	 * The '<em><b>Star</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Star</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #STAR
	 * @model name="star"
	 * @generated
	 * @ordered
	 */
	public static final int STAR_VALUE = 5;

	/**
	 * An array of all the '<em><b>EMultiplicity</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final EMultiplicity[] VALUES_ARRAY =
		new EMultiplicity[] {
			ONE,
			ZERO,
			ZERO_ONE,
			ZERO_STAR,
			ONE_STAR,
			STAR,
		};

	/**
	 * A public read-only list of all the '<em><b>EMultiplicity</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<EMultiplicity> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>EMultiplicity</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static EMultiplicity get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			EMultiplicity result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>EMultiplicity</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static EMultiplicity getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			EMultiplicity result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>EMultiplicity</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static EMultiplicity get(int value) {
		switch (value) {
			case ONE_VALUE: return ONE;
			case ZERO_VALUE: return ZERO;
			case ZERO_ONE_VALUE: return ZERO_ONE;
			case ZERO_STAR_VALUE: return ZERO_STAR;
			case ONE_STAR_VALUE: return ONE_STAR;
			case STAR_VALUE: return STAR;
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
	private EMultiplicity(int value, String name, String literal) {
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
	
} //EMultiplicity
