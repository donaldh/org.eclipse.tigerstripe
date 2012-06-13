package org.eclipse.tigerstripe.workbench.ui.internal;

import static org.eclipse.tigerstripe.workbench.ui.internal.preferences.GeneralPreferencePage.P_WEAK_RESTART;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.patterns.PatternFactory;
import org.eclipse.tigerstripe.workbench.internal.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
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

	public static WeakRestart INSTANCE = new WeakRestart();

	protected WeakRestart() {
	}

	public boolean isEnabled() {
		IPreferenceStore store = EclipsePlugin.getDefault()
				.getPreferenceStore();
		return store.getBoolean(P_WEAK_RESTART);
	}

	public void restart(final IProgressMonitor monitor) {
		if (!isEnabled()) {
			monitor.done();
			return;
		}

		// Reset model
		execute(monitor, new ArtifactManagerOperation("Reset model") {
			@Override
			protected void run(IProgressMonitor subMonitor,
					ArtifactManager artifactManager) {
				artifactManager.reset(subMonitor);
			}
		});

		// Rebuild and Reload model
		try {
			ResourcesPlugin.getWorkspace().build(
					IncrementalProjectBuilder.CLEAN_BUILD,
					new NullProgressMonitor() {
						@Override
						public void done() {
							reload(monitor);
							fireUpdated();
						}
					});
		} catch (CoreException ex) {
			EclipsePlugin.log(ex);
		}
	}

	private ListenerList activeProfileListeners = new ListenerList();

	protected void reload(IProgressMonitor monitor) {
		try {
			execute(monitor, new ArtifactManagerOperation("Reload model") {
				@Override
				protected void run(IProgressMonitor subMonitor,
						ArtifactManager artifactManager) {
					artifactManager.refresh(true, subMonitor);
				}
			});
		} finally {
			monitor.done();
		}
	}
	
	protected void reopenEditors(IWorkbenchPage page) {
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
			// monitor.worked(1);
		}
	}

	public void addListener(Listener listener) {
		activeProfileListeners.add(listener);
	}

	public void removeListener(Listener listener) {
		activeProfileListeners.remove(listener);
	}

	protected void fireUpdated() {
		// Close editors
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchWindow[] windows = PlatformUI.getWorkbench()
						.getWorkbenchWindows();
				for (IWorkbenchWindow window : windows) {
					IWorkbenchPage[] pages = window.getPages();
					for (IWorkbenchPage page : pages) {
						reopenEditors(page);
					}
				}
			}
		});

		final Object[] listeners = activeProfileListeners.getListeners();
		for (Object listener : listeners) {
			try {
				((Listener) listener).updated();
			} catch (Throwable e) {
				EclipsePlugin.log(e);
			}
		}
	}

	public interface Listener {
		void updated();
	}

	protected abstract class ArtifactManagerOperation {
		private String name;

		protected ArtifactManagerOperation(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		abstract protected void run(IProgressMonitor monitor,
				ArtifactManager artifactManager);
	}

	protected void execute(IProgressMonitor monitor,
			ArtifactManagerOperation operation) {
		try {
			TigerstripeProjectAuditor.setTurnedOffForImport(true);
			IProject[] projects = EclipsePlugin.getWorkspace().getRoot()
					.getProjects();

			monitor.beginTask(operation.getName(), 10 + projects.length + 5);

			TigerstripeProjectFactory.INSTANCE.resetPhantomProject();
			BasePlugin.getDefault().getPhantomTigerstripeProjectMgr().reset();
			monitor.worked(10);

			for (IProject proj : projects) {
				try {
					if (!proj.isOpen()) {
						continue;
					}
					ITigerstripeModelProject mp = AdaptHelper.adapt(proj,
							ITigerstripeModelProject.class);
					if (mp == null) {
						continue;
					}
					try {
						ArtifactManager artifactManager = mp
								.getArtifactManagerSession()
								.getArtifactManager();
						artifactManager.loadPhantomManager();
						operation.run(SubMonitor.convert(monitor),
								artifactManager);
					} catch (Exception e) {
						EclipsePlugin.log(e);
					}

				} finally {
					monitor.worked(1);
				}
			}

			PatternFactory.reset();
			monitor.worked(5);
		} catch (Exception e) {
			EclipsePlugin.log(e);
		} finally {
			TigerstripeProjectAuditor.setTurnedOffForImport(false);
		}
	}

}
