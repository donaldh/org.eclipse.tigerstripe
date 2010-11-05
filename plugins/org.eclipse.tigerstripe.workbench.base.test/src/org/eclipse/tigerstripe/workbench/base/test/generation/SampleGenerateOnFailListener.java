/******************************************************************************
 * Copyright (c) 2009 by Cisco Systems, Inc. All rights reserved.
 * 
 * This software contains proprietary information which shall not be reproduced
 * or transferred to other documents and shall not be disclosed to others or
 * used for manufacturing or any other purpose without prior permission of Cisco
 * Systems.
 * 
 *****************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test.generation;

import org.eclipse.tigerstripe.workbench.generation.GenerateCompleteListenerRunStatus;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.core.generation.IGenerateCompleteListener;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * @author danijoh2
 *
 */
public class SampleGenerateOnFailListener implements IGenerateCompleteListener {
    public static String MESSAGE = "SampleGenerateOnFailListener";
    public GenerateCompleteListenerRunStatus run(GenerateCompletionStatus success,
            ITigerstripeModelProject project, PluginRunStatus[] runStatus) {
        return new GenerateCompleteListenerRunStatus(MESSAGE + " " + success);
    }
}
