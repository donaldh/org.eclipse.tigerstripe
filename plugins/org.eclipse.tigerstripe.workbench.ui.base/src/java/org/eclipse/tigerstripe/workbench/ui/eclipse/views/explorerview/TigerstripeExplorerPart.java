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
package org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.dnd.DelegatingDropAdapter;
import org.eclipse.jdt.internal.ui.dnd.JdtViewerDragAdapter;
import org.eclipse.jdt.internal.ui.dnd.ResourceTransferDragAdapter;
import org.eclipse.jdt.internal.ui.filters.LibraryFilter;
import org.eclipse.jdt.internal.ui.filters.OutputFolderFilter;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerContentProvider;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerLabelProvider;
import org.eclipse.jdt.internal.ui.packageview.SelectionTransferDragAdapter;
import org.eclipse.jdt.internal.ui.packageview.SelectionTransferDropAdapter;
import org.eclipse.jdt.internal.ui.util.JavaUIHelp;
import org.eclipse.jdt.internal.ui.viewsupport.AppearanceAwareLabelProvider;
import org.eclipse.jdt.internal.ui.viewsupport.DecoratingJavaLabelProvider;
import org.eclipse.jdt.internal.ui.viewsupport.FilterUpdater;
import org.eclipse.jdt.internal.ui.viewsupport.JavaElementImageProvider;
import org.eclipse.jdt.internal.ui.viewsupport.ProblemTreeViewer;
import org.eclipse.jdt.internal.ui.viewsupport.StatusBarUpdater;
import org.eclipse.jdt.internal.ui.workingsets.WorkingSetModel;
import org.eclipse.jdt.ui.JavaElementLabels;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationEnd;
import org.eclipse.tigerstripe.api.artifacts.model.IField;
import org.eclipse.tigerstripe.api.artifacts.model.ILabel;
import org.eclipse.tigerstripe.api.artifacts.model.IMethod;
import org.eclipse.tigerstripe.api.artifacts.model.IModelComponent;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.utils.TSElementSorter;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.actions.TigerstripeExplorerActionGroup;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.dnd.FileTransferDragAdapter;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.dnd.FileTransferDropAdapter;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.filters.ClasspathContainerFilter;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.filters.DottedFilesFilter;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.filters.EmptyDefaultPackageFilter;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.ui.part.IShowInTarget;
import org.eclipse.ui.part.ResourceTransfer;
import org.eclipse.ui.part.ShowInContext;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.navigator.LocalSelectionTransfer;

/**
 * This is the Artifact Explorer view.
 * 
 * It provides a Specific explorer view for Tigerstripe Projects, showing
 * artifacts in an organized way, so that the actual pojos can be hidden.
 * 
 * @author Eric Dillon
 * 
 */
