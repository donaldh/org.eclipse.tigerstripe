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
import java.util.Map;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAnnotationAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IStereotypeAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.ComponentNameProvider;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EChangeableEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.patterns.IArtifactPatternResult;
import org.eclipse.tigerstripe.workbench.patterns.IRelationPattern;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class RelationPattern extends ArtifactPattern implements IRelationPattern {

	private String aEndType = "";
	private String zEndType = "";
	
	public String getAEndType() {
		return aEndType;
	}
	public void setAEndType(String endType) {
		aEndType = endType;
	}
	public String getZEndType() {
		return zEndType;
	}
	public void setZEndType(String endType) {
		zEndType = endType;
	}
	
	
	
	@Override
	public void setElement(Element artifactElement) {
		super.setElement(artifactElement);
		if ( xmlParserUtils.elementHasAssociationSpecifics(artifactElement) ){
			Map<String,Object> endData = xmlParserUtils.getAssociationEndData(artifactElement);
			setAEndType((String) endData.get(IArtifactSetFeatureRequest.AENDType));
			setZEndType((String) endData.get(IArtifactSetFeatureRequest.ZENDType));
		} else if ( xmlParserUtils.elementHasDependencySpecifics(artifactElement)){
			Map<String,Object> dependencyData = xmlParserUtils.getDependencyEndData(artifactElement);
			setAEndType((String) dependencyData.get(IArtifactSetFeatureRequest.AENDType));
			setZEndType((String) dependencyData.get(IArtifactSetFeatureRequest.ZENDType));
		}
	}



	private static ArrayList<String> endCreateFeatures = new ArrayList<String>(Arrays.asList(
			IArtifactSetFeatureRequest.AENDMULTIPLICITY,
			IArtifactSetFeatureRequest.ZENDMULTIPLICITY,
			IArtifactSetFeatureRequest.AEND,
			IArtifactSetFeatureRequest.ZEND,
			IArtifactSetFeatureRequest.AENDType,
			IArtifactSetFeatureRequest.ZENDType));
	
	public IArtifactPatternResult createArtifact(ITigerstripeModelProject project,
			String packageName, String artifactName,
			String extendedArtifactName, String endType, String endType2)
			throws TigerstripeException {
		IArtifactPatternResult result = super.createArtifact(project, packageName, artifactName, extendedArtifactName);
		Map<String,Object> endData = xmlParserUtils.getAssociationEndData(element);
		
		IAbstractArtifact artifact = result.getArtifact();
		if (artifact instanceof IAssociationArtifact){
			IAssociationArtifact association = (IAssociationArtifact) artifact;
			IAssociationEnd end = association.getAEnd();
			if (end == null){
				end = association.makeAssociationEnd();
				
			}
			end.setMultiplicity(EMultiplicity.parse((String) endData.get(IArtifactSetFeatureRequest.AENDMULTIPLICITY)));
			IType type = end.getType();
			if ( type == null){
				type = end.makeType();
				
			}
			type.setFullyQualifiedName(endType);
			end.setType(type);
			association.setAEnd(end);
			
			
			IAssociationEnd end2 = association.getZEnd();
			if (end2 == null){
				end2 = association.makeAssociationEnd();	
			}
			end2.setMultiplicity(EMultiplicity.parse((String) endData.get(IArtifactSetFeatureRequest.ZENDMULTIPLICITY)));
			IType type2 = end2.getType();
			if ( type2 == null){
				type2 = end.makeType();
			}
			
			type2.setFullyQualifiedName(endType2);
			end2.setType(type2);
			association.setZEnd(end2);
			
			for (String feature : endData.keySet()){

				if (feature.equals(IAnnotationAddFeatureRequest.AEND_ANNOTATION_FEATURE)){
//					addAnnotation(end, (EObject) endData.get(feature));
					LinkedHashMap<String,Object> residual = new LinkedHashMap<String,Object>();
					residual.put(IAnnotationAddFeatureRequest.ANNOTATION_FEATURE, endData.get(feature));
					result.getResidualRequests().put(end, residual);
				} else if (feature.equals(IAnnotationAddFeatureRequest.ZEND_ANNOTATION_FEATURE)){
//					addAnnotation(end2, (EObject) endData.get(feature));
					LinkedHashMap<String,Object> residual = new LinkedHashMap<String,Object>();
					residual.put(IAnnotationAddFeatureRequest.ANNOTATION_FEATURE, endData.get(feature));
					result.getResidualRequests().put(end2, residual);

				} else if (feature.equals(IStereotypeAddFeatureRequest.AEND_STEREOTYPE_FEATURE)){
					end.addStereotypeInstance((IStereotypeInstance) endData.get(feature));
				} else if (feature.equals(IStereotypeAddFeatureRequest.ZEND_STEREOTYPE_FEATURE)){
					end2.addStereotypeInstance((IStereotypeInstance) endData.get(feature));
				} else if (!endCreateFeatures.contains(feature)){
					IAssociationEnd targetEnd;
					int endref = 0;
					if (feature.startsWith("aEnd")) {
						targetEnd = end;
						endref = 0;
					} else {
						targetEnd = end2;
						endref = 1;
					}
					String newValue = (String) endData.get(feature);
					if (feature.endsWith("Aggregation")) {
						targetEnd.setAggregation(EAggregationEnum
								.parse(newValue));
					} else if (feature.endsWith("Navigable")) {
						boolean bool = Boolean.valueOf(newValue);
						targetEnd.setNavigable(bool);
					} else if (feature.endsWith("Ordered")) {
						boolean bool = Boolean.valueOf(newValue);
						targetEnd.setOrdered(bool);
					} else if (feature.endsWith("Changeable")) {
						targetEnd
								.setChangeable(EChangeableEnum.parse(newValue));
					} else if (feature.endsWith("Name")) {
						if (! "".equals(newValue))
								targetEnd.setName(newValue);
						else {
							ComponentNameProvider nameFactory = ComponentNameProvider.getInstance();
							String name = nameFactory.getNewAssociationEndName(this,association, endref);
							targetEnd.setName(name);
						}
					} else if (feature.endsWith("Unique")) {
						targetEnd.setUnique(Boolean.valueOf(newValue));
					} else if (feature.endsWith("Visibility")) {
						if ("PUBLIC".equals(newValue))
							targetEnd.setVisibility(EVisibility.PUBLIC);
						else if ("PROTECTED".equals(newValue))
							targetEnd.setVisibility(EVisibility.PROTECTED);
						else if ("PRIVATE".equals(newValue))
							targetEnd.setVisibility(EVisibility.PRIVATE);
						else if ("PACKAGE".equals(newValue))
							targetEnd.setVisibility(EVisibility.PACKAGE);
					} else if (feature.endsWith("Comment")){
						targetEnd.setComment(newValue);
					} 
					
				}
			}
			
		}
		if (artifact instanceof IDependencyArtifact) {
			// This bit ignores nearly everything from the template
			// as there is nothing of any value there
			IDependencyArtifact dependency = (IDependencyArtifact) artifact;
			IType type = dependency.getAEndType();
			if (type == null){
				type = dependency.makeType();
				dependency.setAEndType(type);
			}
			type.setFullyQualifiedName(endType);
			IType type2 = dependency.getZEndType();
			if (type2 == null){
				type2 = dependency.makeType();
				dependency.setZEndType(type2);
			}
			type2.setFullyQualifiedName(endType2);
		}
		
		 return result;
	}
	
	@Override
	public Collection<Class<?>> getUsedAnnotations() {
		Collection<Class<?>> usedAnnotations = super.getUsedAnnotations();
		if ( xmlParserUtils.elementHasAssociationSpecifics(getElement()) ){
			NodeList nodes = xmlParserUtils.getElements(getElement(), "associationEnd");
			for (int fn = 0; fn < nodes.getLength(); fn++) {
				Element element = (Element) nodes.item(fn);
				addUniqueAnnotations(usedAnnotations, element);
			}
		} else if ( xmlParserUtils.elementHasDependencySpecifics(getElement())){
			// Dependency ends don't support Annotations!
		}

		return usedAnnotations;
	}
	
}
