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
package org.eclipse.tigerstripe.workbench.base.test.migration;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.metamodel.ERefByEnum;
import org.eclipse.tigerstripe.metamodel.IField;
import org.eclipse.tigerstripe.metamodel.MetamodelFactory;
import org.eclipse.tigerstripe.metamodel.VisibilityEnum;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.IModelManager;
import org.eclipse.tigerstripe.workbench.model.IModelRepository;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * Test all aspects of a ManagedEntity making sure whatever was true on the
 * deprecated interface is still true after migration to EMF-based metamodel.
 * 
 * @author erdillon
 * 
 */
public class TestFieldMigration extends TestCase {

	private ITigerstripeModelProject project;

	protected void setUp() throws Exception {
		super.setUp();

		// Create a Test Project to use for this test
		IProjectDetails details = TigerstripeCore.makeProjectDetails();
		details.setName("TestFieldMigration");
		project = (ITigerstripeModelProject) TigerstripeCore.createProject(
				details, null, ITigerstripeModelProject.class, null,
				new NullProgressMonitor());
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		if (project != null)
			project.delete(true, null);
	}

	/**
	 * Simple create from Old to New API
	 * 
	 * @throws Exception
	 */
	public void testAllFieldModifiersOtoN() throws Exception {
		org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession session = project
				.getArtifactManagerSession();

		org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact mea = (org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact) session
				.makeArtifact(org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact.class
						.getName());
		mea.setName("Mea");
		mea.setPackage("com.mycompany.testON");

		// Create a few attributes, trying all modifiers
		org.eclipse.tigerstripe.workbench.model.deprecated_.IField simpleInt = mea
				.makeField();
		simpleInt.setName("simpleInt");
		org.eclipse.tigerstripe.workbench.model.deprecated_.IType type = simpleInt
				.makeType();
		type.setFullyQualifiedName("int");
		simpleInt.setType(type);
		mea.addField(simpleInt);

		// ====================================================
		// Visibility Modifier
		org.eclipse.tigerstripe.workbench.model.deprecated_.IField privateInt = mea
				.makeField();
		privateInt.setName("privateInt");
		privateInt.setVisibility(EVisibility.PRIVATE);
		type = privateInt.makeType();
		type.setFullyQualifiedName("int");
		privateInt.setType(type);
		mea.addField(privateInt);

		org.eclipse.tigerstripe.workbench.model.deprecated_.IField publicInt = mea
				.makeField();
		publicInt.setName("publicInt");
		publicInt.setVisibility(EVisibility.PUBLIC);
		type = publicInt.makeType();
		type.setFullyQualifiedName("int");
		publicInt.setType(type);
		mea.addField(publicInt);

		org.eclipse.tigerstripe.workbench.model.deprecated_.IField protectedInt = mea
				.makeField();
		protectedInt.setName("protectedInt");
		protectedInt.setVisibility(EVisibility.PROTECTED);
		type = protectedInt.makeType();
		type.setFullyQualifiedName("int");
		protectedInt.setType(type);
		mea.addField(protectedInt);

		org.eclipse.tigerstripe.workbench.model.deprecated_.IField packageInt = mea
				.makeField();
		packageInt.setName("packageInt");
		packageInt.setVisibility(EVisibility.PACKAGE);
		type = packageInt.makeType();
		type.setFullyQualifiedName("int");
		packageInt.setType(type);
		mea.addField(packageInt);

		// =========================================================
		// is Readonly
		org.eclipse.tigerstripe.workbench.model.deprecated_.IField readOnlyInt = mea
				.makeField();
		readOnlyInt.setName("readOnlyInt");
		readOnlyInt.setReadOnly(true);
		type = readOnlyInt.makeType();
		type.setFullyQualifiedName("int");
		readOnlyInt.setType(type);
		mea.addField(readOnlyInt);

		org.eclipse.tigerstripe.workbench.model.deprecated_.IField notReadOnlyInt = mea
				.makeField();
		notReadOnlyInt.setName("notReadOnlyInt");
		notReadOnlyInt.setReadOnly(false);
		type = notReadOnlyInt.makeType();
		type.setFullyQualifiedName("int");
		notReadOnlyInt.setType(type);
		mea.addField(notReadOnlyInt);

		// =========================================================
		// is ordered
		org.eclipse.tigerstripe.workbench.model.deprecated_.IField orderedInt = mea
				.makeField();
		orderedInt.setName("orderedInt");
		orderedInt.setOrdered(true);
		type = orderedInt.makeType();
		type.setFullyQualifiedName("int");
		orderedInt.setType(type);
		mea.addField(orderedInt);

		org.eclipse.tigerstripe.workbench.model.deprecated_.IField notOrderedInt = mea
				.makeField();
		notOrderedInt.setName("notOrderedInt");
		notOrderedInt.setOrdered(false);
		type = notOrderedInt.makeType();
		type.setFullyQualifiedName("int");
		notOrderedInt.setType(type);
		mea.addField(notOrderedInt);

		// =========================================================
		// is unique
		org.eclipse.tigerstripe.workbench.model.deprecated_.IField uniqueInt = mea
				.makeField();
		uniqueInt.setName("uniqueInt");
		uniqueInt.setUnique(true);
		type = uniqueInt.makeType();
		type.setFullyQualifiedName("int");
		uniqueInt.setType(type);
		mea.addField(uniqueInt);

		org.eclipse.tigerstripe.workbench.model.deprecated_.IField notUniqueInt = mea
				.makeField();
		notUniqueInt.setName("notUniqueInt");
		notUniqueInt.setUnique(false);
		type = notUniqueInt.makeType();
		type.setFullyQualifiedName("int");
		notUniqueInt.setType(type);
		mea.addField(notUniqueInt);

		// =========================================================
		// is optional
		org.eclipse.tigerstripe.workbench.model.deprecated_.IField optionalInt = mea
				.makeField();
		optionalInt.setName("optionalInt");
		optionalInt.setOptional(true);
		type = uniqueInt.makeType();
		type.setFullyQualifiedName("int");
		optionalInt.setType(type);
		mea.addField(optionalInt);

		org.eclipse.tigerstripe.workbench.model.deprecated_.IField notOptionalInt = mea
				.makeField();
		notOptionalInt.setName("notOptionalInt");
		notOptionalInt.setOptional(false);
		type = notOptionalInt.makeType();
		type.setFullyQualifiedName("int");
		notOptionalInt.setType(type);
		mea.addField(notOptionalInt);

		// Default Value
		org.eclipse.tigerstripe.workbench.model.deprecated_.IField noDefaultInt = mea
				.makeField();
		noDefaultInt.setName("noDefaultInt");
		type = noDefaultInt.makeType();
		type.setFullyQualifiedName("int");
		noDefaultInt.setType(type);
		mea.addField(noDefaultInt);

		org.eclipse.tigerstripe.workbench.model.deprecated_.IField defaultInt = mea
				.makeField();
		defaultInt.setName("defaultInt");
		defaultInt.setDefaultValue("3");
		type = defaultInt.makeType();
		type.setFullyQualifiedName("int");
		defaultInt.setType(type);
		mea.addField(defaultInt);

		org.eclipse.tigerstripe.workbench.model.deprecated_.IField setNoDefaultInt = mea
				.makeField();
		setNoDefaultInt.setName("setNoDefaultInt");
		setNoDefaultInt.setDefaultValue(null);
		type = setNoDefaultInt.makeType();
		type.setFullyQualifiedName("int");
		setNoDefaultInt.setType(type);
		mea.addField(setNoDefaultInt);

		// RefBy
		org.eclipse.tigerstripe.workbench.model.deprecated_.IField refByNA = mea
				.makeField();
		refByNA.setName("refByNA"); // By default it is NA
		type = refByNA.makeType();
		type.setFullyQualifiedName("int");
		refByNA.setType(type);
		mea.addField(refByNA);

		org.eclipse.tigerstripe.workbench.model.deprecated_.IField refByKey = mea
				.makeField();
		refByKey.setName("refByKey");
		refByKey
				.setRefBy(org.eclipse.tigerstripe.workbench.model.deprecated_.IField.REFBY_KEY);
		type = refByKey.makeType();
		type.setFullyQualifiedName("int");
		refByKey.setType(type);
		mea.addField(refByKey);

		org.eclipse.tigerstripe.workbench.model.deprecated_.IField refByKeyResult = mea
				.makeField();
		refByKeyResult.setName("refByKeyResult");
		refByKeyResult
				.setRefBy(org.eclipse.tigerstripe.workbench.model.deprecated_.IField.REFBY_KEYRESULT);
		type = refByKeyResult.makeType();
		type.setFullyQualifiedName("int");
		refByKeyResult.setType(type);
		mea.addField(refByKeyResult);

		org.eclipse.tigerstripe.workbench.model.deprecated_.IField refByValue = mea
				.makeField();
		refByValue.setName("refByValue");
		refByValue
				.setRefBy(org.eclipse.tigerstripe.workbench.model.deprecated_.IField.REFBY_VALUE);
		type = refByValue.makeType();
		type.setFullyQualifiedName("int");
		refByValue.setType(type);
		mea.addField(refByValue);

		// And SAVE !!!
		mea.doSave(new NullProgressMonitor());

		// THIS IS MANDATORY BECAUSE THE OLD API WAS OUTSIDE OF ECLIPSE
		// RESOURCES
		IProject iProject = (IProject) project.getAdapter(IProject.class);
		iProject.refreshLocal(IResource.DEPTH_INFINITE,
				new NullProgressMonitor());

		IModelManager mMgr = project.getModelManager();
		IModelRepository repo = mMgr.getDefaultRepository();

		// This should go away soon as clients shouldn't be worried about that.
		repo.refresh(null);

		org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact nMea = (org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact) repo
				.getArtifactByFullyQualifiedName("com.mycompany.testON.Mea");
		assertNotNull(nMea);

		boolean simpleIntFound = false;
		boolean privateIntFound = false;
		boolean publicIntFound = false;
		boolean protectedIntFound = false;
		boolean packageIntFound = false;
		boolean notReadOnlyIntFound = false;
		boolean readOnlyIntFound = false;
		boolean orderedIntFound = false;
		boolean notOrderedIntFound = false;
		boolean uniqueIntFound = false;
		boolean notUniqueIntFound = false;
		boolean optionalIntFound = false;
		boolean notOptionalIntFound = false;
		boolean noDefaultIntFound = false;
		boolean defaultIntFound = false;
		boolean setNoDefaultIntFound = false;
		boolean refByNAFound = false;
		boolean refByKeyFound = false;
		boolean refByKeyResultFound = false;
		boolean refByValueFound = false;
		for (IField field : nMea.getFields()) {
			if ("simpleInt".equals(field.getName())) {
				assertTrue("int"
						.equals(field.getType().getFullyQualifiedName()));
				simpleIntFound = true;
			} else if ("privateInt".equals(field.getName())) {
				assertTrue(field.getVisibility() == VisibilityEnum.PRIVATE);
				privateIntFound = true;
			} else if ("publicInt".equals(field.getName())) {
				assertTrue(field.getVisibility() == VisibilityEnum.PUBLIC);
				publicIntFound = true;
			} else if ("packageInt".equals(field.getName())) {
				assertTrue(field.getVisibility() == VisibilityEnum.PACKAGE);
				packageIntFound = true;
			} else if ("protectedInt".equals(field.getName())) {
				assertTrue(field.getVisibility() == VisibilityEnum.PROTECTED);
				protectedIntFound = true;
			} else if ("notReadOnlyInt".equals(field.getName())) {
				assertTrue(!field.isReadOnly());
				notReadOnlyIntFound = true;
			} else if ("readOnlyInt".equals(field.getName())) {
				assertTrue(field.isReadOnly());
				readOnlyIntFound = true;
			} else if ("notOrderedInt".equals(field.getName())) {
				assertTrue(!field.isOrdered());
				notOrderedIntFound = true;
			} else if ("orderedInt".equals(field.getName())) {
				assertTrue(field.isOrdered());
				orderedIntFound = true;
			} else if ("notUniqueInt".equals(field.getName())) {
				assertTrue(!field.isUnique());
				notUniqueIntFound = true;
			} else if ("uniqueInt".equals(field.getName())) {
				assertTrue(field.isUnique());
				uniqueIntFound = true;
			} else if ("notOptionalInt".equals(field.getName())) {
				assertTrue(!field.isOptional());
				notOptionalIntFound = true;
			} else if ("optionalInt".equals(field.getName())) {
				assertTrue(field.isOptional());
				optionalIntFound = true;
			} else if ("noDefaultInt".equals(field.getName())) {
				assertTrue(!field.isSetDefaultValue());
				noDefaultIntFound = true;
			} else if ("defaultInt".equals(field.getName())) {
				assertTrue(field.isSetDefaultValue());
				assertTrue("3".equals(field.getDefaultValue()));
				defaultIntFound = true;
			} else if ("setNoDefaultInt".equals(field.getName())) {
				assertTrue(!field.isSetDefaultValue());
				setNoDefaultIntFound = true;
			} else if ("refByNA".equals(field.getName())) {
				assertTrue(field.getRefBy() == ERefByEnum.REF_BY_VALUE); // This
				// used
				// to
				// be
				// the
				// default,
				// need
				// to
				// keep
				// it.
				refByNAFound = true;
			} else if ("refByKey".equals(field.getName())) {
				assertTrue(field.getRefBy() == ERefByEnum.REF_BY_KEY);
				refByKeyFound = true;
			} else if ("refByKeyResult".equals(field.getName())) {
				assertTrue(field.getRefBy() == ERefByEnum.REF_BY_KEY_RESULT);
				refByKeyResultFound = true;
			} else if ("refByValue".equals(field.getName())) {
				assertTrue(field.getRefBy() == ERefByEnum.REF_BY_VALUE);
				refByValueFound = true;
			}
		}

		assertTrue(simpleIntFound);
		assertTrue(privateIntFound);
		assertTrue(publicIntFound);
		assertTrue(packageIntFound);
		assertTrue(protectedIntFound);
		assertTrue(readOnlyIntFound);
		assertTrue(notReadOnlyIntFound);
		assertTrue(orderedIntFound);
		assertTrue(notOrderedIntFound);
		assertTrue(uniqueIntFound);
		assertTrue(notUniqueIntFound);
		assertTrue(optionalIntFound);
		assertTrue(notOptionalIntFound);
		assertTrue(defaultIntFound);
		assertTrue(noDefaultIntFound);
		assertTrue(setNoDefaultIntFound);
		assertTrue(refByKeyFound);
		assertTrue(refByNAFound);
		assertTrue(refByKeyResultFound);
		assertTrue(refByValueFound);
	}

