package org.eclipse.tigerstripe.annotation.internal.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationRuntimeException;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.internal.core.LazyProvider.Loader;
import org.eclipse.tigerstripe.espace.resources.core.EObjectRouter;

public class MasterRouter implements EObjectRouter {

	private final LazyProvider<List<EObjectRouter>> routers = new LazyProvider<List<EObjectRouter>>(new Loader<List<EObjectRouter>>() {

		public List<EObjectRouter> load() {
			return createRouters();
		}
	});
	
	private List<EObjectRouter> createRouters() {
		IConfigurationElement[] configs = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(AnnotationManager.ROUTER_EXTPT);
		List<EObjectRouter> routers = new ArrayList<EObjectRouter>();
		for (IConfigurationElement config : configs) {
			try {
				EObjectRouter provider = (EObjectRouter) config
						.createExecutableExtension("class");
				routers.add(provider);
			} catch (CoreException e) {
				AnnotationPlugin.log(e);
			}
		}
		return Collections.unmodifiableList(routers);
	}

	
	public IResource route(Annotation ann) {
		for (EObjectRouter router : getRouters()) {
			IResource path = router.route(ann);
			if (path != null) {
				return path;
			}
		}
		if (ann != null) {
			Object annotable = AnnotationPlugin.getManager()
					.getAnnotatedObject(ann);

			if (annotable != null) {
				if (annotable instanceof IAdaptable) {
					IResource resource = (IResource) ((IAdaptable) annotable)
							.getAdapter(IResource.class);
					if (resource != null) {

						IProject project = resource.getProject();

						if (project != null && project.isOpen()) {
							IPath path = new Path("annotations")
									.addFileExtension(IAnnotationManager.ANNOTATION_FILE_EXTENSION);
							return project.getFile(path);
						}
					}
				}
			}
		}
		
		throw new AnnotationRuntimeException("Unable to find save path for annotation "+ann);
	}

	public List<EObjectRouter> getRouters() {
		return routers.get();
	}
}
