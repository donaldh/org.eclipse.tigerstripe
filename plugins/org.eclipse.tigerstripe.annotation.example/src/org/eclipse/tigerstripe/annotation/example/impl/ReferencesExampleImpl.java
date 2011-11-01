/**
 * <copyright>
 * </copyright>
 *
 * $Id: ReferencesExampleImpl.java,v 1.1 2011/11/01 11:12:15 asalnik Exp $
 */
package org.eclipse.tigerstripe.annotation.example.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.tigerstripe.annotation.example.ExamplePackage;
import org.eclipse.tigerstripe.annotation.example.ReferencesExample;

import org.eclipse.tigerstripe.workbench.annotation.modelReference.ModelReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>References Example</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.annotation.example.impl.ReferencesExampleImpl#getEntity <em>Entity</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotation.example.impl.ReferencesExampleImpl#getAttributes <em>Attributes</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotation.example.impl.ReferencesExampleImpl#getStringRefToAttribute <em>String Ref To Attribute</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotation.example.impl.ReferencesExampleImpl#getStringRefsToAttributes <em>String Refs To Attributes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ReferencesExampleImpl extends EObjectImpl implements ReferencesExample {
	/**
	 * The cached value of the '{@link #getEntity() <em>Entity</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntity()
	 * @generated
	 * @ordered
	 */
	protected ModelReference entity;

	/**
	 * The cached value of the '{@link #getAttributes() <em>Attributes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttributes()
	 * @generated
	 * @ordered
	 */
	protected EList<ModelReference> attributes;

	/**
	 * The default value of the '{@link #getStringRefToAttribute() <em>String Ref To Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStringRefToAttribute()
	 * @generated
	 * @ordered
	 */
	protected static final String STRING_REF_TO_ATTRIBUTE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStringRefToAttribute() <em>String Ref To Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStringRefToAttribute()
	 * @generated
	 * @ordered
	 */
	protected String stringRefToAttribute = STRING_REF_TO_ATTRIBUTE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getStringRefsToAttributes() <em>String Refs To Attributes</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStringRefsToAttributes()
	 * @generated
	 * @ordered
	 */
	protected EList<String> stringRefsToAttributes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ReferencesExampleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExamplePackage.Literals.REFERENCES_EXAMPLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelReference getEntity() {
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetEntity(ModelReference newEntity, NotificationChain msgs) {
		ModelReference oldEntity = entity;
		entity = newEntity;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ExamplePackage.REFERENCES_EXAMPLE__ENTITY, oldEntity, newEntity);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEntity(ModelReference newEntity) {
		if (newEntity != entity) {
			NotificationChain msgs = null;
			if (entity != null)
				msgs = ((InternalEObject)entity).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ExamplePackage.REFERENCES_EXAMPLE__ENTITY, null, msgs);
			if (newEntity != null)
				msgs = ((InternalEObject)newEntity).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ExamplePackage.REFERENCES_EXAMPLE__ENTITY, null, msgs);
			msgs = basicSetEntity(newEntity, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExamplePackage.REFERENCES_EXAMPLE__ENTITY, newEntity, newEntity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ModelReference> getAttributes() {
		if (attributes == null) {
			attributes = new EObjectContainmentEList<ModelReference>(ModelReference.class, this, ExamplePackage.REFERENCES_EXAMPLE__ATTRIBUTES);
		}
		return attributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getStringRefToAttribute() {
		return stringRefToAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStringRefToAttribute(String newStringRefToAttribute) {
		String oldStringRefToAttribute = stringRefToAttribute;
		stringRefToAttribute = newStringRefToAttribute;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExamplePackage.REFERENCES_EXAMPLE__STRING_REF_TO_ATTRIBUTE, oldStringRefToAttribute, stringRefToAttribute));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getStringRefsToAttributes() {
		if (stringRefsToAttributes == null) {
			stringRefsToAttributes = new EDataTypeUniqueEList<String>(String.class, this, ExamplePackage.REFERENCES_EXAMPLE__STRING_REFS_TO_ATTRIBUTES);
		}
		return stringRefsToAttributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ExamplePackage.REFERENCES_EXAMPLE__ENTITY:
				return basicSetEntity(null, msgs);
			case ExamplePackage.REFERENCES_EXAMPLE__ATTRIBUTES:
				return ((InternalEList<?>)getAttributes()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ExamplePackage.REFERENCES_EXAMPLE__ENTITY:
				return getEntity();
			case ExamplePackage.REFERENCES_EXAMPLE__ATTRIBUTES:
				return getAttributes();
			case ExamplePackage.REFERENCES_EXAMPLE__STRING_REF_TO_ATTRIBUTE:
				return getStringRefToAttribute();
			case ExamplePackage.REFERENCES_EXAMPLE__STRING_REFS_TO_ATTRIBUTES:
				return getStringRefsToAttributes();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ExamplePackage.REFERENCES_EXAMPLE__ENTITY:
				setEntity((ModelReference)newValue);
				return;
			case ExamplePackage.REFERENCES_EXAMPLE__ATTRIBUTES:
				getAttributes().clear();
				getAttributes().addAll((Collection<? extends ModelReference>)newValue);
				return;
			case ExamplePackage.REFERENCES_EXAMPLE__STRING_REF_TO_ATTRIBUTE:
				setStringRefToAttribute((String)newValue);
				return;
			case ExamplePackage.REFERENCES_EXAMPLE__STRING_REFS_TO_ATTRIBUTES:
				getStringRefsToAttributes().clear();
				getStringRefsToAttributes().addAll((Collection<? extends String>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ExamplePackage.REFERENCES_EXAMPLE__ENTITY:
				setEntity((ModelReference)null);
				return;
			case ExamplePackage.REFERENCES_EXAMPLE__ATTRIBUTES:
				getAttributes().clear();
				return;
			case ExamplePackage.REFERENCES_EXAMPLE__STRING_REF_TO_ATTRIBUTE:
				setStringRefToAttribute(STRING_REF_TO_ATTRIBUTE_EDEFAULT);
				return;
			case ExamplePackage.REFERENCES_EXAMPLE__STRING_REFS_TO_ATTRIBUTES:
				getStringRefsToAttributes().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ExamplePackage.REFERENCES_EXAMPLE__ENTITY:
				return entity != null;
			case ExamplePackage.REFERENCES_EXAMPLE__ATTRIBUTES:
				return attributes != null && !attributes.isEmpty();
			case ExamplePackage.REFERENCES_EXAMPLE__STRING_REF_TO_ATTRIBUTE:
				return STRING_REF_TO_ATTRIBUTE_EDEFAULT == null ? stringRefToAttribute != null : !STRING_REF_TO_ATTRIBUTE_EDEFAULT.equals(stringRefToAttribute);
			case ExamplePackage.REFERENCES_EXAMPLE__STRING_REFS_TO_ATTRIBUTES:
				return stringRefsToAttributes != null && !stringRefsToAttributes.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (stringRefToAttribute: ");
		result.append(stringRefToAttribute);
		result.append(", stringRefsToAttributes: ");
		result.append(stringRefsToAttributes);
		result.append(')');
		return result.toString();
	}

} //ReferencesExampleImpl
