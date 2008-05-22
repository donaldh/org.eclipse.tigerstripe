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
package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.io.Writer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.metamodel.impl.IQueryArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjQuerySpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.artifacts.QueryArtifactPersister;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class QueryArtifact extends AbstractArtifact implements IQueryArtifact {

	public final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.QUERY;

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	public final static QueryArtifact MODEL = new QueryArtifact(null);

	public String getArtifactType() {
		return IQueryArtifact.class.getName();
	}

	@Override
	public String getMarkingTag() {
		return QueryArtifact.MARKING_TAG;
	}

	@Override
	public AbstractArtifact getModel() {
		return MODEL;
	}

	public String getLabel() {
		return ArtifactMetadataFactory.INSTANCE.getMetadata(
				IQueryArtifactImpl.class.getName()).getLabel();
	}

	@Override
	public AbstractArtifact extractFromClass(JavaClass javaClass,
			ArtifactManager artifactMgr, IProgressMonitor monitor) {
		QueryArtifact result = new QueryArtifact(javaClass, artifactMgr,
				monitor);

		return result;
	}

	public IType getReturnedType() {
		return getReturnedEntityType();
	}

	public IType makeType() {
		return new Type(getArtifactManager());
	}

	public void setReturnedType(IType type) {
		setReturnedEntityType((Type) type);
	}

	public Type getReturnedEntityType() {
		OssjQuerySpecifics specifics = (OssjQuerySpecifics) getIStandardSpecifics();
		return (Type) specifics.getReturnedEntityIType();
	}

	protected void setReturnedEntityType(Type type) {
		OssjQuerySpecifics specifics = (OssjQuerySpecifics) getIStandardSpecifics();
		specifics.setReturnedEntityIType(type);
	}

	public QueryArtifact(ArtifactManager artifactMgr) {
		super(artifactMgr);
		OssjQuerySpecifics specifics = new OssjQuerySpecifics(this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	public QueryArtifact(JavaClass javaClass, ArtifactManager artifactMgr,
			IProgressMonitor monitor) {
		super(javaClass, artifactMgr, monitor);
		OssjQuerySpecifics specifics = new OssjQuerySpecifics(this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	@Override
	public void resolveReferences(IProgressMonitor monitor)
			throws TigerstripeException {
		super.resolveReferences(monitor);

		// // BUG 450 no need to extract that here anymore. The specifics are
		// built at this stage
		// Tag tag = getFirstTagByName(MARKING_TAG);
		//
		// if (tag != null) {
		// Properties props = tag.getProperties();
		// String strType = props.getProperty("return");
		// setReturnedEntityType(new Type(strType, "", getArtifactManager()));
		// }
	}

	@Override
	protected AbstractArtifactPersister getPersister(Writer writer) {
		return new QueryArtifactPersister(this, writer);
	}

	@Override
	protected IAbstractArtifact makeArtifact() {
		return new QueryArtifact(getArtifactManager());
	}
}