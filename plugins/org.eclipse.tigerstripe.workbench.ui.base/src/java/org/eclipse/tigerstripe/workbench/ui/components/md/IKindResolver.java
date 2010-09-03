package org.eclipse.tigerstripe.workbench.ui.components.md;

/**
 * Resolve the kind of object																																					
 */
public interface IKindResolver {

	/**											
	 * @param object to kind resolve
	 * @return kind of object
	 */
	Object toKind(Object object);

}
