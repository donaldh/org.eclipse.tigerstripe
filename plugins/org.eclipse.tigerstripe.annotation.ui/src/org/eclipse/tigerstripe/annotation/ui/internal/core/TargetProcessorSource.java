package org.eclipse.tigerstripe.annotation.ui.internal.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;
import org.eclipse.tigerstripe.annotation.ui.core.ITargetProcessor;

public class TargetProcessorSource {

	public static Collection<ITargetProcessor> getProcessors() {
		final IConfigurationElement[] elements = Platform
				.getExtensionRegistry()
				.getConfigurationElementsFor(
						"org.eclipse.tigerstripe.annotation.ui.targetProcessor");

		List<ITargetProcessor> result = new ArrayList<ITargetProcessor>(
				elements.length);
		for (IConfigurationElement el : elements) {
			try {
				ITargetProcessor processor = (ITargetProcessor) el
						.createExecutableExtension("class");
				result.add(processor);
			} catch (CoreException e) {
				AnnotationUIPlugin.log(e);
			}
		}
		return result;
	}
}
