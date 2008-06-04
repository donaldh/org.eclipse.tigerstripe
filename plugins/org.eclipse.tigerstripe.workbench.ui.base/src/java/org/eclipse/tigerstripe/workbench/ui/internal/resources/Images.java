/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.resources;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.metamodel.impl.IAssociationArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IAssociationClassArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IDatatypeArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IDependencyArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IEnumArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IEventArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IExceptionArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IFieldImpl;
import org.eclipse.tigerstripe.metamodel.impl.ILiteralImpl;
import org.eclipse.tigerstripe.metamodel.impl.IManagedEntityArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IMethodImpl;
import org.eclipse.tigerstripe.metamodel.impl.IPrimitiveTypeImpl;
import org.eclipse.tigerstripe.metamodel.impl.IQueryArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IUpdateProcedureArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

/**
 * Access to all images for the UI.
 * 
 * @author erdillon
 * 
 */
public class Images {

	private static URL iconsBaseURL = null;

	private Images() {
	}

	private static ImageRegistry fgImageRegistry = null;

	private static HashMap<String, ImageDescriptor> fgAvoidSWTErrorMap = null;

	static {
		iconsBaseURL = EclipsePlugin.getDefault().getBundle().getEntry(
				"src/icons/");
		if (fgAvoidSWTErrorMap == null)
			fgAvoidSWTErrorMap = new HashMap<String, ImageDescriptor>();
	}

	public static Image get(String key) {
		return getImageRegistry().get(key);
	}

	public static ImageDescriptor getDescriptor(String key) {
		return fgAvoidSWTErrorMap.get(key);
	}

	/*
	 * Helper method to access the image registry from the JavaPlugin class.
	 */
	/* package */static ImageRegistry getImageRegistry() {
		if (fgImageRegistry == null) {
			fgImageRegistry = new ImageRegistry();
			for (String key : fgAvoidSWTErrorMap.keySet()) {
				fgImageRegistry.put(key, fgAvoidSWTErrorMap.get(key));
			}
		}
		return fgImageRegistry;
	}

	private static URL localIcon(String name) {
		try {
			return new URL(iconsBaseURL, name);
		} catch (MalformedURLException e) {
			// will never happen
			return iconsBaseURL;
		}
	}

	private static ImageDescriptor createManaged(String key, URL iconURL) {
		ImageDescriptor result = ImageDescriptor.createFromURL(iconURL);
		if (fgAvoidSWTErrorMap == null) {
			fgAvoidSWTErrorMap = new HashMap<String, ImageDescriptor>();
		}
		fgAvoidSWTErrorMap.put(key, result);
		if (fgImageRegistry != null) {
			EclipsePlugin.logErrorMessage("Image registry already defined"); //$NON-NLS-1$
		}
		return result;
	}

	// ===========================================================
	// ===========================================================
	// ===========================================================

	private static final String NEW_ = "NEW_";

	private static final String GS_ = "GS_";

	public static final String ENTITY_ICON = IManagedEntityArtifactImpl.class
			.getName();
	public static final String ENTITY_ICON_NEW = ENTITY_ICON + NEW_;
	public static final String ENTITY_ICON_GS = ENTITY_ICON + GS_;

	public static final String DATATYPE_ICON = IDatatypeArtifactImpl.class
			.getName();
	public static final String DATATYPE_ICON_NEW = DATATYPE_ICON + NEW_;
	public static final String DATATYPE_ICON_GS = DATATYPE_ICON + GS_;

	public static final String PRIMITIVE_ICON = IPrimitiveTypeImpl.class
			.getName();
	public static final String PRIMITIVE_ICON_NEW = PRIMITIVE_ICON + NEW_;
	public static final String PRIMITIVE_ICON_GS = PRIMITIVE_ICON + GS_;

	public static final String ASSOCIATION_ICON = IAssociationArtifactImpl.class
			.getName();
	public static final String ASSOCIATION_ICON_NEW = ASSOCIATION_ICON + NEW_;
	public static final String ASSOCIATION_ICON_GS = ASSOCIATION_ICON + GS_;

	public static final String DEPENDENCY_ICON = IDependencyArtifactImpl.class
			.getName();
	public static final String DEPENDENCY_ICON_NEW = DEPENDENCY_ICON + NEW_;
	public static final String DEPENDENCY_ICON_GS = DEPENDENCY_ICON + GS_;

