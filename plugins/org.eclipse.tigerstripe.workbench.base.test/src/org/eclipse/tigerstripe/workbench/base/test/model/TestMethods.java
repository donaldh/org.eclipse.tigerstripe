package org.eclipse.tigerstripe.workbench.base.test.model;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument.EDirection;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestMethods extends junit.framework.TestCase {

	protected IAbstractTigerstripeProject createModelProject(String projectName)
			throws TigerstripeException {
		IProjectDetails details = TigerstripeCore.makeProjectDetails();
		IAbstractTigerstripeProject aProject = TigerstripeCore.createProject(
				projectName, details, null, ITigerstripeModelProject.class,
				null, new NullProgressMonitor());
		return aProject;
	}

	public final void testMethods() throws TigerstripeException {
		IAbstractTigerstripeProject aProject = createModelProject("testMethods");
		assertTrue(aProject instanceof ITigerstripeModelProject);
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
		ArtifactTestHelper artHelper = new ArtifactTestHelper(project);

		IManagedEntityArtifact top = (IManagedEntityArtifact) artHelper
				.createArtifact(IManagedEntityArtifact.class.getName(), "Top",
						"com.test");

		IMethod method1 = top.makeMethod();
		String methodName = "method1";
		method1.setName(methodName);
		top.addMethod(method1);
		top.doSave(new NullProgressMonitor());

		Collection<IMethod> methods = top.getMethods();
		assertTrue("Method collection  does not include correct method",
				methods.iterator().next().getName().equals(methodName));

		String comment = "These are the words";
		EVisibility vis = EVisibility.PUBLIC;
		String defaultReturnValue = "0";
		String methodReturnName = "return";

		IType type = method1.makeType();
		type.setFullyQualifiedName("int");
		type.setTypeMultiplicity(EMultiplicity.ZERO_ONE);
		method1.setReturnType(type);

		method1.setComment(comment);
		method1.setVisibility(vis);
		method1.setAbstract(true);
		method1.setOptional(true);
		method1.setOrdered(true);
		method1.setUnique(true);
		method1.setDefaultReturnValue(defaultReturnValue);
		method1.setReturnName(methodReturnName);
		method1.setVoid(false);

		top.doSave(new NullProgressMonitor());

		// //////////
		// Make sure we got the right values
		IMethod gotMethod = top.getMethods().iterator().next();
		assertTrue("Didn't get the same method back!", gotMethod
				.equals(method1));
		assertTrue("Method name changed on save", gotMethod.getName().equals(
				methodName));
		assertTrue("Method comment changed on save", gotMethod.getComment()
				.equals(comment));
		assertTrue("Method default Value changed on save", gotMethod
				.getDefaultReturnValue().equals(defaultReturnValue));
		assertTrue("Method return name changed on save", gotMethod
				.getReturnName().equals(methodReturnName));
		assertTrue("Method abstract changed on save", gotMethod.isAbstract());
		assertTrue("Method optional changed on save", gotMethod.isOptional());
		assertTrue("Method unique changed on save", gotMethod.isUnique());
		assertTrue("Method ordered changed on save", gotMethod.isOrdered());
		assertTrue("Method visibility changed on save", gotMethod
				.getVisibility().equals(vis));
		assertTrue("Method type changed on save", gotMethod.getReturnType()
				.getName().equals(type.getName()));

		// /////////
		// Now change all the values and see of they have been reset!
		String methodName2 = "method2";
		method1.setName(methodName2);
		IType newType = method1.makeType();
		newType.setFullyQualifiedName("String");
		newType.setTypeMultiplicity(EMultiplicity.ZERO_STAR);

		comment = "Changed words";
		vis = EVisibility.PRIVATE;
		defaultReturnValue = "\"Now use this\"";
		methodReturnName = "getBack";
		method1.setReturnType(newType);
		method1.setComment(comment);
		method1.setVisibility(vis);
		method1.setAbstract(false);
		method1.setOptional(false);
		method1.setOrdered(false);
		method1.setUnique(false);
		method1.setDefaultReturnValue(defaultReturnValue);
		method1.setReturnName(methodReturnName);

		top.doSave(new NullProgressMonitor());

		// //////////
		// Make sure we got the right values
		IMethod gotNewMethod = top.getMethods().iterator().next();
		assertTrue("Didn't get the same method back!", gotNewMethod
				.equals(method1));
		assertTrue("Method name changed on save", gotNewMethod.getName()
				.equals(methodName2));
		assertTrue("Method comment changed on save", gotNewMethod.getComment()
				.equals(comment));
		assertTrue("Method default Value changed on save", gotNewMethod
				.getDefaultReturnValue().equals(defaultReturnValue));
		assertTrue("Method return name changed on save", gotNewMethod
				.getReturnName().equals(methodReturnName));
		assertTrue("Method abstract changed on save", !gotNewMethod
				.isAbstract());
		assertTrue("Method optional changed on save", !gotNewMethod
				.isOptional());
		assertTrue("Method unique changed on save", !gotNewMethod.isUnique());
		assertTrue("Method ordered changed on save", !gotNewMethod.isOrdered());
		assertTrue("Method visibility changed on save", gotNewMethod
				.getVisibility().equals(vis));
		assertTrue("Method type changed on save", gotNewMethod.getReturnType()
				.getName().equals(newType.getName()));

		// /////////
		// test for a void method setting
		method1.setVoid(true);
		top.doSave(new NullProgressMonitor());
		IMethod gotVoidMethod = top.getMethods().iterator().next();
		assertTrue("Method void changed on save", gotVoidMethod.isVoid());
		assertTrue("void Method type not void", gotNewMethod.getReturnType()
				.getName().equals("void"));

		// Test Argument list setting and changing
		String[] argNames = { "one", "two", "three" };
		String[] defaultValues = { "une", "deux", "trois" };
		String[] descriptions = { "uno", "due", "tre" };
		int[] refBys = { 0, 1, 2 };

		IArgument arg0 = method1.makeArgument();
		arg0.setName(argNames[0]);
		arg0.setDefaultValue(defaultValues[0]);
		arg0.setOrdered(true);
		arg0.setUnique(true);
		arg0.setComment(descriptions[0]);
		arg0.setRefBy(refBys[0]);
		arg0.setType(type);
		arg0.setDirection(EDirection.OUT);

		method1.addArgument(arg0);
		top.doSave(new NullProgressMonitor());

		// //////////
		// Make sure we got the right list size
		gotNewMethod = top.getMethods().iterator().next();
		assertTrue("Wrong number of Arguments ("
				+ gotNewMethod.getArguments().size() + ")", gotNewMethod
				.getArguments().size() == 1);

		// Make sure we got the right values
		IArgument firstArg = gotNewMethod.getArguments().iterator().next();
		assertTrue("Didn't get the same argument back!", firstArg.equals(arg0));
		assertTrue("Argument name changed on save", firstArg.getName().equals(
				argNames[0]));
		assertTrue("Argument comment changed on save", firstArg.getComment()
				.equals(descriptions[0]));
		assertTrue("Argument default Value changed on save", firstArg
				.getDefaultValue().equals(defaultValues[0]));
		assertTrue("Argument unique changed on save", firstArg.isUnique());
		assertTrue("Argument ordered changed on save", firstArg.isOrdered());
		assertTrue("Argument type changed on save", firstArg.getType()
				.getName().equals(type.getName()));
		assertTrue("Argument direction changed on save", firstArg.getDirection()
				.equals(EDirection.OUT));

		// Change the values and retest
		arg0.setName(argNames[2]);
		arg0.setDefaultValue(defaultValues[2]);
		arg0.setOrdered(false);
		arg0.setUnique(false);
		arg0.setComment(descriptions[2]);
		arg0.setRefBy(refBys[2]);
		arg0.setType(newType);
		arg0.setDirection(EDirection.IN);
		top.doSave(new NullProgressMonitor());

		gotNewMethod = top.getMethods().iterator().next();
		assertTrue("Wrong number of Arguments ("
				+ gotNewMethod.getArguments().size() + ")", gotNewMethod
				.getArguments().size() == 1);

		// Make sure we got the right values
		firstArg = gotNewMethod.getArguments().iterator().next();
		assertTrue("Didn't get the same argument back!", firstArg.equals(arg0));
		assertTrue("Argument name changed on save", firstArg.getName().equals(
				argNames[2]));
		assertTrue("Argument comment changed on save", firstArg.getComment()
				.equals(descriptions[2]));
		assertTrue("Argument default Value changed on save", firstArg
				.getDefaultValue().equals(defaultValues[2]));
		assertTrue("Argument unique changed on save", !firstArg.isUnique());
		assertTrue("Argument ordered changed on save", !firstArg.isOrdered());
		assertTrue("Argument type changed on save", firstArg.getType()
				.getName().equals(newType.getName()));
		assertTrue("Argument direction changed on save", firstArg.getDirection()
				.equals(EDirection.IN));

		// Add a second one and make sure they come back in the "correct" order
		IArgument arg1 = method1.makeArgument();
		arg1.setName(argNames[1]);
		arg1.setDefaultValue(defaultValues[1]);
		arg1.setOrdered(false);
		arg1.setUnique(false);
		arg1.setComment(descriptions[1]);
		arg1.setRefBy(refBys[1]);
		arg1.setType(newType);
		arg1.setDirection(EDirection.OUT);

		method1.addArgument(arg1);
		top.doSave(new NullProgressMonitor());

		// //////////
		// Make sure we got the right list size
		gotNewMethod = top.getMethods().iterator().next();
		assertTrue("Wrong number of Arguments ("
				+ gotNewMethod.getArguments().size() + ")", gotNewMethod
				.getArguments().size() == 2);
		// Make sure we got the right values IN THE RIGHT ORDER
		Iterator<IArgument> argIterator = gotNewMethod.getArguments()
				.iterator();
		IArgument arg = argIterator.next();
		assertTrue("Didn't get the same argument back in position 0!", arg
				.equals(arg0));
		arg = argIterator.next();
		assertTrue("Didn't get the same argument back in position 1!", arg
				.equals(arg1));

		// Test Exception list setting and changing

		String[] fqns = { "org.eclipse.one", "org.eclipse.two",
				"org.eclipse.three" };
		IException exception0 = method1.makeException();
		exception0.setFullyQualifiedName(fqns[0]);
		method1.addException(exception0);
		top.doSave(new NullProgressMonitor());

		// //////////
		// Make sure we got the right list size
		gotNewMethod = top.getMethods().iterator().next();
		assertTrue("Wrong number of Exceptions ("
				+ gotNewMethod.getExceptions().size() + ")", gotNewMethod
				.getExceptions().size() == 1);
		// Make sure we got the right values
		IException firstExc = gotNewMethod.getExceptions().iterator().next();
		assertTrue("Didn't get the same argument back!", firstExc
				.equals(exception0));
		assertTrue("Exception fqname changed on save", firstExc
				.getFullyQualifiedName().equals(fqns[0]));

		// Change the values
		exception0.setFullyQualifiedName(fqns[2]);
		top.doSave(new NullProgressMonitor());
		// Make sure we got the right list size
		gotNewMethod = top.getMethods().iterator().next();
		assertTrue("Wrong number of Exceptions ("
				+ gotNewMethod.getExceptions().size() + ")", gotNewMethod
				.getExceptions().size() == 1);
		// Make sure we got the right values
		firstExc = gotNewMethod.getExceptions().iterator().next();
		assertTrue("Didn't get the same argument back!", firstExc
				.equals(exception0));
		assertTrue("Exception fqname changed on save", firstExc
				.getFullyQualifiedName().equals(fqns[2]));

		IException exception1 = method1.makeException();
		exception1.setFullyQualifiedName(fqns[1]);
		method1.addException(exception1);
		top.doSave(new NullProgressMonitor());

		// //////////
		// Make sure we got the right list size
		gotNewMethod = top.getMethods().iterator().next();
		Collection<IException> exceptions = gotNewMethod.getExceptions();
		assertTrue("Wrong number of Exceptions (" + exceptions.size() + ")",
				exceptions.size() == 2);
		assertTrue("Missing exception", exceptions.contains(exception0));
		assertTrue("Missing exception", exceptions.contains(exception1));

		project.delete(true, new NullProgressMonitor());
	}

}
