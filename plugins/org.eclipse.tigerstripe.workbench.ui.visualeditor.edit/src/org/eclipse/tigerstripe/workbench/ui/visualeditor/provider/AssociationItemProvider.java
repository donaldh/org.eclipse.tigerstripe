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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.provider;

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
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * This is the item provider adapter for a
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association} object.
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class AssociationItemProvider extends QualifiedNamedElementItemProvider
		implements IEditingDomainItemProvider, IStructuredItemContentProvider,
		ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AssociationItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public List getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addAEndPropertyDescriptor(object);
			addAEndNamePropertyDescriptor(object);
			addAEndMultiplicityPropertyDescriptor(object);
			addAEndIsNavigablePropertyDescriptor(object);
			addAEndIsOrderedPropertyDescriptor(object);
			addAEndIsUniquePropertyDescriptor(object);
			addAEndIsChangeablePropertyDescriptor(object);
			addAEndAggregationPropertyDescriptor(object);
			addZEndPropertyDescriptor(object);
			addZEndNamePropertyDescriptor(object);
			addZEndMultiplicityPropertyDescriptor(object);
			addZEndIsNavigablePropertyDescriptor(object);
			addZEndIsOrderedPropertyDescriptor(object);
			addZEndIsUniquePropertyDescriptor(object);
			addZEndIsChangeablePropertyDescriptor(object);
			addZEndAggregationPropertyDescriptor(object);
			addAEndVisibilityPropertyDescriptor(object);
			addZEndVisibilityPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the AEnd feature. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addAEndPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory) adapterFactory)
								.getRootAdapterFactory(), getResourceLocator(),
						getString("_UI_Association_aEnd_feature"), getString(
								"_UI_PropertyDescriptor_description",
								"_UI_Association_aEnd_feature",
								"_UI_Association_type"),
						VisualeditorPackage.Literals.ASSOCIATION__AEND, true,
						false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the AEnd Name feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addAEndNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Association_aEndName_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_Association_aEndName_feature",
						"_UI_Association_type"),
				VisualeditorPackage.Literals.ASSOCIATION__AEND_NAME, true,
				false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null,
				null));
	}

	/**
	 * This adds a property descriptor for the AEnd Multiplicity feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addAEndMultiplicityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Association_aEndMultiplicity_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_Association_aEndMultiplicity_feature",
						"_UI_Association_type"),
				VisualeditorPackage.Literals.ASSOCIATION__AEND_MULTIPLICITY,
				true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				null, null));
	}

	/**
	 * This adds a property descriptor for the AEnd Is Navigable feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addAEndIsNavigablePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Association_aEndIsNavigable_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_Association_aEndIsNavigable_feature",
						"_UI_Association_type"),
				VisualeditorPackage.Literals.ASSOCIATION__AEND_IS_NAVIGABLE,
				true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				null, null));
	}

	/**
	 * This adds a property descriptor for the AEnd Is Ordered feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addAEndIsOrderedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Association_aEndIsOrdered_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_Association_aEndIsOrdered_feature",
						"_UI_Association_type"),
				VisualeditorPackage.Literals.ASSOCIATION__AEND_IS_ORDERED,
				true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				null, null));
	}

	/**
	 * This adds a property descriptor for the AEnd Is Unique feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addAEndIsUniquePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Association_aEndIsUnique_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_Association_aEndIsUnique_feature",
						"_UI_Association_type"),
				VisualeditorPackage.Literals.ASSOCIATION__AEND_IS_UNIQUE, true,
				false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null,
				null));
	}

	/**
	 * This adds a property descriptor for the AEnd Is Changeable feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addAEndIsChangeablePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Association_aEndIsChangeable_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_Association_aEndIsChangeable_feature",
						"_UI_Association_type"),
				VisualeditorPackage.Literals.ASSOCIATION__AEND_IS_CHANGEABLE,
				true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				null, null));
	}

	/**
	 * This adds a property descriptor for the AEnd Aggregation feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addAEndAggregationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Association_aEndAggregation_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_Association_aEndAggregation_feature",
						"_UI_Association_type"),
				VisualeditorPackage.Literals.ASSOCIATION__AEND_AGGREGATION,
				true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				null, null));
	}

	/**
	 * This adds a property descriptor for the ZEnd feature. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addZEndPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory) adapterFactory)
								.getRootAdapterFactory(), getResourceLocator(),
						getString("_UI_Association_zEnd_feature"), getString(
								"_UI_PropertyDescriptor_description",
								"_UI_Association_zEnd_feature",
								"_UI_Association_type"),
						VisualeditorPackage.Literals.ASSOCIATION__ZEND, true,
						false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the ZEnd Name feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addZEndNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Association_zEndName_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_Association_zEndName_feature",
						"_UI_Association_type"),
				VisualeditorPackage.Literals.ASSOCIATION__ZEND_NAME, true,
				false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null,
				null));
	}

	/**
	 * This adds a property descriptor for the ZEnd Multiplicity feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addZEndMultiplicityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Association_zEndMultiplicity_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_Association_zEndMultiplicity_feature",
						"_UI_Association_type"),
				VisualeditorPackage.Literals.ASSOCIATION__ZEND_MULTIPLICITY,
				true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				null, null));
	}

	/**
	 * This adds a property descriptor for the ZEnd Is Navigable feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addZEndIsNavigablePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Association_zEndIsNavigable_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_Association_zEndIsNavigable_feature",
						"_UI_Association_type"),
				VisualeditorPackage.Literals.ASSOCIATION__ZEND_IS_NAVIGABLE,
				true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				null, null));
	}

	/**
	 * This adds a property descriptor for the ZEnd Is Ordered feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addZEndIsOrderedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Association_zEndIsOrdered_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_Association_zEndIsOrdered_feature",
						"_UI_Association_type"),
				VisualeditorPackage.Literals.ASSOCIATION__ZEND_IS_ORDERED,
				true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				null, null));
	}

	/**
	 * This adds a property descriptor for the ZEnd Is Unique feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addZEndIsUniquePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Association_zEndIsUnique_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_Association_zEndIsUnique_feature",
						"_UI_Association_type"),
				VisualeditorPackage.Literals.ASSOCIATION__ZEND_IS_UNIQUE, true,
				false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null,
				null));
	}

	/**
	 * This adds a property descriptor for the ZEnd Is Changeable feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addZEndIsChangeablePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Association_zEndIsChangeable_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_Association_zEndIsChangeable_feature",
						"_UI_Association_type"),
				VisualeditorPackage.Literals.ASSOCIATION__ZEND_IS_CHANGEABLE,
				true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				null, null));
	}

	/**
	 * This adds a property descriptor for the ZEnd Aggregation feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addZEndAggregationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Association_zEndAggregation_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_Association_zEndAggregation_feature",
						"_UI_Association_type"),
				VisualeditorPackage.Literals.ASSOCIATION__ZEND_AGGREGATION,
				true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				null, null));
	}

	/**
	 * This adds a property descriptor for the AEnd Visibility feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addAEndVisibilityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Association_aEndVisibility_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_Association_aEndVisibility_feature",
						"_UI_Association_type"),
				VisualeditorPackage.Literals.ASSOCIATION__AEND_VISIBILITY,
				true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				null, null));
	}

	/**
	 * This adds a property descriptor for the ZEnd Visibility feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addZEndVisibilityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Association_zEndVisibility_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_Association_zEndVisibility_feature",
						"_UI_Association_type"),
				VisualeditorPackage.Literals.ASSOCIATION__ZEND_VISIBILITY,
				true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				null, null));
	}

	/**
	 * This returns Association.gif. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, Images.get(Images.ASSOCIATION_ICON));
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((Association) object).getName();
		return label == null || label.length() == 0 ? getString("_UI_Association_type")
				: getString("_UI_Association_type") + " " + label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to
	 * update any cached children and by creating a viewer notification, which
	 * it passes to {@link #fireNotifyChanged}. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(Association.class)) {
		case VisualeditorPackage.ASSOCIATION__AEND_NAME:
		case VisualeditorPackage.ASSOCIATION__AEND_MULTIPLICITY:
		case VisualeditorPackage.ASSOCIATION__AEND_IS_NAVIGABLE:
		case VisualeditorPackage.ASSOCIATION__AEND_IS_ORDERED:
		case VisualeditorPackage.ASSOCIATION__AEND_IS_UNIQUE:
		case VisualeditorPackage.ASSOCIATION__AEND_IS_CHANGEABLE:
		case VisualeditorPackage.ASSOCIATION__AEND_AGGREGATION:
		case VisualeditorPackage.ASSOCIATION__ZEND_NAME:
		case VisualeditorPackage.ASSOCIATION__ZEND_MULTIPLICITY:
		case VisualeditorPackage.ASSOCIATION__ZEND_IS_NAVIGABLE:
		case VisualeditorPackage.ASSOCIATION__ZEND_IS_ORDERED:
		case VisualeditorPackage.ASSOCIATION__ZEND_IS_UNIQUE:
		case VisualeditorPackage.ASSOCIATION__ZEND_IS_CHANGEABLE:
		case VisualeditorPackage.ASSOCIATION__ZEND_AGGREGATION:
		case VisualeditorPackage.ASSOCIATION__AEND_VISIBILITY:
		case VisualeditorPackage.ASSOCIATION__ZEND_VISIBILITY:
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
	@Override
	protected void collectNewChildDescriptors(Collection newChildDescriptors,
			Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);
	}

	/**
	 * Return the resource locator for this item provider's resources. <!--
	 * begin-user-doc --> Using a single bundle for all icons:
	 * org.eclipse.tigerstripe.workbench.ui.resources <!-- end-user-doc -->
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return super.getResourceLocator();
	}

}
