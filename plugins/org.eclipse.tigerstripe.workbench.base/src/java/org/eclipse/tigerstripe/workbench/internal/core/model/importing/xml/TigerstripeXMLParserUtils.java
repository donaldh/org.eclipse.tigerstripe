package org.eclipse.tigerstripe.workbench.internal.core.model.importing.xml;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IAttributeSetRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.ILiteralSetRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodSetRequest;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EChangeableEnum;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TigerstripeXMLParserUtils {

	private PrintWriter out = null;
	private MessageList messages = null;
	private String namespace;
	// The profileSession is needed to resolve stereotypes
	private IWorkbenchProfileSession profileSession = TigerstripeCore.getWorkbenchProfileSession();
	
	public TigerstripeXMLParserUtils(String namespace){
		this.namespace = namespace;
	}
	
	public TigerstripeXMLParserUtils(String namespace, PrintWriter out, MessageList messages){
		this.namespace = namespace;
		this.out = out;
		this.messages = messages;
	}
	
	public String getArtifactType(Element artifactElement){
		String artifactTypeName = artifactElement.getAttribute("artifactType");
		return artifactTypeName;
	}
	
	public String getBaseType(Element artifactElement){
		NodeList specificsNodes = artifactElement
		.getElementsByTagNameNS(namespace, "enumerationSpecifics");
		for (int in = 0; in < specificsNodes.getLength(); in++) {
			Element specificNodes = (Element) specificsNodes.item(in);
			String baseType = specificNodes.getAttribute("baseType");
			return baseType;
		}
		return null;
	}
	
	public String getReturnType(Element artifactElement){
		NodeList specificsNodes = artifactElement
		.getElementsByTagNameNS(namespace, "querySpecifics");
		for (int in = 0; in < specificsNodes.getLength(); in++) {
			Element specificNode = (Element) specificsNodes.item(in);
			// TODO The schema has the multiplicty as well...
			String returnType = specificNode.getAttribute("returnedTypeName");
			return returnType;
		}
		return null;
	}

	public Collection<String> getArtifactImplementsData(Element artifactElement ){
		Collection<String> implementsData = new ArrayList<String>();
		NodeList implementsNodes = artifactElement
			.getElementsByTagNameNS(namespace, "implementedInterface");
		for (int in = 0; in < implementsNodes.getLength(); in++) {
			Element implementsNode = (Element) implementsNodes.item(in);
			implementsData.add(implementsNode.getTextContent());
		}
		return implementsData;
	}
	
	
	public Map<String,Object> getArtifactEndData(Element artifactElement ){
		Map<String,Object> endData = new HashMap<String,Object>();
		NodeList endNodes = artifactElement
			.getElementsByTagNameNS(namespace, "associationEnd");
		for (int fn = 0; fn < endNodes.getLength(); fn++) {
			Element endNode = (Element) endNodes.item(fn);

			String comment = getComment(endNode);
			if (endNode.getAttribute("end").equals("AEnd")) {
				endData.put(IArtifactSetFeatureRequest.AENDName,endNode.getAttribute("name"));
				endData.put(IArtifactSetFeatureRequest.AENDAGGREGATION,endNode.getAttribute("aggregation"));
				endData.put(IArtifactSetFeatureRequest.AENDISCHANGEABLE,endNode.getAttribute("changeable"));
				endData.put(IArtifactSetFeatureRequest.AENDMULTIPLICITY,endNode.getAttribute("multiplicity"));
				endData.put(IArtifactSetFeatureRequest.AENDNAVIGABLE,endNode.getAttribute("navigable"));
				endData.put(IArtifactSetFeatureRequest.AENDISORDERED,endNode.getAttribute("ordered"));
				endData.put(IArtifactSetFeatureRequest.AENDVISIBILITY,endNode.getAttribute("visibility"));
				endData.put(IArtifactSetFeatureRequest.AENDISUNIQUE,endNode.getAttribute("unique"));
				if (comment != null){
					endData.put(IArtifactSetFeatureRequest.AENDCOMMENT,comment);
				}
			} else if (endNode.getAttribute("end").equals("ZEnd")) {
				endData.put(IArtifactSetFeatureRequest.ZENDName,endNode.getAttribute("name"));
				endData.put(IArtifactSetFeatureRequest.ZENDAGGREGATION,endNode.getAttribute("aggregation"));
				endData.put(IArtifactSetFeatureRequest.ZENDISCHANGEABLE,endNode.getAttribute("changeable"));
				endData.put(IArtifactSetFeatureRequest.ZENDMULTIPLICITY,endNode.getAttribute("multiplicity"));
				endData.put(IArtifactSetFeatureRequest.ZENDNAVIGABLE,endNode.getAttribute("navigable"));
				endData.put(IArtifactSetFeatureRequest.ZENDISORDERED,endNode.getAttribute("ordered"));
				endData.put(IArtifactSetFeatureRequest.ZENDVISIBILITY,endNode.getAttribute("visibility"));
				endData.put(IArtifactSetFeatureRequest.ZENDISUNIQUE,endNode.getAttribute("unique"));
				if (comment != null){
					endData.put(IArtifactSetFeatureRequest.ZENDCOMMENT,comment);
				}
			}
			
			// TODO - These have no Updater FEATURES at the moment	

//			Collection<IStereotypeInstance> stereos = getStereotypes(endNode);
//			if (stereos.size() > 0){
//				endData.put("stereotypes",stereos);
//			}
		}
	
		return endData;
	}
	
	public Collection<Map<String,Object>> getArtifactFieldData(Element artifactElement ){
		Collection<Map<String,Object>> allFieldData = new ArrayList<Map<String,Object>>();
		
		NodeList fieldNodes = artifactElement
			.getElementsByTagNameNS(namespace, "field");
		for (int fn = 0; fn < fieldNodes.getLength(); fn++) {
			Element field = (Element) fieldNodes.item(fn);
			Map<String,Object> fieldData = new HashMap<String,Object>();

			fieldData.put(IAttributeSetRequest.NAME_FEATURE,field.getAttribute("name"));
			
			fieldData.put(IAttributeSetRequest.TYPE_FEATURE,field.getAttribute("type"));
			
			fieldData.put(IAttributeSetRequest.MULTIPLICITY_FEATURE,field.getAttribute("typeMultiplicity"));

			fieldData.put(IAttributeSetRequest.VISIBILITY_FEATURE,field.getAttribute("visibility"));
			
			if (field.hasAttribute("ordered")) {
				fieldData.put(IAttributeSetRequest.ISORDERED_FEATURE,field.getAttribute("ordered"));
			}

			if (field.hasAttribute("unique")) {
				fieldData.put(IAttributeSetRequest.ISUNIQUE_FEATURE,field.getAttribute("unique"));
			}

			if (field.hasAttribute("defaultValue")) {
				fieldData.put(IAttributeSetRequest.DEFAULTVALUE_FEATURE,field.getAttribute("defaultValue"));
			}

			String comment = getComment(field);
			if (comment != null){
				fieldData.put(IAttributeSetRequest.COMMENT_FEATURE,comment);
			}
			
			// TODO - These have no Updater FEATURES at the moment			
//			fieldData.put("optional",Boolean.parseBoolean(field
//					.getAttribute("optional")));
//			fieldData.put("readOnly",Boolean.parseBoolean(field
//					.getAttribute("readonly")));
//			

//			Collection<IStereotypeInstance> stereos = getStereotypes(field);
//			if (stereos.size() > 0){
//				fieldData.put("stereotypes",stereos);
//			}
			allFieldData.add(fieldData);
		}
		return allFieldData;
	}
	
	public Collection<Map<String,Object>> getArtifactLiteralData(Element artifactElement ){
		Collection<Map<String,Object>> allLiteralData = new ArrayList<Map<String,Object>>();
		
		NodeList literalNodes = artifactElement
			.getElementsByTagNameNS(namespace, "literal");
		for (int fn = 0; fn < literalNodes.getLength(); fn++) {
			Element literal = (Element) literalNodes.item(fn);
			Map<String,Object> literalData = new HashMap<String,Object>();

			literalData.put(ILiteralSetRequest.NAME_FEATURE,literal.getAttribute("name"));
			
			literalData.put(ILiteralSetRequest.TYPE_FEATURE,literal.getAttribute("type"));

			literalData.put(ILiteralSetRequest.VISIBILITY_FEATURE,literal.getAttribute("visibility"));
			
			literalData.put(ILiteralSetRequest.VALUE_FEATURE,literal.getAttribute("value"));

			String comment = getComment(literal);
			if (comment != null){
				literalData.put(ILiteralSetRequest.COMMENT_FEATURE,comment);
			}
			
			// TODO - These have no Updater FEATURES at the moment			

//			Collection<IStereotypeInstance> stereos = getStereotypes(literal);
//			if (stereos.size() > 0){
//				literalData.put("stereotypes",stereos);
//			}
			allLiteralData.add(literalData);
		}
		return allLiteralData;
	}
	
	
	public Collection<Map<String,Object>> getArtifactMethodData(Element artifactElement ){
		Collection<Map<String,Object>> allMethodData = new ArrayList<Map<String,Object>>();
		
		NodeList methodNodes = artifactElement
			.getElementsByTagNameNS(namespace, "method");
		for (int fn = 0; fn < methodNodes.getLength(); fn++) {
			Element method = (Element) methodNodes.item(fn);
			Map<String,Object> methodData = new HashMap<String,Object>();

			methodData.put(IMethodSetRequest.NAME_FEATURE,method.getAttribute("name"));
			
			if (method.hasAttribute("returnType")) {
				methodData.put(IMethodSetRequest.TYPE_FEATURE,method.getAttribute("returnType"));
			} else {
				methodData.put(IMethodSetRequest.TYPE_FEATURE,"void");
			}
			
			methodData.put(IMethodSetRequest.ISVOID_FEATURE,method.getAttribute("isVoid"));
			
			methodData.put(IMethodSetRequest.VISIBILITY_FEATURE,method.getAttribute("visibility"));
			
			methodData.put(IMethodSetRequest.ISABSTRACT_FEATURE,method.getAttribute("isAbstract"));
			
			
			if (method.hasAttribute("returnTypeMultiplicity")) {
				methodData.put(IMethodSetRequest.MULTIPLICITY_FEATURE,method.getAttribute("returnTypeMultiplicity"));
			}
			
			if (method.hasAttribute("methodReturnName")) {
				methodData.put(IMethodSetRequest.RETURNNAME_FEATURE,method.getAttribute("methodReturnName"));
			}
			
			if (method.hasAttribute("defaultReturnValue")) {
				methodData.put(IMethodSetRequest.DEFAULTRETURNVALUE_FEATURE,method.getAttribute("defaultReturnValue"));
			}
			
			if (method.hasAttribute("ordered")) {
				methodData.put(IMethodSetRequest.ISORDERED_FEATURE,method.getAttribute("ordered"));
			}

			if (method.hasAttribute("unique")) {
				methodData.put(IMethodSetRequest.ISUNIQUE_FEATURE,method.getAttribute("unique"));
			}

			String comment = getComment(method);
			if (comment != null){
				methodData.put(IMethodSetRequest.COMMENT_FEATURE,comment);
			}
			
			
			Collection<Map<String,Object>> allArgumentData = new ArrayList<Map<String,Object>>();
			NodeList argumentNodes = method
				.getElementsByTagNameNS(namespace, "argument");
			for (Integer en = 0; en < argumentNodes.getLength(); en++) {
				Map<String,Object> argumentData = new HashMap<String,Object>();
				Element argument = (Element) argumentNodes.item(en);
				argumentData.put(IMethodAddFeatureRequest.ARGUMENT_NAME_FEATURE,argument.getAttribute("name"));
				argumentData.put(IMethodAddFeatureRequest.ARGUMENT_TYPE_FEATURE,argument.getAttribute("type"));
				argumentData.put(IMethodAddFeatureRequest.ARGUMENT_MULTIPLICITY_FEATURE,argument.getAttribute("typeMultiplicty"));
				argumentData.put(IMethodAddFeatureRequest.ARGUMENT_ISUNIQUE_FEATURE,argument.getAttribute("unique"));
				argumentData.put(IMethodAddFeatureRequest.ARGUMENT_ISORDERED_FEATURE,argument.getAttribute("ordered"));
				if (argument.hasAttribute("defaultValue")){
					argumentData.put(IMethodAddFeatureRequest.ARGUMENT_DEFAULTVALUE_FEATURE,argument.getAttribute("defaultValue"));
				}
				String argumentComment = getComment(argument);
				if (argumentComment != null){
					argumentData.put(IMethodAddFeatureRequest.ARGUMENT_COMMENT_FEATURE,argumentComment);
				}
				// This add should be used carefully to make sure the arguments are added in the correct order!
				allArgumentData.add(argumentData);
			}
			if (allArgumentData.size() > 0){
				methodData.put(IMethodAddFeatureRequest.ARGUMENTS_FEATURE,allArgumentData);
			}

			
			Collection<String> exceptionData = new ArrayList<String>();
			NodeList exceptionNodes = method
				.getElementsByTagNameNS(namespace, "exception");
			for (int en = 0; en < exceptionNodes.getLength(); en++) {
				Element exception = (Element) exceptionNodes.item(en);
				exceptionData.add(exception.getAttribute("name"));
			}
			if (exceptionData.size() > 0){
				methodData.put(IMethodAddFeatureRequest.EXCEPTIONS_FEATURE,exceptionData);
			}
			
			
			
			
			
			// TODO - These have no Updater FEATURES at the moment			
//			
//			
//			Collection<IStereotypeInstance> stereos = getStereotypes(method);
//			if (stereos.size() > 0){
//				methodData.put("stereotypes",stereos);
//			}
			allMethodData.add(methodData);
		}
		return allMethodData;
	}
	
	public String getComment(Element element) {
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			// Assume valid XML - at most one comment element!
			if (childNodes.item(i) instanceof Element) {
				Element child = (Element) childNodes.item(i);
				if (child.getLocalName().equals("comment")) {
					String comment = child.getTextContent();
					return comment;
				}
			}
		}
		return null;
	}
	
	/**
	 * Find the sterotypes for this "thing"
	 * 
	 * @param element
	 * @return
	 */
	private Collection<IStereotypeInstance> getStereotypes(Element element) {
		Collection<IStereotypeInstance> isis = new ArrayList<IStereotypeInstance>();
		NodeList groupNodes = element.getChildNodes();
		for (int g = 0; g < groupNodes.getLength(); g++) {
			// Assume valid XML - at most one comment element!
			if (groupNodes.item(g) instanceof Element) {
				Element groupElement = (Element) groupNodes.item(g);
				if (groupElement.getLocalName().equals("stereotypes")) {

					NodeList childNodes = groupElement.getChildNodes();
					for (int i = 0; i < childNodes.getLength(); i++) {
						// Assume valid XML - at most one comment element!
						if (childNodes.item(i) instanceof Element) {
							Element stereoElement = (Element) childNodes
									.item(i);
							if (stereoElement.getLocalName().equals(
									"stereotype")) {
								IStereotype tsStereo = this.profileSession
										.getActiveProfile()
										.getStereotypeByName(
												stereoElement
														.getAttribute("name"));

								// TigerstripeRuntime.logInfoMessage(stereoElement.getAttribute("name"));

								if (tsStereo == null) {
									String msgText = "No Stereotype in Current Profile of name :"
											+ stereoElement
													.getAttribute("name");
									addMessage(messages, msgText, 0);
									print(out,msgText, 0);
									continue;
								}
								IStereotypeInstance tsStereoInstance = tsStereo
										.makeInstance();
								NodeList stereotypeAttributeNodes = stereoElement
										.getElementsByTagNameNS(namespace,
												"stereotypeAttribute");
								for (int sa = 0; sa < stereotypeAttributeNodes
										.getLength(); sa++) {
									Element stereoAttributeElement = (Element) stereotypeAttributeNodes
											.item(sa);
									IStereotypeAttribute att = tsStereo
											.getAttributeByName(stereoAttributeElement
													.getAttribute("name"));
									if (att != null) {
										try {
											// / New stuff
											NodeList stereotypeAttributeValueNodes = stereoAttributeElement
													.getElementsByTagNameNS(
															namespace, "value");

											if ((Boolean
													.parseBoolean(stereoAttributeElement
															.getAttribute("array")))) {
												// can be any number of value
												// elements
												String[] vals = new String[stereotypeAttributeValueNodes
														.getLength()];
												for (int sav = 0; sav < stereotypeAttributeValueNodes
														.getLength(); sav++) {
													Element stereotypeAttributeValue = (Element) stereotypeAttributeValueNodes
															.item(sav);
													vals[sav] = stereotypeAttributeValue
															.getTextContent();
												}
												tsStereoInstance
														.setAttributeValues(
																att, vals);
											} else {
												// Should only be one value
												// element - take the first
												// one...
												Element stereotypeAttributeValue = (Element) stereotypeAttributeValueNodes
														.item(0);
												String val = stereotypeAttributeValue
														.getTextContent();
												tsStereoInstance
														.setAttributeValue(att,
																val);
											}
											// / end of new stuff

										} catch (Exception e) {
											String msgText = "Failed to set Stereotype Attribute :"
													+ stereoAttributeElement
															.getAttribute("name");
											addMessage(messages, msgText, 0);
											print(out,msgText, 0);
											continue;
										}
									}
								}
								isis.add(tsStereoInstance);
							}
						}
					}
				}
			}
		}

		return isis;
	}
	
	private void addMessage(MessageList messages, String msgText, int severity) {
		if (this.messages != null){
			Message newMsg = new Message();
			newMsg.setMessage(msgText);
			newMsg.setSeverity(severity);
			messages.addMessage(newMsg);
		}
	}
	private void print(PrintWriter out,String msgText, int severity){
		String prefix;
		switch (severity){
		case 0: prefix = "ERROR";
		break;
		case 1: prefix = "WARN";
		break;
		case 2: prefix = "INFO";
		break;
		case 3: prefix = "DEBUG";
		break;
		case 4: prefix = "TRACE";
		break;
		default :prefix = "ERROR";
		};
		if (out != null){
			out.println("Error : " + msgText);
		}
	}
}
