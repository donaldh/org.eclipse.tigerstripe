package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.MigrationHelper;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.model.IComponentNameProvider;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryArtifactsByType;

public class ComponentNameProvider implements IComponentNameProvider{

	
	private String FIELD     = "attribute";
	private String LITERAL   = "literal";
	private String METHOD    = "method";
	private String ARGUMENT  = "arg";
	private String END       = "end";
	
	private static IComponentNameProvider extension;
	private static ComponentNameProvider instance;

	private ComponentNameProvider(){
		
	}
	
	
	public static ComponentNameProvider getInstance(){
		if (instance == null){
			instance = new ComponentNameProvider();
			
			// Run any custom rules that are defined in the extension point.
			try {
				IConfigurationElement[] elements  = Platform.getExtensionRegistry()
				.getConfigurationElementsFor("org.eclipse.tigerstripe.workbench.base.customComponentNaming");
				for (IConfigurationElement element : elements){
					final IComponentNameProvider customProvider  = (IComponentNameProvider) element.createExecutableExtension("namingClass");
					extension = customProvider;
				}
			}catch (CoreException e ){
				BasePlugin.logErrorMessage("Failed to instantiate custom Naming rules");
				extension = null;
			}
			
			
			
		}
		return instance;
	}
	
	
	/**
	 * Use the factory if it exists! If not call a local version.
	 *
	 */
	public String getNewArtifactName(Class artifactClass, ITigerstripeModelProject project, String packageName) {
		
		/*
		 * If there is something provided through the extension point...
		 * call it safely.
		 */
		if (extension != null){
			String extensionName = null;
			final Class aClass = artifactClass;
			final ITigerstripeModelProject tsProject = project;
			final String pName = packageName;
			
			
			final class RunnableWithResult implements ISafeRunnable {
				private String  result;
				public void handleException(Throwable exception) {
					BasePlugin.log(exception);
				}

				public void run() throws Exception {
					  result = extension.getNewArtifactName(aClass,tsProject,pName);
				}
				
				public String getResult(){
					return this.result;
				}
				
			}
			
			RunnableWithResult myRunnable = new RunnableWithResult();
			
			SafeRunner.run(myRunnable);
			
			extensionName = myRunnable.getResult();
			
			if (extensionName != null){
				return extensionName;
			}
		}
		// Otherwise call the local (default) version
		return getLocalArtifactName(artifactClass,project,packageName);

	}

	
	/**
	 * Use the factory if it exists! If not call a local version.
	 *
	 */
	public String getNewFieldName(IAbstractArtifact artifact) {
		if (extension != null){
			String extensionName = null;
			final IAbstractArtifact aArtifact = artifact;
			
			final class RunnableWithResult implements ISafeRunnable {
				private String  result;
				public void handleException(Throwable exception) {
					BasePlugin.log(exception);
				}

				public void run() throws Exception {
					  result = extension.getNewFieldName(aArtifact);
				}
				
				public String getResult(){
					return this.result;
				}
				
			}
			
			RunnableWithResult myRunnable = new RunnableWithResult();
			
			SafeRunner.run(myRunnable);
			
			extensionName = myRunnable.getResult();

			if (extensionName != null){
				return extensionName;
			}
		}
		return getLocalFieldName(artifact);

	}

	/**
	 * Use the factory if it exists! If not call a local version.
	 *
	 */
	public String getNewLiteralName(IAbstractArtifact artifact) {
		if (extension != null){
			String extensionName = null;
			final IAbstractArtifact aArtifact = artifact;
			
			final class RunnableWithResult implements ISafeRunnable {
				private String  result;
				public void handleException(Throwable exception) {
					BasePlugin.log(exception);
				}

				public void run() throws Exception {
					  result = extension.getNewLiteralName( aArtifact);
				}
				
				public String getResult(){
					return this.result;
				}
				
			}
			
			RunnableWithResult myRunnable = new RunnableWithResult();
			
			SafeRunner.run(myRunnable);
			
			extensionName = myRunnable.getResult();

			if (extensionName != null){
				return extensionName;
			}
		}
		return getLocalLiteralName(artifact);

	}
	
