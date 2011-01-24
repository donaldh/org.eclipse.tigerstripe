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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
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

	protected abstract Integer getEndOrderNumber(AssociationInstance association);

	protected abstract void setEndOrderNumber(AssociationInstance association,
			Integer orderNumber);

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
			List<AssociationInstance> instances = collectRelatedAssociations(association);
			orderAssociationsByOrderNumber(instances);

			final AssociationInstance[] associations = instances
					.toArray(new AssociationInstance[instances.size()]);
			final OrderValuesDialog dialog = new OrderValuesDialog(getShell(),
					"Set Order", getEndOrderNumber(association), associations);
			dialog.setLabelProvider(new LabelProvider() {

				@Override
				public String getText(Object element) {
					AssociationInstance ai = ((AssociationInstance) element);
					return getEndInstance(ai).getInstanceName();
				}
			});
			if (dialog.open() == Dialog.OK) {
				TransactionalEditingDomain editDomain = ((IGraphicalEditPart) mySelectedElement)
						.getEditingDomain();
				Command cmd = new AbstractCommand() {
					@Override
					public boolean canExecute() {
						return true;
					}

					public void execute() {
						if (dialog.isCustom()) {
							setEndOrderNumber(association,
									dialog.getCustomOrderNumber());
						} else {
							setOrderNumbers(associations);
						}
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

	private List<AssociationInstance> collectRelatedAssociations(AssociationInstance association) {
		List<AssociationInstance> associations = new ArrayList<AssociationInstance>();
		InstanceMap map = (InstanceMap) association.eContainer();
		String baseFQN = association.getFullyQualifiedName();
		for (Object object : map.getAssociationInstances()) {
			AssociationInstance instance = (AssociationInstance) object;
			if (baseFQN.equals(instance.getFullyQualifiedName())
					&& isEndIsOrdered(instance)) {
				associations.add(instance);
			}
		}
		return associations;
	}

	private void orderAssociationsByOrderNumber(List<AssociationInstance> associations) {
		Collections.sort(associations, new Comparator<AssociationInstance>() {
			public int compare(AssociationInstance association1,
					AssociationInstance association2) {
				Integer orderNumber1 = getEndOrderNumber(association1);
				Integer orderNumber2 = getEndOrderNumber(association2);
				if (orderNumber1 == null && orderNumber2 == null) {
					return 0;
				} else if (orderNumber1 == null && orderNumber2 != null) {
					return 1;
				} else if (orderNumber1 != null && orderNumber2 == null) {
					return -1;
				} else {
					return orderNumber1.compareTo(orderNumber2);
				}
			}
		});
	}

	private void setOrderNumbers(AssociationInstance[] associations) {
		for (int i = 0; i < associations.length; i++) {
			setEndOrderNumber(associations[i], i + 1);
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

	private AssociationInstance getAssociationInstance() {
		EObject object = getCorrespondingEObject();
		if (object instanceof AssociationInstance) {
			return (AssociationInstance) object;
		}
		return null;
	}

	private class OrderValuesDialog extends StatusDialog {
		private final String title;
		private Integer orderNumber;
		private boolean isCustom;
		private final Object[] values;

		private Composite avdArea;

		private Composite customContent;
		private Button customOrderingButton;
		private Text customOrderNumberText;

		private Composite groupContent;
		private Button groupOrderingButton;
		private TableViewer groupViewer;
		private Button groupUpButton;
		private Button groupDownButton;

		private ILabelProvider labelProvider;

		public OrderValuesDialog(Shell shell, String title,
				Integer orderNumber, Object[] input) {
			super(shell);
			this.title = title;
			this.values = input;
			this.orderNumber = orderNumber;
		}

		public void setLabelProvider(ILabelProvider labelProvider) {
			this.labelProvider = labelProvider;
		}

		public boolean isCustom() {
			return isCustom;
		}

		public Integer getCustomOrderNumber() {
			return orderNumber;
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

			Composite content = new Composite(avdArea, SWT.NONE);
			GridLayout gridLayout = new GridLayout(1, false);
			gridLayout.marginHeight = 0;
			gridLayout.marginWidth = 0;
			gridLayout.horizontalSpacing = 0;
			gridLayout.verticalSpacing = 0;
			content.setLayout(gridLayout);
			content.setLayoutData(new GridData(GridData.FILL_BOTH));

			Group box = new Group(content, SWT.NONE);
			box.setLayout(new GridLayout(1, false));
			box.setLayoutData(new GridData(GridData.FILL_BOTH));

			customOrderingButton = new Button(box, SWT.RADIO);
			customOrderingButton.setText("Set custom order number");
			customContent = new Composite(box, SWT.NONE);
			customContent.setLayout(new GridLayout(2, false));
			customContent.setLayoutData(new GridData(GridData.FILL_BOTH));
			CLabel number = new CLabel(customContent, SWT.NONE);
			number.setText("Order number:");
			customOrderNumberText = new Text(customContent, SWT.BORDER);
			customOrderNumberText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
					| GridData.GRAB_HORIZONTAL));
			customOrderNumberText.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					validateCustomOrderNumber();
				}
			});
			customOrderNumberText.setText(orderNumber != null ? orderNumber
					.toString() : "");

			groupOrderingButton = new Button(box, SWT.RADIO);
			groupOrderingButton.setText("Order all association instances");
			groupContent = new Composite(box, SWT.NONE);
			groupContent.setLayout(new GridLayout(2, false));
			groupContent.setLayoutData(new GridData(GridData.FILL_BOTH));
			Table table = new Table(groupContent, SWT.BORDER | SWT.MULTI
					| SWT.FULL_SELECTION);
			groupViewer = new TableViewer(table);
			if (labelProvider == null) {
				labelProvider = new LabelProvider();
			}
			groupViewer.setLabelProvider(labelProvider);
			groupViewer.setContentProvider(new ArrayContentProvider());
			groupViewer.setInput(values);
			groupViewer.addSelectionChangedListener(new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					updateGroupContentButtons();
				}
			});
			groupViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
			createGroupContentButtons(groupContent);

			SelectionListener orderingTypeListener = new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					Object source = e.getSource();
					if (source instanceof Button) {
						Button sourceButton = (Button) source;
						if (sourceButton == customOrderingButton) {
							isCustom = true;
							setEnabledRecursive(customContent, true);
							setEnabledRecursive(groupContent, false);
							validateCustomOrderNumber();
						} else if (sourceButton == groupOrderingButton) {
							isCustom = false;
							setEnabledRecursive(customContent, false);
							setEnabledRecursive(groupContent, true);
							updateGroupContentButtons();
							updateStatus(Status.OK_STATUS);
						}
					}
				}
			};
			customOrderingButton.addSelectionListener(orderingTypeListener);
			groupOrderingButton.addSelectionListener(orderingTypeListener);

			return avdArea;
		}

		private void validateCustomOrderNumber() {
			try {
				orderNumber = Integer.parseInt(customOrderNumberText.getText());
				updateStatus(Status.OK_STATUS);
			} catch (NumberFormatException e) {
				updateStatus(new Status(IStatus.ERROR,
						InstanceDiagramEditorPlugin.ID,
						"Order number must be an integer"));
			}
		}

		private void setEnabledRecursive(Control control, boolean enabled) {
			if (control instanceof Composite) {
				for (Control child : ((Composite) control).getChildren()) {
					setEnabledRecursive(child, enabled);
				}
			}
			control.setEnabled(enabled);
		}

		@Override
		protected void okPressed() {
			if (avdArea != null) {
				avdArea.forceFocus();
			}
			super.okPressed();
		}

		private void createGroupContentButtons(Composite parent) {
			Composite panel = new Composite(parent, SWT.NONE);
			RowLayout layout = new RowLayout(SWT.VERTICAL);
			layout.pack = false;
			panel.setLayout(layout);
			groupUpButton = new Button(panel, SWT.PUSH);
			groupUpButton.setText("Up");
			groupUpButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					int index = getSelectionIndexes()[0];
					swapElements(index, index - 1);
					valuesChanged(index - 1);
				}
			});
			groupDownButton = new Button(panel, SWT.PUSH);
			groupDownButton.setText("Down");
			groupDownButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					int index = getSelectionIndexes()[0];
					swapElements(index, index + 1);
					valuesChanged(index + 1);
				}
			});
			panel.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		}

		private void swapElements(int index1, int index2) {
			Object val = values[index2];
			values[index2] = values[index1];
			values[index1] = val;
		}

		private void valuesChanged(int selectionIndex) {
			groupViewer.refresh();
			if (selectionIndex > 0) {
				groupViewer.getTable().setSelection(selectionIndex);
			}
			updateGroupContentButtons();
		}

		private int[] getSelectionIndexes() {
			return groupViewer.getTable().getSelectionIndices();
		}

		private void updateGroupContentButtons() {
			int[] selections = getSelectionIndexes();
			if (selections.length == 1 && values.length > 0) {
				groupUpButton.setEnabled(selections[0] != 0);
				groupDownButton.setEnabled(selections[0] != values.length - 1);
			} else {
				groupUpButton.setEnabled(false);
				groupDownButton.setEnabled(false);
			}
		}
	}
}
