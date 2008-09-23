/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.patterns;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.xml.TigerstripeXMLParserUtils;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.w3c.dom.Element;

public interface IArtifactPattern extends IPattern{

	public void setParserUtils(TigerstripeXMLParserUtils utils);
	
	public void setElement(Element artifactElement);
	
	/**
	 * The type of the Artifact to create.
	 * This is used to drive aspects of the wizard
	 * @return
	 */
	public String getTargetArtifactType();

	/**
	 * The proposed name from the pattern file (if any)
	 * @return
	 */
	public String getArtifactName();
	
	/**
	 * The extended class from the pattern file (if any)
	 * @return
	 */
	public String getExtendedArtifactName();
	
	
	/** 
	 * Create and return an artifact that matches the pattern
	 * NOTE : This does not add it to the manager
	 * @param project
	 * @param packageName
	 * @param artifactName
	 * @param extendedArtifactName
	 * @return
	 * @throws TigerstripeException
	 */
	public IAbstractArtifact createArtifact(ITigerstripeModelProject project,
			String packageName, String artifactName, String extendedArtifactName)  throws TigerstripeException ;
	
	/**
	 * Convenience method for adding the created artifact to a manager.
	 * @param project
	 * @param artifact
	 * @throws TigerstripeException
	 */
	public void addToManager(ITigerstripeModelProject project, IAbstractArtifact artifact) throws TigerstripeException;

	/**
	 * Add any <em>TS</em> annotations that might need to be added from the pattern.
	 * @param project
	 * @param newArtifact
	 * @throws TigerstripeException
	 */
	public void annotateArtifact(ITigerstripeModelProject project, IAbstractArtifact newArtifact) throws TigerstripeException;

}
