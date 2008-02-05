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
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramFactory;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage;

/**
 * This is the item provider adapter for a
 * {@link org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance}
 * object. <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class ClassInstanceItemProvider extends InstanceItemProvider implements
		IEditingDomainItemProvider, IStructuredItemContentProvider,
		ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ClassInstanceItemProvider(AdapterFactory adapterFactory) {
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

			addAssociationsPropertyDescriptor(object);
			addAssociationClassInstancePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Associations feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected void addAssociationsPropertyDescriptor(Object object) {
		// itemPropertyDescriptors.add
		// (createItemPropertyDescriptor
		// (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
		// getResourceLocator(),
		// getString("_UI_ClassInstance_associations_feature"),
		// getString("_UI_PropertyDescriptor_description",
		// "_UI_ClassInstance_associations_feature", "_UI_ClassInstance_type"),
		// InstancediagramPackage.Literals.CLASS_INSTANCE__ASSOCIATIONS,
		// true,
		// false,
		// true,
		// null,
		// null,
		// null));
	}

	/**
	 * This adds a property descriptor for the Association Class Instance
	 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected void addAssociationClassInstancePropertyDescriptor(Object object) {
		// itemPropertyDescriptors.add
		// (createItemPropertyDescriptor
		// (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
		// getResourceLocator(),
		// getString("_UI_ClassInstance_associationClassInstance_feature"),
		// getString("_UI_PropertyDescriptor_description",
		// "_UI_ClassInstance_associationClassInstance_feature",
		// "_UI_ClassInstance_type"),
		// InstancediagramPackage.Literals.CLASS_INSTANCE__ASSOCIATION_CLASS_INSTANCE,
		// true,
		// false,
		// false,
		// ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
		// null,
		// null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to
	 * deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand},
	 * {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in
	 * {@link #createCommand}. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Collection getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures
					.add(InstancediagramPackage.Literals.CLASS_INSTANCE__VARIABLES);
		}
		return childrenFeatures;
	}

	/**
	 * This returns ClassInstance.gif. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated NOT
	 */
	public Object getImage(Object object) {
		Object icon = getIcon(object, false);
		return overlayImage(object, icon);
		// return overlayImage(object,
		// getResourceLocator().getImage("full/obj16/ClassInstance"));
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getText(Object object) {
		String label = ((ClassInstance) object).getName();
		return label == null || label.length() == 0 ? getString("_UI_ClassInstance_type")
				: getString("_UI_ClassInstance_type") + " " + label;
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

		switch (notification.getFeatureID(ClassInstance.class)) {
		case InstancediagramPackage.CLASS_INSTANCE__ASSOCIATION_CLASS_INSTANCE:
			fireNotifyChanged(new ViewerNotification(notification, notification
					.getNotifier(), false, true));
			return;
		case InstancediagramPackage.CLASS_INSTANCE__VARIABLES:
			fireNotifyChanged(new ViewerNotification(notification, notification
					.getNotifier(), true, false));
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

		newChildDescriptors.add(createChildParameter(
				InstancediagramPackage.Literals.CLASS_INSTANCE__VARIABLES,
				InstancediagramFactory.eINSTANCE.createVariable()));
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
