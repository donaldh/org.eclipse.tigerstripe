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
package org.eclipse.tigerstripe.workbench.eclipse.wizards.generate;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.TSRuntimeBasedWizardPage;
import org.eclipse.tigerstripe.workbench.internal.core.generation.M0GenerationUtils;
import org.eclipse.tigerstripe.workbench.internal.core.generation.M0RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginHousing;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NewTigerstripeM0RunWizardPage extends TSRuntimeBasedWizardPage {

	private final static int INDENT = 20;

	private static final String PAGE_NAME = "NewTigerstripeM0RunWizardPage"; //$NON-NLS-1$

	private Button[] pluginSelectionButtons;

	private boolean controlsCreated = false;

	private String[] buttonNames;

	private M0RunConfig runConfig;

	private boolean generateControlsInitialized = false;

	private boolean pluginsControlsInitialized = false;

	/**
	 * Creates a new <code>NewPackageWizardPage</code>
	 */
	public NewTigerstripeM0RunWizardPage() {
		super(PAGE_NAME);

		setTitle("Instance Driven Generation (M0-Level)");
		setDescription("This wizard will trigger M0 Generators.");

	}

	// -------- Initialization ---------

	/**
	 * The wizard owning this page is responsible for calling this method with
	 * the current selection. The selection is used to initialize the fields of
	 * the wizard page.
	 * 
	 * @param selection
	 *            used to initialize the fields
	 */
	public void init(IStructuredSelection selection) {

		IStructuredSelection lSelection = selection;
		// basically ignore the selection, since we will use the project in
		// focus
		IProject selectedProject = EclipsePlugin.getProjectInFocus();
		if (selectedProject != null) {
			lSelection = new StructuredSelection(selectedProject);
		}
		IJavaElement jelem = getInitialJavaElement(lSelection);

		initTSRuntimeContext(jelem);
		initContainerPage(jelem);
		initPage(jelem);
	}

	private void initPage(IJavaElement jElement) {

		initRunConfigFromContext();

		initFromContext();
		updatePage();
	}

	/**
	 * If no run config is defined, build one up from the context. Otherwise,
	 * just re-use
	 * 
	 */
	private void initRunConfigFromContext() {
		// TODO: we may remember the settings from one run to the next
		try {
			runConfig = (M0RunConfig) RunConfig.newGenerationConfig(
					getTSProject(), RunConfig.M0);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	// -------- UI Creation ---------

	/*
	 * @see WizardPage#createControl
	 */
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createContainerControls(composite, nColumns);
		createSeparator(composite, nColumns);

		// createLabel(composite, nColumns);
		// createOutputDirControls(composite, nColumns);
		createGenerationControls(composite, nColumns);
		// createFacetControls(composite, nColumns);
		createPluginsControls(composite, nColumns);
		createExplanation(composite, nColumns);

		controlsCreated = true;

		setControl(composite);

		Dialog.applyDialogFont(composite);
		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
		updatePage();
	}

	private void createExplanation(Composite composite, int nColumns) {
		Label blank = new Label(composite, SWT.NULL);
		blank.setText(" ");
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.BEGINNING;
		gd.heightHint = 15;
		gd.horizontalSpan = nColumns;
		blank.setLayoutData(gd);

		Link link = new Link(composite, SWT.NONE);
		link
				.setText("To change the generation details, please edit the corresponding project.");
		link.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false,
				false, nColumns, 1));
	}

	/**
	 * Creates a separator line. Expects a <code>GridLayout</code> with at
	 * least 1 column.
	 * 
	 * @param composite
	 *            the parent composite
	 * @param nColumns
	 *            number of columns to span
	 */
	@Override
	protected void createSeparator(Composite composite, int nColumns) {
		(new Separator(SWT.SEPARATOR | SWT.HORIZONTAL)).doFillIntoGrid(
				composite, nColumns, convertHeightInCharsToPixels(1));
	}

	protected void createPluginsControls(Composite composite, int nColumns) {

		Group pluginsGroup = new Group(composite, SWT.V_SCROLL);
		GridLayout layout = new GridLayout(1, true);
		pluginsGroup.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		gd.heightHint = 85;
		gd.horizontalSpan = nColumns;
		pluginsGroup.setLayoutData(gd);
		pluginsGroup.setText("Plugins");

		ScrolledComposite scC = new ScrolledComposite(pluginsGroup,
				SWT.V_SCROLL);
		scC.setExpandHorizontal(true);
		scC.setExpandVertical(true);
		gd = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL
				| GridData.GRAB_VERTICAL);
		scC.setLayoutData(gd);

		final Composite content = new Composite(scC, SWT.NONE);
		scC.setContent(content);
		layout = new GridLayout();
		layout.numColumns = 1;
		content.setLayout(layout);

		List<String> labels = new ArrayList<String>();
		for (PluginHousing housing : M0GenerationUtils.m0PluginHousings()) {
			labels.add(housing.getLabel());
		}
		buttonNames = labels.toArray(new String[labels.size()]);

		pluginSelectionButtons = new Button[buttonNames.length];
		for (int index = 0; index < pluginSelectionButtons.length; index++) {
			pluginSelectionButtons[index] = new Button(content, SWT.CHECK);
			pluginSelectionButtons[index].setText(buttonNames[index]);

			gd = new GridData(GridData.FILL_HORIZONTAL
					| GridData.GRAB_HORIZONTAL);
			gd.horizontalIndent = INDENT * 2;
			pluginSelectionButtons[index].setLayoutData(gd);

			final int thisIndex = index;
			pluginSelectionButtons[index]
					.addSelectionListener(new SelectionListener() {
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {
							widgetSelected(e);
						}

						@Override
						public void widgetSelected(SelectionEvent e) {
							runConfig.getPluginConfigs()[thisIndex]
									.setEnabled(pluginSelectionButtons[thisIndex]
											.getSelection());
						}
					});
		}
		scC.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		pluginsControlsInitialized = true;
	}

	private Text outputDirText;

	protected void createGenerationControls(Composite composite, int nColumns) {
		Group generateGroup = new Group(composite, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		generateGroup.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		gd.horizontalSpan = nColumns;
		generateGroup.setLayoutData(gd);
		generateGroup.setText("Generation");

		Label l = new Label(generateGroup, SWT.NULL);
		l.setText("Output Directory");

		outputDirText = new Text(generateGroup, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		outputDirText.setLayoutData(gd);
		outputDirText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				runConfig
						.setOutputPath(new Path(outputDirText.getText().trim()));
			}

		});

		l = new Label(generateGroup, SWT.NULL);
		l.setText("Scope");

		Button thisDiagramContext = new Button(generateGroup, SWT.RADIO);
		thisDiagramContext.setText("This diagram only");
		thisDiagramContext.setSelection(true);
		thisDiagramContext.setEnabled(true);

		l = new Label(generateGroup, SWT.NULL);
		l.setText("");
		Button enclosingBlueprint = new Button(generateGroup, SWT.RADIO);
		enclosingBlueprint.setText("Enclosing Blueprint");
		enclosingBlueprint.setSelection(false);
		enclosingBlueprint.setEnabled(false);

		generateControlsInitialized = true;
	}

	protected ITigerstripeModelProject getTSProject()
			throws TigerstripeException {
		ITigerstripeModelProject handle = null;
		if (getTSRuntimeContext() != null) {
			handle = getTSRuntimeContext().getProjectHandle();
			return handle;
		}

		throw new TigerstripeException("Invalid project");
	}

	/**
	 * Perform any required update based on the runtime context
	 * 
	 */
	@Override
	protected void initFromContext() {
		updatePage();
	}

	/**
	 * Updates the page status by checking we're pointing at a valid TS Project
	 * with at least 1 profile enabled.
	 * 
	 */
	protected IStatus updatePage() {
		StatusInfo status = new StatusInfo();
		if (!controlsCreated)
			return status;

		String message = null;

		// This is called early on, even before buttons are created by the
		// parent so we need to check for !null

		updateGenerateControls();
		updatePluginsControls();

		if (message != null) {
			setErrorMessage(message);
			status.setError(message);
		} else {
			setErrorMessage(null);
		}

		setPageComplete(getErrorMessage() == null);
		return status;
	}

	protected void updatePluginsControls() {
		if (pluginsControlsInitialized) {
			String message;
			// Update profile selection checks and check one is selected.
			for (int i = 0; i < buttonNames.length; i++) {
				Button button = pluginSelectionButtons[i];
				button.setEnabled(false);
				button.setSelection(false);
			}

			IPluginConfig[] refs = runConfig.getPluginConfigs();
			boolean oneAtleastIsEnabled = false;
			for (int i = 0; i < refs.length; i++) {
				for (int j = 0; j < buttonNames.length; j++) {
					if (buttonNames[j].equals(((PluginConfig) refs[i])
							.getLabel())) {
						pluginSelectionButtons[j].setSelection(refs[i]
								.isEnabled());
						oneAtleastIsEnabled = oneAtleastIsEnabled
								| refs[i].isEnabled();
					}
				}
			}
			if (!oneAtleastIsEnabled) {
				message = "At least one plugin must be selected.";
				setErrorMessage(message);
			}
		}
	}

	protected void updateGenerateControls() {
		if (generateControlsInitialized) {
			outputDirText.setText(runConfig.getOutputPath().toOSString());
		}
	}

	@Override
	protected IStatus containerChanged() {
		IStatus result = super.containerChanged();
		if (result.isOK()) {
			String str = getPackageFragmentRootText();
			IPath path = new Path(str);
			IResource res = getWorkspaceRoot().findMember(path);

			int resType = res.getType();
			if (resType == IResource.PROJECT || resType == IResource.FOLDER) {
				IProject proj = res.getProject();
				IJavaProject jproject = JavaCore.create(proj);

				initTSRuntimeContext(jproject);
			}
			result = updatePage();
		}

		return result;
	}

	public M0RunConfig getM0RunConfig() {
		return this.runConfig;
	}
}