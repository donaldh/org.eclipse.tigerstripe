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
package org.eclipse.tigerstripe.workbench.optional.bulkImport;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.model.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.model.IAssociationEnd;
import org.eclipse.tigerstripe.api.model.IField;
import org.eclipse.tigerstripe.api.model.ILabel;
import org.eclipse.tigerstripe.api.model.IMethod;
import org.eclipse.tigerstripe.api.model.IModelComponent;
import org.eclipse.tigerstripe.api.model.IType;
import org.eclipse.tigerstripe.api.model.IMethod.IArgument;
import org.eclipse.tigerstripe.api.model.IMethod.IException;
import org.eclipse.tigerstripe.api.model.IMethod.OssjEntityMethodFlavor;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IDependencyArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IEnumArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IEventArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IManagedEntityArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IQueryArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.ISessionArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.ISessionArtifact.IEmittedEvent;
import org.eclipse.tigerstripe.api.model.artifacts.ISessionArtifact.IEntityMethodFlavorDetails;
import org.eclipse.tigerstripe.api.model.artifacts.ISessionArtifact.IExposedUpdateProcedure;
import org.eclipse.tigerstripe.api.model.artifacts.ISessionArtifact.IManagedEntityDetails;
import org.eclipse.tigerstripe.api.model.artifacts.ISessionArtifact.INamedQuery;
import org.eclipse.tigerstripe.api.model.artifacts.ossj.IEventDescriptorEntry;
import org.eclipse.tigerstripe.api.model.artifacts.ossj.IOssjArtifactSpecifics;
import org.eclipse.tigerstripe.api.model.artifacts.ossj.IOssjEntitySpecifics;
import org.eclipse.tigerstripe.api.model.artifacts.ossj.IOssjEnumSpecifics;
import org.eclipse.tigerstripe.api.model.artifacts.ossj.IOssjQuerySpecifics;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.model.EventDescriptorEntry;
import org.eclipse.tigerstripe.core.model.ossj.specifics.OssjArtifactSpecifics;
import org.eclipse.tigerstripe.core.model.ossj.specifics.OssjEventSpecifics;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.core.util.messages.Message;
import org.eclipse.tigerstripe.core.util.messages.MessageList;
import org.eclipse.tigerstripe.tools.compare.Difference;

/**
 * Take in an array of Differences and fix them
 * 
 * @author Richard Craddock
 * 
 */
public class DiffFixer {

	private int MESSAGE_LEVEL = 3;

	private MessageList messages;

