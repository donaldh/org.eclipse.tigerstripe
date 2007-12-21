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
package org.eclipse.tigerstripe.workbench.ui.resources;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * @author Eric Dillon
 * @since 1.2
 */
public class Images {

	private static Images instance = null;

	/**
	 * This is allows to remove the dependency on "Image" type and be more
	 * portable across plugins
	 * 
	 * @param key
	 * @return
	 */
	public Object getObject(String key) {
		return get(key);
	}

	public static Images getInstance() {
		if (instance == null) {
			instance = new Images();
			instance.init();
		}
		return instance;
	}

	// private static final String NAME_PREFIX=
	// "org.eclipse.tigerstripe.eclipse.runtime.images"; //$NON-NLS-1$
	private static final String NAME_PREFIX = ""; //$NON-NLS-1$

	private static final int NAME_PREFIX_LENGTH = NAME_PREFIX.length();

	private URL fgIconBaseURL = null;

	// The plug-in registry
	private ImageRegistry fgImageRegistry = null;

	private HashMap fgAvoidSWTErrorMap = null;

	private void init() {
		fgIconBaseURL = Activator.getDefault().getBundle().getEntry("icons/"); //$NON-NLS-1$
		if (fgAvoidSWTErrorMap == null) {
			fgAvoidSWTErrorMap = new HashMap();
		}
	}

	public static final String ENTITY_ICON = NAME_PREFIX + "entity.gif"; //$NON-NLS-1$

	public static final String ENTITY_NEW_ICON = NAME_PREFIX + "new_entity.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_ENTITY_ICON = getInstance()
			.createManaged("", ENTITY_ICON); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ENTITY_NEW_ICON = getInstance()
			.createManaged("", ENTITY_NEW_ICON); //$NON-NLS-1$

	public static final String PRIMITIVE_ICON = NAME_PREFIX + "primitive.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_PRIMITIVE_ICON = getInstance()
			.createManaged("", PRIMITIVE_ICON); //$NON-NLS-1$

	public static final String ASSOCIATIONARROW_ICON = NAME_PREFIX
			+ "AssociationIcon-small.png"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_ASSOCIATIONARROW_ICON = getInstance()
			.createManaged("", ASSOCIATIONARROW_ICON); //$NON-NLS-1$

	public static final String ASSOCIATIONARROW_LARGE_ICON = NAME_PREFIX
			+ "AssociationIcon-large.png"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_ASSOCIATIONARROW_LARGE_ICON = getInstance()
			.createManaged("", ASSOCIATIONARROW_LARGE_ICON); //$NON-NLS-1$

	public static final String DEPENDENCYARROW_ICON = NAME_PREFIX
			+ "RelationshipIcon-small.png"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_DEPENDENCYARROW_ICON = getInstance()
			.createManaged("", DEPENDENCYARROW_ICON); //$NON-NLS-1$

	public static final String DEPENDENCYARROW_LARGE_ICON = NAME_PREFIX
			+ "RelationshipIcon-large.png"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_DEPENDENCYARROW_LARGE_ICON = getInstance()
			.createManaged("", DEPENDENCYARROW_LARGE_ICON); //$NON-NLS-1$

	// ===============================================
	public static final String EXTENDSARROW_ICON = NAME_PREFIX
			+ "ExtendsIcon-small.png"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_EXTENDSARROW_ICON = getInstance()
			.createManaged("", EXTENDSARROW_ICON); //$NON-NLS-1$

	public static final String EXTENDSARROW_LARGE_ICON = NAME_PREFIX
			+ "ExtendsIcon-large.png"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_EXTENDSARROW_LARGE_ICON = getInstance()
			.createManaged("", EXTENDSARROW_LARGE_ICON); //$NON-NLS-1$

	// ===============================================
	public static final String METHODPUB_ICON = NAME_PREFIX
			+ "methpub_obj-small.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_METHODPUB_ICON = getInstance()
			.createManaged("", METHODPUB_ICON); //$NON-NLS-1$

