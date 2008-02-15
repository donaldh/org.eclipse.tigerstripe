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

import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.runtime.diagram.ui.editparts.CompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ListItemComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramColorRegistry;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.tools.DragEditPartsTrackerEx;
import org.eclipse.gmf.runtime.diagram.ui.tools.TextDirectEditManager;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ISemanticParser;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ParserHintAdapter;
import org.eclipse.gmf.runtime.notation.FontStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.policies.ClassInstanceOpenEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.policies.InstanceTextNonResizableEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.policies.InstanceTextSelectionEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.policies.VariableItemSemanticEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.providers.InstanceElementTypes;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramReferenceMapper;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramUtils;

/**
 * @generated
 */
public class VariableEditPart extends CompartmentEditPart implements
		ITextAwareEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 2001;

	/**
	 * @generated
	 */
	private DirectEditManager manager;

	/**
	 * @generated
	 */
	private IParser parser;

	/**
	 * @generated
	 */
	private List parserElements;

	/**
	 * @generated
	 */
	private String defaultText;

	private IArtifactManagerSession artMgrSession = null;
	private IField iField = null;
	private ClassInstanceEditPart classInstanceEditPart = null;

	/**
	 * @generated
	 */
	public VariableEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	@Override
	public DragTracker getDragTracker(Request request) {
		if (request instanceof SelectionRequest
				&& ((SelectionRequest) request).getLastButtonPressed() == 3)
			return null;
		return new DragEditPartsTrackerEx(this);
	}

	/**
	 * @generated NOT
	 */
	@Override
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new VariableItemSemanticEditPolicy());
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE,
				new InstanceTextNonResizableEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new ListItemComponentEditPolicy());
		// installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
		// new LabelDirectEditPolicy());
		removeEditPolicy(EditPolicy.DIRECT_EDIT_ROLE);
		installEditPolicy(EditPolicyRoles.OPEN_ROLE,
				new ClassInstanceOpenEditPolicy());
	}

	/**
	 * @generated
	 */
	protected String getLabelTextHelper(IFigure figure) {
		if (figure instanceof WrapLabel)
			return ((WrapLabel) figure).getText();
		else
			return ((Label) figure).getText();
	}

	/**
	 * @generated
	 */
	protected void setLabelTextHelper(IFigure figure, String text) {
		if (figure instanceof WrapLabel) {
			((WrapLabel) figure).setText(text);
		} else {
			((Label) figure).setText(text);
		}
	}

	/**
	 * @generated
	 */
	protected Image getLabelIconHelper(IFigure figure) {
		if (figure instanceof WrapLabel)
			return ((WrapLabel) figure).getIcon();
		else
			return ((Label) figure).getIcon();
	}

	/**
	 * @generated
	 */
	protected void setLabelIconHelper(IFigure figure, Image icon) {
		if (figure instanceof WrapLabel) {
			((WrapLabel) figure).setIcon(icon);
		} else {
			((Label) figure).setIcon(icon);
		}
	}

	/**
	 * @generated
	 */
	public void setLabel(IFigure figure) {
		unregisterVisuals();
		setFigure(figure);
		defaultText = getLabelTextHelper(figure);
		registerVisuals();
		refreshVisuals();
	}

	/**
	 * @generated
	 */
	@Override
	protected List getModelChildren() {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	@Override
	public IGraphicalEditPart getChildBySemanticHint(String semanticHint) {
		return null;
	}

	/**
	 * @generated
	 */
	protected EObject getParserElement() {
		EObject element = resolveSemanticElement();
		return element != null ? element : (View) getModel();
	}

	/**
	 * @generated
	 */
	protected Image getLabelIcon() {
		return null;
	}

	/**
	 * @generated
	 */
	protected String getLabelText() {
		String text = null;
		if (getParser() != null) {
			text = getParser().getPrintString(
					new EObjectAdapter(getParserElement()),
					getParserOptions().intValue());
		}
		if (text == null || text.length() == 0) {
			text = defaultText;
		}
		return text;
	}

	/**
	 * @generated
	 */
	public void setLabelText(String text) {
		setLabelTextHelper(getFigure(), text);
		Object pdEditPolicy = getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
		if (pdEditPolicy instanceof InstanceTextSelectionEditPolicy) {
			((InstanceTextSelectionEditPolicy) pdEditPolicy).refreshFeedback();
		}
	}

	/**
	 * @generated
	 */
	public String getEditText() {
		if (getParser() == null)
			return ""; //$NON-NLS-1$
		return getParser().getEditString(
				new EObjectAdapter(getParserElement()),
				getParserOptions().intValue());
	}

	/**
	 * @generated NOT
	 */
	protected boolean isEditable() {
		// return getEditText() != null;
		return false;
	}

	/**
	 * @generated
	 */
	public ICellEditorValidator getEditTextValidator() {
		return new ICellEditorValidator() {

			public String isValid(final Object value) {
				if (value instanceof String) {
					final EObject element = getParserElement();
					final IParser parser = getParser();
					try {
						IParserEditStatus valid = (IParserEditStatus) getEditingDomain()
								.runExclusive(new RunnableWithResult.Impl() {

									public void run() {
										setResult(parser.isValidEditString(
												new EObjectAdapter(element),
												(String) value));
									}
								});
						return valid.getCode() == IParserEditStatus.EDITABLE ? null
								: valid.getMessage();
					} catch (InterruptedException ie) {
						TigerstripeRuntime.logErrorMessage(
								"InterruptedException detected", ie);
					}
				}

				// shouldn't get here
				return null;
			}
		};
	}

	/**
	 * @generated
	 */
	public IContentAssistProcessor getCompletionProcessor() {
		if (getParser() == null)
			return null;
		return getParser().getCompletionProcessor(
				new EObjectAdapter(getParserElement()));
	}

	/**
	 * @generated
	 */
	public ParserOptions getParserOptions() {
		return ParserOptions.NONE;
	}

	/**
	 * @generated
	 */
	public IParser getParser() {
		if (parser == null) {
			String parserHint = ((View) getModel()).getType();
			ParserHintAdapter hintAdapter = new ParserHintAdapter(
					getParserElement(), parserHint) {

				@Override
				public Object getAdapter(Class adapter) {
					if (IElementType.class.equals(adapter))
						return InstanceElementTypes.Variable_2001;
					return super.getAdapter(adapter);
				}
			};
			parser = ParserService.getInstance().getParser(hintAdapter);
		}
		return parser;
	}

	/**
	 * @generated
	 */
	protected DirectEditManager getManager() {
		if (manager == null) {
			setManager(new TextDirectEditManager(this, TextDirectEditManager
					.getTextCellEditorClass(this), InstanceEditPartFactory
					.getTextCellEditorLocator(this)));
		}
		return manager;
	}

	/**
	 * @generated
	 */
	protected void setManager(DirectEditManager manager) {
		this.manager = manager;
	}

	/**
	 * @generated
	 */
	protected void performDirectEdit() {
		getManager().show();
	}

	/**
	 * @generated
	 */
	protected void performDirectEdit(Point eventLocation) {
		if (getManager().getClass() == TextDirectEditManager.class) {
			((TextDirectEditManager) getManager()).show(eventLocation
					.getSWTPoint());
		}
	}

	/**
	 * @generated
	 */
	private void performDirectEdit(char initialCharacter) {
		if (getManager() instanceof TextDirectEditManager) {
			((TextDirectEditManager) getManager()).show(initialCharacter);
		} else {
			performDirectEdit();
		}
	}

	/**
	 * @generated
	 */
	@Override
	protected void performDirectEditRequest(Request request) {
		final Request theRequest = request;
		try {
			getEditingDomain().runExclusive(new Runnable() {

				public void run() {
					if (isActive() && isEditable()) {
						if (theRequest
								.getExtendedData()
								.get(
										RequestConstants.REQ_DIRECTEDIT_EXTENDEDDATA_INITIAL_CHAR) instanceof Character) {
							Character initialChar = (Character) theRequest
									.getExtendedData()
									.get(
											RequestConstants.REQ_DIRECTEDIT_EXTENDEDDATA_INITIAL_CHAR);
							performDirectEdit(initialChar.charValue());
						} else if ((theRequest instanceof DirectEditRequest)
								&& (getEditText().equals(getLabelText()))) {
							DirectEditRequest editRequest = (DirectEditRequest) theRequest;
							performDirectEdit(editRequest.getLocation());
						} else {
							performDirectEdit();
						}
					}
				}
			});
		} catch (InterruptedException e) {
			TigerstripeRuntime.logErrorMessage("InterruptedException detected",
					e);
		}
	}

	/**
	 * @generated
	 */
	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshLabel();
		refreshFont();
		refreshFontColor();
		refreshUnderline();
		refreshStrikeThrough();
	}

	/**
	 * @generated
	 */
	protected void refreshLabel() {
		setLabelTextHelper(getFigure(), getLabelText());
		setLabelIconHelper(getFigure(), getLabelIcon());
		Object pdEditPolicy = getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
		if (pdEditPolicy instanceof InstanceTextSelectionEditPolicy) {
			((InstanceTextSelectionEditPolicy) pdEditPolicy).refreshFeedback();
		}
	}

	/**
	 * @generated
	 */
	protected void refreshUnderline() {
		FontStyle style = (FontStyle) getFontStyleOwnerView().getStyle(
				NotationPackage.eINSTANCE.getFontStyle());
		if (style != null && getFigure() instanceof WrapLabel) {
			((WrapLabel) getFigure()).setTextUnderline(style.isUnderline());
		}
	}

	/**
	 * @generated
	 */
	protected void refreshStrikeThrough() {
		FontStyle style = (FontStyle) getFontStyleOwnerView().getStyle(
				NotationPackage.eINSTANCE.getFontStyle());
		if (style != null && getFigure() instanceof WrapLabel) {
			((WrapLabel) getFigure()).setTextStrikeThrough(style
					.isStrikeThrough());
		}
	}

	/**
	 * @generated
	 */
	@Override
	protected void refreshFont() {
		FontStyle style = (FontStyle) getFontStyleOwnerView().getStyle(
				NotationPackage.eINSTANCE.getFontStyle());
		if (style != null) {
			FontData fontData = new FontData(style.getFontName(), style
					.getFontHeight(), (style.isBold() ? SWT.BOLD : SWT.NORMAL)
					| (style.isItalic() ? SWT.ITALIC : SWT.NORMAL));
			setFont(fontData);
		}
	}

	/**
	 * @generated
	 */
	@Override
	protected void setFontColor(Color color) {
		getFigure().setForegroundColor(color);
	}

	/**
	 * @generated
	 */
	@Override
	protected void addSemanticListeners() {
		if (getParser() instanceof ISemanticParser) {
			EObject element = resolveSemanticElement();
			parserElements = ((ISemanticParser) getParser())
					.getSemanticElementsBeingParsed(element);
			for (int i = 0; i < parserElements.size(); i++) {
				addListenerFilter(
						"SemanticModel" + i, this, (EObject) parserElements.get(i)); //$NON-NLS-1$
			}
		} else {
			super.addSemanticListeners();
		}
	}

	/**
	 * @generated
	 */
	@Override
	protected void removeSemanticListeners() {
		if (parserElements != null) {
			for (int i = 0; i < parserElements.size(); i++) {
				removeListenerFilter("SemanticModel" + i); //$NON-NLS-1$
			}
		} else {
			super.removeSemanticListeners();
		}
	}

	/**
	 * @generated
	 */
	@Override
	protected AccessibleEditPart getAccessibleEditPart() {
		if (accessibleEP == null) {
			accessibleEP = new AccessibleGraphicalEditPart() {

				@Override
				public void getName(AccessibleEvent e) {
					e.result = getLabelTextHelper(getFigure());
				}
			};
		}
		return accessibleEP;
	}

	/**
	 * @generated
	 */
	private View getFontStyleOwnerView() {
		return getPrimaryView();
	}

	/**
	 * @generated
	 */
	@Override
	protected void addNotationalListeners() {
		super.addNotationalListeners();
		addListenerFilter("PrimaryView", this, getPrimaryView()); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	@Override
	protected void removeNotationalListeners() {
		super.removeNotationalListeners();
		removeListenerFilter("PrimaryView"); //$NON-NLS-1$
	}

	private ClassInstanceEditPart getClassInstanceEditPart() {
		// if the value is still null, initialize the classInstanceEditPart
		// value
		if (classInstanceEditPart == null) {
			classInstanceEditPart = (ClassInstanceEditPart) this.getParent()
					.getParent();
		}
		return classInstanceEditPart;
	}

	private IArtifactManagerSession getArtifactManagerSession() {
		// if the value is still null, initialize the artMgrSession value
		if (artMgrSession == null) {
			ClassInstanceEditPart classInstEditPart = getClassInstanceEditPart();
			InstanceMapEditPart mapEditPart = (InstanceMapEditPart) classInstEditPart
					.getParent();
			DiagramGraphicalViewer mapViewer = (DiagramGraphicalViewer) mapEditPart
					.getViewer();
			DiagramEditDomain domain = (DiagramEditDomain) mapViewer
					.getEditDomain();
			IResource res = (IResource) domain.getEditorPart().getEditorInput()
					.getAdapter(IResource.class);
			IAbstractTigerstripeProject aProject = EclipsePlugin
					.getITigerstripeProjectFor(res.getProject());
			if (!(aProject instanceof ITigerstripeModelProject))
				throw new RuntimeException("non-Tigerstripe Project found");
			ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
			try {
				artMgrSession = project.getArtifactManagerSession();
			} catch (TigerstripeException e) {
				throw new RuntimeException("IArtifactManagerSession not found");
			}
		}
		return artMgrSession;
	}

	private IField getIField() {
		// get the iField value corresponding to this variable edit part
		// from the artifact manager
		IArtifactManagerSession artMgrSessn = this.getArtifactManagerSession();
		ClassInstanceEditPart classInstEditPart = this
				.getClassInstanceEditPart();
		ClassInstance classInstance = (ClassInstance) ((View) classInstEditPart
				.getModel()).getElement();
		Variable variable = (Variable) ((View) this.getModel()).getElement();
		try {
			IAbstractArtifact iArtifact = artMgrSessn
					.getArtifactByFullyQualifiedName(classInstance
							.getFullyQualifiedName());
			iField = InstanceDiagramUtils.findIFieldByName(iArtifact, variable
					.getName());
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logInfoMessage("TigerstripeException detected",
					e);
		}
		return iField;
	}

	/**
	 * @generated NOT
	 */
	@Override
	protected void handleNotificationEvent(Notification event) {
		Object feature = event.getFeature();
		if (NotationPackage.eINSTANCE.getFontStyle_FontColor().equals(feature)) {
			Integer c = (Integer) event.getNewValue();
			setFontColor(DiagramColorRegistry.getInstance().getColor(c));
		} else if (NotationPackage.eINSTANCE.getFontStyle_Underline().equals(
				feature)) {
			refreshUnderline();
		} else if (NotationPackage.eINSTANCE.getFontStyle_StrikeThrough()
				.equals(feature)) {
			refreshStrikeThrough();
		} else if (NotationPackage.eINSTANCE.getFontStyle_FontHeight().equals(
				feature)
				|| NotationPackage.eINSTANCE.getFontStyle_FontName().equals(
						feature)
				|| NotationPackage.eINSTANCE.getFontStyle_Bold()
						.equals(feature)
				|| NotationPackage.eINSTANCE.getFontStyle_Italic().equals(
						feature)) {
			refreshFont();
		} else if (event.getEventType() == Notification.SET
				&& feature instanceof EAttribute
				&& ((EAttribute) feature).getName().equals("value")) {
			IArtifactManagerSession artMgrSessn = this
					.getArtifactManagerSession();
			ClassInstanceEditPart classInstEditPart = this
					.getClassInstanceEditPart();
			// if here, are setting a new value for the variable, so we need to
			// update the
			// reference mapper if this field is a non-primitive field
			ClassInstance classInstance = (ClassInstance) ((View) classInstEditPart
					.getModel()).getElement();
			Variable variable = (Variable) ((View) this.getModel())
					.getElement();
			try {
				IAbstractArtifact iArtifact = artMgrSessn
						.getArtifactByFullyQualifiedName(classInstance
								.getFullyQualifiedName());
				IField field = this.getIField();
				InstanceMap map = (InstanceMap) classInstance.eContainer();
				if (!InstanceDiagramUtils.isPrimitive(artMgrSessn, field
						.getType())) {
					// remove the references to the old value(s)
					String oldValue = (String) event.getOldValue();
					if (oldValue != null && oldValue.length() > 0) {
						List<String> oldValues = InstanceDiagramUtils
								.instanceReferencesAsList((String) event
										.getOldValue());
						InstanceDiagramReferenceMapper.eINSTANCE
								.removeVariableReferences(map, variable,
										oldValues);
					}
					// and add in the references to the new value(s)
					String newValue = (String) event.getNewValue();
					if (newValue != null && newValue.length() > 0) {
						List<String> newValues = InstanceDiagramUtils
								.instanceReferencesAsList(newValue);
						InstanceDiagramReferenceMapper.eINSTANCE
								.addVariableReferences(variable, newValues);
					}
				}
			} catch (TigerstripeException e) {
				throw new RuntimeException("IAbstractArtifact not found");
			}
			refreshLabel();
		} else if (event.getEventType() == Notification.SET
				&& feature instanceof EReference
				&& ((EReference) feature).getName().equals("element")) {
			// end up here when undo'ing the deletion of a class instance that
			// contained variables
			// that referenced other instances in the diagram...
			IArtifactManagerSession artMgrSessn = this
					.getArtifactManagerSession();
			ClassInstanceEditPart classInstEditPart = this
					.getClassInstanceEditPart();
			// if here, are setting a new value for the variable in a class
			// instance, so we need to
			// update the reference mapper if this field is a non-primitive
			// field
			ClassInstance classInstance = (ClassInstance) ((View) classInstEditPart
					.getModel()).getElement();
			Variable variable = (Variable) ((View) this.getModel())
					.getElement();
			try {
				IAbstractArtifact iArtifact = artMgrSessn
						.getArtifactByFullyQualifiedName(classInstance
								.getFullyQualifiedName());
				IField field = this.getIField();
				InstanceMap map = (InstanceMap) classInstance.eContainer();
				if (!InstanceDiagramUtils.isPrimitive(artMgrSessn, field
						.getType())) {
					// remove the references to the old value(s)
					Variable oldVar = (Variable) event.getOldValue();
					if (oldVar != null) {
						String oldValue = oldVar.getValue();
						if (oldValue != null && oldValue.length() > 0) {
							List<String> oldValues = InstanceDiagramUtils
									.instanceReferencesAsList((String) event
											.getOldValue());
							InstanceDiagramReferenceMapper.eINSTANCE
									.removeVariableReferences(map, variable,
											oldValues);
						}
					}
					// and add in the references to the new value(s)
					Variable newVar = (Variable) event.getNewValue();
					if (newVar != null) {
						String newValue = newVar.getValue();
						if (newValue != null && newValue.length() > 0) {
							List<String> newValues = InstanceDiagramUtils
									.instanceReferencesAsList(newValue);
							InstanceDiagramReferenceMapper.eINSTANCE
									.addVariableReferences(variable, newValues);
						}
					}
				}
			} catch (TigerstripeException e) {
				throw new RuntimeException("IAbstractArtifact not found");
			}
			refreshLabel();
		} else {
			if (getParser() != null
					&& getParser().isAffectingEvent(event,
							getParserOptions().intValue())) {
				refreshLabel();
			}
			if (getParser() instanceof ISemanticParser) {
				ISemanticParser modelParser = (ISemanticParser) getParser();
				if (modelParser.areSemanticElementsAffected(null, event)) {
					removeSemanticListeners();
					if (resolveSemanticElement() != null) {
						addSemanticListeners();
					}
					refreshLabel();
				}
			}
		}
		super.handleNotificationEvent(event);
	}

	/**
	 * @generated
	 */
	@Override
	protected IFigure createFigure() {
		IFigure label = createFigurePrim();
		defaultText = getLabelTextHelper(label);
		return label;
	}

	/**
	 * @generated
	 */
	protected IFigure createFigurePrim() {
		return new VariableNameFigure();
	}

	/**
	 * @generated
	 */
	public class VariableNameFigure extends
			org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel {

		/**
		 * @generated
		 */
		public VariableNameFigure() {

			this.setText("<...>");
			createContents();
		}

		/**
		 * @generated
		 */
		private void createContents() {
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

}
