package org.eclipse.tigerstripe.workbench.ui.internal.annotation.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.annotation.properties.ModelReferenceCellEditor;

public class ModelReferenceEditorRegistry {
	private static final String EXT_ID = EclipsePlugin.PLUGIN_ID + "."
			+ "modelReferenceEditor";

	private static final String ATTR_ANN_PACKAGE = "annotationPackage";
	private static final String ATTR_ANN_CLASS = "annotationClass";
	private static final String ATTR_ANN_ATTR = "annotationAttribute";
	private static final String ATTR_CELL_EDITOR_CLASS = "cellEditor";

	private static final String ELEM_VISIBLE = "visible";
	private static final String ELEM_VALID = "valid";
	private static final String ATTR_INTERFACE = "interface";
	
	private static final String ATTR_TITLE = "title";
	private static final String ELEM_MESSAGE = "message";

	private static class Contribution {
		public final String annotationPackage;
		public final String annotationClass;
		public final String annotationAttr;

		public final List<Class<?>> visible;
		public final List<Class<?>> valid;
		
		public final String title;
		public final String message;
		
		private final IConfigurationElement conf;

		public Contribution(final IConfigurationElement conf) {
			this.annotationPackage = conf.getAttribute(ATTR_ANN_PACKAGE);
			this.annotationClass = conf.getAttribute(ATTR_ANN_CLASS);
			this.annotationAttr = conf.getAttribute(ATTR_ANN_ATTR);
			this.conf = conf;
			
			this.title = conf.getAttribute(ATTR_TITLE);
			
			final IConfigurationElement[] messages = conf.getChildren(ELEM_MESSAGE);
			if(messages.length > 0) {
				message = messages[0].getValue(); 
			} else {
				message = null;
			}

			final List<Class<?>> visible = new ArrayList<Class<?>>();
			for(final IConfigurationElement v : conf.getChildren(ELEM_VISIBLE)) {
				final Class<?> clazz = loadClass(v.getAttribute(ATTR_INTERFACE));
				if(clazz != null) {
					visible.add(clazz);
				}
			}
			this.visible = Collections.unmodifiableList(visible);

			final List<Class<?>> valid = new ArrayList<Class<?>>();
			for(final IConfigurationElement v : conf.getChildren(ELEM_VALID)) {
				final Class<?> clazz = loadClass(v.getAttribute(ATTR_INTERFACE));
				if(clazz != null) {
					valid.add(clazz);
				}
			}
			this.valid = Collections.unmodifiableList(valid);
		}

		private Class<?> loadClass(String attribute) {
			try {
				return this.getClass().getClassLoader().loadClass(attribute);
			} catch (ClassNotFoundException cnfe) {
				EclipsePlugin.log(cnfe);
				return null;
			} catch(LinkageError le) {
				EclipsePlugin.log(le);
				return null;
			}
		}

		public ModelReferenceCellEditor createEditor() throws CoreException {
			final ModelReferenceCellEditor editor;
			if(conf.getAttribute(ATTR_CELL_EDITOR_CLASS) != null || conf.getChildren(ATTR_CELL_EDITOR_CLASS).length > 0) {
				editor = (ModelReferenceCellEditor) this.conf
						.createExecutableExtension(ATTR_CELL_EDITOR_CLASS);
			} else {
				editor = new ModelReferenceCellEditor();
			}
			
			if(title != null) {
				editor.setTitle(title);
			}
			if(message != null) {
				editor.setMessage(message);
			}
			if(!visible.isEmpty()) {
				editor.setVisibleClasses(visible);
			}
			if(!valid.isEmpty()) {
				editor.setValidClasses(valid);
			}
			return editor;
		}
	}

	// lazy by class loading semantics
	private static class Holder {
		private static final ModelReferenceEditorRegistry INSTANCE = new ModelReferenceEditorRegistry();
	}

	public static ModelReferenceEditorRegistry getInstance() {
		return Holder.INSTANCE;
	}

	private final List<Contribution> contributions;

	private ModelReferenceEditorRegistry() {
		final List<Contribution> c = new ArrayList<Contribution>();
		for (final IConfigurationElement conf : Platform.getExtensionRegistry()
				.getConfigurationElementsFor(EXT_ID)) {
			c.add(new Contribution(conf));
		}
		contributions = Collections.unmodifiableList(c);
	}

	public boolean getApplicable(final EClass eclass,
			final EStructuralFeature feature) {
		return find(eclass, feature) != null;
	}

	public ModelReferenceCellEditor createCellEditor(final EClass eclass,
			final EStructuralFeature feature) throws CoreException {
		Contribution contrib = find(eclass, feature);
		if (contrib == null) {
			throw new IllegalArgumentException(
					"createCellEditor() called where applicable() is false");
		}
		return contrib.createEditor();
	}

	private Contribution find(final EClass eclass,
			final EStructuralFeature feature) {
		for (final Contribution contrib : contributions) {
			if (contrib.annotationPackage != null
					&& !contrib.annotationPackage.isEmpty()
					&& !contrib.annotationPackage.equals(eclass.getEPackage()
							.getNsURI())) {
				return null;
			}

			if (contrib.annotationClass.equals(eclass.getName())
					&& contrib.annotationAttr.equals(feature.getName())) {
				return contrib;
			}
		}

		return null;
	}
}
