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
		final String elementName = "disabledPattern";
		final String patternNameAttributeName = "patternName";
		
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
				newElement.setName(elementName);
				newElement.setAttribute(patternNameAttributeName, PATTERN_NAME);
				extensionToAdd.add(newElement);
				
				
				
								
			}
		};
		PDEModelUtility.modifyModel(mod, new NullProgressMonitor());
	}

	public void removeDisabledPattern(IProject project, String patternName){

		final String EXT_PT = LocalContributions.PATTERNS_EXT_PT;
		final String elementName = "disabledPattern";
		
		final String patternNameAttributeName = "patternName";
		
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
								if (childElement.getName().equals(elementName) &&
										childElement.getAttribute(patternNameAttributeName).getValue().equals(PATTERN_NAME)){
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
	
	public void addPatternDefintion(IProject project, String patternFileName, String validatorClass){
		
		final String EXT_PT = LocalContributions.PATTERNS_EXT_PT;
		final String elementName = "patternDefinition";
		
		final String validatorClassAttributeName = "validator_class";
		final String patternFileAttributeName = "patternFile";
		
		final String PATTERN_FILE_NAME = patternFileName;
		final String VALIDATOR_CLASS = validatorClass;
		
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
				newElement.setName(elementName);
				newElement.setAttribute(patternFileAttributeName, PATTERN_FILE_NAME);
				if (! VALIDATOR_CLASS.equals("")){
					newElement.setAttribute(validatorClassAttributeName, VALIDATOR_CLASS);
				}
				extensionToAdd.add(newElement);
				
				
				
								
			}
		};
		PDEModelUtility.modifyModel(mod, new NullProgressMonitor());
	}
	
	public void removePatternDefinition(IProject project, String patternFileName, String validatorClass){

		final String EXT_PT = LocalContributions.PATTERNS_EXT_PT;
		final String elementName = "patternDefinition";
		
		final String validatorClassAttributeName = "validator_class";
		final String patternFileAttributeName = "patternFile";
		
		final String PATTERN_FILE_NAME = patternFileName;
		final String VALIDATOR_CLASS = validatorClass;

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
								if (childElement.getName().equals(elementName) &&
										childElement.getAttribute(patternFileAttributeName).getValue().equals(PATTERN_FILE_NAME)){
									if(childElement.getAttribute(validatorClassAttributeName) != null &&
											childElement.getAttribute(validatorClassAttributeName).getValue().equals(VALIDATOR_CLASS)){
										extensionToRemoveFrom.remove(child);

									}

								}
							}

						}

					}
				}				

			}
		};
		PDEModelUtility.modifyModel(mod, new NullProgressMonitor());
	}

	public void addAudit(IProject project, String name, String className){
		
		final String EXT_PT = LocalContributions.AUDIT_EXT_PT;
		final String elementName = "customAuditRule";
		
		final String classAttributeName = "auditorClass";
		final String nameAttributeName = "name";
		
		final String NAME = name;
		final String CLASS_NAME = className;
		
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
				newElement.setName(elementName);
				newElement.setAttribute(nameAttributeName, NAME);
				newElement.setAttribute(classAttributeName, CLASS_NAME);
				extensionToAdd.add(newElement);
				
				
				
								
			}
		};
		PDEModelUtility.modifyModel(mod, new NullProgressMonitor());
	}
	
	public void removeAudit(IProject project, String name, String className){

		final String EXT_PT = LocalContributions.AUDIT_EXT_PT;
		final String elementName = "customAuditRule";
		
		final String classAttributeName = "auditorClass";
		final String nameAttributeName = "name";
		
		final String NAME = name;
		final String CLASS_NAME = className;

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
//								if (childElement.getName().equals(elementName) &&
//										childElement.getAttribute(nameAttributeName).getValue().equals(NAME)){
									if(childElement.getAttribute(classAttributeName) != null &&
											childElement.getAttribute(classAttributeName).getValue().equals(CLASS_NAME)){
										extensionToRemoveFrom.remove(child);
									}
//								}
							}

						}

					}
				}				

			}
		};
		PDEModelUtility.modifyModel(mod, new NullProgressMonitor());
	}
	
	public void addDecorator(IProject project, String className){
		
		final String EXT_PT = LocalContributions.DECORATOR_EXT_PT;
		final String elementName = "decorator";
		
		final String classAttributeName = "class";
		final String CLASS_NAME = className;
		
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
				newElement.setName(elementName);
				newElement.setAttribute(classAttributeName, CLASS_NAME);
				extensionToAdd.add(newElement);
				
				
				
								
			}
		};
		PDEModelUtility.modifyModel(mod, new NullProgressMonitor());
	}
	
	public void removeDecorator(IProject project, String className){

		final String EXT_PT = LocalContributions.DECORATOR_EXT_PT;
		final String elementName = "decorator";
		
		final String classAttributeName = "class";
		

		final String CLASS_NAME = className;

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

								if(childElement.getAttribute(classAttributeName) != null &&
										childElement.getAttribute(classAttributeName).getValue().equals(CLASS_NAME)){

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
	
	public void addNaming(IProject project, String name, String className){
		
		final String EXT_PT = LocalContributions.NAMING_EXT_PT;
		final String elementName = "customNamingRule";
		
		final String classAttributeName = "namingClass";
		final String nameAttributeName = "name";
		
		final String NAME = name;
		final String CLASS_NAME = className;
		
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
				newElement.setName(elementName);
				newElement.setAttribute(nameAttributeName, NAME);
				newElement.setAttribute(classAttributeName, CLASS_NAME);
				extensionToAdd.add(newElement);
				
				
				
								
			}
		};
		PDEModelUtility.modifyModel(mod, new NullProgressMonitor());
	}

	public void removeNaming(IProject project, String name, String className){

		final String EXT_PT = LocalContributions.NAMING_EXT_PT;
		final String elementName = "customNamingRule";
		
		final String classAttributeName = "namingClass";
		final String nameAttributeName = "name";
		
		final String NAME = name;
		final String CLASS_NAME = className;

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
//								if (childElement.getName().equals(elementName) &&
//										childElement.getAttribute(nameAttributeName).getValue().equals(NAME)){
									if(childElement.getAttribute(classAttributeName) != null &&
											childElement.getAttribute(classAttributeName).getValue().equals(CLASS_NAME)){
										extensionToRemoveFrom.remove(child);
									}
//								}
							}

						}

					}
				}				

			}
		};
		PDEModelUtility.modifyModel(mod, new NullProgressMonitor());
	}
	
	
