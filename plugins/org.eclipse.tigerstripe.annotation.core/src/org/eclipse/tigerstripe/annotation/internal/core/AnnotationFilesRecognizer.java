package org.eclipse.tigerstripe.annotation.internal.core;

import static org.eclipse.tigerstripe.annotation.core.AnnotationPlugin.PLUGIN_ID;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.internal.core.LazyProvider.Loader;

public class AnnotationFilesRecognizer implements IAnnotationFilesRecognizer {

	private static final String ANNOTATION_FILES_EXTPT = PLUGIN_ID + "."
			+ "annotationFiles";
	private static final String ANNOTATION_FILES_EXTPT_FILE_FILTER = "fileFilter";
	private static final String ANNOTATION_FILES_EXTPT_FILE_FILTER_ATT_VALUE = "value";

	private final LazyProvider<List<AnnotationFilesFilter>> annotationFiles = new LazyProvider<List<AnnotationFilesFilter>>(
			new Loader<List<AnnotationFilesFilter>>() {

				public List<AnnotationFilesFilter> load() {
					return createFilters();
				}
			});

	public boolean isAnnotationFile(IFile file) {
		for (AnnotationFilesFilter filter : annotationFiles.get()) {
			if (filter.isAnnotationFile(file)) {
				return true;
			}
		}
		return false;
	}

	public boolean couldContainAnnotationFiles(IContainer container) {
		for (AnnotationFilesFilter filter : annotationFiles.get()) {
			if (filter.couldContainAnnotationFiles(container)) {
				return true;
			}
		}
		return false;
	}

	private List<AnnotationFilesFilter> createFilters() {
		List<AnnotationFilesFilter> result = new ArrayList<AnnotationFilesFilter>();
		IConfigurationElement[] configs = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(ANNOTATION_FILES_EXTPT);
		for (IConfigurationElement config : configs) {
			try {
				List<String> filters = new ArrayList<String>();
				if (ANNOTATION_FILES_EXTPT_FILE_FILTER.equals(config.getName())) {
					String val = config
							.getAttribute(ANNOTATION_FILES_EXTPT_FILE_FILTER_ATT_VALUE);
					if (val != null) {
						filters.add(val);
					}
				}
				if (filters.size() > 0) {
					AnnotationFilesFilter filesFilter = new AnnotationFilesFilter(
							filters.toArray(new String[filters.size()]));
					result.add(filesFilter);
				}
			} catch (Exception e) {
				AnnotationPlugin.log(e);
			}
		}
		return result;
	}
}
