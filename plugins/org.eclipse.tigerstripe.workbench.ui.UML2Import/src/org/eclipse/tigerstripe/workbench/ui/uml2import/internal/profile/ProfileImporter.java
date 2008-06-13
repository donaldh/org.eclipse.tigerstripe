/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.profile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.metamodel.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.profile.WorkbenchProfile;
import org.eclipse.tigerstripe.workbench.internal.core.profile.primitiveType.PrimitiveTypeDef;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.StereotypeAttributeFactory;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.profile.primitiveType.IPrimitiveTypeDef;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IEntryListStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeScopeDetails;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.Utilities;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.resource.UMLResource;

public class ProfileImporter {
	
	private PrintWriter out;
	private MessageList messages;
	private List<String> ignoreList;
	private boolean replace;
	
	public ProfileImporter(MessageList messages){
		this.messages = messages;
		
	}
	public void loadProfile(WorkbenchProfile handle, File source, 
			List<String> ignoreList, boolean replace,
			IProgressMonitor monitor) {
		
		this.ignoreList = ignoreList;
		this.replace = replace;
		
		File logFile = new File(source + "/TSLoadprofile.log");

		try {
			loadUMLPrimitives(handle);
			Utilities.setupPaths();
			this.out = new PrintWriter(new FileOutputStream(logFile));
			File[] fList = source.listFiles();
			monitor.beginTask("Loading Files ", fList.length);
			for (int i = 0; i < fList.length; i++) {
				monitor.worked(i);
				File modelFile = fList[i];
				if (!modelFile.isFile())
					continue;
				if (!modelFile.getName().endsWith(UMLResource.FILE_EXTENSION)
						&& !modelFile.getName().endsWith("uml2"))
					continue;
				Model model;
				Profile profile;
				try {
					model = Utilities.openModelFile(modelFile);
					if (model != null) {
						this.out.println("Loading Model " + model.getName());

						loadUMLModel(model, handle, new SubProgressMonitor(
								monitor, 1));
					}
					profile = Utilities.openProfileFile(modelFile);
					if (profile != null) {
						this.out
								.println("Loading Profile " + profile.getName());
						loadUMLProfile(profile, handle, model,
								new SubProgressMonitor(monitor, 1));
					}
				} catch (InvocationTargetException e) {
					e.printStackTrace(this.out);
					this.out.close();
					return;
				}

			}
		} catch (Exception e) {
			String msgText = " Failed to load Profile or model ";
			e.printStackTrace(this.out);
			addMessage(msgText, 0);
			this.out.println("ERROR :" + msgText);

			this.out.close();
			return;
		}
		this.out.close();
		Utilities.tearDown();

	}

	private void loadUMLPrimitives(WorkbenchProfile handle){
		// We need to add String as its a UMLPrimitive that might get used
		// We have to lower case the first letter for TS reasons.
		// Other built-ins - Boolean and Integer are there already 
		
		ArrayList existingDefs = new ArrayList();
		Collection<IPrimitiveTypeDef> defs = handle.getPrimitiveTypeDefs(true);
		for (IPrimitiveTypeDef prim :defs) {
			existingDefs.add(prim.getName());
		}
		
		IPrimitiveTypeDef prim = new PrimitiveTypeDef();
		prim.setName("string");
		prim.setPackageName("primitive");
		prim.setDescription("Autocreated by UML profile import");
		try {
			if (!existingDefs.contains(prim.getName())) {
				handle.addPrimitiveTypeDef(prim);
				existingDefs.add(prim.getName());
				this.out.println("INFO : Added primitive Type " + "string");
			}
		} catch (Exception e) {
			String msgText = "Failed to add primitive Type " + "string";
			addMessage(msgText, 0);
			this.out.println("ERROR : " + msgText);
			e.printStackTrace(this.out);
		}
	}
	
