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
package org.eclipse.tigerstripe.generators.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;

import org.eclipse.tigerstripe.generators.models.AssociationEndFacade;
import org.eclipse.tigerstripe.generators.utils.LegacyFilter;

import org.eclipse.tigerstripe.generators.models.AbstractComponentModel;
import org.eclipse.tigerstripe.generators.models.ArgumentFacade;

public class DocUtils
{

	private static IArtifactManagerSession managerSession;
	
	public static IArtifactManagerSession getManagerSession() {
		return managerSession;
	}


	/**
	 * Generate a simple hash based on a string
	 */
	public static String getHash(String input){
		long h =0;
		for (int i=0;i<input.length();i++){
			h=(h<<2L) + Character.getNumericValue(input.charAt(i));
		}
		PluginLog.logTrace("Getting hash for "+input+ " : Returning "+Long.toHexString(h));
		return Long.toHexString(h);
	}

	public static Collection<DocPackage> getRootPackages(IArtifactManagerSession session, Collection<Object> allArtifacts){
		Map<String, DocPackage> packageMap = new HashMap<String, DocPackage>();
		try {
			//IArtifactQuery query = session.makeQuery(IQueryAllArtifacts.class.getName());
			//Collection allArtifacts = session.queryArtifact(query);
			// Filter out legacy artifacts
			for (Object art : allArtifacts){
				//LegacyFilter filter = new LegacyFilter();
				IAbstractArtifact localArtifact = (IAbstractArtifact) art;
				//if (filter.select(localArtifact) && ! localArtifact.getPackage().equals("primitive")){
				//if (localArtifact.getTigerstripeProject() != null && ! localArtifact.getPackage().equals("primitive")){
				if (! localArtifact.getPackage().equals("primitive")){
				String localPackageName = localArtifact.getPackage();
				DocPackage myPackage;
				if (! packageMap.containsKey(localPackageName)){
					// make a new DocPackage
					
					// Also check/make all parent elements
					String[] segments = localPackageName.split("\\.");
					
					String prev = "";
					DocPackage parent = null;
					DocPackage segmentPackage;
					for (String segment : segments){
						String name = prev+segment;

						if (! packageMap.containsKey(name)){
							segmentPackage= new DocPackage();
							segmentPackage.setFullyQualifiedName(name);
							packageMap.put(name, segmentPackage);
						} else {
							segmentPackage = packageMap.get(name);
						}
						if (parent != null){
							parent.addSubPackage(segmentPackage);
						}
						prev = name + ".";
						parent = segmentPackage;
					}
					// myPackage will be the last one.
					myPackage = parent;
				} else {
					myPackage = packageMap.get(localPackageName);
				}
				
				if (localArtifact instanceof IAbstractArtifact
						
						){
					myPackage.setElements(true);
				}
				
				}
				
			}
		} catch (Exception e){
			PluginLog.logError("Failure to query artifacts",e);
		}

		Collection<DocPackage> packages = sortDocPackageByName(packageMap.values());
		Collection<DocPackage> rootPackages = new ArrayList<DocPackage>();
		for (DocPackage p : packages){
			if (!p.getFullyQualifiedName().contains(".")){
				rootPackages.add(p);
			}
		}
		return rootPackages;

	}
	
	public static Collection<DocPackage> sortDocPackageByName(Collection packages) {
    	ArrayList sorted = new ArrayList();

        if (packages != null) {
        	sorted.addAll(packages);
	        	Collections.sort (sorted, new java.util.Comparator() {
	            public int compare(Object arg0, Object arg1) 
	            {
	                if (arg0 instanceof DocPackage
	                        && arg1 instanceof DocPackage) {
	                	DocPackage a = (DocPackage) arg0;
	                	DocPackage b = (DocPackage) arg1;
	                    return a.getFullyQualifiedName().compareTo(b.getFullyQualifiedName());
	                } else {
	                	return 0;

	                }
	            }
	        });
        }
	  	return sorted;

    }
	
    public String getMultiplicityString(AssociationEndFacade end){
        String ret = "";
        ret = end.getMultiplicity().getLabel();
        return ret;
    }
    
    public static Collection sortByName(Collection attrs){
    	ArrayList sorted = new ArrayList();

        if (attrs != null) {
        	sorted.addAll(attrs);
	        	Collections.sort (sorted, new java.util.Comparator() {
	            public int compare(Object arg0, Object arg1) 
	            {
	                if (arg0 instanceof AbstractComponentModel
	                        && arg1 instanceof AbstractComponentModel) {
	                	AbstractComponentModel a = (AbstractComponentModel) arg0;
	                	AbstractComponentModel b = (AbstractComponentModel) arg1;
	                    return a.getName().compareTo(b.getName());
	                } else if (arg0 instanceof ArgumentFacade
	                		&& arg1 instanceof ArgumentFacade) {
	                	ArgumentFacade a = (ArgumentFacade) arg0;
	                	ArgumentFacade b = (ArgumentFacade) arg1;
	                	return a.getName().compareTo(b.getName());
	                } else {
	                	return 0;
	                }
	            }
	        });
        }
	  	return sorted;

    }
	
    public String encode(String str) {
		  if (str == null)
		   return "";

		  StringBuffer sb = new StringBuffer();
		  char[] data = str.toCharArray();
		  char c, lastC = 0x0;
		  for (char element : data) {
		   c = element;
		   if (c == '"') {
		    sb.append("&quot;");
		   } else if (c == '\'') {
		    sb.append("&apos;");
		   } else if (c == '<') {
		    	sb.append("&lt;");
		   } else if (c == '>') {
			   sb.append("&gt;");
		   }
		   else if (c == '&') {
		    sb.append("&amp;");
		   } else if (c == '/' && lastC == '*') { // added to handle Javadoc
		    // comments
		    sb.deleteCharAt(sb.length() - 1);
		    sb.append("&eCom;");
		    lastC = 0x0;
		    continue;
		   } else if (c == '*' && lastC == '/') { // added to handle Javadoc
		    // comments
		    sb.deleteCharAt(sb.length() - 1);
		    sb.append("&bCom;");
		    lastC = 0x0;
		    continue;
		   } else {
		    sb.append(c);
		   }
		   lastC = c;
		  }
		  return sb.toString();
		 }

}
