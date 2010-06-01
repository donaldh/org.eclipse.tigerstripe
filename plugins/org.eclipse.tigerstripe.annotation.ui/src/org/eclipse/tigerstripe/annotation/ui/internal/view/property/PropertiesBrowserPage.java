/******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 *    xored software, Inc. - initial API and Implementation (Yuri Strot) 
 ****************************************************************************/
package org.eclipse.tigerstripe.annotation.ui.internal.view.property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.annotation.core.AnnotationFactory;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;
import org.eclipse.tigerstripe.annotation.ui.Images;
import org.eclipse.tigerstripe.annotation.ui.core.IAnnotationActionConstants;
import org.eclipse.tigerstripe.annotation.ui.core.view.AnnotationNote;
import org.eclipse.tigerstripe.annotation.ui.core.view.INote;
import org.eclipse.tigerstripe.annotation.ui.core.view.INoteListener;
import org.eclipse.tigerstripe.annotation.ui.core.view.INoteProvider;
import org.eclipse.tigerstripe.annotation.ui.core.view.NoteLabelProvider;
import org.eclipse.tigerstripe.annotation.ui.internal.actions.RemoveAllNoteAction;
import org.eclipse.tigerstripe.annotation.ui.internal.actions.RemoveNoteAction;
import org.eclipse.tigerstripe.annotation.ui.util.AsyncExecUtil;
import org.eclipse.tigerstripe.annotation.ui.util.WorkbenchUtil;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.views.properties.tabbed.ISectionDescriptor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

/**
 * A property sheet page for annotations.
 * 
 * @author Yuri Strot
 */
