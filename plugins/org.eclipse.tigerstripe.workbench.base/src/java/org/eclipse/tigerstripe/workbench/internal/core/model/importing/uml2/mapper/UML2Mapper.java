/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.mapper;

import java.util.List;
import java.util.ListIterator;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.project.INameProvider;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElement;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableModel;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.IModelImportConfiguration;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.ModelImporterListener;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.UML2ModelImportConfiguration;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.annotables.AnnotableModelFromUML2;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.annotables.UML2AnnotableDatatype;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.annotables.UML2AnnotableDependency;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.annotables.UML2AnnotableElementPackage;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;

public class UML2Mapper {

	private ITigerstripeModelProject targetProject;

	private MessageList messageList;

	private IModelImportConfiguration config;

	public UML2Mapper(MessageList messageList,
			ITigerstripeModelProject targetProject, IModelImportConfiguration config) {
		this.messageList = messageList;

		if (messageList == null) {
			messageList = new MessageList();
		}
		this.targetProject = targetProject;
		this.config = config;
	}

	protected ITigerstripeModelProject getTargetProject() {
		return this.targetProject;
	}

	protected MessageList getMessageList() {
		return this.messageList;
	}

	public AnnotableModel mapToModel(ModelImporterListener listener)
			throws TigerstripeException {
		AnnotableModelFromUML2 result = new AnnotableModelFromUML2(
				getTargetProject(), config);

		UML2ModelImportConfiguration uml2Config = (UML2ModelImportConfiguration) config;
		Model uml2Model = uml2Config.getModel();
		String modelLibrary = uml2Config.getModelLibrary();

		// Walk the model
		TreeIterator t = uml2Model.eAllContents();

		int monCount = 0;
		while (t.hasNext()) {
			monCount++;
			t.next();
		}

		try {
			// ((ArtifactManagerSessionImpl) mgrSession)
			// .setLockForGeneration(true);
			// TigerstripeProjectAuditor.setTurnedOffForImport(true);

			UML2MappingUtils utils = new UML2MappingUtils(modelLibrary,
					messageList, getTargetProject());

			listener.importBeginTask("Processing UML Classes ", monCount);
			t = uml2Model.eAllContents();
			while (t.hasNext()) {
				EObject o = (EObject) t.next();
				if (o instanceof AssociationClass) {
					// // Map to AssociationClass Artifact
					// mapToArtifact(o,
					// IAssociationClassArtifact.class.getName(),
					// monitor);
				} else if (o instanceof Class) {
					UML2ClassMapper classMapper = new UML2ClassMapper(
							(NamedElement) o, utils);
					AnnotableElement element = classMapper.mapToAnnotable(
							listener, messageList);
					result.addAnnotableElement(element);
				} else if (o instanceof Association) {
					UML2AssociationMapper assocMapper = new UML2AssociationMapper(
							(Association) o, utils);
					AnnotableElement element = assocMapper.mapToAnnotable(
							listener, messageList);
					result.addAnnotableElement(element);
				} else if (o instanceof Enumeration) {
					UML2EnumMapper enumMapper = new UML2EnumMapper(
							(Enumeration) o, utils);
					AnnotableElement element = enumMapper.mapToAnnotable(
							listener, messageList);
					result.addAnnotableElement(element);
				} else if (o instanceof Interface) {
					UML2InterfaceMapper intfMapper = new UML2InterfaceMapper(
							(Interface) o, utils);
					AnnotableElement element = intfMapper.mapToAnnotable(
							listener, messageList);
					result.addAnnotableElement(element);
				}
				listener.worked(1);
			}

			// Do a second pass to set the generalizations.
			t = uml2Model.eAllContents();
			listener.importBeginTask("Post-Processing on Generalizations",
					monCount);
			while (t.hasNext()) {
				EObject o = (EObject) t.next();
				if (o instanceof Classifier) {
					Classifier element = (Classifier) o;
					String annotableName = element.getQualifiedName();
					if (annotableName != null) {
						String annotableFullyQualifiedName = annotableName
								.replaceAll("::", ".");
						// remove the model name
						annotableFullyQualifiedName = annotableFullyQualifiedName
								.substring(annotableFullyQualifiedName
										.indexOf(".") + 1);

						for (AnnotableElement elm : result
								.getAnnotableElements()) {
							if (elm.getFullyQualifiedName().equals(
									annotableFullyQualifiedName)) {
								setGeneralization(elm, element, utils,
										messageList, result);
							}
						}
					}
				}
				listener.worked(1);
			}

			// Build dependencies
			t = uml2Model.eAllContents();

			listener.importBeginTask("Post-Processing on Dependencies",
					monCount);
			while (t.hasNext()) {
				EObject o = (EObject) t.next();
				if (o instanceof Classifier) {
					Classifier element = (Classifier) o;
					String packageName = element.getNearestPackage()
							.getQualifiedName();
					for (Object depO : element.getClientDependencies()) {
						if (depO instanceof Dependency) {
							Dependency dep = (Dependency) depO;
							String depName = dep.getName();

							Classifier client = null;
							Classifier supplier = null;
							for (Object c : dep.getClients()) {
								if (c instanceof Classifier) {
									client = (Classifier) c;
									break; // Only do one..
								}
							}
							for (Object s : dep.getSuppliers()) {
								if (s instanceof Classifier) {
									supplier = (Classifier) s;
									break; // Only do one..
								}
							}

							// So go ahead and make one..
							if (supplier != null && client != null) {
								String depFQN = UML2MappingUtils
										.convertToFQN(packageName + "."
												+ depName);

								String validPackage = ValidIdentifiersUtils
										.getValidPackageName(messageList, Util
												.packageOf(depFQN),
												getTargetProject(), depFQN);
								UML2AnnotableElementPackage pack = new UML2AnnotableElementPackage(
										validPackage);

								// Set the AEnd
								String aEndName = ValidIdentifiersUtils
										.getValidArtifactName(
												messageList,
												Util
														.nameOf(UML2MappingUtils
																.convertToFQN(client
																		.getQualifiedName())));
								String aEndPackage = ValidIdentifiersUtils
										.getValidPackageName(
												new MessageList(),
												Util
														.packageOf(UML2MappingUtils
																.convertToFQN(client
																		.getQualifiedName())),
												getTargetProject(), aEndName);
								if (aEndPackage != null
										&& aEndPackage.length() != 0) {
									aEndName = aEndPackage + "." + aEndName;
								}
								UML2AnnotableDatatype aType = new UML2AnnotableDatatype(
										aEndName);

								// Set the ZEnd
								String zEndName = ValidIdentifiersUtils
										.getValidArtifactName(
												messageList,
												Util
														.nameOf(UML2MappingUtils
																.convertToFQN(supplier
																		.getQualifiedName())));
								String zEndPackage = ValidIdentifiersUtils
										.getValidPackageName(
												new MessageList(),
												Util
														.packageOf(UML2MappingUtils
																.convertToFQN(supplier
																		.getQualifiedName())),
												getTargetProject(), zEndName);
								if (zEndPackage != null
										&& zEndPackage.length() != 0) {
									zEndName = zEndPackage + "." + zEndName;
								}
								UML2AnnotableDatatype zType = new UML2AnnotableDatatype(
										zEndName);

								String validElementName = ValidIdentifiersUtils
										.getValidArtifactName(messageList, Util
												.nameOf(depFQN));
								if (depName == null) {
									INameProvider provider = utils
											.getNameProvider();
									depName = provider.getUniqueName(
											IDependencyArtifact.class,
											validPackage, true);
									validElementName = depName;
									Message msg = new Message();
									msg
											.setMessage("No name for dependency between '"
													+ aEndName
													+ "' and '"
													+ zEndName
													+ "'. Setting to "
													+ depName + ".");
									msg.setSeverity(Message.WARNING);
									messageList.addMessage(msg);
								}

								UML2AnnotableDependency depAnnotable = new UML2AnnotableDependency(
										validElementName);
								depAnnotable.setAnnotableElementPackage(pack);
								depAnnotable.setAEnd(aType);
								depAnnotable.setZEnd(zType);
								depAnnotable
										.setAnnotationType(AnnotableElement.AS_DEPENDENCY);

								// checking for duplicates
								updateName(result, depAnnotable);

								result.addAnnotableElement(depAnnotable);

							}

						}
					}
				}
				listener.worked(1);
			}

			listener.importDone();
		} finally {
			// ((ArtifactManagerSessionImpl) mgrSession)
			// .setLockForGeneration(false);
			// TigerstripeProjectAuditor.setTurnedOffForImport(false);
		}

		// final String pName= tSProjectName;
		//
		// IWorkspaceRunnable op = new IWorkspaceRunnable() {
		//
		// public void run(IProgressMonitor monitor) throws CoreException {
		// try {
		// IResource resource = ResourcesPlugin.getWorkspace()
		// .getRoot().findMember(new Path(pName));
		// resource.refreshLocal(IResource.DEPTH_INFINITE, null);
		// } finally {
		// monitor.done();
		// }
		// }
		// };
		//
		// try {
		// ResourcesPlugin.getWorkspace().run(op, monitor);
		// } catch (CoreException e) {
		// }
		// }

		// Should we apply anything to that?
		if (config.getReferenceProject() != null) {
			result
					.applyTargetProjectArtifactTypes(config
							.getReferenceProject());
		}

		return result;
	}

