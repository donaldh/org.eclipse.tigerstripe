/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.utils;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.ActiveShellExpression;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.texteditor.AnnotationPreference;
import org.eclipse.ui.texteditor.DefaultMarkerAnnotationAccess;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.IUpdate;
import org.eclipse.ui.texteditor.MarkerAnnotationPreferences;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

public class SpellCheckingTextBox {

	private final StyledText textField;
	private Composite parentComposite;

	public SpellCheckingTextBox(Composite composite, String initialText) {
		parentComposite = composite;
		
		AnnotationModel annotationModel = new AnnotationModel();
		IAnnotationAccess annotationAccess = new DefaultMarkerAnnotationAccess();

		final SourceViewer sourceViewer = new SourceViewer(parentComposite,
				null, null,
				true, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		textField = sourceViewer.getTextWidget();
		textField.setIndent(2);
		textField.setLayoutData(new GridData(GridData.FILL_BOTH));

		final SourceViewerDecorationSupport support = new SourceViewerDecorationSupport(
				sourceViewer, null, annotationAccess,
				EditorsUI.getSharedTextColors());

		Iterator<?> e = new MarkerAnnotationPreferences()
				.getAnnotationPreferences().iterator();
		while (e.hasNext()) {
			support.setAnnotationPreference((AnnotationPreference) e.next());
		}

		support.install(EditorsUI.getPreferenceStore());

		final IHandlerService handlerService = (IHandlerService) PlatformUI
				.getWorkbench().getService(IHandlerService.class);
		final IHandlerActivation handlerActivation = installQuickFixActionHandler(
				handlerService, sourceViewer);

		final TextViewerAction cutAction = new TextViewerAction(sourceViewer,
				ITextOperationTarget.CUT);
		cutAction.setText("Cut");
		cutAction.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_CUT);

		final TextViewerAction copyAction = new TextViewerAction(sourceViewer,
				ITextOperationTarget.COPY);
		copyAction.setText("Copy");
		copyAction.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_COPY);

