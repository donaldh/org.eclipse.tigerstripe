package org.eclipse.tigerstripe.annotation.internal.core;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;

public interface IAnnotationFilesRecognizer {
	
	public boolean isAnnotationFile(IFile file);

	public boolean couldContainAnnotationFiles(IContainer container);
}
