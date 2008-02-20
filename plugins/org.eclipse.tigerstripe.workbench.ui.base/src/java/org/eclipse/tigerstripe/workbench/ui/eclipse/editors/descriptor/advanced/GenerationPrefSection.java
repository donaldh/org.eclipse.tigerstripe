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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.advanced;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.project.IAdvancedProperties;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.XSLFileSelectionDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.DescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.TigerstripeDescriptorSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.preferences.GenerationPreferencePage;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.part.FileEditorInput;

public class GenerationPrefSection extends TigerstripeDescriptorSectionPart {

	private Button generateContainedModules;

	private Button generateRefProjects;

	private Button ignoreFacets;

	private Button processUseCases;

	private Button useXslt;

	private Text xsltText;

	private Text processedUseCaseExt;

	private Text outputDirectory;

	private Button xsltBrowse;

	private Label extLabel;

	// Should a report be generated upon run (PROP_GENERATION_GenerateReport)
	private Button generateReportButton;

	// Should messages from code generation be logged during run
	// (PROP_GENERATION_LogMessages)
	private Button logMessagesButton;

	// Artifact elements without tags should be ignored
	// (PROP_MISC_IgnoreArtifactElementsWithoutTag)
	private Button ignoreElementsWithoutTagsButton;

	private Button applyDefaultPreferences;

	private boolean silentUpdate;

