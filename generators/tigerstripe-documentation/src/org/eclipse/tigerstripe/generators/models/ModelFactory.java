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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;

import org.eclipse.tigerstripe.generators.utils.DocUtils;

public class ModelFactory{

	private static ModelFactory instance;
	
	/**
	 * A Private Map that keeps track of all of the instances created
	 */
	private Map<String, AbstractClassModel> modelMap = new HashMap<String, AbstractClassModel>();

	private ModelFactory() {
	}

	public static ModelFactory getInstance() {
		if (instance == null) {
			instance = new ModelFactory();
		}
		return instance;
	}

   public AbstractClassModel getModel(IAbstractArtifact artifact){
	   
	   /** See if it already exists */
	   if (modelMap.containsKey(artifact.getFullyQualifiedName())){
		   return modelMap.get(artifact.getFullyQualifiedName());
	   }
	   // Else create a new one
	   AbstractClassModel newModel;
	   if (artifact instanceof IAssociationClassArtifact){
		   newModel = new AssociationClassModel(artifact);
	   } else if (artifact instanceof IAssociationArtifact){
		   newModel = new AssociationModel(artifact);
	   } else if (artifact instanceof IDatatypeArtifact){
		   newModel = new DatatypeModel(artifact);
	   } else if (artifact instanceof IManagedEntityArtifact){
		   newModel = new ManagedEntityModel(artifact);
	   } else if (artifact instanceof IEnumArtifact){
		   newModel = new EnumerationModel(artifact);
	   }else if (artifact instanceof ISessionArtifact){
		   newModel = new SessionModel(artifact);
	   }else if (artifact instanceof IExceptionArtifact){
		   newModel = new ExceptionModel(artifact);
	   }else if (artifact instanceof IDependencyArtifact){
		   newModel = new DependencyModel(artifact);
	   }else if (artifact instanceof IQueryArtifact){
		   newModel = new QueryModel(artifact);
	   }else if (artifact instanceof IUpdateProcedureArtifact){
		   newModel = new UpdateProcedureModel(artifact);
	   }else if (artifact instanceof IEventArtifact){
		   newModel = new NotificationModel(artifact);
	   } else {
		   PluginLog.logError("ModelFactory : Unknown artifact type for "+artifact.getFullyQualifiedName());
		   newModel = null;
	   }
	   modelMap.put(artifact.getFullyQualifiedName(), newModel);
	   return newModel;
   }   

   /**
    * Special case for creating an exceptionModel from anIextException (which is not an artifact)
    * 
    * @param exception
    * @return
    */
   public AbstractClassModel getModel(IException exception){
	   
	   /** See if it already exists */
	   if (modelMap.containsKey(exception.getFullyQualifiedName())){
		   return modelMap.get(exception.getFullyQualifiedName());
	   }
	       IArtifactManagerSession managerSession = DocUtils.getManagerSession();
	       if (managerSession != null){
	    	   IExceptionArtifact artifact = (IExceptionArtifact) managerSession.getArtifactByFullyQualifiedName(exception.getFullyQualifiedName(),true);
	    	   if (artifact != null){
	    		   ExceptionModel newModel = new ExceptionModel(artifact);
	    		   modelMap.put(artifact.getFullyQualifiedName(), newModel);
	    		   return newModel;
	    	   }
	       }
	       // This will return null if there is not an artifact for this exception
	       PluginLog.logWarning("ModelFactory : No artifact type for exception"+exception.getFullyQualifiedName());
	       return null;
		   
   }
}

