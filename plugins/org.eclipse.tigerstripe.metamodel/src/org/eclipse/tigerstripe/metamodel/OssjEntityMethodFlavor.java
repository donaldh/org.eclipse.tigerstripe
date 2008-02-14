/**
 * <copyright>
 * </copyright>
 *
 * $Id: OssjEntityMethodFlavor.java,v 1.1 2008/02/14 23:58:00 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Ossj Entity Method Flavor</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getOssjEntityMethodFlavor()
 * @model
 * @generated
 */
public enum OssjEntityMethodFlavor implements Enumerator {
	/**
	 * The '<em><b>Simple</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SIMPLE_VALUE
	 * @generated
	 * @ordered
	 */
	SIMPLE(0, "simple", "simple"),

	/**
	 * The '<em><b>Simple By Key</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SIMPLE_BY_KEY_VALUE
	 * @generated
	 * @ordered
	 */
	SIMPLE_BY_KEY(1, "simpleByKey", "simpleByKey"),

	/**
	 * The '<em><b>Bulk Atomic</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BULK_ATOMIC_VALUE
	 * @generated
	 * @ordered
	 */
	BULK_ATOMIC(2, "bulkAtomic", "bulkAtomic"),

	/**
	 * The '<em><b>Bulk Atomic By Keys</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BULK_ATOMIC_BY_KEYS_VALUE
	 * @generated
	 * @ordered
	 */
	BULK_ATOMIC_BY_KEYS(3, "bulkAtomicByKeys", "bulkAtomicByKeys"),

	/**
	 * The '<em><b>Bulk Best Effort</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BULK_BEST_EFFORT_VALUE
	 * @generated
	 * @ordered
	 */
	BULK_BEST_EFFORT(4, "bulkBestEffort", "bulkBestEffort"),

	/**
	 * The '<em><b>Bulk Best Effort By Keys</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BULK_BEST_EFFORT_BY_KEYS_VALUE
	 * @generated
	 * @ordered
	 */
	BULK_BEST_EFFORT_BY_KEYS(5, "bulkBestEffortByKeys", "bulkBestEffortByKeys"),

	/**
	 * The '<em><b>By Template</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BY_TEMPLATE_VALUE
	 * @generated
	 * @ordered
	 */
	BY_TEMPLATE(6, "byTemplate", "byTemplate"),

	/**
	 * The '<em><b>By Templates</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BY_TEMPLATES_VALUE
	 * @generated
	 * @ordered
	 */
	BY_TEMPLATES(7, "byTemplates", "byTemplates"),

	/**
	 * The '<em><b>By Template Best Effort</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BY_TEMPLATE_BEST_EFFORT_VALUE
	 * @generated
	 * @ordered
	 */
	BY_TEMPLATE_BEST_EFFORT(8, "byTemplateBestEffort", "byTemplateBestEffort"),

	/**
	 * The '<em><b>By Templates Best Effort</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BY_TEMPLATES_BEST_EFFORT_VALUE
	 * @generated
	 * @ordered
	 */
	BY_TEMPLATES_BEST_EFFORT(9, "byTemplatesBestEffort", "byTemplatesBestEffort"),

	/**
	 * The '<em><b>By Auto Naming</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BY_AUTO_NAMING_VALUE
	 * @generated
	 * @ordered
	 */
	BY_AUTO_NAMING(10, "byAutoNaming", "byAutoNaming");

	/**
	 * The '<em><b>Simple</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Simple</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SIMPLE
	 * @model name="simple"
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_VALUE = 0;

	/**
	 * The '<em><b>Simple By Key</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Simple By Key</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SIMPLE_BY_KEY
	 * @model name="simpleByKey"
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_BY_KEY_VALUE = 1;

	/**
	 * The '<em><b>Bulk Atomic</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Bulk Atomic</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BULK_ATOMIC
	 * @model name="bulkAtomic"
	 * @generated
	 * @ordered
	 */
	public static final int BULK_ATOMIC_VALUE = 2;

	/**
	 * The '<em><b>Bulk Atomic By Keys</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Bulk Atomic By Keys</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BULK_ATOMIC_BY_KEYS
	 * @model name="bulkAtomicByKeys"
	 * @generated
	 * @ordered
	 */
	public static final int BULK_ATOMIC_BY_KEYS_VALUE = 3;

