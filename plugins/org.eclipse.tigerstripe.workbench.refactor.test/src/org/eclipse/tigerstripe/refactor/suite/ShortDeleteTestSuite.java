package org.eclipse.tigerstripe.refactor.suite;

import org.eclipse.tigerstripe.refactor.delete.DeleteEnt1;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ShortDeleteTestSuite extends TestCase {
	
	public static Test suite(){
		TestSuite suite = new TestSuite();
		
		suite.addTestSuite(SimpleCleanWorkspace.class);
		
		suite.addTestSuite(DeleteEnt1.class);
		
		suite.addTestSuite(DeleteProject.class);
		
		return suite;
	}

}