public class TigerstripeExplorerPart extends ViewPart implements IMenuListener,
		ISetSelectionTarget, IShowInTarget {

	public boolean show(ShowInContext context) {
		if (context.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) context
					.getSelection();
			Object obj = ssel.getFirstElement();
			if (obj instanceof IModelComponent) {
				if (obj instanceof IAbstractArtifact) {
					revealArtifact((IAbstractArtifact) obj);
				} else if (obj instanceof IField) {
					IField field = (IField) obj;
					revealArtifact((IAbstractArtifact) field
							.getContainingArtifact());
				} else if (obj instanceof IMethod) {
					IMethod method = (IMethod) obj;
					revealArtifact((IAbstractArtifact) method
							.getContainingArtifact());
				} else if (obj instanceof ILabel) {
					ILabel label = (ILabel) obj;
					revealArtifact((IAbstractArtifact) label
							.getContainingArtifact());
				} else if (obj instanceof IAssociationEnd) {
					IAssociationEnd end = (IAssociationEnd) obj;
					revealArtifact((IAbstractArtifact) end
							.getContainingArtifact());
				}
			}
			return true;
		}
		return false;
	}

	public final static String AEXPLORER_ID = "org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew";

	private boolean fIsCurrentLayoutFlat; // true means flat, false means

	// hierachical
	private ISelection fLastOpenSelection;

	private int fRootMode;

	private TigerstripeExplorerActionGroup fActionSet;

	private Menu fContextMenu;

	private FilterUpdater fFilterUpdater;

	// ====================
	private Clipboard clipboard;

	private ProblemTreeViewer treeViewer;

	private NewTigerstripeExplorerContentProvider contentProvider;

	private TigerstripeExplorerLabelProviderWrapper labelProviderWrapper;

	private ISelectionChangedListener fPostSelectionListener;

	public TigerstripeExplorerPart() {
		this.contentProvider = new NewTigerstripeExplorerContentProvider(true);
		// first, use reflection to create an instance of a PackageExplorerLabel
		// provider...
		PackageExplorerLabelProvider labelProvider = getPackageExplorerLabelProviderInstance();
		// then, pass that instance into the constructor for the
		// TigerstripeExplorerLabelProviderWrapper
		// class (creating an instance of the wrapper class that can be used
		// here)
		this.labelProviderWrapper = new TigerstripeExplorerLabelProviderWrapper(
				AppearanceAwareLabelProvider.DEFAULT_TEXTFLAGS
						| JavaElementLabels.P_COMPRESSED,
				AppearanceAwareLabelProvider.DEFAULT_IMAGEFLAGS
						| JavaElementImageProvider.SMALL_ICONS, labelProvider);

		fPostSelectionListener = new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				handlePostSelectionChanged(event);
			}
		};

	}

	private PackageExplorerLabelProvider getPackageExplorerLabelProviderInstance() {
		try {
			Constructor[] constructors = PackageExplorerLabelProvider.class
					.getConstructors();
			for (Constructor constructor : constructors) {
				Class[] parameterTypes = constructor.getParameterTypes();
				if (parameterTypes.length == 1
						&& parameterTypes[0] == PackageExplorerContentProvider.class) {
					// is an Eclipse v3.3 constructor, so set up the Object[]
					// appropriately
					// and create an instance...
					Object[] argList = new Object[] { this.contentProvider };
					return (PackageExplorerLabelProvider) constructor
							.newInstance(argList);
				} else if (parameterTypes.length == 3
						&& parameterTypes[2] == PackageExplorerContentProvider.class) {
					// is an Eclipse v3.2 constructor, so set up the Object[]
					// appropriately
					// and create an instance...
					Object[] argList = new Object[] {
							AppearanceAwareLabelProvider.DEFAULT_TEXTFLAGS
									| JavaElementLabels.P_COMPRESSED,
							AppearanceAwareLabelProvider.DEFAULT_IMAGEFLAGS
									| JavaElementImageProvider.SMALL_ICONS,
							this.contentProvider };
					return (PackageExplorerLabelProvider) constructor
							.newInstance(argList);
				}
			}
		} catch (InstantiationException e) {
			EclipsePlugin.log(e);
		} catch (InvocationTargetException e) {
			EclipsePlugin.log(e);
		} catch (IllegalAccessException e) {
			EclipsePlugin.log(e);
		}
		return null;
	}

	/**
	 * Handles post selection changed in viewer.
	 * 
	 * Links to editor (if option enabled).
	 */
	private void handlePostSelectionChanged(SelectionChangedEvent event) {
		// TODO: see PackageExplorerPart
	}

	/**
	 * Makes the package explorer part visible in the active perspective. If
	 * there isn't a package explorer part registered <code>null</code> is
	 * returned. Otherwise the opened view part is returned.
	 */
	public static TigerstripeExplorerPart openInActivePerspective() {
		try {
			return (TigerstripeExplorerPart) EclipsePlugin.getActivePage()
					.showView(AEXPLORER_ID);
		} catch (PartInitException pe) {
			return null;
		}
	}

	public void selectReveal(ISelection selection) {
		selectReveal(selection, 0);
	}

	private void selectReveal(final ISelection selection, final int count) {
		Control ctrl = getTreeViewer().getControl();
		if (ctrl == null || ctrl.isDisposed())
			return;
		ISelection javaSelection = convertSelection(selection);
		treeViewer.setSelection(javaSelection, true);
		NewTigerstripeExplorerContentProvider provider = (NewTigerstripeExplorerContentProvider) getTreeViewer()
				.getContentProvider();
		ISelection cs = treeViewer.getSelection();
		// If we have Pending changes and the element could not be selected then
		// we try it again on more time by posting the select and reveal
		// asynchronuoulsy
		// to the event queue. See PR
		// http://bugs.eclipse.org/bugs/show_bug.cgi?id=30700
		// for a discussion of the underlying problem.
		if (count == 0 && !javaSelection.equals(cs)) {
			ctrl.getDisplay().asyncExec(new Runnable() {
				public void run() {
					selectReveal(selection, count + 1);
				}
			});
		}
	}

	private ISelection convertSelection(ISelection s) {
		if (!(s instanceof IStructuredSelection))
			return s;

		Object[] elements = ((StructuredSelection) s).toArray();
		if (!containsResources(elements))
			return s;

		for (int i = 0; i < elements.length; i++) {
			Object o = elements[i];
			if (!(o instanceof IJavaElement)) {
				if (o instanceof IResource) {
					IJavaElement jElement = JavaCore.create((IResource) o);
					if (jElement != null && jElement.exists())
						elements[i] = jElement;
				} else if (o instanceof IAdaptable) {
					IResource r = (IResource) ((IAdaptable) o)
							.getAdapter(IResource.class);
					if (r != null) {
						IJavaElement jElement = JavaCore.create(r);
						if (jElement != null && jElement.exists())
							elements[i] = jElement;
						else
							elements[i] = r;
					}
				}
			}
		}

		return new StructuredSelection(elements);
	}

	private boolean containsResources(Object[] elements) {
		for (int i = 0; i < elements.length; i++) {
			Object o = elements[i];
			if (!(o instanceof IJavaElement)) {
				if (o instanceof IResource)
					return true;
				if ((o instanceof IAdaptable)
						&& ((IAdaptable) o).getAdapter(IResource.class) != null)
					return true;
			}
		}
		return false;
	}

	/**
	 * Answers whether this part shows the packages flat or hierarchical.
	 * 
	 * @since 2.1
	 */
	public boolean isFlatLayout() {
		return fIsCurrentLayoutFlat;
	}

	private Object findInputElement() {
		Object input = getSite().getPage().getInput();
		if (input instanceof IWorkspace)
			return JavaCore.create(((IWorkspace) input).getRoot());
		else if (input instanceof IContainer) {
			IJavaElement element = JavaCore.create((IContainer) input);
			if (element != null && element.exists())
				return element;
			return input;
		}
		// 1GERPRT: ITPJUI:ALL - Packages View is empty when shown in Type
		// Hierarchy Perspective
		// we can't handle the input
		// fall back to show the workspace
		return JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());

	}

	@Override
	public void createPartControl(Composite parent) {

		treeViewer = new TSProblemTreeViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		treeViewer.setContentProvider(contentProvider);
		treeViewer.setLabelProvider(new DecoratingJavaLabelProvider(
				labelProviderWrapper, false));

		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(this);
		fContextMenu = menuMgr.createContextMenu(treeViewer.getTree());
		treeViewer.getTree().setMenu(fContextMenu);

		// Register viewer with site. This must be done before making the
		// actions.
		IWorkbenchPartSite site = getSite();
		site.registerContextMenu(menuMgr, treeViewer);
		site.setSelectionProvider(treeViewer);
		// site.getPage().addPartListener(fPartListener);

		// if (fMemento != null) {
		// restoreLinkingEnabled(fMemento);
		// }
		//		
		makeActions(); // call before registering for selection changes

		// Set input after filter and sorter has been set. This avoids resorting
		// and refiltering.
		restoreFilterAndSorter();
		treeViewer.setInput(findInputElement());
		initFrameActions();
		initKeyListener();

		treeViewer.addPostSelectionChangedListener(fPostSelectionListener);

		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				fActionSet.handleDoubleClick(event);
			}
		});

		treeViewer.addOpenListener(new IOpenListener() {
			public void open(OpenEvent event) {
				fActionSet.handleOpen(event);
				fLastOpenSelection = event.getSelection();
			}
		});

		initDragAndDrop();

		IStatusLineManager slManager = getViewSite().getActionBars()
				.getStatusLineManager();
		treeViewer.addSelectionChangedListener(new StatusBarUpdater(slManager));
		// treeViewer.addTreeListener(fExpansionListener);

		// if (fMemento != null)
		// restoreUIState(fMemento);
		// fMemento= null;

		// Set help for the view
		JavaUIHelp.setHelp(treeViewer, IJavaHelpContextIds.PACKAGES_VIEW);

		fillActionBars();

		updateTitle();

		fFilterUpdater = new FilterUpdater(treeViewer);
		ResourcesPlugin.getWorkspace()
				.addResourceChangeListener(fFilterUpdater);

		// Syncing the package explorer has to be done here. It can't be done
		// when restoring the link state since the package explorers input isn't
		// set yet.
		// if (isLinkingEnabled()) {
		// IEditorPart editor= getViewSite().getPage().getActiveEditor();
		// if (editor != null) {
		// editorActivated(editor);
		// }
		// }
	}

	private void initFrameActions() {
		fActionSet.getUpAction().update();
		fActionSet.getBackAction().update();
		fActionSet.getForwardAction().update();
	}

	/**
	 * Create the KeyListener for doing the refresh on the viewer.
	 */
	private void initKeyListener() {
		treeViewer.getControl().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent event) {
				fActionSet.handleKeyEvent(event);
			}
		});
	}

	private void restoreFilterAndSorter() {
		treeViewer.addFilter(new OutputFolderFilter());
		treeViewer.addFilter(new DottedFilesFilter());
		treeViewer.addFilter(new ClasspathContainerFilter());
		treeViewer.addFilter(new EmptyDefaultPackageFilter());
		treeViewer.addFilter(new LibraryFilter());
		setSorter();
		// if (fMemento != null)
		// fActionSet.restoreFilterAndSorterState(fMemento);
	}

	private void setSorter() {
		treeViewer.setSorter(new TSElementSorter());
	}

	private void makeActions() {
		fActionSet = new TigerstripeExplorerActionGroup(this);
	}

	private void fillActionBars() {
		IActionBars actionBars = getViewSite().getActionBars();
		fActionSet.fillActionBars(actionBars);
	}

	@Override
	public void setFocus() {
		if (treeViewer != null)
			treeViewer.getControl().setFocus();
	}

	private void initDragAndDrop() {
		initDrag();
		initDrop();
	}

	private void initDrag() {
		int ops = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
		Transfer[] transfers = new Transfer[] {
				LocalSelectionTransfer.getInstance(),
				ResourceTransfer.getInstance(), FileTransfer.getInstance() };
		TransferDragSourceListener[] dragListeners = new TransferDragSourceListener[] {
				new SelectionTransferDragAdapter(treeViewer),
				new ResourceTransferDragAdapter(treeViewer),
				new FileTransferDragAdapter(treeViewer) };
		treeViewer.addDragSupport(ops, transfers, new JdtViewerDragAdapter(
				treeViewer, dragListeners));
	}

	private void initDrop() {
		int ops = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK
				| DND.DROP_DEFAULT;
		Transfer[] transfers = new Transfer[] {
				LocalSelectionTransfer.getInstance(),
				FileTransfer.getInstance() };
		TransferDropTargetListener[] dropListeners = new TransferDropTargetListener[] {
				new ArtifactComponentTransferDropAdapter(treeViewer),
				new LogicalExplorerNodeTransferDropAdapter(treeViewer),
				new SelectionTransferDropAdapter(treeViewer),
				new FileTransferDropAdapter(treeViewer),
		// new WorkingSetDropAdapter(this)
		};
		treeViewer.addDropSupport(ops, transfers, new DelegatingDropAdapter(
				dropListeners));
	}

	@Override
	public void saveState(IMemento memento) {
		super.saveState(memento);
	}

	public Clipboard getClipboard() {
		if (clipboard == null)
			clipboard = new Clipboard(getSite().getShell().getDisplay());
		return clipboard;
	}

	public TreeViewer getTreeViewer() {
		return this.treeViewer;
	}

	// ED: made it public instead of package level so TigerstripeProjectAuditor
	// can call this
	// upon facet change
	public void projectStateChanged(Object root) {
		Control ctrl = treeViewer.getControl();
		if (ctrl != null && !ctrl.isDisposed()) {
			treeViewer.refresh(root, true);
			// trigger a syntetic selection change so that action refresh their
			// enable state.
			treeViewer.setSelection(treeViewer.getSelection());
		}
	}

	public void toggleLayout() {
		// Update current state and inform content and label providers
		fIsCurrentLayoutFlat = !fIsCurrentLayoutFlat;
		saveLayoutState(null);

		contentProvider.setIsFlatLayout(isFlatLayout());
		labelProviderWrapper.setIsFlatLayout(isFlatLayout());

		treeViewer.getControl().setRedraw(false);
		treeViewer.refresh();
		treeViewer.getControl().setRedraw(true);
	}

	/**
	 * Returns the name for the given element. Used as the name for the current
	 * frame.
	 */
	String getFrameName(Object element) {
		if (element instanceof IJavaElement)
			return ((IJavaElement) element).getElementName();
		else
			return labelProviderWrapper.getText(element);
	}

	/**
	 * Returns the tool tip text for the given element.
	 */
	String getToolTipText(Object element) {
		String result;
		if (!(element instanceof IResource)) {
			if (element instanceof IJavaModel) {
				result = TSExplorerMessages.PackageExplorerPart_workspace;
			} else if (element instanceof IJavaElement) {
				result = JavaElementLabels.getTextLabel(element,
						AppearanceAwareLabelProvider.DEFAULT_TEXTFLAGS);
			} else if (element instanceof IWorkingSet) {
				result = ((IWorkingSet) element).getName();
			} else if (element instanceof WorkingSetModel) {
				result = TSExplorerMessages.PackageExplorerPart_workingSetModel;
			} else {
				result = labelProviderWrapper.getText(element);
			}
		} else {
			IPath path = ((IResource) element).getFullPath();
			if (path.isRoot()) {
				result = TSExplorerMessages.PackageExplorer_title;
			} else {
				result = path.makeRelative().toString();
			}
		}

		return result;
		// // if (fWorkingSetName == null)
		// // return result;
		//
		// String wsstr=
		// Messages.format(TSExplorerMessages.PackageExplorer_toolTip, new
		// String[] { fWorkingSetName });
		// if (result.length() == 0)
		// return wsstr;
		// return Messages.format(TSExplorerMessages.PackageExplorer_toolTip2,
		// new String[] { result, fWorkingSetName });
	}

	public int getRootMode() {
		return fRootMode;
	}

	public boolean showProjects() {
		return fRootMode == org.eclipse.jdt.internal.ui.workingsets.ViewActionGroup.SHOW_PROJECTS;
	}

	/**
	 * Updates the title text and title tool tip. Called whenever the input of
	 * the viewer changes.
	 */
	void updateTitle() {
		Object input = treeViewer.getInput();
		if (input == null || (input instanceof IJavaModel)) {
			setContentDescription(""); //$NON-NLS-1$
			setTitleToolTip(""); //$NON-NLS-1$
		} else {
			String inputText = JavaElementLabels.getTextLabel(input,
					AppearanceAwareLabelProvider.DEFAULT_TEXTFLAGS);
			setContentDescription(inputText);
			setTitleToolTip(getToolTipText(input));
		}
	}

	/**
	 * Called when the context menu is about to open. Override to add your own
	 * context dependent menu contributions.
	 */
	public void menuAboutToShow(IMenuManager menu) {
		JavaPlugin.createStandardGroups(menu);

		fActionSet.setContext(new ActionContext(getSelection()));
		fActionSet.fillContextMenu(menu);
		fActionSet.setContext(null);
	}

	/**
	 * Returns the current selection.
	 */
	private ISelection getSelection() {
		return treeViewer.getSelection();
	}

	// TODO
	private void saveLayoutState(IMemento memento) {
		// if (memento != null) {
		// memento.putInteger(TAG_LAYOUT, getLayoutAsInt());
		// } else {
		// //if memento is null save in preference store
		// IPreferenceStore store=
		// EclipsePlugin.getDefault().getPreferenceStore();
		// store.setValue(TAG_LAYOUT, getLayoutAsInt());
		// }
	}

	public void revealArtifact(IAbstractArtifact artifact) {
		try {
			IResource res = TSExplorerUtils.getIResourceForArtifact(artifact);
			StructuredSelection ssel = new StructuredSelection(res);
			selectReveal(ssel);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}
}
