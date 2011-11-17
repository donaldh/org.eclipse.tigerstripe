package org.eclipse.tigerstripe.workbench.ui.internal.views.hierarchy;

import static org.eclipse.jface.window.Window.OK;
import static org.eclipse.swt.SWT.HORIZONTAL;
import static org.eclipse.swt.SWT.H_SCROLL;
import static org.eclipse.swt.SWT.LEFT;
import static org.eclipse.swt.SWT.NONE;
import static org.eclipse.swt.SWT.SINGLE;
import static org.eclipse.swt.SWT.VERTICAL;
import static org.eclipse.swt.SWT.V_SCROLL;
import static org.eclipse.tigerstripe.workbench.model.FqnUtils.getFqnForResource;
import static org.eclipse.tigerstripe.workbench.ui.internal.views.hierarchy.HierarchyView.ViewType.BOTH;
import static org.eclipse.tigerstripe.workbench.ui.internal.views.hierarchy.HierarchyView.ViewType.DOWN;
import static org.eclipse.tigerstripe.workbench.ui.internal.views.hierarchy.HierarchyView.ViewType.UP;
import static org.eclipse.tigerstripe.workbench.utils.AdaptHelper.adapt;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.tigerstripe.workbench.IModelAnnotationChangeDelta;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeWorkspaceNotifier;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.model.IContextProjectAware;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMember;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ReadOnlyArtifactEditorInput;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.AbstractArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions.TSOpenAction;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ViewPart;

/**
 * The view which display hierarchy of a artifact. Artifact is set via 
 * {@link HierarchyView#setInput(IAbstractArtifact)} method. 
 * <br>
 * <h5>Has three view modes:</h5>
 * <ul>
 * <li>Subtypes hierarchy. Shows the given artifact and its extending or implementing artifacts 
 * as <b>childs</b> in tree</li>
 * 
 * <li>Supertype hierarchy. Shows the given artifact and its extended or implemented artifacts 
 * as <b>childs</b> in tree</li>
 * 
 * <li>Supertype and subtype hierarchy(both). Shows the given artifact and its extended or implemented artifacts 
 * as <b>parents</b> and extending or implementing as <b>childs</b></li>
 * </ul>
 * Default mode is Supertype and subtype hierarchy(both).
 * <br>
 * The behavior of the view is similar to behavior of the analogous JDT view.
 */
public class HierarchyView extends ViewPart implements ITigerstripeChangeListener {

	public static final String ID = "org.eclipse.tigerstripe.view.hierarchy";
	private static final int HISTORY_LIMIT = 12;
	private static final Object[] EMPTY = new Object[0];
	
