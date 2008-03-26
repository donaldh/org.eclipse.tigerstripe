package org.eclipse.tigerstripe.workbench.ui.base.test.editors.helper;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.generator.GeneratorDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.PluginDescriptorEditor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class EditorHelper {

	public static IEditorPart getEditorPartForArtifact(String fqn, IProject project)
			throws Exception {
		IWorkbenchWindow windows[] = PlatformUI.getWorkbench()
				.getWorkbenchWindows();
		for (int i = 0; i < windows.length; i++) {
			IWorkbenchPage pages[] = windows[i].getPages();
			for (int j = 0; j < pages.length; j++) {
				IEditorReference[] refs = pages[j].getEditorReferences();
				for (IEditorReference ref : refs) {
					IEditorPart part = ref.getEditor(false);
					if (part instanceof ArtifactEditorBase) {
						ArtifactEditorBase base = (ArtifactEditorBase) part;
						IEditorInput input = base.getEditorInput();
						if (input instanceof IFileEditorInput) {
							IFileEditorInput fInput = (IFileEditorInput) input;
							IFile file = fInput.getFile();
							if (file.getFullPath().isValidSegment(
									fqn.replace(".", "/"))) {
								return part;
							}
						}
					}
				}
			}
		}
		return null;
	}

	public static IEditorPart getEditorPartForPluginDescriptor(IProject project)
	throws Exception {
		IWorkbenchWindow windows[] = PlatformUI.getWorkbench()
		.getWorkbenchWindows();
		for (int i = 0; i < windows.length; i++) {
			IWorkbenchPage pages[] = windows[i].getPages();
			for (int j = 0; j < pages.length; j++) {
				IEditorReference[] refs = pages[j].getEditorReferences();
				for (IEditorReference ref : refs) {
					IEditorPart part = ref.getEditor(false);
					if (part instanceof PluginDescriptorEditor) {
						PluginDescriptorEditor base = (PluginDescriptorEditor) part;
						IEditorInput input = base.getEditorInput();
						if (input instanceof IFileEditorInput) {
							IFileEditorInput fInput = (IFileEditorInput) input;
							IFile file = fInput.getFile();
							if (file.getFullPath().isValidSegment(
									"ts-plugin.xml")) {
								return part;
							}
						}
					}
				}
			}
		}
		return null;
	}
}
