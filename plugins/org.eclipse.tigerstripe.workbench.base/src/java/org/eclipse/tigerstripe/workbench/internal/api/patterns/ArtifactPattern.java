/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.api.patterns;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.ModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.ArtifactCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.ArtifactDeleteRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.ArtifactLinkCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.BaseArtifactElementRequest;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.MethodCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAnnotationAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeSetRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.ILiteralSetRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodSetRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IStereotypeAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.xml.TigerstripeXMLParserUtils;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.annotation.AnnotationHelper;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.patterns.IArtifactPattern;
import org.eclipse.tigerstripe.workbench.patterns.IArtifactPatternResult;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.w3c.dom.Element;

public abstract class ArtifactPattern extends Pattern implements IArtifactPattern  {

	protected Element artifactElement;
	protected TigerstripeXMLParserUtils xmlParserUtils;
	protected String artifactType;
	protected String artifactName = "";
	protected String extendedArtifactName = "";
	
	private IAbstractArtifact artifact;
	private AnnotationHelper helper = AnnotationHelper.getInstance();
	
	public void setParserUtils(TigerstripeXMLParserUtils utils) {
		this.xmlParserUtils = utils;
	}

	public Element getElement() {
		return artifactElement;
	}

	public void setElement(Element artifactElement) {
		this.artifactElement = artifactElement;
		
		// We need to do a few extractions for wizard re-use
		String artifactType = xmlParserUtils.getArtifactType(this.artifactElement);
		this.setTargetArtifactType(artifactType);
		
		if (artifactElement.hasAttribute("extendedArtifact")){
			this.setExtendedArtifactname(artifactElement.getAttribute("extendedArtifact"));
		}
		
	}
	
	public void  setTargetArtifactType(String artifactType) {
		this.artifactType = artifactType;
	}

	public String getTargetArtifactType() {	
		return this.artifactType;
	}

	public void setArtifactname(String artifactName){
		this.artifactName = artifactName;
	}
	
	public String getArtifactName() {
		return this.artifactName;
	}

	public void setExtendedArtifactname(String artifactName){
		this.extendedArtifactName = artifactName;
	}
	
	public String getExtendedArtifactName() {
		return this.extendedArtifactName;
	}

	static class ArtifactPatternResult implements IArtifactPatternResult {
		private IAbstractArtifact artifact;
		private Map<IModelComponent,Map<String,Object>> residualRequests;
		/**
		 * @param artifact
		 * @param residuals
		 */
		public ArtifactPatternResult(IAbstractArtifact artifact,
				Map<IModelComponent,Map<String, Object>> residuals) {
			this.artifact = artifact;
			this.residualRequests = residuals;
		}
		/**
		 * @return the artifact
		 */
		public IAbstractArtifact getArtifact() {
			return artifact;
		}
		
		/**
		 * @return the residualRequests
		 */
		public Map<IModelComponent,Map<String, Object>> getResidualRequests() {
			return residualRequests;
		}
	}
	
	/**
	 * Create the new artifact,
	 * set the values for names etc
	 * IT DOES NOT ADD it to the artifactManager.
	 * 
	 */
	public IArtifactPatternResult createArtifact(ITigerstripeModelProject project,
			String packageName, String artifactName, String extendedArtifactName
			) throws TigerstripeException {
		
		IArtifactManagerSession session = project.getArtifactManagerSession();
		artifact = session.makeArtifact(this.getTargetArtifactType());
		
		
		artifact.setPackage(packageName);
		artifact.setName(artifactName);
		if (! "".equals(extendedArtifactName)){
			
			IAbstractArtifact extendedArtifact = session.getArtifactByFullyQualifiedName(extendedArtifactName, true);
			if (extendedArtifact == null){
				extendedArtifact = session.makeArtifact(getTargetArtifactType());
				extendedArtifact.setFullyQualifiedName(extendedArtifactName);
			}
			artifact.setExtendedArtifact(extendedArtifact);
		}
		
		if (this.getTargetArtifactType().equals(ManagedEntityArtifact.class.getName())){
			Collection<String> implementsData = xmlParserUtils.getArtifactImplementsData(artifactElement);
			Collection<IAbstractArtifact> implementedArtifacts = new ArrayList<IAbstractArtifact>();
			for (String implemented : implementsData){
				IAbstractArtifact implementedArtifact = session.getArtifactByFullyQualifiedName(implemented, true);
				if (implementedArtifact == null){
					implementedArtifact = session.makeArtifact(getTargetArtifactType());
					implementedArtifact.setFullyQualifiedName(implemented);
				}
				implementedArtifacts.add(implementedArtifact);
			}
			this.artifact.setImplementedArtifacts(implementedArtifacts);
		}

		addArtifactBasics();
//		addComponentRequests(artifactElement);
		
		
		return new ArtifactPatternResult(artifact, addComponentRequests(artifactElement));
	}
	
