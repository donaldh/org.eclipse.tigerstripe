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
package org.eclipse.tigerstripe.core.plugin.ossjxml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.core.model.ArtifactManager;
import org.eclipse.tigerstripe.core.module.ModuleArtifactManager;
import org.eclipse.tigerstripe.core.plugin.Expander;
import org.eclipse.tigerstripe.core.plugin.PackageToSchemaMapper;
import org.eclipse.tigerstripe.core.plugin.XmlPluginRef;
import org.eclipse.tigerstripe.core.plugin.PackageToSchemaMapper.PckXSDMapping;
import org.eclipse.tigerstripe.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;

/**
 * This helper class allows to build a list of imports required for a specific
 * list of Artifacts, and allows for look ups of: - the import lists - the
 * corresponding XML prefixes
 * 
 * @author Eric Dillon
 * @since 1.0
 */
public class XmlSchemaImportsHelper {

	private final static String DEFAULT_PREFIX = "tns";

	private PackageToSchemaMapper localMapper;

	public XmlSchemaImportsHelper(PackageToSchemaMapper localMapper) {
		this.localMapper = localMapper;
		nRefList = new ArrayList();
	}

	private Collection nRefList;

	public void buildImportList(ArtifactManager mgr, Collection content)
			throws TigerstripeException {
		buildImportList(mgr, content, true);
	}

