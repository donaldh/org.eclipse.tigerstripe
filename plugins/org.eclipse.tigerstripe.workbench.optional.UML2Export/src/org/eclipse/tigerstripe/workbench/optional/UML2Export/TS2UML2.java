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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IEmittedEvent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IExposedUpdateProcedure;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IManagedEntityDetails;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.INamedQuery;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.StructuredClassifier;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.VisibilityKind;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.util.UMLUtil;

public class TS2UML2 {

	boolean mapUnknownTypes = true;

	private int MESSAGE_LEVEL = 3;

	private IArtifactManagerSession mgrSession;
	private PrintWriter out;
	private MessageList messages;

	private ITigerstripeModelProject tsProject;

	private Model model;
	private Model typesModel;
	private Model unknownTypeModel;

	private Model umlMetamodel;
	private Map<String, Type> typeMap;
	private Map<String, Type> unknownTypeMap;
	private Profile tsProfile;

	/** constructor */
	public TS2UML2() {

	}

	public void loadTigerstripeToUML(File exportDir, String tSProjectName,
			String exportFilename, PrintWriter out, MessageList messages,
			IProgressMonitor monitor, Map<String, Type> primitiveTypeMap,
			Profile tsProfile, Model typesModel) throws TigerstripeException {

		this.out = out;
		this.messages = messages;
		this.tsProfile = tsProfile;
		this.typesModel = typesModel;

		unknownTypeModel = UMLFactory.eINSTANCE.createModel();
		unknownTypeModel.setName("UnknownTypes");

		unknownTypeMap = new HashMap<String, Type>();

		try {
			this.umlMetamodel = (Model) Utilities
					.openModelURI(org.eclipse.emf.common.util.URI
							.createURI(UMLResource.UML_METAMODEL_URI));
		} catch (Exception e) {
			String msgText = "Problem opening MetaModel";
			addMessage(msgText, 0);
			this.out.println("Error : " + msgText);
			e.printStackTrace(this.out);
		}

		try {
			IResource tsContainer = ResourcesPlugin.getWorkspace().getRoot()
					.findMember(new Path(tSProjectName));

			java.net.URI projectURI = tsContainer.getLocationURI();
			tsProject = (ITigerstripeModelProject) TigerstripeCore
					.findProject(projectURI);
			this.mgrSession = tsProject.getArtifactManagerSession();
			this.mgrSession.refresh(true, new NullProgressMonitor());

		} catch (Exception e) {
			String msgText = "Problem opening TS Project " + tSProjectName;
			addMessage(msgText, 0);
			this.out.println("Error : " + msgText);
			e.printStackTrace(this.out);
		}

		String modelName = tsProject.getProjectLabel();
		this.model = UMLFactory.eINSTANCE.createModel();
		model.setName(modelName);
		model.applyProfile(tsProfile);

		typeMap = new HashMap<String, Type>();

		// Iterate over the Project, creating classes per artifact.
		// This will create packages as necessary

		IArtifactQuery myQuery = mgrSession.makeQuery(IQueryAllArtifacts.class
				.getName());
		myQuery.setIncludeDependencies(false);
		Collection<IAbstractArtifact> projectArtifacts = mgrSession
				.queryArtifact(myQuery);

		monitor.beginTask("Creating UML Classes :", projectArtifacts.size());
		String msgText = "Found " + projectArtifacts.size()
				+ " Project Artifacts ";
		addMessage(msgText, 2);
		out.println(msgText);
		for (IAbstractArtifact artifact : projectArtifacts) {
			this.out.println("Processing " + artifact.getFullyQualifiedName());
			monitor.setTaskName("Creating UML Classes : " + artifact.getName());
			String packageName = artifact.getPackage();
			Package modelPackage = makeOrFindPackage(packageName);

			// NB Not all artifact types map to classes.
			// Associations are UML Associations
			// for example, and Dependencies don't map at all

			if (artifact instanceof IManagedEntityArtifact
					|| artifact instanceof IDatatypeArtifact
					|| artifact instanceof IQueryArtifact
					|| artifact instanceof IEventArtifact
					|| artifact instanceof IUpdateProcedureArtifact
					|| artifact instanceof IExceptionArtifact) {
				Class clazz = makeOrFindClass(artifact);
				this.out.println("Class : " + clazz.getQualifiedName());

			} else if (artifact instanceof ISessionArtifact) {
				Interface intf = makeOrFindInterface(artifact);
			}

			if (artifact instanceof IEnumArtifact) {
				Enumeration enumer = makeOrFindEnum(artifact);
				if (enumer != null) {
					this.out.println("Enum : " + enumer.getQualifiedName());
					Stereotype eS = enumer.getApplicableStereotype(tsProfile
							.getQualifiedName()
							+ "::tigerstripe_enumeration");
					enumer.applyStereotype(eS);
					addComponentStereotype(artifact, enumer);
				}
			}
			monitor.worked(1);
		}
		monitor.done();

		this.out.println();
		monitor.beginTask("Adding Relationships :", projectArtifacts.size());

		// Need another pass for assocs & dependencies
		for (IAbstractArtifact artifact : projectArtifacts) {
			if (artifact instanceof IAssociationClassArtifact) {
				this.out.println("Relationships to AssociationClass "
						+ artifact.getFullyQualifiedName());
				AssociationClass associationClass = makeOrFindAssociationClass(artifact);
				if (associationClass != null) {
					Stereotype aS = associationClass
							.getApplicableStereotype(tsProfile
									.getQualifiedName()
									+ "::tigerstripe_associationClass");
					associationClass.applyStereotype(aS);
					addComponentStereotype(artifact, associationClass);

					String className = artifact.getFullyQualifiedName();
					String umlClassName = mapName(className);
					addAttributes(artifact, ((StructuredClassifier) typeMap
							.get(umlClassName)));
					addOperations(artifact, ((Class) typeMap.get(umlClassName)));
				}
			} else if (artifact instanceof IAssociationArtifact) {
				this.out.println("Realtionship to Association  "
						+ artifact.getFullyQualifiedName());

				Association association = makeOrFindAssociation(artifact);
				if (association != null) {
					Stereotype aS = association
							.getApplicableStereotype(tsProfile
									.getQualifiedName()
									+ "::tigerstripe_association");
					association.applyStereotype(aS);
					addComponentStereotype(artifact, association);
				}
			} else if (artifact instanceof IDependencyArtifact) {
				Dependency dep = makeDependency(artifact);
				if (dep != null) {
					Stereotype depS = dep.getApplicableStereotype(tsProfile
							.getQualifiedName()
							+ "::tigerstripe_dependency");
					dep.applyStereotype(depS);
					addComponentStereotype(artifact, dep);
				}
			} else if (artifact instanceof ISessionArtifact) {
				this.out.println("Interface Settings  "
						+ artifact.getFullyQualifiedName());
				Interface intf = makeOrFindInterface(artifact);
				Stereotype iS = intf.getApplicableStereotype(tsProfile
						.getQualifiedName()
						+ "::tigerstripe_session");
				intf.applyStereotype(iS);
				ArrayList emitList = new ArrayList();
				for (IEmittedEvent emitted : ((ISessionArtifact) artifact)
						.getEmittedEvents()) {
					emitList.add(mapName(emitted.getFullyQualifiedName()));
				}
				intf.setValue(iS, "emits", emitList);

				ArrayList manageList = new ArrayList();
				for (IManagedEntityDetails details : ((ISessionArtifact) artifact)
						.getManagedEntityDetails()) {
					manageList.add(mapName(details.getFullyQualifiedName()));
				}
				intf.setValue(iS, "manages", manageList);

				ArrayList supportList = new ArrayList();
				for (INamedQuery query : ((ISessionArtifact) artifact)
						.getNamedQueries()) {
					supportList.add(mapName(query.getFullyQualifiedName()));
				}
				intf.setValue(iS, "supports", supportList);

				ArrayList exposesList = new ArrayList();
				for (IExposedUpdateProcedure proc : ((ISessionArtifact) artifact)
						.getExposedUpdateProcedures()) {
					exposesList.add(mapName(proc.getFullyQualifiedName()));
				}
				intf.setValue(iS, "exposes", exposesList);
				String className = artifact.getFullyQualifiedName();
				String umlClassName = mapName(className);
				// TODO

				addOperations(artifact, ((Interface) typeMap.get(umlClassName)));

				addComponentStereotype(artifact, intf);
			}
			monitor.worked(1);
		}
		monitor.done();

		this.out.println();
		monitor.beginTask("Adding Attributes etc :", projectArtifacts.size());
		// Re-pass to add Attributes etc Can't do this until we have created all
		// classes
		// in case of references
		for (IAbstractArtifact artifact : projectArtifacts) {

			out.println("Artifact Attributes etc "
					+ artifact.getFullyQualifiedName());
			String packageName = artifact.getPackage();
			Package modelPackage = makeOrFindPackage(packageName);

			if (artifact instanceof IManagedEntityArtifact
					|| artifact instanceof IDatatypeArtifact
					|| artifact instanceof IQueryArtifact
					|| artifact instanceof IEventArtifact
					|| artifact instanceof IUpdateProcedureArtifact
					|| artifact instanceof IExceptionArtifact) {
				Class clazz = makeOrFindClass(artifact);
				out.println("Class : " + clazz.getQualifiedName());
				String className = artifact.getFullyQualifiedName();
				String umlClassName = mapName(className);
				addAttributes(artifact, ((StructuredClassifier) typeMap
						.get(umlClassName)));
				addOperations(artifact, ((Class) typeMap.get(umlClassName)));
				if (artifact instanceof IManagedEntityArtifact) {
					EList as = clazz.getApplicableStereotypes();
					Stereotype meS = clazz.getApplicableStereotype(tsProfile
							.getQualifiedName()
							+ "::tigerstripe_managedEntity");
					clazz.applyStereotype(meS);
				} else if (artifact instanceof IDatatypeArtifact) {
					Stereotype dS = clazz.getApplicableStereotype(tsProfile
							.getQualifiedName()
							+ "::tigerstripe_datatype");
					clazz.applyStereotype(dS);
				} else if (artifact instanceof IQueryArtifact) {
					Stereotype qS = clazz.getApplicableStereotype(tsProfile
							.getQualifiedName()
							+ "::tigerstripe_query");
					clazz.applyStereotype(qS);
					IType rType = ((IQueryArtifact) artifact).getReturnedType();
					Type type = getUMLType(rType);
					if (type != null) {
						String retTypeString = type.getQualifiedName();
						clazz.setValue(qS, "returnedtype", retTypeString);
					} else {
						out.println(" No return type "
								+ ((IQueryArtifact) artifact).getReturnedType()
										.getFullyQualifiedName());
					}
				} else if (artifact instanceof IEventArtifact) {
					Stereotype nS = clazz.getApplicableStereotype(tsProfile
							.getQualifiedName()
							+ "::tigerstripe_notification");
					clazz.applyStereotype(nS);
				} else if (artifact instanceof IUpdateProcedureArtifact) {
					Stereotype dS = clazz.getApplicableStereotype(tsProfile
							.getQualifiedName()
							+ "::tigerstripe_updateProcedure");
					clazz.applyStereotype(dS);
				} else if (artifact instanceof IExceptionArtifact) {
					Stereotype exS = clazz.getApplicableStereotype(tsProfile
							.getQualifiedName()
							+ "::tigerstripe_exception");
					clazz.applyStereotype(exS);

				}
				addComponentStereotype(artifact, clazz);
			}

			monitor.worked(1);
		}

		monitor.done();

		this.out.println();
		monitor.beginTask("Doing Realizations :", projectArtifacts.size());

		// Need another pass for generalizations
		for (IAbstractArtifact artifact : projectArtifacts) {
			if (artifact instanceof IManagedEntityArtifact) {
				IManagedEntityArtifact entity = (IManagedEntityArtifact) artifact;
				// Do the implements
				Class clazz = makeOrFindClass(entity);
				for (IAbstractArtifact impl : entity.getImplementedArtifacts()) {
					Interface implClazz = makeOrFindInterface(impl);
					clazz.createInterfaceRealization("implements", implClazz);
					this.out.println("Created implementation "
							+ implClazz.getQualifiedName());
				}
			} else if (artifact instanceof IAssociationClassArtifact) {
				IAssociationClassArtifact assocClass = (IAssociationClassArtifact) artifact;
				// Do the implements
				Class clazz = makeOrFindClass(assocClass);
				for (IAbstractArtifact impl : assocClass
						.getImplementedArtifacts()) {
					Interface implClazz = makeOrFindInterface(impl);
					clazz.createInterfaceRealization("implements", implClazz);
					this.out.println("Created implementation "
							+ implClazz.getQualifiedName());
				}
			}
		}

		this.out.println();
		monitor.beginTask("Adding Generalizations :", projectArtifacts.size());

		// Need another pass for generalizations
		this.out.println("Adding Generalizations : " + projectArtifacts.size());
		for (IAbstractArtifact artifact : projectArtifacts) {
			if (artifact.hasExtends()) {
				this.out.println("Artifact Generalization "
						+ artifact.getFullyQualifiedName());
				// TODO What about Assocs / AssocClasses
				if (artifact instanceof IManagedEntityArtifact
						|| artifact instanceof IDatatypeArtifact
						|| artifact instanceof ISessionArtifact
						|| artifact instanceof IExceptionArtifact) {
					// this.out.println ("General case!");
					Class clazz = makeOrFindClass(artifact);
					Class extendClazz = makeOrFindClass(artifact
							.getExtendedArtifact());
					Generalization gen = clazz
							.createGeneralization(extendClazz);
				} else if (artifact instanceof IEnumArtifact) {
					// this.out.println ("Enum case!");
					Enumeration enumer = makeOrFindEnum(artifact);
					Enumeration extendEnumer = makeOrFindEnum(artifact
							.getExtendedArtifact());
					Generalization gen = enumer
							.createGeneralization(extendEnumer);
				} else if (artifact instanceof IAssociationClassArtifact) {
					// this.out.println ("Assoc Class case!");
					// Note that AssocClass can extend MEntity or AssocClass
					AssociationClass association = makeOrFindAssociationClass(artifact);
					if (artifact.getExtendedArtifact() instanceof IManagedEntityArtifact) {
						Class extendsClass = makeOrFindClass(artifact
								.getExtendedArtifact());
						Generalization gen = association
								.createGeneralization(extendsClass);
					} else {
						AssociationClass extendsAssociation = makeOrFindAssociationClass(artifact
								.getExtendedArtifact());
						Generalization gen = association
								.createGeneralization(extendsAssociation);
					}
				} else if (artifact instanceof IAssociationArtifact) {
					// this.out.println ("Assoc case!");
					Association association = makeOrFindAssociation(artifact);
					Association extendsAssociation = makeOrFindAssociation(artifact
							.getExtendedArtifact());
					Generalization gen = association
							.createGeneralization(extendsAssociation);
				} else if (artifact instanceof IDependencyArtifact) {
					// Can't generalize Dependencies...
				} else if (artifact instanceof ISessionArtifact) {
					this.out.println("Session case!");
					Interface intf = makeOrFindInterface(artifact);
					Interface extendIntf = makeOrFindInterface(artifact
							.getExtendedArtifact());
					Generalization gen = intf.createGeneralization(extendIntf);
				}

			}
			monitor.worked(1);
		}
		this.out.println("Finished Generalizations");
		monitor.done();

		if (unknownTypeMap.size() > 0) {
			URI fileUri = URI.createFileURI(exportDir.getAbsolutePath());
			msgText = "Unknown types output to : " + fileUri.toFileString();
			addMessage(msgText, 2);

			save(unknownTypeModel, fileUri.appendSegment("umlProfile")
					.appendSegment(exportFilename + ".UnknownTypes")
					.appendFileExtension(UMLResource.FILE_EXTENSION));
		}

		URI fileUri = URI.createFileURI(exportDir.getAbsolutePath());
		msgText = "Model output to : " + fileUri.toFileString();
		addMessage(msgText, 2);

		save(model, fileUri.appendSegment(exportFilename).appendFileExtension(
				UMLResource.FILE_EXTENSION));
		monitor.done();

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

		Resource resource = resourceSet.createResource(fileUri);
		EList contents = resource.getContents();
		contents.add(packageToSave);
		for (Iterator allContents = UMLUtil.getAllContents(packageToSave, true,
				false); allContents.hasNext();) {
			EObject eObject = (EObject) allContents.next();
			if (eObject instanceof Element) {
				contents
						.addAll(((Element) eObject).getStereotypeApplications());
			}
		}
		try {
			resource.save(null);
			out.println("UML2 export complete");
		} catch (IOException ioe) {
			TigerstripeRuntime.logInfoMessage("UML2 export failed to save.");
			out.println(ioe.getMessage());
		}
	}

