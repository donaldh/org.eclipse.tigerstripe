package org.eclipse.tigerstripe.refactor.suite;

import org.eclipse.tigerstripe.refactor.references.ProjectReferences;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ShortReferenceProjectTestSuite extends TestCase {
   public static Test suite(){
	   TestSuite suite = new TestSuite();
	   
	   suite.addTestSuite(SimpleCleanWorkspace.class);
	   
	   suite.addTestSuite(ProjectReferences.class);
	   
	   suite.addTestSuite(DeleteProject.class);
	   
	   return suite;
   }
}