	private void loadUMLModel(Model model, WorkbenchProfile handle,
			SubProgressMonitor subMonitor) {
		ArrayList existingDefs = new ArrayList();
		Collection<IPrimitiveTypeDef> defs = handle.getPrimitiveTypeDefs(true);
		for (IPrimitiveTypeDef prim :defs) {
			existingDefs.add(prim.getName());
		}
		TreeIterator t = model.eAllContents();
		subMonitor.beginTask("Processing UML Classes ", model
				.allOwnedElements().size());
		int monCount = 0;
		while (t.hasNext()) {

			String primName = "";
			EObject o = (EObject) t.next();
			if (o instanceof PrimitiveType) {
				PrimitiveType pType = (PrimitiveType) o;

				IPrimitiveTypeDef prim = new PrimitiveTypeDef();
				primName = pType.getName();
				subMonitor.setTaskName("Processing UML Classes : " + primName);
				primName = unCapitalize(primName);
				prim.setName(primName);
				prim.setPackageName("primitive");
				prim.setDescription("Imported from " + model.getName());
				try {
					if (!existingDefs.contains(prim.getName())) {
						handle.addPrimitiveTypeDef(prim);
						existingDefs.add(prim.getName());
						this.out.println("INFO : Added primitive Type " + primName);
					}
				} catch (Exception e) {
					String msgText = "Failed to add primitive Type " + primName;
					addMessage(msgText, 0);
					this.out.println("ERROR : " + msgText);
					e.printStackTrace(this.out);
				}
				subMonitor.worked(monCount);
			}
			monCount++;

		}
	}

