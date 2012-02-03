package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions;

import static org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions.CopyPasteUtils.doPaste;
import static org.eclipse.tigerstripe.workbench.utils.AdaptHelper.adapt;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.internal.ui.refactoring.reorg.PasteAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.ui.IWorkbenchSite;

public class TSPasteAction extends PasteAction {

	private final Clipboard clipboard;

	public TSPasteAction(IWorkbenchSite site, Clipboard clipboard) {
		super(site, clipboard);
		this.clipboard = clipboard;
	}

	@Override
	public void run(IStructuredSelection selection) {
		Object selected = selection.getFirstElement();
		if (CopyPasteUtils.containsMembers(clipboard)) {
			if (selected instanceof IAdaptable) {
				IAbstractArtifact artifact = adapt((IAdaptable) selected,
						IAbstractArtifact.class);
				if (artifact != null) {
					if (doPaste(artifact, clipboard, true)) {
						return;
					}
				}
			}
		}
		super.run(selection);
	}
}
