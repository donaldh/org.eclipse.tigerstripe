/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - danijoh2
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test.generation;

import junit.framework.TestCase;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.generation.GenerateCompleteManager;
import org.eclipse.tigerstripe.workbench.internal.core.generation.IGenerateCompleteListener.GenerateCompletionStatus;

public class TestGenerateCompleteFramework extends TestCase {
    GenerateCompleteManager manager;

    @Override
    protected void setUp() {
        manager = GenerateCompleteManager.getInstance();
    }

    @Override
    protected void tearDown() {
        manager = null;
    }

    public void testSuccessfulGeneration() {
        PluginRunStatus[] runStatus = new PluginRunStatus[] { new PluginRunStatus(
                "Success") };
        PluginRunStatus[] results = manager.notifyListeners(null, runStatus);
        assertEquals("Resulting status not expected length.", 2, results.length);// SUCCESS and ALWAYS listeners
        String successListenerMessage = SampleGenerateOnSuccessListener.MESSAGE + " " + GenerateCompletionStatus.SUCCESS;
        String alwaysListenerMessage = SampleGenerateAlwaysListener.MESSAGE + " " + GenerateCompletionStatus.SUCCESS;
        if (!((results[0].getMessage().equals(successListenerMessage) || 
            results[1].getMessage().equals(successListenerMessage)) &&
            (results[0].getMessage().equals(alwaysListenerMessage) ||
            results[1].getMessage().equals(alwaysListenerMessage)))) {
            //If the messages don't match what we expected
            fail("Results weren't as expected.\nRecieved Messages: 1)" + results[0].getMessage() + "2)" + results[1].getMessage() + "\nExpected Messages: 1)" + successListenerMessage + " 2)" + alwaysListenerMessage);
        }
    }

    public void testPartialFailedGeneration() {
        PluginRunStatus successStatus = new PluginRunStatus("Success");
        Status status = new Status(IStatus.ERROR, BasePlugin.getPluginId(),
                "Failure");
        PluginRunStatus failStatus = new PluginRunStatus("Failure");
        failStatus.add(status);
        PluginRunStatus[] runStatus = new PluginRunStatus[] { successStatus,
                failStatus };
        PluginRunStatus[] results = manager.notifyListeners(null, runStatus);
        assertEquals("Resulting status not expected length.", 2, results.length);// FAILURE and ALWAYS listeners
        String failListenerMessage = SampleGenerateOnFailListener.MESSAGE + " " + GenerateCompletionStatus.FAILURE;
        String alwaysListenerMessage = SampleGenerateAlwaysListener.MESSAGE + " " + GenerateCompletionStatus.FAILURE;
        if (!((results[0].getMessage().equals(failListenerMessage) || 
            results[1].getMessage().equals(failListenerMessage)) &&
            (results[0].getMessage().equals(alwaysListenerMessage) ||
            results[1].getMessage().equals(alwaysListenerMessage)))) {
            //If the messages don't match what we expected
            fail("Results weren't as expected.\nRecieved Messages: 1)" + results[0].getMessage() + "2)" + results[1].getMessage() + "\nExpected Messages: 1)" + failListenerMessage + " 2)" + alwaysListenerMessage);
        }
    }

    public void testFailedGeneration() {
        Status status = new Status(IStatus.ERROR, BasePlugin.getPluginId(),
                "Failure");
        PluginRunStatus failStatus = new PluginRunStatus("Failure");
        failStatus.add(status);
        PluginRunStatus[] runStatus = new PluginRunStatus[] { failStatus };
        PluginRunStatus[] results = manager.notifyListeners(null, runStatus);
        assertEquals("Resulting status not expected length.", 2, results.length);// FAILURE and ALWAYS listeners
        String failListenerMessage = SampleGenerateOnFailListener.MESSAGE + " " + GenerateCompletionStatus.FAILURE;
        String alwaysListenerMessage = SampleGenerateAlwaysListener.MESSAGE + " " + GenerateCompletionStatus.FAILURE;
        if (!((results[0].getMessage().equals(failListenerMessage) || 
            results[1].getMessage().equals(failListenerMessage)) &&
            (results[0].getMessage().equals(alwaysListenerMessage) ||
            results[1].getMessage().equals(alwaysListenerMessage)))) {
            //If the messages don't match what we expected
            fail("Results weren't as expected.\nRecieved Messages: 1)" + results[0].getMessage() + "2)" + results[1].getMessage() + "\nExpected Messages: 1)" + failListenerMessage + " 2)" + alwaysListenerMessage);
            }
    }
}