	/**
	 * Add operations
	 */
	private void addOperations(IAbstractArtifact artifact, Interface clazz) {
		for (IMethod method : artifact.getMethods()) {
			Operation operation = clazz.createOwnedOperation(method.getName(),
					null, null);
			operation.setIsAbstract(method.isAbstract());
			operation.setIsOrdered(method.isOrdered());
			operation.setIsUnique(method.isUnique());

			// TODO method.getStereotypeInstances()
			// TODO method.getReturnRefBy()

			// TODO method.isInstanceMethod()
			// TODO method.isIteratorReturn()
			// TODO method.isOptional()
			// TODO method.isVoid()
			switch (method.getVisibility()) {
			case PACKAGE:
				operation.setVisibility(VisibilityKind.PACKAGE_LITERAL);
				break;
			case PRIVATE:
				operation.setVisibility(VisibilityKind.PRIVATE_LITERAL);
				break;
			case PROTECTED:
				operation.setVisibility(VisibilityKind.PROTECTED_LITERAL);
				break;
			case PUBLIC:
				operation.setVisibility(VisibilityKind.PUBLIC_LITERAL);
				break;
			}

			Comment comment = operation.createOwnedComment();
			comment.setBody(method.getComment());
			Type type = getUMLType(method.getReturnType());
			if (type != null) {
				Parameter result = operation.createReturnResult("return", type);
				result.setLower(1); // Returns are mandatory
				result.setUpper(getUpperBound(method.getReturnType()
						.getTypeMultiplicity()));
				result.setDefault(method.getDefaultReturnValue());
				addReturnTypeStereotype(method, result);
				result.setName(method.getReturnName());
				for (IArgument arg : method.getArguments()) {

					Type argType = getUMLType(arg.getType());
					if (argType != null) {
						Parameter param = operation.createOwnedParameter(arg
								.getName(), argType);
						Comment parameterComment = param.createOwnedComment();
						parameterComment.setBody(arg.getComment());
						// TODO arg.getRefBy()
						// Multiplicity
						param.setLower(getLowerBound(arg.getType()
								.getTypeMultiplicity()));
						param.setUpper(getUpperBound(arg.getType()
								.getTypeMultiplicity()));
						param.setIsOrdered(arg.isOrdered());
						param.setIsUnique(arg.isUnique());
						param.setDefault(arg.getDefaultValue());
					} else {
						// No type for this.
						String msgText = "No type info for :"
								+ artifact.getName() + ":" + method.getName()
								+ ": Argument "
								+ arg.getType().getFullyQualifiedName();
						this.out.println("ERROR : " + msgText);
						addMessage(msgText, 1);
						return;
					}
				}
				for (IException exception : method.getExceptions()) {

					// TODO

				}
				addComponentStereotype(method, operation);
			} else {
				// No type for this.
				String msgText = "No type info for :" + artifact.getName()
						+ ":" + method.getName() + ": Return type "
						+ method.getReturnType().getFullyQualifiedName();
				this.out.println("ERROR : " + msgText);
				addMessage(msgText, 1);
				return;
			}
			this.out.println("Added operation " + method.getName());
		}

	}

