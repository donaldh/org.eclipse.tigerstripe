/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.builder;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.IProjectDependencyChangeListener;
import org.eclipse.tigerstripe.workbench.project.IProjectDependencyDelta;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;

/**
 * The purpose of the ModelAuditorHelper is to keep track of all used types in a
 * project, and undefined types per artifacts, so that when a change in the
 * model occurs, we can quickly figure out which artifacts are to be re-audited.
 * 
 * The auditor relies on this helper during the each audit cycle: - at the
 * beginning of the cycle to figure out what needs to be audited. - at the end
 * of the cycle to update the helper internal structures once all audits were
 * done.
 * 
 * Upon first audit, the Helper will initialize itself with the project.
 * 
 * @author erdillon
 * 
 */
public class ModelAuditorHelper implements IProjectDependencyChangeListener {

	private boolean isInitialized = false;

	private final ITigerstripeModelProject project;

	// the keys are the artifact being referenced, the values are the set of all
	// artifacts where the key is referenced
	private final HashMap<String, Set<String>> referencedArtifacts = new HashMap<String, Set<String>>();

	// the keys are the artifact being unresolved yet referenced, the values are
	// the set of all artifacts where the key is referenced
	private final HashMap<String, Set<String>> unresolvedArtifacts = new HashMap<String, Set<String>>();

	// The keys are all artifacts in the project, the values are sets of
	// artifact that being referenced.
	// This is only intended so that updates to the above 2 hashs can be
	// efficient
	private final HashMap<String, Set<String>> artifactsWithReferences = new HashMap<String, Set<String>>();

	public ModelAuditorHelper(ITigerstripeModelProject project) {
		this.project = project;

		project.addProjectDependencyChangeListener(this);
	}

	protected ITigerstripeModelProject getProject() {
		return this.project;
	}


	public void reset() {
		isInitialized = false;
		initialize();
	}

	/**
	 * Builds up the internal structures based on the project. This is being
	 * lazily called upon first access to the helper.
	 * 
	 */
	@SuppressWarnings("deprecation")
	protected void initialize() {
		if (isInitialized)
			return;

		try {
			IQueryAllArtifacts query = (IQueryAllArtifacts) project
					.getArtifactManagerSession().makeQuery(
							IQueryAllArtifacts.class.getName());
			query.setIncludeDependencies(false);
			Collection<IAbstractArtifact> allArtifacts = project
					.getArtifactManagerSession().queryArtifact(query);
			for (IAbstractArtifact artifact : allArtifacts) {
				artifactAudited(artifact);
			}

			isInitialized = true;
		} catch (IllegalArgumentException e) {
			BasePlugin.log(e);
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}

	}

	/**
	 * This method is to be called at the beginning of an audit cycle
	 * 
	 * Returns a list of all Artifacts to audit as a result to adding this
	 * artifact to the project.
	 * 
	 * This is intended to be used by the audit as it is trying to figure out
	 * what to audit
	 * 
	 * @param artifact
	 * @return
	 */
	public Set<String> auditListForArtifactAdded(String artifactFQN) {
		initialize();
		Set<String> toAudit = unresolvedArtifacts.get(artifactFQN);
		if (toAudit == null)
			toAudit = new HashSet<String>();
		toAudit.add(artifactFQN);
		return toAudit;
	}

	/**
	 * This method is to be called at the beginning of an audit cycle
	 * 
	 * Returns a list of all Artifacts to audit as a result to changing this
	 * artifact in the project.
	 * 
	 * This is intended to be used by the audit as it is trying to figure out
	 * what to audit
	 * 
	 * @param artifact
	 * @return
	 */
	public Set<String> auditListForArtifactChanged(String artifactFQN) {
		initialize();
		Set<String> toAudit = referencedArtifacts.get(artifactFQN);
		if (toAudit == null)
			toAudit = new HashSet<String>();
		toAudit.add(artifactFQN);
		return toAudit;
	}

	/**
	 * This method is to be called at the beginning of an audit cycle
	 * 
	 * Returns a list of all Artifacts to audit as a result to removing this
	 * artifact FQN from the project.
	 * 
	 * This is intended to be used by the audit as it is trying to figure out
	 * what to audit
	 * 
	 * @param artifactFQN
	 * @return
	 */
	public Set<String> auditListForArtifactFQNRemoved(String artifactFQN) {
		initialize();
		// All artifacts that were referencing this FQN should be audited.
		Set<String> toAudit = referencedArtifacts.get(artifactFQN);
		if (toAudit == null)
			toAudit = new HashSet<String>();
		return toAudit;
	}

