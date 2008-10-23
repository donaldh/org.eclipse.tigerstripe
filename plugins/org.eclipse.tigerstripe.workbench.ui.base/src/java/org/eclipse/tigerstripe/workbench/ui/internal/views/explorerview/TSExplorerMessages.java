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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.osgi.util.NLS;

public final class TSExplorerMessages extends NLS {

	// FIXME use our own bundle
	private static final String BUNDLE_NAME = "org.eclipse.jdt.internal.ui.packageview.PackagesMessages";//$NON-NLS-1$

	private TSExplorerMessages() {
		// Do not instantiate
	}

	public static String PackageExplorerContentProvider_update_job_description;
	public static String BuildGroup_buildProject;
	public static String BuildGroup_rebuildProject;
	public static String DragAdapter_deleting;
	public static String DragAdapter_problem;
	public static String DragAdapter_problemTitle;
	public static String DragAdapter_refreshing;
	public static String DropAdapter_alreadyExists;
	public static String DropAdapter_errorSame;
	public static String DropAdapter_errorSubfolder;
	public static String DropAdapter_errorTitle;
	public static String DropAdapter_errorMessage;
	public static String DropAdapter_question;
	public static String FilterSelectionAction_apply_label;
	public static String FilterSelectionAction_apply_toolTip;
	public static String FilterSelectionAction_dialog_title;
	public static String GotoPackage_action_label;
	public static String GotoPackage_dialog_message;
	public static String GotoPackage_dialog_title;
	public static String GotoPackage_action_description;
	public static String GotoRequiredProjectAction_label;
	public static String GotoRequiredProjectAction_description;
	public static String GotoRequiredProjectAction_tooltip;
	public static String GotoType_action_label;
	public static String GotoType_action_description;
	public static String GotoType_dialog_message;
	public static String GotoType_dialog_title;
	public static String GotoType_error_message;
	public static String GotoResource_action_label;
	public static String GotoResource_dialog_title;
	public static String OpenResource_action_description;
	public static String OpenResource_action_label;
	public static String OpenResource_error_message;
	public static String OpenResource_error_messageArgs;
	public static String OpenResource_error_messageProblems;
	public static String OpenResource_error_title;
	public static String Sorter_expectPackage;
	public static String ShowLibraries_hideReferencedLibs;
	public static String ShowLibraries_showReferencedLibs;
	public static String ShowBinaries_hideBinaryProjects;
	public static String ShowBinaries_showBinaryProjects;
	public static String ShowInNavigator_description;
	public static String ShowInNavigator_error;
	public static String ShowInNavigator_label;
	public static String PackageExplorer_filters;
	public static String PackageExplorer_gotoTitle;
	public static String PackageExplorer_openPerspective;
	public static String PackageExplorer_refactoringTitle;
	public static String PackageExplorer_referencedLibs;
	public static String PackageExplorer_binaryProjects;
	public static String PackageExplorer_title;
	public static String PackageExplorer_toolTip;
	public static String PackageExplorer_toolTip2;
	public static String PackageExplorer_toolTip3;
	public static String PackageExplorer_openWith;
	public static String PackageExplorer_element_not_present;
	public static String PackageExplorer_filteredDialog_title;
	public static String PackageExplorer_notFound;
	public static String PackageExplorer_removeFilters;
	public static String LibraryContainer_name;
	public static String LayoutActionGroup_show_libraries_in_group;
	public static String PackageExplorerPart_removeFiltersSpecific;
	public static String PackageExplorerPart_notFoundSepcific;
	public static String SelectionTransferDropAdapter_error_title;
	public static String SelectionTransferDropAdapter_error_message;
	public static String SelectionTransferDropAdapter_dialog_title;
	public static String SelectionTransferDropAdapter_dialog_preview_label;
	public static String SelectionTransferDropAdapter_dialog_question;
	public static String LayoutActionGroup_label;
	public static String LayoutActionGroup_flatLayoutAction_label;
	public static String LayoutActionGroup_hierarchicalLayoutAction_label;
	public static String ClassPathContainer_unbound_label;
	public static String ClassPathContainer_unknown_label;
	public static String PackageExplorerPart_workspace;
	public static String PackageExplorerPart_workingSetModel;
	

	static {
		NLS.initializeMessages(BUNDLE_NAME, TSExplorerMessages.class);
	}
}