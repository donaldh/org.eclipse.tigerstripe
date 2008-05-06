package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.Utilities;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Model;

public class ModelImporter {

	private ArrayList diffList;
	private MessageList messages;
	private PrintWriter out;
	private ITigerstripeModelProject tigerstripeProject;
	
	private String importFilename;
	private String profilesDir;
	
	private Model model;
	private String modelLibrary;
	private Map<Classifier, String> classMap;
	private Map<String,IAbstractArtifact> extractedArtifacts;
	
	public ModelImporter (String importFilename,
			ITigerstripeModelProject tigerstripeProject, String profilesDir){
		diffList = new ArrayList();
		this.importFilename = importFilename;
		this.tigerstripeProject = tigerstripeProject;
		this.profilesDir = profilesDir;
		
	}
	
	public boolean doInitialLoad() throws CoreException {
		
		try {
		messages = new MessageList();
		
		File importFile = new File(importFilename);
		File logFile = new File(importFile.getParent()
				+ "/UML2ToTigerstripeImport.log");

		out = new PrintWriter(new FileOutputStream(logFile));

		String importText = "Import " + importFilename + " into "
				+ tigerstripeProject.getProjectDetails().getName();

		out.println(importText);

		Message message = new Message();
		message.setMessage(importText);
		message.setSeverity(2);
		messages.addMessage(message);

		out.println("Loading file " + importFile.getName());
		
		model = null;
		// Simply get the model - nothing fancy now!
		// Set profile stuff
		Utilities.setupPaths();
		// Add specifics to point to MODEL_PROFILE.
		Utilities.addPaths(new File(getProfilesDir()));

		
		//load Model
		File modelFile = new File(importFilename);
		model = Utilities.openModelFile(modelFile);
		
		modelLibrary = getLibraryName(new File(
				getProfilesDir()), messages);
		
		
		// Get any implementations of the ImodelTrimmer from the extension point
		IExtensionRegistry registry = Platform.getExtensionRegistry();
	      IExtensionPoint extensionPoint =
	         registry.getExtensionPoint("org.eclipse.tigerstripe.workbench.ui.UML2Import.umlImportModelTrimmer");
	      IConfigurationElement points[] =
	         extensionPoint.getConfigurationElements();
	      for (IConfigurationElement element : points){
	    	  IConfigurationElement children[] = element.getChildren("trimmer");
	    	  if(children != null && children.length != 0){
	    		  
	    		  	final IModelTrimmer trimmer  = (IModelTrimmer) children[0].createExecutableExtension("trimmer_class");
	    		  	String trimmerName = children[0].getAttribute("name");
	    		  	String trimmerText = "Model trimmed using trimmer "+trimmerName+" extension";
	    		  	Message trimMessage = new Message();
	    		  	trimMessage.setMessage(trimmerText);
	    		  	trimMessage.setSeverity(2);
	    			messages.addMessage(message);
	    			
	    		  	
	    			SafeRunner.run(new ISafeRunnable() {
	    				public void handleException(Throwable exception) {
	    					EclipsePlugin.log(exception);
	    				}

	    				public void run() throws Exception {
	    					model = trimmer.trimModel(model);
	    				}
	    				
	    			});
	    	  	}
	      }
	      
	      out.flush();
	      return true;
	      
		} catch (Exception e){
			String msgText = "Problem loading UML File " + importFilename;
			Message newMsg = new Message();
			newMsg.setMessage(msgText);
			newMsg.setSeverity(0);
			messages.addMessage(newMsg);

			out.println("Error : " + msgText);
			e.printStackTrace(out);
			out.close();
			return false;
		}
	}

