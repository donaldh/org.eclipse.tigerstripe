/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.patterns;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.sdk.internal.ISDKProvider;
import org.eclipse.tigerstripe.workbench.sdk.internal.SDKConstants;
import org.eclipse.tigerstripe.workbench.sdk.internal.ModelUpdater;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.PatternFileContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.dialogs.SelectContributerDialog;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.ConfigEditor;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.ExtensionSectionPart;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.wizards.AddPatternDefinitionWizard;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class PatternSection extends ExtensionSectionPart implements
		IFormPart {

	protected DetailsPart detailsPart;
	protected ISDKProvider provider;

	public PatternSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit,
			int style) {
		super(page, parent, toolkit,  null,
				ExpandableComposite.TWISTIE | style);
		setTitle("Contributions to '"+SDKConstants.PATTERNS_CREATION_PART+"'");
		getSection().marginWidth = 10;
		getSection().marginHeight = 5;
		getSection().clientVerticalSpacing = 4;

		createContent();

		updateMaster();
	}

	protected SashForm sashForm;

	/**
	 * Creates the content of the master/details block inside the managed form.
	 * This method should be called as late as possible inside the parent part.
	 * 
	 * @param managedForm
	 *            the managed form to create the block in
	 */
	@Override
	public void createContent() {
		IManagedForm managedForm = getPage().getManagedForm();
		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = getToolkit();

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		getSection().setLayoutData(td);

		Composite body = getToolkit().createComposite(getSection());
		GridLayout layout = new GridLayout();
		layout.marginWidth = 5;
		layout.marginHeight = 5;
		body.setLayout(layout);
		sashForm = new SashForm(body, SWT.VERTICAL);
		toolkit.adapt(sashForm, false, false);
		sashForm.setMenu(body.getMenu());
		sashForm.setToolTipText("Define/Edit attributes for this Artifact.");
		sashForm.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createMasterPart(managedForm, sashForm);
		createDetailsPart(managedForm, sashForm);
		// createToolBarActions(managedForm);
		//sashForm.setWeights(new int[] { 50, 50 });
		form.updateToolBar();

		getSection().setClient(body);
		getToolkit().paintBordersFor(body);
	}

	class MasterContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ISDKProvider) {
				provider = (ISDKProvider) inputElement;
				return provider.getPatternFileContributions().toArray();
			}
			return new Object[0];
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}
	}

	class MasterLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		
		public String getColumnText(Object obj, int index) {
			PatternFileContribution field = (PatternFileContribution) obj;

			IPattern pattern = provider.getPattern(field.getContributor(), field.getFileName());

			if (index == 1){
				if (pattern != null){
					return pattern.getName();
				} else { 
					return "";
				}
			} else if (index == 2){
				if (pattern != null){
					return pattern.getUILabel();
				} else { 
					return "";
				}
			} else if (index == 3){
				if (pattern != null){
					return Integer.toString(pattern.getIndex());
				} else { 
					return "";
				}
			} else if (index == 4){
				if (pattern != null){
					return pattern.getIconPath();
				} else { 
					return "";
				}
			} else if (index == 5){
				return field.getValidatorClass();
			} else if (index == 6){
				return field.getContributor().toString();
			}else {
				return field.getFileName();
			}
		}

		public Image getColumnImage(Object obj, int index) {
			PatternFileContribution field = (PatternFileContribution) obj;

			IPattern pattern = provider.getPattern(field.getContributor(), field.getFileName());
			if (index == 4){
				if (pattern != null)
					return pattern.getImageDescriptor().createImage();
			}
			return null;
		}
	}

	
	Listener listener = new Listener() {
		public void handleEvent(Event e) {
			// determine new sort column and direction
			TableColumn sortColumn = viewer.getTable().getSortColumn();
			TableColumn currentColumn = (TableColumn) e.widget;
			int dir = viewer.getTable().getSortDirection();

			if (sortColumn == currentColumn) {
				dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
			} else {
				viewer.getTable().setSortColumn(currentColumn);
				dir = SWT.UP;
			}

			viewer.getTable().setSortDirection(dir);
			viewer.setSorter(new PatternFileSorter(((ConfigEditor) getPage().getEditor()).getIProvider(),dir, sortColumn.getText()));
			
			refresh();
			updateMaster();

		}
	};
	
	// ====================================================================
	private TableViewer viewer;

	private String[] patternColumnNames = new String[]{ "Pattern File","Pattern Name","UI Label","Index","Icon Path","Validator Class","Contributor"};
	
	TableColumn patternFileNameColumn;
	TableColumn patternNameColumn;
	TableColumn patternUiLabelColumn;
	TableColumn patternIndexColumn;
	TableColumn patternIconPathColumn;
	TableColumn patternValidatorClassColumn;
	TableColumn patternContributorColumn;

	private Button addContributionButton;
	private Button disableContributionButton;
	private Button enableContributionButton;
	private Button removeContributionButton;

	public TableViewer getViewer() {
		return this.viewer;
	}

	protected void createMasterPart(final IManagedForm managedForm,
			Composite parent) {

		FormToolkit toolkit = getToolkit();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);

		Composite sectionClient = toolkit.createComposite(section);
		FormLayout layout = new FormLayout();
		sectionClient.setLayout(layout);

		Table patternTable = toolkit.createTable(sectionClient, SWT.FULL_SELECTION);

		patternTable.setHeaderVisible(true);
		patternTable.setLinesVisible(true);


		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.bottom = new FormAttachment(100, -150);
		fd.left = new FormAttachment(0, 10);
		fd.right = new FormAttachment(80);
		fd.width = 400;
		patternTable.setLayoutData(fd);

		patternFileNameColumn = new TableColumn(patternTable, SWT.NULL);
		patternFileNameColumn.setWidth(250);
		patternFileNameColumn.setText(patternColumnNames[0]);
		
		patternNameColumn = new TableColumn(patternTable, SWT.NULL);
		patternNameColumn.setWidth(250);
		patternNameColumn.setText(patternColumnNames[1]);
		
		patternUiLabelColumn = new TableColumn(patternTable, SWT.NULL);
		patternUiLabelColumn.setWidth(100);
		patternUiLabelColumn.setText(patternColumnNames[2]);

		patternIndexColumn= new TableColumn(patternTable, SWT.NULL);
		patternIndexColumn.setWidth(50);
		patternIndexColumn.setText(patternColumnNames[3]);
		
		patternIconPathColumn= new TableColumn(patternTable, SWT.NULL);
		patternIconPathColumn.setWidth(250);
		patternIconPathColumn.setText(patternColumnNames[4]);
		
		patternValidatorClassColumn = new TableColumn(patternTable, SWT.NULL);
		patternValidatorClassColumn.setWidth(250);
		patternValidatorClassColumn.setText(patternColumnNames[5]);
		
		
		patternContributorColumn = new TableColumn(patternTable, SWT.NULL);
		patternContributorColumn.setWidth(250);
		patternContributorColumn.setText(patternColumnNames[6]);
		
	

		

		addContributionButton = toolkit.createButton(sectionClient, "Add",
				SWT.PUSH);
		// Support for testing
		addContributionButton.setData("name", "Add_Attribute");
		addContributionButton.setEnabled(true);
		fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment(patternTable, 5);
		fd.right = new FormAttachment(100, -5);
		addContributionButton.setLayoutData(fd);
		addContributionButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				addButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		enableContributionButton = toolkit.createButton(sectionClient, "Enable",
				SWT.PUSH);
		// Support for testing
		enableContributionButton.setData("name", "Enable_Attribute");
		enableContributionButton.setEnabled(true);
		fd = new FormData();
		fd.top = new FormAttachment(addContributionButton, 5);
		fd.left = new FormAttachment(patternTable, 5);
		fd.right = new FormAttachment(100, -5);
		enableContributionButton.setLayoutData(fd);
		enableContributionButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				enableButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});
		
		disableContributionButton = toolkit.createButton(sectionClient, "Disable",
				SWT.PUSH);
		// Support for testing
		disableContributionButton.setData("name", "Disable_Attribute");
		disableContributionButton.setEnabled(true);
		fd = new FormData();
		fd.top = new FormAttachment(enableContributionButton, 5);
		fd.left = new FormAttachment(patternTable, 5);
		fd.right = new FormAttachment(100, -5);
		disableContributionButton.setLayoutData(fd);
		disableContributionButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				disableButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		
		removeContributionButton = toolkit.createButton(sectionClient, "Remove",
				SWT.PUSH);
		// Support for testing
		removeContributionButton.setData("name", "Remove_Attribute");
		removeContributionButton.setData("name","removeAttributeButton");
		removeContributionButton.setEnabled(true);
		fd = new FormData();
		fd.top = new FormAttachment(disableContributionButton, 5);
		fd.left = new FormAttachment(patternTable, 5);
		fd.right = new FormAttachment(100, -5);
		removeContributionButton.setLayoutData(fd);

		removeContributionButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				removeButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		// This label keeps the "List" part shorter than the "Details" part to
		// the RHS

