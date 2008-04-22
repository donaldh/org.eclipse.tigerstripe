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
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.AbstractArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * This is the item provider adapter for a
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact}
 * object. <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class AbstractArtifactItemProvider extends
		QualifiedNamedElementItemProvider implements
		IEditingDomainItemProvider, IStructuredItemContentProvider,
		ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AbstractArtifactItemProvider(AdapterFactory adapterFactory) {
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

			addExtendsPropertyDescriptor(object);
			addImplementsPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Extends feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addExtendsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_AbstractArtifact_extends_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_AbstractArtifact_extends_feature",
						"_UI_AbstractArtifact_type"),
				VisualeditorPackage.Literals.ABSTRACT_ARTIFACT__EXTENDS, true,
				false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the Implements feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addImplementsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_AbstractArtifact_implements_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_AbstractArtifact_implements_feature",
						"_UI_AbstractArtifact_type"),
				VisualeditorPackage.Literals.ABSTRACT_ARTIFACT__IMPLEMENTS,
				true, false, true, null, null, null));
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
			childrenFeatures
					.add(VisualeditorPackage.Literals.ABSTRACT_ARTIFACT__ATTRIBUTES);
			childrenFeatures
					.add(VisualeditorPackage.Literals.ABSTRACT_ARTIFACT__LITERALS);
			childrenFeatures
					.add(VisualeditorPackage.Literals.ABSTRACT_ARTIFACT__METHODS);
			childrenFeatures
					.add(VisualeditorPackage.Literals.ABSTRACT_ARTIFACT__REFERENCES);
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
	 * This returns the extends arrow icon... <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, Images.get(Images.EXTENDSARROW_ICON));
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((AbstractArtifact) object).getName();
		return label == null || label.length() == 0 ? getString("_UI_AbstractArtifact_type")
				: getString("_UI_AbstractArtifact_type") + " " + label;
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

		switch (notification.getFeatureID(AbstractArtifact.class)) {
		case VisualeditorPackage.ABSTRACT_ARTIFACT__ATTRIBUTES:
		case VisualeditorPackage.ABSTRACT_ARTIFACT__LITERALS:
		case VisualeditorPackage.ABSTRACT_ARTIFACT__METHODS:
		case VisualeditorPackage.ABSTRACT_ARTIFACT__REFERENCES:
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
				VisualeditorPackage.Literals.ABSTRACT_ARTIFACT__ATTRIBUTES,
				VisualeditorFactory.eINSTANCE.createAttribute()));

		newChildDescriptors.add(createChildParameter(
				VisualeditorPackage.Literals.ABSTRACT_ARTIFACT__LITERALS,
				VisualeditorFactory.eINSTANCE.createLiteral()));

		newChildDescriptors.add(createChildParameter(
				VisualeditorPackage.Literals.ABSTRACT_ARTIFACT__METHODS,
				VisualeditorFactory.eINSTANCE.createMethod()));

		newChildDescriptors.add(createChildParameter(
				VisualeditorPackage.Literals.ABSTRACT_ARTIFACT__REFERENCES,
				VisualeditorFactory.eINSTANCE.createReference()));
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

	protected Image getIcon(Object object) {
		return getIcon(object, true);
	}

	protected Image getIcon(Object object, boolean transparencySupported) {
		if (object instanceof AbstractArtifact) {
			AbstractArtifact artifact = (AbstractArtifact) object;
			try {
				IAbstractArtifact correspondingIArtifact = artifact
						.getCorrespondingIArtifact();
				if (correspondingIArtifact != null) {
					AbstractArtifactLabelProvider provider = new AbstractArtifactLabelProvider();
					return provider.getImage(correspondingIArtifact,
							transparencySupported);
				}
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeException detected", e);
			}
		}
		return null;
	}
}