public void addComponentIconProvider(IProject project, String name, String className){
		
	final String EXT_PT = LocalContributions.METADATA_EXT_PT;
	final String elementName = "modelComponentIconProvider";
	
	final String classAttributeName = "provider";
	final String nameAttributeName = "artifactType";
	
	final String NAME = name;
	final String CLASS_NAME = className;
		
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
				newElement.setName(elementName);
				newElement.setAttribute(nameAttributeName, NAME);
				newElement.setAttribute(classAttributeName, CLASS_NAME);

				extensionToAdd.add(newElement);
				
				
				
								
			}
		};
		PDEModelUtility.modifyModel(mod, new NullProgressMonitor());
	}

	public void removeComponentIconProvider(IProject project, String name, String className){

		final String EXT_PT = LocalContributions.METADATA_EXT_PT;
		final String elementName = "modelComponentIconProvider";
		
		final String classAttributeName = "provider";
		final String nameAttributeName = "artifactType";
		
		final String NAME = name;
		final String CLASS_NAME = className;

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
								if (childElement.getName().equals(elementName) &&
										childElement.getAttribute(nameAttributeName).getValue().equals(NAME)){
									if(childElement.getAttribute(classAttributeName) != null &&
											childElement.getAttribute(classAttributeName).getValue().equals(CLASS_NAME)){
										extensionToRemoveFrom.remove(child);
									}
								}
							}

						}

					}
				}				

			}
		};
		PDEModelUtility.modifyModel(mod, new NullProgressMonitor());
	}
	
	
	
	
	public void addArtifactMetadata(IProject project, String artifactType, String userLabel, 
			boolean hasFields,
			boolean hasLiterals,
			boolean hasMethods,
			String icon, String icon_new , String icon_grey){
		
		final String EXT_PT = LocalContributions.METADATA_EXT_PT;
		final String elementName = "artifactMetadata";
		
		final String artifactTypeAttributeName = "artifactType";
		final String labelAttributeName = "userLabel";
		
		final String fieldsAttributeName = "hasFields";
		final boolean FIELDS = hasFields;
		
		final String literalsAttributeName = "hasLiterals";
		final boolean LITERALS = hasFields;
		
		final String methodsAttributeName = "hasMethods";
		final boolean METHODS = hasFields;
		
		final String ARTIFACT_TYPE = artifactType;
		final String USER_LABEL = userLabel;
		
		final String iconAttributeName = "icon";
		final String ICON = icon;
		
		final String icon_newAttributeName = "icon_new";
		final String ICON_NEW = icon_new;
		
		final String icon_gsAttributeName = "icon_gs";
		final String ICON_GREY = icon_grey;
		
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
				newElement.setName(elementName);
				newElement.setAttribute(artifactTypeAttributeName, ARTIFACT_TYPE);
				newElement.setAttribute(labelAttributeName, USER_LABEL);
				newElement.setAttribute(fieldsAttributeName, Boolean.toString(FIELDS));
				newElement.setAttribute(literalsAttributeName, Boolean.toString(LITERALS));
				newElement.setAttribute(methodsAttributeName, Boolean.toString(METHODS));
				if (!ICON.equals(""))
						newElement.setAttribute(iconAttributeName, ICON);
				if (!ICON_NEW.equals(""))
					newElement.setAttribute(icon_newAttributeName, ICON_NEW);
				if (!ICON_GREY.equals(""))
					newElement.setAttribute(icon_gsAttributeName, ICON_GREY);
				extensionToAdd.add(newElement);			
								
			}
		};
		PDEModelUtility.modifyModel(mod, new NullProgressMonitor());
	}
	
	public void removeArtifactMetadata(IProject project, String name, String userLabel){

		final String EXT_PT = LocalContributions.METADATA_EXT_PT;
		final String elementName = "artifactMetadata";
		
		final String labelAttributeName = "userLabel";
		final String nameAttributeName = "artifactType";
		
		final String NAME = name;
		final String USER_LABEL = userLabel;

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
								if (childElement.getName().equals(elementName) &&
										childElement.getAttribute(nameAttributeName).getValue().equals(NAME)){
									if(childElement.getAttribute(labelAttributeName) != null &&
											childElement.getAttribute(labelAttributeName).getValue().equals(USER_LABEL)){
										extensionToRemoveFrom.remove(child);
									}
								}
							}

						}

					}
				}				

			}
		};
		PDEModelUtility.modifyModel(mod, new NullProgressMonitor());
	}
	
	public void addArtifactIcon(IProject project, String name, String icon, String icon_new , String icon_grey){
		
		final String EXT_PT = LocalContributions.METADATA_EXT_PT;
		final String elementName = "artifactIcon";
		
		final String nameAttributeName = "artifactName";
		final String NAME = name;
		
		final String iconAttributeName = "icon";
		final String ICON = icon;
		
		final String icon_newAttributeName = "icon_new";
		final String ICON_NEW = icon_new;
		
		final String icon_gsAttributeName = "icon_gs";
		final String ICON_GREY = icon_grey;
		
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
				newElement.setName(elementName);
				newElement.setAttribute(nameAttributeName, NAME);
				newElement.setAttribute(iconAttributeName, ICON);
				newElement.setAttribute(icon_newAttributeName, ICON_NEW);
				newElement.setAttribute(icon_gsAttributeName, ICON_GREY);
				extensionToAdd.add(newElement);
				
				
				
								
			}
		};
		PDEModelUtility.modifyModel(mod, new NullProgressMonitor());
	}
	
	public void removeArtifactIcon(IProject project, String name){

		final String EXT_PT = LocalContributions.METADATA_EXT_PT;
		final String elementName = "artifactIcon";
		
		final String nameAttributeName = "artifactName";
		
		final String NAME = name;

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
								if (childElement.getName().equals(elementName) &&
										childElement.getAttribute(nameAttributeName).getValue().equals(NAME)){
									
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
