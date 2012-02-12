package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class WrapHelper {

	public static <T extends IModelComponent> List<T> wrapList(
			ITigerstripeModelProject context, List<T> artifacts) {
		List<T> result = new ArrayList<T>(artifacts.size());
		for (T art : artifacts) {
			result.add(wrap(context, art));
		}
		return result;
	}

	public static <T extends IModelComponent> Collection<T> wrapCollection(
			ITigerstripeModelProject context, Collection<T> artifacts) {
		Collection<T> result = new ArrayList<T>(artifacts.size());
		for (T art : artifacts) {
			result.add(wrap(context, art));
		}
		return result;
	}
	
	public static <T extends IModelComponent> Set<T> wrapSet(
			ITigerstripeModelProject context, Set<T> artifacts) {
		Set<T> result = new HashSet<T>(artifacts.size());
		for (T art : artifacts) {
			result.add(wrap(context, art));
		}
		return result;
	}

	public static <T extends IModelComponent> T wrap(
			ITigerstripeModelProject context, T art) {
		if (art == null) {
			return null;
		}
		return ContextProjectAwareProxy
				.newInstanceOrChangeContext(art, context);
	}
}
