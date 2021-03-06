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
package org.eclipse.tigerstripe.workbench.internal.builder;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.ScopePattern;
import org.eclipse.tigerstripe.workbench.internal.api.contract.useCase.IUseCaseReference;
import org.eclipse.tigerstripe.workbench.internal.contract.ContractUtils;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;

public class ContractSegmentAuditor implements IFileExtensionBasedAuditor{

	private IProject project;

	String extension = IContractSegment.FILE_EXTENSION;

	public String getFileExtension(){
		return extension;
	}
	
	public void run(IProject project, List<IResource> resources, IProgressMonitor monitor) {

//		this.project = project;
//		
//		if (resources == null || resources.size() == 0)
//			return;
//
//		if (getTSProject() != null) {
//			monitor.beginTask("Checking Facets", resources.size());
//
//			for (IResource res : resources) {
//				try {
//					IContractSegment facet = InternalTigerstripeCore
//							.getIContractSession().makeIContractSegment(
//									res.getLocationURI());
//
//					// Start checks here
//					if (facet == null) {
//						TigerstripeProjectAuditor.reportError("Facet '"
//								+ res.getProjectRelativePath()
//								+ "' is not a valid facet file.", res, 222);
//					} else {
//						checkScope(facet, res);
//						checkUseCaseDocs(facet, res);
//						checkFacetRefs(facet, res);
//					}
//
//				} catch (Exception e) {
//					BasePlugin.log(e);
//				}
//				monitor.worked(1);
//			}
//			monitor.done();
//		} else {
//			TigerstripeProjectAuditor.reportError("Project '"
//					+ project.getName() + "' is invalid", project, 222);
//		}
	}

	private ITigerstripeModelProject getTSProject() {
		return (ITigerstripeModelProject) project
		.getAdapter(ITigerstripeModelProject.class);
	}
	
	private void checkFacetRefs(IContractSegment facet, IResource res) {
		for (IFacetReference ref : facet.getFacetReferences()) {
			if (!ref.canResolve()) {
				TigerstripeProjectAuditor.reportError(
						"Can't find facet reference '"
								+ ref.getProjectRelativePath()
								+ " referenced in " + facet.getName() + "'.",
						res, 222);
			} else {
				try {
					if (ref.resolve().getURI().equals(facet.getURI())) {
						TigerstripeProjectAuditor.reportError(
								"A facet can't reference itself. ( "
										+ facet.getName() + ")", res, 222);
					}
				} catch (TigerstripeException e) {
					BasePlugin.log(e);
				}
			}
		}
	}

	/**
	 * Checks the scope of a facet
	 * 
	 * @param facet
	 * @param res
	 */
	private void checkScope(IContractSegment facet, IResource res) {
		ISegmentScope scope = facet.getISegmentScope();
		if (scope.getPatterns().length == 0) {
			TigerstripeProjectAuditor.reportWarning(
					"No scope defined for facet '" + facet.getName() + "'.",
					res, 222);
		} else {
			checkArtifactScopes(facet, res);
		}
		
		// Check that the annotationContext is valid, i.e.
		// either include or exclude or nothing, but not both include and exclude
		if (scope.getAnnotationContextPatterns(ISegmentScope.EXCLUDES).length != 0 
				&& scope.getAnnotationContextPatterns(ISegmentScope.INCLUDES).length != 0 ) {
			TigerstripeProjectAuditor.reportError(
					"Inconsistent Annotation context definition: use 'Inclusion' or 'Exclusion', but not both. (" + facet.getName() + ").",
					res, 222);
		}
	}

	private void checkUseCaseDocs(IContractSegment facet, IResource res) {
		IUseCaseReference[] refs = facet.getUseCaseRefs();
		for (IUseCaseReference ref : refs) {
			if (!ref.canResolve()) {
				TigerstripeProjectAuditor.reportError(
						"Invalid Use Case Document '"
								+ ref.getProjectRelativePath()
								+ "' referenced in facet '" + facet.getName()
								+ "'.", res, 222);
			}
		}
	}

	/**
	 * Checks that if a scope mentions a FQN explicity that FQN can be resolved
	 * 
	 * @param facet
	 * @param res
	 */
	private void checkArtifactScopes(IContractSegment facet, IResource res) {
		ISegmentScope scope = facet.getISegmentScope();
		ITigerstripeModelProject tsProject = getTSProject();
		if (tsProject == null)
			return;

		try {
			IArtifactManagerSession session = tsProject
					.getArtifactManagerSession();
			IArtifactQuery query = session.makeQuery(IQueryAllArtifacts.class
					.getName());
			Collection<IAbstractArtifact> allartifacts = session
					.queryArtifact(query);
			for (ScopePattern pattern : scope
					.getPatterns(ISegmentScope.INCLUDES)) {
				if (pattern.isFQN()) {
					String fqn = pattern.pattern;
					IAbstractArtifact artifact = session
							.getArtifactByFullyQualifiedName(fqn);
					if (artifact == null) {
						TigerstripeProjectAuditor.reportError("Artifact '"
								+ fqn + "' included in scope of '"
								+ facet.getName() + "' cannot be resolved.",
								res, 222);
					}
				} else {
					// Its a package pattern - or at least it should be
					/*
					 * It should match "something" in the project We don't
					 * manage packages explicitly
					 * 
					 * Use a very large sledgehammer..
					 */
					Pattern p = Pattern.compile(ContractUtils
							.mapFromUserPattern(pattern.pattern));
					boolean foundOne = false;
					for (IAbstractArtifact art : allartifacts) {
						Matcher m = p.matcher(art.getFullyQualifiedName());
						if (m.matches()) {
							foundOne = true;
							break;
						}
					}
					if (!foundOne) {
						TigerstripeProjectAuditor.reportError("Pattern '"
								+ pattern.pattern
								+ "' in 'includes' scope of '"
								+ facet.getName()
								+ "' does not match any artifacts.", res, 222);
					}

				}
			}

			for (ScopePattern pattern : scope
					.getPatterns(ISegmentScope.EXCLUDES)) {
				if (pattern.isFQN()) {
					String fqn = pattern.pattern;
					IAbstractArtifact artifact = session
							.getArtifactByFullyQualifiedName(fqn);
					if (artifact == null) {
						TigerstripeProjectAuditor.reportError("Artifact '"
								+ fqn + "' excluded of scope of '"
								+ facet.getName() + "' cannot be resolved.",
								res, 222);
					}
				} else {
					// Its a package pattern - or at least it should be
					/*
					 * It should match "something" in the project We don't
					 * manage packages explicitly
					 * 
					 * Use a very large sledgehammer..
					 */
					Pattern p = Pattern.compile(ContractUtils
							.mapFromUserPattern(pattern.pattern));
					boolean foundOne = false;
					for (IAbstractArtifact art : allartifacts) {
						Matcher m = p.matcher(art.getFullyQualifiedName());
						if (m.matches()) {
							foundOne = true;
							break;
						}
					}
					if (!foundOne) {
						TigerstripeProjectAuditor.reportError("Pattern '"
								+ pattern.pattern
								+ "' in 'excludes' scope of '"
								+ facet.getName()
								+ "' does not match any artifacts.", res, 222);
					}

				}
			}

		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
	}

	private static final char[] invalidStereotypeNameChars = { ' ', ';', '\'',
			':', ',', '<', '>', '"', '/', '?', '!', '@', '#', '$', '%', '^',
			'&', '*', '(', ')', '-', '+', '=', '|', '\\' };
}
