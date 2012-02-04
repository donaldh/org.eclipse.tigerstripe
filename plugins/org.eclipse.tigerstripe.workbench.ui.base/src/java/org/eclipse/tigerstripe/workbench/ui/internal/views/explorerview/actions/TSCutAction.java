package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions;

import static org.eclipse.tigerstripe.workbench.ui.EclipsePlugin.getClipboard;
import static org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions.CopyPasteUtils.allMemebers;
import static org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions.CopyPasteUtils.copyMemebers;

import org.eclipse.jdt.internal.ui.refactoring.reorg.CutAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchSite;

public class TSCutAction extends CutAction {

	private final TSPasteAction pasteAction;

	public TSCutAction(IWorkbenchSite site, TSPasteAction pasteAction) {
		super(site);
		this.pasteAction = pasteAction;
	}

	@Override
	public void selectionChanged(IStructuredSelection selection) {
		super.selectionChanged(selection);
		Object[] objects = selection.toArray();
		if (allMemebers(objects)) {
			setEnabled(true);
		}
	}

	@Override
	public void run(IStructuredSelection selection) {
		Object[] objects = selection.toArray();
		if (copyMemebers(objects, getClipboard())) {
			if (pasteAction != null) {
				pasteAction.setMembersToCut(objects);
			}
		} else {
			super.run(selection);
		}
	}

}