	public static final String METHODPUB_ICON_WBG = NAME_PREFIX
			+ "methpub_obj-small_wbg.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_METHODPUB_ICON_WBG = getInstance()
			.createManaged("", METHODPUB_ICON_WBG); //$NON-NLS-1$

	public static final String METHODPUB_LARGE_ICON = NAME_PREFIX
			+ "methpub_obj-large.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_METHODPUB_LARGE_ICON = getInstance()
			.createManaged("", METHODPUB_LARGE_ICON); //$NON-NLS-1$

	public static final String METHODPUB_LARGE_ICON_WBG = NAME_PREFIX
			+ "methpub_obj-large_wbg.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_METHODPUB_LARGE_ICON_WBG = getInstance()
			.createManaged("", METHODPUB_LARGE_ICON_WBG); //$NON-NLS-1$

	// ===============================================
	public static final String LITERAL_ICON = NAME_PREFIX + "literal-small.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_LITERAL_ICON = getInstance()
			.createManaged("", LITERAL_ICON); //$NON-NLS-1$

	public static final String LITERAL_LARGE_ICON = NAME_PREFIX
			+ "literal-large.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_LITERAL_LARGE_ICON = getInstance()
			.createManaged("", LITERAL_LARGE_ICON); //$NON-NLS-1$

	public static final String LITERAL_ICON_WBG = NAME_PREFIX
			+ "literal-small_wbg.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_LITERAL_ICON_WBG = getInstance()
			.createManaged("", LITERAL_ICON_WBG); //$NON-NLS-1$

	public static final String LITERAL_LARGE_ICON_WBG = NAME_PREFIX
			+ "literal-large_wbg.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_LITERAL_LARGE_ICON_WBG = getInstance()
			.createManaged("", LITERAL_LARGE_ICON_WBG); //$NON-NLS-1$

	// ===============================================
	public static final String FIELDPRIVATE_ICON = NAME_PREFIX
			+ "field_private_obj-small.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_FIELDPRIVATE_ICON = getInstance()
			.createManaged("", FIELDPRIVATE_ICON); //$NON-NLS-1$

	public static final String FIELDPRIVATE_LARGE_ICON = NAME_PREFIX
			+ "field_private-large.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_FIELDPRIVATE_LARGE_ICON = getInstance()
			.createManaged("", FIELDPRIVATE_LARGE_ICON); //$NON-NLS-1$

	public static final String FIELDPRIVATE_ICON_WBG = NAME_PREFIX
			+ "field_private_obj-small_wbg.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_FIELDPRIVATE_ICON_WBG = getInstance()
			.createManaged("", FIELDPRIVATE_ICON_WBG); //$NON-NLS-1$

	public static final String FIELDPRIVATE_LARGE_ICON_WBG = NAME_PREFIX
			+ "field_private-large_wbg.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_FIELDPRIVATE_LARGE_ICON_WBG = getInstance()
			.createManaged("", FIELDPRIVATE_LARGE_ICON_WBG); //$NON-NLS-1$

	public static final String ASSOCIATION_ICON = NAME_PREFIX
			+ "association.gif"; //$NON-NLS-1$

	public static final String ASSOCIATION_NEW_ICON = NAME_PREFIX
			+ "new_association.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_ASSOCIATION_ICON = getInstance()
			.createManaged("", ASSOCIATION_ICON); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ASSOCIATION_NEW_ICON = getInstance()
			.createManaged("", ASSOCIATION_NEW_ICON); //$NON-NLS-1$

	public static final String ASSOCIATIONCLASS_ICON = NAME_PREFIX
			+ "AssocClassLinkIcon-small.png"; //$NON-NLS-1$

	public static final String ASSOCIATIONCLASSCLASS_ICON = NAME_PREFIX
			+ "association_class.gif"; //$NON-NLS-1$

	public static final String ASSOCIATIONCLASS_NEW_ICON = NAME_PREFIX
			+ "new_associationClass.gif"; //$NON-NLS-1$