	public void addToManager(ITigerstripeModelProject project, IAbstractArtifact newArtifact) throws TigerstripeException {
		IArtifactManagerSession session = project.getArtifactManagerSession();
		newArtifact.doSave(null);
		session.addArtifact(newArtifact);
	}
	
	public void annotateArtifact(ITigerstripeModelProject project, IArtifactPatternResult patternResult) throws TigerstripeException {
		Collection<EObject> annotationContents = xmlParserUtils.getAnnotations(artifactElement);

		for (EObject content : annotationContents){
			addAnnotation(patternResult.getArtifact(), content);
		}
		
		for(Map.Entry<IModelComponent,Map<String,Object>> residualEntry : patternResult.getResidualRequests().entrySet())
		{
			for(Map.Entry<String, Object> entry : residualEntry.getValue().entrySet())
			{
				if(entry.getKey().equals(IAnnotationAddFeatureRequest.ANNOTATION_FEATURE))
					addAnnotation(residualEntry.getKey(), (EObject)entry.getValue());
			}
		}
	}
	
	private void addArtifactBasics() throws TigerstripeException {

		String commentText = xmlParserUtils.getComment(artifactElement);
		if (commentText != null){
			this.artifact.setComment(commentText);
		}
		
		// Optional attributes
		if (artifactElement.hasAttribute("isAbstract")){

			this.artifact.setAbstract(Boolean.parseBoolean(artifactElement.getAttribute("isAbstract")));
		}
		// artifact Stereotypes
		Collection<IStereotypeInstance> stereotypeInstances = xmlParserUtils.getStereotypes(artifactElement, "stereotypes");
		for (IStereotypeInstance instance : stereotypeInstances){
			this.artifact.addStereotypeInstance(instance);
		}
	}


	/**
	 * These are all items that have NO inputs from the UI.
	 * 
	 * @param pattern
	 * @param artifactElement
	 */
	private Map<IModelComponent,Map<String, Object>> addComponentRequests( Element artifactElement)throws TigerstripeException {
		Map<IModelComponent,Map<String, Object>> residuals = new LinkedHashMap<IModelComponent,Map<String, Object>>();
		// Do the fields
		Collection<Map<String,Object>> allFieldData = xmlParserUtils.getArtifactFieldData(artifactElement);
		for (Map<String,Object> fieldData : allFieldData){
			// Create the Field
			IField field = artifact.makeField();
			Map<String,Object> residualMap = new LinkedHashMap<String,Object>();
			updateField(field, fieldData, residualMap);
			if(!residualMap.isEmpty()) residuals.put(field,residualMap);
			artifact.addField(field);
		}
		
		
		// Do the literals
		Collection<Map<String,Object>> allLiteralData = xmlParserUtils.getArtifactLiteralData(artifactElement);
		for (Map<String,Object> literalData : allLiteralData){
			// Create the Literal
			ILiteral literal = artifact.makeLiteral();
			Map<String,Object> residualMap = new LinkedHashMap<String,Object>();
			updateLiteral(literal, literalData, residualMap);
			if(!residualMap.isEmpty()) residuals.put(literal,residualMap);
			artifact.addLiteral(literal);

		}
		
		// Do the methods
		Collection<Map<String,Object>> allmethodData = xmlParserUtils.getArtifactMethodData(artifactElement);
		for (Map<String,Object> methodData : allmethodData){
			// Create the createRequest
			IMethod method = artifact.makeMethod();
			Map<String,Object> residualMap = new LinkedHashMap<String,Object>();
			updateMethod(method, methodData, residualMap);
			if(!residualMap.isEmpty()) residuals.put(method,residualMap);
			artifact.addMethod(method);
			
		}
		
		return residuals;
	}