	/**
	 * 
	 * @param diffs
	 * @param bundle
	 * @param out
	 * @param messages
	 * @return - those diffs that need a second pass
	 */
	public ArrayList<Difference> fixAll(ArrayList<Difference> diffs,
			ImportBundle bundle, PrintWriter out, MessageList messages) {

		ArrayList<Difference> secondPassDiffs = new ArrayList<Difference>();
		IArtifactManagerSession mgrSession = bundle.getMgrSession();
		Map<String, IAbstractArtifact> extractedArtifacts = bundle
				.getExtractedArtifacts();
		this.messages = messages;
		/*
		 * Every diff in the diffs should be applied to the artifacts in the
		 * project managed by this session
		 */
		if (diffs.size() > 0) {
			out.println("UPDATING MODEL WITH CHANGES ");
			addMessage("UPDATING MODEL WITH CHANGES ", 1);
		}

		// Do a sort on the diffs. Handle all of the presence thinsg first
		// this prevenst theme getting lost.
		ArrayList<Difference> sortedDiffs = sortDiffs(diffs);
		/*
		 * for (Difference diff : sortedDiffs) { out.println( "Sorted Difference : "
		 * +diff); }
		 */

		for (Difference diff : sortedDiffs) {
			out.println("Handling Difference : " + diff);
			try {
				IAbstractArtifact artifact = mgrSession
						.getArtifactByFullyQualifiedName(diff.getLocal());
				IAbstractArtifact extractedArtifact = extractedArtifacts
						.get(diff.getLocal());

				IField field = null;
				ILabel label = null;
				IMethod method = null;
				IType type = null;

				// ================== Artifacts ============================

				/**
				 * This case is for a missing artifact in the project We will
				 * *NOT* remove any extra artifacts
				 */
				if (diff.getScope().equals("Artifact")) {
					if (diff.getLocalVal().equals("present")) {
						mgrSession.addArtifact(extractedArtifact);
						IAbstractArtifact newArt = mgrSession
								.getArtifactByFullyQualifiedName(extractedArtifact
										.getFullyQualifiedName());
						// update a list of diffs that added artifacts,
						// so that we can re-check their extends in a second
						// pass
						if (extractedArtifact.getExtendedIArtifact() != null) {
							String extName = extractedArtifact
									.getExtendedIArtifact()
									.getFullyQualifiedName();
							if (!extName.equals("")) {
								secondPassDiffs.add(new Difference("value",
										diff.getLocal(), diff.getRemote(),
										"Artifact:Extends", "", extName, ""));
							}
							// set it to null for now
							newArt.setExtendedIArtifact(null);

							newArt.doSave(new TigerstripeNullProgressMonitor());
							String msgText = "INFO : Added Artifact "
									+ diff.getLocal();
							out.println(msgText);

						}

					} else {
						String msgText = "New Artifact " + diff.getLocal()
								+ " will not be changed";
						out.println("INFO : " + msgText);
						addMessage(msgText, 3);
					}
				}

				/**
				 * This case is for a difference within an Property
				 * 
				 */
				if (diff.getScope().startsWith("Artifact:Property:")) {
					IAbstractArtifact abstractArtifact = (IAbstractArtifact) artifact;
					IAbstractArtifact extractedAbstractArtifact = (IAbstractArtifact) extractedArtifact;
					if (diff.getType().equals("value")) {
						IOssjArtifactSpecifics specs = (IOssjArtifactSpecifics) abstractArtifact
								.getIStandardSpecifics();
						IOssjArtifactSpecifics extractedSpecs = (IOssjArtifactSpecifics) extractedAbstractArtifact
								.getIStandardSpecifics();
						specs.setInterfaceProperties(extractedSpecs
								.getInterfaceProperties());
						String msgText = "INFO : Updated Artifact specifics for "
								+ diff.getLocal();
						out.println(msgText);
						artifact.doSave(new TigerstripeNullProgressMonitor());
					} else {
						String msgText = "WARNING : Unable to fix diff " + diff;
						out.println(msgText);
						addMessage(msgText, 2);
					}
				}

				/**
				 * This case is for a different extends
				 * 
				 */
				if (diff.getScope().startsWith("Artifact:Extends")) {
					try {
						String fqn = diff.getLocalVal();
						IAbstractArtifact extArtifact = mgrSession
								.getArtifactByFullyQualifiedName(fqn);
						// if (extArtifact != null){
						artifact.setExtendedIArtifact(extArtifact);
						String msgText = "INFO : Updated Extends for "
								+ diff.getLocal();
						out.println(msgText);
						artifact.doSave(new TigerstripeNullProgressMonitor());
						// }
					} catch (TigerstripeException t) {

					}

				}

				/**
				 * This case is for a different implements
				 * 
				 */
				if (diff.getScope().startsWith("Artifact:Implements")) {
					try {
						String fqnList = diff.getLocalVal();
						String[] fqnArray = fqnList.split(",");
						IAbstractArtifact[] implArray = new IAbstractArtifact[fqnArray.length];
						for (int f = 0; f < fqnArray.length; f++) {
							// They might not exist just yet so make one..
							// This should have no long term effects...
							implArray[f] = mgrSession.makeArtifact(fqnArray[f]);
						}
						artifact.setImplementedArtifacts(implArray);
						String msgText = "INFO : Updated Implements for "
								+ diff.getLocal();
						out.println(msgText);
						artifact.doSave(new TigerstripeNullProgressMonitor());
						// }
					} catch (TigerstripeException t) {

					}

				}

				/**
				 * This case is for a different artifact comment
				 * 
				 */
				if (diff.getScope().startsWith("Artifact:Detail")) {
					if (diff.getObject().equals("Comment")) {
						artifact.setComment(diff.getLocalVal());
						String msgText = "INFO : Updated Comment for "
								+ diff.getLocal();
						out.println(msgText);
						artifact.doSave(new TigerstripeNullProgressMonitor());
					} else if (diff.getObject().equals("Abstract")) {
						artifact.setAbstract(!artifact.isAbstract());
						String msgText = "INFO : Updated Abstract for "
								+ diff.getLocal();
						out.println(msgText);
						artifact.doSave(new TigerstripeNullProgressMonitor());
					}
				}

				/**
				 * This case is for a different artifact stereotype
				 * 
				 */
				if (diff.getScope().startsWith("Artifact:Stereotype")) {
					String stName = "";
					String attName = "";
					String[] st = diff.getObject().split(":");
					stName = st[0];

					if (diff.getType().equals("value")) {
						attName = st[1];
						IStereotypeInstance[] insts = artifact
								.getStereotypeInstances();
						IStereotypeInstance inst;
						for (int i = 0; i < insts.length; i++) {
							if (insts[i].getName().equals(stName)) {
								inst = insts[i];
								IStereotypeInstance instAPI = (IStereotypeInstance) inst;
								IStereotypeAttribute attribute = instAPI
										.getCharacterizingIStereotype()
										.getAttributeByName(attName);

								instAPI.setAttributeValue(attribute, diff
										.getLocalVal());
								String msgText = "INFO : Updated Stereotype "
										+ stName + ":" + attName
										+ " value for " + diff.getLocal();
								out.println(msgText);
								artifact
										.doSave(new TigerstripeNullProgressMonitor());
							}
						}
						// Special case for Array Types
					} else if (diff.getScope().endsWith(":ArrayValue")) {
						TigerstripeRuntime.logInfoMessage("Dealing with array");
						if (diff.getRemoteVal().equals("absent")) {
							attName = st[1];
							String value = st[2];
							// Have to be careful here - can't do a wholesale
							// mash....
							IStereotypeInstance[] insts = artifact
									.getStereotypeInstances();
							IStereotypeInstance inst;
							for (int i = 0; i < insts.length; i++) {
								if (insts[i].getName().equals(stName)) {
									inst = insts[i];
									TigerstripeRuntime.logInfoMessage("iname "
											+ inst.getName());
									IStereotypeInstance instAPI = (IStereotypeInstance) inst;
									IStereotypeAttribute attribute = instAPI
											.getCharacterizingIStereotype()
											.getAttributeByName(attName);
									TigerstripeRuntime.logInfoMessage("sname "
											+ attName);
									if (!attribute.isArray()) {
										// This is bad...
										String msgText = "WARNING : Unable to fix diff "
												+ diff;
										out.println(msgText);
										addMessage(msgText, 2);
									} else {
										String[] vals = instAPI
												.getAttributeValues(attribute);
										TigerstripeRuntime.logInfoMessage(vals
												.toString());
										String[] newVals = new String[vals.length + 1];
										for (int v = 0; v < vals.length; v++) {
											newVals[v] = vals[v];
										}
										newVals[vals.length] = value;
										TigerstripeRuntime.logInfoMessage(vals
												.toString());
										instAPI.setAttributeValues(attribute,
												newVals);
										String msgText = "INFO : Updated Stereotype "
												+ stName
												+ ":"
												+ attName
												+ " Array value for "
												+ diff.getLocal();
										out.println(msgText);
										artifact
												.doSave(new TigerstripeNullProgressMonitor());
									}
								}
							}
						} else {
							String msgText = "INFO : New Stereotype Array Value "
									+ stName
									+ ":"
									+ attName
									+ "("
									+ st[2]
									+ ")"
									+ " on "
									+ diff.getLocal()
									+ " will not be changed";
							out.println(msgText);
						}

					} else {
						if (diff.getLocalVal().equals("present")) {
							IStereotypeInstance[] insts = extractedArtifact
									.getStereotypeInstances();
							IStereotypeInstance inst;
							for (int i = 0; i < insts.length; i++) {
								if (insts[i].getName().equals(stName)) {
									inst = insts[i];
									IStereotypeInstance instAPI = (IStereotypeInstance) inst;
									artifact.addStereotypeInstance(instAPI);
									String msgText = "INFO : Added stereotype "
											+ stName + " for "
											+ diff.getLocal();
									out.println(msgText);
									artifact
											.doSave(new TigerstripeNullProgressMonitor());
								}
							}
						} else {
							String msgText = "INFO : New Stereotype " + stName
									+ " on " + diff.getLocal()
									+ " will not be changed";
							out.println(msgText);
						}

					}
				}

				/**
				 * This case is for a difference within some OSSJ Specifics
				 * 
				 */
				if (diff.getScope().startsWith("Artifact:OSSJSpecifics")) {
					IAbstractArtifact abstractArtifact = (IAbstractArtifact) artifact;
					IAbstractArtifact extractedAbstractArtifact = (IAbstractArtifact) extractedArtifact;
					OssjArtifactSpecifics specs = (OssjArtifactSpecifics) abstractArtifact
							.getIStandardSpecifics();
					OssjArtifactSpecifics extractedSpecs = (OssjArtifactSpecifics) extractedAbstractArtifact
							.getIStandardSpecifics();
					if (diff.getObject().equals("SessionFactoryMethods")) {
						specs.setSessionFactoryMethods(extractedSpecs
								.isSessionFactoryMethods());
						String msgText = "INFO : Updated Artifact OSSJ specifics:SessionFactoryMethods for "
								+ diff.getLocal();
						out.println(msgText);
						artifact.doSave(new TigerstripeNullProgressMonitor());
					} else if (diff.getObject().equals("SingleExtension")) {
						specs.setSingleExtensionType(extractedSpecs
								.isSingleExtensionType());
						String msgText = "INFO : Updated Artifact OSSJ specifics:SingleExtensionfor "
								+ diff.getLocal();
						out.println(msgText);
						artifact.doSave(new TigerstripeNullProgressMonitor());
					}

					else {
						String msgText = "WARNING : Unable to fix diff " + diff;
						out.println(msgText);
						addMessage(msgText, 2);
					}
				}

				// ================== Fields ============================

				/**
				 * This case is for a missing field in the project We will *NOT*
				 * remove any extra fields
				 */
				if (diff.getScope().equals("Artifact:Field")) {
					if (diff.getLocalVal().equals("present")) {
						field = getIField(extractedArtifact, diff.getObject());
						artifact.addIField(field);
						String msgText = "INFO : Added Field "
								+ diff.getObject() + "on " + diff.getLocal();
						out.println(msgText);
						artifact.doSave(new TigerstripeNullProgressMonitor());
					} else {
						String msgText = "New Field " + diff.getObject()
								+ " in Artifact  " + diff.getLocal()
								+ " will not be changed";
						out.println("INFO : " + msgText);
						addMessage(msgText, 3);
					}
				}

				/**
				 * This case is for a difference within an existing Field NB
				 * Will do multiple changes in one go...
				 * 
				 * This is dangerous as we could have new Stereotypes in the
				 * model. Or new Stereotype Array Values.....
				 * 
				 */
				if (diff.getScope().startsWith("Artifact:Field:")) {
					// Can only get a "presence" diff for stereotypes.
					if (diff.getType().equals("value")
							|| ((diff.getType().equals("presence") && diff
									.getLocalVal().equals("present")) && !diff
									.getScope().endsWith(":ArrayValue"))) {

						String fieldName = "";
						if (diff.getObject().indexOf(":") > 0) {
							fieldName = diff.getObject().substring(0,
									diff.getObject().indexOf(":"));
						} else {
							fieldName = diff.getObject();
						}

						field = getIField(artifact, fieldName);
						IField diffField = getIField(extractedArtifact,
								fieldName);
						Collection<IStereotypeInstance> extraStereos = getExtraStereos(
								field, diffField);
						if (field != null) {
							IField[] fields = new IField[1];
							fields[0] = field;
							artifact.removeIFields(fields);
						}
						for (IStereotypeInstance inst : extraStereos) {
							diffField
									.addStereotypeInstance((IStereotypeInstance) inst);
							/*
							 * String msgText = "INFO : New Stereotype " +
							 * inst.getName() + " on " + diff.getLocal() + "
							 * will not be changed"; out.println(msgText);
							 */
						}
						artifact.addIField(diffField);
						artifact.doSave(new TigerstripeNullProgressMonitor());
						String msgText = "INFO : Replaced Field "
								+ diff.getObject() + " on " + diff.getLocal();
						out.println(msgText);
						artifact.doSave(new TigerstripeNullProgressMonitor());
					} else if (diff.getScope().endsWith(":ArrayValue")) {
						// out.println("Dealing with array");
						String fieldName = "";
						if (diff.getObject().indexOf(":") > 0) {
							fieldName = diff.getObject().substring(0,
									diff.getObject().indexOf(":"));
						} else {
							fieldName = diff.getObject();
						}
						field = getIField(artifact, fieldName);
						String stName = "";
						String attName = "";
						String[] st = diff.getObject().split(":");
						stName = st[1];
						if (diff.getRemoteVal().equals("absent")) {
							attName = st[2];
							String value = st[3];
							// Have to be careful here - can't do a wholesale
							// mash....
							IStereotypeInstance[] insts = field
									.getStereotypeInstances();
							IStereotypeInstance inst;
							for (int i = 0; i < insts.length; i++) {
								// out.println(insts[i].getName());
								if (insts[i].getName().equals(stName)) {
									inst = insts[i];
									// out.println("iname "+inst.getName());
									IStereotypeInstance instAPI = (IStereotypeInstance) inst;
									IStereotypeAttribute attribute = instAPI
											.getCharacterizingIStereotype()
											.getAttributeByName(attName);
									out.println("sname " + attName);
									if (!attribute.isArray()) {
										// This is bad...
										String msgText = "WARNING : Unable to fix diff "
												+ diff;
										out.println(msgText);
										addMessage(msgText, 2);
									} else {
										String[] vals = instAPI
												.getAttributeValues(attribute);
										String[] newVals = new String[vals.length + 1];
										for (int v = 0; v < vals.length; v++) {
											newVals[v] = vals[v];
											// out.println(v+newVals[v]);
										}
										newVals[vals.length] = value;
										// out.println(newVals[vals.length]);
										instAPI.setAttributeValues(attribute,
												newVals);
										String msgText = "INFO : Updated Stereotype "
												+ stName
												+ ":"
												+ attName
												+ " Array value for "
												+ diff.getLocal();
										out.println(msgText);
										artifact
												.doSave(new TigerstripeNullProgressMonitor());
									}
								}
							}
						}
					} else {
						/*
						 * String msgText = "INFO : Will not update new items " +
						 * diff.getObject() + " on " + diff.getLocal();
						 * out.println(msgText);
						 */
					}
				}

				// ================== Labels ============================

				/**
				 * This case is for a missing label in the project We will *NOT*
				 * remove any extra labels
				 * 
				 * 
				 */
				if (diff.getScope().equals("Artifact:Label")) {
					if (diff.getLocalVal().equals("present")) {
						label = getILabel(extractedArtifact, diff.getObject());
						artifact.addILabel(label);
						artifact.doSave(new TigerstripeNullProgressMonitor());
						String msgText = "INFO : Added Label "
								+ diff.getObject() + " on " + diff.getLocal();
						out.println(msgText);
					} else {
						String msgText = "New Label " + diff.getObject()
								+ " in Artifact " + diff.getLocal()
								+ " will not be changed";
						out.println("INFO : " + msgText);
						addMessage(msgText, 3);
					}
				}

				/**
				 * This case is for a difference within an existing Label NB
				 * Will do multiple changes in one go...
				 * 
				 * This is dangerous as we could have added Stereotypes in the
				 * model. Or new Stereotype Array Values.....
				 * 
				 * Copy out "new Stereotypes" - Replace Label - Add back "new
				 * Stereotypes"
				 * 
				 */
				if (diff.getScope().startsWith("Artifact:Label:")) {
					// Can get a "presence" diff for stereotypes.
					if (diff.getType().equals("value")
							|| ((diff.getType().equals("presence") && diff
									.getLocalVal().equals("present")) && !diff
									.getScope().endsWith(":ArrayValue"))) {
						String labelName = "";
						if (diff.getObject().indexOf(":") > 0) {
							labelName = diff.getObject().substring(0,
									diff.getObject().indexOf(":"));
						} else {
							labelName = diff.getObject();
						}

						label = getILabel(artifact, labelName);
						ILabel diffLabel = getILabel(extractedArtifact,
								labelName);
						Collection<IStereotypeInstance> extraStereos = getExtraStereos(
								label, diffLabel);
						if (label != null) {
							ILabel[] labels = new ILabel[1];
							labels[0] = label;
							artifact.removeILabels(labels);
						}

						artifact.addILabel(diffLabel);
						for (IStereotypeInstance inst : extraStereos) {
							diffLabel
									.addStereotypeInstance((IStereotypeInstance) inst);
							/*
							 * String msgText = "INFO : New Stereotype " +
							 * inst.getName() + " on " + diff.getLocal() + "
							 * will not be changed"; out.println(msgText);
							 */
						}
						String msgText = "INFO : Replaced Label "
								+ diff.getObject() + " on " + diff.getLocal();
						out.println(msgText);
						artifact.doSave(new TigerstripeNullProgressMonitor());
					} else if (diff.getScope().endsWith(":ArrayValue")) {
						String labelName;
						if (diff.getObject().indexOf(":") > 0) {
							labelName = diff.getObject().substring(0,
									diff.getObject().indexOf(":"));
						} else {
							labelName = diff.getObject();
						}
						label = getILabel(artifact, labelName);
						// out.println("Dealing with array");
						String stName = "";
						String attName = "";
						String[] st = diff.getObject().split(":");
						stName = st[1];
						if (diff.getRemoteVal().equals("absent")) {
							attName = st[2];
							String value = st[3];
							// Have to be careful here - can't do a wholesale
							// mash....
							IStereotypeInstance[] insts = label
									.getStereotypeInstances();
							IStereotypeInstance inst;
							for (int i = 0; i < insts.length; i++) {
								if (insts[i].getName().equals(stName)) {
									inst = insts[i];
									// out.println("iname "+inst.getName());
									IStereotypeInstance instAPI = (IStereotypeInstance) inst;
									IStereotypeAttribute attribute = instAPI
											.getCharacterizingIStereotype()
											.getAttributeByName(attName);
									// out.println("sname "+attName);
									if (!attribute.isArray()) {
										// This is bad...
										String msgText = "WARNING : Unable to fix diff "
												+ diff;
										out.println(msgText);
										addMessage(msgText, 2);
									} else {
										String[] vals = instAPI
												.getAttributeValues(attribute);
										// out.println(vals.toString());
										String[] newVals = new String[vals.length + 1];
										for (int v = 0; v < vals.length; v++) {
											newVals[v] = vals[v];
										}
										newVals[vals.length] = value;
										// out.println(vals.toString());
										instAPI.setAttributeValues(attribute,
												newVals);
										String msgText = "INFO : Updated Stereotype "
												+ stName
												+ ":"
												+ attName
												+ " Array value for "
												+ diff.getLocal();
										out.println(msgText);
										artifact
												.doSave(new TigerstripeNullProgressMonitor());
									}
								}
							}
						}
					} else {
						/*
						 * String msgText = "INFO : Will not update new items " +
						 * diff.getObject() + " on " + diff.getLocal();
						 * out.println(msgText);
						 */
					}
				}

				// ================== Methods ============================

				/**
				 * This case is for a missing method in the project We will
				 * *NOT* remove any extra methods
				 * 
				 * 
				 */
				if (diff.getScope().equals("Artifact:Method")) {
					if (diff.getLocalVal().equals("present")) {
						method = getIMethod(extractedArtifact, diff.getObject());
						artifact.addIMethod(method);
						artifact.doSave(new TigerstripeNullProgressMonitor());
						String msgText = "INFO : Added Method "
								+ diff.getObject() + " on " + diff.getLocal();
						out.println(msgText);
					} else {
						String msgText = "New Method " + diff.getObject()
								+ " in Artifact " + diff.getLocal()
								+ " will not be changed";
						out.println("INFO : " + msgText);
						addMessage(msgText, 3);
					}
				}

				/**
				 * This case is for a difference within an existing Method NB
				 * Will do multiple changes in one go...
				 * 
				 * For method this is dangerous as we could have added
				 * Arguments, Exceptions or Stereotypes. Or new Stereotype Array
				 * Values.....
				 */
				if (diff.getScope().startsWith("Artifact:Method:")) {
					// Can get presence error on exception or stereotype or
					// argument
					if (diff.getType().equals("value")
							|| ((diff.getType().equals("presence") && diff
									.getLocalVal().equals("present")) && !diff
									.getScope().endsWith(":ArrayValue"))) {

						String methodName = "";
						if (diff.getObject().indexOf(":") > 0) {
							methodName = diff.getObject().substring(0,
									diff.getObject().indexOf(":"));
						} else {
							methodName = diff.getObject();
						}
						method = getIMethod(artifact, methodName);
						IMethod diffMethod = getIMethod(extractedArtifact,
								methodName);
						Collection<IStereotypeInstance> extraStereos = getExtraStereos(
								method, diffMethod);
						Collection<IException> extraExceptions = getExtraExceptions(
								method, diffMethod);
						Collection<IArgument> extraArguments = getExtraArguments(
								method, diffMethod);
						if (method != null) {
							IMethod[] methods = new IMethod[1];
							methods[0] = method;
							artifact.removeIMethods(methods);
						}
						for (IStereotypeInstance inst : extraStereos) {
							diffMethod
									.addStereotypeInstance((IStereotypeInstance) inst);
							/*
							 * String msgText = "INFO : New Stereotype " +
							 * inst.getName() + " on " + diff.getLocal() + "
							 * will not be changed"; out.println(msgText);
							 */
						}
						for (IException exception : extraExceptions) {
							diffMethod.addIException(exception);
							/*
							 * String msgText = "INFO : New Exception " +
							 * exception.getName() + " on " + diff.getLocal() + "
							 * will not be changed"; out.println(msgText);
							 */
						}
						for (IArgument argument : extraArguments) {
							diffMethod.addIArgument(argument);
							/*
							 * String msgText = "INFO : New Argument " +
							 * argument.getName() + " on " + diff.getLocal() + "
							 * will not be changed"; out.println(msgText);
							 */
						}
						artifact.addIMethod(diffMethod);
						artifact.doSave(new TigerstripeNullProgressMonitor());
						String msgText = "INFO : Replaced Method " + methodName
								+ " on " + diff.getLocal();
						out.println(msgText);
						artifact.doSave(new TigerstripeNullProgressMonitor());

					} else if (diff.getScope().endsWith(":ArrayValue")) {
						String methodName;
						if (diff.getObject().indexOf(":") > 0) {
							methodName = diff.getObject().substring(0,
									diff.getObject().indexOf(":"));
						} else {
							methodName = diff.getObject();
						}
						method = getIMethod(artifact, methodName);
						// out.println("Dealing with array");
						String stName = "";
						String attName = "";
						String[] st = diff.getObject().split(":");
						stName = st[1];
						if (diff.getRemoteVal().equals("absent")) {
							attName = st[2];
							String value = st[3];
							// Have to be careful here - can't do a wholesale
							// mash....
							IStereotypeInstance[] insts = method
									.getStereotypeInstances();
							IStereotypeInstance inst;
							// out.println("Len" +insts.length);
							for (int i = 0; i < insts.length; i++) {
								// out.println(insts[i].getName());
								if (insts[i].getName().equals(stName)) {
									inst = insts[i];
									// out.println("iname "+inst.getName());
									IStereotypeInstance instAPI = (IStereotypeInstance) inst;
									IStereotypeAttribute attribute = instAPI
											.getCharacterizingIStereotype()
											.getAttributeByName(attName);
									// out.println("sname "+attName);
									if (!attribute.isArray()) {
										// This is bad...
										String msgText = "WARNING : Unable to fix diff "
												+ diff;
										out.println(msgText);
										addMessage(msgText, 2);
									} else {
										String[] vals = instAPI
												.getAttributeValues(attribute);
										String[] newVals = new String[vals.length + 1];
										for (int v = 0; v < vals.length; v++) {
											newVals[v] = vals[v];
											// out.println(v+newVals[v]);
										}
										newVals[vals.length] = value;
										// out.println(newVals[vals.length]);
										instAPI.setAttributeValues(attribute,
												newVals);
										String msgText = "INFO : Updated Stereotype "
												+ stName
												+ ":"
												+ attName
												+ " Array value for "
												+ diff.getLocal();
										out.println(msgText);
										artifact
												.doSave(new TigerstripeNullProgressMonitor());
									}
								}
							}
						}

					} else {
						/*
						 * String msgText = "INFO : Will not update new items " +
						 * diff.getObject() + " on " + diff.getLocal();
						 * out.println(msgText);
						 */
					}
				}
				// ================== Associations ============================

				/**
				 * This case is for a difference on an Association End NB Will
				 * do multiple changes in one go...
				 */
				if (diff.getScope().startsWith("Association:AssociationEnd:")) {
					// Need to know if A or Z
					IAssociationEnd end = null;
					IAssociationEnd extractedEnd = null;
					if (diff.getScope().startsWith(
							"Association:AssociationEnd:A")) {
						end = ((IAssociationArtifact) artifact).getAEnd();
						extractedEnd = ((IAssociationArtifact) extractedArtifact)
								.getAEnd();
						if (extractedEnd instanceof IAssociationEnd) {
							((IAssociationArtifact) artifact)
									.setAEnd((IAssociationEnd) extractedEnd);
							String msgText = "INFO : Updated End A "
									+ diff.getObject() + " on "
									+ diff.getLocal();
							out.println(msgText);
						}
					} else {
						end = ((IAssociationArtifact) artifact).getZEnd();
						extractedEnd = ((IAssociationArtifact) extractedArtifact)
								.getZEnd();
						if (extractedEnd instanceof IAssociationEnd) {
							((IAssociationArtifact) artifact)
									.setZEnd((IAssociationEnd) extractedEnd);
							String msgText = "INFO : Updated End Z "
									+ diff.getObject() + " on "
									+ diff.getLocal();
							out.println(msgText);
						}
					}
					artifact.doSave(new TigerstripeNullProgressMonitor());
				}

				// ================== Dependencies ============================

				/**
				 * This case is for a difference on a Dependency End NB Will do
				 * multiple changes in one go...
				 */
				if (diff.getScope().startsWith("Dependency:DependencyEnd:")) {
					// Need to know if A or Z
					IDependencyArtifact dependency = (IDependencyArtifact) artifact;
					IDependencyArtifact extractedDependency = (IDependencyArtifact) extractedArtifact;
					if (diff.getScope()
							.startsWith("Dependency:DependencyEnd:A")) {
						dependency
								.setAEndType((IType) dependency.getAEndType());
						String msgText = "INFO : Updated End A "
								+ diff.getObject() + " on " + diff.getLocal();
						out.println(msgText);
					} else {
						dependency
								.setZEndType((IType) dependency.getZEndType());
						String msgText = "INFO : Updated End A "
								+ diff.getObject() + " on " + diff.getLocal();
						out.println(msgText);
					}
					artifact.doSave(new TigerstripeNullProgressMonitor());
				}

				/**
				 * This case is for a difference on a ManagedEntity Specifics
				 * 
				 */
				if (diff.getScope().startsWith("ManagedEntity:Specifics")) {
					IManagedEntityArtifact entity = (IManagedEntityArtifact) artifact;
					IManagedEntityArtifact extractedEntity = (IManagedEntityArtifact) extractedArtifact;
					IOssjEntitySpecifics entitySpecs = (IOssjEntitySpecifics) entity
							.getIStandardSpecifics();
					IOssjEntitySpecifics extractedSpecs = (IOssjEntitySpecifics) extractedEntity
							.getIStandardSpecifics();
					if (diff.getObject().equals("PrimaryKeyType")) {
						entitySpecs.setPrimaryKey(extractedSpecs
								.getPrimaryKey());
						artifact.doSave(new TigerstripeNullProgressMonitor());
						String msgText = "INFO : Updated PrimaryKeyType "
								+ diff.getObject() + " on " + diff.getLocal();
						out.println(msgText);
						artifact.doSave(new TigerstripeNullProgressMonitor());
					} else if (diff.getObject().equals("ExtensibilityType")) {
						entitySpecs.setExtensibilityType(extractedSpecs
								.getExtensibilityType());
						artifact.doSave(new TigerstripeNullProgressMonitor());
						String msgText = "INFO : Updated ExtensibilityType "
								+ diff.getObject() + " on " + diff.getLocal();
						out.println(msgText);
						artifact.doSave(new TigerstripeNullProgressMonitor());
					} else {
						String msgText = "WARNING : Unable to fix diff " + diff;
						out.println(msgText);
						addMessage(msgText, 2);
					}
				}

				/**
				 * This case is for a difference on a ManagedEntity
				 * MethodDetails
				 * 
				 */
				if (diff.getScope().startsWith("ManagedEntity:MethodDetails")) {
					IManagedEntityArtifact entity = (IManagedEntityArtifact) artifact;
					IManagedEntityArtifact extractedEntity = (IManagedEntityArtifact) extractedArtifact;
					IOssjEntitySpecifics entitySpecs = (IOssjEntitySpecifics) entity
							.getIStandardSpecifics();
					IOssjEntitySpecifics extractedSpecs = (IOssjEntitySpecifics) extractedEntity
							.getIStandardSpecifics();

					String[] objectBits = diff.getObject().split(":");
					String detailsMethodName = objectBits[0];
					String flavorName = objectBits[1];

					OssjEntityMethodFlavor flavEnum = OssjEntityMethodFlavor
							.valueFromPojoLabel(flavorName);
					IMethod flavorMethod = null;
					IEntityMethodFlavorDetails details = null;

					if ("create".equals(detailsMethodName)) {
						details = entitySpecs.getCRUDFlavorDetails(
								IOssjEntitySpecifics.CREATE, flavEnum);
					} else if ("get".equals(detailsMethodName)) {
						details = entitySpecs.getCRUDFlavorDetails(
								IOssjEntitySpecifics.GET, flavEnum);
					} else if ("set".equals(detailsMethodName)) {
						details = entitySpecs.getCRUDFlavorDetails(
								IOssjEntitySpecifics.SET, flavEnum);
					} else if ("remove".equals(detailsMethodName)) {
						details = entitySpecs.getCRUDFlavorDetails(
								IOssjEntitySpecifics.DELETE, flavEnum);
					} else {
						boolean setone = false;
						for (IMethod ossjMethod : artifact.getIMethods()) {
							if (ossjMethod.getName().equals(detailsMethodName)) {
								flavorMethod = ossjMethod;
								details = ossjMethod
										.getEntityMethodFlavorDetails(flavEnum);
								setone = true;
							}
						}
						if (!setone) {
							String msgText = "Failed to find Flavor details - could not find method "
									+ detailsMethodName
									+ " on "
									+ artifact.getFullyQualifiedName();
							addMessage(msgText, 0);
							out.println("Error : " + msgText);
							break;
						}
					}
					// value can be for flag or comment.
					if (diff.getType().equals("value")) {
						if (objectBits[objectBits.length - 1].equals("Flag")) {
							details.setFlag(diff.getLocalVal());
						} else if (objectBits[objectBits.length - 1]
								.equals("Comment")) {
							details.setComment(diff.getLocalVal());
						}
					} else if (diff.getRemoteVal().equals("absent")) {
						// presence for exceptions.
						// The name is the last bit of the scope string
						details.addException(objectBits[objectBits.length - 1]);
					} else {
						String msgText = "INFO : New MethodDetails Exception "
								+ objectBits[objectBits.length - 1] + " on "
								+ diff.getLocal() + "  will not be changed";
						out.println(msgText);
					}
					if ("create".equals(detailsMethodName)) {
						entitySpecs.setCRUDFlavorDetails(
								IOssjEntitySpecifics.CREATE, flavEnum, details);
					} else if ("get".equals(detailsMethodName)) {
						entitySpecs.setCRUDFlavorDetails(
								IOssjEntitySpecifics.GET, flavEnum, details);
					} else if ("set".equals(detailsMethodName)) {
						entitySpecs.setCRUDFlavorDetails(
								IOssjEntitySpecifics.SET, flavEnum, details);
					} else if ("remove".equals(detailsMethodName)) {
						entitySpecs.setCRUDFlavorDetails(
								IOssjEntitySpecifics.DELETE, flavEnum, details);
					} else {
						// a method variant
						flavorMethod.setEntityMethodFlavorDetails(flavEnum,
								details);
					}
					artifact.doSave(new TigerstripeNullProgressMonitor());
					String msgText = "INFO : Updated FlavorDetails on "
							+ diff.getLocal();
					out.println(msgText);

				}

				/**
				 * This case is for a difference on a Enumeration Specifics
				 * 
				 */
				if (diff.getScope().equals("Enumeration:Specifics")) {
					IEnumArtifact enumeration = (IEnumArtifact) artifact;
					IEnumArtifact extractedEnumeration = (IEnumArtifact) extractedArtifact;
					IOssjEnumSpecifics enumSpecs = (IOssjEnumSpecifics) enumeration
							.getIStandardSpecifics();
					IOssjEnumSpecifics extractedSpecs = (IOssjEnumSpecifics) extractedEnumeration
							.getIStandardSpecifics();
					if (diff.getObject().equals("BaseType")) {
						enumSpecs.setBaseIType(extractedSpecs.getBaseIType());
						artifact.doSave(new TigerstripeNullProgressMonitor());
						String msgText = "INFO : Updated BaseType on "
								+ diff.getLocal();
						out.println(msgText);
					} else if (diff.getObject().equals("Extensible")) {
						enumSpecs.setExtensible(extractedSpecs.getExtensible());
						artifact.doSave(new TigerstripeNullProgressMonitor());
						String msgText = "INFO : Updated Extensible  on "
								+ diff.getLocal();
						out.println(msgText);
					} else {
						String msgText = "WARNING : Unable to fix diff " + diff;
						out.println(msgText);
						addMessage(msgText, 2);
					}

				}

				/**
				 * This case is for a difference on a Query Specifics
				 * 
				 */
				if (diff.getScope().equals("Query:Specifics")) {
					IQueryArtifact query = (IQueryArtifact) artifact;
					IQueryArtifact extractedQuery = (IQueryArtifact) extractedArtifact;
					if (diff.getObject().equals("ReturnedEntityType")) {
						IOssjQuerySpecifics querySpecs = (IOssjQuerySpecifics) query
								.getIStandardSpecifics();
						IOssjQuerySpecifics extractedSpecs = (IOssjQuerySpecifics) extractedQuery
								.getIStandardSpecifics();
						querySpecs.setReturnedEntityIType(extractedSpecs
								.getReturnedEntityIType());
						artifact.doSave(new TigerstripeNullProgressMonitor());
						String msgText = "INFO : Updated ReturnType "
								+ diff.getObject() + " on " + diff.getLocal();
						out.println(msgText);
					} else {
						String msgText = "WARNING : Unable to fix diff " + diff;
						out.println(msgText);
						addMessage(msgText, 2);
					}
				}

				/**
				 * This case is for a difference on a Event Specifics
				 * 
				 */
				if (diff.getScope().startsWith("Event:Specifics")) {
					IEventArtifact event = (IEventArtifact) artifact;
					IEventArtifact extractedEvent = (IEventArtifact) extractedArtifact;
					OssjEventSpecifics eventSpecs = (OssjEventSpecifics) event
							.getIStandardSpecifics();
					OssjEventSpecifics extractedSpecs = (OssjEventSpecifics) extractedEvent
							.getIStandardSpecifics();
					if (diff.getScope().endsWith(":EventDescriptorEntry")) {

						if (diff.getType().equals("value")) {
							IEventDescriptorEntry[] entries = eventSpecs
									.getEventDescriptorEntries();
							// find the entry with the right name
							for (int e = 0; e < entries.length; e++) {
								if (entries[e].getLabel().equals(
										diff.getObject())) {
									entries[e].setPrimitiveType(diff
											.getLocalVal());
									eventSpecs
											.setEventDescriptorEntries(entries);
								}
							}
							artifact
									.doSave(new TigerstripeNullProgressMonitor());
							String msgText = "INFO : EventDescriptorEntry "
									+ diff.getObject() + " updated on "
									+ diff.getLocal();
							out.println(msgText);
						} else if (diff.getRemoteVal().equals("absent")) {
							IEventDescriptorEntry[] entries = extractedSpecs
									.getEventDescriptorEntries();
							for (int e = 0; e < entries.length; e++) {
								if (entries[e].getLabel().equals(
										diff.getObject())) {
									IEventDescriptorEntry newOne = entries[e];
									// Add it to the other side...
									IEventDescriptorEntry[] eventEntries = eventSpecs
											.getEventDescriptorEntries();
									ArrayList<IEventDescriptorEntry> entList = new ArrayList<IEventDescriptorEntry>(
											Arrays.asList(eventEntries));
									entList.add(newOne);
									eventSpecs
											.setEventDescriptorEntries(entList
													.toArray(new EventDescriptorEntry[0]));
								}
							}
							artifact
									.doSave(new TigerstripeNullProgressMonitor());
							String msgText = "INFO : EventDescriptorEntry "
									+ diff.getObject() + " added to "
									+ diff.getLocal();
							out.println(msgText);
						} else {
							String msgText = "INFO : New EventDescriptorEntry "
									+ diff.getObject() + " on "
									+ diff.getLocal() + " will not be changed";
							out.println(msgText);
						}
					} else if (diff.getScope().endsWith(
							":CustomEventDescriptorEntry")) {

						if (diff.getType().equals("value")) {
							IEventDescriptorEntry[] entries = eventSpecs
									.getCustomEventDescriptorEntries();
							// find the entry with the right name
							for (int e = 0; e < entries.length; e++) {
								if (entries[e].getLabel().equals(
										diff.getObject())) {
									entries[e].setPrimitiveType(diff
											.getLocalVal());
									eventSpecs
											.setCustomEventDescriptorEntries(entries);
								}
							}
							artifact
									.doSave(new TigerstripeNullProgressMonitor());
							String msgText = "INFO : CustomEventDescriptorEntry "
									+ diff.getObject()
									+ " updated on "
									+ diff.getLocal();
							out.println(msgText);
						} else if (diff.getRemoteVal().equals("absent")) {
							IEventDescriptorEntry[] entries = extractedSpecs
									.getCustomEventDescriptorEntries();
							for (int e = 0; e < entries.length; e++) {
								if (entries[e].getLabel().equals(
										diff.getObject())) {
									IEventDescriptorEntry newOne = entries[e];
									// Add it to the other side...
									IEventDescriptorEntry[] eventEntries = eventSpecs
											.getCustomEventDescriptorEntries();
									ArrayList<IEventDescriptorEntry> entList = new ArrayList<IEventDescriptorEntry>(
											Arrays.asList(eventEntries));
									entList.add(newOne);
									eventSpecs
											.setCustomEventDescriptorEntries(entList
													.toArray(new EventDescriptorEntry[0]));
								}
							}
							artifact
									.doSave(new TigerstripeNullProgressMonitor());
							String msgText = "INFO : CustomEventDescriptorEntry "
									+ diff.getObject()
									+ " added to "
									+ diff.getLocal();
							out.println(msgText);
						} else {
							String msgText = "INFO : New CustomEventDescriptorEntry "
									+ diff.getObject()
									+ " on "
									+ diff.getLocal() + " will not be changed";
							out.println(msgText);
						}
					} else {
						String msgText = "WARNING : Unable to fix diff " + diff;
						out.println(msgText);
						addMessage(msgText, 2);
					}
				}

				/**
				 * This case is for a difference on a Session Specifics
				 * 
				 */
				if (diff.getScope().startsWith("Session:")) {
					ISessionArtifact session = (ISessionArtifact) artifact;
					ISessionArtifact extractedSession = (ISessionArtifact) extractedArtifact;
					if (diff.getScope().startsWith("Session:ManagedEntity")) {
						if (diff.getLocalVal().equals("absent")) {
							// This is a new MODEL thing
							String msgText = "INFO : New Managed Entity on "
									+ diff.getObject() + " on "
									+ diff.getLocal() + " will not be changed";
							out.println(msgText);
						} else {
							// This is new XML Thing
							IManagedEntityDetails q = session
									.makeIManagedEntityDetails();
							q.setFullyQualifiedName(diff.getObject());
							session.addIManagedEntityDetails(q);
							session
									.doSave(new TigerstripeNullProgressMonitor());
							String msgText = "INFO : Managed Entity "
									+ diff.getObject() + " to "
									+ diff.getLocal();
							out.println(msgText);
						}
					} else if (diff.getScope().startsWith("Session:Event")) {
						if (diff.getLocalVal().equals("absent")) {
							// This is a new MODEL thing
							String msgText = "INFO : New Event on "
									+ diff.getObject() + " on "
									+ diff.getLocal() + " will not be changed";
							out.println(msgText);
						} else {
							// This is new XML Thing
							IEmittedEvent q = session.makeIEmittedEvent();
							q.setFullyQualifiedName(diff.getObject());
							session.addIEmittedEvent(q);
							session
									.doSave(new TigerstripeNullProgressMonitor());
							String msgText = "INFO : Added Event "
									+ diff.getObject() + " to "
									+ diff.getLocal();
							out.println(msgText);
						}
					} else if (diff.getScope().startsWith(
							"Session:UpdateProcedure")) {
						if (diff.getLocalVal().equals("absent")) {
							// This is a new MODEL thing
							String msgText = "INFO : New UpdateProcedure on "
									+ diff.getObject() + " on "
									+ diff.getLocal() + " will not be changed";
							out.println(msgText);
						} else {
							// This is new XML Thing
							IExposedUpdateProcedure q = session
									.makeIExposedUpdateProcedure();
							q.setFullyQualifiedName(diff.getObject());
							session.addIExposedUpdateProcedure(q);
							session
									.doSave(new TigerstripeNullProgressMonitor());
							String msgText = "INFO : Added UpdateProcedure "
									+ diff.getObject() + " to "
									+ diff.getLocal();
							out.println(msgText);
						}
					} else if (diff.getScope().startsWith("Session:Query")) {
						if (diff.getLocalVal().equals("absent")) {
							// This is a new MODEL thing
							String msgText = "INFO : New Named Query on "
									+ diff.getObject() + " on "
									+ diff.getLocal() + " will not be changed";
							out.println(msgText);
						} else {
							// This is new XML Thing
							INamedQuery q = session.makeINamedQuery();
							q.setFullyQualifiedName(diff.getObject());
							session.addINamedQuery(q);
							session
									.doSave(new TigerstripeNullProgressMonitor());
							String msgText = "INFO : Added Named Query "
									+ diff.getObject() + " to "
									+ diff.getLocal();
							out.println(msgText);
						}

					} else {
						String msgText = "WARNING : Unable to fix diff " + diff;
						out.println(msgText);
						addMessage(msgText, 2);
					}
				}

			} catch (TigerstripeException t) {
				String msgText = "ERROR : Failed on diff : " + diff;
				out.println(msgText);
				addMessage(msgText, 0);
				t.printStackTrace(out);
				continue;
			}
		}
		return secondPassDiffs;
	}