		final TextViewerAction pasteAction = new TextViewerAction(sourceViewer,
				ITextOperationTarget.PASTE);
		pasteAction.setText("Paste");
		pasteAction
				.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_PASTE);

		final TextViewerAction selectAllAction = new TextViewerAction(
				sourceViewer, ITextOperationTarget.SELECT_ALL);
		selectAllAction.setText("Select All");
		selectAllAction
				.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_SELECT_ALL);

		MenuManager contextMenu = new MenuManager();
		contextMenu.add(cutAction);
		contextMenu.add(copyAction);
		contextMenu.add(pasteAction);
		contextMenu.add(selectAllAction);
		contextMenu.add(new Separator());

		final SubMenuManager quickFixMenu = new SubMenuManager(contextMenu);
		quickFixMenu.setVisible(true);
		quickFixMenu.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				quickFixMenu.removeAll();

				IAnnotationModel annotationModel = sourceViewer
						.getAnnotationModel();
				Iterator<?> annotationIterator = annotationModel
						.getAnnotationIterator();
				while (annotationIterator.hasNext()) {
					Annotation annotation = (Annotation) annotationIterator
							.next();
					if (!annotation.isMarkedDeleted()
							&& includes(
									annotationModel.getPosition(annotation),
									sourceViewer.getTextWidget()
											.getCaretOffset())
							&& sourceViewer.getQuickAssistAssistant().canFix(
									annotation)) {
						ICompletionProposal[] computeQuickAssistProposals = sourceViewer
								.getQuickAssistAssistant()
								.getQuickAssistProcessor()
								.computeQuickAssistProposals(
										sourceViewer
												.getQuickAssistInvocationContext());
						for (int i = 0; i < computeQuickAssistProposals.length; i++) {
							final ICompletionProposal proposal = computeQuickAssistProposals[i];
							quickFixMenu.add(new Action(proposal
									.getDisplayString()) {

								@Override
								public void run() {
									proposal.apply(sourceViewer.getDocument());
								}

								@Override
								public ImageDescriptor getImageDescriptor() {
									if (proposal.getImage() != null) {
										return ImageDescriptor
												.createFromImage(proposal
														.getImage());
									}
									return null;
								}
							});
						}
					}
				}
			}

		});

		textField.addFocusListener(new FocusListener() {

			private IHandlerActivation cutHandlerActivation;
			private IHandlerActivation copyHandlerActivation;
			private IHandlerActivation pasteHandlerActivation;
			private IHandlerActivation selectAllHandlerActivation;

			public void focusGained(FocusEvent e) {
				cutAction.update();
				copyAction.update();
				IHandlerService service = (IHandlerService) PlatformUI
						.getWorkbench().getService(IHandlerService.class);
				this.cutHandlerActivation = service.activateHandler(
						IWorkbenchCommandConstants.EDIT_CUT, new ActionHandler(
								cutAction), new ActiveShellExpression(
								parentComposite.getShell()));
				this.copyHandlerActivation = service.activateHandler(
						IWorkbenchCommandConstants.EDIT_COPY,
						new ActionHandler(copyAction),
						new ActiveShellExpression(parentComposite.getShell()));
				this.pasteHandlerActivation = service.activateHandler(
						IWorkbenchCommandConstants.EDIT_PASTE,
						new ActionHandler(pasteAction),
						new ActiveShellExpression(parentComposite.getShell()));
				this.selectAllHandlerActivation = service.activateHandler(
						IWorkbenchCommandConstants.EDIT_SELECT_ALL,
						new ActionHandler(selectAllAction),
						new ActiveShellExpression(parentComposite.getShell()));

			}

			public void focusLost(FocusEvent e) {
				IHandlerService service = (IHandlerService) PlatformUI
						.getWorkbench().getService(IHandlerService.class);

				if (cutHandlerActivation != null) {
					service.deactivateHandler(cutHandlerActivation);
				}

				if (copyHandlerActivation != null) {
					service.deactivateHandler(copyHandlerActivation);
				}

				if (pasteHandlerActivation != null) {
					service.deactivateHandler(pasteHandlerActivation);
				}

				if (selectAllHandlerActivation != null) {
					service.deactivateHandler(selectAllHandlerActivation);
				}
			}

		});

		sourceViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {

					public void selectionChanged(SelectionChangedEvent event) {
						cutAction.update();
						copyAction.update();
					}

				});

		sourceViewer.getTextWidget().addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				support.uninstall();
				handlerService.deactivateHandler(handlerActivation);
			}

		});

		Document document = new Document(initialText);

		sourceViewer.configure(new TextSourceViewerConfiguration(EditorsUI
				.getPreferenceStore()));
		sourceViewer.setDocument(document, annotationModel);

		textField.setMenu(contextMenu.createContextMenu(textField));
		textField.selectAll();
	}

	protected boolean includes(Position position, int caretOffset) {
		return position.includes(caretOffset)
				|| (position.offset + position.length) == caretOffset;
	}

	private IHandlerActivation installQuickFixActionHandler(
			IHandlerService handlerService, SourceViewer sourceViewer) {
		return handlerService.activateHandler(
				ITextEditorActionDefinitionIds.QUICK_ASSIST,
				createQuickFixActionHandler(sourceViewer),
				new ActiveShellExpression(sourceViewer.getTextWidget()
						.getShell()));
	}

	private ActionHandler createQuickFixActionHandler(
			final ITextOperationTarget textOperationTarget) {
		Action quickFixAction = new Action() {
			@Override
			public void run() {
				textOperationTarget.doOperation(ISourceViewer.QUICK_ASSIST);
			}
		};
		quickFixAction
				.setActionDefinitionId(ITextEditorActionDefinitionIds.QUICK_ASSIST);
		return new ActionHandler(quickFixAction);
	}

	public void setEnabled(boolean enabled) {
		textField.setEnabled(enabled);
	}

	public String getText() {
		return textField.getText();
	}

	public void setText(String text) {
		textField.setText(text);
	}

	public void setFocus() {
		textField.setFocus();
	}
	
	public void addModifyListener(ModifyListener modifyListener) {
		textField.addModifyListener(modifyListener);
	}

	public void addKeyListener(KeyListener keyListener) {
		textField.addKeyListener(keyListener);
	}

	private class TextViewerAction extends Action implements IUpdate {
		private int operationCode = -1;
		private final ITextOperationTarget operationTarget;

		public TextViewerAction(ITextViewer viewer, int operationCode) {
			this.operationCode = operationCode;
			operationTarget = viewer.getTextOperationTarget();
			update();
		}

		public void update() {
			boolean wasEnabled = isEnabled();
			boolean isEnabled = (operationTarget != null && operationTarget
					.canDoOperation(operationCode));
			setEnabled(isEnabled);
			if (wasEnabled != isEnabled) {
				firePropertyChange(ENABLED, wasEnabled ? Boolean.TRUE
						: Boolean.FALSE, isEnabled ? Boolean.TRUE
						: Boolean.FALSE);
			}
		}

		@Override
		public void run() {
			if (operationCode != -1 && operationTarget != null) {
				operationTarget.doOperation(operationCode);
			}
		}
	}

	public Control getControl() {
		return textField;
	}
}