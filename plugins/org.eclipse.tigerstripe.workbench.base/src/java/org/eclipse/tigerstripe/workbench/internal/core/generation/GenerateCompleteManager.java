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

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.UnsupportedAttributeException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.generation.IGenerateCompleteListener.GenerateCompletionNotificationMode;
import org.eclipse.tigerstripe.workbench.internal.core.generation.IGenerateCompleteListener.GenerateCompletionStatus;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class GenerateCompleteManager {
    public static GenerateCompleteManager INSTANCE;
    IConfigurationElement[] config;
    private static final String EXTENSION_POINT_ID = "org.eclipse.tigerstripe.workbench.base.generateComplete";
    private List<CompleteListener> listeners;
    private List<PluginRunStatus> errors;
    
    private class CompleteListener {
        IGenerateCompleteListener listener;
        GenerateCompletionNotificationMode mode;
        
        public CompleteListener(IGenerateCompleteListener listener, GenerateCompletionNotificationMode mode) {
            this.listener = listener;
            this.mode = mode;
        }
    }
    

    private GenerateCompleteManager() {
        config = Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_POINT_ID);
        listeners = new ArrayList<CompleteListener>();
        errors = new ArrayList<PluginRunStatus>();
        try {
            //lets cache the list of working listeners instead of recreating it in the notifyListeners method each time
            for (IConfigurationElement element : config) {
                IGenerateCompleteListener listener = (IGenerateCompleteListener) element.createExecutableExtension("class");
                GenerateCompletionNotificationMode mode = getNotificationMode(element.getAttribute("notificationMode"));
                listeners.add(new CompleteListener(listener, mode));
            }
        }
        catch (CoreException e) {
            //This will probably be thrown if an extension fails to initialize
            Status status = new Status(IStatus.ERROR, BasePlugin.getPluginId(), 222,
                    "An error was detected while trying to create a post generation action.", e);
            BasePlugin.logErrorStatus("Tigerstripe Post Generation Action Error Detected.", status);
            PluginRunStatus pluginStatus = new PluginRunStatus(e.getMessage());
            pluginStatus.add(status);
            errors.add(pluginStatus);
        }
        catch (UnsupportedAttributeException e) {
            //This is thrown if the supplied 'notificationMode' attribute cannot be correctly parsed
            //If this ever happens the getNotificationMode method below probably needs to be updated
            //to be sure it still matches up with the values of GenerateCompletionNotificationMode.
            Status status = new Status(IStatus.ERROR, BasePlugin.getPluginId(), 222,
                    "An error was detected while trying to get a post action notification mode.", e);
            BasePlugin.logErrorStatus("Tigerstripe Post Generation Action Error Detected.", status);
            PluginRunStatus pluginStatus = new PluginRunStatus(e.getMessage());
            pluginStatus.add(status);
            errors.add(pluginStatus);
        }
    }
    
    public static GenerateCompleteManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GenerateCompleteManager();
        }
        return INSTANCE;
    }
    
    public PluginRunStatus[] notifyListeners(ITigerstripeModelProject project, PluginRunStatus[] runStatus) {
        GenerateCompletionStatus success = GenerateCompletionStatus.SUCCESS;
        //Look through the statuses to see if any generations failed
        for (PluginRunStatus status : runStatus) {
            if (!status.isOK()) {
                success = GenerateCompletionStatus.FAILURE; //If so lets say generation was a failure
            }
        }
        
        final List<PluginRunStatus> result = new ArrayList<PluginRunStatus>();
        final GenerateCompletionStatus s = success;
        final ITigerstripeModelProject p = project;
        final PluginRunStatus[] rs = runStatus;
        for (final CompleteListener completeListener : listeners) {
            final IGenerateCompleteListener listener = completeListener.listener;
            final GenerateCompletionNotificationMode mode = completeListener.mode;
            if (mode == GenerateCompletionNotificationMode.ALWAYS ||
                    (mode == GenerateCompletionNotificationMode.ON_SUCCESS_ONLY && s == GenerateCompletionStatus.SUCCESS) ||
                    (mode == GenerateCompletionNotificationMode.ON_FAILURE_ONLY && s == GenerateCompletionStatus.FAILURE)) {
                ISafeRunnable runnable = new ISafeRunnable() {
                    public void handleException(Throwable e) {
                        //The extension just threw an exception, we should probably log it
                        Status status = new Status(IStatus.ERROR, BasePlugin.getPluginId(), 222,
                                "An error was detected while executing a post generation action.", e);
                        BasePlugin.logErrorStatus("Tigerstripe Post Generation Action Error Detected.", status);
                        PluginRunStatus pluginStatus = new PluginRunStatus(e.getMessage());
                        pluginStatus.add(status);
                        result.add(pluginStatus);
                    }
                    
                    public void run() throws Exception {
                        PluginRunStatus pluginStatus = listener.run(s, p, rs);
                        if (pluginStatus != null) {
                            result.add(pluginStatus);
                        }
                    }
                };
                SafeRunner.run(runnable);
            }
        }
        if (errors.size() > 0) {
            result.addAll(errors);
        }
        if (result.size() > 0) {
            return result.toArray(new PluginRunStatus[result.size()]);
        }
        return new PluginRunStatus[0];
    }

    /**
     * @param attribute The String representation of the notificationMode of the extension action
     * @return The enumeration value for the string representation
     * @throws If the attribute fails to parse to an enum
     */
    private GenerateCompletionNotificationMode getNotificationMode(
            String attribute) throws UnsupportedAttributeException {
        if (attribute.equals(GenerateCompletionNotificationMode.ALWAYS.toString())) {
            return GenerateCompletionNotificationMode.ALWAYS;
        }
        if (attribute.equals(GenerateCompletionNotificationMode.ON_SUCCESS_ONLY.toString())) {
            return GenerateCompletionNotificationMode.ON_SUCCESS_ONLY;
        }
        if (attribute.equals(GenerateCompletionNotificationMode.ON_FAILURE_ONLY.toString())) {
            return GenerateCompletionNotificationMode.ON_FAILURE_ONLY;
        }
        throw new UnsupportedAttributeException("Unable to parse a valid notification mode from the attribute 'notificationMode' with value '" + attribute + "'.", attribute);
    }
}
