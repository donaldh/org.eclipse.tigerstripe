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
package org.eclipse.tigerstripe.core.model;

import java.io.Writer;

import org.apache.log4j.Logger;
import org.eclipse.tigerstripe.api.model.ILabel;
import org.eclipse.tigerstripe.api.model.IType;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IEnumArtifact;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.core.model.ossj.EnumArtifactPersister;
import org.eclipse.tigerstripe.core.model.ossj.specifics.OssjEnumSpecifics;
import org.eclipse.tigerstripe.core.model.persist.AbstractArtifactPersister;

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

	public final static String LABEL = "Enumeration Artifact";

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	public final static EnumArtifact MODEL = new EnumArtifact(null);

	public String getArtifactType() {
		return IEnumArtifact.class.getName();
	}

	public String getIArtifactType() {
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
		return LABEL;
	}

	@Override
	public AbstractArtifact extractFromClass(JavaClass javaClass,
			ArtifactManager artifactMgr, ITigerstripeProgressMonitor monitor) {
		EnumArtifact result = new EnumArtifact(javaClass, artifactMgr, monitor);

		return result;
	}

	public EnumArtifact(ArtifactManager artifactMgr) {
		super(artifactMgr);
		OssjEnumSpecifics specifics = new OssjEnumSpecifics(this);
		setIStandardSpecifics(specifics);
	}

	public EnumArtifact(JavaClass javaClass, ArtifactManager artifactMgr,
			ITigerstripeProgressMonitor monitor) {
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
	 * @return Name of the label with lowest integer value.
	 */
	public String getMinLabel() {

		if (getBaseType().getFullyQualifiedName().equals("int")) {
			ILabel[] labels = getILabels();

			int val = 0;
			int min = 0;
			int index = 0;
			for (int i = 0; i < labels.length; i++) {
				val = Integer.valueOf(labels[i].getValue());
				if (val <= min) {
					min = val;
					index = i;
				}
			}
			return labels[index].getName();
		} else
			return getILabels()[0].getName();
	}

	/**
	 * Return the NAME of the Enum with the lowest value. If a String based Enum
	 * the return is indeterminate
	 * 
	 * @return Name of the label with lowest integer value.
	 */
	public String getMaxLabel() {
		if (getBaseType().getFullyQualifiedName().equals("int")) {
			ILabel[] labels = getILabels();
			int val = 0;
			int max = 0;
			int index = 0;
			for (int i = 0; i < labels.length; i++) {
				val = Integer.valueOf(labels[i].getValue());
				if (val >= max) {
					max = val;
					index = i;
				}
			}
			return labels[index].getName();
		} else
			return getILabels()[0].getName();
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