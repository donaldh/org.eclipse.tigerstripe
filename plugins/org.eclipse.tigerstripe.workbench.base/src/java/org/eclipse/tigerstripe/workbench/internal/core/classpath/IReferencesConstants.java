package org.eclipse.tigerstripe.workbench.internal.core.classpath;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * This interface represent constants for model project references
 */
public interface IReferencesConstants {

	/**
	 * Classpth container path which identify model project references container
	 */
	public static final IPath REFERENCES_CONTAINER_PATH = new Path(
			"org.eclipse.tigerstripe.workbench.base.modelReferences"); //$NON-NLS-1$

	public static final String CONTAINER_DESCRIPTION = "Model Project References";

}