	public void testAllFieldModifiersNtoO() throws Exception {
		IModelManager mMgr = project.getModelManager();
		IModelRepository repo = mMgr.getDefaultRepository();

		repo.refresh(null); // TODO: shouldn't need this!

		org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact nMea = MetamodelFactory.eINSTANCE
				.createIManagedEntityArtifact();
		nMea.setName("Mea");
		nMea.setPackage("com.mycompany.testAllFieldModifiersNtoO");

		// Create a few attributes, trying all modifiers
		IField simpleInt = MetamodelFactory.eINSTANCE.createIField();
		simpleInt.setName("simpleInt");
		org.eclipse.tigerstripe.metamodel.IType type = MetamodelFactory.eINSTANCE
				.createIType();
		type.setFullyQualifiedName("int");
		simpleInt.setType(type);
		nMea.getFields().add(simpleInt);

		// ====================================================
		// Visibility Modifier
		IField privateInt = MetamodelFactory.eINSTANCE.createIField();
		privateInt.setName("privateInt");
		privateInt.setVisibility(VisibilityEnum.PRIVATE);
		type = MetamodelFactory.eINSTANCE.createIType();
		type.setFullyQualifiedName("int");
		privateInt.setType(type);
		nMea.getFields().add(privateInt);

		IField publicInt = MetamodelFactory.eINSTANCE.createIField();
		publicInt.setName("publicInt");
		publicInt.setVisibility(VisibilityEnum.PUBLIC);
		type = MetamodelFactory.eINSTANCE.createIType();
		type.setFullyQualifiedName("int");
		publicInt.setType(type);
		nMea.getFields().add(publicInt);

		IField protectedInt = MetamodelFactory.eINSTANCE.createIField();
		protectedInt.setName("protectedInt");
		protectedInt.setVisibility(VisibilityEnum.PROTECTED);
		type = MetamodelFactory.eINSTANCE.createIType();
		type.setFullyQualifiedName("int");
		protectedInt.setType(type);
		nMea.getFields().add(protectedInt);

		IField packageInt = MetamodelFactory.eINSTANCE.createIField();
		packageInt.setName("packageInt");
		packageInt.setVisibility(VisibilityEnum.PACKAGE);
		type = MetamodelFactory.eINSTANCE.createIType();
		type.setFullyQualifiedName("int");
		packageInt.setType(type);
		nMea.getFields().add(packageInt);

		// =========================================================
		// is Readonly
		IField readOnlyInt = MetamodelFactory.eINSTANCE.createIField();
		readOnlyInt.setName("readOnlyInt");
		readOnlyInt.setReadOnly(true);
		type = MetamodelFactory.eINSTANCE.createIType();
		type.setFullyQualifiedName("int");
		readOnlyInt.setType(type);
		nMea.getFields().add(readOnlyInt);

		IField notReadOnlyInt = MetamodelFactory.eINSTANCE.createIField();
		notReadOnlyInt.setName("notReadOnlyInt");
		notReadOnlyInt.setReadOnly(false);
		type = MetamodelFactory.eINSTANCE.createIType();
		type.setFullyQualifiedName("int");
		notReadOnlyInt.setType(type);
		nMea.getFields().add(notReadOnlyInt);

		// =========================================================
		// is ordered
		IField orderedInt = MetamodelFactory.eINSTANCE.createIField();
		orderedInt.setName("orderedInt");
		orderedInt.setOrdered(true);
		type = MetamodelFactory.eINSTANCE.createIType();
		type.setFullyQualifiedName("int");
		orderedInt.setType(type);
		nMea.getFields().add(orderedInt);

		IField notOrderedInt = MetamodelFactory.eINSTANCE.createIField();
		notOrderedInt.setName("notOrderedInt");
		notOrderedInt.setOrdered(false);
		type = MetamodelFactory.eINSTANCE.createIType();
		type.setFullyQualifiedName("int");
		notOrderedInt.setType(type);
		nMea.getFields().add(notOrderedInt);

		// =========================================================
		// is unique
		IField uniqueInt = MetamodelFactory.eINSTANCE.createIField();
		uniqueInt.setName("uniqueInt");
		uniqueInt.setUnique(true);
		type = MetamodelFactory.eINSTANCE.createIType();
		type.setFullyQualifiedName("int");
		uniqueInt.setType(type);
		nMea.getFields().add(uniqueInt);

		IField notUniqueInt = MetamodelFactory.eINSTANCE.createIField();
		notUniqueInt.setName("notUniqueInt");
		notUniqueInt.setUnique(false);
		type = MetamodelFactory.eINSTANCE.createIType();
		type.setFullyQualifiedName("int");
		notUniqueInt.setType(type);
		nMea.getFields().add(notUniqueInt);

		// =========================================================
		// is optional
		IField optionalInt = MetamodelFactory.eINSTANCE.createIField();
		optionalInt.setName("optionalInt");
		optionalInt.setOptional(true);
		type = MetamodelFactory.eINSTANCE.createIType();
		type.setFullyQualifiedName("int");
		optionalInt.setType(type);
		nMea.getFields().add(optionalInt);

		IField notOptionalInt = MetamodelFactory.eINSTANCE.createIField();
		notOptionalInt.setName("notOptionalInt");
		notOptionalInt.setOptional(false);
		type = MetamodelFactory.eINSTANCE.createIType();
		type.setFullyQualifiedName("int");
		notOptionalInt.setType(type);
		nMea.getFields().add(notOptionalInt);

		// Default Value
		IField noDefaultInt = MetamodelFactory.eINSTANCE.createIField();
		noDefaultInt.setName("noDefaultInt");
		type = MetamodelFactory.eINSTANCE.createIType();
		type.setFullyQualifiedName("int");
		noDefaultInt.setType(type);
		nMea.getFields().add(noDefaultInt);

		IField defaultInt = MetamodelFactory.eINSTANCE.createIField();
		defaultInt.setName("defaultInt");
		defaultInt.setDefaultValue("3");
		type = MetamodelFactory.eINSTANCE.createIType();
		type.setFullyQualifiedName("int");
		defaultInt.setType(type);
		nMea.getFields().add(defaultInt);

		IField setNoDefaultInt = MetamodelFactory.eINSTANCE.createIField();
		setNoDefaultInt.setName("setNoDefaultInt");
		setNoDefaultInt.setDefaultValue(null);
		type = MetamodelFactory.eINSTANCE.createIType();
		type.setFullyQualifiedName("int");
		setNoDefaultInt.setType(type);
		nMea.getFields().add(setNoDefaultInt);

		// RefBy
		IField refByNA = MetamodelFactory.eINSTANCE.createIField();
		refByNA.setName("refByNA");
		refByNA.setRefBy(ERefByEnum.NON_APPLICABLE);
		type = MetamodelFactory.eINSTANCE.createIType();
		type.setFullyQualifiedName("int");
		refByNA.setType(type);
		nMea.getFields().add(refByNA);

		IField refByKey = MetamodelFactory.eINSTANCE.createIField();
		refByKey.setName("refByKey");
		refByKey.setRefBy(ERefByEnum.REF_BY_KEY);
		type = MetamodelFactory.eINSTANCE.createIType();
		type.setFullyQualifiedName("int");
		refByKey.setType(type);
		nMea.getFields().add(refByKey);

		IField refByKeyResult = MetamodelFactory.eINSTANCE.createIField();
		refByKeyResult.setName("refByKeyResult");
		refByKeyResult.setRefBy(ERefByEnum.REF_BY_KEY_RESULT);
		type = MetamodelFactory.eINSTANCE.createIType();
		type.setFullyQualifiedName("int");
		refByKeyResult.setType(type);
		nMea.getFields().add(refByKeyResult);

		IField refByValue = MetamodelFactory.eINSTANCE.createIField();
		refByValue.setName("refByValue");
		refByValue.setRefBy(ERefByEnum.REF_BY_VALUE);
		type = MetamodelFactory.eINSTANCE.createIType();
		type.setFullyQualifiedName("int");
		refByValue.setType(type);
		nMea.getFields().add(refByValue);

		repo.store(nMea, true);

		IArtifactManagerSession session = project.getArtifactManagerSession();
		session.refresh(true, new NullProgressMonitor()); // required

		IManagedEntityArtifact oMea = (IManagedEntityArtifact) session
				.getArtifactByFullyQualifiedName("com.mycompany.testAllFieldModifiersNtoO.Mea");
		assertNotNull(oMea);

		boolean simpleIntFound = false;
		boolean privateIntFound = false;
		boolean publicIntFound = false;
		boolean protectedIntFound = false;
		boolean packageIntFound = false;
		boolean notReadOnlyIntFound = false;
		boolean readOnlyIntFound = false;
		boolean orderedIntFound = false;
		boolean notOrderedIntFound = false;
		boolean uniqueIntFound = false;
		boolean notUniqueIntFound = false;
		boolean optionalIntFound = false;
		boolean notOptionalIntFound = false;
		boolean noDefaultIntFound = false;
		boolean defaultIntFound = false;
		boolean setNoDefaultIntFound = false;
		boolean refByNAFound = false;
		boolean refByKeyFound = false;
		boolean refByKeyResultFound = false;
		boolean refByValueFound = false;
		for (org.eclipse.tigerstripe.workbench.model.deprecated_.IField field : oMea
				.getFields()) {
			if ("simpleInt".equals(field.getName())) {
				assertTrue("int"
						.equals(field.getType().getFullyQualifiedName()));
				simpleIntFound = true;
			} else if ("privateInt".equals(field.getName())) {
				assertTrue(field.getVisibility() == EVisibility.PRIVATE);
				privateIntFound = true;
			} else if ("publicInt".equals(field.getName())) {
				assertTrue(field.getVisibility() == EVisibility.PUBLIC);
				publicIntFound = true;
			} else if ("packageInt".equals(field.getName())) {
				assertTrue(field.getVisibility() == EVisibility.PACKAGE);
				packageIntFound = true;
			} else if ("protectedInt".equals(field.getName())) {
				assertTrue(field.getVisibility() == EVisibility.PROTECTED);
				protectedIntFound = true;
			} else if ("notReadOnlyInt".equals(field.getName())) {
				assertTrue(!field.isReadOnly());
				notReadOnlyIntFound = true;
			} else if ("readOnlyInt".equals(field.getName())) {
				assertTrue(field.isReadOnly());
				readOnlyIntFound = true;
			} else if ("notOrderedInt".equals(field.getName())) {
				assertTrue(!field.isOrdered());
				notOrderedIntFound = true;
			} else if ("orderedInt".equals(field.getName())) {
				assertTrue(field.isOrdered());
				orderedIntFound = true;
			} else if ("notUniqueInt".equals(field.getName())) {
				assertTrue(!field.isUnique());
				notUniqueIntFound = true;
			} else if ("uniqueInt".equals(field.getName())) {
				assertTrue(field.isUnique());
				uniqueIntFound = true;
			} else if ("notOptionalInt".equals(field.getName())) {
				assertTrue(!field.isOptional());
				notOptionalIntFound = true;
			} else if ("optionalInt".equals(field.getName())) {
				assertTrue(field.isOptional());
				optionalIntFound = true;
			} else if ("noDefaultInt".equals(field.getName())) {
				assertTrue(field.getDefaultValue() == null);
				noDefaultIntFound = true;
			} else if ("defaultInt".equals(field.getName())) {
				assertTrue("3".equals(field.getDefaultValue()));
				defaultIntFound = true;
			} else if ("setNoDefaultInt".equals(field.getName())) {
				assertTrue(field.getDefaultValue() == null
						|| field.getDefaultValue().length() == 0);
				setNoDefaultIntFound = true;
			} else if ("refByNA".equals(field.getName())) {
				assertTrue(field.getRefBy() == org.eclipse.tigerstripe.workbench.model.deprecated_.IField.REFBY_VALUE);
				refByNAFound = true;
			} else if ("refByKey".equals(field.getName())) {
				assertTrue(field.getRefBy() == org.eclipse.tigerstripe.workbench.model.deprecated_.IField.REFBY_KEY);
				refByKeyFound = true;
			} else if ("refByKeyResult".equals(field.getName())) {
				assertTrue(field.getRefBy() == org.eclipse.tigerstripe.workbench.model.deprecated_.IField.REFBY_KEYRESULT);
				refByKeyResultFound = true;
			} else if ("refByValue".equals(field.getName())) {
				assertTrue(field.getRefBy() == org.eclipse.tigerstripe.workbench.model.deprecated_.IField.REFBY_VALUE);
				refByValueFound = true;
			}
		}

		assertTrue(simpleIntFound);
		assertTrue(privateIntFound);
		assertTrue(publicIntFound);
		assertTrue(packageIntFound);
		assertTrue(protectedIntFound);
		assertTrue(readOnlyIntFound);
		assertTrue(notReadOnlyIntFound);
		assertTrue(orderedIntFound);
		assertTrue(notOrderedIntFound);
		assertTrue(uniqueIntFound);
		assertTrue(notUniqueIntFound);
		assertTrue(optionalIntFound);
		assertTrue(notOptionalIntFound);
		assertTrue(defaultIntFound);
		assertTrue(noDefaultIntFound);
		assertTrue(setNoDefaultIntFound);
		assertTrue(refByNAFound);
		assertTrue(refByKeyFound);
		assertTrue(refByKeyResultFound);
		assertTrue(refByValueFound);
	}

