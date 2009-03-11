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

package org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.annotation;

import java.util.Collection;

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
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.sdk.internal.ISDKProvider;
import org.eclipse.tigerstripe.workbench.sdk.internal.SDKConstants;
import org.eclipse.tigerstripe.workbench.sdk.internal.ModelUpdater;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationExplicitFileRouterContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationPackageLabelContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationPropertyProviderContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationTypeContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.ConfigEditor;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.ExtensionSectionPart;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.wizards.AddAnnotationTypeWizard;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.wizards.AddAuditWizard;
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

public class AnnotationSection extends ExtensionSectionPart implements
		IFormPart {

	protected DetailsPart detailsPart;
	protected ISDKProvider provider;


	public AnnotationSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit,
			int style) {
		super(page, parent, toolkit,  null,
				ExpandableComposite.TWISTIE | style);
		setTitle("Contributions to '"+SDKConstants.ANNOTATIONS_EXT_PT+"'");
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
		sashForm.setWeights(new int[] { 35, 65 });
		form.updateToolBar();

		getSection().setClient(body);
		getToolkit().paintBordersFor(body);
	}

	class MasterContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ISDKProvider) {
				provider = (ISDKProvider) inputElement;
				return provider.getAnnotationTypeContributions().toArray();
			}
			return new Object[0];
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}
	}

	public ISDKProvider getProvider() {
		return provider;
	}

	class MasterLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		
		public String getColumnText(Object obj, int index) {
			AnnotationTypeContribution field = (AnnotationTypeContribution) obj;
		
			if (index == 1){
				return field.getName();
			} else if (index == 2){
				return Integer.toString(provider.getExtractor().getAnnotationMap().get(field).size());
			} else if (index == 3){
				return field.getEClass();
			} else if (index == 4){
				return field.getNamespace();
			} else if (index == 5){
				return field.getContributor().toString();
			} else if (index == 6){
				String annotationName = provider.getPackageForAnnotation(field)+field.getName();
				Collection<AnnotationExplicitFileRouterContribution> routers = provider.getAnnotationExplicitFileRouterContributions();
				for (AnnotationExplicitFileRouterContribution router : routers){
					if ((router.getEPackage()+"."+router.getEClass()).equals(annotationName)){
						return router.getPath();
					} 
					if (router.getEPackage().equals(provider.getPackageForAnnotation(field))){
						return router.getPath();
					} 
					if (router.getNsURI().equals(field.getNamespace())){
						return router.getPath();
					}
				}

				return "";
			}else {
				String packageNS = field.getNamespace();
				Collection<AnnotationPackageLabelContribution> labels = provider.getAnnotationPackageLabelContributions();
				for (AnnotationPackageLabelContribution label : labels){
					if (label.getUri().equals(packageNS)){
						return label.getName();
					}
				}
				return ""; 
			}
		}

		public Image getColumnImage(Object obj, int index) {
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
			viewer.setSorter(new AnnotationSorter(dir, sortColumn.getText()));
			refresh();
			updateMaster();

		}
	};
	
	// ====================================================================
	private TableViewer viewer;

	private String[] annotationTypeNames = new String[]{"Package","Name","Usage","eClass","Namespace","Contributor", "Router"};
	
	TableColumn annotationPackageLabelColumn;
	TableColumn annotationTypeNameColumn;
	TableColumn annotationTypeEClassColumn;
	TableColumn annotationTypeUsageColumn;
	TableColumn annotationTypeNamepsaceColumn;
	TableColumn annotationTypeContributorColumn;
	TableColumn annotationTypeRouterColumn;

	private Button addContributionButton;

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

		Table annotationTable = toolkit.createTable(sectionClient, SWT.FULL_SELECTION);

		annotationTable.setHeaderVisible(true);
		annotationTable.setLinesVisible(true);


		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.bottom = new FormAttachment(100, -150);
		fd.left = new FormAttachment(0, 10);
		fd.right = new FormAttachment(80);
		fd.width = 400;
		annotationTable.setLayoutData(fd);

		annotationPackageLabelColumn = new TableColumn(annotationTable, SWT.NULL);
		annotationPackageLabelColumn.setWidth(150);
		annotationPackageLabelColumn.setText(annotationTypeNames[0]);
		
		annotationTypeNameColumn = new TableColumn(annotationTable, SWT.NULL);
		annotationTypeNameColumn.setWidth(250);
		annotationTypeNameColumn.setText(annotationTypeNames[1]);

		
		annotationTypeUsageColumn = new TableColumn(annotationTable, SWT.NULL);
		annotationTypeUsageColumn.setWidth(75);
		annotationTypeUsageColumn.setText(annotationTypeNames[2]);
		
		annotationTypeEClassColumn = new TableColumn(annotationTable, SWT.NULL);
		annotationTypeEClassColumn.setWidth(250);
		annotationTypeEClassColumn.setText(annotationTypeNames[3]);
		
		annotationTypeNamepsaceColumn= new TableColumn(annotationTable, SWT.NULL);
		annotationTypeNamepsaceColumn.setWidth(250);
		annotationTypeNamepsaceColumn.setText(annotationTypeNames[4]);
		
		
		annotationTypeContributorColumn = new TableColumn(annotationTable, SWT.NULL);
		annotationTypeContributorColumn.setWidth(250);
		annotationTypeContributorColumn.setText(annotationTypeNames[5]);
		
		annotationTypeRouterColumn = new TableColumn(annotationTable, SWT.NULL);
		annotationTypeRouterColumn.setWidth(250);
		annotationTypeRouterColumn.setText(annotationTypeNames[6]);

		annotationTypeNameColumn.addListener(SWT.Selection, listener);
		annotationTypeUsageColumn.addListener(SWT.Selection, listener);
		annotationTypeEClassColumn.addListener(SWT.Selection, listener);
		annotationTypeNamepsaceColumn.addListener(SWT.Selection, listener);
		annotationTypeContributorColumn.addListener(SWT.Selection, listener);

		addContributionButton = toolkit.createButton(sectionClient, "Add",
				SWT.PUSH);
		// Support for testing
		addContributionButton.setData("name", "Add_Contribution");
		addContributionButton.setEnabled(true);
		fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment(annotationTable, 5);
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

		

		
		removeContributionButton = toolkit.createButton(sectionClient, "Remove",
				SWT.PUSH);
		// Support for testing
		removeContributionButton.setData("name", "Remove_Contribution");
		removeContributionButton.setData("name","removeAttributeButton");
		removeContributionButton.setEnabled(true);
		fd = new FormData();
		fd.top = new FormAttachment(addContributionButton, 5);
		fd.left = new FormAttachment(annotationTable, 5);
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

		Label l = toolkit.createLabel(sectionClient, "", SWT.NULL);
		fd = new FormData();
		fd.top = new FormAttachment(removeContributionButton, 5);
		fd.left = new FormAttachment(annotationTable, 5);
		fd.right = new FormAttachment(100, -5);
		fd.height = 250;
		l.setLayoutData(fd);

		final IFormPart part = this;
		viewer = new TableViewer(annotationTable);
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
		viewer.getTable().setSortColumn(annotationTypeNameColumn);
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

	/**
	 * Triggered when the add button is pushed
	 * 
	 */
	protected void addButtonSelected(SelectionEvent event) {
		Shell shell = EclipsePlugin.getActiveWorkbenchShell();
		AddAnnotationTypeWizard wiz = new AddAnnotationTypeWizard(((ConfigEditor) getPage().getEditor())
				.getIProvider());
		WizardDialog dialog =
			new WizardDialog(shell, wiz);
		dialog.open();
	}

	protected void markPageModified() {
	}

	/**
	 * Gets the default attribute type from the active profile.
	 */
	private String getDefaultTypeName() throws TigerstripeException {
		IWorkbenchProfile profile = TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile();
		return profile.getDefaultPrimitiveTypeString();
	}

	/**
	 * Triggered when the remove button is pushed
	 * 
	 */
	protected void removeButtonSelected(SelectionEvent event) {
		// We know the pattern based on the current selection in the table
		AnnotationTypeContribution cont = (AnnotationTypeContribution) viewer.getTable().getSelection()[0].getData();
		
		IResource res = (IResource) cont.getContributor().getAdapter(IResource.class);
		
		IProject contProject = (IProject) res.getProject();
		ModelUpdater mu = new ModelUpdater();
		if (contProject != null){
			mu.removeContribution(contProject,SDKConstants.ANNOTATIONS_EXT_PT,SDKConstants.ANNOTATIONS_DEFINITION_PART,cont.getPluginElement());
		}
		
		updateMaster();
		
	}

	/**
	 * Updates the current state of the master
	 * 
	 * 
	 */
	public void updateMaster() {
		// The remove button should be inactive for "readOnly" items
		if (viewer.getSelection() != null && !viewer.getSelection().isEmpty()
				&& !((AnnotationTypeContribution) viewer.getTable().getSelection()[0].getData()).isReadOnly()) {
			removeContributionButton.setEnabled(true);
		} else {
			removeContributionButton.setEnabled(false);
		}
	}

	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(AnnotationTypeContribution.class, 
				new AnnotationDetailsPage());
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

}