	public static final String ASSOCIATIONCLASS_ICON = IAssociationClassArtifactImpl.class
			.getName();
	public static final String ASSOCIATIONCLASS_ICON_NEW = ASSOCIATIONCLASS_ICON
			+ NEW_;
	public static final String ASSOCIATIONCLASS_ICON_GS = ASSOCIATIONCLASS_ICON
			+ GS_;

	public static final String QUERY_ICON = IQueryArtifactImpl.class.getName();
	public static final String QUERY_ICON_NEW = QUERY_ICON + NEW_;
	public static final String QUERY_ICON_GS = QUERY_ICON + GS_;

	public static final String UPDATEPROC_ICON = IUpdateProcedureArtifactImpl.class
			.getName();
	public static final String UPDATEPROC_ICON_NEW = UPDATEPROC_ICON + NEW_;
	public static final String UPDATEPROC_ICON_GS = UPDATEPROC_ICON + GS_;

	public static final String NOTIFICATION_ICON = IEventArtifactImpl.class
			.getName();
	public static final String NOTIFICATION_ICON_NEW = NOTIFICATION_ICON + NEW_;
	public static final String NOTIFICATION_ICON_GS = NOTIFICATION_ICON + GS_;

	public static final String ENUM_ICON = IEnumArtifactImpl.class.getName();
	public static final String ENUM_ICON_NEW = ENUM_ICON + NEW_;
	public static final String ENUM_ICON_GS = ENUM_ICON + GS_;

	public static final String EXCEPTION_ICON = IExceptionArtifactImpl.class
			.getName();
	public static final String EXCEPTION_ICON_NEW = EXCEPTION_ICON + NEW_;
	public static final String EXCEPTION_ICON_GS = EXCEPTION_ICON + GS_;

	public static final String SESSION_ICON = ISessionArtifactImpl.class
			.getName();
	public static final String SESSION_ICON_NEW = SESSION_ICON + NEW_;
	public static final String SESSION_ICON_GS = SESSION_ICON + GS_;

	public static final String FIELD_ICON = IFieldImpl.class.getName();
	public static final String FIELD_ICON_NEW = FIELD_ICON + NEW_;
	public static final String FIELD_ICON_GS = FIELD_ICON + GS_;

	public static final String METHOD_ICON = IMethodImpl.class.getName();
	public static final String METHOD_ICON_NEW = METHOD_ICON + NEW_;
	public static final String METHOD_ICON_GS = METHOD_ICON + GS_;

	public static final String LITERAL_ICON = ILiteralImpl.class.getName();
	public static final String LITERAL_ICON_NEW = LITERAL_ICON + NEW_;
	public static final String LITERAL_ICON_GS = LITERAL_ICON + GS_;

	// ==========================================================
	// Local Icons
	public static final String CHECKED_ICON = "checked.gif";
	public static final String UNCHECKED_ICON = "unchecked.gif";

	public static final String PROFILE_ICON = "profile.gif";
	public static final String TSPROJECT_FOLDER = "tsProjectFolder.gif";
	public static final String PLUGINPROJECT_FOLDER = "pluginProjectFolder.gif";
	public static final String TS_LOGO = "ts_wiz.gif";
	public static final String WIZARD_IMPORT_LOGO = "ts_wiz-import.gif";

	public static final String VISUALEDITOR_ICON = "VisualeditorDiagramFile.gif";
	public static final String INSTANCEEDITOR_ICON = "InstancediagramDiagramFile.gif";
	public static final String CONTRACTSEGMENT_ICON = "segment.gif";
	public static final String CONTRACTUSECASE_ICON = "useCase.gif";
	public static final String SPLASH = "splash.gif";

	public static final String EXTENDSARROW_ICON = "ExtendsIcon-small.png";
	public static final String REFERENCEARROW_ICON = "ReferenceIcon-small.png";

