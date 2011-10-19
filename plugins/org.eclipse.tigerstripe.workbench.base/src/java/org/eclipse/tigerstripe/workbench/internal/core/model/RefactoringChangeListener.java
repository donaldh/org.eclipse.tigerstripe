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
package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.refactoring.descriptors.DeleteDescriptor;
import org.eclipse.jdt.core.refactoring.descriptors.MoveDescriptor;
import org.eclipse.jdt.core.refactoring.descriptors.RenameJavaElementDescriptor;
import org.eclipse.jdt.internal.core.JavaElement;
import org.eclipse.jdt.internal.core.util.MementoTokenizer;
import org.eclipse.ltk.core.refactoring.RefactoringContribution;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringDescriptor;
import org.eclipse.ltk.core.refactoring.history.IRefactoringExecutionListener;
import org.eclipse.ltk.core.refactoring.history.RefactoringExecutionEvent;
import org.eclipse.ltk.core.refactoring.resource.MoveResourcesDescriptor;
import org.eclipse.ltk.core.refactoring.resource.RenameResourceDescriptor;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * Artifact managers of respective projects need to be notified whenever a
 * refactoring is triggered from the UI
 * 
 * This singleton registers itself for notification in case of refactoring
 * actions, and propagates accordingly to the appropriate Art. mgr.
 * 
 * @author erdillon
 * 
 */
public class RefactoringChangeListener implements IRefactoringExecutionListener {

	private static final String ATTR_INPUT = "input";

	private final ListenerList artMgrs = new ListenerList();

	private static RefactoringChangeListener instance = null;

	private RefactoringChangeListener() {
		// singleton
	}

	public static RefactoringChangeListener getInstance() {
		if (instance == null) {
			instance = new RefactoringChangeListener();
			startListening();
		}
		return instance;
	}

	public void addArtifactManager(ArtifactManager manager) {
		ITigerstripeModelProject tsProject = manager.getTSProject()
				.getTSProject();
		if (tsProject != null) {
			artMgrs.add(manager);
		}
	}

	public void removeArtifactManager(ArtifactManager manager) {
		artMgrs.remove(manager);
	}

	protected static void startListening() {
		RefactoringCore.getHistoryService().addExecutionListener(instance);
	}

	public void executionNotification(RefactoringExecutionEvent event) {

		if (event.getEventType() == RefactoringExecutionEvent.PERFORMED) {
			RefactoringDescriptor des = event.getDescriptor()
					.requestDescriptor(new NullProgressMonitor());
			if (des instanceof RenameJavaElementDescriptor) {
				RefactoringContribution contr = RefactoringCore
						.getRefactoringContribution(des.getID());
				Map<?, ?> attrs = contr.retrieveArgumentMap(des);
				String handle = (String) attrs.get(ATTR_INPUT);
				String project = des.getProject();
				String srcFQN = getFullyQualifiedName(handle);
				String newName = (String) attrs.get("name");

//				if (newName.indexOf(".") != -1) {
//					System.out.println("Process package rename from " + srcFQN
//							+ " to " + newName + " in " + project);
//				} else
//					System.out.println("Process rename from " + srcFQN + " to "
//							+ newName + " in " + project);
			}
			if (des instanceof MoveDescriptor) {
				MoveDescriptor descriptor = (MoveDescriptor) des;
				RefactoringContribution contr = RefactoringCore
						.getRefactoringContribution(des.getID());
				Map<?, ?> attrs = contr.retrieveArgumentMap(des);
				String project = des.getProject();
				String destination = getFullyQualifiedName((String) attrs
						.get("destination"));
				int units = Integer.parseInt((String) attrs.get("units"));
				String[] elements = new String[units];
				for (int i = 1; i <= units; i++) {
					String key = "element" + i;
					elements[i - 1] = getFullyQualifiedName((String) attrs
							.get(key));
//					System.out.println("Moving " + elements[i - 1] + " to "
//							+ destination + " in  " + project);
				}
			}

			if (des instanceof RenameResourceDescriptor) {
//				System.out.println(des);
			}
			if (des instanceof MoveResourcesDescriptor) {
//				System.out.println(des);
			}
		} else {
			RefactoringDescriptor des = event.getDescriptor()
					.requestDescriptor(new NullProgressMonitor());
			if (des instanceof DeleteDescriptor) {
				DeleteDescriptor descriptor = (DeleteDescriptor) des;
				RefactoringContribution contr = RefactoringCore
						.getRefactoringContribution(des.getID());
				Map<?, ?> attrs = contr.retrieveArgumentMap(des);
				String project = des.getProject();
				int units = Integer.parseInt((String) attrs.get("elements"));
				String[] elements = new String[units];

				IProject proj = (IProject) ResourcesPlugin.getWorkspace()
						.getRoot().findMember(project);
				if (proj != null) {
					ITigerstripeModelProject tsProj = (ITigerstripeModelProject) proj
							.getAdapter(ITigerstripeModelProject.class);
					if (tsProj != null) {
						try {
							ArtifactManager mgr = ((ArtifactManagerSessionImpl) tsProj
									.getArtifactManagerSession())
									.getArtifactManager();
							for (int i = 1; i <= units; i++) {
								String key = "element" + i;
								elements[i - 1] = getFullyQualifiedName((String) attrs
										.get(key));
								final IAbstractArtifact artifact = mgr
										.getArtifactByFullyQualifiedName(
												elements[i - 1], false,
												(IProgressMonitor) null);
								if (artifact != null) {
									mgr.notifyArtifactDeleted(artifact);
									AnnotationPlugin.getManager()
											.fireDeleted(new ILazyObject() {
												public Object getObject() {
													return artifact;
												}
											});
								}
//								System.out
//										.println("Removing " + elements[i - 1]
//												+ " from  " + project);
							}
						} catch (TigerstripeException e) {
							BasePlugin.log(e);
						}
					}
				}
			} else {
//				System.out.println(event);
			}
		}
	}

	private String getFullyQualifiedName(String handle) {
		String packageFragment = null;
		String type = null;
		
		if (handle==null) {
			return null;
		}
		
		for (MementoTokenizer memento = new MementoTokenizer(handle); memento
				.hasMoreTokens();) {
			String token = memento.nextToken();
			if (token.equals(String.valueOf(JavaElement.JEM_PACKAGEFRAGMENT))) {
				packageFragment = memento.nextToken();
			} else if (token.equals(String
					.valueOf(JavaElement.JEM_COMPILATIONUNIT))) {
				type = memento.nextToken().replace(".java", "");
			} else if (token.equals(String.valueOf(JavaElement.JEM_TYPE))) {
				type = memento.nextToken();
			}
		}

		if (type == null || type.length() == 0) {
			// This is a package being renamed
			return packageFragment;
		}

		if (packageFragment != null && packageFragment.length() != 0) {
			return packageFragment + "." + type;
		} else {
			return type;
		}
	}
}
