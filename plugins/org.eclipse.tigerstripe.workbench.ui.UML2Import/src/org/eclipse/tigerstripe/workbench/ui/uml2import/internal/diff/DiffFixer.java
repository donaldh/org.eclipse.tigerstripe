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
package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.diff;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventDescriptorEntry;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjArtifactSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjEventSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.internal.tools.compare.Difference;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.OssjEntityMethodFlavor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IEmittedEvent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IEntityMethodFlavorDetails;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IExposedUpdateProcedure;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IManagedEntityDetails;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.INamedQuery;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IEventDescriptorEntry;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjArtifactSpecifics;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjEntitySpecifics;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjEnumSpecifics;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjQuerySpecifics;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;

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
			Map<String, IAbstractArtifact> extractedArtifacts, IArtifactManagerSession mgrSession,  PrintWriter out, MessageList messages) {

		ArrayList<Difference> secondPassDiffs = new ArrayList<Difference>();

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
				ILiteral literal = null;
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
						if (extractedArtifact.getExtendedArtifact() != null) {
							String extName = extractedArtifact
									.getExtendedArtifact()
									.getFullyQualifiedName();
							if (!extName.equals("")) {
								secondPassDiffs.add(new Difference("value",
										diff.getLocal(), diff.getRemote(),
										"Artifact:Extends", "", extName, ""));
							}
							// set it to null for now
							newArt.setExtendedArtifact(null);
						}
							mgrSession.addArtifact(newArt);
							newArt.doSave(new NullProgressMonitor());
							mgrSession.getArtifactByFullyQualifiedName(newArt.getFullyQualifiedName());
							String msgText = "INFO : Added Artifact "
									+ diff.getLocal();
							out.println(msgText);


					} else {
						String msgText = "Project Artifact (Not in the import file) " + diff.getLocal()
								+ " will not be changed.";
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
						artifact.doSave(new NullProgressMonitor());
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
						artifact.setExtendedArtifact(extArtifact);
						String msgText = "INFO : Updated Extends for "
								+ diff.getLocal();
						out.println(msgText);
						artifact.doSave(new NullProgressMonitor());
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
						Collection<IAbstractArtifact> implArray = new ArrayList<IAbstractArtifact>();
						for (int f = 0; f < fqnArray.length; f++) {
							// They might not exist just yet so make one..
							// This should have no long term effects...
							implArray.add((IAbstractArtifact) mgrSession.makeArtifact(fqnArray[f]));
						}
						artifact.setImplementedArtifacts(implArray);
						String msgText = "INFO : Updated Implements for "
								+ diff.getLocal();
						out.println(msgText);
						artifact.doSave(new NullProgressMonitor());
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
						artifact.doSave(new NullProgressMonitor());
					} else if (diff.getObject().equals("Abstract")) {
						artifact.setAbstract(!artifact.isAbstract());
						String msgText = "INFO : Updated Abstract for "
								+ diff.getLocal();
						out.println(msgText);
						artifact.doSave(new NullProgressMonitor());
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
						Collection<IStereotypeInstance> insts = artifact
								.getStereotypeInstances();
						
						for (IStereotypeInstance inst :insts) {
							if (inst.getName().equals(stName)) {
								IStereotypeInstance instAPI = (IStereotypeInstance) inst;
								IStereotypeAttribute attribute = instAPI
										.getCharacterizingStereotype()
										.getAttributeByName(attName);

								instAPI.setAttributeValue(attribute, diff
										.getLocalVal());
								String msgText = "INFO : Updated Stereotype "
										+ stName + ":" + attName
										+ " value for " + diff.getLocal();
								out.println(msgText);
								artifact
										.doSave(new NullProgressMonitor());
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
							Collection<IStereotypeInstance> insts = artifact
									.getStereotypeInstances();
							
							for (IStereotypeInstance inst :insts) {
								if (inst.getName().equals(stName)) {
									TigerstripeRuntime.logInfoMessage("iname "
											+ inst.getName());
									IStereotypeInstance instAPI = (IStereotypeInstance) inst;
									IStereotypeAttribute attribute = instAPI
											.getCharacterizingStereotype()
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
												.doSave(new NullProgressMonitor());
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
							Collection<IStereotypeInstance> insts = extractedArtifact
									.getStereotypeInstances();
							
							for (IStereotypeInstance inst : insts) {
								if (inst.getName().equals(stName)) {

									IStereotypeInstance instAPI = (IStereotypeInstance) inst;
									artifact.addStereotypeInstance(instAPI);
									String msgText = "INFO : Added stereotype "
											+ stName + " for "
											+ diff.getLocal();
									out.println(msgText);
									artifact
											.doSave(new NullProgressMonitor());
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
						artifact.doSave(new NullProgressMonitor());
					} else if (diff.getObject().equals("SingleExtension")) {
						specs.setSingleExtensionType(extractedSpecs
								.isSingleExtensionType());
						String msgText = "INFO : Updated Artifact OSSJ specifics:SingleExtensionfor "
								+ diff.getLocal();
						out.println(msgText);
						artifact.doSave(new NullProgressMonitor());
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
						artifact.addField(field);
						String msgText = "INFO : Added Field "
								+ diff.getObject() + "on " + diff.getLocal();
						out.println(msgText);
						artifact.doSave(new NullProgressMonitor());
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
							artifact.removeFields(Collections.singleton(field));
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
						artifact.addField(diffField);
						artifact.doSave(new NullProgressMonitor());
						String msgText = "INFO : Replaced Field "
								+ diff.getObject() + " on " + diff.getLocal();
						out.println(msgText);
						artifact.doSave(new NullProgressMonitor());
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
							Collection<IStereotypeInstance> insts = field
									.getStereotypeInstances();
							
							for (IStereotypeInstance inst : insts) {
								
								if (inst.getName().equals(stName)) {
									// out.println("iname "+inst.getName());
									IStereotypeInstance instAPI = (IStereotypeInstance) inst;
									IStereotypeAttribute attribute = instAPI
											.getCharacterizingStereotype()
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
												.doSave(new NullProgressMonitor());
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

				// ================== literals ============================

				/**
				 * This case is for a missing literals in the project We will *NOT*
				 * remove any extra literals
				 * 
				 * 
				 */
				if (diff.getScope().equals("Artifact:Literal")) {
					if (diff.getLocalVal().equals("present")) {
						literal = getILiteral(extractedArtifact, diff.getObject());
						artifact.addLiteral(literal);
						artifact.doSave(new NullProgressMonitor());
						String msgText = "INFO : Added Literal "
								+ diff.getObject() + " on " + diff.getLocal();
						out.println(msgText);
					} else {
						String msgText = "New Literal " + diff.getObject()
								+ " in Artifact " + diff.getLocal()
								+ " will not be changed";
						out.println("INFO : " + msgText);
						addMessage(msgText, 3);
					}
				}

				/**
				 * This case is for a difference within an existing literals NB
				 * Will do multiple changes in one go...
				 * 
				 * This is dangerous as we could have added Stereotypes in the
				 * model. Or new Stereotype Array Values.....
				 * 
				 * Copy out "new Stereotypes" - Replace literals - Add back "new
				 * Stereotypes"
				 * 
				 */
				if (diff.getScope().startsWith("Artifact:Literal:")) {
					// Can get a "presence" diff for stereotypes.
					if (diff.getType().equals("value")
							|| ((diff.getType().equals("presence") && diff
									.getLocalVal().equals("present")) && !diff
									.getScope().endsWith(":ArrayValue"))) {
						String literalName = "";
						if (diff.getObject().indexOf(":") > 0) {
							literalName = diff.getObject().substring(0,
									diff.getObject().indexOf(":"));
						} else {
							literalName = diff.getObject();
						}

						literal = getILiteral(artifact, literalName);
						ILiteral diffLiteral = getILiteral(extractedArtifact,
								literalName);
						Collection<IStereotypeInstance> extraStereos = getExtraStereos(
								literal, diffLiteral);
						if (literal != null) {
							artifact.removeLiterals(Collections.singleton(literal));
						}

						artifact.addLiteral(diffLiteral);
						for (IStereotypeInstance inst : extraStereos) {
							diffLiteral
									.addStereotypeInstance((IStereotypeInstance) inst);
							/*
							 * String msgText = "INFO : New Stereotype " +
							 * inst.getName() + " on " + diff.getLocal() + "
							 * will not be changed"; out.println(msgText);
							 */
						}
						String msgText = "INFO : Replaced Literal "
								+ diff.getObject() + " on " + diff.getLocal();
						out.println(msgText);
						artifact.doSave(new NullProgressMonitor());
					} else if (diff.getScope().endsWith(":ArrayValue")) {
						String literalName;
						if (diff.getObject().indexOf(":") > 0) {
							literalName = diff.getObject().substring(0,
									diff.getObject().indexOf(":"));
						} else {
							literalName = diff.getObject();
						}
						literal = getILiteral(artifact, literalName);
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
							Collection<IStereotypeInstance> insts = literal
									.getStereotypeInstances();
							
							for (IStereotypeInstance inst :insts) {
								if (inst.getName().equals(stName)) {

									// out.println("iname "+inst.getName());
									IStereotypeInstance instAPI = (IStereotypeInstance) inst;
									IStereotypeAttribute attribute = instAPI
											.getCharacterizingStereotype()
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
												.doSave(new NullProgressMonitor());
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
						artifact.addMethod(method);
						artifact.doSave(new NullProgressMonitor());
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
							artifact.removeMethods(Collections
									.singleton(method));
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
							diffMethod.addException(exception);
							/*
							 * String msgText = "INFO : New Exception " +
							 * exception.getName() + " on " + diff.getLocal() + "
							 * will not be changed"; out.println(msgText);
							 */
						}
						for (IArgument argument : extraArguments) {
							diffMethod.addArgument(argument);
							/*
							 * String msgText = "INFO : New Argument " +
							 * argument.getName() + " on " + diff.getLocal() + "
							 * will not be changed"; out.println(msgText);
							 */
						}
						artifact.addMethod(diffMethod);
						artifact.doSave(new NullProgressMonitor());
						String msgText = "INFO : Replaced Method " + methodName
								+ " on " + diff.getLocal();
						out.println(msgText);
						artifact.doSave(new NullProgressMonitor());

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
							Collection<IStereotypeInstance> insts = method
									.getStereotypeInstances();
							
							// out.println("Len" +insts.length);
							for (IStereotypeInstance inst:  insts) {
								// out.println(insts[i].getName());
								if (inst.getName().equals(stName)) {
									inst = inst;
									// out.println("iname "+inst.getName());
									IStereotypeInstance instAPI = (IStereotypeInstance) inst;
									IStereotypeAttribute attribute = instAPI
											.getCharacterizingStereotype()
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
												.doSave(new NullProgressMonitor());
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
					artifact.doSave(new NullProgressMonitor());
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
					artifact.doSave(new NullProgressMonitor());
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
						artifact.doSave(new NullProgressMonitor());
						String msgText = "INFO : Updated PrimaryKeyType "
								+ diff.getObject() + " on " + diff.getLocal();
						out.println(msgText);
						artifact.doSave(new NullProgressMonitor());
					} else if (diff.getObject().equals("ExtensibilityType")) {
						entitySpecs.setExtensibilityType(extractedSpecs
								.getExtensibilityType());
						artifact.doSave(new NullProgressMonitor());
						String msgText = "INFO : Updated ExtensibilityType "
								+ diff.getObject() + " on " + diff.getLocal();
						out.println(msgText);
						artifact.doSave(new NullProgressMonitor());
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
						for (IMethod ossjMethod : artifact.getMethods()) {
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
					artifact.doSave(new NullProgressMonitor());
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
						artifact.doSave(new NullProgressMonitor());
						String msgText = "INFO : Updated BaseType on "
								+ diff.getLocal();
						out.println(msgText);
					} else if (diff.getObject().equals("Extensible")) {
						enumSpecs.setExtensible(extractedSpecs.getExtensible());
						artifact.doSave(new NullProgressMonitor());
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
						artifact.doSave(new NullProgressMonitor());
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
									.doSave(new NullProgressMonitor());
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
									.doSave(new NullProgressMonitor());
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
									.doSave(new NullProgressMonitor());
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
									.doSave(new NullProgressMonitor());
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
									.makeManagedEntityDetails();
							q.setFullyQualifiedName(diff.getObject());
							session.addManagedEntityDetails(q);
							session
									.doSave(new NullProgressMonitor());
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
							IEmittedEvent q = session.makeEmittedEvent();
							q.setFullyQualifiedName(diff.getObject());
							session.addEmittedEvent(q);
							session
									.doSave(new NullProgressMonitor());
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
									.makeExposedUpdateProcedure();
							q.setFullyQualifiedName(diff.getObject());
							session.addExposedUpdateProcedure(q);
							session
									.doSave(new NullProgressMonitor());
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
							INamedQuery q = session.makeNamedQuery();
							q.setFullyQualifiedName(diff.getObject());
							session.addNamedQuery(q);
							session
									.doSave(new NullProgressMonitor());
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
		if (component.getStereotypeInstances().size() > 0) {
			Collection<IStereotypeInstance> remainingModelStereos = new ArrayList(
					Arrays.asList(component.getStereotypeInstances()));
			Collection<IStereotypeInstance> modelStereos = component.getStereotypeInstances();
			for (IStereotypeInstance stereo : modelStereos) {
				// go on the name..
				for (IStereotypeInstance exStereo : extractedComponent.getStereotypeInstances()) {
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
		if (component.getExceptions().size() > 0) {
			Collection<IException> remainingExceptions = new ArrayList(Arrays
					.asList(component.getExceptions()));
			Collection<IException> modelExceptions = component
					.getExceptions();
			for (IException excep : modelExceptions) {
				// go on the name..
				for (IException exException : extractedComponent
						.getExceptions()) {
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
		Collection<IArgument> remainingArguments = new ArrayList<IArgument>();
		
		Iterator<IArgument> allArguments = component.getArguments().iterator();
		Iterator<IArgument> extractedArguments = extractedComponent.getArguments().iterator();
		
		int a = 0;
		while (allArguments.hasNext()){
			IArgument argument = allArguments.next();
			if (extractedArguments.hasNext()){
				extractedArguments.next();
			} else {
				remainingArguments.add(argument);
			}
			a++;
		}
		return remainingArguments;

	}

	public IField getIField(IAbstractArtifact artifact, String fieldName) {
		for (IField field : artifact.getFields()) {
			if (field.getName().equals(fieldName))
				return field;
		}
		return null;
	}

	public ILiteral getILiteral(IAbstractArtifact artifact, String literalName) {
		for (ILiteral literal : artifact.getLiterals()) {
			if (literal.getName().equals(literalName))
				return literal;
		}
		return null;
	}

	public IMethod getIMethod(IAbstractArtifact artifact, String methodName) {
		for (IMethod method : artifact.getMethods()) {
			if (method.getName().equals(methodName))
				return method;
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
