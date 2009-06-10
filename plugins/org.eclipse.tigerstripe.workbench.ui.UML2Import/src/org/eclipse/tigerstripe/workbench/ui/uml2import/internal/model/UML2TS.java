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
package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EChangeableEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument.EDirection;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjEnumSpecifics;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.profile.primitiveType.IPrimitiveTypeDef;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ImportUtilities;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.BehavioralFeature;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.InstanceValue;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.LiteralSpecification;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.ProfileApplication;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.VisibilityKind;
import org.eclipse.uml2.uml.internal.impl.EnumerationLiteralImpl;

public class UML2TS {

	private Map<EObject, String> classMap;
	
	private String PRIMITIVE_PREFIX = "primitive.";

	private String DEFAULT_MULTIPLICITY = "1";

	private IArtifactManagerSession mgrSession;

	private PrintWriter out;

	private String modelLibrary;

	private MessageList messages;

	private IWorkbenchProfileSession profileSession;
	
	private int nullClassCounter = 0;
	
	private CoreArtifactSettingsProperty property;
	
	private boolean ignoreUnknown = false;
	private String unknownType = "primitive.unknown";
	private ICommentProcessor commentProcessor;
	
	/** constructor */
	public UML2TS(Map<EObject, String> classMap, PrintWriter out, CoreArtifactSettingsProperty property) {
		this.classMap = classMap;
		this.out = out;
		this.property = property;
		this.profileSession = TigerstripeCore.getWorkbenchProfileSession();
		getCommentProcessor();
		
		out.println ("INFO : MAPPINGS USED FOR EXTRACT");
		for (EObject o : classMap.keySet()){
			if ( o instanceof NamedElement){
				out.println("INFO : Mapping Element :"+((NamedElement) o).getQualifiedName()+  "    "+classMap.get(o));
			}
		}
		out.flush();
		
	}

