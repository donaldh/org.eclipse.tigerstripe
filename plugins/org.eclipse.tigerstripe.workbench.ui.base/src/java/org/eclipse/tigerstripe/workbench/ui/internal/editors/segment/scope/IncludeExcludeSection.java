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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
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
import org.eclipse.tigerstripe.workbench.ui.internal.editors.segment.SegmentEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.segment.TigerstripeSegmentSectionPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class IncludeExcludeSection extends TigerstripeSegmentSectionPart {

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

	private class PatternLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof ISegmentScope.ScopePattern) {
				ISegmentScope.ScopePattern pattern = (ISegmentScope.ScopePattern) element;
				return pattern.pattern;
			} else if (element instanceof ISegmentScope.ScopeStereotypePattern) {
				ISegmentScope.ScopeStereotypePattern pattern = (ISegmentScope.ScopeStereotypePattern) element;
				return pattern.stereotypeName;
			}
			return "<unknown>";
		}
	}

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

	private class PatternViewSorter extends ViewerSorter {

		private boolean sort = false;

		private boolean sorted = false;

		public void setSort(boolean sort) {
			this.sort = sort;
		}

		public boolean isSorted() {
			return sorted;
		}

		public void setSorted(boolean sorted) {
			this.sorted = sorted;
		}

		@Override
		public void sort(Viewer viewer, Object[] elements) {
			if (sort /* && !sorted */) {
				super.sort(viewer, elements);
				sort = false;
			}
		}

	}

	private class PatternCellModifier implements ICellModifier {

		private TableViewer viewer;

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

	private ISegmentScope getScope() throws TigerstripeException {
		return ((ScopePage) getPage()).getScope();
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

		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		getSection().setLayout(layout);

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = 550;
		getSection().setLayoutData(td);

		// Includes table
		Label l = getToolkit().createLabel(getBody(), "Included patterns");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		l.setLayoutData(td);

		includesTable = getToolkit().createTable(getBody(),
				SWT.BORDER | SWT.FLAT | SWT.FULL_SELECTION);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.rowspan = 3;
		td.heightHint = 100;
		td.grabHorizontal = true;
		includesTable.setEnabled(!this.isReadonly());
		includesTable.setLayoutData(td);
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
			includesViewer.setSorter(new PatternViewSorter());

			includesViewer.setInput(getScope());
			includesViewer.setColumnProperties(new String[] { "PATTERN" });
			final TextCellEditor entryListCellEditor = new TextCellEditor(
					includesViewer.getTable());
			includesViewer
					.setCellEditors(new CellEditor[] { entryListCellEditor });
			includesViewer.setCellModifier(new PatternCellModifier(
					includesViewer));
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		addIncludesButton = getToolkit().createButton(getBody(), "Add",
				SWT.PUSH);
		td = new TableWrapData(TableWrapData.LEFT);
		td.maxWidth = 75;
		addIncludesButton.setEnabled(!this.isReadonly());
		addIncludesButton.setLayoutData(td);
		addIncludesButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// empty
			}

			public void widgetSelected(SelectionEvent e) {
				addPatternSelected(ISegmentScope.INCLUDES);
			}
		});

		sortIncludesButton = getToolkit().createButton(getBody(), "Sort",
				SWT.PUSH);
		td = new TableWrapData(TableWrapData.LEFT);
		td.maxWidth = 75;
		sortIncludesButton.setEnabled(!this.isReadonly());
		sortIncludesButton.setLayoutData(td);
		sortIncludesButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// empty
			}

			public void widgetSelected(SelectionEvent e) {
				sortButtonSelected(includesViewer);
			}

		});

		removeIncludesButton = getToolkit().createButton(getBody(), "Remove",
				SWT.PUSH);
		td = new TableWrapData(TableWrapData.LEFT);
		td.maxWidth = 75;
		removeIncludesButton.setLayoutData(td);
		removeIncludesButton.setEnabled(!this.isReadonly());
		removeIncludesButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// empty
			}

			public void widgetSelected(SelectionEvent e) {
				removePatternSelected(ISegmentScope.INCLUDES);
			}
		});

		// Spacer
		l = getToolkit().createLabel(getBody(), "");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		td.heightHint = 15;
		l.setLayoutData(td);

		// excludes table
		l = getToolkit().createLabel(getBody(), "Excluded patterns");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		l.setLayoutData(td);
		excludesTable = getToolkit().createTable(getBody(),
				SWT.BORDER | SWT.FLAT);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.rowspan = 3;
		td.heightHint = 100;
		excludesTable.setEnabled(!this.isReadonly());
		excludesTable.setLayoutData(td);
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
			excludesViewer.setSorter(new PatternViewSorter());
			excludesViewer.setInput(getScope());
			excludesViewer.setColumnProperties(new String[] { "PATTERN" });
			final TextCellEditor entryListCellEditor = new TextCellEditor(
					excludesViewer.getTable());
			excludesViewer
					.setCellEditors(new CellEditor[] { entryListCellEditor });
			excludesViewer.setCellModifier(new PatternCellModifier(
					excludesViewer));
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		addExcludesButton = getToolkit().createButton(getBody(), "Add",
				SWT.PUSH);
		td = new TableWrapData(TableWrapData.LEFT);
		// td.maxWidth = 75;
		addExcludesButton.setEnabled(!this.isReadonly());
		addExcludesButton.setLayoutData(td);
		addExcludesButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// empty
			}

			public void widgetSelected(SelectionEvent e) {
				addPatternSelected(ISegmentScope.EXCLUDES);
			}
		});

		sortExcludesButton = getToolkit().createButton(getBody(), "Sort",
				SWT.PUSH);
		td = new TableWrapData(TableWrapData.LEFT);
		td.maxWidth = 75;
		sortExcludesButton.setEnabled(!this.isReadonly());
		sortExcludesButton.setLayoutData(td);
		sortExcludesButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// empty
			}

			public void widgetSelected(SelectionEvent e) {
				sortButtonSelected(excludesViewer);
			}
		});

		removeExcludesButton = getToolkit().createButton(getBody(), "Remove",
				SWT.PUSH);
		td = new TableWrapData(TableWrapData.LEFT);
		td.maxWidth = 75;
		removeExcludesButton.setLayoutData(td);
		removeExcludesButton.setEnabled(!this.isReadonly());
		removeExcludesButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// empty
			}

			public void widgetSelected(SelectionEvent e) {
				removePatternSelected(ISegmentScope.EXCLUDES);
			}
		});

		l = getToolkit().createLabel(getBody(), " ");
		td = new TableWrapData(TableWrapData.LEFT);
		td.heightHint = 55;
		td.colspan = 2;
		l.setLayoutData(td);

		// Stereotype excludes table
		l = getToolkit().createLabel(getBody(), "Stereotype-based Exclusions");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		l.setLayoutData(td);
		stereotypeExcludesTable = getToolkit().createTable(getBody(),
				SWT.BORDER | SWT.FLAT);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.rowspan = 3;
		td.heightHint = 100;
		stereotypeExcludesTable.setEnabled(!this.isReadonly());
		stereotypeExcludesTable.setLayoutData(td);
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
			stereotypeExcludesViewer.setSorter(new PatternViewSorter());
			stereotypeExcludesViewer.setInput(getScope());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		addStereotypeExcludesButton = getToolkit().createButton(getBody(),
				"Add", SWT.PUSH);
		td = new TableWrapData(TableWrapData.LEFT);
		// td.maxWidth = 75;
		addStereotypeExcludesButton.setEnabled(!this.isReadonly());
		addStereotypeExcludesButton.setLayoutData(td);
		addStereotypeExcludesButton
				.addSelectionListener(new SelectionListener() {
					public void widgetDefaultSelected(SelectionEvent e) {
						// empty
					}

					public void widgetSelected(SelectionEvent e) {
						addStereotypePatternSelected(ISegmentScope.EXCLUDES);
					}
				});

		sortStereotypeExcludesButton = getToolkit().createButton(getBody(),
				"Sort", SWT.PUSH);
		td = new TableWrapData(TableWrapData.LEFT);
		// td.maxWidth = 75;
		sortStereotypeExcludesButton.setEnabled(!this.isReadonly());
		sortStereotypeExcludesButton.setLayoutData(td);
		sortStereotypeExcludesButton
				.addSelectionListener(new SelectionListener() {
					public void widgetDefaultSelected(SelectionEvent e) {
						// empty
					}

					public void widgetSelected(SelectionEvent e) {
						sortButtonSelected(stereotypeExcludesViewer);
					}

				});

		removeStereotypeExcludesButton = getToolkit().createButton(getBody(),
				"Remove", SWT.PUSH);
		td = new TableWrapData(TableWrapData.LEFT);
		td.maxWidth = 75;
		removeStereotypeExcludesButton.setLayoutData(td);
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

		updateSection();

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
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
			if (stereotype == null) {
				EclipsePlugin.logErrorMessage("Unknown stereotype '"
						+ stereoLabel + "'. Ignoring");
				continue;
			} else {
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
		((PatternViewSorter) viewer.getSorter()).setSorted(false);
		viewer.refresh(true);
	}

	protected void sortButtonSelected(TableViewer viewer) {
		PatternViewSorter viewSorter = (PatternViewSorter) viewer.getSorter();
		if (!viewSorter.isSorted()) {
			viewSorter.setSort(true);
			viewer.refresh(true);
			viewSorter.setSorted(true);
		} else {
			viewSorter.setSorted(false);
			viewer.refresh(true);
		}
	}

	protected void markPageModified() {
		SegmentEditor editor = (SegmentEditor) getPage().getEditor();
		editor.pageModified();
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

		PatternViewSorter viewSorter = (PatternViewSorter) viewer.getSorter();
		viewer.setInput(getScope());
		if (viewSorter.isSorted()) {
			viewSorter.setSort(true);
		}
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
