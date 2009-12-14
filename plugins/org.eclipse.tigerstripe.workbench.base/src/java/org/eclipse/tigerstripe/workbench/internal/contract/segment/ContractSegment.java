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
package org.eclipse.tigerstripe.workbench.internal.contract.segment;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.ScopeAnnotationPattern;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.ScopePattern;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.ScopeStereotypePattern;
import org.eclipse.tigerstripe.workbench.internal.api.contract.useCase.IUseCaseReference;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeOssjProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.contract.useCase.UseCaseReference;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.internal.core.versioning.VersionAwareElement;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ContractSegment extends VersionAwareElement implements
		IContractSegment {

	private final static String ELEMENT_TAG = "contractSegment";

	public static final String FACET_TAG = "facetReference";

	public ContractSegment(URI uri) {
		super(uri);
	}

	private SegmentScope scope = new SegmentScope();

	private List<IUseCaseReference> useCaseRefs = new ArrayList<IUseCaseReference>();

	/**
	 * A set of facet references of which the scope will be included in this
	 * facet.
	 */
	private List<IFacetReference> facetReferences = new ArrayList<IFacetReference>();

	public void addUseCase(IUseCaseReference useCase)
			throws TigerstripeException {
		if (!useCaseRefs.contains(useCase)) {
			useCaseRefs.add(useCase);
		}
	}

	public IUseCaseReference[] getUseCaseRefs() {
		IUseCaseReference[] result = new IUseCaseReference[useCaseRefs.size()];
		return useCaseRefs.toArray(result);
	}

	public void removeUseCase(IUseCaseReference useCase)
			throws TigerstripeException {
		if (useCaseRefs.contains(useCase)) {
			useCaseRefs.remove(useCase);
		}
	}

	public ISegmentScope getISegmentScope() {
		return scope;
	}

	/**
	 * Returns the combined scope for the corresponding facet.
	 * 
	 * Indeed since Bug 1016, the scope of a facet can be augmented with
	 * references to other facets.
	 * 
	 * @param facet
	 * @return
	 */
	public ISegmentScope getCombinedScope() throws TigerstripeException {
		if (getFacetReferences().length == 0)
			return getISegmentScope();
		else {
			SegmentScope scope = new SegmentScope();
			for (IFacetReference ref : getFacetReferences()) {
				if (!ref.canResolve())
					throw new TigerstripeException("Can't resolve facet: "
							+ ref.getProjectRelativePath());

				if (ref.resolve().getURI().equals(getURI()))
					throw new TigerstripeException(
							"Circular facet reference detected. Ignoring.");
				ISegmentScope refScope = ref.resolve().getCombinedScope();
				ScopeStereotypePattern[] annPatterns = refScope
						.getStereotypePatterns();
				for (ScopeStereotypePattern annPattern : annPatterns) {
					scope.addStereotypePattern(annPattern);
				}

				ScopePattern[] scopePatterns = refScope.getPatterns();
				for (ScopePattern scopePattern : scopePatterns) {
					scope.addPattern(scopePattern);
				}
			}

			ISegmentScope origScope = getISegmentScope();
			for (ScopeStereotypePattern annPattern : origScope
					.getStereotypePatterns()) {
				scope.addStereotypePattern(annPattern);
			}
			for (ScopePattern scopePattern : origScope.getPatterns()) {
				scope.addPattern(scopePattern);
			}
			return scope;
		}
	}

	@Override
	protected void appendBody(Element rootElement) throws TigerstripeException {
		appendScope(rootElement);
		appendFacetReferences(rootElement);
		appendUseCases(rootElement);
	}

	@Override
	protected String getElementTag() {
		return ELEMENT_TAG;
	}

	@Override
	protected void parseBody(Element rootElement) throws TigerstripeException {
		parseScope(rootElement);
		parseFacetReferences(rootElement);
		parseUseCases(rootElement);
	}

	private void appendFacetReferences(Element rootElement)
			throws TigerstripeException {
		Element useCasesElem = rootElement.addElement("facetRefs");

		for (IFacetReference ref : facetReferences) {

			Element refElm = useCasesElem.addElement(FACET_TAG);
			if (ref.isAbsolute()) {
				try {
					refElm.addAttribute("uri", ref.getURI().toASCIIString());
				} catch (TigerstripeException e) {
					TigerstripeRuntime.logErrorMessage(
							"TigerstripeException detected", e);
				}
			} else {
				refElm.addAttribute("relPath", Util.fixWindowsPath(ref
						.getProjectRelativePath()));

				if (getContainingProject() != null) {
					if (ref.getContainingProject() != null
							&& !ref.getContainingProject().getName().equals(
									getContainingProject().getName())) {
						refElm.addAttribute("project", ref
								.getContainingProject().getName());
					}
				}
			}
		}
	}

	private void appendUseCases(Element rootElement)
			throws TigerstripeException {
		Element useCasesElem = rootElement.addElement("useCaseRefs");

		for (IUseCaseReference ref : getUseCaseRefs()) {
			Element useCaseElem = useCasesElem.addElement("useCaseRef");
			if (ref.isAbsolute()) {
				try {
					useCaseElem.addAttribute("uri", ref.getURI()
							.toASCIIString());
				} catch (TigerstripeException e) {
					TigerstripeRuntime.logErrorMessage(
							"TigerstripeException detected", e);
				}
			} else {
				useCaseElem.addAttribute("relPath", Util.fixWindowsPath(ref
						.getProjectRelativePath()));
				// if (ref.getContainingProject() != null
				// && !ref.getContainingProject().getProjectLabel()
				// .equals(getProjectLabel())) {
				// useCaseElem.addAttribute("project",
				// ref.getContainingProject()
				// .getProjectLabel());
				// }
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void parseFacetReferences(Element rootElement)
			throws TigerstripeException {
		facetReferences.clear();

		Element facetRefsElem = rootElement.element("facetRefs");
		if (facetRefsElem == null)
			return;

		for (Iterator iter = facetRefsElem.elementIterator(FACET_TAG); iter
				.hasNext();) {
			Element facetRefElem = (Element) iter.next();

			Attribute uriNode = facetRefElem.attribute("uri");
			Attribute relPath = facetRefElem.attribute("relPath");
			Attribute projectLabel = facetRefElem.attribute("project");
			String uriStr = null;
			String relPathStr = null;
			String projectLabelStr = null;
			if (uriNode != null)
				uriStr = uriNode.getStringValue();

			if (relPath != null) {
				relPathStr = Util.fixWindowsPath(relPath.getStringValue());
			}

			if (projectLabel != null) {
				projectLabelStr = projectLabel.getStringValue();
			}

			try {
				FacetReference ref = null;
				if (uriStr != null) {
					URI uri = new URI(uriStr);
					ref = new FacetReference(uri,
							(ITigerstripeModelProject) this
									.getContainingProject());
				} else if (relPathStr != null) {
					if (projectLabelStr != null) {
						ref = new FacetReference(
								relPathStr,
								projectLabelStr,
								(ITigerstripeModelProject) getContainingProject());
					} else {
						TigerstripeProject proj = ((TigerstripeOssjProjectHandle) getContainingProject())
								.getTSProject();
						ref = new FacetReference(relPathStr, proj);
					}
				}
				if (ref != null) {
					facetReferences.add(ref);
				}
			} catch (URISyntaxException e) {
				TigerstripeRuntime.logErrorMessage(
						"URISyntaxException detected", e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void parseUseCases(Element rootElement) throws TigerstripeException {
		useCaseRefs.clear();
		Element useCasesElem = rootElement.element("useCaseRefs");
		if (useCasesElem == null)
			return;

		for (Iterator iter = useCasesElem.elementIterator("useCaseRef"); iter
				.hasNext();) {
			Element useCaseElem = (Element) iter.next();
			Attribute uriNode = useCaseElem.attribute("uri");
			Attribute relPath = useCaseElem.attribute("relPath");
			Attribute projectLabel = useCaseElem.attribute("project");
			String uriStr = null;
			String relPathStr = null;
			String projectLabelStr = null;
			if (uriNode != null)
				uriStr = uriNode.getStringValue();

			if (relPath != null) {
				relPathStr = relPath.getStringValue();
			}

			if (projectLabel != null) {
				projectLabelStr = projectLabel.getStringValue();
			}

			try {
				UseCaseReference ref = null;
				if (uriStr != null) {
					URI uri = new URI(uriStr);
					ref = new UseCaseReference(uri,
							(ITigerstripeModelProject) getContainingProject());
				} else if (relPathStr != null) {

					if (projectLabelStr != null) {
						ref = new UseCaseReference(
								relPathStr,
								projectLabelStr,
								(ITigerstripeModelProject) getContainingProject());
					} else {
						if (getContainingProject() instanceof ITigerstripeModelProject) {
							TigerstripeProjectHandle handle = (TigerstripeProjectHandle) getContainingProject();
							ref = new UseCaseReference(relPathStr, handle
									.getTSProject());
						}
					}
				}
				if (ref != null) {
					useCaseRefs.add(ref);
				}
			} catch (URISyntaxException e) {
				TigerstripeRuntime.logErrorMessage(
						"URISyntaxException detected", e);
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeException detected", e);
			}
		}
	}

	private void appendScope(Element rootElement) throws TigerstripeException {
		Element scopeElement = rootElement.addElement("scope");
		for (ScopePattern pattern : getISegmentScope().getPatterns()) {
			Element patternElement = scopeElement.addElement("pattern");
			patternElement.addAttribute("type", String.valueOf(pattern.type));
			patternElement.addText(pattern.pattern);
		}

		// "annotationPattern" should not be used anymore. It was used when
		// "stereotypes" were named "annotations" in Tigerstripe.
		// Now that we have the TAF, this is now deprecated.
		// for (ScopeStereotypePattern pattern : getISegmentScope()
		// .getStereotypePatterns()) {
		// Element patternElement = scopeElement
		// .addElement("annotationPattern");
		// patternElement.addAttribute("type", String.valueOf(pattern.type));
		// patternElement.addText(pattern.stereotypeName);
		// }

		for (ScopeStereotypePattern pattern : getISegmentScope()
				.getStereotypePatterns()) {
			Element patternElement = scopeElement
					.addElement("stereotypePattern");
			patternElement.addAttribute("type", String.valueOf(pattern.type));
			patternElement.addText(pattern.stereotypeName);
		}

		for (ScopeAnnotationPattern pattern : getISegmentScope()
				.getAnnotationPatterns()) {
			Element patternElement = scopeElement
					.addElement("tsAnnotationPattern");
			patternElement.addAttribute("type", String.valueOf(pattern.type));
			patternElement.addText(pattern.annotationID);
		}

		for (ScopeAnnotationPattern pattern : getISegmentScope()
				.getAnnotationContextPatterns()) {
			Element patternElement = scopeElement
					.addElement("annotationContextPattern");
			patternElement.addAttribute("type", String.valueOf(pattern.type));
			patternElement.addText(pattern.annotationID);
		}
	}

	@SuppressWarnings("unchecked")
	private void parseScope(Element rootElement) throws TigerstripeException {
		scope.clear();
		Element scopeElement = rootElement.element("scope");
		if (scopeElement != null) {
			for (Element patternElement : (List<Element>) scopeElement
					.elements()) {
				int type = Integer.parseInt(patternElement
						.attributeValue("type"));
				String pattern = patternElement.getText();
				if ("pattern".equals(patternElement.getName())) {
					ScopePattern pat = new ScopePattern();
					pat.type = type;
					pat.pattern = pattern;
					scope.addPattern(pat);
				} else if ("annotationPattern".equals(patternElement.getName())) {
					// This tag is now deprecated, and only read for
					// compatibility.
					// the tag to use is now "stereotypePattern"
					ScopeStereotypePattern pat = new ScopeStereotypePattern();
					pat.type = type;
					pat.stereotypeName = pattern;
					scope.addStereotypePattern(pat);
				} else if ("stereotypePattern".equals(patternElement.getName())) {
					ScopeStereotypePattern pat = new ScopeStereotypePattern();
					pat.type = type;
					pat.stereotypeName = pattern;
					scope.addStereotypePattern(pat);
				} else if ("tsAnnotationPattern".equals(patternElement
						.getName())) {
					ScopeAnnotationPattern pat = new ScopeAnnotationPattern();
					pat.type = type;
					pat.annotationID = pattern;
					scope.addAnnotationPattern(pat);
				} else if ("annotationContextPattern".equals(patternElement
						.getName())) {
					ScopeAnnotationPattern pat = new ScopeAnnotationPattern();
					pat.type = type;
					pat.annotationID = pattern;
					scope.addAnnotationContextPattern(pat);
				}
			}
		}
	}

	public void addFacetReference(IFacetReference facetReference)
			throws TigerstripeException {
		if (!facetReferences.contains(facetReference)) {
			facetReferences.add(facetReference);
		}
	}

	public IFacetReference[] getFacetReferences() {
		IFacetReference[] result = new IFacetReference[facetReferences.size()];
		return facetReferences.toArray(result);
	}

	public void removeFacetReference(IFacetReference facetReference)
			throws TigerstripeException {
		if (facetReferences.contains(facetReference)) {
			facetReferences.remove(facetReference);
		}
	}

	public String[] getAnnotationContext() {
		ArrayList<String> result = new ArrayList<String>();

		if (getISegmentScope().getAnnotationContextPatterns(
				ISegmentScope.INCLUDES).length == 0
				&& getISegmentScope().getAnnotationContextPatterns(
						ISegmentScope.EXCLUDES).length == 0) {
			for (AnnotationType type : AnnotationPlugin.getManager().getTypes()) {
				result.add(type.getId());
			}
		} else if (getISegmentScope().getAnnotationContextPatterns(
				ISegmentScope.INCLUDES).length != 0) {
			// make sure we only include those that are defined
			List<ScopeAnnotationPattern> includePatterns = Arrays
					.asList(getISegmentScope().getAnnotationContextPatterns(
							ISegmentScope.INCLUDES));
			ArrayList<String> includeIds = new ArrayList<String>();
			for (ScopeAnnotationPattern pattern : includePatterns) {
				includeIds.add(pattern.annotationID);
			}
			for (AnnotationType type : AnnotationPlugin.getManager().getTypes()) {
				String id = type.getId();
				if (includeIds.contains(id)) {
					result.add(id);
				}
			}
		} else if (getISegmentScope().getAnnotationContextPatterns(
				ISegmentScope.EXCLUDES).length != 0) {
			List<ScopeAnnotationPattern> excludePatterns = Arrays
					.asList(getISegmentScope().getAnnotationContextPatterns(
							ISegmentScope.EXCLUDES));
			ArrayList<String> excludeIds = new ArrayList<String>();
			for (ScopeAnnotationPattern pattern : excludePatterns) {
				excludeIds.add(pattern.annotationID);
			}
			for (AnnotationType type : AnnotationPlugin.getManager().getTypes()) {
				String id = type.getId();
				if (!excludeIds.contains(id)) {
					result.add(id);
				}
			}
		}

		return result.toArray(new String[result.size()]);
	}
}
