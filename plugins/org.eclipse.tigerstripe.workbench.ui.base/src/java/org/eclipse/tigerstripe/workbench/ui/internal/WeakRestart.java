package org.eclipse.tigerstripe.workbench.ui.internal;

import static org.eclipse.tigerstripe.workbench.ui.internal.preferences.GeneralPreferencePage.P_WEAK_RESTART;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.tigerstripe.workbench.internal.api.patterns.PatternFactory;
import org.eclipse.tigerstripe.workbench.internal.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.profile.PhantomTigerstripeProjectMgr;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProjectFactory;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.ProfileEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.AbstractDiagramEditor;
import org.eclipse.tigerstripe.workbench.utils.AdaptHelper;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class WeakRestart {

	public static boolean isEnabled() {
		IPreferenceStore store = EclipsePlugin.getDefault()
				.getPreferenceStore();
		return store.getBoolean(P_WEAK_RESTART);
	}
	
	public static void restart(IProgressMonitor monitor) {
		if (!isEnabled()) {
			monitor.done();
			return;
		}
		
		try {
			TigerstripeProjectAuditor.setTurnedOffForImport(true);
			
			IProject[] projects = EclipsePlugin.getWorkspace().getRoot()
					.getProjects();

			monitor.beginTask("Restart", 10 + projects.length + 5);

			TigerstripeProjectFactory.INSTANCE.resetPhantomProject();
			PhantomTigerstripeProjectMgr.getInstance().reset();
			monitor.worked(10);
			
			for (IProject proj : projects) {
				try {
					if (!proj.isOpen()) {
						continue;
					}
					ITigerstripeModelProject mp = AdaptHelper.adapt(proj, ITigerstripeModelProject.class);
					if (mp == null) {
						continue;
					}
					try {
						ArtifactManager artifactManager = mp.getArtifactManagerSession().getArtifactManager();
						artifactManager.loadPhantomManager();
						artifactManager.refresh(true, null);
					} catch (Exception e) {
						EclipsePlugin.log(e);
					}
					
				} finally {
					monitor.worked(1);
				}
			}
	
			PatternFactory.reset();
			monitor.worked(5);
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				
				public void run() {
					IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
					for (IWorkbenchWindow window : windows) {
						IWorkbenchPage[] pages = window.getPages();
						for (IWorkbenchPage page : pages) {
							try {
								IEditorReference[] editorReferences = page.getEditorReferences();
								for (IEditorReference er : editorReferences) {
									IEditorPart editor = er.getEditor(false);
									if (editor instanceof ArtifactEditorBase
											|| editor instanceof AbstractDiagramEditor
											|| editor instanceof ProfileEditor) {
										IEditorInput input = editor.getEditorInput();
										page.closeEditor(editor, true);
										String id = er.getId();
										try {
											page.openEditor(input, id);
										} catch (PartInitException e) {
											EclipsePlugin.log(e);
										}
									}
								}
							} finally {
//								monitor.worked(1);
							}
						}
					}
				}
			});
		} catch (Exception e) {
			EclipsePlugin.log(e);
		} finally {
			TigerstripeProjectAuditor.setTurnedOffForImport(false);
		}
		restarted();
		monitor.done();
	}
	
	private static ListenerList activeProfileListeners = new ListenerList();

	private static void restarted() {
		for (Object obj : activeProfileListeners.getListeners()) {
			try {
				((Listener) obj).afterRestart();
			} catch (Throwable e) {
				EclipsePlugin.log(e);
			}
		}

		try {
			ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.CLEAN_BUILD, null);
		} catch(CoreException ex) {
			EclipsePlugin.log(ex);
		}
	}

	public static void addListener(Listener listener) {
		activeProfileListeners.add(listener);
	}

	public static void removeListener(Listener listener) {
		activeProfileListeners.remove(listener);
	}

	public static interface Listener {
		void afterRestart() throws Throwable;
	}
}
