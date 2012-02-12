/*******************************************************************************
 * Copyright (c) 2011 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Valentin Erastov)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.convert;

import static org.eclipse.tigerstripe.workbench.convert.ConvertUtils.getPointAndSize;
import static org.eclipse.tigerstripe.workbench.convert.ConvertUtils.makeDeleteFromClassDiagramCommand;
import static org.eclipse.tigerstripe.workbench.model.ModelUtils.mapWritablePropertiesExclude;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.INodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.convert.SuggestConvertationDialog.Result;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.internal.core.util.CheckUtils;
import org.eclipse.tigerstripe.workbench.internal.core.util.Provider;
import org.eclipse.tigerstripe.workbench.internal.core.util.Tuple;
import org.eclipse.tigerstripe.workbench.model.ModelUtils;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.InstanceDiagramEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.UndoContextBindable;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.synchronization.DiagramSynchronizationManager;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.synchronization.ProjectDiagramsSynchronizer;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.CompositeOperation;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.CreateArtifactOperation;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.DeleteArtifactOperation;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.EmptyOperation;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeDiagramEditor;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeDiagramEditorPlugin;
import org.eclipse.tigerstripe.workbench.utils.RunnableWithResult;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class ConvertArtifactOperation extends AbstractOperation {

	protected static final String EXTENDED_ARTIFACT_ATTR = "extendedArtifact";
	protected static final String IMPLEMENTED_ARTIFACTS_ATTR = "implementedArtifacts";
	protected final IArtifactManagerSession session;
	protected IAbstractArtifact from;
	protected final String toType;
	protected boolean association;

	protected String mainFqn;
	protected Collection<String> toConvert;

	protected Set<String> parents;
	protected Set<String> children;

	protected boolean convertParents;
	protected boolean convertChildren;

	protected IUndoableOperation deleteOperation;
	protected IUndoableOperation createOperation;

	protected CompositeOperation deleteRelationsOperation;
	protected List<OpDesciptor> operations;

	protected Set<IRelationship> relationships;
	protected final Set<IEditorPart> contextParts;
	protected final Map<String, Provider<IAbstractArtifact>> providers = new HashMap<String, Provider<IAbstractArtifact>>();

	public ConvertArtifactOperation(IArtifactManagerSession session,
			IAbstractArtifact from, String toType, Set<IEditorPart> contextParts) {
		super("Convert");
		this.contextParts = contextParts;
		this.session = CheckUtils.notNull(session, "session");
		this.from = CheckUtils.notNull(from, "from");
		this.toType = CheckUtils.notNull(toType, "toType");

		// Set human label after checks.
		setLabel("Convert " + from.getName());
	}

	public boolean init(IProgressMonitor monitor) throws ExecutionException {
		try {
			association = from instanceof IRelationship;
			operations = new ArrayList<ConvertArtifactOperation.OpDesciptor>();

			mainFqn = from.getFullyQualifiedName();

			toConvert = new LinkedHashSet<String>();
			LinkedHashSet<IAbstractArtifact> toConvertAsModels = new LinkedHashSet<IAbstractArtifact>();

			toConvert.add(mainFqn);
			toConvertAsModels.add(from);

			Set<IAbstractArtifact> parentsAsModels = new HashSet<IAbstractArtifact>();
			Set<IAbstractArtifact> childrenAsModels = new HashSet<IAbstractArtifact>();

			ModelUtils.featchHierarhyUpAsModels(from, parentsAsModels);
			ModelUtils.featchHierarhyDownAsModels(from, childrenAsModels);
			parentsAsModels.remove(from);
			childrenAsModels.remove(from);
			
			if (from instanceof IAssociationClassArtifact) {
				removeEntities(parentsAsModels);
				removeEntities(childrenAsModels);
			}
			
			parents = new HashSet<String>(parentsAsModels.size());
			children = new HashSet<String>(childrenAsModels.size());

			for (IAbstractArtifact art : parentsAsModels) {
				parents.add(art.getFullyQualifiedName());
			}

			for (IAbstractArtifact art : childrenAsModels) {
				children.add(art.getFullyQualifiedName());
			}

			toConvert.addAll(parents);
			toConvert.addAll(children);

			toConvertAsModels.addAll(parentsAsModels);
			toConvertAsModels.addAll(childrenAsModels);

			relationships = getRelations();

			if (from instanceof IAssociationClassArtifact && toType.equals(IAssociationArtifact.class.getName())) {
				Collection<IMethod> methods = from.getMethods();
				Collection<IField> fields = from.getFields();
				if ((methods!=null && methods.size()>0) || (fields!=null && fields.size()>0)) {
					String message = "Converting to an Association will remove all methods and fields for the selected Association Class.  Are you sure you want to continue?";
					MessageDialog messageDialog = new MessageDialog(TigerstripeDiagramEditorPlugin.getInstance().getWorkbench().getDisplay().getActiveShell(), "Warning", null, message, MessageDialog.WARNING, new String[]{"No", "Yes"}, 1);
					int result = messageDialog.open();
					if (result==0 || result==SWT.DEFAULT)
						return false;
				}
			}
			
			Collection<DiagramEditor> partsToSave = getPartsToSave(contextParts);

			if (!parents.isEmpty() || !children.isEmpty()
					|| !relationships.isEmpty() || !partsToSave.isEmpty()) {
				Shell shell = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell();

				SuggestConvertationDialog sd = new SuggestConvertationDialog(
						shell, mainFqn, !parents.isEmpty(),
						!children.isEmpty(), !relationships.isEmpty(),
						partsToSave);
				if (sd.open() != Window.OK) {
					return false;
				}

				Result res = sd.getResult();
				this.convertParents = res.convertParents;
				this.convertChildren = res.convertChildren;
			}

			/*
			 * We must save all diagrams only for updating synchronization
			 * info(cached by fqn)
			 */
			savePartsGlobal(monitor, contextParts);

			Set<IEditorPart> openedParts = findOpened();
			openedParts.addAll(contextParts);

			List<IUndoableOperation> relationsOperations = new ArrayList<IUndoableOperation>();
			for (IRelationship r : relationships) {
				if (r instanceof IAbstractArtifact) {
					relationsOperations.add(new DeleteArtifactOperation(
							(IAbstractArtifact) r, session, true));
				}
			}

			List<IUndoableOperation> deleteOprations = new ArrayList<IUndoableOperation>();
			List<IUndoableOperation> createOprations = new ArrayList<IUndoableOperation>();
			HierarchyHelper hierarchyHelper = new HierarchyHelper(
					toConvertAsModels);
			for (IAbstractArtifact art : toConvertAsModels) {

				String fqn = art.getFullyQualifiedName();
				boolean needConvert = needConvert(fqn);
				deleteOprations.add(new DeleteArtifactOperation(art, session,
						!needConvert));

				if (needConvert) {

					Class<?> mapClass = art instanceof IAssociationArtifact ? IAssociationArtifact.class
							: IAbstractArtifact.class;

					Map<String, Object> savedProperties = mapWritablePropertiesExclude(
							mapClass, art, IMPLEMENTED_ARTIFACTS_ATTR,
							EXTENDED_ARTIFACT_ATTR);

					if (convertParents || (children.contains(fqn) && convertChildren)) {
						IAbstractArtifact extended = hierarchyHelper
								.getExtended(fqn);
						if (extended != null) {
							if (eligableInheratance(from, extended)) {
								savedProperties.put(
										EXTENDED_ARTIFACT_ATTR,
										makeProxy(toType, extended
												.getFullyQualifiedName()));
							}
						}

						List<IAbstractArtifact> implemented = hierarchyHelper
								.getImplemented(fqn);
						if (!implemented.isEmpty()) {
							List<IAbstractArtifact> impls = new ArrayList<IAbstractArtifact>();
							for (IAbstractArtifact impl : impls) {
								if (eligableInheratance(from, impl)) {
									impls.add(makeProxy(toType,
											impl.getFullyQualifiedName()));
								}
							}
							savedProperties.put(IMPLEMENTED_ARTIFACTS_ATTR,
									impls);
						}
					}

					pipelineProperties(art, savedProperties);

					CreateArtifactOperation createOperation = new CreateArtifactOperation(
							session, toType, savedProperties);
					createOprations.add(createOperation);
					providers.put(fqn, createOperation);
				}
			}
			pipelineCreateOperations(createOprations);
			pipelineDeleteOperations(deleteOprations);
			
			deleteOperation = new CompositeOperation("Delete Artifact",
					deleteOprations);
			createOperation = new CompositeOperation("Create Artifact",
					createOprations);

			deleteRelationsOperation = new CompositeOperation(
					"Delete Relation", relationsOperations);

			for (IEditorPart opened : openedParts) {
				if (opened instanceof DiagramEditor) {
					operations.add(makeDescriptor((DiagramEditor) opened));
				}
			}

			from = null;
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
			return false;
		}
		return true;
	}

	protected void pipelineDeleteOperations(
			List<IUndoableOperation> oprations) {
	}

	protected void pipelineCreateOperations(
			List<IUndoableOperation> oprations) {
	}

	protected void pipelineProperties(IAbstractArtifact art,
			Map<String, Object> props) {
	}

	private boolean eligableInheratance(IAbstractArtifact art,
			IAbstractArtifact extended) {
		if (art instanceof IAssociationClassArtifact) {
			return !(extended instanceof IManagedEntityArtifact);
		} else {
			return true;
		}
	}

	private void removeEntities(Set<IAbstractArtifact> artifacts) {
		Iterator<IAbstractArtifact> it = artifacts.iterator();
		while (it.hasNext()) {
			IAbstractArtifact artifact = it.next();
			if (artifact instanceof IManagedEntityArtifact) {
				it.remove();
			}
		}
	}

	private IAbstractArtifact makeProxy(String toType, String extendedFqn) {
		IAbstractArtifactInternal extended = (IAbstractArtifactInternal) session
				.makeArtifact(toType);
		extended.setFullyQualifiedName(extendedFqn);
		extended.setProxy(true);
		return extended;
	}

	private static void save(final IEditorPart part,
			final IProgressMonitor monitor) {
		part.doSave(monitor);
	}

	private OpDesciptor makeDescriptor(DiagramEditor diagramEditor) {

		OpDesciptor od = new OpDesciptor();
		od.part = diagramEditor;

		List<IUndoableOperation> partOperations = new ArrayList<IUndoableOperation>();
		for (IRelationship r : relationships) {
			if (r instanceof IAbstractArtifact) {
				partOperations.add(makeDeleteRelationFromDiagram(diagramEditor,
						r));
			}
		}

		DiagramEditPart diagramEditPart = diagramEditor.getDiagramEditPart();
		if (diagramEditor instanceof TigerstripeDiagramEditor) {
			List<Provider<IAbstractArtifact>> dproviders = new ArrayList<Provider<IAbstractArtifact>>();
			List<Tuple<Point, Dimension>> sizes = new ArrayList<Tuple<Point, Dimension>>();

			for (String fqn : toConvert) {
				partOperations.add(makeDeleteFromClassDiagramCommand(
						diagramEditPart, fqn));

				if (needConvert(fqn)) {

					Tuple<Point, Dimension> ps = getPointAndSize(
							diagramEditPart, fqn);
					Provider<IAbstractArtifact> provider = providers.get(fqn);
					if (provider == null) {
						throw new IllegalStateException();
					}
					dproviders.add(provider);
					sizes.add(ps);
				}
			}
			DefferedDropOperation dropOperation = new DefferedDropOperation(
					diagramEditPart, dproviders, sizes);
			partOperations.add(dropOperation);

		} else if (diagramEditor instanceof InstanceDiagramEditor) {

			Set<String> toRepaint = new HashSet<String>();

			for (String fqn : toConvert) {
				if (!needConvert(fqn) || association) {
					partOperations.add(ConvertUtils
							.makeDeleteFromInstanceDiagramCommand(
									diagramEditPart, fqn));
				} else {
					toRepaint.add(fqn);
				}
			}
			partOperations.add(new RefreshOperation("Repaint",
					(InstanceDiagramEditor) diagramEditor, toRepaint));
		}

		pipelinePartOperations(partOperations, diagramEditor);
		
		od.operation = new CompositeOperation("Convert Part Operation",
				partOperations);
		return od;
	}

	protected void pipelinePartOperations(
			List<IUndoableOperation> partOperations, DiagramEditor diagramEditor) {
	}

	private boolean needConvert(String fqn) {
		if (fqn.equals(mainFqn)) {
			return true;
		}
		if (children.contains(fqn) && convertChildren) {
			return true;
		}
		if (parents.contains(fqn) && convertParents) {
			return true;
		}
		return false;
	}

	public void removeEditor(DiagramEditor diagramEditor) {
		Iterator<OpDesciptor> it = operations.iterator();
		while (it.hasNext()) {
			OpDesciptor od = it.next();
			if (diagramEditor.equals(od.part)) {
				it.remove();
			}
		}
		contextParts.remove(diagramEditor);
	}

	private Set<IEditorPart> findOpened() {
		DiagramSynchronizationManager synchronizationManager = DiagramSynchronizationManager
				.getInstance();

		Collection<ProjectDiagramsSynchronizer> diagramsSynchronizers = synchronizationManager
				.getDiagramsSynchronizers();

		Set<IEditorPart> openedParts = new HashSet<IEditorPart>();
		for (ProjectDiagramsSynchronizer ds : diagramsSynchronizers) {
			for (String fqn : toConvert) {
				openedParts.addAll(ds.getOpenedDiagrams(fqn));
			}
		}
		return openedParts;
	}

	protected IUndoableOperation makeDeleteRelationFromDiagram(
			DiagramEditor editor, IRelationship r) {
		return makeDeleteFromDiagram(editor, r.getFullyQualifiedName());
	}

	protected IUndoableOperation makeDeleteFromDiagram(DiagramEditor editor,
			String fqn) {

		if (editor instanceof TigerstripeDiagramEditor) {
			return ConvertUtils.makeDeleteFromClassDiagramCommand(
					editor.getDiagramEditPart(), fqn);
		} else if (editor instanceof InstanceDiagramEditor) {
			return ConvertUtils.makeDeleteFromInstanceDiagramCommand(
					editor.getDiagramEditPart(), fqn);
		} else {
			return EmptyOperation.INSTANCE;
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private IUndoableOperation makeDestroyCommand(DiagramEditor editor,
			List<? extends EObject> semantic) {
		if (semantic.isEmpty()) {
			return EmptyOperation.INSTANCE;
		}

		List<IUndoableOperation> commands = new ArrayList<IUndoableOperation>();
		for (EObject e : semantic) {

			List<INodeEditPart> partsForElement = editor
					.getDiagramGraphicalViewer().findEditPartsForElement(
							EMFCoreUtil.getProxyID(e), INodeEditPart.class);

			for (INodeEditPart part : partsForElement) {

				View view = (View) part.getModel();
				commands.add(new DeleteCommand(view));
			}
		}
		switch (commands.size()) {
		case 0:
			return EmptyOperation.INSTANCE;
		case 1:
			return commands.get(0);
		default:
			return new CompositeOperation("Delete Relations", commands);
		}
	}

	private Set<IRelationship> getRelations() throws TigerstripeException {

		Set<IRelationship> relationships = new HashSet<IRelationship>();

		for (String fqn : toConvert) {
			List<IRelationship> originatingRelationshipForFQN = session
					.getOriginatingRelationshipForFQN(fqn, true);

			List<IRelationship> terminatingRelationshipForFQN = session
					.getTerminatingRelationshipForFQN(fqn, true);

			relationships.addAll(originatingRelationshipForFQN);
			relationships.addAll(terminatingRelationshipForFQN);
		}
		return relationships;
	}


	@Override
	public IStatus execute(final IProgressMonitor monitor, final IAdaptable info)
			throws ExecutionException {

		IStatus status = WithoutSynchRunner
				.run(new RunnableWithResult<IStatus, ExecutionException>() {

					public IStatus run() throws ExecutionException {
						doExecute(monitor, info);
						return Status.OK_STATUS;
					}
				});
		savePartsAsync(monitor);
		bindUndoContext();
		return status;
	}

	protected void doExecute(final IProgressMonitor monitor,
			final IAdaptable info) throws ExecutionException {
		deleteRelationsOperation.execute(monitor, info);
		deleteOperation.execute(monitor, info);
		createOperation.execute(monitor, info);
		for (OpDesciptor o : operations) {
			o.operation.execute(monitor, info);
		}
		resolveHierarchyProxies();
	}
	
	@Override
	public IStatus redo(final IProgressMonitor monitor, final IAdaptable info)
			throws ExecutionException {
		savePartsGlobal(monitor);
		IStatus status = WithoutSynchRunner
				.run(new RunnableWithResult<IStatus, ExecutionException>() {

					public IStatus run() throws ExecutionException {
						doRedo(monitor, info);
						return Status.OK_STATUS;
					}
				});
		savePartsAsync(monitor);
		return status;
	}

	protected void doRedo(final IProgressMonitor monitor,
			final IAdaptable info) throws ExecutionException {
		removeClosed();
		deleteRelationsOperation.redo(monitor, info);
		deleteOperation.redo(monitor, info);
		createOperation.redo(monitor, info);
		for (OpDesciptor o : operations) {
			o.operation.redo(monitor, info);
		}
		Collection<OpDesciptor> newOpened = addOpened();
		for (OpDesciptor o : newOpened) {
			o.operation.execute(monitor, info);
		}
	}
	
	@Override
	public IStatus undo(final IProgressMonitor monitor, final IAdaptable info)
			throws ExecutionException {

		savePartsGlobal(monitor);
		IStatus status = WithoutSynchRunner
				.run(new RunnableWithResult<IStatus, ExecutionException>() {

					public IStatus run() throws ExecutionException {
						doUndo(monitor, info);
						return Status.OK_STATUS;
					}
				});
		savePartsAsync(monitor);
		return status;
	}

	protected void doUndo(final IProgressMonitor monitor,
			final IAdaptable info) throws ExecutionException {
		removeClosed();
		createOperation.undo(monitor, info);
		deleteOperation.undo(monitor, info);
		deleteRelationsOperation.undo(monitor, info);

		int size = operations.size();
		for (int i = size - 1; i > -1; --i) {
			operations.get(i).operation.undo(monitor, info);
		}
		Collection<DiagramEditor> newOpened = findNewOpened();
		for (DiagramEditor de : newOpened) {
			validateAndCorrect(de, monitor);
		}
	}
	
	private void savePartsAsync(final IProgressMonitor monitor) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			public void run() {
				saveParts(monitor);
			}
		});
	}

	private void resolveHierarchyProxies() {
		Collection<Provider<IAbstractArtifact>> values = providers.values();
		for (Provider<IAbstractArtifact> provider : values) {
			provider.get().getExtendedArtifact();
			provider.get().getImplementedArtifacts();
		}
	}

	private void saveParts(IProgressMonitor monitor) {
		for (OpDesciptor od : operations) {
			if (contextParts.contains(od.part)) {
				continue;
			}
			save(od.part, monitor);
		}
	}

	private void savePartsGlobal(IProgressMonitor monitor) {
		savePartsGlobal(monitor, contextParts);
	}

	private static void savePartsGlobal(IProgressMonitor monitor,
			Set<IEditorPart> contextParts) {
		for (DiagramEditor de : getPartsToSave(contextParts)) {
			save(de, monitor);
		}
	}

	private static Collection<DiagramEditor> getPartsToSave(
			Set<IEditorPart> contextParts) {
		Collection<DiagramEditor> allParts = getAllParts();
		allParts.removeAll(contextParts);
		Iterator<DiagramEditor> it = allParts.iterator();
		while (it.hasNext()) {
			DiagramEditor de = it.next();
			if (!de.isDirty()) {
				it.remove();
			}
		}
		return allParts;
	}

	private static Collection<DiagramEditor> getAllParts() {
		Collection<DiagramEditor> result = new ArrayList<DiagramEditor>();
		IWorkbenchWindow windows[] = PlatformUI.getWorkbench()
				.getWorkbenchWindows();
		for (int i = 0; i < windows.length; i++) {
			IWorkbenchPage pages[] = windows[i].getPages();
			for (int j = 0; j < pages.length; j++) {
				IEditorReference[] refs = pages[j].getEditorReferences();
				for (IEditorReference ref : refs) {
					IEditorPart part = ref.getEditor(false);
					if (part instanceof DiagramEditor) {
						result.add((DiagramEditor) part);
					}
				}
			}
		}
		return result;
	}

	private void validateAndCorrect(DiagramEditor de, IProgressMonitor monitor) {
		if (de instanceof TigerstripeDiagramEditor) {
			new ClassDiagramCorrector().correct(de.getDiagramEditPart(),
					session.getArtifactManager(), monitor);
		} else if (de instanceof InstanceDiagramEditor) {
			new InstanceDiagramCorrector().correct(de.getDiagramEditPart(),
					session.getArtifactManager(), monitor);
		}
	}

	private void removeClosed() {
		Set<IEditorPart> opened = findOpened();
		Iterator<OpDesciptor> it = operations.iterator();
		while (it.hasNext()) {
			OpDesciptor od = it.next();
			if (!opened.contains(od.part)) {
				it.remove();
			}
		}
		contextParts.retainAll(opened);
	}

	private Collection<OpDesciptor> addOpened() {
		List<DiagramEditor> newOpened = findNewOpened();
		List<OpDesciptor> toAdd = new ArrayList<OpDesciptor>(newOpened.size());
		for (DiagramEditor de : newOpened) {
			toAdd.add(makeDescriptor(de));
		}
		operations.addAll(toAdd);
		return toAdd;
	}

	private List<DiagramEditor> findNewOpened() {
		Set<IEditorPart> opened = findOpened();

		Set<IEditorPart> currentState = new HashSet<IEditorPart>();
		for (OpDesciptor od : operations) {
			currentState.add(od.part);
		}

		List<DiagramEditor> newOpened = new ArrayList<DiagramEditor>();
		for (IEditorPart part : opened) {
			if (part instanceof DiagramEditor) {
				DiagramEditor editor = (DiagramEditor) part;
				if (!currentState.contains(editor)) {
					newOpened.add(editor);
				}
			}
		}
		return newOpened;
	}

	private void bindUndoContext() {
		for (OpDesciptor od : operations) {
			((UndoContextBindable) od.part).bindUndoContext(this);
		}
	}

	private static class HierarchyHelper {

		private final Map<String, IAbstractArtifact> extended = new HashMap<String, IAbstractArtifact>();
		private final Map<String, List<IAbstractArtifact>> implemented = new HashMap<String, List<IAbstractArtifact>>();

		public HierarchyHelper(Collection<IAbstractArtifact> artifacts) {
			for (IAbstractArtifact art : artifacts) {
				String fqn = art.getFullyQualifiedName();
				List<IAbstractArtifact> impls = getImplementedFromMap(fqn);
				for (IAbstractArtifact impl : art.getImplementedArtifacts()) {
					impls.add(impl);
				}
				IAbstractArtifact ext = art.getExtendedArtifact();
				if (ext != null) {
					extended.put(fqn, ext);
				}
			}
		}

		public List<IAbstractArtifact> getImplemented(String fqn) {
			List<IAbstractArtifact> list = implemented.get(fqn);
			if (list == null) {
				return Collections.emptyList();
			} else {
				return list;
			}
		}

		public IAbstractArtifact getExtended(String fqn) {
			return extended.get(fqn);
		}

		private List<IAbstractArtifact> getImplementedFromMap(String fqn) {
			List<IAbstractArtifact> impls = implemented.get(fqn);
			if (impls == null) {
				impls = new ArrayList<IAbstractArtifact>();
				implemented.put(fqn, impls);
			}
			return impls;
		}
	}

	private static class OpDesciptor {
		public IUndoableOperation operation;
		public DiagramEditor part;
	}
}
