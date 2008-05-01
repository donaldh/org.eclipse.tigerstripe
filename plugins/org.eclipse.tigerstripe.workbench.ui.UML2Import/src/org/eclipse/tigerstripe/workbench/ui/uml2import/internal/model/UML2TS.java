package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EChangeableEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjEnumSpecifics;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
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
import org.eclipse.uml2.uml.LiteralSpecification;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.ProfileApplication;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.VisibilityKind;
import org.eclipse.uml2.uml.internal.impl.EnumerationLiteralImpl;

public class UML2TS {

	private Map<Classifier, String> classMap;
	
	private String PRIMITIVE_PREFIX = "primitive.";

	private String DEFAULT_MULTIPLICITY = "1";

	private int MESSAGE_LEVEL = 3;

	private IArtifactManagerSession mgrSession;

	private PrintWriter out;

	private String modelLibrary;

	private MessageList messages;

	private IWorkbenchProfileSession profileSession;
	
	private int nullClassCounter = 0;

	private String typeDefAnnotation = "TypeDefinition";
	private String exceptionAnnotation = "Exception";
	
	/** constructor */
	public UML2TS(Map<Classifier, String> classMap) {
		this.classMap = classMap;
		this.profileSession = TigerstripeCore.getWorkbenchProfileSession();
	}

	/**
	 * 	 * Pull all of the "candidate artifacts" from the model
	 * 
	 */ 
	public Map<String,IAbstractArtifact> extractArtifacts(Model model, String modelLibrary, PrintWriter out, MessageList messages,
			ITigerstripeModelProject tsProject){
		
		System.out.println("Extracting");
		// This is where we store the extracted stuff..
		Map<String,IAbstractArtifact> extractedArtifacts = new HashMap<String, IAbstractArtifact>();
		this.out = out;;
		out.flush();
		this.messages = messages;
		this.modelLibrary = modelLibrary;
		
		// Walk the model
		TreeIterator t = model.eAllContents();
		try {
			this.mgrSession = tsProject.getArtifactManagerSession();
			t = model.eAllContents();
			while (t.hasNext()) {
				EObject o = (EObject) t.next();
				if (classMap.containsKey(o) && classMap.get(o) != ""){
					IAbstractArtifact thisOne = mapToArtifact(o, classMap.get(o));
					if (thisOne != null ){
						extractedArtifacts.put(thisOne.getFullyQualifiedName(), thisOne);
					} else {
						out.println("Null returned - probs not an artifact!");
					}
				}
			}
			
			// Build dependencies
			t = model.eAllContents();

			out.println("Post-Processing on Dependencies");
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
									String supplierFQN = convertToFQN(supplier.getQualifiedName());
									IAbstractArtifact supplierArtifact = extractedArtifacts.get(supplierFQN);
									if (supplierArtifact != null){
										suppliers.add(supplierArtifact);
									} else {
										this.out.println("Failed to find supplier for implementation "+supplierFQN);
									}
								}
							}
							
