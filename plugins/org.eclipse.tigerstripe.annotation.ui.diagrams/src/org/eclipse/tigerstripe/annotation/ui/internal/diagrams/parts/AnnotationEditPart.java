/**
 * 
 */
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams.parts;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.OpenDiagramEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ViewComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.DiagramLinkDragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.NonSemanticEditPolicy;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationAdapter;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationListener;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.AnnotationNode;
import org.eclipse.tigerstripe.annotation.ui.util.DisplayAnnotationUtil;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationEditPart extends ShapeNodeEditPart {
    
    // resource listener
    private ResourceListener listener = null;
    
    private boolean diagramLinkMode = false;
    
    private IAnnotationListener annotationListener; 
    private ConnectionCollector collector;

	/**
	 * constructor
	 * @param view the view controlled by this edit part
	 */
	public AnnotationEditPart(View view) {
		super(view);
		collector = new ConnectionCollector(view);
	}
	
	private void addChangeListener() {
		if (annotationListener == null) {
			annotationListener = new AnnotationAdapter() {
				
				@Override
				public void annotationsChanged(Annotation[] annotations) {
					if (getNotationView().isVisible() && 
							isAnnotationChanged(annotations)) {
						updatePart();
					}
				}
			
			};
			AnnotationPlugin.getManager().addAnnotationListener(annotationListener);
		}
	}
	
	protected boolean isAnnotationChanged(Annotation[] changedAnnotations) {
		Annotation ann = getAnnotation();
		if (ann != null) {
			for (Annotation annotation : changedAnnotations) {
				if (ann.equals(annotation))
					return true;
			}
		}
		return false;
	}
	
	private void updatePart() {
		try {
			getEditingDomain().runExclusive(new Runnable() {
				public void run() {
					refreshVisuals();
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void removeChangeListener() {
		if (annotationListener != null) {
			AnnotationPlugin.getManager().removeAnnotationListener(annotationListener);
			annotationListener = null;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#activate()
	 */
	@Override
	public void activate() {
		addChangeListener();
		super.activate();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#deactivate()
	 */
	@Override
	public void deactivate() {
		super.deactivate();
		removeChangeListener();
	}

	/**
	 * Creates a note figure.
	 */
	protected NodeFigure createNodeFigure() {
		IMapMode mm = getMapMode();
		Insets insets = new Insets(mm.DPtoLP(5), mm.DPtoLP(5), mm.DPtoLP(5), mm.DPtoLP(14));
		AnnotationFigure noteFigure = new AnnotationFigure(mm.DPtoLP(100), mm.DPtoLP(56), insets);
		Label label = new Label();
		noteFigure.add(label);
		return noteFigure;
	}
	
	protected AnnotationNode getAnnotationModel() {
		return (AnnotationNode)getModel();
	}
	
	public Annotation getAnnotation() {
		return getAnnotationModel().getAnnotation();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		if (getFigure() != null) {
			List<?> children = getFigure().getChildren();
			if (children != null && children.size() > 0) {
				Label label = (Label)children.get(children.size() - 1);
				Annotation annotation = getAnnotation();
				if (annotation != null)
					label.setText(DisplayAnnotationUtil.getText(annotation));
				else
					label.setText("");
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#refreshSourceConnections()
	 */
	@Override
	protected void refreshSourceConnections() {
		collector.start();
		super.refreshSourceConnections();
		collector.refresh();
		collector.finish();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#refreshTargetConnections()
	 */
	@Override
	protected void refreshTargetConnections() {
		collector.start();
		super.refreshTargetConnections();
		collector.refresh();
		collector.finish();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createChild(java.lang.Object)
	 */
	@Override
	protected EditPart createChild(Object model) {
		EditPart part = super.createChild(model);
		if (part instanceof AnnotationConnectionEditPart) {
			collector.addConnection((AnnotationConnectionEditPart)part);
		}
		return part;
	}

	/** Adds support for diagram links. */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();

		// Remove semantic edit policy and install a non-semantic edit policy
		removeEditPolicy(EditPolicyRoles.SEMANTIC_ROLE);
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
			new NonSemanticEditPolicy());

		// Add Note support for diagram links
		// The following two edit policies support the links.		
		installEditPolicy(
			EditPolicyRoles.DRAG_DROP_ROLE,
			new DiagramLinkDragDropEditPolicy());
		
		installEditPolicy(
			EditPolicyRoles.OPEN_ROLE,
			new OpenDiagramEditPolicy());

		// This View doesn't have semantic elements so use a component edit
		// policy that only gets a command to delete the view
		installEditPolicy(
			EditPolicy.COMPONENT_ROLE,
			new ViewComponentEditPolicy());
	}
	
	/**
	 * this method will return the primary child EditPart  inside this edit part
	 * @return the primary child view inside this edit part
	 */
	public EditPart getPrimaryChildEditPart(){
		return null;//getChildBySemanticHint(CommonParserHint.DESCRIPTION);
	}
    
    public Object getPreferredValue(EStructuralFeature feature) {
        Object preferenceStore = getDiagramPreferencesHint()
            .getPreferenceStore();
        if (preferenceStore instanceof IPreferenceStore) {
            if (feature == NotationPackage.eINSTANCE.getLineStyle_LineColor()) {
                
                return FigureUtilities.RGBToInteger(new RGB(0, 0, 0));
                
            } else if (feature == NotationPackage.eINSTANCE
                .getFillStyle_FillColor()) {
                
                return FigureUtilities.RGBToInteger(new RGB(255, 0, 0));
                
            }
        }

        return super.getPreferredValue(feature);
    }
    
    private class ResourceListener extends ResourceSetListenerImpl{
        private WeakReference resourceRef = null; 
        private AnnotationEditPart editPart= null;
        public ResourceListener(Resource resource, AnnotationEditPart editPart){
            resourceRef = new WeakReference(resource);
            this.editPart = editPart;
        }
        
        /**
         * Disposes my context from the operation history when a resource is
         * unloaded from my editing domain.
         */
        public void resourceSetChanged(ResourceSetChangeEvent event) {
            if (editPart ==null || resourceRef.get()==null) {
                if (editPart != null && editPart.listener!=null) {
                    getEditingDomain().removeResourceSetListener(editPart.listener);
                    editPart.listener = null;
                }
                return;
            }
            boolean unloaded = isResourceUnloaded(event.getNotifications());
            if (unloaded && editPart.isActive()) {
                editPart.refresh();
                if (editPart.listener!=null) {
                    getEditingDomain().removeResourceSetListener(editPart.listener);
                    editPart.listener = null;
                }
            }
        }
        
        /**
         * Finds resources that have sent unload notifications.
         * 
         * @param notifications
         *            notifications received from a transaction
         * @return a set of resources that the notifications indicate have been
         *         unloaded, or <code>null</code> if none
         */
        private boolean isResourceUnloaded(Collection notifications) {
            for (Iterator iter = notifications.iterator(); iter.hasNext();) {
                Notification next = (Notification) iter.next();
                if (next.getNotifier()!=resourceRef.get())
                    return false;
                if (NotificationFilter.RESOURCE_UNLOADED.matches(next)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isPostcommitOnly() {
            // only interested in post-commit "resourceSetChanged" event
            return true;
        }

    }
    
    protected void addSemanticListeners() {
        // the resource listener is needed only in diagram link mode
        if (diagramLinkMode){
            if (listener==null){
                listener = new ResourceListener(getNotationView().getElement().eResource(),this);
            }
            getEditingDomain().addResourceSetListener(listener);
        }
        super.addSemanticListeners();
    }

    protected void removeSemanticListeners() {
        //the resource listener is needed only in diagram link mode
        if (listener!=null) {
            getEditingDomain().removeResourceSetListener(listener);
            listener = null;
        }
        super.removeSemanticListeners();
    }

    protected void handleNotificationEvent(Notification notification) {
        Object feature = notification.getFeature();
        if (feature == NotationPackage.eINSTANCE.getView_Element() && notification.getEventType() == Notification.RESOLVE
                 && ((EObject)notification.getNotifier())== getNotationView() && listener != null) {
            // skipping the resolve event whenever the editpart is already resolved.
            return;
        }
        
        super.handleNotificationEvent(notification);
    }
}
