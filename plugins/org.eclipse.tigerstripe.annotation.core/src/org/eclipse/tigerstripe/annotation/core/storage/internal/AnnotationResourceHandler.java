package org.eclipse.tigerstripe.annotation.core.storage.internal;

import static org.eclipse.tigerstripe.annotation.core.AnnotationPlugin.PLUGIN_ID;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.BasicResourceHandler;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationResourceProcessor;
import org.eclipse.tigerstripe.annotation.internal.core.LazyProvider;

public class AnnotationResourceHandler extends BasicResourceHandler {

	private static final String EXTPT_PREFIX = PLUGIN_ID + ".";
	private static final String ANNOTATION_RESOURCE_PROCESSOR_EXT_POINT = EXTPT_PREFIX
			+ "resourceProcessor";
	private static final String PROCESSOR_ATTR_CLASS = "class";

	private static AnnotationResourceHandler theInstance;

	private final LazyProvider<List<IAnnotationResourceProcessor>> processors = new LazyProvider<List<IAnnotationResourceProcessor>>(
			new LazyProvider.Loader<List<IAnnotationResourceProcessor>>() {

				public List<IAnnotationResourceProcessor> load() {
					return loadProcessors();
				}
			});

	private AnnotationResourceHandler() {
	}

	public static AnnotationResourceHandler getInstance() {
		if (theInstance == null) {
			theInstance = new AnnotationResourceHandler();
		}
		return theInstance;
	}

	@Override
	public void postLoad(XMLResource resource, InputStream inputStream,
			Map<?, ?> options) {
		for (IAnnotationResourceProcessor processor : getProcessors()) {
			processor.postLoad(resource);
		}
		super.postLoad(resource, inputStream, options);
	}

	@Override
	public void preSave(XMLResource resource, OutputStream outputStream,
			Map<?, ?> options) {
		for (IAnnotationResourceProcessor processor : getProcessors()) {
			processor.preSave(resource);
		}
		super.preSave(resource, outputStream, options);
	}

	@Override
	public void postSave(XMLResource resource, OutputStream outputStream,
			Map<?, ?> options) {
		for (IAnnotationResourceProcessor processor : getProcessors()) {
			processor.postSave(resource);
		}
		super.postSave(resource, outputStream, options);
	}

	protected List<IAnnotationResourceProcessor> loadProcessors() {
		List<IAnnotationResourceProcessor> processors = new ArrayList<IAnnotationResourceProcessor>();
		IConfigurationElement[] configs = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(
						ANNOTATION_RESOURCE_PROCESSOR_EXT_POINT);
		for (IConfigurationElement config : configs) {
			try {
				IAnnotationResourceProcessor processor = (IAnnotationResourceProcessor) config
						.createExecutableExtension(PROCESSOR_ATTR_CLASS);
				processors.add(processor);
			} catch (Exception e) {
				AnnotationPlugin.log(e);
			}
		}
		return processors;
	}

	public List<IAnnotationResourceProcessor> getProcessors() {
		return processors.get();
	}
}