	/**
	 * 	 * Pull all of the "candidate artifacts" from the model
	 * 
	 */ 
	public Map<String,IAbstractArtifact> extractArtifacts(Model model, String modelLibrary,  MessageList messages,
			ITigerstripeModelProject tsProject,boolean ignoreUnknown, String unknownType ){
		
		
		// This is where we store the extracted stuff..
		Map<String,IAbstractArtifact> extractedArtifacts = new HashMap<String, IAbstractArtifact>();
		
		out.flush();
		this.ignoreUnknown = ignoreUnknown;
		this.unknownType = unknownType;
		this.messages = messages;
		this.modelLibrary = modelLibrary;
		out.println("INFO : EXTRACTING FROM UML MODEL");
		for (Package p : model.getImportedPackages()){
			out.println("INFO : Imported Package :"+p.getName());	
		}
		out.println("INFO : ignoreUnknown ->"+ignoreUnknown);
		out.println("INFO : unknownTypes mapped to ->"+unknownType);
		
		
		// Walk the model
		TreeIterator t = model.eAllContents();
		try {
			this.mgrSession = tsProject.getArtifactManagerSession();
			t = model.eAllContents();
			while (t.hasNext()) {
				EObject o = (EObject) t.next();
				if (classMap.containsKey(o) && classMap.get(o) != ""){
					if (! (o instanceof Dependency)){
						IAbstractArtifact thisOne = mapToArtifact(o, classMap.get(o));
						if (thisOne != null ){
							extractedArtifacts.put(thisOne.getFullyQualifiedName(), thisOne);
						} else {
							out.println("WARN : Null returned when looking for EObject in project - probably not an artifact!");
						}
					}
				}
			}
			
			// Build dependencies
			t = model.eAllContents();

			out.println("INFO : Post-Processing on Dependencies");
			while (t.hasNext()) {
				EObject o = (EObject) t.next();
				if (o instanceof Classifier) {
					Classifier element = (Classifier) o;
					String packageName = element.getNearestPackage()
							.getQualifiedName();
					for (Object depO : element.getClientDependencies()) {
						if (depO instanceof InterfaceRealization){
							InterfaceRealization implReal = (InterfaceRealization) depO;
							Classifier client = null;
							

							List<IAbstractArtifact> suppliers = new ArrayList<IAbstractArtifact>();

							Classifier supplier = null;
							for (Object c : implReal.getClients()) {
								if (c instanceof Classifier) {
									client = (Classifier) c;
									//this.out.println("Client " + client.getQualifiedName());
									// There should only be one client!
									break;
								}
							}
							for (Object s : implReal.getSuppliers()) {
								if (s instanceof Classifier) {
									supplier = (Classifier) s;
									//this.out.println("Supplier " + supplier.getQualifiedName());
									// Find the supplier artifact
									String supplierFQN = ImportUtilities.convertToFQN(supplier.getQualifiedName(),messages,out);
									IAbstractArtifact supplierArtifact = extractedArtifacts.get(supplierFQN);
									if (supplierArtifact != null){
										suppliers.add(supplierArtifact);
									} else {
										this.out.println("INFO : Failed to find supplier for implementation "+supplierFQN);
									}
								}
							}
							
							// Find our client
							String clientFQN = ImportUtilities.convertToFQN(client.getQualifiedName(),messages,out);
							IAbstractArtifact clientArtifact = extractedArtifacts.get(clientFQN);
							if (clientArtifact != null){
								if (clientArtifact instanceof IManagedEntityArtifact){
									IManagedEntityArtifact clientEntity = (IManagedEntityArtifact) clientArtifact;
									Collection<IAbstractArtifact> existing = clientEntity.getImplementedArtifacts();
									
									ArrayList<IAbstractArtifact> imp = new ArrayList<IAbstractArtifact>();
									imp.addAll(existing);
									imp.addAll(suppliers);
									
								    clientEntity.setImplementedArtifacts(imp);
								    this.out.println("INFO : Adding "+clientEntity.getImplementedArtifacts().size()+" implements relations to "+clientFQN);

								    
								} else if (clientArtifact instanceof AssociationClassArtifact){
									AssociationClassArtifact clientEntity = (AssociationClassArtifact) clientArtifact;
									Collection<IAbstractArtifact> existing = clientEntity.getImplementedArtifacts();
									
									ArrayList<IAbstractArtifact> imp = new ArrayList<IAbstractArtifact>();
									imp.addAll(existing);
									imp.addAll(suppliers);
									
								    clientEntity.setImplementedArtifacts(imp);
								    this.out.println("INFO : Adding "+clientEntity.getImplementedArtifacts().size()+" implements relations to "+clientFQN);
								} else {
									this.out.println("WARN : Client for implementation is not a Managed Entity or Association Class "+clientFQN);
								}
							} else {
								this.out.println("WARN : Failed to find client for implementation "+clientFQN);
							}
							

						} else if (depO instanceof Dependency) {
							// See if we mapped or ignored it!
							if (classMap.containsKey(o) && classMap.get(o) == IDependencyArtifact.class.getName()){
								// We can ONLY map to a DependencyArtifact
								Dependency dep = (Dependency) depO;
								String depName = dep.getName();
								String depQName = dep.getQualifiedName();
								this.out.println("INFO : Dep Name : " + depName + " "+depQName);

								// Make a dependency artifact							

								Classifier client = null;
								Classifier supplier = null;
								for (Object c : dep.getClients()) {
									if (c instanceof Classifier) {
										client = (Classifier) c;
										break; // Only do one..
									}
								}
								for (Object s : dep.getSuppliers()) {
									if (s instanceof Classifier) {
										supplier = (Classifier) s;
										break; // Only do one..
									}
								}

								// So go ahead and make one..
								if (supplier != null && client != null) {

									if (depQName == null || depQName.equals("")){
										depQName = client.getName() + "_DependsOn_"+ supplier.getName();
									}

									IAbstractArtifact depArtifact = this.mgrSession
									.makeArtifact(IDependencyArtifact.class
											.getName());
									IDependencyArtifact dependency = (IDependencyArtifact) depArtifact;
									dependency
									.setFullyQualifiedName(ImportUtilities.convertToFQN(depQName,messages,out));

									this.out.println();
									this.out.println("INFO : MAKING ARTIFACT : "
											+ depArtifact.getLabel()
											+ " FQN "
											+ depArtifact.getFullyQualifiedName());
									IType atype = dependency.makeType();
									atype.setFullyQualifiedName(ImportUtilities.convertToFQN(client
											.getQualifiedName(),messages,out));
									dependency.setAEndType(atype);

									IType ztype = dependency.makeType();
									ztype
									.setFullyQualifiedName(ImportUtilities.convertToFQN(supplier
											.getQualifiedName(),messages,out));
									dependency.setZEndType(ztype);

									//IStereotype tsStereo = this.profileSession.getActiveProfile()
									//	.getStereotypeByName("DependencyLabel");

									//IStereotypeInstance depLabel = tsStereo.makeInstance();
									//String labelName = "label";
									//IStereotypeAttribute tsStereoAttribute = null;
									//IStereotypeAttribute[] tsStereoAttributes = depLabel.getCharacterizingStereotype().getAttributes();
									//for (int at = 0; at < tsStereoAttributes.length; at++) {
									//	if (tsStereoAttributes[at].getName().equals(labelName)) {
									//		tsStereoAttribute = tsStereoAttributes[at];
									//		break;
									//	}

									//}

									//depLabel.setAttributeValue(tsStereoAttribute, depName);
									//dependency.addStereotypeInstance(depLabel);

									extractedArtifacts.put(dependency.getFullyQualifiedName(), dependency);
								}
							}
						}
					}
				}
			}



			// Do a second pass to set the generalizations.
			t = model.eAllContents();

			this.out.println("INFO : Post-Processing on Generalizations");
			nullClassCounter = 0;
			while (t.hasNext()) {
				EObject o = (EObject) t.next();
				if (o instanceof Classifier) {
					Classifier element = (Classifier) o;
					String baseFullyQualifiedName = ImportUtilities.convertToFQN(element.getQualifiedName(),messages,out);
					IAbstractArtifact artifact = extractedArtifacts.get(baseFullyQualifiedName);
					if (artifact != null ){
						for (Classifier gen : element.getGenerals()){
							String genFQN = gen.getQualifiedName();
							String genFullyQualifiedName = ImportUtilities.convertToFQN(genFQN,messages,out);
							out.println("INFO : Generalisation of : "+ baseFullyQualifiedName+ " : " +genFullyQualifiedName);
							IAbstractArtifact genArtifact = extractedArtifacts.get(genFullyQualifiedName);
							if (genArtifact != null) {
								artifact.setExtendedArtifact(genArtifact);
							} else {
								genArtifact = this.mgrSession.makeArtifact(artifact);
								genArtifact.setFullyQualifiedName(genFullyQualifiedName);
								artifact.setExtendedArtifact(genArtifact);
							}
							// TS MetaModel only supports one generalization;
							break;
						}
					} else {
						// TODO 
						out.println("The artifact is null because we probabaly had to make up a name for it!");
					}
				}
			}
		
		} catch (Exception e){
			// TODO something here
			out.println("ERROR : Unknown error");
			e.printStackTrace(out);
		} finally {
			out.flush();
			//out.println(extractedArtifacts);
			return extractedArtifacts;
		}
	}
	
	
	private IAbstractArtifact mapToArtifact(EObject o, String artifactTypeName) {
		NamedElement element = (NamedElement) o;
		String eleName;
		boolean hasTempName = false;
		if (element.getName() == null || element.getName().equals("")){
			String msText = "UML Class with null name - defaulting (found in "+o.eResource().getURI()+" )";
			
			ImportUtilities.addMessage(msText, 0, messages);
			this.out.println("ERROR :" + msText);
			eleName = "element"+Integer.toString(nullClassCounter);
			nullClassCounter++;
			hasTempName = true;
		} else {
			eleName = element.getName();
			hasTempName = false;
		}
		
		
		// See if this type of artifact is supported
		boolean supportedInProfile = false;
		for (String enabled :property.getEnabledArtifactTypes()){
			if (enabled.equals(artifactTypeName)){
				supportedInProfile = true;
				break;
			}
		}
		if (!supportedInProfile){
			String msgText = "Unsupported Artifact type in current profile ("+artifactTypeName+") for : "+eleName;
			ImportUtilities.addMessage(msgText, 0, messages);
			out.println( "ERROR : "+msgText);
			return null;
		}
		
		IAbstractArtifact artifact = createArtifact(element,
				artifactTypeName);

		
		
		if (artifact == null) {
			String msgText = "Failed to extract class to artifact:"
				+ eleName;
			ImportUtilities.addMessage(msgText, 0, messages);
			out.println( "ERROR : "+msgText);
			return null;
		}
		
		String msText = "Processing UML Class : " + eleName;
		this.out.println("INFO :" + msText);
		// Some EObjects could be of Type "NestedClassifier" which we can't
		// handle in our model.
		// Not sure how to detect these..this is a bit "hacky"
		String packageName = element.getNearestPackage().getQualifiedName();

		msText = "packageName :" + packageName;
		this.out.println("INFO :" + msText);

		String myFQN = "";
		if (element.getQualifiedName() != null){
			String elementName = element.getQualifiedName();
			myFQN = elementName;
			msText = "elementName :" + elementName;
			this.out.println("INFO :" + msText);

			// Top level package will have the format
			// Project::top
			elementName = elementName.substring(packageName.length());
			if (elementName.length() > 2){
				if (elementName.substring(2).contains("::")) {
					// some elements left in name - ie not child of the package
					
					
					
					
					String msgText = "Nested Classifier is not supported in TS : "
						+ eleName + " "+element.getClass().getName();
					ImportUtilities.addMessage(msgText, 0, messages);
					this.out.println("Error :" + msgText);
					return null;
				}
			}
		} else {

			msText = "elementName :" + eleName;
			this.out.println("INFO :" + msText);
			myFQN = packageName+"::"+eleName;
		}



		String artifactFullyQualifiedName = ImportUtilities.convertToFQN(myFQN,messages,out);
		
		if (artifact instanceof IPackageArtifact){
			// for a package we need to make sure the whole
			// thing is handled by convertToFQN (as the name is the package name)
			int len = artifactFullyQualifiedName.length();
			artifactFullyQualifiedName = ImportUtilities.convertToFQN(myFQN+"::XXX",messages,out);
			artifactFullyQualifiedName = artifactFullyQualifiedName.substring(0,len);
		}

		this.out.println("INFO : MAKING ARTIFACT : " + artifact.getLabel() + " FQN "
				+ artifactFullyQualifiedName);
		artifact.setFullyQualifiedName(artifactFullyQualifiedName);

		artifact.setComment(extractComment(artifact.getComment(),element));

		// Do Constants
		// shouldn't see any in model - only Enums
		setConstants(artifact, element);

		setAttributes(artifact, element);

		// Do Operations (check the model - shouldn't have any of these on
		// non-interfaces)
		setOperations(artifact, element);

		// Specifics for various artifact types...


			try {
				if (artifact instanceof IAssociationArtifact) {
					IAssociationArtifact assoc = (IAssociationArtifact) artifact;
					setAssociationEnds(assoc, element);
					if (assoc.getAEnd() != null && assoc.getZEnd() != null){
						if (hasTempName){
							// the make a new name based on the names of the classes on the ends

							String aEndClassName = assoc.getAEnd().getType().getName();
							String bEndClassName = assoc.getZEnd().getType().getName();

							String newName = aEndClassName+"To"+bEndClassName;
							out.println("INFO : Remapped temp "+ eleName + " to "+newName);
							assoc.setFullyQualifiedName(assoc.getPackage()+"."+newName);
						} 
					} else {
						// this was a bad import - drop it now.
						String msgText = "Failed to extract association to artifact (missing end information) :"
							+ eleName;
						ImportUtilities.addMessage(msgText, 0, messages);
						out.println( "ERROR : "+msgText);
						return null;
					}
				}
				if (artifact instanceof IAssociationClassArtifact) {
					IAssociationClassArtifact assoc = (IAssociationClassArtifact) artifact;
					setAssociationEnds(assoc, element);
					if (assoc.getAEnd() != null && assoc.getZEnd() != null){
						if (hasTempName){
							// the make a new name based on the names of the classes on the ends

							String aEndClassName = assoc.getAEnd().getType().getName();
							String bEndClassName = assoc.getZEnd().getType().getName();

							String newName = aEndClassName+"To"+bEndClassName;
							out.println("INFO : Remapped temp "+ eleName + " to "+newName);
							assoc.setFullyQualifiedName(assoc.getPackage()+"."+newName);
						} 
					} else {
						// this was a bad import - drop it now.
						String msgText = "Failed to extract association to artifact (missing end information) :"
							+ eleName;
						ImportUtilities.addMessage(msgText, 0, messages);
						out.println( "ERROR : "+msgText);
						return null;
					}
				}
				if (element instanceof Classifier){
					artifact.setAbstract(((Classifier) element).isAbstract());
				}
				return artifact;

			} catch (Exception e) {
				String msgText = "Failed to extract class to artifact:"
					+ artifact.getName();
				ImportUtilities.addMessage(msgText, 0, messages);
				this.out.println("ERROR : " + msgText);
				e.printStackTrace(this.out);
				return null;
			}

		}