	/**
	 * Add operations
	 */
	private void addOperations(IAbstractArtifact artifact, Class clazz) {
		for (IMethod method : artifact.getMethods()) {
			Operation operation = clazz.createOwnedOperation(method.getName(),
					null, null);
			operation.setIsAbstract(method.isAbstract());
			operation.setIsOrdered(method.isOrdered());
			operation.setIsUnique(method.isUnique());

			// TODO method.getStereotypeInstances()
			// TODO method.getReturnRefBy()

			// TODO method.isInstanceMethod()
			// TODO method.isIteratorReturn()
			// TODO method.isOptional()
			// TODO method.isVoid()
			switch (method.getVisibility()) {
			case PACKAGE:
				operation.setVisibility(VisibilityKind.PACKAGE_LITERAL);
				break;
			case PRIVATE:
				operation.setVisibility(VisibilityKind.PRIVATE_LITERAL);
				break;
			case PROTECTED:
				operation.setVisibility(VisibilityKind.PROTECTED_LITERAL);
				break;
			case PUBLIC:
				operation.setVisibility(VisibilityKind.PUBLIC_LITERAL);
				break;
			}

			Comment comment = operation.createOwnedComment();
			comment.setBody(method.getComment());
			Type type = getUMLType(method.getReturnType());
			if (type != null) {
				Parameter result = operation.createReturnResult("return", type);
				result.setLower(1); // Returns are mandatory
				result.setUpper(getUpperBound(method.getReturnType()
						.getTypeMultiplicity()));
				result.setDefault(method.getDefaultReturnValue());
				addReturnTypeStereotype(method, result);
				result.setName(method.getReturnName());

				for (IArgument arg : method.getArguments()) {

					Type argType = getUMLType(arg.getType());
					if (argType != null) {
						Parameter param = operation.createOwnedParameter(arg
								.getName(), argType);
						Comment parameterComment = param.createOwnedComment();
						parameterComment.setBody(arg.getComment());
						// TODO arg.getRefBy()
						// Multiplicity
						param.setLower(getLowerBound(arg.getType()
								.getTypeMultiplicity()));
						param.setUpper(getUpperBound(arg.getType()
								.getTypeMultiplicity()));
						param.setIsOrdered(arg.isOrdered());
						param.setIsUnique(arg.isUnique());
						param.setDefault(arg.getDefaultValue());
					} else {
						// No type for this.
						String msgText = "No type info for :"
								+ artifact.getName() + ":" + method.getName()
								+ ": Argument "
								+ arg.getType().getFullyQualifiedName();
						this.out.println("ERROR : " + msgText);
						addMessage(msgText, 1);
						return;
					}
				}
				for (IException exception : method.getExceptions()) {

					// TODO

				}
				addComponentStereotype(method, operation);
			} else {
				// No type for this.
				String msgText = "No type info for :" + artifact.getName()
						+ ":" + method.getName() + ": Return type "
						+ method.getReturnType().getFullyQualifiedName();
				this.out.println("ERROR : " + msgText);
				addMessage(msgText, 1);
				return;
			}
			this.out.println("Added operation " + method.getName());
		}
	}

