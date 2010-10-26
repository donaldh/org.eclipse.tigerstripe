/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import junit.framework.Assert;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.util.FileUtils;
import org.eclipse.tigerstripe.workbench.plugins.IGlobalRule;
import org.eclipse.tigerstripe.workbench.plugins.IGlobalTemplateRule;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateBasedRule;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;

public class M1ProjectHelper {

	public final static String TEMPLATES = "resources/templates";
	public final static String SRCS = "resources/src";

	/**
	 * Creates a M1 project and copies all the templates located in the
	 * src/resources/templates directory into the created generator project
	 * 
	 * @param projectName
	 * @return
	 * @throws TigerstripeException
	 */
	public static ITigerstripeM1GeneratorProject createM1Project(
			String projectName, boolean populateContents)
			throws TigerstripeException, IOException, CoreException {
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		ITigerstripeM1GeneratorProject project = (ITigerstripeM1GeneratorProject) TigerstripeCore
				.createProject(projectName, projectDetails, null,
						ITigerstripeM1GeneratorProject.class, null, null);

		if (populateContents) {
			addJavaSrc(project);
			addGlobalRules(project);
			IProject iProj = (IProject) project.getAdapter(IProject.class);
			iProj.refreshLocal(IResource.DEPTH_INFINITE, null);
		}

		return project;
	}

	private static void copyFiles(ITigerstripeM1GeneratorProject project,
			String srcDir, String targetDir) throws IOException {
		IPath projectLocation = project.getLocation();
		IPath targetTemplatesPath = projectLocation.append(targetDir);
		
		
		String baseBundleRoot = BundleUtils.INSTANCE.getBundleRoot();
		System.out.println("Base bundle ZZ"+baseBundleRoot+"ZZ");
		if (baseBundleRoot.endsWith(".jar")){
			
			JarFile jar = new JarFile(baseBundleRoot);
			
			Enumeration<JarEntry> entries = jar.entries();
			
			while (entries.hasMoreElements()) {
				JarEntry file = (JarEntry) entries.nextElement();
				
				if ( file.getName().startsWith(srcDir)){
				System.out.println("Jar Entry  "+file.getName());
				
				String namePart = file.getName().substring(srcDir.length()+1);
				
				File f = new File(targetTemplatesPath + File.separator + file.getName());
				System.out.println("File "+f.getAbsolutePath()+ " "+f.exists());
				
				if (f.isDirectory()) { // if its a directory, create it
					f.mkdir();
					continue;
				}
				
				if (! f.exists()){
					f.createNewFile();
				}
				
				InputStream is = jar.getInputStream(file); // get the input stream
				FileOutputStream fos = new FileOutputStream(f);
				
				while (is.available() > 0) {  // write contents of 'is' to 'fos'
					fos.write(is.read());
				}
				
				fos.close();
				is.close();
				}
			}

		} else {
		

			File templatesDir = new File(baseBundleRoot + File.separator + srcDir);
			File[] templates = templatesDir.listFiles();
			Assert.assertNotNull(templatesDir.toString(),templates);
			if (templates == null)
				templates = new File[0];
			for (File template : templates) {
				String targetPath = targetTemplatesPath.toOSString();

				if (!(new File(targetPath + File.separator + template.getName())).exists()) {
					if (template.isFile()){
						FileUtils
						.copy(template.getAbsolutePath(), targetPath, true);
					}
					else {
						FileUtils.copyDir(template.getParentFile()
								.getAbsolutePath(), targetPath, true);
					}
				}
			}
		}
	}

	
	
	private static void copyTemplates(ITigerstripeM1GeneratorProject project)
			throws IOException {
		copyFiles(project, TEMPLATES, "templates");
	}

	private static void addJavaSrc(ITigerstripeM1GeneratorProject project)
			throws IOException {
		copyFiles(project, SRCS, "src");
	}

	private static void addGlobalRules(ITigerstripeM1GeneratorProject project)
			throws TigerstripeException {

		try {
			copyTemplates(project);
		} catch (IOException e) {
			throw new TigerstripeException(e.getMessage(), e);
		}
		ITigerstripeM1GeneratorProject gProject = (ITigerstripeM1GeneratorProject) project
				.makeWorkingCopy(null);

		ITemplateBasedRule rule = (ITemplateBasedRule) gProject
				.makeRule(IGlobalTemplateRule.class);

		rule.setName("Rule1");
		rule.setOutputFile("list.txt");
		rule.setEnabled(true);
		rule.setTemplate("templates/listAll.vm");

		gProject.addGlobalRule((IGlobalRule) rule);
		gProject.commit(null);
	}
}