	/**
	 * Use the factory if it exists! If not call a local version.
	 *
	 */
	public String getNewMethodName(IAbstractArtifact artifact) {
		if (extension != null){
			String extensionName = null;
			final IAbstractArtifact aArtifact = artifact;
			
			final class RunnableWithResult implements ISafeRunnable {
				private String  result;
				public void handleException(Throwable exception) {
					BasePlugin.log(exception);
				}

				public void run() throws Exception {
					  result = extension.getNewMethodName( aArtifact);
				}
				
				public String getResult(){
					return this.result;
				}
				
			}
			
			RunnableWithResult myRunnable = new RunnableWithResult();
			
			SafeRunner.run(myRunnable);
			
			extensionName = myRunnable.getResult();

			if (extensionName != null){
				return extensionName;
			}
		}
		return getLocalMethodName(artifact);

	}
	
	/**
	 * Use the factory if it exists! If not call a local version.
	 *
	 */
	public String getNewArgumentName(IMethod method) {
		if (extension != null){
			String extensionName = null;
			final IMethod aMethod = method;
			
			final class RunnableWithResult implements ISafeRunnable {
				private String  result;
				public void handleException(Throwable exception) {
					BasePlugin.log(exception);
				}

				public void run() throws Exception {
					  result = extension.getNewArgumentName( aMethod);
				}
				
				public String getResult(){
					return this.result;
				}
				
			}
			
			RunnableWithResult myRunnable = new RunnableWithResult();
			
			SafeRunner.run(myRunnable);
			
			extensionName = myRunnable.getResult();

			if (extensionName != null){
				return extensionName;
			}
		}
		return getLocalArgumentName( method);

	}
	
	
	/**
	 * Use the factory if it exists! If not call a local version.
	 *
	 */
	public String getNewAssociationEndName(IAbstractArtifact artifact, int whichEnd) {
		if (extension != null){
			String extensionName = null;
			final IAbstractArtifact aArtifact = artifact;
			final int fWhichEnd = whichEnd;
			
			final class RunnableWithResult implements ISafeRunnable {
				private String  result;
				public void handleException(Throwable exception) {
					BasePlugin.log(exception);
				}

				public void run() throws Exception {
					  result = extension.getNewAssociationEndName(aArtifact, fWhichEnd);
				}
				
				public String getResult(){
					return this.result;
				}
				
			}
			
			RunnableWithResult myRunnable = new RunnableWithResult();
			
			SafeRunner.run(myRunnable);
			
			extensionName = myRunnable.getResult();

			if (extensionName != null){
				return extensionName;
			}
		}
		return getLocalAssociationEndName( artifact, whichEnd);

	}
	
	/*
	 * This will be used if no extensions registered or nothing returned.
	 */
	private String getLocalArtifactName(Class artifactClass, ITigerstripeModelProject project, String packageName) {
		try {
		IArtifactManagerSession session = project.getArtifactManagerSession();
		String labelStr = Misc
		.removeIllegalCharacters(ArtifactMetadataFactory.INSTANCE
				.getMetadata(
						MigrationHelper
								.artifactMetadataMigrateClassname(artifactClass
										.getName())).getLabel(MigrationHelper
												.artifactMetadataMigrateClassname(artifactClass
														.getName())));
		
		IArtifactQuery allArtsByType = session.makeQuery(IQueryArtifactsByType.class
				.getName());
		IQueryArtifactsByType typeQuery = (IQueryArtifactsByType) allArtsByType;
		typeQuery.setIncludeDependencies(false);
		typeQuery.setArtifactType(artifactClass.getName());

		Collection<IAbstractArtifact> artifacts = session
				.queryArtifact(typeQuery);
		
		int count = artifacts.size();
		
		String newName = labelStr+count;
		// make sure we're not creating a duplicate 
		// THIS VERSION DOESN'T CARE ABOUT THE PACKAGE - THE NAME IS UNIQUE ACROSS THE PROJECT
		
		boolean ok ;
		do {
			ok = true;
			for (IAbstractArtifact exists : artifacts){
				if (exists.getName().equals(newName)){
					count++;
					newName = labelStr+count;
					ok = false;
					break ;
				}
			}
		} 
		while ( ! ok);
		return newName;

		} catch (TigerstripeException t){
			return "NewArtifact";
		}
	}
	
	
	/**
	 * Very basic implementation that counts the items,
	 * and makes a new name based on the number of items.
	 * 
	 * @param field
	 * @param artifact
	 * @return
	 */
	private String getLocalFieldName(IAbstractArtifact artifact){
		
		Collection<IField> existingFields = artifact.getFields();
		int count = existingFields.size();
		
		String newName = FIELD+count;
		// make sure we're not creating a duplicate
		boolean ok ;
		do {
			ok = true;
			for (IField exists : existingFields){
				if (exists.getName().equals(newName)){
					count++;
					newName = FIELD+count;
					ok = false;
					break ;
				}
			}
		} 
		while ( ! ok);
		return newName;
	}
	