	/**
	 * Add attributes
	 */
	private void addAttributes(IAbstractArtifact artifact,
			StructuredClassifier classifier) {
		for (IField field : artifact.getFields()) {
			Property attribute;
			Type type = getUMLType(field.getType());
			if (type != null) {
				int lowerBound = getLowerBound(field.getType()
						.getTypeMultiplicity());
				int upperBound = getUpperBound(field.getType()
						.getTypeMultiplicity());
				this.out.println("Bounds " + lowerBound + " " + upperBound);
				attribute = classifier.createOwnedAttribute(field.getName(),
						type, lowerBound, upperBound);
				attribute.setIsOrdered(field.isOrdered());
				attribute.setIsUnique(field.isUnique());
				if (field.getDefaultValue() != null) {
					attribute.setDefault(field.getDefaultValue());
				}
				switch (field.getVisibility()) {
				case PACKAGE:
					attribute.setVisibility(VisibilityKind.PACKAGE_LITERAL);
					break;
				case PRIVATE:
					attribute.setVisibility(VisibilityKind.PRIVATE_LITERAL);
					break;
				case PROTECTED:
					attribute.setVisibility(VisibilityKind.PROTECTED_LITERAL);
					break;
				case PUBLIC:
					attribute.setVisibility(VisibilityKind.PUBLIC_LITERAL);
					break;
				}

				out.println("     Added attribute : " + field.getName());
			} else {
				// No type for this.
				String msgText = "No type info for :" + artifact.getName()
						+ ":" + field.getName() + ":"
						+ field.getType().getFullyQualifiedName();
				this.out.println("ERROR : " + msgText);
				addMessage(msgText, 1);
				continue;
			}

			// add any stereotypes
			addComponentStereotype(field, attribute);
		}
	}

