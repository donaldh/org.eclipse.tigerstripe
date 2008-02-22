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
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * This is the item provider adapter for a
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact}
 * object. <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class SessionFacadeArtifactItemProvider extends
		AbstractArtifactItemProvider implements IEditingDomainItemProvider,
		IStructuredItemContentProvider, ITreeItemContentProvider,
		IItemLabelProvider, IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SessionFacadeArtifactItemProvider(AdapterFactory adapterFactory) {
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

			addManagedEntitiesPropertyDescriptor(object);
			addEmittedNotificationsPropertyDescriptor(object);
			addNamedQueriesPropertyDescriptor(object);
			addExposedProceduresPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Managed Entities feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addManagedEntitiesPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory) adapterFactory)
								.getRootAdapterFactory(),
						getResourceLocator(),
						getString("_UI_SessionFacadeArtifact_managedEntities_feature"),
						getString(
								"_UI_PropertyDescriptor_description",
								"_UI_SessionFacadeArtifact_managedEntities_feature",
								"_UI_SessionFacadeArtifact_type"),
						VisualeditorPackage.Literals.SESSION_FACADE_ARTIFACT__MANAGED_ENTITIES,
						true, false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the Emitted Notifications feature.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addEmittedNotificationsPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory) adapterFactory)
								.getRootAdapterFactory(),
						getResourceLocator(),
						getString("_UI_SessionFacadeArtifact_emittedNotifications_feature"),
						getString(
								"_UI_PropertyDescriptor_description",
								"_UI_SessionFacadeArtifact_emittedNotifications_feature",
								"_UI_SessionFacadeArtifact_type"),
						VisualeditorPackage.Literals.SESSION_FACADE_ARTIFACT__EMITTED_NOTIFICATIONS,
						true, false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the Named Queries feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addNamedQueriesPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory) adapterFactory)
								.getRootAdapterFactory(),
						getResourceLocator(),
						getString("_UI_SessionFacadeArtifact_namedQueries_feature"),
						getString(
								"_UI_PropertyDescriptor_description",
								"_UI_SessionFacadeArtifact_namedQueries_feature",
								"_UI_SessionFacadeArtifact_type"),
						VisualeditorPackage.Literals.SESSION_FACADE_ARTIFACT__NAMED_QUERIES,
						true, false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the Exposed Procedures feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addExposedProceduresPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory) adapterFactory)
								.getRootAdapterFactory(),
						getResourceLocator(),
						getString("_UI_SessionFacadeArtifact_exposedProcedures_feature"),
						getString(
								"_UI_PropertyDescriptor_description",
								"_UI_SessionFacadeArtifact_exposedProcedures_feature",
								"_UI_SessionFacadeArtifact_type"),
						VisualeditorPackage.Literals.SESSION_FACADE_ARTIFACT__EXPOSED_PROCEDURES,
						true, false, true, null, null, null));
	}

	/**
	 * This returns SessionFacadeArtifact.gif. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public Object getImage(Object object) {
		Object icon = getIcon(object, false);
		if (icon == null) // this is necessary for the floating palette
			icon = Images.get(Images.SESSION_ICON);
		return overlayImage(object, icon);
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((SessionFacadeArtifact) object).getName();
		return label == null || label.length() == 0 ? getString("_UI_SessionFacadeArtifact_type")
				: getString("_UI_SessionFacadeArtifact_type") + " " + label;
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
