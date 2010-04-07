package org.eclipse.tigerstripe.workbench.internal.core.classpath;

import java.util.ArrayList;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.module.InstalledModule;
import org.eclipse.tigerstripe.workbench.internal.core.module.InstalledModuleManager;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * This class represent classpath container for model project references
 */
public class ReferencesClasspathContainer implements IClasspathContainer {

	private ITigerstripeModelProject project;
	private IClasspathEntry[] entries;

	public ReferencesClasspathContainer(ITigerstripeModelProject project) {
		this.project = project;
	}

	public IClasspathEntry[] getClasspathEntries() {
		if (entries == null) {
			entries = computeEntries();
		}
		return entries;
	}

	private IClasspathEntry[] computeEntries() {
		ArrayList<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		try {
			ModelReference[] references = project.getModelReferences();
			for (ModelReference reference : references) {
				ITigerstripeModelProject model = reference.getResolvedModel();
				if (reference.isWorkspaceReference()) {
					// workspace project
					IClasspathEntry entry = JavaCore.newProjectEntry(model
							.getFullPath());
					entries.add(entry);
				} else if (reference.isInstalledModuleReference()) {
					// installed module
					String id = reference.getToModelId();
					InstalledModule module = InstalledModuleManager
							.getInstance().getModule(id);
					if (module != null) {
						IPath path = module.getPath();
						IClasspathEntry entry = JavaCore.newLibraryEntry(path,
								null, null);
						entries.add(entry);
					}
				}
			}
		} catch (TigerstripeException e) {
		}
		return entries.toArray(new IClasspathEntry[entries.size()]);
	}

	public String getDescription() {
		return IReferencesConstants.CONTAINER_DESCRIPTION;
	}

	public int getKind() {
		return K_APPLICATION;
	}

	public IPath getPath() {
		return IReferencesConstants.REFERENCES_CONTAINER_PATH;
	}

}