	/**
	 * Builds the list of import for the given content. For each of the
	 * artifacts in the content, all references will be explored to make sure we
	 * have all the xsds in the import.
	 * 
	 * @param content
	 */
	public void buildImportList(ArtifactManager mgr, Collection content,
			boolean referencesOnly) throws TigerstripeException {
		// TigerstripeRuntime.logInfoMessage("Building importsList refOnly=" +
		// referencesOnly
		// );
		int prefixCount = 0;
		nRefList = new ArrayList();

		seedRefListWithCommonAPI(mgr);

		Expander exp = new Expander();

		// TigerstripeRuntime.logInfoMessage("Looping on content");
		for (Iterator iter = content.iterator(); iter.hasNext();) {
			AbstractArtifact artifact = (AbstractArtifact) iter.next();

			XmlPluginRef projRef = (XmlPluginRef) artifact.getTSProject()
					.findPluginRef(XmlPluginRef.MODEL);
			exp.setPluginRef(projRef);

			// TigerstripeRuntime.logInfoMessage( "Artifact = " +
			// artifact.getFullyQualifiedName() );
			// Should we be referencing local artifacts (this is the case when
			// building the list of imports for the WSDL)
			// if (!referencesOnly) {
			// This is a ref to a locally defined artifact
			// We only need an include if not using default schema
			if (!localMapper.useDefaultMapping()) {
				PckXSDMapping mapping = localMapper.getPckXSDMapping(artifact
						.getPackage());
				NamespaceRef nRef = new NamespaceRef();

				if (mapping.getUserPrefix().length() == 0) {
					nRef.prefix = "ns" + prefixCount++;
				} else {
					nRef.prefix = exp.expandVar(mapping.getUserPrefix(),
							artifact.getTSProject());
				}

				// TODO expand
				nRef.targetNamespace = exp.expandVar(mapping
						.getTargetNamespace(), artifact.getTSProject());
				nRef.targetLocation = exp.expandVar(mapping.getXsdLocation(),
						artifact.getTSProject());

				checkNamespaceRef(artifact, artifact.getPackage(), nRef);

				// BUG 280 (part) Only add one reference to a namespace
				// This will be the first one encountered - which should be a
				// local one...
				// This is OK, because the current "getPrefixForNamespace" only
				// returns the first one anyway.

				if (!refListContains(nRef)) {
					nRefList.add(nRef);
					/*
					 * TigerstripeRuntime.logInfoMessage( "Adding nonDefault" +
					 * nRef.prefix + ":" + nRef.targetNamespace + ":" +
					 * artifact.getPackage() );
					 */
				}
			} else {
				PckXSDMapping mapping = localMapper.getDefaultMapping();
				NamespaceRef nRef = new NamespaceRef();
				if (mapping.getUserPrefix().length() == 0) {
					nRef.prefix = "ns" + prefixCount++;
				} else {
					nRef.prefix = exp.expandVar(mapping.getUserPrefix(),
							artifact.getTSProject());
				}
				// TODO expand
				nRef.targetNamespace = exp.expandVar(mapping
						.getTargetNamespace(), artifact.getTSProject());
				nRef.targetLocation = exp.expandVar(mapping.getXsdLocation(),
						artifact.getTSProject());

				checkNamespaceRef(artifact, artifact.getPackage(), nRef);
				if (!refListContains(nRef)) {
					nRefList.add(nRef);
					/*
					 * TigerstripeRuntime.logInfoMessage( "Adding Default" +
					 * nRef.prefix + ":" + nRef.targetNamespace + ":" +
					 * artifact.getPackage() );
					 */
				}
			}
			// }

			Collection references = artifact.getReferencedArtifacts();
			// TigerstripeRuntime.logInfoMessage("Looping on references (" +
			// references.size()
			// + ")");
			for (Iterator refIter = references.iterator(); refIter.hasNext();) {
				String fqn = (String) refIter.next();
				AbstractArtifact refArtifact = mgr
						.getArtifactByFullyQualifiedName(fqn, true,
								new TigerstripeNullProgressMonitor());
				if (refArtifact != null

				// ED: not sure why this was here. it would cause TS to ignore
				// importing from a module that defines the same package
				// namespace
				// && !refArtifact.getPackage().equals(
				// artifact.getPackage())) {
				) {
					// TigerstripeRuntime.logInfoMessage("Handling ref=" +
					// refArtifact.getFullyQualifiedName() );
					ArtifactManager parentMgr = refArtifact
							.getArtifactManager();
					if (parentMgr instanceof ModuleArtifactManager) {
						// TigerstripeRuntime.logInfoMessage(" ... found it in a
						// module");
						// this is a ref to an artifact defined in a dependency
						ModuleArtifactManager moduleMgr = (ModuleArtifactManager) parentMgr;
						TigerstripeProject embeddedProject = moduleMgr
								.getEmbeddedProject();
						XmlPluginRef ref = (XmlPluginRef) embeddedProject
								.findPluginRef(XmlPluginRef.MODEL);
						if (ref != null) {
							PckXSDMapping mapping = ref.getMapper()
									.getPckXSDMapping(refArtifact.getPackage());
							NamespaceRef nRef = new NamespaceRef();
							if (mapping.getUserPrefix().length() == 0) {
								nRef.prefix = "ns" + prefixCount++;
							} else {
								nRef.prefix = exp.expandVar(mapping
										.getUserPrefix(), ref.getProject());
							}

							// expand any Vars before adding to the mapping

							nRef.targetNamespace = exp.expandVar(mapping
									.getTargetNamespace(), ref.getProject());
							nRef.targetLocation = exp.expandVar(mapping
									.getXsdLocation(), ref.getProject());

							checkNamespaceRef(artifact, refArtifact
									.getPackage(), nRef);
							if (!refListContains(nRef)) {
								nRefList.add(nRef);
								/*
								 * TigerstripeRuntime.logInfoMessage( "Adding
								 * from Module" + nRef.prefix + ":" +
								 * nRef.targetNamespace + ":" +
								 * refArtifact.getPackage() );
								 */
							}
						}
					} else if (!isInSameProject(refArtifact, artifact)) {
						// This is a ref defined in a referenced project
						TigerstripeProject embeddedProject = null;
						try {
							embeddedProject = ((TigerstripeProjectHandle) refArtifact
									.getIProject()).getTSProject();
						} catch (TigerstripeException e) {
							// shouldn't happen here
							TigerstripeRuntime.logErrorMessage(
									"TigerstripeException detected", e);
						}
						XmlPluginRef ref = (XmlPluginRef) embeddedProject
								.findPluginRef(XmlPluginRef.MODEL);
						if (ref != null) {
							PckXSDMapping mapping = ref.getMapper()
									.getPckXSDMapping(refArtifact.getPackage());
							NamespaceRef nRef = new NamespaceRef();
							if (mapping.getUserPrefix().length() == 0) {
								nRef.prefix = "ns" + prefixCount++;
							} else {
								nRef.prefix = exp.expandVar(mapping
										.getUserPrefix(), ref.getProject());
							}
							// TODO expand
							nRef.targetNamespace = exp.expandVar(mapping
									.getTargetNamespace(), ref.getProject());
							nRef.targetLocation = exp.expandVar(mapping
									.getXsdLocation(), ref.getProject());

							checkNamespaceRef(artifact, refArtifact
									.getPackage(), nRef);
							if (!refListContains(nRef)) {
								nRefList.add(nRef);
								/*
								 * TigerstripeRuntime.logInfoMessage( "Adding
								 * from Ref" + nRef.prefix + ":" +
								 * nRef.targetNamespace + ":" +
								 * refArtifact.getPackage() );
								 */
							}
						}
					} else {
						// This is a ref to a locally defined artifact
						// We only need an include if not using default schema
						// TigerstripeRuntime.logInfoMessage(" ... local
						// definition, checking
						// mapping");
						if (!localMapper.useDefaultMapping()) {
							// TigerstripeRuntime.logInfoMessage( " ... not
							// using default
							// mapping");
							PckXSDMapping mapping = localMapper
									.getPckXSDMapping(refArtifact.getPackage());
							NamespaceRef nRef = new NamespaceRef();
							if (mapping.getUserPrefix().length() == 0) {
								nRef.prefix = "ns" + prefixCount++;
							} else {
								nRef.prefix = exp.expandVar(mapping
										.getUserPrefix(), artifact
										.getTSProject());
							}
							nRef.targetNamespace = exp.expandVar(mapping
									.getTargetNamespace(), artifact
									.getTSProject());
							nRef.targetLocation = exp.expandVar(mapping
									.getXsdLocation(), artifact.getTSProject());

							checkNamespaceRef(artifact, refArtifact
									.getPackage(), nRef);
							if (!refListContains(nRef)) {
								nRefList.add(nRef);
								/*
								 * TigerstripeRuntime.logInfoMessage( "Adding
								 * Local" + nRef.prefix + ":" +
								 * nRef.targetNamespace + ":" +
								 * refArtifact.getPackage() );
								 */
							}
						}
					}
				}
			}
		}
	}

