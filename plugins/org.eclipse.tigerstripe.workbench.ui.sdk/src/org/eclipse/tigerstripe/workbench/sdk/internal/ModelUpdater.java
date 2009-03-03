package org.eclipse.tigerstripe.workbench.sdk.internal;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

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
import org.w3c.dom.Node;

public class ModelUpdater {
	
	public void addSimpleExtension(IProject project, String extensionPoint, String elementName, Map<String, String> attributes){
		
		addSimpleExtension( project,  extensionPoint,  elementName,  attributes, true);
			
	}
	
	public void addSimpleExtension(IProject project, String extensionPoint, String elementName, Map<String, String> attributes, boolean inSequence){
		
		final String EXT_PT = extensionPoint;
		final String ELEMENT_NAME = elementName;
		
		final Map<String, String> attributeMap = attributes;
		final IFile file = project.getFile("plugin.xml");
		final boolean inSequenceAllowed = inSequence;
		
		ModelModification mod = new ModelModification(file) {
			protected void modifyModel(IBaseModel model, IProgressMonitor monitor) throws CoreException {
				if (!(model instanceof IPluginModelBase))
					return;
				IPluginModelBase pluginModel = (IPluginModelBase) model;
				IExtensions extensions = pluginModel.getExtensions();
				IExtensionsModelFactory factory = pluginModel.getFactory();
				
				IPluginExtension extensionToAdd = null;
				
				// Need to handle the case where a new element has to have ots own 
				// extension - ie NOT a sequence.
		
				if (inSequenceAllowed){
					// See if there is an existing extension to this EXT_PT
					for (IPluginExtension extension : extensions.getExtensions()){
						if (extension.getPoint().equals(EXT_PT)){
							extensionToAdd = extension;
							break;
						}
					}
				}
				if (extensionToAdd == null){
					
					extensionToAdd = factory.createExtension();
					extensionToAdd.setPoint(EXT_PT);
					extensionToAdd.setName("From TSDK");
					extensions.add(extensionToAdd);
				}
				
				IPluginElement newElement = factory.createElement(extensionToAdd);
				newElement.setName(ELEMENT_NAME);
				for (String key : attributeMap.keySet()){
					if ( ! attributeMap.get(key).equals("")){
						newElement.setAttribute(key, attributeMap.get(key));
					}
				}
				extensionToAdd.add(newElement);				
			}
		};
		PDEModelUtility.modifyModel(mod, new NullProgressMonitor());
		
	}
	
	public void removeContribution(IProject project, String extensionPoint, String element ,IPluginElement pluginElement){

		final String EXT_PT = extensionPoint; //LocalContributions.ANNOTATIONS_EXT_PT;
		final String elementName = element; //"definition";
		
		final IFile file = project.getFile("plugin.xml");
		System.out.println(pluginElement);
		final IPluginElement pio = pluginElement;

		ModelModification mod = new ModelModification(file) {
			protected void modifyModel(IBaseModel model, IProgressMonitor monitor) throws CoreException {
				if (!(model instanceof IPluginModelBase))
					return;
				IPluginModelBase pluginModel = (IPluginModelBase) model;
				IExtensions extensions = pluginModel.getExtensions();
				IPluginExtension extensionToRemoveFrom = null;
				// Need to get the existing extension to this EXT_PT
				for (IPluginExtension extension : extensions.getExtensions()){
					if (extension.getPoint().equals(EXT_PT)){
						extensionToRemoveFrom = extension;
						for (IPluginObject child : extension.getChildren()){
							//System.out.println(child);
							// TODO This removes exact duplicates - is that a good thing?
							if ( child instanceof IPluginElement){
								if (ElementComparer.compareElements((IPluginElement) child, pio)){
									extensionToRemoveFrom.remove(child);
									// TODO - If the extension is now empty, remove it too
									if (extension.getChildCount() == 0){
										extensions.remove(extension);
									}
									break;
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
