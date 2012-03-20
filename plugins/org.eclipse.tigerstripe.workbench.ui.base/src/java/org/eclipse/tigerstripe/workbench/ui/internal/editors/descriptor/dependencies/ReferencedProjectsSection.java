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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.dependencies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.AbstractTigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.core.module.InstalledModule;
import org.eclipse.tigerstripe.workbench.internal.core.project.Dependency;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.components.md.MasterDetails;
import org.eclipse.tigerstripe.workbench.ui.components.md.MasterDetailsBuilder;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.TigerstripeProjectSelectionDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.DescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.TigerstripeDescriptorSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Section used to define Tigerstripe dependencies.  Look under tigerstripe.xml > Dependencies
 * 
 * @author nmehrega
 */
public class ReferencedProjectsSection extends TigerstripeDescriptorSectionPart {

	private CheckboxTableViewer viewer;

	private Button removeButton;
	
	private Button selectAllButton;
	
	private Button deselectAllButton;

	private MasterDetails masterDetails;

	private ClasspathChangesListener classpathListener;
	
	private Button modifyRuntimeDependencies;
	
	private static final String DEP_CHECKBOX_LABEL = "Modify dependencies at generation time (advanced)"; 
	
	private static final String ADVANCED_PROPERTY_MODIFY_DEP_AT_RUNTIME = "modifyDependenciesAtGenerationTime";

	private class ClasspathChangesListener implements IElementChangedListener {

		public void elementChanged(ElementChangedEvent event) {
			IJavaElementDelta delta = event.getDelta();
			IJavaElementDelta[] childs = delta.getAffectedChildren();
			for (IJavaElementDelta child : childs) {
				if (child.getElement().getElementType() == IJavaElement.JAVA_PROJECT) {
					IJavaProject jProject = (IJavaProject) child.getElement();
					if (isCurrentProject(jProject) && isClassPathChanged(child)) {
						resolveReferencedModels();
						refreshReferences();
					}
				}
			}
		}

		private boolean isCurrentProject(IJavaProject jProject) {
			IJavaProject jTsProject = (IJavaProject) getTSProject()
					.getAdapter(IJavaProject.class);
			if (jTsProject != null && jTsProject.equals(jProject)) {
				return true;
			}
			return false;
		}

		private boolean isClassPathChanged(IJavaElementDelta delta) {
			if ((delta.getFlags() & IJavaElementDelta.F_CLASSPATH_CHANGED) != 0
					|| (delta.getFlags() & IJavaElementDelta.F_RESOLVED_CLASSPATH_CHANGED) != 0) {
				return true;
			}
			return false;
		}

		private void resolveReferencedModels() {
			try {
				ModelReference[] references = getTSProject()
						.getModelReferences();
				for (ModelReference reference : references) {
					reference.resolveModel();
				}
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}

		private void refreshReferences() {
			Control control = viewer.getControl();
			if (!control.isDisposed()) {
				control.getDisplay().asyncExec(new Runnable() {
					public void run() {
						refresh();
					}
				});
			}
		}
	}