	public Collection<IStereotypeInstance> getExtraStereos(
			IModelComponent component, IModelComponent extractedComponent) {
		/*
		 * extra ones are ones that are in the MODEL and not the import
		 * 
		 */
		if (component.getStereotypeInstances().length > 0) {
			Collection<IStereotypeInstance> remainingModelStereos = new ArrayList(
					Arrays.asList(component.getStereotypeInstances()));
			Collection<IStereotypeInstance> modelStereos = Arrays
					.asList(component.getStereotypeInstances());
			for (IStereotypeInstance stereo : modelStereos) {
				// go on the name..
				for (IStereotypeInstance exStereo : Arrays
						.asList(extractedComponent.getStereotypeInstances())) {
					if (exStereo.getName().equals(stereo.getName())) {
						remainingModelStereos.remove(stereo);
					}
				}
			}

			return remainingModelStereos;
		} else
			return new ArrayList();
	}

	public Collection<IException> getExtraExceptions(IMethod component,
			IMethod extractedComponent) {
		/*
		 * extra ones are ones that are in the MODEL and not the import
		 */
		if (component.getIExceptions().length > 0) {
			Collection<IException> remainingExceptions = new ArrayList(Arrays
					.asList(component.getIExceptions()));
			Collection<IException> modelExceptions = Arrays.asList(component
					.getIExceptions());
			for (IException excep : modelExceptions) {
				// go on the name..
				for (IException exException : Arrays.asList(extractedComponent
						.getIExceptions())) {
					if (exException.getName().equals(excep.getName())) {
						remainingExceptions.remove(excep);
					}
				}
			}
			return remainingExceptions;
		} else
			return new ArrayList();
	}