	// Creating the managed version of all of that
	static {

		// Do all the local ones
		createManaged(PROFILE_ICON, localIcon(PROFILE_ICON));
		createManaged(TSPROJECT_FOLDER, localIcon(TSPROJECT_FOLDER));
		createManaged(PLUGINPROJECT_FOLDER, localIcon(PLUGINPROJECT_FOLDER));
		createManaged(CHECKED_ICON, localIcon(CHECKED_ICON));
		createManaged(UNCHECKED_ICON, localIcon(UNCHECKED_ICON));
		createManaged(TS_LOGO, localIcon(TS_LOGO));
		createManaged(WIZARD_IMPORT_LOGO, localIcon(WIZARD_IMPORT_LOGO));
		createManaged(VISUALEDITOR_ICON, localIcon(VISUALEDITOR_ICON));
		createManaged(INSTANCEEDITOR_ICON, localIcon(INSTANCEEDITOR_ICON));
		createManaged(CONTRACTSEGMENT_ICON, localIcon(CONTRACTSEGMENT_ICON));
		createManaged(CONTRACTUSECASE_ICON, localIcon(CONTRACTUSECASE_ICON));
		createManaged(SPLASH, localIcon(SPLASH));
		createManaged(EXTENDSARROW_ICON, localIcon(EXTENDSARROW_ICON));
		createManaged(REFERENCEARROW_ICON, localIcon(REFERENCEARROW_ICON));

		// Managed Entity
		createManaged(ENTITY_ICON, ArtifactMetadataFactory.INSTANCE
				.getMetadata(ENTITY_ICON).getIconURL(null));
		createManaged(ENTITY_ICON_NEW, ArtifactMetadataFactory.INSTANCE
				.getMetadata(ENTITY_ICON).getNewIconURL(null));
		createManaged(ENTITY_ICON_GS, ArtifactMetadataFactory.INSTANCE
				.getMetadata(ENTITY_ICON).getGreyedoutIconURL(null));

		// Datatype
		createManaged(DATATYPE_ICON, ArtifactMetadataFactory.INSTANCE
				.getMetadata(DATATYPE_ICON).getIconURL(null));
		createManaged(DATATYPE_ICON_NEW, ArtifactMetadataFactory.INSTANCE
				.getMetadata(DATATYPE_ICON).getNewIconURL(null));
		createManaged(DATATYPE_ICON_GS, ArtifactMetadataFactory.INSTANCE
				.getMetadata(DATATYPE_ICON).getGreyedoutIconURL(null));

		// Primitive Type
		createManaged(PRIMITIVE_ICON, ArtifactMetadataFactory.INSTANCE
				.getMetadata(PRIMITIVE_ICON).getIconURL(null));
		createManaged(PRIMITIVE_ICON_NEW, ArtifactMetadataFactory.INSTANCE
				.getMetadata(PRIMITIVE_ICON).getNewIconURL(null));
		createManaged(PRIMITIVE_ICON_GS, ArtifactMetadataFactory.INSTANCE
				.getMetadata(PRIMITIVE_ICON).getGreyedoutIconURL(null));

		// Association
		createManaged(ASSOCIATION_ICON, ArtifactMetadataFactory.INSTANCE
				.getMetadata(ASSOCIATION_ICON).getIconURL(null));
		createManaged(ASSOCIATION_ICON_NEW, ArtifactMetadataFactory.INSTANCE
				.getMetadata(ASSOCIATION_ICON).getNewIconURL(null));
		createManaged(ASSOCIATION_ICON_GS, ArtifactMetadataFactory.INSTANCE
				.getMetadata(ASSOCIATION_ICON).getGreyedoutIconURL(null));

		// Dependency
		createManaged(DEPENDENCY_ICON, ArtifactMetadataFactory.INSTANCE
				.getMetadata(DEPENDENCY_ICON).getIconURL(null));
		createManaged(DEPENDENCY_ICON_NEW, ArtifactMetadataFactory.INSTANCE
				.getMetadata(DEPENDENCY_ICON).getNewIconURL(null));
		createManaged(DEPENDENCY_ICON_GS, ArtifactMetadataFactory.INSTANCE
				.getMetadata(DEPENDENCY_ICON).getGreyedoutIconURL(null));

		// Association Class
		createManaged(ASSOCIATIONCLASS_ICON, ArtifactMetadataFactory.INSTANCE
				.getMetadata(ASSOCIATIONCLASS_ICON).getIconURL(null));
		createManaged(ASSOCIATIONCLASS_ICON_NEW,
				ArtifactMetadataFactory.INSTANCE.getMetadata(
						ASSOCIATIONCLASS_ICON).getNewIconURL(null));
		createManaged(ASSOCIATIONCLASS_ICON_GS,
				ArtifactMetadataFactory.INSTANCE.getMetadata(
						ASSOCIATIONCLASS_ICON).getGreyedoutIconURL(null));

		// Query
		createManaged(QUERY_ICON, ArtifactMetadataFactory.INSTANCE.getMetadata(
				QUERY_ICON).getIconURL(null));
		createManaged(QUERY_ICON_NEW, ArtifactMetadataFactory.INSTANCE
				.getMetadata(QUERY_ICON).getNewIconURL(null));
		createManaged(QUERY_ICON_GS, ArtifactMetadataFactory.INSTANCE
				.getMetadata(QUERY_ICON).getGreyedoutIconURL(null));

		// Update Procedure
		createManaged(UPDATEPROC_ICON, ArtifactMetadataFactory.INSTANCE
				.getMetadata(UPDATEPROC_ICON).getIconURL(null));
		createManaged(UPDATEPROC_ICON_NEW, ArtifactMetadataFactory.INSTANCE
				.getMetadata(UPDATEPROC_ICON).getNewIconURL(null));
		createManaged(UPDATEPROC_ICON_GS, ArtifactMetadataFactory.INSTANCE
				.getMetadata(UPDATEPROC_ICON).getGreyedoutIconURL(null));

		// Update Procedure
		createManaged(NOTIFICATION_ICON, ArtifactMetadataFactory.INSTANCE
				.getMetadata(NOTIFICATION_ICON).getIconURL(null));
		createManaged(NOTIFICATION_ICON_NEW, ArtifactMetadataFactory.INSTANCE
				.getMetadata(NOTIFICATION_ICON).getNewIconURL(null));
		createManaged(NOTIFICATION_ICON_GS, ArtifactMetadataFactory.INSTANCE
				.getMetadata(NOTIFICATION_ICON).getGreyedoutIconURL(null));

		// Update Procedure
		createManaged(ENUM_ICON, ArtifactMetadataFactory.INSTANCE.getMetadata(
				ENUM_ICON).getIconURL(null));
		createManaged(ENUM_ICON_NEW, ArtifactMetadataFactory.INSTANCE
				.getMetadata(ENUM_ICON).getNewIconURL(null));
		createManaged(ENUM_ICON_GS, ArtifactMetadataFactory.INSTANCE
				.getMetadata(ENUM_ICON).getGreyedoutIconURL(null));

		// Exception
		createManaged(EXCEPTION_ICON, ArtifactMetadataFactory.INSTANCE
				.getMetadata(EXCEPTION_ICON).getIconURL(null));
		createManaged(EXCEPTION_ICON_NEW, ArtifactMetadataFactory.INSTANCE
				.getMetadata(EXCEPTION_ICON).getNewIconURL(null));
		createManaged(EXCEPTION_ICON_GS, ArtifactMetadataFactory.INSTANCE
				.getMetadata(EXCEPTION_ICON).getGreyedoutIconURL(null));

		// Session
		createManaged(SESSION_ICON, ArtifactMetadataFactory.INSTANCE
				.getMetadata(SESSION_ICON).getIconURL(null));
		createManaged(SESSION_ICON_NEW, ArtifactMetadataFactory.INSTANCE
				.getMetadata(SESSION_ICON).getNewIconURL(null));
		createManaged(SESSION_ICON_GS, ArtifactMetadataFactory.INSTANCE
				.getMetadata(SESSION_ICON).getGreyedoutIconURL(null));

		// Field
		createManaged(FIELD_ICON, ArtifactMetadataFactory.INSTANCE.getMetadata(
				FIELD_ICON).getIconURL(null));
		createManaged(FIELD_ICON_NEW, ArtifactMetadataFactory.INSTANCE
				.getMetadata(FIELD_ICON).getNewIconURL(null));
		createManaged(FIELD_ICON_GS, ArtifactMetadataFactory.INSTANCE
				.getMetadata(FIELD_ICON).getGreyedoutIconURL(null));

		// Method
		createManaged(METHOD_ICON, ArtifactMetadataFactory.INSTANCE
				.getMetadata(METHOD_ICON).getIconURL(null));
		createManaged(METHOD_ICON_NEW, ArtifactMetadataFactory.INSTANCE
				.getMetadata(METHOD_ICON).getNewIconURL(null));
		createManaged(METHOD_ICON_GS, ArtifactMetadataFactory.INSTANCE
				.getMetadata(METHOD_ICON).getGreyedoutIconURL(null));

		// Literal
		createManaged(LITERAL_ICON, ArtifactMetadataFactory.INSTANCE
				.getMetadata(LITERAL_ICON).getIconURL(null));
		createManaged(LITERAL_ICON_NEW, ArtifactMetadataFactory.INSTANCE
				.getMetadata(LITERAL_ICON).getNewIconURL(null));
		createManaged(LITERAL_ICON_GS, ArtifactMetadataFactory.INSTANCE
				.getMetadata(LITERAL_ICON).getGreyedoutIconURL(null));
	}
}