	private void setAssociationEnds(IAssociationArtifact assocArtifact,
			NamedElement element) {

		if (element instanceof Association) {
			Association assoc = (Association) element;

			List mEndTypes = assoc.getMemberEnds();
			if (assoc.getMemberEnds().size() != 2) {
				String msgText = "Association No of Ends != 2 "
						+ assocArtifact.getName();
				ImportUtilities.addMessage(msgText, 0, messages);
				this.out.println("ERROR : " + msgText);
			}
			ListIterator mEndTypesIt = mEndTypes.listIterator();
			boolean first = true;
			while (mEndTypesIt.hasNext()) {

				Property mEnd = (Property) mEndTypesIt.next();
				this.out.println("INFO : Association End " + mEnd.getName() );
				IAssociationEnd end = assocArtifact.makeAssociationEnd();

				end.setName(ImportUtilities.nameCheck(mEnd.getName(),messages,out));
				IType type = assocArtifact.makeField().makeType();

				if (!setTypeDetails(type, mEnd.getType(), mEnd, assocArtifact
						.getName()
						+ " : " + mEnd.getName(), true)) {
					continue;
					// This will go wrong as the Association needs to have a
					// type at both ends...
				}
				end.setType(type);

				if (ImportUtilities.nameCheck(mEnd.getName(),messages,out) != null) {
					end.setName(ImportUtilities.nameCheck(mEnd.getName(),messages,out));
				} else {
					String msgText = "Association End has no name : "
							+ assocArtifact.getName();
					ImportUtilities.addMessage(msgText, 1, messages);
					this.out.println("WARN : " + msgText);
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

				end.setComment(extractComment(end.getComment(),mEnd));
				
				// TODO - I don't know how to find this in the model ........
				end.setChangeable(EChangeableEnum.parse("none"));

				end
						.setMultiplicity(EMultiplicity
								.parse(readMultiplicity(mEnd)));

				// navigable and ordered
				end.setNavigable(mEnd.isNavigable());
				end.setOrdered(mEnd.isOrdered());
				end.setUnique(mEnd.isUnique());

				// Visibility
                VisibilityKind viz = mEnd.getVisibility();
				
				if (viz.getLiteral().equals("private")){
					end.setVisibility(IModelComponent.EVisibility.PRIVATE);
				} else if (viz.getLiteral().equals("package")){
					end.setVisibility(IModelComponent.EVisibility.PACKAGE);
				} else if (viz.getLiteral().equals("protected")){
					end.setVisibility(IModelComponent.EVisibility.PROTECTED);
				} else {
					end.setVisibility(IModelComponent.EVisibility.PUBLIC);
				}
				
				
				if (first) {
					assocArtifact.setAEnd(end);
					first = false;
				} else {
					assocArtifact.setZEnd(end);
				}

			}
			if (assocArtifact.getAEnd() != null && assocArtifact.getZEnd() != null){
				
			// Swap the aggregation  over....!!!!!!!!!!!!!
			EAggregationEnum temp = assocArtifact.getAEnd().getAggregation();
		
			((IAssociationEnd) (assocArtifact.getAEnd())).setAggregation(assocArtifact.getZEnd().getAggregation());
			((IAssociationEnd) assocArtifact.getZEnd()).setAggregation(temp);

			}
		} else {
			out.println("Not really an Association - Munging");
			if (element instanceof Classifier){
				AssociationClass parent = null;
				Classifier c = (Classifier) element;
				List gens = c.getGenerals();
				ListIterator genIt = gens.listIterator();
				while (genIt.hasNext()) {
					Classifier gen = (Classifier) genIt.next();
					if (gen instanceof AssociationClass) {
						parent = (AssociationClass) gen;
					}
				}
				if ( parent != null){
					setAssociationEnds(assocArtifact, parent);
					// need to add a comment that these are copied
					assocArtifact.getAEnd().setComment(
							"WARNING : End details copied form parent \n"+
							assocArtifact.getAEnd().getComment()
							);
					assocArtifact.getZEnd().setComment(
							"WARNING : End details copied form parent \n"+
							assocArtifact.getZEnd().getComment()
							);
				}
				
			}
			
		}

	}

	
	
	private void setGeneralization(IAbstractArtifact artifact,
			Classifier element) {

		List gens = element.getGenerals();
		ListIterator genIt = gens.listIterator();
		while (genIt.hasNext()) {
			Classifier gen = (Classifier) genIt.next();
			try {
				if (gen.getQualifiedName() != null){
					String genName = ImportUtilities.convertToFQN(gen.getQualifiedName(),messages,out);
					this.out.println("INFO : " + artifact.getName() + " Generalization "
							+ genName);

					IAbstractArtifact genArtifact = this.mgrSession
					                .getArtifactByFullyQualifiedName(genName);
					if (genArtifact == null) {
						String msgText = "Failed to retreive generalization for Artifact : "
							+ artifact.getName();
						ImportUtilities.addMessage(msgText, 0, messages);
						this.out.println("ERROR : " + msgText);
					} else {
						artifact.setExtendedArtifact(genArtifact);
					}
				}
			} catch (Exception e) {
				String msgText = "Failed to retreive generalization for Artifact : "
					+ artifact.getName();
				ImportUtilities.addMessage(msgText, 0, messages);
				this.out.println("ERROR : " + msgText);
				e.printStackTrace(this.out);
				return;
			}
		}


	}

	private void setConstants(IAbstractArtifact artifact, NamedElement element) {
		if (artifact instanceof IEnumArtifact){
			// In UML model should only be on Enums ?
			// TODO How does that square with Tigerstripe ?
			String cType ;
			List children = element.getOwnedElements();

			ListIterator it = children.listIterator();
			while (it.hasNext()) {
				EObject child = (EObject) it.next();
				if (child instanceof EnumerationLiteral) {
					EnumerationLiteral enumLit = (EnumerationLiteral) child;
					// Might have an EnumValue stereotype ?
					out.println("INFO : Enum Literal : " + enumLit.getName());
					String eName = ImportUtilities.nameCheck(enumLit.getName(),messages,out);
					if (eName.substring(0, 1).matches("[0-9]")){
						eName = "_"+eName;
						out.println("INFO : Name prepended : " + eName);
					}

					ILiteral iLiteral = artifact.makeLiteral();
					iLiteral.setName(eName);
					IType type = iLiteral.makeType();

					ValueSpecification spec = enumLit.getSpecification();
					if (spec instanceof LiteralInteger) {
						type.setFullyQualifiedName("int");
						LiteralInteger lit = (LiteralInteger) spec;
						iLiteral.setValue(Integer.toString(lit.getValue()));
					} else if (spec instanceof LiteralString) {
						type.setFullyQualifiedName("String");
						LiteralString lit = (LiteralString) spec;
						iLiteral.setValue(lit.getValue());
					} else {
						if (spec != null){
							if (spec.getLabel() != null){
								out.println("WARN : Literal type unknown ("+spec.getLabel()+") : defaulting to String ");
							} else if (spec.getName() != null){
								out.println("WARN : Literal type unknown ("+spec.getName()+") : defaulting to String ");
							} else {
								out.println("WARN : Literal type unknown : defaulting to String ");
							}
						} else {
							out.println("WARN : Literal type unknown : defaulting to String ");
						}
						type.setFullyQualifiedName("String");
						// If unknown type, set the label to the name by default
						iLiteral.setValue("\"" + iLiteral.getName() + "\"");

					}

					iLiteral.setType(type);
					artifact.addLiteral(iLiteral);


					List appliedStereotypes = ((NamedElement) child)
					.getAppliedStereotypes();
					if (appliedStereotypes.size() > 0) {

						int count = 0;
						for (int s = 0; s < appliedStereotypes.size(); s++) {
							Stereotype stereo = (Stereotype) appliedStereotypes.get(s);
							String stereoName = ImportUtilities.nameCheck(stereo.getName(), messages, out);

							if (!stereo.getName().equals("EnumValue")){
								IStereotype tsStereo = this.profileSession.getActiveProfile()
								.getStereotypeByName(stereoName);
								if (tsStereo == null) {
									String msgText = "No Stereotype Named "+stereoName +" in TS";
									ImportUtilities.addMessage(msgText, 0, messages);
									this.out.println("ERROR : " + msgText);
									continue;
								}
								IStereotypeInstance tsStereoInstance = tsStereo.makeInstance();

								List stereoAttributes = stereo.getAllAttributes();
								for (int a = 0; a < stereoAttributes.size(); a++) {
									Property attribute = (Property) stereoAttributes
									.get(a);
									String attributeName = ImportUtilities.nameCheck(attribute.getName(), messages, out);


									if (!attribute.getName().startsWith("base_")) {
										// if ((element.getValue(stereo,
										// attribute.getName()) != null) ){
										this.out.println("INFO : Enum Lit Stereo Attribute : "
												+ stereoName
												+ ":"
												+ attributeName
												+ " -> "
												+ ((NamedElement) child).getValue(
														stereo, attribute.getName()));
										count++;
										iLiteral.setValue(((NamedElement) child)
												.getValue(stereo,
														attribute.getName())
														.toString());
										
										addAttributesToStereoType(tsStereoInstance, stereo,
												attribute.getName(),  attributeName, (NamedElement) child);
										iLiteral.addStereotypeInstance(tsStereoInstance);

									}

								}
							} else if (stereo.getName().equals("EnumValue")){
								// TODO - Is this specific to RSM ?
								List stereoAttributes = stereo.getAllAttributes();

								//this.out.println("Found EnumValue "+stereoAttributes.size());

								for (int a = 0; a < stereoAttributes.size(); a++) {
									Property attribute = (Property) stereoAttributes.get(a);

									if (attribute.getName().equals("value")) {
										//this.out.println("Got the Value "+((NamedElement) child).hasValue(stereo,attribute.getName()));
										if (((NamedElement) child).hasValue(stereo,attribute.getName())){
											this.out.println("INFO : Enum Value : "
													+ stereo.getName()
													+ ":Value --> "+((NamedElement) child).getValue(stereo,attribute.getName()).toString());
											iLiteral.setValue(((NamedElement) child).getValue(stereo,attribute.getName()).toString());
										}
									} else {
										iLiteral.setValue("-1");
									}

								}
							}
						}
					}
				}
			}		
			out.println("INFO : Finished handling constants");
			// Now set the base Type for the Enum
			IEnumArtifact enumArtifact = (IEnumArtifact) artifact;
			
			if (enumArtifact.getLiterals().size()>0){
				ILiteral firstLit = enumArtifact.getLiterals().iterator().next();
				enumArtifact.setBaseType(firstLit.getType());
				//System.out.println(enumArtifact.getBaseTypeStr());
			} else {
				// Leave as the default - we have no idea!
			}
		}
	}


	private void setOperations(IAbstractArtifact artifact, NamedElement element) {
		List children = element.getOwnedElements();

		ListIterator it = children.listIterator();
		while (it.hasNext()) {
			EObject child = (EObject) it.next();
			if (child instanceof Operation) {
				Operation operation = (Operation) child;

				out.println("INFO : Operation :" + operation.getName());
				try {
					IMethod method = artifact.makeMethod();
					method.setName(operation.getName());
					method.setComment(extractComment(method.getComment(),((NamedElement) child)));

					// ============ return type =====================
					Parameter returnResult = operation.getReturnResult();
					if (returnResult != null){
						Type retType = returnResult.getType();

						if (retType == null){
							// assume this is a void return - although that might have a "void type" as well
							out.println("INFO : Operation is void ");
							method.setVoid(true);
							IType iType = method.makeType();
							iType.setFullyQualifiedName("void");
							method.setReturnType(iType);
							method.setReturnName("");
						} else {
							IType iType = method.makeType();
							if (!setTypeDetails(iType, retType, returnResult, artifact
									.getName()
									+ " : " + returnResult.getName())) {
								continue;
							}

							out.println("INFO : Operation return : "
									+ operation.getName() + " : "
									+ iType.getFullyQualifiedName());
							method.setReturnType(iType);
							if ( returnResult.getName() != null){
								method.setReturnName(returnResult.getName());
								this.out.println("INFO : Operation return Name : "
										+ returnResult.getName());
							} else {
								method.setReturnName("");
							}
						}
						if (returnResult.isSetDefault()){
							method.setDefaultReturnValue(returnResult.getDefault());
						}

						ArrayList stInstances = readStereotypes((NamedElement) returnResult);
						ListIterator stereoIt = stInstances.listIterator();
						while (stereoIt.hasNext()) {
							IStereotypeInstance iSI = (IStereotypeInstance) stereoIt
							.next();
							method.addReturnStereotypeInstance(iSI);
						}


					} else {
						// No return type - so make it a void method
						out.println("INFO : Operation has no return element - make void ");
						method.setVoid(true);
						IType iType = method.makeType();
						iType.setFullyQualifiedName("void");
						method.setReturnType(iType);
						method.setReturnName("");
					}

					method.setOrdered(operation.isOrdered());
					method.setUnique(operation.isUnique());
					method.setAbstract(operation.isAbstract());



					// ============ parameters =====================
					BehavioralFeature bF = (BehavioralFeature) child;
					List params = bF.getOwnedParameters();
					ListIterator paramIt = params.listIterator();
					while (paramIt.hasNext()) {
						Parameter param = (Parameter) paramIt.next();

						if (param.getDirection().getValue() == ParameterDirectionKind.RETURN) {
							// In TS, this is the return, so ignore it.
							continue;
						}
						IArgument arg = method.makeArgument();
						arg.setName(param.getName());
						
						EDirection dir = EDirection.INOUT;
						if (param.getDirection().getValue() == ParameterDirectionKind.IN)
							dir = EDirection.IN;
						else if (param.getDirection().getValue() == ParameterDirectionKind.OUT)	
							dir = EDirection.OUT;
						else if (param.getDirection().getValue() == ParameterDirectionKind.INOUT)	
							dir = EDirection.INOUT;
						arg.setDirection(dir);

						IType aType = method.makeType();
						Type pType = param.getType();
						if (!setTypeDetails(aType, pType, param, artifact.getName()
								+ " : " + param.getName())) {
							continue;
						}
						out.println("INFO : Operation parameter : "
								+ operation.getName() + " : " + param.getName()
								+ " : " + aType.getFullyQualifiedName());
						arg.setType(aType);


						ArrayList stInstances = readStereotypes((NamedElement) param);
						ListIterator stereoIt = stInstances.listIterator();
						while (stereoIt.hasNext()) {
							IStereotypeInstance iSI = (IStereotypeInstance) stereoIt
							.next();
							arg.addStereotypeInstance(iSI);
						}


						// ============================

						// Add some comments (now that we can!)
						arg.setComment(extractComment(arg.getComment(),(NamedElement) param));
						arg.setOrdered(param.isOrdered());
						arg.setUnique(param.isUnique());
						if (param.isSetDefault()){
							arg.setDefaultValue(param.getDefault());
						}

						method.addArgument(arg);
					}

					// Any Exceptions?
					List exceptions = bF.getRaisedExceptions();
					ListIterator excepIt = exceptions.listIterator();
					while (excepIt.hasNext()) {
						Class excep = (Class) excepIt.next();
						IException tsExcep = method.makeException();
						String excepFQN = ImportUtilities.convertToFQN(((NamedElement) excep)
								.getQualifiedName(),messages,out);
						tsExcep.setFullyQualifiedName(excepFQN);
						method.addException(tsExcep);
					}

					// Now we support Abstract on Methods
					artifact.setAbstract(((Classifier) element).isAbstract());

					VisibilityKind viz = operation.getVisibility();

					if (viz.getLiteral().equals("private")){
						method.setVisibility(IModelComponent.EVisibility.PRIVATE);
					} else if (viz.getLiteral().equals("package")){
						method.setVisibility(IModelComponent.EVisibility.PACKAGE);
					} else if (viz.getLiteral().equals("protected")){
						method.setVisibility(IModelComponent.EVisibility.PROTECTED);
					} else {
						method.setVisibility(IModelComponent.EVisibility.PUBLIC);
					}

					ArrayList stInstances = readStereotypes((NamedElement) child);
					ListIterator stereoIt = stInstances.listIterator();
					while (stereoIt.hasNext()) {
						IStereotypeInstance iSI = (IStereotypeInstance) stereoIt
						.next();
						method.addStereotypeInstance(iSI);
					}

					artifact.addMethod(method);
				} catch (Exception t){
					String msgText = "Method not fully addded";
					ImportUtilities.addMessage(msgText, 0, messages);
					out.println("ERROR : " + msgText);
					t.printStackTrace(out);
				}
			}
		}
	}

	private boolean setTypeDetails(IType iType, Type uType,
			MultiplicityElement param, String location) {
		return setTypeDetails(iType, uType, param, location, true);
	}

	private boolean setTypeDetails(IType iType, Type uType,
			MultiplicityElement param, String location, boolean setMulti) {
		boolean optional = true;
		try {
			if (uType != null && uType.eIsProxy()){
				// Type not loaded
				if (ignoreUnknown){
					String msgText = "Type not loaded : " + location + " ignoring";
					ImportUtilities.addMessage(msgText, 0, messages);
					out.println("WARN : " + msgText);
					return false;
				} else {
					String msgText = "Type not loaded : " + location + " Setting to "+ unknownType +"..."+ uType.toString();
					ImportUtilities.addMessage(msgText, 0, messages);
					out.println("ERROR : " + msgText);
					iType.setFullyQualifiedName(unknownType);
					return true;
				}
			}
			if ((uType == null) || (uType.getQualifiedName() == null)) {
				// TODO - Is this an error in the model?
				if (ignoreUnknown){
					String msgText = "Unsure of type : " + location + " ignoring";
					ImportUtilities.addMessage(msgText, 0, messages);
					out.println("WARN : " + msgText);
					return false;
				} else {
					String msgText = "Unsure of type : " + location + " Setting to "+ unknownType;
					ImportUtilities.addMessage(msgText, 0, messages);
					out.println("ERROR : " + msgText);
					iType.setFullyQualifiedName(unknownType);
					return true;
				}
			}
				
			// Need to determine if this is a primitive type 
			if (uType instanceof PrimitiveType){
				String pTypeFQN = uType.getName();
				String prim = checkPrimitives(ImportUtilities.nameCheck(pTypeFQN, messages, out));
				if (!prim.equals("")) {
					iType.setFullyQualifiedName(prim);
				} else {
					if (ignoreUnknown){
						String msgText = "Neither a model nor a known primitive type : " + location + " ignoring";
						ImportUtilities.addMessage(msgText, 0, messages);
						out.println("WARN : " + msgText);
						return false;
					} else {
						String msgText = "Neither a model nor a known primitive type : " + location + " Setting to "+ unknownType +"..."+ uType.toString();
						ImportUtilities.addMessage(msgText, 0, messages);
						out.println("ERROR : " + msgText);
						iType.setFullyQualifiedName(unknownType);
						return true;
					}
				}
				
			} else {
				iType.setFullyQualifiedName(ImportUtilities.convertToFQN(uType.getQualifiedName(),messages,out));
			}
			
			if (setMulti) {
				// optional = setMultiplicityNew(iType, param);
				iType.setTypeMultiplicity(EMultiplicity
						.parse(readMultiplicity(param)));
			}
			return true;
		} catch (Exception e){
			String msgText = "Exception working out type : " + location + " Skipping...";
			ImportUtilities.addMessage(msgText, 0, messages);
			out.println("ERROR : " + msgText);
			e.printStackTrace(out);
			return false;
		}
	}

	private boolean getOptional(IType iType, MultiplicityElement property) {
		ValueSpecification lowerVal = property.getLowerValue();
		String lower = "";
		if (lowerVal != null) {
			lower = "" + property.getLower();
			if (lower.equals("0")) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private void setAttributes(IAbstractArtifact artifact, NamedElement element) {
		List children = element.getOwnedElements();
		// Look for a child of type Property.
		// Need to dissociate these from Association things - eg OwnedEnd

		ListIterator it = children.listIterator();
		while (it.hasNext()) {
			EObject child = (EObject) it.next();
			if (child instanceof Property) {
				Property property = (Property) child;

				if (property.getAssociation() != null) {
					// This ain't an attribute , but part of an association, so
					// skip it here
					this.out
					.println("INFO : Skipping property ("+property.getName()+")- it's as assoc. thing");
					continue;
				}
				try{
					IField field = artifact.makeField();
					field.setName(ImportUtilities.nameCheck(property.getName(),messages,out));

					Type propType = property.getType();
					IType type = field.makeType();
					if (!setTypeDetails(type, propType,
							(MultiplicityElement) child, artifact.getName() + " : "
							+ field.getName())) {
						continue;
					}
					// TODO - Is this valid any more?
					field.setOptional(getOptional(type,
							(MultiplicityElement) child));

					this.out.println("INFO : Property : " + property.getName() + " : "
							+ type.getFullyQualifiedName());
					field.setType(type);

					field.setComment(extractComment(field.getComment(),(NamedElement) child));

					field.setOrdered(property.isOrdered());
					field.setUnique(property.isUnique());

					if (property.isSetDefault()){
						field.setDefaultValue(property.getDefault());
					}
					if (property.isReadOnly()){
						field.setReadOnly(property.isReadOnly());
					}

					// Visibility 
					VisibilityKind viz = property.getVisibility();

					if (viz.getLiteral().equals("private")){
						field.setVisibility(IModelComponent.EVisibility.PRIVATE);
					} else if (viz.getLiteral().equals("package")){
						field.setVisibility(IModelComponent.EVisibility.PACKAGE);
					} else if (viz.getLiteral().equals("protected")){
						field.setVisibility(IModelComponent.EVisibility.PROTECTED);
					} else {
						field.setVisibility(IModelComponent.EVisibility.PUBLIC);
					}

					ArrayList stInstances = readStereotypes((NamedElement) child);

					ListIterator stereoIt = stInstances.listIterator();
					while (stereoIt.hasNext()) {
						IStereotypeInstance iSI = (IStereotypeInstance) stereoIt
						.next();
						field.addStereotypeInstance(iSI);
					}

					artifact.addField(field);
				} catch (Exception t){
					String msgText = "Attribute not addded";
					ImportUtilities.addMessage(msgText, 0, messages);
					out.println("ERROR : " + msgText);
				}
			}
		}

	}

	private ArrayList readStereotypes(NamedElement element) {

		ArrayList allTSStereoInstances = new ArrayList();
		List appliedStereotypes = element.getAppliedStereotypes();
		if (appliedStereotypes.size() > 0) {
			for (int s = 0; s < appliedStereotypes.size(); s++) {
				Stereotype stereo = (Stereotype) appliedStereotypes.get(s);
				String stereoName = ImportUtilities.nameCheck(stereo.getName(), messages, out);
				
				List stereoAttributes = stereo.getAllAttributes();

				IStereotype tsStereo = this.profileSession.getActiveProfile()
						.getStereotypeByName(stereoName);
				if (tsStereo == null) {
					String msgText = "No Stereotype Named "+stereoName +" in TS";
					ImportUtilities.addMessage(msgText, 0, messages);
					this.out.println("ERROR : " + msgText);
					continue;
				}
				IStereotypeInstance tsStereoInstance = tsStereo.makeInstance();

				int count = 0;
				for (int a = 0; a < stereoAttributes.size(); a++) {
					Property attribute = (Property) stereoAttributes.get(a);
					String attributeName = ImportUtilities.nameCheck(attribute.getName(), messages, out);
					if (!attribute.getName().startsWith("base_")) {
						// if ((element.getValue(stereo, attribute.getName()) !=
						// null) ){
						
						this.out.println("INFO : Property Stereo Attribute : "
										+ stereoName
										+ ":"
										+ attribute.getName()
										+ " -> "
										+ element.getValue(stereo, attribute
												.getName()));
						count++;

						addAttributesToStereoType(tsStereoInstance, stereo,
								attribute.getName(),  attributeName, element);
					}
				}
				if (count == 0) {
					// We've handled no other attributes,so
					// This is an "existence" type of Stereotype
					// so it's mere presence is important
					// We have given the user a choice in the profile stuff so
					// our
					// behaviour needs to be mildly intelligent.
					this.out.println("INFO :  Property stereo Attribute : "
							+ stereo.getName() + ":Existence --> true");
				}
				allTSStereoInstances.add(tsStereoInstance);

			}
		}
		return allTSStereoInstances;
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
			} else if (! upper.equals("1") && !upper.equals("0")){
				// FOund an instance where upper was "2"!
				this.out.println("WARN :  Overriding upper Multiplicity from "+upper);
				upper = "*";
			}
		}

		if (lowerVal != null) {
			lower = "" + property.getLower();
			if (lower.equals("-1")) {
				// Can't ever see this happening...
				lower = "*";
			} else if (! lower.equals("1") && !lower.equals("0")){
				// FOund an instance where upper was "2"!
				lower = "0";
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
		if (multiplicityString.equals("0..*")) {
			// Set it to some default
			multiplicityString = "*";
		}
		if (multiplicityString.equals("1..1")) multiplicityString = "1";
		
		// Check it's valid !
		
		this.out.println("INFO : Multiplicity -> " + multiplicityString);
		return multiplicityString;
	}

	/**
	 * Have to do this away from other stereos as they define behaviour
	 * 
	 * @param element
	 */
	private IAbstractArtifact createArtifact(NamedElement element,
			String artifactTypeName) {

		IAbstractArtifact absArtifact;

		boolean hasTypeDef = false;
		boolean isException = false;
		List allTSStereoInstances = new ArrayList();

		List appliedStereotypes = element.getAppliedStereotypes();
		if (appliedStereotypes.size() > 0) {
			for (int s = 0; s < appliedStereotypes.size(); s++) {
				Stereotype stereo = (Stereotype) appliedStereotypes.get(s);
				String stereoName = ImportUtilities.nameCheck(stereo.getName(), messages, out);
				
				IStereotype tsStereo = this.profileSession.getActiveProfile()
						.getStereotypeByName(stereoName);
				if (tsStereo == null) {
					String msgText = "No Stereotype in TS with name :"
							+ stereoName;
					ImportUtilities.addMessage(msgText, 0, messages);
					this.out.println("ERROR : " + msgText);
					continue;
				}
				IStereotypeInstance tsStereoInstance = tsStereo.makeInstance();

				List stereoAttributes = stereo.getAllAttributes();
				int count = 0;

				for (int a = 0; a < stereoAttributes.size(); a++) {
					Property attribute = (Property) stereoAttributes.get(a);
					String attributeName = ImportUtilities.nameCheck(attribute.getName(), messages, out);
					if (!attribute.getName().startsWith("base_")) {
						// if ((element.getValue(stereo, attribute.getName()) !=
						// null) ){
						this.out
								.println("INFO : Artifact Stereo Attribute : "
										+ stereoName
										+ ":"
										+ attributeName
										+ " -> "
										+ element.getValue(stereo, attribute.getName()));
						count++;

						// Add attributes to the instance
						addAttributesToStereoType(tsStereoInstance, stereo,
								attribute.getName(), attributeName, element);
					}
				}
				if (count == 0) {
					// We've handled no other attributes,so
					// This is an "existence" type of Stereotype
					// so it's mere presence is important
					// We have given the user a choice in the profile stuff so
					// our
					// behaviour needs to be mildly intelligent.
					this.out.println("INFO : Artifact Stereotype : "
							+ stereo.getName() + ":Existence --> true");
				}
				allTSStereoInstances.add(tsStereoInstance);
			}

		}

		try {
			absArtifact = this.mgrSession.makeArtifact(artifactTypeName);
		} catch (IllegalArgumentException t) {
			out.println("ERROR : artifactTypeName is invalid "+artifactTypeName);
			return null;
		}

		// TODO Apply the stereotypes to the artifacts...
		ListIterator stereoIt = allTSStereoInstances.listIterator();
		while (stereoIt.hasNext()) {
			IStereotypeInstance iSI = (IStereotypeInstance) stereoIt.next();
			// TODO Put this in....
			absArtifact.addStereotypeInstance(iSI);
		}

		return absArtifact;

	}

	private void addAttributesToStereoType(
			IStereotypeInstance tsStereoInstance, Stereotype stereo,
			String umlAattributeName, String tsAttributeName, NamedElement element) {

		IStereotypeAttribute[] tsStereoAttributes = tsStereoInstance
				.getCharacterizingStereotype().getAttributes();
		
		IStereotypeAttribute tsStereoAttribute = null;
		for (int at = 0; at < tsStereoAttributes.length; at++) {
			if (tsStereoAttributes[at].getName().equals(tsAttributeName)) {
				tsStereoAttribute = tsStereoAttributes[at];
				break;
			}

		}
		if (tsStereoAttribute != null  && element.getValue(stereo, umlAattributeName) != null) {
			try {
				//this.out.println(tsStereoAttribute.isArray());
				//this.out.println(attribute.getName());
				//this.out.println(stereo.getName());
				//this.out.println(element);
				//this.out.println(element.getValue(stereo, attribute.getName()));
				if (tsStereoAttribute.isArray() && element.getValue(stereo, umlAattributeName) instanceof EList ){
					
					// Need to split the value into an array and set that.
					EList valList =  (EList) element.getValue(stereo, umlAattributeName);
					String[] vals = new String[valList.size()];
					for (int i=0;i<valList.size();i++){
						vals[i] = valList.get(i).toString();
					}
					tsStereoInstance.setAttributeValues(tsStereoAttribute, vals);
				} else if (element.getValue(stereo, umlAattributeName) instanceof String){	
					tsStereoInstance.setAttributeValue(tsStereoAttribute, 
							element.getValue(stereo, umlAattributeName).toString());
				} else if (element.getValue(stereo, umlAattributeName) instanceof EnumerationLiteralImpl){
					EnumerationLiteralImpl enumImpl = (EnumerationLiteralImpl) element.getValue(stereo, umlAattributeName);
					tsStereoInstance.setAttributeValue(tsStereoAttribute, 
							enumImpl.getName());
				} else {
					tsStereoInstance.setAttributeValue(tsStereoAttribute, 
							element.getValue(stereo, umlAattributeName).toString());
					out.println ("WARN : Uncertain stereo attr type "+ tsStereoInstance.getName() + ":"
							+ tsAttributeName);
				}
			} catch (Exception e) {
				String msgText = "Failed to set attribute Value on Stereotype : "
						+ element.getName()
						+ " : "
						+ stereo.getName()
						+ " : "
						+ umlAattributeName;
				ImportUtilities.addMessage(msgText, 0, messages);
				this.out.println("ERROR : " + msgText);
				e.printStackTrace(out);
				return;
			}
		}

	}

	public String extractComment(String startComment,NamedElement element) {
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
		if (comment != null) {
			String processedComment;
			if (commentProcessor != null){
				try {
					processedComment = commentProcessor.processString(comment);
					this.out.println("INFO : Comment Processed to :" + processedComment);
				} catch (Exception e){
					//Mishandled
					this.out.println("WARNING : Comment Processing failed" + e.getMessage());
					processedComment = comment;
				}
			}
			
			this.out.println("INFO : Comment " + comment);
			if (startComment != "")
				return startComment+"\n"+comment;
			else 
				return comment;
		}
		return startComment;
	}

	/** Just a play method - for trying stuff out */
	public void loadUML(Model model) {
		// Make sure the TS project is OK

		// Walk the model
		TreeIterator t = model.eAllContents();
		while (t.hasNext()) {
			EObject o = (EObject) t.next();
			if (o instanceof Class) {
				Class cl = (Class) o;
				out.println("Class" + cl.getQualifiedName());

			} else if (o instanceof AssociationClass) {
				AssociationClass assocCl = (AssociationClass) o;
				out.println("AssociationClass"
						+ assocCl.getQualifiedName());

			} else if (o instanceof Association) {
				Association assoc = (Association) o;
				out.println("Association" + assoc.getQualifiedName());

			} else if (o instanceof Enumeration) {
				Enumeration enumeration = (Enumeration) o;
				out.println("Enumeration"
						+ enumeration.getQualifiedName());

			} else if (o instanceof EnumerationLiteral) {
				EnumerationLiteral enumLit = (EnumerationLiteral) o;
				// System.out.println("EnumerationLiteral" +
				// enumLit.getQualifiedName());

			} else if (o instanceof Property) {
				Property prop = (Property) o;
				// System.out.println("Property" + prop.getQualifiedName());

			} else if (o instanceof LiteralSpecification) {
				LiteralSpecification lit = (LiteralSpecification) o;
				// System.out.println("LiteralSpecification" +
				// lit.getQualifiedName());

			} else if (o instanceof Property) {
				Property prop = (Property) o;
				// System.out.println("Property" + prop.getQualifiedName());

			} else if (o instanceof Operation) {
				Operation op = (Operation) o;
				// System.out.println("Operation" + op.getQualifiedName());

			} else if (o instanceof Comment) {
				Comment comm = (Comment) o;
				// System.out.println("Comment" + comm.getBody());

			} else if (o instanceof EAnnotation) {
				EAnnotation ann = (EAnnotation) o;
				// System.out.println("EAnnotation" + ann.getBody());

			} else if (o instanceof Generalization) {
				Generalization gen = (Generalization) o;
				// System.out.println("Generalization");

			} else if (o instanceof OpaqueExpression) {
				OpaqueExpression op = (OpaqueExpression) o;
				// System.out.println("OpaqueExpression");

			} else if (o instanceof InstanceValue) {
				InstanceValue iVal = (InstanceValue) o;
				// System.out.println("InstanceValue");

			} else if (o instanceof Constraint) {
				Constraint constraint = (Constraint) o;
				// System.out.println("constraint");

			} else if (o instanceof Package) {
				Package pack = (Package) o;
				// Not really very interesting
				// System.out.println("Package" + pack.getQualifiedName());

			} else if (o instanceof PackageImport) {
				PackageImport packImp = (PackageImport) o;
				// Not really very interesting
				// System.out.println("PackageImport" +
				// packImp.getQualifiedName());

			} else if (o instanceof ProfileApplication) {
				ProfileApplication profApp = (ProfileApplication) o;
				// Not really very interesting
				// System.out.println("profApp" + pack.getQualifiedName());

			} else {
				//System.out.println("Something else " + o.getClass());
			}
		}
	}

	/** 
	 * See if this is a known primitive. We may need to lower case the first letter?
	 * 
	 * @param name
	 * @return
	 */
	private String  checkPrimitives(String name) {
		IWorkbenchProfile profile = TigerstripeCore.getWorkbenchProfileSession().getActiveProfile();
		Collection<IPrimitiveTypeDef> typeDefs = profile.getPrimitiveTypeDefs(true);
		for (IPrimitiveTypeDef def : typeDefs){
			
			// Special handling for UML built ins
			if (name.equals("Integer")){
				return "int";
			}
			if (name.equals("String")){
				// This is a bit of a dodgy maneovre
				return "primitive.string";
			}
			if (name.equals("Boolean")){
				return "boolean";
			}
			if (name.equals("UnlimitedNatural")){
				return "double";
			}
			
			String pack = def.getPackageName();
			if (pack.equals(IPrimitiveTypeArtifact.RESERVED)){
				pack = "";
			} else {
				pack = pack+".";
			}
			
			if (def.getName().equals(name)){
				return pack+name;
			}
			if (def.getName().equals(unCapitalize(name))){
				return pack+unCapitalize(name);
			}
		}
		return "";
	}
	
	private boolean checkReservedPrimitive(String name) {

		String[][] res = IPrimitiveTypeArtifact.reservedPrimitiveTypes;
		for (int p = 0; p < res.length; p++) {
			String resName = res[p][0];
			if (resName.equals(name)) {
				return true;
			}
		}
		return false;
	}

	private String unCapitalize(String in){
		String out = in.substring(0,1).toLowerCase();
		out = out+in.substring(1);
		if (! out.equals(in)){
			this.out.println("Warning :  primitiveType '"+in+"' mapped to '"+out+"'");
		}
		return out;
	}


	public Map<EObject, String> getClassMap() {
		return classMap;
	}

	private void getCommentProcessor(){

		try {
			// Get any implementations of the ICommentProcessor from the extension
			// point
			IConfigurationElement[] elements = Platform
			.getExtensionRegistry()
			.getConfigurationElementsFor(
					"org.eclipse.tigerstripe.workbench.ui.UML2Import.umlImportCommentProcessor");
			for (IConfigurationElement element : elements) {
				final ICommentProcessor processor = (ICommentProcessor) element
				.createExecutableExtension("processor_class");
				String processorName = element.getAttribute("name");
				String processorText = "INFO : Comments Processed using processor "
					+ processorName + " extension";
				out.println(processorText);
///
				SafeRunner.run(new ISafeRunnable() {
					public void handleException(Throwable exception) {
						EclipsePlugin.log(exception);
						out
						.println("ERROR  : GETTING MAPPING with extension Point threw exception.");
						exception.printStackTrace(out);
					}

					public void run() throws Exception {
						out.println("INFO : GETTING MAPPINGS");
						// just try it out first!
						processor.processString("Dummy");
						commentProcessor = processor;
					}

				});
			}

		} catch (Exception e) {
			// If we didn't find a good one, we will use a default,
			// so just carry on
			this.commentProcessor = null;
		}
	}
	
}
