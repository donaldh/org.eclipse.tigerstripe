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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.segment.scope;

import static org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.EXCLUDES;
import static org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.INCLUDES;
import static org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.Kind.PATTERN;
import static org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.Kind.STEREOTYPE;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.ScopePattern;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.ScopeStereotypePattern;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.BrowseForStereotypeDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class IncludeExcludeSection extends ScopeSection {

	private Button addIncludesButton;

	private Button sortIncludesButton;

	private Button removeIncludesButton;

	private Table includesTable;

	private TableViewer includesViewer;

	private Button addExcludesButton;

	private Button sortExcludesButton;

	private Button removeExcludesButton;

	private Table excludesTable;

	private TableViewer excludesViewer;

	private Table stereotypeExcludesTable;

	private TableViewer stereotypeExcludesViewer;

	private Button addStereotypeExcludesButton;

	private Button sortStereotypeExcludesButton;

	private Button removeStereotypeExcludesButton;

	private class StereotypePatternContentProvider implements
			IStructuredContentProvider {

		private int targetType = ISegmentScope.INCLUDES;

		/**
		 * 
		 * @param type
		 *            needs to be either {@link ISegmentScope#INCLUDES} or
		 *            {@link ISegmentScope#EXCLUDES}
		 */
		public StereotypePatternContentProvider(int type) {
			if (type == ISegmentScope.INCLUDES
					|| type == ISegmentScope.EXCLUDES) {
				targetType = type;
			}
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ISegmentScope) {
				ISegmentScope scope = (ISegmentScope) inputElement;
				ScopeStereotypePattern[] patterns = scope
						.getStereotypePatterns(targetType);
				return patterns;
			} else
				return new ScopeStereotypePattern[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	private class PatternContentProvider implements IStructuredContentProvider {

		private int targetType = ISegmentScope.INCLUDES;

		/**
		 * 
		 * @param type
		 *            needs to be either {@link ISegmentScope#INCLUDES} or
		 *            {@link ISegmentScope#EXCLUDES}
		 */
		public PatternContentProvider(int type) {
			if (type == ISegmentScope.INCLUDES
					|| type == ISegmentScope.EXCLUDES) {
				targetType = type;
			}
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ISegmentScope) {
				ISegmentScope scope = (ISegmentScope) inputElement;
				ScopePattern[] patterns = scope.getPatterns(targetType);
				return patterns;
			} else
				return new ScopePattern[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	private class PatternCellModifier implements ICellModifier {

		private final TableViewer viewer;

		public PatternCellModifier(TableViewer viewer) {
			this.viewer = viewer;
		}

		public boolean canModify(Object element, String property) {
			if ("PATTERN".equals(property))
				return true;

			return false;
		}

		public Object getValue(Object element, String property) {
			if ("PATTERN".equals(property)) {
				ScopePattern entry = (ScopePattern) element;
				return entry.pattern;
			}

			return null;
		}

		public void modify(Object item, String property, Object value) {

			// null indicates that the validator rejected the values
			if (value == null)
				return;

			ScopePattern pattern = (ScopePattern) ((TableItem) item).getData();
			if (pattern.pattern != null && !pattern.pattern.equals(value)) {
				pattern.pattern = (String) value;
				markPageModified();
				viewer.refresh(true);
			}
		}
	}

	public IncludeExcludeSection(ScopePage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR);
		setTitle("Scope Details");
		createContent();
	}

	/**
	 * Note: this is not implemented yet as it doesn't seem to apply yet, but it
	 * is here as provision... just in case.
	 * 
	 * @return
	 */
	@Override
	protected boolean isReadonly() {
		return false;
	}

	@Override
	protected void createContent() {
		PatternLabelProvider labelProvider = new PatternLabelProvider();
		getSection().setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		getBody().setLayout(
				TigerstripeLayoutFactory.createFormTableWrapLayout(1, false));
		getBody().setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		createIncludeTable(getToolkit(), getBody(), labelProvider);
		createExcludeTable(getToolkit(), getBody(), labelProvider);
		createStereotypeExcludeTable(getToolkit(), getBody(), labelProvider);

		updateSection();

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createIncludeTable(FormToolkit toolkit, Composite parent,
			PatternLabelProvider labelProvider) {
		toolkit.createLabel(parent, "Included patterns:").setLayoutData(
				new TableWrapData(TableWrapData.FILL_GRAB));

		Composite tableClient = toolkit.createComposite(parent);
		tableClient.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		tableClient.setLayout(TigerstripeLayoutFactory
				.createFormPaneTableWrapLayout(2, false));

		includesTable = toolkit.createTable(tableClient, SWT.BORDER | SWT.FLAT
				| SWT.FULL_SELECTION);
		includesTable.setEnabled(!this.isReadonly());
		includesTable.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				updateRemoveButtonsState();
			}
		});

		includesViewer = new TableViewer(includesTable);
		try {
			includesViewer.setLabelProvider(labelProvider);
			includesViewer.setContentProvider(new PatternContentProvider(
					ISegmentScope.INCLUDES));

			includesViewer.setInput(getScope());
			includesViewer.setColumnProperties(new String[] { "PATTERN" });
			final TextCellEditor entryListCellEditor = new TextCellEditor(
					includesViewer.getTable());
			includesViewer
					.setCellEditors(new CellEditor[] { entryListCellEditor });
			entryListCellEditor.setValidator(EmptyValidator.INSTANCE);
			
			includesViewer.setCellModifier(new PatternCellModifier(
					includesViewer));
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		Composite buttonClient = toolkit.createComposite(tableClient);
		buttonClient.setLayout(TigerstripeLayoutFactory
				.createButtonsTableWrapLayout());

		addIncludesButton = toolkit.createButton(buttonClient, "Add", SWT.PUSH);
		addIncludesButton.setEnabled(!this.isReadonly());
		addIncludesButton.setLayoutData(new TableWrapData(TableWrapData.FILL));
		addIncludesButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// empty
			}

			public void widgetSelected(SelectionEvent e) {
				addPatternSelected(ISegmentScope.INCLUDES);
			}
		});

		sortIncludesButton = toolkit.createButton(buttonClient, "Sort",
				SWT.PUSH);
		sortIncludesButton.setEnabled(!this.isReadonly());
		sortIncludesButton.setLayoutData(new TableWrapData(TableWrapData.FILL));
		sortIncludesButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// empty
			}

			public void widgetSelected(SelectionEvent e) {
				sortButtonSelected(INCLUDES, PATTERN, includesViewer);
			}

		});

		removeIncludesButton = toolkit.createButton(buttonClient, "Remove",
				SWT.PUSH);
		removeIncludesButton
				.setLayoutData(new TableWrapData(TableWrapData.FILL));
		removeIncludesButton.setEnabled(!this.isReadonly());
		removeIncludesButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// empty
			}

			public void widgetSelected(SelectionEvent e) {
				removePatternSelected(ISegmentScope.INCLUDES);
			}
		});

		Point p = buttonClient.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = p.y;
		includesTable.setLayoutData(td);
	}

	private void createExcludeTable(FormToolkit toolkit, Composite parent,
			PatternLabelProvider labelProvider) {
		toolkit.createLabel(parent, "Excluded patterns:").setLayoutData(
				new TableWrapData(TableWrapData.FILL_GRAB));

		Composite tableClient = toolkit.createComposite(parent);
		tableClient.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		tableClient.setLayout(TigerstripeLayoutFactory
				.createFormPaneTableWrapLayout(2, false));

		excludesTable = toolkit.createTable(tableClient, SWT.BORDER | SWT.FLAT);
		excludesTable.setEnabled(!this.isReadonly());
		excludesTable.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				updateRemoveButtonsState();
			}
		});

		excludesViewer = new TableViewer(excludesTable);
		try {
			excludesViewer.setLabelProvider(labelProvider);
			excludesViewer.setContentProvider(new PatternContentProvider(
					ISegmentScope.EXCLUDES));
			excludesViewer.setInput(getScope());
			excludesViewer.setColumnProperties(new String[] { "PATTERN" });
			final TextCellEditor entryListCellEditor = new TextCellEditor(
					excludesViewer.getTable());
			entryListCellEditor.setValidator(EmptyValidator.INSTANCE);
			excludesViewer
					.setCellEditors(new CellEditor[] { entryListCellEditor });
			excludesViewer.setCellModifier(new PatternCellModifier(
					excludesViewer));
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		Composite buttonClient = toolkit.createComposite(tableClient);
		buttonClient.setLayout(TigerstripeLayoutFactory
				.createButtonsTableWrapLayout());

		addExcludesButton = toolkit.createButton(buttonClient, "Add", SWT.PUSH);
		addExcludesButton.setEnabled(!this.isReadonly());
		addExcludesButton.setLayoutData(new TableWrapData(TableWrapData.FILL));
		addExcludesButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// empty
			}

			public void widgetSelected(SelectionEvent e) {
				addPatternSelected(ISegmentScope.EXCLUDES);
			}
		});

		sortExcludesButton = toolkit.createButton(buttonClient, "Sort",
				SWT.PUSH);
		sortExcludesButton.setEnabled(!this.isReadonly());
		sortExcludesButton.setLayoutData(new TableWrapData(TableWrapData.FILL));
		sortExcludesButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// empty
			}

			public void widgetSelected(SelectionEvent e) {
				sortButtonSelected(EXCLUDES, PATTERN, excludesViewer);
			}
		});

		removeExcludesButton = toolkit.createButton(buttonClient, "Remove",
				SWT.PUSH);
		removeExcludesButton
				.setLayoutData(new TableWrapData(TableWrapData.FILL));
		removeExcludesButton.setEnabled(!this.isReadonly());
		removeExcludesButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// empty
			}

			public void widgetSelected(SelectionEvent e) {
				removePatternSelected(ISegmentScope.EXCLUDES);
			}
		});

		Point p = buttonClient.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = p.y;
		excludesTable.setLayoutData(td);
	}

	private void createStereotypeExcludeTable(FormToolkit toolkit,
			Composite parent, PatternLabelProvider labelProvider) {
		toolkit.createLabel(parent, "Stereotype-based Exclusions:")
				.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite tableClient = toolkit.createComposite(parent);
		tableClient.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		tableClient.setLayout(TigerstripeLayoutFactory
				.createFormPaneTableWrapLayout(2, false));

		stereotypeExcludesTable = toolkit.createTable(tableClient, SWT.BORDER
				| SWT.FLAT);
		stereotypeExcludesTable.setEnabled(!this.isReadonly());
		stereotypeExcludesTable.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		stereotypeExcludesTable.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				updateRemoveButtonsState();
			}
		});

		stereotypeExcludesViewer = new TableViewer(stereotypeExcludesTable);
		try {
			stereotypeExcludesViewer.setLabelProvider(labelProvider);
			stereotypeExcludesViewer
					.setContentProvider(new StereotypePatternContentProvider(
							ISegmentScope.EXCLUDES));
			stereotypeExcludesViewer.setInput(getScope());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		Composite buttonClient = toolkit.createComposite(tableClient);
		buttonClient.setLayout(TigerstripeLayoutFactory
				.createButtonsTableWrapLayout());

		addStereotypeExcludesButton = toolkit.createButton(buttonClient, "Add",
				SWT.PUSH);
		addStereotypeExcludesButton.setEnabled(!this.isReadonly());
		addStereotypeExcludesButton.setLayoutData(new TableWrapData(
				TableWrapData.FILL));
		addStereotypeExcludesButton
				.addSelectionListener(new SelectionListener() {
					public void widgetDefaultSelected(SelectionEvent e) {
						// empty
					}

					public void widgetSelected(SelectionEvent e) {
						addStereotypePatternSelected(ISegmentScope.EXCLUDES);
					}
				});

		sortStereotypeExcludesButton = toolkit.createButton(buttonClient,
				"Sort", SWT.PUSH);
		sortStereotypeExcludesButton.setEnabled(!this.isReadonly());
		sortStereotypeExcludesButton.setLayoutData(new TableWrapData(
				TableWrapData.FILL));
		sortStereotypeExcludesButton
				.addSelectionListener(new SelectionListener() {
					public void widgetDefaultSelected(SelectionEvent e) {
						// empty
					}

					public void widgetSelected(SelectionEvent e) {
						sortButtonSelected(EXCLUDES, STEREOTYPE, stereotypeExcludesViewer);
					}

				});

		removeStereotypeExcludesButton = toolkit.createButton(buttonClient,
				"Remove", SWT.PUSH);
		removeStereotypeExcludesButton.setLayoutData(new TableWrapData(
				TableWrapData.FILL));
		removeStereotypeExcludesButton.setEnabled(!this.isReadonly());
		removeStereotypeExcludesButton
				.addSelectionListener(new SelectionListener() {
					public void widgetDefaultSelected(SelectionEvent e) {
						// empty
					}

					public void widgetSelected(SelectionEvent e) {
						removeStereotypePatternSelected(ISegmentScope.EXCLUDES);
					}
				});

		Point p = buttonClient.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = p.y;
		stereotypeExcludesTable.setLayoutData(td);
	}

	private ITigerstripeModelProject getTSProject() throws TigerstripeException {
		IFile file = ((IFileEditorInput) getPage().getEditorInput()).getFile();
		IProject project = file.getProject();
		IAbstractTigerstripeProject aProject = (IAbstractTigerstripeProject) project
				.getAdapter(IAbstractTigerstripeProject.class);
		;
		if (aProject instanceof ITigerstripeModelProject)
			return (ITigerstripeModelProject) aProject;
		return null;
	}

	private void addPatternSelected(int type) {
		ScopePattern newPattern = new ScopePattern();
		newPattern.type = type;
		newPattern.pattern = "com.mycompany";
		try {
			if (getTSProject() != null) {
				newPattern.pattern = getTSProject().getProjectDetails()
						.getProperty(
								IProjectDetails.DEFAULTARTIFACTPACKAGE_PROP,
								newPattern.pattern);
			}
			newPattern.pattern = newPattern.pattern + ".*";
			ISegmentScope scope = getScope();
			
			// Navid Mehregani: Bugzilla 322400 -  [Form Editor] Cannot add multiple patterns to Facet editor
			int index = 0;
			while(scope.containsPattern(newPattern)) {
				newPattern.pattern = newPattern.pattern.substring(0, newPattern.pattern.lastIndexOf((index-1==-1?"":String.valueOf(index-1)) + ".*")) + String.valueOf(index) + ".*";
				index++;
			}
			
			getScope().addPattern(newPattern);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		markPageModified();
		if (type == ISegmentScope.EXCLUDES)
			try {
				refreshViewerAfterAdd(excludesViewer);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		else {
			try {
				refreshViewerAfterAdd(includesViewer);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
		updateSortButtonsState();
	}

	private void addStereotypePatternSelected(int type) {

		IWorkbenchProfile activeProfile = TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile();
		TableItem[] items = stereotypeExcludesTable.getItems();
		Collection<IStereotypeInstance> existingStereotypes = new ArrayList<IStereotypeInstance>();
		for (int i = 0; i < items.length; i++) {
			String stereoLabel = ((ScopeStereotypePattern) items[i].getData()).stereotypeName;
			IStereotype stereotype = activeProfile
					.getStereotypeByName(stereoLabel);
			if (stereotype != null) {
				existingStereotypes.add(stereotype.makeInstance());
			}
		}

		BrowseForStereotypeDialog dialog = new BrowseForStereotypeDialog(null,
				existingStereotypes);
		try {
			IStereotype[] selected = dialog
					.browseAvailableStereotypes(getBody().getShell());
			if (selected.length != 0) {
				for (IStereotype stereo : selected) {
					ISegmentScope.ScopeStereotypePattern pattern = new ISegmentScope.ScopeStereotypePattern();
					pattern.type = ISegmentScope.EXCLUDES;
					pattern.stereotypeName = stereo.getName();
					getScope().addStereotypePattern(pattern);
				}
				markPageModified();
				try {
					refreshViewerAfterAdd(stereotypeExcludesViewer);
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		updateSortButtonsState();
	}

	private void refreshViewerAfterAdd(TableViewer viewer)
			throws TigerstripeException {
		viewer.setInput(getScope());
		viewer.refresh(true);
	}

	private void removePatternSelected(int type) {
		Table targetTable = null;
		if (type == ISegmentScope.INCLUDES) {
			targetTable = includesTable;
		} else {
			targetTable = excludesTable;
		}
		int[] indices = targetTable.getSelectionIndices();

		String msg = "Do you really want to remove ";
		if (indices.length > 1) {
			msg += "these patterns?";
		} else {
			msg += "this pattern?";
		}
		if (MessageDialog.openConfirm(getSection().getShell(),
				"Remove Scope Pattern", msg)) {
			for (int index : indices) {
				ScopePattern pat = (ScopePattern) targetTable.getItem(index)
						.getData();
				try {
					getScope().removePattern(pat);
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}

			markPageModified();
			if (type == ISegmentScope.EXCLUDES)
				try {
					refreshViewerAfterRemove(excludesViewer);
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			else {
				try {
					refreshViewerAfterRemove(includesViewer);
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
			updateSortButtonsState();
			updateRemoveButtonsState();
		}
	}

	private void removeStereotypePatternSelected(int type) {
		Table targetTable = null;
		if (type == ISegmentScope.INCLUDES) {
			// targetTable = includesTable;// not supported for now
		} else {
			targetTable = stereotypeExcludesTable;
		}
		int[] indices = targetTable.getSelectionIndices();

		String msg = "Do you really want to remove ";
		if (indices.length > 1) {
			msg += "these stereotypes?";
		} else {
			msg += "this stereotype?";
		}
		if (MessageDialog.openConfirm(getSection().getShell(),
				"Remove Scope Stereotype-based Exclusion", msg)) {
			for (int index : indices) {
				ScopeStereotypePattern pat = (ScopeStereotypePattern) targetTable
						.getItem(index).getData();
				try {
					getScope().removeStereotypePattern(pat);
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}

			markPageModified();
			if (type == ISegmentScope.EXCLUDES)
				try {
					refreshViewerAfterRemove(stereotypeExcludesViewer);
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			else {
				// try {
				// includesViewer.setInput(getScope());
				// includesViewer.refresh(true);
				// } catch (TigerstripeException e) {
				// EclipsePlugin.log(e);
				// }
			}
			updateSortButtonsState();
			updateRemoveButtonsState();
		}
	}

	private void refreshViewerAfterRemove(TableViewer viewer)
			throws TigerstripeException {
		viewer.setInput(getScope());
		viewer.refresh(true);
	}

	private void updateRemoveButtonsState() {
		int[] indices = includesTable.getSelectionIndices();
		removeIncludesButton.setEnabled(indices.length != 0);
		indices = excludesTable.getSelectionIndices();
		removeExcludesButton.setEnabled(indices.length != 0);
		indices = stereotypeExcludesTable.getSelectionIndices();
		removeStereotypeExcludesButton.setEnabled(indices.length != 0);
	}

	private void updateSortButtonsState() {
		sortIncludesButton.setEnabled(includesTable.getItemCount() > 1);
		sortExcludesButton.setEnabled(excludesTable.getItemCount() > 1);
		sortStereotypeExcludesButton.setEnabled(stereotypeExcludesTable
				.getItemCount() > 1);
	}

	private void updateSection() {
		int[] indices = includesTable.getSelectionIndices();
		includesViewer.refresh(true);

		indices = excludesTable.getSelectionIndices();
		excludesViewer.refresh(true);

		indices = stereotypeExcludesTable.getSelectionIndices();
		stereotypeExcludesViewer.refresh(true);

		updateRemoveButtonsState();
		updateSortButtonsState();
	}

	@Override
	public void setFocus() {
		updateSection();
		super.setFocus();
	}

	@Override
	public void refresh() {
		updateSection();
		super.refresh();
	}

}
