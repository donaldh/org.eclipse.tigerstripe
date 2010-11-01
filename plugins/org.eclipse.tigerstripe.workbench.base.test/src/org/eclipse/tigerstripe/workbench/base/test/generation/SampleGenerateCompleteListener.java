/******************************************************************************
 * Copyright (c) 2009 by Cisco Systems, Inc. All rights reserved.
 * 
 * This software contains proprietary information which shall not be reproduced
 * or transferred to other documents and shall not be disclosed to others or
 * used for manufacturing or any other purpose without prior permission of Cisco
 * Systems.
 * 
 * Contributors:
 *    Cisco Systems, Inc. - danijoh2
 *****************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test.generation;

import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.core.generation.IGenerateCompleteListener;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class SampleGenerateCompleteListener implements IGenerateCompleteListener {
    public static String MESSAGE = "SampleGenerateCompleteListener";
    public PluginRunStatus run(GenerateCompletionStatus success,
            ITigerstripeModelProject project, PluginRunStatus[] runStatus) {
        PluginRunStatus status = new PluginRunStatus(MESSAGE + " " + success);
        return status;
    }
}