	/**
	 * The '<em><b>Bulk Best Effort</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Bulk Best Effort</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BULK_BEST_EFFORT
	 * @model name="bulkBestEffort"
	 * @generated
	 * @ordered
	 */
	public static final int BULK_BEST_EFFORT_VALUE = 4;

	/**
	 * The '<em><b>Bulk Best Effort By Keys</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Bulk Best Effort By Keys</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BULK_BEST_EFFORT_BY_KEYS
	 * @model name="bulkBestEffortByKeys"
	 * @generated
	 * @ordered
	 */
	public static final int BULK_BEST_EFFORT_BY_KEYS_VALUE = 5;

	/**
	 * The '<em><b>By Template</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>By Template</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BY_TEMPLATE
	 * @model name="byTemplate"
	 * @generated
	 * @ordered
	 */
	public static final int BY_TEMPLATE_VALUE = 6;

	/**
	 * The '<em><b>By Templates</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>By Templates</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BY_TEMPLATES
	 * @model name="byTemplates"
	 * @generated
	 * @ordered
	 */
	public static final int BY_TEMPLATES_VALUE = 7;

	/**
	 * The '<em><b>By Template Best Effort</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>By Template Best Effort</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BY_TEMPLATE_BEST_EFFORT
	 * @model name="byTemplateBestEffort"
	 * @generated
	 * @ordered
	 */
	public static final int BY_TEMPLATE_BEST_EFFORT_VALUE = 8;

	/**
	 * The '<em><b>By Templates Best Effort</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>By Templates Best Effort</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BY_TEMPLATES_BEST_EFFORT
	 * @model name="byTemplatesBestEffort"
	 * @generated
	 * @ordered
	 */
	public static final int BY_TEMPLATES_BEST_EFFORT_VALUE = 9;

	/**
	 * The '<em><b>By Auto Naming</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>By Auto Naming</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BY_AUTO_NAMING
	 * @model name="byAutoNaming"
	 * @generated
	 * @ordered
	 */
	public static final int BY_AUTO_NAMING_VALUE = 10;

	/**
	 * An array of all the '<em><b>Ossj Entity Method Flavor</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final OssjEntityMethodFlavor[] VALUES_ARRAY =
		new OssjEntityMethodFlavor[] {
			SIMPLE,
			SIMPLE_BY_KEY,
			BULK_ATOMIC,
			BULK_ATOMIC_BY_KEYS,
			BULK_BEST_EFFORT,
			BULK_BEST_EFFORT_BY_KEYS,
			BY_TEMPLATE,
			BY_TEMPLATES,
			BY_TEMPLATE_BEST_EFFORT,
			BY_TEMPLATES_BEST_EFFORT,
			BY_AUTO_NAMING,
		};

	/**
	 * A public read-only list of all the '<em><b>Ossj Entity Method Flavor</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<OssjEntityMethodFlavor> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Ossj Entity Method Flavor</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static OssjEntityMethodFlavor get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			OssjEntityMethodFlavor result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Ossj Entity Method Flavor</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static OssjEntityMethodFlavor getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			OssjEntityMethodFlavor result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Ossj Entity Method Flavor</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static OssjEntityMethodFlavor get(int value) {
		switch (value) {
			case SIMPLE_VALUE: return SIMPLE;
			case SIMPLE_BY_KEY_VALUE: return SIMPLE_BY_KEY;
			case BULK_ATOMIC_VALUE: return BULK_ATOMIC;
			case BULK_ATOMIC_BY_KEYS_VALUE: return BULK_ATOMIC_BY_KEYS;
			case BULK_BEST_EFFORT_VALUE: return BULK_BEST_EFFORT;
			case BULK_BEST_EFFORT_BY_KEYS_VALUE: return BULK_BEST_EFFORT_BY_KEYS;
			case BY_TEMPLATE_VALUE: return BY_TEMPLATE;
			case BY_TEMPLATES_VALUE: return BY_TEMPLATES;
			case BY_TEMPLATE_BEST_EFFORT_VALUE: return BY_TEMPLATE_BEST_EFFORT;
			case BY_TEMPLATES_BEST_EFFORT_VALUE: return BY_TEMPLATES_BEST_EFFORT;
			case BY_AUTO_NAMING_VALUE: return BY_AUTO_NAMING;
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
	private OssjEntityMethodFlavor(int value, String name, String literal) {
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
	
} //OssjEntityMethodFlavor
