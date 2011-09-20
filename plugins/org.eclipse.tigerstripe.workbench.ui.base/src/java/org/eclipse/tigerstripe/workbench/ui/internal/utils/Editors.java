package org.eclipse.tigerstripe.workbench.ui.internal.utils;

import org.eclipse.core.resources.IResource;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class Editors {

	public static IEditorPart findForResource(IResource resource) {
		for (IWorkbenchWindow w : PlatformUI.getWorkbench()
				.getWorkbenchWindows()) {
			for (IWorkbenchPage page : w.getPages()) {
				for (IEditorReference ref : page.getEditorReferences()) {
					IEditorInput input;
					try {
						input = ref.getEditorInput();
					} catch (PartInitException e) {
						BasePlugin.log(e);
						continue;
					}
					if (input instanceof IFileEditorInput) {
						if (resource.equals(((IFileEditorInput) input).getFile())) {
							return ref.getEditor(true);
						}
					}
				}
			}
		}
		return null;
	}
}
