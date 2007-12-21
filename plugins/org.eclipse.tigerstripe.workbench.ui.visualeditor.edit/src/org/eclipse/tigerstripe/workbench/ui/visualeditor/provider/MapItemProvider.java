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
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * This is the item provider adapter for a
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Map} object. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class MapItemProvider extends ItemProviderAdapter implements
		IEditingDomainItemProvider, IStructuredItemContentProvider,
		ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MapItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public List getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addBasePackagePropertyDescriptor(object);
			// not to be seen by users
			// addPropertiesPropertyDescriptor(object);

		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Base Package feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addBasePackagePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Map_basePackage_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_Map_basePackage_feature", "_UI_Map_type"),
				VisualeditorPackage.Literals.MAP__BASE_PACKAGE, true, false,
				false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
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
	@Override
	public Collection getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(VisualeditorPackage.Literals.MAP__ARTIFACTS);
			childrenFeatures
					.add(VisualeditorPackage.Literals.MAP__ASSOCIATIONS);
			childrenFeatures
					.add(VisualeditorPackage.Literals.MAP__DEPENDENCIES);
			childrenFeatures.add(VisualeditorPackage.Literals.MAP__PROPERTIES);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper
		// feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns Map.gif. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage(
				"full/obj16/Map"));
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((Map) object).getBasePackage();
		return label == null || label.length() == 0 ? getString("_UI_Map_type")
				: getString("_UI_Map_type") + " " + label;
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

		switch (notification.getFeatureID(Map.class)) {
		case VisualeditorPackage.MAP__BASE_PACKAGE:
			fireNotifyChanged(new ViewerNotification(notification, notification
					.getNotifier(), false, true));
			return;
		case VisualeditorPackage.MAP__ARTIFACTS:
		case VisualeditorPackage.MAP__ASSOCIATIONS:
		case VisualeditorPackage.MAP__DEPENDENCIES:
		case VisualeditorPackage.MAP__PROPERTIES:
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
	@Override
	protected void collectNewChildDescriptors(Collection newChildDescriptors,
			Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add(createChildParameter(
				VisualeditorPackage.Literals.MAP__ARTIFACTS,
				VisualeditorFactory.eINSTANCE.createAbstractArtifact()));

		newChildDescriptors.add(createChildParameter(
				VisualeditorPackage.Literals.MAP__ARTIFACTS,
				VisualeditorFactory.eINSTANCE.createManagedEntityArtifact()));

		newChildDescriptors.add(createChildParameter(
				VisualeditorPackage.Literals.MAP__ARTIFACTS,
				VisualeditorFactory.eINSTANCE.createDatatypeArtifact()));

		newChildDescriptors.add(createChildParameter(
				VisualeditorPackage.Literals.MAP__ARTIFACTS,
				VisualeditorFactory.eINSTANCE.createNotificationArtifact()));

		newChildDescriptors.add(createChildParameter(
				VisualeditorPackage.Literals.MAP__ARTIFACTS,
				VisualeditorFactory.eINSTANCE.createNamedQueryArtifact()));

		newChildDescriptors.add(createChildParameter(
				VisualeditorPackage.Literals.MAP__ARTIFACTS,
				VisualeditorFactory.eINSTANCE.createEnumeration()));

		newChildDescriptors.add(createChildParameter(
				VisualeditorPackage.Literals.MAP__ARTIFACTS,
				VisualeditorFactory.eINSTANCE.createUpdateProcedureArtifact()));

		newChildDescriptors.add(createChildParameter(
				VisualeditorPackage.Literals.MAP__ARTIFACTS,
				VisualeditorFactory.eINSTANCE.createExceptionArtifact()));

		newChildDescriptors.add(createChildParameter(
				VisualeditorPackage.Literals.MAP__ARTIFACTS,
				VisualeditorFactory.eINSTANCE.createSessionFacadeArtifact()));

		newChildDescriptors.add(createChildParameter(
				VisualeditorPackage.Literals.MAP__ARTIFACTS,
				VisualeditorFactory.eINSTANCE.createAssociationClassClass()));

		newChildDescriptors.add(createChildParameter(
				VisualeditorPackage.Literals.MAP__ASSOCIATIONS,
				VisualeditorFactory.eINSTANCE.createAssociation()));

		newChildDescriptors.add(createChildParameter(
				VisualeditorPackage.Literals.MAP__ASSOCIATIONS,
				VisualeditorFactory.eINSTANCE.createAssociationClass()));

		newChildDescriptors.add(createChildParameter(
				VisualeditorPackage.Literals.MAP__DEPENDENCIES,
				VisualeditorFactory.eINSTANCE.createDependency()));

		newChildDescriptors.add(createChildParameter(
				VisualeditorPackage.Literals.MAP__PROPERTIES,
				VisualeditorFactory.eINSTANCE.createDiagramProperty()));
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
