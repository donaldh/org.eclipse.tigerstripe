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
 * {@link NewTigerstripeExplorerContentProvider} that is now used in the
 * {@link TigerstripeExplorerPart}
 * 
 * This class is only maintained here for future reference and should not be
 * used.
 * 
 * @author Eric Dillon
 * @deprecated since 2.2.1 using {@link NewTigerstripeExplorerContentProvider}
 *             instead
 */
@Deprecated
class TigerstripeExplorerContentProvider
// extends
// StandardJavaElementContentProvider implements IElementChangedListener
{
	//
	// protected static final int ORIGINAL = 0;
	//
	// protected static final int PARENT = 1 << 0;
	//
	// protected static final int GRANT_PARENT = 1 << 1;
	//
	// protected static final int PROJECT = 1 << 2;
	//
	// TreeViewer fViewer;
	//
	// private Object fInput;
	//
	// private boolean fIsFlatLayout;
	//
	// private PackageFragmentProvider fPackageFragmentProvider = new
	// PackageFragmentProvider();
	//
	// private int fPendingChanges;
	//
	// TigerstripeExplorerPart fPart;
	//
	// public PackageFragmentProvider getPackageFragmentProvider() {
	// return this.fPackageFragmentProvider;
	// }
	//
	// public TigerstripeExplorerContentProvider(TigerstripeExplorerPart part,
	// boolean provideMembers) {
	// super(provideMembers);
	// fPart = part;
	// }
	//
	// public void elementChanged(ElementChangedEvent event) {
	// // 58952 delete project does not update Package Explorer [package
	// // explorer]
	// // if the input to the viewer is deleted then refresh to avoid the
	// // display of stale elements
	// if (inputDeleted())
	// return;
	//
	// // Creating an extra thread here... that did not exist in the
	// // PackageExplorer case
	// // Not sure why it is needed here... :-(
	// final ElementChangedEvent eventF = event;
	// postRunnable(new Runnable() {
	// public void run() {
	// try {
	// processDelta(eventF.getDelta());
	// } catch (JavaModelException e) {
	// EclipsePlugin.log(e);
	// }
	//
	// }
	// });
	// }
	//
	// private boolean inputDeleted() {
	// if (fInput == null)
	// return false;
	// if ((fInput instanceof IJavaElement)
	// && ((IJavaElement) fInput).exists())
	// return false;
	// if ((fInput instanceof IResource) && ((IResource) fInput).exists())
	// return false;
	// if (fInput instanceof WorkingSetModel)
	// return false;
	// postRefresh(fInput, ORIGINAL, fInput);
	// return true;
	// }
	//
	// /**
	// * Processes a delta recursively. When more than two children are affected
	// * the tree is fully refreshed starting at this node. The delta is
	// processed
	// * in the current thread but the viewer updates are posted to the UI
	// thread.
	// */
	// private void processDelta(IJavaElementDelta delta)
	// throws JavaModelException {
	//
	// int kind = delta.getKind();
	// int flags = delta.getFlags();
	// IJavaElement element = delta.getElement();
	// int elementType = element.getElementType();
	//
	// if (elementType != IJavaElement.JAVA_MODEL
	// && elementType != IJavaElement.JAVA_PROJECT) {
	// IJavaProject proj = element.getJavaProject();
	// if (proj == null || !proj.getProject().isOpen()) // TODO: Not
	// // needed if
	// // parent
	// // already did
	// // the 'open'
	// // check!
	// return;
	// }
	//
	// if (!fIsFlatLayout && elementType == IJavaElement.PACKAGE_FRAGMENT) {
	// fPackageFragmentProvider.processDelta(delta);
	// if (processResourceDeltas(delta.getResourceDeltas(), element))
	// return;
	// handleAffectedChildren(delta, element);
	// return;
	// }
	//
	// if (elementType == IJavaElement.COMPILATION_UNIT) {
	// ICompilationUnit cu = (ICompilationUnit) element;
	// if (!JavaModelUtil.isPrimary(cu)) {
	// return;
	// }
	//
	// if (!getProvideMembers() && cu.isWorkingCopy()) {
	// return;
	// }
	//
	// if ((kind == IJavaElementDelta.CHANGED)
	// && !isStructuralCUChange(flags)) {
	// return; // test moved ahead
	// }
	//
	// if (!isOnClassPath(cu)) { // TODO: isOnClassPath expensive! Should
	// // be put after all cheap tests
	// return;
	// }
	//
	// }
	//
	// if (elementType == IJavaElement.JAVA_PROJECT) {
	//
	// // handle open and closing of a project
	// if ((flags & (IJavaElementDelta.F_CLOSED | IJavaElementDelta.F_OPENED))
	// != 0) {
	// postRefresh(element, ORIGINAL, element);
	// return;
	// }
	// // if the raw class path has changed we refresh the entire project
	// if ((flags & IJavaElementDelta.F_CLASSPATH_CHANGED) != 0) {
	// postRefresh(element, ORIGINAL, element);
	// return;
	// }
	// }
	//
	// if (kind == IJavaElementDelta.REMOVED) {
	// Object parent = internalGetParent(element);
	// if (element instanceof IPackageFragment) {
	// // refresh package fragment root to allow filtering empty
	// // (parent) packages: bug 72923
	// if (fViewer.testFindItem(parent) != null)
	// postRefresh(parent, PARENT, element);
	// return;
	// }
	//
	// postRemove(element);
	// if (parent instanceof IPackageFragment)
	// postUpdateIcon((IPackageFragment) parent);
	// // we are filtering out empty subpackages, so we
	// // a package becomes empty we remove it from the viewer.
	// if (isPackageFragmentEmpty(element.getParent())) {
	// if (fViewer.testFindItem(parent) != null)
	// postRefresh(internalGetParent(parent), GRANT_PARENT,
	// element);
	// }
	// return;
	// }
	//
	// if (kind == IJavaElementDelta.ADDED) {
	// Object parent = internalGetParent(element);
	// // we are filtering out empty subpackages, so we
	// // have to handle additions to them specially.
	// if (parent instanceof IPackageFragment) {
	// Object grandparent = internalGetParent(parent);
	// // 1GE8SI6: ITPJUI:WIN98 - Rename is not shown in Packages View
	// // avoid posting a refresh to an unvisible parent
	// if (parent.equals(fInput)) {
	// postRefresh(parent, PARENT, element);
	// } else {
	// // refresh from grandparent if parent isn't visible yet
	// if (fViewer.testFindItem(parent) == null)
	// postRefresh(grandparent, GRANT_PARENT, element);
	// else {
	// postRefresh(parent, PARENT, element);
	// }
	// }
	// return;
	// } else {
	// postAdd(parent, element);
	// }
	// }
	//
	// if (elementType == IJavaElement.COMPILATION_UNIT) {
	// if (kind == IJavaElementDelta.CHANGED) {
	// // isStructuralCUChange already performed above
	// postRefresh(element, ORIGINAL, element);
	// updateSelection(delta);
	// }
	// return;
	// }
	// // no changes possible in class files
	// if (elementType == IJavaElement.CLASS_FILE)
	// return;
	//
	// if (elementType == IJavaElement.PACKAGE_FRAGMENT_ROOT) {
	// // the contents of an external JAR has changed
	// if ((flags & IJavaElementDelta.F_ARCHIVE_CONTENT_CHANGED) != 0) {
	// postRefresh(element, ORIGINAL, element);
	// return;
	// }
	// // the source attachment of a JAR has changed
	// if ((flags & (IJavaElementDelta.F_SOURCEATTACHED |
	// IJavaElementDelta.F_SOURCEDETACHED)) != 0)
	// postUpdateIcon(element);
	//
	// if (isClassPathChange(delta)) {
	// // throw the towel and do a full refresh of the affected java
	// // project.
	// postRefresh(element.getJavaProject(), PROJECT, element);
	// return;
	// }
	// }
	//
	// if (processResourceDeltas(delta.getResourceDeltas(), element))
	// return;
	//
	// handleAffectedChildren(delta, element);
	// }
	//
	// private static boolean isStructuralCUChange(int flags) {
	// // No refresh on working copy creation (F_PRIMARY_WORKING_COPY)
	// return ((flags & IJavaElementDelta.F_CHILDREN) != 0)
	// || ((flags & (IJavaElementDelta.F_CONTENT |
	// IJavaElementDelta.F_FINE_GRAINED)) == IJavaElementDelta.F_CONTENT);
	// }
	//
	// private void handleProjectNatureMigration(IProject project)
	// throws CoreException {
	//
	// if (project != null && project.isOpen()) {
	// // A little logic here due to the split of the community plugin into
	// // *.base and *.ui.base
	// // The nature has changed and needs to be update on the fly
	// IProjectDescription description = project.getDescription();
	// String[] natures = description.getNatureIds();
	// boolean updateOldNature = false;
	// for (int index = 0; index < natures.length; index++) {
	// if (EclipsePlugin.OLDPROJECT_NATURE_ID.equals(natures[index])) {
	// natures[index] = EclipsePlugin.PROJECT_NATURE_ID;
	// updateOldNature = true;
	// } else if (EclipsePlugin.OLDPLUGINPROJECT_NATURE_ID
	// .equals(natures[index])) {
	// natures[index] = EclipsePlugin.PLUGINPROJECT_NATURE_ID;
	// updateOldNature = true;
	// }
	// }
	//
	// if (updateOldNature) {
	// description.setNatureIds(natures);
	// project.setDescription(description, new NullProgressMonitor());
	// }
	// }
	// }
	//
	// /**
	// * Updates the selection. It finds newly added elements and selects them.
	// */
	// private void updateSelection(IJavaElementDelta delta) {
	// final IJavaElement addedElement = findAddedElement(delta);
	// if (addedElement != null) {
	// final StructuredSelection selection = new StructuredSelection(
	// addedElement);
	// postRunnable(new Runnable() {
	// public void run() {
	// Control ctrl = fViewer.getControl();
	// if (ctrl != null && !ctrl.isDisposed()) {
	// // 19431
	// // if the item is already visible then select it
	// if (fViewer.testFindItem(addedElement) != null)
	// fViewer.setSelection(selection);
	// }
	// }
	// });
	// }
	// }
	//
	// private IJavaElement findAddedElement(IJavaElementDelta delta) {
	// if (delta.getKind() == IJavaElementDelta.ADDED)
	// return delta.getElement();
	//
	// IJavaElementDelta[] affectedChildren = delta.getAffectedChildren();
	// for (int i = 0; i < affectedChildren.length; i++)
	// return findAddedElement(affectedChildren[i]);
	//
	// return null;
	// }
	//
	// /**
	// * Updates the package icon
	// */
	// private void postUpdateIcon(final IJavaElement element) {
	// postRunnable(new Runnable() {
	// public void run() {
	// // 1GF87WR: ITPUI:ALL - SWTEx + NPE closing a workbench window.
	// Control ctrl = fViewer.getControl();
	// if (ctrl != null && !ctrl.isDisposed())
	// fViewer.update(element,
	// new String[] { IBasicPropertyConstants.P_IMAGE });
	// }
	// });
	// }
	//
	// /* package */void handleAffectedChildren(IJavaElementDelta delta,
	// IJavaElement element) throws JavaModelException {
	// IJavaElementDelta[] affectedChildren = delta.getAffectedChildren();
	// if (affectedChildren.length > 1) {
	// // a package fragment might become non empty refresh from the parent
	// if (element instanceof IPackageFragment) {
	// IJavaElement parent = (IJavaElement) internalGetParent(element);
	// // 1GE8SI6: ITPJUI:WIN98 - Rename is not shown in Packages View
	// // avoid posting a refresh to an unvisible parent
	// if (element.equals(fInput)) {
	// postRefresh(element, ORIGINAL, element);
	// } else {
	// postRefresh(parent, PARENT, element);
	// }
	// return;
	// }
	// // more than one child changed, refresh from here downwards
	// if (element instanceof IPackageFragmentRoot) {
	// Object toRefresh = skipProjectPackageFragmentRoot((IPackageFragmentRoot)
	// element);
	// postRefresh(toRefresh, ORIGINAL, toRefresh);
	// } else {
	// postRefresh(element, ORIGINAL, element);
	// }
	// return;
	// }
	// processAffectedChildren(affectedChildren);
	// }
	//
	// protected void processAffectedChildren(IJavaElementDelta[]
	// affectedChildren)
	// throws JavaModelException {
	// for (int i = 0; i < affectedChildren.length; i++) {
	// processDelta(affectedChildren[i]);
	// }
	// }
	//
	// private boolean isOnClassPath(ICompilationUnit element) {
	// IJavaProject project = element.getJavaProject();
	// if (project == null || !project.exists())
	// return false;
	// return project.isOnClasspath(element);
	// }
	//
	// /**
	// * Process resource deltas.
	// *
	// * @return true if the parent got refreshed
	// */
	// private boolean processResourceDeltas(IResourceDelta[] deltas, Object
	// parent) {
	// if (deltas == null)
	// return false;
	//
	// if (deltas.length > 1) {
	// // more than one child changed, refresh from here downwards
	// postRefresh(parent, ORIGINAL, parent);
	// return true;
	// }
	//
	// for (int i = 0; i < deltas.length; i++) {
	// if (processResourceDelta(deltas[i], parent))
	// return true;
	// }
	//
	// return false;
	// }
	//
	// /**
	// * Process a resource delta.
	// *
	// * @return true if the parent got refreshed
	// */
	// private boolean processResourceDelta(IResourceDelta delta, Object parent)
	// {
	// int status = delta.getKind();
	// int flags = delta.getFlags();
	//
	// IResource resource = delta.getResource();
	// // filter out changes affecting the output folder
	// if (resource == null)
	// return false;
	//
	// // this could be optimized by handling all the added children in the
	// // parent
	// if ((status & IResourceDelta.REMOVED) != 0) {
	// if (parent instanceof IPackageFragment) {
	// // refresh one level above to deal with empty package filtering
	// // properly
	// postRefresh(internalGetParent(parent), PARENT, parent);
	// return true;
	// } else {
	// postRemove(resource);
	// postRefresh(internalGetParent(parent), PARENT, resource);
	// return true;
	// }
	// }
	// if ((status & IResourceDelta.ADDED) != 0) {
	// if (parent instanceof IPackageFragment) {
	// // refresh one level above to deal with empty package filtering
	// // properly
	// postRefresh(internalGetParent(parent), PARENT, parent);
	// return true;
	// } else {
	// postAdd(parent, resource);
	// postRefresh(internalGetParent(parent), PARENT, resource);
	// return true;
	// }
	// }
	//
	// // open/close state change of a project
	// if ((flags & IResourceDelta.OPEN) != 0) {
	// postProjectStateChanged(internalGetParent(parent));
	// return true;
	// }
	//
	// if ((status & IResourceDelta.CHANGED) != 0) {
	// postRefresh(resource, PARENT, resource);
	// return true;
	// }
	//		
	// processResourceDeltas(delta.getAffectedChildren(), resource);
	// return false;
	// }
	//
	// public void setIsFlatLayout(boolean state) {
	// fIsFlatLayout = state;
	// }
	//
	// private void postRefresh(Object root, int relation, Object
	// affectedElement) {
	// // JFace doesn't refresh when object isn't part of the viewer
	// // Therefore move the refresh start down to the viewer's input
	// if (isParent(root, fInput))
	// root = fInput;
	// List toRefresh = new ArrayList(1);
	// toRefresh.add(root);
	// augmentElementToRefresh(toRefresh, relation, affectedElement);
	// postRefresh(toRefresh, true);
	// }
	//
	// protected void augmentElementToRefresh(List toRefresh, int relation,
	// Object affectedElement) {
	// if (affectedElement instanceof ICompilationUnit) {
	// ICompilationUnit unit = (ICompilationUnit) affectedElement;
	// toRefresh.add(unit.getJavaProject());
	// } else if (affectedElement instanceof IResource) {
	// IResource res = (IResource) affectedElement;
	// IResource parent = res.getParent();
	// if (parent instanceof IProject) {
	// toRefresh.add(JavaCore.create(parent));
	// } else {
	// toRefresh.add(res.getParent());
	// }
	// }
	// }
	//
	// boolean isParent(Object root, Object child) {
	// Object parent = getParent(child);
	// if (parent == null)
	// return false;
	// if (parent.equals(root))
	// return true;
	// return isParent(root, parent);
	// }
	//
	// /* package */void postRefresh(final List toRefresh,
	// final boolean updateLabels) {
	// postRunnable(new Runnable() {
	// public void run() {
	// Control ctrl = fViewer.getControl();
	// if (ctrl != null && !ctrl.isDisposed()) {
	// for (Iterator iter = toRefresh.iterator(); iter.hasNext();) {
	// fViewer.refresh(iter.next(), updateLabels);
	// }
	// }
	// }
	// });
	// }
	//
	// private void postAdd(final Object parent, final Object element) {
	// postRunnable(new Runnable() {
	// public void run() {
	// Control ctrl = fViewer.getControl();
	// if (ctrl != null && !ctrl.isDisposed()) {
	// // TODO workaround for 39754 New projects being added to the
	// // TreeViewer twice
	// if (fViewer.testFindItem(element) == null)
	// fViewer.add(parent, element);
	// }
	// }
	// });
	// }
	//
	// private void postRemove(final Object element) {
	// postRunnable(new Runnable() {
	// public void run() {
	// Control ctrl = fViewer.getControl();
	// if (ctrl != null && !ctrl.isDisposed()) {
	// fViewer.remove(element);
	// }
	// }
	// });
	// }
	//
	// private void postProjectStateChanged(final Object root) {
	// postRunnable(new Runnable() {
	// public void run() {
	// fPart.projectStateChanged(root);
	// }
	// });
	// }
	//
	// /* package */void postRunnable(final Runnable r) {
	// Control ctrl = fViewer.getControl();
	// final Runnable trackedRunnable = new Runnable() {
	// public void run() {
	// try {
	// r.run();
	// } finally {
	// removePendingChange();
	// }
	// }
	// };
	// if (ctrl != null && !ctrl.isDisposed()) {
	// addPendingChange();
	// try {
	// ctrl.getDisplay().asyncExec(trackedRunnable);
	// } catch (RuntimeException e) {
	// removePendingChange();
	// throw e;
	// } catch (Error e) {
	// removePendingChange();
	// throw e;
	// }
	// }
	// }
	//
	// // ------ Pending change management due to the use of asyncExec in
	// // postRunnable.
	//
	// public synchronized boolean hasPendingChanges() {
	// return fPendingChanges > 0;
	// }
	//
	// private synchronized void addPendingChange() {
	// fPendingChanges++;
	// // TigerstripeRuntime.logInfoMessage(fPendingChanges);
	// }
	//
	// synchronized void removePendingChange() {
	// fPendingChanges--;
	// if (fPendingChanges < 0)
	// fPendingChanges = 0;
	// // TigerstripeRuntime.logInfoMessage(fPendingChanges);
	// }
	//
	// /*
	// * (non-Javadoc) Method declared on IContentProvider.
	// */
	// public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	// {
	// super.inputChanged(viewer, oldInput, newInput);
	// fPackageFragmentProvider.inputChanged(viewer, oldInput, newInput);
	// fViewer = (TreeViewer) viewer;
	// if (oldInput == null && newInput != null) {
	// JavaCore.addElementChangedListener(this);
	// } else if (oldInput != null && newInput == null) {
	// JavaCore.removeElementChangedListener(this);
	// }
	// fInput = newInput;
	// }
	//
	// // ------ Code which delegates to PackageFragmentProvider ------
	//
	// private boolean needsToDelegateGetChildren(Object element) {
	// int type = -1;
	// if (element instanceof IJavaElement) {
	// type = ((IJavaElement) element).getElementType();
	// }
	// return (!fIsFlatLayout && (type == IJavaElement.PACKAGE_FRAGMENT
	// || type == IJavaElement.PACKAGE_FRAGMENT_ROOT || type ==
	// IJavaElement.JAVA_PROJECT));
	// }
	//
	// public Object[] getChildren(Object parentElement) {
	// // Filter all children for logical nodes
	// List<Object> filteredChildren = new ArrayList<Object>();
	// for (Object object : getChildrenInternal(parentElement)) {
	// Object node = LogicalExplorerNodeFactory.getInstance().getNode(
	// object);
	// if (node != null) {
	// filteredChildren.add(node);
	// }
	// }
	// return filteredChildren.toArray(new Object[filteredChildren.size()]);
	// }
	//
	// public Object[] getChildrenInternal(Object parentElement) {
	// Object[] children = NO_CHILDREN;
	// try {
	// if (parentElement instanceof ICompilationUnit
	// || parentElement instanceof IClassFile) {
	// IAbstractArtifact artifact = TSExplorerUtils
	// .getArtifactFor(parentElement);
	// if (artifact != null) {
	// return artifact.getChildren();
	// }
	// }
	//
	// if (parentElement instanceof IJavaModel)
	// return getTigerstripeProjects((IJavaModel) parentElement);
	//
	// if (parentElement instanceof ClassPathContainer)
	// return NO_CHILDREN; // don't show the classpath jars.
	// // return getContainerPackageFragmentRoots((ClassPathContainer)
	// // parentElement);
	//
	// if (parentElement instanceof IProject)
	// return ((IProject) parentElement).members();
	//
	// if (needsToDelegateGetChildren(parentElement)) {
	// Object[] packageFragments = fPackageFragmentProvider
	// .getChildren(parentElement);
	// children = getWithParentsResources(packageFragments,
	// parentElement);
	// // To enable proper rendering of Module components are
	// // artifacts,
	// // we need to post-process the result here
	// if (parentElement instanceof JarPackageFragmentRoot) {
	// children = postProcessPackageFragmentRoot(
	// (JarPackageFragmentRoot) parentElement, children);
	// }
	// } else {
	// children = super.getChildren(parentElement);
	// }
	//
	// if (parentElement instanceof IJavaProject) {
	// IJavaProject project = (IJavaProject) parentElement;
	// return rootsAndContainers(project, children);
	// } else {
	// return children;
	// }
	//
	// } catch (CoreException e) {
	// return NO_CHILDREN;
	// }
	// }
	//
	// /**
	// *
	// * @author Eric Dillon
	// */
	// private Object[] postProcessPackageFragmentRoot(
	// IPackageFragmentRoot packageRoot, Object[] children) {
	// ArrayList<Object> result = new ArrayList<Object>();
	//
	// for (Object obj : children) {
	// if (obj instanceof JarEntryFile) {
	// JarEntryFile file = (JarEntryFile) obj;
	// if (file.getName().endsWith("ts-module.xml")) {
	// continue; // ts-module shouldn't be displayed
	// }
	// } else if (obj instanceof IPackageFragment) {
	// IPackageFragment fragment = (IPackageFragment) obj;
	// if (fragment.getElementName().startsWith("META-INF")) {
	// continue; // META-INF shouldn't be displayed
	// }
	// }
	// result.add(obj);
	// }
	//
	// return result.toArray();
	// }
	//
	// /**
	// * Note: This method is for internal use only. Clients should not call
	// this
	// * method.
	// */
	// protected Object[] getTigerstripeProjects(IJavaModel jm)
	// throws JavaModelException {
	// List result = new ArrayList();
	// IProject[] projects = EclipsePlugin.getWorkspace().getRoot()
	// .getProjects();
	// for (int i = 0; i < projects.length; i++) {
	// try {
	//
	// // Since 1.2 the nature has changed name @see #295
	// handleProjectNatureMigration(projects[i]);
	//
	// if (TigerstripePluginProjectNature.hasNature(projects[i])) {
	// result.add(jm.getJavaProject(projects[i].getName()));
	//
	// if (TSExplorerUtils.getProjectHandleFor(projects[i]) != null) {
	//
	// }
	// } else if (TigerstripeProjectNature.hasNature(projects[i])) {
	// result.add(jm.getJavaProject(projects[i].getName()));
	//
	// // At this point we build up the Artifact Manager
	// // from the snapshot we have
	// if (TSExplorerUtils.getProjectHandleFor(projects[i]) != null) {
	//
	// // @since 0.4
	// // For compatibility reasons, we check that a
	// // TigerstripeProjectAuditor
	// // is associated with the project. If not, just add it
	// // silently.
	// final IProject project = projects[i];
	// if (!TigerstripeProjectAuditor
	// .hasTigerstripeProjectAuditor(projects[i])) {
	// TigerstripeProjectAuditor
	// .addBuilderToProject(project);
	// new Job("Tigerstripe Project Audit") {
	// protected IStatus run(IProgressMonitor monitor) {
	// try {
	// project
	// .build(
	// TigerstripeProjectAuditor.FULL_BUILD,
	// TigerstripeProjectAuditor.BUILDER_ID,
	// null, monitor);
	// } catch (CoreException e) {
	// EclipsePlugin.log(e);
	// }
	// return org.eclipse.core.runtime.Status.OK_STATUS;
	// }
	// }.schedule();
	// }
	// }
	// }
	// } catch (CoreException e) {
	// EclipsePlugin.log(e);
	// }
	// }
	// return result.toArray();
	// }
	//
	// private Object[] rootsAndContainers(IJavaProject project, Object[] roots)
	// throws JavaModelException {
	// List result = new ArrayList(roots.length);
	// Set containers = new HashSet(roots.length);
	// Set containedRoots = new HashSet(roots.length);
	//
	// IClasspathEntry[] entries = project.getRawClasspath();
	// for (int i = 0; i < entries.length; i++) {
	// IClasspathEntry entry = entries[i];
	// if (entry != null
	// && entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
	// IPackageFragmentRoot[] roots1 = project
	// .findPackageFragmentRoots(entry);
	// containedRoots.addAll(Arrays.asList(roots1));
	// containers.add(entry);
	// }
	// }
	// for (int i = 0; i < roots.length; i++) {
	// if (roots[i] instanceof IPackageFragmentRoot) {
	// if (!containedRoots.contains(roots[i])) {
	// result.add(roots[i]);
	// }
	// } else {
	// result.add(roots[i]);
	// }
	// }
	// // for (Iterator each = containers.iterator(); each.hasNext();) {
	// // IClasspathEntry element = (IClasspathEntry) each.next();
	// // result.add(new ClassPathContainer(project, element));
	// // }
	// return result.toArray();
	// }
	//
	// private Object[] getContainerPackageFragmentRoots(
	// ClassPathContainer container) {
	// return container.getChildren(container);
	// }
	//
	// public Object getParent(Object child) {
	// if (needsToDelegateGetParent(child)) {
	// return fPackageFragmentProvider.getParent(child);
	// } else
	// return super.getParent(child);
	// }
	//
	// protected Object internalGetParent(Object element) {
	// // since we insert logical package containers we have to fix
	// // up the parent for package fragment roots so that they refer
	// // to the container and containers refere to the project
	// //
	// if (element instanceof IPackageFragmentRoot) {
	// IPackageFragmentRoot root = (IPackageFragmentRoot) element;
	// IJavaProject project = root.getJavaProject();
	// try {
	// IClasspathEntry[] entries = project.getRawClasspath();
	// for (int i = 0; i < entries.length; i++) {
	// IClasspathEntry entry = entries[i];
	// if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
	// if (ClassPathContainer.contains(project, entry, root))
	// return new ClassPathContainer(project, entry);
	// }
	// }
	// } catch (JavaModelException e) {
	// // fall through
	// }
	// }
	// if (element instanceof ClassPathContainer) {
	// return ((ClassPathContainer) element).getJavaProject();
	// }
	// return super.internalGetParent(element);
	// }
	//
	// private boolean needsToDelegateGetParent(Object element) {
	// int type = -1;
	// if (element instanceof IJavaElement)
	// type = ((IJavaElement) element).getElementType();
	// return (!fIsFlatLayout && type == IJavaElement.PACKAGE_FRAGMENT);
	// }
	//
	// /**
	// * Returns the given objects with the resources of the parent.
	// */
	// private Object[] getWithParentsResources(Object[] existingObject,
	// Object parent) {
	// Object[] objects = super.getChildren(parent);
	// List list = new ArrayList();
	// // Add everything that is not a PackageFragment
	// for (int i = 0; i < objects.length; i++) {
	// Object object = objects[i];
	// if (!(object instanceof IPackageFragment)) {
	// list.add(object);
	// }
	// }
	// if (existingObject != null)
	// list.addAll(Arrays.asList(existingObject));
	// return list.toArray();
	// }
	//
}
