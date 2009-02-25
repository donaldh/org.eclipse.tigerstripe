package org.eclipse.tigerstripe.workbench.sdk.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.pde.core.IBaseModel;
import org.eclipse.pde.core.plugin.IExtensions;
import org.eclipse.pde.core.plugin.IExtensionsModelFactory;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginObject;
import org.eclipse.pde.internal.ui.util.ModelModification;
import org.eclipse.pde.internal.ui.util.PDEModelUtility;

public class ModelUpdater {

	public void addDisabledPattern(IProject project, String patternName){
		
		final String EXT_PT = LocalContributions.PATTERNS_EXT_PT;
		final String PATTERN_NAME = patternName;
		
		final IFile file = project.getFile("plugin.xml");
		
		ModelModification mod = new ModelModification(file) {
			protected void modifyModel(IBaseModel model, IProgressMonitor monitor) throws CoreException {
				if (!(model instanceof IPluginModelBase))
					return;
				IPluginModelBase pluginModel = (IPluginModelBase) model;
				IExtensions extensions = pluginModel.getExtensions();
				IExtensionsModelFactory factory = pluginModel.getFactory();
				
				IPluginExtension extensionToAdd = null;
				// See if there is an existing extension to this EXT_PT
				for (IPluginExtension extension : extensions.getExtensions()){
					if (extension.getPoint().equals(EXT_PT)){
						extensionToAdd = extension;
						break;
					}
				}
				if (extensionToAdd ==  null){
					
					extensionToAdd = factory.createExtension();
					extensionToAdd.setPoint(EXT_PT);
					extensionToAdd.setName("From TSDK");
					extensions.add(extensionToAdd);
				}
				
				IPluginElement newElement = factory.createElement(extensionToAdd);
				newElement.setName("disabledPattern");
				newElement.setAttribute("patternName", PATTERN_NAME);
				extensionToAdd.add(newElement);
				
				
				
								
			}
		};
		PDEModelUtility.modifyModel(mod, new NullProgressMonitor());
	}
	
public void removeDisabledPattern(IProject project, String patternName){
		
		final String EXT_PT = LocalContributions.PATTERNS_EXT_PT;
		final String PATTERN_NAME = patternName;
		
		final IFile file = project.getFile("plugin.xml");
		
		ModelModification mod = new ModelModification(file) {
			protected void modifyModel(IBaseModel model, IProgressMonitor monitor) throws CoreException {
				if (!(model instanceof IPluginModelBase))
					return;
				IPluginModelBase pluginModel = (IPluginModelBase) model;
				IExtensions extensions = pluginModel.getExtensions();
				IExtensionsModelFactory factory = pluginModel.getFactory();
				
				IPluginExtension extensionToRemoveFrom = null;
				// Need to get the existing extension to this EXT_PT
				for (IPluginExtension extension : extensions.getExtensions()){
					if (extension.getPoint().equals(EXT_PT)){
						extensionToRemoveFrom = extension;
						for (IPluginObject child : extension.getChildren()){
							if (child instanceof IPluginElement){
								IPluginElement childElement = (IPluginElement) child;
								if (childElement.getName().equals("disabledPattern") &&
										childElement.getAttribute("patternName").getValue().equals(PATTERN_NAME)){
									extensionToRemoveFrom.remove(child);
								}
							}
				
						}
						
					}
				}				
								
			}
		};
		PDEModelUtility.modifyModel(mod, new NullProgressMonitor());
	}
	
}
