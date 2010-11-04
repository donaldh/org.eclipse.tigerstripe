/******************************************************************************
 * Copyright (c) 2009 by Cisco Systems, Inc. All rights reserved.
 * 
 * This software contains proprietary information which shall not be reproduced
 * or transferred to other documents and shall not be disclosed to others or
 * used for manufacturing or any other purpose without prior permission of Cisco
 * Systems.
 * 
 *****************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.action;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring.RenameModelArtifactWizard;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ClassDiagramShapeNodeEditPart;
import org.eclipse.ui.IActionDelegate;

/**
 * @author danijoh2
 *
 */
public class RefactorRenameAction implements IActionDelegate {

    ISelection selection;
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action) {
        Shell shell = EclipsePlugin.getActiveWorkbenchShell();
        RenameModelArtifactWizard wizard = new RenameModelArtifactWizard();

        EObject element = findElement();
        if (element == null)
            return;

        if (element instanceof QualifiedNamedElement) {
            try {
                IAbstractArtifact artifact = ((QualifiedNamedElement)element).getCorrespondingIArtifact();
                if (artifact != null) {
                    wizard.init((IStructuredSelection) new StructuredSelection(artifact));
                    WizardDialog dialog = new WizardDialog(shell, wizard);
                    dialog.open();
                }
            }
            catch (TigerstripeException e) {
                EclipsePlugin.log(e);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
        this.selection = selection;
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection ssel = (IStructuredSelection) selection;
            action.setEnabled(ssel.toList().size() == 1
                    && findElement() != null);
            return;
        }
        action.setEnabled(false);
    }

    private EObject findElement() {
        EditPart targetPart = findTargetPart();
        if (targetPart instanceof ClassDiagramShapeNodeEditPart )
            return ((Node) targetPart.getModel()).getElement();
        else if (targetPart instanceof ConnectionEditPart)
            return ((Edge) targetPart.getModel()).getElement();
        return null;
    }

    private EditPart findTargetPart() {
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection ssel = (IStructuredSelection) selection;
            Object obj = ssel.getFirstElement();
            if (obj instanceof EditPart)
                return (EditPart) obj;
        }
        return null;
    }

}
