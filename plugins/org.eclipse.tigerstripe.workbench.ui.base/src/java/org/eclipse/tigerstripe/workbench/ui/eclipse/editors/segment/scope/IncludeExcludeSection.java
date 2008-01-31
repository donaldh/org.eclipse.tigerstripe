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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.segment.scope;

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
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.ScopeAnnotationPattern;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.ScopePattern;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.StereotypeInstance;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.BrowseForStereotypeDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.segment.SegmentEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.segment.TigerstripeSegmentSectionPart;
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

	private Table annotationExcludesTable;

	private TableViewer annotationExcludesViewer;

	private Button addAnnotationExcludesButton;

	private Button sortAnnotationExcludesButton;

	private Button removeAnnotationExcludesButton;

	private class PatternLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof ISegmentScope.ScopePattern) {
				ISegmentScope.ScopePattern pattern = (ISegmentScope.ScopePattern) element;
				return pattern.pattern;
			} else if (element instanceof ISegmentScope.ScopeAnnotationPattern) {
				ISegmentScope.ScopeAnnotationPattern pattern = (ISegmentScope.ScopeAnnotationPattern) element;
				return pattern.annotationName;
			}
			return "<unknown>";
		}
	}

	private class AnnotationPatternContentProvider implements
			IStructuredContentProvider {

		private int targetType = ISegmentScope.INCLUDES;

		/**
		 * 
		 * @param type
		 *            needs to be either {@link ISegmentScope#INCLUDES} or
		 *            {@link ISegmentScope#EXCLUDES}
		 */
		public AnnotationPatternContentProvider(int type) {
			if (type == ISegmentScope.INCLUDES
					|| type == ISegmentScope.EXCLUDES) {
				targetType = type;
			}
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ISegmentScope) {
				ISegmentScope scope = (ISegmentScope) inputElement;
				ScopeAnnotationPattern[] patterns = scope
						.getAnnotationPatterns(targetType);
				return patterns;
			} else
				return new ScopeAnnotationPattern[0];
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

		// annotation excludes table
		l = getToolkit().createLabel(getBody(), "Annotation-based Exclusions");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		l.setLayoutData(td);
		annotationExcludesTable = getToolkit().createTable(getBody(),
				SWT.BORDER | SWT.FLAT);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.rowspan = 3;
		td.heightHint = 100;
		annotationExcludesTable.setEnabled(!this.isReadonly());
		annotationExcludesTable.setLayoutData(td);
		annotationExcludesTable.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				updateRemoveButtonsState();
			}
		});

		annotationExcludesViewer = new TableViewer(annotationExcludesTable);
		try {
			annotationExcludesViewer.setLabelProvider(labelProvider);
			annotationExcludesViewer
					.setContentProvider(new AnnotationPatternContentProvider(
							ISegmentScope.EXCLUDES));
			annotationExcludesViewer.setSorter(new PatternViewSorter());
			annotationExcludesViewer.setInput(getScope());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		addAnnotationExcludesButton = getToolkit().createButton(getBody(),
				"Add", SWT.PUSH);
		td = new TableWrapData(TableWrapData.LEFT);
		// td.maxWidth = 75;
		addAnnotationExcludesButton.setEnabled(!this.isReadonly());
		addAnnotationExcludesButton.setLayoutData(td);
		addAnnotationExcludesButton
				.addSelectionListener(new SelectionListener() {
					public void widgetDefaultSelected(SelectionEvent e) {
						// empty
					}

					public void widgetSelected(SelectionEvent e) {
						addAnnotationPatternSelected(ISegmentScope.EXCLUDES);
					}
				});

		sortAnnotationExcludesButton = getToolkit().createButton(getBody(),
				"Sort", SWT.PUSH);
		td = new TableWrapData(TableWrapData.LEFT);
		// td.maxWidth = 75;
		sortAnnotationExcludesButton.setEnabled(!this.isReadonly());
		sortAnnotationExcludesButton.setLayoutData(td);
		sortAnnotationExcludesButton
				.addSelectionListener(new SelectionListener() {
					public void widgetDefaultSelected(SelectionEvent e) {
						// empty
					}

					public void widgetSelected(SelectionEvent e) {
						sortButtonSelected(annotationExcludesViewer);
					}

				});

		removeAnnotationExcludesButton = getToolkit().createButton(getBody(),
				"Remove", SWT.PUSH);
		td = new TableWrapData(TableWrapData.LEFT);
		td.maxWidth = 75;
		removeAnnotationExcludesButton.setLayoutData(td);
		removeAnnotationExcludesButton.setEnabled(!this.isReadonly());
		removeAnnotationExcludesButton
				.addSelectionListener(new SelectionListener() {
					public void widgetDefaultSelected(SelectionEvent e) {
						// empty
					}

					public void widgetSelected(SelectionEvent e) {
						removeAnnotationPatternSelected(ISegmentScope.EXCLUDES);
					}
				});

		updateSection();

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private ITigerstripeProject getTSProject() throws TigerstripeException {
		IFile file = ((IFileEditorInput) getPage().getEditorInput()).getFile();
		IProject project = file.getProject();
		IAbstractTigerstripeProject aProject = EclipsePlugin
				.getITigerstripeProjectFor(project);
		if (aProject instanceof ITigerstripeProject)
			return (ITigerstripeProject) aProject;
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

	private void addAnnotationPatternSelected(int type) {

		IWorkbenchProfile activeProfile = TigerstripeCore.getWorkbenchProfileSession()
				.getActiveProfile();
		TableItem[] items = annotationExcludesTable.getItems();
		Collection<IStereotypeInstance> existingStereotypes = new ArrayList<IStereotypeInstance>();
		for (int i = 0; i < items.length; i++) {
			String stereoLabel = ((ScopeAnnotationPattern) items[i].getData()).annotationName;
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
					ISegmentScope.ScopeAnnotationPattern pattern = new ISegmentScope.ScopeAnnotationPattern();
					pattern.type = ISegmentScope.EXCLUDES;
					pattern.annotationName = stereo.getName();
					getScope().addAnnotationPattern(pattern);
				}
				markPageModified();
				try {
					refreshViewerAfterAdd(annotationExcludesViewer);
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

	private void removeAnnotationPatternSelected(int type) {
		Table targetTable = null;
		if (type == ISegmentScope.INCLUDES) {
			// targetTable = includesTable;// not supported for now
		} else {
			targetTable = annotationExcludesTable;
		}
		int[] indices = targetTable.getSelectionIndices();

		String msg = "Do you really want to remove ";
		if (indices.length > 1) {
			msg += "these annotations?";
		} else {
			msg += "this annotation?";
		}
		if (MessageDialog.openConfirm(getSection().getShell(),
				"Remove Scope Annotation-based Exclusion", msg)) {
			for (int index : indices) {
				ScopeAnnotationPattern pat = (ScopeAnnotationPattern) targetTable
						.getItem(index).getData();
				try {
					getScope().removeAnnotationPattern(pat);
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}

			markPageModified();
			if (type == ISegmentScope.EXCLUDES)
				try {
					refreshViewerAfterRemove(annotationExcludesViewer);
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
		indices = annotationExcludesTable.getSelectionIndices();
		removeAnnotationExcludesButton.setEnabled(indices.length != 0);
	}

	private void updateSortButtonsState() {
		sortIncludesButton.setEnabled(includesTable.getItemCount() > 1);
		sortExcludesButton.setEnabled(excludesTable.getItemCount() > 1);
		sortAnnotationExcludesButton.setEnabled(annotationExcludesTable
				.getItemCount() > 1);
	}

	private void updateSection() {
		int[] indices = includesTable.getSelectionIndices();
		includesViewer.refresh(true);

		indices = excludesTable.getSelectionIndices();
		excludesViewer.refresh(true);

		indices = annotationExcludesTable.getSelectionIndices();
		annotationExcludesViewer.refresh(true);

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
