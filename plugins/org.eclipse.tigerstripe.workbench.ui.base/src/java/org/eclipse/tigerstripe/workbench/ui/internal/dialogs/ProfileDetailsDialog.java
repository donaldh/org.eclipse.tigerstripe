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
package org.eclipse.tigerstripe.workbench.ui.internal.dialogs;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.profile.ProfileValidator;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.LicensedAccess;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.TSWorkbenchProfileRole;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.elements.MessageListDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class ProfileDetailsDialog extends Dialog {

	private Text versionText;

	private Text descText;

	private Text nameText;

	private Button factoryProfileButton;

	private Button deployProfileButton;

	public ProfileDetailsDialog(IShellProvider parentShell) {
		super(parentShell);
	}

	public ProfileDetailsDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		getShell().setSize(500, 300);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		area.setLayout(layout);
		area.setLayoutData(new GridData(GridData.FILL_BOTH));
		applyDialogFont(area);

		IWorkbenchProfile profile = TigerstripeCore.getWorkbenchProfileSession()
				.getActiveProfile();

		Label nameLabel = new Label(area, SWT.NONE);
		nameLabel.setText("Name");

		nameText = new Text(area, SWT.NONE | SWT.BORDER);
		nameText.setText(profile.getName());
		nameText.setEditable(false);
		nameText.setEnabled(false);
		nameText.setBackground(new Color(null, 255, 255, 255));
		GridData gd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = 2;
		nameText.setLayoutData(gd);

		Label versionLabel = new Label(area, SWT.NONE);
		versionLabel.setText("Version");

		versionText = new Text(area, SWT.NONE | SWT.BORDER);
		versionText.setText(profile.getVersion());
		versionText.setEditable(false);
		versionText.setEnabled(false);
		versionText.setBackground(new Color(null, 255, 255, 255));
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = 2;
		versionText.setLayoutData(gd);

		Label descLabel = new Label(area, SWT.NONE);
		descLabel.setText("Description");
		descLabel
				.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

		descText = new Text(area, SWT.V_SCROLL | SWT.BORDER);
		descText.setText(profile.getDescription());
		descText.setEditable(false);
		descText.setBackground(new Color(null, 255, 255, 255));
		gd = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL
				| GridData.GRAB_VERTICAL);
		gd.heightHint = 70;
		gd.horizontalSpan = 2;
		descText.setLayoutData(gd);
		descText.setEnabled(false);

		new Label(area, SWT.NULL);

		factoryProfileButton = new Button(area, SWT.PUSH);
		factoryProfileButton.setText("Reset Profile");
		factoryProfileButton
				.setToolTipText("Reset profile to factory defaults");
		factoryProfileButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				resetProfile();
				refresh();
			}
		});

		deployProfileButton = new Button(area, SWT.PUSH);
		deployProfileButton.setText("Deploy Profile...");
		deployProfileButton
				.setToolTipText("Deploy a new Profile in Tigerstripe Workbench");
		deployProfileButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				deployProfile();
				refresh();
			}
		});

		getShell().setText("Active Profile Details");
		return area;
	}

	private void refresh() {
		IWorkbenchProfile profile = TigerstripeCore.getWorkbenchProfileSession()
				.getActiveProfile();
		descText.setText(profile.getDescription());
		nameText.setText(profile.getName());
		versionText.setText(profile.getVersion());
	}

	private void resetProfile() {
		if (LicensedAccess.getWorkbenchProfileRole() != TSWorkbenchProfileRole.CREATE_EDIT
				&& LicensedAccess.getWorkbenchProfileRole() != TSWorkbenchProfileRole.DEPLOY_UNDEPLOY) {
			String errMessage = "You cannot reset to the factory default profile\n\n"
					+ "Your Tigerstripe license has insufficient privileges for this operation, "
					+ "please contact Tigerstripe if you wish to be able to reset to the factory "
					+ "default workbench profiles";
			MessageDialog.openError(getShell(), "Reset Profile Error",
					errMessage);
		} else {
			if (MessageDialog
					.openConfirm(
							getShell(),
							"Reset Active profile to Factory Defaults?",
							"You are about to reset the active profile to factory defaults.\nAll open editors will be closed.\n\nDo you want to continue?\n\n(You will be able to rollback to the current active profile).  ")) {

				IRunnableWithProgress op = new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) {
						try {
							monitor.beginTask("Resetting profile...", 10);

							monitor.subTask("Closing all editors");
							EclipsePlugin.closeAllEditors(true, true, false,
									false, true);
							monitor.worked(2);

							monitor
									.subTask("Resetting profile to factory defaults");
							IWorkbenchProfileSession session = TigerstripeCore
									.getWorkbenchProfileSession();
							rollbackCreated = session.setDefaultActiveProfile();
							monitor.worked(2);

							monitor.subTask("Reloading Workbench");
							session.reloadActiveProfile();
							monitor.done();
							operationSucceeded = true;
						} catch (TigerstripeException e) {
							EclipsePlugin.log(e);
							operationSucceeded = false;
						}
					}
				};

				IWorkbench wb = PlatformUI.getWorkbench();
				IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
				Shell shell = win != null ? win.getShell() : null;

				try {
					ProgressMonitorDialog dialog = new ProgressMonitorDialog(
							shell);
					dialog.run(true, false, op);
				} catch (InterruptedException e) {
					EclipsePlugin.log(e);
				} catch (InvocationTargetException e) {
					EclipsePlugin.log(e);
				}

				if (operationSucceeded) {
					if (rollbackCreated) {
					}
					MessageDialog
							.openInformation(getShell(),
									"Active Profile Rollback",
									"The active profile was successfully reset to factory defaults");
				} else {
					MessageDialog
							.openError(
									getShell(),
									"Error while reseting active Profile",
									"An error occured while trying to reset the active profile to factory defaults:\n"
											+ "Please check the Error Log for more details");
				}
			}
		}
	}

	private boolean rollbackCreated = false;

	private boolean operationSucceeded = false;

	private void deployProfile() {
		if (LicensedAccess.getWorkbenchProfileRole() != TSWorkbenchProfileRole.CREATE_EDIT
				&& LicensedAccess.getWorkbenchProfileRole() != TSWorkbenchProfileRole.DEPLOY_UNDEPLOY) {
			String errMessage = "You cannot deploy a new profile\n\n"
					+ "Your Tigerstripe license has insufficient privileges for this operation, "
					+ "please contact Tigerstripe if you wish to be able to deploy "
					+ "workbench profiles";
			MessageDialog.openError(getShell(), "Deploy Profile Error",
					errMessage);
		} else {

			FileDialog dialog = new FileDialog(getShell());
			dialog.setFilterExtensions(new String[] { "*.wbp" });
			dialog.setText("Select profile");
			String file = dialog.open();
			if (file != null) {

				final File srcFile = new File(file);
				if (!srcFile.exists() || !srcFile.canRead()) {
					MessageDialog.openError(getShell(), "Profile Deploy Error",
							"Can't deploy profile " + file
									+ ": unable to read file.");
					return;
				} else {
					try {
						final IWorkbenchProfile handle = TigerstripeCore
								.getWorkbenchProfileSession()
								.getWorkbenchProfileFor(
										srcFile.getAbsolutePath());

						ProfileValidator validator = new ProfileValidator();
						MessageList msgList = new MessageList();
						boolean isClean = validator.validate(handle, msgList);

						if (!msgList.isEmpty()) {
							String title;
							if (msgList.hasNoError()) {
								title = "Profile Details";
							} else {
								title = "Profile contains errors";

							}

							MessageListDialog msgDialog = new MessageListDialog(
									getShell(), msgList, title);

							msgDialog.create();
							if (!msgList.hasNoError()) {
								msgDialog.disableOKButton();
							}

							if (msgDialog.open() == Window.CANCEL)
								return;
						}

						if (MessageDialog
								.openConfirm(
										getShell(),
										"Save as Active Profile",
										"You are about to set this profile ('"
												+ handle.getName()
												+ "') as the active profile. All open editors will be closed.\n\nDo you want to continue?\n\n(You will be able to rollback to the current active profile).  ")) {

							IRunnableWithProgress op = new IRunnableWithProgress() {
								public void run(IProgressMonitor monitor) {
									try {
										monitor.beginTask(
												"Deploying new Active Profile",
												10);

										monitor.subTask("Closing all editors");
										EclipsePlugin.closeAllEditors(true,
												true, false, false, true);
										monitor.worked(2);

										IWorkbenchProfileSession session = TigerstripeCore
												.getWorkbenchProfileSession();
										monitor.subTask("Creating Profile");

										rollbackCreated = session
												.saveAsActiveProfile(handle);
										monitor.worked(2);

										monitor.subTask("Reloading workbench");
										session.reloadActiveProfile();
										monitor.done();

										operationSucceeded = true;
										if (rollbackCreated) {
										}

									} catch (TigerstripeException e) {
										EclipsePlugin.log(e);
										operationSucceeded = false;
									}
								}
							};

							IWorkbench wb = PlatformUI.getWorkbench();
							IWorkbenchWindow win = wb
									.getActiveWorkbenchWindow();
							Shell shell = win != null ? win.getShell() : null;

							try {
								ProgressMonitorDialog pDialog = new ProgressMonitorDialog(
										shell);
								pDialog.run(true, false, op);
							} catch (InterruptedException e) {
								EclipsePlugin.log(e);
							} catch (InvocationTargetException e) {
								EclipsePlugin.log(e);
							}

							String rollbackStr = "";
							if (rollbackCreated) {
								rollbackStr = "\n\n(A rollback file was successfully created)";
							}

							if (operationSucceeded) {
								MessageDialog
										.openInformation(
												getShell(),
												"Success",
												"Profile '"
														+ handle.getName()
														+ "' is now the active profile for this instance of Tigerstripe Workbench.\n\nWorkbench is now ready to be used with this new active profile."
														+ rollbackStr);

							} else {
								MessageDialog
										.openError(
												getShell(),
												"Error while setting active Profile",
												"An error occured while trying to set the active profile.\n"
														+ "Please check the Error Log for more details");
							}

						}

					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}

				}
			}

		}
	}
}
