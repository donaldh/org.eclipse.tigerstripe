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
import org.eclipse.tigerstripe.workbench.ui.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * This is the item provider adapter for a
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency} object.
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class DependencyItemProvider extends QualifiedNamedElementItemProvider
		implements IEditingDomainItemProvider, IStructuredItemContentProvider,
		ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DependencyItemProvider(AdapterFactory adapterFactory) {
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
			addAEndMultiplicityPropertyDescriptor(object);
			addAEndIsNavigablePropertyDescriptor(object);
			addZEndPropertyDescriptor(object);
			addZEndMultiplicityPropertyDescriptor(object);
			addZEndIsNavigablePropertyDescriptor(object);
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
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Dependency_aEnd_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_Dependency_aEnd_feature", "_UI_Dependency_type"),
				VisualeditorPackage.Literals.DEPENDENCY__AEND, true, false,
				true, null, null, null));
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
				getString("_UI_Dependency_aEndMultiplicity_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_Dependency_aEndMultiplicity_feature",
						"_UI_Dependency_type"),
				VisualeditorPackage.Literals.DEPENDENCY__AEND_MULTIPLICITY,
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
				getString("_UI_Dependency_aEndIsNavigable_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_Dependency_aEndIsNavigable_feature",
						"_UI_Dependency_type"),
				VisualeditorPackage.Literals.DEPENDENCY__AEND_IS_NAVIGABLE,
				true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				null, null));
	}

	/**
	 * This adds a property descriptor for the ZEnd feature. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addZEndPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Dependency_zEnd_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_Dependency_zEnd_feature", "_UI_Dependency_type"),
				VisualeditorPackage.Literals.DEPENDENCY__ZEND, true, false,
				true, null, null, null));
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
				getString("_UI_Dependency_zEndMultiplicity_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_Dependency_zEndMultiplicity_feature",
						"_UI_Dependency_type"),
				VisualeditorPackage.Literals.DEPENDENCY__ZEND_MULTIPLICITY,
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
				getString("_UI_Dependency_zEndIsNavigable_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_Dependency_zEndIsNavigable_feature",
						"_UI_Dependency_type"),
				VisualeditorPackage.Literals.DEPENDENCY__ZEND_IS_NAVIGABLE,
				true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				null, null));
	}

	/**
	 * This returns Dependency.gif. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, Images.getInstance().getObject(
				Images.ASSOCIATIONARROW_ICON));
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((Dependency) object).getName();
		return label == null || label.length() == 0 ? getString("_UI_Dependency_type")
				: getString("_UI_Dependency_type") + " " + label;
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

		switch (notification.getFeatureID(Dependency.class)) {
		case VisualeditorPackage.DEPENDENCY__AEND_MULTIPLICITY:
		case VisualeditorPackage.DEPENDENCY__AEND_IS_NAVIGABLE:
		case VisualeditorPackage.DEPENDENCY__ZEND_MULTIPLICITY:
		case VisualeditorPackage.DEPENDENCY__ZEND_IS_NAVIGABLE:
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
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return TigerstripeEditPlugin.INSTANCE;
	}

}
