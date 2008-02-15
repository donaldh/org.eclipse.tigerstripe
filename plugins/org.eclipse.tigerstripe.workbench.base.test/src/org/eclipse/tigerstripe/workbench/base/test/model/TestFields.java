package org.eclipse.tigerstripe.workbench.base.test.model;

import java.util.Collection;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestFields extends junit.framework.TestCase {

	protected IAbstractTigerstripeProject createModelProject(String projectName)
			throws TigerstripeException {
		IProjectDetails details = TigerstripeCore.makeProjectDetails();
		details.setName(projectName);
		IAbstractTigerstripeProject aProject = TigerstripeCore.createProject(
				details, null, ITigerstripeModelProject.class, null,
				new NullProgressMonitor());
		return aProject;
	}

	
	public final void testFields() throws TigerstripeException {
		IAbstractTigerstripeProject aProject = createModelProject("testFields");
		assertTrue(aProject instanceof ITigerstripeModelProject);
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
		ArtifactTestHelper artHelper = new ArtifactTestHelper(project);

		IManagedEntityArtifact top = (IManagedEntityArtifact) artHelper
				.createArtifact(IManagedEntityArtifact.class.getName(), "Top",
						"com.test");

		IField field1 = top.makeField();
		String fieldName = "field1";
		field1.setName(fieldName);
		top.addField(field1);
		top.doSave(new NullProgressMonitor());

		Collection<IField> fields = top.getFields();
		assertTrue("Field collection  does not include correct field", fields
				.iterator().next().getName().equals(fieldName));

		String comment = "These are the words";
		String defaultValue = "Start with this";
		int refBy = 0;
		EVisibility vis = EVisibility.PUBLIC;

		IType type = field1.makeType();
		type.setFullyQualifiedName("test.thisone");
		type.setTypeMultiplicity(EMultiplicity.ZERO_ONE);
		field1.setType(type);

		field1.setComment(comment);
		field1.setOptional(true);
		field1.setReadOnly(true);
		field1.setDefaultValue(defaultValue);
		field1.setOrdered(true);
		field1.setUnique(true);
		field1.setRefBy(refBy);
		field1.setVisibility(vis);

		top.doSave(new NullProgressMonitor());

		// //////////
		// Make sure we got the right values
		IField gotField = top.getFields().iterator().next();
		assertTrue("Didn't get the same field back!", gotField.equals(field1));
		assertTrue("Field name changed on save", gotField.getName().equals(
				"field1"));
		assertTrue("Field comment changed on save", gotField.getComment()
				.equals(comment));
		assertTrue("Field default changed on save", gotField.getDefaultValue()
				.equals(defaultValue));
		assertTrue("Field optional changed on save", gotField.isOptional());
		assertTrue("Field readOnly changed on save", gotField.isReadOnly());
		assertTrue("Field unique changed on save", gotField.isUnique());
		assertTrue("Field ordered changed on save", gotField.isOrdered());
		assertTrue("Field refBy changed on save", gotField.getRefBy() == refBy);
		assertTrue("Field visibility changed on save", gotField.getVisibility()
				.equals(vis));
		assertTrue("Field type changed on save", gotField.getType().getName()
				.equals(type.getName()));

		// /////////
		// Now change all the values and see of they have been reset!

		field1.setName("field2");
		IType newType = field1.makeType();
		newType.setFullyQualifiedName("test.otherone");
		newType.setTypeMultiplicity(EMultiplicity.ZERO_STAR);

		comment = "Changed words";
		defaultValue = "Now use this";
		refBy = 2;
		vis = EVisibility.PRIVATE;

		field1.setType(newType);

		field1.setComment(comment);
		field1.setOptional(false);
		field1.setReadOnly(false);
		field1.setDefaultValue(defaultValue);
		field1.setOrdered(false);
		field1.setUnique(false);
		field1.setRefBy(refBy);
		field1.setVisibility(vis);

		top.doSave(new NullProgressMonitor());

		// //////////
		// Make sure we got the right values
		IField gotNewField = top.getFields().iterator().next();
		assertTrue("Didn't get the same field back!", gotNewField
				.equals(field1));
		assertTrue("Field name changed on save", gotNewField.getName().equals(
				"field2"));
		assertTrue("Field comment changed on save", gotNewField.getComment()
				.equals(comment));
		assertTrue("Field default changed on save", gotNewField
				.getDefaultValue().equals(defaultValue));
		assertTrue("Field optional changed on save", !gotNewField.isOptional());
		assertTrue("Field readOnly changed on save", !gotNewField.isReadOnly());
		assertTrue("Field unique changed on save", !gotNewField.isUnique());
		assertTrue("Field ordered changed on save", !gotNewField.isOrdered());
		assertTrue("Field refBy changed on save",
				gotNewField.getRefBy() == refBy);
		assertTrue("Field visibility changed on save", gotNewField
				.getVisibility().equals(vis));
		assertTrue("Field type changed on save", gotNewField.getType()
				.getName().equals(newType.getName()));

		project.delete(true, new NullProgressMonitor());
	}

}