	protected void addAnnotation(IModelComponent component, EObject content) throws TigerstripeException{
		try {
			String annotationClass = content.getClass().getInterfaces()[0].getName();
			Annotation anno = helper.addAnnotation(component, Util.packageOf(annotationClass), Util.nameOf(annotationClass));
			anno.setContent(content);
			AnnotationHelper.getInstance().saveAnnotation(anno);
		} catch (Exception e){
			e.printStackTrace();
			throw new TigerstripeException("Exception adding annotation to component",e);
		}
	}
	
	private static ArrayList<String> fieldCreateFeatures = new ArrayList<String>(Arrays.asList(
			IAttributeSetRequest.NAME_FEATURE,
			IAttributeSetRequest.TYPE_FEATURE,
			IAttributeSetRequest.MULTIPLICITY_FEATURE));
	
	private void updateField(IField field, Map<String,Object> fieldData, Map<String, Object> residuals) throws TigerstripeException {
		field.setName((String) fieldData.get(IAttributeSetRequest.NAME_FEATURE));
		IType type = field.makeType();
		type.setFullyQualifiedName((String) fieldData.get(IAttributeSetRequest.TYPE_FEATURE));
		type.setTypeMultiplicity(EMultiplicity.parse((String) fieldData.get(IAttributeSetRequest.MULTIPLICITY_FEATURE)));
		field.setType(type);
		// iterate over other features
		for (String feature : fieldData.keySet()){
			if (feature.equals(IAnnotationAddFeatureRequest.ANNOTATION_FEATURE)){				
//				addAnnotation(field,(EObject) fieldData.get(feature));
				residuals.put(feature, fieldData.get(feature));
			} else if (feature.equals(IStereotypeAddFeatureRequest.STEREOTYPE_FEATURE)){
				field.addStereotypeInstance((IStereotypeInstance) fieldData.get(feature));
			} else if (!fieldCreateFeatures.contains(feature)){
				String newValue = (String) fieldData.get(feature);

				if (IAttributeSetRequest.COMMENT_FEATURE.equals(feature)) {
					field.setComment(newValue);
				} else if (IAttributeSetRequest.VISIBILITY_FEATURE.equals(feature)) {
					if ("PUBLIC".equalsIgnoreCase(newValue)) {
						field.setVisibility(EVisibility.PUBLIC);
					} else if ("PROTECTED".equalsIgnoreCase(newValue)) {
						field.setVisibility(EVisibility.PROTECTED);
					} else if ("PRIVATE".equalsIgnoreCase(newValue)) {
						field.setVisibility(EVisibility.PRIVATE);
					} else if ("PACKAGE".equalsIgnoreCase(newValue)) {
						field.setVisibility(EVisibility.PACKAGE);
					}					
				} else if (IAttributeSetRequest.ISUNIQUE_FEATURE.equals(feature)) {
					boolean bool = Boolean.parseBoolean(newValue);
					field.setUnique(bool);					
				} else if (IAttributeSetRequest.ISORDERED_FEATURE.equals(feature)) {
					boolean bool = Boolean.parseBoolean(newValue);
					field.setOrdered(bool);					
				} else if (IAttributeSetRequest.READONLY_FEATURE.equals(feature)) {
					boolean bool = Boolean.parseBoolean(newValue);
					field.setReadOnly(bool);					
				} else if (IAttributeSetRequest.DEFAULTVALUE_FEATURE.equals(feature)) {
					field.setDefaultValue(newValue);
				}
				
			}
		}
	}
	

	private static ArrayList<String> literalCreateFeatures = new ArrayList<String>(Arrays.asList(
			ILiteralSetRequest.NAME_FEATURE,
			ILiteralSetRequest.TYPE_FEATURE,
			ILiteralSetRequest.VALUE_FEATURE));