	public GenerationPrefSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR
				| ExpandableComposite.TWISTIE | ExpandableComposite.COMPACT);
		setTitle("Generation");
		createContent();
	}

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class DefaultPageListener implements ModifyListener,
			SelectionListener {

		public void modifyText(ModifyEvent e) {
			handleModifyText(e);
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}

	}

	private void handleWidgetSelected(SelectionEvent e) {
		ITigerstripeModelProject handle = getTSProject();

		if (e.getSource() == applyDefaultPreferences) {
			MessageBox dialog = new MessageBox(getSection().getShell(),
					SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			dialog.setText("Apply default preferences");
			dialog
					.setMessage("Do you really want to apply default preferences for Advanced Project Configuration?\nAll current values will be lost.");
			if (dialog.open() == SWT.YES) {
				try {
					applyAdvancedPropertiesDefaults();
				} catch (TigerstripeException ee) {
					Status status = new Status(
							IStatus.ERROR,
							TigerstripePluginConstants.PLUGIN_ID,
							222,
							"Error while applying default Advanced Preferences on Project",
							ee);
					EclipsePlugin.log(status);
				}
			}
		} else if (e.getSource() == generateReportButton) {
			try {
				handle.setAdvancedProperty(
						IAdvancedProperties.PROP_GENERATION_GenerateReport,
						String.valueOf(generateReportButton.getSelection()));
				markPageModified();
			} catch (TigerstripeException ee) {
				Status status = new Status(
						IStatus.ERROR,
						TigerstripePluginConstants.PLUGIN_ID,
						222,
						"Error while setting "
								+ IAdvancedProperties.PROP_GENERATION_GenerateReport
								+ " advanced property on Project "
								+ handle.getProjectLabel(), ee);
				EclipsePlugin.log(status);
			}

		} else if (e.getSource() == logMessagesButton) {
			try {
				handle.setAdvancedProperty(
						IAdvancedProperties.PROP_GENERATION_LogMessages, String
								.valueOf(logMessagesButton.getSelection()));
				markPageModified();
			} catch (TigerstripeException ee) {
				Status status = new Status(
						IStatus.ERROR,
						TigerstripePluginConstants.PLUGIN_ID,
						222,
						"Error while setting "
								+ IAdvancedProperties.PROP_GENERATION_LogMessages
								+ " advanced property on Project "
								+ handle.getProjectLabel(), ee);
				EclipsePlugin.log(status);
			}

		} else if (e.getSource() == ignoreElementsWithoutTagsButton) {
			try {
				handle
						.setAdvancedProperty(
								IAdvancedProperties.PROP_MISC_IgnoreArtifactElementsWithoutTag,
								String.valueOf(ignoreElementsWithoutTagsButton
										.getSelection()));
				markPageModified();
			} catch (TigerstripeException ee) {
				Status status = new Status(
						IStatus.ERROR,
						TigerstripePluginConstants.PLUGIN_ID,
						222,
						"Error while setting "
								+ IAdvancedProperties.PROP_MISC_IgnoreArtifactElementsWithoutTag
								+ " advanced property on Project "
								+ handle.getProjectLabel(), ee);
				EclipsePlugin.log(status);
			}
		}
	}

	private void applyAdvancedPropertiesDefaults() throws TigerstripeException {
		ITigerstripeModelProject handle = getTSProject();

		IPreferenceStore store = EclipsePlugin.getDefault()
				.getPreferenceStore();

		IProjectDetails details = getTSProject().getProjectDetails();
		details.setProjectOutputDirectory(store
				.getString(GenerationPreferencePage.P_TARGETPATH));
		getTSProject().setProjectDetails(details);

		handle
				.setAdvancedProperty(
						IAdvancedProperties.PROP_GENERATION_GenerateReport,
						store
								.getString(IAdvancedProperties.PROP_GENERATION_GenerateReport));
		handle
				.setAdvancedProperty(
						IAdvancedProperties.PROP_GENERATION_LogMessages,
						store
								.getString(IAdvancedProperties.PROP_GENERATION_LogMessages));
		refresh();
		markPageModified();
	}

	protected void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			// when updating the form, the changes to all fields should be
			// ignored so that the form is not marked as dirty.

			// markPageModified();
		}
	}

	@Override
	protected void createContent() {
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		getSection().setLayoutData(td);

		DefaultPageListener listener = new DefaultPageListener();

		try {
			createApplyDefaultsButton(getBody(), getToolkit(), listener);
			createButtons(getBody(), getToolkit());
			createGenerationExpandable(getBody(), getToolkit(), listener);
		} catch (TigerstripeException ee) {
			Status status = new Status(IStatus.WARNING,
					TigerstripePluginConstants.PLUGIN_ID, 111,
					"Unexpected Exception", ee);
			EclipsePlugin.log(status);
		}
		createPreferenceLinkMsg(getBody(), getToolkit());
		updateForm();
		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createButtons(Composite parent, FormToolkit toolkit)
			throws TigerstripeException {
		GridData gd = null;

		GridLayout layout = new GridLayout();
		layout.numColumns = 8;
		parent.setLayout(layout);

		Label l = toolkit.createLabel(parent, "Target Directory:");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 1;
		l.setLayoutData(gd);
		outputDirectory = toolkit.createText(parent, "");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 7;
		outputDirectory.setLayoutData(gd);
		outputDirectory.setEnabled(!this.isReadonly());
		outputDirectory.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				ITigerstripeModelProject handle = getTSProject();
				if (!isSilentUpdate()) {
					try {
						IProjectDetails details = handle.getProjectDetails();
						details.setProjectOutputDirectory(outputDirectory
								.getText().trim());
						handle.setProjectDetails(details);
						markPageModified();
					} catch (TigerstripeException ee) {
						EclipsePlugin.log(ee);
					}
				}
			}

		});

		ignoreFacets = toolkit.createButton(parent,
				"Ignore selected Facets for generation", SWT.CHECK);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 8;
		ignoreFacets.setLayoutData(gd);
		ignoreFacets.setEnabled(!this.isReadonly());
		ignoreFacets.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (!isSilentUpdate()) {
					try {
						IProjectDetails projectDetails = getTSProject()
								.getProjectDetails();
						projectDetails.getProperties().put(
								IProjectDetails.IGNORE_FACETS,
								Boolean.toString(ignoreFacets.getSelection()));
						getTSProject().setProjectDetails(projectDetails);
						markPageModified();
					} catch (TigerstripeException ee) {
						EclipsePlugin.log(ee);
					}
				}
			}
		});

		generateContainedModules = toolkit.createButton(parent,
				"Generate module dependencies", SWT.CHECK);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 8;
		generateContainedModules.setLayoutData(gd);
		generateContainedModules.setEnabled(!this.isReadonly());
		generateContainedModules.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (!isSilentUpdate()) {
					try {
						IProjectDetails projectDetails = getTSProject()
								.getProjectDetails();
						projectDetails.getProperties().put(
								IProjectDetails.GENERATE_MODULES,
								Boolean.toString(generateContainedModules
										.getSelection()));
						getTSProject().setProjectDetails(projectDetails);
						markPageModified();
					} catch (TigerstripeException ee) {
						EclipsePlugin.log(ee);
					}
				}
			}
		});

		generateRefProjects = toolkit.createButton(parent,
				"Generate Referenced Projects", SWT.CHECK);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 8;
		generateRefProjects.setLayoutData(gd);
		generateRefProjects.setEnabled(!this.isReadonly());
		generateRefProjects.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (!isSilentUpdate()) {
					try {
						IProjectDetails projectDetails = getTSProject()
								.getProjectDetails();
						projectDetails.getProperties().put(
								IProjectDetails.GENERATE_REFPROJECTS,
								Boolean.toString(generateRefProjects
										.getSelection()));
						getTSProject().setProjectDetails(projectDetails);
						markPageModified();
					} catch (TigerstripeException ee) {
						EclipsePlugin.log(ee);
					}
				}
			}
		});

		processUseCases = toolkit.createButton(parent, "Process Use Cases",
				SWT.CHECK);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 8;
		processUseCases.setLayoutData(gd);
		processUseCases.setEnabled(!this.isReadonly());
		processUseCases.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (!isSilentUpdate()) {
					try {
						IProjectDetails projectDetails = getTSProject()
								.getProjectDetails();
						projectDetails.getProperties().put(
								IProjectDetails.PROCESS_USECASES,
								Boolean
										.toString(processUseCases
												.getSelection()));
						getTSProject().setProjectDetails(projectDetails);
						updateXsltText();
						markPageModified();
					} catch (TigerstripeException ee) {
						EclipsePlugin.log(ee);
					}
				}
			}
		});

		useXslt = toolkit.createButton(parent, "Post-process with XSLT",
				SWT.CHECK);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 8;
		gd.horizontalIndent = 15;
		useXslt.setLayoutData(gd);
		useXslt.setEnabled(!this.isReadonly());
		useXslt.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (!isSilentUpdate()) {
					try {
						IProjectDetails projectDetails = getTSProject()
								.getProjectDetails();
						projectDetails.getProperties().put(
								IProjectDetails.USECASE_USEXSLT,
								Boolean.toString(useXslt.getSelection()));
						updateXsltText();
						getTSProject().setProjectDetails(projectDetails);
						markPageModified();
					} catch (TigerstripeException ee) {
						EclipsePlugin.log(ee);
					}
				}
			}
		});

		xsltText = toolkit.createText(parent, "");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 7;
		gd.horizontalIndent = 15;
		xsltText.setLayoutData(gd);
		updateXsltText();
		xsltText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (!isSilentUpdate()) {
					try {
						IProjectDetails projectDetails = getTSProject()
								.getProjectDetails();
						projectDetails.getProperties()
								.put(IProjectDetails.USECASE_XSL,
										xsltText.getText());
						getTSProject().setProjectDetails(projectDetails);
						updateXsltText();
						markPageModified();
					} catch (TigerstripeException ee) {
						EclipsePlugin.log(ee);
					}
				}
			}

		});

		xsltBrowse = toolkit.createButton(parent, "Browse", SWT.PUSH);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 1;
		xsltBrowse.setLayoutData(gd);
		xsltBrowse.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				XSLFileSelectionDialog dialog = new XSLFileSelectionDialog(
						null, false, false);
				IFile baseFile = ((FileEditorInput) getPage().getEditorInput())
						.getFile();
				dialog.setInput(baseFile.getProject().getLocation().toFile());
				dialog.setDoubleClickSelects(true);
				dialog.setTitle("Select XSL Transformation");

				if (dialog.open() == Window.OK) {
					Object[] toAdd = dialog.getResult();
					for (int i = 0; i < toAdd.length; i++) {
						File file = (File) toAdd[i];

						try {
							String relative = Util.getRelativePath(file,
									getTSProject().getLocation().toFile());

							IPath path = new Path(relative);
							IResource res = baseFile.getParent().findMember(
									path);

							xsltText.setText(res.getProjectRelativePath()
									.toOSString());
						} catch (IOException ee) {
							EclipsePlugin.log(ee);
						}
					}
				}

			}
		});

		extLabel = toolkit.createLabel(parent, "File Extension");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 1;
		gd.horizontalIndent = 15;
		extLabel.setLayoutData(gd);

		processedUseCaseExt = toolkit.createText(parent, "");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 7;
		processedUseCaseExt.setLayoutData(gd);
		processedUseCaseExt
				.setToolTipText("The file extension of the resulting use case files.");
		processedUseCaseExt.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (!isSilentUpdate()) {
					try {
						IProjectDetails projectDetails = getTSProject()
								.getProjectDetails();
						projectDetails.getProperties().put(
								IProjectDetails.USECASE_PROC_EXT,
								processedUseCaseExt.getText());
						getTSProject().setProjectDetails(projectDetails);
						updateXsltText();
						markPageModified();
					} catch (TigerstripeException ee) {
						EclipsePlugin.log(ee);
					}
				}
			}

		});

		toolkit.paintBordersFor(parent);
	}

	protected void updateXsltText() {
		if (xsltText != null) {
			xsltText.setEnabled(processUseCases.getSelection()
					&& useXslt.getSelection() && !this.isReadonly());
		}

		if (xsltBrowse != null) {
			xsltBrowse.setEnabled(processUseCases.getSelection()
					&& useXslt.getSelection() && !this.isReadonly());
		}

		if (processedUseCaseExt != null) {
			processedUseCaseExt.setEnabled(processUseCases.getSelection()
					&& !this.isReadonly());
		}

		if (useXslt != null) {
			useXslt.setEnabled(processUseCases.getSelection()
					&& !this.isReadonly());
		}

		if (extLabel != null) {
			extLabel.setEnabled(processUseCases.getSelection()
					&& !this.isReadonly());
		}
	}

	/**
	 * Set the silent update flag
	 * 
	 * @param silentUpdate
	 */
	private void setSilentUpdate(boolean silentUpdate) {
		this.silentUpdate = silentUpdate;
	}

	/**
	 * If silent Update is set, the form should not consider the updates to
	 * fields.
	 * 
	 * @return
	 */
	private boolean isSilentUpdate() {
		return this.silentUpdate;
	}

	@Override
	public void commit(boolean onSave) {
		super.commit(onSave);
	}

	protected void markPageModified() {
		DescriptorEditor editor = (DescriptorEditor) getPage().getEditor();
		editor.pageModified();
	}

	@Override
	public void refresh() {
		updateForm();
	}

	protected void updateForm() {
		setSilentUpdate(true);
		try {

			ITigerstripeModelProject handle = getTSProject();

			outputDirectory.setText(handle.getProjectDetails()
					.getProjectOutputDirectory());

			ignoreFacets.setEnabled(handle.getFacetReferences().length != 0);
			ignoreFacets.setSelection("true".equalsIgnoreCase(handle
					.getProjectDetails().getProperty(
							IProjectDetails.IGNORE_FACETS,
							IProjectDetails.IGNORE_FACETS_DEFAULT)));
			generateContainedModules
					.setEnabled(handle.getDependencies().length > 1); // there
			// the
			// OSSJ
			// legacy
			// one
			// always...
			generateContainedModules.setSelection("true"
					.equalsIgnoreCase(handle.getProjectDetails().getProperty(
							IProjectDetails.GENERATE_MODULES,
							IProjectDetails.GENERATE_MODULES_DEFAULT)));
			generateRefProjects
					.setEnabled(handle.getReferencedProjects().length != 0);
			generateRefProjects.setSelection("true".equalsIgnoreCase(handle
					.getProjectDetails().getProperty(
							IProjectDetails.GENERATE_REFPROJECTS,
							IProjectDetails.GENERATE_REFPROJECTS_DEFAULT)));
			processUseCases.setSelection("true".equalsIgnoreCase(handle
					.getProjectDetails().getProperty(
							IProjectDetails.PROCESS_USECASES,
							IProjectDetails.PROCESS_USECASES_DEFAULT)));
			useXslt.setSelection("true".equalsIgnoreCase(handle
					.getProjectDetails().getProperty(
							IProjectDetails.USECASE_USEXSLT,
							IProjectDetails.USECASE_USEXSLT_DEFAULT)));
			xsltText.setText(handle.getProjectDetails().getProperty(
					IProjectDetails.USECASE_XSL,
					IProjectDetails.USECASE_XSL_DEFAULT));
			processedUseCaseExt.setText(handle.getProjectDetails().getProperty(
					IProjectDetails.USECASE_PROC_EXT,
					IProjectDetails.USECASE_PROC_EXT_DEFAULT));
			updateXsltText();

			if (((TigerstripeProjectHandle) handle).requiresDescriptorUpgrade()
					&& !getShownDescriptorUpgrade()) {
				setShownDescriptorUpgrade(true);
				MessageBox dialog = new MessageBox(getSection().getShell(),
						SWT.ICON_INFORMATION | SWT.OK);
				dialog.setText("Upgrade project descriptor");
				dialog
						.setMessage("Advanced properties are not properly set (in project '"
								+ handle.getProjectLabel()
								+ "').\nDefault preferences will be applied.");
				dialog.open();
				applyAdvancedPropertiesDefaults();
			}

			generateReportButton
					.setSelection("true"
							.equalsIgnoreCase(handle
									.getAdvancedProperty(IAdvancedProperties.PROP_GENERATION_GenerateReport)));

			logMessagesButton
					.setSelection("true"
							.equalsIgnoreCase(handle
									.getAdvancedProperty(IAdvancedProperties.PROP_GENERATION_LogMessages)));

		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		setSilentUpdate(false);
	}

	protected boolean getShownDescriptorUpgrade() {
		AdvancedConfigurationPage page = (AdvancedConfigurationPage) getPage();
		return page.getShownDescriptorUpgrade();
	}

	protected void setShownDescriptorUpgrade(boolean setShownDescriptorUpgrade) {
		AdvancedConfigurationPage page = (AdvancedConfigurationPage) getPage();
		page.setShownDescriptorUpgrade(setShownDescriptorUpgrade);
	}

	private void createApplyDefaultsButton(Composite composite,
			FormToolkit toolkit, DefaultPageListener listener) {
		applyDefaultPreferences = toolkit.createButton(composite,
				"Apply default preferences", SWT.PUSH);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 8;
		applyDefaultPreferences.setLayoutData(gd);
		applyDefaultPreferences.addSelectionListener(listener);
	}

	private void createGenerationExpandable(Composite composite,
			FormToolkit toolkit, DefaultPageListener listener) {

		Section lclSection = toolkit.createSection(composite,
				ExpandableComposite.TWISTIE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 8;
		lclSection.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 8;
		lclSection.setLayoutData(gd);
		lclSection.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				if (getManagedForm() != null)
					getManagedForm().reflow(true);
			}
		});
		lclSection.setText("Other Properties");

		Composite innerComposite = toolkit.createComposite(lclSection);
		layout = new GridLayout();
		layout.numColumns = 8;
		innerComposite.setLayout(layout);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 8;
		innerComposite.setLayoutData(gd);
		lclSection.setClient(innerComposite);

		generateReportButton = toolkit.createButton(innerComposite,
				"Generate report", SWT.CHECK);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 8;
		generateReportButton.setLayoutData(gd);
		generateReportButton.addSelectionListener(listener);

		logMessagesButton = toolkit.createButton(innerComposite,
				"Capture standard output/error during generation.", SWT.CHECK);
		logMessagesButton
				.setToolTipText("Redirects standard output/error to a 'generation.log' log file during generation");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 8;
		logMessagesButton.setLayoutData(gd);
		logMessagesButton.addSelectionListener(listener);

		getToolkit().paintBordersFor(innerComposite);

	}

	private void createPreferenceLinkMsg(Composite parent, FormToolkit toolkit) {
		GridData gd = null;

		String data = "<form><p></p><p>To set default values to be reused across multiple Tigerstripe Projects, please use the <a href=\"preferences\">Tigerstripe Generation Preferences</a> page.</p></form>";
		FormText rtext = toolkit.createFormText(parent, true);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 8;
		rtext.setLayoutData(gd);
		rtext.setText(data, true, false);
		rtext.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				PreferenceDialog dialog = new PreferenceDialog(getBody()
						.getShell(), EclipsePlugin.getDefault().getWorkbench()
						.getPreferenceManager());
				dialog.setSelectedNode(GenerationPreferencePage.PAGE_ID);
				dialog.open();
			}
		});
	}

}
