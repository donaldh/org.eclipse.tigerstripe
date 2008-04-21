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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;
import org.eclipse.tigerstripe.annotation.ui.core.CompositeRefactorListener;
import org.eclipse.tigerstripe.annotation.ui.core.IAnnotationUIManager;
import org.eclipse.tigerstripe.annotation.ui.core.IRefactoringListener;
import org.eclipse.tigerstripe.annotation.ui.core.IRefactoringSupport;
import org.eclipse.tigerstripe.annotation.ui.core.ISelectionConverter;
import org.eclipse.tigerstripe.annotation.ui.core.ISelectionFilter;
import org.eclipse.tigerstripe.annotation.ui.util.WorkbenchUtil;
import org.eclipse.ui.ISelectionListener;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationUIManager implements IAnnotationUIManager, IRefactoringListener {
	
	private static final String WORKBENCH_ANNOTATION_PROVIDER_EXTPT	=
		"org.eclipse.tigerstripe.annotation.ui.workbenchAnnotationProvider";
	
	private static final String ANNOTATION_PROVIDER_IMAGES_EXTPT =
		"org.eclipse.tigerstripe.annotation.ui.annotationTypeImages";
	
	private static final String REFACTORING_NAME = "refactoringSupport";
	private static final String CONVERTOR_NAME = "selectionConverter";
	private static final String ATTR_CLASS = "class";
	private static final String ATTR_ICON = "icon";
	private static final String ATTR_TYPE_ID = "typeID";
	
	private static AnnotationUIManager instance;
	
	private AnnotationSelectionListener selectionListener;
	private CompositeRefactorListener refactorListener;

	private ISelectionConverter[] converters;
	private Map<String, ImageDescriptor> descriptors = new HashMap<String, ImageDescriptor>();
	
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
	
	public ImageDescriptor getImage(AnnotationType type) {
		return getImageMap().get(type.getId());
	}
	
	protected Map<String, ImageDescriptor> getImageMap() {
		if (descriptors == null) {
			IConfigurationElement[] configs = Platform.getExtensionRegistry(
    			).getConfigurationElementsFor(ANNOTATION_PROVIDER_IMAGES_EXTPT);
            for (IConfigurationElement config : configs) {
            	try {
            		String path = config.getAttribute(ATTR_ICON);
            		String typeID = config.getAttribute(ATTR_TYPE_ID);
            		
            		String id = config.getDeclaringExtension().getNamespaceIdentifier();
            		URL url = Platform.getBundle(id).getEntry(path);
            		descriptors.put(typeID, ImageDescriptor.createFromURL(url));
                }
                catch (Exception e) {
                	AnnotationUIPlugin.log(e);
                }
            }
		}
		return descriptors;
	}
	
	public void addRefactoringListener(IRefactoringListener listener) {
		if (refactorListener == null)
			loadProviders();
		refactorListener.addListener(listener);
	}
	
	public void removeRefactoringListener(IRefactoringListener listener) {
		if (refactorListener == null)
			loadProviders();
		refactorListener.removeListener(listener);
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
        refactorListener = new CompositeRefactorListener();
		
		IConfigurationElement[] configs = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(WORKBENCH_ANNOTATION_PROVIDER_EXTPT);
        for (IConfigurationElement config : configs) {
        	try {
        		String name = config.getName();
        		if (REFACTORING_NAME.equals(name)) {
    	            IRefactoringSupport provider = 
    	            	(IRefactoringSupport)config.createExecutableExtension(ATTR_CLASS);
    	            provider.addRefactoringListener(refactorListener);
        		}
        		else if (CONVERTOR_NAME.equals(name)) {
        			ISelectionConverter converter = 
        				(ISelectionConverter)config.createExecutableExtension(ATTR_CLASS);
        			converters.add(converter);
        		}
            }
            catch (CoreException e) {
	            e.printStackTrace();
            }
        }
        this.converters = converters.toArray(new ISelectionConverter[converters.size()]);
        selectionListener = new AnnotationSelectionListener(this.converters);
        refactorListener.addListener(this);
        WorkbenchUtil.getWindow().getSelectionService().addPostSelectionListener(selectionListener);
	}

	public void containerUpdated() {
    }

	public void refactoringPerformed(Map<URI, URI> changes) {
		Iterator<URI> keys = changes.keySet().iterator();
		while (keys.hasNext()) {
	        URI newUri = (URI) keys.next();
	        URI oldUri = changes.get(newUri);
	        AnnotationPlugin.getManager().setUri(newUri, oldUri);
        }
    }

	public void open(Object object) {
		if (converters == null)
			loadProviders();
		ISelection selection = new StructuredSelection(object);
		for (int i = 0; i < converters.length; i++) {
			converters[i].open(selection);
        }
    }

}