	private void updateLiteral(ILiteral literal,Map<String,Object> literalData, Map<String, Object> residuals ) throws TigerstripeException{
		literal.setName((String) literalData.get(ILiteralSetRequest.NAME_FEATURE));
		IType type = literal.makeType();
		type.setFullyQualifiedName((String) literalData.get(ILiteralSetRequest.TYPE_FEATURE));
		literal.setType(type);
		literal.setValue((String) literalData.get(ILiteralSetRequest.VALUE_FEATURE));
		// iterate over other features
		for (String feature : literalData.keySet()){
			if (feature.equals(IAnnotationAddFeatureRequest.ANNOTATION_FEATURE)){
//				addAnnotation(literal, (EObject) literalData.get(feature));
				residuals.put(feature, literalData.get(feature));
			} else if (feature.equals(IStereotypeAddFeatureRequest.STEREOTYPE_FEATURE)){
				literal.addStereotypeInstance((IStereotypeInstance) literalData.get(feature));
			} else	if (!literalCreateFeatures.contains(feature)){
				String newValue = (String) literalData.get(feature);
				if (ILiteralSetRequest.COMMENT_FEATURE.equals(feature)) {
					literal.setComment(newValue);
				} else if (ILiteralSetRequest.VISIBILITY_FEATURE.equals(feature)) {
					if ("PUBLIC".equalsIgnoreCase(newValue)) {
						literal.setVisibility(EVisibility.PUBLIC);
					} else if ("PROTECTED".equalsIgnoreCase(newValue)) {
						literal.setVisibility(EVisibility.PROTECTED);
					} else if ("PRIVATE".equalsIgnoreCase(newValue)) {
						literal.setVisibility(EVisibility.PRIVATE);
					} else if ("PACKAGE".equalsIgnoreCase(newValue)) {
						literal.setVisibility(EVisibility.PACKAGE);
					}
				}
			}
		}
	}
	
	
	private static ArrayList<String> methodCreateFeatures = new ArrayList<String>(Arrays.asList(
			IMethodSetRequest.NAME_FEATURE,
			IMethodSetRequest.TYPE_FEATURE,
			IMethodSetRequest.MULTIPLICITY_FEATURE));
	
	private static ArrayList<String> methodAddFeatures = new ArrayList<String>(Arrays.asList(
			IMethodAddFeatureRequest.EXCEPTIONS_FEATURE,
			IMethodAddFeatureRequest.ARGUMENTS_FEATURE));
	
	private static ArrayList<String> argumentCreateFeatures = new ArrayList<String>(Arrays.asList(
			IMethodAddFeatureRequest.ARGUMENT_NAME_FEATURE,
			IMethodAddFeatureRequest.ARGUMENT_TYPE_FEATURE,
			IMethodAddFeatureRequest.ARGUMENT_MULTIPLICITY_FEATURE));
	
