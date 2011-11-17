package org.eclipse.tigerstripe.workbench.ui.internal.handlers;

import static org.eclipse.tigerstripe.workbench.utils.AdaptHelper.adapt;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.IElementWrapper;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.model.ContextProjectAwareProxy;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.RelationshipAnchor;
import org.eclipse.tigerstripe.workbench.ui.internal.views.hierarchy.HierarchyView;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * The main handler for opening the {@link HierarchyView}. Tries to find current selection 
 * and obtain from selection a artifact. When opens the obtained artifact in the {@link HierarchyView}.
 * If artifact has been obtained from the selection then the {@link HierarchyView} gets the focus, 
 * else nothing occurs.   
 */
public class ShowHierarchyHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null) {
			final IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
			if (activePage != null) {
				try {
					ISelection selection = HandlerUtil.getCurrentSelection(event); 
					if (selection instanceof IStructuredSelection) {
						Object element = ((IStructuredSelection) selection).getFirstElement();
						ITigerstripeModelProject context = null;
						if (element instanceof IElementWrapper) {
							IElementWrapper wrapper = (IElementWrapper) element;
							context = wrapper.getContextProject();
							element = wrapper.getElement();
						}
						
						IAbstractArtifact input = findArtifact(element);
						
						if (input == null) {
							if (element instanceof IAdaptable) {
								IAdaptable adaptable = (IAdaptable) element;
								input = adapt(adaptable, IAbstractArtifact.class);
								if (input == null) {
									IModelComponent mc = adapt(adaptable, IModelComponent.class);
									if (mc != null) {
										IModelComponent containing = mc.getContainingModelComponent();
										if (containing instanceof IAbstractArtifact) {
											input = (IAbstractArtifact) containing;
										}
									}
								}
							}
						}
						
						if (input != null && !(input instanceof IPackageArtifact)) {
							if (context != null) {
								input = ContextProjectAwareProxy.newInstance(input, context);
							}
							HierarchyView view = (HierarchyView) activePage.showView(HierarchyView.ID);
							view.setInput(input);
						}
					}
				} catch (PartInitException e) {
					BasePlugin.log(e);
				}
			}
		}
		return null;
	}
	
	private IAbstractArtifact findArtifact(Object element) {
		if (element instanceof IAbstractArtifact) {
			return (IAbstractArtifact) element;
		} else if (element instanceof IModelComponent) {
			IModelComponent containing = ((IModelComponent) element).getContainingModelComponent();
			if (containing instanceof IAbstractArtifact) {
				return (IAbstractArtifact) containing; 
			}
		} else if (element instanceof IRelationshipEnd) {
			return ((IRelationshipEnd) element).getType().getArtifact();
		} else if (element instanceof RelationshipAnchor) {
			return ((RelationshipAnchor) element).getEnd().getType().getArtifact();
		} 
		return null;
	}
}

