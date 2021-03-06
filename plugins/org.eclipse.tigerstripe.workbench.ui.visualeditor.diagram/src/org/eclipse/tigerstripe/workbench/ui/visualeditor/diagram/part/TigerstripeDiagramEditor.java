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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part;

import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.LifecycleHandlerProvider.getHandlers;
import static org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.ClassDiagramLogicalNode.MODEL_EXT;
import static org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.Constants.DOMAIN_ID;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.AutomaticRouter;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.document.StorageDiagramDocumentProvider;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.ConnectionLayerEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.FanRouter;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.ObliqueRouter;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.viewsupport.IViewPartInputProvider;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.AbstractDiagramEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.Lifecycle;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.GMFEditorHandler;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassConnectionEditPart.AssocClassLinkPolylineConnectionEx;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.TigerstripeEditPartFactory;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ide.IGotoMarker;

/**
 * @generated
 */
public class TigerstripeDiagramEditor extends AbstractDiagramEditor implements
		IViewPartInputProvider, IGotoMarker {

	/**
	 * @generated
	 */
	public static final String ID = "org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeDiagramEditorID"; //$NON-NLS-1$

	/**
	 * @generated
	 */
	public TigerstripeDiagramEditor() {
		super(true);
	}

	/**
	 * @generated NOT
	 */
	@Override
	protected String getEditingDomainID() {
		return DOMAIN_ID;
	}

	/**
	 * @generated
	 */
	@Override
	protected TransactionalEditingDomain createEditingDomain() {
		TransactionalEditingDomain domain = super.createEditingDomain();
		domain.setID(getEditingDomainID());
		return domain;
	}

	/**
	 * @generated
	 */
	@Override
	protected void setDocumentProvider(IEditorInput input) {
		if (input.getAdapter(IFile.class) != null) {
			setDocumentProvider(new TigerstripeDocumentProvider());
		} else {
			setDocumentProvider(new StorageDiagramDocumentProvider());
		}
	}

	/**
	 * @generated
	 */
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		DiagramRootEditPart root = (DiagramRootEditPart) getDiagramGraphicalViewer()
				.getRootEditPart();
		LayeredPane printableLayers = (LayeredPane) root
				.getLayer(LayerConstants.PRINTABLE_LAYERS);
		FreeformLayer extLabelsLayer = new FreeformLayer();
		extLabelsLayer.setLayoutManager(new DelegatingLayout());
		printableLayers.addLayerAfter(extLabelsLayer,
				TigerstripeEditPartFactory.EXTERNAL_NODE_LABELS_LAYER,
				LayerConstants.PRIMARY_LAYER);
		LayeredPane scalableLayers = (LayeredPane) root
				.getLayer(LayerConstants.SCALABLE_LAYERS);
		FreeformLayer scaledFeedbackLayer = new FreeformLayer();
		scaledFeedbackLayer.setEnabled(false);
		scalableLayers.addLayerAfter(scaledFeedbackLayer,
				LayerConstants.SCALED_FEEDBACK_LAYER,
				DiagramRootEditPart.DECORATION_UNPRINTABLE_LAYER);
		printableLayers.removeLayer(LayerConstants.CONNECTION_LAYER);
		printableLayers.addLayerBefore(new MyConnectionLayerEx(),
				LayerConstants.CONNECTION_LAYER,
				printableLayers.getLayer(LayerConstants.PRIMARY_LAYER));
	}

	public class MyConnectionLayerEx extends ConnectionLayerEx {

		private ConnectionRouter obliqueRouter = null;

		@Override
		public ConnectionRouter getObliqueRouter() {
			if (obliqueRouter == null) {
				AutomaticRouter router = new FanRouter();
				router.setNextRouter(new MyObliqueRouter());
				obliqueRouter = router;
			}

			return obliqueRouter;
		}

	}

	public class MyObliqueRouter extends ObliqueRouter {

		@Override
		protected boolean checkShapesIntersect(Connection conn,
				PointList newLine) {
			if (conn instanceof AssocClassLinkPolylineConnectionEx)
				return false;
			else
				return super.checkShapesIntersect(conn, newLine);
		}
	}

	private GMFEditorHandler handler;

	@Override
	protected void initializeGraphicalViewer() {
		//TODO register GMFEditorHandler as lifecycle handler through wxtension point
		handler = new GMFEditorHandler(this);
		super.initializeGraphicalViewer();
		handler.initialize();
		initializeInternal();
	}

	private void initializeInternal() {
		for (Lifecycle handler : getHandlers()) {
			handler.init(getDiagramEditPart(), this, getUndoContext());	
		}
	}

	@Override
	protected void initializeGraphicalViewerContents() {
		if (getDiagram() != null
				&& getDiagram().getElement() instanceof Map
				&& ((Map) getDiagram().getElement())
						.getCorrespondingITigerstripeProject() == null) {
			handler.initializeInMap();
		}
		super.initializeGraphicalViewerContents();
	}

	@Override
	public void dispose() {
		for (Lifecycle handler : getHandlers()) {
			handler.dispose(getDiagramEditPart(), this, getUndoContext());	
		}
		if (handler != null)
			handler.dispose();
		super.dispose();
	}

	public Object getViewPartInput() {
		DiagramGraphicalViewer viewer = (DiagramGraphicalViewer) this
				.getDiagramGraphicalViewer();
		DiagramEditDomain domain = (DiagramEditDomain) viewer.getEditDomain();

		IDiagramWorkbenchPart pa = domain.getDiagramEditorPart();
		Diagram diag = pa.getDiagram();
		Map map = (Map) diag.getElement();
		String pack = map.getBasePackage();
		ITigerstripeModelProject proj = map
				.getCorrespondingITigerstripeProject();

		IProject project = (IProject) proj.getAdapter(IProject.class);
		if (project == null) {
			return null;
		}

		IJavaProject p = (IJavaProject) proj.getAdapter(IJavaProject.class);
		IPath path = new Path(pack.replaceAll("\\.", "/"));
		try {
			return p.findElement(path);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	protected IPath getModelPath(IPath diagramPath) {
		return diagramPath.removeFileExtension().addFileExtension(MODEL_EXT);
	}
}
