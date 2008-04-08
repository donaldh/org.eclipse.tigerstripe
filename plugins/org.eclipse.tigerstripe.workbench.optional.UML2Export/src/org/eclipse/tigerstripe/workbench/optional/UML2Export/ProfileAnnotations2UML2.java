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
package org.eclipse.tigerstripe.workbench.optional.UML2Export;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IEntryListStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeScopeDetails;
import org.eclipse.uml2.common.util.UML2Util;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

public class ProfileAnnotations2UML2 {

	private IWorkbenchProfileSession profileSession;
	private PrintWriter out;
	private Profile profile;
	private Map<String, Stereotype> stereotypeMap;

	private Model umlMetamodel;

	/** constructor */
	public ProfileAnnotations2UML2() {
		this.profileSession = TigerstripeCore.getWorkbenchProfileSession();
		UMLUtilities.setupPaths();
		try {
			this.umlMetamodel = UMLUtilities.openModelURI(URI
					.createURI(UMLResource.UML_METAMODEL_URI));
		} catch (Exception e) {

		}

	}

	public Profile loadTSProfileAnnotationstoUML(File exportDir,
			String exportFilename, PrintWriter out, MessageList messages,
			IProgressMonitor monitor, Map<String, Type> primitiveTypeMap,
			Model typesModel) throws TigerstripeException {

		this.out = out;
		Map<String, Type> enumMap = new HashMap<String, Type>();
		this.stereotypeMap = new HashMap<String, Stereotype>();

		String profileName = this.profileSession.getActiveProfile().getName()
				+ ".Profile";
		this.profile = UMLFactory.eINSTANCE.createProfile();
		profile.setName(profileName);

		Collection<IStereotype> stereos = this.profileSession.getActiveProfile()
				.getStereotypes();

		// Possible metaClasses for scope

		org.eclipse.uml2.uml.Class operationMetaclass = (org.eclipse.uml2.uml.Class) umlMetamodel
				.getOwnedType(UMLPackage.Literals.OPERATION.getName());
		profile.createMetaclassReference(operationMetaclass);

		org.eclipse.uml2.uml.Class propertyMetaclass = (org.eclipse.uml2.uml.Class) umlMetamodel
				.getOwnedType(UMLPackage.Literals.PROPERTY.getName());
		profile.createMetaclassReference(propertyMetaclass);

		org.eclipse.uml2.uml.Class enumLiteralMetaclass = (org.eclipse.uml2.uml.Class) umlMetamodel
				.getOwnedType(UMLPackage.Literals.ENUMERATION_LITERAL.getName());
		profile.createMetaclassReference(enumLiteralMetaclass);

		org.eclipse.uml2.uml.Class argumentMetaclass = (org.eclipse.uml2.uml.Class) umlMetamodel
				.getOwnedType(UMLPackage.Literals.PARAMETER.getName());
		profile.createMetaclassReference(argumentMetaclass);

		// classes

		org.eclipse.uml2.uml.Class classMetaclass = (org.eclipse.uml2.uml.Class) umlMetamodel
				.getOwnedType(UMLPackage.Literals.CLASS.getName());
		profile.createMetaclassReference(classMetaclass);

		org.eclipse.uml2.uml.Class associationMetaclass = (org.eclipse.uml2.uml.Class) umlMetamodel
				.getOwnedType(UMLPackage.Literals.ASSOCIATION.getName());
		profile.createMetaclassReference(associationMetaclass);

		org.eclipse.uml2.uml.Class associationClassMetaclass = (org.eclipse.uml2.uml.Class) umlMetamodel
				.getOwnedType(UMLPackage.Literals.ASSOCIATION_CLASS.getName());
		profile.createMetaclassReference(associationClassMetaclass);

		org.eclipse.uml2.uml.Class interfaceMetaclass = (org.eclipse.uml2.uml.Class) umlMetamodel
				.getOwnedType(UMLPackage.Literals.INTERFACE.getName());
		profile.createMetaclassReference(interfaceMetaclass);

		org.eclipse.uml2.uml.Class enumerationMetaclass = (org.eclipse.uml2.uml.Class) umlMetamodel
				.getOwnedType(UMLPackage.Literals.ENUMERATION.getName());
		profile.createMetaclassReference(enumerationMetaclass);

		org.eclipse.uml2.uml.Class dependencyMetaclass = (org.eclipse.uml2.uml.Class) umlMetamodel
				.getOwnedType(UMLPackage.Literals.DEPENDENCY.getName());
		profile.createMetaclassReference(dependencyMetaclass);

		Map<String, org.eclipse.uml2.uml.Class> scopeLookup = new HashMap<String, org.eclipse.uml2.uml.Class>();

		scopeLookup.put(IManagedEntityArtifact.class.getName(), classMetaclass);
		scopeLookup.put(IDatatypeArtifact.class.getName(), classMetaclass);
		scopeLookup.put(IQueryArtifact.class.getName(), classMetaclass);
		scopeLookup.put(IUpdateProcedureArtifact.class.getName(),
				classMetaclass);
		scopeLookup.put(IEventArtifact.class.getName(), classMetaclass);
		scopeLookup.put(IExceptionArtifact.class.getName(), classMetaclass);

		scopeLookup.put(ISessionArtifact.class.getName(), interfaceMetaclass);
		scopeLookup.put(IEnumArtifact.class.getName(), enumerationMetaclass);

		scopeLookup.put(IAssociationArtifact.class.getName(),
				associationMetaclass);
		scopeLookup.put(IAssociationClassArtifact.class.getName(),
				associationClassMetaclass);

		scopeLookup.put(IDependencyArtifact.class.getName(),
				dependencyMetaclass);
		// Primitive

		// create the Enumerations for any Check List type of stereos
		for (IStereotype stereo : stereos) {
			
			for (IStereotypeAttribute attr : stereo.getAttributes()) {
				if (attr.getKind() == IStereotypeAttribute.ENTRY_LIST_KIND) {
					// Need an enum for these.
					Enumeration entries = profile.createOwnedEnumeration(stereo
							.getName()
							+ "_" + attr.getName() + "Types");
					for (String entry : ((IEntryListStereotypeAttribute) attr)
							.getSelectableValues()) {
						entries.createOwnedLiteral(entry);
					}
					enumMap.put(stereo.getName() + "_" + attr.getName()
							+ "Types", entries);
					out.println("Added " + stereo.getName() + "_"
							+ attr.getName() + "Types" + " enum");
				}
			}
		}

		for (IStereotype stereo : stereos) {
			Stereotype stereotype = profile.createOwnedStereotype(stereo
					.getName(), false);
			out.println("Added " + stereo.getName() + " Stereotype");
			for (IStereotypeAttribute attr : stereo.getAttributes()) {
				Type attrType = null;
				switch (attr.getKind()) {
				case IStereotypeAttribute.STRING_ENTRY_KIND:
					attrType = primitiveTypeMap.get("string");
					break;
				case IStereotypeAttribute.CHECKABLE_KIND:
					attrType = primitiveTypeMap.get("boolean");
					break;
				case IStereotypeAttribute.ENTRY_LIST_KIND:
					attrType = enumMap.get(stereo.getName() + "_"
							+ attr.getName() + "Types");
					break;
				}

				if (attrType != null) {
					if (attr.isArray()) {
						int lowerBound = 0;
						int upperBound = -1;
						stereotype.createOwnedAttribute(attr.getName(),
								attrType, lowerBound, upperBound);
						out.println("     Added Attribute " + attr.getName());
					} else {
						stereotype.createOwnedAttribute(attr.getName(),
								attrType);
						out.println("     Added Attribute " + attr.getName());
					}
				} else {
					out.println("ERROR : Failed on stereotype attribute");
				}
			}
			// Need to set scope...
			IStereotypeScopeDetails scopeDetails = stereo
					.getStereotypeScopeDetails();

			if (scopeDetails.isMethodLevel()) {
				stereotype.createExtension(operationMetaclass, false);
			}
			if (scopeDetails.isLiteralLevel()) {
				stereotype.createExtension(enumLiteralMetaclass, false);
			}
			if (scopeDetails.isAttributeLevel()) {
				stereotype.createExtension(propertyMetaclass, false);
			}

			if (scopeDetails.isArgumentLevel()) {
				stereotype.createExtension(argumentMetaclass, false);
			}

			String[] artifactScopes = scopeDetails.getArtifactLevelTypes();
			ArrayList<String> doneExtensions = new ArrayList<String>();
			for (int ii = 0; ii < artifactScopes.length; ii++) {
				if (scopeLookup.containsKey(artifactScopes[ii])) {
					Class metaClass = scopeLookup.get(artifactScopes[ii]);
					// Can't do it if it's already there...
					if (!doneExtensions.contains(metaClass.getName())) {
						stereotype.createExtension(metaClass, false);
						out.println("     Setting scope for "
								+ stereotype.getName() + " to "
								+ metaClass.getName());
						doneExtensions.add(metaClass.getName());
					} else {
						// out.println("already set "
						// +metaClass.getName()+"_"+stereotype.getName());
					}
				} else {
					out.println("artifactScope not set " + artifactScopes[ii]);
				}
			}

			// Not yet implemented
			scopeDetails.isArgumentLevel();

			stereotypeMap.put(stereo.getName(), stereotype);
		}

		// make some Tigerstripe specific sterotypes - per artifact

		// =======

		Stereotype meStereotype = profile.createOwnedStereotype(
				"tigerstripe_managedEntity", false);
		stereotypeMap.put(meStereotype.getName(), meStereotype);
		meStereotype.createExtension(classMetaclass, false);

		Stereotype dataStereotype = profile.createOwnedStereotype(
				"tigerstripe_datatype", false);
		stereotypeMap.put(dataStereotype.getName(), dataStereotype);
		dataStereotype.createExtension(classMetaclass, false);

		Stereotype notifStereotype = profile.createOwnedStereotype(
				"tigerstripe_notification", false);
		stereotypeMap.put(notifStereotype.getName(), notifStereotype);
		notifStereotype.createExtension(classMetaclass, false);

		Stereotype upStereotype = profile.createOwnedStereotype(
				"tigerstripe_updateProcedure", false);
		stereotypeMap.put(upStereotype.getName(), upStereotype);
		upStereotype.createExtension(classMetaclass, false);

		Stereotype exStereotype = profile.createOwnedStereotype(
				"tigerstripe_exception", false);
		stereotypeMap.put(exStereotype.getName(), exStereotype);
		exStereotype.createExtension(classMetaclass, false);

		Stereotype qStereotype = profile.createOwnedStereotype(
				"tigerstripe_query", false);
		qStereotype.createOwnedAttribute("returnedtype", primitiveTypeMap
				.get("string"));
		stereotypeMap.put(qStereotype.getName(), qStereotype);
		qStereotype.createExtension(classMetaclass, false);

		Stereotype aStereotype = profile.createOwnedStereotype(
				"tigerstripe_association", false);
		stereotypeMap.put(aStereotype.getName(), aStereotype);
		aStereotype.createExtension(associationMetaclass, false);

		Stereotype acStereotype = profile.createOwnedStereotype(
				"tigerstripe_associationClass", false);
		stereotypeMap.put(acStereotype.getName(), acStereotype);
		acStereotype.createExtension(associationClassMetaclass, false);

		Stereotype eStereotype = profile.createOwnedStereotype(
				"tigerstripe_enumeration", false);
		stereotypeMap.put(eStereotype.getName(), eStereotype);
		eStereotype.createExtension(enumerationMetaclass, false);

		Stereotype depStereotype = profile.createOwnedStereotype(
				"tigerstripe_dependency", false);
		stereotypeMap.put(depStereotype.getName(), depStereotype);
		depStereotype.createExtension(dependencyMetaclass, false);

		Stereotype sessStereotype = profile.createOwnedStereotype(
				"tigerstripe_session", false);
		int lowerBound = 0;
		int upperBound = -1;
		sessStereotype.createOwnedAttribute("emits", primitiveTypeMap
				.get("string"), lowerBound, upperBound);
		sessStereotype.createOwnedAttribute("manages", primitiveTypeMap
				.get("string"), lowerBound, upperBound);
		sessStereotype.createOwnedAttribute("supports", primitiveTypeMap
				.get("string"), lowerBound, upperBound);
		sessStereotype.createOwnedAttribute("exposes", primitiveTypeMap
				.get("string"), lowerBound, upperBound);
		stereotypeMap.put(sessStereotype.getName(), sessStereotype);
		sessStereotype.createExtension(interfaceMetaclass, false);

		URI fileUri = URI.createFileURI(exportDir.getAbsolutePath());

		String msgText = "Created " + stereotypeMap.size() + " stereotypes";
		Message message = new Message();
		message.setMessage(msgText);
		message.setSeverity(2);
		messages.addMessage(message);

		out.println(msgText);

		msgText = "Profile output to : " + fileUri.toFileString();
		message = new Message();
		message.setMessage(msgText);
		message.setSeverity(2);
		messages.addMessage(message);
		out.println(msgText);

		save(profile, fileUri.appendSegment("umlProfile").appendSegment(
				exportFilename + ".Profile").appendFileExtension(
				UMLResource.FILE_EXTENSION));

		profile.define();

		return profile;

	}

	public Profile getProfile() {
		return profile;
	}

	public static ResourceSet resourceSet = new ResourceSetImpl();

	/**
	 * Save the model to a Resource.
	 * 
	 * @param package_
	 * @param uri
	 */
	protected void save(org.eclipse.uml2.uml.Package packageToSave,
			org.eclipse.emf.common.util.URI fileUri) {

		Resource resource;// = RESOUR.createResource(uri);
		resource = resourceSet.createResource(fileUri);
		EList contents = resource.getContents();

		contents.add(packageToSave);

		for (Iterator allContents = UML2Util.getAllContents(packageToSave,
				true, false); allContents.hasNext();) {

			EObject eObject = (EObject) allContents.next();

			if (eObject instanceof Element) {
				contents
						.addAll(((Element) eObject).getStereotypeApplications());
			}
		}

		try {
			resource.save(null);
			out.println("Done.");
		} catch (IOException ioe) {
			out.println(ioe.getMessage());
		}

	}
}
