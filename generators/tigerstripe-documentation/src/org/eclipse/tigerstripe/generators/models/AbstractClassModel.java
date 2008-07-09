/******************************************************************************* 
 * 
 * Copyright (c) 2008 Cisco Systems, Inc. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 *    Cisco Systems, Inc. - dkeysell
********************************************************************************/

package org.eclipse.tigerstripe.generators.models;


import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactModel;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.IProjectDescriptor;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IQueryRelationshipsByArtifact;
import org.apache.commons.lang.StringUtils;

public class AbstractClassModel extends AbstractComponentModel implements IArtifactModel{
	
	private IAbstractArtifact artifact;
	private Collection associationEnds;
	private Collection dependencies;
	

	
	
	public AbstractClassModel(){
		super();
	}
	
	
	public AbstractClassModel(IAbstractArtifact artifact){
		build(artifact);
	}
	
	public void setIArtifact(IAbstractArtifact artifact ){
			build(artifact);
	}

	public void setPluginConfig(IPluginConfig newPluginRef) {
			super.setPluginRef(newPluginRef);
			
	}

	public void setPluginRef(IPluginConfig newPluginRef) {
			super.setPluginRef(newPluginRef);
			
	}

	protected void build(IAbstractArtifact artifact ){
		super.build(artifact);
		this.artifact = (IAbstractArtifact) artifact;
		setRelationEnds();
		
	}
	
	/**
	 * 	Returns the IArtifact used to build this Model
	 * 
	 * @return IArtifact - the artifact used to build this model
	 */
	public IAbstractArtifact getArtifact(){
		return (IAbstractArtifact) this.artifact;
	}
	
	public String getArtifactType(){
		String in = this.artifact.getArtifactType();
		return StringUtils.substringAfterLast(in, ".");	
	}
	
	public String getPackage(){
		return getArtifact().getPackage();
	}
	
	
	public String getArtifactName(){
		return getArtifact().getName();
	}
	
	/**
	 * 	Returns the path which is the package name transformed into a path description.
	 * 
	 * @return String - the path to be used
	 */
	public String getOutPath(){
		return getPackage().replace(".", "/");
	}

	
	/** 
	 * add getAssociationEnds
	 * these are the ends of any association that "points" 
	 * at this artifact 
	 * 
	 * 
	 * 
	 */
	public void setRelationEnds(){
		try {    		
			ArrayList assEnds = new ArrayList();
			ArrayList deps = new ArrayList();
			ITigerstripeModelProject project = artifact.getTigerstripeProject();
			// Legacy stuff ha sno project
			if (project != null){
				IArtifactManagerSession session = project.getArtifactManagerSession();    		
				IQueryRelationshipsByArtifact outQuery = (IQueryRelationshipsByArtifact) session.makeQuery(IQueryRelationshipsByArtifact.class.getName());
				outQuery.setIncludeDependencies(true);
				outQuery.setOriginatingFrom(artifact.getFullyQualifiedName());
				// OUTS
				Collection outRefs = session.queryArtifact(outQuery);

				// Each return is actually an Association Artifact, so get the "end" that points to us.
				// We're looking at out goings, so the Aend should be us!
				//System.out.println("Outgoing refs for "+artifact.getName() +" "+outRefs.size());
				for (Object ref : outRefs){
					if (ref instanceof IAssociationArtifact){
							AssociationEndFacade aef = new AssociationEndFacade(((IAssociationArtifact)ref).getAEnd());
							aef.setParentModel(ModelFactory.getInstance().getModel((IAssociationArtifact) ref));
							assEnds.add(aef );
						
					} else if (ref instanceof IDependencyArtifact){						
							deps.add((IDependencyArtifact)ref);
					}
				}

				IQueryRelationshipsByArtifact inQuery = (IQueryRelationshipsByArtifact) session.makeQuery(IQueryRelationshipsByArtifact.class.getName());
				inQuery.setTerminatingIn(artifact.getFullyQualifiedName());
				inQuery.setIncludeDependencies(true);
				Collection inRefs = session.queryArtifact(inQuery);

				// Each return is actually an Association Artifact, so get the "end" that points to us.
				for (Object ref : inRefs){
					if (ref instanceof IAssociationArtifact){
							AssociationEndFacade aef = new AssociationEndFacade(((IAssociationArtifact)ref).getZEnd());
							aef.setParentModel(ModelFactory.getInstance().getModel((IAssociationArtifact) ref));
							assEnds.add(aef );
					} 
				}
			}
    		associationEnds = assEnds;
    		dependencies = deps;
   			return ;
    		
		} catch (TigerstripeException t){
			t.printStackTrace();
			return;
		}
	}
	
	
	