							// Find our client
							String clientFQN = convertToFQN(client.getQualifiedName());
							IAbstractArtifact clientArtifact = extractedArtifacts.get(clientFQN);
							if (clientArtifact != null){
								if (clientArtifact instanceof IManagedEntityArtifact){
									IManagedEntityArtifact clientEntity = (IManagedEntityArtifact) clientArtifact;
									Collection<IAbstractArtifact> existing = clientEntity.getImplementedArtifacts();
									
									ArrayList<IAbstractArtifact> imp = new ArrayList<IAbstractArtifact>();
									imp.addAll(existing);
									imp.addAll(suppliers);
									
								    clientEntity.setImplementedArtifacts(imp);
								    this.out.println("Adding "+clientEntity.getImplementedArtifacts().size()+" implements relations to "+clientFQN);

								    
								} else if (clientArtifact instanceof AssociationClassArtifact){
									AssociationClassArtifact clientEntity = (AssociationClassArtifact) clientArtifact;
									Collection<IAbstractArtifact> existing = clientEntity.getImplementedArtifacts();
									
									ArrayList<IAbstractArtifact> imp = new ArrayList<IAbstractArtifact>();
									imp.addAll(existing);
									imp.addAll(suppliers);
									
								    clientEntity.setImplementedArtifacts(imp);
								    this.out.println("Adding "+clientEntity.getImplementedArtifacts().size()+" implements relations to "+clientFQN);
								} else {
									this.out.println("Client for implementation is not a Managed Entity or Association Class "+clientFQN);
								}
							} else {
								this.out.println("Failed to find client for implementation "+clientFQN);
							}
							

						} else if (depO instanceof Dependency) {
							Dependency dep = (Dependency) depO;
							String depName = dep.getName();
							this.out.println("Dep Name " + dep.getName());
							this.out.println(depO.getClass().getName());

							// Treat as a dependency artifact

							if (depName == null || depName.equals("")){
								depName = "implements";
							}

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
								IAbstractArtifact depArtifact = this.mgrSession
								.makeArtifact(IDependencyArtifact.class
										.getName());
								IDependencyArtifact dependency = (IDependencyArtifact) depArtifact;
								dependency
								.setFullyQualifiedName(convertToFQN(packageName
										+ "."
										+ client.getName()
										+ depName + supplier.getName()));
								this.out.println("ARTIFACT : "
										+ IDependencyArtifact.class.getName()
										+ " FQN "
										+ depArtifact.getFullyQualifiedName());
								IType atype = dependency.makeType();
								atype.setFullyQualifiedName(convertToFQN(client
										.getQualifiedName()));
								dependency.setAEndType(atype);

								IType ztype = dependency.makeType();
								ztype
								.setFullyQualifiedName(convertToFQN(supplier
										.getQualifiedName()));
								dependency.setZEndType(ztype);
								IStereotype tsStereo = this.profileSession.getActiveProfile()
								.getStereotypeByName("DependencyLabel");

								IStereotypeInstance depLabel = tsStereo.makeInstance();
								String labelName = "label";
								IStereotypeAttribute tsStereoAttribute = null;
								IStereotypeAttribute[] tsStereoAttributes = depLabel.getCharacterizingStereotype().getAttributes();
								for (int at = 0; at < tsStereoAttributes.length; at++) {
									if (tsStereoAttributes[at].getName().equals(labelName)) {
										tsStereoAttribute = tsStereoAttributes[at];
										break;
									}

								}

								depLabel.setAttributeValue(tsStereoAttribute, depName);
								dependency.addStereotypeInstance(depLabel);

								extractedArtifacts.put(dependency.getFullyQualifiedName(), dependency);

							}
						}
					}
				}
			}

			

			// Do a second pass to set the generalizations.
			t = model.eAllContents();

			this.out.println("Post-Processing on Generalizations");
			nullClassCounter = 0;
			while (t.hasNext()) {
				EObject o = (EObject) t.next();
				if (o instanceof Classifier) {
					Classifier element = (Classifier) o;
					String eleName = "";
					String packageName = element.getNearestPackage().getQualifiedName();
					if (element.getName() == null){
					    eleName = "element"+Integer.toString(nullClassCounter);
					    nullClassCounter++;
					} else {
						eleName = element.getName();
					}
					String myFQN = packageName+"::"+eleName;
									
					String artifactFullyQualifiedName = convertToFQN(myFQN);

					IAbstractArtifact artifact = extractedArtifacts.get(artifactFullyQualifiedName);
					if (artifact != null) {
						setGeneralization(artifact, element);
					}
				}
			}
		
		} catch (Exception e){
			// TODO something here
			out.println("oh SHIT");
			e.printStackTrace();
		} finally {
			out.flush();
			System.out.println(extractedArtifacts);
			return extractedArtifacts;
		}
	}
	
	/**
	 * Load the UML model into TS
	 * 
	 * @param model
	 * @param tSProjectName
	 */
	public void loadUMLtoTigerstripe(Model model, ITigerstripeModelProject tsProject,
			String modelLibrary, PrintWriter out, MessageList messages,
			IProgressMonitor monitor) throws TigerstripeException {
		this.out = out;
		this.modelLibrary = modelLibrary;
		this.messages = messages;
		// Make sure the TS project is OK
		try {
			this.mgrSession = tsProject.getArtifactManagerSession();

		} catch (Exception e) {
			String msgText = "Problem opening TS Project ";
			addMessage(msgText, 0);
			this.out.println("Error : " + msgText);
			e.printStackTrace(this.out);
			return;
		}

		// Walk the model
		TreeIterator t = model.eAllContents();

		int monCount = 0;
		while (t.hasNext()) {
			monCount++;
			t.next();
		}

		try {
			((ArtifactManagerSessionImpl) mgrSession)
					.setLockForGeneration(true);
			// TODO PUT THIS BACK
			//TigerstripeProjectAuditor.setTurnedOffForImport(true);

			monitor.beginTask("Processing UML Classes ", monCount);
			t = model.eAllContents();
			while (t.hasNext()) {
				EObject o = (EObject) t.next();
				/*if (o instanceof AssociationClass) {
					// Map to AssociationClass Artifact
					mapToArtifact(o, IAssociationClassArtifact.class.getName(),
							monitor);
				} else if (o instanceof Class) {
					// Map to Entity or Datatype Artifact depending
					mapToArtifact(o, IManagedEntityArtifact.class.getName(),
							monitor);
				} else if (o instanceof Association) {
					// Map to Association Artifact
					mapToArtifact(o, IAssociationArtifact.class.getName(),
							monitor);
				} else if (o instanceof Enumeration) {
					// Map to Enumeration Artifact
					mapToArtifact(o, IEnumArtifact.class.getName(), monitor);
				} else if (o instanceof Interface) {
					// Map to Interface Artifact
					mapToArtifact(o, ISessionArtifact.class.getName(), monitor);
				}*/
				monitor.worked(1);
			}


			// Build dependencies
			t = model.eAllContents();

			monitor.beginTask("Post-Processing on Dependencies", monCount);
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
									String supplierFQN = convertToFQN(supplier.getQualifiedName());
									IAbstractArtifact supplierArtifact = this.mgrSession.getArtifactByFullyQualifiedName(supplierFQN);
									if (supplierArtifact != null){
										suppliers.add(supplierArtifact);
									} else {
										this.out.println("Failed to find supplier for implementation "+supplierFQN);
									}
								}
							}

						
							
							
							
							// Find our client
							String clientFQN = convertToFQN(client.getQualifiedName());
							IAbstractArtifact clientArtifact = this.mgrSession.getArtifactByFullyQualifiedName(clientFQN);
							if (clientArtifact != null){
								if (clientArtifact instanceof ManagedEntityArtifact){
									ManagedEntityArtifact clientEntity = (ManagedEntityArtifact) clientArtifact;
									Collection<IAbstractArtifact> existing = clientEntity.getImplementedArtifacts();
									
									ArrayList<IAbstractArtifact> imp = new ArrayList<IAbstractArtifact>();
									imp.addAll(existing);
									imp.addAll(suppliers);
									
								    clientEntity.setImplementedArtifacts(imp);
								    this.out.println("Adding "+clientEntity.getImplementedArtifacts().size()+" implements relations to "+clientFQN);

								    
								} else if (clientArtifact instanceof AssociationClassArtifact){
									AssociationClassArtifact clientEntity = (AssociationClassArtifact) clientArtifact;
									Collection<IAbstractArtifact> existing = clientEntity.getImplementedArtifacts();
									
									ArrayList<IAbstractArtifact> imp = new ArrayList<IAbstractArtifact>();
									imp.addAll(existing);
									imp.addAll(suppliers);
									
								    clientEntity.setImplementedArtifacts(imp);
								    this.out.println("Adding "+clientEntity.getImplementedArtifacts().size()+" implements relations to "+clientFQN);
								} else {
									this.out.println("Client for implementation is not a Managed Entity or Association Class "+clientFQN);
								}
							} else {
								this.out.println("Failed to find client for implementation "+clientFQN);
							}
							

						} else if (depO instanceof Dependency) {
							Dependency dep = (Dependency) depO;
							String depName = dep.getName();
							this.out.println("Dep Name " + dep.getName());
							this.out.println(depO.getClass().getName());

							// Treat as a dependency artifact

							if (depName == null || depName.equals("")){
								depName = "implements";
							}

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
								IAbstractArtifact depArtifact = this.mgrSession
								.makeArtifact(IDependencyArtifact.class
										.getName());
								IDependencyArtifact dependency = (IDependencyArtifact) depArtifact;
								dependency
								.setFullyQualifiedName(convertToFQN(packageName
										+ "."
										+ client.getName()
										+ depName + supplier.getName()));
								this.out.println("ARTIFACT : "
										+ IDependencyArtifact.class.getName()
										+ " FQN "
										+ depArtifact.getFullyQualifiedName());
								IType atype = dependency.makeType();
								atype.setFullyQualifiedName(convertToFQN(client
										.getQualifiedName()));
								dependency.setAEndType(atype);

								IType ztype = dependency.makeType();
								ztype
								.setFullyQualifiedName(convertToFQN(supplier
										.getQualifiedName()));
								dependency.setZEndType(ztype);
								IStereotype tsStereo = this.profileSession.getActiveProfile()
								.getStereotypeByName("DependencyLabel");

								IStereotypeInstance depLabel = tsStereo.makeInstance();
								String labelName = "label";
								IStereotypeAttribute tsStereoAttribute = null;
								IStereotypeAttribute[] tsStereoAttributes = depLabel.getCharacterizingStereotype().getAttributes();
								for (int at = 0; at < tsStereoAttributes.length; at++) {
									if (tsStereoAttributes[at].getName().equals(labelName)) {
										tsStereoAttribute = tsStereoAttributes[at];
										break;
									}

								}

								depLabel.setAttributeValue(tsStereoAttribute, depName);
								dependency.addStereotypeInstance(depLabel);

								this.mgrSession.addArtifact(dependency);
								dependency.doSilentSave(new NullProgressMonitor());


							}
						}
					}
				}
				monitor.worked(1);
			}

			

			// Do a second pass to set the generalizations.
			t = model.eAllContents();

			monitor.beginTask("Post-Processing on Generalizations", monCount);
			nullClassCounter = 0;
			while (t.hasNext()) {
				EObject o = (EObject) t.next();
				if (o instanceof Classifier) {
					Classifier element = (Classifier) o;
					String eleName = "";
					String packageName = element.getNearestPackage().getQualifiedName();
					if (element.getName() == null){
					    eleName = "element"+Integer.toString(nullClassCounter);
					    nullClassCounter++;
					} else {
						eleName = element.getName();
					}
					String myFQN = packageName+"::"+eleName;
					
					
					
					String artifactFullyQualifiedName = convertToFQN(myFQN);
					// remove the model name
//					artifactFullyQualifiedName = artifactFullyQualifiedName
//							.substring(artifactFullyQualifiedName.indexOf(".") + 1);
					IAbstractArtifact artifact = this.mgrSession
							.getArtifactByFullyQualifiedName(artifactFullyQualifiedName);
					if (artifact != null) {
						setGeneralization(artifact, element);
						
						artifact.doSilentSave(new NullProgressMonitor());
					}
				}
				monitor.worked(1);
			}
			monitor.done();
			
		} finally {
			((ArtifactManagerSessionImpl) mgrSession)
					.setLockForGeneration(false);

			mgrSession.refresh(true, monitor);
		}

		final String pName= tsProject.getProjectDetails().getName();

		IWorkspaceRunnable op = new IWorkspaceRunnable() {

			public void run(IProgressMonitor monitor) throws CoreException {
				try {
					IResource resource = ResourcesPlugin.getWorkspace()
							.getRoot().findMember(new Path(pName));
					resource.refreshLocal(IResource.DEPTH_INFINITE, null);
				}  finally {
					monitor.done();
				}
			}
		};

		try {
			ResourcesPlugin.getWorkspace().run(op, monitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private IAbstractArtifact mapToArtifact(EObject o, String artifactTypeName) {
		NamedElement element = (NamedElement) o;
		String eleName;
		boolean hasTempName = false;
		IAbstractArtifact artifact = readArtifactStereotypes(element,
				artifactTypeName);

		if (artifact == null) {
			String msgText = "Failed to extract class to artifact:"
				+ artifact.getName();
			addMessage(msgText, 0);
			return null;
		}
		if (element.getName() == null){
			String msText = "UML Class with null name - defaulting";
			addMessage(msText, 0);
			this.out.println("Error :" + msText);
			eleName = "element"+Integer.toString(nullClassCounter);
			nullClassCounter++;
			hasTempName = true;
		} else {
			eleName = element.getName();
			hasTempName = false;
		}
		String msText = "Processing UML Class : " + eleName;
		this.out.println("Info :" + msText);
		// Some EObjects could be of Type "NestedClassifier" which we can't
		// handle in our model.
		// Not sure how to detect these..this is a bit "hacky"
		String packageName = element.getNearestPackage().getQualifiedName();

		msText = "packageName :" + packageName;
		this.out.println("Info :" + msText);

		String myFQN = "";
		if (element.getQualifiedName() != null){
			String elementName = element.getQualifiedName();
			myFQN = elementName;
			msText = "elementName :" + elementName;
			this.out.println("Info :" + msText);

			elementName = elementName.substring(packageName.length() + 2);

			if (elementName.contains("::")) {
				// some elements left in name - ie not child of the package
				String msgText = "Nested Classifier is not supported in TS : "
					+ eleName;
				addMessage(msgText, 0);
				this.out.println("Error :" + msgText);
				return null;
			}
		} else {

			msText = "elementName :" + eleName;
			this.out.println("Info :" + msText);
			myFQN = packageName+"::"+eleName;
		}



		String artifactFullyQualifiedName = convertToFQN(myFQN);

		this.out.println("ARTIFACT : " + artifactTypeName + " FQN "
				+ artifactFullyQualifiedName);
		artifact.setFullyQualifiedName(artifactFullyQualifiedName);

		artifact.setComment(setComment(element));

		// Do Constants
		// shouldn't see any in model - only Enums
		setConstants(artifact, element);

		setAttributes(artifact, element);

		// Do Operations (check the model - shouldn't have any of these on
		// non-interfaces)
		setOperations(artifact, element);

		// Specifics for various artifact types...
	
		//TODO
		
		// Enums need base type - cheat and look at the first entry...
		if (false) {
			if (artifact instanceof IEnumArtifact) {
				IEnumArtifact enumArt = (IEnumArtifact) artifact;
				Collection<ILiteral> labels = enumArt.getLiterals();
				IType type;
				if (labels.size() > 0) {
					type = ((ILiteral) labels.iterator().next() ).getType();
				} else {
					// Make it String by default
					type = artifact.makeLiteral().makeType();
					type.setFullyQualifiedName("String");

				}
				IOssjEnumSpecifics enumSpecs = (IOssjEnumSpecifics) enumArt
				.getIStandardSpecifics();
				enumSpecs.setBaseIType(type);
			}
		}

		if (true) {
			// they should all be int...
			if (artifact instanceof IEnumArtifact) {
				IEnumArtifact enumArt = (IEnumArtifact) artifact;
				IType type = artifact.makeLiteral().makeType();
				type.setFullyQualifiedName("int");
				IOssjEnumSpecifics enumSpecs = (IOssjEnumSpecifics) enumArt
				.getIStandardSpecifics();
				enumSpecs.setBaseIType(type);
			}
		}

			try {
				if (artifact instanceof IAssociationArtifact) {
					IAssociationArtifact assoc = (IAssociationArtifact) artifact;
					setAssociationEnds(assoc, element);
					if (hasTempName){
						// the make a new name based on the names of the classes on the ends
						String aEndClassName = assoc.getAEnd().getType().getName();
						String bEndClassName = assoc.getZEnd().getType().getName();

						String newName = aEndClassName+"To"+bEndClassName;
						assoc.setFullyQualifiedName(assoc.getPackage()+"."+newName);

					}
				}

				artifact.setAbstract(((Classifier) element).isAbstract());
				return artifact;

			} catch (Exception e) {
				String msgText = "Failed to extract class to artifact:"
					+ artifact.getName();
				addMessage(msgText, 0);
				this.out.println("Error : " + msgText);
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
				addMessage(msgText, 0);
				this.out.println("Error : " + msgText);
			}
			ListIterator mEndTypesIt = mEndTypes.listIterator();
			boolean first = true;
			while (mEndTypesIt.hasNext()) {

				Property mEnd = (Property) mEndTypesIt.next();
				this.out.println("   End " + mEnd.getName() + " "
						+ mEnd.getType().getName());
				IAssociationEnd end = assocArtifact.makeAssociationEnd();

				end.setName(nameCheck(mEnd.getName()));
				IType type = assocArtifact.makeField().makeType();

				if (!setTypeDetails(type, mEnd.getType(), mEnd, assocArtifact
						.getName()
						+ " : " + mEnd.getName(), true)) {
					continue;
					// This will go wrong as the Association needs to have a
					// type at both ends...
				}
				end.setType(type);

				if (nameCheck(mEnd.getName()) != null) {
					end.setName(nameCheck(mEnd.getName()));
				} else {
					String msgText = "Association End has no name : "
							+ assocArtifact.getName();
					addMessage(msgText, 1);
					this.out.println("Warning : " + msgText);
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

				end.setComment(setComment(mEnd));
				
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
			// Swap the aggregation  over....!!!!!!!!!!!!!

			EAggregationEnum temp = assocArtifact.getAEnd().getAggregation();
		
			((IAssociationEnd) (assocArtifact.getAEnd())).setAggregation(assocArtifact.getZEnd().getAggregation());
			((IAssociationEnd) assocArtifact.getZEnd()).setAggregation(temp);

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
					String genName = convertToFQN(gen.getQualifiedName());
					this.out.println("    " + artifact.getName() + " Generalization "
							+ genName);

					IAbstractArtifact genArtifact = this.mgrSession
					                .getArtifactByFullyQualifiedName(genName);
					if (genArtifact == null) {
						String msgText = "Failed to retreive generalization for Artifact : "
							+ artifact.getName();
						addMessage(msgText, 0);
						this.out.println("Error : " + msgText);
					} else {
						artifact.setExtendedArtifact(genArtifact);
					}
				}
			} catch (Exception e) {
				String msgText = "Failed to retreive generalization for Artifact : "
					+ artifact.getName();
				addMessage(msgText, 0);
				this.out.println("Error : " + msgText);
				e.printStackTrace(this.out);
				return;
			}
		}


	}

	private void setConstants(IAbstractArtifact artifact, NamedElement element) {
		// In UML model should only be on Enums ?
		// How does that square with Tigerstripe ?
	
		String cType = getConstantsType(artifact, element);

		List children = element.getOwnedElements();

		ListIterator it = children.listIterator();
		while (it.hasNext()) {
			EObject child = (EObject) it.next();
			if (child instanceof EnumerationLiteral) {
				EnumerationLiteral enumLit = (EnumerationLiteral) child;
				// Might have an EnumValue stereotype ?
				System.out.println("    Enum Literal : " + enumLit.getName());

				ILiteral iLiteral = artifact.makeLiteral();
				String eName = nameCheck(enumLit.getName());
				if (eName.substring(0, 1).matches("[0-9]")){
					eName = "_"+eName;
					this.out.println("    Name prepended : " + eName);
				}
				iLiteral.setName(eName);
				IType type = iLiteral.makeType();
				type.setFullyQualifiedName(cType);
				iLiteral.setType(type);
				artifact.addLiteral(iLiteral);

				// If String set the label to the name by default - may be
				// overridden by an EnumVal
				// otherwise if int set it to -1
				System.out.println(cType);
				if (cType.equals("String")) {
					iLiteral.setValue("\"" + iLiteral.getName() + "\"");
				} else {
					iLiteral.setValue(Integer.toString(-1));
				}

				List appliedStereotypes = ((NamedElement) child)
						.getAppliedStereotypes();
				if (appliedStereotypes.size() > 0) {

					int count = 0;
					for (int s = 0; s < appliedStereotypes.size(); s++) {
						Stereotype stereo = (Stereotype) appliedStereotypes
						.get(s);
						
						if (!stereo.getName().equals("EnumValue")){
							IStereotype tsStereo = this.profileSession.getActiveProfile()
							.getStereotypeByName(stereo.getName());
							if (tsStereo == null) {
								String msgText = "No Stereotype Named "+stereo.getName() +" in TS:"
								+ stereo.getName();
								addMessage(msgText, 0);
								this.out.println("Error : " + msgText);
								continue;
							}
							IStereotypeInstance tsStereoInstance = tsStereo.makeInstance();

							List stereoAttributes = stereo.getAllAttributes();
							for (int a = 0; a < stereoAttributes.size(); a++) {
								Property attribute = (Property) stereoAttributes
								.get(a);


								if (!attribute.getName().startsWith("base_")) {
									// if ((element.getValue(stereo,
									// attribute.getName()) != null) ){
									this.out.println("    Enum Lit Stereo Attribute : "
											+ stereo.getName()
											+ ":"
											+ attribute.getName()
											+ " -> "
											+ ((NamedElement) child).getValue(
													stereo, attribute.getName()));
									count++;
									iLiteral.setValue(((NamedElement) child)
											.getValue(stereo,
													attribute.getName())
													.toString());
									addAttributesToStereoType(tsStereoInstance, stereo,
											attribute, (NamedElement) child);
									iLiteral.addStereotypeInstance(tsStereoInstance);
									
									}

							}
						} else if (stereo.getName().equals("EnumValue")){

							List stereoAttributes = stereo.getAllAttributes();

							//this.out.println("Found EnumValue "+stereoAttributes.size());

							for (int a = 0; a < stereoAttributes.size(); a++) {
								Property attribute = (Property) stereoAttributes.get(a);
								
								if (attribute.getName().equals("value")) {
									//this.out.println("Got the Value "+((NamedElement) child).hasValue(stereo,attribute.getName()));
									if (((NamedElement) child).hasValue(stereo,attribute.getName())){
										this.out.println("    Enum Value : "
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
		// After all that we need to set the literal types, and values for any that did not get one.
		
		
		
		this.out.println("    Finished handling constants");

	}

	private String getConstantsType(IAbstractArtifact artifact,
			NamedElement element) {
		// See if anything has a Value, then setting All of them to that,
		// giving any without a Value a default value

		List children = element.getOwnedElements();
		ListIterator it = children.listIterator();
		while (it.hasNext()) {
			EObject child = (EObject) it.next();
			if (child instanceof EnumerationLiteral) {
				//EnumerationLiteral enumLit = (EnumerationLiteral) child;

				List appliedStereotypes = ((NamedElement) child)
						.getAppliedStereotypes();
				if (appliedStereotypes.size() > 0) {

					for (int s = 0; s < appliedStereotypes.size(); s++) {
						Stereotype stereo = (Stereotype) appliedStereotypes
								.get(s);
						if (stereo.getName().equals("EnumValue")){
							List stereoAttributes = stereo.getAllAttributes();
							for (int a = 0; a < stereoAttributes.size(); a++) {
								Property attribute = (Property) stereoAttributes
								.get(a);

								if (!attribute.getName().startsWith("base_")) {
									if (attribute.getType().getName().equals(
									"Integer")) {
										return "int";
									} else if (attribute.getType().getName()
											.equals("string")) {
										return "string";
									}else {
										String msgText = "Unhandled type for Enum Value "
											+ stereo.getName()
											+ ":"
											+ attribute.getName()
											+ " -> "
											+ attribute.getType().getName();
										addMessage(msgText, 0);
										this.out.println("Error : " + msgText);
										//return "string";
									}
								}
							}
						}
					}
				}
			}
		}
		String msgText = "Could not determine Enum type - Defaulting to string";
		//addMessage(msgText, 0);
		this.out.println("Info : " + msgText);
		return "string";
	}

	private void setOperations(IAbstractArtifact artifact, NamedElement element) {
		List children = element.getOwnedElements();

		ListIterator it = children.listIterator();
		while (it.hasNext()) {
			EObject child = (EObject) it.next();
			if (child instanceof Operation) {
				Operation operation = (Operation) child;

				this.out.println("    Operation :" + operation.getName());
				IMethod method = artifact.makeMethod();
				method.setName(operation.getName());
				method.setComment(setComment(((NamedElement) child)));

				// ============ return type =====================
				Parameter returnResult = operation.getReturnResult();
				if (returnResult != null){
					Type retType = returnResult.getType();

					IType iType = method.makeType();
					if (!setTypeDetails(iType, retType, returnResult, artifact
							.getName()
							+ " : " + returnResult.getName())) {
						continue;
					}
					this.out.println("    Operation return : "
							+ operation.getName() + " : "
							+ iType.getFullyQualifiedName());
					method.setReturnType(iType);
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
					
					method.setReturnName(returnResult.getName());
					this.out.println("    Operation return Name : "
							+ returnResult.getName());
				} else {
					// No return type!
					this.out.println("    Operation return : "
							+ "unknown Type");

				}
				
				method.setOrdered(operation.isOrdered());
				method.setUnique(operation.isUnique());

				
				
				// ============ parameters =====================
				BehavioralFeature bF = (BehavioralFeature) child;
				List params = bF.getOwnedParameters();
				ListIterator paramIt = params.listIterator();
				while (paramIt.hasNext()) {
					Parameter param = (Parameter) paramIt.next();

					if (param.getDirection().getValue() != ParameterDirectionKind.IN) {
						// This is the return, so ignore it.
						continue;
					}
					IArgument arg = method.makeArgument();
					arg.setName(param.getName());

					IType aType = method.makeType();
					Type pType = param.getType();
					if (!setTypeDetails(aType, pType, param, artifact.getName()
							+ " : " + param.getName())) {
						continue;
					}
					this.out.println("    Operation parameter : "
							+ operation.getName() + " : " + param.getName()
							+ " : " + aType.getFullyQualifiedName());
					arg.setType(aType);

					// TODO The model might have stereos on these. TS can't yet
					// handle
//					 ============================
					ArrayList stInstances = readStereotypes((NamedElement) param);
					ListIterator stereoIt = stInstances.listIterator();
					while (stereoIt.hasNext()) {
						IStereotypeInstance iSI = (IStereotypeInstance) stereoIt
								.next();
						arg.addStereotypeInstance(iSI);
					}
					
					
					// ============================

					// Add some comments (now that we can!)
					arg.setComment(setComment((NamedElement) param));
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
					String excepFQN = convertToFQN(((NamedElement) excep)
							.getQualifiedName());
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
			if ((uType == null) || (uType.getQualifiedName() == null)) {
				System.out.println(uType);
				System.out.println(uType.getQualifiedName());
				// TODO - Is this an error in the model?
				String msgText = "Unsure of type : " + location + " Skipping...";
				addMessage(msgText, 0);
				this.out.println("Error : " + msgText);
				return false;
			}
			// Need to determine if this is a primitive type that we know about
			String pTypeFQN = convertToFQN(uType.getQualifiedName());
			if (uType.getQualifiedName().startsWith(this.modelLibrary)) {
				if (!checkReservedPrimitive(pTypeFQN)) {
					pTypeFQN = PRIMITIVE_PREFIX + pTypeFQN;
				}
			}

			iType.setFullyQualifiedName(pTypeFQN);
			if (setMulti) {
				// optional = setMultiplicityNew(iType, param);
				iType.setTypeMultiplicity(EMultiplicity
						.parse(readMultiplicity(param)));
			}
			return true;
		} catch (Exception e){
			String msgText = "Excpetion working out type : " + location + " Skipping...";
			addMessage(msgText, 0);
			this.out.println("Error : " + msgText);
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
							.println("   Skipping property - it's as assoc. thing");
					continue;
				}

				IField field = artifact.makeField();
				field.setName(nameCheck(property.getName()));

				Type propType = property.getType();
				IType type = field.makeType();
				// if (!setTypeDetails(type, propType,
				// (MultiplicityElement) child, artifact.getName()+" : "+
				// field.getName())){
				if (!setTypeDetails(type, propType,
						(MultiplicityElement) child, artifact.getName() + " : "
								+ field.getName())) {
					continue;
				}

				field
						.setOptional(getOptional(type,
								(MultiplicityElement) child));

				this.out.println("    Property : " + property.getName() + " : "
						+ type.getFullyQualifiedName());
				field.setType(type);

				field.setComment(setComment((NamedElement) child));

				field.setOrdered(property.isOrdered());
				field.setUnique(property.isUnique());
				
				if (property.isSetDefault()){
					field.setDefaultValue(property.getDefault());
				}
				if (property.isReadOnly()){
					field.setReadOnly(property.isReadOnly());
				}
				
				/*
				 * DOn't thinnk this is valid - cf EMPTY vs NOT-PRESENT? if
				 * (multi.startsWith("0")){ field.setOptional(true); }
				 */

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

				/*
				 switch (field.getVisibility()) { 
				 case IextModelComponent.VISIBILITY_PACKAGE :
					 attribute.setVisibility(VisibilityKind.PACKAGE_LITERAL);
					 break;
				 case IextModelComponent.VISIBILITY_PRIVATE :
					 attribute.setVisibility(VisibilityKind.PRIVATE_LITERAL);
					 break;
				 case IextModelComponent.VISIBILITY_PROTECTED :
					 attribute.setVisibility(VisibilityKind.PROTECTED_LITERAL);
					 break;
				 case IextModelComponent.VISIBILITY_PUBLIC :
					 attribute.setVisibility(VisibilityKind.PUBLIC_LITERAL);
					 break; 
				 }
				
				*/
				
				
				// Other bits like RefBy, Optional, ReadOnly?
				// RefBy not to be used
				// ReadOnly is not set anywhere in the model IMHO
				// In the model there are default values for some stuff?
				// And stereotypes.....
				ArrayList stInstances = readStereotypes((NamedElement) child);

				ListIterator stereoIt = stInstances.listIterator();
				while (stereoIt.hasNext()) {
					IStereotypeInstance iSI = (IStereotypeInstance) stereoIt
							.next();
					// TODO Put this in....
					field.addStereotypeInstance(iSI);
				}

				artifact.addField(field);
			}
		}

	}

	private ArrayList readStereotypes(NamedElement element) {

		ArrayList allTSStereoInstances = new ArrayList();
		List appliedStereotypes = element.getAppliedStereotypes();
		if (appliedStereotypes.size() > 0) {
			for (int s = 0; s < appliedStereotypes.size(); s++) {
				Stereotype stereo = (Stereotype) appliedStereotypes.get(s);
				List stereoAttributes = stereo.getAllAttributes();

				IStereotype tsStereo = this.profileSession.getActiveProfile()
						.getStereotypeByName(stereo.getName());
				if (tsStereo == null) {
					String msgText = "No Stereotype Named "+stereo.getName() +" in TS:"
							+ stereo.getName();
					addMessage(msgText, 0);
					this.out.println("Error : " + msgText);
					continue;
				}
				IStereotypeInstance tsStereoInstance = tsStereo.makeInstance();

				int count = 0;
				for (int a = 0; a < stereoAttributes.size(); a++) {
					Property attribute = (Property) stereoAttributes.get(a);
					if (!attribute.getName().startsWith("base_")) {
						// if ((element.getValue(stereo, attribute.getName()) !=
						// null) ){
						
						this.out.println("    Property Stereo Attribute : "
										+ stereo.getName()
										+ ":"
										+ attribute.getName()
										+ " -> "
										+ element.getValue(stereo, attribute
												.getName()));
						count++;

						addAttributesToStereoType(tsStereoInstance, stereo,
								attribute, element);
					}
				}
				if (count == 0) {
					// We've handled no other attributes,so
					// This is an "existence" type of Stereotype
					// so it's mere presence is important
					// We have given the user a choice in the profile stuff so
					// our
					// behaviour needs to be mildly intelligent.
					this.out.println("    Property stereo Attribute : "
							+ stereo.getName() + ":Existence --> true");
				}
				allTSStereoInstances.add(tsStereoInstance);

			}
		}
		return allTSStereoInstances;
	}

	/*private void setMultiplicity(IType type, MultiplicityElement property) {
		String multi = readMultiplicity(property);
		// TODO - is there more to say here (eg re:Optional).
		if (multi.equals("0..1")) {
			type.setMultiplicity(IextType.MULTIPLICITY_SINGLE);
		} else {
			type.setMultiplicity(IextType.MULTIPLICITY_MULTI);
		}
	}

	private boolean setMultiplicityNew(IType type, MultiplicityElement property) {

		String multiplicityString = "";
		String upper = "";
		int multi = IextType.MULTIPLICITY_SINGLE;
		String lower = "";
		boolean optional = true;

		ValueSpecification upperVal = property.getUpperValue();
		ValueSpecification lowerVal = property.getLowerValue();

		if (upperVal != null) {
			upper = "" + property.getUpper();
			if (upper.equals("-1")) {
				multi = IextType.MULTIPLICITY_MULTI;
			} else {
				multi = IextType.MULTIPLICITY_SINGLE;
			}
		}

		if (lowerVal != null) {
			lower = "" + property.getLower();
			if (lower.equals("0")) {
				// Can't ever see this happening...
				optional = true;
			} else {
				optional = false;
			}
		}
		type.setMultiplicity(multi);
		return optional;
	}*/

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
				this.out.println("        Overriding upper Multiplicity from "+upper);
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
		
		this.out.println("        Multiplicity -> " + multiplicityString);
		return multiplicityString;
	}

	/**
	 * Have to do this away from other stereos as they define behaviour
	 * 
	 * @param element
	 */
	private IAbstractArtifact readArtifactStereotypes(NamedElement element,
			String artifactTypeName) {

		IAbstractArtifact absArtifact;

		boolean hasTypeDef = false;
		boolean isException = false;
		List allTSStereoInstances = new ArrayList();

		List appliedStereotypes = element.getAppliedStereotypes();
		if (appliedStereotypes.size() > 0) {
			for (int s = 0; s < appliedStereotypes.size(); s++) {
				Stereotype stereo = (Stereotype) appliedStereotypes.get(s);

				IStereotype tsStereo = this.profileSession.getActiveProfile()
						.getStereotypeByName(stereo.getName());
				if (tsStereo == null) {
					String msgText = "No Stereotype in TS with name :"
							+ stereo.getName();
					addMessage(msgText, 0);
					this.out.println("Error : " + msgText);
					continue;
				}
				IStereotypeInstance tsStereoInstance = tsStereo.makeInstance();

				List stereoAttributes = stereo.getAllAttributes();
				int count = 0;

				for (int a = 0; a < stereoAttributes.size(); a++) {
					Property attribute = (Property) stereoAttributes.get(a);
					if (!attribute.getName().startsWith("base_")) {
						// if ((element.getValue(stereo, attribute.getName()) !=
						// null) ){
						this.out
								.println("    Artifact Stereo Attribute : "
										+ stereo.getName()
										+ ":"
										+ attribute.getName()
										+ " -> "
										+ element.getValue(stereo, attribute
												.getName()));
						count++;

						// Add attributes to the instance
						addAttributesToStereoType(tsStereoInstance, stereo,
								attribute, element);
					}
				}
				if (count == 0) {
					// We've handled no other attributes,so
					// This is an "existence" type of Stereotype
					// so it's mere presence is important
					// We have given the user a choice in the profile stuff so
					// our
					// behaviour needs to be mildly intelligent.
					this.out.println("    Artifact Attribute : "
							+ stereo.getName() + ":Existence --> true");
				}
				allTSStereoInstances.add(tsStereoInstance);
			}

		}

		System.out.println("artifactTypeName "+artifactTypeName);
		try {
			absArtifact = this.mgrSession.makeArtifact(artifactTypeName);
		} catch (IllegalArgumentException t) {
			System.out.println("artifactTypeName is invalid "+artifactTypeName);
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
			Property attribute, NamedElement element) {

		IStereotypeAttribute[] tsStereoAttributes = tsStereoInstance
				.getCharacterizingStereotype().getAttributes();
		IStereotypeAttribute tsStereoAttribute = null;
		for (int at = 0; at < tsStereoAttributes.length; at++) {
			if (tsStereoAttributes[at].getName().equals(attribute.getName())) {
				tsStereoAttribute = tsStereoAttributes[at];
				break;
			}

		}
		if (tsStereoAttribute != null) {
			try {
				//this.out.println(tsStereoAttribute.isArray());
				//this.out.println(attribute.getName());
				//this.out.println(stereo.getName());
				//this.out.println(element);
				//this.out.println(element.getValue(stereo, attribute.getName()));
				if (tsStereoAttribute.isArray() && element.getValue(stereo, attribute.getName()) instanceof EList ){
					
					// Need to split the value into an array and set that.
					EList valList =  (EList) element.getValue(stereo, attribute.getName());
					String[] vals = new String[valList.size()];
					for (int i=0;i<valList.size();i++){
						vals[i] = valList.get(i).toString();
					}
					tsStereoInstance.setAttributeValues(tsStereoAttribute, vals);
				} else if (element.getValue(stereo, attribute.getName()) instanceof String){	
					tsStereoInstance.setAttributeValue(tsStereoAttribute, 
							element.getValue(stereo, attribute.getName()).toString());
				} else if (element.getValue(stereo, attribute.getName()) instanceof EnumerationLiteralImpl){
					EnumerationLiteralImpl enumImpl = (EnumerationLiteralImpl) element.getValue(stereo, attribute.getName());
					tsStereoInstance.setAttributeValue(tsStereoAttribute, 
							enumImpl.getName());
				} else {
					tsStereoInstance.setAttributeValue(tsStereoAttribute, 
							element.getValue(stereo, attribute.getName()).toString());
					out.println ("Uncertain stereo attr type "+ tsStereoInstance.getName() + ":"
							+ attribute.getName());
				}
			} catch (Exception e) {
				String msgText = "Failed to set attribute Value on Stereotype : "
						+ element.getName()
						+ " : "
						+ stereo.getName()
						+ " : "
						+ attribute.getName();
				addMessage(msgText, 0);
				this.out.println("Error : " + msgText);
				e.printStackTrace();
				return;
			}
		}

	}

	public String setComment(NamedElement element) {
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
			this.out.println("    Comment " + comment);
			return comment;
		}
		return null;
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
				System.out.println("Class" + cl.getQualifiedName());

			} else if (o instanceof AssociationClass) {
				AssociationClass assocCl = (AssociationClass) o;
				System.out.println("AssociationClass"
						+ assocCl.getQualifiedName());

			} else if (o instanceof Association) {
				Association assoc = (Association) o;
				System.out.println("Association" + assoc.getQualifiedName());

			} else if (o instanceof Enumeration) {
				Enumeration enumeration = (Enumeration) o;
				System.out.println("Enumeration"
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
				System.out.println("Something else " + o.getClass());
			}
		}
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

	/**
	 * map naming to TS compatible style
	 * 
	 */
	private String convertToFQN(String name) {
		if (name != null) {

			String dottedName = "";
			String[] segments = name.split("::");
			for (int i=0;i<segments.length-1;i++){
                // Make sure the packages all start with a lower case letter
				String segmentName =  segments[i].substring(0,1).toLowerCase()+segments[i].substring(1);
				if (! segmentName.substring(0,1).equals(segments[i].substring(0,1)) && i!=0){
					String msgText = " Package Name Segment mapped : " + segments[i] + " -> " + segmentName;
					addMessage(msgText, 1);
					this.out.println("Warning:" + msgText);
				}
				if (i==0){
					//dottedName = nameCheck(segmentName);
					dottedName = segmentName;
				} else {
					dottedName = dottedName+"."+nameCheck(segmentName);
				}
			}
			dottedName = dottedName+"."+nameCheck(segments[segments.length-1]);
			return dottedName.substring(dottedName.indexOf(".")+1);
		} else {
			return null;
		}
	}

	/**
	 * check for invalid constructs
	 * 
	 * eg "." in name - map to "_" "-" in name - map to "_" " " in name - map to
	 * "_"
	 * 
	 * 
	 * 
	 * @param name
	 * @return
	 */
	private String nameCheck(String name) {
		if (name != null && name.length()!=0) {
			String inName = name.trim();
			if (inName.contains(" ")) {
				inName = inName.replaceAll(" ", "_");
			}
			if (inName.contains("-")) {
				inName = inName.replaceAll("-", "_");
			}
			if (inName.contains(".")) {
				inName = inName.replaceAll("\\.", "_");
			}

			if (!inName.equals(name)) {
				String msgText = " Name mapped : " + name + " -> " + inName;
				addMessage(msgText, 1);
				this.out.println("Warning:" + msgText);
			}

			return inName;
		} else {
			return null;
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

	public String getExceptionAnnotation() {
		return exceptionAnnotation;
	}

	public void setExceptionAnnotation(String exceptionAnnotation) {
		this.exceptionAnnotation = exceptionAnnotation;
	}

	public String getTypeDefAnnotation() {
		return typeDefAnnotation;
	}

	public void setTypeDefAnnotation(String typeDefAnnotation) {
		this.typeDefAnnotation = typeDefAnnotation;
	}

	public Map<Classifier, String> getClassMap() {
		return classMap;
	}
}
