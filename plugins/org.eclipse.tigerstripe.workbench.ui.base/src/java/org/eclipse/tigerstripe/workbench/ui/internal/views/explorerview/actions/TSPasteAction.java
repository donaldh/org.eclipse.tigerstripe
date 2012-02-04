package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions;

import static java.util.Collections.singleton;
import static org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions.CopyPasteUtils.doPaste;
import static org.eclipse.tigerstripe.workbench.utils.AdaptHelper.adapt;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.internal.ui.refactoring.reorg.PasteAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMember;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.IWorkbenchSite;

public class TSPasteAction extends PasteAction {

	private final Clipboard clipboard;
	private Object[] toCut;

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
						if (toCut != null) {
							for (Object obj : toCut) {
								try {
									if (obj instanceof IMember) {
										IMember member = (IMember) obj;
										IAbstractArtifact parent = member.getContainingArtifact();
										if (parent != null) {
											if (member instanceof IField) {
												parent.removeFields(singleton((IField)member));
											} else if (member instanceof IMethod) {
												parent.removeMethods(singleton((IMethod)member));
											} else if (member instanceof ILiteral) {
												parent.removeLiterals(singleton((ILiteral)member));
											}
										}
										parent.doSave(new NullProgressMonitor());
									}
								} catch (Exception e) {
									EclipsePlugin.log(e);
								}
							}
							toCut = null;
						}
						return;
					}
				}
			}
		}
		super.run(selection);
	}

	public void setMembersToCut(Object[] toCut) {
		this.toCut = toCut;
	}
}
