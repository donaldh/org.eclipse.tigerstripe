/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Anton Salnik) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test.adapt;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.base.test.AbstractTigerstripeTestCase;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestTigerstripeURIAdapterFactory extends
		AbstractTigerstripeTestCase {

	private ITigerstripeModelProject project;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		project = (ITigerstripeModelProject) createEmptyModelProject(
				"AnnotationTestProject", "AnnotationTestProject");
		createEachArtifactType(project);
	}

	@Override
	protected void tearDown() throws Exception {
		removeEachArtifactType(project);
		deleteModelProject(project);
		project = null;
		super.tearDown();
	}

	public void testModelComponents() throws Exception {
		Collection<IAbstractArtifact> artifacts = getAllArtifacts(project);
		assertTrue(artifacts.size() > 0);
		IAbstractArtifact artifact = null;
		for (IAbstractArtifact art : artifacts) {
			adaptAndCheck(art);
			if (art instanceof IManagedEntityArtifact) {
				artifact = art;
			}
		}

		// create literal/method/field
		assertNotNull(artifact);
		assertTrue(artifact.getChildren().isEmpty());
		createChildrens(artifact);

		IArtifactManagerSession mgrSession = project
				.getArtifactManagerSession();
		artifact = mgrSession.getArtifactByFullyQualifiedName(artifact
				.getFullyQualifiedName());
		assertNotNull(artifact);

		Collection<IField> childs = artifact.getFields();
		assertTrue(!childs.isEmpty());
		for (Iterator<?> it = childs.iterator(); it.hasNext();) {
			IModelComponent component = (IModelComponent) it.next();
			adaptAndCheck(component);
		}
	}

	private void adaptAndCheck(IModelComponent source)
			throws TigerstripeException {
		URI uri = TigerstripeURIAdapterFactory.toURI(source);
		assertNotNull(uri);
		assertTrue(TigerstripeURIAdapterFactory.isRelated(uri));
		IModelComponent component = TigerstripeURIAdapterFactory
				.uriToComponent(uri);
		assertNotNull(component);
		assertTrue(source.equals(component));
	}

	private void createChildrens(IAbstractArtifact artifact)
			throws TigerstripeException {
		IField field = artifact.makeField();
		String fieldName = "field1";
		field.setName(fieldName);
		artifact.addField(field);

		ILiteral literal1 = artifact.makeLiteral();
		String literalName = "literal1";
		literal1.setName(literalName);
		artifact.addLiteral(literal1);

		IMethod method1 = artifact.makeMethod();
		String methodName = "method1";
		method1.setName(methodName);
		artifact.addMethod(method1);

		artifact.doSave(new NullProgressMonitor());
	}
}
