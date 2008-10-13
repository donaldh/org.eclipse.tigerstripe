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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
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
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.document.FileEditorInputProxy;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.document.StorageDiagramDocumentProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.editor.FileDiagramEditor;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.ConnectionLayerEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.FanRouter;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.ObliqueRouter;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.viewsupport.IViewPartInputProvider;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.GMFEditorHandler;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MapEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.TigerstripeEditPartFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassConnectionEditPart.AssocClassLinkPolylineConnectionEx;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ide.IGotoMarker;

/**
 * @generated
 */
public class TigerstripeDiagramEditor extends FileDiagramEditor implements
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
	 * @generated
	 */
	@Override
	protected String getEditingDomainID() {
		return "org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.EditingDomain"; //$NON-NLS-1$
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
				LayerConstants.CONNECTION_LAYER, printableLayers
						.getLayer(LayerConstants.PRIMARY_LAYER));
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
		handler = new GMFEditorHandler(this);
		super.initializeGraphicalViewer();
		handler.initialize();
	}

	@Override
	public void dispose() {
		if (handler != null)
			handler.dispose();
		super.dispose();
	}

	
	public Object getViewPartInput() {
		DiagramGraphicalViewer viewer = (DiagramGraphicalViewer) this.getDiagramGraphicalViewer();
		DiagramEditDomain domain = (DiagramEditDomain) viewer.getEditDomain();
		
		domain.getEditorPart();
		IResource res = (IResource) domain.getEditorPart().getEditorInput()
			.getAdapter(IResource.class);
		
		//This isn't actually the right way to do this..
		IResource parent = res.getParent();

		return parent.getAdapter(IJavaElement.class);
	}

	
}
