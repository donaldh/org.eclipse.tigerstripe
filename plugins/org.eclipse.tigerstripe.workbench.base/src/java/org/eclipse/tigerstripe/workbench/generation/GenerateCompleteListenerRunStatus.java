/******************************************************************************
 * Copyright (c) 2009 by Cisco Systems, Inc. All rights reserved.
 * 
 * This software contains proprietary information which shall not be reproduced
 * or transferred to other documents and shall not be disclosed to others or
 * used for manufacturing or any other purpose without prior permission of Cisco
 * Systems.
 * 
 *****************************************************************************/
package org.eclipse.tigerstripe.workbench.generation;

/**
 * @author danijoh2
 *
 */
public class GenerateCompleteListenerRunStatus extends PluginRunStatus {
    public GenerateCompleteListenerRunStatus(String message) {
        super(message);
    }
    
    @Override
    public String toString() {
        return toString(false);
    }
    
    @Override
    public String toString(boolean includeHtml) {
        return (includeHtml) ? super.getMessage() + "<br/>" : super.getMessage() + "\n";
    }

}
