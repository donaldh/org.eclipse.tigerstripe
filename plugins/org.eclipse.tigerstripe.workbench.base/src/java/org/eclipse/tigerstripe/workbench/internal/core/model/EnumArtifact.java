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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.metamodel.impl.IEnumArtifactImpl;
import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjEnumSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.artifacts.EnumArtifactPersister;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * @author Eric Dillon
 * 
 */
public class EnumArtifact extends AbstractArtifact implements IEnumArtifact {

	/** logger for output */
	private static Logger log = Logger.getLogger(EnumArtifact.class);

	public final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.ENUM;

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	public final static EnumArtifact MODEL = new EnumArtifact(null);

	public String getArtifactType() {
		return IEnumArtifact.class.getName();
	}

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	@Override
	public String getMarkingTag() {
		return EnumArtifact.MARKING_TAG;
	}

	@Override
	public AbstractArtifact getModel() {
		return MODEL;
	}

	public String getLabel() {
		return ArtifactMetadataFactory.INSTANCE.getMetadata(
				IEnumArtifactImpl.class.getName()).getLabel();
	}

	@Override
	public AbstractArtifact extractFromClass(JavaClass javaClass,
			ArtifactManager artifactMgr, IProgressMonitor monitor) {
		EnumArtifact result = new EnumArtifact(javaClass, artifactMgr, monitor);

		return result;
	}

	public EnumArtifact(ArtifactManager artifactMgr) {
		super(artifactMgr);
		OssjEnumSpecifics specifics = new OssjEnumSpecifics(this);
		setIStandardSpecifics(specifics);
	}

	public EnumArtifact(JavaClass javaClass, ArtifactManager artifactMgr,
			IProgressMonitor monitor) {
		super(javaClass, artifactMgr, monitor);
		OssjEnumSpecifics specifics = new OssjEnumSpecifics(this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	public Type getBaseType() {
		OssjEnumSpecifics specifics = (OssjEnumSpecifics) getIStandardSpecifics();
		return specifics.getBaseType();
	}

	public String getBaseTypeStr() {
		return getBaseType().getFullyQualifiedName();
	}

	/**
	 * Return the NAME of the Enum with the lowest value. If a String based Enum
	 * the return is indeterminate
	 * 
	 * @return Name of the literal with lowest integer value.
	 */
	public String getMinLiteral() {

		if (getLiterals().isEmpty())
			return null;

		if (getBaseType().getFullyQualifiedName().equals("int")) {
			ILiteral result = null;
			int val = 0;
			int min = 0;
			for (ILiteral literal : getLiterals()) {
				val = Integer.valueOf(literal.getValue());
				if (val <= min) {
					min = val;
					result = literal;
				}
			}
			return result.getName();
		} else
			return getLiterals().iterator().next().getName();
	}

	/**
	 * Return the NAME of the Enum with the lowest value. If a String based Enum
	 * the return is indeterminate
	 * 
	 * @return Name of the literal with lowest integer value.
	 */
	public String getMaxLiteral() {
		if (getLiterals().isEmpty())
			return null;

		if (getBaseType().getFullyQualifiedName().equals("int")) {
			ILiteral result = null;
			int val = 0;
			int max = 0;
			for (ILiteral literal : getLiterals()) {
				val = Integer.valueOf(literal.getValue());
				if (val >= max) {
					max = val;
					result = literal;
				}
			}
			return result.getName();
		} else
			return getLiterals().iterator().next().getName();
	}

	public boolean getExtensible() {
		OssjEnumSpecifics specifics = (OssjEnumSpecifics) getIStandardSpecifics();
		return specifics.getExtensible();
	}

	public void setBaseType(IType type) {
		OssjEnumSpecifics specifics = (OssjEnumSpecifics) getIStandardSpecifics();
		specifics.setBaseType((Type) type);
	}

	@Override
	protected AbstractArtifactPersister getPersister(Writer writer) {
		return new EnumArtifactPersister(this, writer);
	}

	@Override
	protected IAbstractArtifact makeArtifact() {
		return new EnumArtifact(getArtifactManager());
	}
}