	public static final String ASSOCIATIONCLASS_LARGE_ICON = NAME_PREFIX
			+ "AssocClassLinkIcon-large.png"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_ASSOCIATIONCLASS_ICON = getInstance()
			.createManaged("", ASSOCIATIONCLASS_ICON); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ASSOCIATIONCLASSCLASS_ICON = getInstance()
			.createManaged("", ASSOCIATIONCLASSCLASS_ICON); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ASSOCIATIONCLASS_NEW_ICON = getInstance()
			.createManaged("", ASSOCIATIONCLASS_NEW_ICON); //$NON-NLS-1$

	public static final String QUERY_ICON = NAME_PREFIX + "query.gif"; //$NON-NLS-1$

	public static final String QUERY_NEW_ICON = NAME_PREFIX + "new_query.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_QUERY_ICON = getInstance()
			.createManaged("", QUERY_ICON); //$NON-NLS-1$

	public static final ImageDescriptor DESC_QUERY_NEW_ICON = getInstance()
			.createManaged("", QUERY_NEW_ICON); //$NON-NLS-1$

	public static final String DATATYPE_ICON = NAME_PREFIX + "datatype.gif"; //$NON-NLS-1$

	public static final String DATATYPE_NEW_ICON = NAME_PREFIX
			+ "new_datatype.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_DATATYPE_ICON = getInstance()
			.createManaged("", DATATYPE_ICON);

	public static final String CHECKED_ICON = NAME_PREFIX + "checked.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_CHECKED_ICON = getInstance()
			.createManaged("", CHECKED_ICON);

	public static final String UNCHECKED_ICON = NAME_PREFIX + "unchecked.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_UNCHECKED_ICON = getInstance()
			.createManaged("", UNCHECKED_ICON); //$NON-NLS-1$

	public static final ImageDescriptor DESC_DATATYPE_NEW_ICON = getInstance()
			.createManaged("", DATATYPE_NEW_ICON); //$NON-NLS-1$

	public static final String UPDATEPROC_ICON = NAME_PREFIX + "updateProc.gif"; //$NON-NLS-1$

	public static final String UPDATEPROC_NEW_ICON = NAME_PREFIX
			+ "new_updateProc.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_UPDATEPROC_ICON = getInstance()
			.createManaged("", UPDATEPROC_ICON); //$NON-NLS-1$

	public static final ImageDescriptor DESC_UPDATEPROC_NEW_ICON = getInstance()
			.createManaged("", UPDATEPROC_NEW_ICON); //$NON-NLS-1$

	public static final String NOTIFICATION_ICON = NAME_PREFIX
			+ "notification.gif"; //$NON-NLS-1$

	public static final String NOTIFICATION_NEW_ICON = NAME_PREFIX
			+ "new_notification.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_NOTIFICATION_ICON = getInstance()
			.createManaged("", NOTIFICATION_ICON); //$NON-NLS-1$

	public static final ImageDescriptor DESC_NOTIFICATION_NEW_ICON = getInstance()
			.createManaged("", NOTIFICATION_NEW_ICON); //$NON-NLS-1$

	public static final String ENUM_ICON = NAME_PREFIX + "enum.gif"; //$NON-NLS-1$

	public static final String ENUM_NEW_ICON = NAME_PREFIX + "new_enum.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_ENUM_ICON = getInstance()
			.createManaged("", ENUM_ICON); //$NON-NLS-1$

	public static final ImageDescriptor DESC_ENUM_NEW_ICON = getInstance()
			.createManaged("", ENUM_NEW_ICON); //$NON-NLS-1$

	public static final String EXCEPTION_ICON = NAME_PREFIX + "exception.gif";

	public static final String EXCEPTION_NEW_ICON = NAME_PREFIX
			+ "new_exception.gif";

	public static final ImageDescriptor DESC_EXCEPTION_ICON = getInstance()
			.createManaged("", EXCEPTION_ICON);

	public static final ImageDescriptor DESC_EXCEPTION_NEW_ICON = getInstance()
			.createManaged("", EXCEPTION_NEW_ICON);

	public static final String SESSION_ICON = NAME_PREFIX + "session.gif";

	public static final String SESSION_NEW_ICON = NAME_PREFIX
			+ "new_session.gif";

	public static final String SPLASH = NAME_PREFIX + "splash.gif";

