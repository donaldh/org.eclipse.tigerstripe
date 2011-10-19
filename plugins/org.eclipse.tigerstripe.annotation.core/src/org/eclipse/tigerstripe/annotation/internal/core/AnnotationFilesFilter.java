package org.eclipse.tigerstripe.annotation.internal.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class AnnotationFilesFilter {

	private IPath[] filters;

	public AnnotationFilesFilter(String[] sFilters) {
		List<IPath> filters = new ArrayList<IPath>();
		for (String sFilter : sFilters) {
			if (Path.ROOT.isValidPath(sFilter)) {
				filters.add(new Path(sFilter));
			}
		}
		this.filters = filters.toArray(new IPath[filters.size()]);
	}

	public boolean isAnnotationFile(IFile file) {
		// filters are project based, remove project segment
		IPath path = file.getFullPath();
		path = path.removeFirstSegments(1);
		for (IPath filter : filters) {
			if (filter.segmentCount() != path.segmentCount()) {
				continue;
			}
			boolean matches = true;
			for (int i = 0; i < path.segmentCount(); i++) {
				String regex = filter.segment(i).replace("?", ".?")
						.replace("*", ".*?");
				if (!path.segment(i).matches(regex)) {
					matches = false;
					break;
				}
			}

			if (matches) {
				return true;
			}
		}
		return false;
	}

	public boolean couldContainAnnotationFiles(IContainer container) {
		IPath path = container.getFullPath();
		path = path.removeFirstSegments(1);
		for (IPath filter : filters) {
			if (filter.segmentCount() <= path.segmentCount()) {
				continue;
			}
			return true;
		}
		return false;
	}
}