	class ReferencedProjectsContentProvider implements
			IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {

			if (inputElement instanceof ITigerstripeModelProject) {
				ITigerstripeModelProject project = (ITigerstripeModelProject) inputElement;
				try {
					ModelReference[] modelReferences = project
							.getModelReferences();
					IDependency[] dependencies = project.getDependencies();
					Object[] elements = new Object[modelReferences.length
							+ dependencies.length];
					System.arraycopy(modelReferences, 0, elements, 0,
							modelReferences.length);
					System.arraycopy(dependencies, 0, elements,
							modelReferences.length, dependencies.length);
					return elements;
				} catch (TigerstripeException e) {
					return new Object[0];
				}
			}
			return new Object[0];
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	class ReferencedProjectsLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public String getColumnText(Object obj, int index) {
			
			if (obj instanceof ModelReference) {
				ModelReference ref = (ModelReference) obj;

				String modelId = ref.getToModelId();
				String projectName = null, version = null;
				if (ref.isResolved()) {
					ITigerstripeModelProject project = ref.getResolvedModel();
					projectName = project.getName();
					try {
						version = project.getProjectDetails().getVersion();
					} catch (Exception e) {
					}
				}

				StringBuffer buffer = new StringBuffer();
				if (modelId != null && modelId.length() > 0) {
					buffer.append(modelId);
				} else {
					buffer.append(projectName);
				}

				if (version != null && version.length() > 0) {
					buffer.append(" (");
					buffer.append(version);
					buffer.append(")");
				}

				return buffer.toString();

			} else if (obj instanceof IDependency) {
				return ((IDependency) obj).getPath();
			} else {
				return "Unknown";
			}
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public String getText(Object obj) {
			return getColumnText(obj, 0);
		}

		@Override
		public Image getImage(Object obj) {
			return IMAGE_PROVIDER.getImage(obj);
		}
	}

	public ReferencedProjectsSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR);
		setTitle("Dependencies");
	}

	@Override
	public void initialize(IManagedForm form) {
		super.initialize(form);
		createContent();
		initializeDependencyEnablement();
		
		classpathListener = new ClasspathChangesListener();
		JavaCore.addElementChangedListener(classpathListener,
				ElementChangedEvent.POST_CHANGE);
	}
	
	private void initializeDependencyEnablement() {
		
		TigerstripeProject tsProject = getActualTSProject();
		String enableModificationOfDepAtRuntime = null;
		
		if (tsProject != null) 
			enableModificationOfDepAtRuntime = tsProject.getAdvancedProperty(ADVANCED_PROPERTY_MODIFY_DEP_AT_RUNTIME);
		
		if (enableModificationOfDepAtRuntime!=null && enableModificationOfDepAtRuntime.equalsIgnoreCase("true")) {
			modifyRuntimeDependencies.setSelection(true);
			int numOfDependencies = viewer.getTable().getItemCount();
			for (int i=0; i < numOfDependencies; i++) { 
				Object element = viewer.getElementAt(i);
				if (element instanceof ModelReference) {
					ModelReference reference = (ModelReference)element;
					viewer.setChecked(reference, reference.isEnabled());
					
				} else if (element instanceof IDependency) {
					IDependency dependency = (IDependency)element;
					viewer.setChecked(dependency, dependency.isEnabled());
				}
			}
			
			selectAllButton.setEnabled(true);
			deselectAllButton.setEnabled(true);
			
		} else {
			// Dependency modification at runtime is off
			modifyRuntimeDependencies.setSelection(false);
			selectAllButton.setEnabled(false);
			deselectAllButton.setEnabled(false);
			
			viewer.setAllChecked(true);
		}
	}
	
	private TigerstripeProject getActualTSProject() {
		Object input = viewer.getInput();
		
		if (input instanceof TigerstripeProjectHandle) {
			TigerstripeProject tsProject = null;
			try {
				tsProject = ((TigerstripeProjectHandle)input).getTSProject();
			} catch (TigerstripeException e) {
				String exceptionMessage = e.getMessage();
				EclipsePlugin.logErrorMessage("Problem occured while getting Tigerstripe Project: " + exceptionMessage==null?"":exceptionMessage, e);
				return null;
			}
			
			return tsProject;
		}
		
		return null;		
	}
	
	

	@Override
	public void dispose() {
		if (classpathListener != null) {
			JavaCore.removeElementChangedListener(classpathListener);
		}
		super.dispose();
	}

