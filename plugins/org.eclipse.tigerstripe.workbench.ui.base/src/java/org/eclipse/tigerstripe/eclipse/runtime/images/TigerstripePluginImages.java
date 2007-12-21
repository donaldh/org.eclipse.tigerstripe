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
package org.eclipse.tigerstripe.eclipse.runtime.images;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class TigerstripePluginImages {

	// private static final String NAME_PREFIX=
	// "org.eclipse.tigerstripe.eclipse.runtime.images"; //$NON-NLS-1$
	private static final String NAME_PREFIX = ""; //$NON-NLS-1$

	private static final int NAME_PREFIX_LENGTH = NAME_PREFIX.length();

	private static URL fgIconBaseURL = null;

	// The plug-in registry
	private static ImageRegistry fgImageRegistry = null;

	private static HashMap fgAvoidSWTErrorMap = null;

	// Determine display depth. If depth > 4 then we use high color images.
	// Otherwise low color
	// images are used
	static {
		fgIconBaseURL = EclipsePlugin.getDefault().getBundle().getEntry(
				"src/icons/"); //$NON-NLS-1$
	}

	public static final String ENTITY_ICON = NAME_PREFIX + "entity.gif"; //$NON-NLS-1$

	public static final String ENTITY_ICON_GS = NAME_PREFIX + "entity_gs.gif"; //$NON-NLS-1$

	public static final String ENTITY_ICON_WBG = NAME_PREFIX + "entity_wbg.gif"; //$NON-NLS-1$

	public static final String ENTITY_ICON_WBG_GS = NAME_PREFIX
			+ "entity_wbg_gs.gif"; //$NON-NLS-1$

	public static final String ENTITY_NEW_ICON = NAME_PREFIX + "new_entity.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_ENTITY_ICON = createManaged(
			"", ENTITY_ICON); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ENTITY_ICON_GS = createManaged(
			"", ENTITY_ICON_GS); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ENTITY_ICON_WBG = createManaged(
			"", ENTITY_ICON_WBG); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ENTITY_ICON_WBG_GS = createManaged(
			"", ENTITY_ICON_WBG_GS); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ENTITY_NEW_ICON = createManaged(
			"", ENTITY_NEW_ICON); //$NON-NLS-1$

	public static final String PRIMITIVE_ICON = NAME_PREFIX + "primitive.gif"; //$NON-NLS-1$

	public static final String PRIMITIVE_ICON_GS = NAME_PREFIX
			+ "primitive_gs.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_PRIMITIVE_ICON = createManaged(
			"", PRIMITIVE_ICON); //$NON-NLS-1$

	public static final ImageDescriptor DESC_PRIMITIVE_ICON_GS = createManaged(
			"", PRIMITIVE_ICON_GS); //$NON-NLS-1$

	public static final String ASSOCIATION_ICON = NAME_PREFIX
			+ "association.gif"; //$NON-NLS-1$

	public static final String ASSOCIATION_ICON_GS = NAME_PREFIX
			+ "association_gs.gif"; //$NON-NLS-1$

	public static final String ASSOCIATION_NEW_ICON = NAME_PREFIX
			+ "new_association.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_ASSOCIATION_ICON = createManaged(
			"", ASSOCIATION_ICON); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ASSOCIATION_ICON_GS = createManaged(
			"", ASSOCIATION_ICON_GS); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ASSOCIATION_NEW_ICON = createManaged(
			"", ASSOCIATION_NEW_ICON); //$NON-NLS-1$

	public static final String DEPENDENCY_ICON = NAME_PREFIX + "dependency.gif"; //$NON-NLS-1$

	public static final String DEPENDENCY_ICON_GS = NAME_PREFIX
			+ "dependency_gs.gif"; //$NON-NLS-1$

	public static final String DEPENDENCY_NEW_ICON = NAME_PREFIX
			+ "new_dependency.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_DEPENDENCY_ICON = createManaged(
			"", DEPENDENCY_ICON); //$NON-NLS-1$

	public static final ImageDescriptor DESC_DEPENDENCY_ICON_GS = createManaged(
			"", DEPENDENCY_ICON_GS); //$NON-NLS-1$

	public static final ImageDescriptor DESC_DEPENDENCY_NEW_ICON = createManaged(
			"", DEPENDENCY_NEW_ICON); //$NON-NLS-1$

	public static final String ASSOCIATIONCLASS_ICON = NAME_PREFIX
			+ "association_class.gif"; //$NON-NLS-1$

	public static final String ASSOCIATIONCLASS_ICON_GS = NAME_PREFIX
			+ "association_class_gs.gif"; //$NON-NLS-1$

	public static final String ASSOCIATIONCLASS_ICON_WBG = NAME_PREFIX
			+ "association_class_wbg.gif"; //$NON-NLS-1$

	public static final String ASSOCIATIONCLASS_ICON_WBG_GS = NAME_PREFIX
			+ "association_class_wbg_gs.gif"; //$NON-NLS-1$

	public static final String ASSOCIATIONCLASS_NEW_ICON = NAME_PREFIX
			+ "new_associationClass.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_ASSOCIATIONCLASS_ICON = createManaged(
			"", ASSOCIATIONCLASS_ICON); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ASSOCIATIONCLASS_ICON_GS = createManaged(
			"", ASSOCIATIONCLASS_ICON_GS); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ASSOCIATIONCLASS_ICON_WBG = createManaged(
			"", ASSOCIATIONCLASS_ICON_WBG); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ASSOCIATIONCLASS_ICON_WBG_GS = createManaged(
			"", ASSOCIATIONCLASS_ICON_WBG_GS); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ASSOCIATIONCLASS_NEW_ICON = createManaged(
			"", ASSOCIATIONCLASS_NEW_ICON); //$NON-NLS-1$

	public static final String QUERY_ICON = NAME_PREFIX + "query.gif";

	public static final String QUERY_ICON_GS = NAME_PREFIX + "query_gs.gif";

	public static final String QUERY_ICON_WBG = NAME_PREFIX + "query_wbg.gif";

	public static final String QUERY_ICON_WBG_GS = NAME_PREFIX
			+ "query_wbg_gs.gif";

	public static final String QUERY_NEW_ICON = NAME_PREFIX + "new_query.gif";

	public static final ImageDescriptor DESC_QUERY_ICON = createManaged("",
			QUERY_ICON);

	public static final ImageDescriptor DESC_QUERY_ICON_GS = createManaged("",
			QUERY_ICON_GS);

	public static final ImageDescriptor DESC_QUERY_ICON_WBG = createManaged("",
			QUERY_ICON_WBG);

	public static final ImageDescriptor DESC_QUERY_ICON_WBG_GS = createManaged(
			"", QUERY_ICON_WBG_GS);

	public static final ImageDescriptor DESC_QUERY_NEW_ICON = createManaged("",
			QUERY_NEW_ICON);

	public static final String DATATYPE_ICON = NAME_PREFIX + "datatype.gif";

	public static final String DATATYPE_ICON_GS = NAME_PREFIX
			+ "datatype_gs.gif";

	public static final String DATATYPE_ICON_WBG = NAME_PREFIX
			+ "datatype_wbg.gif";

	public static final String DATATYPE_ICON_WBG_GS = NAME_PREFIX
			+ "datatype_wbg_gs.gif";

	public static final String DATATYPE_NEW_ICON = NAME_PREFIX
			+ "new_datatype.gif";

	public static final ImageDescriptor DESC_DATATYPE_ICON = createManaged("",
			DATATYPE_ICON);

	public static final ImageDescriptor DESC_DATATYPE_ICON_GS = createManaged(
			"", DATATYPE_ICON_GS);

	public static final ImageDescriptor DESC_DATATYPE_ICON_WBG = createManaged(
			"", DATATYPE_ICON_WBG);

	public static final ImageDescriptor DESC_DATATYPE_ICON_WBG_GS = createManaged(
			"", DATATYPE_ICON_WBG_GS);

	public static final String CHECKED_ICON = NAME_PREFIX + "checked.gif";

	public static final ImageDescriptor DESC_CHECKED_ICON = createManaged("",
			CHECKED_ICON);

	public static final String UNCHECKED_ICON = NAME_PREFIX + "unchecked.gif";

	public static final ImageDescriptor DESC_UNCHECKED_ICON = createManaged("",
			UNCHECKED_ICON);

	public static final ImageDescriptor DESC_DATATYPE_NEW_ICON = createManaged(
			"", DATATYPE_NEW_ICON);

	public static final String UPDATEPROC_ICON = NAME_PREFIX + "updateProc.gif";

	public static final String UPDATEPROC_ICON_GS = NAME_PREFIX
			+ "updateProc_gs.gif";

	public static final String UPDATEPROC_ICON_WBG = NAME_PREFIX
			+ "updateProc_wbg.gif";

	public static final String UPDATEPROC_ICON_WBG_GS = NAME_PREFIX
			+ "updateProc_wbg_gs.gif";

	public static final String UPDATEPROC_NEW_ICON = NAME_PREFIX
			+ "new_updateProc.gif";

	public static final ImageDescriptor DESC_UPDATEPROC_ICON = createManaged(
			"", UPDATEPROC_ICON);

	public static final ImageDescriptor DESC_UPDATEPROC_ICON_GS = createManaged(
			"", UPDATEPROC_ICON_GS);

	public static final ImageDescriptor DESC_UPDATEPROC_ICON_WBG = createManaged(
			"", UPDATEPROC_ICON_WBG);

	public static final ImageDescriptor DESC_UPDATEPROC_ICON_WBG_GS = createManaged(
			"", UPDATEPROC_ICON_WBG_GS);

	public static final ImageDescriptor DESC_UPDATEPROC_NEW_ICON = createManaged(
			"", UPDATEPROC_NEW_ICON);

	public static final String NOTIFICATION_ICON = NAME_PREFIX
			+ "notification.gif";

	public static final String NOTIFICATION_ICON_GS = NAME_PREFIX
			+ "notification_gs.gif";

	public static final String NOTIFICATION_ICON_WBG = NAME_PREFIX
			+ "notification_wbg.gif";

	public static final String NOTIFICATION_ICON_WBG_GS = NAME_PREFIX
			+ "notification_wbg_gs.gif";

	public static final String NOTIFICATION_NEW_ICON = NAME_PREFIX
			+ "new_notification.gif";

	public static final ImageDescriptor DESC_NOTIFICATION_ICON = createManaged(
			"", NOTIFICATION_ICON);

	public static final ImageDescriptor DESC_NOTIFICATION_ICON_GS = createManaged(
			"", NOTIFICATION_ICON_GS);

	public static final ImageDescriptor DESC_NOTIFICATION_ICON_WBG = createManaged(
			"", NOTIFICATION_ICON_WBG);

	public static final ImageDescriptor DESC_NOTIFICATION_ICON_WBG_GS = createManaged(
			"", NOTIFICATION_ICON_WBG_GS);

	public static final ImageDescriptor DESC_NOTIFICATION_NEW_ICON = createManaged(
			"", NOTIFICATION_NEW_ICON);

	public static final String ENUM_ICON = NAME_PREFIX + "enum.gif";

	public static final String ENUM_ICON_GS = NAME_PREFIX + "enum_gs.gif";

	public static final String ENUM_ICON_WBG = NAME_PREFIX + "enum_wbg.gif";

	public static final String ENUM_ICON_WBG_GS = NAME_PREFIX
			+ "enum_wbg_gs.gif";

	public static final String ENUM_NEW_ICON = NAME_PREFIX + "new_enum.gif";

	public static final ImageDescriptor DESC_ENUM_ICON = createManaged("",
			ENUM_ICON);

	public static final ImageDescriptor DESC_ENUM_ICON_GS = createManaged("",
			ENUM_ICON_GS);

	public static final ImageDescriptor DESC_ENUM_ICON_WBG = createManaged("",
			ENUM_ICON_WBG);

	public static final ImageDescriptor DESC_ENUM_ICON_WBG_GS = createManaged(
			"", ENUM_ICON_WBG_GS);

	public static final ImageDescriptor DESC_ENUM_NEW_ICON = createManaged("",
			ENUM_NEW_ICON);

	public static final String EXCEPTION_ICON = NAME_PREFIX + "exception.gif";

	public static final String EXCEPTION_ICON_GS = NAME_PREFIX
			+ "exception_gs.gif";

	public static final String EXCEPTION_ICON_WBG = NAME_PREFIX
			+ "exception_wbg.gif";

	public static final String EXCEPTION_ICON_WBG_GS = NAME_PREFIX
			+ "exception_wbg_gs.gif";

	public static final String EXCEPTION_NEW_ICON = NAME_PREFIX
			+ "new_exception.gif";

	public static final ImageDescriptor DESC_EXCEPTION_ICON = createManaged("",
			EXCEPTION_ICON);

	public static final ImageDescriptor DESC_EXCEPTION_ICON_GS = createManaged(
			"", EXCEPTION_ICON_GS);

	public static final ImageDescriptor DESC_EXCEPTION_ICON_WBG = createManaged(
			"", EXCEPTION_ICON_WBG);

	public static final ImageDescriptor DESC_EXCEPTION_ICON_WBG_GS = createManaged(
			"", EXCEPTION_ICON_WBG_GS);

	public static final ImageDescriptor DESC_EXCEPTION_NEW_ICON = createManaged(
			"", EXCEPTION_NEW_ICON);

	public static final String SESSION_ICON = NAME_PREFIX + "session.gif";

	public static final String SESSION_ICON_GS = NAME_PREFIX + "session_gs.gif";

	public static final String SESSION_ICON_WBG = NAME_PREFIX
			+ "session_wbg.gif";

	public static final String SESSION_ICON_WBG_GS = NAME_PREFIX
			+ "session_wbg_gs.gif";

	public static final String SESSION_NEW_ICON = NAME_PREFIX
			+ "new_session.gif";

	public static final String SPLASH = NAME_PREFIX + "splash.gif";

	public static final String UNTITLED = NAME_PREFIX + "Untitled-1.gif";

	public static final ImageDescriptor DESC_UNTITLED = createManaged("",
			UNTITLED);

	public static final ImageDescriptor DESC_SPLASH = createManaged("", SPLASH);

	public static final String TSPROJECT_FOLDER = NAME_PREFIX
			+ "tsProjectFolder.gif";

	public static final ImageDescriptor DESC_TSPROJECT_FOLDER = createManaged(
			"", TSPROJECT_FOLDER);

	public static final String PLUGINPROJECT_FOLDER = NAME_PREFIX
			+ "pluginProjectFolder.gif";

	public static final ImageDescriptor DESC_PLUGINPROJECT_FOLDER = createManaged(
			"", PLUGINPROJECT_FOLDER);

	public static final ImageDescriptor DESC_SESSION_ICON = createManaged("",
			SESSION_ICON);

	public static final ImageDescriptor DESC_SESSION_ICON_GS = createManaged(
			"", SESSION_ICON_GS);

	public static final ImageDescriptor DESC_SESSION_ICON_WBG = createManaged(
			"", SESSION_ICON_WBG);

	public static final ImageDescriptor DESC_SESSION_ICON_WBG_GS = createManaged(
			"", SESSION_ICON_WBG_GS);

	public static final ImageDescriptor DESC_SESSION_NEW_ICON = createManaged(
			"", SESSION_NEW_ICON);

	public static final String ATTRIBUTE_NEW_WIZ = NAME_PREFIX
			+ "new_attribute_wiz.gif";

	public static final ImageDescriptor DESC_ATTRIBUTE_NEW_WIZ = createManaged(
			"", ATTRIBUTE_NEW_WIZ);

	public static final String METHOD_NEW_WIZ = NAME_PREFIX
			+ "new_method_wiz.gif";

	public static final ImageDescriptor DESC_METHOD_NEW_WIZ = createManaged("",
			METHOD_NEW_WIZ);

	public static final ImageDescriptor DESC_TS_LOGO = createManaged("",
			NAME_PREFIX + "ts_wiz.gif");

	public static final ImageDescriptor WIZARD_IMPORT_LOGO = createManaged("",
			NAME_PREFIX + "ts_wiz-import.gif");

	public static final ImageDescriptor WIZARD_NEWENTITY_LOGO = createManaged(
			"", NAME_PREFIX + "ts_wiz-entity.gif");

	public static final String VISUALEDITOR_ICON = NAME_PREFIX
			+ "VisualeditorDiagramFile.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_VISUALEDITOR_ICON = createManaged(
			"", VISUALEDITOR_ICON); //$NON-NLS-1$

	public static final String INSTANCEEDITOR_ICON = NAME_PREFIX
			+ "InstancediagramDiagramFile.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_INSTANCEEDITOR_ICON = createManaged(
			"", INSTANCEEDITOR_ICON); //$NON-NLS-1$

	public static final String CONTRACTSEGMENT_ICON = NAME_PREFIX
			+ "segment.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_CONTRACTSEGMENT_ICON = createManaged(
			"", CONTRACTSEGMENT_ICON); //$NON-NLS-1$

	public static final String CONTRACTUSECASE_ICON = NAME_PREFIX
			+ "useCase.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_CONTRACTUSECASE_ICON = createManaged(
			"", CONTRACTUSECASE_ICON); //$NON-NLS-1$

	static {
		if (fgAvoidSWTErrorMap == null) {
			fgAvoidSWTErrorMap = new HashMap();
		}
	}

	/**
	 * Returns the image managed under the given key in this registry.
	 * 
	 * @param key
	 *            the image's key
	 * @return the image managed under the given key
	 */
	public static Image get(String key) {
		return getImageRegistry().get(key);
	}

	/*
	 * Helper method to access the image registry from the JavaPlugin class.
	 */
	/* package */static ImageRegistry getImageRegistry() {
		if (fgImageRegistry == null) {
			fgImageRegistry = new ImageRegistry();
			for (Iterator iter = fgAvoidSWTErrorMap.keySet().iterator(); iter
					.hasNext();) {
				String key = (String) iter.next();
				fgImageRegistry.put(key, (ImageDescriptor) fgAvoidSWTErrorMap
						.get(key));
			}
			fgAvoidSWTErrorMap = null;
		}
		return fgImageRegistry;
	}

	private static URL makeIconFileURL(String prefix, String name)
			throws MalformedURLException {
		if (fgIconBaseURL == null)
			throw new MalformedURLException();

		StringBuffer buffer = new StringBuffer(prefix);
		if (!"".equals(prefix))
			buffer.append('/');
		buffer.append(name);
		return new URL(fgIconBaseURL, buffer.toString());
	}

	private static ImageDescriptor createManaged(String prefix, String name) {
		try {
			ImageDescriptor result = ImageDescriptor
					.createFromURL(makeIconFileURL(prefix, name
							.substring(NAME_PREFIX_LENGTH)));
			if (fgAvoidSWTErrorMap == null) {
				fgAvoidSWTErrorMap = new HashMap();
			}
			fgAvoidSWTErrorMap.put(name, result);
			if (fgImageRegistry != null) {
				JavaPlugin.logErrorMessage("Image registry already defined"); //$NON-NLS-1$
			}
			return result;
		} catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}

}