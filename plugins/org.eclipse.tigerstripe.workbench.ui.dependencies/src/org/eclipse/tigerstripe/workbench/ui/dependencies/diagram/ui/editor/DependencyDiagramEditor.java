/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.editor;

import java.util.EventObject;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencyDiagramHandler;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencySubject;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IExternalContext;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.parts.DependenciesEditPartFactory;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.parts.SubjectEditPart;
import org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.ModelsFactory;
import org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.Registry;
import org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.SubjectStyleService;
import org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.Utils;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.IShowInSource;
import org.eclipse.ui.part.ShowInContext;

public abstract class DependencyDiagramEditor extends GraphicalEditor implements
		IShowInSource {

	private Registry registry;
	private IDependencyDiagramHandler handler;
	private boolean viewStateDirty;

	public abstract IExternalContext getExternalContext();

	/** Create a new ShapesEditor instance. This is called by the Workspace. */
	public DependencyDiagramEditor() {
		setEditDomain(new DefaultEditDomain(this));
		handler = new DependencyDiagramHandler(this);

	}

	/**
	 * Configure the graphical viewer before it receives contents.
	 * <p>
	 * This is the place to choose an appropriate RootEditPart and
	 * EditPartFactory for your editor. The RootEditPart determines the behavior
	 * of the editor's "work-area". For example, GEF includes zoomable and
	 * scrollable root edit parts. The EditPartFactory maps model elements to
	 * edit parts (controllers).
	 * </p>
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();

		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new DependenciesEditPartFactory());
		viewer.setRootEditPart(new ScalableFreeformRootEditPart());
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));

		// configure the context menu provider
		ContextMenuProvider cmProvider = new DependencyContextMenuProvider(
				getSite(), viewer, getActionRegistry());
		viewer.setContextMenu(cmProvider);
		getSite().registerContextMenu(cmProvider, viewer);
	}

	@Override
	public void commandStackChanged(EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		Diagram diagram = registry.getDiagramToSave();
		if (diagram == null) {
			return;
		}
		getExternalContext().save(diagram);
		viewStateDirty = false;
		getCommandStack().markSaveLocation();
	}

	@Override
	protected void initializeGraphicalViewer() {
		updateViewer();
	}

	public void updateViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setContents(getModel()); // set the contents of this editor

		if (Utils.ensureLayoutDown(viewer, false)) {
			viewStateDirty = false;
			getCommandStack().markSaveLocation();
		}

		// Layer currentLayer = getDiagram().getCurrentLayer();
		// if (!currentLayer.isWasLayouting()) {
		// LayoutUtils.layout(viewer.getRootEditPart().getContents(), false);
		// currentLayer.setWasLayouting(true);
		// doSave(new NullProgressMonitor());
		// }
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public boolean isDirty() {
		if (super.isDirty()) {
			return true;
		}
		if (viewStateDirty) {
			return true;
		}
		return false;
	}

	public ShowInContext getShowInContext() {
		List<?> parts = getGraphicalViewer().getSelectedEditParts();
		IDependencySubject depSubj = null;
		if (parts != null && !parts.isEmpty()) {
			Object obj = parts.get(0);
			if (obj instanceof SubjectEditPart) {
				depSubj = Utils.findExternalModel(
						((SubjectEditPart) obj).getSubject(),
						getGraphicalViewer());
			}
		}
		if (depSubj == null) {
			return null;
		}
		IResource res = (IResource) depSubj.getAdapter(IResource.class);
		if (res == null) {
			return null;
		}
		return new ShowInContext(null, new StructuredSelection(res));
	}

	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		IExternalContext context = getExternalContext();
		Diagram loadedDiagram = loadOldDiagram();
		registry = new Registry(context.getRootExternalModel(), loadedDiagram);
		registry.addModelChangeListener(new Registry.IModelChangeListener() {

			public void modelChanged() {
				viewStateDirty = true;
				firePropertyChange(IEditorPart.PROP_DIRTY);
			}
		});
		setPartName(context.getName());
	}

	public IDependencyDiagramHandler getHandler() {
		return handler;
	}

	private Diagram loadOldDiagram() {
		Diagram loaded = getExternalContext().load(Diagram.class);
		return loaded == null ? ModelsFactory.INSTANCE.createDiagram() : loaded;
	}

	@Override
	protected void setGraphicalViewer(GraphicalViewer viewer) {
		viewer.setProperty(Registry.class.getName(), registry);
		viewer.setProperty(SubjectStyleService.class.getName(),
				new SubjectStyleService(registry));
		super.setGraphicalViewer(viewer);
	}

	@Override
	protected GraphicalViewer getGraphicalViewer() {
		return super.getGraphicalViewer();
	}

	Diagram getModel() {
		return getDiagram();
	}

	public Diagram getDiagram() {
		return registry.getDiagram();
	}

	public Registry getRegistry() {
		return registry;
	}
}