//		Label l = toolkit.createLabel(sectionClient, "", SWT.NULL);
//		fd = new FormData();
//		fd.top = new FormAttachment(removeContributionButton, 5);
//		fd.left = new FormAttachment(patternTable, 5);
//		fd.right = new FormAttachment(100, -5);
//		fd.height = 250;
//		l.setLayoutData(fd);

		final IFormPart part = this;
		viewer = new TableViewer(patternTable);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				selIndex = viewer.getTable().getSelectionIndex();
				managedForm.fireSelectionChanged(part, event.getSelection());
				viewerSelectionChanged(event);
			}
		});
		viewer.setContentProvider(new MasterContentProvider());
		viewer.setLabelProvider(new MasterLabelProvider());
		
		viewer.setInput(((ConfigEditor) getPage().getEditor())
				.getIProvider());
		viewer.getTable().setSortColumn(patternFileNameColumn);

		patternFileNameColumn.addListener(SWT.Selection, listener);
		patternNameColumn.addListener(SWT.Selection, listener);
		patternUiLabelColumn.addListener(SWT.Selection, listener);
		patternIndexColumn.addListener(SWT.Selection, listener);
		patternValidatorClassColumn.addListener(SWT.Selection, listener);
		patternContributorColumn.addListener(SWT.Selection, listener);
		
		toolkit.paintBordersFor(sectionClient);
		section.setClient(sectionClient);
	}

	/**
	 * Updates the master's side based on the selection on the list of
	 * attributes
	 * 
	 */
	protected void viewerSelectionChanged(SelectionChangedEvent event) {
		updateMaster();
	}

	protected void markPageModified() {
	}
	
	/**
	 * Triggered when the add button is pushed
	 * 
	 */
	protected void addButtonSelected(SelectionEvent event) {
		// Show the "AddPattern" wizard
		Shell shell = EclipsePlugin.getActiveWorkbenchShell();
		AddPatternDefinitionWizard wiz = new AddPatternDefinitionWizard(((ConfigEditor) getPage().getEditor())
				.getIProvider());
		WizardDialog dialog =
			new WizardDialog(shell, wiz);
		dialog.open();
	}

	/**
	 * Triggered when the enable button is pushed
	 * 
	 */
	protected void enableButtonSelected(SelectionEvent event) {
		
	}
	
	
	/**
	 * Triggered when the disable button is pushed
	 * 
	 */
	protected void disableButtonSelected(SelectionEvent event) {
		
		try {
			// We know the pattern based on the current selection in the table
			PatternFileContribution patt = (PatternFileContribution) viewer.getTable().getSelection()[0].getData();
			Map<String,String> attributes = new HashMap<String, String>();
			attributes.put("patternName", provider.getPattern(patt.getContributor(), patt.getFileName()).getName());
			
			
			// Need to ask which contributer to put the "disablement" in
			SelectContributerDialog dialog = new SelectContributerDialog(
					getSection().getShell(),
					provider);
			dialog.setTitle("Contributer Selection");
			dialog.setMessage("Enter a filter (* = any number of characters)"
					+ " or an empty string for no filtering: ");

			IPluginModelBase contributerForSaving= dialog.browseAvailableContribuers();
			
			IResource res = (IResource) contributerForSaving.getAdapter(IResource.class);
			IProject contProject = (IProject) res.getProject();
			ModelUpdater mu = new ModelUpdater();
			if (contProject != null){
				mu.addSimpleExtension(contProject, SDKConstants.PATTERNS_EXT_PT, "disabledPattern", attributes);
			}
			
			updateMaster();

		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		
	}


	/**
	 * Triggered when the remove button is pushed
	 * 
	 */
	protected void removeButtonSelected(SelectionEvent event) {
		// We know the pattern based on the current selection in the table
		PatternFileContribution cont = (PatternFileContribution) viewer.getTable().getSelection()[0].getData();
		
		IResource res = (IResource) cont.getContributor().getAdapter(IResource.class);
		
		IProject contProject = (IProject) res.getProject();
		ModelUpdater mu = new ModelUpdater();
		if (contProject != null){
			mu.removeContribution(contProject, SDKConstants.PATTERNS_EXT_PT, SDKConstants.PATTERNS_CREATION_PART, cont.getPluginElement());
		}
		
		updateMaster();
	}





	/**
	 * Updates the current state of the master
	 * 
	 * 
	 */
	public void updateMaster() {
		// The enable button should be inactive when nothing selected
		if (viewer.getSelection() != null && !viewer.getSelection().isEmpty()){
			
			// Need to check that the thing is disabled in the first place, and the disablement is 
			// an editable one.
			
			enableContributionButton.setEnabled(true);
		} else {
			enableContributionButton.setEnabled(false);
		}
		
		
		// The disable button should be inactive when nothing selected
		if (viewer.getSelection() != null && !viewer.getSelection().isEmpty()){
			disableContributionButton.setEnabled(true);
		} else {
			disableContributionButton.setEnabled(false);
		}
		
		// The remove button should be inactive for "readOnly" items
		if (viewer.getSelection() != null && !viewer.getSelection().isEmpty()
				&& !((PatternFileContribution) viewer.getTable().getSelection()[0].getData()).isReadOnly()) {
			removeContributionButton.setEnabled(true);
		} else {
			removeContributionButton.setEnabled(false);
		}
	}

	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(PatternFileContribution.class, 
				new PatternDetailsPage());
	}


	private void createDetailsPart(final IManagedForm mform, Composite parent) {
		detailsPart = new DetailsPart(mform, parent, SWT.NULL);
		mform.addPart(detailsPart);
		registerPages(detailsPart);
	}

	/**
	 * Commits the part. Subclasses should call 'super' when overriding.
	 * 
	 * @param onSave
	 *            <code>true</code> if the request to commit has arrived as a
	 *            result of the 'save' action.
	 */
	@Override
	public void commit(boolean onSave) {
		super.commit(onSave);
		detailsPart.commit(onSave);
	}

	private int selIndex = -1;

	@Override
	public void refresh() {
		viewer.setInput(((ConfigEditor) getPage().getEditor())
				.getIProvider());
		viewer.refresh();
		if (selIndex != -1) {
			Object refreshedMethod = viewer.getTable().getItem(selIndex)
					.getData();
			viewer.setSelection(new StructuredSelection(refreshedMethod), true);
		}
		updateMaster();
	}

	public DetailsPart getDetailsPart() {
		return detailsPart;
	}

	protected ISDKProvider getProvider() {
		return provider;
	}

}
