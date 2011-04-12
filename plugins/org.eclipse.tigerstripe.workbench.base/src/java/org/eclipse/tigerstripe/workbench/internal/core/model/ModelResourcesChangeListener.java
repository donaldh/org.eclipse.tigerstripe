/******************************************************************************
 * Copyright (c) 2009 by Cisco Systems, Inc. All rights reserved.
 * 
 * This software contains proprietary information which shall not be reproduced
 * or transferred to other documents and shall not be disclosed to others or
 * used for manufacturing or any other purpose without prior permission of Cisco
 * Systems.
 * 
 *****************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.model;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.IModelAnnotationChangeDelta;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * @author danijoh2
 *
 */
public class ModelResourcesChangeListener implements ITigerstripeChangeListener {
    
    public static String TARGET_OUT_OF_DATE_MARKER = "org.eclipse.tigerstripe.workbench.base.targetOutOfDateMarker";
    
    public static String MODEL_RESOURCES_CHANGE_LISTENER_ID = BasePlugin.PLUGIN_ID + ".ModelResourcesChangeListener";
    
    protected void markTargetOutOfDate(ITigerstripeModelProject project) {
        if (project == null)
            return;
        try {
            IProjectDetails details = project.getProjectDetails();
            if (details != null) {
                String outputDir = details.getOutputDirectory();
                final String projectName = project.getName();
                IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
                if (proj != null) {
                    final IResource folder = proj.findMember(outputDir);
                    if (folder != null) {
                        WorkspaceJob job = new WorkspaceJob("Add Out-of-date Marker") {
                            @Override
                            public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
                                try {
                                    IMarker[] markers = folder.findMarkers(TARGET_OUT_OF_DATE_MARKER, true, IResource.DEPTH_INFINITE);
                                    if (markers.length <= 0) {
                                        IMarker marker = folder.createMarker(TARGET_OUT_OF_DATE_MARKER);
                                        marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
                                        marker.setAttribute(IMarker.MESSAGE, "Target folder may be out of date due to changes in the project. Running Tigerstripe generation on the project will remove this message.");
                                        return new Status(IStatus.OK, MODEL_RESOURCES_CHANGE_LISTENER_ID, "Added target folder out-of-date marker on project " + projectName);
                                    }
                                    return new Status(IStatus.OK, BasePlugin.PLUGIN_ID, "Target folder already marked out-of-date on project " + projectName);
                                } catch (Exception e) {
                                    throw new CoreException(new Status(IStatus.ERROR, MODEL_RESOURCES_CHANGE_LISTENER_ID, "Failed to add target folder out-of-date marker on project " + projectName));
                                }
                            }
                        };
                        job.setRule(folder);
                        job.schedule();
                    }
                }
            }
        } catch (Exception e) {
            // IGNORE, this is just a convenience framework anyway.
        }
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener#projectAdded(org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject)
     */
    public void projectAdded(IAbstractTigerstripeProject project) {
        return;
    }

    /* (non-Javadoc)
     * @see org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener#projectDeleted(java.lang.String)
     */
    public void projectDeleted(String projectName) {
        return;
    }

    /* (non-Javadoc)
     * @see org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener#descriptorChanged(org.eclipse.core.resources.IResource)
     */
    public void descriptorChanged(IResource changedDescriptor) {
       
        ITigerstripeModelProject modelProject = TigerstripeCore.findModelProjectByID(changedDescriptor.getProject().getName());
        markTargetOutOfDate(modelProject);
    }

    /* (non-Javadoc)
     * @see org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener#modelChanged(org.eclipse.tigerstripe.workbench.IModelChangeDelta[])
     */
    public void modelChanged(IModelChangeDelta[] delta) {
       
        for (int i = 0; i < delta.length; i++) {
            try {
                for (ITigerstripeModelProject project : delta[i].getAffectedProjects()) {
                    markTargetOutOfDate(project);
                }
            } catch (Exception e) {
                // IGNORE, this is just a convenience framework anyway.
            }
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener#annotationChanged(org.eclipse.tigerstripe.workbench.IModelAnnotationChangeDelta[])
     */
    public void annotationChanged(IModelAnnotationChangeDelta[] delta) {
        
        for (IModelAnnotationChangeDelta change : delta) {
            String uri = change.getAffectedModelComponentURI().toString();
            
            int start = uri.indexOf("/");
            uri = uri.substring(start+1);
            
            int end = uri.indexOf("/");
            String projectName;
            if (end == -1)
                projectName = uri;
            else
                projectName = uri.substring(0, end);
            
            ITigerstripeModelProject project = TigerstripeCore.findModelProjectByID(projectName);
            markTargetOutOfDate(project);
        }
        return;
    }

    /* (non-Javadoc)
     * @see org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener#artifactResourceChanged(org.eclipse.core.resources.IResource)
     */
    public void artifactResourceChanged(IResource changedArtifactResource) {
        
        ITigerstripeModelProject modelProject = TigerstripeCore.findModelProjectByID(changedArtifactResource.getProject().getName());
        markTargetOutOfDate(modelProject);
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener#artifactResourceAdded(org.eclipse.core.resources.IResource)
     */
    public void artifactResourceAdded(IResource addedArtifactResource) {
       
        ITigerstripeModelProject modelProject = TigerstripeCore.findModelProjectByID(addedArtifactResource.getProject().getName());
        markTargetOutOfDate(modelProject);
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener#artifactResourceRemoved(org.eclipse.core.resources.IResource)
     */
    public void artifactResourceRemoved(IResource removedArtifactResource) {
       
        ITigerstripeModelProject modelProject = TigerstripeCore.findModelProjectByID(removedArtifactResource.getProject().getName());
        markTargetOutOfDate(modelProject);
        
    }
    
    public void activeFacetChanged(ITigerstripeModelProject project) {
        markTargetOutOfDate(project);
    }
}
