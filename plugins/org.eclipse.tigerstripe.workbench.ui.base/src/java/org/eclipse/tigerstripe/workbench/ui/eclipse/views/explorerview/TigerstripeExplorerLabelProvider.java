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
package org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview;

/**
 * This now deprecated as of 2.2.1.
 * 
 * This code had been put in place originaly by doing a lot of duplication of
 * the eclipse code. This has been re-worked and replaced by
 * {@link NewTigerstripeExplorerLabelProvider} that is now used in the
 * {@link TigerstripeExplorerPart}
 * 
 * This class is only maintained here for future reference and should not be
 * used.
 * 
 * @author Eric Dillon
 * @deprecated since 2.2.1 using {@link NewTigerstripeExplorerLabelProvider}
 *             instead
 */
@Deprecated
public class TigerstripeExplorerLabelProvider
// extends
// AppearanceAwareLabelProvider
{
	//
	// private AbstractArtifactLabelProvider artifactLabelProvider = new
	// AbstractArtifactLabelProvider();
	//
	// private TigerstripeExplorerContentProvider fContentProvider;
	//
	// private boolean fIsFlatLayout;
	//
	// private TigerstripeExplorerProblemsDecorator fProblemDecorator;
	//
	// TigerstripeExplorerLabelProvider(long textFlags, int imageFlags,
	// TigerstripeExplorerContentProvider cp) {
	// super(textFlags, imageFlags);
	// fProblemDecorator = new TigerstripeExplorerProblemsDecorator();
	// addLabelDecorator(fProblemDecorator);
	// Assert.isNotNull(cp);
	// fContentProvider = cp;
	// }
	//
	// private String getNameDelta(IPackageFragment topFragment,
	// IPackageFragment bottomFragment) {
	//
	// String topName = topFragment.getElementName();
	// String bottomName = bottomFragment.getElementName();
	//
	// if (topName.equals(bottomName))
	// return topName;
	//
	// String deltaname = bottomName.substring(topName.length() + 1);
	// return deltaname;
	// }
	//
	// public void setIsFlatLayout(boolean state) {
	// fIsFlatLayout = state;
	// fProblemDecorator.setIsFlatLayout(state);
	// }
	//
	// public String getInternalText(Object element) {
	//
	// if (fIsFlatLayout || !(element instanceof IPackageFragment))
	// return super.getText(element);
	//
	// IPackageFragment fragment = (IPackageFragment) element;
	//
	// if (fragment.isDefaultPackage()) {
	// return super.getText(fragment);
	// } else {
	// Object parent = fContentProvider.getPackageFragmentProvider()
	// .getParent(fragment);
	// if (parent instanceof IPackageFragment)
	// return getNameDelta((IPackageFragment) parent, fragment);
	// else
	// return super.getText(fragment);
	// }
	// }
	//
	// public String getText(Object element) {
	// if (element instanceof IJavaProject) {
	// IJavaProject jProject = (IJavaProject) element;
	// IProject iProject = jProject.getProject();
	// try {
	// if (TigerstripeProjectNature.hasNature(iProject)) {
	// IAbstractTigerstripeProject aProject = EclipsePlugin
	// .getITigerstripeProjectFor(iProject);
	// if (aProject instanceof ITigerstripeProject) {
	// ITigerstripeProject tsProject = (ITigerstripeProject) aProject;
	// try {
	// if (tsProject.getActiveFacet() != null
	// && tsProject.getActiveFacet().canResolve()) {
	// String activeName = jProject.getElementName()
	// + " {"
	// + tsProject.getActiveFacet().resolve()
	// .getName() + "}";
	// return decorateText(activeName, element);
	// }
	// } catch (TigerstripeException e) {
	// // Upon import the tigerstripe.xml may not be
	// // there yet, so all we want is to display
	// // the project name. There won't be any active
	// // facet at that stage anyway
	// return super.getText(element);
	// }
	// }
	// }
	// } catch (CoreException e) {
	// EclipsePlugin.log(e);
	// }
	// return super.getText(element);
	// } else if (element instanceof IJavaElement) {
	// IJavaElement jElem = (IJavaElement) element;
	//
	// if (jElem.getElementType() == IJavaElement.COMPILATION_UNIT
	// || jElem.getElementType() == IJavaElement.CLASS_FILE) {
	// IAbstractArtifact artifact = TSExplorerUtils
	// .getArtifactFor(element);
	// if (artifact != null) {
	// return decorateText(artifact.getName(), element);
	// } else {
	// return super.getText(element);
	// }
	// }
	// } else if (element instanceof IField) {
	// IField field = (IField) element;
	// return decorateText(field.getLabelString(), element);
	// } else if (element instanceof IMethod) {
	// IMethod method = (IMethod) element;
	// return decorateText(method.getLabelString(), element);
	// } else if (element instanceof ILabel) {
	// ILabel label = (ILabel) element;
	// return decorateText(label.getLabelString(), element);
	// } else if (element instanceof IRelationshipEnd) {
	// IRelationshipEnd end = (IRelationshipEnd) element;
	// // String endName =
	// // end.getNameForType(end.getType().getFullyQualifiedName());
	// String endName = end.getName();
	// return (endName.equals("")) ? decorateText(end.getType().getName(),
	// element) : decorateText(endName + "::"
	// + end.getType().getName(), element);
	// } else if (element instanceof AbstractLogicalExplorerNode) {
	// AbstractLogicalExplorerNode node = (AbstractLogicalExplorerNode) element;
	// return decorateText(node.getText(), element);
	// }
	//
	// return getInternalText(element);
	// }
	//
	// public Image getImage(Object element) {
	//
	// if (element instanceof IJavaProject) {
	// IJavaProject jProject = (IJavaProject) element;
	// IProject iProject = jProject.getProject();
	//
	// try {
	// if (TigerstripeProjectNature.hasNature(iProject)
	// && iProject.isOpen()) {
	// return decorateImage(TigerstripePluginImages
	// .get(TigerstripePluginImages.TSPROJECT_FOLDER),
	// element);
	// } else if (TigerstripePluginProjectNature.hasNature(iProject)
	// && iProject.isOpen()) {
	// return decorateImage(TigerstripePluginImages
	// .get(TigerstripePluginImages.PLUGINPROJECT_FOLDER),
	// element);
	// }
	// } catch (CoreException e) {
	// EclipsePlugin.log(e);
	// }
	// } else if (element instanceof IJavaElement) {
	// IJavaElement jElem = (IJavaElement) element;
	//
	// // Tigerstripe projects need special handling to display icons
	// // properly
	// IProject iProject = jElem.getJavaProject().getProject();
	// try {
	// if (TigerstripeProjectNature.hasNature(iProject)) {
	// if (jElem.getElementType() == IJavaElement.COMPILATION_UNIT
	// || jElem.getElementType() == IJavaElement.CLASS_FILE) {
	// IAbstractArtifact artifact = TSExplorerUtils
	// .getArtifactFor(element);
	// if (artifact != null) {
	// return decorateImage(artifactLabelProvider
	// .getImage(artifact), element);
	// } else {
	// return super.getImage(element);
	// }
	// }
	// }
	// } catch (CoreException e) {
	// EclipsePlugin.log(e);
	// }
	// } else if (element instanceof IField) {
	// return decorateImage(Images.getInstance().get(
	// Images.FIELDPRIVATE_ICON), element);
	// } else if (element instanceof IMethod) {
	// return decorateImage(Images.getInstance()
	// .get(Images.METHODPUB_ICON), element);
	// } else if (element instanceof ILabel) {
	// return decorateImage(Images.getInstance().get(Images.LITERAL_ICON),
	// element);
	// } else if (element instanceof IAssociationEnd) {
	// return decorateImage(Images.getInstance().get(
	// Images.ASSOCIATIONARROW_ICON), element);
	// } else if (element instanceof IRelationshipEnd) {
	// return decorateImage(Images.getInstance().get(
	// Images.DEPENDENCYARROW_ICON), element);
	// } else if (element instanceof AbstractLogicalExplorerNode) {
	// AbstractLogicalExplorerNode node = (AbstractLogicalExplorerNode) element;
	// if (node.getImage() != null) {
	// return decorateImage(node.getImage(), node.getKeyResource());
	// } else {
	// return super.getImage(node.getKeyResource());
	// }
	// }
	// return super.getImage(element);
	// }
	//
	// @Override
	// public Color getForeground(Object element) {
	// if (element instanceof IJavaProject) {
	// IJavaProject jProject = (IJavaProject) element;
	// IProject iProject = jProject.getProject();
	// try {
	// if (TigerstripeProjectNature.hasNature(iProject)) {
	// IAbstractTigerstripeProject aProject = EclipsePlugin
	// .getITigerstripeProjectFor(iProject);
	// if (aProject instanceof ITigerstripeProject) {
	// ITigerstripeProject tsProject = (ITigerstripeProject) aProject;
	// try {
	// if (tsProject.getActiveFacet() != null) {
	// return ColorUtils.TS_ORANGE;
	// }
	// } catch (TigerstripeException e) {
	// // upon import the project cannot be resolved
	// // yet, and the facet cannot be determined.
	// return ColorUtils.BLACK;
	// }
	// }
	// }
	// } catch (CoreException e) {
	// EclipsePlugin.log(e);
	// }
	// return ColorUtils.BLACK;
	// } else if (element instanceof IJavaElement) {
	// IJavaElement jElem = (IJavaElement) element;
	//
	// // Tigerstripe projects need special handling to display icons
	// // properly
	// IProject iProject = jElem.getJavaProject().getProject();
	// try {
	// if (TigerstripeProjectNature.hasNature(iProject)) {
	// if (jElem.getElementType() == IJavaElement.COMPILATION_UNIT
	// || jElem.getElementType() == IJavaElement.CLASS_FILE) {
	// IAbstractArtifact artifact = TSExplorerUtils
	// .getArtifactFor(element);
	// if (artifact != null) {
	// return artifactLabelProvider
	// .getForeground(artifact);
	// } else {
	// return super.getForeground(element);
	// }
	// }
	// }
	// } catch (CoreException e) {
	// EclipsePlugin.log(e);
	// }
	// } else if (element instanceof IField) {
	// return artifactLabelProvider.getForeground(element);
	// } else if (element instanceof IMethod) {
	// return artifactLabelProvider.getForeground(element);
	// } else if (element instanceof ILabel) {
	// return artifactLabelProvider.getForeground(element);
	// } else if (element instanceof IAssociationEnd) {
	// return artifactLabelProvider.getForeground(element);
	// } else if (element instanceof IRelationshipEnd) {
	// return artifactLabelProvider.getForeground(element);
	// } else if (element instanceof IResource
	// && IContractSegment.FILE_EXTENSION.equals(((IResource) element)
	// .getFileExtension())) {
	// IResource res = (IResource) element;
	// IProject project = res.getProject();
	// IAbstractTigerstripeProject aProject = EclipsePlugin
	// .getITigerstripeProjectFor(project);
	// if (aProject instanceof ITigerstripeProject) {
	// ITigerstripeProject tsProject = (ITigerstripeProject) aProject;
	// try {
	// if (tsProject.getActiveFacet() != null) {
	// IFacetReference ref = tsProject.getActiveFacet();
	// if (res.getLocationURI().equals(ref.getURI())) {
	// return ColorUtils.TS_ORANGE;
	// }
	// }
	// } catch (TigerstripeException e) {
	// EclipsePlugin.log(e);
	// }
	// }
	// }
	// return super.getForeground(element);
	// }
	//
}
