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
package org.eclipse.tigerstripe.workbench.internal.tools.compare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjArtifactSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.module.ModuleArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjArtifactSpecifics;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;

public class CompareUtils {

//	public static ArrayList<Difference> compareImplements(
//			IAbstractArtifact aArtifact, IAbstractArtifact bArtifact) {
//		ArrayList<Difference> differences = new ArrayList<Difference>();
//		String aExt = aArtifact.getImplementedArtifactsAsStr() ;
//		String bExt = bArtifact.getImplementedArtifactsAsStr();
//		
//		
//		if (!aExt.equals(bExt)) {
//			differences.add(new Difference("value", aArtifact
//					.getFullyQualifiedName(),
//					bArtifact.getFullyQualifiedName(), "Artifact:Implements", "",
//					aExt, bExt));
//		}
//		return differences;
//	}
	
	public static ArrayList<Difference> compareExtends(
			IAbstractArtifact aArtifact, IAbstractArtifact bArtifact) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		String aExt;
		String bExt;
		IAbstractArtifact aExtends = aArtifact.getExtendedArtifact();
		IAbstractArtifact bExtends = bArtifact.getExtendedArtifact();
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
		if (!aEnd.getType().getFullyQualifiedName().equals(
				bEnd.getType().getFullyQualifiedName())) {
			// compare Type
			differences.add(new Difference("value", aEnd
					.getContainingArtifact().getFullyQualifiedName(), bEnd
					.getContainingArtifact().getFullyQualifiedName(),
					"Association:AssociationEnd:" + aORz + ":Type", aName, aEnd
							.getType().getFullyQualifiedName(), bEnd.getType()
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
					aName, aEnd.getVisibility().getLabel(),
					bEnd.getVisibility().getLabel()));
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
				return project.getProjectLabel();
			// Could be in a Dependency
			ArtifactManager parent = artifact.getArtifactManager();
			if (parent instanceof ModuleArtifactManager) {
				project = ((ModuleArtifactManager) parent).getEmbeddedProject();
				return "module:" + project.getProjectLabel();
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
		Collection<IStereotypeInstance> aInstances = aComponent
				.getStereotypeInstances();
		Collection<IStereotypeInstance> bInstances = bComponent
				.getStereotypeInstances();

		Iterator<IStereotypeInstance> aIt;

		aIt = aInstances.iterator();
		while (aIt.hasNext()) {
			IStereotypeInstance aInst = aIt.next();
			boolean foundit = false;
			Iterator<IStereotypeInstance> bIt = bInstances
					.iterator();
			while (bIt.hasNext()) {
				IStereotypeInstance bInst = bIt.next();
				
				// Bugzilla 319758: NPE during project import
				if ((aInst.getCharacterizingStereotype() != null) && 
						(aInst.getCharacterizingStereotype().equals(bInst.getCharacterizingStereotype()))) {
					// compare the values
					foundit = true;
					for (IStereotypeAttribute attr : aInst
							.getCharacterizingStereotype()
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

		Iterator<IStereotypeInstance> bIt = bInstances.iterator();
		while (bIt.hasNext()) {
			IStereotypeInstance bInst = bIt.next();
			boolean foundit = false;
			aIt = aInstances.iterator();
			while (aIt.hasNext()) {
				IStereotypeInstance aInst = aIt.next();
				
				// Bugzilla 319758: NPE during project import
				if ((aInst.getCharacterizingStereotype() != null) && 
					(aInst.getCharacterizingStereotype().equals(bInst.getCharacterizingStereotype()))) {
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
			object = aArgument.getContainingMethod().getName() + ":"
					+ aArgument.getName() + ":";
		}

		ArrayList<Difference> differences = new ArrayList<Difference>();
		// Check that the same stereotypes are applied.
		// Then check the values for the attributes
		Collection<IStereotypeInstance> aInstances = aArgument
				.getStereotypeInstances();
		Collection<IStereotypeInstance> bInstances = bArgument
				.getStereotypeInstances();

		Iterator<IStereotypeInstance> aIt;

		aIt = aInstances.iterator();
		while (aIt.hasNext()) {
			IStereotypeInstance aInst = aIt.next();
			boolean foundit = false;
			Iterator<IStereotypeInstance> bIt = bInstances.iterator();
			while (bIt.hasNext()) {
				IStereotypeInstance bInst = bIt.next();
				if (aInst.getCharacterizingStereotype().equals(
						bInst.getCharacterizingStereotype())) {
					// compare the values
					foundit = true;
					for (IStereotypeAttribute attr : aInst
							.getCharacterizingStereotype().getAttributes()) {
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

		Iterator<IStereotypeInstance> bIt = bInstances.iterator();
		while (bIt.hasNext()) {
			IStereotypeInstance bInst = bIt.next();
			boolean foundit = false;
			aIt = aInstances.iterator();
			while (aIt.hasNext()) {
				IStereotypeInstance aInst = aIt.next();
				if (aInst.getCharacterizingStereotype().equals(
						bInst.getCharacterizingStereotype())) {
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
		Collection<IStereotypeInstance> aInstances = aMethod
				.getReturnStereotypeInstances();
		Collection<IStereotypeInstance> bInstances = bMethod
				.getReturnStereotypeInstances();

		Iterator<IStereotypeInstance> aIt;

		aIt = aInstances.iterator();
		while (aIt.hasNext()) {
			IStereotypeInstance aInst = aIt.next();
			boolean foundit = false;
			Iterator<IStereotypeInstance> bIt = bInstances.iterator();
			while (bIt.hasNext()) {
				IStereotypeInstance bInst = bIt.next();
				if (aInst.getCharacterizingStereotype().equals(
						bInst.getCharacterizingStereotype())) {
					// compare the values
					foundit = true;
					for (IStereotypeAttribute attr : aInst
							.getCharacterizingStereotype().getAttributes()) {
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

		Iterator<IStereotypeInstance> bIt = bInstances.iterator();
		while (bIt.hasNext()) {
			IStereotypeInstance bInst = bIt.next();
			boolean foundit = false;
			aIt = aInstances.iterator();
			while (aIt.hasNext()) {
				IStereotypeInstance aInst = aIt.next();
				if (aInst.getCharacterizingStereotype().equals(
						bInst.getCharacterizingStereotype())) {
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

	public static ArrayList<Difference> compareLiterals(
			IAbstractArtifact aArtifact, IAbstractArtifact bArtifact) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		Map<String, ILiteral> aLiteralMap = new HashMap();
		Map<String, String> aLiteralValueMap = new HashMap();
		Map aTypeMap = new HashMap();
		Map aVisibilityMap = new HashMap();
		Map aCommentMap = new HashMap();

		Map<String, ILiteral> bLiteralMap = new HashMap();
		Map<String, String> bLiteralValueMap = new HashMap();

		Map bTypeMap = new HashMap();
		Map bVisibilityMap = new HashMap();
		Map bCommentMap = new HashMap();
		for (ILiteral literal : aArtifact.getLiterals()) {
			aLiteralMap.put(literal.getName(), literal);
			aLiteralValueMap.put(literal.getName(), literal.getValue());
			aVisibilityMap.put(literal.getName(), literal.getVisibility());
			aTypeMap.put(literal.getName(), literal.getType()
					.getFullyQualifiedName());
			aCommentMap.put(literal.getName(), literal.getComment());

		}
		for (ILiteral literal : bArtifact.getLiterals()) {
			bLiteralMap.put(literal.getName(), literal);
			bLiteralValueMap.put(literal.getName(), literal.getValue());
			bVisibilityMap.put(literal.getName(), literal.getVisibility());
			bTypeMap.put(literal.getName(), literal.getType()
					.getFullyQualifiedName());
			bCommentMap.put(literal.getName(), literal.getComment());
		}

		for (Object literalName : aLiteralMap.keySet()) {
			if (bLiteralMap.containsKey(literalName.toString())) {
				// it's in both, so compare
				// TigerstripeRuntime.logInfoMessage(CompareUtils.class, "Hunky
				// Dory "+aArtifact.getFullyQualifiedName()+""+labelName+"
				// "+aLabelMap.get(labelName)+ " "+bLabelMap.get(labelName));
				if (!aLiteralValueMap.get(literalName).equals(
						bLiteralValueMap.get(literalName))) {
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(), "Artifact:Literal:Value",
							literalName.toString(), aLiteralValueMap.get(literalName)
									.toString(), bLiteralValueMap.get(literalName)
									.toString()));
				}
				// Check Type
				if (!aTypeMap.get(literalName).equals(bTypeMap.get(literalName))) {
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(), "Artifact:Literal:Type",
							literalName.toString(), aTypeMap.get(literalName)
									.toString(), bTypeMap.get(literalName)
									.toString()));
				}
				// Check visibility
				if (!aVisibilityMap.get(literalName).equals(
						bVisibilityMap.get(literalName))) {
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
							"Artifact:Literal:Visibility", literalName.toString(),
							aVisibilityMap.get(literalName).toString(),
							bVisibilityMap.get(literalName).toString()));
				}
				// Comment

				differences.addAll(compareLiteralComment(
						aLiteralMap.get(literalName), bLiteralMap.get(literalName)));
				differences.addAll(CompareUtils.compareStereotypes(aArtifact,
						bArtifact, aLiteralMap.get(literalName), bLiteralMap
								.get(literalName), "Artifact:Literal"));
				bLiteralMap.remove(literalName.toString());
			} else {
				differences.add(new Difference("presence", aArtifact
						.getFullyQualifiedName(), bArtifact
						.getFullyQualifiedName(), "Artifact:Literal", literalName
						.toString(), "present", "absent"));
			}
		}
		for (Object literalName : bLiteralMap.keySet()) {
			// Stuff in B not A
			differences.add(new Difference(
					// "","Artifact Label in B not A",
					// bArtifact.getFullyQualifiedName(),
					// labelName.toString(),""));
					"presence", aArtifact.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(), "Artifact:Literal",
					literalName.toString(), "absent", "present"));
		}
		return differences;
	}

	public static ArrayList<Difference> compareFields(
			IAbstractArtifact aArtifact, IAbstractArtifact bArtifact) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		Map aFieldMap = new HashMap();
		Map bFieldMap = new HashMap();
		for (IField field : aArtifact.getFields()) {
			aFieldMap.put(field.getName(), field);
		}
		for (IField field : bArtifact.getFields()) {
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
				if (!aField.getType().getFullyQualifiedName().equals(
						bField.getType().getFullyQualifiedName())) {
					// compare Type
					differences.add(new Difference(
							// "","Artifact Field:Type",
							// aArtifact.getFullyQualifiedName(),
							// fieldName.toString(),aField.getIType().getFullyQualifiedName(),bField.getIType().getFullyQualifiedName()));
							"value", aArtifact.getFullyQualifiedName(),
							bArtifact.getFullyQualifiedName(),
							"Artifact:Field:Type", fieldName.toString(), aField
									.getType().getFullyQualifiedName(), bField
									.getType().getFullyQualifiedName()));
				}
				if (aField.getType().getTypeMultiplicity() != bField
						.getType().getTypeMultiplicity()) {
					// compare Multiplicity
					differences.add(new Difference(
							// "","Artifact Field:Multiplicity",
							// aArtifact.getFullyQualifiedName(),
							// fieldName.toString(),aField.getIType().getMultiplicity(),bField.getIType().getMultiplicity()));
							"value", aArtifact.getFullyQualifiedName(),
							bArtifact.getFullyQualifiedName(),
							"Artifact:Field:Multiplicity",
							fieldName.toString(), aField.getType()
									.getTypeMultiplicity().toString(), bField
									.getType().getTypeMultiplicity()
									.toString()));
				}
//				if (aField.getRefBy() != bField.getRefBy()) {
//					// Compare RefBy
//					// TODO : Should only check this if it is relevant?
//					differences.add(new Difference(
//							// "","Artifact Field:RefBy",
//							// aArtifact.getFullyQualifiedName(),
//							// fieldName.toString(),aField.getRefBy(),bField.getRefBy()));
//							"value", aArtifact.getFullyQualifiedName(),
//							bArtifact.getFullyQualifiedName(),
//							"Artifact:Field:RefBy", fieldName.toString(),
//							((Integer) aField.getRefBy()).toString(),
//							((Integer) bField.getRefBy()).toString()));
//				}
				if (aField.getVisibility() != bField.getVisibility()) {
					// Compare Visibility
					differences.add(new Difference(
							// "","Artifact Field:Visibility",
							// aArtifact.getFullyQualifiedName(),
							// fieldName.toString(),aField.getVisibility(),bField.getVisibility()));
							"value", aArtifact.getFullyQualifiedName(),
							bArtifact.getFullyQualifiedName(),
							"Artifact:Field:Visibility", fieldName.toString(),
							aField.getVisibility().getLabel(),
							bField.getVisibility().getLabel()));
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
		for (IMethod method : aArtifact.getMethods()) {
			aMethodMap.put(method.getName(), method);
		}
		for (IMethod method : bArtifact.getMethods()) {
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
							methodName.toString(), aMethod
									.getVisibility().getLabel(),
							bMethod.getVisibility().getLabel()));
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

				if (!aMethod.getReturnType().getFullyQualifiedName().equals(
						bMethod.getReturnType().getFullyQualifiedName())) {
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
							"Artifact:Method:ReturnType",
							methodName.toString(), aMethod.getReturnType()
									.getFullyQualifiedName(), bMethod
									.getReturnType().getFullyQualifiedName()));
				}
				if (!aMethod.getReturnName().equals(
						bMethod.getReturnName())) {
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
							"Artifact:Method:ReturnName",
							methodName.toString(), aMethod
									.getReturnName(), bMethod
									.getReturnName()));
				}
				if (aMethod.getReturnType().getTypeMultiplicity() != bMethod
						.getReturnType().getTypeMultiplicity()) {
					differences.add(new Difference("value", aArtifact
							.getFullyQualifiedName(), bArtifact
							.getFullyQualifiedName(),
							"Artifact:Method:ReturnType:Multiplicity",
							methodName.toString(), aMethod.getReturnType()
									.getTypeMultiplicity().toString(), bMethod
									.getReturnType().getTypeMultiplicity()
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

				if (aMethod.getArguments().size() == bMethod.getArguments().size()) {
					// Arguments - order IS important so just Iterate
					Iterator<IArgument> aIterator = aMethod.getArguments().iterator();
					Iterator<IArgument> bIterator = bMethod.getArguments().iterator();
					for (int j = 0; j < aMethod.getArguments().size(); j++) {
						IArgument aArg = aIterator.next();
						IArgument bArg = bIterator.next();
						
						
						if (!aArg.getName().equals(bArg.getName())) {
							differences.add(new Difference(
									// "","Artifact Method:Argument:Name",
									// aArtifact.getFullyQualifiedName(),
									// methodName.toString()+":["+j+"]",aArg.getName(),bArg.getName()));
									"value", aArtifact.getFullyQualifiedName(),
									bArtifact.getFullyQualifiedName(),
									"Artifact:Method:Argument:Name", methodName
											.toString()
											+ ":[" + j + "]", aArg
											.getName(), bArg.getName()));
						}
						if (!aArg.getType().getFullyQualifiedName()
								.equals(
										bArg.getType()
												.getFullyQualifiedName())) {
							differences
									.add(new Difference(
											// "","Artifact
											// Method:Argument:Type",
											// aArtifact.getFullyQualifiedName(),
											// methodName.toString()+":["+j+"]",aArg.getIType().getFullyQualifiedName(),bArg.getIType().getFullyQualifiedName()));
											"value", aArtifact
													.getFullyQualifiedName(),
											bArtifact.getFullyQualifiedName(),
											"Artifact:Method:Argument:Type",
											methodName.toString() + ":[" + j
													+ "]", aArg.getType()
													.getFullyQualifiedName(),
											bArg.getType()
													.getFullyQualifiedName()));
						}
						if (aArg.getType().getTypeMultiplicity() != bArg
								.getType().getTypeMultiplicity()) {
							differences.add(new Difference(
									// "","Artifact
									// Method:Argument:Multiplicity",
									// aArtifact.getFullyQualifiedName(),
									// methodName.toString()+":["+j+"]",aArg.getIType().getMultiplicity(),bArg.getIType().getMultiplicity()));
									"value", aArtifact.getFullyQualifiedName(),
									bArtifact.getFullyQualifiedName(),
									"Artifact:Method:Argument:Multiplicity",
									methodName.toString() + ":[" + j + "]",
									aArg.getType().getTypeMultiplicity()
											.toString(), bArg.getType()
											.getTypeMultiplicity().toString()));
						}
						if (aArg.getRefBy() != bArg.getRefBy()) {
							differences
									.add(new Difference(
											// "","Artifact
											// Method:Argument:RefBy",
											// aArtifact.getFullyQualifiedName(),
											// methodName.toString()+":["+j+"]",aArg.getRefBy(),bArg.getRefBy()));
											"value", aArtifact
													.getFullyQualifiedName(),
											bArtifact.getFullyQualifiedName(),
											"Artifact:Method:Argument:RefBy",
											methodName.toString() + ":[" + j
													+ "]", ((Integer) aArg
													.getRefBy()).toString(),
											((Integer) bArg.getRefBy())
													.toString()));
						}
						if (aArg.isOrdered() != bArg.isOrdered()) {
							differences
									.add(new Difference("value", aArtifact
											.getFullyQualifiedName(), bArtifact
											.getFullyQualifiedName(),
											"Artifact:Method:Argument:Ordered",
											methodName.toString() + ":[" + j
													+ "]", ((Boolean) aArg
													.isOrdered()).toString(),
											((Boolean) bArg.isOrdered())
													.toString()));
						}
						if (aArg.isUnique() != bArg.isUnique()) {
							differences
									.add(new Difference("value", aArtifact
											.getFullyQualifiedName(), bArtifact
											.getFullyQualifiedName(),
											"Artifact:Method:Argument:Unique",
											methodName.toString() + ":[" + j
													+ "]", ((Boolean) aArg
													.isUnique()).toString(),
											((Boolean) bArg.isUnique())
													.toString()));
						}

						if (aArg.getDefaultValue() != null
								& bArg.getDefaultValue() != null) {
							if (!aArg.getDefaultValue().equals(
									bArg.getDefaultValue())) {
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
														+ j + "]", aArg
														.getDefaultValue(),
												bArg.getDefaultValue()));
							}
						} else if (!(aArg.getDefaultValue() == null & bArg
								.getDefaultValue() == null)) {
							// ie only one of them is null
							if (aArg.getDefaultValue() == null) {
								differences
										.add(new Difference(
												"value",
												aArtifact
														.getFullyQualifiedName(),
												bArtifact
														.getFullyQualifiedName(),
												"Artifact:Method:Argument:DefaultValue",
												methodName.toString() + ":["
														+ j + "]", "", bArg
														.getDefaultValue()));
							}
							if (bArg.getDefaultValue() == null) {
								differences
										.add(new Difference(
												"value",
												aArtifact
														.getFullyQualifiedName(),
												bArtifact
														.getFullyQualifiedName(),
												"Artifact:Method:Argument:DefaultValue",
												methodName.toString() + ":["
														+ j + "]", aArg
														.getDefaultValue(), ""));
							}

						}

						differences.addAll(compareArgumentComment(aArg,
								bArg));
						differences.addAll(CompareUtils
								.compareArgumentStereotypes(aArtifact,
										bArtifact, aArg, bArg,
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
									.toString(), ((Integer) aMethod.getArguments().size())
									.toString(), ((Integer) bMethod.getArguments().size())
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

				ArrayList<String> aExcepsNames = new ArrayList<String>();
				ArrayList<String> bExcepsNames = new ArrayList<String>();
				for (IMethod.IException ex : aMethod.getExceptions()) {
					aExcepsNames.add(ex.getFullyQualifiedName());
				}
				for (IMethod.IException ex : bMethod.getExceptions()) {
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

	public static ArrayList<Difference> compareLiteralComment(ILiteral a, ILiteral b) {
		ArrayList<Difference> differences = new ArrayList<Difference>();
		String aComment = a.getComment();
		String bComment = b.getComment();
		if (!aComment.equals(bComment)) {
			differences.add(new Difference("value", a.getContainingArtifact()
					.getFullyQualifiedName(), b.getContainingArtifact()
					.getFullyQualifiedName(), "Artifact:Literal:Comment", a
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
			differences.add(new Difference("value", a.getContainingMethod()
					.getContainingArtifact().getFullyQualifiedName(), b
					.getContainingMethod().getContainingArtifact()
					.getFullyQualifiedName(),
					"Artifact:Method:Argument:Comment", a
							.getContainingMethod().getName()
							+ ":" + a.getName(), aComment, bComment));
		}
		return differences;
	}

}
