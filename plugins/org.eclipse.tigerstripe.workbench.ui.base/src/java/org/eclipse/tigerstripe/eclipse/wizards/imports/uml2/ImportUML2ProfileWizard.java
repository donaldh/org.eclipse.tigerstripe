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
package org.eclipse.tigerstripe.eclipse.wizards.imports.uml2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.api.API;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationClassArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IDatatypeArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IEnumArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IExceptionArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IManagedEntityArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.ISessionArtifact;
import org.eclipse.tigerstripe.api.external.profile.stereotype.IextStereotype;
import org.eclipse.tigerstripe.api.external.profile.stereotype.IextStereotypeAttribute;
import org.eclipse.tigerstripe.api.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.api.profile.primitiveType.IPrimitiveTypeDef;
import org.eclipse.tigerstripe.api.profile.stereotype.IEntryListStereotypeAttribute;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeScopeDetails;
import org.eclipse.tigerstripe.core.profile.primitiveType.PrimitiveTypeDef;
import org.eclipse.tigerstripe.core.profile.stereotype.StereotypeAttributeFactory;
import org.eclipse.tigerstripe.core.util.messages.Message;
import org.eclipse.tigerstripe.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.MessageListDialog;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
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

/**
 * This is a sample new wizard. Its role is to create a new file resource in the
 * provided container. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "mpe". If a
 * sample multi-page editor (also available as a template) is registered for the
 * same extension, it will be able to open it.
 */

public class ImportUML2ProfileWizard extends Wizard implements INewWizard {

	private int MESSAGE_LEVEL = 3;

	private ImportUML2ProfileWizardPage firstPage;

	private IStructuredSelection fSelection;
	private MessageList messages;

	private ImageDescriptor image;
	private PrintWriter out;
	private boolean replace = true;
	private List ignoreList = Arrays.asList("TypeDefinition", "Exception",
			"EnumValue");

	public IStructuredSelection getSelection() {
		return this.fSelection;
	}

	/**
	 * Constructor for NewProjectWizard.
	 */
	public ImportUML2ProfileWizard() {
		super();
		setNeedsProgressMonitor(true);

		setDefaultPageImageDescriptor(image);

		setWindowTitle("Import UML2 Profile...");
		messages = new MessageList();
	}

	/**
	 * Adding the page to the wizard.
	 */

	@Override
	public void addPages() {
		super.addPages();

		this.firstPage = new ImportUML2ProfileWizardPage();

		addPage(this.firstPage);
		this.firstPage.init(getSelection());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		fSelection = currentSelection;
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					extractProfiles(monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(false, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException
					.getMessage());
			return false;
		}

		// FIXME perform a refresh on created profile

		return true;
	}

	private boolean extractProfiles(IProgressMonitor monitor)
			throws CoreException {
		replace = this.firstPage.getReplace();
		// mapExistence = this.firstPage.getMapExistence();
		try {
			File pro = new File(this.firstPage.getProfileFilename());
			IWorkbenchProfile handle = API.getIWorkbenchProfileSession()
					.getWorkbenchProfileFor(pro.getAbsolutePath());
			Utilities.setupPaths();
			loadProfile(handle, new File(this.firstPage.getFilename()), monitor);
			forceCreateStereotypes(handle);

			FileWriter writer = new FileWriter(pro.getAbsolutePath());
			try {
				writer.write(handle.asText());
				writer.flush();
			} finally {
				if (writer != null) {
					writer.close();
				}
			}

		} catch (Exception e) {
			String msgText = "Parse error loading profiles";
			addMessage(msgText, 0);
			this.out.println("Error :" + msgText);

			this.out.close();
			e.printStackTrace(this.out);
		}

		this.out.close();
		monitor.done();
		if (!this.messages.isEmpty()) {
			MessageListDialog msgDialog = new MessageListDialog(
					this.getShell(), this.messages);
			msgDialog.open();
		}
		return true;
	}

