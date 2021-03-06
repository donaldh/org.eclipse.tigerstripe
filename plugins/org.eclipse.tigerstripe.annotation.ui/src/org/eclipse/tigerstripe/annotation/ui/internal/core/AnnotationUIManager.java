/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.ui.internal.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.util.AnnotationUtils;
import org.eclipse.tigerstripe.annotation.core.util.ClassName;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;
import org.eclipse.tigerstripe.annotation.ui.core.IAnnotationUIManager;
import org.eclipse.tigerstripe.annotation.ui.core.ISelectionConverter;
import org.eclipse.tigerstripe.annotation.ui.core.ISelectionFilter;
import org.eclipse.tigerstripe.annotation.ui.util.WorkbenchUtil;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Yuri Strot
 * 
 */
public class AnnotationUIManager implements IAnnotationUIManager {

	private static final String WORKBENCH_ANNOTATION_PROVIDER_EXTPT = "org.eclipse.tigerstripe.annotation.ui.workbenchAnnotationProvider";

	private static final String ANNOTATION_LABEL_PROVIDER = "org.eclipse.tigerstripe.annotation.ui.annotationLabelProvider";

	private static final String CONVERTOR_NAME = "selectionConverter";
	private static final String ATTR_CLASS = "class";
	private static final String ATTR_ECLASS = "eclass";
	private static final String ATTR_EPACKAGE = "epackage-uri";

	private static AnnotationUIManager instance;

	private AnnotationSelectionListener selectionListener;

	private ISelectionConverter[] converters;
	private Map<AnnotationType, ILabelProvider> providers;

	private AnnotationUIManager() {
	}

	public static AnnotationUIManager getInstance() {
		if (instance == null)
			instance = new AnnotationUIManager();
		return instance;
	}

	public void addSelectionListener(ISelectionListener listener) {
		if (selectionListener == null)
			loadProviders();
		selectionListener.addListener(listener);
	}

	public void removeSelectionListener(ISelectionListener listener) {
		if (selectionListener == null)
			loadProviders();
		selectionListener.removeListener(listener);
	}

	public ISelection getSelection() {
		if (selectionListener == null)
			loadProviders();
		ISelection sel = selectionListener.getSelection();
		if (sel == null)
			sel = new StructuredSelection();
		return sel;
	}

	public ILabelProvider getLabelProvider(AnnotationType type) {
		return getProviderMap().get(type);
	}

	protected Map<AnnotationType, ILabelProvider> getProviderMap() {
		if (providers == null) {
			providers = new HashMap<AnnotationType, ILabelProvider>();
			IConfigurationElement[] configs = Platform.getExtensionRegistry()
					.getConfigurationElementsFor(ANNOTATION_LABEL_PROVIDER);
			for (IConfigurationElement config : configs) {
				try {
					ILabelProvider provider = (ILabelProvider) config
							.createExecutableExtension(ATTR_CLASS);
					String epackageUri = config.getAttribute(ATTR_EPACKAGE);
					String eclass = config.getAttribute(ATTR_ECLASS);
					EClass clazz = AnnotationUtils
							.getClass(epackageUri, eclass);
					ClassName name = AnnotationUtils
							.getInstanceClassName(clazz);
					AnnotationType type = AnnotationPlugin.getManager()
							.getType(name.getPackage(), name.getClazz());
					if (type == null)
						throw new NullPointerException(
								"Annotation type with epackage-uri="
										+ epackageUri + " and eclass=" + eclass
										+ " do not defined");
					providers.put(type, provider);
				} catch (Exception e) {
					AnnotationUIPlugin.log(e);
				}
			}
		}
		return providers;
	}

	public void addSelectionFilter(ISelectionFilter filter) {
		if (selectionListener == null)
			loadProviders();
		selectionListener.getFilter().addFilter(filter);
	}

	public void removeSelectionFilter(ISelectionFilter filter) {
		if (selectionListener == null)
			loadProviders();
		selectionListener.getFilter().removeFilter(filter);
	}

	protected void loadProviders() {
		List<ISelectionConverter> converters = new ArrayList<ISelectionConverter>();

		IConfigurationElement[] configs = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(
						WORKBENCH_ANNOTATION_PROVIDER_EXTPT);
		for (IConfigurationElement config : configs) {
			try {
				String name = config.getName();
				if (CONVERTOR_NAME.equals(name)) {
					ISelectionConverter converter = (ISelectionConverter) config
							.createExecutableExtension(ATTR_CLASS);
					converters.add(converter);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		this.converters = converters.toArray(new ISelectionConverter[converters
				.size()]);
		selectionListener = new AnnotationSelectionListener(this.converters);
		WorkbenchUtil.getWindow().getSelectionService()
				.addPostSelectionListener(selectionListener);
	}

	public ISelection convertSelection(IWorkbenchPart part, ISelection selection) {
		if (selectionListener == null)
			loadProviders();
		return selectionListener.convertSelection(part, selection);
	}

	public void open(Object object) {
		if (converters == null)
			loadProviders();
		ISelection selection = new StructuredSelection(object);
		for (int i = 0; i < converters.length; i++) {
			try {
				converters[i].open(selection);
			} catch (Exception e) {
				AnnotationUIPlugin.log(e);
			}
		}
	}

}
