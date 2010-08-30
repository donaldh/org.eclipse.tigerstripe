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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.generate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IGlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.generation.M1RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginHousing;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginManager;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggableHousing;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.GlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.util.MatchedConfigHousing;
import org.eclipse.tigerstripe.workbench.plugins.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.plugins.OsgiPluggablePluginSection;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.plugins.PluggablePluginSection;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.ColorUtils;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.TSRuntimeContext;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.TSRuntimeBasedWizardPage;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NewTigerstripeRunWizardPage extends TSRuntimeBasedWizardPage {

	private final static int INDENT = 20;

	private Map<String, Collection<PluggableHousing>> housingNameMap = new HashMap<String, Collection<PluggableHousing>>();

	private final static String USE_CURRENTFACET_BASE = "Use current facet only";

	private static final String PAGE_NAME = "NewTigerstripeRunWizardPage"; //$NON-NLS-1$

	private Button[] generatorSelectionButtons;

	private boolean controlsCreated = false;

	private String[] buttonNames;

	private List<PluginHousing> housings;

	private M1RunConfig runConfig;

	private boolean facetControlsInitialized = false;

	private boolean generateControlsInitialized = false;

	private boolean pluginsControlsInitialized = false;

	private Table selectedFacetsTable;

	private TableViewer selectedFacetsTableViewer;

	private Button mergeFacetsButton;

	private Button usePluginConfigFacetsButton;

	/**
	 * Creates a new <code>NewPackageWizardPage</code>
	 */
	public NewTigerstripeRunWizardPage() {
		super(PAGE_NAME);

		setTitle("Tigerstripe Generation");
		setDescription("This wizard will generate the Service Contracts for a Tigerstripe project.");
		

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
		if (runConfig == null) {
			try {
				runConfig = (M1RunConfig) RunConfig.newGenerationConfig(
						getTSProject(), RunConfig.M1);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
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
		createFacetControls(composite, nColumns);
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

		// Need to know what plugins are registered before creating the
		// Controls.
		// This should be dynamic and derived from the list of "registered"
		// plugins
		PluginManager manager = PluginManager.getManager();
		housings = manager.getRegisteredHousings();
		ArrayList<String> labels = new ArrayList<String>();
		
		
		if (! PluginManager.isOsgiVersioning()){
			for (Iterator it = housings.iterator(); it.hasNext();) {
				PluginHousing housing = (PluginHousing) it.next();
				if (housing.getCategory() == IPluginConfig.GENERATE_CATEGORY
						&& housing.getPluginNature() != EPluggablePluginNature.M0) {
					labels.add(housing.getLabel());
				}
			}
		} else {
			
			//Map<String, Collection<PluggableHousing>> map = new HashMap<String, Collection<PluggableHousing>>();
			for (PluggableHousing housing : manager.getRegisteredPluggableHousings()) {
				String name = housing.getPluginName();
				if (housingNameMap.containsKey(name)){
					housingNameMap.get(name).add(housing);
				} else {
					Collection<PluggableHousing> phs = new ArrayList<PluggableHousing>();
					phs.add(housing);
					housingNameMap.put(name, phs);
				}
			}
			for (String name : housingNameMap.keySet()){
				labels.add(name);
			}
		}
		
		
		
		
		
		
		buttonNames = labels.toArray(new String[labels.size()]);

		generatorSelectionButtons = new Button[buttonNames.length];
		for (int index = 0; index < generatorSelectionButtons.length; index++) {
			generatorSelectionButtons[index] = new Button(content, SWT.CHECK);
			generatorSelectionButtons[index].setEnabled(false);
			generatorSelectionButtons[index].setText(buttonNames[index]);

			gd = new GridData(GridData.FILL_HORIZONTAL
					| GridData.GRAB_HORIZONTAL);
			gd.horizontalIndent = INDENT * 2;
			generatorSelectionButtons[index].setLayoutData(gd);
		}
		scC.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		pluginsControlsInitialized = true;
	}

	private Text outputDirText;

	private Button generateModules;

	private Button generateRefProjects;

	private Button processUseCases;

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
		l.setText("Output Directory:");

		outputDirText = new Text(generateGroup, SWT.NULL);
		outputDirText.setEditable(false);
		outputDirText.setEnabled(false);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		outputDirText.setLayoutData(gd);

		generateModules = new Button(generateGroup, SWT.CHECK);
		generateModules.setText("Generate included modules");
		generateModules.setEnabled(false);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gd.horizontalIndent = INDENT;
		gd.horizontalSpan = 2;
		generateModules.setLayoutData(gd);

		generateRefProjects = new Button(generateGroup, SWT.CHECK);
		generateRefProjects.setText("Generate referenced projects");
		generateRefProjects.setEnabled(false);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gd.horizontalIndent = INDENT;
		gd.horizontalSpan = 2;
		generateRefProjects.setLayoutData(gd);

		processUseCases = new Button(generateGroup, SWT.CHECK);
		processUseCases.setText("Process Use Cases");
		processUseCases.setEnabled(false);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gd.horizontalIndent = INDENT;
		gd.horizontalSpan = 2;
		processUseCases.setLayoutData(gd);

		generateControlsInitialized = true;
	}

	private Button useCurrentFacet;

	private Button useSelectedFacets;

	private Button ignoreFacets;

	class FacetLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public String getText(Object obj) {
			IFacetReference dep = (IFacetReference) obj;
			if (dep.isAbsolute()) {
				try {
					return dep.getURI().toASCIIString();
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
					return "<unknown>";
				}
			} else
				return dep.getProjectRelativePath();
		}
	}

	private void createFacetControls(Composite composite, int nColumns) {
		Group facetGroup = new Group(composite, SWT.NULL);
		GridLayout layout = new GridLayout(1, true);
		facetGroup.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		gd.horizontalSpan = nColumns;
		facetGroup.setLayoutData(gd);
		facetGroup.setText("Facets");

		ignoreFacets = new Button(facetGroup, SWT.CHECK);
		ignoreFacets.setText("Ignore facets");
		gd = new GridData();
		gd.horizontalSpan = 2;
		ignoreFacets.setLayoutData(gd);
		ignoreFacets.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				runConfig.setIgnoreFacets(ignoreFacets.getSelection());
				updatePage();
			}
		});

		useCurrentFacet = new Button(facetGroup, SWT.RADIO);
		useCurrentFacet.setText(USE_CURRENTFACET_BASE);
		gd = new GridData();
		gd.horizontalIndent = INDENT;
		useCurrentFacet.setLayoutData(gd);
		useCurrentFacet.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				runConfig.setUsePluginConfigFacets(usePluginConfigFacetsButton
						.getSelection());
				runConfig.setUseCurrentFacet(useCurrentFacet.getSelection());
				runConfig.setUseProjectFacets(useSelectedFacets.getSelection());
				updatePage();
			}
		});

		useSelectedFacets = new Button(facetGroup, SWT.RADIO);
		useSelectedFacets.setText("Use project facets.");
		gd = new GridData();
		gd.horizontalIndent = INDENT;
		useSelectedFacets.setLayoutData(gd);
		useSelectedFacets.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				runConfig.setUsePluginConfigFacets(usePluginConfigFacetsButton
						.getSelection());
				runConfig.setUseCurrentFacet(useCurrentFacet.getSelection());
				runConfig.setUseProjectFacets(useSelectedFacets.getSelection());
				updatePage();
			}
		});

		selectedFacetsTable = new Table(facetGroup, SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL
				| GridData.GRAB_VERTICAL);
		gd.horizontalIndent = INDENT * 2;
		gd.horizontalSpan = 2;
		selectedFacetsTable.setLayoutData(gd);
		selectedFacetsTable.setForeground(ColorUtils.LIGHT_GREY);

		selectedFacetsTableViewer = new TableViewer(selectedFacetsTable);
		selectedFacetsTableViewer
				.setContentProvider(new IStructuredContentProvider() {
					public Object[] getElements(Object inputElement) {
						ITigerstripeModelProject project = (ITigerstripeModelProject) inputElement;
						try {
							return project.getFacetReferences();
						} catch (TigerstripeException e) {
							return new IFacetReference[0];
						}
					}

					public void dispose() {
					}

					public void inputChanged(Viewer viewer, Object oldInput,
							Object newInput) {
					}
				});
		selectedFacetsTableViewer.setLabelProvider(new FacetLabelProvider());
		try {
			selectedFacetsTableViewer.setInput(getTSProject());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		mergeFacetsButton = new Button(facetGroup, SWT.CHECK);
		mergeFacetsButton.setText("Merge facets for generation");
		gd = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL
				| GridData.GRAB_VERTICAL);
		gd.horizontalIndent = INDENT * 2;
		gd.horizontalSpan = 2;
		mergeFacetsButton.setLayoutData(gd);
		mergeFacetsButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				runConfig.setMergeFacets(mergeFacetsButton.getSelection());
				updatePage();
			}
		});

		usePluginConfigFacetsButton = new Button(facetGroup, SWT.RADIO);
		usePluginConfigFacetsButton.setText("Use &'per plugin&' facets.");
		gd = new GridData();
		gd.horizontalIndent = INDENT;
		usePluginConfigFacetsButton.setLayoutData(gd);
		usePluginConfigFacetsButton
				.addSelectionListener(new SelectionListener() {
					public void widgetDefaultSelected(SelectionEvent e) {
					}

					public void widgetSelected(SelectionEvent e) {
						runConfig
								.setUsePluginConfigFacets(usePluginConfigFacetsButton
										.getSelection());
						runConfig.setUseCurrentFacet(useCurrentFacet
								.getSelection());
						runConfig.setUseProjectFacets(useSelectedFacets
								.getSelection());
						updatePage();
					}
				});

		facetControlsInitialized = true;
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

		TSRuntimeContext context = getTSRuntimeContext();

		if (context == null || !context.isValidProject()) {
			message = "Please select a valid Tigerstripe project.";
			if (generatorSelectionButtons != null) {
				// Update profile selection checks and check one is selected.
				for (int i = 0; i < buttonNames.length; i++) {
					Button button = generatorSelectionButtons[i];
					button.setEnabled(false);
					button.setSelection(false);
				}
			}
		} else {
			// This is called early on, even before buttons are created by the
			// parent
			// so we need to check for !null

			updateGenerateControls();
			updateFacetsControls();
			updatePluginsControls();

		}

		if (getErrorMessage() == null) {
			if (message != null) {
				setErrorMessage(message);
				status.setError(message);
			} else {
				setErrorMessage(null);
			}
		}

		setPageComplete(getErrorMessage() == null);
		return status;
	}

	protected void updatePluginsControls() {
		if (pluginsControlsInitialized) {
			String message;
			// Update profile selection checks and check one is selected.
			for (int i = 0; i < buttonNames.length; i++) {
				Button button = generatorSelectionButtons[i];
				button.setEnabled(false);
				button.setSelection(false);
			}

			try {
				ITigerstripeModelProject handle = getTSRuntimeContext()
						.getProjectHandle();
				if (handle != null) {
					IPluginConfig[] refs = handle.getPluginConfigs();
					
					
					boolean oneAtleastIsEnabled = false;
					if (! PluginManager.isOsgiVersioning()){
						for (int i = 0; i < refs.length; i++) {
							// oneAtleastIsEnabled = oneAtleastIsEnabled
							if (refs[i].isEnabled()
									&& refs[i].getCategory() == IPluginConfig.GENERATE_CATEGORY
									&& refs[i].getPluginNature() != EPluggablePluginNature.M0) {
								for (int j = 0; j < buttonNames.length; j++) {
									if (buttonNames[j]
									                .equals(((PluginConfig) refs[i])
									                		.getLabel())) {
										generatorSelectionButtons[j]
										                          .setSelection(refs[i].isEnabled());
										oneAtleastIsEnabled = true;
									}
								}
							}
						}
					} else {
						for (String name : housingNameMap.keySet()){
							MatchedConfigHousing mch = PluginManager.getManager().resolve(housingNameMap.get(name), refs);
							if (mch.getConfig()!= null && mch.getConfig().isEnabled()
									&& mch.getConfig().getCategory() == IPluginConfig.GENERATE_CATEGORY
									&& mch.getConfig().getPluginNature() != EPluggablePluginNature.M0) {
								for (int j = 0; j < buttonNames.length; j++) {
									if (buttonNames[j]
									                .equals(((PluginConfig) mch.getConfig())
									                		.getPluginName())) {
										String newButtonName = name + " "+ mch.getHousing().getVersion();
										
										generatorSelectionButtons[j].setText(newButtonName);
										generatorSelectionButtons[j]
										                          .setSelection(((PluginConfig) mch.getConfig()).isEnabled());

										oneAtleastIsEnabled = true;
									}
								}
							}
						}

					}
					if (!oneAtleastIsEnabled) {
						message = "At least one plugin must be selected.";
						setErrorMessage(message);
					}
				}
			} catch (TigerstripeException e) {
				// The wizard is currently not pointing at a valid TS
				// project
				// We ignore
			}

		}
	}

	protected void updateGenerateControls() {
		if (generateControlsInitialized) {
			try {
				outputDirText.setText(getTSProject().getProjectDetails()
						.getOutputDirectory());
				ignoreFacets.setSelection(runConfig.isIgnoreFacets());
				generateModules
						.setSelection("true"
								.equalsIgnoreCase(getTSProject()
										.getProjectDetails()
										.getProperty(
												IProjectDetails.GENERATE_MODULES,
												IProjectDetails.GENERATE_MODULES_DEFAULT)));
				generateRefProjects
						.setSelection("true"
								.equalsIgnoreCase(getTSProject()
										.getProjectDetails()
										.getProperty(
												IProjectDetails.GENERATE_REFPROJECTS,
												IProjectDetails.GENERATE_REFPROJECTS_DEFAULT)));
				processUseCases
						.setSelection("true"
								.equalsIgnoreCase(getTSProject()
										.getProjectDetails()
										.getProperty(
												IProjectDetails.PROCESS_USECASES,
												IProjectDetails.PROCESS_USECASES_DEFAULT)));
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	protected void updateFacetsControls() {

		int n = 0;
		int pluginFacets = 0;
		try {
			IPluginConfig[] configs = getTSProject().getPluginConfigs();
			for (IPluginConfig config : configs) {
				if (config.getFacetReference() != null)
					pluginFacets++;
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		if (facetControlsInitialized) {
			try {
				n = getTSProject().getFacetReferences().length;
				IFacetReference activeFacet = getTSProject().getActiveFacet();

				if (activeFacet == null && n == 0 && pluginFacets == 0) {
					// no facet whatsoever
					useCurrentFacet.setEnabled(false);
					useCurrentFacet.setSelection(false);
					useSelectedFacets.setEnabled(false);
					useSelectedFacets.setSelection(false);
					ignoreFacets.setEnabled(false);
					ignoreFacets.setSelection(false);
					mergeFacetsButton.setEnabled(false);
					mergeFacetsButton.setSelection(false);
					selectedFacetsTableViewer.getTable().setEnabled(false);
					usePluginConfigFacetsButton.setEnabled(false);
					usePluginConfigFacetsButton.setSelection(false);
					return;
				}

				if (activeFacet != null && activeFacet.canResolve()) {
					useCurrentFacet.setEnabled(!runConfig.isIgnoreFacets());
					useCurrentFacet.setSelection(runConfig.isUseCurrentFacet());
					useCurrentFacet.setText(USE_CURRENTFACET_BASE + "("
							+ activeFacet.resolve().getName() + ")");
				} else {
					useCurrentFacet.setEnabled(false);
					useCurrentFacet.setSelection(false);
					useCurrentFacet.setText(USE_CURRENTFACET_BASE);
				}
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}

			if (n != 0) {
				useSelectedFacets.setEnabled(!runConfig.isIgnoreFacets());
				useSelectedFacets.setSelection(runConfig.isUseProjectFacets());
				mergeFacetsButton.setEnabled(!runConfig.isIgnoreFacets());
				mergeFacetsButton.setSelection(runConfig.isMergeFacets());
				selectedFacetsTableViewer.getTable().setEnabled(
						!runConfig.isIgnoreFacets());
			} else {
				useSelectedFacets.setEnabled(false);
				useSelectedFacets.setSelection(false);
				mergeFacetsButton.setEnabled(false);
				mergeFacetsButton.setSelection(false);
				selectedFacetsTableViewer.getTable().setEnabled(false);
			}

			if (pluginFacets != 0) {
				usePluginConfigFacetsButton.setEnabled(!runConfig
						.isIgnoreFacets());
				usePluginConfigFacetsButton.setSelection(runConfig
						.isUsePluginConfigFacets());
			} else {
				usePluginConfigFacetsButton.setEnabled(false);
				usePluginConfigFacetsButton.setSelection(false);
			}

			if (runConfig.isIgnoreFacets()) {
				// if (runConfig.isUseCurrentFacet()) {
				// selectedFacetsTable.setEnabled(false);
				// mergeFacetsButton.setEnabled(false);
				// usePluginConfigFacetsButton.setEnabled(false);
				// } else if (runConfig.isUsePluginConfigFacets()) {
				// selectedFacetsTable.setEnabled(false);
				// mergeFacetsButton.setEnabled(false);
				// usePluginConfigFacetsButton.setEnabled(true);
				// } else {
				// selectedFacetsTable.setEnabled(true);
				// mergeFacetsButton.setEnabled(true);
				// usePluginConfigFacetsButton.setEnabled(false);
				// }
				// } else {
				useCurrentFacet.setEnabled(false);
				selectedFacetsTable.setEnabled(false);
				mergeFacetsButton.setEnabled(false);
				usePluginConfigFacetsButton.setEnabled(false);
			}
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
	
	

	public M1RunConfig getRunConfig() {
		return this.runConfig;
	}
}