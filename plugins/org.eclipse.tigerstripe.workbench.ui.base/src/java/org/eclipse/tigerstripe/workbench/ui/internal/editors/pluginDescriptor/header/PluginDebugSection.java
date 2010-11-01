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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.header;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.LicensedAccess;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.TSWorkbenchPluggablePluginRole;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.generator.GeneratorDescriptorSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.GeneratorDeploymentUIHelper;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class PluginDebugSection extends GeneratorDescriptorSectionPart {

	public PluginDebugSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR);
		setTitle("Testing");
		createContent();
	}

	@Override
	protected void createContent() {
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		getSection().setLayout(layout);

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		getSection().setLayoutData(td);

		createProjectComponents(getBody(), getToolkit());

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	// used with progress dialogs
	private boolean operationSucceeded = false;

	private String deploymentPath = null;

	private void createProjectComponents(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		FormText rtext = toolkit.createFormText(parent, true);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		rtext.setLayoutData(td);
		StringBuffer buf = new StringBuffer();

		buf.append("<form>");
		buf
				.append("<li><a href=\"deploy\">Package and deploy this generator.</a></li>");
		buf.append("<li><a href=\"undeploy\">Un-deploy this generator</a></li>");
		buf.append("</form>");
		rtext.setText(buf.toString(), true, false);
		rtext.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				IRunnableWithProgress op = null;
				operationSucceeded = false;
				if ("deploy".equals(e.getHref())) {

					if (projectHasErrors())
						return;

					if (LicensedAccess.getWorkbenchPluggablePluginRole() != TSWorkbenchPluggablePluginRole.CREATE_EDIT) {
						String errMessage = "You cannot package and deploy a new Tigerstripe generator\n\n"
								+ "Your Tigerstripe license has insufficient privileges for this operation, "
								+ "please contact Tigerstripe if you wish to be able to package and deploy "
								+ "generators";
						MessageDialog.openError(getBody().getShell(),
								"Package/Deploy Generator Error", errMessage);
					} else {
						try {
							if (getPage().getEditor().isDirty()) {
								MessageDialog
										.openWarning(
												getBody().getShell(),
												"Un-saved changes",
												"This generator descriptor has un-saved changes.\n\nPlease save this file before trying to deploy this generator.");
								return;
							}

							final ITigerstripeGeneratorProject projectHandle = getIPluggablePluginProject();
							final GeneratorDeploymentUIHelper helper = new GeneratorDeploymentUIHelper();
							if (MessageDialog
									.openConfirm(
											getBody().getShell(),
											"Deploy new generator",
											"You are about to deploy this generator ('"
													+ projectHandle.getName()
													+ "'). All open editors will be closed.\nDo you want to continue?.  ")) {

								op = new IRunnableWithProgress() {
									public void run(IProgressMonitor monitor) {
										try {
											deploymentPath = helper.deploy(
													projectHandle, monitor)
													.toOSString();
											operationSucceeded = deploymentPath != null;
										} catch (TigerstripeException e) {
											operationSucceeded = false;
											EclipsePlugin.log(e);
										}
									}
								};

								IWorkbench wb = PlatformUI.getWorkbench();
								IWorkbenchWindow win = wb
										.getActiveWorkbenchWindow();
								Shell shell = win != null ? win.getShell()
										: null;

								try {
									ProgressMonitorDialog dialog = new ProgressMonitorDialog(
											shell);
									dialog.run(true, false, op);
								} catch (InterruptedException ee) {
									EclipsePlugin.log(ee);
								} catch (InvocationTargetException ee) {
									EclipsePlugin.log(ee.getTargetException());
								}

								if (operationSucceeded) {
									MessageDialog
											.openInformation(
													getBody().getShell(),
													projectHandle.getName()
															+ " Generator",
													"Generator '"
															+ projectHandle
																	.getName()
															+ "("
															+ projectHandle
																	.getProjectDetails()
																	.getVersion()
															+ ") was successfully deployed.\n ("
															+ deploymentPath
															+ ")");
								} else {
									MessageDialog
											.openError(
													getBody().getShell(),
													projectHandle.getName()
															+ " Generator",
													"Generator '"
															+ projectHandle
																	.getName()
															+ "("
															+ projectHandle
																	.getProjectDetails()
																	.getVersion()
															+ ") could not be deployed. See Error log for more details");
								}
							}
						} catch (TigerstripeException ee) {
							EclipsePlugin.log(ee);
						}
					}
				} else if ("undeploy".equals(e.getHref())) {
					if (LicensedAccess.getWorkbenchPluggablePluginRole() != TSWorkbenchPluggablePluginRole.CREATE_EDIT) {
						String errMessage = "You cannot undeploy a Tigerstripe generator\n\n"
								+ "Your Tigerstripe license has insufficient privileges for this operation, "
								+ "please contact Tigerstripe if you wish to be able to undeploy "
								+ "generators";
						MessageDialog.openError(getBody().getShell(),
								"Undeploy Generator Error", errMessage);
					} else {
						try {
							final ITigerstripeGeneratorProject projectHandle = getIPluggablePluginProject();
							final GeneratorDeploymentUIHelper helper = new GeneratorDeploymentUIHelper();
							op = new IRunnableWithProgress() {
								public void run(IProgressMonitor monitor) {
									try {
										deploymentPath = helper.unDeploy(
												projectHandle, monitor)
												.toOSString();
										operationSucceeded = deploymentPath != null;
									} catch (TigerstripeException e) {
										operationSucceeded = false;
										EclipsePlugin.log(e);
									}
								}
							};
							IWorkbench wb = PlatformUI.getWorkbench();
							IWorkbenchWindow win = wb
									.getActiveWorkbenchWindow();
							Shell shell = win != null ? win.getShell() : null;

							try {
								ProgressMonitorDialog dialog = new ProgressMonitorDialog(
										shell);
								dialog.run(true, false, op);
							} catch (InterruptedException ee) {
								EclipsePlugin.log(ee);
							} catch (InvocationTargetException ee) {
								EclipsePlugin.log(ee);
							}

							if (operationSucceeded)
								MessageDialog.openInformation(getBody()
										.getShell(), projectHandle.getName()
										+ " Generator", "Generator '"
										+ projectHandle.getName()
										+ "("
										+ projectHandle.getProjectDetails()
												.getVersion()
										+ ") was successfully un-deployed.\n ("
										+ deploymentPath + ")");
							else {
								MessageDialog
										.openError(
												getBody().getShell(),
												projectHandle.getName()
														+ " Generator",
												"An Error occured while trying to un-deploy generator '"
														+ projectHandle
																.getName()
														+ "("
														+ projectHandle
																.getProjectDetails()
																.getVersion()
														+ ")\n from ("
														+ deploymentPath
														+ ").\nSee Error log for more details.");
							}

						} catch (TigerstripeException ee) {
							EclipsePlugin.log(ee);
						}
					}
				}
			}
		});
	}
}
