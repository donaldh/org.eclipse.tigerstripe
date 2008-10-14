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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.emf.ecore.EObject;
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
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.policies.ClassInstanceCanonicalEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.policies.ClassInstanceGraphicalNodeEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.policies.ClassInstanceItemSemanticEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.policies.ClassInstanceOpenEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.policies.TigerstripeInstanceConnectionHandleEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.InstanceVisualIDRegistry;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.providers.InstanceElementTypes;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.TigerstripeShapeNodeEditPart;

/**
 * @generated NOT
 */
public class ClassInstanceEditPart extends TigerstripeShapeNodeEditPart
		implements TigerstripeEditableEntityEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 1001;

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
	public ClassInstanceEditPart(View view) {
		super(view);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class key) {
		if (key.equals(IAbstractArtifact.class)) {
			Object model = this.getModel();
			if (model instanceof Node) {
				Node node = (Node) model;
				ClassInstance element = (ClassInstance) node.getElement();
				if ( element == null ) {
					return null;
				}
				try {
					return element.getArtifact();
				} catch (TigerstripeException e) {
					return null;
				}
			}
		}

		return super.getAdapter(key);
	}

	/**
	 * @generated NOT
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
								if (type == InstanceElementTypes.Variable_2001) {
									EditPart compartmentEditPart = getChildBySemanticHint(InstanceVisualIDRegistry
											.getType(ClassInstanceVariableCompartmentEditPart.VISUAL_ID));
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
				new ClassInstanceItemSemanticEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new ClassInstanceGraphicalNodeEditPolicy());
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
				new ClassInstanceCanonicalEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
		installEditPolicy(EditPolicyRoles.OPEN_ROLE,
				new ClassInstanceOpenEditPolicy());
		installEditPolicy(EditPolicyRoles.CONNECTION_HANDLES_ROLE,
				new TigerstripeInstanceConnectionHandleEditPolicy());
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
		ClassInstanceFigure figure = new ClassInstanceFigure();
		return primaryShape = figure;
	}

	/**
	 * @generated
	 */
	public ClassInstanceFigure getPrimaryShape() {
		return (ClassInstanceFigure) primaryShape;
	}

	/**
	 * @generated
	 */
	protected boolean addFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof ClassInstanceNamePackageArtifactNameEditPart) {
			((ClassInstanceNamePackageArtifactNameEditPart) childEditPart)
					.setLabel(getPrimaryShape()
							.getFigureClassInstanceNameFigure());
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
	 * @generated
	 */
	@Override
	public EditPart getPrimaryChildEditPart() {
		return getChildBySemanticHint(InstanceVisualIDRegistry
				.getType(ClassInstanceNamePackageArtifactNameEditPart.VISUAL_ID));
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
	public class ClassInstanceFigure extends org.eclipse.draw2d.RectangleFigure {

		/**
		 * @generated NOT
		 */
		public ClassInstanceFigure() {

			// this.setBackgroundColor(CLASSINSTANCEFIGURE_BACK);
			createContents();
		}

		/**
		 * @generated NOT
		 */
		private void createContents() {
			WrapLabel fig_0 = new WrapLabel();
			fig_0.setText("<...>");

			setFigureClassInstanceNameFigure(fig_0);

			Object layData0 = null;

			this.add(fig_0, layData0);
		}

		/**
		 * @generated NOT
		 */
		private WrapLabel fClassInstanceNameFigure;

		/**
		 * @generated NOT
		 */
		public WrapLabel getFigureClassInstanceNameFigure() {
			return fClassInstanceNameFigure;
		}

		/**
		 * @generated NOT
		 */
		private void setFigureClassInstanceNameFigure(WrapLabel fig) {
			fClassInstanceNameFigure = fig;
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
	public static final org.eclipse.swt.graphics.Color CLASSINSTANCEFIGURE_BACK = new org.eclipse.swt.graphics.Color(
			null, 250, 245, 210);

	@Override
	protected void handleArtifactPropertyChanged(String propertyKey,
			String oldValue, String newValue) {
		// No property handler yet, as no propery defined on Instance Diagrams
	}

}
