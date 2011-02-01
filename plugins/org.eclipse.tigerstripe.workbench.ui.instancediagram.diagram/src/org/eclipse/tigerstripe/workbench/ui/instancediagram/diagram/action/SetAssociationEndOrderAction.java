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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.action;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.render.editparts.RenderedDiagramRootEditPart;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.Activator;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.InstanceMapEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.BaseDiagramPartAction;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.InstanceDiagramEditorPlugin;
import org.eclipse.ui.IObjectActionDelegate;

public abstract class SetAssociationEndOrderAction extends
		BaseDiagramPartAction implements IObjectActionDelegate {

	protected abstract boolean isAssociationEndIsOrdered(
			AssociationInstance association);

	protected abstract IAssociationEnd getEnd(
			IAssociationArtifact associationArtifact);

	protected abstract String getEndOrder(AssociationInstance association);

	protected abstract void setEndOrder(AssociationInstance association,
			String order);

	protected abstract Instance getEndInstance(AssociationInstance association);

	protected boolean isEndIsOrdered(AssociationInstance association) {
		// Before 230101 bug "is ordered" flag was ignored and flag value was
		// always
		// "false" during associations
		// creation. So to support ordering on diagrams which was created before
		// 230101 we should get
		// "is ordered" flag from direct artifact instance.
		try {
			IAbstractArtifact artifact = association.getArtifact();
			if (artifact != null && artifact instanceof IAssociationArtifact) {
				IAssociationArtifact associationArtifact = (IAssociationArtifact) artifact;
				IAssociationEnd end = getEnd(associationArtifact);
				if (end != null) {
					return end.isOrdered();
				}
			}
		} catch (TigerstripeException e) {
			Activator
					.getDefault()
					.getLog()
					.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, e
							.getLocalizedMessage(), e));
		}
		return isAssociationEndIsOrdered(association);
	}

	public void run(IAction action) {
		final AssociationInstance association = getAssociationInstance();
		if (association != null) {

			final OrderValuesDialog dialog = new OrderValuesDialog(getShell(),
					"Set Order", getEndOrder(association));
			if (dialog.open() == Dialog.OK) {
				TransactionalEditingDomain editDomain = ((IGraphicalEditPart) mySelectedElement)
						.getEditingDomain();
				Command cmd = new AbstractCommand() {
					@Override
					public boolean canExecute() {
						return true;
					}

					public void execute() {
						String order = dialog.getOrder();
						if (order != null) {
							order = order.trim();
							if (order.length() == 0) {
								order = null;
							}
						}
						setEndOrder(association, order);

						InstanceMapEditPart mapEditPart = getMapEditPart();
						mapEditPart.refreshAssociationEndLabels();
					}

					public void redo() {
					}

					@Override
					public boolean canUndo() {
						return false;
					}
				};
				editDomain.getCommandStack().execute(cmd);
				editDomain.getCommandStack().flush();
			}
		}
	}

	@Override
	protected boolean isEnabled() {
		AssociationInstance association = getAssociationInstance();
		if (association != null) {
			return isEndIsOrdered(association);
		}
		return false;
	}

	private InstanceMapEditPart getMapEditPart() {
		return (InstanceMapEditPart) ((RenderedDiagramRootEditPart) mySelectedElement
				.getParent()).getContents();
	}

	protected AssociationInstance getAssociationInstance() {
		EObject object = getCorrespondingEObject();
		if (object instanceof AssociationInstance) {
			return (AssociationInstance) object;
		}
		return null;
	}

	private class OrderValuesDialog extends StatusDialog {
		private final String title;
		private String order;

		private Composite avdArea;

		private Text orderText;

		public OrderValuesDialog(Shell shell, String title, String order) {
			super(shell);
			this.title = title;
			this.order = order;
			// disable ok button, will be activated on first edit
			updateStatus(new Status(IStatus.ERROR,
					InstanceDiagramEditorPlugin.ID, null));
		}

		public String getOrder() {
			return order;
		}

		@Override
		protected void configureShell(Shell shell) {
			super.configureShell(shell);
			if (title != null) {
				shell.setText(title);
			}
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			avdArea = (Composite) super.createDialogArea(parent);

			Composite customContent = new Composite(avdArea, SWT.NONE);
			customContent.setLayout(new GridLayout(2, false));
			customContent.setLayoutData(new GridData(GridData.FILL_BOTH));
			CLabel number = new CLabel(customContent, SWT.NONE);
			number.setText("Order:");
			orderText = new Text(customContent, SWT.BORDER);
			orderText.setLayoutData(new GridData(
					GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
			orderText.setText(order != null ? order : "");
			orderText.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					validateOrder();
				}
			});

			return avdArea;
		}

		private void validateOrder() {
			order = orderText.getText();
			updateStatus(Status.OK_STATUS);
		}

		@Override
		protected void okPressed() {
			if (avdArea != null) {
				avdArea.forceFocus();
			}
			super.okPressed();
		}
	}
}