	private void addComponentStereotype(IModelComponent component,
			Element attribute) {
		for (IStereotypeInstance inst : component.getStereotypeInstances()) {
			Stereotype stereotype = attribute.getApplicableStereotype(tsProfile
					.getQualifiedName()
					+ "::" + inst.getName());
			if (stereotype != null) {
				attribute.applyStereotype(stereotype);
				out.println("     Applied Stereotype " + inst.getName()
						+ " to component : " + component.getName());
				for (IStereotypeAttribute stAttr : inst
						.getCharacterizingStereotype().getIAttributes()) {
					try {
						if (stAttr.isArray()) {
							out.println(attribute.getValue(stereotype,
									stAttr.getName()).getClass().getName());
							attribute.setValue(stereotype, stAttr.getName(),
									Arrays.asList(inst
											.getAttributeValues(stAttr
													.getName())));
							out
									.println("     Added Stereotype Array attribute : "
											+ stAttr.getName());
						} else {
							attribute.setValue(stereotype, stAttr.getName(),
									inst.getAttributeValue(stAttr.getName()));
							out.println("     Added Stereotype attribute : "
									+ stAttr.getName());
						}
					} catch (TigerstripeException t) {
						// / oops
						out.println("Went wrong");
					}
				}
			} else {
				String msgText = "Did not find stereotype " + inst.getName()
						+ " for " + component.getName();
				out.println("ERROR : " + msgText);
				addMessage(msgText, 0);
			}
		}
	}

	private void addReturnTypeStereotype(IMethod method, Element result) {
		for (IStereotypeInstance inst : method.getReturnStereotypeInstances()) {
			Stereotype stereotype = result.getApplicableStereotype(tsProfile
					.getQualifiedName()
					+ "::" + inst.getName());
			if (stereotype != null) {
				result.applyStereotype(stereotype);
				out.println("     Applied Stereotype " + inst.getName()
						+ " to return of method : " + method.getName());
				for (IStereotypeAttribute stAttr : inst
						.getCharacterizingStereotype().getIAttributes()) {
					try {
						if (stAttr.isArray()) {
							// out.println(result.getValue(stereotype,
							// stAttr.getName()).getClass().getName());
							result.setValue(stereotype, stAttr.getName(),
									Arrays.asList(inst
											.getAttributeValues(stAttr
													.getName())));
							out
									.println("     Added Stereotype Array attribute : "
											+ stAttr.getName());
						} else {
							result.setValue(stereotype, stAttr.getName(), inst
									.getAttributeValue(stAttr.getName()));
							out.println("     Added Stereotype attribute : "
									+ stAttr.getName());
						}
					} catch (TigerstripeException t) {
						// / oops
						out.println("Went wrong");
					}
				}
			} else {
				String msgText = "Did not find stereotype " + inst.getName()
						+ " for return of : " + method.getName();
				out.println("ERROR : " + msgText);
				addMessage(msgText, 0);
			}
		}
	}

	private Type getUMLType(IType iType) {
		// The type here might be a another classifier, or a primitive type
		// OR Any built-in Type?

		String iTypeName = mapName(iType.getFullyQualifiedName());

		// Look in project classes
		if (typeMap.containsKey(iTypeName)) {
			this.out.println("Mapped "
					+ typeMap.get(iTypeName).getQualifiedName());
			return typeMap.get(iTypeName);
		}

		iTypeName = iType.getName().replace(".", "::");

		// Now look at primitive types..
		if (typesModel.getOwnedType(iTypeName) != null)
			return typesModel.getOwnedType(iTypeName);

		/*
		 * This doesn't make any sense during export as you can never recover on
		 * import // Look for java versions of the names.. BuiltInTypeMapper
		 * mapper = new BuiltInTypeMapper(); if
		 * (mapper.getMappedName(iTypeName)!= null){ if
		 * (typesModel.getOwnedType(mapper.getMappedName(iTypeName)) != null){
		 * return typesModel.getOwnedType(mapper.getMappedName(iTypeName)); } }
		 */

		// Make a "special" model for the unknown types.....
		// Note the need to make fully qualified Name
		if (mapUnknownTypes) {
			if (unknownTypeModel.getOwnedType(iTypeName) != null)
				return unknownTypeModel.getOwnedType(iTypeName);
			else {
				// OK - so we got here because the type isn't known , so we'd
				// better create one
				String p = iType.getFullyQualifiedName().substring(0,
						iType.getFullyQualifiedName().lastIndexOf("."));

				Class madeClass = null;
				Package modelPackage = makeOrFindPackage(p, unknownTypeModel);
				EList classList = modelPackage.getOwnedMembers();
				for (Object cl : classList) {
					if (cl instanceof Class) {
						Class aClass = (Class) cl;
						if (aClass.getQualifiedName().equals(iTypeName)) {
							// this.out.println("Found it!");
							madeClass = aClass;
						}
					}
				}
				if (madeClass == null) {
					// Didn't find it so make it!
					// this.out.println("Make it!");
					madeClass = modelPackage.createOwnedClass(iType.getName(),
							true);
				}
				if (madeClass != null) {
					this.out.println(iTypeName + " Added to unknown type Map");
					unknownTypeMap.put(madeClass.getQualifiedName(), madeClass);
					return madeClass;
				}
			}
		}

		return null;
	}

