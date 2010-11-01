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
package org.eclipse.tigerstripe.workbench.internal.core.generation;

import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * An interface for listeners/actions for when tigerstripe project generation is completed
 * @see generateComplete extension point
 */
public interface IGenerateCompleteListener {
    
    public enum GenerateCompletionStatus {
        SUCCESS,
        FAILURE
    }
    
    public enum GenerateCompletionNotificationMode {
        ON_SUCCESS_ONLY,
        ON_FAILURE_ONLY,
        ALWAYS
    }
    
    /**
     * This method is executed upon completion of model generation
     * @param status SUCCESS if all plug-in generations were successful, FAILURE if one or more failed
     * @param project The project that the generation was ran on
     * @param runStatus The PluginRunStatus array for detailed information for all the generations
     * @return A run status of the action to be shown to the user for information purposes, return null if this doesn't apply
     */
    public PluginRunStatus run(GenerateCompletionStatus status, ITigerstripeModelProject project, PluginRunStatus[] runStatus);
}
