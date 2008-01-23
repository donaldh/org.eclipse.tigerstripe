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
package org.eclipse.tigerstripe.tools.compare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.model.IAssociationEnd;
import org.eclipse.tigerstripe.api.model.IField;
import org.eclipse.tigerstripe.api.model.ILabel;
import org.eclipse.tigerstripe.api.model.IMethod;
import org.eclipse.tigerstripe.api.model.IModelComponent;
import org.eclipse.tigerstripe.api.model.IMethod.IArgument;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IAssociationClassArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IDatatypeArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IDependencyArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IEnumArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IEventArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IExceptionArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IManagedEntityArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IQueryArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.ISessionArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.ossj.IOssjArtifactSpecifics;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.core.model.ArtifactManager;
import org.eclipse.tigerstripe.core.model.ossj.specifics.OssjArtifactSpecifics;
import org.eclipse.tigerstripe.core.module.ModuleArtifactManager;
import org.eclipse.tigerstripe.core.project.TigerstripeProject;

public class CompareUtils {

	public static ArrayList<Difference> compareExtends(
			IAbstractArtifact aArtifact, IAbstractArtifact bArtifact) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		String aExt;
		String bExt;
		IAbstractArtifact aExtends = aArtifact.getExtendedIArtifact();
		IAbstractArtifact bExtends = bArtifact.getExtendedIArtifact();
		if (aExtends != null) {
			aExt = aExtends.getFullyQualifiedName();
		} else {
			aExt = "";
		}
		if (aArtifact instanceof IExceptionArtifact) {
			if (aExt.equals(Exception.class.getName())) {
				aExt = "";
			}
		}
		if (bExtends != null) {
			bExt = bExtends.getFullyQualifiedName();
		} else {
			bExt = "";
		}
		if (bArtifact instanceof IExceptionArtifact) {
			if (bExt.equals(Exception.class.getName())) {
				bExt = "";
			}
		}
		if (!aExt.equals(bExt)) {
			differences.add(new Difference("value", aArtifact
					.getFullyQualifiedName(),
					bArtifact.getFullyQualifiedName(), "Artifact:Extends", "",
					aExt, bExt));
		}
		return differences;
	}

	public static ArrayList<Difference> compareInterface(
			IAbstractArtifact aArtifact, IAbstractArtifact bArtifact) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		IOssjArtifactSpecifics aSpec = (IOssjArtifactSpecifics) aArtifact
				.getIStandardSpecifics();
		Properties aProp = aSpec.getInterfaceProperties();
		IOssjArtifactSpecifics bSpec = (IOssjArtifactSpecifics) bArtifact
				.getIStandardSpecifics();
		Properties bProp = bSpec.getInterfaceProperties();
		differences.addAll(compareProps(aArtifact, bArtifact, "interface",
				aProp, bProp));
		return differences;
	}

	public static ArrayList<Difference> compareAssociationEnds(
			IAssociationArtifact aArtifact, IAssociationArtifact bArtifact) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		IAssociationEnd aAEnd = aArtifact.getAEnd();
		IAssociationEnd bAEnd = bArtifact.getAEnd();
		differences.addAll(compareAssociationEnd(aAEnd, bAEnd, "A"));
		IAssociationEnd aZEnd = aArtifact.getZEnd();
		IAssociationEnd bZEnd = bArtifact.getZEnd();
		differences.addAll(compareAssociationEnd(aZEnd, bZEnd, "Z"));
		return differences;
	}

	public static ArrayList<Difference> compareAssociationEnd(
			IAssociationEnd aEnd, IAssociationEnd bEnd, String aORz) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		String aName = aEnd.getName();
		String bName = bEnd.getName();
		if (!aName.equals(bName)) {
			// Name mismatch
			differences
					.add(new Difference("value", aEnd.getContainingArtifact()
							.getFullyQualifiedName(), bEnd
							.getContainingArtifact().getFullyQualifiedName(),
							"Association:AssociationEnd:" + aORz, "Name",
							aName, bName));
		}
		if (!aEnd.getIType().getFullyQualifiedName().equals(
				bEnd.getIType().getFullyQualifiedName())) {
			// compare Type
			differences.add(new Difference("value", aEnd
					.getContainingArtifact().getFullyQualifiedName(), bEnd
					.getContainingArtifact().getFullyQualifiedName(),
					"Association:AssociationEnd:" + aORz + ":Type", aName, aEnd
							.getIType().getFullyQualifiedName(), bEnd.getIType()
							.getFullyQualifiedName()));
		}
		if (aEnd.getMultiplicity() != bEnd.getMultiplicity()) {
			// compare Multiplicity
			differences.add(new Difference("value", aEnd
					.getContainingArtifact().getFullyQualifiedName(), bEnd
					.getContainingArtifact().getFullyQualifiedName(),
					"Association:AssociationEnd:" + aORz + ":Multiplicity",
					aName, aEnd.getMultiplicity().getLabel(), bEnd
							.getMultiplicity().getLabel()));
		}
		if (aEnd.getChangeable() != bEnd.getChangeable()) {
			// compare Changeable
			differences.add(new Difference("value", aEnd
					.getContainingArtifact().getFullyQualifiedName(), bEnd
					.getContainingArtifact().getFullyQualifiedName(),
					"Association:AssociationEnd:" + aORz + ":Changeable",
					aName, aEnd.getChangeable().getLabel(), bEnd
							.getChangeable().getLabel()));
		}
		if (aEnd.getAggregation() != bEnd.getAggregation()) {
			// compare Aggregation
			differences.add(new Difference("value", aEnd
					.getContainingArtifact().getFullyQualifiedName(), bEnd
					.getContainingArtifact().getFullyQualifiedName(),
					"Association:AssociationEnd:" + aORz + ":Aggregation",
					aName, aEnd.getAggregation().getLabel(), bEnd
							.getAggregation().getLabel()));
		}

		if (aEnd.isNavigable() != bEnd.isNavigable()) {
			// compare Navigable
			differences.add(new Difference("value", aEnd
					.getContainingArtifact().getFullyQualifiedName(), bEnd
					.getContainingArtifact().getFullyQualifiedName(),
					"Association:AssociationEnd:" + aORz + ":Navigable", aName,
					Boolean.toString(aEnd.isNavigable()), Boolean.toString(bEnd
							.isNavigable())));
		}
		if (aEnd.isOrdered() != bEnd.isOrdered()) {
			// compare Navigable
			differences.add(new Difference("value", aEnd
					.getContainingArtifact().getFullyQualifiedName(), bEnd
					.getContainingArtifact().getFullyQualifiedName(),
					"Association:AssociationEnd:" + aORz + ":Ordered", aName,
					Boolean.toString(aEnd.isOrdered()), Boolean.toString(bEnd
							.isOrdered())));
		}
		if (aEnd.isUnique() != bEnd.isUnique()) {
			// compare Unique
			differences.add(new Difference("value", aEnd
					.getContainingArtifact().getFullyQualifiedName(), bEnd
					.getContainingArtifact().getFullyQualifiedName(),
					"Association:AssociationEnd:" + aORz + ":Unique", aName,
					Boolean.toString(aEnd.isUnique()), Boolean.toString(bEnd
							.isUnique())));
		}
		if (aEnd.getVisibility() != bEnd.getVisibility()) {
			differences.add(new Difference("value", aEnd
					.getContainingArtifact().getFullyQualifiedName(), bEnd
					.getContainingArtifact().getFullyQualifiedName(),
					"Association:AssociationEnd:" + aORz + ":Visibility",
					aName, ((Integer) aEnd.getVisibility()).toString(),
					((Integer) bEnd.getVisibility()).toString()));
		}
		return differences;
	}

	public static ArrayList<Difference> compareDependencyEnds(
			IDependencyArtifact aArtifact, IDependencyArtifact bArtifact) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		String aName = aArtifact.getAEndType().getFullyQualifiedName();
		String bName = bArtifact.getAEndType().getFullyQualifiedName();
		if (!aName.equals(bName)) {
			// Name mismatch
			differences.add(new Difference("value", aArtifact
					.getFullyQualifiedName(),
					bArtifact.getFullyQualifiedName(),
					"Dependency:DependencyEnd:A", "EndType", aName, bName));
		}
		aName = aArtifact.getZEndType().getFullyQualifiedName();
		bName = bArtifact.getZEndType().getFullyQualifiedName();
		if (!aName.equals(bName)) {
			// Name mismatch
			differences.add(new Difference("value", aArtifact
					.getFullyQualifiedName(),
					bArtifact.getFullyQualifiedName(),
					"Dependency:DependencyEnd:Z", "EndType", aName, bName));
		}
		return differences;
	}

	public static ArrayList<Difference> compareSingleExtension(
			IAbstractArtifact aArtifact, IAbstractArtifact bArtifact) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		OssjArtifactSpecifics aSpec = (OssjArtifactSpecifics) aArtifact
				.getIStandardSpecifics();
		boolean aSingle = aSpec.isSingleExtensionType();
		OssjArtifactSpecifics bSpec = (OssjArtifactSpecifics) bArtifact
				.getIStandardSpecifics();
		boolean bSingle = bSpec.isSingleExtensionType();
		if (aSingle != bSingle) {
			differences.add(new Difference("value", aArtifact
					.getFullyQualifiedName(),
					bArtifact.getFullyQualifiedName(),
					"Artifact:OSSJSpecifics", "SingleExtension",
					((Boolean) aSingle).toString(), ((Boolean) bSingle)
							.toString()));
		}
		return differences;
	}

	public static ArrayList<Difference> compareSessionFactoryMethods(
			IAbstractArtifact aArtifact, IAbstractArtifact bArtifact) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		OssjArtifactSpecifics aSpec = (OssjArtifactSpecifics) aArtifact
				.getIStandardSpecifics();
		boolean aFactory = aSpec.isSessionFactoryMethods();
		OssjArtifactSpecifics bSpec = (OssjArtifactSpecifics) bArtifact
				.getIStandardSpecifics();
		boolean bFactory = bSpec.isSessionFactoryMethods();
		if (aFactory != bFactory) {
			differences.add(new Difference("value", aArtifact
					.getFullyQualifiedName(),
					bArtifact.getFullyQualifiedName(),
					"Artifact:OSSJSpecifics", "SessionFactoryMethods",
					((Boolean) aFactory).toString(), ((Boolean) bFactory)
							.toString()));
		}
		return differences;
	}

	public static ArrayList<Difference> compareProps(
			IAbstractArtifact aArtifact, IAbstractArtifact bArtifact,
			String propType, Properties aProp, Properties bProp) {
		// TigerstripeRuntime.logInfoMessage(CompareUtils.class, "Comparing
		// "+propType+" "+aProp.size()+" "+bProp.size());
		ArrayList<Difference> differences = new ArrayList<Difference>();
		Enumeration aPropNames = aProp.propertyNames();
		Enumeration bPropNames = bProp.propertyNames();
		for (Enumeration e; aPropNames.hasMoreElements();) {
			String pName = (String) aPropNames.nextElement();

			String aVal = aProp.getProperty(pName, "");
			String bVal = bProp.getProperty(pName, "");
			// TigerstripeRuntime.logInfoMessage(CompareUtils.class, pName+"
			// "+aVal+" "+bVal);
			if (!bProp.containsKey(pName)) {
				// Missing prop in b
				differences.add(new Difference("presence", aArtifact
						.getFullyQualifiedName(), bArtifact
						.getFullyQualifiedName(), "Artifact:Property:"
						+ propType, pName, "present", "absent"));
			} else {
				if (!aVal.equals(bVal)) {
					// PropertyValue mismatch
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(), "Artifact:Property:"
							+ propType, pName, aVal, bVal));
				}
			}
		}
		aPropNames = aProp.propertyNames();
		bPropNames = bProp.propertyNames();
		for (Enumeration b; bPropNames.hasMoreElements();) {
			String pName = (String) bPropNames.nextElement();
			String aVal = aProp.getProperty(pName, "");
			String bVal = bProp.getProperty(pName, "");
			// TigerstripeRuntime.logInfoMessage(CompareUtils.class, pName+"
			// "+aVal+" "+bVal);
			if (!aProp.containsKey(pName)) {
				// Missing prop in a
				differences.add(new Difference("presence", aArtifact
						.getFullyQualifiedName(), bArtifact
						.getFullyQualifiedName(), "Artifact:Property:"
						+ propType, pName, "absent", "present"));
			}
		}

		return differences;
	}

	public static String getProjectLocation(IAbstractArtifact iAbsArtifact) {
		if (iAbsArtifact instanceof AbstractArtifact) {
			AbstractArtifact artifact = (AbstractArtifact) iAbsArtifact;
			TigerstripeProject project = artifact.getTSProject();
			if (project != null)
				return project.getProjectDetails().getName();
			// Could be in a Dependency
			ArtifactManager parent = artifact.getArtifactManager();
			if (parent instanceof ModuleArtifactManager) {
				project = ((ModuleArtifactManager) parent).getEmbeddedProject();
				return "module:" + project.getProjectDetails().getName();
			}
			return "";
		} else
			return "";
	}

	public static String typeOf(IAbstractArtifact artifact) {
		if (artifact instanceof ISessionArtifact)
			return ISessionArtifact.class.getName();
		else if (artifact instanceof IUpdateProcedureArtifact)
			return IUpdateProcedureArtifact.class.getName();
		else if (artifact instanceof IEnumArtifact)
			return IEnumArtifact.class.getName();
		else if (artifact instanceof IEventArtifact)
			return IEventArtifact.class.getName();
		else if (artifact instanceof IDatatypeArtifact)
			return IDatatypeArtifact.class.getName();
		else if (artifact instanceof IExceptionArtifact)
			return IExceptionArtifact.class.getName();
		else if (artifact instanceof IQueryArtifact)
			return IQueryArtifact.class.getName();
		else if (artifact instanceof IManagedEntityArtifact)
			return IManagedEntityArtifact.class.getName();
		else if (artifact instanceof IAssociationArtifact)
			return IAssociationArtifact.class.getName();
		else if (artifact instanceof IAssociationClassArtifact)
			return IAssociationClassArtifact.class.getName();
		else if (artifact instanceof IDependencyArtifact)
			return IDependencyArtifact.class.getName();
		else
			return "Unknown Artifact Type";

	}

	public static ArrayList<Difference> compareStereotypes(
			IAbstractArtifact aArtifact, IAbstractArtifact bArtifact,
			IModelComponent aComponent, IModelComponent bComponent, String tag) {

		String object = "";
		if (aComponent instanceof IAbstractArtifact) {
			object = "";
		} else {
			object = aComponent.getName() + ":";
		}

		ArrayList<Difference> differences = new ArrayList<Difference>();
		// Check that the same stereotypes are applied.
		// Then check the values for the attributes
		List<IStereotypeInstance> aInstances = Arrays.asList(aComponent
				.getStereotypeInstances());
		List<IStereotypeInstance> bInstances = Arrays.asList(bComponent
				.getStereotypeInstances());

		ListIterator<IStereotypeInstance> aIt;

		aIt = aInstances.listIterator();
		while (aIt.hasNext()) {
			IStereotypeInstance aInst = aIt.next();
			boolean foundit = false;
			ListIterator<IStereotypeInstance> bIt = bInstances
					.listIterator();
			while (bIt.hasNext()) {
				IStereotypeInstance bInst = bIt.next();
				if (aInst.getCharacterizingIStereotype().equals(
						bInst.getCharacterizingIStereotype())) {
					// compare the values
					foundit = true;
					for (IStereotypeAttribute attr : aInst
							.getCharacterizingIStereotype()
							.getIAttributes()) {
						try {
							if (attr.isArray()) {
								// compare the array values...
								// order is NOT important
								differences.addAll(compareLists(aArtifact
										.getFullyQualifiedName(), bArtifact
										.getFullyQualifiedName(), tag
										+ ":Stereotype:ArrayValue", object
										+ aInst.getName() + ":"
										+ attr.getName() + ":", new ArrayList(
										Arrays.asList(aInst
												.getAttributeValues(attr))),
										new ArrayList(Arrays.asList(bInst
												.getAttributeValues(attr)))));
							} else {

								if (!aInst.getAttributeValue(attr).equals(
										bInst.getAttributeValue(attr))) {
									differences.add(new Difference("value",
											aArtifact.getFullyQualifiedName(),
											bArtifact.getFullyQualifiedName(),
											tag + ":Stereotype:Value", object
													+ aInst.getName() + ":"
													+ attr.getName(), aInst
													.getAttributeValue(attr),
											bInst.getAttributeValue(attr)));
								}
							}
						} catch (TigerstripeException t) {
							// Should never happen
							TigerstripeRuntime.logErrorMessage(
									"TigerstripeException detected", t);
						}
					}
				}
			}
			if (!foundit) {
				// Not there.
				differences.add(new Difference("presence", aArtifact
						.getFullyQualifiedName(), bArtifact
						.getFullyQualifiedName(), tag + ":Stereotype", object
						+ aInst.getName(), "present", "absent"));
			}
		}

		ListIterator<IStereotypeInstance> bIt = bInstances.listIterator();
		while (bIt.hasNext()) {
			IStereotypeInstance bInst = bIt.next();
			boolean foundit = false;
			aIt = aInstances.listIterator();
			while (aIt.hasNext()) {
				IStereotypeInstance aInst = aIt.next();
				if (aInst.getCharacterizingIStereotype().equals(
						bInst.getCharacterizingIStereotype())) {
					foundit = true;
				}
			}
			if (!foundit) {
				// Not there.
				differences.add(new Difference("presence", aArtifact
						.getFullyQualifiedName(), bArtifact
						.getFullyQualifiedName(), tag + ":Stereotype", object
						+ bInst.getName(), "absent", "present"));
			}
		}

		return differences;
	}

	public static ArrayList<Difference> compareArgumentStereotypes(
			IAbstractArtifact aArtifact, IAbstractArtifact bArtifact,
			IArgument aArgument, IArgument bArgument, String tag) {

		String object = "";
		if (aArgument instanceof IAbstractArtifact) {
			object = "";
		} else {
			object = aArgument.getContainingIMethod().getName() + ":"
					+ aArgument.getName() + ":";
		}

		ArrayList<Difference> differences = new ArrayList<Difference>();
		// Check that the same stereotypes are applied.
		// Then check the values for the attributes
		List<IStereotypeInstance> aInstances = Arrays.asList(aArgument
				.getStereotypeInstances());
		List<IStereotypeInstance> bInstances = Arrays.asList(bArgument
				.getStereotypeInstances());

		ListIterator<IStereotypeInstance> aIt;

		aIt = aInstances.listIterator();
		while (aIt.hasNext()) {
			IStereotypeInstance aInst = aIt.next();
			boolean foundit = false;
			ListIterator<IStereotypeInstance> bIt = bInstances.listIterator();
			while (bIt.hasNext()) {
				IStereotypeInstance bInst = bIt.next();
				if (aInst.getCharacterizingIStereotype().equals(
						bInst.getCharacterizingIStereotype())) {
					// compare the values
					foundit = true;
					for (IStereotypeAttribute attr : aInst
							.getCharacterizingIStereotype().getAttributes()) {
						try {
							if (attr.isArray()) {
								// compare the array values...
								// order is NOT important
								differences.addAll(compareLists(aArtifact
										.getFullyQualifiedName(), bArtifact
										.getFullyQualifiedName(), tag
										+ ":Stereotype:ArrayValue", object
										+ aInst.getName() + ":"
										+ attr.getName() + ":", new ArrayList(
										Arrays.asList(aInst
												.getAttributeValues(attr))),
										new ArrayList(Arrays.asList(bInst
												.getAttributeValues(attr)))));
							} else {

								if (!aInst.getAttributeValue(attr).equals(
										bInst.getAttributeValue(attr))) {
									differences.add(new Difference("value",
											aArtifact.getFullyQualifiedName(),
											bArtifact.getFullyQualifiedName(),
											tag + ":Stereotype:Value", object
													+ aInst.getName() + ":"
													+ attr.getName(), aInst
													.getAttributeValue(attr),
											bInst.getAttributeValue(attr)));
								}
							}
						} catch (TigerstripeException t) {
							// Should never happen
							TigerstripeRuntime.logErrorMessage(
									"TigerstripeException detected", t);
						}
					}
				}
			}
			if (!foundit) {
				// Not there.
				differences.add(new Difference("presence", aArtifact
						.getFullyQualifiedName(), bArtifact
						.getFullyQualifiedName(), tag + ":Stereotype", object
						+ aInst.getName(), "present", "absent"));
			}
		}

		ListIterator<IStereotypeInstance> bIt = bInstances.listIterator();
		while (bIt.hasNext()) {
			IStereotypeInstance bInst = bIt.next();
			boolean foundit = false;
			aIt = aInstances.listIterator();
			while (aIt.hasNext()) {
				IStereotypeInstance aInst = aIt.next();
				if (aInst.getCharacterizingIStereotype().equals(
						bInst.getCharacterizingIStereotype())) {
					foundit = true;
				}
			}
			if (!foundit) {
				// Not there.
				differences.add(new Difference("presence", aArtifact
						.getFullyQualifiedName(), bArtifact
						.getFullyQualifiedName(), tag + ":Stereotype", object
						+ bInst.getName(), "absent", "present"));
			}
		}

		return differences;
	}

	public static ArrayList<Difference> compareMethodReturnStereotypes(
			IAbstractArtifact aArtifact, IAbstractArtifact bArtifact,
			IMethod aMethod, IMethod bMethod, String tag) {

		String object = "";
		if (aMethod instanceof IAbstractArtifact) {
			object = "";
		} else {
			object = aMethod.getName() + ":";
		}

		ArrayList<Difference> differences = new ArrayList<Difference>();
		// Check that the same stereotypes are applied.
		// Then check the values for the attributes
		List<IStereotypeInstance> aInstances = Arrays.asList(aMethod
				.getReturnStereotypeInstances());
		List<IStereotypeInstance> bInstances = Arrays.asList(bMethod
				.getReturnStereotypeInstances());

		ListIterator<IStereotypeInstance> aIt;

		aIt = aInstances.listIterator();
		while (aIt.hasNext()) {
			IStereotypeInstance aInst = aIt.next();
			boolean foundit = false;
			ListIterator<IStereotypeInstance> bIt = bInstances.listIterator();
			while (bIt.hasNext()) {
				IStereotypeInstance bInst = bIt.next();
				if (aInst.getCharacterizingIStereotype().equals(
						bInst.getCharacterizingIStereotype())) {
					// compare the values
					foundit = true;
					for (IStereotypeAttribute attr : aInst
							.getCharacterizingIStereotype().getAttributes()) {
						try {
							if (attr.isArray()) {
								// compare the array values...
								// order is NOT important
								differences.addAll(compareLists(aArtifact
										.getFullyQualifiedName(), bArtifact
										.getFullyQualifiedName(), tag
										+ ":Stereotype:ArrayValue", object
										+ aInst.getName() + ":"
										+ attr.getName() + ":", new ArrayList(
										Arrays.asList(aInst
												.getAttributeValues(attr))),
										new ArrayList(Arrays.asList(bInst
												.getAttributeValues(attr)))));
							} else {

								if (!aInst.getAttributeValue(attr).equals(
										bInst.getAttributeValue(attr))) {
									differences.add(new Difference("value",
											aArtifact.getFullyQualifiedName(),
											bArtifact.getFullyQualifiedName(),
											tag + ":Stereotype:Value", object
													+ aInst.getName() + ":"
													+ attr.getName(), aInst
													.getAttributeValue(attr),
											bInst.getAttributeValue(attr)));
								}
							}
						} catch (TigerstripeException t) {
							// Should never happen
							TigerstripeRuntime.logErrorMessage(
									"TigerstripeException detected", t);
						}
					}
				}
			}
			if (!foundit) {
				// Not there.
				differences.add(new Difference("presence", aArtifact
						.getFullyQualifiedName(), bArtifact
						.getFullyQualifiedName(), tag + ":Stereotype", object
						+ aInst.getName(), "present", "absent"));
			}
		}

		ListIterator<IStereotypeInstance> bIt = bInstances.listIterator();
		while (bIt.hasNext()) {
			IStereotypeInstance bInst = bIt.next();
			boolean foundit = false;
			aIt = aInstances.listIterator();
			while (aIt.hasNext()) {
				IStereotypeInstance aInst = aIt.next();
				if (aInst.getCharacterizingIStereotype().equals(
						bInst.getCharacterizingIStereotype())) {
					foundit = true;
				}
			}
			if (!foundit) {
				// Not there.
				differences.add(new Difference("presence", aArtifact
						.getFullyQualifiedName(), bArtifact
						.getFullyQualifiedName(), tag + ":Stereotype", object
						+ bInst.getName(), "absent", "present"));
			}
		}

		return differences;
	}

	public static ArrayList<Difference> compareLabels(
			IAbstractArtifact aArtifact, IAbstractArtifact bArtifact) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		Map<String, ILabel> aLabelMap = new HashMap();
		Map<String, String> aLabelValueMap = new HashMap();
		Map aTypeMap = new HashMap();
		Map aVisibilityMap = new HashMap();
		Map aCommentMap = new HashMap();

		Map<String, ILabel> bLabelMap = new HashMap();
		Map<String, String> bLabelValueMap = new HashMap();

		Map bTypeMap = new HashMap();
		Map bVisibilityMap = new HashMap();
		Map bCommentMap = new HashMap();
		for (ILabel label : aArtifact.getILabels()) {
			aLabelMap.put(label.getName(), label);
			aLabelValueMap.put(label.getName(), label.getValue());
			aVisibilityMap.put(label.getName(), label.getVisibility());
			aTypeMap.put(label.getName(), label.getIType()
					.getFullyQualifiedName());
			aCommentMap.put(label.getName(), label.getComment());

		}
		for (ILabel label : bArtifact.getILabels()) {
			bLabelMap.put(label.getName(), label);
			bLabelValueMap.put(label.getName(), label.getValue());
			bVisibilityMap.put(label.getName(), label.getVisibility());
			bTypeMap.put(label.getName(), label.getIType()
					.getFullyQualifiedName());
			bCommentMap.put(label.getName(), label.getComment());
		}

		for (Object labelName : aLabelMap.keySet()) {
			if (bLabelMap.containsKey(labelName.toString())) {
				// it's in both, so compare
				// TigerstripeRuntime.logInfoMessage(CompareUtils.class, "Hunky
				// Dory "+aArtifact.getFullyQualifiedName()+""+labelName+"
				// "+aLabelMap.get(labelName)+ " "+bLabelMap.get(labelName));
				if (!aLabelValueMap.get(labelName).equals(
						bLabelValueMap.get(labelName))) {
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(), "Artifact:Label:Value",
							labelName.toString(), aLabelValueMap.get(labelName)
									.toString(), bLabelValueMap.get(labelName)
									.toString()));
				}
				// Check Type
				if (!aTypeMap.get(labelName).equals(bTypeMap.get(labelName))) {
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(), "Artifact:Label:Type",
							labelName.toString(), aTypeMap.get(labelName)
									.toString(), bTypeMap.get(labelName)
									.toString()));
				}
				// Check visibility
				if (!aVisibilityMap.get(labelName).equals(
						bVisibilityMap.get(labelName))) {
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
							"Artifact:Label:Visibility", labelName.toString(),
							aVisibilityMap.get(labelName).toString(),
							bVisibilityMap.get(labelName).toString()));
				}
				// Comment

				differences.addAll(compareLabelComment(
						aLabelMap.get(labelName), bLabelMap.get(labelName)));
				differences.addAll(CompareUtils.compareStereotypes(aArtifact,
						bArtifact, aLabelMap.get(labelName), bLabelMap
								.get(labelName), "Artifact:Label"));
				bLabelMap.remove(labelName.toString());
			} else {
				differences.add(new Difference("presence", aArtifact
						.getFullyQualifiedName(), bArtifact
						.getFullyQualifiedName(), "Artifact:Label", labelName
						.toString(), "present", "absent"));
			}
		}
		for (Object labelName : bLabelMap.keySet()) {
			// Stuff in B not A
			differences.add(new Difference(
					// "","Artifact Label in B not A",
					// bArtifact.getFullyQualifiedName(),
					// labelName.toString(),""));
					"presence", aArtifact.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(), "Artifact:Label",
					labelName.toString(), "absent", "present"));
		}
		return differences;
	}

	public static ArrayList<Difference> compareFields(
			IAbstractArtifact aArtifact, IAbstractArtifact bArtifact) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		Map aFieldMap = new HashMap();
		Map bFieldMap = new HashMap();
		for (IField field : aArtifact.getIFields()) {
			aFieldMap.put(field.getName(), field);
		}
		for (IField field : bArtifact.getIFields()) {
			bFieldMap.put(field.getName(), field);
		}
		for (Object fieldName : aFieldMap.keySet()) {
			if (bFieldMap.containsKey(fieldName.toString())) {
				// it's in both, so compare
				IField aField = (IField) aFieldMap.get(fieldName);
				IField bField = (IField) bFieldMap.get(fieldName);
				differences.addAll(compareFieldComment(aField, bField));
				differences.addAll(CompareUtils.compareStereotypes(aArtifact,
						bArtifact, aField, bField, "Artifact:Field"));
				if (!aField.getIType().getFullyQualifiedName().equals(
						bField.getIType().getFullyQualifiedName())) {
					// compare Type
					differences.add(new Difference(
							// "","Artifact Field:Type",
							// aArtifact.getFullyQualifiedName(),
							// fieldName.toString(),aField.getIType().getFullyQualifiedName(),bField.getIType().getFullyQualifiedName()));
							"value", aArtifact.getFullyQualifiedName(),
							bArtifact.getFullyQualifiedName(),
							"Artifact:Field:Type", fieldName.toString(), aField
									.getIType().getFullyQualifiedName(), bField
									.getIType().getFullyQualifiedName()));
				}
				if (aField.getIType().getTypeMultiplicity() != bField
						.getIType().getTypeMultiplicity()) {
					// compare Multiplicity
					differences.add(new Difference(
							// "","Artifact Field:Multiplicity",
							// aArtifact.getFullyQualifiedName(),
							// fieldName.toString(),aField.getIType().getMultiplicity(),bField.getIType().getMultiplicity()));
							"value", aArtifact.getFullyQualifiedName(),
							bArtifact.getFullyQualifiedName(),
							"Artifact:Field:Multiplicity",
							fieldName.toString(), aField.getIType()
									.getTypeMultiplicity().toString(), bField
									.getIType().getTypeMultiplicity()
									.toString()));
				}
				if (aField.getRefBy() != bField.getRefBy()) {
					// Compare RefBy
					// TODO : Should only check this if it is relevant?
					differences.add(new Difference(
							// "","Artifact Field:RefBy",
							// aArtifact.getFullyQualifiedName(),
							// fieldName.toString(),aField.getRefBy(),bField.getRefBy()));
							"value", aArtifact.getFullyQualifiedName(),
							bArtifact.getFullyQualifiedName(),
							"Artifact:Field:RefBy", fieldName.toString(),
							((Integer) aField.getRefBy()).toString(),
							((Integer) bField.getRefBy()).toString()));
				}
				if (aField.getVisibility() != bField.getVisibility()) {
					// Compare Visibility
					differences.add(new Difference(
							// "","Artifact Field:Visibility",
							// aArtifact.getFullyQualifiedName(),
							// fieldName.toString(),aField.getVisibility(),bField.getVisibility()));
							"value", aArtifact.getFullyQualifiedName(),
							bArtifact.getFullyQualifiedName(),
							"Artifact:Field:Visibility", fieldName.toString(),
							((Integer) aField.getVisibility()).toString(),
							((Integer) bField.getVisibility()).toString()));
				}
				if (aField.isOptional() != bField.isOptional()) {
					// Compare Optional
					differences.add(new Difference(
							// "","Artifact Field:Optional",
							// aArtifact.getFullyQualifiedName(),
							// fieldName.toString(),aField.isOptional(),bField.isOptional()));
							"value", aArtifact.getFullyQualifiedName(),
							bArtifact.getFullyQualifiedName(),
							"Artifact:Field:Optional", fieldName.toString(),
							((Boolean) aField.isOptional()).toString(),
							((Boolean) bField.isOptional()).toString()));
				}
				if (aField.isReadOnly() != bField.isReadOnly()) {
					// Compare ReadOnly
					differences.add(new Difference(
							// "","Artifact Field:ReadOnly",
							// aArtifact.getFullyQualifiedName(),
							// fieldName.toString(),aField.isReadOnly(),bField.isReadOnly()));
							"value", aArtifact.getFullyQualifiedName(),
							bArtifact.getFullyQualifiedName(),
							"Artifact:Field:ReadOnly", fieldName.toString(),
							((Boolean) aField.isReadOnly()).toString(),
							((Boolean) bField.isReadOnly()).toString()));
				}
				if (aField.isUnique() != bField.isUnique()) {
					// Compare Unique
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(), "Artifact:Field:Unique",
							fieldName.toString(), ((Boolean) aField.isUnique())
									.toString(), ((Boolean) bField.isUnique())
									.toString()));
				}
				if (aField.isOrdered() != bField.isOrdered()) {
					// Compare Ordered
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(), "Artifact:Field:Ordered",
							fieldName.toString(),
							((Boolean) aField.isOrdered()).toString(),
							((Boolean) bField.isOrdered()).toString()));
				}
				if (aField.getDefaultValue() != null
						& bField.getDefaultValue() != null) {
					if (!aField.getDefaultValue().equals(
							bField.getDefaultValue())) {
						// Compare DefaultValue
						differences.add(new Difference("value", aArtifact
								.getFullyQualifiedName(), bArtifact
								.getFullyQualifiedName(),
								"Artifact:Field:DefaultValue", fieldName
										.toString(), aField.getDefaultValue(),
								bField.getDefaultValue()));
					}
				} else if (!(aField.getDefaultValue() == null & bField
						.getDefaultValue() == null)) {
					// ie only one of them is null
					if (aField.getDefaultValue() == null) {
						differences.add(new Difference("value", aArtifact
								.getFullyQualifiedName(), bArtifact
								.getFullyQualifiedName(),
								"Artifact:Field:DefaultValue", fieldName
										.toString(), "", bField
										.getDefaultValue()));
					}
					if (bField.getDefaultValue() == null) {
						differences.add(new Difference("value", aArtifact
								.getFullyQualifiedName(), bArtifact
								.getFullyQualifiedName(),
								"Artifact:Field:DefaultValue", fieldName
										.toString(), aField.getDefaultValue(),
								""));
					}

				}
				bFieldMap.remove(fieldName.toString());
			} else {
				differences.add(new Difference(
						// "","Artifact Field in A not B",
						// aArtifact.getFullyQualifiedName(),
						// fieldName.toString(),""));
						"presence", aArtifact.getFullyQualifiedName(),
						bArtifact.getFullyQualifiedName(), "Artifact:Field",
						fieldName.toString(), "present", "absent"));
			}
		}
		for (Object fieldName : bFieldMap.keySet()) {
			// Stuff in B not A
			differences.add(new Difference(
					// "","Artifact Field in B not A",
					// bArtifact.getFullyQualifiedName(),
					// fieldName.toString(),""));
					"presence", aArtifact.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(), "Artifact:Field",
					fieldName.toString(), "absent", "present"));
		}
		return differences;
	}

	public static ArrayList<Difference> compareMethods(
			IAbstractArtifact aArtifact, IAbstractArtifact bArtifact) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		Map<String, IMethod> aMethodMap = new HashMap();
		Map<String, IMethod> bMethodMap = new HashMap();
		for (IMethod method : aArtifact.getIMethods()) {
			aMethodMap.put(method.getName(), method);
		}
		for (IMethod method : bArtifact.getIMethods()) {
			bMethodMap.put(method.getName(), method);
		}

		for (Object methodName : aMethodMap.keySet()) {
			if (bMethodMap.containsKey(methodName.toString())) {
				// it's in both, so compare

				IMethod aMethod = (IMethod) aMethodMap.get(methodName);
				IMethod bMethod = (IMethod) bMethodMap.get(methodName);

				if (aMethod.isAbstract() != bMethod.isAbstract()) {
					// Compare Abstract
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
							"Artifact:Method:Abstract", methodName.toString(),
							((Boolean) aMethod.isAbstract()).toString(),
							((Boolean) bMethod.isAbstract()).toString()));
				}
				if (aMethod.isOptional() != bMethod.isOptional()) {
					// Compare Optional
					differences.add(new Difference(
							// "","Method:Optional",
							// aArtifact.getFullyQualifiedName(),
							// methodName.toString(),aMethod.isOptional(),bMethod.isOptional()));
							"value", aArtifact.getFullyQualifiedName(),
							bArtifact.getFullyQualifiedName(),
							"Artifact:Method:Optional", methodName.toString(),
							((Boolean) aMethod.isOptional()).toString(),
							((Boolean) bMethod.isOptional()).toString()));
				}
				if (aMethod.isOrdered() != bMethod.isOrdered()) {
					// Compare Ordered
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
							"Artifact:Method:Ordered", methodName.toString(),
							((Boolean) aMethod.isOrdered()).toString(),
							((Boolean) bMethod.isOrdered()).toString()));
				}
				if (aMethod.isUnique() != bMethod.isUnique()) {
					// Compare Unique
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(), "Artifact:Method:Unique",
							methodName.toString(), ((Boolean) aMethod
									.isUnique()).toString(), ((Boolean) bMethod
									.isUnique()).toString()));
				}
				if (aMethod.getVisibility() != bMethod.getVisibility()) {
					// Compare Visibility
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
							"Artifact:Method:Visibility",
							methodName.toString(), ((Integer) aMethod
									.getVisibility()).toString(),
							((Integer) bMethod.getVisibility()).toString()));
				}
				// Need to be less clever....
				// In theory of one or the other is void, then the return type
				// is not relevant?
				// But we should leave the "intelligence" to the calling
				// application

				if (aMethod.isVoid() != bMethod.isVoid()) {
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
							"Artifact:Method:ReturnType:Void", methodName
									.toString(), ((Boolean) aMethod.isVoid())
									.toString(), ((Boolean) bMethod.isVoid())
									.toString()));
				}

				if (!aMethod.getReturnIType().getFullyQualifiedName().equals(
						bMethod.getReturnIType().getFullyQualifiedName())) {
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
							"Artifact:Method:ReturnType",
							methodName.toString(), aMethod.getReturnIType()
									.getFullyQualifiedName(), bMethod
									.getReturnIType().getFullyQualifiedName()));
				}
				if (!aMethod.getMethodReturnName().equals(
						bMethod.getMethodReturnName())) {
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
							"Artifact:Method:ReturnName",
							methodName.toString(), aMethod
									.getMethodReturnName(), bMethod
									.getMethodReturnName()));
				}
				if (aMethod.getReturnIType().getTypeMultiplicity() != bMethod
						.getReturnIType().getTypeMultiplicity()) {
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
							"Artifact:Method:ReturnType:Multiplicity",
							methodName.toString(), aMethod.getReturnIType()
									.getTypeMultiplicity().toString(), bMethod
									.getReturnIType().getTypeMultiplicity()
									.toString()));
				}
				if (aMethod.isIteratorReturn() != bMethod.isIteratorReturn()) {
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
							"Artifact:Method:ReturnType:Iterator", methodName
									.toString(), ((Boolean) aMethod
									.isIteratorReturn()).toString(),
							((Boolean) bMethod.isIteratorReturn()).toString()));
				}
				if (aMethod.getReturnRefBy() != bMethod.getReturnRefBy()) {
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
							"Artifact:Method:ReturnType:RefBy", methodName
									.toString(), ((Integer) aMethod
									.getReturnRefBy()).toString(),
							((Integer) bMethod.getReturnRefBy()).toString()));
				}

				if (aMethod.getDefaultReturnValue() != null
						& bMethod.getDefaultReturnValue() != null) {
					if (!aMethod.getDefaultReturnValue().equals(
							bMethod.getDefaultReturnValue())) {
						// Compare DefaultValue
						differences
								.add(new Difference(
										"value",
										aArtifact.getFullyQualifiedName(),
										bArtifact.getFullyQualifiedName(),
										"Artifact:Method:ReturnType:DefaultReturnValue",
										methodName.toString(), aMethod
												.getDefaultReturnValue(),
										bMethod.getDefaultReturnValue()));
					}
				} else if (!(aMethod.getDefaultReturnValue() == null & bMethod
						.getDefaultReturnValue() == null)) {
					// ie only one of them is null
					if (aMethod.getDefaultReturnValue() == null) {
						differences
								.add(new Difference(
										"value",
										aArtifact.getFullyQualifiedName(),
										bArtifact.getFullyQualifiedName(),
										"Artifact:Method:ReturnType:DefaultReturnValue",
										methodName.toString(), "", bMethod
												.getDefaultReturnValue()));
					}
					if (bMethod.getDefaultReturnValue() == null) {
						differences
								.add(new Difference(
										"value",
										aArtifact.getFullyQualifiedName(),
										bArtifact.getFullyQualifiedName(),
										"Artifact:Method:ReturnType:DefaultReturnValue",
										methodName.toString(), aMethod
												.getDefaultReturnValue(), ""));
					}

				}

				differences.addAll(compareMethodComment(aMethod, bMethod));
				differences.addAll(CompareUtils.compareStereotypes(aArtifact,
						bArtifact, aMethod, bMethod, "Artifact:Method"));
				differences.addAll(CompareUtils.compareMethodReturnStereotypes(
						aArtifact, bArtifact, aMethod, bMethod,
						"Artifact:Method:Return"));

				IMethod.IArgument[] aArgs = aMethod.getIArguments();
				IMethod.IArgument[] bArgs = bMethod.getIArguments();
				if (aArgs.length == bArgs.length) {
					// Arguments - order IS important so just Iterate
					for (int j = 0; j < aArgs.length; j++) {
						if (!aArgs[j].getName().equals(bArgs[j].getName())) {
							differences.add(new Difference(
									// "","Artifact Method:Argument:Name",
									// aArtifact.getFullyQualifiedName(),
									// methodName.toString()+":["+j+"]",aArgs[j].getName(),bArgs[j].getName()));
									"value", aArtifact.getFullyQualifiedName(),
									bArtifact.getFullyQualifiedName(),
									"Artifact:Method:Argument:Name", methodName
											.toString()
											+ ":[" + j + "]", aArgs[j]
											.getName(), bArgs[j].getName()));
						}
						if (!aArgs[j].getIType().getFullyQualifiedName()
								.equals(
										bArgs[j].getIType()
												.getFullyQualifiedName())) {
							differences
									.add(new Difference(
											// "","Artifact
											// Method:Argument:Type",
											// aArtifact.getFullyQualifiedName(),
											// methodName.toString()+":["+j+"]",aArgs[j].getIType().getFullyQualifiedName(),bArgs[j].getIType().getFullyQualifiedName()));
											"value", aArtifact
													.getFullyQualifiedName(),
											bArtifact.getFullyQualifiedName(),
											"Artifact:Method:Argument:Type",
											methodName.toString() + ":[" + j
													+ "]", aArgs[j].getIType()
													.getFullyQualifiedName(),
											bArgs[j].getIType()
													.getFullyQualifiedName()));
						}
						if (aArgs[j].getIType().getTypeMultiplicity() != bArgs[j]
								.getIType().getTypeMultiplicity()) {
							differences.add(new Difference(
									// "","Artifact
									// Method:Argument:Multiplicity",
									// aArtifact.getFullyQualifiedName(),
									// methodName.toString()+":["+j+"]",aArgs[j].getIType().getMultiplicity(),bArgs[j].getIType().getMultiplicity()));
									"value", aArtifact.getFullyQualifiedName(),
									bArtifact.getFullyQualifiedName(),
									"Artifact:Method:Argument:Multiplicity",
									methodName.toString() + ":[" + j + "]",
									aArgs[j].getIType().getTypeMultiplicity()
											.toString(), bArgs[j].getIType()
											.getTypeMultiplicity().toString()));
						}
						if (aArgs[j].getRefBy() != bArgs[j].getRefBy()) {
							differences
									.add(new Difference(
											// "","Artifact
											// Method:Argument:RefBy",
											// aArtifact.getFullyQualifiedName(),
											// methodName.toString()+":["+j+"]",aArgs[j].getRefBy(),bArgs[j].getRefBy()));
											"value", aArtifact
													.getFullyQualifiedName(),
											bArtifact.getFullyQualifiedName(),
											"Artifact:Method:Argument:RefBy",
											methodName.toString() + ":[" + j
													+ "]", ((Integer) aArgs[j]
													.getRefBy()).toString(),
											((Integer) bArgs[j].getRefBy())
													.toString()));
						}
						if (aArgs[j].isOrdered() != bArgs[j].isOrdered()) {
							differences
									.add(new Difference("value", aArtifact
											.getFullyQualifiedName(), bArtifact
											.getFullyQualifiedName(),
											"Artifact:Method:Argument:Ordered",
											methodName.toString() + ":[" + j
													+ "]", ((Boolean) aArgs[j]
													.isOrdered()).toString(),
											((Boolean) bArgs[j].isOrdered())
													.toString()));
						}
						if (aArgs[j].isUnique() != bArgs[j].isUnique()) {
							differences
									.add(new Difference("value", aArtifact
											.getFullyQualifiedName(), bArtifact
											.getFullyQualifiedName(),
											"Artifact:Method:Argument:Unique",
											methodName.toString() + ":[" + j
													+ "]", ((Boolean) aArgs[j]
													.isUnique()).toString(),
											((Boolean) bArgs[j].isUnique())
													.toString()));
						}

						if (aArgs[j].getDefaultValue() != null
								& bArgs[j].getDefaultValue() != null) {
							if (!aArgs[j].getDefaultValue().equals(
									bArgs[j].getDefaultValue())) {
								// Compare DefaultValue
								differences
										.add(new Difference(
												"value",
												aArtifact
														.getFullyQualifiedName(),
												bArtifact
														.getFullyQualifiedName(),
												"Artifact:Method:Argument:DefaultValue",
												methodName.toString() + ":["
														+ j + "]", aArgs[j]
														.getDefaultValue(),
												bArgs[j].getDefaultValue()));
							}
						} else if (!(aArgs[j].getDefaultValue() == null & bArgs[j]
								.getDefaultValue() == null)) {
							// ie only one of them is null
							if (aArgs[j].getDefaultValue() == null) {
								differences
										.add(new Difference(
												"value",
												aArtifact
														.getFullyQualifiedName(),
												bArtifact
														.getFullyQualifiedName(),
												"Artifact:Method:Argument:DefaultValue",
												methodName.toString() + ":["
														+ j + "]", "", bArgs[j]
														.getDefaultValue()));
							}
							if (bArgs[j].getDefaultValue() == null) {
								differences
										.add(new Difference(
												"value",
												aArtifact
														.getFullyQualifiedName(),
												bArtifact
														.getFullyQualifiedName(),
												"Artifact:Method:Argument:DefaultValue",
												methodName.toString() + ":["
														+ j + "]", aArgs[j]
														.getDefaultValue(), ""));
							}

						}

						differences.addAll(compareArgumentComment(aArgs[j],
								bArgs[j]));
						differences.addAll(CompareUtils
								.compareArgumentStereotypes(aArtifact,
										bArtifact, aArgs[j], bArgs[j],
										"Artifact:Method:Argument"));

					}
				} else {
					differences.add(new Difference(
							// "","Artifact Method:Arguments:Length",
							// aArtifact.getFullyQualifiedName(),
							// methodName.toString(),aArgs.length,bArgs.length));
							"value", aArtifact.getFullyQualifiedName(),
							bArtifact.getFullyQualifiedName(),
							"Artifact:Method:ArgumentList", methodName
									.toString(), ((Integer) aArgs.length)
									.toString(), ((Integer) bArgs.length)
									.toString()));

				}
				if (aMethod.isInstanceMethod() != bMethod.isInstanceMethod()) {
					// Compare Instance
					differences.add(new Difference(
							// "","Artifact Method:Instance",
							// aArtifact.getFullyQualifiedName(),
							// methodName.toString(),aMethod.isInstanceMethod(),bMethod.isInstanceMethod()));
							"value", aArtifact.getFullyQualifiedName(),
							bArtifact.getFullyQualifiedName(),
							"Artifact:Method:Instance", methodName.toString(),
							((Boolean) aMethod.isInstanceMethod()).toString(),
							((Boolean) bMethod.isInstanceMethod()).toString()));
				}

				IMethod.IException[] aExceps = aMethod.getIExceptions();
				IMethod.IException[] bExceps = bMethod.getIExceptions();
				ArrayList<String> aExcepsNames = new ArrayList<String>();
				ArrayList<String> bExcepsNames = new ArrayList<String>();
				for (IMethod.IException ex : aExceps) {
					aExcepsNames.add(ex.getFullyQualifiedName());
				}
				for (IMethod.IException ex : bExceps) {
					bExcepsNames.add(ex.getFullyQualifiedName());
				}
				for (String name : aExcepsNames) {
					if (!bExcepsNames.contains(name)) {
						differences.add(new Difference(
								// "","Artifact Method:Exceptions in A not B",
								// bArtifact.getFullyQualifiedName(),
								// methodName.toString(),name));
								"presence", aArtifact.getFullyQualifiedName(),
								bArtifact.getFullyQualifiedName(),
								"Artifact:Method:Exception", methodName
										.toString()
										+ ":" + name, "present", "absent"));
					} else {
						bExcepsNames.remove(name);
					}
				}
				for (String name : bExcepsNames) {
					differences.add(new Difference(
							// "","Artifact Method:Exceptions in B not A",
							// bArtifact.getFullyQualifiedName(),
							// methodName.toString(),name));
							"presence", aArtifact.getFullyQualifiedName(),
							bArtifact.getFullyQualifiedName(),
							"Artifact:Method:Exception", methodName.toString()
									+ ":" + name, "absent", "present"));
				}

				bMethodMap.remove(methodName.toString());
			} else {
				differences.add(new Difference(
						// "","Artifact Method in A not B",
						// aArtifact.getFullyQualifiedName(),
						// methodName.toString(),""));
						"presence", aArtifact.getFullyQualifiedName(),
						bArtifact.getFullyQualifiedName(), "Artifact:Method",
						methodName.toString(), "present", "absent"));
			}
		}
		for (Object methodName : bMethodMap.keySet()) {
			// Stuff in B not A
			differences.add(new Difference(
					// "","Artifact Method in B not A",
					// bArtifact.getFullyQualifiedName(),
					// methodName.toString(),""));
					"presence", aArtifact.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(), "Artifact:Method",
					methodName.toString(), "absent", "present"));
		}
		return differences;
	}

	public static ArrayList<Difference> compareLists(String local,
			String remote, String scope, String object,
			ArrayList<String> list1, ArrayList<String> list2) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		for (String name : list1) {
			if (list2.contains(name)) {
				list2.remove(name);
			} else {
				differences.add(new Difference(
						// "",diffType,location, component,"",""));
						"presence", local, remote, scope, object + name,
						"present", "absent"));
			}
		}
		for (String name : list2) {
			differences.add(new Difference(
					// "",diffType,location, component,"",""));
					"presence", local, remote, scope, object + name, "absent",
					"present"));

		}
		return differences;
	}

	public static ArrayList<Difference> compareArtifactComment(
			IAbstractArtifact a, IAbstractArtifact b) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		String aComment = a.getComment();
		String bComment = b.getComment();
		if (!aComment.equals(bComment)) {
			differences.add(new Difference("value", a.getFullyQualifiedName(),
					b.getFullyQualifiedName(), "Artifact:Detail", "Comment",
					aComment, bComment));
		}
		return differences;
	}

	public static ArrayList<Difference> compareFieldComment(IField a, IField b) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		String aComment = a.getComment();
		String bComment = b.getComment();
		if (!aComment.equals(bComment)) {
			differences.add(new Difference("value", a.getContainingArtifact()
					.getFullyQualifiedName(), b.getContainingArtifact()
					.getFullyQualifiedName(), "Artifact:Field:Comment", a
					.getName(), aComment, bComment));
		}
		return differences;
	}

	public static ArrayList<Difference> compareLabelComment(ILabel a, ILabel b) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		String aComment = a.getComment();
		String bComment = b.getComment();
		if (!aComment.equals(bComment)) {
			differences.add(new Difference("value", a.getContainingArtifact()
					.getFullyQualifiedName(), b.getContainingArtifact()
					.getFullyQualifiedName(), "Artifact:Label:Comment", a
					.getName(), aComment, bComment));
		}
		return differences;
	}

	public static ArrayList<Difference> compareMethodComment(IMethod a,
			IMethod b) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		String aComment = a.getComment();
		String bComment = b.getComment();
		if (!aComment.equals(bComment)) {
			differences.add(new Difference("value", a.getContainingArtifact()
					.getFullyQualifiedName(), b.getContainingArtifact()
					.getFullyQualifiedName(), "Artifact:Method:Comment", a
					.getName(), aComment, bComment));
		}
		return differences;
	}

	public static ArrayList<Difference> compareArgumentComment(IArgument a,
			IArgument b) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		String aComment = a.getComment();
		String bComment = b.getComment();
		if (!aComment.equals(bComment)) {
			differences.add(new Difference("value", a.getContainingIMethod()
					.getContainingArtifact().getFullyQualifiedName(), b
					.getContainingIMethod().getContainingArtifact()
					.getFullyQualifiedName(),
					"Artifact:Method:Argument:Comment", a
							.getContainingIMethod().getName()
							+ ":" + a.getName(), aComment, bComment));
		}
		return differences;
	}

}
