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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.mapper;

import java.util.List;
import java.util.ListIterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.impl.NameProviderImpl;
import org.eclipse.tigerstripe.workbench.internal.api.project.INameProvider;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableDatatype;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.mapper.UmlDatatypeMapper;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.annotables.UML2AnnotableAssociation;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.annotables.UML2AnnotableAssociationEnd;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.annotables.UML2AnnotableDatatype;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.annotables.UML2AnnotableElement;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.annotables.UML2AnnotableElementAttribute;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.annotables.UML2AnnotableElementConstant;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.annotables.UML2AnnotableElementOperationException;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.annotables.UML2AnnotableElementOperationParameter;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.annotables.UML2AnnotableOperation;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.model.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd.EChangeableEnum;
import org.eclipse.tigerstripe.workbench.model.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.model.artifacts.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.BehavioralFeature;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.VisibilityKind;

public class UML2MappingUtils {

	private String DEFAULT_MULTIPLICITY = "0..1";

	private String modelLibrary;

	private MessageList messageList;

	private UmlDatatypeMapper umlDatatypeMapper;

	private ITigerstripeModelProject targetProject;

	public UML2MappingUtils(String modelLibrary, MessageList messageList,
			ITigerstripeModelProject targetProject) {
		this.modelLibrary = modelLibrary;
		this.messageList = messageList;
		this.targetProject = targetProject;

		try {
			umlDatatypeMapper = new UmlDatatypeMapper(targetProject);
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
	}

	public ITigerstripeModelProject getTargetProject() {
		return this.targetProject;
	}

	public String getComment(NamedElement element) {
		String comment = null;
		List children = element.getOwnedElements();
		// Look for a child of type comment.
		ListIterator it = children.listIterator();
		while (it.hasNext()) {
			EObject child = (EObject) it.next();
			if (child instanceof Comment) {
				Comment comm = (Comment) child;
				comment = comm.getBody();
			}
		}
		if (comment != null)
			return comment;
		return null;
	}

	public void setAttributes(UML2AnnotableElement annotable,
			NamedElement element) {
		List children = element.getOwnedElements();
		// Look for a child of type Property.
		// Need to dissociate these from Association things - eg OwnedEnd

		ListIterator it = children.listIterator();
		while (it.hasNext()) {
			EObject child = (EObject) it.next();
			if (child instanceof Property) {
				Property property = (Property) child;
				// this.out.println(property.getName());
				if (property.getAssociation() != null) {
					// This ain't an attribute , but part of an association, so
					// skip it here
					// this.out
					// .println(" Skipping property - it's as assoc. thing");
					continue;
				}

				UML2AnnotableElementAttribute attribute = new UML2AnnotableElementAttribute(
						ValidIdentifiersUtils.getValidAttributeName(
								messageList, property.getName()));

				Type propType = property.getType();
				UML2AnnotableDatatype type = null;
				if (propType != null) {
					String rawName = convertToFQN(propType.getQualifiedName());
					String validName = ValidIdentifiersUtils
							.getValidArtifactName(new MessageList(), Util
									.nameOf(rawName));
					String validPackage = ValidIdentifiersUtils
							.getValidPackageName(new MessageList(), Util
									.packageOf(rawName), getTargetProject(),
									rawName);
					if (validPackage != null && validPackage.length() != 0) {
						validName = validPackage + "." + validName;
					}
					type = new UML2AnnotableDatatype(validName);
				} else {
					type = new UML2AnnotableDatatype("String");
					Message msg = new Message();
					msg.setMessage("Undefined attribute type for '"
							+ property.getName() + "' in " + element.getName());
					msg.setSeverity(Message.WARNING);
					messageList.addMessage(msg);
				}
				attribute.setType(type);

				// if (!setTypeDetails(type, propType,
				// (MultiplicityElement) child, artifact.getName()+" : "+
				// field.getName())){
				if (!setTypeDetails(type, propType,
						(MultiplicityElement) child, annotable.getName() + "::"
								+ attribute.getName())) {
					continue;
				}

				// field
				// .setOptional(getOptional(type,
				// (MultiplicityElement) child));

				// this.out.println(" Property : " + property.getName() + " : "
				// + type.getFullyQualifiedName());
				// field.setIType(type);

				attribute.setDescription(getComment((NamedElement) child));

				// Not sure our int values are the same as in UML
				int umlVis = property.getVisibility().getValue();
				int tsVis = 0;
				switch (umlVis) {
				case VisibilityKind.PUBLIC:
					tsVis = EVisibility.indexOf(EVisibility.PUBLIC);
					break;
				case VisibilityKind.PRIVATE:
					tsVis = EVisibility.indexOf(EVisibility.PRIVATE);
					break;
				case VisibilityKind.PROTECTED:
					tsVis = EVisibility.indexOf(EVisibility.PROTECTED);
					break;
				case VisibilityKind.PACKAGE:
					tsVis = EVisibility.indexOf(EVisibility.PACKAGE);
					break;
				}
				attribute.setVisibility(tsVis);
				/*
				 * DOn't thinnk this is valid - cf EMPTY vs NOT-PRESENT? if
				 * (multi.startsWith("0")){ field.setOptional(true); }
				 */

				// Visibility is always public - and that is default so no
				// action here?
				// Other bits like RefBy, Optional, ReadOnly?
				// RefBy not to be used
				// ReadOnly is not set anywhere in the model IMHO
				// In the model there are default values for some stuff?
				// And stereotypes.....
				// ArrayList stInstances = readStereotypes((NamedElement)
				// child);
				//
				// ListIterator stereoIt = stInstances.listIterator();
				// while (stereoIt.hasNext()) {
				// IStereotypeInstance iSI = (IStereotypeInstance) stereoIt
				// .next();
				// // TODO Put this in....
				// field.addStereotypeInstance(iSI);
				// }
				annotable.addAnnotableElementAttribute(attribute);
			}
		}

	}

	private boolean setTypeDetails(UML2AnnotableDatatype iType, Type uType,
			MultiplicityElement param, String location) {
		return setTypeDetails(iType, uType, param, location, true);
	}

	private boolean setTypeDetails(UML2AnnotableDatatype iType, Type uType,
			MultiplicityElement param, String location, boolean setMulti) {
		boolean optional = true;

		String pTypeFQN = null;
		if ((uType == null) || (uType.getQualifiedName() == null)) {
			// TODO - Is this an error in the model.
			String msgText = "Could not resolve type of: " + location
					+ " setting to 'String'";
			Message msg = new Message();
			msg.setMessage(msgText);
			msg.setSeverity(Message.WARNING);
			messageList.addMessage(msg);
			// this.out.println("Error : " + msgText);
			pTypeFQN = "String";
		} else {
			// Need to determine if this is a primitive type that we know about
			pTypeFQN = convertToFQN(uType.getQualifiedName());

			if (modelLibrary != null) {
				if (uType.getQualifiedName().startsWith(this.modelLibrary)) {
					int indexOfDot = pTypeFQN.indexOf(".");
					if (indexOfDot != -1) {
						pTypeFQN = pTypeFQN.substring(indexOfDot + 1);
						if (!checkReservedPrimitive(pTypeFQN)) {
							pTypeFQN = ITigerstripeConstants.BASEPRIMITIVE_PACKAGE
									+ "." + pTypeFQN;
						}
					}
				}
			}
		}

		String rawName = pTypeFQN;
		String validName = ValidIdentifiersUtils.getValidArtifactName(
				new MessageList(), Util.nameOf(rawName));
		String validPackage = ValidIdentifiersUtils.getValidPackageName(
				new MessageList(), Util.packageOf(rawName), getTargetProject(),
				rawName);
		if (validPackage != null && validPackage.length() != 0) {
			validName = validPackage + "." + validName;
		}

		iType.setName(validName);
		if (setMulti) {
			optional = setMultiplicityNew(iType, param);
		}
		return true;
	}

	private boolean setMultiplicityNew(UML2AnnotableDatatype type,
			MultiplicityElement property) {

		String upper = "";
		int multi = IType.MULTIPLICITY_SINGLE;
		String lower = "";
		boolean optional = true;

		ValueSpecification upperVal = property.getUpperValue();
		ValueSpecification lowerVal = property.getLowerValue();

		if (upperVal != null) {
			upper = "" + property.getUpper();
			if (upper.equals("-1")) {
				multi = IType.MULTIPLICITY_MULTI;
			} else {
				multi = IType.MULTIPLICITY_SINGLE;
			}
		}

		if (lowerVal != null) {
			lower = "" + property.getLower();
			if (lower.equals("0")) {
				// Can't ever see this happening...
				optional = true;
			} else {
				optional = false;
			}
		}
		type.setMultiplicity(multi);
		return optional;
	}

	public void setOperations(UML2AnnotableElement artifact,
			NamedElement element) {
		List children = element.getOwnedElements();

		ListIterator it = children.listIterator();
		while (it.hasNext()) {
			EObject child = (EObject) it.next();
			if (child instanceof Operation) {
				Operation operation = (Operation) child;

				// this.out.println(" Operation :" + operation.getName());
				UML2AnnotableOperation op = new UML2AnnotableOperation(
						ValidIdentifiersUtils.getValidOperationName(
								messageList, operation.getName()));
				op.setDescription(getComment(((NamedElement) child)));
				op.setAbstract(operation.isAbstract());
				// Not sure our int values are the same as in UML
				int umlVis = operation.getVisibility().getValue();
				int tsVis = 0;
				switch (umlVis) {
				case VisibilityKind.PUBLIC:
					tsVis = EVisibility.indexOf(EVisibility.PUBLIC);
					break;
				case VisibilityKind.PRIVATE:
					tsVis = EVisibility.indexOf(EVisibility.PRIVATE);
					break;
				case VisibilityKind.PROTECTED:
					tsVis = EVisibility.indexOf(EVisibility.PROTECTED);
					break;
				case VisibilityKind.PACKAGE:
					tsVis = EVisibility.indexOf(EVisibility.PACKAGE);
					break;
				}
				op.setVisibility(tsVis);

				// ============ return type =====================
				Parameter returnResult = operation.getReturnResult();
				if (returnResult != null && returnResult.getType() != null) {
					Type retType = returnResult.getType();

					UML2AnnotableDatatype iType = new UML2AnnotableDatatype();

					if (!setTypeDetails(iType, retType, returnResult, artifact
							.getName()
							+ " : " + returnResult.getName())) {
						continue;
					}
					// this.out.println(" Operation return : " +
					// operation.getName()
					// + " : " + iType.getFullyQualifiedName());
					op.setReturnType(iType);
				} else {
					op.setReturnType(new UML2AnnotableDatatype("void"));
				}

				// ============ parameters =====================
				BehavioralFeature bF = (BehavioralFeature) child;
				List params = bF.getOwnedParameters();
				ListIterator paramIt = params.listIterator();
				while (paramIt.hasNext()) {
					Parameter param = (Parameter) paramIt.next();

					if (param.getDirection().getValue() != ParameterDirectionKind.IN) {
						// This is the return, so ignore it.
						continue;
					}
					UML2AnnotableElementOperationParameter arg = new UML2AnnotableElementOperationParameter(
							ValidIdentifiersUtils.getValidParameterName(
									messageList, param.getName()));

					UML2AnnotableDatatype aType = new UML2AnnotableDatatype();
					Type pType = param.getType();
					if (!setTypeDetails(aType, pType, param, artifact.getName()
							+ " : " + param.getName())) {
						continue;
					}
					// this.out.println(" Operation parameter : "
					// + operation.getName() + " : " + param.getName()
					// + " : " + aType.getFullyQualifiedName());
					arg.setType(aType);

					// TODO The model might have stereos on these. TS can't yet
					// handle

					// Add some comments (now that we can!)
					arg.setDescription(getComment(param));

					op.addAnnotableElementOperationParameter(arg);
				}

				// Any Exceptions?
				List exceptions = bF.getRaisedExceptions();
				ListIterator excepIt = exceptions.listIterator();
				while (excepIt.hasNext()) {
					org.eclipse.uml2.uml.Class excep = (org.eclipse.uml2.uml.Class) excepIt
							.next();
					String excepFQN = convertToFQN(((NamedElement) excep)
							.getQualifiedName());
					String rawName = excepFQN;
					String validName = ValidIdentifiersUtils
							.getValidArtifactName(messageList, Util
									.nameOf(rawName));
					String validPackage = ValidIdentifiersUtils
							.getValidPackageName(new MessageList(), Util
									.packageOf(rawName), getTargetProject(),
									excepFQN);
					if (validPackage != null && validPackage.length() != 0) {
						validName = validPackage + "." + validName;
					}

					UML2AnnotableElementOperationException tsExcep = new UML2AnnotableElementOperationException(
							validName);
					op.addAnnotableElementOperationException(tsExcep);
				}

				// // "Abstract" Must be in profile - will Fail otherwise...
				// IStereotype tsStereo = this.profileSession.getActiveProfile()
				// .getStereotypeByName("Abstract");
				// IStereotypeInstance tsStereoInstance =
				// tsStereo.makeInstance();
				// IStereotypeAttribute[] stereoAttributes = tsStereo
				// .getAttributes();
				// for (int a = 0; a < stereoAttributes.length; a++) {
				// IStereotypeAttribute attribute = (IStereotypeAttribute)
				// stereoAttributes[a];
				// if (attribute.getName().equals("isAbstract")) {
				// try {
				// tsStereoInstance.setAttributeValue(attribute,
				// (((Classifier) element).isAbstract() ? "0"
				// : "1"));
				// } catch (TigerstripeException t) {
				// TigerstripeRuntime.logInfoMessage("Failed to set Attribute to
				// Abstract");
				// TigerstripeRuntime.logErrorMessage("TigerstripeException
				// detected", t);
				// }
				// }
				// }
				//
				// method.addStereotypeInstance(tsStereoInstance);

				// ArrayList stInstances = readStereotypes((NamedElement)
				// child);
				// ListIterator stereoIt = stInstances.listIterator();
				// while (stereoIt.hasNext()) {
				// IStereotypeInstance iSI = (IStereotypeInstance) stereoIt
				// .next();
				// method.addStereotypeInstance(iSI);
				// }

				artifact.addAnnotableElementOperation(op);
			}
		}
	}

	public void setAssociationEnds(UML2AnnotableAssociation annotable,
			NamedElement element) {

		if (element instanceof Association) {
			Association assoc = (Association) element;

			List mEndTypes = assoc.getMemberEnds();
			if (assoc.getMemberEnds().size() != 2) {
				String msgText = "Association No of Ends != 2 "
						+ annotable.getName();
				Message msg = new Message();
				msg.setMessage(msgText);
				msg.setSeverity(Message.ERROR);
				messageList.addMessage(msg);
			}
			ListIterator mEndTypesIt = mEndTypes.listIterator();
			boolean first = true;
			while (mEndTypesIt.hasNext()) {

				Property mEnd = (Property) mEndTypesIt.next();
				// this.out.println(" End " + mEnd.getName() + " "
				// + mEnd.getType().getName());
				UML2AnnotableAssociationEnd end = new UML2AnnotableAssociationEnd(
						nameCheck(mEnd.getName()));

				UML2AnnotableDatatype type = new UML2AnnotableDatatype();

				if (!setTypeDetails(type, mEnd.getType(), mEnd, annotable
						.getName()
						+ " : " + mEnd.getName(), true)) {
					continue;
					// This will go wrong as the Association needs to have a
					// type at both ends...
				}
				end.setType(type);

				if (nameCheck(mEnd.getName()) != null) {
					end.setName(nameCheck(mEnd.getName()));
				} else {
					String msgText = "Association End has no name : "
							+ annotable.getName();
					Message msg = new Message();
					msg.setMessage(msgText);
					msg.setSeverity(Message.WARNING);
					messageList.addMessage(msg);
					// this.out.println("Warning : " + msgText);
					// Set a default name
					if (first) {
						end.setName("_aEnd");
					} else {
						end.setName("_zEnd");
					}
				}

				// Set aggregation, changeable and mulitplicity

				AggregationKind agg = mEnd.getAggregation();
				end.setAggregation(EAggregationEnum.parse(agg.getName()));

				// TODO - I don't know how to find this in the model ........
				end.setChangeable(EChangeableEnum.parse("none"));

				String multStr = readMultiplicity(mEnd);
				if (IModelComponent.EMultiplicity.parse(multStr) != null) {
					end.setEndMultiplicity(IModelComponent.EMultiplicity.parse(multStr));
				} else {
					end.setEndMultiplicity(IModelComponent.EMultiplicity.ONE_STAR);
					Message msg = new Message();
					msg.setMessage("Multiplicity '" + multStr
							+ "' not supported for " + annotable.getName()
							+ ". Reverting to '1..*'.");
					msg.setSeverity(Message.WARNING);
					messageList.addMessage(msg);
				}

				// navigable and ordered
				end.setNavigable(mEnd.isNavigable());
				end.setOrdered(mEnd.isOrdered());

				// Not sure our int values are the same as in UML
				int umlVis = mEnd.getVisibility().getValue();
				int tsVis = 0;
				switch (umlVis) {
				case VisibilityKind.PUBLIC:
					tsVis = EVisibility.indexOf(EVisibility.PUBLIC);
					break;
				case VisibilityKind.PRIVATE:
					tsVis = EVisibility.indexOf(EVisibility.PRIVATE);
					break;
				case VisibilityKind.PROTECTED:
					tsVis = EVisibility.indexOf(EVisibility.PROTECTED);
					break;
				case VisibilityKind.PACKAGE:
					tsVis = EVisibility.indexOf(EVisibility.PACKAGE);
					break;
				}
				end.setVisibility(tsVis);

				if (first) {
					annotable.setAEnd(end);
					first = false;
				} else {

					if (annotable.getAEnd().getName().equals(end.getName())) {
						end.setName(end.getName() + "_");
					}
					annotable.setZEnd(end);
				}

			}

		}

	}

	private String readMultiplicity(MultiplicityElement property) {

		// 0..1
		// 1..1
		// 0..*
		// 1..*
		// make it something like one of these ..
		// ONE("1"), ZERO("0"), ZERO_ONE("0..1"), ZERO_STAR("0..*"),
		// ONE_STAR("1..*"), STAR("*");
		String multiplicityString = "";
		String upper = "";
		String lower = "";

		ValueSpecification upperVal = property.getUpperValue();
		ValueSpecification lowerVal = property.getLowerValue();

		if (upperVal != null) {
			upper = "" + property.getUpper();
			if (upper.equals("-1")) {
				upper = "*";
			}
		}

		if (lowerVal != null) {
			lower = "" + property.getLower();
			if (lower.equals("-1")) {
				// Can't ever see this happening...
				lower = "*";
			}
		}

		if ((upperVal != null) && (lowerVal != null)) {
			// Lets say it's an array...
			multiplicityString = lower + ".." + upper;
		} else {
			// One is blank so just add them together..
			multiplicityString = lower + upper;
		}

		if (multiplicityString.equals("")) {
			// Set it to some default
			multiplicityString = DEFAULT_MULTIPLICITY;
		}

		if (multiplicityString.equals("1..1"))
			multiplicityString = "1";
		// this.out.println(" Multiplicity -> " + multiplicityString);
		return multiplicityString;
	}

	public void setConstants(UML2AnnotableElement annotable,
			NamedElement element) {
		// Can't see many in model - only Enums

		// giving any without a Value a default value
		String cType = getConstantsType(element);

		// better just do this for Enums
		List children = element.getOwnedElements();

		ListIterator it = children.listIterator();
		while (it.hasNext()) {
			EObject child = (EObject) it.next();
			if (child instanceof EnumerationLiteral) {
				EnumerationLiteral enumLit = (EnumerationLiteral) child;
				// Might have an EnumValue stereotype ?

				UML2AnnotableElementConstant constant = new UML2AnnotableElementConstant(
						nameCheck(enumLit.getName()));
				UML2AnnotableDatatype type = new UML2AnnotableDatatype(cType);
				constant.setType(type);
				annotable.addAnnotableElementConstant(constant);

				// If String set the label to the name by default - may be
				// overridden by an EnumVal
				// otherwise if int set it to -1
				if (cType.equals("String")) {
					constant.setValue("\"" + constant.getName() + "\"");
				} else {
					constant.setValue(Integer.toString(-1));
				}

				ValueSpecification spec = enumLit.getSpecification();
				if (spec == null) {
					constant.setValue("0");
					String msgText = "Unspecified constant value "
							+ constant.getName() + " using '-1'.";
					Message msg = new Message();
					msg.setMessage(msgText);
					msg.setSeverity(Message.WARNING);
					messageList.addMessage(msg);
					constant.setValue("-1");
				} else
					constant.setValue(spec.stringValue());

				AnnotableDatatype datatype = constant.getType();
				if (spec instanceof LiteralString) {
					datatype.setName("String");
					constant.setType(datatype);
				} else if (spec instanceof LiteralInteger) {
					datatype.setName("int");
					constant.setType(datatype);
				}

				List appliedStereotypes = ((NamedElement) child)
						.getAppliedStereotypes();
				if (appliedStereotypes.size() > 0) {

					int count = 0;
					for (int s = 0; s < appliedStereotypes.size(); s++) {
						Stereotype stereo = (Stereotype) appliedStereotypes
								.get(s);
						List stereoAttributes = stereo.getAllAttributes();
						for (int a = 0; a < stereoAttributes.size(); a++) {
							Property attribute = (Property) stereoAttributes
									.get(a);

							if (!attribute.getName().startsWith("base_")) {
								// if ((element.getValue(stereo,
								// attribute.getName()) != null) ){
								// this.out.println(" Enum Stereo Attribute : "
								// + stereo.getName()
								// + ":"
								// + attribute.getName()
								// + " -> "
								// + ((NamedElement) child).getValue(
								// stereo, attribute.getName()));
								count++;
								constant.setValue(((NamedElement) child)
										.getValue(stereo, attribute.getName())
										.toString());
								// These might be string or int ONLY , so need
								// to set the type of the label accordingly.
								if (attribute.getType().getName().equals(
										"Integer")) {
									type.setName("int");
								} else if (!attribute.getType().getName()
										.equals("string")) {
									String msgText = "Unhandled type for Enum Value : "
											+ stereo.getName()
											+ ":"
											+ attribute.getName()
											+ " -> "
											+ attribute.getType().getName();
									Message msg = new Message();
									msg.setMessage(msgText);
									msg.setSeverity(Message.ERROR);
								}
							}

							if (count == 0) {
								// We've handled no other attributes,so
								// This is an "existence" type of Stereotype
								// so it's mere presence is important
								// We have given the user a choice in the
								// profile stuff so our
								// behaviour needs to be mildly intelligent.
								// this.out.println(" Enum Attribute : "
								// + stereo.getName()
								// + ":Existence --> true");
							}

						}
					}
				}
			}
		}

	}

	private String getConstantsType(NamedElement element) {
		// See if anything has a Value, then setting All of them to that,
		// giving any without a Value a default value

		List children = element.getOwnedElements();
		ListIterator it = children.listIterator();
		while (it.hasNext()) {
			EObject child = (EObject) it.next();
			if (child instanceof EnumerationLiteral) {
				List appliedStereotypes = ((NamedElement) child)
						.getAppliedStereotypes();
				if (appliedStereotypes.size() > 0) {

					for (int s = 0; s < appliedStereotypes.size(); s++) {
						Stereotype stereo = (Stereotype) appliedStereotypes
								.get(s);
						List stereoAttributes = stereo.getAllAttributes();
						for (int a = 0; a < stereoAttributes.size(); a++) {
							Property attribute = (Property) stereoAttributes
									.get(a);

							if (!attribute.getName().startsWith("base_")) {
								if (attribute.getType().getName().equals(
										"Integer"))
									return "int";
								else if (!attribute.getType().getName().equals(
										"string")) {
									String msgText = "Unhandled type for Enum Value "
											+ stereo.getName()
											+ ":"
											+ attribute.getName()
											+ " -> "
											+ attribute.getType().getName();
									Message msg = new Message();
									msg.setMessage(msgText);
									msg.setSeverity(Message.ERROR);
									messageList.addMessage(msg);
									return "int";
								}
							}
						}
					}
				}
			}
		}
		return "String";
	}

	private boolean checkReservedPrimitive(String name) {

		String[][] res = IPrimitiveTypeArtifact.reservedPrimitiveTypes;
		for (int p = 0; p < res.length; p++) {
			String resName = res[p][0];
			if (resName.equals(name))
				return true;
		}
		return false;
	}

	/**
	 * check for invalid constructs
	 * 
	 * eg "." in name - map to "_" "-" in name - map to "_" " " in name - map to
	 * "_"
	 * 
	 * 
	 * 
	 * @param name
	 * @return
	 */
	private String nameCheck(String name) {
		if (name != null) {
			String inName = name;
			if (inName.contains(" ")) {
				inName = inName.replaceAll(" ", "_");
			}
			if (inName.contains("-")) {
				inName = inName.replaceAll("-", "_");
			}
			if (inName.contains(".")) {
				inName = inName.replaceAll("\\.", "_");
			}

			if (!inName.equals(name)) {
				String msgText = " Name mapped : " + name + " -> " + inName;
				Message msg = new Message();
				msg.setMessage(msgText);
				msg.setSeverity(Message.WARNING);
				messageList.addMessage(msg);
				// this.out.println("Warning:" + msgText);
			}

			return inName;
		} else
			return null;
	}

	/**
	 * map naming to TS compatible style
	 * 
	 */
	public static String convertToFQN(String name) {
		if (name != null) {
			String dottedName = name.replaceAll("::", ".");
			// remove the model name
			return dottedName.substring(dottedName.indexOf(".") + 1);
		} else
			return null;
	}

	private NameProviderImpl localNameProvider;

	public INameProvider getNameProvider() {
		if (localNameProvider == null) {
			localNameProvider = new NameProviderImpl(getTargetProject());
		}
		return localNameProvider;
	}

}
