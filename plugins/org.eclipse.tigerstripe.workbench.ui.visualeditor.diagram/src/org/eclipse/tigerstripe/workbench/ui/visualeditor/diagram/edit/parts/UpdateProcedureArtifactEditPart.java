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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.TigerstripeShapeNodeEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies.OpenInEditorEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies.UpdateProcedureArtifactCanonicalEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies.UpdateProcedureArtifactGraphicalNodeEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies.UpdateProcedureArtifactItemSemanticEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.utils.ArtifactPropertyChangeHandler;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeVisualIDRegistry;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers.TigerstripeElementTypes;

/**
 * @generated NOT
 */
public class UpdateProcedureArtifactEditPart extends
		AdaptableTigerstripeShapeNodeEditPart implements
		ClassDiagramShapeNodeEditPart, TigerstripeEditableEntityEditPart {

	private ArtifactPropertyChangeHandler artifactPropertyChangeHandler;

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 1007;

	/**
	 * @generated
	 */
	protected IFigure contentPane;

	/**
	 * @generated
	 */
	protected IFigure primaryShape;

	/**
	 * @generated
	 */
	public UpdateProcedureArtifactEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	@Override
	protected void createDefaultEditPolicies() {
		installEditPolicy(EditPolicyRoles.CREATION_ROLE,
				new CreationEditPolicy() {

					@Override
					public Command getCommand(Request request) {
						if (understandsRequest(request)) {
							if (request instanceof CreateViewAndElementRequest) {
								CreateElementRequestAdapter adapter = ((CreateViewAndElementRequest) request)
										.getViewAndElementDescriptor()
										.getCreateElementRequestAdapter();
								IElementType type = (IElementType) adapter
										.getAdapter(IElementType.class);
								if (type == TigerstripeElementTypes.Attribute_2009) {
									EditPart compartmentEditPart = getChildBySemanticHint(TigerstripeVisualIDRegistry
											.getType(UpdateProcedureArtifactAttributeCompartmentEditPart.VISUAL_ID));
									return compartmentEditPart == null ? null
											: compartmentEditPart
													.getCommand(request);
								}
							}
							return super.getCommand(request);
						}
						return null;
					}
				});
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new UpdateProcedureArtifactItemSemanticEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new UpdateProcedureArtifactGraphicalNodeEditPolicy());
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
				new UpdateProcedureArtifactCanonicalEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
		installEditPolicy(EditPolicyRoles.OPEN_ROLE,
				new OpenInEditorEditPolicy());
	}

	/**
	 * @generated
	 */
	protected LayoutEditPolicy createLayoutEditPolicy() {
		LayoutEditPolicy lep = new LayoutEditPolicy() {

			@Override
			protected EditPolicy createChildEditPolicy(EditPart child) {
				EditPolicy result = child
						.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
				if (result == null) {
					result = new NonResizableEditPolicy();
				}
				return result;
			}

			@Override
			protected Command getMoveChildrenCommand(Request request) {
				return null;
			}

			@Override
			protected Command getCreateCommand(CreateRequest request) {
				return null;
			}
		};
		return lep;
	}

	/**
	 * @generated
	 */
	protected IFigure createNodeShape() {
		UpdateProcedureArtifactFigure figure = new UpdateProcedureArtifactFigure();
		return primaryShape = figure;
	}

	/**
	 * @generated
	 */
	public UpdateProcedureArtifactFigure getPrimaryShape() {
		return (UpdateProcedureArtifactFigure) primaryShape;
	}

	/**
	 * @generated
	 */
	protected boolean addFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof UpdateProcedureArtifactStereotypesEditPart) {
			((UpdateProcedureArtifactStereotypesEditPart) childEditPart)
					.setLabel(getPrimaryShape()
							.getFigureUpdateProcedureStereotypesFigure());
			return true;
		}
		if (childEditPart instanceof UpdateProcedureArtifactNamePackageEditPart) {
			((UpdateProcedureArtifactNamePackageEditPart) childEditPart)
					.setLabel(getPrimaryShape()
							.getFigureUpdateProcedureNameFigure());
			return true;
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected boolean removeFixedChild(EditPart childEditPart) {
		return false;
	}

	/**
	 * @generated
	 */
	protected NodeFigure createNodePlate() {
		return new DefaultSizeNodeFigure(getMapMode().DPtoLP(40), getMapMode()
				.DPtoLP(40));
	}

	/**
	 * Creates figure for this edit part.
	 * 
	 * Body of this method does not depend on settings in generation model so
	 * you may safely remove <i>generated</i> tag and modify it.
	 * 
	 * @generated
	 */
	@Override
	protected NodeFigure createNodeFigure() {
		NodeFigure figure = createNodePlate();
		figure.setLayoutManager(new StackLayout());
		IFigure shape = createNodeShape();
		figure.add(shape);
		contentPane = setupContentPane(shape);
		return figure;
	}

	/**
	 * Default implementation treats passed figure as content pane. Respects
	 * layout one may have set for generated figure.
	 * 
	 * @param nodeShape
	 *            instance of generated figure class
	 * @generated
	 */
	protected IFigure setupContentPane(IFigure nodeShape) {
		if (nodeShape.getLayoutManager() == null) {
			ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
			layout.setSpacing(getMapMode().DPtoLP(5));
			nodeShape.setLayoutManager(layout);
		}
		return nodeShape; // use nodeShape itself as contentPane
	}

	/**
	 * @generated
	 */
	@Override
	public IFigure getContentPane() {
		if (contentPane != null)
			return contentPane;
		return super.getContentPane();
	}

	/**
	 * @generated NOT
	 */
	@Override
	public EditPart getPrimaryChildEditPart() {
		return getChildBySemanticHint(TigerstripeVisualIDRegistry
				.getType(UpdateProcedureArtifactNamePackageEditPart.VISUAL_ID));
	}

	/**
	 * @generated
	 */
	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		if (addFixedChild(childEditPart))
			return;
		super.addChildVisual(childEditPart, -1);
	}

	/**
	 * @generated
	 */
	@Override
	protected void removeChildVisual(EditPart childEditPart) {
		if (removeFixedChild(childEditPart))
			return;
		super.removeChildVisual(childEditPart);
	}

	/**
	 * @generated NOT
	 */
	public class UpdateProcedureArtifactFigure extends
			org.eclipse.draw2d.RectangleFigure {

		/**
		 * @generated NOT
		 */
		public UpdateProcedureArtifactFigure() {
			createContents();
		}

		/**
		 * @generated NOT
		 */
		private void createContents() {
			org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel fig_0 = new org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel();
			fig_0.setText("<<...>>");

			setFigureUpdateProcedureStereotypesFigure(fig_0);

			Object layData0 = null;

			this.add(fig_0, layData0);
			WrapLabel fig_1 = new WrapLabel();
			fig_1.setText("<...>");

			setFigureUpdateProcedureNameFigure(fig_1);

			Object layData1 = null;

			this.add(fig_1, layData1);
		}

		/**
		 * @generated NOT
		 */
		private WrapLabel fUpdateProcedureNameFigure;

		/**
		 * @generated NOT
		 */
		public WrapLabel getFigureUpdateProcedureNameFigure() {
			return fUpdateProcedureNameFigure;
		}

		/**
		 * @generated NOT
		 */
		private void setFigureUpdateProcedureNameFigure(WrapLabel fig) {
			fUpdateProcedureNameFigure = fig;
		}

		/**
		 * @generated
		 */
		private org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel fUpdateProcedureStereotypesFigure;

		/**
		 * @generated
		 */
		public org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel getFigureUpdateProcedureStereotypesFigure() {
			return fUpdateProcedureStereotypesFigure;
		}

		/**
		 * @generated
		 */
		private void setFigureUpdateProcedureStereotypesFigure(
				org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel fig) {
			fUpdateProcedureStereotypesFigure = fig;
		}

		/**
		 * @generated
		 */
		private boolean myUseLocalCoordinates = false;

		/**
		 * @generated
		 */
		@Override
		protected boolean useLocalCoordinates() {
			return myUseLocalCoordinates;
		}

		/**
		 * @generated
		 */
		protected void setUseLocalCoordinates(boolean useLocalCoordinates) {
			myUseLocalCoordinates = useLocalCoordinates;
		}

	}

	/**
	 * @generated
	 */
	public static final org.eclipse.swt.graphics.Color UPDATEPROCEDUREARTIFACTFIGURE_BACK = new org.eclipse.swt.graphics.Color(
			null, 250, 245, 210);

	@Override
	protected void handleArtifactPropertyChanged(String propertyKey,
			String oldValue, String newValue) {
		if (artifactPropertyChangeHandler == null) {
			AbstractArtifact artifact = (AbstractArtifact) ((Node) getModel())
					.getElement();
			if (artifact != null) {
				artifactPropertyChangeHandler = new ArtifactPropertyChangeHandler(
						artifact);
			} else {
				EclipsePlugin
						.logErrorMessage("Unable to handle property change");
				return;
			}
		}
		artifactPropertyChangeHandler.handleArtifactPropertyChange(propertyKey,
				oldValue, newValue);
	}

}
