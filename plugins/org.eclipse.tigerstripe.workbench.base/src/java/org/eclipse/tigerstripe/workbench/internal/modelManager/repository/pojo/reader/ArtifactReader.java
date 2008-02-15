/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo.reader;

import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.metamodel.IMethod;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.Tag;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;

public abstract class ArtifactReader extends ModelComponentReader {

	protected JavaClass class_ = null;

	public void setClass(JavaClass class_) {
		this.class_ = class_;
	}

	public abstract IAbstractArtifact readArtifact()
			throws TigerstripeException;

	protected abstract String getMarkingTag();

	protected void internalRead(IAbstractArtifact artifact)
			throws TigerstripeException {

		resetTags();

		artifact.setName(class_.getName());
		artifact.setPackage(class_.getPackage());
		artifact.setComment(xmlEncode.decode(class_.getComment()));

		// Start with class-level tags
		DocletTag[] dtags = class_.getTags();
		for (int i = 0; i < dtags.length; i++) {
			Tag tag = new Tag(dtags[i]);
			tags.add(tag);
		}

		// Looks for the isAbstract value on the naming tag
		readIsAbstract(artifact, class_);

		// Extract all the stereotypes
		readStereotypes();

		// Extract implemented Artifacts
		readImplementedArtifacts();

		// Extract referenced comments
		readReferencedComments();

		// Then the methods
		readMethods(artifact);

		// And the fields
		readFields(artifact);

		// And the literals
		readLiterals(artifact);

		// The extends clause
		if (!"java.lang.Object".equals(class_.getSuperJavaClass()
				.getFullyQualifiedName())
				&& !"".equals(class_.getSuperJavaClass()
						.getFullyQualifiedName())) {

			String parentClass = class_.getSuperJavaClass()
					.getFullyQualifiedName();

			artifact.setExtendedArtifact(makeProxyArtifact(parentClass));
		}

	}

	protected void readImplementedArtifacts() {
		System.err.println("readImplementedArtifacts not implemented.");
	}

	protected abstract IAbstractArtifact makeProxyArtifact(String parentClass);

	private void readIsAbstract(IAbstractArtifact artifact, JavaClass class_) {
		Tag markingTag = getTagsByName(getMarkingTag()).get(0);
		if (markingTag != null) {
			String val = markingTag.getProperties().getProperty("isAbstract",
					"false");
			artifact.setAbstract(Boolean.parseBoolean(val));
		}
	}

	private void readMethods(IAbstractArtifact artifact)
			throws TigerstripeException {
		JavaMethod[] methods = class_.getMethods();
		for (int i = 0; i < methods.length; i++) {
			MethodReader mReader = new MethodReader(methods[i]);
			IMethod eMethod = mReader.readMethod();
			artifact.getMethods().add(eMethod);
		}
	}

	private void readFields(IAbstractArtifact artifact)
			throws TigerstripeException {
		JavaField[] fields = class_.getFields();
		for (int i = 0; i < fields.length; i++) {
			FieldReader fReader = new FieldReader(fields[i]);
			org.eclipse.tigerstripe.metamodel.IField eField = fReader
					.readField();
			artifact.getFields().add(eField);
		}
	}

	private void readLiterals(IAbstractArtifact artifact)
			throws TigerstripeException {
		JavaField[] fields = class_.getFields();
		for (int i = 0; i < fields.length; i++) {
			LiteralReader lReader = new LiteralReader(fields[i]);
			org.eclipse.tigerstripe.metamodel.ILiteral eLiteral = lReader
					.readLiteral();
			artifact.getLiterals().add(eLiteral);
		}
	}

}
