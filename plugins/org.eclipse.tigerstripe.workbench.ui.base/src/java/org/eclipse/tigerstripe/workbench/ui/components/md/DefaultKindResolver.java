package org.eclipse.tigerstripe.workbench.ui.components.md;

/**
 * Default implementation of the resolver kind
 */
public class DefaultKindResolver implements IKindResolver {

	public Object toKind(Object object) {
		return object.getClass();
	}

}
