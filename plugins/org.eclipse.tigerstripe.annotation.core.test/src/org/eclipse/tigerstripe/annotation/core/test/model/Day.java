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
package org.eclipse.tigerstripe.annotation.core.test.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * @author Yuri Strot
 * @model
 */
public enum Day implements Enumerator {
	  /**
	   * @model name="Sunday"
	   */
	  SUNDAY(0, "Sunday", "Sunday"),
	  
	  /**
	   * @model name="Monday"
	   */
	  MONDAY(1, "Monday", "Monday"),
	  
	  /**
	   * @model name="Tuesday"
	   */
	  TUESDAY(2, "Tuesday", "Tuesday"),
	  
	  /**
	   * @model name="Wednesday"
	   */
	  WEDNESDAY(3, "Wednesday", "Wednesday"),
	  
	  /**
	   * @model name="Thursday"
	   */
	  THURSDAY(4, "Thursday", "Thursday"),
	  
	  /**
	   * @model name="Friday"
	   */
	  FRIDAY(5, "Friday", "Friday"),
	  
	  /**
	   * @model name="Saturday"
	   */
	  SATURDAY(6, "Saturday", "Saturday");

			/**
	 * The '<em><b>Sunday</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Sunday</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SUNDAY
	 * @model name="Sunday"
	 * @generated
	 * @ordered
	 */
	public static final int SUNDAY_VALUE = 0;
			/**
	 * The '<em><b>Monday</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Monday</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MONDAY
	 * @model name="Monday"
	 * @generated
	 * @ordered
	 */
	public static final int MONDAY_VALUE = 1;
			/**
	 * The '<em><b>Tuesday</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Tuesday</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TUESDAY
	 * @model name="Tuesday"
	 * @generated
	 * @ordered
	 */
	public static final int TUESDAY_VALUE = 2;
			/**
	 * The '<em><b>Wednesday</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Wednesday</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #WEDNESDAY
	 * @model name="Wednesday"
	 * @generated
	 * @ordered
	 */
	public static final int WEDNESDAY_VALUE = 3;
			/**
	 * The '<em><b>Thursday</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Thursday</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #THURSDAY
	 * @model name="Thursday"
	 * @generated
	 * @ordered
	 */
	public static final int THURSDAY_VALUE = 4;
			/**
	 * The '<em><b>Friday</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Friday</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #FRIDAY
	 * @model name="Friday"
	 * @generated
	 * @ordered
	 */
	public static final int FRIDAY_VALUE = 5;
			/**
	 * The '<em><b>Saturday</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Saturday</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SATURDAY
	 * @model name="Saturday"
	 * @generated
	 * @ordered
	 */
	public static final int SATURDAY_VALUE = 6;
			/**
	 * An array of all the '<em><b>Day</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final Day[] VALUES_ARRAY =
		new Day[] {
			SUNDAY,
			MONDAY,
			TUESDAY,
			WEDNESDAY,
			THURSDAY,
			FRIDAY,
			SATURDAY,
		};
			/**
	 * A public read-only list of all the '<em><b>Day</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<Day> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

			/**
	 * Returns the '<em><b>Day</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Day get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			Day result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

			/**
	 * Returns the '<em><b>Day</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Day getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			Day result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

			/**
	 * Returns the '<em><b>Day</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Day get(int value) {
		switch (value) {
			case SUNDAY_VALUE: return SUNDAY;
			case MONDAY_VALUE: return MONDAY;
			case TUESDAY_VALUE: return TUESDAY;
			case WEDNESDAY_VALUE: return WEDNESDAY;
			case THURSDAY_VALUE: return THURSDAY;
			case FRIDAY_VALUE: return FRIDAY;
			case SATURDAY_VALUE: return SATURDAY;
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
	private Day(int value, String name, String literal) {
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

}
