/******************************************************************************* 
 * 
 * Copyright (c) 2008 Cisco Systems, Inc. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 *    Cisco Systems, Inc. - dkeysell
********************************************************************************/

package org.eclipse.tigerstripe.generators.models;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;


public abstract class AbstractComponentModel {
	
	protected static IPluginConfig pluginRef;
	
	private IModelComponent component;
	
	public AbstractComponentModel() {
		super();
	}

	public void build(IModelComponent component){
		this.component = component;
	}
	
	public String getName(){
		return component.getName();
	}
	
	public String getComment(){
		return component.getComment();
	}
	
	public Collection<IStereotypeInstance> getStereotypeInstances() {
		return component.getStereotypeInstances();
	}
	
	public String findTaggedValue(String tagToGet){
		Collection<IStereotypeInstance> stereos = getStereotypeInstances();
		for (IStereotypeInstance stereo :stereos){
			if (stereo.getName().equals(tagToGet)) {
				try {
					String[] values = stereo.getAttributeValues("value");
					if (values.length >0){
					String value = values[0];
					return value;
					} else 
					{
						return "";
					}
				}
				 catch (TigerstripeException t){
					 try {
						 String value = stereo.getAttributeValue("value");
						 return value;
					 } catch (TigerstripeException t2){
						 // Basically this thing isn't set
						 return "";
					 }
				 }
			}
		}
		return null;
	}
	
	/** 
	 * Use for an array Stereotype attribute
	 * @param tagToGet
	 * @return
	 */
	public Collection findTaggedValues( String tagToGet){
		Collection<String> values = new ArrayList<String>();
		Collection<IStereotypeInstance> stereos = getStereotypeInstances();
		for (IStereotypeInstance stereo :stereos){	
			if (stereo.getName().equals(tagToGet)) {
				try {
			
					String[] valueEntries = stereo.getAttributeValues("value");
					for (int v=0;v<valueEntries.length;v++){
					    values.add(valueEntries[v].trim());	
					}
					
				}
				 catch (TigerstripeException t){
					 return null;
				 }
			}
		}
		return values;
	}
	
	public IModelComponent.EVisibility getVisibility() {
		return component.getVisibility();
	}
	
	public boolean isInActiveFacet() throws TigerstripeException {
		return component.isInActiveFacet();
	}

	public String getVisibilityStr(){
		
		if (getVisibility() == IModelComponent.EVisibility.PUBLIC){
			return "public";
		}else if (getVisibility() == IModelComponent.EVisibility.PROTECTED){
			return "protected";
		}else if (getVisibility() == IModelComponent.EVisibility.PRIVATE){
			return "private";
		} else {
			return "public";
		}
	}

	public String getDocumentation(String leader){
		int lineLength = 64; 
		return getDocumentation(leader, lineLength, true);
	}
	
	
	public String getDocumentation(String leader, int lineLength, boolean htmlStyle){
		// split into lines and pre-pend the "leader" on each one.

		// if htmlStyle, then each paragraph should be containied in <p> </p> tags.
		// A paragraph is delimited by an empty line?
		
		
		// Make the line max of lineLength Chars
		
		if (getComment().length() == 0){
			return leader;
		}
		String fullComment = "";
		String[] paras =  getComment().split("\n\n");
		for (String para : paras){
			String newComment = "";
			String[] lines = para.split("\n");
			for (String line: lines){

				while (line.length() > lineLength-leader.length()){
					String firstLineFullWords;
					String remains;
					
					String firstline = line.substring(0,lineLength-leader.length());
					if (firstline.contains(" ")){
						firstLineFullWords = firstline.substring(0,firstline.lastIndexOf(" "));
						remains = line.substring(firstLineFullWords.length()+1);
					} else {
						firstLineFullWords = firstline;
						remains = line.substring(firstLineFullWords.length());
					}
					newComment = newComment+leader+firstLineFullWords+"\n";
					line = remains;

				}
				newComment = newComment+leader+line;
			}
			if (htmlStyle){
				newComment = leader + "<p>" + "\n" + newComment +"\n" + leader + "</p>";
			}
			if (fullComment.equals("")){
				fullComment = newComment;
			} else {
				fullComment = fullComment + "\n"+newComment;
			}
		}
		return fullComment;
	}
	

	public String getConfiguredProperty(String property){
		if (! isConfiguredProperty(property))
			return "";
		return (String) this.getPluginRef().getProperty(property).toString();
	}

	public boolean isConfiguredProperty(String property){
		if (null  == this.getPluginRef().getProperty(property)){
			return false; }
		else {

			return true;}
	}
	

	public void setPluginRef(IPluginConfig pluginRef) {
		this.pluginRef = pluginRef;
	}

	public IPluginConfig getPluginRef() {
		return this.pluginRef;
	}
	

}