	@Override
	protected void createContent() {
		GridDataFactory.fillDefaults().grab(true, true).applyTo(getSection());
		GridLayoutFactory.fillDefaults().applyTo(getSection());
		GridLayoutFactory.fillDefaults().applyTo(getBody());
		getSection().setClient(getBody());

		Composite parent = getToolkit().createComposite(getBody());
		GridDataFactory.fillDefaults().grab(true, true).applyTo(parent);
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(true)
				.applyTo(parent);

		Composite masterContainer = getToolkit().createComposite(parent);
		Composite detailsContainer = getToolkit().createComposite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(1, 1).numColumns(2).applyTo(masterContainer);
		GridLayoutFactory.fillDefaults().applyTo(detailsContainer);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(masterContainer);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(detailsContainer);

		createTable(masterContainer, getToolkit());
		
		modifyRuntimeDependencies = new Button(parent, SWT.CHECK);
		modifyRuntimeDependencies.setEnabled(true); 
		modifyRuntimeDependencies.setText(DEP_CHECKBOX_LABEL); 
		modifyRuntimeDependencies.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				TigerstripeProject tsProject = getActualTSProject();
				
				if (tsProject!=null) 
					tsProject.setAdvancedProperty(ADVANCED_PROPERTY_MODIFY_DEP_AT_RUNTIME,  modifyRuntimeDependencies.getSelection()?"true":"false");
				
				if (modifyRuntimeDependencies.getSelection()) {
					selectAllButton.setEnabled(true);
					deselectAllButton.setEnabled(true);
					getActualTSProject().setDirty();
					markPageModified();
					
				} else {
					enableAllDependencies(true);
					selectAllButton.setEnabled(false);
					deselectAllButton.setEnabled(false);
				} 
			}
			
		});
		

		getToolkit().paintBordersFor(masterContainer);

		masterDetails = MasterDetailsBuilder
				.create()
				.addDetail(ModelReference.class,
						new ReferenceDetails(getToolkit(), detailsContainer))
				.addDetail(Dependency.class,
						new DependencyDetails(getToolkit(), detailsContainer))
				.build();
	}

	private void enableAllDependencies(boolean markAsDirty) {
		
		boolean stateChanged  = false;
		
		// Walk through all dependencies and enable them.
		int numOfDependencies = viewer.getTable().getItemCount();
		for (int i=0; i < numOfDependencies; i++) { 
			TableItem item = viewer.getTable().getItem(i);
			if (!item.getChecked()) {
				stateChanged = true;
				item.setChecked(true);
				Object element = viewer.getElementAt(i);
				if (element instanceof ModelReference) {
					ModelReference reference = (ModelReference)element;
					reference.setEnabled(true);							
				} else if (element instanceof IDependency) {
					IDependency dependency = (IDependency)element;
					dependency.setEnabled(true);
				}
			}
		}
	
		if (stateChanged && markAsDirty) {
			getActualTSProject().setDirty();
			markPageModified();
			Object input = viewer.getInput();
			if (input instanceof TigerstripeProjectHandle) 
				((TigerstripeProjectHandle)input).markCacheAsDirty();
		}
	}
	
	private void disableAllDependencies() {
		
		boolean stateChanged  = false;
			
		// Walk through all dependencies and enable them.
		int numOfDependencies = viewer.getTable().getItemCount();
		for (int i=0; i < numOfDependencies; i++) { 
			TableItem item = viewer.getTable().getItem(i);
			if (item.getChecked()) {
				stateChanged = true;
				item.setChecked(false);
				Object element = viewer.getElementAt(i);
				if (element instanceof ModelReference) {
					ModelReference reference = (ModelReference)element;
					reference.setEnabled(false);							
				} else if (element instanceof IDependency) {
					IDependency dependency = (IDependency)element;
					dependency.setEnabled(false);
				}
			}
		}
	
		if (stateChanged) {
			getActualTSProject().setDirty();
			markPageModified();
			Object input = viewer.getInput();
			if (input instanceof TigerstripeProjectHandle) 
				((TigerstripeProjectHandle)input).markCacheAsDirty();
		}
	}
	
	private void createTable(Composite parent, FormToolkit toolkit) {
		Table t = toolkit.createTable(parent, SWT.NULL | SWT.MULTI | SWT.CHECK);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 2;
		t.setLayoutData(gd);

		Composite buttonsClient = toolkit.createComposite(parent);
		buttonsClient.setLayout(TigerstripeLayoutFactory
				.createButtonsGridLayout());
		buttonsClient.setLayoutData(new GridData(
				GridData.VERTICAL_ALIGN_BEGINNING));

		// Add Dependency button
		Button addButton = toolkit.createButton(buttonsClient, "Add", SWT.PUSH);
		addButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				addButtonSelected();
			}
		});
		addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Remove Dependency button
		removeButton = toolkit.createButton(buttonsClient, "Remove", SWT.PUSH);
		removeButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				removeButtonSelected();
			}
		});
		removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Select All button
		selectAllButton = toolkit.createButton(buttonsClient, "Select All", SWT.PUSH);
		selectAllButton.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				enableAllDependencies(true);
			}
		});
		selectAllButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Deselect All button
		deselectAllButton = toolkit.createButton(buttonsClient, "Deselect All", SWT.PUSH);
		deselectAllButton.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				disableAllDependencies();
			}
		});
		deselectAllButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		viewer = new CheckboxTableViewer(t);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				updateDetails();
				updateButtonsEnablement();
			}
		});
		viewer.setContentProvider(new ReferencedProjectsContentProvider());
		final ITableLabelProvider labelProvider = new ReferencedProjectsLabelProvider();
		viewer.setLabelProvider(labelProvider);
		ITextProvider textAdapter = new ITextProvider() {

			public String getText(Object obj) {
				return labelProvider.getColumnText(obj, 0);
			}
		};
		viewer.setSorter(new DependenciesSorter(textAdapter, DEPENDENCY_KIND_RESOLVER));

		AbstractTigerstripeProjectHandle handle = (AbstractTigerstripeProjectHandle) getTSProject();
		viewer.setInput(handle);
		
		viewer.addCheckStateListener(new ICheckStateListener() {
			
			public void checkStateChanged(CheckStateChangedEvent event) {
				if (!modifyRuntimeDependencies.getSelection()) {
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Operation Not Enabled", "To modify dependencies at generation time (i.e. run generation on certain modules), you'll need to explicitly enable the option below: '" + DEP_CHECKBOX_LABEL + "'");
					viewer.setChecked(event.getElement(), true);
				} else {
					getActualTSProject().setDirty(); 
					
					Object source = event.getElement();	
					if (source instanceof IDependency) {
						((IDependency)source).setEnabled(event.getChecked());
					} else if (source instanceof ModelReference) {
						((ModelReference)source).setEnabled(event.getChecked());
					}
					
					Object input = viewer.getInput();
					if (input instanceof TigerstripeProjectHandle) 
						((TigerstripeProjectHandle)input).markCacheAsDirty();
					
					markPageModified();
				}
			}
		});
		
	}

	protected void addButtonSelected() {
		List<ModelReference> filteredOutProjects = new ArrayList<ModelReference>();

		try {
			filteredOutProjects
					.add(ModelReference.referenceFromProject(getTSProject())); // the
																		// current
																		// project
			for (ModelReference prjRefs : getTSProject().getModelReferences()) {
				filteredOutProjects.add(prjRefs);
			}
		} catch (TigerstripeException e) {
			// ignore here
		}

		TigerstripeProjectSelectionDialog dialog = new TigerstripeProjectSelectionDialog(
				getSection().getShell(), filteredOutProjects, getTSProject());
		if (dialog.open() == Window.OK) {
			Object[] results = dialog.getResult();
			for (Object res : results) {
				ModelReference ref = null;
				if (res instanceof IJavaProject) {
					IJavaProject prj = (IJavaProject) res;

					ITigerstripeModelProject tsPrj = (ITigerstripeModelProject) prj
							.getProject().getAdapter(
									ITigerstripeModelProject.class);
					if (tsPrj != null) {
						try {
							ref = ModelReference.referenceFromProject(tsPrj);
						} catch (TigerstripeException e) {
							EclipsePlugin.log(e);
						}
					}
				} else if (res instanceof InstalledModule) {
					InstalledModule module = (InstalledModule) res;
					ref = new ModelReference(getTSProject(),
							module.getModuleID());
				}
				if (ref != null) {
					try {
						getTSProject().addModelReference(ref);
						viewer.refresh(true);
						viewer.setChecked(ref, true);  // NM: Check newly added dependency
						markPageModified();
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
					continue;
				}
				if (res instanceof IResource) {
					try {
						IDependency dep = getTSProject().makeDependency(((IResource) res).getProjectRelativePath().toOSString());
						getTSProject().addDependency(dep, new NullProgressMonitor());
						viewer.refresh(true);
						viewer.setChecked(dep, true);  // NM: Check newly added dependency
						markPageModified();
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			}
			
		}
		viewer.refresh();
	}

	protected void removeButtonSelected() {
		TableItem[] selectedItems = viewer.getTable().getSelection();
		List<ModelReference> modelRefFields = new ArrayList<ModelReference>();
		List<IDependency> dependencyFields = new ArrayList<IDependency>();

		for (int i = 0; i < selectedItems.length; i++) {
			Object data = selectedItems[i].getData();
			if (data instanceof ModelReference) {
				modelRefFields.add((ModelReference) data);
			} else if (data instanceof IDependency) {
				dependencyFields.add((IDependency) data);
			}
		}

		String message = "Do you really want to remove ";
		if (selectedItems.length > 1) {
			message = message + "these " + selectedItems.length + " items?";
		} else {
			message = message + "this item?";
		}

		MessageDialog msgDialog = new MessageDialog(getBody().getShell(),
				"Remove Reference/Dependency", null, message,
				MessageDialog.QUESTION, new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == 0) {
			ITigerstripeModelProject handle = getTSProject();
			try {
				handle.removeModelReferences(modelRefFields
						.toArray(new ModelReference[0]));
				handle.removeDependencies(
						dependencyFields.toArray(new IDependency[0]),
						new NullProgressMonitor());
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			markPageModified();
			viewer.refresh(true);
		}
	}

	protected void markPageModified() {
		DescriptorEditor editor = (DescriptorEditor) getPage().getEditor();
		editor.pageModified();
	}

	@Override
	public void refresh() {
		updateViewerInput();
		
		updateDetails();
		
		if (!modifyRuntimeDependencies.getSelection()) 
			enableAllDependencies(false);  // Don't mark the page as dirty after a refresh.
		
		updateButtonsEnablement();
	}

	private void updateDetails() {
		masterDetails.switchTo(((IStructuredSelection) viewer.getSelection())
				.getFirstElement());
	}
	
	private void updateButtonsEnablement() {
		TableItem[] selectedItems = viewer.getTable().getSelection();
		if (selectedItems != null && selectedItems.length > 0) {
			removeButton.setEnabled(true);
		} else {
			removeButton.setEnabled(false);
		}
	}

	// [nmehrega] Bugzilla 322566: Update the viewer's input if our working copy
	// of TS Project has changed.
	// This, for example, happens when the project descriptor is modified and
	// saved.
	private void updateViewerInput() {
		Object tsProjectWorkingCopy = getTSProject();
		viewer.setInput(tsProjectWorkingCopy);
		initializeDependencyEnablement();
	}

	private static final IDependencyKindResolver DEPENDENCY_KIND_RESOLVER = new IDependencyKindResolver() {

		public DependencyKind resolve(Object obj) {
			if (obj instanceof ModelReference) {
				ModelReference ref = (ModelReference) obj;
				if (ref.isResolved()) {
					if (ref.isWorkspaceReference())
						return DependencyKind.PROJECT;
					else if (ref.isInstalledModuleReference())
						return DependencyKind.MODULE;
				}
			}
			if (obj instanceof IDependency) {
				return DependencyKind.DEPENDENCY;
			} else {
				return DependencyKind.UNKNOWN;
			}
		}
	};

	private static final DependenciesImageProvider IMAGE_PROVIDER = new DependenciesImageProvider(
			DEPENDENCY_KIND_RESOLVER);
}
