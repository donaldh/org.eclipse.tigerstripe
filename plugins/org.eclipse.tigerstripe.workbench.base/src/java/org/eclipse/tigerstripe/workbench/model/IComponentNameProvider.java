package org.eclipse.tigerstripe.workbench.model;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/** 
 * Objects implementing this interface should provide a name for any type of 
 * ModelComponent.
 * 
 * If an implementation returns null, then the default will be used.
 * 
 * The methods all takes in :
 * - an object which defines the SCOPE within which we can evaluate the name. eg
 *  for an Artifact, we need to make sure its unique in the scope of the PROJECT.
 *  for a Field, the scope is the ARTIFACT to which it is to be added.
 * 
 * @author rcraddoc
 *
 */
public interface IComponentNameProvider {
	
	/**
	 * This version should be used to get the new name for an
	 * artifact - the name will be based on the class, and potentially on the
	 * ends of associations. 
	 * 
	 * The "target" packageName is provided in case naming rule needs to ensure artifact
	 * uniqueness within a package.
	 * 
	 * @param artifactClass
	 * @param project
	 * @param packageName
	 * @return
	 */
	public String getNewArtifactName(Class artifactClass, ITigerstripeModelProject project, String packageName);
	
	/**
	 * This version should be used to select a name of 
	 * an IField - where the existing fields on the
	 * artifact is required for validity checking
	 * 
	 * @param artifact
	 * @return
	 */
	public String getNewFieldName(IAbstractArtifact artifact);
	
	/**
	 * This version should be used to select a name of 
	 * an ILiteral - where the existing literals on the
	 * artifact is required for validity checking
	 * 
	 * @param artifact
	 * @return
	 */
	public String getNewLiteralName(IAbstractArtifact artifact);
	
	/**
	 * This version should be used to select a name of 
	 * an IMethod - where the existing methods on the
	 * artifact is required for validity checking
	 * 
	 * @param artifact
	 * @return
	 */
	public String getNewMethodName(IAbstractArtifact artifact);
	
	/**
	 * This version should be used to select a name of 
	 * an IArgument where the existing arguments on the
	 * method is required for validity checking
	 * 
	 * @param method
	 * @return
	 * 
	 */
	public String getNewArgumentName(IMethod method);

	public final static int AEND = 0;

	public final static int ZEND = 1;
	
	/**
	 * This version should be used to select a name of 
	 * an AssociationEnd - where the existing methods on the
	 * artifact is required for validity checking
	 * 
	 * @param artifact - must be an IAssociationArtifact or IAssociationClassArtifact
	 * @param whichEnd - should be one of AEND or ZEND
	 * @return
	 */
	public String getNewAssociationEndName(IAbstractArtifact artifact, int whichEnd);
	
}