	private int depCounter = 0;

	private void updateName(AnnotableModelFromUML2 result,
			UML2AnnotableDependency dep) {
		if (result.getAnnotableElements().contains(dep)) {
			String name = dep.getName() + depCounter++;
			dep.setName(name);
			updateName(result, dep);
		}
		return;
	}

	private void setGeneralization(AnnotableElement artifact,
			Classifier element, UML2MappingUtils utils,
			MessageList messageList, AnnotableModelFromUML2 annotableElement) {

		List gens = element.getGenerals();
		ListIterator genIt = gens.listIterator();
		while (genIt.hasNext()) {
			Classifier gen = (Classifier) genIt.next();
			String rawGenName = UML2MappingUtils.convertToFQN(gen
					.getQualifiedName());

			String name = ValidIdentifiersUtils.getValidArtifactName(
					new MessageList(), Util.nameOf(rawGenName));
			String pack = ValidIdentifiersUtils.getValidPackageName(
					new MessageList(), Util.packageOf(rawGenName),
					getTargetProject(), name);
			String genName = name;
			if (!"".equals(pack)) {
				genName = pack + "." + name;
			}
			// this.out.println(" " + artifact.getName() + " Generalization "
			// + genName);
			try {

				AnnotableElement targetElm = null;
				for (AnnotableElement elm : annotableElement
						.getAnnotableElements()) {
					if (elm.getFullyQualifiedName().equals(genName)) {
						targetElm = elm;
					}
				}

				if (targetElm == null) {
					String msgText = "Failed to retreive generalization for Artifact : "
							+ artifact.getName();
					Message msg = new Message();
					msg.setMessage(msgText);
					msg.setSeverity(Message.WARNING);
					messageList.addMessage(msg);
					// this.out.println("Error : " + msgText);
				} else {
					artifact.setParentAnnotableElement(targetElm);
				}
			} catch (Exception e) {
				String msgText = "Failed to retreive generalization for Artifact : "
						+ artifact.getName();
				Message msg = new Message();
				msg.setMessage(msgText);
				msg.setSeverity(Message.ERROR);
				messageList.addMessage(msg);
				// this.out.println("Error : " + msgText);
				// e.printStackTrace(this.out);
				return;
			}
		}

	}
}