	private String mapName(String name, Model modelToUse) {
		return modelToUse.getName() + "::" + name.replace(".", "::");
	}

	private String mapName(String name) {
		return this.model.getName() + "::" + name.replace(".", "::");
	}

	/**
	 * Find a class if it exists, or make one if it doesn't.
	 */
	private Interface makeOrFindInterface(IAbstractArtifact artifact,
			Map<String, Type> mapOfTypes) {
		try {
			String packageName = artifact.getPackage();
			String className = artifact.getFullyQualifiedName();

			String umlClassName = mapName(className);
			Package modelPackage = makeOrFindPackage(packageName);
			EList classList = modelPackage.getOwnedMembers();
			for (Object cl : classList) {
				if (cl instanceof Interface) {
					Interface aClass = (Interface) cl;
					if (aClass.getQualifiedName().equals(umlClassName))
						return aClass;
				}
			}

			Interface interf = modelPackage.createOwnedInterface(artifact
					.getName());
			this.out.println("Made a new interface "
					+ interf.getQualifiedName());

			Comment comment = interf.createOwnedComment();
			comment.setBody(artifact.getComment());
			interf.setIsAbstract(artifact.isAbstract());
			mapOfTypes.put(interf.getQualifiedName(), interf);
			return interf;
		} catch (Exception e) {
			String msgText = artifact.getName() + e.getMessage();
			out.println("ERROR :" + msgText);
			addMessage(msgText, 0);
			e.printStackTrace(this.out);
			return null;
		}
	}

	private Interface makeOrFindInterface(IAbstractArtifact artifact) {
		return makeOrFindInterface(artifact, this.typeMap);
	}

	/**
	 * Find a class if it exists, or make one if it doesn't.
	 */
	private Class makeOrFindClass(IAbstractArtifact artifact) {
		try {
			String packageName = artifact.getPackage();
			String className = artifact.getFullyQualifiedName();
			String umlClassName = mapName(className);

			Package modelPackage = makeOrFindPackage(packageName);
			EList classList = modelPackage.getOwnedMembers();
			for (Object cl : classList) {
				if (cl instanceof Class) {
					Class aClass = (Class) cl;
					if (aClass.getQualifiedName().equals(umlClassName))
						return aClass;
				}
			}

			Class clazz = modelPackage.createOwnedClass(artifact.getName(),
					artifact.isAbstract());
			this.out.println("Made a new class " + clazz.getQualifiedName());

			Comment comment = clazz.createOwnedComment();
			comment.setBody(artifact.getComment());

			clazz.setIsAbstract(artifact.isAbstract());
			typeMap.put(clazz.getQualifiedName(), clazz);
			return clazz;
		} catch (Exception e) {
			String msgText = artifact.getName() + e.getMessage();
			out.println("ERROR :" + msgText);
			addMessage(msgText, 0);
			e.printStackTrace(this.out);
			return null;
		}
	}

	/**
	 * Find a class if it exists, or make one if it doesn't.
	 */
	private Enumeration makeOrFindEnum(IAbstractArtifact artifact) {
		try {
			String packageName = artifact.getPackage();
			String className = artifact.getFullyQualifiedName();
			String umlClassName = mapName(className);

			Package modelPackage = makeOrFindPackage(packageName);
			EList classList = modelPackage.getOwnedMembers();
			for (Object cl : classList) {
				if (cl instanceof Enumeration) {
					Enumeration aClass = (Enumeration) cl;
					if (aClass.getQualifiedName().equals(umlClassName))
						return aClass;
				}
			}

			Enumeration enumz = modelPackage.createOwnedEnumeration(artifact
					.getName());
			this.out.println("Made a new enumeration "
					+ enumz.getQualifiedName());
			typeMap.put(enumz.getQualifiedName(), enumz);

			// We can add EnumLiterals
			for (ILiteral literal : artifact.getLiterals()) {
				EnumerationLiteral lit = enumz.createOwnedLiteral(literal
						.getName());
				this.out.println("Made a new literal " + literal.getName());
				if (literal.getType().getName().equals("int")) {
					LiteralInteger literalInt = UMLFactory.eINSTANCE
							.createLiteralInteger();
					literalInt.setValue(Integer.parseInt(literal.getValue()));
					lit.setSpecification(literalInt);
				} else {
					LiteralString literalString = UMLFactory.eINSTANCE
							.createLiteralString();
					literalString.setValue(literal.getValue());
					lit.setSpecification(literalString);
				}
			}
			enumz.setIsAbstract(artifact.isAbstract());
			return enumz;
		} catch (Exception e) {
			String msgText = artifact.getName() + e.getMessage();
			out.println("ERROR :" + msgText);
			addMessage(msgText, 0);
			e.printStackTrace(this.out);
			return null;
		}
	}

