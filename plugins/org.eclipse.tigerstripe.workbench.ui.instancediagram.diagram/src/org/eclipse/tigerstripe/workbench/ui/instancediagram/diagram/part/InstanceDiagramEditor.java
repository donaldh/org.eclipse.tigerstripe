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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part;

import static org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.InstanceDiagramLogicalNode.MODEL_EXT;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.document.StorageDiagramDocumentProvider;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.GMFInstanceDiagramEditorHandler;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.InstanceEditPartFactory;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.AbstractDiagramEditor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ide.IGotoMarker;

/**
 * @generated
 */
public class InstanceDiagramEditor extends AbstractDiagramEditor implements
		IGotoMarker {

	/**
	 * @generated
	 */
	public static final String ID = "org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.InstanceDiagramEditorID"; //$NON-NLS-1$

	/**
	 * @generated NOT
	 */
	public InstanceDiagramEditor() {
		super(false);
	}

	/**
	 * @generated
	 */
	@Override
	protected String getEditingDomainID() {
		return "org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.EditingDomain"; //$NON-NLS-1$
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
			setDocumentProvider(new InstanceDocumentProvider());
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
				InstanceEditPartFactory.EXTERNAL_NODE_LABELS_LAYER,
				LayerConstants.PRIMARY_LAYER);
		LayeredPane scalableLayers = (LayeredPane) root
				.getLayer(LayerConstants.SCALABLE_LAYERS);
		FreeformLayer scaledFeedbackLayer = new FreeformLayer();
		scaledFeedbackLayer.setEnabled(false);
		scalableLayers.addLayerAfter(scaledFeedbackLayer,
				LayerConstants.SCALED_FEEDBACK_LAYER,
				DiagramRootEditPart.DECORATION_UNPRINTABLE_LAYER);
	}

	private GMFInstanceDiagramEditorHandler handler;

	@Override
	protected void initializeGraphicalViewer() {
		handler = new GMFInstanceDiagramEditorHandler(this);
		super.initializeGraphicalViewer();
		handler.initialize();
	}

	@Override
	protected void initializeGraphicalViewerContents() {
		if (getDiagram() != null
				&& getDiagram().getElement() instanceof InstanceMap
				&& ((InstanceMap) getDiagram().getElement())
						.getCorrespondingITigerstripeProject() == null) {
			handler.initializeInMap();
		}
		super.initializeGraphicalViewerContents();
	}

	@Override
	public void dispose() {
		if (handler != null)
			handler.dispose();
		super.dispose();
	}

	@Override
	protected IPath getModelPath(IPath diagramPath) {
		return diagramPath.removeFileExtension().addFileExtension(MODEL_EXT);
	}
}