	/**
	 * make a couple of things that TS can't currently handle
	 * 
	 * 
	 */
	private void forceCreateStereotypes(IWorkbenchProfile handle) {

		// RC - This is removed because the model now supports an abstract flag
		// on the
		// artifacts/methods.

		// I've left it here beacuse the logic is good and we might want to use
		// it as a "model"
		// should we want to put others back

		/*
		 * HashMap existingStereoNames = new HashMap(); IextStereotype[]
		 * existingStereos = handle.getStereotypes(); IStereotype newStereo;
		 * 
		 * for (int e=0;e<existingStereos.length;e++){ String exName =
		 * existingStereos[e].getName();
		 * existingStereoNames.put(exName,existingStereos[e]); } if(
		 * existingStereoNames.keySet().contains("Abstract")){ try {
		 * handle.removeStereotype((IStereotype)
		 * existingStereoNames.get("Abstract")); }catch (Exception e){ String
		 * msgText = "Failed to remove existing stereotype " + "Abstract";
		 * addMessage(msgText, 0); this.out.println("Error : "+msgText);
		 * e.printStackTrace(this.out); return; } } newStereo = new
		 * org.eclipse.tigerstripe.core.profile.stereotype.Stereotype(handle);
		 * newStereo.setName("Abstract"); newStereo.setDescription("Force
		 * created by Import tool");
		 * 
		 * IStereotypeScopeDetails thisScope = (IStereotypeScopeDetails)
		 * newStereo.getStereotypeScopeDetails(); // All artifacts ArrayList<String>
		 * typeList = new ArrayList<String>();
		 * typeList.add(IManagedEntityArtifact.class.getName());
		 * typeList.add(IDatatypeArtifact.class.getName());
		 * typeList.add(IExceptionArtifact.class.getName());
		 * typeList.add(IEnumArtifact.class.getName());
		 * typeList.add(IAssociationArtifact.class.getName());
		 * typeList.add(IAssociationClassArtifact.class.getName());
		 * typeList.add(ISessionArtifact.class.getName()); String[] strArr = new
		 * String[0]; String[] types = typeList.toArray(strArr);
		 * thisScope.setArtifactLevelTypes(types);
		 * 
		 * thisScope.setMethodLevel(true); StereotypeAttributeFactory fact = new
		 * StereotypeAttributeFactory(); IStereotypeAttribute attribute = null;
		 * int kind = IStereotypeAttribute.CHECKABLE_KIND;
		 * 
		 * attribute = fact.makeAttribute(kind);
		 * attribute.setName("isAbstract"); attribute.setDescription("Force
		 * created by Import tool"); attribute.setDefaultValue("1"); try{
		 * newStereo.addAttribute(attribute); handle.addStereotype(newStereo); }
		 * catch (Exception e){ String msgText = "Failed to add sterotype :
		 * "+attribute.getName() ; addMessage(msgText, 0);
		 * this.out.println("Error : "+msgText); e.printStackTrace(this.out); }
		 */

	}

	private void loadProfile(IWorkbenchProfile handle, File source,
			IProgressMonitor monitor) {
		File logFile = new File(source + "/TSLoadprofile.log");

		try {
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
			this.out.println("Error :" + msgText);

			this.out.close();
			return;
		}
		this.out.close();
		Utilities.tearDown();

	}