	public void testMultiplicityNtoO() throws Exception {
		org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession session = project
				.getArtifactManagerSession();

		org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact mea = (org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact) session
				.makeArtifact(org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact.class
						.getName());
		mea.setName("Mea");
		mea.setPackage("com.mycompany.testMultiplicityON");

		// =========================================================
		// Multiplicity 0
		org.eclipse.tigerstripe.workbench.model.deprecated_.IField zero = mea
				.makeField();
		zero.setName("zero");
		IType type = zero.makeType();
		type.setFullyQualifiedName("int");
		type.setTypeMultiplicity(EMultiplicity.ZERO);
		zero.setType(type);
		mea.addField(zero);

		// =========================================================
		// Multiplicity 1
		org.eclipse.tigerstripe.workbench.model.deprecated_.IField one = mea
				.makeField();
		one.setName("one");
		type = one.makeType();
		type.setFullyQualifiedName("int");
		type.setTypeMultiplicity(EMultiplicity.ONE);
		one.setType(type);
		mea.addField(one);

		// =========================================================
		// Multiplicity *
		org.eclipse.tigerstripe.workbench.model.deprecated_.IField star = mea
				.makeField();
		star.setName("star");
		type = star.makeType();
		type.setFullyQualifiedName("int");
		type.setTypeMultiplicity(EMultiplicity.STAR);
		star.setType(type);
		mea.addField(star);

		// =========================================================
		// Multiplicity 0..1
		org.eclipse.tigerstripe.workbench.model.deprecated_.IField zeroOne = mea
				.makeField();
		zeroOne.setName("zeroOne");
		type = zeroOne.makeType();
		type.setFullyQualifiedName("int");
		type.setTypeMultiplicity(EMultiplicity.ZERO_ONE);
		zeroOne.setType(type);
		mea.addField(zeroOne);

		// =========================================================
		// Multiplicity 0..*
		org.eclipse.tigerstripe.workbench.model.deprecated_.IField zeroStar = mea
				.makeField();
		zeroStar.setName("zeroStar");
		type = zeroStar.makeType();
		type.setFullyQualifiedName("int");
		type.setTypeMultiplicity(EMultiplicity.ZERO_STAR);
		zeroStar.setType(type);
		mea.addField(zeroStar);

		// =========================================================
		// Multiplicity 1..*
		org.eclipse.tigerstripe.workbench.model.deprecated_.IField oneStar = mea
				.makeField();
		oneStar.setName("oneStar");
		type = oneStar.makeType();
		type.setFullyQualifiedName("int");
		type.setTypeMultiplicity(EMultiplicity.ONE_STAR);
		oneStar.setType(type);
		mea.addField(oneStar);

		// And SAVE !!!
		mea.doSave(new NullProgressMonitor());

		// THIS IS MANDATORY BECAUSE THE OLD API WAS OUTSIDE OF ECLIPSE
		// RESOURCES
		IProject iProject = (IProject) project.getAdapter(IProject.class);
		iProject.refreshLocal(IResource.DEPTH_INFINITE,
				new NullProgressMonitor());

		IModelManager mMgr = project.getModelManager();
		IModelRepository repo = mMgr.getDefaultRepository();

		// This should go away soon as clients shouldn't be worried about that.
		repo.refresh(null);

		org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact nMea = (org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact) repo
				.getArtifactByFullyQualifiedName("com.mycompany.testMultiplicityON.Mea");
		assertNotNull(nMea);

		boolean oneFound = false;
		boolean zeroFound = false;
		boolean starFound = false;
		boolean zeroOneFound = false;
		boolean zeroStarFound = false;
		boolean oneStarFound = false;
		for (IField field : nMea.getFields()) {
			if ("one".equals(field.getName())) {
				assertTrue(field.getType().getMultiplicity().getLowerBound() == 1
						&& field.getType().getMultiplicity().getUpperBound() == 1);
				oneFound = true;
			} else if ("zero".equals(field.getName())) {
				assertTrue(field.getType().getMultiplicity().getLowerBound() == 0
						&& field.getType().getMultiplicity().getUpperBound() == 0);
				zeroFound = true;
			} else if ("star".equals(field.getName())) {
				assertTrue(field.getType().getMultiplicity().getLowerBound() == 0
						&& field.getType().getMultiplicity().getUpperBound() == -1);
				starFound = true;
			} else if ("zeroOne".equals(field.getName())) {
				assertTrue(field.getType().getMultiplicity().getLowerBound() == 0
						&& field.getType().getMultiplicity().getUpperBound() == 1);
				zeroOneFound = true;
			} else if ("zeroStar".equals(field.getName())) {
				assertTrue(field.getType().getMultiplicity().getLowerBound() == 0
						&& field.getType().getMultiplicity().getUpperBound() == -1);
				zeroStarFound = true;
			} else if ("oneStar".equals(field.getName())) {
				assertTrue(field.getType().getMultiplicity().getLowerBound() == 1
						&& field.getType().getMultiplicity().getUpperBound() == -1);
				oneStarFound = true;
			}
		}

		assertTrue(oneFound);
		assertTrue(zeroFound);
		assertTrue(starFound);
		assertTrue(zeroOneFound);
		assertTrue(zeroStarFound);
		assertTrue(oneStarFound);
	}

	public void testSimpleNtoOCreate() throws Exception {
		IModelManager mMgr = project.getModelManager();
		IModelRepository repo = mMgr.getDefaultRepository();

		repo.refresh(null); // TODO: shouldn't need this!
		
		org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact nMea = MetamodelFactory.eINSTANCE
				.createIManagedEntityArtifact();
		nMea.setName("Mea");
		nMea.setPackage("com.mycompany.testNO");
		repo.store(nMea, true);

		IArtifactManagerSession session = project.getArtifactManagerSession();
		session.refresh(true, new NullProgressMonitor()); // required

		IManagedEntityArtifact oMea = (IManagedEntityArtifact) session
				.getArtifactByFullyQualifiedName("com.mycompany.testNO.Mea");
		assertNotNull(oMea);

		// At this stage, you need to be in the edit domain to make changes to
		// nMea
		try {
			nMea.setAbstract(true);
			throw new TigerstripeException(
					"Set without TX shouldn't be allowed");
		} catch (IllegalStateException e) {
			// we're good!
		}
	}

}