	private TableViewer membersViewer;
	private TreeViewer hierarchyViewer;
	private Label selectedImg;
	private Label selectedName;
	private SashForm sashForm;
	private final Deque<IAbstractArtifact> history = new LinkedList<IAbstractArtifact>();
	private List<ChangeHierarchyViewTypeAction> viewTypeGroup;
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(VERTICAL));
		sashForm = new SashForm(parent, VERTICAL);

		Composite upPart = new Composite(sashForm, NONE);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(upPart);
		createHierarchyTree(upPart);
		hierarchyViewer.getTree().setLayoutData(
				new GridData(GridData.FILL_BOTH));

		Composite bottomPart = new Composite(sashForm, NONE);
		GridLayoutFactory.fillDefaults().numColumns(1).spacing(0, 0).applyTo(bottomPart);

		Composite selectedPanel = new Composite(bottomPart, SWT.NONE);
		selectedPanel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		GridLayoutFactory.fillDefaults().numColumns(2).spacing(0, 0).equalWidth(false).applyTo(selectedPanel);
		
		selectedImg = new Label(selectedPanel, LEFT);
		GridDataFactory.fillDefaults().hint(16, 16).minSize(16, 16).applyTo(selectedImg);
		
		selectedName = new Label(selectedPanel, LEFT);
		selectedName.setLayoutData(new GridData(GridData.FILL_BOTH));
		selectedName.setText(" ");

		Label separator = new Label(selectedPanel, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridDataFactory.fillDefaults().grab(true, true).span(2, 1).applyTo(separator);
		
		createMembersList(bottomPart);
		membersViewer.getControl().setLayoutData(
				new GridData(GridData.FILL_BOTH));

		addMenuActions();
		
		sashForm.setWeights(new int[] { 2, 1 });
		applyOrientation(parent);
		addResizeListener(parent);
		
		TigerstripeWorkspaceNotifier.INSTANCE
				.addTigerstripeChangeListener(this, ALL);
	}

	@Override
	public void dispose() {
		TigerstripeWorkspaceNotifier.INSTANCE
				.removeTigerstripeChangeListener(this);
		super.dispose();
	}
	
	/**
	 * Add resize listener for changing orientation of view(vertical or horizontal)
	 * when proportions are changed.
	 */
	private void addResizeListener(final Composite parent) {
		parent.addControlListener(new ControlListener() {
			public void controlMoved(ControlEvent e) {
			}
			public void controlResized(ControlEvent e) {
				if (!parent.isDisposed()) {
					applyOrientation(parent);
					sashForm.layout();
				}
			}
		});
	}

	private void applyOrientation(final Composite parent) {
		Point size = parent.getSize();
		if (size.y > size.x) {
			sashForm.setOrientation(VERTICAL);
			sashForm.setWeights(new int[] { 2, 1 });
		} else {
			sashForm.setOrientation(HORIZONTAL);
			sashForm.setWeights(new int[] { 1, 1 });
		}
	}

	
	private void createHierarchyTree(Composite container) {
		hierarchyViewer = new TreeViewer(container, SINGLE | H_SCROLL | V_SCROLL);
		hierarchyViewer.setContentProvider(new HierarchyContentProvider());
		hierarchyViewer.setLabelProvider(new HierarchyLabelProvider());
		hierarchyViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					Object element = ((IStructuredSelection) selection)
							.getFirstElement();
					if (element instanceof Node) {
						IWorkbenchWindow window = PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow();
						if (window != null) {
							IWorkbenchPage activePage = window.getActivePage();
							IAbstractArtifact artifact = ((Node) element).artifact;
							try {
								if (activePage != null) {
									IEditorInput input;
									if (artifact instanceof IContextProjectAware) {
										input = new ReadOnlyArtifactEditorInput(
												null, artifact);
									} else {
										if (!exitsts(artifact)) {
											return;
										}
										IResource resource = adapt(artifact,
												IResource.class);
										if (resource instanceof IFile) {
											input = new FileEditorInput(
													(IFile) resource);
										} else {
											input = new ReadOnlyArtifactEditorInput(
													null, artifact);
										}
									}
									IDE.openEditor(
											activePage,
											input,
											TSOpenAction
													.getEditorIdForArtifact(artifact));
								}
							} catch (Exception e) {
								BasePlugin.log(e);
							}
						}
					}
				}
			}
		});

		hierarchyViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {

					public void selectionChanged(SelectionChangedEvent event) {
						Node node = (Node) ((IStructuredSelection) event
								.getSelection()).getFirstElement();
						if (node != null) {
							membersViewer.setInput(node.artifact);
							selectedName.setText(node.artifact.getName());
							selectedImg
									.setImage(SimpleNameArtifactLabelProvider.INSTANCE
											.getImage(node.artifact));
						} else {
							selectedName.setText("");
							selectedImg.setImage(null);
						}
					}
				});
	}

	private static boolean exitsts(IAbstractArtifact artifact) {
		ArtifactManager artifactManager = getArtifactManager(artifact);
		if (artifactManager == null) {
			return false;
		}
		return artifactManager.getArtifactByFullyQualifiedName(
				artifact.getFullyQualifiedName(), false,
				new NullProgressMonitor()) != null;
	}
	
	private static ArtifactManager getArtifactManager(IAbstractArtifact artifact) {
		try {
			ITigerstripeModelProject project;
				project = artifact.getProject();
			if (project == null) {
				return null;
			}
			IArtifactManagerSession session = project.getArtifactManagerSession();
			if (session == null) {
				return null;
			}
			return session.getArtifactManager();
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
		return null;
	}
	
	private void createMembersList(Composite container) {
		membersViewer = new TableViewer(container, SINGLE | H_SCROLL | V_SCROLL);
		membersViewer.setContentProvider(new MembersContentProvider());
		membersViewer.setLabelProvider(new MembersLabelProvider());
	}

	private void addMenuActions() {
		IActionBars actionBars = getViewSite().getActionBars();
		IToolBarManager toolBarManager = actionBars.getToolBarManager();
		viewTypeGroup = new ArrayList<ChangeHierarchyViewTypeAction>(3);
		viewTypeGroup.add(new ChangeHierarchyViewTypeAction(BOTH));
		viewTypeGroup.add(new ChangeHierarchyViewTypeAction(UP));
		viewTypeGroup.add(new ChangeHierarchyViewTypeAction(DOWN));
		for (ChangeHierarchyViewTypeAction action : viewTypeGroup) {
			toolBarManager.add(action);
		}
		toolBarManager.add(new Separator());
		toolBarManager.add(new HistoryAction());
	}

	public ViewType getCurrentViewType() {
		for (ChangeHierarchyViewTypeAction action : viewTypeGroup) {
			if (action.isChecked()) {
				return action.getViewType();
			}
		}
		return BOTH;
	}

	@Override
	public void setFocus() {
		hierarchyViewer.getTree().setFocus();
	}

	public void reset() {
		clearViewers();
		history.clear();
	}

	private void clearViewers() {
		hierarchyViewer.setInput(null);
		membersViewer.setInput(null);
	}

	public void setInput(IAbstractArtifact input) {
		setInputInternal(input);
		if (input != null) {
			addToHistory(input);
		}
	}

	public IAbstractArtifact getInput() {
		return (IAbstractArtifact) hierarchyViewer.getInput();
	}

	private void addToHistory(IAbstractArtifact artifact) {
		history.remove(artifact);
		history.addFirst(artifact);
		while (history.size() >= HISTORY_LIMIT) {
			history.removeLast();
		}
	}

	private void setInputInternal(IAbstractArtifact input) {
		hierarchyViewer.setInput(input);
		HierarchyContentProvider cp = getHierarchyCP();
		hierarchyViewer.setSelection(new StructuredSelection(cp.main));
		hierarchyViewer.expandToLevel(cp.main, 1);
		hierarchyViewer.refresh();
		setContentDescription(input.getName());
	}

	private HierarchyContentProvider getHierarchyCP() {
		return (HierarchyContentProvider) hierarchyViewer.getContentProvider();
	}

	private void listen(final MenuItem item) {
		item.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				setInputInternal((IAbstractArtifact) item.getData());
			}
		});
	}

	private Node findNode(final String fqn) {
		HierarchyContentProvider hierarchyCP = getHierarchyCP();
		if (hierarchyCP.root == null) {
			return null;
		}
		final Node[] result = new Node[1];
		traverseNodes(hierarchyCP.root, new NodeVisitor() {
			
			public boolean visit(Node node) {
				if (fqn.equals(node.artifact.getFullyQualifiedName())) {
					result[0] = node;
					return false;
				}
				return true;
			}
		});
		return result[0];
	}

	private boolean traverseNodes(Node root, NodeVisitor visitor) {
		if (!visitor.visit(root)) {
			return false;
		}
		for (Node node : root.children) {
			if (!traverseNodes(node, visitor)) {
				return false;
			}
		}
		return true;
	}
	
	public void projectAdded(IAbstractTigerstripeProject project) {
	}

	public void projectDeleted(String projectName) {
	}

	public void descriptorChanged(IResource changedDescriptor) {
	}

	public void modelChanged(IModelChangeDelta[] delta) {
	}

	public void annotationChanged(IModelAnnotationChangeDelta[] delta) {
	}

	private boolean existsInTree(IAbstractArtifact artifact) {
		return findNode(artifact.getFullyQualifiedName()) != null;
	}
	
	/**
	 * Check for is artifact present in current hierarchy model
	 */
	private boolean hasMentions(IAbstractArtifact artifact) {
		if (existsInTree(artifact)) {
			return true;
		}
		IAbstractArtifact extendedArtifact = artifact.getExtendedArtifact();
		if (extendedArtifact != null) {
			if (existsInTree(extendedArtifact)) {
				return true;
			}
		}
		for (IAbstractArtifact art : artifact.getExtendingArtifacts()) {
			if (existsInTree(art)) {
				return true;
			}
		}
		for (IAbstractArtifact art : artifact.getImplementedArtifacts()) {
			if (existsInTree(art)) {
				return true;
			}
		}
		for (IAbstractArtifact art : artifact.getImplementingArtifacts()) {
			if (existsInTree(art)) {
				return true;
			}
		}
		return false;
	}
	
	public void artifactResourceChanged(IResource changedArtifactResource) {
		IAbstractArtifact artifact = adapt(changedArtifactResource, IAbstractArtifact.class);
		if (artifact != null) {
			if (hasMentions(artifact)) {
				refreshInput();
			}
		}
	}

	private void refreshInput() {
		HierarchyContentProvider hcp = getHierarchyCP();
		if (hcp.main == null) {
			return;
		}
		ArtifactManager artifactManager = getArtifactManager(hcp.main.artifact);
		if (artifactManager != null) {
			final IAbstractArtifactInternal refreshed = artifactManager.getArtifactByFullyQualifiedName(
					hcp.main.artifact.getFullyQualifiedName(), false,
					new NullProgressMonitor());
			
			if (refreshed != null) {
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
					
					public void run() {
						setInputInternal(refreshed);
					}
				});
			}
		}
	}

	public void artifactResourceAdded(IResource addedArtifactResource) {
		artifactResourceChanged(addedArtifactResource);
	}

	public void artifactResourceRemoved(IResource removedArtifactResource) {
		String fqn = getFqnForResource(removedArtifactResource);
		if (fqn != null) {
			Iterator<IAbstractArtifact> it = history.iterator();
			while (it.hasNext()) {
				IAbstractArtifact artifact = it.next();
				if (fqn.equals(artifact.getFullyQualifiedName())) {
					it.remove();
				}
			}
			Node main = getHierarchyCP().main;
			if (main != null && fqn.equals(main.artifact.getFullyQualifiedName())) {
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
					
					public void run() {
						clearViewers();
					}
				});
			} else if (findNode(fqn) != null) {
				refreshInput();
			}
		}
	}

	public void activeFacetChanged(ITigerstripeModelProject project) {
	}
	
	private boolean hideInherited() {
		return true;
	}

	/**
	 * Content provider for tree.  
	 * Uses the internal state of hierarchy as tree of {@link Node} that store a reference to artifact.
	 * Builds the internal state every time when input is changed.
	 */
	private class HierarchyContentProvider implements ITreeContentProvider {

		private Node main;
		private Node root;

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (newInput == null) {
				main = null;
				root = null;
				return;
			}
			ViewType viewType = getCurrentViewType();
			IAbstractArtifact artifact = (IAbstractArtifact) newInput;

			main = new Node(artifact);
			root = main;

			Set<String> seen = new HashSet<String>();
			seen.add(artifact.getFullyQualifiedName());
			
			if (viewType == BOTH) {
				artifact = artifact.getExtendedArtifact();
				while (artifact != null) {
					if (!seen.add(artifact.getFullyQualifiedName())) {
						break;
					}
					Node node = root;
					root = new Node(artifact);
					node.parent = root;
					root.children.add(node);
					artifact = artifact.getExtendedArtifact();
				}
			}

			if (viewType == BOTH || viewType == DOWN) {
				fetchChildren(main, seen);
			}
			
			if (viewType == UP) {
				fetchParents(main, seen);
			}
		}

		private void fetchChildren(Node node, Set<String> seen) {
			for (IAbstractArtifact art : node.artifact
					.getImplementingArtifacts()) {
				addToChildren(node, art, seen, true);
			}
			for (IAbstractArtifact art : node.artifact.getExtendingArtifacts()) {
				addToChildren(node, art, seen, true);
			}
		}

		private void fetchParents(Node node, Set<String> seen) {
			for (IAbstractArtifact art : node.artifact
					.getImplementedArtifacts()) {
				addToChildren(node, art, seen, false);
			}
			
			IAbstractArtifact extendedArtifact = node.artifact.getExtendedArtifact();
			if (extendedArtifact != null) {
				addToChildren(node, extendedArtifact, seen, false);
			}
		}
		
		private void addToChildren(Node node, IAbstractArtifact art,
				Set<String> seen, boolean down) {
			String fqn = art.getFullyQualifiedName();
			if (!seen.add(fqn)) {
				return;
			}
			Node child = new Node(art);
			node.children.add(child);
			child.parent = node;
			if (down) {
				fetchChildren(child, seen);
			} else {
				fetchParents(child, seen);
			}
			seen.remove(fqn);
		}

		public Object[] getElements(Object inputElement) {
			if (root == null) {
				return EMPTY;
			} else {
				return new Object[] { root };
			}
		}

		public Object[] getChildren(Object parentElement) {
			Node node = (Node) parentElement;
			return node.children.toArray();
		}

		public Object getParent(Object element) {
			Node node = (Node) element;
			return node.parent;
		}

		public boolean hasChildren(Object element) {
			Node node = (Node) element;
			return !node.children.isEmpty();
		}

		public void dispose() {
		}
	}

	private static class HierarchyLabelProvider extends LabelProvider {

		private AbstractArtifactLabelProvider artifactLabelProvider = new AbstractArtifactLabelProvider();

		@Override
		public String getText(Object element) {
			Node node = (Node) element;
			return node.artifact.getName();
		}

		@Override
		public Image getImage(Object element) {
			Node node = (Node) element;
			return artifactLabelProvider.getImage(node.artifact);
		}
	}
	
	private static class SimpleNameArtifactLabelProvider extends
			AbstractArtifactLabelProvider {

		public static final SimpleNameArtifactLabelProvider INSTANCE = new SimpleNameArtifactLabelProvider();
		
		@Override
		public String getText(Object element) {
			IAbstractArtifact artifact = (IAbstractArtifact) element;
			return artifact.getName();
		}
	}

	private class MembersContentProvider implements IStructuredContentProvider {

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		public Object[] getElements(Object inputElement) {
			IAbstractArtifact artifact = (IAbstractArtifact) inputElement;

			List<IMember> members = new ArrayList<IMember>();
			members.addAll(artifact.getLiterals());
			if (!hideInherited()) {
				members.addAll(artifact.getInheritedLiterals());
			}
			members.addAll(artifact.getFields());
			if (!hideInherited()) {
				members.addAll(artifact.getInheritedFields());
			}
			members.addAll(artifact.getMethods());
			if (!hideInherited()) {
				members.addAll(artifact.getInheritedMethods());
			}
			return members.toArray();
		}

		public void dispose() {
		}
	}

	private static class MembersLabelProvider extends StyledCellLabelProvider {

		private static final Color TYPE_COLOR = new Color(null, 145, 130, 80);

		@Override
		public void update(ViewerCell cell) {
			IMember member = (IMember) cell.getElement();
			StyledString string = new StyledString(member.getName());
			if (member instanceof IMethod) {
				string.append("(");
				IMethod method = (IMethod) member;
				Iterator<IArgument> it = method.getArguments().iterator();
				if (it.hasNext()) {
					IArgument arg = it.next();
					string.append(arg.getName());
					while (it.hasNext()) {
						arg = it.next();
						string.append(", ");
						string.append(arg.getName());
					}
				}
				string.append(")");
			}
			IType type = member.getType();
			string.append(" : " + type.getName(), new ColorStyler(TYPE_COLOR));
			cell.setText(string.getString());
			cell.setStyleRanges(string.getStyleRanges());

			if (member instanceof IField) {
				cell.setImage(Images.get(Images.FIELD_ICON));
			} else if (member instanceof IMethod) {
				cell.setImage(Images.get(Images.METHOD_ICON));
			} else if (member instanceof ILiteral) {
				cell.setImage(Images.get(Images.LITERAL_ICON));
			}

			super.update(cell);
		}
	}

	static class Node {
		IAbstractArtifact artifact;
		LinkedHashSet<Node> children = new LinkedHashSet<Node>();
		Node parent;

		public Node(IAbstractArtifact artifact) {
			this.artifact = artifact;
		}
	}

	static enum ViewType {
		BOTH, UP, DOWN;
	}

	static class ColorStyler extends StyledString.Styler {

		private final Color color;

		public ColorStyler(Color color) {
			this.color = color;
		}
		
		@Override
		public void applyStyles(TextStyle textStyle) {
			textStyle.foreground = color;
		}
		
	}
	
	class ChangeHierarchyViewTypeAction extends Action {

		private final ViewType viewType;

		public ChangeHierarchyViewTypeAction(ViewType viewType) {
			super("", AS_RADIO_BUTTON);
			this.viewType = viewType;

			switch (viewType) {
			case BOTH:
				setImageDescriptor(Images.getDescriptor(Images.HIERARCHY));
				setToolTipText("Show the Type Hierarchy");
				setChecked(true);
				break;
			case UP:
				setImageDescriptor(Images.getDescriptor(Images.HIERARCHY_UP));
				setToolTipText("Show the Supertype Hierarchy");
				break;
			case DOWN:
				setImageDescriptor(Images.getDescriptor(Images.HIERARCHY_DOWN));
				setToolTipText("Show the Subtype Hierarchy");
				break;
			}
		}

		@Override
		public void run() {
			IAbstractArtifact input = getInput();
			if (input != null) {
				setInput(input);
			}
		}

		public ViewType getViewType() {
			return viewType;
		}
	}

	class HistoryAction extends Action implements IMenuCreator {

		public HistoryAction() {
			super("History", AS_DROP_DOWN_MENU);
			setImageDescriptor(Images.getDescriptor(Images.HISTORY_LIST));
			setToolTipText("Previous Type Hierarchies");
			setMenuCreator(this);
		}

		public void dispose() {
		}

		public Menu getMenu(Control parent) {
			Menu menu = new Menu(parent);
			if (history.isEmpty()) {
				MenuItem empty = new MenuItem(menu, SWT.NONE);
				empty.setText("empty");
				empty.setEnabled(false);
			} else {
				for (IAbstractArtifact artifact : history) {
					MenuItem item = new MenuItem(menu, SWT.RADIO);
					item.setText(artifact.getName());
					item.setData(artifact);
					listen(item);
					if (artifact.equals(getInput())) {
						item.setSelection(true);
					}
				}
				new MenuItem(menu, SWT.SEPARATOR);
				MenuItem clearAll = new MenuItem(menu, SWT.PUSH);
				clearAll.setText("Clear History");
				clearAll.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						reset();
					}
				});
			}
			return menu;
		}

		public Menu getMenu(Menu parent) {
			return null;
		}

		@Override
		public void run() {
			if (history.isEmpty()) {
				return;
			}
			ListDialog historyDialog = new ListDialog(getSite().getShell());
			historyDialog
					.setLabelProvider(new SimpleNameArtifactLabelProvider());
			historyDialog.setContentProvider(new ArrayContentProvider());
			historyDialog.setInput(history.toArray());
			historyDialog.setTitle("Hierarchy Search History");
			historyDialog
					.setMessage("Select the element to open in the type hierarchy");
			historyDialog.setInitialSelections(new Object[] { getInput() });
			if (historyDialog.open() == OK) {
				Object[] result = historyDialog.getResult();
				if (result.length > 0) {
					setInputInternal((IAbstractArtifact) result[0]);
				}
			}
		}
	}
	
	static interface NodeVisitor {
		
		boolean visit(Node node);
	}
}