public class PropertiesBrowserPage extends TabbedPropertySheetPage implements
		IPropertyChangeListener, IPropertiesSelectionListener {

	private static final int NO_CHANGES = 0;
	private static final int NON_SELECTION_CHANGES = 1;
	private static final int SELECTION_CHANGES = 2;

	/**
	 * the contributor for this property sheet page
	 */
	private INotePropertySheetContributor contributor;

	private IStructuredSelection selectedElements;

	private IWorkbenchPart part;

	private Composite composite;
	private Composite leftPart;

	private TableViewer viewer;

	private INote[] currentSelection;

	private static INote NULL_NOTE = new AnnotationNote(
			AnnotationFactory.eINSTANCE.createAnnotation());

	private StructuredSelection EMPTY_SELECTION = new StructuredSelection();

	private Map<INote, DirtyListener> adapters = new HashMap<INote, DirtyListener>();

	private boolean block = false;

	/**
	 * Constructor
	 * 
	 * @param contributor
	 *            the <code>ITabbedPropertySheetPageContributor</code> for this
	 *            property sheet page
	 */
	public PropertiesBrowserPage(INotePropertySheetContributor contributor,
			INoteProvider[] providers) {
		super(contributor);
		this.contributor = contributor;
		this.providers = providers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.IPage#dispose()
	 */
	public void dispose() {
		super.dispose();
		/**
		 * Clean up
		 */
		contributor = null;
		selectedElements = null;
		removeListeners();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.IPage#setActionBars(org.eclipse.ui.IActionBars)
	 */
	public void setActionBars(IActionBars bars) {
		IToolBarManager manager = bars.getToolBarManager();

		int providersSize = providers.length;

		filters = new Action[providersSize];

		for (int i = 0; i < providersSize; i++) {
			filters[i] = new NoteFilterAction(providers[i]);
			manager.add(filters[i]);
		}

		addAction = new Action("Add") {
			public void run() {
				PropertiesSelectionManager.getInstance().getSelection()
						.addDefaultValue();
			}
		};
		addAction.setImageDescriptor(Images.getDescriptor(Images.ADD));
		manager.add(addAction);

		removeAction = new Action("Remove") {
			public void run() {
				PropertiesSelectionManager.getInstance().getSelection()
						.remove();
			}
		};
		removeAction.setImageDescriptor(Images.getDescriptor(Images.REMOVE));
		manager.add(removeAction);

		saveAction = new Action("Save") {
			public void run() {
				saveAnnotation();
			}
		};
		saveAction.setImageDescriptor(Images.getDescriptor(Images.SAVE));
		manager.add(saveAction);

		saveAllAction = new Action("Save All") {
			public void run() {
				saveAllAnnotations();
			}
		};
		saveAllAction.setImageDescriptor(Images.getDescriptor(Images.SAVE_ALL));
		manager.add(saveAllAction);

		revertAction = new Action("Revert") {
			public void run() {
				revertAnnotation();
			}
		};
		revertAction.setImageDescriptor(Images.getDescriptor(Images.REVERT));
		manager.add(revertAction);

		revertAllAction = new Action("Revert All") {
			public void run() {
				revertAllAnnotations();
			}
		};
		revertAllAction.setImageDescriptor(Images
				.getDescriptor(Images.REVERT_ALL));
		manager.add(revertAllAction);

		bars.clearGlobalActionHandlers();
		IHandlerService service = (IHandlerService) getSite().getService(
				IHandlerService.class);
		service.activateHandler("org.eclipse.ui.file.save",
				new AbstractHandler() {
					public Object execute(ExecutionEvent arg0)
							throws ExecutionException {
						if (saveAction.isEnabled())
							saveAction.run();
						return null;
					}
				});
		service.activateHandler("org.eclipse.ui.file.saveAll",
				new AbstractHandler() {
					public Object execute(ExecutionEvent arg0)
							throws ExecutionException {
						if (saveAllAction.isEnabled())
							saveAllAction.run();
						return null;
					}
				});
		bars.updateActionBars();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse
	 * .jface.util.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		/* not implemented */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.IPage#setFocus()
	 */
	public void setFocus() {
		getControl().setFocus();
	}

	private void addListeners() {
		PropertiesSelectionManager.getInstance().addListener(this);
	}

	private void removeListeners() {
		PropertiesSelectionManager.getInstance().removeListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite
	 * )
	 */
	public void createControl(Composite parent) {

		TabbedPropertySheetWidgetFactory factory = new TabbedPropertySheetWidgetFactory();

		composite = factory.createComposite(parent);
		composite.setLayout(getEmptyLayout(2));

		leftPart = factory.createComposite(composite);
		GridData data = new GridData(GridData.FILL_VERTICAL);
		data.widthHint = 200;
		leftPart.setLayoutData(data);
		createNavigateMenu(leftPart, factory);
		leftPart.setVisible(false);

		Composite rightPart = factory.createComposite(composite);
		FormLayout layout = new FormLayout();
		rightPart.setLayout(layout);
		rightPart.setLayoutData(new GridData(GridData.FILL_BOTH));

		super.createControl(rightPart);
		addListeners();
	}

	private void initMenu(Control control, IMenuListener menuListener) {
		MenuManager menuMgr = new LimitMenuManager(new String[] {
				IAnnotationActionConstants.ANNOTATION_PROPERTIES,
				IAnnotationActionConstants.ANNOTATION_PROPERTIES_GROUP });
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(menuListener);
		Menu menu = menuMgr.createContextMenu(control);
		control.setMenu(menu);
		getSite().registerContextMenu(contributor.getContributorId(), menuMgr,
				viewer);
	}

	private Action[] filters;
	private Action addAction;
	private Action removeAction;
	private Action saveAction;
	private Action saveAllAction;
	private Action revertAction;
	private Action revertAllAction;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.annotation.ui.internal.view.property.
	 * IPropertiesSelectionListener#selectionChanged(java.util.List, int)
	 */
	public void selectionChanged(PropertySelection selection) {
		int status = PropertySelection.SINGLE_SELECTION;
		if (selection != null && !selection.isReadOnly()) {
			status = selection.getStatus();
		}
		switch (status) {
		case PropertySelection.SINGLE_SELECTION:
			addAction.setToolTipText("Add to the list");
			removeAction.setToolTipText("Remove element");
			break;
		case PropertySelection.CHILD_SELECTION:
			addAction.setToolTipText("Insert element before selection");
			removeAction.setToolTipText("Remove selected element");
			break;
		default:
			addAction.setToolTipText("Append element to the end of the list");
			removeAction.setToolTipText("Clear list");
			break;
		}
		addAction.setEnabled(status > 0);
		removeAction.setEnabled(status > 0);
	}

	protected List<ISectionDescriptor> getDescriptors(
			ISectionDescriptor descriptor) {
		ArrayList<ISectionDescriptor> descriptors = new ArrayList<ISectionDescriptor>();
		descriptors.add(descriptor);
		return descriptors;
	}

	private void fillContentMenu(IMenuManager manager) {
		String group = IAnnotationActionConstants.ANNOTATION_PROPERTIES_GROUP;
		manager.add(new GroupMarker(group));

		INote note = getSelectedNote();
		for (INoteProvider provider : providers) {
			provider.fillMenu(manager, group, note);
		}

		if (note != null && !note.isReadOnly()) {
			RemoveNoteAction action = new RemoveNoteAction(note);
			ActionContributionItem item = new ActionContributionItem(action);
			manager.appendToGroup(group, item);
		}

		if (currentSelection != null) {
			RemoveAllNoteAction removeAll = new RemoveAllNoteAction(
					currentSelection);
			if (removeAll.isEnabled()) {
				ActionContributionItem item = new ActionContributionItem(
						removeAll);
				manager.appendToGroup(group, item);
			}
		}
	}

	private GridLayout getEmptyLayout(int columns) {
		GridLayout layout = new GridLayout();
		layout.numColumns = columns;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		return layout;
	}

	protected Composite createTitle(Composite parent,
			TabbedPropertySheetWidgetFactory factory) {
		parent.setLayout(new FormLayout());

		TabbedPropertyTitle title = new TabbedPropertyTitle(parent, factory);
		title.setTitle("Annotations", null);

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(0, 0);
		title.setLayoutData(formData);

		Composite composite = factory.createComposite(parent, SWT.NONE);

		formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(title, 0);
		formData.bottom = new FormAttachment(100, 0);
		composite.setLayoutData(formData);

		return composite;
	}

	private INote getSelectedNote() {
		int index = viewer.getTable().getSelectionIndex();
		if (currentSelection != null) {
			if (index >= 0 && index < currentSelection.length)
				return currentSelection[index];
		}
		return null;
	}

	private void save(INote note) {
		DirtyListener listener = adapters.get(note);
		if (listener == null || listener.isDirty()) {
			block = true;
			note.save();
			if (listener != null)
				listener.clear();
			block = false;
		}
	}

	private void revert(INote note) {
		DirtyListener listener = adapters.get(note);
		block = true;
		note.revert();
		block = false;
		if (listener != null)
			listener.clear();
	}

	public void saveAnnotation() {
		INote note = getSelectedNote();
		if (note != null) {
			save(note);
		}
	}

	public void saveAllAnnotations() {
		if (currentSelection != null) {
			for (int i = 0; i < currentSelection.length; i++) {
				save(currentSelection[i]);
			}
		}
	}

	public void revertAnnotation() {
		INote note = getSelectedNote();
		if (note != null) {
			revert(note);
		}
		updatePageSelection();
	}

	private void updatePageSelection() {
		INote note = getSelectedNote();
		if (note != null) {
			superSelectionChanged(part, new StructuredSelection(note) {
				public boolean equals(Object o) {
					return false;
				}
			});
		}
	}

	public void revertAllAnnotations() {
		if (currentSelection != null) {
			for (int i = 0; i < currentSelection.length; i++) {
				revert(currentSelection[i]);
			}
		}
		updatePageSelection();
	}

	protected void createNavigateMenu(Composite parent,
			TabbedPropertySheetWidgetFactory factory) {
		Composite composite = createTitle(parent, factory);
		FormLayout formLayout = new FormLayout();
		formLayout.marginTop = 4;
		formLayout.marginRight = 4;
		composite.setLayout(formLayout);

		final Table table = new Table(composite, SWT.BORDER
				| SWT.FULL_SELECTION);
		factory.paintBordersFor(composite);

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);

		table.setLayoutData(formData);
		new TableTooltip(table) {
			@Override
			protected String getTooltip(TableItem item) {
				if (item.getData() instanceof INote) {
					INote note = (INote) item.getData();
					return note.getDescription();
				}
				return null;
			}
		};

		viewer = new TableViewer(table);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				INote note = getSelectedNote();
				if (note != null) {
					setPageSelection(note);
					updateStatus();
				}
			}
		});
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new NoteLabelProvider());

		initMenu(table, new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContentMenu(manager);
			}
		});
	}

	protected void setPageSelection(INote note) {
		superSelectionChanged(part, new StructuredSelection(note));
	}

	protected void setPageEmpty() {
		superSelectionChanged(part, new StructuredSelection(NULL_NOTE));
	}

	protected void adapt(int index) {
		INote note = currentSelection[index];
		DirtyListener listener = adapters.remove(note);
		if (listener != null) {
			listener.dispose();
		}
		UIDirtyAdapter adapter = new UIDirtyAdapter(note);
		adapters.put(note, adapter);
	}

	protected void updatePage() {
		if (viewer != null && !viewer.getTable().isDisposed()) {
			dirtyChanged(NO_CHANGES);
			currentSelection = getNotes();
			int newSelection = viewer.getTable().getSelectionIndex();
			boolean showPage = currentSelection != null;
			if (showPage) {
				viewer.setInput(currentSelection);
				for (int i = 0; i < currentSelection.length; i++) {
					adapt(i);
				}
				int itemCount = viewer.getTable().getItemCount();
				if (newSelection < 0 || newSelection >= itemCount) {
					newSelection = 0;
				}
				if (newSelection >= 0 && itemCount > newSelection) {
					viewer.getTable().select(newSelection);
					setPageSelection(currentSelection[newSelection]);
				} else {
					setPageEmpty();
				}
			} else {
				superSelectionChanged(null, EMPTY_SELECTION);
			}
			leftPart.setVisible(showPage);
			composite.layout();
		}
	}

	private void dirtyChanged(int status) {
		switch (status) {
		case NO_CHANGES:
			saveAction.setEnabled(false);
			saveAllAction.setEnabled(false);
			revertAction.setEnabled(false);
			revertAllAction.setEnabled(false);
			break;
		case NON_SELECTION_CHANGES:
			saveAction.setEnabled(false);
			saveAllAction.setEnabled(true);
			revertAction.setEnabled(false);
			revertAllAction.setEnabled(true);
			break;
		case SELECTION_CHANGES:
			saveAction.setEnabled(true);
			saveAllAction.setEnabled(true);
			revertAction.setEnabled(true);
			revertAllAction.setEnabled(true);
			break;
		}
	}

	private void superSelectionChanged(IWorkbenchPart part, ISelection selection) {
		INote note = getSelectedNote();
		if (note != null)
			TabDescriptionManipulator.getInstance().update(note);
		super.selectionChanged(part, selection);
	}

	private void updateStatus() {
		int status = NO_CHANGES;
		if (currentSelection != null) {
			INote selected = getSelectedNote();
			for (int i = 0; i < currentSelection.length; i++) {
				DirtyListener listener = adapters.get(currentSelection[i]);
				if (listener != null && listener.isDirty()) {
					if (selected == currentSelection[i]) {
						status = SELECTION_CHANGES;
						break;
					}
					status = NON_SELECTION_CHANGES;
				}
			}
		}
		dirtyChanged(status);
	}

	private class UIDirtyAdapter extends DirtyListener {

		public UIDirtyAdapter(INote note) {
			super(note);
		}

		@Override
		protected void update() {
			viewer.refresh(getNote());
			updateStatus();
		}

	}

	@Override
	public Control getControl() {
		return composite;
	}

	protected void disposeSelection() {
		List<INote> dirties = new ArrayList<INote>();
		if (currentSelection != null) {
			for (INote node : currentSelection) {
				DirtyListener listener = adapters.remove(node);
				if (listener != null) {
					if (listener.isDirty())
						dirties.add(node);
					listener.dispose();
				}
			}
		}
		for (DirtyListener listener : adapters.values()) {
			listener.dispose();
		}
		adapters = new HashMap<INote, DirtyListener>();
		if (dirties.size() > 0) {
			block = true;
			Shell shell = WorkbenchUtil.getShell();
			if (shell != null) {
				INote[] nodes = dirties.toArray(new INote[dirties.size()]);
				MessageBox message = new MessageBox(shell, SWT.ICON_QUESTION
						| SWT.YES | SWT.NO);
				message
						.setMessage("Annotations have been modified. Save changes?");
				message.setText("Save Annotations");
				if (message.open() == SWT.YES)
					for (INote node : nodes) {
						node.save();
					}
			}
			block = false;
		}
	}

	private void setSelected(ISelection selection) {
		if (selection instanceof IStructuredSelection)
			selectedElements = (IStructuredSelection) selection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.
	 * IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (block)
			return;
		disposeSelection();
		setSelected(selection);
		this.part = part;
		AsyncExecUtil.run(composite, new Runnable() {

			public void run() {
				updatePage();
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ILabelProviderListener#labelProviderChanged
	 * (org.eclipse.jface.viewers.LabelProviderChangedEvent)
	 */
	public void labelProviderChanged(LabelProviderChangedEvent event) {
		if (event.getElements() == null && getControl() != null) {
			super.labelProviderChanged(event);
			return;
		}

		// TODO Need to understand why is it for?
		// IStructuredSelection structuredSelection = getSelectedElements();
		// if (structuredSelection == null) {
		// return;
		// }
		//
		// List<EObject> selection = new ArrayList<EObject>();
		// for (Iterator<?> e = structuredSelection.iterator(); e.hasNext();) {
		// Object next = e.next();
		// if (next instanceof IAdaptable) {
		// EObject object = (EObject) ((IAdaptable) next)
		// .getAdapter(EObject.class);
		// if (object != null)
		// selection.add(object);
		// } else if (next instanceof EObject) {
		// selection.add((EObject) next);
		// }
		// }
		//
		// if (selection.isEmpty())
		// return;
		//
		// List<EObject> elementsAffected = new ArrayList<EObject>();
		// for (int i = 0; i < event.getElements().length; i++) {
		// Object next = event.getElements()[i];
		// if (next instanceof IAdaptable) {
		// EObject object = (EObject) ((IAdaptable) next)
		// .getAdapter(EObject.class);
		// if (object != null)
		// elementsAffected.add(object);
		// } else if (next instanceof EObject) {
		// elementsAffected.add((EObject) next);
		// }
		// }
		//
		// selection.retainAll(elementsAffected);
		// if (!selection.isEmpty())
		super.labelProviderChanged(event);

	}

	private INote[] getNotes() {
		boolean notable = false;
		for (INoteProvider provider : providers) {
			if (provider.isNotable()) {
				notable = true;
				break;
			}
		}
		if (!notable)
			return null;
		List<INote> notes = new ArrayList<INote>();
		Iterator<?> it = selectedElements.iterator();
		while (it.hasNext()) {
			Object object = (Object) it.next();
			if (object instanceof INote) {
				notes.add((INote) object);
			}
		}
		return notes.toArray(new INote[notes.size()]);
	}

	class NoteFilterAction extends Action implements INoteListener {
		private INoteProvider provider;

		public NoteFilterAction(INoteProvider provider) {
			super();
			this.provider = provider;
			provider.addListener(this);
			String label = provider.getLabel();
			setText("Show " + label);
			setImageDescriptor(provider.getImageDescriptor());
			setToolTipText(getText());
			setChecked(!isHideNotes(provider));
		}

		public void run() {
			valueChanged(isChecked());
		}

		private void valueChanged(boolean checked) {
			setChecked(checked);
			setHideNotes(provider, !checked);
			contributor.updateNotes();
		}

		public void notesChanged(INote[] notes) {
			valueChanged(true);
		}
	}

	private String getHideNotesPropertyName(INoteProvider provider) {
		return "AnnotationPropertyView.NotesFilter." + provider.getLabel()
				+ ".isChecked";
	}

	boolean isHideNotes(INoteProvider provider) {
		return AnnotationUIPlugin.getDefault().getPreferenceStore().getBoolean(
				getHideNotesPropertyName(provider));
	}

	void setHideNotes(INoteProvider provider, boolean value) {
		AnnotationUIPlugin.getDefault().getPreferenceStore().setValue(
				getHideNotesPropertyName(provider), value);
	}

	private INoteProvider[] providers;

}