	/**
	 * Very basic implementation that counts the items,
	 * and makes a new name based on the number of items.
	 * 
	 * @param field
	 * @param artifact
	 * @return
	 */
	private String getLocalLiteralName(IAbstractArtifact artifact){
		Collection<ILiteral> existingLiterals = artifact.getLiterals();
		int count = existingLiterals.size();
		
		String newName = LITERAL+count;
		// make sure we're not creating a duplicate
		boolean ok ;
		do {
			ok = true;
			for (ILiteral exists : existingLiterals){
				if (exists.getName().equals(newName)){
					count++;
					newName = LITERAL+count;
					ok = false;
					break ;
				}
			}
		} 
		while ( ! ok);
		return newName;
	}
	
	/**
	 * Very basic implementation that counts the items,
	 * and makes a new name based on the number of items.
	 * 
	 * @param field
	 * @param artifact
	 * @return
	 */
	private String getLocalMethodName( IAbstractArtifact artifact){
		Collection<IMethod> existingMethods = artifact.getMethods();
		int count = existingMethods.size();
		
		String newName = METHOD+count;
		// make sure we're not creating a duplicate
		boolean ok ;
		do {
			ok = true;
			for (IMethod exists : existingMethods){
				if (exists.getName().equals(newName)){
					count++;
					newName = METHOD+count;
					ok = false;
					break ;
				}
			}
		} 
		while ( ! ok);
		return newName;
	}
	
	/** 
	 * This is not a model component in the "old" API. 
	 * 
	 * @param method
	 * @return
	 */
	private String getLocalArgumentName( IMethod method){
		Collection<IArgument> existingArguments = method.getArguments();
		int count = existingArguments.size();
		
		String newName = ARGUMENT+count;
		// make sure we're not creating a duplicate
		boolean ok ;
		do {
			ok = true;
			for (IArgument exists : existingArguments){
				if (exists.getName().equals(newName)){
					count++;
					newName = ARGUMENT+count;
					ok = false;
					break ;
				}
			}
		} 
		while ( ! ok);
		return newName;
	}
	