	/**
	 * This method is to be called at the end of an audit cycle.
	 * 
	 * This method is called by the auditor once it's audited the given artifact
	 * so that the helper can update its internal structures.
	 * 
	 * This method will be trigger in 2 cases: an artifact had changed and was
	 * just audited, or an artifact had been added and was just audited.
	 * 
	 * @param artifact
	 */
	public void artifactsAudited(Set<IAbstractArtifact> artifacts) {
		initialize();
		for (IAbstractArtifact artifact : artifacts) {
			artifactAudited(artifact);
		}
	}

	@SuppressWarnings("deprecation")
	protected void artifactAudited(IAbstractArtifact artifact) {
		String fqn = artifact.getFullyQualifiedName();
		// First if this artifact was an un-resolved FQN, remove that key from
		// the unresolved hash
		if (unresolvedArtifacts.containsKey(fqn)) {
			unresolvedArtifacts.remove(fqn);
		}

		// Clear up all spots where this artifact was appearing in
		// the referencedArtifacts hash. Use the reverse hash
		// (artifactsWithReferences)
		Set<String> referencedFQNs = artifactsWithReferences.remove(fqn);
		if (referencedFQNs != null) {
			// for each of these remove fqn from their sets
			for (String referencedFQN : referencedFQNs) {
				Set<String> set = referencedArtifacts.get(referencedFQN);
				if (set != null && !set.remove(fqn)) {
					// OUTCH! This should never happen, this means the
					// structures
					// are
					// corrupted!
					IStatus status = new Status(
							IStatus.WARNING,
							BasePlugin.PLUGIN_ID,
							"Artifact auditor internal indexes are corrupted for project '"
									+ project.getName()
									+ "'. Please try to run a 'Clean audit now'.");
					BasePlugin.log(status);
				}
			}
		}

		// Finally re-add the fqn to the proper sets, and to the
		// artifactsWithReferences
		Collection<IAbstractArtifact> referenceds = artifact
				.getReferencedArtifacts();
		Set<String> setForUpdate = new HashSet<String>();
		for (IAbstractArtifact referenced : referenceds) {
			String refFQN = referenced.getFullyQualifiedName();
			setForUpdate.add(refFQN);
			try {
				IAbstractArtifact ref = project.getArtifactManagerSession()
						.getArtifactByFullyQualifiedName(refFQN);
				if (ref == null || ((IAbstractArtifactInternal) ref).isProxy()) {
					// this is a reference to an unresolved artifact.
					Set<String> set = unresolvedArtifacts.get(refFQN);
					if (set == null) {
						set = new HashSet<String>();
						unresolvedArtifacts.put(refFQN, set);
					}
					set.add(fqn);
				} else {
					// this is a reference to an existing artifact
					Set<String> set = referencedArtifacts.get(refFQN);
					if (set == null) {
						set = new HashSet<String>();
						referencedArtifacts.put(refFQN, set);
					}
					set.add(fqn);
				}
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			}
		}

		// Finally update the reverse lookup hash
		if (!referenceds.isEmpty()) {
			artifactsWithReferences.put(fqn, setForUpdate);
		}
	}

	/**
	 * This method is to be called at the end of an audit cycle.
	 * 
	 * This will update the internal structures to reflect the removal of the
	 * given artifact FQN
	 * 
	 * @param fqn
	 */
	public void artifactRemoved(String fqn) {
		initialize();

		if (referencedArtifacts.containsKey(fqn)) {
			// Ok, so this artifact was referenced by a set of artifact.
			// it needs to be removed from referencedArtifacts list, as it should already
			// be in the unresolvedArtifacts list.
			referencedArtifacts.remove(fqn);
		}

		// Now we need to figure out if this artifact is in some of the sets
		// of the referencedArtifacts, as we need to remove them.
		// We use the artifactsWithReferences hash as a reverse key mechanism,
		// then
		// update the referencedArtifact hash.
		if (!artifactsWithReferences.containsKey(fqn))
			// if this artifact wasn't referencing any artifact so no need
			// to worry about the referencedArtifact hash.
			return;

		Set<String> referencedFQNs = artifactsWithReferences.remove(fqn);
		// for each of these remove fqn from their sets
		for (String referencedFQN : referencedFQNs) {
			Set<String> set = referencedArtifacts.get(referencedFQN);
			
			if ( set == null ) {
				// This indicates that the reference is crossing project boundaries!
				System.out.println("FQN=" + referencedFQN);
				continue;
			}
			
			if (!set.remove(fqn)) {
				// OUTCH! This should never happen, this means the structures
				// are corrupted!
				IStatus status = new Status(IStatus.WARNING,
						BasePlugin.PLUGIN_ID,
						"Artifact auditor internal indexes are corrupted for project '"
								+ project.getName()
								+ "'. Please try to run a 'Clean audit now'.");
				BasePlugin.log(status);
			}
		}
	}

	public void projectDependenciesChanged(IProjectDependencyDelta delta) {
		reset();
	}
}
