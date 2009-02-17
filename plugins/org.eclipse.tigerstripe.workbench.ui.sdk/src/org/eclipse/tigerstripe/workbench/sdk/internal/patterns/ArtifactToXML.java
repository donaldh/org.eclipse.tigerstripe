/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.sdk.internal.patterns;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.util.encode.XmlEscape;
import org.eclipse.tigerstripe.workbench.model.annotation.IAnnotationCapable;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ArtifactToXML {

	private XmlEscape escaper = new XmlEscape();
	
	public ArtifactToXML(Document document) {
		super();
		this.document = document;
	}

	public static String TS_NSURI = "http://org.eclipse.tigerstripe/xml/tigerstripeExport/v2-0";
	
	private Document document;
	private boolean includeEndNames = true;
	
	public Element artifactToElement (IAbstractArtifact artifact){
		return artifactToElement(artifact, true);
	}
	
	// This is what the export XML plugin does!
	public Element artifactToElement (IAbstractArtifact artifact, boolean includeEndNames){
		this.includeEndNames = includeEndNames;
		
		Element artifactElement = document.createElementNS(TS_NSURI, "artifact");
		artifactElement.setPrefix("ts");
		
		artifactElement.setAttribute("name", "$user");
		artifactElement.setAttribute("artifactType", artifact.getArtifactType());
		
		if (artifact.hasExtends()){
			artifactElement.setAttribute("extendedArtifact", artifact.getExtendedArtifact().getFullyQualifiedName());
		}
		
		if (!(artifact instanceof IPackageArtifact)){
			artifactElement.setAttribute("isAbstract", Boolean.toString(artifact.isAbstract()));
		}
		
		if (!artifact.getImplementedArtifacts().isEmpty()){
			Element implementsElement = document.createElementNS(TS_NSURI, "implements");
			implementsElement.setPrefix("ts");
			for (IAbstractArtifact implemented : artifact.getImplementedArtifacts()){
				Element impl = document.createElementNS(TS_NSURI, "implementedInterface");
				impl.setPrefix("ts");
				impl.appendChild(document.createTextNode(implemented.getFullyQualifiedName()));
				implementsElement.appendChild(impl);
			}
			artifactElement.appendChild(implementsElement);
		}
		
		Element annotationsElement = getAnnotations(artifact);
		if (annotationsElement != null){
			artifactElement.appendChild(annotationsElement);
		}
		
		Element commentElement = getComment(artifact);
		if (commentElement != null){
			artifactElement.appendChild(commentElement);
		}
		
		Element stereotypesElement = getStereotypes(artifact);
		if (stereotypesElement != null){
			artifactElement.appendChild(stereotypesElement);
		}
		
		Element literalsElement = getLiterals(artifact);
		if (literalsElement != null){
			artifactElement.appendChild(literalsElement);
		}
		
		Element fieldsElement = getFields(artifact);
		if (fieldsElement != null){
			artifactElement.appendChild(fieldsElement);
		}
		
		Element methodsElement = getMethods(artifact);
		if (methodsElement != null){
			artifactElement.appendChild(methodsElement);
		}
		
		Element specificsElement = getSpecifics(artifact);
		if (specificsElement != null){
			artifactElement.appendChild(specificsElement);
		}
		
		return artifactElement;
		
	}
	
	private Element getComment(IModelComponent component){
		if (component.getComment().length() > 0){
			Element commentElement = document.createElementNS(TS_NSURI, "comment");
			commentElement.setPrefix("ts");
			//TODO - check the encoding!
			commentElement.appendChild(document.createTextNode(escaper.encode(component.getComment())));
			return commentElement;
		} else {
			return null;
		}
	}
	
	private Element getStereotypes(IModelComponent component){
		if (component.getStereotypeInstances().isEmpty()){
			return null;
		} else {
			Element stereotypesElement = document.createElementNS(TS_NSURI, "stereotypes");
			stereotypesElement.setPrefix("ts");
			for (IStereotypeInstance instance : component.getStereotypeInstances()){
				stereotypesElement.appendChild(getStereotypesCommon(instance));
			}
			return stereotypesElement;
		}
	}
	
	private Element getReturnStereotypes(IMethod component){
		if (component.getReturnStereotypeInstances().isEmpty()){
			return null;
		} else {
			Element stereotypesElement = document.createElementNS(TS_NSURI, "returnStereotypes");
			stereotypesElement.setPrefix("ts");
			for (IStereotypeInstance instance : component.getStereotypeInstances()){
				stereotypesElement.appendChild(getStereotypesCommon(instance));
			}
			return stereotypesElement;
		}
	}
	
	
	private Element getStereotypesCommon(IStereotypeInstance instance ){
		Element instanceElement = document.createElementNS(TS_NSURI, "stereotype");
		instanceElement.setPrefix("ts");
		instanceElement.setAttribute("name", instance.getName());
		for (IStereotypeAttribute attribute : instance.getCharacterizingStereotype().getAttributes()){
			Element attributeElement = document.createElementNS(TS_NSURI, "stereotypeAttribute");
			attributeElement.setPrefix("ts");
			attributeElement.setAttribute("name", attribute.getName());
			attributeElement.setAttribute("array", Boolean.toString(attribute.isArray()));
			if (!attribute.isArray()){
				try {
					Element valueElement = document.createElementNS(TS_NSURI, "value");
					valueElement.setPrefix("ts");
					valueElement.appendChild(document.createTextNode(escaper.encode(instance.getAttributeValue(attribute))));
					attributeElement.appendChild(valueElement);
				} catch (TigerstripeException t){
					// Don't care!
				}
			} else {
				try {
					for (String val  : instance.getAttributeValues(attribute)){
						Element valueElement = document.createElementNS(TS_NSURI, "value");
						valueElement.setPrefix("ts");
						valueElement.appendChild(document.createTextNode(val));
						attributeElement.appendChild(valueElement);
					}
				} catch (TigerstripeException t){
					// Don't care!
				}
			}
			instanceElement.appendChild(attributeElement);
		}
		return instanceElement;
	}
	
	
	private Element getLiterals(IAbstractArtifact artifact){
		if (artifact.getLiterals().isEmpty()){
			return null;
		} else {
			Element literalsElement = document.createElementNS(TS_NSURI, "literals");
			literalsElement.setPrefix("ts");
			for (ILiteral literal : artifact.getLiterals()){
				Element literalElement = document.createElementNS(TS_NSURI, "literal");
				literalElement.setPrefix("ts");
				literalElement.setAttribute("name", literal.getName());
				literalElement.setAttribute("value", literal.getValue());
				literalElement.setAttribute("type", literal.getType().getFullyQualifiedName());
				literalElement.setAttribute("visibility", literal.getVisibility().getLabel());
				Element annotationsElement = getAnnotations(literal);
				if (annotationsElement != null){
					literalElement.appendChild(annotationsElement);
				}
				Element stereotypesElement = getStereotypes(literal);
				if (stereotypesElement != null){
					literalElement.appendChild(stereotypesElement);
				}
				Element commentElement = getComment(literal);
				if (commentElement != null){
					literalElement.appendChild(commentElement);
				}
				literalsElement.appendChild(literalElement);
			}
			return literalsElement;
		}
	}
	
	private Element getFields(IAbstractArtifact artifact){
		if (artifact.getFields().isEmpty()){
			return null;
		} else {
			Element fieldsElement = document.createElementNS(TS_NSURI, "fields");
			fieldsElement.setPrefix("ts");
			for (IField field : artifact.getFields()){
				Element fieldElement = document.createElementNS(TS_NSURI, "field");
				fieldElement.setPrefix("ts");
				fieldElement.setAttribute("name", field.getName());
				fieldElement.setAttribute("type", field.getType().getFullyQualifiedName());
				fieldElement.setAttribute("typeMultiplicity", field.getType().getTypeMultiplicity().getLabel());
				fieldElement.setAttribute("visibility", field.getVisibility().getLabel());
				
				fieldElement.setAttribute("readonly", Boolean.toString(field.isReadOnly()));
				fieldElement.setAttribute("unique", Boolean.toString(field.isUnique()));
				fieldElement.setAttribute("ordered", Boolean.toString(field.isOrdered()));
				if (field.getDefaultValue() != null){
					fieldElement.setAttribute("defaultValue", escaper.encode(field.getDefaultValue()));
				}
				Element annotationsElement = getAnnotations(field);
				if (annotationsElement != null){
					fieldElement.appendChild(annotationsElement);
				}
				Element stereotypesElement = getStereotypes(field);
				if (stereotypesElement != null){
					fieldElement.appendChild(stereotypesElement);
				}
				Element commentElement = getComment(field);
				if (commentElement != null){
					fieldElement.appendChild(commentElement);
				}
				fieldsElement.appendChild(fieldElement);
			}
			return fieldsElement;
		}
	}
	
	private Element getMethods(IAbstractArtifact artifact){
		if (artifact.getMethods().isEmpty()){
			return null;
		} else {
			Element methodsElement = document.createElementNS(TS_NSURI, "methods");
			methodsElement.setPrefix("ts");
			for (IMethod method : artifact.getMethods()){
				Element methodElement = document.createElementNS(TS_NSURI, "method");
				methodElement.setPrefix("ts");
				methodElement.setAttribute("name", method.getName());
				methodElement.setAttribute("visibility", method.getVisibility().getLabel());
				methodElement.setAttribute("returnType", method.getReturnType().getFullyQualifiedName());
				methodElement.setAttribute("returnTypeMultiplicity", method.getReturnType().getTypeMultiplicity().getLabel());
				
				methodElement.setAttribute("isVoid", Boolean.toString(method.isVoid()));
				methodElement.setAttribute("isAbstract", Boolean.toString(method.isAbstract()));
				methodElement.setAttribute("unique", Boolean.toString(method.isUnique()));
				methodElement.setAttribute("ordered", Boolean.toString(method.isOrdered()));
				if (method.getDefaultReturnValue() != null){
					methodElement.setAttribute("defaultReturnValue", escaper.encode(method.getDefaultReturnValue()));
				}
				if (!method.getReturnName().equals("")){
					methodElement.setAttribute("methodReturnName", escaper.encode(method.getReturnName()));
				}
				Element annotationsElement = getAnnotations(method);
				if (annotationsElement != null){
					methodElement.appendChild(annotationsElement);
				}
				Element stereotypesElement = getStereotypes(method);
				if (stereotypesElement != null){
					methodElement.appendChild(stereotypesElement);
				}
				Element commentElement = getComment(method);
				if (commentElement != null){
					methodElement.appendChild(commentElement);
				}
				// arguments
				Element argumentsElement = getArguments(method);
				if (argumentsElement != null){
					methodElement.appendChild(argumentsElement);
				}
				// return Stereotypes
				Element returnStereotypesElement = getReturnStereotypes(method);
				if (returnStereotypesElement != null){
					methodElement.appendChild(returnStereotypesElement);
				}
				// exceptions
				Element exceptionsElement = getExceptions(method);
				if (exceptionsElement != null){
					methodElement.appendChild(exceptionsElement);
				}
				methodsElement.appendChild(methodElement);
			}
			return methodsElement;
		}
	}
	
	private Element getArguments(IMethod method){
		if (method.getArguments().isEmpty()){
			return null;
		} else {
			Element argumentsElement = document.createElementNS(TS_NSURI, "arguments");
			argumentsElement.setPrefix("ts");
			for (IArgument argument : method.getArguments()){
				Element argumentElement = document.createElementNS(TS_NSURI, "argument");
				argumentElement.setPrefix("ts");
				argumentElement.setAttribute("name", argument.getName());
				argumentElement.setAttribute("type", argument.getType().getFullyQualifiedName());
				argumentElement.setAttribute("typeMultiplicity", argument.getType().getTypeMultiplicity().getLabel());
				argumentElement.setAttribute("unique", Boolean.toString(argument.isUnique()));
				argumentElement.setAttribute("ordered", Boolean.toString(argument.isOrdered()));
				if (argument.getDefaultValue() != null){
					argumentElement.setAttribute("defaultValue", escaper.encode(argument.getDefaultValue()));
				}
				// Argument is not an  IModelComponent
				// AND Comment/stereotypes are round the opposite way to everything else
				Element annotationsElement = getAnnotations(argument);
				if (annotationsElement != null){
					argumentElement.appendChild(annotationsElement);
				}
				if (argument.getComment().length() > 0){
					Element commentElement = document.createElementNS(TS_NSURI, "comment");
					commentElement.setPrefix("ts");
					//TODO - check the encoding!
					commentElement.appendChild(document.createTextNode(argument.getComment()));
					argumentElement.appendChild(commentElement);
				}
				if (! argument.getStereotypeInstances().isEmpty()){
					Element stereotypesElement = document.createElementNS(TS_NSURI, "stereotypes");
					stereotypesElement.setPrefix("ts");
					for (IStereotypeInstance instance : argument.getStereotypeInstances()){
						stereotypesElement.appendChild(getStereotypesCommon(instance));
					}
					argumentElement.appendChild(stereotypesElement);
				}

				argumentsElement.appendChild(argumentElement);
			}
			return argumentsElement;
		}
	}
	
	private Element getExceptions(IMethod method){
		if (method.getExceptions().isEmpty()){
			return null;
		} else {
			Element exceptionsElement = document.createElementNS(TS_NSURI, "exceptions");
			exceptionsElement.setPrefix("ts");
			for (IException exception : method.getExceptions()){
				Element exceptionElement = document.createElementNS(TS_NSURI, "exception");
				exceptionElement.setPrefix("ts");
				exceptionElement.setAttribute("name", exception.getFullyQualifiedName());
				exceptionsElement.appendChild(exceptionElement);
			}
			return exceptionsElement;
		}
	}
	
	private Element getSpecifics(IAbstractArtifact artifact){
		if (artifact instanceof IEnumArtifact){
			Element specificsElement = document.createElementNS(TS_NSURI, "enumerationSpecifics");
			specificsElement.setPrefix("ts");
			specificsElement.setAttribute("baseType", ((IEnumArtifact) artifact).getBaseTypeStr());
			return specificsElement;
		} else if (artifact instanceof IAssociationClassArtifact){
			Element specificsElement = document.createElementNS(TS_NSURI, "associationClassSpecifics");
			specificsElement.setPrefix("ts");
			specificsElement.appendChild(getAssociationEnd(((IAssociationClassArtifact) artifact).getAEnd(), "AEnd")); 
			specificsElement.appendChild(getAssociationEnd(((IAssociationClassArtifact) artifact).getZEnd(), "ZEnd")); 
			return specificsElement;
		} else if (artifact instanceof IAssociationArtifact){
			Element specificsElement = document.createElementNS(TS_NSURI, "associationSpecifics");
			specificsElement.setPrefix("ts");
			specificsElement.appendChild(getAssociationEnd(((IAssociationArtifact) artifact).getAEnd(), "AEnd")); 
			specificsElement.appendChild(getAssociationEnd(((IAssociationArtifact) artifact).getZEnd(), "ZEnd")); 
			return specificsElement;
		} else if (artifact instanceof IDependencyArtifact){
			Element specificsElement = document.createElementNS(TS_NSURI, "dependencySpecifics");
			specificsElement.setPrefix("ts");
			specificsElement.setAttribute("aEndTypeName", ((IDependencyArtifact) artifact).getAEndType().getFullyQualifiedName());
			specificsElement.setAttribute("zEndTypeName", ((IDependencyArtifact) artifact).getZEndType().getFullyQualifiedName());
			return specificsElement;
		} else if (artifact instanceof IQueryArtifact){
			Element specificsElement = document.createElementNS(TS_NSURI, "querySpecifics");
			specificsElement.setPrefix("ts");
			specificsElement.setAttribute("returnedTypeName", ((IQueryArtifact) artifact).getReturnedType().getFullyQualifiedName());
			specificsElement.setAttribute("returnedTypeMultiplicity", ((IQueryArtifact) artifact).getReturnedType().getTypeMultiplicity().getLabel());
			return specificsElement;
		} 
			
		
		return null;
	}
	
	
	private Element getAssociationEnd(IAssociationEnd end, String name){
		Element endElement = document.createElementNS(TS_NSURI, "associationEnd");
		endElement.setPrefix("ts");
		endElement.setAttribute("end", name);
		// This is used in patterns - a blank name means the end name is made up using the naming algorithm
		if (includeEndNames){
			endElement.setAttribute("name", end.getName());
		} else {
			endElement.setAttribute("name", "");
		}
		endElement.setAttribute("type", end.getType().getFullyQualifiedName());
		endElement.setAttribute("multiplicity", end.getMultiplicity().getLabel());
		endElement.setAttribute("unique", Boolean.toString(end.isUnique()));
		endElement.setAttribute("ordered", Boolean.toString(end.isOrdered()));
		endElement.setAttribute("visibility", end.getVisibility().getLabel());
		
		endElement.setAttribute("aggregation", end.getAggregation().getLabel());
		endElement.setAttribute("changeable", end.getChangeable().getLabel());
		endElement.setAttribute("navigable", Boolean.toString(end.isNavigable()));
		Element annotationsElement = getAnnotations(end);
		if (annotationsElement != null){
			endElement.appendChild(annotationsElement);
		}
		Element stereotypesElement = getStereotypes(end);
		if (stereotypesElement != null){
			endElement.appendChild(stereotypesElement);
		}
		Element commentElement = getComment(end);
		if (commentElement != null){
			endElement.appendChild(commentElement);
		}
		return endElement;
		
	}
	
	
	private Element getAnnotations(IAnnotationCapable capable){
		if (capable.hasAnnotations()){
			Element annotationsElement = document.createElementNS(TS_NSURI, "annotations");
			annotationsElement.setPrefix("ts");
			for (Object  ann : capable.getAnnotations()){
				EObject annotation = (EObject) ann;
				ResourceSet set = new ResourceSetImpl();
				URI temp = URI.createURI("temp");
				Resource res = set.createResource(temp);
				EList contents = res.getContents();
				contents.add(annotation);
				OutputStream myOutputStream = new ByteArrayOutputStream();
				Map options = new HashMap();
				options.put(XMLResource.OPTION_DECLARE_XML, false);
				
				try {
					res.save(myOutputStream, options);
					String annoString = myOutputStream.toString();
					CDATASection section = document.createCDATASection(annoString);
					annotationsElement.appendChild(section);
				} catch (Exception e){
					
				}
				
			}
			return annotationsElement; 
		} else {
			return null;
		}
	}
}