	public Collection<IArgument> getExtraArguments(IMethod component,
			IMethod extractedComponent) {
		/*
		 * extra ones are ones that are in the MODEL and not the import.
		 * 
		 * NOTE Order is important, so just cut off the length that are alreday
		 * there
		 */
		IArgument[] allArguments = component.getIArguments();
		IArgument[] exArguments = extractedComponent.getIArguments();
		if (component.getIArguments().length - exArguments.length > 0) {
			IArgument[] remainingArguments = new IArgument[component
					.getIArguments().length
					- exArguments.length];
			for (int i = 0; i < remainingArguments.length; i++) {
				int arg = extractedComponent.getIArguments().length + i;
				// TigerstripeRuntime.logInfoMessage(i + " " + arg);
				remainingArguments[i] = allArguments[arg];
				// TigerstripeRuntime.logInfoMessage(remainingArguments[i].getName());
				// ;
			}
			return Arrays.asList(remainingArguments);
		}
		return Arrays.asList(new IArgument[0]);
	}

	public IField getIField(IAbstractArtifact artifact, String fieldName) {
		IField[] fields = artifact.getIFields();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getName().equals(fieldName))
				return fields[i];
		}
		return null;
	}

	public ILabel getILabel(IAbstractArtifact artifact, String labelName) {
		ILabel[] labels = artifact.getILabels();
		for (int i = 0; i < labels.length; i++) {
			if (labels[i].getName().equals(labelName))
				return labels[i];
		}
		return null;
	}

	public IMethod getIMethod(IAbstractArtifact artifact, String methodName) {
		IMethod[] methods = artifact.getIMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equals(methodName))
				return methods[i];
		}
		return null;
	}

	public ArrayList<Difference> sortDiffs(ArrayList<Difference> inDiffs) {
		ArrayList<Difference> sortedDiffs = new ArrayList<Difference>();
		ArrayList<Difference> tempDiffs = new ArrayList<Difference>();
		for (Difference diff : inDiffs) {
			if (diff.getType().equals("value")) {
				sortedDiffs.add(diff);
			} else {
				tempDiffs.add(diff);
			}
		}
		sortedDiffs.addAll(tempDiffs);
		return sortedDiffs;
	}

	private void addMessage(String msgText, int severity) {
		if (severity <= MESSAGE_LEVEL) {
			Message newMsg = new Message();
			newMsg.setMessage(msgText);
			newMsg.setSeverity(severity);
			this.messages.addMessage(newMsg);
		}

	}

}