	public static final String UNTITLED = NAME_PREFIX + "Untitled-1.gif";

	public static final ImageDescriptor DESC_UNTITLED = getInstance()
			.createManaged("", UNTITLED);

	public static final ImageDescriptor DESC_SPLASH = getInstance()
			.createManaged("", SPLASH);

	public static final String TSPROJECT_FOLDER = NAME_PREFIX
			+ "tsProjectFolder.gif";

	public static final ImageDescriptor DESC_TSPROJECT_FOLDER = getInstance()
			.createManaged("", TSPROJECT_FOLDER);

	public static final String PLUGINPROJECT_FOLDER = NAME_PREFIX
			+ "pluginProjectFolder.gif";

	public static final ImageDescriptor DESC_PLUGINPROJECT_FOLDER = getInstance()
			.createManaged("", PLUGINPROJECT_FOLDER);

	public static final ImageDescriptor DESC_SESSION_ICON = getInstance()
			.createManaged("", SESSION_ICON);

	public static final ImageDescriptor DESC_SESSION_NEW_ICON = getInstance()
			.createManaged("", SESSION_NEW_ICON);

	public static final String ATTRIBUTE_NEW_WIZ = NAME_PREFIX
			+ "new_attribute_wiz.gif";

	public static final ImageDescriptor DESC_ATTRIBUTE_NEW_WIZ = getInstance()
			.createManaged("", ATTRIBUTE_NEW_WIZ);

	public static final String METHOD_NEW_WIZ = NAME_PREFIX
			+ "new_method_wiz.gif";

	public static final ImageDescriptor DESC_METHOD_NEW_WIZ = getInstance()
			.createManaged("", METHOD_NEW_WIZ);

	public static final ImageDescriptor DESC_TS_LOGO = getInstance()
			.createManaged("", NAME_PREFIX + "ts_wiz.gif");

	public static final ImageDescriptor WIZARD_IMPORT_LOGO = getInstance()
			.createManaged("", NAME_PREFIX + "ts_wiz-import.gif");

	public static final ImageDescriptor WIZARD_NEWENTITY_LOGO = getInstance()
			.createManaged("", NAME_PREFIX + "ts_wiz-entity.gif");

	public static final String PROFILE_ICON = NAME_PREFIX + "profile.gif"; //$NON-NLS-1$

	public static final ImageDescriptor DESC_PROFILE_ICON = getInstance()
			.createManaged("", PROFILE_ICON);

	/**
	 * Returns the image managed under the given key in this registry.
	 * 
	 * @param key
	 *            the image's key
	 * @return the image managed under the given key
	 */
	public Image get(String key) {
		return getImageRegistry().get(key);
	}

	/*
	 * Helper method to access the image registry from the JavaPlugin class.
	 */
	protected ImageRegistry getImageRegistry() {
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

	// private static ImageDescriptor create(String prefix, String name) {
	//
	// try {
	// return ImageDescriptor.createFromURL(makeIconFileURL(prefix, name));
	// } catch (MalformedURLException e) {
	// return ImageDescriptor.getMissingImageDescriptor();
	// }
	// }
	//
	private URL makeIconFileURL(String prefix, String name)
			throws MalformedURLException {
		if (fgIconBaseURL == null)
			throw new MalformedURLException();

		StringBuffer buffer = new StringBuffer(prefix);
		if (!"".equals(prefix))
			buffer.append('/');
		buffer.append(name);
		return new URL(fgIconBaseURL, buffer.toString());
	}

	private ImageDescriptor createManaged(String prefix, String name) {
		try {
			ImageDescriptor result = ImageDescriptor
					.createFromURL(makeIconFileURL(prefix, name
							.substring(NAME_PREFIX_LENGTH)));
			if (fgAvoidSWTErrorMap == null) {
				fgAvoidSWTErrorMap = new HashMap();
			}
			fgAvoidSWTErrorMap.put(name, result);
			if (fgImageRegistry != null) {
				Activator.logErrorMessage("Image registry already defined"); //$NON-NLS-1$
			}
			return result;
		} catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}

}