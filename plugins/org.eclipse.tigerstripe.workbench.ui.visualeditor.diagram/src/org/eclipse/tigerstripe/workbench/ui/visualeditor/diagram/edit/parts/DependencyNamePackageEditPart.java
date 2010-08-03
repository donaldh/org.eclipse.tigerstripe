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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.LabelDirectEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramColorRegistry;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.tools.TextDirectEditManager;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ISemanticParser;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ParserHintAdapter;
import org.eclipse.gmf.runtime.notation.FontStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.impl.NodeImpl;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.InitialDiagramPrefs;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring.RenameModelArtifactWizard;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies.TigerstripeTextSelectionEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeVisualIDRegistry;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers.TigerstripeElementTypes;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.DiagramPropertiesHelper;

/**
 * @generated NOT
 */
public class DependencyNamePackageEditPart extends
		StereotypeNamePackageEditPart implements ITextAwareEditPart,
		NamePackageInterface {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 4031;

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

	/**
	 * @generated
	 */
	static {
		registerSnapBackPosition(TigerstripeVisualIDRegistry
				.getType(DependencyNamePackageEditPart.VISUAL_ID), new Point(0,
				-15));
	}

	/**
	 * @generated
	 */
	public DependencyNamePackageEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	@Override
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new LabelDirectEditPolicy());
	}

	/**
	 * @generated
	 */
	@Override
	public int getKeyPoint() {
		return ConnectionLocator.MIDDLE;
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
	 * @generated NOT
	 */
	protected String getLabelText() {
		String text = null;
		if (getParser() != null) {
			text = getParser().getPrintString(
					new EObjectAdapter(getParserElement()),
					getParserOptions().intValue());
		}
		QualifiedNamedElement qualNamedElem = (QualifiedNamedElement) ((NodeImpl) this
				.getModel()).getElement();
		String packageName = qualNamedElem.getPackage();
		Map map = (Map) qualNamedElem.eContainer();
		String elemPackageName = null;
		if (packageName == null)
			elemPackageName = map.getBasePackage();
		else
			elemPackageName = packageName;
		if (text == null || text.length() == 0) {
			text = defaultText;
		} else if (hideArtifactPackages(map)
				|| elemPackageName.equals(map.getBasePackage())) {
			// since the packages match, truncate to just show the name
			// or the diagram is set to hide packages anyway..
			int lastDotPos = text.lastIndexOf(".");
			if (lastDotPos > 0 && lastDotPos < (text.length() - 1)) {
				String newText = text.substring(lastDotPos + 1);
				text = newText;
			}
		}

		text = decorateText(text);

		// now add the stereotype string as a prefix...
		DiagramPropertiesHelper helper = new DiagramPropertiesHelper(map);
		try {
			IAbstractArtifact iArtifact = qualNamedElem
					.getCorrespondingIArtifact();
			StringBuffer stereotypeBuffer = new StringBuffer();
			if (!Boolean.parseBoolean(helper
					.getPropertyValue(DiagramPropertiesHelper.HIDESTEREOTYPES))) {
				IWorkbenchProfile profile = TigerstripeCore.getWorkbenchProfileSession().getActiveProfile();
				if (iArtifact != null) {
					Collection<IStereotypeInstance> stereotypes = iArtifact
							.getStereotypeInstances();
					
					for (IStereotypeInstance stereotype : stereotypes) {
						IStereotype stereo = profile.getStereotypeByName(stereotype.getName());
						if (stereo != null){
							if (stereotypeBuffer.length() == 0)
								stereotypeBuffer.append("<<");
							else
								stereotypeBuffer.append(", ");
							stereotypeBuffer.append(stereotype.getName());
						}
					}
				}
				if (stereotypeBuffer.length() != 0)
					stereotypeBuffer.append(">>\n");
				
			}
			// get the length of the first line (the bufferLen); remember the
			// extra newline character
			// doesn't count towards the length
			int bufferLen = stereotypeBuffer.toString().length() - 1;
			// and get the length of the second line (the text label)
			if (bufferLen != -1) {// if == -1 annotations are hidden no need
				// to align
				int textLen = text.length();
				if (bufferLen >= textLen) {
					// if the first is greater length than the second, just
					// center
					// the label
					((WrapLabel) this.getFigure())
							.setTextWrapAlignment(PositionConstants.CENTER);
				} else {
					// else, left justify the label and pad the first line to
					// center-align it
					// with the second
					((WrapLabel) this.getFigure())
							.setTextWrapAlignment(PositionConstants.LEFT);
					// figure out how many spaces to prefix the first line with
					// in
					// order to try
					// to force a center-alignment of the labels
					int avgCharWidth = getAvgCharWidth();
					int spaceCharWidth = getRenderedLength(" ", figure
							.getFont());
					double factor = (double) avgCharWidth
							/ (double) spaceCharWidth;
					int numSpaces = (int) (((textLen - bufferLen)) / 2.0
							* factor + 0.5);
					if (numSpaces > 0) {
						for (int i = 0; i < numSpaces; i++)
							stereotypeBuffer.insert(0, ' ');
					}
				}
			}
			stereotypeBuffer.append(text);
			return stereotypeBuffer.toString();
		} catch (TigerstripeException e) {
			return "";
		}
	}

	/**
	 * @generated
	 */
	public void setLabelText(String text) {
		setLabelTextHelper(getFigure(), text);
		Object pdEditPolicy = getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
		if (pdEditPolicy instanceof TigerstripeTextSelectionEditPolicy) {
			((TigerstripeTextSelectionEditPolicy) pdEditPolicy)
					.refreshFeedback();
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
		return getEditText() != null && !isReadonlyArtifact();
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
						return TigerstripeElementTypes.Dependency_3008;
					return super.getAdapter(adapter);
				}
			};
			parser = ParserService.getInstance().getParser(hintAdapter);
		}
		return parser;
	}

	/**
	 * @generated NOT
	 */
	protected DirectEditManager getManager() {
		if (manager == null) {
			setManager(new NameAwareTextDirectEditManager(this,
					TextDirectEditManager.getTextCellEditorClass(this),
					TigerstripeEditPartFactory.getTextCellEditorLocator(this)));
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
	 * @generated NOT
	 */
	protected void performDirectEdit(Point eventLocation) {
		if (getManager() instanceof TextDirectEditManager) {
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
	 * @generated NOT
	 * This now calls out to the refactor logic
	 */
	@Override
	protected void performDirectEditRequest(Request request) {
		
		// Bugzilla 319500: Refactor wizard should not pop up when element is initially created		
		if (manager == null) {
			performDirectEdit();
			return;
		}
		
		Shell shell = EclipsePlugin.getActiveWorkbenchShell();
		RenameModelArtifactWizard wizard = new RenameModelArtifactWizard();
		QualifiedNamedElement qualNamedElem = (QualifiedNamedElement) ((NodeImpl) this
				.getModel()).getElement();

		try {
			IAbstractArtifact artifact = qualNamedElem.getCorrespondingIArtifact();


			if (artifact != null){
				wizard.init((IStructuredSelection) new StructuredSelection(artifact));
				WizardDialog dialog = new WizardDialog(shell, wizard);
				dialog.open();
			}
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("Failed to determine Artifact for refactoring",
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
		if (pdEditPolicy instanceof TigerstripeTextSelectionEditPolicy) {
			((TigerstripeTextSelectionEditPolicy) pdEditPolicy)
					.refreshFeedback();
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
	 * @generated NOT
	 */
	@Override
	protected void refreshFont() {
		FontStyle style = (FontStyle) getFontStyleOwnerView().getStyle(
				NotationPackage.eINSTANCE.getFontStyle());
		if (style != null) {
			FontData fontData = new FontData(style.getFontName(), style
					.getFontHeight(), (style.isBold() ? SWT.BOLD : SWT.NORMAL)
					| (isAbstractArtifact() ? SWT.ITALIC : SWT.NORMAL));
			setFont(fontData);
		} else {
			FontData fontData = new FontData(
					InitialDiagramPrefs.DEFAULT_FONTNAME,
					InitialDiagramPrefs.DEFAULT_FONTSIZE,
					(isAbstractArtifact() ? SWT.ITALIC : SWT.NORMAL));
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
		return (View) getModel();
	}

	/**
	 * @generated NOT
	 */
	@Override
	protected void handleNotificationEvent(Notification event) {
		Object feature = event.getFeature();
		if (VisualeditorPackage.eINSTANCE.getQualifiedNamedElement_IsAbstract()
				.equals(feature)) {
			refreshFont();
		} else if (NotationPackage.eINSTANCE.getFontStyle_FontColor().equals(
				feature)) {
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
	 * @generated NOT
	 */
	protected IFigure createFigurePrim() {
		figure = new DependencyNameFigure();
		return figure;
	}

	/**
	 * @generated
	 */
	public class DependencyNameFigure extends
			org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel {

		/**
		 * @generated NOT
		 */
		public DependencyNameFigure() {

			this.setText("<...>");
			this.setFont(DEPENDENCYNAMEFIGURE_FONT);
			this.setTextAlignment(PositionConstants.CENTER);
			this.setTextJustification(PositionConstants.CENTER);
			this.setTextWrap(true);
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

	/**
	 * @generated
	 */
	public static final org.eclipse.swt.graphics.Font DEPENDENCYNAMEFIGURE_FONT = new org.eclipse.swt.graphics.Font(
			org.eclipse.swt.widgets.Display.getCurrent(),
			InitialDiagramPrefs.DEFAULT_FONTNAME,
			InitialDiagramPrefs.DEFAULT_FONTSIZE,
			InitialDiagramPrefs.DEFAULT_FONTSTYLE);

}