	private boolean isInSameProject(AbstractArtifact refArtifact,
			AbstractArtifact artifact) {
		return refArtifact.getIProject().getURI().equals(
				artifact.getIProject().getURI());
	}

	/**
	 * Checks the ref to make sure it is not empty
	 * 
	 * @param ref
	 * @throws TigerstripeException
	 */
	private void checkNamespaceRef(AbstractArtifact artifact,
			String currentPackage, NamespaceRef ref)
			throws TigerstripeException {
		if (ref.targetNamespace == null || "".equals(ref.targetNamespace))
			throw new TigerstripeException("Could not find mapping for '"
					+ currentPackage + "' package. It is referenced in "
					+ artifact.getFullyQualifiedName() + ".");
	}

	public class NamespaceRef {
		public String targetNamespace;

		public String targetLocation;

		public String prefix;

		public String getNamespace() {
			return targetNamespace;
		}

		public String getPrefix() {
			return prefix;
		}

		public String getLocation() {
			return targetLocation;
		}

		@Override
		public boolean equals(Object arg0) {
			if (arg0 instanceof NamespaceRef) {
				NamespaceRef other = (NamespaceRef) arg0;
				return targetLocation.equals(other.targetLocation);
			}
			return false;
		}
	}

	/**
	 * Returns a list of NamespaceRef
	 * 
	 * @return
	 */
	public Collection getImportsList() {
		// TigerstripeRuntime.logInfoMessage("Getting ImportsList here " +
		// nRefList.size());
		return nRefList;
	}

	/**
	 * check if the list contains one with the same namespace as this one.
	 * 
	 * @param nRef
	 * @return
	 */
	public boolean refListContains(NamespaceRef nRef) {
		for (Iterator iter = nRefList.iterator(); iter.hasNext();) {
			NamespaceRef ref = (NamespaceRef) iter.next();
			if (ref.targetNamespace.equals(nRef.targetNamespace))
				return true;
		}
		// None matched
		return false;
	}

	/**
	 * Returns the prefix for the corresponding targetNamespace
	 * 
	 */
	public String getPrefixForNamespace(String namespace) {
		// TigerstripeRuntime.logInfoMessage("Namespace = "+namespace);
		for (Iterator iter = nRefList.iterator(); iter.hasNext();) {
			NamespaceRef ref = (NamespaceRef) iter.next();
			// TigerstripeRuntime.logInfoMessage("Found NS =
			// "+ref.targetNamespace);
			if (ref.targetNamespace.equals(namespace))
				// TigerstripeRuntime.logInfoMessage("Prefix = "+ref.prefix);
				return ref.prefix;
		}
		// TigerstripeRuntime.logInfoMessage("Default = "+getDefaultPrefix());
		return getDefaultPrefix();
	}

	public String getDefaultPrefix() {
		return DEFAULT_PREFIX;
	}

	/**
	 * Seeding the list of import with the current common to ensure proper
	 * namespace and avoid having to hardcode in the template
	 * 
	 */
	protected void seedRefListWithCommonAPI(ArtifactManager mgr) {

		TigerstripeProject project = mgr.getTSProject();
		if (project == null) {
			// we might be in a Module?
			if (mgr instanceof ModuleArtifactManager) {
				project = ((ModuleArtifactManager) mgr).getEmbeddedProject();
			} else
				return;
		}

		NamespaceRef ref = new NamespaceRef();
		ref.prefix = project.getProjectDetails().getProperty(
				"ossj.common.namespacePrefix", "");
		ref.targetLocation = project.getProjectDetails().getProperty(
				"ossj.common.schemaLocation", "");
		ref.targetNamespace = project.getProjectDetails().getProperty(
				"ossj.common.targetNamespace", "");
		nRefList.add(ref);

	}

}