	private String getLocalAssociationEndName(IAbstractArtifact artifact, int whichEnd){
		
		/*
		 * This ONLY works for Associations! (and AssociationClasses)
		 */
		IAssociationArtifact association;
		ITigerstripeModelProject tsProject;
		
		if (! (artifact instanceof IAssociationArtifact) ){
			return null;
		} else {
			association = (IAssociationArtifact) artifact;
		}
		
		// Are the Ends set at this stage ?
		// If not we have no chance to do anything clever.
		if (association.getAEnd().getType()== null || association.getZEnd().getType()== null) {
			switch (whichEnd) {
			case AEND:
				return "_A"+END;
			case ZEND:
				return "_Z"+END;
			default:
				return "_A"+END;
			}
		}
		
		IType aEndType = association.getAEnd().getType();
		IType zEndType = association.getZEnd().getType();
		
		int index = 0;

		// compute aEnd name
		String aEndName = unCapitalize(aEndType.getName());
		String zEndName = unCapitalize(zEndType.getName());

		try {
			tsProject = artifact.getTigerstripeProject();
			IArtifactManagerSession session = tsProject
			.getArtifactManagerSession();

			Set<IRelationship> existingA = new HashSet<IRelationship>();
			existingA.addAll(session.getOriginatingRelationshipForFQN(
					aEndType.getFullyQualifiedName(), true));
			Set<IRelationship> existingZ = new HashSet<IRelationship>();
			existingZ.addAll(session.getTerminatingRelationshipForFQN(
					aEndType.getFullyQualifiedName(), true));

			Collection<IField> fields = new ArrayList<IField>();
			IAbstractArtifact aArtifact = session
			.getArtifactByFullyQualifiedName(aEndType
					.getFullyQualifiedName());
			if (aArtifact != null) {
				fields.addAll(aArtifact.getFields());
				fields.addAll(aArtifact.getInheritedFields());
			}

			boolean found = false;

			String tmpAName = aEndName;
			do {
				found = false;
				for (IRelationship rel : existingA) {
					// Check the *remote* End of any existing associations
					if (tmpAName.equals(rel.getRelationshipAEnd().getName())) {
						found = true;
						tmpAName = aEndName + "_" + index++;
					}
				}

				if (!found) {
					for (IRelationship rel : existingZ) {
						// Check the *remote* End of any existing
						// associations
						if (tmpAName.equals(rel.getRelationshipZEnd()
								.getName())) {
							found = true;
							tmpAName = aEndName + "_" + index++;
						}
					}
				}

				if (!found) {
					for (IField field : fields) {
						if (tmpAName.equals(field.getName())) {
							found = true;
							tmpAName = aEndName + "_" + index++;
						}
					}

				}

			} while (found);
			aEndName = tmpAName;
		

		// compute zEnd name

		// Check for self association
		if (zEndType.getFullyQualifiedName().equals(aEndType.getFullyQualifiedName())) {
			zEndName = zEndName + "_" + index++;
		} else {
			index = 0;
		}


			existingA = new HashSet<IRelationship>();
			existingA.addAll(session.getOriginatingRelationshipForFQN(
					zEndType.getFullyQualifiedName(), true));
			existingZ = new HashSet<IRelationship>();
			existingZ.addAll(session.getTerminatingRelationshipForFQN(
					zEndType.getFullyQualifiedName(), true));

			fields = new ArrayList<IField>();
			IAbstractArtifact zArtifact = session
			.getArtifactByFullyQualifiedName(aEndType
					.getFullyQualifiedName());
			if (zArtifact != null) {
				fields.addAll(zArtifact.getFields());
				fields.addAll(zArtifact.getInheritedFields());
			}

			found = false;

			String tmpZName = zEndName;
			do {
				found = false;
				for (IRelationship rel : existingZ) {
					if (tmpZName.equals(rel.getRelationshipZEnd().getName())) {
						found = true;
						tmpZName = zEndName + "_" + index++;
					}
				}
				if (!found) {
					for (IRelationship rel : existingA) {
						if (tmpZName.equals(rel.getRelationshipAEnd()
								.getName())) {
							found = true;
							tmpZName = zEndName + "_" + index++;
						}
					}
				}

				if (!found) {
					for (IField field : fields) {
						if (tmpZName.equals(field.getName())) {
							found = true;
							tmpZName = zEndName + "_" + index++;
						}
					}
				}

			} while (found);
			zEndName = tmpZName;
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}


		switch (whichEnd) {
		case AEND:
			return aEndName;
		case ZEND:
			return zEndName;
		default:
			return aEndName;
		}
		
	}
	
		private static String unCapitalize(String str) {
			if (str == null || str.length() == 0)
				return str;
			else if (str.length() == 1)
				return str.toLowerCase();

			return str.substring(0, 1).toLowerCase() + str.substring(1);
		}
	
}