	private void loadUMLProfile(Profile profile, WorkbenchProfile handle,
			Model model, SubProgressMonitor subMonitor) {
		TreeIterator t = profile.eAllContents();
		subMonitor.beginTask("Processing UML Classes ", profile
				.allOwnedElements().size());
		int monCount = 0;
		while (t.hasNext()) {
			EObject o = (EObject) t.next();
			if (o instanceof Element) {
				Element el = (Element) o;
				if (el instanceof Stereotype) {

					Stereotype st = (Stereotype) el;

					if (ignoreList.contains(st.getName())
							|| st.getName().startsWith("tigerstripe_")) {
						continue;
					}
					ArrayList scopes = new ArrayList();
					ArrayList attributes = new ArrayList();

					List attrs = st.getOwnedAttributes();
					ListIterator it = attrs.listIterator();
					while (it.hasNext()) {
						Property att = (Property) it.next();

						String name = att.getName();
						if (name.startsWith("base_")) {
							// defines scope for us
							scopes.add(name.substring(5));
						} else {
							attributes.add(att);
						}
					}

					IStereotype newStereo;
					HashMap existingStereoNames = new HashMap();
					subMonitor.setTaskName("Processing UML Classes : "
							+ st.getName());
					// Before going further see if these's already one of
					// these...
					Collection<IStereotype> existingStereos = handle.getStereotypes();
					for (IStereotype existing : existingStereos) {
						String exName = existing.getName();
						existingStereoNames.put(exName, existing);
					}
					if (existingStereoNames.keySet().contains(st.getName())) {

						if (replace) {
							try {
								handle
										.removeStereotype((IStereotype) existingStereoNames
												.get(st.getName()));
								newStereo = new org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.Stereotype(
										handle);
							} catch (Exception e) {
								String msgText = "Failed to remove existing stereotype "
										+ st.getName();
								addMessage(msgText, 0);
								this.out.println("ERROR : " + msgText);
								e.printStackTrace(this.out);
								continue;
							}
						} else {
							// Update it
							try {
								newStereo = (IStereotype) existingStereoNames
										.get(st.getName());
								handle
										.removeStereotype((IStereotype) existingStereoNames
												.get(st.getName()));
								this.out.println("INFO : Updating stereotype "
										+ st.getName());

							} catch (Exception e) {
								String msgText = "Failed to remove existing stereotype "
										+ st.getName();
								addMessage(msgText, 0);
								this.out.println("ERROR : " + msgText);
								e.printStackTrace(this.out);
								continue;
							}
						}
					} else {
						this.out.println("INFO : Creating stereotype " + st.getName());
						newStereo = new org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.Stereotype(
								handle);

					}

					// Do the name
					newStereo.setName(st.getName());
					newStereo.setDescription("Imported from "
							+ profile.getName());
					// TigerstripeRuntime.logInfoMessage("Stereotype = "+
					// st.getName());

					// Do the Scope(s)

					ListIterator scopeIt = scopes.listIterator();
					IStereotypeScopeDetails thisScope = (IStereotypeScopeDetails) newStereo
							.getStereotypeScopeDetails();
					ArrayList<String> typeList = new ArrayList<String>();
					while (scopeIt.hasNext()) {
						String sc = (String) scopeIt.next();

						if (sc.equals("Class")) {
							typeList
									.add(IManagedEntityArtifact.class.getName());
							typeList.add(IDatatypeArtifact.class.getName());
							typeList.add(IExceptionArtifact.class.getName());
						} else if (sc.equals("DataType")){
							typeList.add(IDatatypeArtifact.class.getName());
						} else if (sc.equals("Enumeration")) {
							typeList.add(IEnumArtifact.class.getName());
						} else if (sc.equals("Association")) {
							typeList.add(IAssociationArtifact.class.getName());
						} else if (sc.equals("AssociationClass")) {
							typeList.add(IAssociationClassArtifact.class
									.getName());
						} else if (sc.equals("Interface")) {
							typeList.add(ISessionArtifact.class.getName());
						} else if (sc.equals("Dependency")) {
							typeList.add(IDependencyArtifact.class.getName());
					
						} else if (sc.equals("EnumerationLiteral")) {
							thisScope.setLiteralLevel(true);
						} else if (sc.equals("Property")) {
							thisScope.setAttributeLevel(true);
						} else if (sc.equals("Operation")) {
							thisScope.setMethodLevel(true);
						} else if (sc.equals("Parameter")) {
							thisScope.setArgumentLevel(true);
						} else {
							String msgText = "Unhandled Scope type : " + sc
									+ " for : " + st.getName();
							addMessage(msgText, 1);
							this.out.println("WARN : " + msgText);
						}
						String[] strArr = new String[0];
						String[] types = typeList.toArray(strArr);
						thisScope.setArtifactLevelTypes(types);
					}

					// StereoTypeAttributes
					StereotypeAttributeFactory fact = new StereotypeAttributeFactory();
					int kind = IStereotypeAttribute.STRING_ENTRY_KIND;

					IStereotypeAttribute attribute = null;

					if (attributes.size() > 0) {
						ListIterator atIt = attributes.listIterator();
						// TigerstripeRuntime.logInfoMessage(" Attrs ");
						while (atIt.hasNext()) {
							Property prop = (Property) atIt.next();
							Type propType = prop.getType();
							String propTypeName = propType.getQualifiedName();

							if (propType instanceof PrimitiveType) {
								PrimitiveType primProp = (PrimitiveType) propType;

								// TigerstripeRuntime.logInfoMessage("A
								// primitive Type");
								kind = IStereotypeAttribute.STRING_ENTRY_KIND;
								attribute = StereotypeAttributeFactory
										.makeAttribute(kind);

								propTypeName = primProp.getName();

								attribute.setName(prop.getName());
								if (propTypeName == null) {
									this.out.println("WARN : No propTypeName for : "
											+ prop.getName());
								}

								if ("String".equals(propTypeName) || "string".equals(propTypeName)) {
									attribute.setDescription("Imported from "
											+ profile.getName());
								} else {
									attribute.setDescription("Imported from "
											+ profile.getName()
											+ "\nThis should be of type "
											+ propTypeName);
									String msgText = st.getName() + ":"
											+ attribute.getName()
											+ " should be of type "
											+ propTypeName + " - it has been set to String";
									addMessage(msgText, 1);
									this.out.println("Warning : " + msgText);
								}

								ValueSpecification upperVal = prop
										.getUpperValue();
								ValueSpecification lowerVal = prop
										.getLowerValue();

								// A bit of a short-cut!
								// TODO - check it's valid....and get bounds..
								// Every example is 1..* so far.
								if ((upperVal != null) && (lowerVal != null)) {
									if (upperVal.equals("-1")){
									// Lets say it's an array...
										attribute.setArray(true);
									} else {
									String msgText = st.getName() + ":"
											+ attribute.getName()
											+ " has unhandled multiplicity :"
											+ prop.getLower() + ".."
											+ prop.getUpper();
									addMessage(msgText, 1);
									this.out.println("WARN : " + msgText);
									}
								}

							} else if (propType instanceof Enumeration) {
								Enumeration enumProp = (Enumeration) propType;
								// These cannot be Arrays...
								// TigerstripeRuntime.logInfoMessage("An enum");
								kind = IStereotypeAttribute.ENTRY_LIST_KIND;
								attribute = StereotypeAttributeFactory
										.makeAttribute(kind);
								attribute.setName(prop.getName());
								attribute.setDescription("Imported from "
										+ profile.getName());
								// get the literals as the valid entries...
								IEntryListStereotypeAttribute listAttr = (IEntryListStereotypeAttribute) attribute;
								List enumValues = enumProp.getOwnedLiterals();

								String[] vals = new String[enumValues.size()];

								for (int r = 0; r < enumValues.size(); r++) {
									EnumerationLiteral eLit = (EnumerationLiteral) enumValues
											.get(r);
									// TigerstripeRuntime.logInfoMessage(eLit.getName());
									vals[r] = eLit.getName();
								}
								try {
									listAttr.setSelectableValues(vals);
								} catch (Exception e) {
									String msgText = "Failed to add List entries to : "
											+ attribute.getName();
									this.out.println("ERROR : " + msgText);
									addMessage(msgText, 0);
									e.printStackTrace(this.out);
								}
							}

							if (attribute != null) {
								try {
									IStereotypeAttribute[] existingAttributes = newStereo
											.getAttributes();
									HashMap eANames = new HashMap();
									for (int eA = 0; eA < existingAttributes.length; eA++) {
										eANames.put(existingAttributes[eA]
												.getName(),
												existingAttributes[eA]);
									}
									if (eANames.keySet().contains(
											attribute.getName())) {
										// Not sure the logic is right here
										// replace will have caused the
										// Stereotype to be replaced
										// so won't have duplicate attribute
										// (probabaly)
										// if (replace){
										newStereo
												.removeAttribute((IStereotypeAttribute) eANames
														.get(attribute
																.getName()));
										newStereo.addAttribute(attribute);
										// } else {
										// Leave it alone!
										// }
									} else {
										newStereo.addAttribute(attribute);
									}
								} catch (Exception e) {
									String msgText = "Failed to add Attribute : "
											+ attribute.getName();
									addMessage(msgText, 0);
									this.out.println("ERROR : " + msgText);
									e.printStackTrace(this.out);

								}
							}
						}
					}

					try {
						handle.addStereotype(newStereo);
					} catch (Exception e) {
						String msgText = "Failed to add sterotype "
								+ newStereo.getName();
						addMessage(msgText, 0);
						this.out.println("Error : " + msgText);
						e.printStackTrace(this.out);
					}
				}

			}
			monCount++;
		}

	}
	
	private String unCapitalize(String in){
		String out = in.substring(0,1).toLowerCase();
		out = out+in.substring(1);
		if (! out.equals(in)){
			this.out.println("Warning :  primitiveType '"+in+"' mapped to '"+out+"'");
		}
		return out;
	}
	
	public void addMessage(String msgText, int severity) {
			Message newMsg = new Message();
			newMsg.setMessage(msgText);
			newMsg.setSeverity(severity);
			this.messages.addMessage(newMsg);

	}
	
}