	private void loadUMLModel(Model model, IWorkbenchProfile handle,
			SubProgressMonitor subMonitor) {
		ArrayList existingDefs = new ArrayList();
		IPrimitiveTypeDef[] defs = handle.getPrimitiveTypeDefs(true);
		for (int d = 0; d < defs.length; d++) {
			existingDefs.add(defs[d].getName());
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
				prim.setName(primName);
				prim.setPackageName("primitive");
				prim.setDescription("Imported from " + model.getName());
				try {
					if (!existingDefs.contains(prim.getName())) {
						handle.addPrimitiveTypeDef(prim);
						this.out.println("Added primitive Type " + primName);
					}
				} catch (Exception e) {
					String msgText = "Failed to add primitive Type " + primName;
					addMessage(msgText, 0);
					this.out.println("Error : " + msgText);
					e.printStackTrace(this.out);
				}
				subMonitor.worked(monCount);
			}
			monCount++;

		}
	}

	private void loadUMLProfile(Profile profile, IWorkbenchProfile handle,
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
					IextStereotype[] existingStereos = handle.getStereotypes();
					for (int e = 0; e < existingStereos.length; e++) {
						String exName = existingStereos[e].getName();
						existingStereoNames.put(exName, existingStereos[e]);
					}
					if (existingStereoNames.keySet().contains(st.getName())) {

						if (replace) {
							try {
								handle
										.removeStereotype((IStereotype) existingStereoNames
												.get(st.getName()));
								newStereo = new org.eclipse.tigerstripe.core.profile.stereotype.Stereotype(
										handle);
							} catch (Exception e) {
								String msgText = "Failed to remove existing stereotype "
										+ st.getName();
								addMessage(msgText, 0);
								this.out.println("Error : " + msgText);
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
								this.out.println("Updating stereotype "
										+ st.getName());

							} catch (Exception e) {
								String msgText = "Failed to remove existing stereotype "
										+ st.getName();
								addMessage(msgText, 0);
								this.out.println("Error : " + msgText);
								e.printStackTrace(this.out);
								continue;
							}
						}
					} else {
						this.out.println("Creating stereotype " + st.getName());
						newStereo = new org.eclipse.tigerstripe.core.profile.stereotype.Stereotype(
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
						} else if (sc.equals("Enumeration")) {
							typeList.add(IEnumArtifact.class.getName());
						} else if (sc.equals("Association")) {
							typeList.add(IAssociationArtifact.class.getName());
						} else if (sc.equals("AssociationClass")) {
							typeList.add(IAssociationClassArtifact.class
									.getName());
						} else if (sc.equals("Interface")) {
							typeList.add(ISessionArtifact.class.getName());

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
							this.out.println("Warning : " + msgText);
						}
						String[] strArr = new String[0];
						String[] types = typeList.toArray(strArr);
						thisScope.setArtifactLevelTypes(types);
					}

					// StereoTypeAttributes
					StereotypeAttributeFactory fact = new StereotypeAttributeFactory();
					int kind = IextStereotypeAttribute.STRING_ENTRY_KIND;

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
								kind = IextStereotypeAttribute.STRING_ENTRY_KIND;
								attribute = StereotypeAttributeFactory
										.makeAttribute(kind);

								propTypeName = primProp.getName();

								attribute.setName(prop.getName());
								if (propTypeName == null) {
									this.out.println("No propTypeName for : "
											+ prop.getName());
								}

								if ("String".equals(propTypeName)) {
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
											+ propTypeName + " Set to String";
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
									// Lets say it's an array...
									String msgText = st.getName() + ":"
											+ attribute.getName()
											+ " has multiplicity :"
											+ prop.getLower() + ".."
											+ prop.getUpper();
									addMessage(msgText, 1);
									this.out.println("Warning : " + msgText);
								}

							} else if (propType instanceof Enumeration) {
								Enumeration enumProp = (Enumeration) propType;
								// These cannot be Arrays...
								// TigerstripeRuntime.logInfoMessage("An enum");
								kind = IextStereotypeAttribute.ENTRY_LIST_KIND;
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
									this.out.println("Error : " + msgText);
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
									this.out.println("Error : " + msgText);
									e.printStackTrace(this.out);

								}
							}
						}
						// This is CISCO specific.
						// } else
						// { if (mapExistence){
						// // TODO Is this a valid thing to do?
						// // Or can we handle this the same as Cisco - ie by
						// it's mere presence?
						// kind = IStereotypeAttribute.CHECKABLE_KIND;
						// attribute = fact.makeAttribute(kind);
						// attribute.setName("Existence");
						// attribute.setDescription("Imported from
						// "+profile.getName());
						// attribute.setDefaultValue("0");
						//
						// try{
						// IStereotypeAttribute[] existingAttributes =
						// newStereo.getAttributes();
						// HashMap eANames = new HashMap();
						// for (int eA=0;eA<existingAttributes.length;eA++){
						// eANames.put(existingAttributes[eA].getName(),existingAttributes[eA]);
						// }
						// if (eANames.keySet().contains(attribute.getName())){
						// // Not sure the logic is right here
						// // replace will have caused the Stereotype to be
						// replaced
						// // so won't have duplicate attribute (probabaly)
						// // if (replace){
						// newStereo.removeAttribute((IStereotypeAttribute)
						// eANames.get(attribute.getName()));
						// newStereo.addAttribute(attribute);
						// // } else {
						// // Leave it alone!
						// //}
						// }else {
						// newStereo.addAttribute(attribute);
						// }
						// } catch (Exception e){
						// String msgText = "Failed to add Attribute :
						// "+attribute.getName() ;
						// addMessage(msgText, 0);
						// this.out.println("Error : "+msgText);
						// e.printStackTrace(this.out);
						//
						// }
						// }
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

	private void addMessage(String msgText, int severity) {
		if (severity <= MESSAGE_LEVEL) {
			Message newMsg = new Message();
			newMsg.setMessage(msgText);
			newMsg.setSeverity(severity);
			this.messages.addMessage(newMsg);
		}

	}

}