	/**
	 * Find an association if it exists, or make one if it doesn't.
	 */
	private Dependency makeDependency(IAbstractArtifact artifact) {
		IDependencyArtifact dependencyArtifact = (IDependencyArtifact) artifact;
		Type aEndType = getUMLType(dependencyArtifact.getAEndType());
		Type zEndType = getUMLType(dependencyArtifact.getZEndType());
		if (aEndType != null && zEndType != null) {
			Type type1 = typeMap.get(aEndType.getQualifiedName());
			Type type2 = typeMap.get(zEndType.getQualifiedName());
			Dependency dep = type1.createDependency(type2);
			dep.setName(artifact.getName());

			this.out.println("Made a new Dependency " + dep.getQualifiedName());
			return dep;
		} else {
			String msgText = artifact.getName()
					+ " One or the other end was not a valid type : "
					+ dependencyArtifact.getAEndType().getFullyQualifiedName()
					+ " "
					+ dependencyArtifact.getZEndType().getFullyQualifiedName();
			out.println("ERROR :" + msgText);
			addMessage(msgText, 0);
			return null;
		}

	}

	private AssociationClass makeOrFindAssociationClass(
			IAbstractArtifact artifact) {
		try {
			String packageName = artifact.getPackage();
			String className = artifact.getFullyQualifiedName();
			String umlClassName = mapName(className);

			Package modelPackage = makeOrFindPackage(packageName);
			EList classList = modelPackage.getOwnedMembers();
			for (Object as : classList) {
				if (as instanceof AssociationClass) {
					AssociationClass aAssoc = (AssociationClass) as;
					if (aAssoc.getQualifiedName().equals(umlClassName))
						return aAssoc;
				}
			}
			// TODO make one...

			IAssociationEnd end1 = ((IAssociationArtifact) artifact).getAEnd();
			Type e1Type = getUMLType(end1.getType());
			Type type1 = null;
			if (e1Type != null)
				type1 = typeMap.get(e1Type.getQualifiedName());

			IAssociationEnd end2 = ((IAssociationArtifact) artifact).getZEnd();
			Type e2Type = getUMLType(end2.getType());
			Type type2 = null;
			if (e2Type != null)
				type2 = typeMap.get(e2Type.getQualifiedName());
			if (type1 != null && type2 != null) {

				boolean end1IsNavigable = end1.isNavigable();
				AggregationKind end1Aggregation = AggregationKind.get(end1
						.getAggregation().getLabel());

				// Swap the types over for some reason....

				String end1Name = end1.getName();
				String end2Name = end2.getName();

				int end1LowerBound = getLowerBound(end1.getMultiplicity());
				int end1UpperBound = getUpperBound(end1.getMultiplicity());

				boolean end2IsNavigable = end2.isNavigable();
				AggregationKind end2Aggregation = AggregationKind.get(end2
						.getAggregation().getLabel());

				int end2LowerBound = getLowerBound(end1.getMultiplicity());
				int end2UpperBound = getUpperBound(end2.getMultiplicity());

				/*
				 * Association assoc = type2.createAssociation(end1IsNavigable,
				 * end1Aggregation, end1Name, end1LowerBound, end1UpperBound,
				 * type1, end2IsNavigable, end2Aggregation, end2Name,
				 * end2LowerBound, end2UpperBound);
				 */

				AssociationClass aClass = UMLFactory.eINSTANCE
						.createAssociationClass();
				aClass.setPackage(modelPackage);
				aClass.setName(artifact.getName());

				Property aEnd;
				if (end2.isNavigable()) {
					aEnd = aClass.createNavigableOwnedEnd(end1Name, type2);
				} else {
					aEnd = aClass.createOwnedEnd(end1Name, type2);
				}
				aEnd.setAggregation(end1Aggregation);

				aEnd.setLower(end1LowerBound);
				aEnd.setUpper(end1UpperBound);

				switch (end1.getVisibility()) {
				case PACKAGE:
					aEnd.setVisibility(VisibilityKind.PACKAGE_LITERAL);
					break;
				case PRIVATE:
					aEnd.setVisibility(VisibilityKind.PRIVATE_LITERAL);
					break;
				case PROTECTED:
					aEnd.setVisibility(VisibilityKind.PROTECTED_LITERAL);
					break;
				case PUBLIC:
					aEnd.setVisibility(VisibilityKind.PUBLIC_LITERAL);
					break;
				}

				Property zEnd;
				if (end1.isNavigable()) {
					zEnd = aClass.createNavigableOwnedEnd(end2Name, type1);
				} else {
					zEnd = aClass.createOwnedEnd(end2Name, type1);
				}
				zEnd.setAggregation(end2Aggregation);

				aEnd.setLower(end2LowerBound);
				aEnd.setUpper(end2UpperBound);

				switch (end2.getVisibility()) {
				case PACKAGE:
					zEnd.setVisibility(VisibilityKind.PACKAGE_LITERAL);
					break;
				case PRIVATE:
					zEnd.setVisibility(VisibilityKind.PRIVATE_LITERAL);
					break;
				case PROTECTED:
					zEnd.setVisibility(VisibilityKind.PROTECTED_LITERAL);
					break;
				case PUBLIC:
					zEnd.setVisibility(VisibilityKind.PUBLIC_LITERAL);
					break;
				}

				aClass.setIsAbstract(artifact.isAbstract());
				typeMap.put(aClass.getQualifiedName(), aClass);
				out.println("Made a new association Class "
						+ aClass.getQualifiedName());
				return aClass;

			} else {
				String msgText = artifact.getName()
						+ " One or the other end was not a valid type : "
						+ end1.getType().getFullyQualifiedName() + " "
						+ end2.getType().getFullyQualifiedName();
				out.println("ERROR :" + msgText);
				addMessage(msgText, 0);
				return null;
			}

		} catch (Exception e) {
			String msgText = artifact.getName() + e.getMessage();
			out.println("ERROR :" + msgText);
			addMessage(msgText, 0);
			e.printStackTrace(this.out);
			return null;
		}
	}

