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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage;

/**
 * This is the item provider adapter for a
 * {@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance}
 * object. <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class AssociationInstanceItemProvider extends InstanceItemProvider
		implements IEditingDomainItemProvider, IStructuredItemContentProvider,
		ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AssociationInstanceItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public List getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addAEndPropertyDescriptor(object);
			addAEndNamePropertyDescriptor(object);
			addAEndMultiplicityLowerBoundPropertyDescriptor(object);
			addAEndMultiplicityUpperBoundPropertyDescriptor(object);
			addAEndIsNavigablePropertyDescriptor(object);
			addAEndIsOrderedPropertyDescriptor(object);
			addAEndIsChangeablePropertyDescriptor(object);
			addAEndAggregationPropertyDescriptor(object);
			addZEndPropertyDescriptor(object);
			addZEndNamePropertyDescriptor(object);
			addZEndMultiplicityLowerBoundPropertyDescriptor(object);
			addZEndMultiplicityUpperBoundPropertyDescriptor(object);
			addZEndIsNavigablePropertyDescriptor(object);
			addZEndIsOrderedPropertyDescriptor(object);
			addZEndIsChangeablePropertyDescriptor(object);
			addZEndAggregationPropertyDescriptor(object);
			addReferenceNamePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the AEnd feature. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected void addAEndPropertyDescriptor(Object object) {
		// itemPropertyDescriptors.add
		// (createItemPropertyDescriptor
		// (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
		// getResourceLocator(),
		// getString("_UI_AssociationInstance_aEnd_feature"),
		// getString("_UI_PropertyDescriptor_description",
		// "_UI_AssociationInstance_aEnd_feature",
		// "_UI_AssociationInstance_type"),
		// InstancediagramPackage.Literals.ASSOCIATION_INSTANCE__AEND,
		// true,
		// false,
		// true,
		// null,
		// null,
		// null));
	}

	/**
	 * This adds a property descriptor for the AEnd Name feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addAEndNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory) adapterFactory)
								.getRootAdapterFactory(),
						getResourceLocator(),
						getString("_UI_AssociationInstance_aEndName_feature"),
						getString("_UI_PropertyDescriptor_description",
								"_UI_AssociationInstance_aEndName_feature",
								"_UI_AssociationInstance_type"),
						InstancediagramPackage.Literals.ASSOCIATION_INSTANCE__AEND_NAME,
						true, false, false,
						ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the AEnd Multiplicity Lower Bound
	 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected void addAEndMultiplicityLowerBoundPropertyDescriptor(Object object) {
		// itemPropertyDescriptors.add
		// (createItemPropertyDescriptor
		// (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
		// getResourceLocator(),
		// getString("_UI_AssociationInstance_aEndMultiplicityLowerBound_feature"),
		// getString("_UI_PropertyDescriptor_description",
		// "_UI_AssociationInstance_aEndMultiplicityLowerBound_feature",
		// "_UI_AssociationInstance_type"),
		// InstancediagramPackage.Literals.ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_LOWER_BOUND,
		// true,
		// false,
		// false,
		// ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
		// null,
		// null));
	}

	/**
	 * This adds a property descriptor for the AEnd Multiplicity Upper Bound
	 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected void addAEndMultiplicityUpperBoundPropertyDescriptor(Object object) {
		// itemPropertyDescriptors.add
		// (createItemPropertyDescriptor
		// (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
		// getResourceLocator(),
		// getString("_UI_AssociationInstance_aEndMultiplicityUpperBound_feature"),
		// getString("_UI_PropertyDescriptor_description",
		// "_UI_AssociationInstance_aEndMultiplicityUpperBound_feature",
		// "_UI_AssociationInstance_type"),
		// InstancediagramPackage.Literals.ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_UPPER_BOUND,
		// true,
		// false,
		// false,
		// ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
		// null,
		// null));
	}

	/**
	 * This adds a property descriptor for the AEnd Is Navigable feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected void addAEndIsNavigablePropertyDescriptor(Object object) {
		// itemPropertyDescriptors.add
		// (createItemPropertyDescriptor
		// (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
		// getResourceLocator(),
		// getString("_UI_AssociationInstance_aEndIsNavigable_feature"),
		// getString("_UI_PropertyDescriptor_description",
		// "_UI_AssociationInstance_aEndIsNavigable_feature",
		// "_UI_AssociationInstance_type"),
		// InstancediagramPackage.Literals.ASSOCIATION_INSTANCE__AEND_IS_NAVIGABLE,
		// true,
		// false,
		// false,
		// ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
		// null,
		// null));
	}

	/**
	 * This adds a property descriptor for the AEnd Is Ordered feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected void addAEndIsOrderedPropertyDescriptor(Object object) {
		// itemPropertyDescriptors.add
		// (createItemPropertyDescriptor
		// (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
		// getResourceLocator(),
		// getString("_UI_AssociationInstance_aEndIsOrdered_feature"),
		// getString("_UI_PropertyDescriptor_description",
		// "_UI_AssociationInstance_aEndIsOrdered_feature",
		// "_UI_AssociationInstance_type"),
		// InstancediagramPackage.Literals.ASSOCIATION_INSTANCE__AEND_IS_ORDERED,
		// true,
		// false,
		// false,
		// ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
		// null,
		// null));
	}

	/**
	 * This adds a property descriptor for the AEnd Is Changeable feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected void addAEndIsChangeablePropertyDescriptor(Object object) {
		// itemPropertyDescriptors.add
		// (createItemPropertyDescriptor
		// (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
		// getResourceLocator(),
		// getString("_UI_AssociationInstance_aEndIsChangeable_feature"),
		// getString("_UI_PropertyDescriptor_description",
		// "_UI_AssociationInstance_aEndIsChangeable_feature",
		// "_UI_AssociationInstance_type"),
		// InstancediagramPackage.Literals.ASSOCIATION_INSTANCE__AEND_IS_CHANGEABLE,
		// true,
		// false,
		// false,
		// ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
		// null,
		// null));
	}

	/**
	 * This adds a property descriptor for the AEnd Aggregation feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected void addAEndAggregationPropertyDescriptor(Object object) {
		// itemPropertyDescriptors.add
		// (createItemPropertyDescriptor
		// (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
		// getResourceLocator(),
		// getString("_UI_AssociationInstance_aEndAggregation_feature"),
		// getString("_UI_PropertyDescriptor_description",
		// "_UI_AssociationInstance_aEndAggregation_feature",
		// "_UI_AssociationInstance_type"),
		// InstancediagramPackage.Literals.ASSOCIATION_INSTANCE__AEND_AGGREGATION,
		// true,
		// false,
		// false,
		// ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
		// null,
		// null));
	}

	/**
	 * This adds a property descriptor for the ZEnd feature. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected void addZEndPropertyDescriptor(Object object) {
		// itemPropertyDescriptors.add
		// (createItemPropertyDescriptor
		// (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
		// getResourceLocator(),
		// getString("_UI_AssociationInstance_zEnd_feature"),
		// getString("_UI_PropertyDescriptor_description",
		// "_UI_AssociationInstance_zEnd_feature",
		// "_UI_AssociationInstance_type"),
		// InstancediagramPackage.Literals.ASSOCIATION_INSTANCE__ZEND,
		// true,
		// false,
		// true,
		// null,
		// null,
		// null));
	}

	/**
	 * This adds a property descriptor for the ZEnd Name feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addZEndNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory) adapterFactory)
								.getRootAdapterFactory(),
						getResourceLocator(),
						getString("_UI_AssociationInstance_zEndName_feature"),
						getString("_UI_PropertyDescriptor_description",
								"_UI_AssociationInstance_zEndName_feature",
								"_UI_AssociationInstance_type"),
						InstancediagramPackage.Literals.ASSOCIATION_INSTANCE__ZEND_NAME,
						true, false, false,
						ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the ZEnd Multiplicity Lower Bound
	 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected void addZEndMultiplicityLowerBoundPropertyDescriptor(Object object) {
		// itemPropertyDescriptors.add
		// (createItemPropertyDescriptor
		// (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
		// getResourceLocator(),
		// getString("_UI_AssociationInstance_zEndMultiplicityLowerBound_feature"),
		// getString("_UI_PropertyDescriptor_description",
		// "_UI_AssociationInstance_zEndMultiplicityLowerBound_feature",
		// "_UI_AssociationInstance_type"),
		// InstancediagramPackage.Literals.ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_LOWER_BOUND,
		// true,
		// false,
		// false,
		// ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
		// null,
		// null));
	}

	/**
	 * This adds a property descriptor for the ZEnd Multiplicity Upper Bound
	 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected void addZEndMultiplicityUpperBoundPropertyDescriptor(Object object) {
		// itemPropertyDescriptors.add
		// (createItemPropertyDescriptor
		// (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
		// getResourceLocator(),
		// getString("_UI_AssociationInstance_zEndMultiplicityUpperBound_feature"),
		// getString("_UI_PropertyDescriptor_description",
		// "_UI_AssociationInstance_zEndMultiplicityUpperBound_feature",
		// "_UI_AssociationInstance_type"),
		// InstancediagramPackage.Literals.ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_UPPER_BOUND,
		// true,
		// false,
		// false,
		// ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
		// null,
		// null));
	}

	/**
	 * This adds a property descriptor for the ZEnd Is Navigable feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected void addZEndIsNavigablePropertyDescriptor(Object object) {
		// itemPropertyDescriptors.add
		// (createItemPropertyDescriptor
		// (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
		// getResourceLocator(),
		// getString("_UI_AssociationInstance_zEndIsNavigable_feature"),
		// getString("_UI_PropertyDescriptor_description",
		// "_UI_AssociationInstance_zEndIsNavigable_feature",
		// "_UI_AssociationInstance_type"),
		// InstancediagramPackage.Literals.ASSOCIATION_INSTANCE__ZEND_IS_NAVIGABLE,
		// true,
		// false,
		// false,
		// ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
		// null,
		// null));
	}

	/**
	 * This adds a property descriptor for the ZEnd Is Ordered feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected void addZEndIsOrderedPropertyDescriptor(Object object) {
		// itemPropertyDescriptors.add
		// (createItemPropertyDescriptor
		// (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
		// getResourceLocator(),
		// getString("_UI_AssociationInstance_zEndIsOrdered_feature"),
		// getString("_UI_PropertyDescriptor_description",
		// "_UI_AssociationInstance_zEndIsOrdered_feature",
		// "_UI_AssociationInstance_type"),
		// InstancediagramPackage.Literals.ASSOCIATION_INSTANCE__ZEND_IS_ORDERED,
		// true,
		// false,
		// false,
		// ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
		// null,
		// null));
	}

	/**
	 * This adds a property descriptor for the ZEnd Is Changeable feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected void addZEndIsChangeablePropertyDescriptor(Object object) {
		// itemPropertyDescriptors.add
		// (createItemPropertyDescriptor
		// (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
		// getResourceLocator(),
		// getString("_UI_AssociationInstance_zEndIsChangeable_feature"),
		// getString("_UI_PropertyDescriptor_description",
		// "_UI_AssociationInstance_zEndIsChangeable_feature",
		// "_UI_AssociationInstance_type"),
		// InstancediagramPackage.Literals.ASSOCIATION_INSTANCE__ZEND_IS_CHANGEABLE,
		// true,
		// false,
		// false,
		// ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
		// null,
		// null));
	}

	/**
	 * This adds a property descriptor for the ZEnd Aggregation feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected void addZEndAggregationPropertyDescriptor(Object object) {
		// itemPropertyDescriptors.add
		// (createItemPropertyDescriptor
		// (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
		// getResourceLocator(),
		// getString("_UI_AssociationInstance_zEndAggregation_feature"),
		// getString("_UI_PropertyDescriptor_description",
		// "_UI_AssociationInstance_zEndAggregation_feature",
		// "_UI_AssociationInstance_type"),
		// InstancediagramPackage.Literals.ASSOCIATION_INSTANCE__ZEND_AGGREGATION,
		// true,
		// false,
		// false,
		// ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
		// null,
		// null));
	}

	/**
	 * This adds a property descriptor for the Reference Name feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected void addReferenceNamePropertyDescriptor(Object object) {
		// itemPropertyDescriptors.add
		// (createItemPropertyDescriptor
		// (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
		// getResourceLocator(),
		// getString("_UI_AssociationInstance_referenceName_feature"),
		// getString("_UI_PropertyDescriptor_description",
		// "_UI_AssociationInstance_referenceName_feature",
		// "_UI_AssociationInstance_type"),
		// InstancediagramPackage.Literals.ASSOCIATION_INSTANCE__REFERENCE_NAME,
		// true,
		// false,
		// false,
		// ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
		// null,
		// null));
	}

	/**
	 * This returns AssociationInstance.gif. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage(
				"full/obj16/AssociationInstance"));
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getText(Object object) {
		String label = ((AssociationInstance) object).getName();
		return label == null || label.length() == 0 ? getString("_UI_AssociationInstance_type")
				: getString("_UI_AssociationInstance_type") + " " + label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to
	 * update any cached children and by creating a viewer notification, which
	 * it passes to {@link #fireNotifyChanged}. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(AssociationInstance.class)) {
		case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_NAME:
		case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_LOWER_BOUND:
		case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_UPPER_BOUND:
		case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_NAVIGABLE:
		case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_ORDERED:
		case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_CHANGEABLE:
		case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_AGGREGATION:
		case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_NAME:
		case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_LOWER_BOUND:
		case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_UPPER_BOUND:
		case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_NAVIGABLE:
		case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_ORDERED:
		case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_CHANGEABLE:
		case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_AGGREGATION:
		case InstancediagramPackage.ASSOCIATION_INSTANCE__REFERENCE_NAME:
			fireNotifyChanged(new ViewerNotification(notification, notification
					.getNotifier(), false, true));
			return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds to the collection of
	 * {@link org.eclipse.emf.edit.command.CommandParameter}s describing all of
	 * the children that can be created under this object. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void collectNewChildDescriptors(Collection newChildDescriptors,
			Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);
	}

	/**
	 * Return the resource locator for this item provider's resources. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ResourceLocator getResourceLocator() {
		return InstanceEditPlugin.INSTANCE;
	}

}
