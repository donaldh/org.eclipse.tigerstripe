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
import static org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.Kind.ANNOTATION;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.ScopeAnnotationPattern;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.ScopePattern;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.BrowseForAnnotationsDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class AnnotationIncludeExcludeSection extends ScopeSection {

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
				ScopeAnnotationPattern[] patterns = scope
						.getAnnotationPatterns(targetType);
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
				ScopeAnnotationPattern entry = (ScopeAnnotationPattern) element;
				return entry.annotationID;
			}

			return null;
		}

		public void modify(Object item, String property, Object value) {

			// null indicates that the validator rejected the values
			if (value == null)
				return;

			ScopeAnnotationPattern pattern = (ScopeAnnotationPattern) ((TableItem) item)
					.getData();
			if (pattern.annotationID != null
					&& !pattern.annotationID.equals(value)) {
				pattern.annotationID = (String) value;
				markPageModified();
				viewer.refresh(true);
			}
		}
	}

	public AnnotationIncludeExcludeSection(ScopePage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR);
		setTitle("Annotation Scope Details");
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

		updateSection();

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createIncludeTable(FormToolkit toolkit, Composite parent,
			PatternLabelProvider labelProvider) {
		toolkit.createLabel(parent, "Annotation included patterns:")
				.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

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
			// includesViewer.setColumnProperties(new String[] { "PATTERN" });
			// final TextCellEditor entryListCellEditor = new TextCellEditor(
			// includesViewer.getTable());
			// includesViewer
			// .setCellEditors(new CellEditor[] { entryListCellEditor });
			// includesViewer.setCellModifier(new PatternCellModifier(
			// includesViewer));
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
				sortButtonSelected(INCLUDES, ANNOTATION, includesViewer);
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
		toolkit.createLabel(parent, "Annotations excluded patterns:")
				.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

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
			// excludesViewer.setColumnProperties(new String[] { "PATTERN" });
			// final TextCellEditor entryListCellEditor = new TextCellEditor(
			// excludesViewer.getTable());
			// excludesViewer
			// .setCellEditors(new CellEditor[] { entryListCellEditor });
			// excludesViewer.setCellModifier(new PatternCellModifier(
			// excludesViewer));
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
				sortButtonSelected(EXCLUDES, ANNOTATION, excludesViewer);
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

	private void addPatternSelected(int type) {

		List<AnnotationType> alreadySelected = new ArrayList<AnnotationType>();

		try {
			for (ScopeAnnotationPattern pattern : getScope()
					.getAnnotationPatterns(type)) {
				String pack = Util.packageOf(pattern.annotationID);
				String clazz = Util.nameOf(pattern.annotationID);
				AnnotationType annType = AnnotationPlugin.getManager().getType(
						pack, clazz);
				if (annType != null)
					alreadySelected.add(annType);
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		BrowseForAnnotationsDialog dialog = new BrowseForAnnotationsDialog(
				alreadySelected);
		try {
			AnnotationType[] types = dialog
					.browseAvailableAnnotationTypes(getSection().getShell());
			if (types.length != 0) {
				ScopeAnnotationPattern newPattern = new ScopeAnnotationPattern();
				newPattern.type = type;
				newPattern.annotationID = types[0].getId();
				try {
					getScope().addAnnotationPattern(newPattern);
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
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
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
	}

	private void updateSortButtonsState() {
		sortIncludesButton.setEnabled(includesTable.getItemCount() > 1);
		sortExcludesButton.setEnabled(excludesTable.getItemCount() > 1);
	}

	private void updateSection() {
		includesViewer.refresh(true);
		excludesViewer.refresh(true);

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