	/**
	 * Find an association if it exists, or make one if it doesn't.
	 */
	private Association makeOrFindAssociation(IAbstractArtifact artifact) {
		// TigerstripeRuntime.logInfoMessage(" artiacf=" +
		// artifact.getFullyQualifiedName());
		try {
			String packageName = artifact.getPackage();
			String className = artifact.getFullyQualifiedName();
			String umlClassName = mapName(className);

			Package modelPackage = makeOrFindPackage(packageName);
			EList classList = modelPackage.getOwnedMembers();
			for (Object as : classList) {
				if (as instanceof Association) {
					Association aAssoc = (Association) as;
					if (aAssoc.getQualifiedName().equals(umlClassName))
						return aAssoc;
				}
			}
			// TODO make one...
			IAssociationEnd end1 = ((IAssociationArtifact) artifact).getAEnd();
			Type e1Type = getUMLType(end1.getType());
			Type type1 = null;
			if (e1Type != null)
				type1 = typeMap.get(e1Type.getQualifiedName());

			IAssociationEnd end2 = ((IAssociationArtifact) artifact).getZEnd();
			Type e2Type = getUMLType(end2.getType());
			Type type2 = null;
			if (e2Type != null)
				type2 = typeMap.get(e2Type.getQualifiedName());
			if (type1 != null && type2 != null) {

				boolean end1IsNavigable = end1.isNavigable();
				AggregationKind end1Aggregation = AggregationKind.get(end1
						.getAggregation().getLabel());

				// Swap the types over for some reason....

				String end1Name = end1.getName();
				String end2Name = end2.getName();

				int end1LowerBound = getLowerBound(end1.getMultiplicity());
				int end1UpperBound = getUpperBound(end1.getMultiplicity());

				boolean end2IsNavigable = end2.isNavigable();
				AggregationKind end2Aggregation = AggregationKind.get(end2
						.getAggregation().getLabel());

				int end2LowerBound = getLowerBound(end2.getMultiplicity());
				int end2UpperBound = getUpperBound(end2.getMultiplicity());

				Association newAssoc = UMLFactory.eINSTANCE.createAssociation();

				Property aEnd = newAssoc.createOwnedEnd(end1Name, type1);
				Property zEnd = newAssoc.createOwnedEnd(end2Name, type2);

				aEnd.setAggregation(end1Aggregation);
				aEnd.setIsNavigable(end1.isNavigable());
				aEnd.setIsOrdered(end1.isOrdered());
				aEnd.setIsUnique(end1.isUnique());

				// Multiplicity
				aEnd.setLower(end1LowerBound);
				aEnd.setUpper(end1UpperBound);

				zEnd.setAggregation(end2Aggregation);
				zEnd.setIsNavigable(end2.isNavigable());
				zEnd.setIsOrdered(end2.isOrdered());
				zEnd.setIsUnique(end2.isUnique());

				// Multiplicity
				zEnd.setLower(end2LowerBound);
				zEnd.setUpper(end2UpperBound);

				newAssoc.setName(artifact.getName());
				newAssoc.setIsAbstract(artifact.isAbstract());

				Association assoc = type2.createAssociation(end1IsNavigable,
						end1Aggregation, end1Name, end1LowerBound,
						end1UpperBound, type1, end2IsNavigable,
						end2Aggregation, end2Name, end2LowerBound,
						end2UpperBound);

				assoc.setName(artifact.getName());
				assoc.setIsAbstract(artifact.isAbstract());
				out.println("Made a new association "
						+ assoc.getQualifiedName());
				return assoc;
			} else {
				String msgText = artifact.getName()
						+ " One or the other end was not a valid type : "
						+ end1.getType().getFullyQualifiedName() + " "
						+ end2.getType().getFullyQualifiedName();
				out.println("ERROR :" + msgText);
				addMessage(msgText, 0);
				return null;
			}
		} catch (Exception e) {
			String msgText = artifact.getName() + e.getMessage();
			out.println("ERROR :" + msgText);
			addMessage(msgText, 0);
			e.printStackTrace(this.out);
			return null;
		}
	}

	// There doesn't always have to be an upper and lower bound......
	// eg "*", "0" or "1" - what does that come out as ?

	private int getLowerBound(IModelComponent.EMultiplicity multi) {
		if (multi.getLabel().startsWith("0"))
			return 0;
		else if (multi.getLabel().startsWith("1"))
			return 1;
		else
			// This will be a star!
			// Is this correct ?
			return 0;
	}

	private int getUpperBound(IModelComponent.EMultiplicity multi) {
		if (multi.getLabel().endsWith("0"))
			return 0;
		else if (multi.getLabel().endsWith("1"))
			return 1;
		else
			return -1;
	}

	/**
	 * Find a package if it exists, or make one if it doesn't.
	 * 
	 * Iterate up the tree making any that are missing
	 * 
	 * @param packageName
	 * @return
	 */
	private Package makeOrFindPackage(String packageName, Model modelToAddTo) {

		Package parent;
		// out.println ("MoF " + packageName);
		if (packageName.contains(".")) {
			parent = makeOrFindPackage(packageName.substring(0, packageName
					.lastIndexOf(".")), modelToAddTo);
		} else {
			parent = modelToAddTo;
		}
		String umlPackageName = mapName(packageName, modelToAddTo);
		EList packageList = parent.getNestedPackages();
		for (Object pack : packageList) {
			Package aPackage = (Package) pack;
			// out.println("Found "+aPackage.getQualifiedName()+" compare
			// "+umlPackageName);
			if (aPackage.getQualifiedName().equals(umlPackageName))
				return aPackage;
		}

		// Didn't find it...so make one.
		Package newPackage = parent.createNestedPackage(packageName
				.substring(packageName.lastIndexOf(".") + 1));
		out.println("Made a new package " + newPackage.getQualifiedName());
		return newPackage;
	}

	private Package makeOrFindPackage(String packageName) {
		return makeOrFindPackage(packageName, this.model);

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