	/**
	 * Returns the fully qualified name for this interface model
	 * 
	 * @return String - the fully qualified name for this interface model
	 */
	public String getFullyQualifiedName(){
		return this.artifact.getFullyQualifiedName();
	}
	
	/**
	 * Returns the generalization of this artifact.
	 * @return
	 */
	public AbstractClassModel getGeneralization(){
		
		if (this.artifact.getExtendedArtifact() != null){
			AbstractClassModel acm = ModelFactory.getInstance().getModel(this.artifact.getExtendedArtifact());
			acm.setPluginRef(this.getPluginRef());
			return acm;
		} else {
			return null; 
		}		
	}
	
	public Collection<AbstractClassModel> getAllGeneralizations(){
		Collection<IAbstractArtifact> ancestors = getArtifact().getAncestors();
		Collection<AbstractClassModel> gens = new ArrayList<AbstractClassModel>();
		for (IAbstractArtifact ancestor : ancestors){
			AbstractClassModel model = ModelFactory.getInstance().getModel(ancestor);
			model.setPluginRef(this.getPluginRef());
			gens.add(model);
		}
		return gens;	
	}
	
	/**
	 *  
	 *  Get a collection of every class that
	 *  Extends this artifact (ie the opposite of Generalizations!) 
	 *  
	 */
	public Collection getRealizations(){
		Collection<IAbstractArtifact> realizations =  getArtifact().getExtendingArtifacts();
		
		Collection<IAbstractArtifact> implemented = getArtifact().getImplementingArtifacts();
		Collection<AbstractClassModel> reals = new ArrayList<AbstractClassModel>();
		
		for (IAbstractArtifact realization : realizations){
			AbstractClassModel model = ModelFactory.getInstance().getModel(realization);
			model.setPluginRef(this.getPluginRef());
			reals.add(model);
		}
		
		//AbstractClassModel[] reals = new AbstractClassModel[implemented.length];
		
		for (IAbstractArtifact implement : implemented){
			AbstractClassModel model = ModelFactory.getInstance().getModel(implement);
			model.setPluginRef(this.getPluginRef());
			//int n = realizations.length + i;
			reals.add(model);
		}
		return reals;	
	
	}

	/**
	 *  
	 *  Get a collection of every class that this artifact
	 *  "exposes"
	 */
	public Collection getImplements(){	
		Collection<IAbstractArtifact> implemented = getArtifact().getImplementedArtifacts();
		Collection<AbstractClassModel> impls = new ArrayList<AbstractClassModel>();
		
		for (IAbstractArtifact implement : implemented){
			AbstractClassModel model = ModelFactory.getInstance().getModel(implement);
			model.setPluginRef(this.getPluginRef());
			//int n = realizations.length + i;
			impls.add(model);
		}
		return impls;	
	
	}

	
	/** 
	 * rename & recast for getIextFields 
	 * 
	 */
	public Collection getAttributes(){
		Collection<FieldFacade> facades = new ArrayList<FieldFacade>();
		for (IField field : getArtifact().getFields(true)){

			FieldFacade facade = new FieldFacade( field);
			facade.setPluginRef(this.getPluginRef());
			facade.setParentModel(this);
			facades.add(facade);
		}
		return facades;
	}
	
	public Collection getAttributes(boolean boo){
		Collection attributes = new ArrayList();
		attributes.addAll(getAttributes());
        if (boo)
        	attributes.addAll(getInheritedAttributes());
        return attributes;
        
	}

	/** 
	 * rename & recast for getInheritedIextFields 
	 * 
	 */
	public Collection getInheritedAttributes(){
		Collection<FieldFacade> facades = new ArrayList<FieldFacade>();
		for (IField field : getArtifact().getInheritedFields(true)){

			FieldFacade facade = new FieldFacade( field);
			facade.setPluginRef(this.getPluginRef());
			facade.setParentModel(this);
			facades.add(facade);
		}
		return facades;
	}
	
	public Collection getLiterals(boolean boo){
		Collection attributes = new ArrayList();
		attributes.addAll(getLiterals());
        if (boo)
        	attributes.addAll(getInheritedLiterals());
        return attributes; 
	}
	
	public Collection getLiterals(){
		Collection<LabelFacade> facades = new ArrayList<LabelFacade>();
		for (ILiteral literal : getArtifact().getLiterals(true)){

			LabelFacade facade = new LabelFacade(literal);
			facade.setPluginRef(this.getPluginRef());
			facade.setParentModel(this);
			facades.add(facade);
		}
		return facades;
	}
	