	private void updateMethod(IMethod method,Map<String,Object> methodData, Map<String, Object> residuals )  throws TigerstripeException{
		method.setName((String) methodData.get(IMethodSetRequest.NAME_FEATURE));
		IType type = method.makeType();
		type.setFullyQualifiedName((String) methodData.get(IMethodSetRequest.TYPE_FEATURE));
		type.setTypeMultiplicity(EMultiplicity.parse((String) methodData.get(IMethodSetRequest.MULTIPLICITY_FEATURE)));
		method.setReturnType(type);
		// Now any "Add" features -
		//ARGUMENTS
		if (methodData.containsKey(IMethodAddFeatureRequest.ARGUMENTS_FEATURE)){
			Collection<Map<String,Object>> allArgumentData = (Collection<Map<String,Object>>) methodData.get(IMethodAddFeatureRequest.ARGUMENTS_FEATURE);
			for (Map<String,Object> argumentData : allArgumentData){
				IArgument argument = method.makeArgument();
				argument.setName((String) argumentData.get(IMethodAddFeatureRequest.ARGUMENT_NAME_FEATURE));
				IType argType = method.makeType();
				argType.setFullyQualifiedName((String) argumentData.get(IMethodAddFeatureRequest.ARGUMENT_TYPE_FEATURE));
				argType.setTypeMultiplicity(EMultiplicity.parse((String) argumentData.get(IMethodAddFeatureRequest.ARGUMENT_MULTIPLICITY_FEATURE)));
				argument.setType(argType);
				for (String featureId : argumentData.keySet()){

					if (featureId.equals(IStereotypeAddFeatureRequest.STEREOTYPE_FEATURE)){
						argument.addStereotypeInstance((IStereotypeInstance) methodData.get(featureId));
					} else if (!argumentCreateFeatures.contains(featureId)){
						String newValue = (String) argumentData.get(featureId);

						if (featureId.endsWith("defaultValue")){
							argument.setDefaultValue(newValue);
						}
						if (featureId.endsWith("isOrdered")){
							boolean bool = Boolean.parseBoolean(newValue);
							argument.setOrdered(bool);
						}
						if (featureId.endsWith("isUnique")){
							boolean bool = Boolean.parseBoolean(newValue);
							argument.setUnique(bool);
						}
						if (featureId.endsWith("comment")){
							argument.setComment(newValue);
						}
					}

				} 
				method.addArgument(argument);
			}
		}
		//EXCEPTIONS
		if (methodData.containsKey(IMethodAddFeatureRequest.EXCEPTIONS_FEATURE)){
			Collection<String> exceptions = (Collection<String>) methodData.get(IMethodAddFeatureRequest.EXCEPTIONS_FEATURE);
			for (String exceptionName : exceptions){
				IException exception = method.makeException();
				exception.setFullyQualifiedName(exceptionName);
				method.addException(exception);
			}
		}
		
		
		// iterate over other features
		for (String feature : methodData.keySet()){

			if (feature.equals(IAnnotationAddFeatureRequest.ANNOTATION_FEATURE)){
//				addAnnotation(method, (EObject) methodData.get(feature));
				residuals.put(feature, methodData.get(feature));
			} else if (feature.equals(IStereotypeAddFeatureRequest.STEREOTYPE_FEATURE)){
				method.addStereotypeInstance((IStereotypeInstance) methodData.get(feature));
			} else if (feature.equals(IStereotypeAddFeatureRequest.RETURN_STEREOTYPE_FEATURE)){
				method.addReturnStereotypeInstance((IStereotypeInstance) methodData.get(feature));
			} else	if (!methodCreateFeatures.contains(feature) && !methodAddFeatures.contains(feature)){
				String newValue = (String) methodData.get(feature);
				if (IMethodSetRequest.COMMENT_FEATURE.equals(feature)) {
					method.setComment(newValue);
				} else if (IMethodSetRequest.ISVOID_FEATURE.equals(feature)) {
					boolean bool = Boolean.parseBoolean(newValue);
					method.setVoid(bool);
				} else if (IMethodSetRequest.VISIBILITY_FEATURE.equals(feature)) {
					if ("PUBLIC".equalsIgnoreCase(newValue)) {
						method.setVisibility(EVisibility.PUBLIC);
					} else if ("PROTECTED".equalsIgnoreCase(newValue)) {
						method.setVisibility(EVisibility.PROTECTED);
					} else if ("PRIVATE".equalsIgnoreCase(newValue)) {
						method.setVisibility(EVisibility.PRIVATE);
					} else if ("PACKAGE".equalsIgnoreCase(newValue)) {
						method.setVisibility(EVisibility.PACKAGE);
					}
				} else if (IMethodSetRequest.ISABSTRACT_FEATURE.equals(feature)) {
					method.setAbstract("true".equals(newValue));
				} else if (IMethodSetRequest.ISUNIQUE_FEATURE.equals(feature)) {
					boolean bool = Boolean.parseBoolean(newValue);
					method.setUnique(bool);
				} else if (IMethodSetRequest.ISORDERED_FEATURE.equals(feature)) {
					boolean bool = Boolean.parseBoolean(newValue);
					method.setOrdered(bool);
				} else if (IMethodSetRequest.DEFAULTRETURNVALUE_FEATURE.equals(feature)) {
					method.setDefaultReturnValue(newValue);
				} else if (IMethodSetRequest.RETURNNAME_FEATURE.equals(feature)) {
					method.setReturnName(newValue);
				}
			}
		}
	}
}
