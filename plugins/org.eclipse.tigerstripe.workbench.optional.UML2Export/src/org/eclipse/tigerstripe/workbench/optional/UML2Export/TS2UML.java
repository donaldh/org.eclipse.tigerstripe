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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
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
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
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

public class TS2UML {

	boolean mapUnknownTypes = true;

	private static int MESSAGE_LEVEL = 3;

	private IArtifactManagerSession mgrSession;
	private static PrintWriter out;
	private static MessageList messages;
	private static ResourceSet resourceSet = new ResourceSetImpl();
	
	private IProgressMonitor monitor;

	private ITigerstripeModelProject tsProject;

	private Model projectModel;
	private Model typesModel;
	private Model unknownTypeModel;

	private Model umlMetamodel;
	private Map<String, Type> typeMap;
	private Map<String, Type> unknownTypeMap;
	private Profile tsProfile;
	private Maker maker;
	private String msgText;

	/** constructor */
	public TS2UML(PrintWriter out, MessageList messages,
			IProgressMonitor monitor) {
		this.out = out;
		this.messages = messages;
		this.monitor = monitor;
	}

	public void loadTigerstripeToUML(File exportDir, String tSProjectName,
			String exportFilename, Map<String, Type> primitiveTypeMap,
			Profile tsProfile, Model typesModel) throws TigerstripeException {


		this.tsProfile = tsProfile;
		this.typesModel = typesModel;
		typeMap = new HashMap<String, Type>();
		unknownTypeModel = UMLFactory.eINSTANCE.createModel();
		unknownTypeModel.setName("UnknownTypes");
		unknownTypeMap = new HashMap<String, Type>();
		
		
		try {
			this.umlMetamodel = (Model) UMLUtilities
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
		
		
		
		Collection<Model> models = new ArrayList<Model>();
		
		for (IDependency dependency : tsProject.getDependencies()){
			out.println("Creating Dependency model: "+dependency.getIProjectDetails().getName());
			Model depModel = UMLFactory.eINSTANCE.createModel();
			depModel.setName(dependency.getIProjectDetails().getName());
			depModel.applyProfile(tsProfile);
			models.add(depModel);
		}
		
		for (ITigerstripeModelProject refProject : tsProject.getReferencedProjects()){
			out.println("Creating Referenced Project model: "+refProject.getProjectDetails().getName());
			Model refModel = UMLFactory.eINSTANCE.createModel();
			refModel.setName(refProject.getProjectDetails().getName());
			refModel.applyProfile(tsProfile);
			models.add(refModel);
		}
		
		
		projectModel = UMLFactory.eINSTANCE.createModel();
		projectModel.setName(modelName);
		projectModel.applyProfile(tsProfile);
		models.add(projectModel);
		
		
		maker = new Maker(typeMap,this.out, this.messages, typesModel, unknownTypeModel, unknownTypeMap, models);
		
		projectToUML();
		saveUnknowns(exportDir, tSProjectName);
		saveModels( models, exportDir);

		

		
	}
	
	private void projectToUML() throws TigerstripeException{

		// Iterate over the Project, creating classes per artifact.
		// This will create packages as necessary
		IArtifactQuery myQuery = mgrSession.makeQuery(IQueryAllArtifacts.class
				.getName());
		myQuery.setIncludeDependencies(true);
		Collection<IAbstractArtifact> projectArtifacts = mgrSession
		.queryArtifact(myQuery);
		

		monitor.beginTask("Creating UML Classes :", projectArtifacts.size());
		String msgText = "Found " + projectArtifacts.size()
				+ " Project Artifacts ";
		addMessage(msgText, 2);
		out.println(msgText);
		for (IAbstractArtifact artifact : projectArtifacts) {
			this.out.println("================================================\nProcessing " + artifact.getFullyQualifiedName());
			monitor.setTaskName("Creating UML Classes : " + artifact.getName());
			
			Package modelPackage = maker.makeOrFindPackage(artifact);

			// NB Not all artifact types map to classes.
			// Associations are UML Associations
			// for example, and Dependencies don't map at all

			if (artifact instanceof IManagedEntityArtifact
					|| artifact instanceof IDatatypeArtifact
					|| artifact instanceof IQueryArtifact
					|| artifact instanceof IEventArtifact
					|| artifact instanceof IUpdateProcedureArtifact
					|| artifact instanceof IExceptionArtifact) {
				Class clazz = maker.makeOrFindClass(artifact);
				this.out.println("    Class : " + clazz.getQualifiedName());

			} else if (artifact instanceof ISessionArtifact) {
				Interface intf = maker.makeOrFindInterface(artifact);
			}

			if (artifact instanceof IEnumArtifact) {
				Enumeration enumer = maker.makeOrFindEnum(artifact);
				if (enumer != null) {
					this.out.println("    Enum : " + enumer.getQualifiedName());
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
				AssociationClass associationClass = maker.makeOrFindAssociationClass(artifact);
				if (associationClass != null) {
					Stereotype aS = associationClass
							.getApplicableStereotype(tsProfile
									.getQualifiedName()
									+ "::tigerstripe_associationClass");
					associationClass.applyStereotype(aS);
					addComponentStereotype(artifact, associationClass);

					String className = artifact.getFullyQualifiedName();
					String umlClassName = Utilities.mapName(className, artifact.getProjectDescriptor().getIProjectDetails().getName());
					addAttributes(artifact, ((StructuredClassifier) typeMap
							.get(umlClassName)));
					addOperations(artifact, ((Class) typeMap.get(umlClassName)));
				}
			} else if (artifact instanceof IAssociationArtifact) {
				this.out.println("Realtionship to Association  "
						+ artifact.getFullyQualifiedName());

				Association association = maker.makeOrFindAssociation(artifact);
				if (association != null) {
					Stereotype aS = association
							.getApplicableStereotype(tsProfile
									.getQualifiedName()
									+ "::tigerstripe_association");
					association.applyStereotype(aS);
					addComponentStereotype(artifact, association);
				}
			} else if (artifact instanceof IDependencyArtifact) {
				Dependency dep = maker.makeDependency(artifact);
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
				Interface intf = maker.makeOrFindInterface(artifact);
				Stereotype iS = intf.getApplicableStereotype(tsProfile
						.getQualifiedName()
						+ "::tigerstripe_session");
				intf.applyStereotype(iS);
				ArrayList emitList = new ArrayList();
				for (IEmittedEvent emitted : ((ISessionArtifact) artifact)
						.getEmittedEvents()) {
					IAbstractArtifact eventArtifact = mgrSession.getArtifactByFullyQualifiedName(emitted.getFullyQualifiedName(),true);
					if ( eventArtifact != null){
						emitList.add(Utilities.mapName(emitted.getFullyQualifiedName(), eventArtifact.getProjectDescriptor().getIProjectDetails().getName()));
					}
				}
				intf.setValue(iS, "emits", emitList);

				ArrayList manageList = new ArrayList();
				for (IManagedEntityDetails details : ((ISessionArtifact) artifact)
						.getManagedEntityDetails()) {
					IAbstractArtifact detailsArtifact = mgrSession.getArtifactByFullyQualifiedName(details.getFullyQualifiedName(),true);
					if ( detailsArtifact != null){
						manageList.add(Utilities.mapName(details.getFullyQualifiedName(), detailsArtifact.getProjectDescriptor().getIProjectDetails().getName()));
					}
				}
				intf.setValue(iS, "manages", manageList);

				ArrayList supportList = new ArrayList();
				for (INamedQuery query : ((ISessionArtifact) artifact)
						.getNamedQueries()) {
					IAbstractArtifact queryArtifact = mgrSession.getArtifactByFullyQualifiedName(query.getFullyQualifiedName(),true);
					if ( queryArtifact != null){
						supportList.add(Utilities.mapName(query.getFullyQualifiedName(), queryArtifact.getProjectDescriptor().getIProjectDetails().getName()));
					}
				}
				intf.setValue(iS, "supports", supportList);

				ArrayList exposesList = new ArrayList();
				for (IExposedUpdateProcedure proc : ((ISessionArtifact) artifact)
						.getExposedUpdateProcedures()) {
					IAbstractArtifact procArtifact = mgrSession.getArtifactByFullyQualifiedName(proc.getFullyQualifiedName(),true);
					if ( procArtifact != null){
					exposesList.add(Utilities.mapName(proc.getFullyQualifiedName(), procArtifact.getProjectDescriptor().getIProjectDetails().getName()));
					}
				}
				intf.setValue(iS, "exposes", exposesList);
				
				String className = artifact.getFullyQualifiedName();
				String umlClassName = Utilities.mapName(className, artifact.getProjectDescriptor().getIProjectDetails().getName());
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

			Package modelPackage = maker.makeOrFindPackage(artifact);

			if (artifact instanceof IManagedEntityArtifact
					|| artifact instanceof IDatatypeArtifact
					|| artifact instanceof IQueryArtifact
					|| artifact instanceof IEventArtifact
					|| artifact instanceof IUpdateProcedureArtifact
					|| artifact instanceof IExceptionArtifact) {
				Class clazz = maker.makeOrFindClass(artifact);
				out.println("Class : " + clazz.getQualifiedName());
				String className = artifact.getFullyQualifiedName();
				String umlClassName = Utilities.mapName(className, artifact.getProjectDescriptor().getIProjectDetails().getName() );
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
					Type type = maker.getUMLType(rType);
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
			out.println("Artifact Attributes end "
					+ artifact.getFullyQualifiedName());
		}

		monitor.done();

		this.out.println();
		monitor.beginTask("Doing Realizations :", projectArtifacts.size());

		// Need another pass for generalizations
		for (IAbstractArtifact artifact : projectArtifacts) {
			if (artifact instanceof IManagedEntityArtifact) {
				IManagedEntityArtifact entity = (IManagedEntityArtifact) artifact;
				// Do the implements
				Class clazz = maker.makeOrFindClass(entity);
				for (IAbstractArtifact impl : entity.getImplementedArtifacts()) {
					Interface implClazz = maker.makeOrFindInterface(impl);
					clazz.createInterfaceRealization("implements", implClazz);
					this.out.println("Created implementation "
							+ implClazz.getQualifiedName());
				}
			} else if (artifact instanceof IAssociationClassArtifact) {
				IAssociationClassArtifact assocClass = (IAssociationClassArtifact) artifact;
				// Do the implements
				Class clazz = maker.makeOrFindClass(assocClass);
				for (IAbstractArtifact impl : assocClass
						.getImplementedArtifacts()) {
					Interface implClazz = maker.makeOrFindInterface(impl);
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
					Class clazz = maker.makeOrFindClass(artifact);
					Class extendClazz = maker.makeOrFindClass(artifact
							.getExtendedArtifact());
					Generalization gen = clazz
							.createGeneralization(extendClazz);
				} else if (artifact instanceof IEnumArtifact) {
					// this.out.println ("Enum case!");
					Enumeration enumer = maker.makeOrFindEnum(artifact);
					Enumeration extendEnumer = maker.makeOrFindEnum(artifact
							.getExtendedArtifact());
					Generalization gen = enumer
							.createGeneralization(extendEnumer);
				} else if (artifact instanceof IAssociationClassArtifact) {
					// this.out.println ("Assoc Class case!");
					// Note that AssocClass can extend MEntity or AssocClass
					AssociationClass association = maker.makeOrFindAssociationClass(artifact);
					if (artifact.getExtendedArtifact() instanceof IManagedEntityArtifact) {
						Class extendsClass = maker.makeOrFindClass(artifact
								.getExtendedArtifact());
						Generalization gen = association
								.createGeneralization(extendsClass);
					} else {
						AssociationClass extendsAssociation = maker.makeOrFindAssociationClass(artifact
								.getExtendedArtifact());
						Generalization gen = association
								.createGeneralization(extendsAssociation);
					}
				} else if (artifact instanceof IAssociationArtifact) {
					// this.out.println ("Assoc case!");
					Association association = maker.makeOrFindAssociation(artifact);
					Association extendsAssociation = maker.makeOrFindAssociation(artifact
							.getExtendedArtifact());
					Generalization gen = association
							.createGeneralization(extendsAssociation);
				} else if (artifact instanceof IDependencyArtifact) {
					// Can't generalize Dependencies...
				} else if (artifact instanceof ISessionArtifact) {
					this.out.println("Session case!");
					Interface intf = maker.makeOrFindInterface(artifact);
					Interface extendIntf = maker.makeOrFindInterface(artifact
							.getExtendedArtifact());
					Generalization gen = intf.createGeneralization(extendIntf);
				}

			}
			monitor.worked(1);
		}
		this.out.println("Finished Generalizations");
		monitor.done();

	}
	
	private void saveUnknowns(File exportDir, String exportFilename){
		
		if (unknownTypeMap.size() > 0) {
			URI fileUri = URI.createFileURI(exportDir.getAbsolutePath());
			msgText = "Unknown types output to : " + fileUri.toFileString();
			addMessage(msgText, 2);

			save(unknownTypeModel, fileUri.appendSegment("umlProfile")
					.appendSegment(exportFilename + ".UnknownTypes")
					.appendFileExtension(UMLResource.FILE_EXTENSION));
		}

	}
	
	private void saveModels(Collection<Model> models,File exportDir){
		for (Model model : models){
			URI fileUri = URI.createFileURI(exportDir.getAbsolutePath());
			msgText = "Model output to : " + (fileUri.appendSegment(model.getName()).appendFileExtension(
					UMLResource.FILE_EXTENSION)).toFileString();
			addMessage(msgText, 2);

			save(model, fileUri.appendSegment(model.getName()).appendFileExtension(
					UMLResource.FILE_EXTENSION));
		}
		monitor.done();
		out.println("UML2 export complete");
	}

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
			out.println("Resource "+resource.getURI() +" saved");
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
			Type type = maker.getUMLType(method.getReturnType());
			if (type != null) {
				Parameter result = operation.createReturnResult("return", type);
				result.setLower(1); // Returns are mandatory
				result.setUpper(Utilities.getUpperBound(method.getReturnType()
						.getTypeMultiplicity()));
				result.setDefault(method.getDefaultReturnValue());
				addReturnTypeStereotype(method, result);
				result.setName(method.getReturnName());
				for (IArgument arg : method.getArguments()) {

					Type argType = maker.getUMLType(arg.getType());
					if (argType != null) {
						Parameter param = operation.createOwnedParameter(arg
								.getName(), argType);
						Comment parameterComment = param.createOwnedComment();
						parameterComment.setBody(arg.getComment());
						// TODO arg.getRefBy()
						// Multiplicity
						param.setLower(Utilities.getLowerBound(arg.getType()
								.getTypeMultiplicity()));
						param.setUpper(Utilities.getUpperBound(arg.getType()
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
			Type type = maker.getUMLType(method.getReturnType());
			if (type != null) {
				Parameter result = operation.createReturnResult("return", type);
				result.setLower(1); // Returns are mandatory
				result.setUpper(Utilities.getUpperBound(method.getReturnType()
						.getTypeMultiplicity()));
				result.setDefault(method.getDefaultReturnValue());
				addReturnTypeStereotype(method, result);
				result.setName(method.getReturnName());

				for (IArgument arg : method.getArguments()) {

					Type argType = maker.getUMLType(arg.getType());
					if (argType != null) {
						Parameter param = operation.createOwnedParameter(arg
								.getName(), argType);
						Comment parameterComment = param.createOwnedComment();
						parameterComment.setBody(arg.getComment());
						// TODO arg.getRefBy()
						// Multiplicity
						param.setLower(Utilities.getLowerBound(arg.getType()
								.getTypeMultiplicity()));
						param.setUpper(Utilities.getUpperBound(arg.getType()
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
			Type type = maker.getUMLType(field.getType());
			if (type != null) {
				int lowerBound = Utilities.getLowerBound(field.getType()
						.getTypeMultiplicity());
				int upperBound = Utilities.getUpperBound(field.getType()
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



	protected static void addMessage(String msgText, int severity) {
		if (severity <= MESSAGE_LEVEL) {
			Message newMsg = new Message();
			newMsg.setMessage(msgText);
			newMsg.setSeverity(severity);
			messages.addMessage(newMsg);
		}

	}
}