	public Collection getInheritedLiterals(){
		Collection<LabelFacade> facades = new ArrayList<LabelFacade>();
		for (ILiteral literal : getArtifact().getInheritedLiterals(true)){

			LabelFacade facade = new LabelFacade(literal);
			facade.setPluginRef(this.getPluginRef());
			facade.setParentModel(this);
			facades.add(facade);
		}
		return facades;
	}


	/** 
	 * rename & recast for getIextMethods 
	 * 
	 */
	public Collection getOperations(){
		Collection<MethodFacade> facades = new ArrayList<MethodFacade>();
		for (IMethod method : getArtifact().getMethods(true)){

			MethodFacade facade = new MethodFacade( method);
			facade.setPluginRef(this.getPluginRef());
			facade.setParentModel(this);
			facades.add(facade);
		}
		return facades;
	}
	
	/** 
	 * rename & recast for getInheritedIextMethods 
	 * 
	 */
	public Collection getInheritedOperations(){
		Collection<MethodFacade> facades = new ArrayList<MethodFacade>();
		for (IMethod method : getArtifact().getInheritedMethods(true)){

			MethodFacade facade = new MethodFacade( method);
			facade.setPluginRef(this.getPluginRef());
			facade.setParentModel(this);
			facades.add(facade);
		}
		return facades;
	}
		
	public Collection getOperations(boolean boo){
		Collection operations = new ArrayList();
		operations.addAll(getOperations());
		if (boo)
			operations.addAll(getInheritedOperations());
		return operations;
	}
	
	/** 
	 * add getAssociationEnds
	 * these are the ends of any association that "points" 
	 * at this artifact 
	 * 
	 */
	public Collection getAssociationEnds(){
		return this.associationEnds;
	
	}
	
	public Collection getNavigableConnectingEnds(){
		ArrayList navigableEnds = new ArrayList();
		for (Object e : getAssociationEnds()){
			AssociationEndFacade end = (AssociationEndFacade) e;
			if (end.getOtherEnd().isNavigable()){
				navigableEnds.add(end.getOtherEnd());
			}
		}
		return navigableEnds;
	}
	
	
	public Collection getDependencies(){
		return this.dependencies;
	}
   
	
	public boolean isAbstract(){
		return this.getArtifact().isAbstract();
	}
		
		

	/**
	 * @return the pluginRef
	 */
	public IPluginConfig getPluginRef() {
		return pluginRef;
	}
	
    
    public boolean isDataType(){
    	if (this instanceof DatatypeModel){
    		return true;
    	}
    	if (this.artifact != null ){
    		if (this.artifact.getPackage().equals("primitive") ){
    			return true;
    		} else {
    			return false;
    		}
    	}
    	return true;
    	
    }
    
    public boolean isException(){
    	if (this instanceof ExceptionModel){
    		return true;
    	}
    	return false;
    }
    
    public boolean isManagedEntity(){
    	if (this instanceof ManagedEntityModel){
    		return true;
    	}
    	return false;
    }
    
    public boolean isDependency(){
    	if (this instanceof DependencyModel){
    		return true;
    	}
    	return false;
    }
    
    public boolean isEnumeration(){
    	if (this instanceof EnumerationModel){
    		return true;
    	}
    	return false;
    }
    
    public boolean isQuery(){
    	if (this instanceof QueryModel){
    		return true;
    	}
    	return false;
    }
    
    public boolean isUpdate(){
    	if (this instanceof UpdateProcedureModel){
    		return true;
    	}
    	return false;
    }
    
    public boolean isNotification(){
    	if (this instanceof NotificationModel){
    		return true;
    	}
    	return false;
    }
    
    public boolean isAssociation(){
		if (this instanceof AssociationClassModel || this instanceof AssociationModel)
			return true;
		else
			return false;
	}
	
	public boolean isAssociationClass(){
		if (this instanceof AssociationClassModel)
			return true;
		else
			return false;
	}
    
	public boolean isInterface(){
		if (this instanceof SessionModel)
			return true;
		else
			return false;
	}
	
    
    public boolean hasExtends(){
    	return this.artifact.hasExtends();
    }
    
    public ITigerstripeModelProject  getProject(){
    	ITigerstripeModelProject pd =null;
    	try{
    	pd= this.artifact.getProject();
    	}
    	catch (TigerstripeException t){   
    		PluginLog.logError("Failure to find project.");
    	}
    	return pd;
    }
    
}