	public boolean doMapping(){

		// For every class, AssociationClass, Enumeration, etc 
		// in the model, we need to provide a
		// CLASS name for the target - no need to get into a twist about attributes etc at this stage.
		
		// TODO  Filter for active types in profile.
		
		if (model == null){
			return false;
		}
		try {
		// Get any implementations of the ImodelTrimmer from the extension point
		IExtensionRegistry registry = Platform.getExtensionRegistry();
	      IExtensionPoint extensionPoint =
	         registry.getExtensionPoint("org.eclipse.tigerstripe.workbench.ui.UML2Import.umlImportModelMapper");
	      IConfigurationElement points[] =
	         extensionPoint.getConfigurationElements();
	      for (IConfigurationElement element : points){
	    	  IConfigurationElement children[] = element.getChildren("mapper");
	    	  if(children != null && children.length != 0){
	    		    final IModelMapper mapper  = (IModelMapper) children[0].createExecutableExtension("mapper_class");
	    		  	String mapperName = children[0].getAttribute("name");
	    		  	String mapperText = "Model mapped using mapper "+mapperName+" extension";
	    		  	this.classMap = mapper.getMapping(model);
	    		  	SafeRunner.run(new ISafeRunnable() {
	    				public void handleException(Throwable exception) {
	    					EclipsePlugin.log(exception);
	    				}

	    				public void run() throws Exception {
	    					classMap = mapper.getMapping(model);
	    				}
	    				
	    			});
	    	  	}
	      }
	      
		} catch (Exception e){
			// If we didn't find a good one, we will use a default,
			// so just carry on
		}
		if (this.classMap == null){
			// Use the default
			IModelMapper mapper  = new DefaultModelMapper();
			this.classMap = mapper.getMapping(model);
		}
		out.println ("MAPPINGS PASSED TO LOADER");
		for (EObject o : classMap.keySet()){
			out.println(((Classifier) o).getQualifiedName()+  "    "+classMap.get(o));
		}
		out.flush();
		return true;
		
	}

	public boolean doSecondLoad() {

		UML2TS uML2TS = new UML2TS(getClassMap());
		this.extractedArtifacts = uML2TS.extractArtifacts(model, modelLibrary, out, messages, this.tigerstripeProject);
		System.out.println(this.extractedArtifacts);
		System.out.println(uML2TS.getClassMap().size());
		System.out.println(messages.asText());
		out.flush();
		Utilities.tearDown();
		return false;
	}

	
	/** find the name of the library of primitive types */
	private String getLibraryName(File profilesDir, MessageList list) {
		try {

			File[] fList = profilesDir.listFiles();
			for (int i = 0; i < fList.length; i++) {
				File modelFile = fList[i];
				if (!modelFile.isFile())
					continue;
				if (!modelFile.getName().endsWith("uml2")
						&& !modelFile.getName().endsWith("uml"))
					continue;
				Model model;
				try {
					model = Utilities.openModelFile(modelFile);
					if (model != null)
						return model.getName();
				} catch (InvocationTargetException e) {
					TigerstripeRuntime.logErrorMessage(
							"InvocationTargetException detected", e);
					// this.out.close();
					return null;
				}
			}
		} catch (Exception e) {
			String msgText = "No UML Profile";
			Message newMsg = new Message();
			newMsg.setMessage(msgText);
			newMsg.setSeverity(Message.WARNING);
			list.addMessage(newMsg);
			// this.out.println("Error : "+msgText);
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
			// this.out.close();
			return null;
		}
		return null;

	}

	
	public ArrayList getDiffList() {
		return diffList;
	}

	public void setDiffList(ArrayList diffList) {
		this.diffList = diffList;
	}

	public ITigerstripeModelProject getTigerstripeProject() {
		return tigerstripeProject;
	}

	public String getImportFilename() {
		return importFilename;
	}

	public String getProfilesDir() {
		return profilesDir;
	}

	public Map<Classifier, String> getClassMap() {
		return classMap;
	}

	public MessageList getMessages() {
		return messages;
	}

	public Map<String, IAbstractArtifact> getExtractedArtifacts() {
		return extractedArtifacts;
	}

	public PrintWriter getOut() {
		return out;
	}

	
	
}
