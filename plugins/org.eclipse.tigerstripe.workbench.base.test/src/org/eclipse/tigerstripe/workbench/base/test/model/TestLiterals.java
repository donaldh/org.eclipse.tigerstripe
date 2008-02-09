package org.eclipse.tigerstripe.workbench.base.test.model;

import java.util.Collection;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.ILiteral;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.model.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.model.artifacts.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestLiterals extends junit.framework.TestCase {

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
		IAbstractTigerstripeProject aProject = createModelProject("testLierals");
		assertTrue(aProject instanceof ITigerstripeModelProject);
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
		ArtifactTestHelper artHelper = new ArtifactTestHelper(project);

		IManagedEntityArtifact top = (IManagedEntityArtifact) artHelper
				.createArtifact(IManagedEntityArtifact.class.getName(), "Top",
						"com.test");

		ILiteral literal1 = top.makeLiteral();
		String literalName = "literal1";
		literal1.setName(literalName);
		top.addLiteral(literal1);
		top.doSave(new NullProgressMonitor());

		Collection<ILiteral> literals = top.getLiterals();
		assertTrue("Literal collection  does not include correct literal", literals
				.iterator().next().getName().equals(literalName));

		String comment = "These are the words";
		EVisibility vis = EVisibility.PUBLIC;
		String value = "0";

		IType type = literal1.makeType();
		type.setFullyQualifiedName("int");
		type.setTypeMultiplicity(EMultiplicity.ZERO_ONE);
		literal1.setType(type);

		literal1.setComment(comment);
		literal1.setVisibility(vis);
		literal1.setValue(value);

		top.doSave(new NullProgressMonitor());

		// //////////
		// Make sure we got the right values
		ILiteral gotLiteral = top.getLiterals().iterator().next();
		assertTrue("Didn't get the same literal back!", gotLiteral.equals(literal1));
		assertTrue("Literal name changed on save", gotLiteral.getName().equals(literalName));
		assertTrue("Literal comment changed on save", gotLiteral.getComment()
				.equals(comment));
		assertTrue("Literal value changed on save", gotLiteral.getValue().equals(value));
		assertTrue("Literal visibility changed on save", gotLiteral.getVisibility()
				.equals(vis));
		assertTrue("Literal type changed on save", gotLiteral.getType().getName()
				.equals(type.getName()));

		// /////////
		// Now change all the values and see of they have been reset!
		String literalName2 = "literal2";
		literal1.setName(literalName2);
		IType newType = literal1.makeType();
		newType.setFullyQualifiedName("String");
		newType.setTypeMultiplicity(EMultiplicity.ZERO_STAR);

		comment = "Changed words";
		value = "\"Now use this\"";
		vis = EVisibility.PRIVATE;

		literal1.setType(newType);

		literal1.setComment(comment);
		literal1.setVisibility(vis);
		literal1.setValue(value);

		top.doSave(new NullProgressMonitor());

		// //////////
		// Make sure we got the right values
		ILiteral gotNewLiteral = top.getLiterals().iterator().next();
		assertTrue("Didn't get the same literal back!", gotNewLiteral
				.equals(literal1));
		assertTrue("Literal name changed on save", gotNewLiteral.getName().equals(
				literalName2));
		assertTrue("Literal comment changed on save", gotNewLiteral.getComment()
				.equals(comment));
		assertTrue("Literal value changed on save", gotNewLiteral.getValue().equals(value));
		assertTrue("Literal visibility changed on save", gotNewLiteral
				.getVisibility().equals(vis));
		assertTrue("Literal type changed on save", gotNewLiteral.getType()
				.getName().equals(newType.getName()));

		
		//////////////////
		// Check that we can't set an invalid type !
		
		
		
		
		
		project.delete(true, new NullProgressMonitor());
	}

}
