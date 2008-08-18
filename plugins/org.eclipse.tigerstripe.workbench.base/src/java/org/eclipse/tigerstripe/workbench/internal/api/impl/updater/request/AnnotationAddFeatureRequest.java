/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request;

import java.util.Collection;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAnnotationAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.annotation.AnnotationHelper;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;

public class AnnotationAddFeatureRequest extends BaseArtifactElementRequest
		implements IAnnotationAddFeatureRequest {

	private String annotationClass;
	private String namespaceURI;
	private URI fileURI;
	private String target;
	
	public void setNamespaceURI(String namespaceURI) {
		this.namespaceURI = namespaceURI;
	}


	public void setAnnotationClass(String annotationClass) {
		this.annotationClass = annotationClass;
	}

	/**
	 * This should be the full path name
	 */
	public void setFileURI(URI fileURI) {
		this.fileURI = fileURI;
	}

	/**
	 * The target is the part of the uri AFTER the '#'
	 * ie the field, method, end etc
	 * It should be blank if the target is the artifact itself
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		super.canExecute(mgrSession);
		try {
			IAbstractArtifact art = mgrSession
					.getArtifactByFullyQualifiedName(getArtifactFQN());
			// TODO Make sure we can find the target Model component
			if (art != null){
				if (target.length() == 0){
					// This is the artifact itself;
					return true;
				} else {
					boolean isMethod = target.contains("(");
					boolean isEnd = target.contains(";");
					if (isMethod){
						Collection<IMethod> methods = art.getMethods();
						for (IMethod method : methods ){
							if (method.getLabelString().equals(target)){
								return true;
							}		
						}
						return false;
					} else if (isEnd){
						if (!(art instanceof AssociationArtifact))
							return false;
						String[] bits = target.split(";");
						if (bits.length != 2)
							return false;
						String endName = bits[1];
						if ( "aEnd".equalsIgnoreCase(endName) || "zEnd".equalsIgnoreCase(endName)){
							return true;
						} else 
							return false;
						
					} else {
						// either a field or literal - I can't tell.
						Collection<IField> fields = art.getFields();
						for (IField field : fields ){
							if (field.getName().equals(target)){
								return true;
							}
						}
						Collection<ILiteral> literals = art.getLiterals();
						for (ILiteral literal : literals ){
							if (literal.getName().equals(target)){
								return true;
							}
						}
						return false;
							
					}
				}			
			} else {
				return false;
			}
			
		} catch (TigerstripeException t) {
			return false;
		}
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
	throws TigerstripeException {
		super.execute(mgrSession);

		IModelComponent modelComponent = null;
		IAbstractArtifact art = mgrSession
		.getArtifactByFullyQualifiedName(getArtifactFQN());

		// TODO Make sure we can find the target Model component
		if (art != null){
			if (target.length() == 0){
				modelComponent = art;
			} else {
				boolean isMethod = target.contains("(");
				boolean isEnd = target.contains(";");
				if (isMethod){
					Collection<IMethod> methods = art.getMethods();
					for (IMethod method : methods ){
						if (method.getLabelString().equals(target)){
							modelComponent = method;
						}		
					}

				} else if (isEnd){
					if (art instanceof AssociationArtifact){
						AssociationArtifact assoc = (AssociationArtifact) art;
						String[] bits = target.split(";");
						String endName = bits[1];
						if ( "aEnd".equalsIgnoreCase(endName)){
							modelComponent = assoc.getAEnd();
						}else if ("zEnd".equalsIgnoreCase(endName)){
							modelComponent = assoc.getZEnd();
						} 
					}
				} else {
					// either a field or literal - I can't tell.
					Collection<IField> fields = art.getFields();
					for (IField field : fields ){
						if (field.getName().equals(target)){
							modelComponent =  field;
						}
					}
					Collection<ILiteral> literals = art.getLiterals();
					for (ILiteral literal : literals ){
						if (literal.getName().equals(target)){
							modelComponent =  literal;
						}
					}

				}
			}			
			if ( modelComponent != null){
				addAnnotationToComponent(modelComponent);
				art.doSave(null);
			}
		}

	}

	private void addAnnotationToComponent(IModelComponent modelComponent) throws TigerstripeException{
		AnnotationHelper helper = AnnotationHelper.getInstance();
		
		
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().
			put("anno", new XMIResourceFactoryImpl());
	

		try {
			Annotation anno = helper.addAnnotation(modelComponent, Util.packageOf(this.annotationClass), Util.nameOf(this.annotationClass));
			// Now add the content
			Resource resource = resourceSet.createResource(this.fileURI);
			resource.load(null);
			EObject eo = resource.getContents().get(0);
			anno.setContent(eo);
			AnnotationHelper.getInstance().saveAnnotation(anno);
			//modelComponent.saveAnnotation(anno);
		} catch (Exception e){
			e.printStackTrace();
			throw new TigerstripeException("Exception adding annotation to component",e);
		}
		
	}
	
	public static EObject createObject( String packageNSURI, String className){
		EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage(packageNSURI);
		EClass eClass = (EClass) ePackage.getEClassifier(className);
		EFactory eFactory = ePackage.getEFactoryInstance();
		EObject eObject = eFactory.create(eClass);
		return eObject;
	}


	public IModelChangeDelta getCorrespondingDelta() {
		ModelChangeDelta delta = new ModelChangeDelta(IModelChangeDelta.ADD);

		try {
			IModelComponent comp = getMgrSession()
					.getArtifactByFullyQualifiedName(getArtifactFQN());
			delta
					.setAffectedModelComponentURI((URI) comp
							.getAdapter(URI.class));

			delta.setFeature(IAnnotationAddFeatureRequest.ANNOTATION_FEATURE);
			delta.setNewValue(annotationClass);
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
			return ModelChangeDelta.UNKNOWNDELTA;
		}

		return delta;
	}


}
